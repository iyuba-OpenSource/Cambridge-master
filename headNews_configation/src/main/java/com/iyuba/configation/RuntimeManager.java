package com.iyuba.configation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

public class RuntimeManager {
    private static Application application;
    private static DisplayMetrics displayMetrics;
    private static Context context;

    // public static Context context;

    public static void setApplicationContext(Context mcontext) {
        context = mcontext;
    }

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application mApplication) {
        application = mApplication;
    }

    public static Context getContext() {
        return context;
    }

    public static String getString(int resourcesID) {
        return application.getResources().getString(resourcesID);
    }

    public static Object getSystemService(String ServiceName) {
        return application.getSystemService(ServiceName);
    }

    public static void setDisplayMetrics(Activity activity) {
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
    }

    public static int getWindowWidth() {
        return displayMetrics.widthPixels;

    }

    public static int getWindowHeight() {
        return displayMetrics.heightPixels;

    }

    public static int dip2px(float dpValue) {
        float scale;
        if (displayMetrics != null) {
            scale = displayMetrics.density;
        } else {
            scale = 1;
        }
        return (int) (dpValue * scale + 0.5F);
    }

    public static int px2dip(float pxValue) {
        float scale = displayMetrics.density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int px2sp(float pxValue) {
        float fontScale = displayMetrics.scaledDensity;
        return (int) (pxValue / fontScale + 0.5F);
    }

    public static int sp2px(float spValue) {
        float fontScale = displayMetrics.scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }
}
