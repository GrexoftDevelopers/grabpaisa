package com.grabpaisa.dashboard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.grabpaisa.R;
import com.grabpaisa.login.MySingletone;
import com.grabpaisa.server.ServerTask;


/**
 * Created by HP on 4/21/2016.
 */
public class Shopping extends Fragment {
    View view;
    private MySingletone mySingletone;

    private Spinner spinnerOperators;

    private ArrayList<String> operatorNames;

    private ArrayList<RechargeItem> recentRecharges;
    private ProgressBar recentProgress;
    private ListView recentRechargeList;

    private TextView txtNoRecharge;
    private Button btnRecharge;
    private EditText etAmount, etMobile;

    public Shopping() {
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
        view = inflater.inflate(R.layout.shoping_fragment, container, false);

        mySingletone = MySingletone.getInstance();

        spinnerOperators = (Spinner)view.findViewById(R.id.spinner_operators);

        recentRechargeList = (ListView)view.findViewById(R.id.list_recent_recharge);
        recentProgress = (ProgressBar)view.findViewById(R.id.progress_recent_recharge);

        txtNoRecharge = (TextView)view.findViewById(R.id.txt_error_no_recharge);

        etAmount = (EditText)view.findViewById(R.id.et_amount);
        etMobile = (EditText)view.findViewById(R.id.et_mobile);

        btnRecharge = (Button)view.findViewById(R.id.btn_recharge);

        if(mySingletone.telecomOperators == null || mySingletone.telecomOperators.isEmpty()){

            btnRecharge.setEnabled(false);

            ServerTask getOperatorsTask = new ServerTask(ServerTask.API_GET_OPERATOR_LIST, null, new ServerTask.Callback() {
                @Override
                public void onCompleted(String response) {

                    try {
                        JSONArray responseJson = new JSONArray(response);
                        System.out.println("response json : " + responseJson.toString());

                        if(responseJson.length() > 0){

                            mySingletone.telecomOperators = new ArrayList<TelecomOperator>();
                            operatorNames = new ArrayList<String>();

                            TelecomOperator telecomOperator;

                            for(int i = 0; i < responseJson.length(); i++){
                                JSONObject operatorJson = responseJson.getJSONObject(i);
                                telecomOperator = new TelecomOperator(operatorJson.getString("OpCode"),operatorJson.getString("OperatorName"));
                                mySingletone.telecomOperators.add(telecomOperator);
                                operatorNames.add(operatorJson.getString("OperatorName"));
                            }
                            btnRecharge.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setOperators();
                }
            });

            getOperatorsTask.execute();

        }

        else{
            operatorNames = new ArrayList<String>();
            for(TelecomOperator telecomOperator : mySingletone.telecomOperators){
                operatorNames.add(telecomOperator.getName());
            }
            setOperators();
        }

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("CustRegNo", Long.parseLong(mySingletone.registrationId));

        ServerTask getRecentRechargeTask = new ServerTask(ServerTask.API_GET_RECENT_RECHARGES, params, new ServerTask.Callback() {
            @Override
            public void onCompleted(String response) {

                if(response != null && !response.equals("null")){

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        if(jsonArray.length() > 0){

                            recentRecharges = new ArrayList<RechargeItem>();

                            JSONObject jsonObject;

                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                String sNo = jsonObject.getString("SN");
                                String amount = jsonObject.getString("Amt");
                                String operatorName = jsonObject.getString("OperatorName");
                                String requestDate = jsonObject.getString("RequesDate");
                                String approvedDate = jsonObject.getString("ApprovedDate");
                                String status = jsonObject.getString("Status");

                                RechargeItem rechargeItem = new RechargeItem(sNo,amount,operatorName,status,requestDate,approvedDate);

                                recentRecharges.add(rechargeItem);

                            }

                            setRecentRecharge();

                        }
                        else{
                            setRecentRecharge();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    setRecentRecharge();
                }

            }
        });

        getRecentRechargeTask.execute();

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int operatorIndex = spinnerOperators.getSelectedItemPosition();
                String operatorCode = mySingletone.telecomOperators.get(operatorIndex).getCode();

                String mobile = etMobile.getText().toString();
                if(mobile.equals("")){
                    Toast.makeText(getActivity(), "enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String amount = etAmount.getText().toString();
                if(amount.equals("") && Integer.parseInt(amount) < 30){
                    Toast.makeText(getActivity(), "Amount should be at least 30", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("CustRegno",mySingletone.registrationId);
                params.put("amt", amount);
                params.put("OperatorCode", operatorCode);
                params.put("Mobile",mobile);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("sending recharge request");

                ServerTask rechargeTask = new ServerTask(ServerTask.API_RECHARGE, params, new ServerTask.Callback() {
                    @Override
                    public void onCompleted(String response) {
                        progressDialog.dismiss();
                        if(response == null || response.equals("null")){
                            Toast.makeText(getActivity(), "Recharge request failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONObject responseJSON = new JSONArray(response).getJSONObject(0);
                            String resResult = responseJSON.getString("ResResult");
                            if(!resResult.equals("Request Submitted Successfully.")){
                                Toast.makeText(getActivity(), "Recharge request failed", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setMessage("Request processed. You will receive a recharge message shortly.").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                progressDialog.show();
                rechargeTask.execute();

            }
        });

        return view;
    }


    private void setOperators(){

        if(operatorNames != null && !operatorNames.isEmpty()){

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, operatorNames);
            spinnerOperators.setAdapter(adapter);
        }



    }

    private void setRecentRecharge(){

        if(recentRecharges != null && !recentRecharges.isEmpty()){
            recentProgress.setVisibility(View.GONE);
            recentRechargeList.setAdapter(new RechargeAdapter());
            txtNoRecharge.setVisibility(View.GONE);
        }
        else{
            recentProgress.setVisibility(View.GONE);
            txtNoRecharge.setVisibility(View.VISIBLE);
            recentRechargeList.setVisibility(View.GONE);
        }

    }

    class RechargeAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            if(recentRecharges != null && !recentRecharges.isEmpty()) return recentRecharges.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rechargeView = null;

            if(convertView != null) rechargeView = convertView;

            else{
                rechargeView = getActivity().getLayoutInflater().inflate(R.layout.item_recharge,null);
            }

            ((TextView)rechargeView.findViewById(R.id.txt_amount)).setText(recentRecharges.get(position).getAmount());
            ((TextView)rechargeView.findViewById(R.id.txt_status)).setText(recentRecharges.get(position).getStatus());
            ((TextView)rechargeView.findViewById(R.id.txt_operator)).setText(recentRecharges.get(position).getOperatorName());
            ((TextView)rechargeView.findViewById(R.id.txt_request_date)).setText("Request date : " + recentRecharges.get(position).getRequestDate());
            ((TextView)rechargeView.findViewById(R.id.txt_approved_date)).setText("Approved date : " + recentRecharges.get(position).getApprovedDate());


            return rechargeView;
        }
    }

}