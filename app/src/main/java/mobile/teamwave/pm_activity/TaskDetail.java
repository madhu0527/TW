package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CommentsPost;
import mobile.teamwave.Http.SigninPost;
import mobile.teamwave.Http.TaskGrpGet;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.application.ListViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TaskDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	String proId, taskGrpId, taskId, taskName, comentCount, commentDetail,
			firstName, lastName, commentImg, comntCreated, checkBoxStatus;
	TextView taskNametxt, taskGrpNameTxt, proNameTxt, createdByTxt, dateTxt;
	ScrollView scroll;
	JSONObject jsonTaskObject, jsonPushComment;
	JSONArray jsonCommentsArr;
	Hostname host;
	SharedPreferences userPref, proPref;
	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
	ListCommentsBaseAdapter commentsAdapter;
	ListView commentsListview;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	CheckBox checkBox;
	Button sendBtn;
	EditText comntEdit;
	Typeface typefaceLight;
	CircleProgressBar pDialog;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	LinearLayout progrLay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_detail);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		taskNametxt = (TextView) findViewById(R.id.taskName);
		taskGrpNameTxt = (TextView) findViewById(R.id.taskGrpName);
		proNameTxt = (TextView) findViewById(R.id.proName);
		createdByTxt = (TextView) findViewById(R.id.createdByTxt);
		dateTxt = (TextView) findViewById(R.id.dateTxt);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		commentsListview = (ListView) findViewById(R.id.commentListview);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		sendBtn = (Button) findViewById(R.id.addCmntBtn);
		comntEdit = (EditText) findViewById(R.id.comentEdittext);
		scroll = (ScrollView) findViewById(R.id.scrollTasks);
		progrLay=(LinearLayout)findViewById(R.id.progrLay);
		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));

		typefaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoLight.ttf");
		taskNametxt.setTypeface(typefaceLight);
		taskGrpNameTxt.setTypeface(typefaceLight);
		proNameTxt.setTypeface(typefaceLight);
		sendBtn.setTypeface(typefaceLight);
		comntEdit.setTypeface(typefaceLight);

		host = new Hostname();
		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		userPref = getSharedPreferences("USER", MODE_PRIVATE);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		taskGrpId = (String) bundle.get("taskGrpId");
		proId = (String) bundle.get("proId");
		taskId = (String) bundle.get("taskId");
		taskName = (String) bundle.get("taskName");
		checkBoxStatus = (String) bundle.get("checked");

		createdByTxt.setText((String) bundle.get("createdBy"));

		actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + taskName
				+ "</font>"));
		// actionBar.setTitle(taskName);

		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		taskNametxt.setText(taskName);
		proNameTxt.setText(proPref.getString("PRONAME", "NV"));

		scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scroll.setFocusable(true);
		scroll.setFocusableInTouchMode(true);
		scroll.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});

		if (isInternetPresent) {
			new CallTaskDetail().execute();
			new CallTasksCommentsList().execute();
		} else {
			showSnack(TaskDetail.this,
					"Please check network connection!",
					"OK");
		}

		System.out.println(checkBoxStatus + " ^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

		if (checkBoxStatus == null || checkBoxStatus == "null"
				|| checkBoxStatus.equals("null")) {
			checkBox.setChecked(false);
		} else {
			if (checkBoxStatus == "true" || checkBoxStatus.equals("true")) {
				checkBox.setChecked(true);
				taskNametxt.setPaintFlags(taskNametxt.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				checkBox.setChecked(false);
				taskNametxt.setPaintFlags(taskNametxt.getPaintFlags()
						& (~Paint.STRIKE_THRU_TEXT_FLAG));
			}
		}

		checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// CheckBox cb = (CheckBox) v;
				if (isInternetPresent) {
					if (checkBox.isChecked() == true) {

						System.out.println("CHECKEDDDDDDDDDDDDDDDDDD");
						taskNametxt.setPaintFlags(taskNametxt.getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);

						try {
							JSONObject jsonPatch = SigninPost.makeRequestCheck(
									host.globalVariable(),
									userPref.getString("TOKEN", "NV"), proId,
									taskGrpId, taskId, "complete");
							System.out
									.println(jsonPatch
											+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							if (jsonPatch.has("completed")) {
								String status = jsonPatch
										.getString("completed");
								/*
								 * Toast.makeText(getActivity(), status,
								 * Toast.LENGTH_SHORT).show();
								 */
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (checkBox.isChecked() == false) {

						System.out.println("UN CHECKDDDDDDDDDDDDDDDD");
						taskNametxt.setPaintFlags(taskNametxt.getPaintFlags()
								& (~Paint.STRIKE_THRU_TEXT_FLAG));
						try {

							JSONObject jsonPatch = SigninPost.makeRequestCheck(
									host.globalVariable(),
									userPref.getString("TOKEN", "NV"), proId,
									taskGrpId, taskId, "reopen");
							System.out
									.println(jsonPatch
											+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							if (jsonPatch.has("reopened")) {
							//	String status = jsonPatch.getString("reopened");
							/*	Toast.makeText(getApplicationContext(),
										"reopened", Toast.LENGTH_SHORT).show();*/
								showSnack(TaskDetail.this,
										"reopened!",
										"OK");
							}

							// countryList.remove(position);
							// dataAdapter.notifyDataSetChanged();

							/*
							 * finish(); startActivity(getIntent());
							 */
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else {
					showSnack(TaskDetail.this,
							"Please check network connection!",
							"OK");
				}

			}
		});
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (comntEdit.getText().toString() == ""
						|| comntEdit.getText().toString().equals("")
						|| comntEdit.getText().toString().length() == 0
						|| comntEdit.getText().toString() == null) {
				/*	Toast.makeText(getApplicationContext(), "Enter comment",
							Toast.LENGTH_SHORT).show();*/
					showSnack(TaskDetail.this,
							"Enter comment!",
							"OK");
				} else {
					if (isInternetPresent) {
						sendBtn.setText("Adding comment.....");
						new SendComment().execute();

					} else {
						showSnack(TaskDetail.this,
								"Please check network connection!",
								"OK");
					}

				}

			}
		});
	}

	public class CallTaskDetail extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonTaskObject = TaskGrpGet.callTasksDetail(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskGrpId,
						taskId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonTaskObject);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonTaskObject != null) {
				try {
					comentCount = jsonTaskObject.getString("comment_count");
					taskGrpNameTxt.setText(jsonTaskObject
							.getString("taskgroup_name"));

					if (jsonTaskObject.getString("is_completed") == "true"
							|| jsonTaskObject.getString("is_completed").equals(
									"true")) {
						checkBox.setChecked(true);
						taskNametxt.setPaintFlags(taskNametxt.getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
					}

					/*
					 * if(jsonTaskObject.has("modified_by")){ JSONObject
					 * json=jsonTaskObject.getJSONObject("modified_by");
					 * 
					 * }
					 */

					String fileCreatedDate = jsonTaskObject
							.getString("created_on");
					fileCreatedDate = fileCreatedDate.substring(0, 10);

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = df.parse(fileCreatedDate);
						System.out.println(date);
						fileCreatedDate = date.toString();
						System.out.println(fileCreatedDate.length());
						String dayStr = fileCreatedDate.substring(0, 3);
						fileCreatedDate = fileCreatedDate.substring(4, 10);
						System.out.println(fileCreatedDate);
						fileCreatedDate.replaceAll("\\s+", " ");

						dateTxt.setText(dayStr + ", " + fileCreatedDate);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//		pDialog.setVisibility(View.GONE);
		//		progrLay.setVisibility(View.GONE);
			} else {
				showSnack(TaskDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
	}

	public class CallTasksCommentsList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonCommentsArr = TaskGrpGet.callTaskComments(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonCommentsArr);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonCommentsArr != null) {
				try {

					// JSONArray jArray = jsonComments.getJSONArray("results");
					for (int i = 0; i < jsonCommentsArr.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject jsonComments = jsonCommentsArr
								.getJSONObject(i);

						commentDetail = jsonComments
								.getString("comment_detail");
						JSONObject jsonCreatedBy = jsonComments
								.getJSONObject("created_by");

						comntCreated = jsonComments.getString("created_on");

						comntCreated = comntCreated.substring(0, 10);

						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date date;
						try {
							date = df.parse(comntCreated);
							System.out.println(date);
							comntCreated = date.toString();
							System.out.println(comntCreated.length());
							comntCreated = comntCreated.substring(4, 10);
							System.out.println(comntCreated);
							comntCreated.replaceAll("\\s+", " ");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						commentImg = jsonCreatedBy.getString("image");
						firstName = jsonCreatedBy.getString("first_name");
						lastName = jsonCreatedBy.getString("last_name");

						map.put("DETAIL", commentDetail);
						map.put("NAME", firstName + " " + lastName);
						map.put("IMAGE", commentImg);
						map.put("comntCreated", comntCreated);

						commentsList.add(map);
					}
					commentsAdapter = new ListCommentsBaseAdapter(
							getApplicationContext(), commentsList);
					commentsListview.setAdapter(commentsAdapter);
					ListViewUtils.setDynamicHeight(commentsListview);
					// scroll.smoothScrollTo(0, 0);
					commentsListview.setSelectionAfterHeaderView();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);

			} else {
				showSnack(TaskDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListCommentsBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListCommentsBaseAdapter(Context context2,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = context2;
			this.listt = listArticles;
			inflator = (LayoutInflater) context2
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
				convertView = inflator.inflate(R.layout.comments_listrow, null);
				holder = new ViewHolder();
				holder.dpNameTxt = (TextView) convertView
						.findViewById(R.id.nameTxt);
				holder.comntDetailTxt = (TextView) convertView
						.findViewById(R.id.commentTxt);
				holder.timeTxt = (TextView) convertView
						.findViewById(R.id.timeTxt);
				holder.img = (ImageView) convertView
						.findViewById(R.id.image_comment);
				holder.dpNameTxt.setTypeface(typefaceLight);
				holder.comntDetailTxt.setTypeface(typefaceLight);
				holder.timeTxt.setTypeface(typefaceLight);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.dpNameTxt.setTag(position);
			holder.comntDetailTxt.setTag(position);

			holder.dpNameTxt.setText(listt.get(position).get("NAME"));
			holder.comntDetailTxt.setText(Html.fromHtml(listt.get(position)
					.get("DETAIL")));
			holder.timeTxt.setText(Html.fromHtml(listt.get(position).get(
					"comntCreated")));

			imageLoader.displayImage(listt.get(position).get("IMAGE"),
					holder.img, options);

			return convertView;
		}

		public class ViewHolder {
			public TextView dpNameTxt, comntDetailTxt, timeTxt;
			ImageView img;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class SendComment extends AsyncTask<Void, Void, Void> {
		@SuppressLint("InlinedApi")
		protected void onPreExecute() {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jsonComment = new JSONObject();
			try {
				jsonComment.put("comment_detail", comntEdit.getText()
						.toString());
				jsonPushComment = CommentsPost.PostTaskComment(jsonComment,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonPushComment != null) {
				System.out.println(jsonPushComment
						+ "^^^^^^^^^^^^ RESULT OF PUSH COMMENT ^^^^^^^^^^^^^^");
				HashMap<String, String> map = new HashMap<String, String>();
				try {
					commentDetail = jsonPushComment.getString("comment_detail");
					JSONObject jsonCreatedBy = jsonPushComment
							.getJSONObject("created_by");

					comntCreated = jsonPushComment.getString("created_on");

					comntCreated = comntCreated.substring(0, 10);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = df.parse(comntCreated);
						System.out.println(date);
						comntCreated = date.toString();
						System.out.println(comntCreated.length());
						comntCreated = comntCreated.substring(4, 10);
						System.out.println(comntCreated);
						comntCreated.replaceAll("\\s+", " ");
						commentImg = jsonCreatedBy.getString("image");
						firstName = jsonCreatedBy.getString("first_name");
						lastName = jsonCreatedBy.getString("last_name");

						map.put("DETAIL", commentDetail);
						map.put("NAME", firstName + " " + lastName);
						map.put("IMAGE", commentImg);
						map.put("comntCreated", comntCreated);

						commentsList.add(map);
						commentsAdapter = new ListCommentsBaseAdapter(
								getApplicationContext(), commentsList);
						commentsListview.setAdapter(commentsAdapter);
						comntEdit.setText("");
						commentsAdapter.notifyDataSetChanged();
						ListViewUtils.setDynamicHeight(commentsListview);
						pDialog.setVisibility(View.GONE);
						progrLay.setVisibility(View.GONE);
						sendBtn.setText("ADD THIS COMMENT");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				showSnack(TaskDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),
				ProjectActivityTaskGroupTabStrip.class);
		i.putExtra("proId", proId);
		i.putExtra("proName", proPref.getString("PRONAME", "NV"));
		startActivity(i);
		super.onBackPressed();
	}
	public void showSnack(TaskDetail appHome, String stringMsg, String ok){
		new SnackBar(TaskDetail.this,
				stringMsg,
				ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}
}
