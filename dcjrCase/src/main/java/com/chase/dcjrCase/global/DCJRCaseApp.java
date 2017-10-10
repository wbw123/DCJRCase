package com.chase.dcjrCase.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class DCJRCaseApp extends Application{
    private static Context mainContext;
    private static Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainContext = this;
        mainHandler = new Handler();
    }

    public static Context getContext(){
        return mainContext;
    }

    public static Handler getHandler(){
        return mainHandler;
    }
}