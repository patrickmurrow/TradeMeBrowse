package com.example.patrickmurrow.trademebrowse;

import android.content.Context;

/**
 * Created by patrickmurrow on 9/11/17.
 */

public class Application extends android.app.Application {
    private static Application INSTANCE;

    public static Context getContext() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
    }
}
