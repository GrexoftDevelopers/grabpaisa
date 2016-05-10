package com.grabpaisa.login;

import android.graphics.Bitmap;

import java.util.List;

import com.grabpaisa.dashboard.App;
import com.grabpaisa.dashboard.TelecomOperator;

/**
 * Created by User 1 on 08-10-2015.
 */
public class MySingletone {

    private static MySingletone instance;
    public Bitmap bitmap;
    public String imgSetUriPath;
    public static boolean flageKiosMode;
    public static int position;

    public String registrationId;

    public String referralId, upLineId, name, news, mobile;

    public float totalBalance, totalEarned, totalRecharge, lastRecharge;

    public int  totalAppsInstalled;

    public List<TelecomOperator> telecomOperators;

    public  List<App> apps;

    //SharedPreferences sharedPreferences  = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    private MySingletone() {
    }

    public static MySingletone getInstance()

    {
        if (instance == null) {
            instance = new MySingletone();
        }
        return instance;

    }

    public void resetData(){
        registrationId = null;
        referralId = null;
        upLineId = null;
        news = null;
        name = null;
        mobile = null;
        totalAppsInstalled = 0;
        totalRecharge = 0;
        totalEarned = 0;
        lastRecharge = 0;
        totalBalance = 0;
    }

}
