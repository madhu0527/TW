package mobile.teamwave.pm_activity;

import mobile.teamwave.crm_activity.AddDeal;
import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.SigninPost;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.crm_activity.CrmMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class Login extends Activity {
	TextView loginwithEmailTxt, forgotsubmitTxt;
	String email, password, hostname, token, emailForgott;
	JSONObject jsonForgot, jsonSignInResult;
	EditText emailEdit, passwordEdit;
	String checkPatternId = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	CircleProgressBar pdialog, pdialog1;
	SharedPreferences userPref;
	SharedPreferences.Editor editor;
	android.support.v7.app.ActionBar actionBar;
	Typeface typefaceLight;
	Hostname host;
	InternetConnectionDetector netConn;
	Boolean isInternetPresent = false;
	Dialog popup;
	ButtonRectangle login;
	ButtonFlat forgotPassTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signin_activity);
		login = (ButtonRectangle) findViewById(R.id.loginTxt);
		loginwithEmailTxt = (TextView) findViewById(R.id.loginwithEmailTxt);
		forgotPassTxt = (ButtonFlat) findViewById(R.id.forgotPassTxt);
		emailEdit = (EditText) findViewById(R.id.emailSignIn);
		passwordEdit = (EditText) findViewById(R.id.passwordSignIn);
		pdialog = (CircleProgressBar) findViewById(R.id.pDialog);
		pdialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		token = userPref.getString("TOKEN", "NV");

		netConn = new InternetConnectionDetector(this);
		isInternetPresent = netConn.isConnectingToInternet();
		if (token == "NV") {

		} else {
			Intent i = new Intent(Login.this, AddDeal.class);
			startActivity(i);
		}

		editor = userPref.edit();
		host = new Hostname();
		hostname = host.globalVariable();

		typefaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");
		// login.setTypeface(typefaceLight);
		emailEdit.setTypeface(typefaceLight);
		passwordEdit.setTypeface(typefaceLight);
		loginwithEmailTxt.setTypeface(typefaceLight);
		// forgotPassTxt.setTypeface(typefaceLight);
		emailEdit.setText("madhurao527@gmail.com");
		passwordEdit.setText("123456");

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				email = emailEdit.getText().toString();
				password = passwordEdit.getText().toString();
				if (email == null || email == "null" || email == ""
						|| email.length() == 0) {

					showSnack(Login.this, "Please enter your email!", "OK");

				} else if (email != null && !email.isEmpty()
						&& !email.matches(checkPatternId)) {

					showSnack(Login.this,
							"Please enter a valid email address!", "OK");

				} else if (password == null || password == "null"
						|| password == "" || password.length() == 0) {
					showSnack(Login.this, "Please enter your password!", "OK");
				} else {
					if (isInternetPresent) {
						new SignIn().execute();
					} else {
						showSnack(Login.this,
								"Please check network connection!", "OK");
					}
				}

			}
		});
		forgotPassTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popup = new Dialog(Login.this);
				popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
				popup.setContentView(R.layout.forgot_password_popup);
				popup.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;
				popup.getWindow().setBackgroundDrawableResource(
						android.R.color.transparent);

				forgotsubmitTxt = (TextView) popup.findViewById(R.id.submitTxt);
				TextView cancelTxt = (TextView) popup
						.findViewById(R.id.cancelTxt);
				final EditText emailForgot = (EditText) popup
						.findViewById(R.id.emailforgot);
				TextView loginwithEmailTxt = (TextView) popup
						.findViewById(R.id.loginwithEmailTxt);

				forgotsubmitTxt.setTypeface(typefaceLight);
				cancelTxt.setTypeface(typefaceLight);
				loginwithEmailTxt.setTypeface(typefaceLight);
				emailForgot.setTypeface(typefaceLight);

				pdialog1 = (CircleProgressBar) popup
						.findViewById(R.id.pDialog1);
				pdialog1.setColorSchemeResources(
						android.R.color.holo_green_light,
						android.R.color.holo_orange_light,
						android.R.color.holo_red_light);
				forgotsubmitTxt.setTypeface(typefaceLight);
				cancelTxt.setTypeface(typefaceLight);
				emailForgot.setTypeface(typefaceLight);
				loginwithEmailTxt.setTypeface(typefaceLight);

				forgotsubmitTxt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						emailForgott = emailForgot.getText().toString();
						if (emailForgott == "" || emailForgott == null
								|| emailForgott.isEmpty()
								|| !emailForgott.matches(checkPatternId)
								|| emailForgott.length() == 0) {
							showSnack(Login.this,
									"Please enter a valid email address!", "OK");
						} else if (isInternetPresent) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							new ForgotPass().execute();
						} else {
							showSnack(Login.this,
									"Please check network connection!", "OK");
						}
					}
				});
				cancelTxt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popup.dismiss();
					}
				});

				popup.show();
			}
		});

	}

	public class SignIn extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			login.setVisibility(View.GONE);
			pdialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonSignIn = new JSONObject();
			try {
				jsonSignIn.put("email", email);
				jsonSignIn.put("password", password);
				System.err.println(jsonSignIn + ":::::::::::::::::::");
				try {
					jsonSignInResult = SigninPost.makeRequestLogin(jsonSignIn,
							hostname);
					System.out.println(jsonSignIn + hostname);
					System.out.println("RESPONSE :::::::::::::::::::::"
							+ jsonSignInResult);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonSignInResult != null) {
				try {

					if (jsonSignInResult.has("token")) {
						token = jsonSignInResult.getString("token");
						Log.d("TOKEN : ",token);
						editor.putString("TOKEN", token);
						editor.commit();

						Intent i = new Intent(Login.this, AppHome.class);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_left);
					} else if (jsonSignInResult.has("non_field_errors")) {
						showSnack(Login.this, "Invalid credintails!", "OK");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				login.setVisibility(View.VISIBLE);
				pdialog.setVisibility(View.GONE);

			} else {
				login.setVisibility(View.VISIBLE);
				pdialog.setVisibility(View.GONE);
				showSnack(Login.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");

			}
		}
	}

	private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
	private long mBackPressed;

	@Override
	public void onBackPressed() {
		if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		} else {
			Toast.makeText(getBaseContext(), "Press again to close app",
					Toast.LENGTH_SHORT).show();
		}

		mBackPressed = System.currentTimeMillis();
	}

	public class ForgotPass extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pdialog1.setVisibility(View.VISIBLE);
			forgotsubmitTxt.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonforgot = new JSONObject();
			System.out.println(emailForgott);
			try {
				jsonforgot.put("email", emailForgott);

				try {
					jsonForgot = SigninPost.makeRequestForgot(jsonforgot,
							hostname);
					System.out.println(jsonforgot + hostname);
					System.out.println("RESPONSE :::::::::::::::::::::"
							+ jsonForgot);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonForgot != null) {
				if (jsonForgot.has("detail")) {
					showSnack(Login.this, "Invalid email!", "OK");
					pdialog1.setVisibility(View.GONE);
					forgotsubmitTxt.setVisibility(View.VISIBLE);
				} else if (jsonForgot.has("status")) {

					showSnack(Login.this,
							"Please check your mail for new password!", "OK");

					popup.dismiss();
					forgotsubmitTxt.setVisibility(View.VISIBLE);
					pdialog1.setVisibility(View.GONE);
				}

			} else {
				showSnack(Login.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
				pdialog1.setVisibility(View.GONE);
				forgotsubmitTxt.setVisibility(View.VISIBLE);
			}
		}
	}

	public void showSnack(Login login, String stringMsg, String ok) {
		new SnackBar(Login.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}

}
