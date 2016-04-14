package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.AllActivitiesGet;
import mobile.teamwave.Http.DiscussionsGet;
import mobile.teamwave.Http.EventsGet;
import mobile.teamwave.Http.FilesGet;
import mobile.teamwave.Http.MsgsGet;
import mobile.teamwave.Http.NotesGet;
import mobile.teamwave.Http.SigninPost;
import mobile.teamwave.Http.TaskGrpGet;
import mobile.teamwave.Http.TimeLogGet;
import mobile.teamwave.TaskGrpPojo.CreatedByPojo;
import mobile.teamwave.TaskGrpPojo.TaskGrpPojo;
import mobile.teamwave.TaskGrpPojo.TasksPojo;
import mobile.teamwave.application.ConvertDatetoTimeZone;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.application.NothingSelectedSpinnerAdapter;
import mobile.teamwave.application.NothingSelectedSpinnerAdapter1;
import mobile.teamwave.application.TASKGRP_MODEL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProjectActivityTabs extends Fragment {
	Hostname host;
	int dateVal = 0, occurrenceCount = 0, repeatStartId = 0, taskgroup_id,
			task_Id;
	String hostname, proId, proName, checkTaskGrpLisitngHostname, taskGrpId,
			taskGrpName, taskGrpNameGlobal, taskName, taskId, addTaskgrpname,
			addTaskName, addTaskAssigned = "", userIdForEMail = "",
			addTaskDuedate, addTaskGrpDisc, addNotesTitle, addNotesDisc,
			addSubMsg, addDiscMsg;
	int addTaskGrpId;
	String addeventTitle, addEvntDisc = "", addStartEventTime, addEndEventTime,
			addEventSTymAllDayCheck, addEventETymAllDayCheck,
			addStartEventDate, addEndEventDate, repeatEnd = "",
			endRecurringPeriod = "", repeatStart = "";
	boolean allDayVal = false, repeatVal = false, isRepeatingVal = false;
	String timeDate, dateObj, dayStr, fullName, verb, contentType,
			globalProName, globalTimeDate, nextCallActivities = "", actionType,
			nextCallTasksGrp = "", timeLogMonthStr, checkRefreshActivity = "",
			checkRefreshTaskGrp = "", checkRefreshDiscusions = "",
			checkRefreshFiles = "", checkRefreshNotes = "";
	String nextCallDiscusion = "", displayName, commentDetail,
			globalTimeDateForTimeLog, startDate, endDate, contentId,
			contenttType, addTaskGrpShowDate = "", taskCheckBoxstatus = "";;
	String nextCallNotes = "", nextCallFiles = "", nextCallTimeLog = "";
	String eventId, eventTitle, mileStoneId, mileStoneTitle, eventStrtDate;
	JSONObject jsonProActivities, jsonProbject, jsonProject, jsonActor,
			jsonTaskGrpObject, jsonTaskListGrpObj, jsonTasks, jsonDiscusions,
			jsonFiles, jsonTimeLog, timeLogJson, jsonTasksGrp, jsonAddTask,
			jsonAddNotes, jsonAddEvent, jsonMsg;
	JSONArray notesJarr, eventsJarray, mileStonesArray, usersArray;
	SharedPreferences userPref;
	Date sDate = null, eDate = null;
	ListView listviewActivities;
	LinearLayout eventsDateLay;
	ArrayList<HashMap<String, String>> prosListActivities = new ArrayList<HashMap<String, String>>();
	// ArrayList<HashMap<String, String>> taskGrpsList = new
	// ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> usersListEmail = new ArrayList<HashMap<String, String>>();
	ArrayList<String> usersListEmailnames = new ArrayList<String>();
	ArrayList<HashMap<String, String>> discussionList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> filesList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> notesList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> timeLogList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventOwnList = new ArrayList<HashMap<String, String>>();
	// ArrayList<TASKGRP_MODEL> taskGrpList = new ArrayList<TASKGRP_MODEL>();
	List<TasksPojo> tasksList = new ArrayList<TasksPojo>();
	String globalDateEvents = "";
	ListEventsMilesAdapter adapterEvents;
	int currentPositionActivity, currentPositionTaskGrp,
			currentPositionDiscusion, currentPositionFiles,
			currentPositionNotes, currentPositionTimeLog,
			addTaskLatestGlobalPosition;
	private int position;
	ProgressDialog dialog;
	int actionBarHeight;
	ListActivityInPro adapterProActi;
	ListDiscussionsBaseAdapter adapterDisussions;
	ListNotesBaseAdapter adapterNotes;
	ListFilesBaseAdapter adapterFiles;
	ListTimeLogAdapter adapterTimeLog;
	private static final String ARG_POSITION = "position";
	TaskGrpListModelAdapter dataTaskGrpAdapter;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	ImageView prevImg, nextImg;
	TextView dateTxtEvnt, noDataTxt;
	CircleProgressBar pDialog, loadingBtm;
	Typeface typefaceRoman;
	InternetConnectionDetector netConn;
	SwipeRefreshLayout mSwipeRefreshLayout;
	Boolean isInternetPresent = false;
	ConvertDatetoTimeZone timezone;
	Runnable run;
	TasksModelAdapter tasksAdapter;
	ButtonFloat addBtn;
	ArrayList<TaskGrpPojo> tasksGrpsList = new ArrayList<TaskGrpPojo>();
	TaskGrpModelAdapter adapterTasks;
	ListView listTasks;

	public static ProjectActivityTabs newInstance(int position) {
		ProjectActivityTabs f = new ProjectActivityTabs();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(final LayoutInflater infaltor,
			final ViewGroup container, Bundle savedInstanceState) {
		View v = infaltor.inflate(R.layout.projects_activities_activity,
				container, false);
		TypedValue tv = new TypedValue();

		if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv,
				true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}

		timezone = new ConvertDatetoTimeZone(getActivity());
		typefaceRoman = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");
		mSwipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.activity_main_swipe_refresh_layout);
		listviewActivities = (ListView) v.findViewById(R.id.listviewActivities);
		eventsDateLay = (LinearLayout) v.findViewById(R.id.eventsDateLay);
		prevImg = (ImageView) v.findViewById(R.id.prevImg);
		nextImg = (ImageView) v.findViewById(R.id.nextImg);
		dateTxtEvnt = (TextView) v.findViewById(R.id.dateTxt);
		noDataTxt = (TextView) v.findViewById(R.id.noDataTxt);
		addBtn = (ButtonFloat) v.findViewById(R.id.addBtn);
		dateTxtEvnt.setTypeface(typefaceRoman);

		pDialog = (CircleProgressBar) v.findViewById(R.id.pDialog);
		loadingBtm = (CircleProgressBar) v.findViewById(R.id.loadingBtm);

		// listviewActivities = (ListView)
		// v.findViewById(R.id.listviewActivities);
		host = new Hostname();
		hostname = host.globalVariable();

		userPref = getActivity().getSharedPreferences("USER", 0);

		Intent intent = getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		proId = (String) bundle.get("proId");
		System.out.println(proId);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		netConn = new InternetConnectionDetector(getActivity());
		isInternetPresent = netConn.isConnectingToInternet();

		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		loadingBtm.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		// call users list
		new Users().execute();

		if (isInternetPresent) {

			if (position == 0) {
				prosListActivities = new ArrayList<HashMap<String, String>>();
				globalTimeDate = null;
				globalProName = null;
				loadingBtm.setVisibility(View.GONE);
				addBtn.setVisibility(View.GONE);
				new ProjectProgressActivities().execute();
			} else if (position == 1) {
				addBtn.setVisibility(View.VISIBLE);
				nextCallTasksGrp = "";
				tasksGrpsList = new ArrayList<TaskGrpPojo>();
				loadingBtm.setVisibility(View.GONE);
				listviewActivities.setVisibility(View.VISIBLE);
				new ProjectTaskGrpList().execute();

			} else if (position == 2) {
				listviewActivities.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
				discussionList = new ArrayList<HashMap<String, String>>();
				new ProjectDiscussionsList().execute();
			} else if (position == 3) {
				listviewActivities.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
				addBtn.setVisibility(View.GONE);
				filesList = new ArrayList<HashMap<String, String>>();
				new ProjectFilesList().execute();
			} else if (position == 4) {
				loadingBtm.setVisibility(View.GONE);
				notesList = new ArrayList<HashMap<String, String>>();
				new ProjectNotesList().execute();
			} /*
			 * else if (position == 5) { globalTimeDateForTimeLog = null;
			 * timeLogList = new ArrayList<HashMap<String, String>>(); new
			 * TimeLogList().execute(); }
			 */
			else if (position == 5) {
				eventsDateLay.setVisibility(View.VISIBLE);
				listviewActivities.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
				eventsList = new ArrayList<HashMap<String, String>>();
				eventOwnList = new ArrayList<HashMap<String, String>>();
				getDate();

				System.out.println(startDate
						+ "   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   " + endDate);
				new EventsList().execute();

				// new MileStonesList().execute();
			}
		} else {
			showSnack(getActivity(),
					"Please check network connection!", "OK");
		}

		prevImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgPrev = SVGParser.getSVGFromResource(getResources(),
				R.raw.icons_pre);
		Drawable drwPrev = svgPrev.createPictureDrawable();
		prevImg.setImageDrawable(drwPrev);

		nextImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgNext = SVGParser.getSVGFromResource(getResources(),
				R.raw.icons_next);
		Drawable drwNext = svgNext.createPictureDrawable();
		nextImg.setImageDrawable(drwNext);

		prevImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateVal = dateVal - 1;
				eventsDateLay.setVisibility(View.VISIBLE);
				listviewActivities.setVisibility(View.VISIBLE);
				eventsList = new ArrayList<HashMap<String, String>>();
				eventOwnList = new ArrayList<HashMap<String, String>>();
				globalDateEvents = "";
				getDate();

				System.out.println(startDate
						+ "   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   " + endDate);
				new EventsList().execute();
			}
		});
		nextImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateVal = dateVal + 1;
				eventsDateLay.setVisibility(View.VISIBLE);
				listviewActivities.setVisibility(View.VISIBLE);
				eventsList = new ArrayList<HashMap<String, String>>();
				eventOwnList = new ArrayList<HashMap<String, String>>();
				globalDateEvents = "";
				getDate();

				System.out.println(startDate
						+ "   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   " + endDate);
				new EventsList().execute();
			}
		});
		mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,
				R.color.green, R.color.blue);
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {

								if (position == 0) {
									globalTimeDate = "";
									globalProName = "";
									nextCallActivities = "";
									prosListActivities = new ArrayList<HashMap<String, String>>();
									new ProjectProgressActivities().execute();
									// setupAdapter();
									// adapter.notifyDataSetChanged();
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											if (prosListActivities.toString() == "[]"
													|| prosListActivities
															.size() == 0) {
											} else {
												adapterProActi
														.notifyDataSetChanged();
												listviewActivities
														.invalidateViews();
											}
										}
									});
								} else if (position == 1) {

									nextCallTasksGrp = "";
									checkRefreshTaskGrp = "";
									tasksGrpsList = new ArrayList<TaskGrpPojo>();
									listviewActivities
											.setVisibility(View.VISIBLE);
									new ProjectTaskGrpList().execute();
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											if (tasksGrpsList.toString() == "[]"
													|| tasksGrpsList.size() == 0) {
											} else {
												dataTaskGrpAdapter
														.notifyDataSetChanged();
												listviewActivities
														.invalidateViews();
											}

										}
									});

								} else if (position == 2) {
									nextCallDiscusion = "";
									checkRefreshDiscusions = "";
									listviewActivities
											.setVisibility(View.VISIBLE);
									discussionList = new ArrayList<HashMap<String, String>>();
									new ProjectDiscussionsList().execute();
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											if (discussionList.toString() == "[]"
													|| discussionList.size() == 0) {
											} else {
												adapterDisussions
														.notifyDataSetChanged();
												listviewActivities
														.invalidateViews();
											}

										}
									});
								} else if (position == 3) {

									nextCallFiles = "";
									checkRefreshFiles = "";
									listviewActivities
											.setVisibility(View.VISIBLE);
									filesList = new ArrayList<HashMap<String, String>>();
									new ProjectFilesList().execute();
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											if (filesList.toString() == "[]"
													|| filesList.size() == 0) {
											} else {
												adapterFiles
														.notifyDataSetChanged();
												listviewActivities
														.invalidateViews();
											}

										}
									});

								} else if (position == 4) {

									nextCallNotes = "";
									checkRefreshNotes = "";
									listviewActivities
											.setVisibility(View.VISIBLE);
									notesList = new ArrayList<HashMap<String, String>>();
									new ProjectNotesList().execute();
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											if (notesList.toString() == "[]"
													|| notesList.size() == 0) {
											} else {
												adapterNotes
														.notifyDataSetChanged();
												listviewActivities
														.invalidateViews();
											}

										}
									});

								} else if (position == 5) {
									listviewActivities
											.setVisibility(View.VISIBLE);
									eventsDateLay.setVisibility(View.VISIBLE);
									listviewActivities
											.setVisibility(View.VISIBLE);
									eventsList = new ArrayList<HashMap<String, String>>();
									eventOwnList = new ArrayList<HashMap<String, String>>();
									globalDateEvents = "";
									new EventsList().execute();
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											if (eventOwnList.toString() == "[]"
													|| eventOwnList.size() == 0) {
											} else {
												adapterEvents
														.notifyDataSetChanged();
												listviewActivities
														.invalidateViews();
											}

										}
									});
								}
								mSwipeRefreshLayout.setRefreshing(false);
							}
						}, 2500);
					}
				});

		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (position == 1) {
					final Dialog popup = new Dialog(getActivity());
					popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					popup.setContentView(R.layout.add_taskgrp_layout);
					popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
					popup.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);

					TextView submitTxt = (TextView) popup
							.findViewById(R.id.addTaskListTxt);
					TextView cancelTxt = (TextView) popup
							.findViewById(R.id.cencelTxt);
					final EditText taskGrpNameEdit = (EditText) popup
							.findViewById(R.id.taskGrpName);
					final EditText discTaskGrpEdit = (EditText) popup
							.findViewById(R.id.discTaskGrp);

					submitTxt.setTypeface(typefaceRoman);
					cancelTxt.setTypeface(typefaceRoman);
					discTaskGrpEdit.setTypeface(typefaceRoman);
					taskGrpNameEdit.setTypeface(typefaceRoman);

					CircleProgressBar pdialog1 = (CircleProgressBar) popup
							.findViewById(R.id.pDialog1);
					pdialog1.setColorSchemeResources(
							android.R.color.holo_green_light,
							android.R.color.holo_orange_light,
							android.R.color.holo_red_light);
					final InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					submitTxt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isInternetPresent) {
								addTaskgrpname = taskGrpNameEdit.getText()
										.toString();
								addTaskGrpDisc = discTaskGrpEdit.getText()
										.toString();

								if (addTaskgrpname == ""
										|| addTaskgrpname.equals("")) {
								/*	Toast.makeText(getActivity(),
											"Enter a task group name",
											Toast.LENGTH_SHORT).show();*/
									
									showSnack(getActivity(),
											"Enter a task group name!", "OK");
									
								} else {
									imm.toggleSoftInput(
											InputMethodManager.SHOW_FORCED, 0);
									popup.dismiss();
									new AddTaskGroup().execute();

								}
							} else {
								showSnack(getActivity(),
										"Please check network connection!", "OK");
							}
						}
					});

					cancelTxt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
									0);
							popup.dismiss();

						}
					});

					popup.show();

					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

				} else if (position == 2) {

					// Add Messages

					final Dialog popup = new Dialog(getActivity());
					popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					popup.setContentView(R.layout.add_discussions);
					popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
					popup.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);
					popup.show();

					final EditText subEdit = (EditText) popup
							.findViewById(R.id.titleMsg);
					final EditText contentEdit = (EditText) popup
							.findViewById(R.id.pbmlDis);
					final TextView postMsg = (TextView) popup
							.findViewById(R.id.submitTxt);
					final TextView newDocHeader = (TextView) popup
							.findViewById(R.id.newMsg);
					final ImageView backImg = (ImageView) popup
							.findViewById(R.id.backImg);

					subEdit.setTypeface(typefaceRoman);
					contentEdit.setTypeface(typefaceRoman);
					postMsg.setTypeface(typefaceRoman);
					newDocHeader.setTypeface(typefaceRoman);

					backImg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							popup.dismiss();
						}
					});
					postMsg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isInternetPresent) {
								addSubMsg = subEdit.getText().toString();
								addDiscMsg = contentEdit.getText().toString();

								if (addSubMsg == "" || addSubMsg.equals("")) {
									/*Toast.makeText(getActivity(),
											"Enter a message title",
											Toast.LENGTH_SHORT).show();*/
									
									showSnack(getActivity(),
											"Enter a message title!", "OK");
									
								} else {
									popup.dismiss();
									new PostMsg().execute();
								}
							} else {
								showSnack(getActivity(),
										"Please check network connection!", "OK");
							}
						}
					});

				} else if (position == 3) {
				} else if (position == 4) {

					// Add notes

					final Dialog popup = new Dialog(getActivity());
					popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					popup.setContentView(R.layout.add_notes_layout);
					popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
					popup.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);

					final EditText titleEdit = (EditText) popup
							.findViewById(R.id.titleTxt);
					final EditText contentEdit = (EditText) popup
							.findViewById(R.id.contentTxt);
					final TextView saveNotes = (TextView) popup
							.findViewById(R.id.addNotes);
					final TextView newDocHeader = (TextView) popup
							.findViewById(R.id.newDocument);
					final ImageView backImg = (ImageView) popup
							.findViewById(R.id.backImg);

					titleEdit.setTypeface(typefaceRoman);
					contentEdit.setTypeface(typefaceRoman);
					saveNotes.setTypeface(typefaceRoman);
					newDocHeader.setTypeface(typefaceRoman);

					titleEdit.setHintTextColor(getResources().getColor(
							R.color.noteTxtcolor));
					contentEdit.setHintTextColor(getResources().getColor(
							R.color.noteTxtcolor));

					titleEdit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							titleEdit.setText("");
							titleEdit.setHint("");
							titleEdit.setCursorVisible(true);
						}
					});
					titleEdit.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub
							saveNotes.setVisibility(View.VISIBLE);
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							if (titleEdit.getText().length() == 0) {
								titleEdit.setHint("Untitled");
							}
						}
					});
					contentEdit.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub
							saveNotes.setVisibility(View.VISIBLE);
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							if (contentEdit.getText().length() == 0) {
								contentEdit.setHint("Type your notes.........");
							}
						}
					});

					contentEdit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							contentEdit.setText("");
							contentEdit.setHint("");
							contentEdit.setCursorVisible(true);
						}
					});

					backImg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							popup.dismiss();
						}
					});


					saveNotes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isInternetPresent) {
								addNotesTitle = titleEdit.getText().toString();
								addNotesDisc = contentEdit.getText().toString();

								if (addNotesTitle == ""
										|| addNotesTitle.equals("")) {
									
									showSnack(getActivity(),
											"Enter a notes title!", "OK");
									
								} else if (addNotesDisc == ""
										|| addNotesDisc.equals("")) {
									showSnack(getActivity(),
											"Enter a notes content!", "OK");
								} else {
									popup.dismiss();
									new AddNotes().execute();

								}
							} else {
								showSnack(getActivity(),
										"Please check network connection!", "OK");
							}
						}
					});

					popup.show();

				} else if (position == 5) {

					// Add Events

					final Dialog popup = new Dialog(getActivity());
					popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					popup.setContentView(R.layout.add_event_activity);
					popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
					popup.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);

					final Button startTymBtn = (Button) popup
							.findViewById(R.id.startTym);
					final Button endTymBtn = (Button) popup
							.findViewById(R.id.endTym);
					final Button startDateBtn = (Button) popup
							.findViewById(R.id.startDate);
					final Button endDateBtn = (Button) popup
							.findViewById(R.id.endDate);
					final TextView saveEvents = (TextView) popup
							.findViewById(R.id.addEvent);
					final ImageView backImg = (ImageView) popup
							.findViewById(R.id.backImg);
					final EditText titleEdit = (EditText) popup
							.findViewById(R.id.eventTitleEdit);
					final EditText contentEdit = (EditText) popup
							.findViewById(R.id.eventDiscEdit);
					final EditText occurEdit = (EditText) popup
							.findViewById(R.id.occurEdit);
					final EditText occurDateEdit = (EditText) popup
							.findViewById(R.id.dateoccEdit);
					final Spinner assignedToSpiiner = (Spinner) popup
							.findViewById(R.id.notAssiSpin);
					final Spinner repeatStartSpin = (Spinner) popup
							.findViewById(R.id.repeatSpin);
					final Spinner repeatEndSpin = (Spinner) popup
							.findViewById(R.id.repeatEndSpin);
					final com.gc.materialdesign.views.CheckBox allDayCheck = (com.gc.materialdesign.views.CheckBox) popup
							.findViewById(R.id.checkBoxAllDay);
					final com.gc.materialdesign.views.CheckBox repeatCheck = (com.gc.materialdesign.views.CheckBox) popup
							.findViewById(R.id.checkBoxRepeatTxt);
					final RelativeLayout repeatStartLay = (RelativeLayout) popup
							.findViewById(R.id.repeatsLay);
					final RelativeLayout repeatEndLay = (RelativeLayout) popup
							.findViewById(R.id.repeatsEndLay);
					final LinearLayout occurNumLay = (LinearLayout) popup
							.findViewById(R.id.occurLay);
					final LinearLayout dateoccLay = (LinearLayout) popup
							.findViewById(R.id.dateoccLay);

					ArrayAdapter<CharSequence> repeatStartAdapter = ArrayAdapter
							.createFromResource(getActivity(),
									R.array.repeatStartysArr,
									R.layout.repeatstartspnr);
					repeatStartAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					repeatStartSpin
							.setAdapter(new NothingSelectedSpinnerAdapter(
									repeatStartAdapter,
									R.layout.repeat_starts_spinner_nothing_selected,
									getActivity()));

					ArrayAdapter<CharSequence> repeatEndAdapter = ArrayAdapter
							.createFromResource(getActivity(),
									R.array.repeatEndsArr,
									R.layout.repeatstartspnr);
					repeatEndAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					repeatEndSpin
							.setAdapter(new NothingSelectedSpinnerAdapter1(
									repeatEndAdapter,
									R.layout.repeat_ends_spinner_nothing_selected,
									getActivity()));

					titleEdit.setTypeface(typefaceRoman);
					contentEdit.setTypeface(typefaceRoman);
					saveEvents.setTypeface(typefaceRoman);

					// setting current time to Buttons
					final Calendar c = Calendar.getInstance();
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);
					try {
						DateFormat f1 = new SimpleDateFormat("HH:mm");
						Date dStartTym = f1.parse(hour + ":" + minute);
						Date dEndTym = f1.parse(hour + 1 + ":" + minute);
						DateFormat f2 = new SimpleDateFormat("h:mm a");

						addStartEventTime = f2.format(dStartTym).toUpperCase();
						addEndEventTime = f2.format(dEndTym).toUpperCase();
						// for All day check & uncheck
						addEventSTymAllDayCheck = f2.format(dStartTym)
								.toUpperCase();
						addEventETymAllDayCheck = f2.format(dEndTym)
								.toUpperCase();
						startTymBtn.setText(f2.format(dStartTym).toUpperCase());
						endTymBtn.setText(f2.format(dEndTym).toUpperCase());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// setting current date & time to Buttons
					startDateBtn.setText(DateFormat.getDateInstance().format(
							new Date()));
					endDateBtn.setText(DateFormat.getDateInstance().format(
							new Date()));

					DateFormat startdateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date startDate = new Date();
					System.out.println(startdateFormat.format(startDate));
					addStartEventDate = startdateFormat.format(startDate);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						sDate = sdf.parse(addStartEventDate);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					DateFormat endDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date endDate = new Date();
					System.out.println(endDateFormat.format(endDate));
					addEndEventDate = endDateFormat.format(endDate);
					try {
						eDate = sdf.parse(addEndEventDate);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(usersListEmail
							+ " ^^^^^^^^^^^^^^^^^^^^^^ ");

					ArrayAdapter<String> assignedAdapter = new ArrayAdapter<String>(
							getActivity(), R.layout.spinr_txt,
							usersListEmailnames);

					assignedAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					assignedToSpiiner
							.setAdapter(new NothingSelectedSpinnerAdapter1(
									assignedAdapter,
									R.layout.assigned_spinner_nothing_selected,
									getActivity()));

					// START DATE LISTENERS

					final Calendar myCalendar = Calendar.getInstance();
					final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							myCalendar.set(Calendar.YEAR, year);
							myCalendar.set(Calendar.MONTH, monthOfYear);
							myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							SimpleDateFormat monthParse = new SimpleDateFormat(
									"MM");
							SimpleDateFormat monthDisplay = new SimpleDateFormat(
									"MMMM");
							// return
							// monthDisplay.format(monthParse.parse(String.valueOf(monthOfYear)));
							int month = monthOfYear + 1;
							try {
								addStartEventDate = year + "-" + month + "-"
										+ dayOfMonth;
								startDateBtn.setText(dayOfMonth
										+ " "
										+ monthDisplay.format(monthParse.parse(String
												.valueOf(monthOfYear + 1)))
										+ " " + year);
								
								//set start date to end date button
							//	int month = monthOfYear + 1;
								addEndEventDate = year + "-" + month + "-"
										+ dayOfMonth;
								endDateBtn.setText(dayOfMonth
										+ " "
										+ monthDisplay.format(monthParse.parse(String
												.valueOf(monthOfYear + 1)))
										+ " " + year);
								
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								eDate = sdf.parse(addEndEventDate);
								sDate = sdf.parse(addStartEventDate);
								System.out.println(sDate + " START DATE");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					};
					// END DATE LISTENERS
					final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							myCalendar.set(Calendar.YEAR, year);
							myCalendar.set(Calendar.MONTH, monthOfYear);
							myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							SimpleDateFormat monthParse = new SimpleDateFormat(
									"MM");
							SimpleDateFormat monthDisplay = new SimpleDateFormat(
									"MMMM");
							// return
							// monthDisplay.format(monthParse.parse(String.valueOf(monthOfYear)));
							int month = monthOfYear + 1;
							addEndEventDate = year + "-" + month + "-"
									+ dayOfMonth;
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								eDate = sdf.parse(addEndEventDate);

								if (sDate.after(eDate)) {
									System.out
											.println(sDate
													+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
													+ eDate);
									/*Toast.makeText(
											getActivity(),
											"End date should not be less than start date",
											Toast.LENGTH_SHORT).show();*/
									showSnack(getActivity(),
											"End date should not be less than start date!", "OK");
								}

								endDateBtn.setText(dayOfMonth
										+ " "
										+ monthDisplay.format(monthParse.parse(String
												.valueOf(monthOfYear + 1)))
										+ " " + year);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					};
					// REPEAT -END TYPE is DATE then show calender
					final DatePickerDialog.OnDateSetListener repeatEndDate = new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							myCalendar.set(Calendar.YEAR, year);
							myCalendar.set(Calendar.MONTH, monthOfYear);
							myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							SimpleDateFormat monthParse = new SimpleDateFormat(
									"MM");
							SimpleDateFormat monthDisplay = new SimpleDateFormat(
									"MMMM");

							endRecurringPeriod = year + "-" + monthOfYear + "-"
									+ dayOfMonth;
							occurDateEdit.setText(year + "-" + monthOfYear
									+ "-" + dayOfMonth);

						}

					};
					// START TIME
					startTymBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Calendar mcurrentTime = Calendar.getInstance();
							int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
							int minute = mcurrentTime.get(Calendar.MINUTE);
							TimePickerDialog mTimePicker;
							mTimePicker = new TimePickerDialog(getActivity(),
									new TimePickerDialog.OnTimeSetListener() {
										@Override
										public void onTimeSet(
												TimePicker timePicker,
												int selectedHour,
												int selectedMinute) {
											// eReminderTime.setText(
											// selectedHour + ":" +
											// selectedMinute);
											try {
												DateFormat f1 = new SimpleDateFormat(
														"HH:mm");
												Date d = f1.parse(selectedHour
														+ ":" + selectedMinute);
												DateFormat f2 = new SimpleDateFormat(
														"h:mm a");
												f2.format(d).toUpperCase(); // "12:18am"
												startTymBtn.setText(f2
														.format(d)
														.toUpperCase());
												addStartEventTime = f2
														.format(d)
														.toUpperCase();
												// for All day check & uncheck
												addEventSTymAllDayCheck = f2
														.format(d)
														.toUpperCase();

											} catch (ParseException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									}, hour, minute, false);// Yes 24 hour time
							mTimePicker.setTitle("Select Time");
							mTimePicker.show();
						}
					});

					// END TIME

					endTymBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Calendar mcurrentTime = Calendar.getInstance();
							int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
							int minute = mcurrentTime.get(Calendar.MINUTE);
							TimePickerDialog mTimePicker;
							mTimePicker = new TimePickerDialog(getActivity(),
									new TimePickerDialog.OnTimeSetListener() {
										@Override
										public void onTimeSet(
												TimePicker timePicker,
												int selectedHour,
												int selectedMinute) {
											// eReminderTime.setText(
											// selectedHour + ":" +
											// selectedMinute);
											try {
												DateFormat f1 = new SimpleDateFormat(
														"HH:mm");
												Date d = f1.parse(selectedHour
														+ ":" + selectedMinute);
												DateFormat f2 = new SimpleDateFormat(
														"h:mm a");
												f2.format(d).toUpperCase(); // "12:18am"
												endTymBtn.setText(f2.format(d)
														.toUpperCase());
												addEndEventTime = f2.format(d)
														.toUpperCase();
												addEventETymAllDayCheck = f2
														.format(d)
														.toUpperCase();
											} catch (ParseException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									}, hour, minute, false);// Yes 24 hour time
							mTimePicker.setTitle("Select Time");
							mTimePicker.show();
						}
					});

					startDateBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							new DatePickerDialog(getActivity(), dateStart,
									myCalendar.get(Calendar.YEAR), myCalendar
											.get(Calendar.MONTH), myCalendar
											.get(Calendar.DAY_OF_MONTH)).show();
						}
					});
					endDateBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							new DatePickerDialog(getActivity(), dateEnd,
									myCalendar.get(Calendar.YEAR), myCalendar
											.get(Calendar.MONTH), myCalendar
											.get(Calendar.DAY_OF_MONTH)).show();
						}
					});
					assignedToSpiiner
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int pos, long arg3) {
									// TODO Auto-generated method stub
									addTaskAssigned = String
											.valueOf(assignedToSpiiner
													.getSelectedItem());
									System.out.println(pos + " : SPINNER POS ");
									System.out.println(addTaskAssigned
											+ "!!!!!!!!!!!!");
									System.out.println(usersListEmail
											+ "!!!!!!!!!!!!!!!");
									if (pos == 0) {

									} else {
										userIdForEMail = usersListEmail.get(
												pos - 1).get(addTaskAssigned);

										System.out
												.println(userIdForEMail
														+ " ==================================");
									}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
								}
							});

					saveEvents.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isInternetPresent) {
								addeventTitle = titleEdit.getText().toString();
								addEvntDisc = contentEdit.getText().toString();

								if (addeventTitle == ""
										|| addeventTitle.equals("")) {
									/*Toast.makeText(getActivity(),
											"Enter a event title",
											Toast.LENGTH_SHORT).show();*/
									showSnack(getActivity(),
											"Enter a event title!", "OK");
									
								} else if (sDate.after(eDate)) {
									System.out
											.println(sDate
													+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
													+ eDate);
								/*	Toast.makeText(
											getActivity(),
											"End date should not be less than start date",
											Toast.LENGTH_SHORT).show();*/
									
									showSnack(getActivity(),
											"End date should not be less than start date!", "OK");
									
								} else {
									popup.dismiss();

									if (repeatEnd == "occurrence"
											|| repeatEnd.equals("occurrence")) {
										Editable value = occurEdit.getText();
										occurrenceCount = Integer.valueOf(value
												.toString());
									}
									if (repeatEnd == "date"
											|| repeatEnd.equals("date")) {
										Editable value = occurDateEdit
												.getText();
										endRecurringPeriod = value.toString();
									}

									new AddEvent().execute();

								}
							} else {
								showSnack(getActivity(),
										"Please check network connection!", "OK");
							}
						}
					});
					backImg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							popup.dismiss();
						}
					});
					allDayCheck.setOncheckListener(new OnCheckListener() {

						@Override
						public void onCheck(
								com.gc.materialdesign.views.CheckBox view,
								boolean isChecked) {
							// TODO Auto-generated method stub

							if (isChecked) {
								System.out
										.println(" CHECKEDDDDDDDDDDDDDDDDDDDDDD ");
								startTymBtn.setText("--");
								endTymBtn.setText("--");
								addStartEventTime = "";
								addEndEventTime = "";
								allDayVal = true;
							} else {
								System.out
										.println("UN CHECKEDDDDDDDDDDDDDDDDDDDDDD ");
								addStartEventTime = addEventSTymAllDayCheck;
								addEndEventTime = addEventETymAllDayCheck;
								startTymBtn.setText(addStartEventTime);
								endTymBtn.setText(addEndEventTime);
								allDayVal = false;
							}
						}
					});
					repeatCheck.setOncheckListener(new OnCheckListener() {

						@Override
						public void onCheck(
								com.gc.materialdesign.views.CheckBox view,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								System.out
										.println(" CHECKEDDDDDDDDDDDDDDDDDDDDDD ");
								repeatStartLay.setVisibility(View.VISIBLE);
								repeatEndLay.setVisibility(View.VISIBLE);
								repeatVal = true;
								isRepeatingVal = true;
								repeatStartId = 3;
							} else {
								System.out
										.println("UN CHECKEDDDDDDDDDDDDDDDDDDDDDD ");
								repeatStartLay.setVisibility(View.GONE);
								repeatEndLay.setVisibility(View.GONE);
								repeatVal = false;
								isRepeatingVal = false;
								repeatStartId = 0;
							}
						}
					});

					repeatStartSpin
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									repeatStart = String
											.valueOf(repeatStartSpin
													.getSelectedItem());

									if (repeatStart == "Every day"
											|| repeatStart.equals("Every day")) {
										repeatStartId = 4;
									} else if (repeatStart == "Every week"
											|| repeatStart.equals("Every week")) {
										repeatStartId = 3;
									} else if (repeatStart == "Every month"
											|| repeatStart
													.equals("Every month")) {
										repeatStartId = 2;
									} else if (repeatStart == "Every year"
											|| repeatStart.equals("Every year")) {
										repeatStartId = 1;
									} else {
										repeatStartId = 0;
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									repeatStartId = 0;
								}
							});
					repeatEndSpin
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									repeatEnd = String.valueOf(repeatEndSpin
											.getSelectedItem());

									if (repeatEnd == "On date"
											|| repeatEnd.equals("On date")) {
										// Show calender
										repeatEnd = "date";

										new DatePickerDialog(
												getActivity(),
												repeatEndDate,
												myCalendar.get(Calendar.YEAR),
												myCalendar.get(Calendar.MONTH),
												myCalendar
														.get(Calendar.DAY_OF_MONTH))
												.show();
										dateoccLay.setVisibility(View.VISIBLE);
										occurNumLay.setVisibility(View.GONE);

									} else if (repeatEnd == "After number of times"
											|| repeatEnd
													.equals("After number of times")) {
										// show edit text popup
										repeatEnd = "occurrence";
										occurNumLay.setVisibility(View.VISIBLE);
										dateoccLay.setVisibility(View.GONE);

									} else {
										repeatEnd = "never";
										occurNumLay.setVisibility(View.GONE);
										dateoccLay.setVisibility(View.GONE);
									}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									repeatEnd = "never";
								}
							});

					popup.show();

				}

			}
		});

		getFragmentManager().popBackStack();
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		MenuItem item = menu.findItem(R.id.addtoCal);
		if (position == 5) {
			item.setVisible(true);
		} else {
			item.setVisible(false);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addtoCal:
			// Not implemented here
			if (eventOwnList.size() == 0 || eventOwnList.toString() == "[]") {
				/*Toast.makeText(getActivity(), "There is no events!",
						Toast.LENGTH_SHORT).show();
				*/
				showSnack(getActivity(),
						"There is no events!", "OK");
				
			} else {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				if (isInternetPresent) {
					System.out.println(eventOwnList
							+ "++++++++++++++++++++++++++++++++++++++");
					pDialog.setVisibility(View.VISIBLE);

					dialog = new ProgressDialog(getActivity());
					dialog.setCancelable(true);
					dialog.setMessage("Loading...");
					// set the progress to be horizontal
					dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					// reset the bar to the default value of 0
					dialog.setProgress(0);

					// set the maximum value
					dialog.setMax(eventOwnList.size());
					// display the progressbar
					dialog.show();

					// create a thread for updating the progress bar
					Thread background = new Thread(new Runnable() {
						public void run() {
							try {
								// enter the code to be run while displaying the
								// progressbar.
								//
								// This example is just going to increment the
								// progress bar:
								// So keep running until the progress value
								// reaches maximum value
								while (dialog.getProgress() <= dialog.getMax()) {
									// wait 500ms between each update
									Thread.sleep(1500);

									// active the update handler
									progressHandler.sendMessage(progressHandler
											.obtainMessage());
								}
							} catch (InterruptedException e) {
								// if something fails do something smart
							}
						}
					});

					// start the background thread
					background.start();

					for (int j = 0; j < eventOwnList.size(); j++) {

						try {
							Date date = new SimpleDateFormat("yyyy-MM-dd")
									.parse(eventOwnList.get(j).get("startD"));

							Date date1 = new SimpleDateFormat("yyyy-MM-dd")
									.parse(eventOwnList.get(j).get("endD"));
							Calendar calBegin = Calendar.getInstance();
							calBegin.setTime(date);
							int monthBegin = calBegin.get(Calendar.MONTH);
							int dayBegin = calBegin.get(Calendar.DAY_OF_MONTH);
							int yearBegin = calBegin.get(Calendar.YEAR);

							Calendar calEnd = Calendar.getInstance();
							calEnd.setTime(date1);
							int monthEnd = calEnd.get(Calendar.MONTH);
							int dayEnd = calEnd.get(Calendar.DAY_OF_MONTH);
							int yearEnd = calEnd.get(Calendar.YEAR);
							System.out.println(yearBegin + " : " + monthBegin
									+ " : " + dayBegin + "___ "
									+ eventOwnList.get(j).get("TITLE"));
							System.out.println(yearEnd + " : " + monthEnd
									+ " : " + dayEnd + "___ "
									+ eventOwnList.get(j).get("TITLE"));

							SimpleDateFormat displayFormat = new SimpleDateFormat(
									"HH:mm");
							SimpleDateFormat parseFormat = new SimpleDateFormat(
									"hh:mm a");
							Date startTym = parseFormat.parse(eventOwnList.get(
									j).get("STARTTIME"));
							Date endTym = parseFormat.parse(eventOwnList.get(j)
									.get("ENDTIME"));

							String sTymHrs = displayFormat.format(startTym)
									.toString().substring(0, 2);
							String sTymMins = displayFormat.format(startTym)
									.toString().substring(3, 5);

							String eTymHrs = displayFormat.format(endTym)
									.toString().substring(0, 2);
							String eTymMins = displayFormat.format(endTym)
									.toString().substring(3, 5);

							System.out.println(displayFormat.format(startTym)
									+ "     START TIMEEEEEEEEEEEEEEEEEE   "
									+ sTymHrs + ":" + sTymMins);
							System.out.println(displayFormat.format(endTym)
									+ "      END TIMEEEEEEEEEEEEEEEEEE"
									+ eTymHrs + ":" + eTymMins);

							ContentValues cv = new ContentValues();
							cv.put("calendar_id", j + 1);
							cv.put("title", eventOwnList.get(j).get("TITLE"));
							// cv.put("dtstart", dtstart );
							// cv.put("dtend", dtend);
							cv.put("rrule", "FREQ=MONTHLY");
							cv.put("description", "comment");
							Calendar start = Calendar.getInstance();
							start.set(yearBegin, monthBegin, dayBegin,
									Integer.parseInt(sTymHrs),
									Integer.parseInt(sTymMins), 0);

							Calendar end = Calendar.getInstance();
							end.set(yearEnd, monthEnd, dayEnd,
									Integer.parseInt(eTymHrs),
									Integer.parseInt(eTymMins), 0);

							long startTime = start.getTimeInMillis();
							long endTime = end.getTimeInMillis();

							cv.put("dtstart", startTime);
							cv.put("dtend", endTime);
							// Insertion on the events of the calendar
							ContentResolver contentResolver = getActivity()
									.getContentResolver();
							cv.put(CalendarContract.Events.EVENT_TIMEZONE,
									TimeZone.getDefault().getID());
							contentResolver
									.insert(Uri
											.parse("content://com.android.calendar/events"),
											cv);

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					dialog.setMessage("Sync completed");
					pDialog.setVisibility(View.GONE);
					/*Toast.makeText(getActivity(), "Sync is completed",
							Toast.LENGTH_SHORT).show();*/
					showSnack(getActivity(),
							"Sync is completed!", "OK");
					return true;
				} else {
					showSnack(getActivity(),
							"Please check network connection!", "OK");
				}
			}

			return false;
			/*
			 * case R.id.fragment_menu_item: // Do Fragment menu item stuff here
			 * return true;
			 */
		default:
			break;
		}

		return false;
	}

	public class AddTaskGroup extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// login.setVisibility(View.GONE);
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("name", addTaskgrpname);
				json.put("description", addTaskGrpDisc);

				jsonTasksGrp = TaskGrpGet.createTaskGroup(json,
						userPref.getString("TOKEN", "NV"), hostname, proId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonTasksGrp != null) {
				System.out.println(jsonTasksGrp
						+ " *********************************");
				Gson gson = new Gson();
				TaskGrpPojo taskGrp = gson.fromJson(jsonTasksGrp.toString(),
						TaskGrpPojo.class);
				tasksGrpsList.add(0, taskGrp);

				Gson gsonn = new Gson();
				String json = gsonn.toJson(taskGrp);
				System.out.println(json + "      !!!!!!!!!!!!!!!!!!!!!!!!!");

				adapterTasks = new TaskGrpModelAdapter(getActivity(),
						R.layout.tasks_grp_list_row, tasksGrpsList);
				addTaskGrpShowDate = "added";
				listviewActivities.setAdapter(adapterTasks);
				pDialog.setVisibility(View.GONE);
				noDataTxt.setText("");
				
			//
			//	addTaskLatestGlobalPosition = 0;


			//	addTaskGrpId = taskGrp.getId();
				
				System.out.println(taskGrp.getId() + " ! ! ! ! ! ! !  ! ! ! ! ");
				
				addTaskPopup(0,taskGrp.getId());
			} else {
				showSnack(getActivity(),
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
			}
		}

	}

	public class AddTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// login.setVisibility(View.GONE);
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("name", addTaskName);
				// addTaskAssigned="{"+"id:"+1 + "}";
				JSONObject assignedJson = new JSONObject();
				if (userIdForEMail == "" || userIdForEMail.equals("")) {
					assignedJson.put("id", userPref.getString("USER_ID", "NV"));
				} else {
					assignedJson.put("id", userIdForEMail);
				}
				json.put("assigned_to", assignedJson);
				JSONArray attachArr = new JSONArray();

				json.put("taskgroup_id", addTaskGrpId);
				json.put("attachment", attachArr);

				jsonAddTask = TaskGrpGet.createTask(json,
						userPref.getString("TOKEN", "NV"), hostname, proId,
						addTaskGrpId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e + "   EXCEPTION");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonAddTask != null) {
				System.out.println(jsonAddTask
						+ " *********************************");
				Gson gson = new Gson();
				TasksPojo tasks = gson.fromJson(jsonAddTask.toString(),
						TasksPojo.class);
				// tasksList.add(tasks);
				System.out.println(addTaskLatestGlobalPosition
						+ " ^^^^^^^^^^^^^^^^^^^^^^^");
				tasksGrpsList.get(addTaskLatestGlobalPosition).getTasks()
						.add(tasks);

				adapterTasks = new TaskGrpModelAdapter(getActivity(),
						R.layout.tasks_grp_list_row, tasksGrpsList);
				listviewActivities.setAdapter(adapterTasks);

				/*
				 * Gson gsonn = new Gson(); String json = gsonn.toJson(tasks);
				 * System.out .println(json +
				 * "      !!!!!!!!!!!!!!!!!!!!!!!!!");
				 */

				// tasksGrpsList.;

				/*
				 * TasksModelAdapter tasksAdapter=new
				 * TasksModelAdapter(getActivity(), R.layout.task_row,
				 * tasksList); //listTasks.setAdapter(tasksAdapter);
				 * tasksAdapter.notifyDataSetChanged();
				 * listviewActivities.invalidateViews();
				 */
				pDialog.setVisibility(View.GONE);
				noDataTxt.setText("");
			} else {
				Toast.makeText(getActivity(),
						"Something went wrong! Please try again",
						Toast.LENGTH_SHORT).show();
				pDialog.setVisibility(View.GONE);
			}
		}

	}

	public class PostMsg extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// login.setVisibility(View.GONE);
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("subject", addSubMsg);
				json.put("description", addDiscMsg);
				json.put("is_message", true);
				JSONArray arr = new JSONArray();
				json.put("attachment", arr);
				jsonMsg = MsgsGet.createMsg(json,
						userPref.getString("TOKEN", "NV"), hostname, proId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonMsg != null) {
				System.out.println(jsonMsg
						+ " *********************************");
				HashMap<String, String> map = new HashMap<String, String>();

				try {
					JSONObject createdByJson = jsonMsg
							.getJSONObject("created_by");
					map.put("FULLNAME", createdByJson.getString("full_name"));
					map.put("DISPLAYPIC", createdByJson.getString("image"));

					String msgTym = jsonMsg.getString("created_on");
					msgTym = timezone.convertDateToTimeZone(msgTym.substring(0,
							18));

					map.put("CREATEDON", msgTym);

					map.put("PROID", proId);

					map.put("DPNAME", jsonMsg.getString("subject"));
					map.put("display_name", jsonMsg.getString("subject"));
					map.put("COMNTDETAIL", jsonMsg.getString("description"));
					map.put("CONTENTID", jsonMsg.getString("id"));
					map.put("CONTENTTYPE", "message");
					discussionList.add(0, map);
					adapterDisussions = new ListDiscussionsBaseAdapter(
							getActivity(), discussionList);
					listviewActivities.setAdapter(adapterDisussions);
					pDialog.setVisibility(View.GONE);
					noDataTxt.setVisibility(View.GONE);
					noDataTxt.setText("");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				Toast.makeText(getActivity(),
						"Something went wrong! Please try again",
						Toast.LENGTH_SHORT).show();
				pDialog.setVisibility(View.GONE);
			}
		}

	}

	public class AddNotes extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// login.setVisibility(View.GONE);
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("project", proId);
				json.put("name", addNotesTitle);
				json.put("content", addNotesDisc);

				jsonAddNotes = NotesGet.createNotes(json,
						userPref.getString("TOKEN", "NV"), hostname, proId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonAddNotes != null) {
				System.out.println(jsonAddNotes
						+ " *********************************");
				HashMap<String, String> map = new HashMap<String, String>();

				try {
					JSONObject jsonCreatedBy = jsonAddNotes
							.getJSONObject("created_by");
					String createdOn = jsonAddNotes.getString("created_on");

					createdOn = createdOn.substring(0, 10);

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = df.parse(createdOn);
						System.out.println(date);
						createdOn = date.toString();
						System.out.println(createdOn.length());
						dayStr = createdOn.substring(0, 3);
						createdOn = createdOn.substring(4, 10);
						System.out.println(createdOn);
						createdOn.replaceAll("\\s+", " ");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					map.put("PROID", proId);
					map.put("NOTES_ID", jsonAddNotes.getString("id"));
					map.put("TITLE", jsonAddNotes.getString("name"));
					map.put("CONTENT", jsonAddNotes.getString("content"));
					map.put("COMENT_COUNT",
							jsonAddNotes.getString("comment_count"));
					map.put("CREATEDON", dayStr + "," + createdOn);
					map.put("CREATEDBY", jsonCreatedBy.getString("full_name"));

					noDataTxt.setVisibility(View.GONE);
					notesList.add(0, map);
					adapterNotes = new ListNotesBaseAdapter(getActivity(),
							notesList);
					listviewActivities.setAdapter(adapterNotes);
					pDialog.setVisibility(View.GONE);
					noDataTxt.setText("");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {

				Toast.makeText(getActivity(),
						"Something went wrong! Please try again",
						Toast.LENGTH_SHORT).show();
				pDialog.setVisibility(View.GONE);
			}
		}

	}

	// Add event
	public class AddEvent extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// login.setVisibility(View.GONE);
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("id", 0);
				json.put("title", addeventTitle);
				json.put("description", addEvntDisc);
				json.put("occurrence_count", occurrenceCount);
				json.put("all_day", allDayVal);

				System.out.println(addStartEventDate + "       "
						+ addEndEventDate + "  *******************  ");

				json.put("start", addStartEventDate);
				json.put("start_time", addStartEventTime);
				json.put("end_time", addEndEventTime);
				json.put("end", addEndEventDate);
				json.put("end_recurring_period", endRecurringPeriod);
				json.put("repeat", repeatVal);
				json.put("is_repeating", isRepeatingVal);
				json.put("rule_id", repeatStartId);
				json.put("end_type", repeatEnd);

				JSONArray jArry = new JSONArray();
				if (userIdForEMail == "" || userIdForEMail.equals("")) {
					jArry.put(userPref.getString("USER_ID", "NV"));
				} else {
					jArry.put(userIdForEMail);
				}
				json.put("emails", jArry);

				System.out.println(json + "   ^^^^^^^^^^^^^^^^^^^^^^^^^^");

				jsonAddEvent = EventsGet.createEvents(json,
						userPref.getString("TOKEN", "NV"), hostname, proId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonAddEvent != null) {
				System.out.println("RESPONSE    :    " + jsonAddEvent
						+ " *********************************");
				HashMap<String, String> eventsMap = new HashMap<String, String>();
				try {
					eventId = jsonAddEvent.getString("id");
					eventTitle = jsonAddEvent.getString("title");
					eventStrtDate = jsonAddEvent.getString("start_dt");
					String timeDate = eventStrtDate;
					// timeDate = timezone.convertDateToTimeZone(timeDate);

					timeDate = timeDate.substring(0, 10);

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = df.parse(timeDate);
						System.out.println(date);
						timeDate = date.toString();
						System.out.println(timeDate.length());
						dayStr = timeDate.substring(0, 3);
						System.out.println(dayStr);
						timeDate = timeDate.substring(4, 10);
						System.out.println(timeDate);
						timeDate.replaceAll("\\s+", " ");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String startTym = jsonAddEvent.getString("start_time");
					// startTym = timezone.convertTimeToUtcZone(startTym);

					String endTym = jsonAddEvent.getString("end_time");
					// endTym = timezone.convertTimeToUtcZone(endTym);

					eventsMap.put("ID", eventId);
					eventsMap.put("TITLE", eventTitle);
					eventsMap.put("DATECHECK", timeDate);
					eventsMap.put("DAY", dayStr);
					eventsMap.put("STARTDATE", eventStrtDate);
					eventsMap.put("STARTTIME", startTym);
					eventsMap.put("ENDTIME", endTym);
					eventsMap
							.put("DISC", jsonAddEvent.getString("description"));
					eventsMap.put("PROID", proId);
					System.out.println(jsonAddEvent.getString("start") + "    "
							+ jsonAddEvent.getString("end"));
					eventsMap.put("startD", jsonAddEvent.getString("start"));
					eventsMap.put("endD", jsonAddEvent.getString("end"));
					System.out.println(eventsMap);

					eventOwnList.add(0, eventsMap);
					// eventsList.add(eventsMap);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				noDataTxt.setVisibility(View.GONE);
				adapterEvents = new ListEventsMilesAdapter(getActivity(),
						eventOwnList);
				listviewActivities.setAdapter(adapterEvents);
				pDialog.setVisibility(View.GONE);
				noDataTxt.setText("");
			} else {
				Toast.makeText(getActivity(),
						"Something went wrong! Please try again",
						Toast.LENGTH_SHORT).show();
				pDialog.setVisibility(View.GONE);
			}
		}

	}

	// PROJECT PROGRESS

	public class ProjectProgressActivities extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			if (nextCallActivities == "" || nextCallActivities.equals("")) {
				pDialog.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
			} else {
				loadingBtm.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonProActivities = AllActivitiesGet.callProActivitiesList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId,
						nextCallActivities);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonProActivities);
			return null;
		}

		protected void onPostExecute(Void unused) {

			try {
				if (jsonProActivities != null) {
					JSONArray jArray = jsonProActivities
							.getJSONArray("results");

					if (jArray.toString() == "[]" || jArray.length() == 0) {
						noDataTxt.setVisibility(View.VISIBLE);
						pDialog.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < jArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonProbject = jArray.getJSONObject(i);
							timeDate = jsonProbject.getString("created_on");
							String activityTym = jsonProbject
									.getString("created_on");
							activityTym = timezone
									.convertDateToTimeZone(activityTym
											.substring(0, 18));
							map.put("createdOnDate", activityTym);

							actionType = jsonProbject.getString("action_type");

							if (actionType == "deleted"
									|| actionType.equals("deleted")) {
								map.put("contentType", "");
							} else {
								// activity detail json

								JSONObject targetObj = jsonProbject
										.getJSONObject("target_obj");
								contentType = jsonProbject
										.getString("content_type");

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
											map.put("taskName", taskName);

											System.out.println(firtPart);
											System.out.println(secdPart);
										}
									}

									System.out.println(secondPart);

								}
								map.put("contentType", contentType);
							}

							if (jsonProbject.has("project_obj")) {
								jsonProject = jsonProbject
										.getJSONObject("project_obj");
								System.out.println(jsonProject);
							}
							if (jsonProbject.has("actor")) {
								jsonActor = jsonProbject.getJSONObject("actor");
								System.out.println(jsonActor);
								fullName = jsonActor.getString("full_name");
								map.put("displayPic",
										jsonActor.getString("image"));
							}
							verb = jsonProbject.getString("verb");

							String proId = jsonProbject.getString("project_id");
							map.put("proId", proId);

							String proName = jsonProject
									.getString("display_name");

							// String timeDate =
							// listt.get(position).get("created").toString();
							// timeDate=timezone.convertDateToTimeZone(timeDate);
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
							}
							System.out.println(globalTimeDate + timeDate);
							if (globalTimeDate == null
									|| !globalTimeDate.equals(timeDate)) {
								globalTimeDate = timeDate;

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

							// map.put("proNameeWithDate", proName);
							map.put("verbTime", timeDate);
							map.put("day", dayStr);
							map.put("fullName", fullName);
							map.put("verb", verb);

							prosListActivities.add(map);
							System.out.println(map);
						}
						currentPositionActivity = listviewActivities
								.getLastVisiblePosition();
						if (getActivity() == null
								|| getActivity().isFinishing() == true) {
							System.out
									.println(" getActivity is returing NULLLLLLLLLLLLL");
						} else {
							adapterProActi = new ListActivityInPro(
									getActivity(), prosListActivities);
							System.out.println(prosListActivities);
							listviewActivities.setAdapter(adapterProActi);
						}

						pDialog.setVisibility(View.GONE);
						loadingBtm.setVisibility(View.GONE);
						listviewActivities.setDivider(null);

						if (checkRefreshActivity != ""
								|| !checkRefreshActivity.equals("")) {
							DisplayMetrics displayMetrics = getResources()
									.getDisplayMetrics();
							int height = displayMetrics.heightPixels;
							System.out
									.println(height
											+ "-------------ACTION BAR HEIGHT--------------");
							listviewActivities.setSelectionFromTop(
									currentPositionActivity + 1, height - 220);
						}

					}

				} else {
					pDialog.setVisibility(View.GONE);
					Toast.makeText(getActivity(),
							"Something went wrong! Check server",
							Toast.LENGTH_SHORT).show();
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

							System.out.println(nextCallActivities);
							try {
								nextCallActivities = jsonProActivities
										.getString("next");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (nextCallActivities == null
									|| nextCallActivities == ""
									|| nextCallActivities.equals("null")
									|| nextCallActivities.equals("")) {
								Toast.makeText(getActivity(),
										"There is no more activities!",
										Toast.LENGTH_LONG).show();
							} else {
								checkRefreshActivity = "NEXT";

								new ProjectProgressActivities().execute();

							}
						}
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListActivityInPro extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;
		PrettyTime prettyTime = new PrettyTime();;

		public ListActivityInPro(Context projectActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = projectActivity;
			this.listt = listArticles;
			/*
			 * inflator = (LayoutInflater) projectActivity
			 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 */
			inflator = getLayoutInflater(null);
			// vi = inflater.inflate(R.layout.stores_listview_layout, parent,
			// false);
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
				convertView = inflator.inflate(R.layout.pro_progress_row,
						parent, false);
				holder = new ViewHolder();
				holder.dayTxt = (TextView) convertView.findViewById(R.id.day);
				holder.dateTxt = (TextView) convertView.findViewById(R.id.date);
				/*
				 * holder.proNameTxt = (TextView) convertView
				 * .findViewById(R.id.proName);
				 */
				holder.createdNameTxt = (TextView) convertView
						.findViewById(R.id.createdName);
				holder.dateContentTypeTxt = (TextView) convertView
						.findViewById(R.id.dateAgo);
				/*
				 * holder.verbNameTxt = (TextView) convertView
				 * .findViewById(R.id.verbName);
				 */
				holder.displayPicImg = (ImageView) convertView
						.findViewById(R.id.displayPic);

				holder.layClick = (RelativeLayout) convertView
						.findViewById(R.id.layClick);

				holder.createdNameTxt.setTypeface(typefaceRoman);
				holder.dateContentTypeTxt.setTypeface(typefaceRoman);
				holder.dateTxt.setTypeface(typefaceRoman);
				holder.dayTxt.setTypeface(typefaceRoman);

				/*
				 * holder.dateLay = (RelativeLayout) convertView
				 * .findViewById(R.id.datelay);
				 */
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.layClick.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println(listt.get(position).get("contentType"));

					if (listt.get(position).get("contentType") == "message"
							|| listt.get(position).get("contentType")
									.equals("message")
							|| listt.get(position).get("contentType") == "messages"
							|| listt.get(position).get("contentType")
									.equals("messages")) {
						Intent i = new Intent(getActivity(),
								MessageDetail.class);
						i.putExtra("ID", listt.get(position).get("ID"));
						i.putExtra("TITLE",
								listt.get(position).get("display_name"));
						i.putExtra("PROID", listt.get(position).get("proId"));
						i.putExtra("msgUrl",
								listt.get(position).get("detailurl"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					}

					else if (listt.get(position).get("contentType") == "events"
							|| listt.get(position).get("contentType")
									.equals("events")) {
						Intent i = new Intent(getActivity(), EventDetail.class);
						i.putExtra("ID", listt.get(position).get("ID"));
						i.putExtra("TITLE",
								listt.get(position).get("display_name"));
						i.putExtra("PROID", listt.get(position).get("proId"));
						i.putExtra("eventUrl",
								listt.get(position).get("detailurl"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "event"
							|| listt.get(position).get("contentType")
									.equals("event")
							|| listt.get(position).get("contentType") == "milestone"
							|| listt.get(position).get("contentType")
									.equals("milestone")) {
						Intent i = new Intent(getActivity(), EventDetail.class);
						i.putExtra("ID", listt.get(position).get("ID"));
						i.putExtra("TITLE", listt.get(position).get("TITLE"));
						i.putExtra("PROID", listt.get(position).get("proId"));
						i.putExtra("eventUrl",
								listt.get(position).get("detailurl"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "task"
							|| listt.get(position).get("contentType")
									.equals("task")
							|| listt.get(position).get("contentType") == "tasks"
							|| listt.get(position).get("contentType")
									.equals("tasks")) {
						Intent i = new Intent(getActivity(), TaskDetail.class);
						i.putExtra("taskName",
								listt.get(position).get("taskName"));
						i.putExtra("proId", listt.get(position).get("proId"));
						i.putExtra("taskId", listt.get(position).get("taskId"));
						i.putExtra("taskGrpId",
								listt.get(position).get("taskGrpId"));
						System.out.println(listt.get(position).get("taskName")
								+ listt.get(position).get("proId")
								+ listt.get(position).get("taskId")
								+ listt.get(position).get("taskGrpId"));
						i.putExtra("createdBy",
								listt.get(position).get("fullName"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "taskgroups"
							|| listt.get(position).get("contentType")
									.equals("taskgroups")
							|| listt.get(position).get("contentType") == "taskgroup"
							|| listt.get(position).get("contentType")
									.equals("taskgroup")) {
						Intent i = new Intent(getActivity(), TasksList.class);
						i.putExtra("taskGrpId", listt.get(position).get("ID"));

						i.putExtra("proId", listt.get(position).get("proId"));
						i.putExtra("taskgrpName",
								listt.get(position).get("display_name"));

						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					}
					// comment
					else if (listt.get(position).get("contentType") == "notes"
							|| listt.get(position).get("contentType")
									.equals("notes")) {
						Intent i = new Intent(getActivity(), NotesDetail.class);
						i.putExtra("proId", listt.get(position).get("proId"));
						i.putExtra("notesId", listt.get(position).get("ID"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("contentType") == "file"
							|| listt.get(position).get("contentType")
									.equals("file")
							|| listt.get(position).get("contentType") == "files"
							|| listt.get(position).get("contentType")
									.equals("files")) {

						if (listt.get(position).get("detailurl")
								.contains("?ids")) {
							System.out
									.println("MULTIPLE FILES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							Intent i = new Intent(getActivity(),
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
							Intent i = new Intent(getActivity(),
									FileDetail.class);

							System.out.println(listt.get(position).get(
									"detailurl")
									+ listt.get(position).get("proId")
									+ listt.get(position).get("ID"));

							i.putExtra("fileUrl",
									listt.get(position).get("detailurl"));
							i.putExtra("proId", listt.get(position)
									.get("proId"));
							i.putExtra("fileId", listt.get(position).get("ID"));
							i.putExtra("title",
									listt.get(position).get("display_name"));
							startActivity(i);
							getActivity()
									.overridePendingTransition(
											R.anim.slide_in_left,
											R.anim.slide_out_left);
						}

					}
				}
			});

			String day = listt.get(position).get("day");

			/*
			 * holder.createdNameTxt.setText(listt.get(position).get("fullName"))
			 * ; holder.verbNameTxt.setText(listt.get(position).get("verb"));
			 */

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

			imageLoader.displayImage(listt.get(position).get("displayPic"),
					holder.displayPicImg, options);

			// holder.dateLay.setTag(position);

			// holder.dayTxt.setTag(position);
			// holder.proNameTxt.setTag(position);

			if (listt.get(position).get("created") == ""
					|| listt.get(position).get("created").equals("")) {
				holder.dateTxt.setVisibility(View.GONE);
				holder.dayTxt.setVisibility(View.GONE);

			} else {
				holder.dateTxt.setText(listt.get(position).get("created"));
				holder.dateTxt.setVisibility(View.VISIBLE);
				holder.dayTxt.setVisibility(View.VISIBLE);
				// holder.proNameTxt.setText(listt.get(position).get("proNameeWithDate"));
			}

			/*
			 * if (listt.get(position).get("proName") == "" ||
			 * listt.get(position).get("proName").equals("")) {
			 * holder.proNameTxt.setVisibility(View.GONE); } else {
			 * holder.proNameTxt.setVisibility(View.VISIBLE); }
			 * holder.proNameTxt.setText(listt.get(position).get("proName"));
			 */

			String time = listt.get(position).get("createdOnDate");

			time = time.replace("T", " ");

			time = time.substring(0, 19);
			long timee = timeStringtoMilis(time);

			holder.dateContentTypeTxt.setText(prettyTime
					.format(new Date(timee)));
			/*
			 * holder.dateContentTypeTxt.setText(listt.get(position).get(
			 * "createdOnDate"));
			 */
			holder.dayTxt.setText(listt.get(position).get("day"));

			return convertView;
		}

		public class ViewHolder {
			public TextView dateTxt, dayTxt, createdNameTxt,
					dateContentTypeTxt;
			public ImageView displayPicImg;
			// public RelativeLayout dateLay;
			public RelativeLayout layClick;
		}
	}

	// TASK GROUP LIST

	public class ProjectTaskGrpList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			System.out.println(nextCallTasksGrp + "!!!!!!!!!!!!!!!!!!");
			if (nextCallTasksGrp == "" || nextCallTasksGrp.equals("")) {
				pDialog.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
			} else {
				loadingBtm.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				System.out.println(nextCallTasksGrp);
				jsonTaskGrpObject = TaskGrpGet.callTaskGrpList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId,
						nextCallTasksGrp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonTaskGrpObject);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (jsonTaskGrpObject != null) {
					JSONArray jArray = jsonTaskGrpObject
							.getJSONArray("results");
					if (jArray.toString() == "[]" || jArray.length() == 0) {
						noDataTxt.setText("No tasks found!");
						noDataTxt.setVisibility(View.VISIBLE);
						pDialog.setVisibility(View.GONE);
					} else {

						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jsonTaskListGrpObj = jArray
									.getJSONObject(i);
							Gson gson = new Gson();
							TaskGrpPojo taskGrp = gson.fromJson(
									jsonTaskListGrpObj.toString(),
									TaskGrpPojo.class);
							tasksGrpsList.add(taskGrp);

							Gson gsonn = new Gson();
							String json = gsonn.toJson(taskGrp);
							System.out.println(json
									+ "      !!!!!!!!!!!!!!!!!!!!!!!!!");

						}

						if (tasksGrpsList == null
								|| tasksGrpsList.toString() == "null") {
							System.out.println(tasksGrpsList
									+ " TASK GRP IS NULLLLLLLLLLLLL");
						} else {

							if (getActivity() == null
									|| getActivity().isFinishing() == true) {
								System.out
										.println(" getActivity is returing NULLLLLLLLLLLLL");
							} else {
								adapterTasks = new TaskGrpModelAdapter(
										getActivity(),
										R.layout.tasks_grp_list_row,
										tasksGrpsList);
								listviewActivities.setAdapter(adapterTasks);
								pDialog.setVisibility(View.GONE);
								loadingBtm.setVisibility(View.GONE);
							}
						}

					}

				} else {
					pDialog.setVisibility(View.GONE);
					Toast.makeText(getActivity(),
							"Something went wrong! Check server",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

					this.currentFirstVisibleItem = firstVisibleItem;
					this.currentVisibleItemCount = visibleItemCount;
					this.totalItemCount = totalItemCount;

				}

				private void isScrollCompleted() {
					if (this.currentVisibleItemCount > 0
							&& this.currentScrollState == SCROLL_STATE_IDLE
							&& this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
						try {
							nextCallTasksGrp = jsonTaskGrpObject
									.getString("next");
							System.out.println(nextCallTasksGrp);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (nextCallTasksGrp == null || nextCallTasksGrp == ""
								|| nextCallTasksGrp.equals("null")
								|| nextCallTasksGrp.equals("")) {
							Toast.makeText(getActivity(),
									"There is no more task groups!",
									Toast.LENGTH_LONG).show();
						} else {
							System.out.println("API IS CALLING AGAIN"
									+ "_____  " + nextCallTasksGrp);
							checkRefreshTaskGrp = "NEXT";
							new ProjectTaskGrpList().execute();

						}
					}
				}
			});

		}
	}

	private class TaskGrpModelAdapter extends ArrayAdapter<TaskGrpPojo> {

		ArrayList<TaskGrpPojo> taskGrpsList;
		LayoutInflater inflator;
		Context context;
		ViewHolder holder;

		public TaskGrpModelAdapter(Context projectTaskGrpList,
				int textViewResourceId, List<TaskGrpPojo> usersList) {
			super(projectTaskGrpList, textViewResourceId, usersList);
			this.taskGrpsList = new ArrayList<TaskGrpPojo>();
			this.taskGrpsList.addAll(usersList);
			context = projectTaskGrpList;
		}

		@Override
		public int getCount() {
			return taskGrpsList.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			Log.v("ConvertView", String.valueOf(position));
			final TaskGrpPojo taskGrps = taskGrpsList.get(position);
			if (convertView == null) {
				inflator = (LayoutInflater) getActivity().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflator.inflate(R.layout.tasks_grp_list_row,
						null);
				holder = new ViewHolder();
				holder.taskGrpNameTxt = (TextView) convertView
						.findViewById(R.id.taskgrpName);
				holder.listTasks = (ListView) convertView
						.findViewById(R.id.listviewTasks);
				holder.taskGrpLay = (LinearLayout) convertView
						.findViewById(R.id.taskGrpLay);
				holder.taskDetailLay = (RelativeLayout) convertView
						.findViewById(R.id.taskDateDetailLay);
				holder.createdByTxt = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.dateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);
				holder.addTask = (TextView) convertView
						.findViewById(R.id.addTask);
				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);
				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);
				holder.taskGrpNameTxt.setTypeface(typefaceRoman);
				holder.createdByTxt.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);
				holder.addTask.setTypeface(typefaceRoman);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.taskGrpLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println(position + "POS" + " "
							+ taskGrps.getId() + " GRP ID " + "   " + proId
							+ " PRO ID " + "  " + taskGrps.getName() + " "
							+ "NAME");

					Intent i = new Intent(getActivity(), TasksList.class);
					i.putExtra("taskGrpId", String.valueOf(taskGrps.getId()));
					i.putExtra("proId", proId);
					i.putExtra("taskgrpName", taskGrps.getName());
					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
				}
			});
			holder.addTask.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					addTaskLatestGlobalPosition = position;

					System.out.println(addTaskLatestGlobalPosition
							+ " POS *********************");

					addTaskGrpId = taskGrps.getId();
					
					
					addTaskPopup(addTaskLatestGlobalPosition,addTaskGrpId);
					
					/*final Dialog popup = new Dialog(getActivity());
					popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					popup.setContentView(R.layout.add_task);
					popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
					popup.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);

					TextView submitTxt = (TextView) popup
							.findViewById(R.id.addTaskListTxt);
					TextView cancelTxt = (TextView) popup
							.findViewById(R.id.cencelTxt);
					final EditText taskNameEdit = (EditText) popup
							.findViewById(R.id.taskName);
					final EditText dueDateEdit = (EditText) popup
							.findViewById(R.id.dueDate);
					final Spinner assignedToSpiiner = (Spinner) popup
							.findViewById(R.id.notAssiSpin);
					RelativeLayout lineSpinr = (RelativeLayout) popup
							.findViewById(R.id.line3);
					submitTxt.setTypeface(typefaceRoman);
					cancelTxt.setTypeface(typefaceRoman);
					taskNameEdit.setTypeface(typefaceRoman);

					CircleProgressBar pdialog1 = (CircleProgressBar) popup
							.findViewById(R.id.pDialog1);
					pdialog1.setColorSchemeResources(
							android.R.color.holo_green_light,
							android.R.color.holo_orange_light,
							android.R.color.holo_red_light);

					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
					if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
						// Do something for lollipop and above versions
						lineSpinr.setVisibility(View.VISIBLE);
					} else {
						// do something for phones running an SDK before
						// lollipop
						lineSpinr.setVisibility(View.GONE);
					}

					ArrayAdapter<String> assignedAdapter = new ArrayAdapter<String>(
							getActivity(), R.layout.spinr_txt,
							usersListEmailnames);

					assignedAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					assignedToSpiiner
							.setAdapter(new NothingSelectedSpinnerAdapter1(
									assignedAdapter,
									R.layout.assigned_spinner_nothing_selected,
									getActivity()));

					final Calendar myCalendar = Calendar.getInstance();
					final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							myCalendar.set(Calendar.YEAR, year);
							myCalendar.set(Calendar.MONTH, monthOfYear);
							myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							SimpleDateFormat monthParse = new SimpleDateFormat(
									"MM");
							SimpleDateFormat monthDisplay = new SimpleDateFormat(
									"MMMM");
							// return
							// monthDisplay.format(monthParse.parse(String.valueOf(monthOfYear)));

							try {
								addStartEventDate = year + "-" + monthOfYear
										+ "-" + dayOfMonth;
								dueDateEdit.setText(dayOfMonth
										+ " "
										+ monthDisplay.format(monthParse.parse(String
												.valueOf(monthOfYear + 1)))
										+ " " + year);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					};
					dueDateEdit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new DatePickerDialog(getActivity(), dateStart,
									myCalendar.get(Calendar.YEAR), myCalendar
											.get(Calendar.MONTH), myCalendar
											.get(Calendar.DAY_OF_MONTH)).show();
						}
					});
					assignedToSpiiner
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int pos, long arg3) {
									// TODO Auto-generated method stub
									addTaskAssigned = String
											.valueOf(assignedToSpiiner
													.getSelectedItem());
									System.out.println(pos + " : SPINNER POS ");
									System.out.println(addTaskAssigned
											+ "!!!!!!!!!!!!");
									System.out.println(usersListEmail
											+ "!!!!!!!!!!!!!!!");
									if (pos == 0) {

									} else {
										userIdForEMail = usersListEmail.get(
												pos - 1).get(addTaskAssigned);

										System.out
												.println(userIdForEMail
														+ " ==================================");
									}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
								}
							});
					submitTxt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isInternetPresent) {
								addTaskName = taskNameEdit.getText().toString();
								addTaskDuedate = dueDateEdit.getText()
										.toString();

								if (addTaskName == "" || addTaskName.equals("")) {
									Toast.makeText(getActivity(),
											"Enter a task name",
											Toast.LENGTH_SHORT).show();
								} else {
									// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
									// 0);
									popup.dismiss();
									new AddTask().execute();

								}
							} else {
								showSnack(getActivity(),
										"Please check network connection!", "OK");
							}
						}

					});

					cancelTxt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
							// 0);
							popup.dismiss();

						}
					});

					popup.show();
*/
					// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

				}
			});

			// tasks array
			tasksList = taskGrps.getTasks();
			System.out.println(tasksList.size() + "!!!!!!!!!!!!!!!!");
			if (addTaskGrpShowDate == "" || addTaskGrpShowDate.equals("")) {
				holder.taskDetailLay.setVisibility(View.GONE);
				holder.addTask.setVisibility(View.GONE);
			} else {
				holder.taskDetailLay.setVisibility(View.GONE);
				holder.addTask.setVisibility(View.GONE);
			}
			if (tasksList.size() == 0) {
				holder.taskDetailLay.setVisibility(View.VISIBLE);
				holder.addTask.setVisibility(View.VISIBLE);
				holder.comntCountTxt.setText(String.valueOf(taskGrps
						.getComment_count()));
				String taskGrpCreatedDate = taskGrps.getCreated_on();

				taskGrpCreatedDate = taskGrpCreatedDate.substring(0, 10);

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

				try {
					Date date = df.parse(taskGrpCreatedDate);
					System.out.println(date);
					taskGrpCreatedDate = date.toString();
					System.out.println(taskGrpCreatedDate.length());
					String dayStr = taskGrpCreatedDate.substring(0, 3);
					taskGrpCreatedDate = taskGrpCreatedDate.substring(4, 10);
					System.out.println(taskGrpCreatedDate);
					taskGrpCreatedDate = taskGrpCreatedDate.replaceAll("\\s+",
							" ");
					holder.dateTxt.setText(taskGrpCreatedDate);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			holder.taskGrpNameTxt.setText(taskGrps.getName());

			CreatedByPojo createdBy = taskGrps.getCreated_by();

			holder.createdByTxt.setText(createdBy.getFull_name());
			System.out.println(tasksList.size()
					+ "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			TasksModelAdapter tasksAdapter = new TasksModelAdapter(
					getActivity(), R.layout.task_row, tasksList);
			tasksAdapter.addTaskGlobalPosition = position;
			holder.listTasks.setAdapter(tasksAdapter);

			// holder.taskNameTxt.setText(tasksList.get(position).getName());

			return convertView;
		}

		private class ViewHolder {
			TextView taskGrpNameTxt, taskNameTxt, createdByTxt, comntCountTxt,
					dateTxt, addTask;
			CheckBox checkBox;
			RelativeLayout tot, checkBoxView, taskDetailLay;
			LinearLayout taskGrpLay, taskGrpLay1;
			ImageView comntImg;
			ListView listTasks;
		}
	}

	private class TasksModelAdapter extends ArrayAdapter<TasksPojo> {

		List<TasksPojo> tasksListPo;
		String checkBoxStatus;
		private LayoutInflater inflator;
		private Context context;
		// boolean array for storing
		// the state of each CheckBox
		boolean[] checkBoxState;
		int addTaskGlobalPosition;
		ViewHolder holder;

		public TasksModelAdapter(Context taskGrpModelAdapter,
				int textViewResourceId, List<TasksPojo> usersList) {
			super(taskGrpModelAdapter, textViewResourceId, usersList);
			System.out.println(usersList.size()
					+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			this.context = taskGrpModelAdapter;
			this.tasksListPo = usersList;

			System.out.println(tasksListPo.size()
					+ "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

			// this.tasksListPo.addAll(usersList);
			// context=taskGrpModelAdapter;
			checkBoxState = new boolean[tasksListPo.size()];
		}

		@Override
		public int getCount() {
			return tasksListPo.size();
		}

		@Override
		public TasksPojo getItem(int position) {
			// TODO mylist_add_ind-generated method stub
			return tasksListPo.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return tasksListPo.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println(tasksListPo.size()
					+ "********************************");
			final TasksPojo tasks = tasksListPo.get(position);
			if (convertView == null) {
				inflator = (LayoutInflater) getActivity().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflator.inflate(R.layout.task_row, null);

				holder = new ViewHolder();
				holder.taskNameTxt = (TextView) convertView
						.findViewById(R.id.tasksName);
				holder.addTask = (TextView) convertView
						.findViewById(R.id.addTask);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);
				holder.createdByTxt = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.dateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);

				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);
				holder.taskDetailLay = (RelativeLayout) convertView
						.findViewById(R.id.taskDetailLay);
				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);

				holder.addTask.setTypeface(typefaceRoman);
				holder.taskNameTxt.setTypeface(typefaceRoman);
				holder.createdByTxt.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			System.out.println(tasks.getName() + "!!!!!!!!!!!!!!!!!!!!!!!!");

			CreatedByPojo createdBy = tasks.getCreated_by();

			holder.createdByTxt.setText(createdBy.getFull_name());

			holder.taskNameTxt.setText(tasks.getName());
			holder.comntCountTxt.setText(String.valueOf(tasks
					.getComment_count()));
			String taskCreatedDate = tasks.getCreated_on();

			taskCreatedDate = taskCreatedDate.substring(0, 10);

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			try {
				Date date = df.parse(taskCreatedDate);
				System.out.println(date);
				taskCreatedDate = date.toString();
				System.out.println(taskCreatedDate.length());
				String dayStr = taskCreatedDate.substring(0, 3);
				taskCreatedDate = taskCreatedDate.substring(4, 10);
				System.out.println(taskCreatedDate);
				taskCreatedDate = taskCreatedDate.replaceAll("\\s+", " ");
				holder.dateTxt.setText(taskCreatedDate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int size = getCount() - 1;

			System.out.println(tasksListPo.size()
					+ "********************************");
			if (position == size) {
				holder.addTask.setVisibility(View.VISIBLE);
			}

			holder.addTask.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					addTaskLatestGlobalPosition = addTaskGlobalPosition;

					System.out.println(addTaskGlobalPosition
							+ " POS *********************");

					addTaskGrpId = tasks.getTaskgroup_id();
					final Dialog popup = new Dialog(getActivity());
					popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					popup.setContentView(R.layout.add_task);
					popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
					popup.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);

					TextView submitTxt = (TextView) popup
							.findViewById(R.id.addTaskListTxt);
					TextView cancelTxt = (TextView) popup
							.findViewById(R.id.cencelTxt);
					final EditText taskNameEdit = (EditText) popup
							.findViewById(R.id.taskName);
					final EditText dueDateEdit = (EditText) popup
							.findViewById(R.id.dueDate);
					final Spinner assignedToSpiiner = (Spinner) popup
							.findViewById(R.id.notAssiSpin);

					submitTxt.setTypeface(typefaceRoman);
					cancelTxt.setTypeface(typefaceRoman);
					taskNameEdit.setTypeface(typefaceRoman);

					CircleProgressBar pdialog1 = (CircleProgressBar) popup
							.findViewById(R.id.pDialog1);
					pdialog1.setColorSchemeResources(
							android.R.color.holo_green_light,
							android.R.color.holo_orange_light,
							android.R.color.holo_red_light);

					ArrayAdapter<String> assignedAdapter = new ArrayAdapter<String>(
							getActivity(), R.layout.spinr_txt,
							usersListEmailnames);

					assignedAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					assignedToSpiiner
							.setAdapter(new NothingSelectedSpinnerAdapter1(
									assignedAdapter,
									R.layout.assigned_spinner_nothing_selected,
									getActivity()));

					final Calendar myCalendar = Calendar.getInstance();
					final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							myCalendar.set(Calendar.YEAR, year);
							myCalendar.set(Calendar.MONTH, monthOfYear);
							myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							SimpleDateFormat monthParse = new SimpleDateFormat(
									"MM");
							SimpleDateFormat monthDisplay = new SimpleDateFormat(
									"MMMM");
							// return
							// monthDisplay.format(monthParse.parse(String.valueOf(monthOfYear)));

							try {
								addStartEventDate = year + "-" + monthOfYear
										+ "-" + dayOfMonth;
								dueDateEdit.setText(dayOfMonth
										+ " "
										+ monthDisplay.format(monthParse.parse(String
												.valueOf(monthOfYear + 1)))
										+ " " + year);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					};
					dueDateEdit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new DatePickerDialog(getActivity(), dateStart,
									myCalendar.get(Calendar.YEAR), myCalendar
											.get(Calendar.MONTH), myCalendar
											.get(Calendar.DAY_OF_MONTH)).show();
						}
					});
					assignedToSpiiner
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int pos, long arg3) {
									// TODO Auto-generated method stub
									addTaskAssigned = String
											.valueOf(assignedToSpiiner
													.getSelectedItem());
									System.out.println(pos + " : SPINNER POS ");
									System.out.println(addTaskAssigned
											+ "!!!!!!!!!!!!");
									System.out.println(usersListEmail
											+ "!!!!!!!!!!!!!!!");
									if (pos == 0) {

									} else {
										userIdForEMail = usersListEmail.get(
												pos - 1).get(addTaskAssigned);

										System.out
												.println(userIdForEMail
														+ " ==================================");
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
								}
							});
					submitTxt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isInternetPresent) {
								addTaskName = taskNameEdit.getText().toString();
								addTaskDuedate = dueDateEdit.getText()
										.toString();

								if (addTaskName == "" || addTaskName.equals("")) {
									Toast.makeText(getActivity(),
											"Enter a task name",
											Toast.LENGTH_SHORT).show();
								} else {
									popup.dismiss();
									new AddTask().execute();
								}
							} else {
								showSnack(getActivity(),
										"Please check network connection!", "OK");
							}
						}

					});

					cancelTxt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
							// 0);
							popup.dismiss();

						}
					});

					popup.show();

					// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

				}
			});

			holder.checkBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton v,
								boolean isChecked) {
							tasks.setIs_completed(isChecked);

							if (isChecked) {
								holder.taskNameTxt
										.setPaintFlags(holder.taskNameTxt
												.getPaintFlags()
												| Paint.STRIKE_THRU_TEXT_FLAG);
								taskCheckBoxstatus = "complete";
								try {

									System.out.println(hostname + "    "
											+ proId + "    "
											+ tasks.getTaskgroup_id() + "   "
											+ tasks.getId());
									taskgroup_id = tasks.getTaskgroup_id();
									task_Id = tasks.getId();

									checkBoxStatus = "true";

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								System.out.println("UN CHECKDDDDDDDDDDDDDDDD");
								holder.taskNameTxt.setPaintFlags(holder.taskNameTxt
										.getPaintFlags()
										& (~Paint.STRIKE_THRU_TEXT_FLAG));
								taskCheckBoxstatus = "reopen";
								try {

									checkBoxStatus = "false";

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							adapterTasks.notifyDataSetChanged();

							new TaskCheckUncheck().execute();
						}
					});

			holder.taskDetailLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println(tasks.getTaskgroup_id()
							+ "________________" + tasks.getTaskgroup_name()
							+ "^^^^^^^^^^^^^^^^^^^^^^");

					Intent i = new Intent(getActivity(), TaskDetail.class);
					i.putExtra("taskGrpId",
							String.valueOf(tasks.getTaskgroup_id()));
					i.putExtra("proId", proId);
					i.putExtra("taskId", String.valueOf(tasks.getId()));
					i.putExtra("taskName", tasks.getName());
					// i.putExtra("createdBy", country.getCompleted_by());
					i.putExtra("checked", checkBoxStatus);
					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
				}
			});

			holder.checkBox.setChecked(tasks.is_completed);
			return convertView;
		}

		private class ViewHolder {
			TextView taskGrpNameTxt, taskNameTxt, createdByTxt, comntCountTxt,
					dateTxt, addTask;
			CheckBox checkBox;
			RelativeLayout taskDetailLay, checkBoxView;
			ImageView comntImg;
			ListView listTasks;
		}
	}

	private class TaskGrpListModelAdapter extends ArrayAdapter<TASKGRP_MODEL> {

		private ArrayList<TASKGRP_MODEL> countryList;
		String checkBoxStatus;

		public TaskGrpListModelAdapter(Context context, int textViewResourceId,
				ArrayList<TASKGRP_MODEL> countryList) {
			super(context, textViewResourceId, countryList);
			this.countryList = new ArrayList<TASKGRP_MODEL>();
			this.countryList.addAll(countryList);
		}

		private class ViewHolder {
			TextView taskGrpNameTxt, taskNameTxt, createdByTxt, comntCountTxt,
					dateTxt;
			CheckBox checkBox;
			RelativeLayout taskDetailLay, checkBoxView;
			LinearLayout taskGrpLay;
			ImageView comntImg;
		}

		@Override
		public int getCount() {
			return countryList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			Log.v("ConvertView", String.valueOf(position));
			final TASKGRP_MODEL country = countryList.get(position);
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tasks_grp_list_row, null);

				holder = new ViewHolder();
				holder.taskGrpNameTxt = (TextView) convertView
						.findViewById(R.id.taskgrpName);
				holder.dateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);
				holder.taskNameTxt = (TextView) convertView
						.findViewById(R.id.tasksName);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);
				holder.taskGrpLay = (LinearLayout) convertView
						.findViewById(R.id.taskGrpLay);
				holder.taskDetailLay = (RelativeLayout) convertView
						.findViewById(R.id.taskDetailLay);
				holder.checkBoxView = (RelativeLayout) convertView
						.findViewById(R.id.checkBoxView);
				holder.createdByTxt = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);

				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);

				holder.taskGrpNameTxt.setTypeface(typefaceRoman);
				holder.taskNameTxt.setTypeface(typefaceRoman);
				holder.createdByTxt.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/*
			 * holder.checkBox.setOnClickListener(new View.OnClickListener() {
			 * public void onClick(View v) { CheckBox cb = (CheckBox) v;
			 * TASKGRP_MODEL country = (TASKGRP_MODEL) cb.getTag();
			 * 
			 * if (cb.isChecked() == true) {
			 * 
			 * System.out.println("CHECKEDDDDDDDDDDDDDDDDDD");
			 * holder.taskNameTxt.setPaintFlags(holder.taskNameTxt
			 * .getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); try { JSONObject
			 * jsonPatch = SigninPost .makeRequestCheck(hostname, userPref
			 * .getString("TOKEN", "NV"), country.getproId(), country
			 * .getGrpId(), country .getTaskId(), "complete"); System.out
			 * .println(jsonPatch +
			 * "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			 * 
			 * if (jsonPatch.has("completed")) { String status = jsonPatch
			 * .getString("completed");
			 * 
			 * Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();
			 * 
			 * }
			 * 
			 * final int position = listviewActivities
			 * .getPositionForView((View) v .getParent());
			 * 
			 * System.out.println("POSITION : " + position + "  LIST SIZE  " +
			 * countryList.size() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			 * checkBoxStatus = "true"; // countryList.remove(position); //
			 * dataAdapter.notifyDataSetChanged(); // new
			 * ProjectTaskGrpList().execute();
			 * 
			 * } catch (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 * 
			 * if (cb.isChecked() == false) {
			 * 
			 * System.out.println("UN CHECKDDDDDDDDDDDDDDDD");
			 * holder.taskNameTxt.setPaintFlags(holder.taskNameTxt
			 * .getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)); try {
			 * 
			 * JSONObject jsonPatch = SigninPost .makeRequestChecked(
			 * host.globalVariable(), userPref .getString("TOKEN", "NV"), proId,
			 * taskGrpId, taskId, "reopen"); System.out .println(jsonPatch +
			 * "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			 * 
			 * if (jsonPatch.has("reopened")) { String status = jsonPatch
			 * .getString("reopened"); Toast.makeText(getActivity(), "reopened",
			 * Toast.LENGTH_SHORT).show(); } checkBoxStatus = "false"; //
			 * countryList.remove(position); //
			 * dataAdapter.notifyDataSetChanged();
			 * 
			 * 
			 * finish(); startActivity(getIntent());
			 * 
			 * } catch (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 * 
			 * country.setSelected(cb.isChecked()); } });
			 */
			System.out.println("POSITION : " + position + "  LIST SIZE  "
					+ countryList.size() + " ********************************");

			holder.taskGrpLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println(country.getGrpId() + "________________"
							+ country.getTaskGrpName()
							+ "^^^^^^^^^^^^^^^^^^^^^^");

					Intent i = new Intent(getActivity(), TasksList.class);
					i.putExtra("taskGrpId", country.getGrpId());

					i.putExtra("proId", country.getproId());
					i.putExtra("taskgrpName", country.getTaskGrpName());

					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
				}
			});
			holder.taskDetailLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println(country.getGrpId() + "________________"
							+ country.getTaskGrpName()
							+ "^^^^^^^^^^^^^^^^^^^^^^");

					Intent i = new Intent(getActivity(), TaskDetail.class);
					i.putExtra("taskGrpId", country.getGrpId());
					i.putExtra("proId", country.getproId());
					i.putExtra("taskId", country.getTaskId());
					i.putExtra("taskName", country.getTaskName());
					i.putExtra("createdBy", country.getCreatedBy());
					i.putExtra("checked", checkBoxStatus);
					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
				}
			});

			String grpName = country.getTaskGrpName();
			if (grpName == "" || grpName.equals("")) {
				holder.taskGrpLay.setVisibility(View.GONE);
			} else {
				holder.taskGrpLay.setVisibility(View.VISIBLE);
			}

			if (country.getTaskName() == "EMPTY_NAME"
					|| country.getTaskName().equals("EMPTY_NAME")) {
				holder.taskNameTxt.setVisibility(View.GONE);
				holder.checkBoxView.setVisibility(View.INVISIBLE);
				holder.taskDetailLay.setClickable(false);
				holder.taskDetailLay.setEnabled(false);
			} else {
				holder.taskDetailLay.setVisibility(View.VISIBLE);
			}
			holder.createdByTxt.setText(country.getCreatedBy());
			holder.taskGrpNameTxt.setText(country.getTaskGrpName());
			holder.taskNameTxt.setText(country.getTaskName());
			holder.comntCountTxt.setText(country.getCommentCount());
			holder.checkBox.setChecked(country.isSelected());
			holder.checkBox.setTag(country);
			holder.dateTxt.setText(country.getCreatedOn());
			holder.taskGrpNameTxt.setTag(country);
			holder.taskNameTxt.setTag(country);

			return convertView;
		}
	}

	// DISCUSSIONS LIST

	public class ProjectDiscussionsList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			if (nextCallDiscusion == "" || nextCallDiscusion.equals("")) {
				pDialog.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
			} else {
				loadingBtm.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				System.out.println(nextCallDiscusion);
				jsonDiscusions = DiscussionsGet.callDiscussionsList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId,
						nextCallDiscusion);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonDiscusions);
			return null;
		}

		protected void onPostExecute(Void unused) {

			try {
				if (jsonDiscusions != null) {
					JSONArray jArray = jsonDiscusions.getJSONArray("results");
					// nextCallDiscusion = jsonDiscusions.getString("next");

					if (jArray.toString() == "[]" || jArray.length() == 0) {
						noDataTxt.setText("No discussions found!");
						noDataTxt.setVisibility(View.VISIBLE);
						pDialog.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < jArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject jsonDiscusions = jArray.getJSONObject(i);

							JSONObject createdByJson = jsonDiscusions
									.getJSONObject("created_by");
							map.put("FULLNAME",
									createdByJson.getString("full_name"));
							map.put("DISPLAYPIC",
									createdByJson.getString("image"));

							String noteTym = jsonDiscusions
									.getString("created_on");
							noteTym = timezone.convertDateToTimeZone(noteTym
									.substring(0, 18));

							map.put("CREATEDON", noteTym);

							JSONObject jsonDP = jsonDiscusions
									.getJSONObject("content_object_detail");

							if (jsonDP.toString() == "[]"
									|| jsonDP.length() == 0) {

								contentType = "delete";

							} else {
								System.err
										.println(jsonDiscusions
												.getJSONObject("content_object_detail")
												+ " ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

								if (jsonDP.has("id")) {
									contentId = jsonDP.getString("id");
								}

								if (jsonDP.has("ctype")) {
									contenttType = jsonDP.getString("ctype");
								}

								if (contenttType == "task"
										|| contenttType.equals("task")) {

									String taskgroup_id = jsonDP
											.getString("taskgroup_id");
									map.put("TASKGRPID", taskgroup_id);
								}
								if (contenttType == "taskgroup"
										|| contenttType.equals("taskgroup")) {

									/*
									 * String taskgroup_id = jsonDP
									 * .getString("taskgroup_id");
									 */
									map.put("TASKGRPID", jsonDP.getString("id"));
									map.put("display_name",
											jsonDP.getString("display_name"));
								}

								else if (contenttType == "file"
										|| contenttType.equals("file")) {
									map.put("detailurl",
											jsonDP.getString("url"));
									map.put("ID", jsonDP.getString("id"));
									map.put("fileTitle",
											jsonDP.getString("display_name"));
								} else if (contenttType == "message"
										|| contenttType.equals("message")
										|| contenttType == "messages"
										|| contenttType.equals("messages")) {
									map.put("detailurl",
											jsonDP.getString("url"));
									map.put("ID", jsonDP.getString("id"));
									map.put("display_name",
											jsonDP.getString("display_name"));

								}
								displayName = jsonDP.getString("display_name");
							}

							map.put("PROID", proId);
							commentDetail = jsonDiscusions
									.getString("comment_detail");
							map.put("DPNAME", displayName);
							map.put("COMNTDETAIL", commentDetail);
							map.put("CONTENTID", contentId);
							map.put("CONTENTTYPE", contenttType);
							discussionList.add(map);
						}
						listviewActivities.setPadding(0, 10, 0, 0);
						currentPositionDiscusion = listviewActivities
								.getLastVisiblePosition();

						if (getActivity() == null
								|| getActivity().isFinishing() == true) {

						} else {
							adapterDisussions = new ListDiscussionsBaseAdapter(
									getActivity(), discussionList);
							listviewActivities.setAdapter(adapterDisussions);
						}

						pDialog.setVisibility(View.GONE);
						loadingBtm.setVisibility(View.GONE);
						if (checkRefreshDiscusions != ""
								|| !checkRefreshDiscusions.equals("")) {
							DisplayMetrics displayMetrics = getResources()
									.getDisplayMetrics();
							int height = displayMetrics.heightPixels;
							System.out
									.println(height
											+ "-------------ACTION BAR HEIGHT--------------");
							listviewActivities.setSelectionFromTop(
									currentPositionDiscusion + 1, height - 220);
						}
					}
				} else {
					pDialog.setVisibility(View.GONE);
					showSnack(getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
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
								nextCallDiscusion = jsonDiscusions
										.getString("next");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (nextCallDiscusion == null
									|| nextCallDiscusion == ""
									|| nextCallDiscusion.equals("null")
									|| nextCallDiscusion.equals("")) {
								/*Toast.makeText(getActivity(),
										"There is no more discussions!",
										Toast.LENGTH_LONG).show();*/
								showSnack(getActivity(),
										"There is no more discussions!",
										"OK");
								
							} else {
								System.out.println("API IS CALLING AGAIN"
										+ "_____  " + nextCallDiscusion);

								checkRefreshDiscusions = "NEXT";

								new ProjectDiscussionsList().execute();

							}
						}
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pDialog.setVisibility(View.GONE);
				loadingBtm.setVisibility(View.GONE);
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListDiscussionsBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;
		PrettyTime prettyTime = new PrettyTime();;

		public ListDiscussionsBaseAdapter(FragmentActivity fragmentActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = fragmentActivity;
			this.listt = listArticles;
			inflator = (LayoutInflater) fragmentActivity
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
				convertView = inflator.inflate(R.layout.discussions_list, null);
				holder = new ViewHolder();
				holder.timeTxt = (TextView) convertView
						.findViewById(R.id.timeTxt);
				holder.comntDetailTxt = (TextView) convertView
						.findViewById(R.id.contentDetailTxt);
				holder.createdNameTxt = (TextView) convertView
						.findViewById(R.id.createdName);
				holder.imageUser = (ImageView) convertView
						.findViewById(R.id.imageUser);

				holder.createdNameTxt.setTypeface(typefaceRoman);
				holder.comntDetailTxt.setTypeface(typefaceRoman);
				holder.timeTxt.setTypeface(typefaceRoman);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			/*
			 * holder.dpNameTxt.setTag(position);
			 * holder.comntDetailTxt.setTag(position);
			 */
			holder.createdNameTxt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listt.get(position).get("CONTENTTYPE") == "taskgroup"
							|| listt.get(position).get("CONTENTTYPE")
									.equals("taskgroup")) {
						Intent i = new Intent(getActivity(), TasksList.class);
						i.putExtra("proId", listt.get(position).get("PROID"));
						i.putExtra("taskGrpId",
								listt.get(position).get("TASKGRPID"));
						i.putExtra("taskgrpName",
								listt.get(position).get("display_name"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("CONTENTTYPE") == "message"
							|| listt.get(position).get("CONTENTTYPE")
									.equals("message")) {
						Intent i = new Intent(getActivity(),
								MessageDetail.class);
						i.putExtra("TITLE",
								listt.get(position).get("display_name"));
						i.putExtra("PROID", listt.get(position).get("PROID"));
						i.putExtra("ID", listt.get(position).get("CONTENTID"));
						/*
						 * i.putExtra("contentType",
						 * listt.get(position).get("CONTENTTYPE"));
						 */

						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("CONTENTTYPE") == "task"
							|| listt.get(position).get("CONTENTTYPE")
									.equals("task")) {
						Intent i = new Intent(getActivity(), TaskDetail.class);
						i.putExtra("taskName", listt.get(position)
								.get("DPNAME"));
						i.putExtra("proId", listt.get(position).get("PROID"));
						i.putExtra("taskId",
								listt.get(position).get("CONTENTID"));
						i.putExtra("taskGrpId",
								listt.get(position).get("TASKGRPID"));

						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("CONTENTTYPE") == "note"
							|| listt.get(position).get("CONTENTTYPE")
									.equals("note")) {
						Intent i = new Intent(getActivity(), NotesDetail.class);
						i.putExtra("proId", listt.get(position).get("PROID"));
						i.putExtra("notesId",
								listt.get(position).get("CONTENTID"));

						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					} else if (listt.get(position).get("CONTENTTYPE") == "file"
							|| listt.get(position).get("CONTENTTYPE")
									.equals("file")
							|| listt.get(position).get("CONTENTTYPE") == "files"
							|| listt.get(position).get("CONTENTTYPE")
									.equals("files")) {
						if (listt.get(position).get("detailurl")
								.contains("?ids")) {
							System.out
									.println("MULTIPLE FILES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							Intent i = new Intent(getActivity(),
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
							Intent i = new Intent(getActivity(),
									FileDetail.class);

							System.out.println(listt.get(position).get(
									"fileUrl")
									+ listt.get(position).get("proId")
									+ listt.get(position).get("ID"));

							i.putExtra("fileUrl",
									listt.get(position).get("detailurl"));
							i.putExtra("proId", listt.get(position)
									.get("PROID"));
							i.putExtra("fileId", listt.get(position).get("ID"));
							i.putExtra("title",
									listt.get(position).get("fileTitle"));
							startActivity(i);
						}

						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					}

				}
			});

			String time = listt.get(position).get("CREATEDON");

			time = time.replace("T", " ");

			time = time.substring(0, 19);
			long timee = timeStringtoMilis(time);

			// holder.dateContentTypeTxt.setText(prettyTime.format(new
			// Date(timee)));
			holder.createdNameTxt.setText(Html.fromHtml(listt.get(position)
					.get("FULLNAME")
					+ " "
					+ "<font color=#1E50A3>"
					+ "<small>"
					+ listt.get(position).get("DPNAME")
					+ "</small>"
					+ "</font>"));

			imageLoader.displayImage(listt.get(position).get("DISPLAYPIC"),
					holder.imageUser, options);
			/* holder.dpNameTxt.setText(listt.get(position).get("DPNAME")); */
			String comntDetail = listt.get(position).get("COMNTDETAIL");
			comntDetail = comntDetail.replaceAll("<br/>", "");
			comntDetail = comntDetail.replaceAll("<p>", "");
			comntDetail = comntDetail.replaceAll("</p>", "");
			System.out.println(comntDetail + "_______________________________");

			/*
			 * System.out.println(listt.get(position)
			 * .get("COMNTDETAIL").replaceAll("(<br/>)+$",
			 * "")+"______!!!!!!!!!!!!!!!!!!!!!!_________________________");
			 */

			holder.comntDetailTxt.setText(Html.fromHtml(comntDetail));
			holder.timeTxt.setText(prettyTime.format(new Date(timee)));

			return convertView;
		}

		public class ViewHolder {
			public TextView createdNameTxt, comntDetailTxt, timeTxt;
			ImageView imageUser;
		}

	}

	// FILES LIST
	public class ProjectFilesList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			if (nextCallFiles == "" || nextCallFiles.equals("")) {
				pDialog.setVisibility(View.VISIBLE);
				loadingBtm.setVisibility(View.GONE);
			} else {
				loadingBtm.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				System.out.println(nextCallFiles);
				jsonFiles = FilesGet
						.callFilesList(host.globalVariable(),
								userPref.getString("TOKEN", "NV"), proId,
								nextCallFiles);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonFiles);
			return null;
		}

		protected void onPostExecute(Void unused) {

			try {
				if (jsonFiles != null) {
					JSONArray jArray = jsonFiles.getJSONArray("results");
					// nextCallDiscusion = jsonDiscusions.getString("next");

					if (jArray.toString() == "[]" || jArray.length() == 0) {

						noDataTxt.setText("No files found!");
						noDataTxt.setVisibility(View.VISIBLE);
						pDialog.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < jArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject jsonFiles = jArray.getJSONObject(i);

							String fileCreatedDate = jsonFiles
									.getString("created_on");
							JSONObject jsonCreatedBy = jsonFiles
									.getJSONObject("created_by");

							map.put("PROID", proId);
							map.put("FILEID", jsonFiles.getString("id"));
							map.put("TITLE", jsonFiles.getString("title"));
							map.put("resource_url",
									jsonFiles.getString("resource_url"));
							map.put("COMENT_COUNT",
									jsonFiles.getString("comment_count"));
							map.put("CREATEDBY",
									jsonCreatedBy.getString("full_name"));
							map.put("THUMBNAIL",
									jsonFiles.getString("thumbnail"));
							map.put("SIZE", jsonFiles.getString("size"));
							map.put("DRIVEBOX",
									jsonFiles.getString("is_drivebox"));
							System.out
									.println(jsonFiles
											.getBoolean("is_drivebox")
											+ " >........................................");

							fileCreatedDate = fileCreatedDate.substring(0, 10);

							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date;
							try {
								date = df.parse(fileCreatedDate);
								System.out.println(date);
								fileCreatedDate = date.toString();
								System.out.println(fileCreatedDate.length());
								dayStr = fileCreatedDate.substring(0, 3);
								fileCreatedDate = fileCreatedDate.substring(4,
										10);
								System.out.println(fileCreatedDate);
								fileCreatedDate.replaceAll("\\s+", " ");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							map.put("CREATEDON", dayStr + ", "
									+ fileCreatedDate);

							map.put("EXTENSION", jsonFiles.getString("ext"));

							filesList.add(map);
						}

						currentPositionFiles = listviewActivities
								.getLastVisiblePosition();

						if (getActivity() == null
								|| getActivity().isFinishing() == true) {

						} else {
							adapterFiles = new ListFilesBaseAdapter(
									getActivity(), filesList);
							listviewActivities.setAdapter(adapterFiles);
						}

						pDialog.setVisibility(View.GONE);
						loadingBtm.setVisibility(View.GONE);

						if (checkRefreshFiles != ""
								|| !checkRefreshFiles.equals("")) {
							DisplayMetrics displayMetrics = getResources()
									.getDisplayMetrics();
							int height = displayMetrics.heightPixels;
							System.out
									.println(height
											+ "-------------ACTION BAR HEIGHT--------------");
							listviewActivities.setSelectionFromTop(
									currentPositionFiles + 1, height - 220);
						}

					}

				} else {
					pDialog.setVisibility(View.GONE);
					loadingBtm.setVisibility(View.GONE);
					/*Toast.makeText(getActivity(),
							"Something went wrong! Check server",
							Toast.LENGTH_SHORT).show();*/
					showSnack(getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
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
								nextCallFiles = jsonFiles.getString("next");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (nextCallFiles == null || nextCallFiles == ""
									|| nextCallFiles.equals("null")
									|| nextCallFiles.equals("")) {
								/*Toast.makeText(getActivity(),
										"There is no more files!",
										Toast.LENGTH_LONG).show();*/
								showSnack(getActivity(),
										"There is no more files!",
										"OK");
							} else {
								System.out.println("API IS CALLING AGAIN"
										+ "_____  " + nextCallFiles);
								try {
									nextCallFiles = jsonFiles.getString("next");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								checkRefreshFiles = "NEXT";

								new ProjectFilesList().execute();

							}
						}
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListFilesBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListFilesBaseAdapter(FragmentActivity fragmentActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = fragmentActivity;
			this.listt = listArticles;
			inflator = (LayoutInflater) fragmentActivity
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
				convertView = inflator.inflate(R.layout.files_list_row, null);
				holder = new ViewHolder();
				holder.fileTitleTxt = (TextView) convertView
						.findViewById(R.id.fileTitle);
				holder.fileDateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);
				holder.createdBy = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.fileImg = (ImageView) convertView
						.findViewById(R.id.fileImg);
				holder.fileDateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.byTxt = (TextView) convertView.findViewById(R.id.byTxt);
				holder.fileSizeTxt = (TextView) convertView
						.findViewById(R.id.fileSize);

				holder.filesLay = (RelativeLayout) convertView
						.findViewById(R.id.filesLay);
				// holder.fileImg=(ImageView)convertView.findViewById(R.id.notesImg);
				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);

				holder.fileTitleTxt.setTypeface(typefaceRoman);
				holder.createdBy.setTypeface(typefaceRoman);
				holder.fileDateTxt.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);
				holder.byTxt.setTypeface(typefaceRoman);
				holder.fileSizeTxt.setTypeface(typefaceRoman);
				/*
				 * holder.fileImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				 * SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
				 * R.drawable.note); Drawable draNotes =
				 * svgNotes.createPictureDrawable();
				 * holder.fileImg.setImageDrawable(draNotes);
				 */

				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.filesLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out
							.println(listt.get(position).get("EXTENSION")
									+ "       >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					/*
					 * if (listt.get(position).get("EXTENSION") == "jpg" ||
					 * listt.get(position).get("EXTENSION") .equals("jpg") ||
					 * listt.get(position).get("EXTENSION") == "PNG" ||
					 * listt.get(position).get("EXTENSION") .equals("PNG") ||
					 * listt.get(position).get("EXTENSION") == "png" ||
					 * listt.get(position).get("EXTENSION") .equals("png") ||
					 * listt.get(position).get("EXTENSION") == "ico" ||
					 * listt.get(position).get("EXTENSION") .equals("ico")) {
					 */
					Intent i = new Intent(getActivity(), FileDetail.class);
					i.putExtra("fileUrl",
							listt.get(position).get("resource_url"));
					i.putExtra("proId", listt.get(position).get("PROID"));
					i.putExtra("fileId", listt.get(position).get("FILEID"));
					i.putExtra("title", listt.get(position).get("TITLE"));
					i.putExtra("ext", listt.get(position).get("EXTENSION"));
					i.putExtra("FROM_TABS", "FROM_TABS");
					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
					/*
					 * } else { Intent intent = new Intent(Intent.ACTION_VIEW,
					 * Uri .parse(listt.get(position).get( "ATTACHEMENT")));
					 * startActivity(intent); }
					 */
				}
			});
			holder.fileTitleTxt.setTag(position);
			holder.fileDateTxt.setTag(position);
			System.out.println(listt.get(position).get("THUMBNAIL"));
			holder.fileImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			/*
			 * if (listt.get(position).get("THUMBNAIL") == null ||
			 * listt.get(position).get("THUMBNAIL").equals("null") ||
			 * listt.get(position).get("THUMBNAIL") == "null" ||
			 * listt.get(position).get("THUMBNAIL").equals(null)) {
			 */

			if (listt.get(position).get("EXTENSION") == "doc"
					|| listt.get(position).get("EXTENSION").equals("doc")) {

				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_docs);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);

			} else if (listt.get(position).get("EXTENSION") == "pdf"
					|| listt.get(position).get("EXTENSION").equals("pdf")) {

				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_pdf);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);

			} else if (listt.get(position).get("EXTENSION") == "jpg"
					|| listt.get(position).get("EXTENSION").equals("jpg")
					|| listt.get(position).get("EXTENSION") == "jpeg"
					|| listt.get(position).get("EXTENSION").equals("jpeg")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_jpg);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);

			} else if (listt.get(position).get("EXTENSION") == "PNG"
					|| listt.get(position).get("EXTENSION").equals("PNG")
					|| listt.get(position).get("EXTENSION") == "png"
					|| listt.get(position).get("EXTENSION").equals("png")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_png);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);
			} else if (listt.get(position).get("EXTENSION") == "ico"
					|| listt.get(position).get("EXTENSION").equals("ico")) {
				imageLoader
						.displayImage(
								"https://cdn3.iconfinder.com/data/icons/graphic-files/512/ICO-512.png",
								holder.fileImg, options);
			} else if (listt.get(position).get("EXTENSION") == "txt"
					|| listt.get(position).get("EXTENSION").equals("txt")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_txt);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);

			} else if (listt.get(position).get("EXTENSION") == "csv"
					|| listt.get(position).get("EXTENSION").equals("csv")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_csv);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);

			} else if (listt.get(position).get("EXTENSION") == "xlsx"
					|| listt.get(position).get("EXTENSION").equals("xlsx")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_xlsx);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);
			} else if (listt.get(position).get("EXTENSION") == "3gp"
					|| listt.get(position).get("EXTENSION").equals("3gp")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.ico_thre_gp);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);
			}
			if (listt.get(position).get("EXTENSION") == "mp4"
					|| listt.get(position).get("EXTENSION").equals("mp4")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.ico_mp4);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);
			} else if (listt.get(position).get("EXTENSION") == "mp3"
					|| listt.get(position).get("EXTENSION").equals("mp3")) {
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_mp3);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.fileImg.setImageDrawable(draNotes);
				/*
				 * imageLoader .displayImage(
				 * "https://cdn1.iconfinder.com/data/icons/files-3/24/File-TXT-512.png"
				 * , holder.fileImg, options);
				 */
			}
			/*
			 * else {
			 * imageLoader.displayImage(listt.get(position).get("THUMBNAIL"),
			 * holder.fileImg, options); }
			 */

			// holder.fileTitleTxt.setText(listt.get(position).get("TITLE"));
			holder.fileTitleTxt.setText(Html.fromHtml(listt.get(position).get(
					"TITLE")
					+ " "
					+ "<font color=#c0c0c0>"
					+ "<small>"
					+ listt.get(position).get("SIZE")
					+ " KB"
					+ "</small>"
					+ "</font>"));
			// holder.fileSizeTxt.setText(listt.get(position).get("SIZE") +
			// " KB");
			holder.createdBy.setText(listt.get(position).get("CREATEDBY"));
			holder.fileDateTxt.setText(Html.fromHtml(listt.get(position).get(
					"CREATEDON")));
			holder.comntCountTxt.setText(listt.get(position)
					.get("COMENT_COUNT"));
			return convertView;
		}

		public class ViewHolder {
			public TextView fileTitleTxt, fileDateTxt, createdBy,
					comntCountTxt, byTxt, fileSizeTxt;
			ImageView fileImg, comntImg;
			RelativeLayout filesLay;
		}

	}

	// ProjectNotesList LIST

	public class ProjectNotesList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				System.out.println(nextCallNotes);
				notesJarr = NotesGet
						.callNotesList(host.globalVariable(),
								userPref.getString("TOKEN", "NV"), proId,
								nextCallNotes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonDiscusions);
			return null;
		}

		protected void onPostExecute(Void unused) {

			try {
				if (notesJarr != null) {

					if (notesJarr.toString() == "[]" || notesJarr.length() == 0) {
						/*
						 * Toast.makeText(getActivity(), "No notes found!",
						 * Toast.LENGTH_SHORT).show();
						 */
						noDataTxt.setText("No notes found!");
						noDataTxt.setVisibility(View.VISIBLE);

						pDialog.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < notesJarr.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject jsonNotes = notesJarr.getJSONObject(i);

							JSONObject jsonCreatedBy = jsonNotes
									.getJSONObject("created_by");

							String createdOn = jsonNotes
									.getString("created_on");

							createdOn = createdOn.substring(0, 10);

							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date;
							try {
								date = df.parse(createdOn);
								System.out.println(date);
								createdOn = date.toString();
								System.out.println(createdOn.length());
								dayStr = createdOn.substring(0, 3);
								createdOn = createdOn.substring(4, 10);
								System.out.println(createdOn);
								createdOn.replaceAll("\\s+", " ");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							map.put("PROID", proId);
							map.put("NOTES_ID", jsonNotes.getString("id"));
							map.put("TITLE", jsonNotes.getString("name"));
							map.put("CONTENT", jsonNotes.getString("content"));
							map.put("COMENT_COUNT",
									jsonNotes.getString("comment_count"));
							map.put("CREATEDON", dayStr + "," + createdOn);
							map.put("CREATEDBY",
									jsonCreatedBy.getString("full_name"));

							notesList.add(map);
						}

						currentPositionNotes = listviewActivities
								.getLastVisiblePosition();
						if (notesList == null || notesList.toString() == "null") {

						} else {
							if (getActivity() == null
									|| getActivity().isFinishing() == true) {

							} else {
								adapterNotes = new ListNotesBaseAdapter(
										getActivity(), notesList);
								listviewActivities.setAdapter(adapterNotes);
							}
						}

						pDialog.setVisibility(View.GONE);
						loadingBtm.setVisibility(View.GONE);
						/*
						 * DisplayMetrics displayMetrics = getResources()
						 * .getDisplayMetrics(); int height =
						 * displayMetrics.heightPixels;
						 * 
						 * listviewActivities.setSelectionFromTop(
						 * currentPositionNotes + 1, height - 220);
						 */
					}

				} else {
					pDialog.setVisibility(View.GONE);
					loadingBtm.setVisibility(View.GONE);
					showSnack(getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListNotesBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;
		Typeface typefaceRoman;

		public ListNotesBaseAdapter(FragmentActivity fragmentActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = fragmentActivity;
			this.listt = listArticles;
			inflator = (LayoutInflater) fragmentActivity
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
				convertView = inflator.inflate(R.layout.notes_list_row, null);
				holder = new ViewHolder();
				holder.titleTxt = (TextView) convertView
						.findViewById(R.id.titleTxt);
				holder.createdBy = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.createdDate = (TextView) convertView
						.findViewById(R.id.dateTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.byTxt = (TextView) convertView.findViewById(R.id.byTxt);
				holder.notesLay = (RelativeLayout) convertView
						.findViewById(R.id.notesLay);
				holder.notesImg = (ImageView) convertView
						.findViewById(R.id.notesImg);
				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);

				typefaceRoman = Typeface.createFromAsset(getActivity()
						.getAssets(), "fonts/HelveticaNeueLTStd_Roman.ttf");
				holder.titleTxt.setTypeface(typefaceRoman);
				holder.createdBy.setTypeface(typefaceRoman);
				holder.createdDate.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);
				holder.byTxt.setTypeface(typefaceRoman);

				holder.notesImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.note);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.notesImg.setImageDrawable(draNotes);

				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.notesLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(listt.get(position).get("NOTES_ID")
							+ " *****************");
					Intent i = new Intent(getActivity(), NotesDetail.class);
					i.putExtra("notesId", listt.get(position).get("NOTES_ID"));
					i.putExtra("proId", listt.get(position).get("PROID"));
					i.putExtra("FROM_TABS", "FROM_TABS");
					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
				}
			});
			holder.titleTxt.setText(listt.get(position).get("TITLE"));
			holder.createdDate.setText(listt.get(position).get("CREATEDON"));
			holder.createdBy.setText(listt.get(position).get("CREATEDBY"));
			holder.comntCountTxt.setText(listt.get(position)
					.get("COMENT_COUNT"));
			return convertView;
		}

		public class ViewHolder {
			public TextView titleTxt, byTxt, createdBy, createdDate,
					comntCountTxt;
			public RelativeLayout notesLay;
			ImageView notesImg, comntImg;
		}

	}

	// TIME LOG

	public class TimeLogList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pDialog.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonTimeLog = TimeLogGet.callTimeLogList(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId,
						nextCallTimeLog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonTimeLog);
			return null;
		}

		protected void onPostExecute(Void unused) {

			try {
				if (jsonTimeLog != null) {
					JSONArray jArray = jsonTimeLog.getJSONArray("results");

					if (jArray.toString() == "[]" || jArray.length() == 0) {
						pDialog.setVisibility(View.GONE);
						noDataTxt.setText("No Time Log found!");
						noDataTxt.setVisibility(View.VISIBLE);

						/*Toast.makeText(getActivity(), "No Time Log found!",
								Toast.LENGTH_SHORT).show();*/
						showSnack(getActivity(),
								"No Time Log found!",
								"OK");
					} else {
						for (int i = 0; i < jArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							timeLogJson = jArray.getJSONObject(i);
							timeLogMonthStr = timeLogJson
									.getString("log_month");
							String taskName, taskIdd;
							if (timeLogJson.isNull("task_object")) {
								taskName = "";
								String disc = timeLogJson
										.getString("description");
								map.put("disc", disc);
								map.put("TASKID", "");
							} else {
								JSONObject jsonTask = timeLogJson
										.getJSONObject("task_object");
								taskName = jsonTask.getString("name");
								taskIdd = jsonTask.getString("id");
								map.put("TASKID", taskIdd);

							}
							JSONObject createdByJson = timeLogJson
									.getJSONObject("created_by");
							String fullName = createdByJson
									.getString("full_name");
							String timeLog = timeLogJson.getString("logdate");
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date;
							try {
								date = df.parse(timeLog);
								System.out.println(date);
								timeLog = date.toString();
								System.out.println(timeLog.length());
								dayStr = timeLog.substring(0, 3);
								timeLog = timeLog.substring(4, 10);
								System.out.println(timeLog);
								timeLog.replaceAll("\\s+", " ");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (globalTimeDateForTimeLog == null
									|| !globalTimeDateForTimeLog
											.equals(timeLog)) {
								globalTimeDateForTimeLog = timeLog;
								map.put("LOGMONTH", globalTimeDateForTimeLog);
							} else {
								map.put("LOGMONTH", "");
							}

							map.put("DATELOG", timeLog);
							map.put("DAY", dayStr);
							map.put("TASKNAME", taskName);
							map.put("FULLNAME", fullName);
							map.put("TIMELOG", timeLogJson.getString("logtime"));
							map.put("PROID", timeLogJson.getString("project"));
							timeLogList.add(map);
							System.out.println(map);
						}
						currentPositionTimeLog = listviewActivities
								.getLastVisiblePosition();
						adapterTimeLog = new ListTimeLogAdapter(getActivity(),
								timeLogList);
						listviewActivities.setAdapter(adapterTimeLog);
						pDialog.setVisibility(View.GONE);
						DisplayMetrics displayMetrics = getResources()
								.getDisplayMetrics();
						int height = displayMetrics.heightPixels;
						System.out
								.println(height
										+ "-------------ACTION BAR HEIGHT--------------");
						listviewActivities.setSelectionFromTop(
								currentPositionTimeLog + 1, height - 220);
					}

				} else {
					pDialog.setVisibility(View.GONE);
					showSnack(getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
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
							if (nextCallTimeLog == null
									|| nextCallTimeLog == ""
									|| nextCallTimeLog.equals("null")
									|| nextCallTimeLog.equals("")) {
						/*		Toast.makeText(getActivity(),
										"There is no more logs!",
										Toast.LENGTH_LONG).show();*/
								showSnack(getActivity(),
										"There is no more logs!",
										"OK");
							} else {
								System.out.println("API IS CALLING AGAIN"
										+ "_____  " + nextCallTimeLog);
								try {
									nextCallTimeLog = jsonProActivities
											.getString("next");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// hostname = nextCall;
								// nextCall = "NEXT";

								new TimeLogList().execute();

							}
						}
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListTimeLogAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListTimeLogAdapter(Context projectActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = projectActivity;
			this.listt = listArticles;
			inflator = (LayoutInflater) projectActivity
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
		public View getView(final int positionn, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflator
						.inflate(R.layout.time_log_list_row, null);
				holder = new ViewHolder();
				holder.dateMonth = (TextView) convertView
						.findViewById(R.id.dateMonth);
				holder.logTime = (TextView) convertView
						.findViewById(R.id.logDate);
				holder.taskNameTxt = (TextView) convertView
						.findViewById(R.id.taskNameTxt);
				holder.createdNameTxt = (TextView) convertView
						.findViewById(R.id.createdName);
				holder.dateLay = (RelativeLayout) convertView
						.findViewById(R.id.dateLay);
				holder.timeTxt = (TextView) convertView
						.findViewById(R.id.logTime);

				holder.layClick = (LinearLayout) convertView
						.findViewById(R.id.layClick);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.layClick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listt.get(positionn).get("TASKID") == ""
							|| listt.get(positionn).get("TASKID").equals("")) {

						System.out.println("TASKID : "
								+ listt.get(positionn).get("TASKID")
								+ "  ***********************" + positionn);
					} else {
						System.out.println("TASKID : "
								+ listt.get(positionn).get("TASKID")
								+ "  ***********************" + positionn);
						Intent i = new Intent(getActivity(),
								TimeLogDetail.class);
						i.putExtra("taskId", listt.get(positionn).get("TASKID"));
						i.putExtra("proId", listt.get(positionn).get("PROID"));
						startActivity(i);
						getActivity().overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_left);
					}
				}
			});

			holder.dateMonth.setTag(positionn);
			holder.taskNameTxt.setTag(positionn);
			holder.createdNameTxt.setTag(positionn);
			holder.layClick.setTag(positionn);
			holder.logTime.setTag(positionn);
			holder.dateLay.setTag(positionn);

			if (listt.get(positionn).get("LOGMONTH") == ""
					|| listt.get(positionn).get("LOGMONTH").equals("")) {
				holder.dateLay.setVisibility(View.GONE);
			} else {
				holder.dateLay.setVisibility(View.VISIBLE);
			}
			if (listt.get(positionn).get("TASKNAME") == ""
					|| listt.get(positionn).get("TASKNAME").equals("")) {
				holder.taskNameTxt.setText(listt.get(position).get("disc"));
			} else {
				holder.taskNameTxt.setText("TASK : "
						+ listt.get(positionn).get("TASKNAME"));
			}

			holder.dateMonth.setText(listt.get(positionn).get("LOGMONTH"));
			holder.logTime.setText(listt.get(positionn).get("DAY") + ","
					+ listt.get(positionn).get("DATELOG"));
			holder.timeTxt.setText(listt.get(positionn).get("TIMELOG"));
			holder.createdNameTxt.setText(listt.get(positionn).get("FULLNAME"));
			return convertView;
		}

		public class ViewHolder {
			public TextView dateMonth, logTime, taskNameTxt, createdNameTxt,
					timeTxt;
			RelativeLayout dateLay;
			LinearLayout layClick;
		}
	}

	// EVENTS List

	public class EventsList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				eventsJarray = EventsGet.callEventsList(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, startDate,
						endDate);
				/*
				 * mileStonesArray = EventsGet.callMileStonessList(
				 * host.globalVariable(), userPref.getString("TOKEN", "NV"),
				 * proId, startDate, endDate);
				 */

				System.out.println(eventsJarray);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (eventsJarray != null) {

				if (eventsJarray.toString() == "[]"
						|| eventsJarray.length() == 0) {
					pDialog.setVisibility(View.GONE);
					/*
					 * Toast.makeText(getActivity(), "No events found!",
					 * Toast.LENGTH_SHORT).show();
					 */
					noDataTxt.setText("No events found!");
					noDataTxt.setVisibility(View.VISIBLE);
					listviewActivities.setAdapter(null);
				} else {
					noDataTxt.setVisibility(View.GONE);
					for (int i = 0; i < eventsJarray.length(); i++) {
						HashMap<String, String> eventsMap = new HashMap<String, String>();
						try {
							JSONObject jsonEvent = eventsJarray
									.getJSONObject(i);
							eventId = jsonEvent.getString("event");
							eventTitle = jsonEvent.getString("title");
							eventStrtDate = jsonEvent.getString("start_dt");
							String timeDate = eventStrtDate;
							timeDate = timezone.convertDateToTimeZone(timeDate);

							timeDate = timeDate.substring(0, 10);

							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date;
							try {
								date = df.parse(timeDate);
								System.out.println(date);
								timeDate = date.toString();
								System.out.println(timeDate.length());
								dayStr = timeDate.substring(0, 3);
								System.out.println(dayStr);
								timeDate = timeDate.substring(4, 10);
								System.out.println(timeDate);
								timeDate.replaceAll("\\s+", " ");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String startTym = jsonEvent.getString("start_time");
							startTym = timezone.convertTimeToUtcZone(startTym);

							String endTym = jsonEvent.getString("end_time");
							endTym = timezone.convertTimeToUtcZone(endTym);

							eventsMap.put("ID", eventId);
							eventsMap.put("TITLE", eventTitle);
							eventsMap.put("DATE", timeDate);
							eventsMap.put("DAY", dayStr);
							eventsMap.put("STARTDATE", eventStrtDate);
							eventsMap.put("STARTTIME", startTym);
							eventsMap.put("ENDTIME", endTym);
							eventsMap.put("DISC",
									jsonEvent.getString("description"));

							System.out.println(jsonEvent.getString("start")
									+ "    " + jsonEvent.getString("end"));
							eventsMap.put("startD",
									jsonEvent.getString("start"));
							eventsMap.put("endD", jsonEvent.getString("end"));
							System.out.println(eventsMap);

							eventsList.add(eventsMap);
							// eventsList.add(eventsMap);
							System.out.println(eventsList);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println(eventsList);

					Collections.sort(eventsList,
							new Comparator<HashMap<String, String>>() {
								public int compare(
										HashMap<String, String> mapping1,
										HashMap<String, String> mapping2) {
									return mapping1.get("DATE").compareTo(
											mapping2.get("DATE"));
								}
							});
					System.out.println(eventsList + "^^^^^^^^^^^^^^^^^^^^^^^^");
					for (int j = 0; j < eventsList.size(); j++) {
						HashMap<String, String> eventsMap = new HashMap<String, String>();
						// String date=eventsList.get(i).get("DATE");
						eventsMap.put("ID", eventsList.get(j).get("ID"));
						eventsMap.put("TITLE", eventsList.get(j).get("TITLE"));
						eventsMap.put("STARTDATE",
								eventsList.get(j).get("STARTDATE"));
						eventsMap.put("DAY", eventsList.get(j).get("DAY"));
						eventsMap.put("DISC", eventsList.get(j).get("DISC"));
						eventsMap.put("PROID", proId);
						eventsMap.put("STARTTIME",
								eventsList.get(j).get("STARTTIME"));
						eventsMap.put("ENDTIME",
								eventsList.get(j).get("ENDTIME"));
						eventsMap
								.put("startD", eventsList.get(j).get("startD"));
						eventsMap.put("endD", eventsList.get(j).get("endD"));

						if (globalDateEvents == ""
								|| globalDateEvents.equals("")
								|| !globalDateEvents.equals(eventsList.get(j)
										.get("DATE"))) {
							globalDateEvents = eventsList.get(j).get("DATE");
							System.out.println();
							eventsMap.put("DATECHECK", globalDateEvents);

							// dateTxt.setVisibility(View.VISIBLE);
							// dateTxt.setText(globalDateEvents);
							System.out.println(eventsList
									+ "^^^^^^^^^^^^^^^^^^^^^^^^");
						} else {
							// dateTxt.setVisibility(View.GONE);
							eventsMap.put("DATECHECK", "");
							System.out.println(eventsList
									+ "^^^^^^^^^^^^^^^^^^^^^^^^");
						}
						eventOwnList.add(eventsMap);
					}

					System.out.println(eventOwnList);
					if (getActivity() == null
							|| getActivity().isFinishing() == true) {

					} else {
						adapterEvents = new ListEventsMilesAdapter(
								getActivity(), eventOwnList);
						listviewActivities.setAdapter(adapterEvents);
					}

					listviewActivities.setDivider(null);
					pDialog.setVisibility(View.GONE);

				}

			}

			/*
			 * System.out.println(eventsList);
			 * 
			 * if (mileStonesArray != null) {
			 * 
			 * if (mileStonesArray.toString() == "[]" ||
			 * mileStonesArray.length() == 0) {
			 * 
			 * } else {
			 * 
			 * for (int i = 0; i < mileStonesArray.length(); i++) {
			 * HashMap<String, String> eventsMap = new HashMap<String,
			 * String>(); try { JSONObject jsonMilestone = mileStonesArray
			 * .getJSONObject(i);
			 * 
			 * mileStoneId = jsonMilestone.getString("id"); mileStoneTitle =
			 * jsonMilestone.getString("title"); String mileStoneDate =
			 * jsonMilestone .getString("start"); eventsMap.put("ID",
			 * mileStoneId); eventsMap.put("TITLE", "MILESTONE : " +
			 * mileStoneTitle); eventsMap.put("DATE", mileStoneDate);
			 * eventsMap.put("STARTDATE", ""); eventsList.add(eventsMap);
			 * System.out.println(eventsList);
			 * 
			 * } catch (JSONException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 * 
			 * }
			 * 
			 * }
			 */
		}

	}

	@SuppressLint("NewApi")
	public class ListEventsMilesAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListEventsMilesAdapter(Context projectActivity,
				ArrayList<HashMap<String, String>> listArticles) {
			// TODO Auto-generated constructor stub
			this.context = projectActivity;
			this.listt = listArticles;
			inflator = (LayoutInflater) projectActivity
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
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}

		@Override
		public View getView(final int pos, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;

			if (convertView == null) {
				convertView = inflator.inflate(R.layout.events_list_row, null);
				holder = new ViewHolder();
				holder.eventName = (TextView) convertView
						.findViewById(R.id.eventName);
				holder.dayTxt = (TextView) convertView.findViewById(R.id.day);
				holder.dateTxt = (TextView) convertView.findViewById(R.id.date);
				holder.startTymTxt = (TextView) convertView
						.findViewById(R.id.startTymEndTymTxt);
				holder.discTxt = (TextView) convertView
						.findViewById(R.id.discTxt);
				holder.datelay = (RelativeLayout) convertView
						.findViewById(R.id.datelay);
				holder.layTime = (LinearLayout) convertView
						.findViewById(R.id.layTime);
				holder.timeImg = (ImageView) convertView
						.findViewById(R.id.timeImg);

				holder.timeImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgTime = SVGParser.getSVGFromResource(getResources(),
						R.raw.icons_time);
				Drawable drwTym = svgTime.createPictureDrawable();
				holder.timeImg.setImageDrawable(drwTym);

				holder.eventName.setTypeface(typefaceRoman);
				holder.dayTxt.setTypeface(typefaceRoman);
				holder.dateTxt.setTypeface(typefaceRoman);
				holder.discTxt.setTypeface(typefaceRoman);
				holder.startTymTxt.setTypeface(typefaceRoman);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			System.out.println(listt);

			holder.eventName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), EventDetail.class);

					// for milestone and events

					// TODO Auto-generated method stub
					/*
					 * System.out.println(listt.get(pos).get("STARTDATE")); if
					 * (listt.get(pos).get("STARTDATE") != "" ||
					 * !listt.get(pos).get("STARTDATE").equals("")) { // this is
					 * milestone i.putExtra("ID", listt.get(pos).get("ID"));
					 * i.putExtra("TITLE", listt.get(pos).get("TITLE"));
					 * i.putExtra("PROID", listt.get(pos).get("PROID"));
					 * 
					 * System.out.println(listt.get(pos).get("TITLE")); } else {
					 * // this is event i.putExtra("ID",
					 * listt.get(pos).get("ID")); i.putExtra("TITLE",
					 * listt.get(pos).get("TITLE")); i.putExtra("PROID",
					 * listt.get(pos).get("PROID"));
					 * 
					 * i.putExtra("STARTTIME", listt.get(pos).get("STARTTIME"));
					 * i.putExtra("ENDTIME", listt.get(pos).get("ENDTIME"));
					 * i.putExtra("startD", listt.get(pos).get("startD"));
					 * i.putExtra("endD", listt.get(pos).get("endD"));
					 * 
					 * 
					 * 
					 * System.out.println(listt.get(pos).get("TITLE"));
					 * 
					 * }
					 */

					i.putExtra("ID", listt.get(pos).get("ID"));
					i.putExtra("TITLE", listt.get(pos).get("TITLE"));
					i.putExtra("PROID", listt.get(pos).get("PROID"));

					i.putExtra("STARTTIME", listt.get(pos).get("STARTTIME"));
					i.putExtra("ENDTIME", listt.get(pos).get("ENDTIME"));
					i.putExtra("startD", listt.get(pos).get("startD"));
					i.putExtra("endD", listt.get(pos).get("endD"));
					i.putExtra("FROM_CALENDER", "FROM_CALENDER");

					System.out.println(listt.get(pos).get("startD")
							+ "!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println(listt.get(pos).get("endD")
							+ "!!!!!!!!!!!!!!!!!!!!!!!!");

					startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_left);
				}
			});

			holder.eventName.setTag(pos);
			holder.dateTxt.setTag(pos);
			holder.dayTxt.setTag(pos);
			holder.startTymTxt.setTag(pos);
			holder.discTxt.setTag(pos);

			System.out.println(listt.get(pos).get("DAY"));
			if (listt.get(pos).get("DATECHECK") != ""
					|| !listt.get(pos).get("DATECHECK").equals("")) {
				// globalDateEvents = listt.get(pos).get("DATE");
				holder.dateTxt.setVisibility(View.VISIBLE);
				holder.dayTxt.setText(listt.get(pos).get("DAY"));
				holder.dateTxt.setText(listt.get(pos).get("DATECHECK"));
			} else {
				holder.datelay.setVisibility(View.INVISIBLE);
				holder.dateTxt.setVisibility(View.GONE);
				holder.dayTxt.setVisibility(View.GONE);
			}
			if (listt.get(pos).get("DISC") == null
					|| listt.get(pos).get("DISC") == "null"
					|| listt.get(pos).get("DISC").equals("null")
					|| listt.get(pos).get("DISC").equals("")) {
				holder.discTxt.setVisibility(View.GONE);
			} else {
				holder.discTxt.setVisibility(View.VISIBLE);
				holder.discTxt.setText(listt.get(pos).get("DISC"));
			}
			holder.eventName.setText(listt.get(pos).get("TITLE"));
			holder.startTymTxt.setText(listt.get(pos).get("STARTTIME") + "-"
					+ listt.get(pos).get("ENDTIME"));
			return convertView;
		}

		public class ViewHolder {
			public TextView eventName, dateTxt, dayTxt, startTymTxt, endTymTxt,
					discTxt;
			RelativeLayout datelay;
			LinearLayout layTime;
			ImageView timeImg;
		}
	}

	public void getDate() {
		Calendar calendar = Calendar.getInstance();
		// dateVal++ or dateVal-- to increase/decrease date
		calendar.add(Calendar.MONTH, dateVal);
		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date monthFirstDay = calendar.getTime();
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date monthLastDay = calendar.getTime();
		System.out.println(monthFirstDay + "_____NEXT MONTH______"
				+ monthLastDay);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		startDate = df.format(monthFirstDay);
		endDate = df.format(monthLastDay);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.LONG, Locale.getDefault());
		dateTxtEvnt.setText(monthName + " " + calendar.get(Calendar.YEAR));
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

	public class Users extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				usersArray = SigninPost.makeRequestUsers(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(usersArray);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			System.out.println(usersArray + "!!!!!!!!!!!!!!!!!!!!!!!");
			if (usersArray == null || usersArray.equals(null)) {

			} else {
				for (int i = 0; i < usersArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject josnUser;
					try {
						josnUser = usersArray.getJSONObject(i);
						map.put(josnUser.getString("email"),
								josnUser.getString("id"));
						usersListEmailnames.add(josnUser.getString("email"));
						usersListEmail.add(map);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	public class TaskCheckUncheck extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject jsonPatch = SigninPost.makeRequestChecked(hostname,
						userPref.getString("TOKEN", "NV"), proId, taskgroup_id,
						task_Id, taskCheckBoxstatus);
				System.out.println(jsonPatch + " : REPONSE");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

		}
	}

	// handler for the background updating
	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.incrementProgressBy(eventOwnList.size());
		}
	};

	public void showSnack(FragmentActivity fragmentActivity, String stringMsg, String ok) {
		new SnackBar(getActivity(), stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}
	
	void addTaskPopup(int addTaskLatestGlobalPosition, int addTaskGrpId){

		// TODO Auto-generated method stub

	//	addTaskLatestGlobalPosition = position;

		System.out.println(addTaskLatestGlobalPosition
				+ " POS *********************");

		this.addTaskGrpId = addTaskGrpId;
		final Dialog popup = new Dialog(getActivity());
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setContentView(R.layout.add_task);
		popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
		popup.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);

		TextView submitTxt = (TextView) popup
				.findViewById(R.id.addTaskListTxt);
		TextView cancelTxt = (TextView) popup
				.findViewById(R.id.cencelTxt);
		final EditText taskNameEdit = (EditText) popup
				.findViewById(R.id.taskName);
		final EditText dueDateEdit = (EditText) popup
				.findViewById(R.id.dueDate);
		final Spinner assignedToSpiiner = (Spinner) popup
				.findViewById(R.id.notAssiSpin);
		RelativeLayout lineSpinr = (RelativeLayout) popup
				.findViewById(R.id.line3);
		submitTxt.setTypeface(typefaceRoman);
		cancelTxt.setTypeface(typefaceRoman);
		taskNameEdit.setTypeface(typefaceRoman);

		CircleProgressBar pdialog1 = (CircleProgressBar) popup
				.findViewById(R.id.pDialog1);
		pdialog1.setColorSchemeResources(
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			// Do something for lollipop and above versions
			lineSpinr.setVisibility(View.VISIBLE);
		} else {
			// do something for phones running an SDK before
			// lollipop
			lineSpinr.setVisibility(View.GONE);
		}

		ArrayAdapter<String> assignedAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinr_txt,
				usersListEmailnames);

		assignedAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		assignedToSpiiner
				.setAdapter(new NothingSelectedSpinnerAdapter1(
						assignedAdapter,
						R.layout.assigned_spinner_nothing_selected,
						getActivity()));

		final Calendar myCalendar = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				SimpleDateFormat monthParse = new SimpleDateFormat(
						"MM");
				SimpleDateFormat monthDisplay = new SimpleDateFormat(
						"MMMM");
				// return
				// monthDisplay.format(monthParse.parse(String.valueOf(monthOfYear)));

				try {
					addStartEventDate = year + "-" + monthOfYear
							+ "-" + dayOfMonth;
					dueDateEdit.setText(dayOfMonth
							+ " "
							+ monthDisplay.format(monthParse.parse(String
									.valueOf(monthOfYear + 1)))
							+ " " + year);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		dueDateEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(getActivity(), dateStart,
						myCalendar.get(Calendar.YEAR), myCalendar
								.get(Calendar.MONTH), myCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		assignedToSpiiner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int pos, long arg3) {
						// TODO Auto-generated method stub
						addTaskAssigned = String
								.valueOf(assignedToSpiiner
										.getSelectedItem());
						System.out.println(pos + " : SPINNER POS ");
						System.out.println(addTaskAssigned
								+ "!!!!!!!!!!!!");
						System.out.println(usersListEmail
								+ "!!!!!!!!!!!!!!!");
						if (pos == 0) {

						} else {
							userIdForEMail = usersListEmail.get(
									pos - 1).get(addTaskAssigned);

							System.out
									.println(userIdForEMail
											+ " ==================================");
						}
					}

					@Override
					public void onNothingSelected(
							AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		submitTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isInternetPresent) {
					addTaskName = taskNameEdit.getText().toString();
					addTaskDuedate = dueDateEdit.getText()
							.toString();

					if (addTaskName == "" || addTaskName.equals("")) {
						Toast.makeText(getActivity(),
								"Enter a task name",
								Toast.LENGTH_SHORT).show();
					} else {
						// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						// 0);
						popup.dismiss();
						new AddTask().execute();

					}
				} else {
					showSnack(getActivity(),
							"Please check network connection!", "OK");
				}
			}

		});

		cancelTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				// 0);
				popup.dismiss();

			}
		});

		popup.show();

		// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

		
	}
	
}
