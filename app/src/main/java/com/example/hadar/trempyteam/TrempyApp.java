package com.example.hadar.trempyteam;

import android.app.Application;
import android.content.Context;

/**
 * Created by aviac on 3/25/2017.
 */

public class TrempyApp extends Application{

        private static Context context;
        public void onCreate() {
            super.onCreate();
            TrempyApp.context = getApplicationContext();
        }
        public static Context getAppContext() {
            return TrempyApp.context;
        }
    }


