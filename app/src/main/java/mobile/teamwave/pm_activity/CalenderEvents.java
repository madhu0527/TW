package mobile.teamwave.pm_activity;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.EventsGet;
import mobile.teamwave.application.ConvertDatetoTimeZone;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class CalenderEvents extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventOwnList = new ArrayList<HashMap<String, String>>();
	CircleProgressBar pDialog;
	JSONArray eventsJarray;
	Hostname host;
	String hostname, startDate, endDate, eventId, eventTitle, mileStoneId,
			mileStoneTitle, eventStrtDate, dayStr;
	String globalDateEvents = "";
	SharedPreferences userPref;
	ListView listviewEvents;
	ImageView prevImg, nextImg;
	TextView dateTxtEvnt, noDataaTxt;
	int dateVal = 0;
	Typeface typefaceRoman;
	LinearLayout eventsDateLay;
	ListEventsMilesAdapter adapterEvents;
	ConvertDatetoTimeZone timezone;
	SwipeRefreshLayout mSwipeRefreshLayout;
	Runnable run;
	LinearLayoutManager layoutManager;
	ProgressDialog dialog;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calender_events);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		listviewEvents = (ListView) findViewById(R.id.listviewEvents);
		prevImg = (ImageView) findViewById(R.id.prevImg);
		nextImg = (ImageView) findViewById(R.id.nextImg);
		dateTxtEvnt = (TextView) findViewById(R.id.dateTxt);
		noDataaTxt = (TextView) findViewById(R.id.noDataaTxt);
		prevImg = (ImageView) findViewById(R.id.prevImg);
		nextImg = (ImageView) findViewById(R.id.nextImg);
		eventsDateLay = (LinearLayout) findViewById(R.id.eventsDateLay);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		getOverflowMenu();
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + "Calender"
				+ "</font>"));
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		host = new Hostname();
		hostname = host.globalVariable();
		
		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		typefaceRoman = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");

		dateTxtEvnt.setTypeface(typefaceRoman);

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

		timezone = new ConvertDatetoTimeZone(CalenderEvents.this);

		mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,
				R.color.green, R.color.blue);
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {

								eventOwnList = new ArrayList<HashMap<String, String>>();
								new EventsList().execute();
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
				System.out.println(eventOwnList
						+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				if (eventOwnList.toString() == "[]" || eventOwnList.size() == 0) {

				} else {
					adapterEvents.notifyDataSetChanged();
					listviewEvents.invalidateViews();
				}

				// mRecyclerView.refreshDrawableState();
			}
		};

		prevImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateVal = dateVal - 1;
				eventsList = new ArrayList<HashMap<String, String>>();
				eventOwnList = new ArrayList<HashMap<String, String>>();
				getDate();

				System.out.println(startDate
						+ "   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   " + endDate);

				pDialog.setVisibility(View.VISIBLE);

				new EventsList().execute();
			}
		});
		nextImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateVal = dateVal + 1;
				eventsList = new ArrayList<HashMap<String, String>>();
				eventOwnList = new ArrayList<HashMap<String, String>>();
				getDate();

				pDialog.setVisibility(View.VISIBLE);

				System.out.println(startDate
						+ "   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   " + endDate);
				new EventsList().execute();
			}
		});

		getDate();
		System.out.println(startDate + "   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   "
				+ endDate);
		new EventsList().execute();
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
				eventsJarray = EventsGet.CalenderEventsList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), startDate, endDate);
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
					noDataaTxt.setVisibility(View.VISIBLE);
					listviewEvents.setAdapter(null);
				} else {

					for (int i = 0; i < eventsJarray.length(); i++) {
						HashMap<String, String> eventsMap = new HashMap<String, String>();
						try {
							JSONObject jsonEvent = eventsJarray
									.getJSONObject(i);

							eventId = jsonEvent.getString("event");
							eventTitle = jsonEvent.getString("title");
							eventStrtDate = jsonEvent.getString("start_dt");
							String timeDate = eventStrtDate;
							timeDate = timeDate.substring(0, 19);
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

							eventsMap.put("PROID",
									jsonEvent.getString("object_id"));
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
						eventsMap.put("PROID", eventsList.get(j).get("PROID"));
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

					adapterEvents = new ListEventsMilesAdapter(
							CalenderEvents.this, eventOwnList);
					listviewEvents.setAdapter(adapterEvents);
					listviewEvents.setDivider(null);
					pDialog.setVisibility(View.GONE);

				}

			}else{
					System.out.println("SERVER IS NOT WORKING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					pDialog.setVisibility(View.GONE);
					showSnack(CalenderEvents.this,
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
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
					Intent i = new Intent(getApplicationContext(),
							EventDetail.class);

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
					System.out.println(listt.get(pos).get("PROID"));
					i.putExtra("ID", listt.get(pos).get("ID"));
					i.putExtra("TITLE", listt.get(pos).get("TITLE"));
					i.putExtra("PROID", listt.get(pos).get("PROID"));
					i.putExtra("FROM_CALENDER", "IAM_CALENDER");
					i.putExtra("STARTTIME", listt.get(pos).get("STARTTIME"));
					i.putExtra("ENDTIME", listt.get(pos).get("ENDTIME"));
					i.putExtra("startD", listt.get(pos).get("startD"));
					i.putExtra("endD", listt.get(pos).get("endD"));

					System.out.println(listt.get(pos).get("startD")
							+ "!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println(listt.get(pos).get("endD")
							+ "!!!!!!!!!!!!!!!!!!!!!!!!");

					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_to_cal, menu);
		MenuItem item = menu.findItem(R.id.addtoCal);
		item.setTitle("Sync to calender");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		case R.id.addtoCal:
			
	    		if (isInternetPresent) {
	    			
	    			if(eventOwnList.size()==0||eventOwnList.toString()=="[]"){
	    	    	/*	Toast.makeText(CalenderEvents.this, "There is no events!",
	    						Toast.LENGTH_SHORT).show();*/
	    	    		showSnack(CalenderEvents.this,
	    						"Oops! There is no events!",
	    						"OK");
	    	    	}else{
	    	    		System.out.println(eventOwnList
								+ "++++++++++++++++++++++++++++++++++++++");
						pDialog.setVisibility(View.VISIBLE);
						
						 	dialog = new ProgressDialog(this);
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
					        Thread background = new Thread (new Runnable() {
					           public void run() {
					               try {
					                   // enter the code to be run while displaying the progressbar.
					                   //
					                   // This example is just going to increment the progress bar:
					                   // So keep running until the progress value reaches maximum value
					                   while (dialog.getProgress()<= dialog.getMax()) {
					                       // wait 500ms between each update
					                       Thread.sleep(1500);
					 
					                       // active the update handler
					                       progressHandler.sendMessage(progressHandler.obtainMessage());
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
								System.out.println(yearBegin + " : " + monthBegin + " : "
										+ dayBegin + "___ "
										+ eventOwnList.get(j).get("TITLE"));
								System.out.println(yearEnd + " : " + monthEnd + " : "
										+ dayEnd + "___ "
										+ eventOwnList.get(j).get("TITLE"));

								SimpleDateFormat displayFormat = new SimpleDateFormat(
										"HH:mm");
								SimpleDateFormat parseFormat = new SimpleDateFormat(
										"hh:mm a");
								Date startTym = parseFormat.parse(eventOwnList.get(j).get(
										"STARTTIME"));
								Date endTym = parseFormat.parse(eventOwnList.get(j).get(
										"ENDTIME"));

								String sTymHrs = displayFormat.format(startTym).toString()
										.substring(0, 2);
								String sTymMins = displayFormat.format(startTym).toString()
										.substring(3, 5);

								String eTymHrs = displayFormat.format(endTym).toString()
										.substring(0, 2);
								String eTymMins = displayFormat.format(endTym).toString()
										.substring(3, 5);

								System.out.println(displayFormat.format(startTym)
										+ "     START TIMEEEEEEEEEEEEEEEEEE   " + sTymHrs
										+ ":" + sTymMins);
								System.out.println(displayFormat.format(endTym)
										+ "      END TIMEEEEEEEEEEEEEEEEEE" + eTymHrs + ":"
										+ eTymMins);

								ContentValues cv = new ContentValues();
								cv.put("calendar_id", j + 1);
								cv.put("title", eventOwnList.get(j).get("TITLE"));
								// cv.put("dtstart", dtstart );
								// cv.put("dtend", dtend);
								cv.put("rrule", "FREQ=MONTHLY");
								cv.put("description", "comment");
								Calendar start = Calendar.getInstance();
								start.set(yearBegin, monthBegin, dayBegin, Integer.parseInt(sTymHrs), Integer.parseInt(sTymMins), 0);

								Calendar end = Calendar.getInstance();
								end.set(yearEnd, monthEnd, dayEnd, Integer.parseInt(eTymHrs), Integer.parseInt(eTymMins), 0);

								long startTime = start.getTimeInMillis();
								long endTime = end.getTimeInMillis();

								cv.put("dtstart", startTime);
								cv.put("dtend", endTime);
								// Insertion on the events of the calendar
								ContentResolver contentResolver = CalenderEvents.this
										.getContentResolver();
								cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone
										.getDefault().getID());
								contentResolver.insert(
										Uri.parse("content://com.android.calendar/events"),
										cv);

							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						 
						dialog.setMessage("Sync completed");
						pDialog.setVisibility(View.GONE);
						/*Toast.makeText(getApplicationContext(), "Sync is completed",
								Toast.LENGTH_SHORT).show();*/
						
						showSnack(CalenderEvents.this,
								"Sync is completed!",
								"OK");
						

						return true;
	    	    	}
	    		} else {
	    			showSnack(CalenderEvents.this,
							"Please check network connection!",
							"OK");
	    	}
			default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	 // handler for the background updating
    Handler progressHandler = new Handler() {
        public void handleMessage(Message msg) {
            dialog.incrementProgressBy(eventOwnList.size());
        }
    };
	public void showSnack(CalenderEvents login, String stringMsg, String ok){
		new SnackBar(CalenderEvents.this,
				stringMsg,
				ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}
}
