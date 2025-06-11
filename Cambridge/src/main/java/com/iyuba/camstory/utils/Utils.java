package com.iyuba.camstory.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.iyuba.camstory.frame.CrashApplication;

import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.util.List;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;

public class Utils {
    static final String LOGTAG = "Sample App";

    private Utils() {}

    static void validateAdUnitId(String adUnitId) throws IllegalArgumentException {
        if (adUnitId == null) {
            throw new IllegalArgumentException("Invalid Ad Unit ID: null ad unit.");
        } else if (adUnitId.length() == 0) {
            throw new IllegalArgumentException("Invalid Ad Unit Id: empty ad unit.");
        } else if (adUnitId.length() > 256) {
            throw new IllegalArgumentException("Invalid Ad Unit Id: length too long.");
        } else if (!isAlphaNumeric(adUnitId)) {
            throw new IllegalArgumentException("Invalid Ad Unit Id: contains non-alphanumeric characters.");
        }
    }

    static void hideSoftKeyboard(final View view) {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    static boolean isAlphaNumeric(final String input) {
        return input.matches("^[a-zA-Z0-9-_]*$");
    }

    static void logToast(Context context, String message) {
        Log.d(LOGTAG, message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if(appProcesses!=null)//有崩溃
        for (RunningAppProcessInfo appProcess: appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    LogUtil.i("后台"+appProcess.processName);
                    return true;
                } else {
                    //LogUtil.i("前台"+appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
    
    public static boolean isNetworkAvailable(final Context context) {
        if (context == null) {
                return false;
        }

        final int internetPermission = context
                        .checkCallingOrSelfPermission(INTERNET);
        if (internetPermission == PackageManager.PERMISSION_DENIED) {
                return false;
        }

        /**
         * This is only checking if we have permission to access the network
         * state It's possible to not have permission to check network state but
         * still be able to access the network itself.
         */
        final int networkAccessPermission = context
                        .checkCallingOrSelfPermission(ACCESS_NETWORK_STATE);
        if (networkAccessPermission == PackageManager.PERMISSION_DENIED) {
                return true;
        }

        // Otherwise, perform the connectivity check.
        try {
                final ConnectivityManager connnectionManager = (ConnectivityManager) context
                                .getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connnectionManager
                                .getActiveNetworkInfo();
                return networkInfo.isConnected();
        } catch (NullPointerException e) {
                return false;
        }
    }

    public static String percent(int x, int y) {
        String percent = "";
        double xx = x * 100.0;
        double yy = y * 100.0;
        double zz = xx / yy;
        DecimalFormat df = new DecimalFormat("##0.0%");
        if (Math.abs(zz) < 0.000000000001) {
            percent = "0.0%";
        } else {
            percent = df.format(zz);
        }
        return percent;
    }

    /****************
     *
     * 发起添加群流程。群号：✘”“(xxxxxxxxx) 的 key 为： q1kbEmyLaJNQvXFuhwBMPDLahHh-K9M0
     * 调用 joinQQGroup(q1kbEmyLaJNQvXFuhwBMPDLahHh-K9M0) 即可发起手Q客户端申请加群
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public static boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            CrashApplication.getInstance().startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
