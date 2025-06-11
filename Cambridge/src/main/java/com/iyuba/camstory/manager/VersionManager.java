package com.iyuba.camstory.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.volley.Request;
import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.AppUpdateCallBack;
import com.iyuba.camstory.listener.LoginRequest;
import com.iyuba.camstory.listener.RequestCallBack;

/**
 * 版本管理
 *
 * @author chentong
 */
public class VersionManager {
    private static VersionManager instance;
    public static int VERSION_CODE;
    public static String VERSION_NAME;

    static {
        VERSION_CODE = getVersionCode(CrashApplication.getInstance());
        VERSION_NAME = getVersion(CrashApplication.getInstance());
    }

    private VersionManager() {
    }

    public static synchronized VersionManager getInstance() {
        if (instance == null) {
            synchronized (VersionManager.class) {
                instance = new VersionManager();
            }
        }
        return instance;
    }

    public static String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.version);
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param version
     * @param aucb
     */
    public void checkNewVersion(int version, final AppUpdateCallBack aucb) {
        LoginRequest.AppUpdateRequest request = new LoginRequest.AppUpdateRequest(version, new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                LoginRequest.AppUpdateRequest response = (LoginRequest.AppUpdateRequest) result;
                if (aucb == null)
                    return;
                if (response.isLastestVersion()) {
                    aucb.appUpdateFaild();
                } else if (response.isOldVersion()) {
                    aucb.appUpdateSave(response.version, response.appUrl);
                }
            }
        });
        CrashApplication.getInstance().getQueue().add(request);
    }
}
