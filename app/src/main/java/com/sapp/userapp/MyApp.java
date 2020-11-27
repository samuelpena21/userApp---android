package com.sapp.userapp;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}
