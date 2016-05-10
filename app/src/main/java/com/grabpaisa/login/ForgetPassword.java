package com.grabpaisa.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.grabpaisa.R;
import com.grabpaisa.server.ServerTask;

/**
 * Created by User1 on 16-10-2015.
 */
public class ForgetPassword extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    EditText etForgotPassword;
    LinearLayout lLForgotPassword;
    KeyboardUtil keyboardUtil;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //     toolbar.setVisibility(View.GONE);
        toolbar.setTitle("Forgot password");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        ;
        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Change Action bar Title color
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(Color.WHITE);

        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle("  Forgot password");
        getActionBar().setLogo(R.drawable.ic_arrow_back_white);
        getActionBar().setHomeButtonEnabled(true);*/
        etForgotPassword = (EditText) findViewById(R.id.et_forgot);
        lLForgotPassword = (LinearLayout) findViewById(R.id.ll_forget_password);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        lLForgotPassword.setOnTouchListener(this);
        //  etForgotPassword.setOnTouchListener(this);
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.ll_forget_password:
                keyboardUtil.hideKeyboard(this);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSubmit) {
            if (etForgotPassword.length() != 10) {
                Toast.makeText(getApplicationContext(), "phone number must be 10 digits in length", Toast.LENGTH_SHORT).show();
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("please wait...");
                progressDialog.setCancelable(false);

                HashMap<String, Object> params = new HashMap<String, Object>();

                params.put("Mobileno", etForgotPassword.getText().toString());

                ServerTask getPasswordTask = new ServerTask(ServerTask.API_FORGOT_PASSWORD, params, new ServerTask.Callback() {
                    @Override
                    public void onCompleted(String response) {
                        progressDialog.dismiss();
                        if(response == null || response.equals("null")){
                            Toast.makeText(ForgetPassword.this, "Some error occured", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            try {
                                JSONObject responseJson = new JSONArray(response).getJSONObject(0);
                                int resResult = responseJson.getInt("ResResult");
                                if(resResult == 1){
                                    Toast.makeText(ForgetPassword.this, "password sent as sms", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(ForgetPassword.this, "invalid mobile number", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                progressDialog.show();
                getPasswordTask.execute();
                // forgotPassword(etForgotPassword.getText().toString().trim());
            }
        }

    }

  /*  private void forgotPassword(final String email) {
        final UserSessionManager session = new UserSessionManager(getApplicationContext());
        String serverUrl = getResources().getString(R.string.server_url);
        //SERVER_PATH/auth?cellnumber=9898980000&password=testPwd

        String url = serverUrl + "forgetPassword";
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
                        Toast.makeText(getApplicationContext(), "A verification link has been send to your email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                        //method to load survey or show dashboard with welcome message...
                    } else {
                        try {

                            JSONObject data = obj.getJSONObject("data");
                            String errorMessage = data.getString("email");
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
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(sr);
    }*/
}