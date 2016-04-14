package mobile.teamwave.pm_activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mobile.teamwave.design.PagerSlidingTabStrip;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.application.Hostname;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class ProjectActivityTaskGroupTabStrip extends ActionBarActivity {
	String hostname;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	MyPagerAdapter adapter;
	public static ArrayList<String> arrayUrl = new ArrayList<String>();
	android.support.v7.app.ActionBar actionBar;
	SharedPreferences prefPro;
	SharedPreferences.Editor editor;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_activity_taskgrp_tabstrip);

		prefPro = getSharedPreferences("PRO_DETAIL", MODE_PRIVATE);
		editor = prefPro.edit();

		getOverflowMenu();

		actionBar = getSupportActionBar();
		actionBar.setElevation(0);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		// getActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		actionBar.setTitle(Html.fromHtml("<font color='#000000'>"
				+ prefPro.getString("PRONAME", "NV") + "</font>"));
		// actionBar.setTitle(bundle.getString("proName"));
		editor.putString("PRONAME", bundle.getString("proName"));
		editor.commit();
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		Hostname host = new Hostname();
		hostname = host.globalVariable();
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);

		arrayUrl.add("projects");
		// arrayUrl.add("discussions");

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Activities", "Tasks", "Discussions",
				"Files", "Notes", "Events" };
		/*private final String[] TITLES = { "Activities", "Tasks", "Discussions",
				"Files", "Notes", "Time Log", "Events" };*/

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return ProjectActivityTabs.newInstance(position);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_to_cal, menu);

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
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(ProjectActivityTaskGroupTabStrip.this,
				PM_MainActivity.class);
		startActivity(i);
		super.onBackPressed();
	}
}
