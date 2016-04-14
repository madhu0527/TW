package mobile.teamwave.crm_activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CRMGetHttp;
import mobile.teamwave.application.ConvertDatetoTimeZone;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.application.InternetConnectionDetector;
import mobile.teamwave.pm_activity.AppHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.viewpagerindicator.CirclePageIndicator;

public class CrmMainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {
	protected DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;
	private ActionBar actionBar;
	SharedPreferences userPref;
	SharedPreferences.Editor editor, editor1;
	Hostname host;
	ImageView progresImg, logoImg, homeScreenImg, contactImg, dealsImg;
	Typeface typefaceRoman, typeFaceMeduim;
	CircleProgressBar pDialog;
	ConvertDatetoTimeZone timezone;
	InternetConnectionDetector netConn;
	SwipeRefreshLayout mSwipeRefreshLayout;
	Boolean isInternetPresent = false;
	LinearLayoutManager layoutManager;
	JSONObject defPipelinesJson, jsonPipes;
	ViewPager viewPager;
	CirclePageIndicator mIndicator;
	Spinner stagesSpin, dealTitle;
	Map<String, List<String>> dealsMap = new HashMap<String, List<String>>();
	ArrayList<HashMap<String, String>> stagesList = new ArrayList<HashMap<String, String>>();
	ListView dealsListview;
	String stageId, stageIdTemp = "", userId, userType = "MYDEALS",
			filterType = "open", pipeLineId, pipeLineType = "default";
	TextView dealsCountTxt, totDealCostTxt;
	private MenuItem mSpinnerItem1 = null;
	Spinner spinnerPipes;
	JSONArray pipleLinesArr;
	ArrayAdapter<String> pipesAdapter;
	ArrayList<String> pipesList = new ArrayList<String>();
	ImageView closeImg;
	RelativeLayout closeFilter;
	LayoutRipple allUserDealsLay, mydealsLay, allOpenLay, allLostLay,
			allWonLay, rottenLay, contactsLay,activityLay;
	RadioButton allUsersRadioBtn, myDealsRadioBtn, allOpenDealsBtn,
			allLostDealsRadioBtn, allWondDealsRadioBtn, allRottenDealsRadioBtn;
	ButtonFlat resetBtn;
	ArrayList<HashMap<String, String>> pipeLineList = new ArrayList<HashMap<String, String>>();
    ButtonFloat addBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crm_main_activity);
		// getOverflowMenu();
		moveDrawerToTop();
		initActionBar();
		initDrawer();

		// to open drawer
		// mDrawerLayout.openDrawer(Gravity.START);
		timezone = new ConvertDatetoTimeZone(CrmMainActivity.this);
	}

	@SuppressLint({ "NewApi", "InflateParams" })
	private void moveDrawerToTop() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(
				R.layout.crm_menu, null); // "null" is important.
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		LinearLayout container = (LinearLayout) drawer
				.findViewById(R.id.drawer_content); // This is the container we
													// defined just now.
		container.addView(child, 0);
		drawer.findViewById(R.id.topBar).setPadding(0, getStatusBarHeight(), 0,
				0);
		// Make the drawer replace the first child
		decor.addView(drawer);
		logoImg = (ImageView) findViewById(R.id.logo);
		progresImg = (ImageView) findViewById(R.id.calenderImg);

		homeScreenImg = (ImageView) findViewById(R.id.homeScreenImg);
		contactImg = (ImageView) findViewById(R.id.peopleImg);
		dealsImg = (ImageView) findViewById(R.id.dealsImg);

		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		// stagesSpin = (Spinner) findViewById(R.id.stagesSpin);
		dealsListview = (ListView) findViewById(R.id.listviewDeals);
		// dealsCountTxt=(TextView)findViewById(R.id.dealsCountTxt);

		viewPager = (ViewPager) findViewById(R.id.pagerBusHome);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		allUsersRadioBtn = (RadioButton) findViewById(R.id.allUsersRadioBtn);
		myDealsRadioBtn = (RadioButton) findViewById(R.id.myDealsRadioBtn);

		allOpenDealsBtn = (RadioButton) findViewById(R.id.allOpenDealsBtn);
		allLostDealsRadioBtn = (RadioButton) findViewById(R.id.allLostDealsRadioBtn);
		allWondDealsRadioBtn = (RadioButton) findViewById(R.id.allWondDealsRadioBtn);
		allRottenDealsRadioBtn = (RadioButton) findViewById(R.id.allRottenDealsRadioBtn);
		addBtn = (ButtonFloat)findViewById(R.id.addBtn);
		typefaceRoman = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_Roman.ttf");
		typeFaceMeduim = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTStd_md.ttf");
		myDealsRadioBtn.setChecked(true);
		allOpenDealsBtn.setChecked(true);

		netConn = new InternetConnectionDetector(CrmMainActivity.this);
		isInternetPresent = netConn.isConnectingToInternet();

		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		// setting SVG Image for Events Image
		logoImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgLogo = SVGParser.getSVGFromResource(getResources(),
				R.raw.logo);
		Drawable drawLogo = svgLogo.createPictureDrawable();
		logoImg.setImageDrawable(drawLogo);

		// setting SVG Image for Events Image
		contactImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgcontact = SVGParser.getSVGFromResource(getResources(),
				R.raw.contacts);
		Drawable drawcontact = svgcontact.createPictureDrawable();
		contactImg.setImageDrawable(drawcontact);

		// setting SVG Image for HOme Image
		progresImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svg = SVGParser.getSVGFromResource(getResources(),
				R.raw.progress);
		Drawable drawable = svg.createPictureDrawable();
		progresImg.setImageDrawable(drawable);

		// setting SVG Image for Events Image
		dealsImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svgdeals = SVGParser.getSVGFromResource(getResources(),
				R.raw.ic_deals);
		Drawable drawdeals = svgdeals.createPictureDrawable();
		dealsImg.setImageDrawable(drawdeals);

		homeScreenImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG svghomeScreen = SVGParser.getSVGFromResource(getResources(),
				R.raw.app_home_icon);
		Drawable drawhomeScreen = svghomeScreen.createPictureDrawable();
		homeScreenImg.setImageDrawable(drawhomeScreen);

		closeImg = (ImageView) findViewById(R.id.closeImg);
		closeFilter = (RelativeLayout) findViewById(R.id.closeFilter);
		// close image
		closeImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		SVG Img = SVGParser.getSVGFromResource(getResources(),
				R.raw.ic_close);
		Drawable emailIg = Img.createPictureDrawable();
		closeImg.setImageDrawable(emailIg);

		closeFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
			}
		});

		homeScreenImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(CrmMainActivity.this, AppHome.class);
                startActivity(i);
            }
        });

		allUserDealsLay = (LayoutRipple) findViewById(R.id.allUsersLay);
		mydealsLay = (LayoutRipple) findViewById(R.id.mydealsLay);
		allOpenLay = (LayoutRipple) findViewById(R.id.allOpenLay);
		allLostLay = (LayoutRipple) findViewById(R.id.allLostLay);
		allWonLay = (LayoutRipple) findViewById(R.id.allWonLay);
		rottenLay = (LayoutRipple) findViewById(R.id.allRottenLay);
		resetBtn = (ButtonFlat) findViewById(R.id.resetTxt);
		contactsLay = (LayoutRipple) findViewById(R.id.contactsLay);
		activityLay = (LayoutRipple) findViewById(R.id.activityLay);
		contactsLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isInternetPresent) {
					Intent intent = new Intent(CrmMainActivity.this,
							PeopleOrganizationActivity.class);
					startActivity(intent);
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}
			}
		});
		activityLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isInternetPresent) {
					Intent intent = new Intent(CrmMainActivity.this,
							ActivityList.class);
					startActivity(intent);
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}
			}
		});

		allUserDealsLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				mDrawerLayout.closeDrawers();
				allUsersRadioBtn.setChecked(true);
				myDealsRadioBtn.setChecked(false);
				pipeLineType = "default";
				userType = "ALLUSERDEALS";

				// userId = "open";
				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});
		mydealsLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				myDealsRadioBtn.setChecked(true);
				allUsersRadioBtn.setChecked(false);
				pipeLineType = "default";
				userType = "MYDEALS";
				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});

		allOpenLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				allOpenDealsBtn.setChecked(true);
				allLostDealsRadioBtn.setChecked(false);
				allWondDealsRadioBtn.setChecked(false);
				allRottenDealsRadioBtn.setChecked(false);
				filterType = "open";

				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});
		allLostLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				allLostDealsRadioBtn.setChecked(true);
				allOpenDealsBtn.setChecked(false);
				allWondDealsRadioBtn.setChecked(false);
				allRottenDealsRadioBtn.setChecked(false);

				filterType = "lost";

				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});
		allWonLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				allWondDealsRadioBtn.setChecked(true);
				allOpenDealsBtn.setChecked(false);
				allLostDealsRadioBtn.setChecked(false);
				allRottenDealsRadioBtn.setChecked(false);
				filterType = "won";

				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});
		rottenLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				allRottenDealsRadioBtn.setChecked(true);
				allOpenDealsBtn.setChecked(false);
				allLostDealsRadioBtn.setChecked(false);
				allWondDealsRadioBtn.setChecked(false);
				filterType = "rotten";

				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});
		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				userType = "MYDEALS";
				filterType = "open";
				pipeLineType = "default";
				myDealsRadioBtn.setChecked(true);
				allOpenDealsBtn.setChecked(true);

				allUsersRadioBtn.setChecked(false);
				allLostDealsRadioBtn.setChecked(false);
				allWondDealsRadioBtn.setChecked(false);
				allRottenDealsRadioBtn.setChecked(false);
				if (isInternetPresent) {
					new DefaultPipeLineDeals().execute();
				} else {
					showSnack(CrmMainActivity.this,
							"Please check network connection!", "OK");
				}

			}
		});
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CrmMainActivity.this,AddDeal.class);
                startActivity(i);
            }
        });
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		userId = userPref.getString("USER_ID", "NV");
		editor = userPref.edit();

		host = new Hostname();
		// Default pipeline & deals
		if (isInternetPresent) {
			new DefaultPipeLineDeals().execute();
			new PipeLines().execute();
		} else {
			showSnack(CrmMainActivity.this, "Please check network connection!",
					"OK");
		}
	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setTitle(Html.fromHtml("<font color='#000000'>Deals</font>"));

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

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mDrawerToggle.syncState();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {

		case R.id.filerIcon:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			return true;

		default:
			return super.onOptionsItemSelected(item);
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

	// private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
	// time passed between two
	// back presses.
	// private long mBackPressed;

	@Override
	public void onBackPressed() {
		finish();
	}

	// Default pipeline & deals

	public class DefaultPipeLineDeals extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				defPipelinesJson = CRMGetHttp.callDefaultPipelines(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"),
						userPref.getString("USER_ID", "NV"), pipeLineType,
						userType, filterType);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			System.out
					.println(defPipelinesJson + "!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			if (defPipelinesJson != null) {
				JSONArray stagesArr;
				try {
					stagesArr = defPipelinesJson.getJSONArray("stages");
					ArrayList<HashMap<String, String>> stagesNamesList = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < stagesArr.length(); i++) {
						HashMap<String, String> mapStage = new HashMap<String, String>();
						JSONObject stageJson = stagesArr.getJSONObject(i);

						JSONArray jsonDealArr = stageJson.getJSONArray("deals");
						String stageName = stageJson.getString("title");
						mapStage.put("title", stageName);
						stagesNamesList.add(mapStage);
						ArrayList<String> listDealsdata = new ArrayList<String>();
						for (int j = 0; j < jsonDealArr.length(); j++) {

							listDealsdata.add(jsonDealArr.get(j).toString());

						}
						List<String> dealsList = new ArrayList<String>(
								listDealsdata);
						dealsMap.put(stageName, dealsList);
					}
					System.out.println(dealsMap);
					ViewPagAdapter adapte = new ViewPagAdapter(
							CrmMainActivity.this, stagesNamesList);

					viewPager.setAdapter(adapte);
					mIndicator.setViewPager(viewPager);

					pDialog.setVisibility(View.GONE);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				showSnack(CrmMainActivity.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
			}
		}

	}

	public class ViewPagAdapter extends PagerAdapter {
		ArrayList<HashMap<String, String>> Stageslist;
		private Context context;
		private LayoutInflater inflater;
		String busiUrl;
		ListPipelinesBaseAdapter adapter;
		ListView dealsListview;

		public ViewPagAdapter(CrmMainActivity businessHome,
				ArrayList<HashMap<String, String>> Stageslist) {
			// TODO Auto-generated constructor stub
			this.context = businessHome;
			this.Stageslist = Stageslist;
			inflater = (LayoutInflater) businessHome
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Stageslist.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			// TODO Auto-generated method stub
			return view == ((LinearLayout) arg1);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.stage_names_row,
					container, false);
			TextView titleTxt;
			titleTxt = (TextView) itemView.findViewById(R.id.stageNameTxt);

			titleTxt.setText(Stageslist.get(position).get("title").toString());

			dealsListview = (ListView) itemView
					.findViewById(R.id.listviewDeals);

			ArrayList<HashMap<String, String>> dealsListt = new ArrayList<HashMap<String, String>>();
			List<String> dealslist = dealsMap.get(Stageslist.get(position)
					.get("title").toString());

			System.err.println("   *************************    " + dealslist);

			for (int j = 0; j < dealslist.size(); j++) {
				HashMap<String, String> map = new HashMap<String, String>();
				String json = dealslist.get(j);
				try {
					JSONObject jsonDeal = new JSONObject(json);
					System.out.println(jsonDeal);
					map.put("DealTile", jsonDeal.getString("title"));
					map.put("Dealvalue", jsonDeal.getString("deal_value"));
					map.put("Dealid", jsonDeal.getString("id"));
					if (jsonDeal.isNull("company")) {
						System.out.println(" NULLL  !!!!!!!!!!!!!!!!!!!!!!!!");
						map.put("DealCompanyName", "");
					} else {
						System.out
								.println(" NOT NULL  !!!!!!!!!!!!!!!!!!!!!!!!");
						JSONObject jsonCompany;

						jsonCompany = jsonDeal.getJSONObject("company");
						System.out.println(jsonCompany
								+ "!!!!!!!!!!!!!!!!!!!!!!!!");

						map.put("DealCompanyName",
								jsonCompany.getString("name"));
						if (jsonDeal.isNull("currency")) {
							map.put("currency", "");
						} else {
							JSONObject jsonCurency = jsonDeal
									.getJSONObject("currency");
							map.put("currency", jsonCurency.getString("symbol"));
						}
					}
					dealsListt.add(map);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * ListPipelinesBaseAdapter adapterDeals = new
			 * ListPipelinesBaseAdapter( CrmMainActivity.this, dealsListt);
			 * dealsListview.setAdapter(adapterDeals);
			 */

			ListPipelinesBaseAdapter adapterDeals = new ListPipelinesBaseAdapter(
					context, dealsListt);
			dealsListview.setAdapter(adapterDeals);

			// Add viewpager_item.xml to ViewPager
			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Remove viewpager_item.xml from ViewPager
			((ViewPager) container).removeView((LinearLayout) object);
		}
	}

	@SuppressLint("NewApi")
	public class ListPipelinesBaseAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;
		private Context context;
		LinearLayout dealClickLay;

		public ListPipelinesBaseAdapter(Context context2,
				ArrayList<HashMap<String, String>> list) {
			// TODO Auto-generated constructor stub
			this.context = context2;
			this.listt = list;
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
				convertView = inflator.inflate(R.layout.crm_deals_list_row,
						null);
				holder = new ViewHolder();
				holder.dealNameTxt = (TextView) convertView
						.findViewById(R.id.dealNameTxt);
				holder.dealPriceTxt = (TextView) convertView
						.findViewById(R.id.dealPriceTxt);
				holder.viewPager = (ViewPager) convertView
						.findViewById(R.id.pagerBusHome);
				dealClickLay = (LinearLayout) convertView
						.findViewById(R.id.dealClickLay);
				holder.mIndicator = (CirclePageIndicator) convertView
						.findViewById(R.id.indicator);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			dealClickLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, DealDetail.class);
					i.putExtra("Dealid",listt.get(position).get("Dealid"));
					startActivity(i);
				}
			});

			holder.dealNameTxt.setTypeface(typefaceRoman);
			holder.dealPriceTxt.setTypeface(typefaceRoman);
			holder.dealNameTxt.setText(listt.get(position).get("DealTile"));

			if (listt.get(position).get("DealCompanyName") == ""
					|| listt.get(position).get("DealCompanyName").equals("")
					|| listt.get(position).get("DealCompanyName") == null
					|| listt.get(position).get("DealCompanyName") == "null") {
				holder.dealPriceTxt.setText(listt.get(position)
						.get("Dealvalue"));
			} else {
				holder.dealPriceTxt.setText(listt.get(position)
						.get("Dealvalue")
						+ " , "
						+ listt.get(position).get("DealCompanyName"));
			}

			return convertView;
		}

		public class ViewHolder {
			public TextView dealNameTxt, dealPriceTxt;
			ViewPager viewPager;
			CirclePageIndicator mIndicator;
		}
	}

	// List pipeline

	public class PipeLines extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				pipleLinesArr = CRMGetHttp.callPipelines(host.globalVariable(),
						userPref.getString("TOKEN", "NV"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void unused) {
			System.out.println(pipleLinesArr + "!!!!!!!!!!!!!!!!!!!!!!!!!!");
			if (pipleLinesArr == null || pipleLinesArr.toString() == null
					|| pipleLinesArr.length() == 0) {
				showSnack(CrmMainActivity.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
			} else {
				for (int i = 0; i < pipleLinesArr.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					try {
						jsonPipes = pipleLinesArr.getJSONObject(i);
						map.put("pipeTitle", jsonPipes.getString("title"));
						map.put("pipeId", jsonPipes.getString("id"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pipeLineList.add(map);
				}
				// title drop down adapter
				// Enabling Spinner dropdown navigation
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				PipelineAdapter adapter = new PipelineAdapter(
						CrmMainActivity.this, pipeLineList);
				// assigning the spinner navigation
				actionBar.setListNavigationCallbacks(adapter,
						CrmMainActivity.this);

			}
		}
	}

	public class PipelineAdapter extends BaseAdapter {
		private Context context;
		ArrayList<HashMap<String, String>> listt;
		private LayoutInflater inflator;

		public PipelineAdapter(CrmMainActivity context,
				ArrayList<HashMap<String, String>> pipeLineList) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.listt = pipeLineList;
			inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listt.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.crm_pipeline_row, null);
				holder = new ViewHolder();
				holder.pipleLineNameTxt = (TextView) convertView
						.findViewById(R.id.txtTitle);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.pipleLineNameTxt.setText(listt.get(position)
					.get("pipeTitle"));

			return convertView;
		}

		public class ViewHolder {
			public TextView pipleLineNameTxt;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.crm_pipeline_spinner, menu);

		return true;
	}

	public void showSnack(CrmMainActivity login, String stringMsg, String ok) {
		new SnackBar(CrmMainActivity.this, stringMsg, ok,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}).show();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		pipeLineId = pipeLineList.get(itemPosition).get("pipeId");
		System.out.println(pipeLineId + " : PIPELINE ID");
		pipeLineType = pipeLineId;
		if (isInternetPresent) {
			new DefaultPipeLineDeals().execute();
		} else {
			showSnack(CrmMainActivity.this, "Please check network connection!",
					"OK");
		}
		return false;
	}
}