package mobile.teamwave.pm_activity;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.AllActivitiesGet;
import mobile.teamwave.Http.AllProjectsGet;
import mobile.teamwave.Http.SigninPost;
import mobile.teamwave.application.ConvertDatetoTimeZone;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PM_MainActivity extends FragmentActivity {
	private ListView listviewActivities;
	JSONObject jObj, jobject, jsonProject, jsonActor;
	String timeDate, dateObj, dayStr, fullName, verb, contentType, actionType,
			globalProName, globalTimeDate, nextCall = "", checkRefresh = "";
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	int currentPosition;
	AllProressBaseAdapter adapter;
	protected DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;
	private android.app.ActionBar actionBar;
	ListView porListview;
	String token, todayDate, tomoDate;
	TextView projectsTxt, projectsTxtt, progressTxt, calenderTxt,
			noDataTxtPros, noDataProgrsTxt;
	JSONObject jsonToken;
	SharedPreferences userPref, prefPro;
	SharedPreferences.Editor editor, editor1;
	Hostname host = new Hostname();
	DisplayImageOptions options;
	ImageLoader imageLoader;
	JSONArray proJarr;
	JSONObject jsonPro;
	String proId, proName, proImg;
	ImageView progresImg, calenderImg, logoImg, homeScreenImg;
	Typeface typefaceRoman, typeFaceMeduim;
	CircleProgressBar pDialog, pDialog1, pDialogBtm;
	ConvertDatetoTimeZone timezone;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	SwipeRefreshLayout mSwipeRefreshLayout;
	Runnable run;
	LinearLayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getOverflowMenu();
		moveDrawerToTop();
		initActionBar();
		initDrawer();

		mDrawerLayout.openDrawer(Gravity.START);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();
		timezone = new ConvertDatetoTimeZone(PM_MainActivity.this);

		mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,
				R.color.green, R.color.blue);
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								globalTimeDate = "";
								globalProName = "";
								nextCall = "";
								list = new ArrayList<HashMap<String, String>>();
								new AllProgressActivities().execute();
								// setupAdapter();
								// adapter.notifyDataSetChanged();
								runOnUiThread(run);
								mSwipeRefreshLayout.setRefreshing(false);
							}
						}, 2500);
					}
				});
		run = new Runnable() {
			public void run() {
				// reload content
				// list.clear();
				if (list.toString() == "[]" || list.size() == 0) {
				} else {
					adapter.notifyDataSetChanged();
					listviewActivities.invalidateViews();
				}
				// mRecyclerView.refreshDrawableState();
			}
		};
	}

	public class AllProgressActivities extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			if (nextCall == "" || nextCall.equals("")) {
				pDialog.setVisibility(View.VISIBLE);
			} else {
				pDialogBtm.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				System.out.println(nextCall);
				jObj = AllActivitiesGet.callAllActivitiesList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), nextCall);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jObj + "!!!!!!!!!!!!!!!");
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			try {

				if (jObj == null || jObj.equals(null)) {
					System.out
							.println("SERVER IS NOT WORKING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					pDialog.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(),
							"Something went wrong! Check server",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONArray jArray = jObj.getJSONArray("results");
					if (jArray.toString() == "[]" || jArray.length() == 0) {
						pDialog.setVisibility(View.GONE);
						noDataProgrsTxt.setVisibility(View.VISIBLE);
					} else if (jArray != null) {

						for (int i = 0; i < jArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jobject = jArray.getJSONObject(i);
							timeDate = jobject.getString("created_on");

							actionType = jobject.getString("action_type");

							if (actionType == "deleted"
									|| actionType.equals("deleted")) {
								map.put("contentType", "");
							} else {
								contentType = jobject.getString("content_type");
								JSONObject targetObj = jobject
										.getJSONObject("target_obj");

								String createdOnDate = jobject
										.getString("created_on");

								// convert date to current UTC date[converting
								// date
								// to current time zone time]
								createdOnDate = timezone
										.convertDateToTimeZone(createdOnDate
												.substring(0, 18));

								//
								System.out
										.println(contentType
												+ "____________________________________________");
								map.put("createdOnDate", createdOnDate);
								if (contentType == "event"
										|| contentType.equals("event")
										|| contentType == "milestone"
										|| contentType.equals("milestone")) {
									String targetId = targetObj.getString("id");
									String targettitle = targetObj
											.getString("display_name");
									map.put("ID", targetId);
									map.put("display_name", targettitle);
									map.put("detailurl",
											targetObj.getString("url"));
								} else if (contentType == "task"
										|| contentType.equals("task")) {
									String taskGrpId = targetObj
											.getString("group_id");
									String taskId = targetObj.getString("id");
									String taskName = targetObj
											.getString("display_name");
									map.put("taskGrpId", taskGrpId);
									map.put("taskId", taskId);
									map.put("display_name", taskName);

									System.out.println(map);
								}

								else if (contentType == "taskgroup"
										|| contentType.equals("taskgroup")) {

									map.put("ID", targetObj.getString("id"));
									String taskGrpName = targetObj
											.getString("display_name");

									map.put("display_name", taskGrpName);

									System.out.println(map);
								} else if (contentType == "file"
										|| contentType.equals("file")) {

									map.put("detailurl",
											targetObj.getString("url"));

									map.put("ID", targetObj.getString("id"));
									map.put("display_name",
											targetObj.getString("display_name"));

									System.out.println(map);
								}

								else if (contentType == "comment"
										|| contentType.equals("comment")
										|| contentType == "note"
										|| contentType.equals("note")
										|| contentType == "message"
										|| contentType.equals("message")) {
									String url = targetObj.getString("url");
									map.put("detailurl", url);
									System.out.println();
									map.put("ID", targetObj.getString("id"));
									map.put("display_name",
											targetObj.getString("display_name"));
									int k = url.indexOf(
											'/',
											1 + url.indexOf('/',
													1 + url.indexOf('/')));

									String firstPart = url.substring(0, k);
									String secondPart = url.substring(k + 1);
									System.out.println(firstPart);

									int iend = secondPart.indexOf("/"); // this
																		// finds
																		// the
																		// first
																		// occurrence
																		// of
																		// "."
									// in string thus giving you the index of
									// where
									// it is in the string

									// Now iend can be -1, if lets say the
									// string
									// had no "." at all in it i.e. no "." is
									// not
									// found.
									// So check and account for it.

									if (iend != -1) {
										contentType = secondPart.substring(0,
												iend);
										System.out.println(contentType);
									}

									char toCheck = '/';
									int count = 0;

									for (char ch : url.toCharArray()) {
										if (ch == toCheck) {
											count++;
										}
									}
									System.out.println(count);
									if (count == 6) {
										if (contentType == "taskgroups"
												|| contentType
														.equals("taskgroups")) {

											int m = secondPart
													.indexOf(
															'/',
															0 + secondPart
																	.indexOf(
																			'/',
																			1 + secondPart
																					.indexOf('/')));

											String firtPart = secondPart
													.substring(0, m);
											String secdPart = secondPart
													.substring(m + 1);

											int mend = secdPart.indexOf("/");
											if (mend != -1) {
												contentType = secdPart
														.substring(0, mend);
												System.out.println(contentType);
											}
											String taskGrpId = targetObj
													.getString("group_id");
											String taskId = targetObj
													.getString("id");
											String taskName = targetObj
													.getString("display_name");
											map.put("taskGrpId", taskGrpId);
											map.put("taskId", taskId);
											map.put("display_name", taskName);

											System.out.println(firtPart);
											System.out.println(secdPart);
										}
									}

									System.out.println(secondPart);

								}
								map.put("contentType", contentType);
							}

							if (jobject.has("project_obj")) {
								jsonProject = jobject
										.getJSONObject("project_obj");
								System.out.println(jsonProject);

							}
							if (jobject.has("actor")) {
								jsonActor = jobject.getJSONObject("actor");
								System.out.println(jsonActor);
								fullName = jsonActor.getString("full_name");
								map.put("displayPic",
										jsonActor.getString("image"));
							}
							verb = jobject.getString("verb");

							// activity detail json

							String proName = jsonProject
									.getString("display_name");

							// String timeDate =
							// listt.get(position).get("created").toString();
							timeDate = timeDate.substring(0, 10);

							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date;
							try {
								date = df.parse(timeDate);
								System.out.println(date);
								timeDate = date.toString();
								System.out.println(timeDate.length());
								dayStr = timeDate.substring(0, 3);
								timeDate = timeDate.substring(4, 10);
								System.out.println(timeDate);
								timeDate.replaceAll("\\s+", " ");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							System.out.println(globalTimeDate + timeDate);
							if (globalTimeDate == null
									|| !globalTimeDate.equals(timeDate)) {
								globalTimeDate = timeDate;
								globalProName = null;
								map.put("created", globalTimeDate);
							} else {
								map.put("created", "");
							}
							if (globalProName == null
									|| !globalProName.equals(proName)) {
								globalProName = proName;
								map.put("proName", globalProName);
							} else {
								map.put("proName", "");
							}
							map.put("proId", jobject.getString("project_id"));
							map.put("PRONAME", proName);
							map.put("verbTime", timeDate);
							map.put("day", dayStr);
							map.put("fullName", fullName);
							map.put("verb", verb);

							list.add(map);
							System.out.println(map);
						}
						currentPosition = listviewActivities
								.getLastVisiblePosition();
						adapter = new AllProressBaseAdapter(
								PM_MainActivity.this, list);
						System.out.println(list);
						listviewActivities.setAdapter(adapter);

						pDialog.setVisibility(View.GONE);
						pDialogBtm.setVisibility(View.GONE);
						if (checkRefresh != "" || !checkRefresh.equals("")) {
							DisplayMetrics displayMetrics = getResources()
									.getDisplayMetrics();
							int height = displayMetrics.heightPixels;
							System.out
									.println(height
											+ "-------------ACTION BAR HEIGHT--------------");
							listviewActivities.setSelectionFromTop(
									currentPosition + 1, height - 220);
						}

					}
				}

				listviewActivities.setOnScrollListener(new OnScrollListener() {

					private int currentScrollState;
					private int currentFirstVisibleItem;
					private int currentVisibleItemCount;
					private int totalItemCount;
					private int mLastFirstVisibleItem;
					private boolean mIsScrollingUp;

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub
						this.currentScrollState = scrollState;
						if (view.getId() == listviewActivities.getId()) {
							final int currentFirstVisibleItem = listviewActivities
									.getFirstVisiblePosition();

							if (currentFirstVisibleItem > mLastFirstVisibleItem) {
								mIsScrollingUp = false;

								/*
								 * Toast.makeText(getActivity(), "SCROLL DOWN",
								 * 1000).show();
								 */
								// actionBar.hide();
							} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
								/*
								 * Toast.makeText(getActivity(), "SCROLL UP",
								 * 1000).show();
								 */
								// actionBar.show();
								mIsScrollingUp = true;
							}

							mLastFirstVisibleItem = currentFirstVisibleItem;
						}
						this.isScrollCompleted();
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO Auto-generated method stub

						this.currentFirstVisibleItem = firstVisibleItem;
						this.currentVisibleItemCount = visibleItemCount;
						this.totalItemCount = totalItemCount;

					}

					private void isScrollCompleted() {
						if (this.currentVisibleItemCount > 0
								&& this.currentScrollState == SCROLL_STATE_IDLE
								&& this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
							/***
							 * In this way I detect if there's been a scroll
							 * which has completed
							 ***/
							/*** do the work for load more date! ***/
							try {
								nextCall = jObj.getString("next");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (nextCall == null || nextCall == ""
									|| nextCall.equals("null")
									|| nextCall.equals("")) {
								Toast.makeText(getApplicationContext(),
										"There is no more activities!",
										Toast.LENGTH_LONG).show();
							} else {
								System.out.println("API IS CALLING AGAIN"
										+ "_____  " + nextCall);
								// hostname = nextCall;
								// nextCall = "NEXT";
								checkRefresh = "NEXT";
								new AllProgressActivities().execute();

							}
						}
					}
				});

				System.out.println(jObj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressLint("NewApi")
	public class AllProressBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;
		PrettyTime prettyTime = new PrettyTime();;

		public AllProressBaseAdapter(PM_MainActivity mainActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = mainActivity;
			this.listt = listArticles;
			inflator = (LayoutInflater) mainActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listt.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO mylist_add_ind-generated method stub
			return listt.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return listt.get(position).hashCode();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflator.inflate(
						R.layout.all_progress_activity_row, null);
				holder = new ViewHolder();
				holder.dayTxt = (TextView) convertView.findViewById(R.id.day);
				holder.dateTxt = (TextView) convertView.findViewById(R.id.date);
				holder.proNameTxt = (TextView) convertView
						.findViewById(R.id.proName);
				holder.createdNameTxt = (TextView) convertView
						.findViewById(R.id.createdName);
				holder.dateContentTypeTxt = (TextView) convertView
						.findViewById(R.id.dateContentType);
				holder.layClick = (RelativeLayout) convertView
						.findViewById(R.id.layClick);
				/*
				 * holder.verbNameTxt=(TextView) convertView
				 * .findViewById(R.id.verbName);
				 */
				holder.displayPicImg = (ImageView) convertView
						.findViewById(R.id.displayPic);
				holder.viewLineHor = (View) convertView
						.findViewById(R.id.viewLine);

				holder.proNameTxt.setTypeface(typeFaceMeduim);
				holder.dayTxt.setTypeface(typeFaceMeduim);

				// holder.dateTxt = (TextView)
				// convertView.findViewById(R.id.Date);
				/*
				 * holder.dateLay = (RelativeLayout) convertView
				 * .findViewById(R.id.datelay);
				 */

				holder.createdNameTxt.setTypeface(typefaceRoman);
				holder.dateContentTypeTxt.setTypeface(typefaceRoman);
				holder.dateTxt.setTypeface(typefaceRoman);
				// holder.dayTxt.setTypeface(typefaceRoman);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.layClick.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println(listt.get(position).get("contentType"));

					editor1.putString("PRONAME",
							listt.get(position).get("PRONAME"));
					editor1.commit();

					if (listt.get(position).get("contentType") == "message"
							|| listt.get(position).get("contentType")
									.equals("message")
							|| listt.get(position).get("contentType") == "messages"
							|| listt.get(position).get("contentType")
									.equals("messages")) {
						Intent i = new Intent(PM_MainActivity.this,
								MessageDetail.class);
						i.putExtra("ID", listt.get(position).get("ID"));
						i.putExtra("TITLE",
								listt.get(position).get("display_name"));
						i.putExtra("PROID", listt.get(position).get("proId"));
						i.putExtra("msgUrl",
								listt.get(position).get("detailurl"));
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "events"
							|| listt.get(position).get("contentType")
									.equals("events")) {
						Intent i = new Intent(PM_MainActivity.this,
								EventDetail.class);
						i.putExtra("ID", listt.get(position).get("ID"));
						i.putExtra("TITLE",
								listt.get(position).get("display_name"));
						i.putExtra("PROID", listt.get(position).get("proId"));
						i.putExtra("eventUrl",
								listt.get(position).get("detailurl"));
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "event"
							|| listt.get(position).get("contentType")
									.equals("event")
							|| listt.get(position).get("contentType") == "milestone"
							|| listt.get(position).get("contentType")
									.equals("milestone")) {
						Intent i = new Intent(PM_MainActivity.this,
								EventDetail.class);
						i.putExtra("ID", listt.get(position).get("ID"));
						i.putExtra("TITLE",
								listt.get(position).get("display_name"));
						i.putExtra("PROID", listt.get(position).get("proId"));
						i.putExtra("eventUrl",
								listt.get(position).get("detailurl"));
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "task"
							|| listt.get(position).get("contentType")
									.equals("task")
							|| listt.get(position).get("contentType") == "tasks"
							|| listt.get(position).get("contentType")
									.equals("tasks")) {
						Intent i = new Intent(PM_MainActivity.this,
								TaskDetail.class);
						i.putExtra("taskName",
								listt.get(position).get("display_name"));
						i.putExtra("proId", listt.get(position).get("proId"));
						i.putExtra("taskId", listt.get(position).get("taskId"));
						i.putExtra("createdBy",
								listt.get(position).get("fullName"));
						i.putExtra("taskGrpId",
								listt.get(position).get("taskGrpId"));
						System.out.println(listt.get(position).get(
								"display_name")
								+ listt.get(position).get("proId")
								+ listt.get(position).get("taskId")
								+ listt.get(position).get("taskGrpId"));
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "taskgroups"
							|| listt.get(position).get("contentType")
									.equals("taskgroups")
							|| listt.get(position).get("contentType") == "taskgroup"
							|| listt.get(position).get("contentType")
									.equals("taskgroup")) {
						Intent i = new Intent(PM_MainActivity.this,
								TasksList.class);
						i.putExtra("taskGrpId", listt.get(position).get("ID"));

						i.putExtra("proId", listt.get(position).get("proId"));
						i.putExtra("taskgrpName",
								listt.get(position).get("display_name"));

						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					}
					// comment
					else if (listt.get(position).get("contentType") == "notes"
							|| listt.get(position).get("contentType")
									.equals("notes")) {
						Intent i = new Intent(PM_MainActivity.this,
								NotesDetail.class);
						i.putExtra("proId", listt.get(position).get("proId"));
						i.putExtra("notesId", listt.get(position).get("ID"));
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "file"
							|| listt.get(position).get("contentType")
									.equals("file")
							|| listt.get(position).get("contentType") == "files"
							|| listt.get(position).get("contentType")
									.equals("files")) {

						System.out
								.println(listt.get(position).get("detailurl"));
						if (listt.get(position).get("detailurl")
								.contains("?ids")) {
							System.out
									.println("MULTIPLE FILES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							Intent i = new Intent(PM_MainActivity.this,
									MultipleFiles.class);

							System.out.println(listt.get(position).get(
									"detailurl")
									+ listt.get(position).get("proId")
									+ listt.get(position).get("ID"));

							i.putExtra("proId", listt.get(position)
									.get("proId"));

							i.putExtra("fileId", listt.get(position).get("ID"));
							startActivity(i);
						} else {
							Intent i = new Intent(PM_MainActivity.this,
									FileDetail.class);
							System.out.println(listt.get(position).get(
									"detailurl")
									+ listt.get(position).get("proId")
									+ listt.get(position).get("ID"));
							i.putExtra("fileUrl",
									listt.get(position).get("detailurl"));
							i.putExtra("proId", listt.get(position)
									.get("proId"));
							i.putExtra("title",
									listt.get(position).get("display_name"));

							i.putExtra("fileId", listt.get(position).get("ID"));
							startActivity(i);
						}

						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					}
				}
			});
			holder.proNameTxt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getApplicationContext(),
							ProjectActivityTaskGroupTabStrip.class);
					i.putExtra("proId", listt.get(position).get("proId"));
					i.putExtra("proName", listt.get(position).get("proName"));
					editor1.putString("PRONAME",
							listt.get(position).get("proName"));
					editor1.commit();
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
				}
			});

			String day = listt.get(position).get("day");

			holder.createdNameTxt.setText(Html.fromHtml(listt.get(position)
					.get("fullName")
					+ " "
					+ listt.get(position).get("verb")
					+ " "
					+ "<font color=#1E50A3>"
					+ "<small>"
					+ listt.get(position).get("display_name")
					+ "</small>"
					+ "</font>"));

			holder.dayTxt.setTag(position);
			holder.proNameTxt.setTag(position);

			String time = listt.get(position).get("createdOnDate");

			if (time == null || time == "null" || time.equals("null")) {
				holder.dateContentTypeTxt.setText("");

			} else {
				time = time.replace("T", " ");

				time = time.substring(0, 19);
				long timee = timeStringtoMilis(time);

				holder.dateContentTypeTxt.setText(prettyTime.format(new Date(
						timee)));
			}

			imageLoader.displayImage(listt.get(position).get("displayPic"),
					holder.displayPicImg, options);

			if (listt.get(position).get("created") == ""
					|| listt.get(position).get("created").equals("")) {
				holder.dateTxt.setVisibility(View.GONE);
				holder.dayTxt.setVisibility(View.GONE);
			} else {
				holder.dateTxt.setVisibility(View.VISIBLE);
				holder.dayTxt.setVisibility(View.VISIBLE);
				// holder.proNameTxt.setText(listt.get(position).get("proNameeWithDate"));
			}

			if (listt.get(position).get("proName") == ""
					|| listt.get(position).get("proName").equals("")) {
				holder.proNameTxt.setVisibility(View.GONE);
				holder.viewLineHor.setVisibility(View.GONE);
			} else {
				holder.proNameTxt.setVisibility(View.VISIBLE);
				holder.viewLineHor.setVisibility(View.VISIBLE);
			}
			holder.proNameTxt.setText(listt.get(position).get("proName")
					.toUpperCase());
			/*
			 * holder.verbNameTxt.setText(listt.get(position).get( "verb"));
			 */
			/*
			 * holder.dateContentTypeTxt.setText(listt.get(position).get(
			 * "verbTime") + " : " + listt.get(position).get("contentType"));
			 */
			holder.dayTxt.setText(day);
			holder.dateTxt.setText(listt.get(position).get("created")
					.toUpperCase());

			return convertView;
		}

		public class ViewHolder {
			public TextView dayTxt, dateTxt, proNameTxt, createdNameTxt,
					dateContentTypeTxt, verbNameTxt;
			View viewLineHor;
			public ImageView displayPicImg;
			// public RelativeLayout dateLay;
			public RelativeLayout layClick;
		}

	}

	@SuppressLint({ "NewApi", "InflateParams" })
	private void moveDrawerToTop() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.pm_menu,
				null); // "null" is important.
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		LinearLayout container = (LinearLayout) drawer
				.findViewById(R.id.drawer_content); // This is the container we
													// defined just now.
		container.addView(child, 0);
		drawer.findViewById(R.id.topBar).setPadding(0, getStatusBarHeight(), 0,
				0);
		// Make the drawer replace the first child
		decor.addView(drawer);

		projectsTxt = (TextView) findViewById(R.id.projectsTxt);
		projectsTxtt = (TextView) findViewById(R.id.projectsTxtt);
		progressTxt = (TextView) findViewById(R.id.progressTxt);
		noDataTxtPros = (TextView) findViewById(R.id.noDataTxt);
		noDataProgrsTxt = (TextView) findViewById(R.id.noDataaTxt);
		calenderTxt = (TextView) findViewById(R.id.calenderTxt);

		porListview = (ListView) findViewById(R.id.listview);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		pDialog1 = (CircleProgressBar) findViewById(R.id.pDialog1);
		pDialogBtm = (CircleProgressBar) findViewById(R.id.loadingBtm);
		LayoutRipple rippleProgress = (LayoutRipple) findViewById(R.id.progressLay);
		LayoutRipple rippleCalender = (LayoutRipple) findViewById(R.id.calenderLay);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
		setOriginRiple(rippleProgress);
		setOriginRiple(rippleCalender);

		progresImg = (ImageView) findViewById(R.id.progressImg);
		calenderImg = (ImageView) findViewById(R.id.calenderImg);
		logoImg = (ImageView) findViewById(R.id.logo);
		homeScreenImg = (ImageView) findViewById(R.id.homeScreenImg);

		listviewActivities = (ListView) findViewById(R.id.listviewActivities);

		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		pDialog1.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		pDialogBtm.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		// setting SVG Image for HOme Image
		progresImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svg = SVGParser.getSVGFromResource(getResources(),
				R.raw.progress);
		Drawable drawable = svg.createPictureDrawable();
		progresImg.setImageDrawable(drawable);
		// setting SVG Image for Events Image
		calenderImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svg_event = SVGParser.getSVGFromResource(getResources(),
				R.raw.event);
		Drawable drawable_event = svg_event.createPictureDrawable();
		calenderImg.setImageDrawable(drawable_event);

		// setting SVG Image for Events Image
		logoImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgLogo = SVGParser.getSVGFromResource(getResources(),
				R.raw.logo);
		Drawable drawLogo = svgLogo.createPictureDrawable();
		logoImg.setImageDrawable(drawLogo);

		homeScreenImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svghomeScreen = SVGParser.getSVGFromResource(getResources(),
				R.raw.app_home_icon);
		Drawable drawhomeScreen = svghomeScreen.createPictureDrawable();
		homeScreenImg.setImageDrawable(drawhomeScreen);

		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		editor = userPref.edit();

		prefPro = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		editor1 = prefPro.edit();

		typefaceRoman = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");
		typeFaceMeduim = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_md.ttf");

		projectsTxt.setTypeface(typefaceRoman);
		progressTxt.setTypeface(typefaceRoman);
		calenderTxt.setTypeface(typefaceRoman);
		projectsTxtt.setTypeface(typefaceRoman);
		noDataTxtPros.setTypeface(typefaceRoman);
		noDataProgrsTxt.setTypeface(typefaceRoman);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		System.out
				.println(userPref.getString("TOKEN_REFRESH_TODAY_DATE", "NV"));
		if (userPref.getString("TOKEN_REFRESH_TODAY_DATE", "NV") == "NV"
				&& userPref.getString("TOKEN_REFRESH_TOMO_DATE", "NV") == "NV") {

			todayDate = df.format(c.getTime());
			todayDate = todayDate.substring(0, 2);
			editor.putString("TOKEN_REFRESH_TODAY_DATE", todayDate);
			editor.commit();

			if (isInternetPresent) {
				new AllProjects().execute();
				new AllProgressActivities().execute();
			} else {
				showSnack(PM_MainActivity.this,
						"Please check network connection!", "OK");
			}

		} else {

			tomoDate = df.format(c.getTime());
			tomoDate = tomoDate.substring(0, 2);
			editor.putString("TOKEN_REFRESH_TOMO_DATE", tomoDate);
			editor.commit();
			System.out.println(userPref.getString("TOKEN_REFRESH_TODAY_DATE",
					"NV"));
			System.out.println(userPref.getString("TOKEN_REFRESH_TOMO_DATE",
					"NV"));
			if (!userPref.getString("TOKEN_REFRESH_TODAY_DATE", "NV").equals(
					userPref.getString("TOKEN_REFRESH_TOMO_DATE", "NV"))) {
				System.out.println("TODAY TOMORROW DATE IS DIFFERENT ");

				showRereshTokenPopUp();
			} else {
				if (isInternetPresent) {
					new AllProjects().execute();
					new AllProgressActivities().execute();
				} else {
					showSnack(PM_MainActivity.this,
							"Please check network connection!", "OK");
				}
			}
		}
		homeScreenImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PM_MainActivity.this, AppHome.class);
				startActivity(i);
			}
		});

		rippleProgress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isInternetPresent) {
					mDrawerLayout.closeDrawers();
					finish();
					startActivity(getIntent());
				} else {
					showSnack(PM_MainActivity.this,
							"Please check network connection!", "OK");
				}
			}
		});
		rippleCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isInternetPresent) {
					mDrawerLayout.closeDrawers();
					Intent i = new Intent(PM_MainActivity.this,
							CalenderEvents.class);
					startActivity(i);
				} else {
					showSnack(PM_MainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});

	}

	private void showRereshTokenPopUp() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				PM_MainActivity.this);
		builder1.setMessage("Your token has expired ! Please refresh your token!");
		builder1.setCancelable(true);
		builder1.setPositiveButton("REFRESH",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// dialog.cancel();
						new RefreshToken().execute();

					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();

	}

	public class RefreshToken extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("token", userPref.getString("TOKEN", "NV"));
				jsonToken = SigninPost.makeRequestRefreshToken(json,
						userPref.getString("TOKEN", "NV"),
						host.globalVariable());
				System.out.println(jsonToken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonToken != null) {
				try {
					if (jsonToken.has("status_code")) {
						System.out
								.println("STATUS CODE 401 -----------------------");
						// Re-direct to login page
						Intent i = new Intent(PM_MainActivity.this, Login.class);
						startActivity(i);
					} else if (jsonToken.has("token")) {
						token = jsonToken.getString("token");
						editor.putString("TOKEN", token);
						/*
						 * Toast.makeText(PM_MainActivity.this, "Refreshed",
						 * Toast.LENGTH_LONG).show();
						 */
						showSnack(PM_MainActivity.this, "Refreshed!", "OK");
						editor.putString("TOKEN_REFRESH_TODAY_DATE", tomoDate);
						editor.commit();
						if (isInternetPresent) {
							new AllProjects().execute();
							new AllProgressActivities().execute();
						} else {
							showSnack(PM_MainActivity.this,
									"Please check network connection!", "OK");
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				showSnack(PM_MainActivity.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");

			}
		}
	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setTitle(Html
				.fromHtml("<font color='#000000'>Progress </font>"));
	}

	@SuppressLint("ResourceAsColor")
	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(createDrawerToggle());

	}

	private DrawerListener createDrawerToggle() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerStateChanged(int state) {
			}
		};

		return mDrawerToggle;
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mDrawerToggle.syncState();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();
		switch (item.getItemId()) {

		case R.id.refresh:
			finish();
			startActivity(getIntent());
			return true;
		case R.id.action_logout:
			editor.clear();
			editor.commit();
			Intent i = new Intent(PM_MainActivity.this, Login.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class AllProjects extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pDialog1.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				proJarr = AllProjectsGet.callProsList(host.globalVariable(),
						userPref.getString("TOKEN", "NV"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(proJarr);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (proJarr == null || proJarr.equals(null)) {
				System.out
						.println("SERVER IS NOT WORKING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				pDialog1.setVisibility(View.GONE);
				showSnack(PM_MainActivity.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
			} else {
				if (proJarr.toString() == "[]" || proJarr.length() == 0) {
					pDialog1.setVisibility(View.GONE);
					noDataTxtPros.setVisibility(View.VISIBLE);
				} else {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < proJarr.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						try {
							jsonPro = proJarr.getJSONObject(i);

							proName = jsonPro.getString("name");
							proImg = jsonPro.getString("logo");
							proId = jsonPro.getString("id");
							map.put("proName", proName);

							if (proImg == null || proImg == "null") {
								map.put("proLogo", "");
							} else {
								map.put("proLogo", proImg);
							}

							map.put("proLabel", jsonPro.getString("label_txt"));
							map.put("proId", proId);

							String createdOnDate = jsonPro
									.getString("last_updated");
							System.out.println(createdOnDate);
							createdOnDate = timezone
									.convertDateToTimeZone(createdOnDate
											.substring(0, 18));

							map.put("createdon", createdOnDate);
							map.put("labelcolor",
									jsonPro.getString("label_color"));

							list.add(map);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					pDialog1.setVisibility(View.GONE);
					ListProBaseAdapter adapter = new ListProBaseAdapter(
							PM_MainActivity.this, list);
					porListview.setAdapter(adapter);

				}
			}
		}
	}

	@SuppressLint("NewApi")
	public class ListProBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;
		PrettyTime prettyTime = new PrettyTime();;

		public ListProBaseAdapter(PM_MainActivity allProjects,
				ArrayList<HashMap<String, String>> list) {
			// TODO Auto-generated constructor stub
			this.context = allProjects;
			this.listt = list;
			inflator = (LayoutInflater) allProjects
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listt.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO mylist_add_ind-generated method stub
			return listt.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return listt.get(position).hashCode();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.pm_pros_list_row, null);
				holder = new ViewHolder();
				holder.proNameTxt = (TextView) convertView
						.findViewById(R.id.proTitleText);
				holder.proLogo = (ImageView) convertView
						.findViewById(R.id.proImage);
				holder.createdOnTxt = (TextView) convertView
						.findViewById(R.id.createdOnTxt);
				holder.labelTxt = (TextView) convertView
						.findViewById(R.id.labelTxt);
				holder.labelLay = (RelativeLayout) convertView
						.findViewById(R.id.labelLay);
				holder.layoutRipple = (LinearLayout) convertView
						.findViewById(R.id.proLay);

				holder.proNameTxt.setTypeface(typeFaceMeduim);


				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String proName = listt.get(position).get("proName");

			String proLogourl = listt.get(position).get("proLogo");

			holder.proNameTxt.setTag(position);

			holder.proLogo.setTag(position);

			holder.proNameTxt.setText(proName);

			holder.proNameTxt.setTypeface(typefaceRoman);
			holder.createdOnTxt.setTypeface(typefaceRoman);

			String time = listt.get(position).get("createdon");

			time = time.replace("T", " ");

			time = time.substring(0, 19);
			long timee = timeStringtoMilis(time);

			holder.createdOnTxt.setText(prettyTime.format(new Date(timee)));
			System.out.println(proLogourl);
			if (proLogourl == "" || proLogourl.equals("")) {
				holder.labelLay.setVisibility(View.VISIBLE);
				holder.proLogo.setVisibility(View.GONE);
				holder.labelTxt.setText(listt.get(position).get("proLabel"));
			} else {
				holder.labelLay.setVisibility(View.GONE);
				holder.proLogo.setVisibility(View.VISIBLE);
				imageLoader.displayImage(proLogourl, holder.proLogo, options);
			}

			holder.layoutRipple.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// proId=o.get("proId").toString();
					Intent i = new Intent(getApplicationContext(),
							ProjectActivityTaskGroupTabStrip.class);
					i.putExtra("proId", listt.get(position).get("proId"));
					i.putExtra("proName", listt.get(position).get("proName"));
					editor1.putString("PRONAME",
							listt.get(position).get("proName"));
					editor1.commit();
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
				}
			});

			return convertView;
		}



		public class ViewHolder {
			public TextView proNameTxt, createdOnTxt, labelTxt;
			public ImageView proLogo;
			RelativeLayout labelLay;
			LinearLayout layoutRipple;
		}

	}

	@SuppressLint("SimpleDateFormat")
	private long timeStringtoMilis(String time) {
		long milis = 0;

		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sd.parse(time);
			milis = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return milis;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
	private long mBackPressed;

	@Override
	public void onBackPressed() {
		if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		} else {
			Toast.makeText(getBaseContext(), "Press again to close app",
					Toast.LENGTH_SHORT).show();
		}

		mBackPressed = System.currentTimeMillis();
	}


	private void setOriginRiple(final LayoutRipple layoutRipple) {

		layoutRipple.post(new Runnable() {

			@Override
			public void run() {
				View v = layoutRipple.getChildAt(0);
				layoutRipple.setxRippleOrigin(ViewHelper.getX(v) + v.getWidth()
						/ 2);
				layoutRipple.setyRippleOrigin(ViewHelper.getY(v)
						+ v.getHeight() / 2);

				layoutRipple.setRippleColor(Color.parseColor("#eeeeee"));

				layoutRipple.setRippleSpeed(30);
			}
		});

	}

	public void showSnack(PM_MainActivity appHome, String stringMsg, String ok) {
		new SnackBar(PM_MainActivity.this, stringMsg, ok,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}
}
