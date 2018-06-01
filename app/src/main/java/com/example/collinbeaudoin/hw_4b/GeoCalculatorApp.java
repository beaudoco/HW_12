package com.example.collinbeaudoin.hw_4b;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class GeoCalculatorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
