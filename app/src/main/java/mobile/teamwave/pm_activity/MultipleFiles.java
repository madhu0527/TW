package mobile.teamwave.pm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.FilesGet;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;

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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MultipleFiles extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	CircleProgressBar pDialog;
	JSONObject jsonFiles;
	String nextCallFiles = "", hostname, proId, dayStr;
	Hostname host;
	SharedPreferences userPref, proPref;
	ArrayList<HashMap<String, String>> filesList = new ArrayList<HashMap<String, String>>();
	ListView listviewFiles;
	int currentPositionFiles;
	Typeface typefaceRoman;
	ListFilesBaseAdapter adapterFiles;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_files_list);

		listviewFiles = (ListView) findViewById(R.id.listviewFiles);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
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
		actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + "Files"
				+ "</font>"));
		typefaceRoman = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		proPref = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);

		host = new Hostname();
		hostname = host.globalVariable();
		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		proId = (String) bundle.get("proId");
		System.out.println(proId + "********   PRO ID *********");

		if (isInternetPresent) {
			new ProjectFilesList().execute();
		} else {
			showSnack(MultipleFiles.this,
					"Please check network connection!",
					"OK");
		}

	}

	// FILES LIST
	public class ProjectFilesList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
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

						currentPositionFiles = listviewFiles
								.getLastVisiblePosition();
						adapterFiles = new ListFilesBaseAdapter(
								getApplicationContext(), filesList);
						listviewFiles.setAdapter(adapterFiles);
						pDialog.setVisibility(View.GONE);
						DisplayMetrics displayMetrics = getResources()
								.getDisplayMetrics();
						int height = displayMetrics.heightPixels;
						System.out
								.println(height
										+ "-------------ACTION BAR HEIGHT--------------");
						listviewFiles.setSelectionFromTop(
								currentPositionFiles + 1, height - 220);
					}

				} else {
					pDialog.setVisibility(View.GONE);
					showSnack(MultipleFiles.this,
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
				}

				listviewFiles.setOnScrollListener(new OnScrollListener() {

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
						if (view.getId() == listviewFiles.getId()) {
							final int currentFirstVisibleItem = listviewFiles
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
							/*	Toast.makeText(getApplicationContext(),
										"There is no more files!",
										Toast.LENGTH_LONG).show();*/
								showSnack(MultipleFiles.this,
										"Oops! There is no more files!",
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
								// nextCallDiscusion = "NEXT";

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

		public ListFilesBaseAdapter(Context context2,
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
					Intent i = new Intent(getApplicationContext(),
							FileDetail.class);
					i.putExtra("fileUrl",
							listt.get(position).get("resource_url"));
					i.putExtra("proId", listt.get(position).get("PROID"));
					i.putExtra("fileId", listt.get(position).get("FILEID"));
					i.putExtra("title", listt.get(position).get("TITLE"));
					i.putExtra("ext", listt.get(position).get("EXTENSION"));

					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
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
	/*		if (listt.get(position).get("THUMBNAIL") == null
					|| listt.get(position).get("THUMBNAIL").equals("null")
					|| listt.get(position).get("THUMBNAIL") == "null"
					|| listt.get(position).get("THUMBNAIL").equals(null)) {*/

				if (listt.get(position).get("EXTENSION") == "doc"
						|| listt.get(position).get("EXTENSION").equals("doc")) {

					SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
							R.raw.files_docs);
					Drawable draNotes = svgNotes.createPictureDrawable();
					holder.fileImg.setImageDrawable(draNotes);
					/*
					 * imageLoader .displayImage(
					 * "https://cdn4.iconfinder.com/data/icons/free-colorful-icons/360/google_docs.png"
					 * , holder.fileImg, options);
					 */
				} else if (listt.get(position).get("EXTENSION") == "pdf"
						|| listt.get(position).get("EXTENSION").equals("pdf")) {

					SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
							R.raw.files_pdf);
					Drawable draNotes = svgNotes.createPictureDrawable();
					holder.fileImg.setImageDrawable(draNotes);
					/*
					 * imageLoader.displayImage(
					 * "http://bjm.scs.illinois.edu/images/pdficon.png",
					 * holder.fileImg, options);
					 */
				} else if (listt.get(position).get("EXTENSION") == "jpg"
						|| listt.get(position).get("EXTENSION").equals("jpg")
						|| listt.get(position).get("EXTENSION") == "jpeg"
						|| listt.get(position).get("EXTENSION").equals("jpeg")) {
					SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
							R.raw.files_jpg);
					Drawable draNotes = svgNotes.createPictureDrawable();
					holder.fileImg.setImageDrawable(draNotes);
					/*
					 * imageLoader .displayImage(
					 * "http://icons.iconarchive.com/icons/untergunter/leaf-mimes/512/jpeg-icon.png"
					 * , holder.fileImg, options);
					 */
				} else if (listt.get(position).get("EXTENSION") == "PNG"
						|| listt.get(position).get("EXTENSION").equals("PNG")
						|| listt.get(position).get("EXTENSION") == "png"
						|| listt.get(position).get("EXTENSION").equals("png")) {
					SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
							R.raw.files_png);
					Drawable draNotes = svgNotes.createPictureDrawable();
					holder.fileImg.setImageDrawable(draNotes);
					/*
					 * imageLoader .displayImage(
					 * "http://d.lanrentuku.com/down/png/1206/plump-by-zerode/filetype-png-icon.png"
					 * , holder.fileImg, options);
					 */
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
					/*
					 * imageLoader .displayImage(
					 * "https://cdn1.iconfinder.com/data/icons/files-3/24/File-TXT-512.png"
					 * , holder.fileImg, options);
					 */
				} else if (listt.get(position).get("EXTENSION") == "csv"
						|| listt.get(position).get("EXTENSION").equals("csv")) {
					SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
							R.raw.files_csv);
					Drawable draNotes = svgNotes.createPictureDrawable();
					holder.fileImg.setImageDrawable(draNotes);
					/*
					 * imageLoader .displayImage(
					 * "https://cdn1.iconfinder.com/data/icons/files-3/24/File-TXT-512.png"
					 * , holder.fileImg, options);
					 */
				} else if (listt.get(position).get("EXTENSION") == "xlsx"
						|| listt.get(position).get("EXTENSION").equals("xlsx")) {
					SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
							R.raw.files_xlsx);
					Drawable draNotes = svgNotes.createPictureDrawable();
					holder.fileImg.setImageDrawable(draNotes);
					/*
					 * imageLoader .displayImage(
					 * "https://cdn1.iconfinder.com/data/icons/files-3/24/File-TXT-512.png"
					 * , holder.fileImg, options);
					 */
				}
				else if (listt.get(position).get("EXTENSION") == "mp3"
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
				// imageLoader.displayImage("https://www.millerfallprotection.com/images/module/document-icon.png",
				// holder.fileImg, options);
			/*} else {
				imageLoader.displayImage(listt.get(position).get("THUMBNAIL"),
						holder.fileImg, options);
			}*/

				holder.fileTitleTxt.setText(Html.fromHtml(listt.get(position).get("TITLE")
						+ " " + "<font color=#c0c0c0>" + "<small>"
						+ listt.get(position).get("SIZE")+ " KB" + "</small>"
						+ "</font>"));
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
		finish();
	}
	public void showSnack(MultipleFiles appHome, String stringMsg, String ok) {
		new SnackBar(MultipleFiles.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}
}
