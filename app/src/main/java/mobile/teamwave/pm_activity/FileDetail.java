package mobile.teamwave.pm_activity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CommentsPost;
import mobile.teamwave.Http.FilesGet;
import mobile.teamwave.application.FileDownloader;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.application.ListViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FileDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	ImageView fileImg;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	JSONArray jsonComments;
	JSONObject jsonFile;
	Hostname host;
	SharedPreferences userPref, proPref;
	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
	ListCommentsBaseAdapter commentsAdapter;
	ListView commentsListview;
	String fileUrl, proId, filetitle, fileId, commentDetail, firstName,
			lastName, commentImg, comntCreated, attachementUrl, downloadUrl,
			imgUrl, fileExt, fromTabs;
	TextView fileNametxt, proNameTxt;
	Button sendBtn;
	EditText comntEdit;
	JSONObject jsonPushComment;
	ScrollView scrollFiles;
	Typeface typefaceLight;
	CircleProgressBar pDialog;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	LinearLayout progrLay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_detail);
		fileImg = (ImageView) findViewById(R.id.fileImg);
		commentsListview = (ListView) findViewById(R.id.commentListview);
		fileNametxt = (TextView) findViewById(R.id.fileName);
		proNameTxt = (TextView) findViewById(R.id.proName);
		sendBtn = (Button) findViewById(R.id.addCmntBtn);
		comntEdit = (EditText) findViewById(R.id.comentEdittext);
		scrollFiles = (ScrollView) findViewById(R.id.scrollFiles);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		progrLay=(LinearLayout)findViewById(R.id.progrLay);
		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();

		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		host = new Hostname();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);

		typefaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoLight.ttf");
		fileNametxt.setTypeface(typefaceLight);
		proNameTxt.setTypeface(typefaceLight);
		sendBtn.setTypeface(typefaceLight);
		comntEdit.setTypeface(typefaceLight);

		// getOverflowMenu();
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		fileUrl = bundle.getString("fileUrl");
		fileId = bundle.getString("fileId");
		proId = (String) bundle.get("proId");
		filetitle = (String) bundle.get("title");
		fromTabs = bundle.getString("FROM_TABS");
		// String ext = (String) bundle.get("ext");
		// System.out.println(ext + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(fileUrl + " **************************************");

		if (isInternetPresent) {

			new FileDetailFromActivity().execute();

		} else {
			showSnack(FileDetail.this,
					"Please check network connection!",
					"OK");
		}

		fileNametxt.setText(filetitle);
		// actionBar.setTitle(filetitle);
		actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + filetitle
				+ "</font>"));

		proNameTxt.setText(proPref.getString("PRONAME", "NV"));

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
					showSnack(FileDetail.this,
							"Enter comment!",
							"OK");
				} else {
					if (isInternetPresent) {
						sendBtn.setText("Adding comment.....");
						new SendComment().execute();
					} else {
						showSnack(FileDetail.this,
								"Please check network connection!",
								"OK");
					}

				}

			}
		});
		scrollFiles
				.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollFiles.setFocusable(true);
		scrollFiles.setFocusableInTouchMode(true);
		scrollFiles.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});

		fileImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FileDetail.this, FileImageViewer.class);
				i.putExtra("imgUrl", imgUrl);
				i.putExtra("imgTitle", filetitle);
				startActivity(i);
			}
		});
		if (isInternetPresent) {
			new CallTasksCommentsList().execute();
		} else {
			showSnack(FileDetail.this,
					"Please check network connection!",
					"OK");
		}

	}

	public class FileDetailFromActivity extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
			progrLay.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonFile = FilesGet.callFilesDetail(host.globalVariable()
						+ fileUrl.substring(1),
						userPref.getString("TOKEN", "NV"), proId, fileId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			if (jsonFile != null) {
				System.out.println(jsonFile);
				try {

					fileExt = jsonFile.getString("ext");
					if (jsonFile.getString("ext") == "jpg"
							|| jsonFile.getString("ext").equals("jpg")
							|| jsonFile.getString("ext") == "jpeg"
							|| jsonFile.getString("ext").equals("jpeg")
							|| jsonFile.getString("ext") == "PNG"
							|| jsonFile.getString("ext").equals("PNG")
							|| jsonFile.getString("ext") == "png"
							|| jsonFile.getString("ext").equals("png")
							|| jsonFile.getString("ext") == "ico"
							|| jsonFile.getString("ext").equals("ico")) {
						fileImg.setVisibility(View.VISIBLE);
						imgUrl = jsonFile.getString("attachment");
						imageLoader.displayImage(imgUrl, fileImg, options);
						attachementUrl = jsonFile.getString("attachment");
						downloadUrl = jsonFile.getString("download_url");
						if (jsonFile.getString("thumbnail") == null
								|| jsonFile.getString("thumbnail") == "null") {

						} else {
							imageLoader.displayImage(
									jsonFile.getString("thumbnail"), fileImg,
									options);
						}
					//	pDialog.setVisibility(View.GONE);
					} else {
						fileImg.setVisibility(View.GONE);

						attachementUrl = jsonFile.getString("attachment");
						downloadUrl = jsonFile.getString("download_url");

						System.out.println(attachementUrl + filetitle);
					//	pDialog.setVisibility(View.GONE);
					//	progrLay.setVisibility(View.GONE);
						new DownloadFile().execute(attachementUrl, filetitle);

						/*
						 * fileImg.setVisibility(View.GONE); Intent intent = new
						 * Intent(Intent.ACTION_VIEW,
						 * Uri.parse(jsonFile.getString("attachment")));
						 * startActivity(intent);
						 */
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				showSnack(FileDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}

		}

	}

	public class CallTasksCommentsList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
		//	pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonComments = FilesGet.callFilesComments(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, fileId);
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
					scrollFiles.smoothScrollTo(0, 0);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			} else {
				Toast.makeText(
						getApplicationContext(),
						"Some thing went wrong with comments!please try again later!",
						2000).show();
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
				jsonPushComment = CommentsPost.PostFileComment(jsonComment,
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), proId, fileId);
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
						pDialog.setVisibility(View.GONE);
						progrLay.setVisibility(View.GONE);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				showSnack(FileDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
				progrLay.setVisibility(View.GONE);
			}
		}
	}

	private class DownloadFile extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... strings) {

			String fileUrl = strings[0]; // ->
											// http://maven.apache.org/maven-1.x/maven.pdf
			String fileName = strings[1]; // -> maven.pdf
			String extStorageDirectory = Environment
					.getExternalStorageDirectory().toString();
			File folder = new File(extStorageDirectory, "TeamWave");
			folder.mkdir();

			File pdfFile = new File(folder, fileName);

			try {
				pdfFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileDownloader.downloadFile(fileUrl, pdfFile);
			// mNotificationHelper.completed();
			return null;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.file_detail, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		case R.id.view:

			/*System.out.println(imgUrl + " __________________________________");
			if (imgUrl ==null||imgUrl=="null"){
				Toast.makeText(FileDetail.this,
						"file is loading...",
						Toast.LENGTH_SHORT).show();
			}else{*/
				if (fileExt == "jpg" || fileExt.equals("jpg") || fileExt == "jpeg"
						|| fileExt.equals("jpeg") || fileExt == "PNG"
						|| fileExt.equals("PNG") || fileExt == "png"
						|| fileExt.equals("png") || fileExt == "ico"
						|| fileExt.equals("ico")) {
					Intent i = new Intent(FileDetail.this, FileImageViewer.class);
					i.putExtra("imgTitle", filetitle);
					i.putExtra("imgUrl", imgUrl);
					startActivity(i);
				}else if(fileExt == "mp3" || fileExt.equals("mp3")||fileExt == "mp4" || fileExt.equals("mp4")) {
				//	Toast.makeText(getApplicationContext(), "Unable to view Audio,videos files",Toast.LENGTH_SHORT).show();
					showSnack(FileDetail.this,
							"Oops! Unable to view Audio,videos files!",
							"OK");
					
					
				}
				else {
					File pdfFile = new File(
							Environment.getExternalStorageDirectory()
									+ "/TeamWave/" + filetitle); // folder name =
																	// Teamwave //
																	// -> filename =
																	// maven.pdf
					Uri path = Uri.fromFile(pdfFile);
					Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
					pdfIntent.setDataAndType(path, "application/pdf");
					pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					try {
						startActivity(pdfIntent);
					} catch (ActivityNotFoundException e) {
						/*Toast.makeText(FileDetail.this,
								"No Application available to view PDF",
								Toast.LENGTH_SHORT).show();*/
						showSnack(FileDetail.this,
								"Oops! No Application available to view PDF!",
								"OK");
					}
			}
			
			return true;
		case R.id.download:
			System.out.println(downloadUrl + "   ^^^^^^^^^^^^^^^^^^^^^^^^^^");
			if (downloadUrl ==null||downloadUrl=="null"){
				/*Toast.makeText(FileDetail.this,
						"file is loading...",
						Toast.LENGTH_SHORT).show();*/
				showSnack(FileDetail.this,
						"Oops! file is loading...",
						"OK");
			}else{
				Uri downUrl = Uri.parse(downloadUrl);

				DownloadManager.Request r = new DownloadManager.Request(downUrl);

				// This put the download in the same Download dir the browser uses
				r.setDestinationInExternalPublicDir(
						Environment.DIRECTORY_DOWNLOADS, filetitle);

				// When downloading music and videos they will be listed in the
				// player
				// (Seems to be available since Honeycomb only)
				r.allowScanningByMediaScanner();

				// Notify user when download is completed
				// (Seems to be available since Honeycomb only)
				r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

				// Start download
				DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
				dm.enqueue(r);

				// new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
			}
			
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
	public void showSnack(FileDetail appHome, String stringMsg, String ok) {
		new SnackBar(FileDetail.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}
}
