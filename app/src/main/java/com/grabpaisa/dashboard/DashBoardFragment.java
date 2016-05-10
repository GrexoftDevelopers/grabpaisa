package com.grabpaisa.dashboard;

/**
 * Created by HP on 4/21/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.grabpaisa.R;
import com.grabpaisa.business.BusinessRecyclerAdapter;
import com.grabpaisa.login.MySingletone;
import com.grabpaisa.server.ServerTask;


public class DashBoardFragment extends Fragment {
    View view;
    private BusinessRecyclerAdapter adapter;
    RecyclerView recyclerView;

    MySingletone mySingletone;

    ProgressBar progressBar;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setVisibility(View.GONE);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mySingletone = MySingletone.getInstance();

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        if(mySingletone.apps == null || mySingletone.apps.isEmpty()){

            progressBar.setVisibility(View.VISIBLE);

            ServerTask getAppsTask = new ServerTask(ServerTask.API_GET_APPS, null, new ServerTask.Callback() {
                @Override
                public void onCompleted(String response) {

                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONArray responseJson = new JSONArray(response);
                        System.out.println("response json : " + responseJson.toString());

                        if(responseJson.length() > 0){

                            mySingletone.apps = new ArrayList<App>();

                            App app;

                            for(int i = 0; i < responseJson.length(); i++){
                                JSONObject operatorJson = responseJson.getJSONObject(i);
                                app = new App(operatorJson.getString("Name"),operatorJson.getString("DLink"));
                                mySingletone.apps.add(app);
                            }

                            setApplications();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            getAppsTask.execute();
        }
        else{
            setApplications();
        }

        //for(int i = 0; i < 10; i++) apps.add(new App("app_" + (i+1), "no link"));

        return view;
    }

    private void setApplications(){
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new BusinessRecyclerAdapter(getActivity(), mySingletone.apps);
        recyclerView.setAdapter(adapter);
    }


}