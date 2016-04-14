package mobile.teamwave.pm_activity;

import mobile.teamwave.Http.SigninPost;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.crm_activity.CrmMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import teamwave.android.mobile.teamwave.R;
import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AppHome extends Activity {
	// android.support.v7.app.ActionBar actionBar;
	ImageView pmImg, crmImg, hrmImg, invoiceImg, userimg, marketingImg,
			reportsImg;
	TextView prosTxt, crmtxt, hrmTxt, invoiceTxt, marketingTxt, reportsTxt;
	Button rertyBtn;
	Typeface typefaceRoman;
	int backgroundColor = Color.parseColor("#eeeeee");
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	SharedPreferences userPref;
	SharedPreferences.Editor editor;
	DisplayImageOptions options;
	String  userId, imageUrl;
	RelativeLayout progLay;
	JSONObject jsonUserData;
	Hostname host;
	CircleProgressBar pdialog;
	ImageLoader imageLoader;
	LinearLayout pmLayout,crmLayout,hrmLayout,invoiceLayout,marketingAutoLayout,reportsLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_home);
		pmImg = (ImageView) findViewById(R.id.prosImg);
		crmImg = (ImageView) findViewById(R.id.crmImg);
		hrmImg = (ImageView) findViewById(R.id.hrmImg);
		invoiceImg = (ImageView) findViewById(R.id.invoiceImg);
		userimg = (ImageView) findViewById(R.id.imageProfile);
		marketingImg = (ImageView) findViewById(R.id.marketingImg);
		reportsImg = (ImageView) findViewById(R.id.reportsImg);
		pdialog = (CircleProgressBar) findViewById(R.id.pDialog);
		prosTxt = (TextView) findViewById(R.id.prosTxt);
		crmtxt = (TextView) findViewById(R.id.crmTxt);
		hrmTxt = (TextView) findViewById(R.id.hrmTxt);
		invoiceTxt = (TextView) findViewById(R.id.invoiceTxt);
		marketingTxt = (TextView) findViewById(R.id.marketingTxt);
		reportsTxt = (TextView) findViewById(R.id.reportsTxt);
		 pmLayout = (LinearLayout) findViewById(R.id.prosLay);
		 crmLayout= (LinearLayout) findViewById(R.id.crmLay);
		 
		 hrmLayout = (LinearLayout) findViewById(R.id.hrmLay);
		 invoiceLayout= (LinearLayout) findViewById(R.id.invoiceLay);
		 marketingAutoLayout = (LinearLayout) findViewById(R.id.marketingLay);
		 reportsLayout= (LinearLayout) findViewById(R.id.reportsLay);
		 
		progLay = (RelativeLayout) findViewById(R.id.progLay);
		typefaceRoman = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");

		prosTxt.setTypeface(typefaceRoman);
		crmtxt.setTypeface(typefaceRoman);
		hrmTxt.setTypeface(typefaceRoman);
		invoiceTxt.setTypeface(typefaceRoman);
		marketingTxt.setTypeface(typefaceRoman);
		reportsTxt.setTypeface(typefaceRoman);

		host = new Hostname();

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();
		pdialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		editor = userPref.edit();
		if (isInternetPresent) {
			new GetCurrentUser().execute();
		} else {
			showSnack(AppHome.this,
					"Please check network connection!",
					"OK");
		}

		pmImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgComnt = SVGParser.getSVGFromResource(getResources(),
				R.raw.ico_pm);
		Drawable drwCmnt = svgComnt.createPictureDrawable();
		pmImg.setImageDrawable(drwCmnt);

		crmImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgCrm = SVGParser.getSVGFromResource(getResources(),
				R.raw.ico_crm);
		Drawable drwCrm = svgCrm.createPictureDrawable();
		crmImg.setImageDrawable(drwCrm);

		hrmImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svghrm = SVGParser.getSVGFromResource(getResources(),
				R.raw.ico_hrm);
		Drawable drwhrm = svghrm.createPictureDrawable();
		hrmImg.setImageDrawable(drwhrm);

		invoiceImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svginvoice = SVGParser.getSVGFromResource(getResources(),
				R.raw.ico_invoice);
		Drawable drwinvoice = svginvoice.createPictureDrawable();
		invoiceImg.setImageDrawable(drwinvoice);

		marketingImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgmarket = SVGParser.getSVGFromResource(getResources(),
				R.raw.ico_automation);
		Drawable drwmarket = svgmarket.createPictureDrawable();
		marketingImg.setImageDrawable(drwmarket);

		reportsImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG reportsComnt = SVGParser.getSVGFromResource(getResources(),
				R.raw.ico_reports);
		Drawable reports = reportsComnt.createPictureDrawable();
		reportsImg.setImageDrawable(reports);

	//	setOriginRiple(pmLayout);
	//	setOriginRiple(crmLayout);

		pmLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isInternetPresent) {
					Intent intent = new Intent(AppHome.this, PM_MainActivity.class);
					intent.putExtra("BACKGROUND", backgroundColor);
					startActivity(intent);
				} else {
					showSnack(AppHome.this,
							"Please check network connection!",
							"OK");
				}

			}
		});
		crmLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				/*showSnack(AppHome.this,
						"Coming soon!",
						"OK");*/
				
				if (isInternetPresent) {
					Intent intent = new Intent(AppHome.this, CrmMainActivity.class);
					intent.putExtra("BACKGROUND", backgroundColor);
					startActivity(intent);
				} else {
					showSnack(AppHome.this,
							"Please check network connection!",
							"OK");
				}

			}
		});
		hrmLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showSnack(AppHome.this,
						"Coming soon!",
						"OK");
			}
		});
		invoiceLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showSnack(AppHome.this,
						"Coming soon!",
						"OK");
			}
		});
		marketingAutoLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showSnack(AppHome.this,
						"Coming soon!",
						"OK");
			}
		});
		hrmLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showSnack(AppHome.this,
						"Coming soon!",
						"OK");
			}
		});
		userimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AppHome.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
			}
		});

	}

	private class GetCurrentUser extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pdialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonUserData = SigninPost.makeRequestCurrentUser(
						userPref.getString("TOKEN", "NV"),
						host.globalVariable());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			System.out.println(jsonUserData + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			if (jsonUserData != null) {
				try {

					if (jsonUserData.has("status_code")) {

					} else {
						imageUrl = jsonUserData.getString("image");
						System.out.println(userimg
								+ " ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
						imageLoader.displayImage(imageUrl, userimg, options);
						userId = jsonUserData.getString("id");
						 editor.putString("USER_ID", userId);
						 editor.commit();

						pdialog.setVisibility(View.GONE);
						progLay.setVisibility(View.GONE);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				showSnack(AppHome.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pdialog.setVisibility(View.GONE);
				progLay.setVisibility(View.GONE);
			}
		}
	}

	private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
	// time passed between two
	// back presses.
	private long mBackPressed;

	@Override
	public void onBackPressed() {
		if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);

		} else {
			Toast.makeText(getBaseContext(), "Press again to close TeamWave",
					Toast.LENGTH_SHORT).show();
		}
		mBackPressed = System.currentTimeMillis();
	}
	public void showSnack(AppHome appHome, String stringMsg, String ok){
		new SnackBar(AppHome.this,
				stringMsg,
				ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}
	
}
