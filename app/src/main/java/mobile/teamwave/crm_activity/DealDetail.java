package mobile.teamwave.crm_activity;

import java.util.ArrayList;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.CrmPojos.CrmDealDetail;
import mobile.teamwave.Http.CRMGetHttp;
import mobile.teamwave.application.Hostname;
import mobile.teamwave.crm_activity.CrmMainActivity.ListPipelinesBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mobile.teamwave.design.PagerSlidingTabStrip;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DealDetail extends ActionBarActivity {
	android.support.v7.app.ActionBar actionBar;
	PagerSlidingTabStrip tabs;
	ViewPager pager, tabsPager;
	String dealId, stageId, stageResponse, dealStageId;
	int stagePos;
	JSONObject jsonDeal, jsonChangeDeal, jsonStage;
	Hostname host;
	SharedPreferences userPref;
	LinearLayout stage0Lay, stage1Lay, stage2Lay, stage3Lay, stage4Lay,
			stage5Lay, stage6Lay;
	boolean stage0 = false, stage1 = false, stage2 = false, stage3 = false,
			stage4 = false, stage5 = false, stage6 = false;
	ArrayList<HashMap<String, String>> stagesNamesList = new ArrayList<HashMap<String, String>>();
	MyPagerAdapter adapter;
	static CrmDealDetail crmDealDetailsPojo;
	Gson gson=new Gson();
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crm_deal_detail);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		// getActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		actionBar.setDisplayShowTitleEnabled(false);
		// tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		stage0Lay = (LinearLayout) findViewById(R.id.lay1);
		stage1Lay = (LinearLayout) findViewById(R.id.lay2);
		stage2Lay = (LinearLayout) findViewById(R.id.lay3);
		stage3Lay = (LinearLayout) findViewById(R.id.lay4);
		stage4Lay = (LinearLayout) findViewById(R.id.lay5);
		stage5Lay = (LinearLayout) findViewById(R.id.lay6);
		stage6Lay = (LinearLayout) findViewById(R.id.lay7);

		// mIndicator.setFades(false);
		host = new Hostname();
		userPref = getSharedPreferences("USER", MODE_PRIVATE);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		dealId = bundle.getString("Dealid");
		stage0Lay.setBackgroundColor(Color.parseColor("#42C25D"));

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabsPager = (ViewPager) findViewById(R.id.pagerTabs);
		

		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				stageId = stagesNamesList.get(position).get("stageId");
				System.out.println(stageId + " !!!!!!!!!!!!!!");
				if (position == 0) {
					stage0Lay.setBackgroundColor(Color.parseColor("#42C25D"));
					stage1 = false;
					stage1Lay.setBackgroundColor(Color.parseColor("#C1EECF"));
				} else if (position == 1) {
					if (stage1 == false) {
						stage0Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage1Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage1 = true;
					} else {
						stage1 = false;

					}
					stage2 = false;
					stage2Lay.setBackgroundColor(Color.parseColor("#C1EECF"));
				} else if (position == 2) {
					if (stage2 == false) {
						stage1Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage2Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage2 = true;
					} else {
						stage2 = false;
					}
					stage3 = false;
					stage3Lay.setBackgroundColor(Color.parseColor("#C1EECF"));
				} else if (position == 3) {
					if (stage3 == false) {
						stage1Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage2Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage3Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage3 = true;
					} else {
						stage3 = false;

					}
					stage4 = false;
					stage4Lay.setBackgroundColor(Color.parseColor("#C1EECF"));
				} else if (position == 4) {
					if (stage4 == false) {
						stage1Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage2Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage3Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage4Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage4 = true;
					} else {
						stage4 = false;
					}
					stage5 = false;
					stage5Lay.setBackgroundColor(Color.parseColor("#C1EECF"));
				} else if (position == 5) {
					if (stage5 == false) {
						stage1Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage2Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage3Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage4Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage5Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage5 = true;
					} else {
						stage5 = false;
					}
					stage6 = false;
					stage6Lay.setBackgroundColor(Color.parseColor("#C1EECF"));
				} else if (position == 6) {
					if (stage6 == false) {
						stage1Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage2Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage3Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage4Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage5Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage6Lay.setBackgroundColor(Color
								.parseColor("#42C25D"));
						stage6 = true;
					} else {
						stage6 = false;
					}
				}
				new MoveDeals().execute();
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
			}
		});

		new DefaultStages().execute();

	}

	// Default pipeline & deals

	public class DefaultStages extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				jsonDeal = CRMGetHttp.callDealDetail(host.globalVariable(),
						userPref.getString("TOKEN", "NV"), dealId);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonDeal != null) {
				crmDealDetailsPojo=gson.fromJson(
						jsonDeal.toString(),
						new TypeToken<CrmDealDetail>() {
						}.getType());
				JSONArray stagesArr;
				try {

					jsonStage = jsonDeal.getJSONObject("stage");

					dealStageId = jsonStage.getString("id");
					JSONObject jsonPipe = jsonDeal.getJSONObject("pipeline");
					stagesArr = jsonPipe.getJSONArray("stages");

					if (stagesArr.length() == 1) {
						stage1Lay.setVisibility(View.GONE);
						stage2Lay.setVisibility(View.GONE);
						stage3Lay.setVisibility(View.GONE);
						stage4Lay.setVisibility(View.GONE);
						stage5Lay.setVisibility(View.GONE);
						stage6Lay.setVisibility(View.GONE);
					} else if (stagesArr.length() == 2) {
						stage2Lay.setVisibility(View.GONE);
						stage3Lay.setVisibility(View.GONE);
						stage4Lay.setVisibility(View.GONE);
						stage5Lay.setVisibility(View.GONE);
						stage6Lay.setVisibility(View.GONE);
					} else if (stagesArr.length() == 3) {
						stage3Lay.setVisibility(View.GONE);
						stage4Lay.setVisibility(View.GONE);
						stage5Lay.setVisibility(View.GONE);
						stage6Lay.setVisibility(View.GONE);
					} else if (stagesArr.length() == 4) {
						stage4Lay.setVisibility(View.GONE);
						stage5Lay.setVisibility(View.GONE);
						stage6Lay.setVisibility(View.GONE);
					} else if (stagesArr.length() == 5) {
						stage5Lay.setVisibility(View.GONE);
						stage6Lay.setVisibility(View.GONE);
					} else if (stagesArr.length() == 6) {
						stage6Lay.setVisibility(View.GONE);
					}

					for (int i = 0; i < stagesArr.length(); i++) {
						HashMap<String, String> mapStage = new HashMap<String, String>();
						JSONObject stageJson = stagesArr.getJSONObject(i);
						String stageName = stageJson.getString("title");
						mapStage.put("title", stageName);
						mapStage.put("stageId", stageJson.getString("id"));
						System.out.println(dealStageId + "="
								+ stageJson.getString("id"));
						String stageId = stageJson.getString("id");
						if (dealStageId == stageId
								|| dealStageId.equals(stageId)) {
							System.out.println(dealStageId + " EQUAL "
									+ stageJson.getString("id"));
							stagePos = i;
						}
						stagesNamesList.add(mapStage);
					}
					ViewPagAdapter adapte = new ViewPagAdapter(DealDetail.this,
							stagesNamesList);

					pager.setAdapter(adapte);
					pager.setCurrentItem(stagePos);
					adapte.notifyDataSetChanged();
					// pDialog.setVisibility(View.GONE);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				showSnack(DealDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
			}
			adapter = new MyPagerAdapter(getSupportFragmentManager());
			tabsPager.setAdapter(adapter);
			final int pageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
							.getDisplayMetrics());
			tabsPager.setPageMargin(pageMargin);
			tabs.setViewPager(tabsPager);
		}
	}

	public class ViewPagAdapter extends PagerAdapter {
		ArrayList<HashMap<String, String>> Stageslist;
		private Context context;
		private LayoutInflater inflater;
		String busiUrl;
		ListPipelinesBaseAdapter adapter;

		public ViewPagAdapter(DealDetail dealDetail,
				ArrayList<HashMap<String, String>> Stageslist) {
			// TODO Auto-generated constructor stub
			this.context = dealDetail;
			this.Stageslist = Stageslist;
			inflater = (LayoutInflater) dealDetail
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
			View itemView = inflater.inflate(R.layout.crm_stages_deal_detail,
					container, false);
			TextView titleTxt;
			titleTxt = (TextView) itemView.findViewById(R.id.stageNameTxt);

			titleTxt.setText(Stageslist.get(position).get("title").toString());
			
			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Remove viewpager_item.xml from ViewPager
			((ViewPager) container).removeView((LinearLayout) object);
		}
	}

	public class MoveDeals extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			try {

				JSONArray jarr = new JSONArray();
				jarr.put(dealId);
				json.put("stage", stageId);
				json.put("ids", jarr);

				jsonChangeDeal = CRMGetHttp.changeDealStage(
						host.globalVariable(),
						userPref.getString("TOKEN", "NV"), dealId, json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (jsonChangeDeal != null) {
				if (jsonChangeDeal.has("reorder")) {
					try {
						stageResponse = jsonChangeDeal.getString("reorder");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					showSnack(DealDetail.this, "Oops! Stage failed to change!",
							"OK");
				}

			} else {
				showSnack(DealDetail.this,
						"Oops! Something went wrong. Please wait a moment!",
						"OK");
			}
		}
	}

	public void showSnack(DealDetail login, String stringMsg, String ok) {
		new SnackBar(DealDetail.this, stringMsg, ok, new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		}).show();
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "                 FLOW             ",
				"            DETAILS          " };

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
			return DealDetails.newInstance(position);
		}
	}

	public static class DealDetails extends Fragment {
		private static final String ARG_POSITION = "position";
		private int position;
		ListView planedListview, pastListview;
		TextView expectedCloseDateTxt,ownerTxt,createdTxt,lastUpdatedTxt,emailTxt;
		public static DealDetails newInstance(int position) {
			DealDetails f = new DealDetails();
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
			View v = null;
			if (position == 0) {
				v = infaltor.inflate(R.layout.crm_people_activity, container,
						false);
				planedListview = (ListView) v.findViewById(R.id.planedListview);
				pastListview = (ListView) v.findViewById(R.id.pastListview);
				
				//new PeopleFlow().execute();
			} else {
				v = infaltor.inflate(R.layout.crm_deal_gendetails, container,
						false);
				expectedCloseDateTxt=(TextView)v.findViewById(R.id.expCloseDateTxtt);
				ownerTxt=(TextView)v.findViewById(R.id.ownerTxtt);
				createdTxt=(TextView)v.findViewById(R.id.createdTxtt);
				lastUpdatedTxt=(TextView)v.findViewById(R.id.lastUpdatedTxtt);
				emailTxt=(TextView)v.findViewById(R.id.emailTxtt);
				/*if(crmDealDetailsPojo.getExpected_close()!=null){
					expectedCloseDateTxt.setText(String.valueOf(crmDealDetailsPojo.getExpected_close()));
				}*/
				
			//	ownerTxt.setText(crmDealDetailsPojo.getDealOwnerPojo().getFull_name());
				
			}
			return v;
		}
	}
}
