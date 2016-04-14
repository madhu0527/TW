package mobile.teamwave.crm_activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mobile.teamwave.Adapters.CurrencyAdapter;
import mobile.teamwave.Adapters.LinkPersonAdapter;
import mobile.teamwave.Adapters.StagesAdapter;
import mobile.teamwave.CrmPojos.CrmCurrencyPojo;
import mobile.teamwave.CrmPojos.CrmDealPipelinesPojo;
import mobile.teamwave.CrmPojos.CrmLinkPersons;
import mobile.teamwave.Http.CRMGetHttp;
import mobile.teamwave.Adapters.PipelineAdapter;
import mobile.teamwave.Utils.Constants;
import mobile.teamwave.application.Hostname;
import teamwave.android.mobile.teamwave.R;


/**
 * Created by goodworklabs on 25/02/2016.
 */
public class AddDeal extends ActionBarActivity {
    android.support.v7.app.ActionBar actionBar;
    RequestQueue queue;
    SharedPreferences userPref;
    Hostname host = new Hostname();
    JSONArray pipleLinesArr, crmLinkPersonsArr, currencyArr;
    Gson gson = new Gson();
    List<CrmDealPipelinesPojo> pipeLinesList;
    Spinner pipeLinesSpinr, stagesSpinner, currencySpnr;
    EditText dealNameEdit,expectedDateEdit,dealValueEdit;
    static final int DATE_DIALOG_FROMID = 0;
    LinearLayout linkpersonLay,linkOrgLay;
    String searchPerson,dealNameStr,expectedCloseDate="",dealVal="0",stageTitle;
    ListView personsListvieew;
    List<CrmLinkPersons> crmLinkPersonsList;
    List<CrmCurrencyPojo> currencyList;
    TextView linkPersonTxt, orgNameTxt, addNewContactTxt;
    JSONObject addDealJson;
    int pipeLineId,currencyId,stageId,rottingDays;
    boolean isDealroting,isTrashed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_add_deal);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#F9F9FA")));
        actionBar.setTitle(Html.fromHtml("<font color='#424242'>" + "Add Deal"
                + "</font>"));
        queue = Volley.newRequestQueue(this);
        userPref = getSharedPreferences("USER", MODE_PRIVATE);
        Log.d("TOKEN", userPref.getString("TOKEN", "NV"));

        pipeLinesSpinr = (Spinner) findViewById(R.id.pipelineSpnr);
        stagesSpinner = (Spinner) findViewById(R.id.stagesSpinner);
        currencySpnr = (Spinner) findViewById(R.id.currencySpnr);
        expectedDateEdit= (EditText) findViewById(R.id.expectedDateEdit);
        dealNameEdit= (EditText) findViewById(R.id.dealNameEdit);
        dealValueEdit = (EditText) findViewById(R.id.dealValueEdit);
        linkpersonLay = (LinearLayout) findViewById(R.id.linkpersonLay);
        linkOrgLay= (LinearLayout) findViewById(R.id.linkOrgLay);
        linkPersonTxt = (TextView) findViewById(R.id.linkPersonTxt);
        orgNameTxt = (TextView) findViewById(R.id.orgNameTxt);

        // call Pipelines

        callPipelines();
        callCurrency();

        pipeLinesSpinr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + " SPINNER POS");
                StagesAdapter adapterStages = new StagesAdapter(
                        AddDeal.this, pipeLinesList.get(position).getCrmStagePojo());
                stagesSpinner.setAdapter(adapterStages);
                pipeLineId = pipeLinesList.get(position).getId();
                pipeLinesList.get(position).getIs_trashed();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pipeLineId = pipeLinesList.get(0).getId();
            }
        });
        stagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + " SPINNER POS");

                stageId = pipeLinesList.get(position).getCrmStagePojo().get(position).getId();
                stageTitle = pipeLinesList.get(position).getCrmStagePojo().get(position).getTitle();
                rottingDays=pipeLinesList.get(position).getCrmStagePojo().get(position).getRotting_days();
                isTrashed=pipeLinesList.get(position).getCrmStagePojo().get(position).is_trashed();
                isDealroting=pipeLinesList.get(position).getCrmStagePojo().get(position).is_deal_rotting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stageId = pipeLinesList.get(0).getCrmStagePojo().get(0).getId();
                stageTitle = pipeLinesList.get(0).getCrmStagePojo().get(0).getTitle();
            }
        });
        currencySpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyId=currencyList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currencyId=currencyList.get(0).getId();
            }
        });
        //
        expectedDateEdit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressWarnings("deprecation")
            public boolean onTouch(View v, MotionEvent event) {
                if (v == expectedDateEdit)
                    showDialog(DATE_DIALOG_FROMID);
                return false;
            }
        });
        linkpersonLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddDeal.this);
                dialog.setContentView(R.layout.crm_popup_linkperson);
                dialog.setTitle("Link to");

                EditText personNameEdit = (EditText) dialog.findViewById(R.id.personNameEdit);
                personsListvieew = (ListView) dialog.findViewById(R.id.personListview);
                addNewContactTxt = (TextView) dialog.findViewById(R.id.addNewContact);
                addNewContactTxt.setVisibility(View.GONE);
                personNameEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count > 2) {
                            searchPerson = s.toString();
                            callLinkAPerson();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                personsListvieew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        linkPersonTxt.setText(crmLinkPersonsList.get(position).getFull_name());
                        orgNameTxt.setText(crmLinkPersonsList.get(position).getCompany_name());
                        dialog.dismiss();
                    }
                });
                addNewContactTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewContact();
                    }
                });
                dialog.show();
            }
        });
        linkOrgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddDeal.this);
                dialog.setContentView(R.layout.crm_popup_linkperson);
                dialog.setTitle("Link to");

                EditText personNameEdit = (EditText) dialog.findViewById(R.id.personNameEdit);
                personsListvieew = (ListView) dialog.findViewById(R.id.personListview);
                addNewContactTxt = (TextView) dialog.findViewById(R.id.addNewContact);
                addNewContactTxt.setVisibility(View.GONE);
                personNameEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count > 2) {
                            searchPerson = s.toString();
                            callLinkAOrg();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                personsListvieew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        linkPersonTxt.setText(crmLinkPersonsList.get(position).getFull_name());
                        orgNameTxt.setText(crmLinkPersonsList.get(position).getCompany_name());
                        dialog.dismiss();
                    }
                });
                addNewContactTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewContact();
                    }
                });
                dialog.show();
            }
        });
    }

    private void callCurrency() {
        new CallCurrency().execute();
    }

    private void addNewContact() {
        new AddNewperson().execute();
    }

    private void callLinkAPerson() {
        new LinkAperson().execute();
    }
    private void callLinkAOrg() {
        new LinkAOrg().execute();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_FROMID:
                return new DatePickerDialog(this, mDateFROMSetListener, cyear,
                        cmonth, cday);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateFROMSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String day = String.valueOf(dayOfMonth);
            String month = String.valueOf(monthOfYear + 1);
            System.out.println(month);
            if (day.length() == 1) {
                day = "0" + day;
            }
            if (month.length() == 1) {
                month = "0" + month;
            }
            expectedDateEdit.setText(day + "-" + month + "-"
                    + String.valueOf(year));
            expectedCloseDate= String.valueOf(year)+"-"+month+"-"+day;
        }
    };

    private void callPipelines() {
        new PipeLines().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crm_add_deal_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                    System.out.print("CLICKED");
                saveDeal();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDeal() {
        dealNameStr=dealNameEdit.getText().toString();
        dealVal=dealValueEdit.getText().toString();
        if(dealVal==""||dealVal.equals("")){
            dealVal="0";
        }
        if(dealNameStr.equals("")||dealNameStr==""||dealNameStr==null){
            showSnack(AddDeal.this,
                    "Enter deal name!",
                    "OK");
        }else{
            new AddNewDeal().execute();
        }

    }

    public class PipeLines extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            //   pDialog.setVisibility(View.VISIBLE);
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
                showSnack(AddDeal.this,

                        "Oops! Something went wrong. Please wait a moment!",
                        "OK");
            } else {

                pipeLinesList = gson.fromJson(pipleLinesArr.toString(), new TypeToken<List<CrmDealPipelinesPojo>>() {
                }.getType());
                PipelineAdapter adapterPipeline = new PipelineAdapter(
                        AddDeal.this, pipeLinesList);
                pipeLinesSpinr.setAdapter(adapterPipeline);
                StagesAdapter adapterStages = new StagesAdapter(AddDeal.this, pipeLinesList.get(0).getCrmStagePojo());
                stagesSpinner.setAdapter(adapterStages);
            }
        }
    }

    public class CallCurrency extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            //   pDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                currencyArr = CRMGetHttp.getCurrencies(host.globalVariable(),
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
            if (currencyArr != null) {
                if (currencyArr.length() == 0) {
                    showSnack(AddDeal.this,
                            "Oops! There is no currency!",
                            "OK");
                } else {
                    currencyList = gson.fromJson(currencyArr.toString(), new TypeToken<List<CrmCurrencyPojo>>() {
                    }.getType());
                    CurrencyAdapter curencyAdapter = new CurrencyAdapter(AddDeal.this, currencyList);
                    currencySpnr.setAdapter(curencyAdapter);
                }

            } else {
                showSnack(AddDeal.this,
                        "Oops! Something went wrong. Please wait a moment!",
                        "OK");
            }
        }
    }

    public class LinkAperson extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    crmLinkPersonsArr = CRMGetHttp.callLinkAPerson(host.globalVariable(),
                            userPref.getString("TOKEN", "NV"), searchPerson);
                    System.out.print(crmLinkPersonsArr + "  RESULT PERSON");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                if (crmLinkPersonsArr.length() == 0) {
                    addNewContactTxt.setVisibility(View.VISIBLE);
                    personsListvieew.setAdapter(null);
                } else {
                    addNewContactTxt.setVisibility(View.GONE);
                    crmLinkPersonsList = gson.fromJson(crmLinkPersonsArr.toString(), new TypeToken<List<CrmLinkPersons>>() {
                    }.getType());
                    LinkPersonAdapter adapterLinkPerson = new LinkPersonAdapter(
                            AddDeal.this, crmLinkPersonsList);
                    personsListvieew.setAdapter(adapterLinkPerson);
                }
            }

    }
    public class LinkAOrg extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                crmLinkPersonsArr = CRMGetHttp.callLinkAOrg(host.globalVariable(),
                        userPref.getString("TOKEN", "NV"), searchPerson);
                System.out.print(crmLinkPersonsArr + "  RESULT PERSON");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (crmLinkPersonsArr.length() == 0) {
                addNewContactTxt.setVisibility(View.VISIBLE);
                personsListvieew.setAdapter(null);
            } else {
                addNewContactTxt.setVisibility(View.GONE);
                crmLinkPersonsList = gson.fromJson(crmLinkPersonsArr.toString(), new TypeToken<List<CrmLinkPersons>>() {
                }.getType());
                LinkPersonAdapter adapterLinkPerson = new LinkPersonAdapter(
                        AddDeal.this, crmLinkPersonsList);
                personsListvieew.setAdapter(adapterLinkPerson);
            }
        }

    }

    public class AddNewDeal extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("pipeline", pipeLineId);
                jsonMain.put("currency", currencyId);
                jsonMain.put("title", dealNameStr);
                if(expectedCloseDate!=""||!expectedCloseDate.equals("")){
                    jsonMain.put("expected_close", expectedCloseDate);
                }
                jsonMain.put("deal_value", dealVal);
                JSONObject jsonStage = new JSONObject();
                jsonStage.put("id",stageId);
                jsonStage.put("title",stageTitle);
                jsonStage.put("is_trashed", isTrashed);
                jsonStage.put("index", 0);
                jsonStage.put("is_deal_rotting",isDealroting);
                jsonStage.put("rotting_days", rottingDays);
                jsonMain.put("stage", jsonStage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                System.out.print("INPUT "+jsonMain);
                addDealJson = CRMGetHttp.makeRequestAddDeal(host.globalVariable(),
                        userPref.getString("TOKEN", "NV"),jsonMain);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Intent intent = new Intent(AddDeal.this, CrmMainActivity.class);
            startActivity(intent);
        }
    }


    public class AddNewperson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    public void showSnack(AddDeal addDeal, String stringMsg, String ok) {
        new SnackBar(AddDeal.this, stringMsg, ok,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}
