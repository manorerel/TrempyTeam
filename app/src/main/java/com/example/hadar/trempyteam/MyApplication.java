package com.example.hadar.trempyteam;

import android.app.Application;
import android.content.Context;

/**
 * Created by מנור on 27/03/2017.
 */

public class MyApplication extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
