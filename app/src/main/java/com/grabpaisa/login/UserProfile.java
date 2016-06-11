package com.grabpaisa.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grabpaisa.R;
import com.grabpaisa.server.ServerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User1 on 15-01-2016.
 */
public class UserProfile extends Fragment {


    private View view;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle("User Profile");
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_profile, container, false);

        final EditText etName = (EditText)view.findViewById(R.id.et_user_name);
        final EditText etEmail = (EditText)view.findViewById(R.id.et_user_email);
        final EditText etMobile = (EditText)view.findViewById(R.id.et_user_mobile);
        final EditText etCurrentPassword = (EditText)view.findViewById(R.id.et_user_current_password);
        final EditText etNewPassword = (EditText)view.findViewById(R.id.et_user_new_password);
        final EditText etRepeatPassword = (EditText)view.findViewById(R.id.et_user_repeat_password);
        final EditText etAddress = (EditText)view.findViewById(R.id.et_user_address);

        final MySingletone mySingletone = MySingletone.getInstance();

        etMobile.setText(mySingletone.mobile);
        etMobile.setEnabled(false);
        etEmail.setText(mySingletone.email);
        etName.setText(mySingletone.name);
        etAddress.setText(mySingletone.address);

        Button btnUpdate = (Button)view.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String name = etName.getText().toString();
                final String address = etAddress.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getActivity(), "enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(getActivity(), "enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("CustRegNo", mySingletone.registrationId);
                params.put("Name", name);
                params.put("EmailId", email);
                params.put("Address", address);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("updating...");

                ServerTask updateTask = new ServerTask(ServerTask.API_UPDATE_PROFILE, params, new ServerTask.Callback() {
                    @Override
                    public void onCompleted(String response) {
                        progressDialog.dismiss();
                        if (response == null || response.equals("null")) {
                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONObject responseJSON = new JSONArray(response).getJSONObject(0);
                            int resResult = responseJSON.getInt("ResResult");
                            if (resResult == 0) {
                                Toast.makeText(getActivity(), "update failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "profile updated", Toast.LENGTH_SHORT).show();
                                mySingletone.email = email;
                                mySingletone.address = address;
                                mySingletone.name = name;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                progressDialog.show();
                updateTask.execute();
            }
        });

        Button btnChangePassword = (Button)view.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String repeatPassword = etRepeatPassword.getText().toString();

                if(currentPassword.equals("")){
                    Toast.makeText(getActivity(), "enter current password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(newPassword.equals("")){
                    Toast.makeText(getActivity(), "enter new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(repeatPassword.equals("")){
                    Toast.makeText(getActivity(), "confirm new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!newPassword.equals(repeatPassword)){
                    Toast.makeText(getActivity(), "password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("CustRegNo", mySingletone.registrationId);
                params.put("oldPass", currentPassword);
                params.put("NewPass", newPassword);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("changing password");

                ServerTask changePasswordTask = new ServerTask(ServerTask.API_CHANGE_PASSWORD, params, new ServerTask.Callback() {
                    @Override
                    public void onCompleted(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject responseJSON = new JSONArray(response).getJSONObject(0);
                            int resResult = responseJSON.getInt("ResResult");
                            if(resResult == 0){
                                Toast.makeText(getActivity(), "incorrect current password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "password changed", Toast.LENGTH_SHORT).show();
                                etCurrentPassword.setText("");
                                etNewPassword.setText("");
                                etRepeatPassword.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                progressDialog.show();
                changePasswordTask.execute();




            }
        });

        ImageButton btnNewPasswordShow = (ImageButton)view.findViewById(R.id.btn_new_password_show);

        btnNewPasswordShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etNewPassword.setTransformationMethod(null);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
                return true;
            }
        });

        ImageButton btnOldPasswordShow = (ImageButton)view.findViewById(R.id.btn_old_password_show);

        btnOldPasswordShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    etCurrentPassword.setTransformationMethod(null);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    etCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
                return true;
            }
        });

        ImageButton btnRepeatPasswordShow = (ImageButton)view.findViewById(R.id.btn_repeat_password_show);

        btnRepeatPasswordShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    etRepeatPassword.setTransformationMethod(null);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    etRepeatPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
                return true;
            }
        });


        return view;
    }


}
