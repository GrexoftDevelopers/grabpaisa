package com.grabpaisa.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


import com.grabpaisa.R;
import com.grabpaisa.navigation.HomeActivity;
import com.grabpaisa.server.ServerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener, View.OnTouchListener {
    Button btnLogin, btnSignUp;
    TextView forGotPassword;
    ViewPager viewPager;

    LinearLayout linearLayout, llInclued;
    ImageView imgLogo;
    ScaleAnimation scale;
    EditText etUserName, etPassword;
    AlphaAnimation animation1;

    SharedPreferences sharedPreferences;

    Context context;
    private String SyncError = "You are offline. Please connect to an Internet connection and try again";
    private String InternetError = "Oops!, Something's wrong in you internet connection. Please Try again";
    private MySingletone singletone;

    public static final int RC_HOMEACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("grabpaisa",MODE_PRIVATE);

        String regId = sharedPreferences.getString("reg_id", null);

        singletone = MySingletone.getInstance();

        if(regId != null){
            singletone.registrationId = regId;

            //Toast.makeText(CreateAcount.this, "response : " + response, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            Bundle b = new Bundle();
            b.putBoolean("fromCreateAccount", false);
            intent.putExtras(b);
            //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, RC_HOMEACTIVITY);
        }


        //      getActionBar().hide();
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        forGotPassword = (TextView) findViewById(R.id.et_forgot);

        linearLayout = (LinearLayout) findViewById(R.id.layout);

        etPassword = (EditText) findViewById(R.id.et_password);
        etUserName = (EditText) findViewById(R.id.user_name);


        //imgLogo.startAnimation(scale);
        //   imgLogo.startAnimation(animationSet);
        forGotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        etPassword.setOnTouchListener(this);
        etUserName.setOnTouchListener(this);
        linearLayout.setOnTouchListener(this);


    }


    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("http://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, "com.nopaltechnologies.resume")));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        // KeyboardUtil.hideKeyboard(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();


                if (userName.equals("") ){//|| password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("CUsername",userName);
                    params.put("CPassword",password);

                    final ProgressDialog progressDialog = new ProgressDialog(this);

                    progressDialog.setMessage("Signing in, please wait");

                    ServerTask serverTask = new ServerTask(ServerTask.API_LOGIN, params, new ServerTask.Callback() {
                        @Override
                        public void onCompleted(String response) {

                            progressDialog.dismiss();

                            if(response == null || response.equals("null")){
                                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            try {
                                JSONArray responseJson = new JSONArray(response);
                                System.out.println("response json : " + responseJson.toString());
                                String resResult = responseJson.getJSONObject(0).getString("ResResult");
                                System.out.println("Res Result : " + resResult);
                                ArrayList<String> errors = new ArrayList<String>();
                                errors.add("Invalid Referral ID");
                                errors.add("Mobile no. is Already Exists,Please Try Another");
                                if(errors.contains(resResult)){
                                    Toast.makeText(MainActivity.this, resResult, Toast.LENGTH_SHORT).show();
//                                    // temporary code
//                                    MySingletone singletone = MySingletone.getInstance();
//                                    singletone.registrationId = "3";
//
//                                    //Toast.makeText(CreateAcount.this, "response : " + response, Toast.LENGTH_SHORT).show();
//
//                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                    Bundle b = new Bundle();
//                                    b.putBoolean("fromCreateAccount", false);
//                                    intent.putExtras(b);
//                                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
                                }
                                else{

                                    singletone.registrationId = resResult;

                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("reg_id", resResult);

                                    editor.commit();

                                    //Toast.makeText(CreateAcount.this, "response : " + response, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    Bundle b = new Bundle();
                                    b.putBoolean("fromCreateAccount", false);
                                    intent.putExtras(b);
                                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivityForResult(intent, RC_HOMEACTIVITY);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    progressDialog.show();

                    serverTask.execute();
                }

                break;
            case R.id.btn_signUp:


                //  btnSignUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonbg));
                Intent intentdemo = new Intent(MainActivity.this, CreateAcount.class);
                // intentdemo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentdemo);
                break;
            case R.id.et_forgot:
                Intent forget = new Intent(MainActivity.this, ForgetPassword.class);
                startActivity(forget);

                Toast.makeText(getApplication(), "Forgot Password", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RC_HOMEACTIVITY:
                    if(data.hasExtra("is_logged_in") && data.getExtras().getBoolean("is_logged_in")) {
                        finish();
                    }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        llInclued.setVisibility(View.GONE);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /*case R.id.et_password:
                etPassword.setCursorVisible(true);
                etPassword.setHint(" ");
            case R.id.user_name:
                etUserName.setCursorVisible(true);
                etUserName.setHint(" ");*/
            case R.id.layout:
                // keyboardUtil.hideKeyboard(this);
                break;
        }
        return false;
    }

    /*private void signIn(final String email, final String password) {
        *//*{

            "email":"sandeep@vizz.in",
                "password":"ashraf",
                "device_uuid":"7058F560-758D-4304-8A08-005E7C3024DE"
        }*//*
        final UserSessionManager session = new UserSessionManager(getApplicationContext());
        String serverUrl = getResources().getString(R.string.server_url);
        //SERVER_PATH/auth?cellnumber=9898980000&password=testPwd

        String url = serverUrl + "login";
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
                    if (obj.getString("status").equals("Success")) {
                        JSONObject data = obj.getJSONObject("data");

                        String agentId = data.getString("agentID");
                        String companyID = data.getString("companyID");
                        String name = data.getString("name");
                        String email = data.getString("email");
                        session.setCompanyId(companyID);


                        //method to load survey or show dashboard with welcome message...
                    } else {
                        try {
                            JSONObject data = obj.getJSONObject("data");
                            String errorMessage = "";
                            session.setCompanyId("");
                            try {
                                errorMessage = data.getString("device_uuid");
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Bundle b = new Bundle();
                                b.putBoolean("fromCreateAccount", false);
                                intent.putExtras(b);
                                //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } catch (Exception ex) {
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
                params.put("email", email);
                params.put("password", password);
                params.put("device_uuid", session.getDeviceId());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(sr);

    }
*/


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showDialogBox(String msg, String title, final Boolean finish) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finish) {
                            finish();
                        }
                    }

                }).show();
    }



  /* private void getLocation() {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location) {
                try {
                    Log.i("location", "lat:" + location.getLatitude());
                }
                catch (Exception ex)
                {
                    Log.i("location", "lat:" + "some problem occur... try getting location from other app");
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        if(!myLocation.getLocation(this, locationResult)){
            showAlert();
        }
    }*/


}
