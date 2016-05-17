package com.grabpaisa.server;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tahir on 5/3/2016.
 */
public class ServerTask extends AsyncTask<String,String, String> {

    private static final String TAG = "SOAP RETURN";

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;

    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String BASE_URL = "http://grabpaisa.com/AppAPI/";

    public static final String API_REGISTRATION = "CheckCustomerRegistration";
    public static final String API_LOGIN = "CheckCustomerLogin";
    public static final String API_GET_DASHBOARD = "GetCustDashBoard";
    public static final String API_GET_APPS = "CheckAppDownloadList";
    public static final String API_GET_RECENT_RECHARGES = "GetCustRechargeList";
    public static final String API_FORGOT_PASSWORD = "ForgetPassword";
    public static final String API_GET_OPERATOR_LIST = "GetOperatorList";
    public static final String API_CHANGE_PASSWORD = "ChangeCustomerPassword";
    public static final String API_UPDATE_PROFILE = "CustomerEditProfile";
    public static final String API_RECHARGE = "CustRechargeRequest";
    public static final String API_DOWNLOAD_APP = "AppDownloadByCustomer";

    private String apiName;

    private HashMap<String,Object> params;

    SoapSerializationEnvelope envelope;

    private Callback mCallback;

    public ServerTask(String apiName, HashMap<String,Object> params, Callback callback){

        this.apiName = apiName;
        this.params = params;
        this.mCallback = callback;

    }

    @Override
    protected void onPreExecute() {

        SoapObject request = new SoapObject(NAMESPACE, apiName);

        SoapObject data = null;

        switch(apiName){
            case API_REGISTRATION:
                data = new SoapObject(NAMESPACE, "oLoginRegData");
                break;
            case API_LOGIN:
                data = new SoapObject(NAMESPACE, "oLogindata");
                break;
            case API_GET_RECENT_RECHARGES:
            case API_GET_DASHBOARD:
                data = new SoapObject(NAMESPACE,"oCustDashBoard");
                break;
            case API_GET_OPERATOR_LIST:
            case API_GET_APPS:
                data = null;
                break;
            case API_FORGOT_PASSWORD:
                data = new SoapObject(NAMESPACE,"oCustMobile");
                break;
            case API_CHANGE_PASSWORD:
                data = new SoapObject(NAMESPACE,"oCustChangePass");
                break;
            case API_UPDATE_PROFILE:
                data = new SoapObject(NAMESPACE,"oCustEditProfile");
                break;
            case API_RECHARGE:
                data = new SoapObject(NAMESPACE,"oCustRechargeRequest");
                break;
            case API_DOWNLOAD_APP:
                data = new SoapObject(NAMESPACE,"oAppNamedDownloadByCustomer");
                break;

        }

        if(params != null && data != null){

            for(Map.Entry entry : params.entrySet()){
                data.addProperty(entry.getKey().toString(), entry.getValue());
                Log.v(TAG,entry.getKey().toString() + " : " + entry.getValue().toString());
            }

            request.addSoapObject(data);

        }

        envelope = getSoapSerializationEnvelope(request);
        Log.v(TAG,envelope.toString());

    }

    @Override
    protected String doInBackground(String... params) {

        String response = null;
        HttpTransportSE httpTransportSE = getHttpTransportSE();
        try{
            httpTransportSE.call(NAMESPACE + apiName, envelope);
            Log.v(TAG, httpTransportSE.requestDump);

            testHttpResponse(httpTransportSE);

            SoapPrimitive responsePrimitive = (SoapPrimitive)envelope.getResponse();

            Log.v(TAG, "envelope body : " + envelope.bodyIn.toString());

            response = responsePrimitive.toString();

            Log.v(TAG,"reponse : " + response);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        if(mCallback != null){
            mCallback.onCompleted(s);
        }
    }

    // get SOAP envelope for a given request
    private final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    private final HttpTransportSE getHttpTransportSE() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(BASE_URL);
        switch(apiName){
            case API_REGISTRATION:
                urlBuilder.append("WSRegistration.asmx?op=CheckCustomerRegistration");
                break;
            case API_LOGIN:
                urlBuilder.append("WSLogin.asmx?op=CheckCustomerLogin");
                break;
            case API_GET_DASHBOARD:
                urlBuilder.append("WSGetDashboard.asmx?op=GetCustDashBoard");
                break;
            case API_GET_OPERATOR_LIST:
                urlBuilder.append("WSGetDashboard.asmx?op=GetOperatorList");
                break;
            case API_GET_APPS:
                urlBuilder.append("WSDownloadApp.asmx?op=CheckAppDownloadList");
                break;
            case API_GET_RECENT_RECHARGES:
                urlBuilder.append("WSGetDashboard.asmx?op=GetCustRechargeList");
                break;
            case API_FORGOT_PASSWORD:
                urlBuilder.append("WSLogin.asmx?op=ForgetPassword");
                break;
            case API_CHANGE_PASSWORD:
                urlBuilder.append("EditProfile.asmx?op=ChangeCustomerPassword");
                break;
            case API_UPDATE_PROFILE:
                urlBuilder.append("EditProfile.asmx?op=CustomerEditProfile");
                break;
            case API_RECHARGE:
                urlBuilder.append("Recharge.asmx?op=CustRechargeRequest");
                break;
            case API_DOWNLOAD_APP:
                urlBuilder.append("WSDownLoadApp.asmx?op=AppDownloadByCustomer");
                break;
        }
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,urlBuilder.toString(),60000);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }

    private final void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v(TAG, "Request XML:\n" + ht.requestDump);
            Log.v(TAG, "Response XML:\n" + ht.responseDump);
        }
    }

    public interface Callback{

        public void onCompleted(String response);

    }



}
