package com.grabpaisa.login;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.grabpaisa.R;
import com.grabpaisa.navigation.HomeActivity;
import com.grabpaisa.server.ServerTask;

public class CreateAcount extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    String acname, mobile_no, email, u_country;
    EditText etEmail,etReferenceId;
    EditText etName, etMobile;
    Button btnCreatAnAccount;
    KeyboardUtil keyboardUtil;
    LinearLayout llCreatAccount;
    private Spinner spinner;

    private CheckBox checkTNC;

    private TextView txtTNC;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_acount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //     toolbar.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("grabpaisa", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("Registration");

        txtTNC = (TextView)findViewById(R.id.txt_tnc);
        txtTNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://grabpaisa.com/tnc.html"));
                startActivity(browserIntent);
            }
        });

        checkTNC = (CheckBox)findViewById(R.id.ch_termConditioon);

        //Change Action bar Title color
        btnCreatAnAccount = (Button) findViewById(R.id.creat_an_account);
        btnCreatAnAccount.setOnClickListener(this);

        llCreatAccount = (LinearLayout) findViewById(R.id.ll_creataccount);

        etEmail = (EditText) findViewById(R.id.et_email);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etName = (EditText)findViewById(R.id.et_TuitonName);
        etReferenceId = (EditText)findViewById(R.id.et_RefranceId);

        llCreatAccount.setOnTouchListener(this);
        keyboardUtil.hideKeyboard(this);

        spinner = (Spinner) findViewById(R.id.spinner);

        List list = new ArrayList();
        list.add("Left");

        list.add("Right");




        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        autoFillData();

    }


    private void autoFillData() {
        etEmail.setText(email);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        keyboardUtil.hideKeyboard(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.creat_an_account:

                String name = etName.getText().toString();
                if(name == null || name.length() == 0){
                    Toast.makeText(CreateAcount.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = etEmail.getText().toString();
                if(email == null || email.length() == 0){
                    Toast.makeText(CreateAcount.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mobile = etMobile.getText().toString();
                if(mobile.length() != 10){
                    Toast.makeText(CreateAcount.this, "mobile number must be 10 digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                String referenceId = etReferenceId.getText().toString();
                if(referenceId == null || referenceId.length() == 0){
                    Toast.makeText(CreateAcount.this, "Enter reference id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkTNC.isChecked()){
                    Toast.makeText(CreateAcount.this, "Please agree to terms & conditions", Toast.LENGTH_SHORT).show();
                    return;
                }
                String side = spinner.getSelectedItem().toString();
                if(side.equals("left")) side = "L";
                else side = "R";
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("ReferralId",referenceId);
                params.put("Name",name);
                params.put("Side",side);
                params.put("Mobile",mobile);
                params.put("EmailId",email);

                final ProgressDialog progressDialog = new ProgressDialog(this);

                progressDialog.setMessage("Registering, please wait");

                ServerTask serverTask = new ServerTask(ServerTask.API_REGISTRATION, params, new ServerTask.Callback() {
                    @Override
                    public void onCompleted(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONArray responseJson = new JSONArray(response);
                            System.out.println("response json : " + responseJson.toString());
                            String resResult = responseJson.getJSONObject(0).getString("ResResult");
                            System.out.println("Res Result : " + resResult);
                            ArrayList<String> errors = new ArrayList<String>();
                            errors.add("Invalid Referral ID.");
                            errors.add("Mobile no. is Already Exists,Please Try Another");
                            if(errors.contains(resResult)){
                                Toast.makeText(CreateAcount.this, resResult, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                MySingletone singletone = MySingletone.getInstance();
                                singletone.registrationId = resResult;

                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("reg_id", resResult);

                                editor.commit();

                                //Toast.makeText(CreateAcount.this, "response : " + response, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CreateAcount.this, HomeActivity.class);
                                Bundle b = new Bundle();
                                b.putBoolean("fromCreateAccount", true);
                                intent.putExtras(b);
                                //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                progressDialog.show();

                serverTask.execute();


                break;
            //case R.id.img_back_arrow:
            //  finish();
            //Toast.makeText(getApplicationContext(), "Comming soon", Toast.LENGTH_LONG).show();
            //  break;
        }

    }

    private void createAccount() {

        /*JsonObject params = new JsonObject();

            params.addProperty("tag", "register");
            params.addProperty("name", "name12");
            params.addProperty("email", "emai232r");
            params.addProperty("mobile", "passworwterd");
            params.addProperty("longitude", "cpasswortertd");
            params.addProperty("latititude", "compantery");
            params.addProperty("board", "addresterts");
            params.addProperty("gender", "countryert");
            params.addProperty("highest_qualification", "serttate");
            params.addProperty("about", "cityetre");
            params.addProperty("password", "zipcoretetde");
System.out.println(params.toString());
        Ion.with(getApplicationContext())
                .load("http://synaptensoft.com/superedunetapps")
                .setJsonObjectBody(params)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        System.out.println("Exception" + e);
                        System.out.println("Json object" + result);
                    }
                });*/
    }
   /* private void createAccount() {
        String serverUrl = getResources().getString(R.string.server_url);
        JSONObject params = new JSONObject();
        try {
            params.put("tag", "register");
            params.put("name", "name12");
            params.put("email", "emai232r");
            params.put("mobile", "passworwterd");
            params.put("longitude", "cpasswortertd");
            params.put("latititude", "compantery");
            params.put("board", "addresterts");
            params.put("gender", "countryert");
            params.put("highest_qualification", "serttate");
            params.put("about", "cityetre");
            params.put("password", "zipcoretetde");
        } catch (JSONException e) {

        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, serverUrl
                + "superedunetapps", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("App", response.toString());


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("App", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                jsonObjReq);
    }*/

    /* private void createAccount() {

         String serverUrl = getResources().getString(R.string.server_url);
         //SERVER_PATH/auth?cellnumber=9898980000&password=testPwd

         String url = serverUrl + "superedunetapps";
         url = url.trim();
         Log.i("url", url);
         RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
         StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 //  mPostCommentResponse.requestCompleted();
                 Log.i("response", response);
                 try {

                     JSONObject obj = new JSONObject(response);
                     if (obj.getString("status") == "Success") {
                         String data = obj.getString("data");
                         Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                         Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                         Bundle b = new Bundle();
                         b.putBoolean("fromCreateAccount", true);
                         i.putExtras(b);
                         startActivity(i);
                         //method to load survey or show dashboard with welcome message...
                     } else {
                         try {
                             JSONObject data = obj.getJSONObject("data");
                             String errorMessage = "";
                             try {
                                 errorMessage = data.getString("mobile");
                             } catch (Exception ex) {
                                 ex.printStackTrace();
                             }
                             try {
                                 errorMessage = data.getString("email");
                             } catch (Exception ex) {
                                 ex.printStackTrace();
                             }
                             try {
                                 errorMessage = data.getString("password");
                             } catch (Exception ex) {
                                 ex.printStackTrace();
                             }
                             try {
                                 errorMessage = data.getString("company");
                             } catch (Exception ex) {
                                 errorMessage = "Some problem occured... try creating again";
                                 ex.printStackTrace();
                             }

                             Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                         } catch (Exception ex) {
                             ex.printStackTrace();
                         }
                     }
                     Log.d("Response", obj.toString());

                 } catch (Throwable t) {
                     Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.i("response", error.toString());
             }
         }) {
             @Override
             protected Map<String, String> getParams() {

                 Map<String, String> params = new HashMap<String, String>();
                 params.put("tag", "register");
                 params.put("username", "name12");
                 params.put("email", "emai232r");
                 params.put("mobile", "passworwterd");
                 params.put("longitude", "cpasswortertd");
                 params.put("latititude", "compantery");
                 params.put("board", "addresterts");
                 params.put("gender", "countryert");
                 params.put("highest_qualification", "serttate");
                 params.put("about", "cityetre");
                 params.put("password", "zipcoretetde");
 System.out.println(params.toString());
                 return params;
             }

             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 Map<String, String> params = new HashMap<String, String>();
               //  params.put("Content-Type","application/x-www-form-urlencoded");
                 params.put("Content-Type","application/json");
                 return params;
             }
         };
         queue.add(sr);
     }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //  refreshItem.getActionView().clearAnimation();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*    void setSpinnerData(ArrayList<SpinnerItemData> list) {
        list.add(new SpinnerItemData("+93", R.drawable.af));
        list.add(new SpinnerItemData("+355", R.drawable.al));
        list.add(new SpinnerItemData("+213", R.drawable.dz));
        list.add(new SpinnerItemData("+376", R.drawable.ad));
        list.add(new SpinnerItemData("+244", R.drawable.ao));
        list.add(new SpinnerItemData("+54", R.drawable.ar));
        list.add(new SpinnerItemData("+374", R.drawable.am));
        list.add(new SpinnerItemData("+297", R.drawable.aw));
        list.add(new SpinnerItemData("+61", R.drawable.au));
        list.add(new SpinnerItemData("+43", R.drawable.at));
        list.add(new SpinnerItemData("+994", R.drawable.az));
        list.add(new SpinnerItemData("+973", R.drawable.bh));
        list.add(new SpinnerItemData("+880", R.drawable.bd));
        list.add(new SpinnerItemData("+375", R.drawable.by));
        list.add(new SpinnerItemData("+32", R.drawable.be));
        list.add(new SpinnerItemData("+501", R.drawable.bz));
        list.add(new SpinnerItemData("+229", R.drawable.bj));
        list.add(new SpinnerItemData("+975", R.drawable.bt));
        list.add(new SpinnerItemData("+591", R.drawable.bo));
        list.add(new SpinnerItemData("+387", R.drawable.ba));
        list.add(new SpinnerItemData("+267", R.drawable.bw));
        list.add(new SpinnerItemData("+55", R.drawable.br));
        list.add(new SpinnerItemData("+673", R.drawable.bn));
        list.add(new SpinnerItemData("+359", R.drawable.bg));
        list.add(new SpinnerItemData("+226", R.drawable.bf));
        list.add(new SpinnerItemData("+95", R.drawable.mm));
        list.add(new SpinnerItemData("+257", R.drawable.bi));
        list.add(new SpinnerItemData("+855", R.drawable.kh));
        list.add(new SpinnerItemData("+237", R.drawable.cm));
        list.add(new SpinnerItemData("+1", R.drawable.ca));
        list.add(new SpinnerItemData("+238", R.drawable.cv));
        list.add(new SpinnerItemData("+236", R.drawable.cf));
        list.add(new SpinnerItemData("+235", R.drawable.td));
        list.add(new SpinnerItemData("+56", R.drawable.cl));
        list.add(new SpinnerItemData("+86", R.drawable.cn));
        list.add(new SpinnerItemData("+61", R.drawable.cx));
        list.add(new SpinnerItemData("+61", R.drawable.cc));
        list.add(new SpinnerItemData("+57", R.drawable.co));
        list.add(new SpinnerItemData("+269", R.drawable.km));
        list.add(new SpinnerItemData("+242", R.drawable.cg));
        list.add(new SpinnerItemData("+243", R.drawable.cd));
        list.add(new SpinnerItemData("+682", R.drawable.ck));
        list.add(new SpinnerItemData("+506", R.drawable.cr));
        list.add(new SpinnerItemData("+385", R.drawable.hr));
        list.add(new SpinnerItemData("+53", R.drawable.cu));
        list.add(new SpinnerItemData("+357", R.drawable.cy));
        list.add(new SpinnerItemData("+420", R.drawable.cz));
        list.add(new SpinnerItemData("+45", R.drawable.dk));
        list.add(new SpinnerItemData("+253", R.drawable.dj));
        list.add(new SpinnerItemData("+670", R.drawable.tl));
        list.add(new SpinnerItemData("+593", R.drawable.ec));
        list.add(new SpinnerItemData("+20", R.drawable.eg));
        list.add(new SpinnerItemData("+503", R.drawable.sv));
        list.add(new SpinnerItemData("+240", R.drawable.gq));
        list.add(new SpinnerItemData("+291", R.drawable.er));
        list.add(new SpinnerItemData("+372", R.drawable.ee));
        list.add(new SpinnerItemData("+251", R.drawable.et));
        list.add(new SpinnerItemData("+500", R.drawable.fk));
        list.add(new SpinnerItemData("+298", R.drawable.fo));
        list.add(new SpinnerItemData("+679", R.drawable.fj));
        list.add(new SpinnerItemData("+358", R.drawable.fi));
        list.add(new SpinnerItemData("+33", R.drawable.fr));
        list.add(new SpinnerItemData("+689", R.drawable.pf));
        list.add(new SpinnerItemData("+241", R.drawable.ga));
        list.add(new SpinnerItemData("+220", R.drawable.gm));
        list.add(new SpinnerItemData("+995", R.drawable.ge));
        list.add(new SpinnerItemData("+49", R.drawable.de));
        list.add(new SpinnerItemData("+233", R.drawable.gh));
        list.add(new SpinnerItemData("+350", R.drawable.gi));
        list.add(new SpinnerItemData("+30", R.drawable.gr));
        list.add(new SpinnerItemData("+299", R.drawable.gl));
        list.add(new SpinnerItemData("+502", R.drawable.gt));
        list.add(new SpinnerItemData("+224", R.drawable.gn));
        list.add(new SpinnerItemData("+245", R.drawable.gw));
        list.add(new SpinnerItemData("+592", R.drawable.gy));
        list.add(new SpinnerItemData("+509", R.drawable.ht));
        list.add(new SpinnerItemData("+504", R.drawable.hn));
        list.add(new SpinnerItemData("+852", R.drawable.hk));
        list.add(new SpinnerItemData("+36", R.drawable.hu));
        list.add(new SpinnerItemData("+91", R.drawable.in));
        list.add(new SpinnerItemData("+62", R.drawable.id));
        list.add(new SpinnerItemData("+98", R.drawable.ir));
        list.add(new SpinnerItemData("+964", R.drawable.iq));
        list.add(new SpinnerItemData("+353", R.drawable.ie));
        list.add(new SpinnerItemData("+44", R.drawable.im));
        list.add(new SpinnerItemData("+972", R.drawable.il));
        list.add(new SpinnerItemData("+39", R.drawable.it));
        list.add(new SpinnerItemData("+225", R.drawable.ci));
        list.add(new SpinnerItemData("+81", R.drawable.jp));
        list.add(new SpinnerItemData("+962", R.drawable.jo));
        list.add(new SpinnerItemData("+7", R.drawable.kz));
        list.add(new SpinnerItemData("+254", R.drawable.ke));
        list.add(new SpinnerItemData("+686", R.drawable.ki));
        list.add(new SpinnerItemData("+965", R.drawable.kw));
        list.add(new SpinnerItemData("+996", R.drawable.kg));
        list.add(new SpinnerItemData("+856", R.drawable.la));
        list.add(new SpinnerItemData("+371", R.drawable.lv));
        list.add(new SpinnerItemData("+961", R.drawable.lb));
        list.add(new SpinnerItemData("+266", R.drawable.ls));
        list.add(new SpinnerItemData("+231", R.drawable.lr));
        list.add(new SpinnerItemData("+218", R.drawable.ly));
        list.add(new SpinnerItemData("+423", R.drawable.li));
        list.add(new SpinnerItemData("+370", R.drawable.lt));
        list.add(new SpinnerItemData("+352", R.drawable.lu));
        list.add(new SpinnerItemData("+853", R.drawable.mo));
        list.add(new SpinnerItemData("+389", R.drawable.mk));
        list.add(new SpinnerItemData("+261", R.drawable.mg));
        list.add(new SpinnerItemData("+265", R.drawable.mw));
        list.add(new SpinnerItemData("+60", R.drawable.my));
        list.add(new SpinnerItemData("+960", R.drawable.mv));
        list.add(new SpinnerItemData("+223", R.drawable.ml));
        list.add(new SpinnerItemData("+356", R.drawable.mt));
        list.add(new SpinnerItemData("+692", R.drawable.mh));
        list.add(new SpinnerItemData("+222", R.drawable.mr));
        list.add(new SpinnerItemData("+230", R.drawable.mu));
        list.add(new SpinnerItemData("+262", R.drawable.yt));
        list.add(new SpinnerItemData("+52", R.drawable.mx));
        list.add(new SpinnerItemData("+691", R.drawable.fm));
        list.add(new SpinnerItemData("+373", R.drawable.md));
        list.add(new SpinnerItemData("+377", R.drawable.mc));
        list.add(new SpinnerItemData("+976", R.drawable.mn));
        list.add(new SpinnerItemData("+382", R.drawable.me));
        list.add(new SpinnerItemData("+212", R.drawable.ma));
        list.add(new SpinnerItemData("+258", R.drawable.mz));
        list.add(new SpinnerItemData("+264", R.drawable.na));
        list.add(new SpinnerItemData("+674", R.drawable.nr));
        list.add(new SpinnerItemData("+977", R.drawable.np));
        list.add(new SpinnerItemData("+31", R.drawable.nl));
        list.add(new SpinnerItemData("+599", R.drawable.an));
        list.add(new SpinnerItemData("+687", R.drawable.nc));
        list.add(new SpinnerItemData("+64", R.drawable.nz));
        list.add(new SpinnerItemData("+505", R.drawable.ni));
        list.add(new SpinnerItemData("+227", R.drawable.ne));
        list.add(new SpinnerItemData("+234", R.drawable.ng));
        list.add(new SpinnerItemData("+683", R.drawable.nu));
        list.add(new SpinnerItemData("+850", R.drawable.kp));
        list.add(new SpinnerItemData("+47", R.drawable.no));
        list.add(new SpinnerItemData("+968", R.drawable.om));
        list.add(new SpinnerItemData("+92", R.drawable.pk));
        list.add(new SpinnerItemData("+680", R.drawable.pw));
        list.add(new SpinnerItemData("+507", R.drawable.pa));
        list.add(new SpinnerItemData("+675", R.drawable.pg));
        list.add(new SpinnerItemData("+595", R.drawable.py));
        list.add(new SpinnerItemData("+51", R.drawable.pe));
        list.add(new SpinnerItemData("+63", R.drawable.ph));
        list.add(new SpinnerItemData("+870", R.drawable.pn));
        list.add(new SpinnerItemData("+48", R.drawable.pl));
        list.add(new SpinnerItemData("+351", R.drawable.pt));
        list.add(new SpinnerItemData("+1", R.drawable.pr));
        list.add(new SpinnerItemData("+974", R.drawable.qa));
        list.add(new SpinnerItemData("+40", R.drawable.ro));
        list.add(new SpinnerItemData("+7", R.drawable.ru));
        list.add(new SpinnerItemData("+250", R.drawable.rw));
        list.add(new SpinnerItemData("+685", R.drawable.ws));
        list.add(new SpinnerItemData("+378", R.drawable.sm));
        list.add(new SpinnerItemData("+239", R.drawable.st));
        list.add(new SpinnerItemData("+966", R.drawable.sa));
        list.add(new SpinnerItemData("+221", R.drawable.sn));
        list.add(new SpinnerItemData("+381", R.drawable.rs));
        list.add(new SpinnerItemData("+248", R.drawable.sc));
        list.add(new SpinnerItemData("+232", R.drawable.sl));
        list.add(new SpinnerItemData("+65", R.drawable.sg));
        list.add(new SpinnerItemData("+421", R.drawable.sk));
        list.add(new SpinnerItemData("+386", R.drawable.si));
        list.add(new SpinnerItemData("+677", R.drawable.sb));
        list.add(new SpinnerItemData("+252", R.drawable.so));
        list.add(new SpinnerItemData("+27", R.drawable.za));
        list.add(new SpinnerItemData("+82", R.drawable.kr));
        list.add(new SpinnerItemData("+34", R.drawable.es));
        list.add(new SpinnerItemData("+94", R.drawable.lk));
        list.add(new SpinnerItemData("+290", R.drawable.sh));
        list.add(new SpinnerItemData("+508", R.drawable.pm));
        list.add(new SpinnerItemData("+249", R.drawable.sd));
        list.add(new SpinnerItemData("+597", R.drawable.sr));
        list.add(new SpinnerItemData("+268", R.drawable.sz));
        list.add(new SpinnerItemData("+46", R.drawable.se));
        list.add(new SpinnerItemData("+41", R.drawable.ch));
        list.add(new SpinnerItemData("+963", R.drawable.sy));
        list.add(new SpinnerItemData("+886", R.drawable.tw));
        list.add(new SpinnerItemData("+992", R.drawable.tj));
        list.add(new SpinnerItemData("+255", R.drawable.tz));
        list.add(new SpinnerItemData("+66", R.drawable.th));
        list.add(new SpinnerItemData("+228", R.drawable.tg));
        list.add(new SpinnerItemData("+690", R.drawable.tk));
        list.add(new SpinnerItemData("+676", R.drawable.to));
        list.add(new SpinnerItemData("+216", R.drawable.tn));
        list.add(new SpinnerItemData("+90", R.drawable.tr));
        list.add(new SpinnerItemData("+993", R.drawable.tm));
        list.add(new SpinnerItemData("+688", R.drawable.tv));
        list.add(new SpinnerItemData("+971", R.drawable.ae));
        list.add(new SpinnerItemData("+256", R.drawable.ug));
        list.add(new SpinnerItemData("+44", R.drawable.gb));
        list.add(new SpinnerItemData("+380", R.drawable.ua));
        list.add(new SpinnerItemData("+598", R.drawable.uy));
        list.add(new SpinnerItemData("+1", R.drawable.us));
        list.add(new SpinnerItemData("+998", R.drawable.uz));
        list.add(new SpinnerItemData("+678", R.drawable.vu));
        list.add(new SpinnerItemData("+39", R.drawable.va));
        list.add(new SpinnerItemData("+58", R.drawable.ve));
        list.add(new SpinnerItemData("+84", R.drawable.vn));
        list.add(new SpinnerItemData("+681", R.drawable.wf));
        list.add(new SpinnerItemData("+967", R.drawable.ye));
        list.add(new SpinnerItemData("+260", R.drawable.zm));
        list.add(new SpinnerItemData("+263", R.drawable.zw));

    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.ll_creataccount:
                keyboardUtil.hideKeyboard(this);
                break;
            /*case R.id.img_drom_down_creataccount:
                spnrcc.performClick();
                break;*/
        }
        return false;
    }


    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.et_TuitonName:
                etName.setHint("");
                etName.setCursorVisible(true);
                break;
            case R.id.et_email:
                etEmail.setHint("");
                break;
       z     case R.id.et_mobile:
                etMobile.setHint(" ");
                break;
            case R.id.et_password:
                etpassword.setHint(" ");
                break;
            case R.id.et_confirm_password:
                etConfirmePassword.setHint(" ");
                break;
            case R.id.et_address:
                etAddress.setHint(" ");
                break;
            case R.id.et_country:
                etCountry.setHint(" ");
                break;
            case R.id.et_city:
                etCity.setHint(" ");
                break;
            case R.id.et_state:
                etState.setHint(" ");
                break;
            case R.id.et_pincode:
                etPincode.setHint(" ");
                break;
            case R.id.et_company:
                etCompany.setHint(" ");
                break;

        }
        return false;
    }*/


}
