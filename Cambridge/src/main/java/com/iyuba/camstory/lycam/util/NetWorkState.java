package com.iyuba.camstory.lycam.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.iyuba.camstory.lycam.manager.RuntimeManager;

/**
 * Created by 10202 on 2015/10/8.
 */
public class NetWorkState {
    public static final int ONLY_WIFI = 0;
    public static final int EXCEPT_2G_3G = 1;
    public static final int EXCEPT_2G = 2;
    public static final int ALL_NET = 3;
    private static NetWorkState instance;
    private String netWorkState;

    public NetWorkState() {
    }

    public static NetWorkState getInstance() {
        if (instance == null) {
            instance = new NetWorkState();
        }
        return instance;
    }
    public static boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) RuntimeManager
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static int getAPNType() {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) RuntimeManager
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (nType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nType == TelephonyManager.NETWORK_TYPE_CDMA
                    || nType == TelephonyManager.NETWORK_TYPE_EDGE) {// 2G
                netType = 1;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 2;
        }
        return netType;
    }
    public boolean isConnectByCondition(int stateIndex) {
        boolean result = true;
        switch (stateIndex) {
            case ONLY_WIFI:
                result = TextUtils.equals(netWorkState, "WIFI");
                break;
            case EXCEPT_2G_3G:
                result = TextUtils.equals(netWorkState, "WIFI") || TextUtils.equals(netWorkState, "4G");
                break;
            case EXCEPT_2G:
                result = !(TextUtils.equals(netWorkState, "NO-NET") || TextUtils.equals(netWorkState, "2G"));
                break;
            case ALL_NET:
                result = !TextUtils.equals(netWorkState, "NO-NET");
                break;
        }
        return result;
    }

    public String getNetWorkState() {
        return netWorkState;
    }

    public void setNetWorkState(String netWorkState) {
        this.netWorkState = netWorkState;
    }
}
