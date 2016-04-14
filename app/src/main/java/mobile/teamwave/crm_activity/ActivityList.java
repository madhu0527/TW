package mobile.teamwave.crm_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import teamwave.android.mobile.teamwave.R;

public class ActivityList extends FragmentActivity {
	private android.app.ActionBar actionBar;
	protected DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crm_activities_list);
		moveDrawerToTop();
		initActionBar();
		initDrawer();
	}

	@SuppressLint({ "NewApi", "InflateParams" })
	private void moveDrawerToTop() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(
				R.layout.crm_activity_filter, null); // "null" is important.
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		LinearLayout container = (LinearLayout) drawer
				.findViewById(R.id.drawer_content); // This is the container we
													// defined just now.
		container.addView(child, 0);
		drawer.findViewById(R.id.right_drawer).setPadding(0, getStatusBarHeight(), 0,
				0);
		// Make the drawer replace the first child
		decor.addView(drawer);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		getActionBar().setDisplayShowHomeEnabled(false);
		actionBar.setTitle(Html
				.fromHtml("<font color='#000000'> Activities</font>"));

	}

	@SuppressLint("ResourceAsColor")
	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(createDrawerToggle());
	}

	private DrawerListener createDrawerToggle() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerStateChanged(int state) {
			}
		};

		return mDrawerToggle;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.crm_pipeline_spinner, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		case R.id.filerIcon:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
