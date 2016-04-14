package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CommentsPost;
import mobile.teamwave.Http.MsgsGet;
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
import android.text.Html;
import android.view.LayoutInflater;
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

public class MessageDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	Hostname host;
	SharedPreferences userPref, proPref;
	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
	ListMsgCommentsBaseAdapter commentsAdapter;
	ListView commentsListview;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	ImageView comentImg;
	String proId, contentID, contentType, commentDetail, comntCreated,
			commentImg, firstName, lastName, dpName, comentCount, msgId, title;
	JSONArray jsonMessages;
	ScrollView scrollmsgs;
	Typeface typefaceLight;
	CircleProgressBar pDialog;
	LinearLayout progrLay;
	TextView titleTxt, proNameTxt, msgNametxt;
	Button sendBtn;
	EditText comntEdit;
	JSONObject jsonPushComment;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msessage_detail);

		scrollmsgs = (ScrollView) findViewById(R.id.scrollEvents);
		msgNametxt = (TextView) findViewById(R.id.msgName);

		comntEdit = (EditText) findViewById(R.id.comentEdittext);
		proNameTxt = (TextView) findViewById(R.id.proName);
		sendBtn = (Button) findViewById(R.id.addCmntBtn);
		commentsListview = (ListView) findViewById(R.id.commentListview);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		progrLay=(LinearLayout)findViewById(R.id.progrLay);
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

		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		proNameTxt.setText(proPref.getString("PRONAME", "NV"));

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		host = new Hostname();

		userPref = getSharedPreferences("USER", MODE_PRIVATE);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		msgId = bundle.getString("ID");
		msgNametxt.setText(bundle.getString("TITLE"));
		proId = bundle.getString("PROID");

		actionBar.setTitle(Html.fromHtml("<font color='#424242'>"
				+ bundle.getString("TITLE") + "</font>"));
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
					showSnack(MessageDetail.this,
							"Enter comment!",
							"OK");
				} else {
					if (isInternetPresent) {
						sendBtn.setText("Adding comment.....");
						new SendComment().execute();
					} else {
						showSnack(MessageDetail.this,
								"Please check network connection!",
								"OK");
					}
				}

			}
		});
		scrollmsgs.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollmsgs.setFocusable(true);
		scrollmsgs.setFocusableInTouchMode(true);
		scrollmsgs.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});

		if (isInternetPresent) {
			new CallMessagesCommentsList().execute();
		} else {
			showSnack(MessageDetail.this,
					"Please check network connection!",
					"OK");
		}

	}

	public class CallMessagesCommentsList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
			progrLay.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonMessages = MsgsGet.callMsgsComments(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, msgId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
					commentsAdapter = new ListMsgCommentsBaseAdapter(
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
				showSnack(MessageDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}
	}

	@SuppressLint("NewApi")
	public class ListMsgCommentsBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;

		public ListMsgCommentsBaseAdapter(Context context2,
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
			pDialog.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jsonComment = new JSONObject();
			try {
				jsonComment.put("comment_detail", comntEdit.getText()
						.toString());
				jsonPushComment = CommentsPost.PostMesgsComment(jsonComment,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, msgId);
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
						commentsAdapter = new ListMsgCommentsBaseAdapter(
								getApplicationContext(), commentsList);
						commentsListview.setAdapter(commentsAdapter);
						comntEdit.setText("");
						commentsAdapter.notifyDataSetChanged();
						ListViewUtils.setDynamicHeight(commentsListview);

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
				sendBtn.setText("ADD THIS COMMENT");
			} else {
				showSnack(MessageDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}
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
		/*Intent i = new Intent(getApplicationContext(),
				ProjectActivityTaskGroupTabStrip.class);
		i.putExtra("proId", proId);
		i.putExtra("proName", proPref.getString("PRONAME", "NV"));
		startActivity(i);
		super.onBackPressed();*/
		finish();
	}
	public void showSnack(MessageDetail appHome, String stringMsg, String ok) {
		new SnackBar(MessageDetail.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}
}
