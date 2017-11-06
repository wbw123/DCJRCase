package com.chase.dcjrCase.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;

public class DCJRCaseApp extends Application{
    private static Context mainContext;
    private static Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainContext = this;
        mainHandler = new Handler();
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static Context getContext(){
        return mainContext;
    }

    public static Handler getHandler(){
        return mainHandler;
    }
}