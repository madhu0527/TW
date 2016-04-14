package mobile.teamwave.pm_activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.UserProfileGet;
import mobile.teamwave.application.ConvertDatetoTimeZone;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Profile extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	CircleProgressBar pDialog;
	JSONObject jsonUser;
	String hostname,lastLogin;
	Hostname host;
	SharedPreferences userPref;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	TextView nameTxt, orgTxt, emailTxt, titleTxt, lastLoginTxt;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	ImageView userImg;
	ConvertDatetoTimeZone timezone;
	PrettyTime prettyTime = new PrettyTime();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + "Profile"
				+ "</font>"));
		timezone = new ConvertDatetoTimeZone(Profile.this);
		
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		nameTxt=(TextView)findViewById(R.id.nameTxt);
		orgTxt=(TextView)findViewById(R.id.organizationTxt);
		emailTxt=(TextView)findViewById(R.id.emailTxt);
		lastLoginTxt=(TextView)findViewById(R.id.lastLoginTxt);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		userImg=(ImageView)findViewById(R.id.imageProfile);
		
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
		hostname = host.globalVariable();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();
		if (isInternetPresent) {
			new UserProfile().execute();
		} else {
			showSnack(Profile.this,
					"Please check network connection!",
					"OK");
		}
	}

	public class UserProfile extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute(Void unused) {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonUser = UserProfileGet.callProfile(hostname,
						userPref.getString("TOKEN", "NV"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			if (jsonUser != null) {
				System.out.println(jsonUser);

				try {
					nameTxt.setText(jsonUser.getString("full_name"));
					titleTxt.setText(jsonUser.getString("job_title"));
					emailTxt.setText(jsonUser.getString("email"));
					JSONObject organizationJson=jsonUser.getJSONObject("organization");
					orgTxt.setText(organizationJson.getString("name"));
					imageLoader.displayImage(jsonUser.getString("image"),
							userImg, options);
					lastLogin=jsonUser.getString("last_login");
					lastLogin = timezone
							.convertDateToTimeZone(lastLogin.substring(0, 18));
					lastLogin = lastLogin.replace("T", " ");

					lastLogin = lastLogin.substring(0, 19);
					long timee = timeStringtoMilis(lastLogin);
					
					lastLoginTxt.setText(prettyTime.format(new Date(
							timee)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pDialog.setVisibility(View.GONE);
			}else{
				showSnack(Profile.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pDialog.setVisibility(View.GONE);
			}
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void showSnack(Profile appHome, String stringMsg, String ok){
		new SnackBar(Profile.this,
				stringMsg,
				ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}
}