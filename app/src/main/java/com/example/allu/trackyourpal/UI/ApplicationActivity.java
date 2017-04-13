package com.example.allu.trackyourpal.UI;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

/**
 * Created by allu on 4/10/17.
 */

public class ApplicationActivity extends Application {
    String TAG = ApplicationActivity.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"init");
    }
}
