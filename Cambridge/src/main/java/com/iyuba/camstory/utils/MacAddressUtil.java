package com.iyuba.camstory.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.iyuba.camstory.frame.CrashApplication;
public class MacAddressUtil {

	public static String getMacAddress() {
		String macAddress = null;
		if (macAddress == null) {
			WifiManager wifiManager = (WifiManager) CrashApplication.getInstance().getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);
			if (wifiManager != null) {
				WifiInfo wInfo = wifiManager.getConnectionInfo();
				if (wInfo != null) {
					macAddress = wInfo.getMacAddress();
				}
			}
		}
		return macAddress;
	}
}
