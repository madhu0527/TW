package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.TimeLogGet;
import mobile.teamwave.application.Hostname;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TimeLogDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	Hostname host;
	SharedPreferences userPref;
	String taskId, proId,timeLogMonthStr,globalTimeDateForTimeLog,dayStr;
	JSONArray jArray;
	JSONObject timeLogJson;
	ListView listview;
	TextView headerTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timelog_detail);
		
		listview=(ListView)findViewById(R.id.taskTimelogListview);
		headerTxt=(TextView)findViewById(R.id.headerTxt);
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#171A1F")));

		host = new Hostname();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		taskId = bundle.getString("taskId");
		proId = bundle.getString("proId");
		new CallTimeLogDetailList().execute();
	}

	public class CallTimeLogDetailList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jArray = TimeLogGet.callTimeLogDetailList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jArray);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jArray != null) {
				if (jArray.toString() == "[]" || jArray.length() == 0) {
					Toast.makeText(TimeLogDetail.this, "No Time Log found!",
							Toast.LENGTH_SHORT).show();
				}else{
					ArrayList<HashMap<String, String>> timeLogList = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < jArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						try {
							timeLogJson = jArray.getJSONObject(i);
							timeLogMonthStr = timeLogJson
									.getString("log_month");
							String taskName;
							if (timeLogJson.isNull("task_object")) {
								taskName = "";
								String disc = timeLogJson
										.getString("description");
								map.put("disc", disc);
							} else {
								JSONObject jsonTask = timeLogJson
										.getJSONObject("task_object");
								taskName = jsonTask.getString("name");
								headerTxt.setText("Everyone's hours for "+taskName);
								actionBar.setTitle(taskName);
								String taskId = jsonTask.getString("id");
								map.put("TASKID", taskId);

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
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					ListTimeLogAdapter	adapterTimeLog = new ListTimeLogAdapter(TimeLogDetail.this,
							timeLogList);
					listview.setAdapter(adapterTimeLog);
				
				}
			}else{
				Toast.makeText(getApplicationContext(),
						"Something went wrong! Check server",
						Toast.LENGTH_SHORT).show();
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
		public View getView(final int position, View convertView,
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

			holder.dateMonth.setTag(position);
			holder.taskNameTxt.setTag(position);

			if (listt.get(position).get("LOGMONTH") == ""
					|| listt.get(position).get("LOGMONTH").equals("")) {
				holder.dateLay.setVisibility(View.GONE);
			} else {
				holder.dateLay.setVisibility(View.VISIBLE);
			}
			if (listt.get(position).get("TASKNAME") == ""
					|| listt.get(position).get("TASKNAME").equals("")) {
				holder.taskNameTxt.setText(listt.get(position).get("disc"));
			} else {
				holder.taskNameTxt.setText("TASK : "
						+ listt.get(position).get("TASKNAME"));
			}

			holder.dateMonth.setText(listt.get(position).get("LOGMONTH"));
			holder.logTime.setText(listt.get(position).get("DAY") + ","
					+ listt.get(position).get("DATELOG"));
			holder.timeTxt.setText(listt.get(position).get("TIMELOG"));
			holder.createdNameTxt.setText(listt.get(position).get("FULLNAME"));
			return convertView;
		}

		public class ViewHolder {
			public TextView dateMonth, logTime, taskNameTxt, createdNameTxt,
					timeTxt;
			RelativeLayout dateLay;
			LinearLayout layClick;
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
}
