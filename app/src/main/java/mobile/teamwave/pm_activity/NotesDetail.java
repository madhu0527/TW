package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import teamwave.android.mobile.teamwave.R;

import mobile.teamwave.Http.CommentsPost;
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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.SnackBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NotesDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	Hostname host;
	SharedPreferences userPref, proPref;
	String notesId, proId, commentDetail, comntCreated, commentImg, firstName,
			lastName, fromTabs;
	TextView titleTxt, createdByTxt, proNameTxt, createdOntxt;
	JSONObject jsonNotes;
	JSONArray jsonMessages;
	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
	ListNotesCommentsBaseAdapter commentsAdapter;
	ListView commentsListview;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	ScrollView scrollNotes;
	Button sendBtn, updateBtn;
	JSONObject jsonPushComment, jsonUpdateNotes;
	EditText comntEdit, contentTxt;
	Typeface typefaceLight;
	CircleProgressBar pDialog;
	LinearLayout progrLay,notesLay;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_detail);
		scrollNotes = (ScrollView) findViewById(R.id.scrollNotes);
		titleTxt = (TextView) findViewById(R.id.noteName);
		contentTxt = (EditText) findViewById(R.id.contentTxt);
		comntEdit = (EditText) findViewById(R.id.comentEdittext);
		proNameTxt = (TextView) findViewById(R.id.proName);
		// createdOntxt = (TextView) findViewById(R.id.createdonTxt);
		commentsListview = (ListView) findViewById(R.id.commentListview);
		sendBtn = (Button) findViewById(R.id.addCmntBtn);
		updateBtn = (Button) findViewById(R.id.updateBtn);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		progrLay=(LinearLayout)findViewById(R.id.progrLay);
		notesLay=(LinearLayout)findViewById(R.id.notesLay);
		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(Html.fromHtml("<font color='#424242'>"
				+ "" + "</font>"));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

		host = new Hostname();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		notesId = bundle.getString("notesId");
		System.out.println(notesId + " !!!!!!!!!!!!!!!!!!!!!!!!!!");
		proId = bundle.getString("proId");
		fromTabs = bundle.getString("FROM_TABS");

		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		proNameTxt.setText(proPref.getString("PRONAME", "NV"));

		typefaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoLight.ttf");
		contentTxt.setTypeface(typefaceLight);
		titleTxt.setTypeface(typefaceLight);
		sendBtn.setTypeface(typefaceLight);
		proNameTxt.setTypeface(typefaceLight);
		comntEdit.setTypeface(typefaceLight);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		scrollNotes
				.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollNotes.setFocusable(true);
		scrollNotes.setFocusableInTouchMode(true);
		scrollNotes.setOnTouchListener(new View.OnTouchListener() {
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
					showSnack(NotesDetail.this,
							"Enter comment!",
							"OK");
				} else {
					if (isInternetPresent) {
						
						sendBtn.setText("Adding comment.....");
						
						new SendComment().execute();

					} else {
						showSnack(NotesDetail.this,
								"Please check network connection!",
								"OK");
					}

				}

			}
		});
		updateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isInternetPresent) {
					contentTxt.setCursorVisible(false);
					updateBtn.setText("Updating....");
					new UpdateNotes().execute();

				} else {
					showSnack(NotesDetail.this,
							"Please check network connection!",
							"OK");
				}

			}
		});
		
		contentTxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				updateBtn.setText("UPDATE NOTE");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				updateBtn.setText("UPDATE NOTE");
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		contentTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contentTxt.setCursorVisible(true);
			}
		});
		notesLay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contentTxt.setCursorVisible(true);
			}
		});
		contentTxt.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// code to execute when EditText loses focus
					updateBtn.setText("UPDATE NOTE");
					updateBtn.setVisibility(View.VISIBLE);
				} else {
					updateBtn.setVisibility(View.VISIBLE);
				}
			}
		});
		if (isInternetPresent) {
			new CallNotesDetail().execute();
			new CallNotesCommentsList().execute();
		} else {
			showSnack(NotesDetail.this,
					"Please check network connection!",
					"OK");
		}
	}

	public class CallNotesDetail extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonNotes = NotesGet.callNotesDetail(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, notesId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonNotes);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonNotes != null) {
				try {
					titleTxt.setText(jsonNotes.getString("name"));
					// actionBar.setTitle(jsonNotes.getString("name"));
					actionBar.setTitle(Html.fromHtml("<font color='#424242'>"
							+ jsonNotes.getString("name") + "</font>"));
					contentTxt.setText(Html.fromHtml(jsonNotes
							.getString("content")));
					String createdOn = jsonNotes.getString("created_on");

					createdOn = createdOn.substring(0, 10);

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = df.parse(createdOn);
						System.out.println(date);
						createdOn = date.toString();
						System.out.println(createdOn.length());
						String dayStr = createdOn.substring(0, 3);
						createdOn = createdOn.substring(4, 10);
						System.out.println(createdOn);
						createdOn.replaceAll("\\s+", " ");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JSONObject jsonCreatedBy = jsonNotes
							.getJSONObject("created_by");
					/*
					 * createdByTxt.setText("Created By :" +
					 * jsonCreatedBy.getString("full_name"));
					 * createdOntxt.setText("Created On : " + createdOn);
					 */
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contentTxt.setVisibility(View.VISIBLE);
			//	pDialog.setVisibility(View.GONE);
			//	progrLay.setVisibility(View.GONE);
			} else {
				showSnack(NotesDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
	}

	public class CallNotesCommentsList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonMessages = NotesGet.callNotesComments(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, notesId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonMessages);
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonMessages != null) {
				try {
					// comentCount = jsonMessages.getString("count");

					// commentCountTxt.setText(comentCount);

					// JSONArray jArray = jsonMessages.getJSONArray("results");
					for (int i = 0; i < jsonMessages.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject jsonComments = jsonMessages.getJSONObject(i);

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
					commentsAdapter = new ListNotesCommentsBaseAdapter(
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
				showSnack(NotesDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListNotesCommentsBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListNotesCommentsBaseAdapter(Context context2,
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

		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jsonComment = new JSONObject();
			try {
				jsonComment.put("comment_detail", comntEdit.getText()
						.toString());
				jsonPushComment = CommentsPost.PostNoteComment(jsonComment,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, notesId);
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
						commentsAdapter = new ListNotesCommentsBaseAdapter(
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
				showSnack(NotesDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
			}
		}
	}

	private class UpdateNotes extends AsyncTask<Void, Void, Void> {
		@SuppressLint("InlinedApi")
		protected void onPreExecute() {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				JSONObject jsonNote = new JSONObject();
				jsonNote.put("content", contentTxt.getText().toString());
				jsonUpdateNotes = NotesGet.UpdateNotes(jsonNote,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, notesId);
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
			if (jsonUpdateNotes != null) {
				contentTxt.setText(contentTxt.getText().toString());
				updateBtn.setText("Updated");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			} else {
				Toast.makeText(
						getApplicationContext(),
						"Some thing went wrong while sending comment!please try again later!",
						2000).show();
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

			/*
			 * System.out.println(jsonUpdateNotes); finish();
			 * startActivity(getIntent());
			 */
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (fromTabs == null || fromTabs == "null" || fromTabs.equals("null")) {
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
	public void showSnack(NotesDetail appHome, String stringMsg, String ok) {
		new SnackBar(NotesDetail.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}
}
