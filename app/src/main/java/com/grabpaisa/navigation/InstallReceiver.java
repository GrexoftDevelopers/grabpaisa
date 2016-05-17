package com.grabpaisa.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.SyncStateContract;

import com.grabpaisa.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by tahir on 5/17/2016.
 */
public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String rawReferrer = intent.getStringExtra("referrer");
        if (rawReferrer != null) {
            System.out.println("raw referrer : " + rawReferrer);
            trackReferrerAttributes(rawReferrer, context);
        }
    }

    private void trackReferrerAttributes(String rawReferrer, Context context) {
        String referrer = "";

        try {
            referrer = URLDecoder.decode(rawReferrer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return;
        }

        if (referrer == null && referrer.equals("")) {
            return;
        }

        Uri uri = Uri.parse('?' + referrer); // appends ? for Uri to pickup query string

        String memberCode = null;
        try {
            memberCode = uri.getQueryParameter("utm_source");
        } catch (UnsupportedOperationException e) {
            return;
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(
                BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).edit();

        if (memberCode != null && !memberCode.equals("")) {
            System.out.println("member : " + memberCode);
            editor.putString("referrer", memberCode);
        }

//        String referralMedium = uri.getQueryParameter("medium");
//        if (referralMedium != null && !referralMedium.equals("")) {
//            System.out.println("medium : " + referralMedium);
//            editor.putString("referral_medium", referralMedium);
//        }

        editor.commit();
    }

}
