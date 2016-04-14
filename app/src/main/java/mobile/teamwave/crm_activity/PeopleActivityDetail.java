package mobile.teamwave.crm_activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import teamwave.android.mobile.teamwave.R;
import mobile.teamwave.Http.CRMGetHttp;
import mobile.teamwave.application.Hostname;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobile.teamwave.design.PagerSlidingTabStrip;
import com.gc.materialdesign.widgets.SnackBar;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.viewpagerindicator.CirclePageIndicator;

public class PeopleActivityDetail extends ActionBarActivity {
	String hostname;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	MyPagerAdapter adapter;
	android.support.v7.app.ActionBar actionBar;
	RelativeLayout peopleLay;
	static TextView personNameTxt, companyNameTxt;
	static LinearLayout progrLay;
	static CircleProgressBar pDialog, pDialogDeal;
	static String personId;
	static LinearLayout telephoneLay;
	static LinearLayout emailLay;
	static String mobileNum, emailStr;
	static ListView listviewDeals, planedListview, pastListview;
	static Hostname host;

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
		actionBar.setDisplayShowTitleEnabled(false);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		peopleLay = (RelativeLayout) findViewById(R.id.peopleLay);
		personNameTxt = (TextView) findViewById(R.id.personNameTxt);
		companyNameTxt = (TextView) findViewById(R.id.companyNameTxt);
		pDialog = (CircleProgressBar) findViewById(R.id.pDialog);
		progrLay = (LinearLayout) findViewById(R.id.progrLay);
		pDialog.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		peopleLay.setVisibility(View.VISIBLE);

		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		personId = bundle.getString("person_id");

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

		private final String[] TITLES = { "  General  ", "    Flow   ",
				"  Deals  " };

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
			return PeopleDetailTabs.newInstance(position);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.crm_person_menu, menu);

		return super.onCreateOptionsMenu(menu);
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
		case R.id.call:
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + mobileNum));
			startActivity(intent);
		return true;
		case R.id.email:
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
					Uri.fromParts("mailto", emailStr, null));
			startActivity(Intent.createChooser(emailIntent,
					"Send email..."));
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class PeopleDetailTabs extends Fragment {
		private static final String ARG_POSITION = "position";
		int position;
		ImageView callImg, emailImg;
		String hostname, fullName, actionType, isCompleted, noteContent,contactType="people";
		JSONObject jsonPeople;
		SharedPreferences userPref;
		TextView phNumTxt, emailTxt;
		JSONArray jarrDeals, jArrFlow;

		public static PeopleDetailTabs newInstance(int position) {
			PeopleDetailTabs f = new PeopleDetailTabs();
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
			// Bundle b = getActivity().getIntent().getExtras();
			View v = null;

			host = new Hostname();
			hostname = host.globalVariable();
			userPref = getActivity().getSharedPreferences("USER", 0);

			if (position == 0) {
				v = infaltor.inflate(R.layout.crm_people_detail_general,
						container, false);

				phNumTxt = (TextView) v.findViewById(R.id.telephoneTxt);
				callImg = (ImageView) v.findViewById(R.id.callImg);
				emailImg = (ImageView) v.findViewById(R.id.emailImg);
				emailTxt = (TextView) v.findViewById(R.id.emailTxt);
				telephoneLay = (LinearLayout) v.findViewById(R.id.telephoneLay);
				emailLay = (LinearLayout) v.findViewById(R.id.emailLay);

				callImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgPrev = SVGParser.getSVGFromResource(getResources(),
						R.raw.ic_call);
				Drawable drwPrev = svgPrev.createPictureDrawable();
				callImg.setImageDrawable(drwPrev);
				// Email
				emailImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgEmail = SVGParser.getSVGFromResource(getResources(),
						R.raw.ic_email);
				Drawable draEmil = svgEmail.createPictureDrawable();
				emailImg.setImageDrawable(draEmil);

				telephoneLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_DIAL);

						intent.setData(Uri.parse("tel:" + mobileNum));
						startActivity(intent);
					}
				});

				emailLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
								Uri.fromParts("mailto", emailStr, null));
						startActivity(Intent.createChooser(emailIntent,
								"Send email..."));
					}
				});

				// general detalis
				new PersonGenDetail().execute();

			} else if (position == 1) {
				v = infaltor.inflate(R.layout.crm_people_activity, container,
						false);
				planedListview = (ListView) v.findViewById(R.id.planedListview);
				pastListview = (ListView) v.findViewById(R.id.pastListview);
				new PeopleFlow().execute();
			} else if (position == 2) {
				v = infaltor.inflate(R.layout.crm_people_org_tabs, container,
						false);
				listviewDeals = (ListView) v.findViewById(R.id.listviewPeoOrg);
				pDialogDeal = (CircleProgressBar) v.findViewById(R.id.pDialog);
				pDialogDeal.setColorSchemeResources(
						android.R.color.holo_green_light,
						android.R.color.holo_orange_light,
						android.R.color.holo_red_light);
				new PersonDeals().execute();
			}
			return v;
		}

		public class PersonGenDetail extends AsyncTask<Void, Void, Void> {

			@Override
			protected void onPreExecute() {
				progrLay.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					jsonPeople = CRMGetHttp.callPeopleGenDetail(hostname,
							userPref.getString("TOKEN", "NV"),contactType, personId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void unused) {
				if (jsonPeople != null) {
					try {
						personNameTxt
								.setText(jsonPeople.getString("full_name"));
						if (jsonPeople.isNull("company")) {
							companyNameTxt.setText("");
						} else {
							JSONObject jsonComp = jsonPeople
									.getJSONObject("company");
							companyNameTxt.setText(jsonComp.getString("name"));
						}
						mobileNum = jsonPeople.getString("phone");
						emailStr = jsonPeople.getString("email");
						phNumTxt.setText(mobileNum);
						emailTxt.setText(emailStr);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progrLay.setVisibility(View.GONE);
				} else {
					progrLay.setVisibility(View.GONE);
					showSnack(
							getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
				}
			}

		}

		public class PersonDeals extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				pDialogDeal.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					jarrDeals = CRMGetHttp.callPersonDeals(hostname,
							userPref.getString("TOKEN", "NV"),"person",personId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void unused) {
				if (jarrDeals != null) {
					ArrayList<HashMap<String, String>> dealsList = new ArrayList<HashMap<String, String>>();
					for (int j = 0; j < jarrDeals.length(); j++) {
						HashMap<String, String> map = new HashMap<String, String>();
						try {
							JSONObject jsonDeal = jarrDeals.getJSONObject(j);
							map.put("DealTile", jsonDeal.getString("title"));
							map.put("Dealvalue",
									jsonDeal.getString("deal_value"));

							if (jsonDeal.isNull("company")) {
								System.out
										.println(" NULLL  !!!!!!!!!!!!!!!!!!!!!!!!");
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
									map.put("currency",
											jsonCurency.getString("symbol"));
								}
							}
							dealsList.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					PersonDealsBaseAdapter adapter = new PersonDealsBaseAdapter(
							getActivity(), dealsList);
					listviewDeals.setAdapter(adapter);
					pDialogDeal.setVisibility(View.GONE);
				} else {
					pDialogDeal.setVisibility(View.GONE);
					showSnack(
							getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
				}
			}
		}

		@SuppressLint("NewApi")
		public class PersonDealsBaseAdapter extends BaseAdapter {
			ArrayList<HashMap<String, String>> listt;
			private LayoutInflater inflator;
			private Context context;

			public PersonDealsBaseAdapter(Context context2,
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
					holder.mIndicator = (CirclePageIndicator) convertView
							.findViewById(R.id.indicator);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.dealNameTxt.setText(listt.get(position).get("DealTile"));

				if (listt.get(position).get("DealCompanyName") == ""
						|| listt.get(position).get("DealCompanyName")
								.equals("")) {
					holder.dealPriceTxt.setText(listt.get(position).get(
							"Dealvalue")
							+ listt.get(position).get("currency"));
				} else {
					holder.dealPriceTxt.setText(listt.get(position).get(
							"Dealvalue")
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

		public class PeopleFlow extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				progrLay.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					jArrFlow = CRMGetHttp.callPersonFlow(host.globalVariable(),
							userPref.getString("TOKEN", "NV"),contactType, personId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void unused) {
				if (jArrFlow != null) {
					System.out.println(jArrFlow + "    RESPONSE ");
					ArrayList<HashMap<String, String>> listPlaned = new ArrayList<HashMap<String, String>>();
					ArrayList<HashMap<String, String>> listPast = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < jArrFlow.length(); i++) {
						HashMap<String, String> mapPlaned = new HashMap<String, String>();
						HashMap<String, String> mapPast = new HashMap<String, String>();

						try {
							JSONObject jsonflow = jArrFlow.getJSONObject(i);

							actionType = jsonflow.getString("type");
							if (actionType == "Activity"
									|| actionType.equals("Activity")) {
								isCompleted = jsonflow
										.getString("is_completed");
								JSONObject jsonperson = jsonflow
										.getJSONObject("person");
								fullName = jsonperson.getString("name");

								if (isCompleted == "false"
										|| isCompleted.equals("false")) {
									mapPlaned.put("isCompleted", isCompleted);
									mapPlaned.put("title",
											jsonflow.getString("title"));
									mapPlaned.put("act_on",
											jsonflow.getString("act_on"));
									mapPlaned.put("fullName", fullName);
									mapPlaned.put("actionType", actionType);
								} else {
									mapPast.put("isCompleted", isCompleted);
									mapPast.put("title",
											jsonflow.getString("title"));
									mapPast.put("act_on",
											jsonflow.getString("act_on"));
									mapPast.put("fullName", fullName);
									mapPast.put("actionType", actionType);
									listPast.add(mapPast);
								}
								if(mapPlaned.isEmpty()){
									
								}else{
									listPlaned.add(mapPlaned);
								}
							} else {
								if (actionType == "Note"
										|| actionType.equals("Note")) {
									mapPast.put("note_content",
											jsonflow.getString("content"));
								} else if (actionType == "File"
										|| actionType.equals("File")) {
									mapPast.put("file_title",
											jsonflow.getString("title"));
								}
								JSONObject jsonCreatedby = jsonflow
										.getJSONObject("created_by");
								fullName = jsonCreatedby.getString("full_name");
								mapPast.put("fullName", fullName);
								mapPast.put("actionType", actionType);
								mapPast.put("act_on",
										jsonflow.getString("act_on"));
								listPast.add(mapPast);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					FlowPlanedBaseAdapter adapter = new FlowPlanedBaseAdapter(
							getActivity(), listPlaned);
					if(listPlaned.size()==0||listPlaned.toString()=="[{}]"||listPlaned.toString().equals("[{}]")){
						
					}else{
						planedListview.setAdapter(adapter);
					}
					
					FlowPastBaseAdapter pastAdapter = new FlowPastBaseAdapter(
							getActivity(), listPast);
					pastListview.setAdapter(pastAdapter);

					progrLay.setVisibility(View.GONE);

				} else {
					progrLay.setVisibility(View.GONE);
					showSnack(
							getActivity(),
							"Oops! Something went wrong. Please wait a moment!",
							"OK");
				}
			}

		}

		@SuppressLint("NewApi")
		public class FlowPlanedBaseAdapter extends BaseAdapter {
			ArrayList<HashMap<String, String>> listt;
			private LayoutInflater inflator;
			private Context context;

			public FlowPlanedBaseAdapter(Context context2,
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
					convertView = inflator.inflate(
							R.layout.crm_people_activity_listrow, null);
					holder = new ViewHolder();
					holder.tasksNameTxt = (TextView) convertView
							.findViewById(R.id.tasksName);
					holder.createdByTxt = (TextView) convertView
							.findViewById(R.id.createdByTxt);
					holder.dateTxt = (TextView) convertView
							.findViewById(R.id.dateTxt);
					holder.planedLay = (RelativeLayout) convertView
							.findViewById(R.id.layPlaned);
					holder.emailImg = (ImageView) convertView
							.findViewById(R.id.emailImg);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				
				holder.emailImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgEmail = SVGParser.getSVGFromResource(getResources(),
						R.raw.ic_email);
				Drawable draEmail = svgEmail.createPictureDrawable();
				holder.emailImg.setImageDrawable(draEmail);
				
				
				if(listt.size()==0||listt.toString()=="[{}]"||listt.toString().equals("[{}]")){
					holder.planedLay.setVisibility(View.GONE);
				}else{
					holder.planedLay.setVisibility(View.VISIBLE);
				}
				holder.tasksNameTxt.setText(listt.get(position).get("title"));
				holder.createdByTxt.setText(listt.get(position).get("fullName"));
				String createdAt=listt.get(position).get("act_on");
				String datee = createdAt.substring(0, 10);
				String fromYear = datee.substring(0, 4);
				try {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dat = df.parse(datee);
					System.out.println(createdAt);
					createdAt = dat.toString();
					System.out.println(createdAt.length());
					createdAt = createdAt.substring(3, 10);
					System.out.println(createdAt);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				holder.dateTxt.setText(createdAt + "," + fromYear);
				
				return convertView;
			}

			public class ViewHolder {
				public TextView tasksNameTxt,createdByTxt,dateTxt;
				RelativeLayout planedLay;
				ImageView emailImg;
			}
		}

		@SuppressLint("NewApi")
		public class FlowPastBaseAdapter extends BaseAdapter {
			ArrayList<HashMap<String, String>> listt;
			private LayoutInflater inflator;
			private Context context;

			public FlowPastBaseAdapter(Context context2,
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
					convertView = inflator.inflate(R.layout.crm_notes_list_row,
							null);
					holder = new ViewHolder();
					holder.notesContentTxt = (TextView) convertView
							.findViewById(R.id.titleTxt);
					holder.fileTitleTxt = (TextView) convertView
							.findViewById(R.id.fileTitleTxt);
					holder.notesLay = (RelativeLayout) convertView
							.findViewById(R.id.notesLay);
					holder.flesLay = (RelativeLayout) convertView
							.findViewById(R.id.flesLay);
					holder.activityLay = (RelativeLayout) convertView
							.findViewById(R.id.activityLay);
					holder.notescreatedByTxt = (TextView) convertView
							.findViewById(R.id.createdByTxt);
					holder.filecreatedByTxt = (TextView) convertView
							.findViewById(R.id.filecreatedTxt);
					holder.activtyNameTxt= (TextView) convertView
							.findViewById(R.id.activitytitleTxt);
					holder.notesImg = (ImageView) convertView
							.findViewById(R.id.notesImg);
					holder.emailImg = (ImageView) convertView
							.findViewById(R.id.activtyImg);
					holder.fileImg = (ImageView) convertView
							.findViewById(R.id.fileTypeImg);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.notesImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgNotes = SVGParser.getSVGFromResource(getResources(),
						R.raw.note);
				Drawable draNotes = svgNotes.createPictureDrawable();
				holder.notesImg.setImageDrawable(draNotes);
				
				holder.emailImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgEmail = SVGParser.getSVGFromResource(getResources(),
						R.raw.ic_email);
				Drawable draEmail = svgEmail.createPictureDrawable();
				holder.emailImg.setImageDrawable(draEmail);
				
				holder.fileImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				SVG svgFile = SVGParser.getSVGFromResource(getResources(),
						R.raw.files_jpg);
				Drawable draFIle = svgFile.createPictureDrawable();
				holder.fileImg.setImageDrawable(draFIle);
				
				if (listt.get(position).get("actionType") == "Note"
						|| listt.get(position).get("actionType").equals("Note")) {
					holder.notesLay.setVisibility(View.VISIBLE);
					holder.flesLay.setVisibility(View.GONE);
					
					String note=listt.get(
							position).get("note_content");
					note = note.replaceAll("<br/>", "");
					note = note.replaceAll("<p>", "");
					note = note.replaceAll("</p>", "");
					
					holder.notesContentTxt.setText(note);
					holder.notescreatedByTxt.setText(listt.get(
							position).get("fullName"));
				} else if (listt.get(position).get("actionType") == "File"
						|| listt.get(position).get("actionType").equals("File")) {
					holder.notesLay.setVisibility(View.GONE);
					holder.flesLay.setVisibility(View.VISIBLE);
					holder.fileTitleTxt.setText("file_title");
					holder.filecreatedByTxt.setText(listt.get(
							position).get("fullName")+" uploaded a file");
				}else if(listt.get(position).get("actionType") == "Activity"
						|| listt.get(position).get("actionType").equals("Activity")){
					holder.activtyNameTxt.setText(listt.get(position).get("title"));
					holder.notesLay.setVisibility(View.GONE);
					holder.flesLay.setVisibility(View.GONE);
					holder.activityLay.setVisibility(View.VISIBLE);
					
				}

				return convertView;
			}

			public class ViewHolder {
				public TextView notesContentTxt,notescreatedByTxt;
				TextView fileTitleTxt,filecreatedByTxt,activtyNameTxt;
				RelativeLayout notesLay, flesLay,activityLay;
				ImageView notesImg,emailImg,fileImg;
			}
		}

		void showSnack(FragmentActivity fragmentActivity, String stringMsg,
				String ok) {
			new SnackBar(getActivity(), stringMsg, ok, new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			}).show();
		}
	}

}
