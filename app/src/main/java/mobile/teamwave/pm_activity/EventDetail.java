package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CommentsPost;
import mobile.teamwave.Http.EventsGet;
import mobile.teamwave.Http.NotesGet;
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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

@SuppressLint("SimpleDateFormat")
public class EventDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	Hostname host;
	SharedPreferences userPref, proPref;
	String eventId, title, proId, commentDetail, comntCreated, commentImg,
			firstName, startDate, endDate, startTym, endTym, eventUrl,
			lastName, fromCalender;
	TextView titleTxt, proNameTxt, fileNametxt;
	Button sendBtn;
	EditText comntEdit;
	JSONObject jsonPushComment, jsonUrlEvent;
	JSONArray jsonEvents;
	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
	ListView commentsListview;
	ListEventsCommentsBaseAdapter commentsAdapter;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	ScrollView scrollEvents;
	Typeface typefaceLight;
	CircleProgressBar pDialog;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	LinearLayout progrLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detail);
		// getOverflowMenu();
		scrollEvents = (ScrollView) findViewById(R.id.scrollEvents);
		titleTxt = (TextView) findViewById(R.id.eventName);
		comntEdit = (EditText) findViewById(R.id.comentEdittext);
		proNameTxt = (TextView) findViewById(R.id.proName);
		sendBtn = (Button) findViewById(R.id.addCmntBtn);
		commentsListview = (ListView) findViewById(R.id.commentListview);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		progrLay = (LinearLayout) findViewById(R.id.progrLay);

		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		host = new Hostname();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		eventId = bundle.getString("ID");
		title = bundle.getString("TITLE");
		proId = bundle.getString("PROID");
		startDate = bundle.getString("startD");
		endDate = bundle.getString("endD");
		startTym = bundle.getString("STARTTIME");
		endTym = bundle.getString("ENDTIME");

		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		proNameTxt.setText(proPref.getString("PRONAME", "NV"));

		eventUrl = bundle.getString("eventUrl");
		fromCalender = bundle.getString("FROM_CALENDER");

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		if (eventUrl == null || eventUrl == "null" || eventUrl.equals("null")) {

		} else {
			if (isInternetPresent) {
				new CallEventsDetail().execute();
			} else {
				showSnack(EventDetail.this,
						"Please check network connection!",
						"OK");
			}
		}

		typefaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoLight.ttf");
		titleTxt.setTypeface(typefaceLight);
		// taskGrpNameTxt.setTypeface(typefaceLight);
		proNameTxt.setTypeface(typefaceLight);
		sendBtn.setTypeface(typefaceLight);
		comntEdit.setTypeface(typefaceLight);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + title
				+ "</font>"));
		// actionBar.setTitle(title);
		titleTxt.setText(title);

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (comntEdit.getText().toString() == ""
						|| comntEdit.getText().toString().equals("")
						|| comntEdit.getText().toString().length() == 0
						|| comntEdit.getText().toString() == null) {
					/*Toast.makeText(getApplicationContext(), "Enter comment",
							Toast.LENGTH_SHORT).show();*/
					showSnack(EventDetail.this,
							"Enter comment!",
							"OK");
				} else {
					if (isInternetPresent) {
						sendBtn.setText("Adding comment.....");
						new SendComment().execute();
					} else {
						showSnack(EventDetail.this,
								"Please check network connection!",
								"OK");
					}
				}

			}
		});

		scrollEvents
				.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollEvents.setFocusable(true);
		scrollEvents.setFocusableInTouchMode(true);
		scrollEvents.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});
		if (isInternetPresent) {
			new CallEventsCommentsList().execute();
		} else {
			showSnack(EventDetail.this,
					"Please check network connection!",
					"OK");
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

		case R.id.addtoCal:
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

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

				String beginHrs = startTym.substring(0, 2);
				String beginMins = startTym.substring(3, 5);

				String endHrs = endTym.substring(0, 2);
				String endMins = endTym.substring(3, 5);

				Calendar beginCal = Calendar.getInstance();
				beginCal.set(yearBegin, monthBegin, dayBegin,
						Integer.parseInt(beginHrs), Integer.parseInt(beginMins));
				long startTime = beginCal.getTimeInMillis();

				Calendar endCal = Calendar.getInstance();
				endCal.set(yearEnd, monthEnd, dayEnd, Integer.parseInt(endHrs),
						Integer.parseInt(endMins));
				long endTime = endCal.getTimeInMillis();

				Intent intent = new Intent(Intent.ACTION_INSERT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra(Events.TITLE, title);
				intent.putExtra(Events.DESCRIPTION, "");
				intent.putExtra(Events.EVENT_LOCATION, "");
				intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
						startTime);
				intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
				intent.putExtra(Events.STATUS, 1);
				intent.putExtra(Events.VISIBLE, 0);
				intent.putExtra(Events.HAS_ALARM, 1);
				startActivity(intent);

			} catch (Exception e) {

			}

			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_to_cal, menu);

		return super.onCreateOptionsMenu(menu);
	}

	public class CallEventsCommentsList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
			progrLay.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonEvents = NotesGet.callEventsComments(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, eventId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonEvents);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonEvents != null) {
				try {
					// comentCount = jsonEvents.getString("count");

					// commentCountTxt.setText(comentCount);

					// JSONArray jArray = jsonEvents.getJSONArray("results");
					for (int i = 0; i < jsonEvents.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject jsonComments = jsonEvents.getJSONObject(i);

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
					commentsAdapter = new ListEventsCommentsBaseAdapter(
							getApplicationContext(), commentsList);
					commentsListview.setAdapter(commentsAdapter);
					ListViewUtils.setDynamicHeight(commentsListview);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			} else {
				showSnack(EventDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
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
				jsonPushComment = CommentsPost.PostEventsComment(jsonComment,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, eventId);
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
			System.out.println(jsonPushComment
					+ "^^^^^^^^^^^^ RESULT OF PUSH COMMENT ^^^^^^^^^^^^^^");

			if (jsonPushComment != null) {
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
						commentsAdapter = new ListEventsCommentsBaseAdapter(
								getApplicationContext(), commentsList);
						commentsListview.setAdapter(commentsAdapter);
						comntEdit.setText("");
						commentsAdapter.notifyDataSetChanged();
						ListViewUtils.setDynamicHeight(commentsListview);
						sendBtn.setText("ADD THIS COMMENT");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			} else {
				showSnack(EventDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}
		}
	}

	@SuppressLint("NewApi")
	public class ListEventsCommentsBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListEventsCommentsBaseAdapter(Context context2,
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

	public class CallEventsDetail extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonUrlEvent = EventsGet.callEventsDetail(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), eventUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonUrlEvent);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonUrlEvent != null) {

				try {
					startDate = jsonUrlEvent.getString("start")
							.substring(0, 10);
					endDate = jsonUrlEvent.getString("end").substring(0, 10);
					startTym = jsonUrlEvent.getString("start_time");
					endTym = jsonUrlEvent.getString("end_time");
					actionBar.setTitle(Html.fromHtml("<font color='#424242'>"
							+ jsonUrlEvent.getString("title") + "</font>"));
					// actionBar.setTitle(title);
					titleTxt.setText(jsonUrlEvent.getString("title"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			} else {
				showSnack(EventDetail.this,
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
		if (fromCalender == null || fromCalender == "null"
				|| fromCalender.equals("null")) {
			Intent i = new Intent(getApplicationContext(),
					ProjectActivityTaskGroupTabStrip.class);
			i.putExtra("proId", proId);
			i.putExtra("proName", proPref.getString("PRONAME", "NV"));
			startActivity(i);
			super.onBackPressed();
		} else {
			finish();
		}
	}

	public void showSnack(EventDetail appHome, String stringMsg, String ok) {
		new SnackBar(EventDetail.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}
}
