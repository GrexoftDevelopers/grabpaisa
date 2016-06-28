package com.grabpaisa.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.grabpaisa.R;
import com.grabpaisa.business.BusinessRecyclerAdapter;
import com.grabpaisa.login.MySingletone;
import com.grabpaisa.navigation.HomeActivity;
import com.grabpaisa.server.ServerTask;

/**
 * Created by HP on 4/21/2016.
 */
public class ChallengeFragment extends Fragment {
    View view;
    private BusinessRecyclerAdapter adapter;
    RecyclerView recyclerView;

    private Button btnGetApps, btnInvite;

    MySingletone mySingletone;

    private DashBoard parent;

    private TextView txtMobile, txtUplineId, txtReferralId, txtNews, txtAppsLeft, txtBalance, txtAppsInstalled, txtEarnings, txtTotalRecharge, txtLastRecharge;

    private View scrollView, footer, progressBar;

    public ChallengeFragment() {
        // Required empty public constructor
    }

    public void setParent(DashBoard parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.challange_fragmnent, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        scrollView = view.findViewById(R.id.scrollview);
        progressBar = view.findViewById(R.id.progress_bar);
        footer = view.findViewById(R.id.footer);

        txtUplineId = (TextView)view.findViewById(R.id.txt_upline_id);
        txtReferralId = (TextView)view.findViewById(R.id.txt_referral_id);
        txtMobile = (TextView)view.findViewById(R.id.txt_mobile);
        txtNews = (TextView)view.findViewById(R.id.txt_news);
        txtAppsLeft = (TextView)view.findViewById(R.id.txt_apps_left);
        txtBalance = (TextView)view.findViewById(R.id.txt_balance);
        txtAppsInstalled = (TextView)view.findViewById(R.id.txt_total_apps_installed);
        txtEarnings = (TextView)view.findViewById(R.id.txt_earning);
        txtTotalRecharge = (TextView)view.findViewById(R.id.txt_total_recharge);
        txtLastRecharge = (TextView)view.findViewById(R.id.txt_last_recharge);

        btnGetApps = (Button)view.findViewById(R.id.btn_apps_left);
        btnGetApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parent != null){
                    parent.showApps();
                }
            }
        });

        //btnGetApps.setOnClickListener();

        mySingletone = MySingletone.getInstance();

        //Toast.makeText(getActivity(), "on create view of challenge fragment", Toast.LENGTH_SHORT).show();

        if(mySingletone.referralId == null){

            scrollView.setVisibility(View.INVISIBLE);
            footer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            HashMap<String, Object> params = new HashMap<String, Object>();

            params.put("CustRegNo",Long.parseLong(mySingletone.registrationId));

            ServerTask getDashboardTask = new ServerTask(ServerTask.API_GET_DASHBOARD, params, new ServerTask.Callback() {
                @Override
                public void onCompleted(String response) {
                    try {
                        JSONObject info = new JSONArray(response).getJSONObject(0);
                        mySingletone.referralId = info.getString("Loginid");
                        mySingletone.email = info.getString("EmailID");
                        mySingletone.name = info.getString("Name");
                        mySingletone.upLineId = info.getString("UplineID");
                        mySingletone.news = info.getString("LiveNews");
                        mySingletone.totalBalance = Float.parseFloat(info.getString("EwalletBal"));
                        mySingletone.totalAppsInstalled = Integer.parseInt(info.getString("TotAppInstalled"));
                        mySingletone.totalEarned = Float.parseFloat(info.getString("TotEarnedAppIst"));
                        mySingletone.totalRecharge = Float.parseFloat(info.getString("TotRecharge"));
                        mySingletone.lastRecharge = Float.parseFloat(info.getString("LastRecharge"));
                        mySingletone.mobile = info.getString("Mobile");
                        mySingletone.address = info.getString("Address");

                        fillData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                }
            });

            getDashboardTask.execute();

        }

        else{
            fillData();
        }

        btnInvite = (Button)view.findViewById(R.id.btn_invite);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent();

                share.setAction(Intent.ACTION_SEND);

                share.setType("text/plain");

                String shareDescription = "I just download an app from Google play store that gives you minimum 42 rupees free mobile recharge. Just try it."
                        + "\n"
                        + "Use referral id : " + mySingletone.referralId + " to register"
                        + "\n"
                        + "https://play.google.com/store/apps/details?id=com.grabpaisa&referrer=utm_source%3D" + mySingletone.referralId;

                share.putExtra(Intent.EXTRA_TEXT, shareDescription);

                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getActivity().startActivity(Intent.createChooser(share,
                        "Select app to share"));

            }
        });

        if(((HomeActivity)getActivity()).firstTimeLogin){
            view.findViewById(R.id.txt_welcome_message).setVisibility(View.VISIBLE);
            view.findViewById(R.id.txt_welcome_title).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.txt_welcome_message).setVisibility(View.GONE);
            view.findViewById(R.id.txt_welcome_title).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(getActivity(), "on start", Toast.LENGTH_SHORT).show();
        if(mySingletone.apps != null && !mySingletone.apps.isEmpty()){
            int appsLeft = mySingletone.apps.size() - mySingletone.totalAppsInstalled;
            String appString = null;
            if(appsLeft == 1) appString = "app";
            else appString = "apps";
            txtAppsLeft.setText(getActivity().getString(R.string.app_left,appsLeft,appString));
        }

    }

    private void fillData(){

        //Toast.makeText(getActivity(), "fill data", Toast.LENGTH_SHORT).show();

        scrollView.setVisibility(View.VISIBLE);
        footer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        txtMobile.setText(mySingletone.mobile);
        txtReferralId.setText("Referral Id : " + mySingletone.referralId);
        //txtUplineId.setText("Upline Id : " + mySingletone.upLineId);
        txtNews.setText(mySingletone.news);
        txtBalance.setText(mySingletone.totalBalance + "");
        txtAppsInstalled.setText(mySingletone.totalAppsInstalled + "");
        txtEarnings.setText(mySingletone.totalEarned + "");
        txtTotalRecharge.setText(mySingletone.totalRecharge + "");
        txtLastRecharge.setText(mySingletone.lastRecharge + "");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mySingletone.name);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        //Toast.makeText(getActivity(), mySingletone.name, Toast.LENGTH_SHORT).show();

    }



}