package mobile.teamwave.crm_activity;

import java.util.ArrayList;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CRMGetHttp;
import mobile.teamwave.application.Hostname;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

import mobile.teamwave.design.PagerSlidingTabStrip;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class PeopleOrganizationActivity extends ActionBarActivity {
	String hostname;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	MyPagerAdapter adapter;
	android.support.v7.app.ActionBar actionBar;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crm_people_org_activity);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		// getActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		actionBar.setTitle(Html.fromHtml("<font color='#000000'>Contacts</font>"));
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);

		// arrayUrl.add("discussions");

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "             PEOPLE          ", "   ORGANIZATIONS  " };

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
			return PeopleOrganizationTabs.newInstance(position);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			onBackPressed();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public static class PeopleOrganizationTabs extends Fragment {
		private int position, actionBarHeight;
		int currentPositionPeople;
		private static final String ARG_POSITION = "position";

		CircleProgressBar pDialog, loadingBtm;
		SharedPreferences userPref;
		JSONObject jsonPeople;
		JSONArray orgJArray;
		ListView listviewPeopleOrg;
		String nextPeople = "", hostname;
		ArrayList<HashMap<String, String>> listPeople = new ArrayList<HashMap<String, String>>();

		public static PeopleOrganizationTabs newInstance(int position) {
			PeopleOrganizationTabs f = new PeopleOrganizationTabs();
			Bundle b = new Bundle();
			b.putInt(ARG_POSITION, position);
			f.setArguments(b);
			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
			position = getArguments().getInt(ARG_POSITION);
		}

		@Override
		public View onCreateView(final LayoutInflater infaltor,
				final ViewGroup container, Bundle savedInstanceState) {
			Bundle b = getActivity().getIntent().getExtras();
			View v = infaltor.inflate(R.layout.crm_people_org_tabs, container,
					false);
			listviewPeopleOrg = (ListView) v.findViewById(R.id.listviewPeoOrg);
			userPref = getActivity().getSharedPreferences("USER", 0);
			pDialog = (CircleProgressBar) v.findViewById(R.id.pDialog);
			loadingBtm = (CircleProgressBar) v.findViewById(R.id.loadingBtm);
			pDialog.setColorSchemeResources(android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			loadingBtm.setColorSchemeResources(android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			Hostname host = new Hostname();
			hostname = host.globalVariable();

			if (position == 0) {
				// calling People list
				new PeopleList().execute();
			} else {
				new OrganizaitonsList().execute();
			}

			TypedValue tv = new TypedValue();

			if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv,
					true)) {
				actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
						getResources().getDisplayMetrics());
			}

			return v;

		}

		public class PeopleList extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				if (nextPeople == "" || nextPeople.equals("")) {
					pDialog.setVisibility(View.VISIBLE);
					loadingBtm.setVisibility(View.GONE);
				} else {
					loadingBtm.setVisibility(View.VISIBLE);
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					jsonPeople = CRMGetHttp.callPeopleList(hostname,
							userPref.getString("TOKEN", "NV"), nextPeople);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(jsonPeople);
				return null;
			}

			@Override
			protected void onPostExecute(Void unused) {
				if (jsonPeople != null) {
					try {
						JSONArray resultArr = jsonPeople.getJSONArray("results");

						for (int i = 0; i < resultArr.length(); i++) {
							JSONObject jsonPeop = resultArr.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("name", jsonPeop.getString("first_name"));
							System.out.println(jsonPeop.getString("id")
									+ " *********** ");
							map.put("person_id", jsonPeop.getString("id"));
							if (jsonPeop.isNull("company")) {
								map.put("orgname", "");
							} else {
								JSONObject jsonComp = jsonPeop
										.getJSONObject("company");
								map.put("orgname", jsonComp.getString("name"));
							}
							listPeople.add(map);
						}
						currentPositionPeople = listviewPeopleOrg
								.getLastVisiblePosition();
						ListPeopleBaseAdapter adapter = new ListPeopleBaseAdapter(
								getActivity(), listPeople);
						listviewPeopleOrg.setAdapter(adapter);
						DisplayMetrics displayMetrics = getResources()
								.getDisplayMetrics();
						int height = displayMetrics.heightPixels;
						System.out.println(height
								+ "-------------ACTION BAR HEIGHT--------------");
						listviewPeopleOrg.setSelectionFromTop(
								currentPositionPeople + 1, height - 220);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pDialog.setVisibility(View.GONE);
					loadingBtm.setVisibility(View.GONE);
				} else {
					pDialog.setVisibility(View.GONE);
					loadingBtm.setVisibility(View.GONE);

				}
				listviewPeopleOrg.setOnScrollListener(new OnScrollListener() {

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
						if (view.getId() == listviewPeopleOrg.getId()) {
							final int currentFirstVisibleItem = listviewPeopleOrg
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
						try {
							this.isScrollCompleted();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub

						this.currentFirstVisibleItem = firstVisibleItem;
						this.currentVisibleItemCount = visibleItemCount;
						this.totalItemCount = totalItemCount;

					}

					private void isScrollCompleted() throws JSONException {
						if (this.currentVisibleItemCount > 0
								&& this.currentScrollState == SCROLL_STATE_IDLE
								&& this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
							/***
							 * In this way I detect if there's been a scroll which
							 * has completed
							 ***/
							/*** do the work for load more date! ***/
							nextPeople = jsonPeople.getJSONObject("links")
									.getString("next");
							System.out.println(nextPeople + " !!!!!!!!!!!!!!!!!");
							if (nextPeople == null || nextPeople == ""
									|| nextPeople.equals("null")
									|| nextPeople.equals("")) {
								/*
								 * Toast.makeText(getActivity(),
								 * "There is no more people",
								 * Toast.LENGTH_LONG).show();
								 */
							} else {

								// nextPeople=hostname;

								new PeopleList().execute();

							}
						}
					}
				});
			}
		}

		@SuppressLint("NewApi")
		public class ListPeopleBaseAdapter extends BaseAdapter {
			ArrayList<HashMap<String, String>> listt;
			private LayoutInflater inflator;
			private Context context;
			PrettyTime prettyTime = new PrettyTime();;

			public ListPeopleBaseAdapter(FragmentActivity fragmentActivity,
					ArrayList<HashMap<String, String>> list) {
				// TODO Auto-generated constructor stub
				this.context = fragmentActivity;
				this.listt = list;
				inflator = (LayoutInflater) fragmentActivity
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
					convertView = inflator.inflate(R.layout.crm_people_list_row,
							null);
					holder = new ViewHolder();
					holder.namePeopleTxt = (TextView) convertView
							.findViewById(R.id.namePeopleTxt);
					holder.imagePeople = (ImageView) convertView
							.findViewById(R.id.imagePeople);
					holder.compnyNameTxt = (TextView) convertView
							.findViewById(R.id.compnyNameTxt);
					holder.clickPepLay = (RelativeLayout) convertView
							.findViewById(R.id.clickPepLay);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (listt.get(position).get("name") == null
						| listt.get(position).get("name") == "null") {
					holder.namePeopleTxt.setVisibility(View.INVISIBLE);
				} else {
					holder.namePeopleTxt.setText(listt.get(position).get("name"));
				}
				if (listt.get(position).get("orgname") == null
						| listt.get(position).get("orgname") == "null") {
					holder.compnyNameTxt.setVisibility(View.INVISIBLE);
				} else {
					holder.compnyNameTxt
							.setText(listt.get(position).get("orgname"));
				}
				holder.clickPepLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(),
								PeopleActivityDetail.class);
						i.putExtra("person_id", listt.get(position)
								.get("person_id"));
						startActivity(i);
					}
				});
				/*
				 * if (proLogourl == "" || proLogourl.equals("")) {
				 * holder.labelLay.setVisibility(View.VISIBLE);
				 * holder.proLogo.setVisibility(View.GONE);
				 * holder.labelTxt.setText(listt.get(position).get("proLabel")); }
				 * else { holder.labelLay.setVisibility(View.GONE);
				 * holder.proLogo.setVisibility(View.VISIBLE);
				 * imageLoader.displayImage(proLogourl, holder.proLogo, options); }
				 */

				return convertView;
			}

			public class ViewHolder {
				public TextView namePeopleTxt, compnyNameTxt;
				public ImageView imagePeople;
				RelativeLayout clickPepLay;
			}

		}

		public class OrganizaitonsList extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				pDialog.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					orgJArray = CRMGetHttp.callOrgaList(hostname,
							userPref.getString("TOKEN", "NV"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(orgJArray);
				return null;
			}

			@Override
			protected void onPostExecute(Void unused) {

				if (orgJArray != null) {

					if (orgJArray.toString() == "[]" || orgJArray.length() == 0) {

					} else {
						ArrayList<HashMap<String, String>> listOrg = new ArrayList<HashMap<String, String>>();
						for (int i = 0; i < orgJArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();

							try {
								JSONObject jsonOrg = orgJArray.getJSONObject(i);
								map.put("name", jsonOrg.getString("name"));

								if (jsonOrg.isNull("address")) {
									map.put("address", "");
								} else {
									map.put("address", jsonOrg.getString("address"));
								}
								map.put("org_id", jsonOrg.getString("id"));
								listOrg.add(map);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						ListOrgBaseAdapter adapter = new ListOrgBaseAdapter(
								getActivity(), listOrg);
						listviewPeopleOrg.setAdapter(adapter);

						pDialog.setVisibility(View.GONE);
						loadingBtm.setVisibility(View.GONE);

					}
				} else {
					pDialog.setVisibility(View.GONE);
					loadingBtm.setVisibility(View.GONE);
				}
			}
		}

		@SuppressLint("NewApi")
		public class ListOrgBaseAdapter extends BaseAdapter {
			ArrayList<HashMap<String, String>> listt;
			private LayoutInflater inflator;
			private Context context;
			PrettyTime prettyTime = new PrettyTime();;

			public ListOrgBaseAdapter(FragmentActivity fragmentActivity,
					ArrayList<HashMap<String, String>> list) {
				// TODO Auto-generated constructor stub
				this.context = fragmentActivity;
				this.listt = list;
				inflator = (LayoutInflater) fragmentActivity
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
					convertView = inflator.inflate(R.layout.crm_people_list_row,
							null);
					holder = new ViewHolder();
					holder.namePeopleTxt = (TextView) convertView
							.findViewById(R.id.namePeopleTxt);
					holder.imagePeople = (ImageView) convertView
							.findViewById(R.id.imagePeople);
					holder.addressNameTxt = (TextView) convertView
							.findViewById(R.id.compnyNameTxt);
					holder.clickPepLay = (RelativeLayout) convertView
							.findViewById(R.id.clickPepLay);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (listt.get(position).get("name") == null
						| listt.get(position).get("name") == "null") {
					holder.namePeopleTxt.setVisibility(View.INVISIBLE);
				} else {
					holder.namePeopleTxt.setText(listt.get(position).get("name"));
				}
				holder.clickPepLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(),
								OrgDetail.class);
						i.putExtra("org_id", listt.get(position)
								.get("org_id"));
						startActivity(i);
					}
				});

				holder.addressNameTxt.setText(listt.get(position).get("address"));

				holder.imagePeople.setVisibility(View.GONE);
				return convertView;
			}

			public class ViewHolder {
				public TextView namePeopleTxt, addressNameTxt;
				public ImageView imagePeople;
				RelativeLayout clickPepLay;
			}

		}
	}
}
