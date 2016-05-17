package com.grabpaisa.navigation;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.grabpaisa.R;

/**
 * Created by tahir on 5/17/2016.
 */
public class GrabPaisaApplication extends Application {

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

/*
 adb shell am broadcast -a com.android.vending.INSTALL_REFERRER
    -n "com.grabpaisa/com.grabpaisa.navigation.InstallReceiver"
            --es referrer "member=gp00417&medium=wp"
            */
}
