package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CommentsPost;
import mobile.teamwave.Http.SigninPost;
import mobile.teamwave.Http.TaskGrpGet;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.application.ListViewUtils;
import mobile.teamwave.application.NothingSelectedSpinnerAdapter1;
import mobile.teamwave.application.TASKS_CHECKBOX_MODEL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TasksList extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	ListView compltdlistviewTasks, uncompltdlistviewTasks;
	JSONObject jsonTaskObject, jsonTaskListObj, jsonAddTask;
	Hostname host;
	SharedPreferences userPref, proPref;
	String proId, taskGrpId, taskGrpName, taskName, isCompleted, taskId,
			commentDetail, firstName, lastName, comntCreated, commentImg,
			addStartEventDate, addTaskAssigned, addTaskName, addTaskDuedate;
	completedTasksAdapter completedTasksAdapter;
	UncompletedTasksAdapter uncompltdTasksAdapter;
	int first = 0, second = 0;
	ArrayList<TASKS_CHECKBOX_MODEL> compldtedTasksList = new ArrayList<TASKS_CHECKBOX_MODEL>();
	ArrayList<TASKS_CHECKBOX_MODEL> UncompldtedTasksList = new ArrayList<TASKS_CHECKBOX_MODEL>();
	Typeface typefaceRoman, typefaceLight;
	ScrollView taksListScroll;
	JSONArray jsonComments, usersArray;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
	ListCommentsBaseAdapter commentsAdapter;
	ListView commentsListview;
	Button sendBtn;
	EditText comntEdit;
	JSONObject jsonPushComment;
	CircleProgressBar pDialog;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	LinearLayout progrLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskslist);
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		commentsListview = (ListView) findViewById(R.id.commentListview);
		sendBtn = (Button) findViewById(R.id.addCmntBtn);
		comntEdit = (EditText) findViewById(R.id.comentEdittext);
		taksListScroll = (ScrollView) findViewById(R.id.taksListScroll);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		progrLay = (LinearLayout) findViewById(R.id.progrLay);
		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		typefaceRoman = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");
		typefaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoLight.ttf");

		compltdlistviewTasks = (ListView) findViewById(R.id.compltdlistviewTasks);
		uncompltdlistviewTasks = (ListView) findViewById(R.id.uncompltdlistviewTasks);
		host = new Hostname();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		taskGrpId = (String) bundle.get("taskGrpId");
		proId = (String) bundle.get("proId");
		taskGrpName = (String) bundle.get("taskgrpName");
		actionBar.setTitle(Html.fromHtml("<font color='#000000'>" + taskGrpName
				+ "</font>"));
		// actionBar.setTitle(taskGrpName);

		taksListScroll
				.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		taksListScroll.setFocusable(true);
		taksListScroll.setFocusableInTouchMode(true);
		taksListScroll.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
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
					/*Toast.makeText(getApplicationContext(), "Enter comment",
							Toast.LENGTH_SHORT).show();*/
					showSnack(TasksList.this,
							"Enter comment!",
							"OK");
				} else {
					if (isInternetPresent) {
						sendBtn.setText("Adding comment.....");
						new SendComment().execute();

					} else {
						showSnack(TasksList.this,
								"Please check network connection!",
								"OK");
					}

				}

			}
		});
		if (isInternetPresent) {
			new CallTasksList().execute();
			new CallTasksCommentsList().execute();
		} else {
			showSnack(TasksList.this,
					"Please check network connection!",
					"OK");
		}
		
		new Users().execute();

	}

	public class CallTasksList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonTaskObject = TaskGrpGet.callTasksList(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskGrpId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonTaskObject);
			return null;
		}

		@SuppressWarnings("unchecked")
		protected void onPostExecute(Void unused) {

			try {
				if (jsonTaskObject != null) {

					if (jsonTaskObject.toString() == "[]"
							|| jsonTaskObject.length() == 0) {
					/*	Toast.makeText(getApplicationContext(),
								"No tasks found!", Toast.LENGTH_SHORT).show();*/
						showSnack(TasksList.this,
								"No tasks found!",
								"OK");
						pDialog.setVisibility(View.GONE);
					} else {

						taskGrpName = jsonTaskObject.getString("name");
						taskGrpId = jsonTaskObject.getString("id");
						JSONArray tasksJsonArr = jsonTaskObject
								.getJSONArray("tasks");
						JSONObject createdByJson = jsonTaskObject
								.getJSONObject("created_by");
						String taskCreatedDate = null;

						try {
							taskCreatedDate = jsonTaskObject
									.getString("created_on");
							taskCreatedDate = taskCreatedDate.substring(0, 10);

							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date;
							date = df.parse(taskCreatedDate);

							System.out.println(date);
							taskCreatedDate = date.toString();
							System.out.println(taskCreatedDate.length());
							String dayStr = taskCreatedDate.substring(0, 3);
							taskCreatedDate = taskCreatedDate.substring(4, 10);
							System.out.println(taskCreatedDate);
							taskCreatedDate.replaceAll("\\s+", " ");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (int i = 0; i < tasksJsonArr.length(); i++) {

							jsonTaskListObj = tasksJsonArr.getJSONObject(i);
							taskName = jsonTaskListObj.getString("name");
							isCompleted = jsonTaskListObj
									.getString("is_completed");
							taskId = jsonTaskListObj.getString("id");

							TASKS_CHECKBOX_MODEL compltdTaskmodel;
							TASKS_CHECKBOX_MODEL uncompltdTaskmodelUn;
							if (isCompleted == "true") {
								if (first == 0) {
									compltdTaskmodel = new TASKS_CHECKBOX_MODEL(
											taskGrpId, proId, taskId, taskName,
											true, true,
											createdByJson
													.getString("full_name"),
											taskCreatedDate,
											jsonTaskListObj
													.getString("comment_count"));
									first++;
								} else {
									compltdTaskmodel = new TASKS_CHECKBOX_MODEL(
											taskGrpId, proId, taskId, taskName,
											true, false,
											createdByJson
													.getString("full_name"),
											taskCreatedDate,
											jsonTaskListObj
													.getString("comment_count"));
								}
								compldtedTasksList.add(compltdTaskmodel);
							} else {
								if (second == 0) {
									uncompltdTaskmodelUn = new TASKS_CHECKBOX_MODEL(
											taskGrpId, proId, taskId, taskName,
											false, true,
											createdByJson
													.getString("full_name"),
											taskCreatedDate,
											jsonTaskListObj
													.getString("comment_count"));

									System.out.println(taskGrpId + proId
											+ taskId + taskName);
									second++;
								} else {
									uncompltdTaskmodelUn = new TASKS_CHECKBOX_MODEL(
											taskGrpId, proId, taskId, taskName,
											false, false,
											createdByJson
													.getString("full_name"),
											taskCreatedDate,
											jsonTaskListObj
													.getString("comment_count"));
									System.out.println(taskGrpId + proId
											+ taskId + taskName);
								}
								UncompldtedTasksList.add(uncompltdTaskmodelUn);
							}
						}

						/*
						 * Collections.sort(countryList, new Comparator() {
						 * 
						 * @Override public int compare(Object obj1, Object
						 * obj2) { TASKS_CHECKBOX_MODEL modle =
						 * (TASKS_CHECKBOX_MODEL)obj1; TASKS_CHECKBOX_MODEL emp2
						 * = (TASKS_CHECKBOX_MODEL)obj2; String
						 * complete=String.valueOf(modle.Iscomplete()); String
						 * complete1=String.valueOf(emp2.Iscomplete());
						 * 
						 * return complete1.compareTo(complete); // return
						 * modle.getIs.compareTo(emp2.Iscomplete()); } });
						 */

						completedTasksAdapter = new completedTasksAdapter(
								TasksList.this, R.layout.tasks_grp_list_row,
								compldtedTasksList);
						uncompltdTasksAdapter = new UncompletedTasksAdapter(
								TasksList.this, R.layout.tasks_grp_list_row,
								UncompldtedTasksList);

						compltdlistviewTasks.setAdapter(completedTasksAdapter);
						uncompltdlistviewTasks
								.setAdapter(uncompltdTasksAdapter);

						// ListViewUtils.setDynamicHeight(compltdlistviewTasks);
						// ListViewUtils.setDynamicHeight(uncompltdlistviewTasks);

						// ListViewUtils.setDynamicHeight(compltdlistviewTasks);
						// pDialog.setVisibility(View.GONE);
						// progrLay.setVisibility(View.GONE);
					}

				} else {
					showSnack(TasksList.this,
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
					pDialog.setVisibility(View.GONE);
					progrLay.setVisibility(View.GONE);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class completedTasksAdapter extends
			ArrayAdapter<TASKS_CHECKBOX_MODEL> {

		private ArrayList<TASKS_CHECKBOX_MODEL> countryList;

		public completedTasksAdapter(Context context, int textViewResourceId,
				ArrayList<TASKS_CHECKBOX_MODEL> countryList) {
			super(context, textViewResourceId, countryList);
			this.countryList = new ArrayList<TASKS_CHECKBOX_MODEL>();
			this.countryList.addAll(countryList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));
			final TASKS_CHECKBOX_MODEL country = countryList.get(position);

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tasks_list_row, null);

				holder = new ViewHolder();
				holder.taskName = (TextView) convertView
						.findViewById(R.id.tasksName);
				holder.sectionHeaderTxt = (TextView) convertView
						.findViewById(R.id.taskgrpName);
				holder.taskDetailLay = (RelativeLayout) convertView
						.findViewById(R.id.taskDetailLay);
				holder.createdByTxt = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);
				holder.dateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);

				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);

				holder.sectionHeaderTxt.setTypeface(typefaceRoman);
				holder.taskName.setTypeface(typefaceRoman);
				holder.createdByTxt.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.checkBox.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					/*
					 * TASKS_CHECKBOX_MODEL country = (TASKS_CHECKBOX_MODEL) cb
					 * .getTag();
					 */

					if (cb.isChecked() == false) {

						System.out.println("UN CHECKDDDDDDDDDDDDDDDD");

						try {
							JSONObject jsonPatch = SigninPost.makeRequestCheck(
									host.globalVariable(),
									userPref.getString("TOKEN", "NV"),
									country.getproId(), country.getGrpId(),
									country.getTaskId(), "reopen");
							System.out
									.println(jsonPatch
											+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							if (jsonPatch.has("reopened")) {
								/*String status = jsonPatch.getString("reopened");
								Toast.makeText(getApplicationContext(),
										"reopened", Toast.LENGTH_SHORT).show();*/
								showSnack(TasksList.this,
										"reopened!",
										"OK");
							}

							final int position = compltdlistviewTasks
									.getPositionForView((View) v.getParent());

							System.out.println("POSITION : " + position
									+ "  LIST SIZE  " + countryList.size()
									+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

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
					if (cb.isChecked() == true) {

						System.out.println("CHECKDDDDDDDDDDDDDDDD");

						try {
							JSONObject jsonPatch = SigninPost.makeRequestCheck(
									host.globalVariable(),
									userPref.getString("TOKEN", "NV"),
									country.getproId(), country.getGrpId(),
									country.getTaskId(), "complete");
							System.out
									.println(jsonPatch
											+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							if (jsonPatch.has("completed")) {
								String status = jsonPatch
										.getString("completed");
								/*Toast.makeText(getApplicationContext(),
										"completed", Toast.LENGTH_SHORT).show();*/
								showSnack(TasksList.this,
										"completed!",
										"OK");
							}

							final int position = compltdlistviewTasks
									.getPositionForView((View) v.getParent());

							System.out.println("POSITION : " + position
									+ "  LIST SIZE  " + countryList.size()
									+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							// countryList.remove(position);
							// dataAdapter.notifyDataSetChanged();

							// finish();
							// startActivity(getIntent());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					else {
						System.out
								.println("UNNNNNNNNN    CHECKEDDDDDDDDDDDDDDDDDD");
					}

					country.setSelected(cb.isChecked());
				}
			});
			holder.taskDetailLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent i = new Intent(TasksList.this, TaskDetail.class);
					i.putExtra("taskGrpId", country.getGrpId());
					i.putExtra("proId", country.getproId());
					i.putExtra("taskId", country.getTaskId());
					i.putExtra("taskName", country.getName());
					i.putExtra("createdBy", country.getCreatedBy());
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
				}
			});

			// holder.code.setText(" (" + country.getCode() + ")");

			if (country.Iscomplete() == true) {
				holder.sectionHeaderTxt.setVisibility(View.VISIBLE);

				if (country.isSelected() == true) {
					holder.sectionHeaderTxt.setText("Completed Tasks");
				}
			} else {
				holder.sectionHeaderTxt.setVisibility(View.GONE);
			}

			holder.createdByTxt.setText(country.getCreatedBy());
			holder.taskName.setText(country.getName());
			holder.checkBox.setChecked(country.isSelected());
			holder.dateTxt.setText(country.getTaskCreatedDate());
			holder.comntCountTxt.setText(country.getCmntCount());
			holder.checkBox.setTag(country);
			return convertView;
		}

		private class ViewHolder {
			TextView taskName, sectionHeaderTxt, createdByTxt, comntCountTxt,
					dateTxt;
			CheckBox checkBox;
			RelativeLayout taskDetailLay;
			LinearLayout taskGrpLay;
			ImageView comntImg;
		}
	}

	private class UncompletedTasksAdapter extends
			ArrayAdapter<TASKS_CHECKBOX_MODEL> {

		private ArrayList<TASKS_CHECKBOX_MODEL> tasksList;

		public UncompletedTasksAdapter(Context context, int textViewResourceId,
				ArrayList<TASKS_CHECKBOX_MODEL> tasksList) {
			super(context, textViewResourceId, tasksList);
			this.tasksList = new ArrayList<TASKS_CHECKBOX_MODEL>();
			this.tasksList.addAll(tasksList);
		}

		private class ViewHolder {
			TextView taskName, sectionHeaderTxt, createdByTxt, comntCountTxt,
					dateTxt, addTask;
			CheckBox checkBox;
			RelativeLayout taskDetailLay;
			LinearLayout taskGrpLay;
			ImageView comntImg;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));
			final TASKS_CHECKBOX_MODEL country = tasksList.get(position);

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tasks_list_row, null);

				holder = new ViewHolder();
				holder.taskName = (TextView) convertView
						.findViewById(R.id.tasksName);
				holder.sectionHeaderTxt = (TextView) convertView
						.findViewById(R.id.taskgrpName);
				holder.taskDetailLay = (RelativeLayout) convertView
						.findViewById(R.id.taskDetailLay);
				holder.createdByTxt = (TextView) convertView
						.findViewById(R.id.createdByTxt);
				holder.dateTxt = (TextView) convertView
						.findViewById(R.id.dateTxt);
				holder.comntCountTxt = (TextView) convertView
						.findViewById(R.id.cmntCount);
				holder.addTask = (TextView) convertView
						.findViewById(R.id.addTask);
				holder.comntImg = (ImageView) convertView
						.findViewById(R.id.comntImg);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);

				holder.comntImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
						R.raw.comment);
				Drawable drwCmnt = svgComnt.createPictureDrawable();
				holder.comntImg.setImageDrawable(drwCmnt);

				holder.sectionHeaderTxt.setTypeface(typefaceRoman);
				holder.taskName.setTypeface(typefaceRoman);
				holder.createdByTxt.setTypeface(typefaceRoman);
				holder.comntCountTxt.setTypeface(typefaceRoman);
				holder.addTask.setTypeface(typefaceRoman);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			int size = getCount() - 1;

			System.out.println(tasksList.size()
					+ "********************************");
			if (position == size) {
				holder.addTask.setVisibility(View.VISIBLE);
			}
			holder.addTask.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					final Dialog popup = new Dialog(TasksList.this);
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

					List<String> usersList = new ArrayList<String>();

					System.out.println(usersArray + "!!!!!!!!!!!!!!!!!!!!!!!");
					if (usersArray == null || usersArray.equals(null)) {

					} else {
						for (int i = 0; i < usersArray.length(); i++) {

							JSONObject josnUser;
							try {
								josnUser = usersArray.getJSONObject(i);
								usersList.add(josnUser.getString("email"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					ArrayAdapter<String> assignedAdapter = new ArrayAdapter<String>(
							TasksList.this, R.layout.spinr_txt, usersList);

					assignedAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					assignedToSpiiner
							.setAdapter(new NothingSelectedSpinnerAdapter1(
									assignedAdapter,
									R.layout.assigned_spinner_nothing_selected,
									TasksList.this));
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
							new DatePickerDialog(TasksList.this, dateStart,
									myCalendar.get(Calendar.YEAR), myCalendar
											.get(Calendar.MONTH), myCalendar
											.get(Calendar.DAY_OF_MONTH)).show();
						}
					});
					assignedToSpiiner
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									addTaskAssigned = String
											.valueOf(assignedToSpiiner
													.getSelectedItem());

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
								/*	Toast.makeText(TasksList.this,
											"Enter a task name",
											Toast.LENGTH_SHORT).show();*/
									showSnack(TasksList.this,
											"Enter a task name!",
											"OK");
								} else {
									
									popup.dismiss();
									new AddTask().execute();
									
								}
							} else {
								showSnack(TasksList.this,
										"Please check network connection!",
										"OK");
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

			holder.checkBox.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;

					if (cb.isChecked() == false) {

						System.out.println("UN CHECKDDDDDDDDDDDDDDDD");

						try {
							JSONObject jsonPatch = SigninPost.makeRequestCheck(
									host.globalVariable(),
									userPref.getString("TOKEN", "NV"),
									country.getproId(), country.getGrpId(),
									country.getTaskId(), "reopen");
							System.out
									.println(jsonPatch
											+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							final int position = compltdlistviewTasks
									.getPositionForView((View) v.getParent());

							System.out.println("POSITION : " + position
									+ "  LIST SIZE  " + tasksList.size()
									+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (cb.isChecked() == true) {

						System.out.println("CHECKDDDDDDDDDDDDDDDD");

						try {
							JSONObject jsonPatch = SigninPost.makeRequestCheck(
									host.globalVariable(),
									userPref.getString("TOKEN", "NV"),
									country.getproId(), country.getGrpId(),
									country.getTaskId(), "complete");
							System.out
									.println(jsonPatch
											+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

							final int position = compltdlistviewTasks
									.getPositionForView((View) v.getParent());

							System.out.println("POSITION : " + position
									+ "  LIST SIZE  " + tasksList.size()
									+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					else {
						System.out
								.println("UNNNNNNNNN    CHECKEDDDDDDDDDDDDDDDDDD");
					}

					country.setSelected(cb.isChecked());
				}
			});
			holder.taskDetailLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent i = new Intent(TasksList.this, TaskDetail.class);
					i.putExtra("taskGrpId", country.getGrpId());
					i.putExtra("proId", country.getproId());
					i.putExtra("taskId", country.getTaskId());
					i.putExtra("taskName", country.getName());
					i.putExtra("createdBy", country.getCreatedBy());
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
				}
			});

			// holder.code.setText(" (" + country.getCode() + ")");

			if (country.Iscomplete() == true) {
				holder.sectionHeaderTxt.setText("Un-Completed Tasks");

				/*
				 * if (country.isSelected() == false) {
				 * holder.sectionHeaderTxt.setText("Completed Tasks"); }
				 */
			} else {
				holder.sectionHeaderTxt.setVisibility(View.GONE);
			}
			holder.createdByTxt.setText(country.getCreatedBy());
			holder.taskName.setText(country.getName());
			holder.checkBox.setChecked(country.isSelected());
			holder.dateTxt.setText(country.getTaskCreatedDate());
			holder.comntCountTxt.setText(country.getCmntCount());
			holder.checkBox.setTag(country);
			return convertView;
		}
	}

	public class CallTasksCommentsList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonComments = TaskGrpGet.callTaskGrpComments(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskGrpId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonComments);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonComments != null) {
				try {
					// comentCount = jsonComments.getString("count");

					// commentCountTxt.setText(comentCount);

					for (int i = 0; i < jsonComments.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject jsonComment = jsonComments.getJSONObject(i);

						commentDetail = jsonComment.getString("comment_detail");
						JSONObject jsonCreatedBy = jsonComment
								.getJSONObject("created_by");

						comntCreated = jsonComment.getString("created_on");

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
					pDialog.setVisibility(View.GONE);
					progrLay.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				showSnack(TasksList.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
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

	private class SendComment extends AsyncTask<Void, Void, Void> {
		@SuppressLint("InlinedApi")
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jsonComment = new JSONObject();
			try {
				jsonComment.put("comment_detail", comntEdit.getText()
						.toString());
				jsonPushComment = CommentsPost.PostTaskGrpComment(jsonComment,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, taskGrpId);
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
				showSnack(TasksList.this,
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
			progrLay.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = new JSONObject();
				json.put("name", addTaskName);
				// addTaskAssigned="{"+"id:"+1 + "}";
				JSONObject assignedJson = new JSONObject();
				assignedJson.put("id", 1);
				JSONArray attachArr = new JSONArray();

				json.put("assigned_to", assignedJson);
				json.put("taskgroup_id", taskGrpId);
				json.put("attachment", attachArr);

				jsonAddTask = TaskGrpGet.createTask(json,
						userPref.getString("TOKEN", "NV"),
						host.globalVariable(), proId,
						Integer.parseInt(taskGrpId));
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
				try {
					taskName = jsonAddTask.getString("name");
					isCompleted = jsonAddTask.getString("is_completed");
					taskId = jsonAddTask.getString("id");
					JSONObject createdByJson = jsonAddTask
							.getJSONObject("created_by");

					String taskCreatedDate = null;

					taskCreatedDate = jsonAddTask.getString("created_on");
					taskCreatedDate = taskCreatedDate.substring(0, 10);

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					date = df.parse(taskCreatedDate);

					System.out.println(date);
					taskCreatedDate = date.toString();
					System.out.println(taskCreatedDate.length());
					String dayStr = taskCreatedDate.substring(0, 3);
					taskCreatedDate = taskCreatedDate.substring(4, 10);
					System.out.println(taskCreatedDate);
					taskCreatedDate.replaceAll("\\s+", " ");

					TASKS_CHECKBOX_MODEL uncompltdTaskmodelUn = new TASKS_CHECKBOX_MODEL(
							taskGrpId, proId, taskId, taskName, false, false,
							createdByJson.getString("full_name"),
							taskCreatedDate,
							jsonTaskListObj.getString("comment_count"));
					System.out.println(taskGrpId + proId + taskId + taskName);
					UncompldtedTasksList.add(uncompltdTaskmodelUn);
				} catch (ParseException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				uncompltdTasksAdapter = new UncompletedTasksAdapter(
						TasksList.this, R.layout.tasks_grp_list_row,
						UncompldtedTasksList);
				uncompltdlistviewTasks.setAdapter(uncompltdTasksAdapter);

				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

			else {
				showSnack(TasksList.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}
		}
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		  Intent i = new Intent(getApplicationContext(),
		  ProjectActivityTaskGroupTabStrip.class); i.putExtra("proId", proId);
		  i.putExtra("proName", proPref.getString("PRONAME", "NV"));
		  startActivity(i); super.onBackPressed();
		 
		//finish();
	}
	public void showSnack(TasksList appHome, String stringMsg, String ok){
		new SnackBar(TasksList.this,
				stringMsg,
				ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}
}
