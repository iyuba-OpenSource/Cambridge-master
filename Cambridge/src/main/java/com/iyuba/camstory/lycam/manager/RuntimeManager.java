package com.iyuba.camstory.lycam.manager;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 作者：renzhy on 16/6/15 13:55
 * 邮箱：renzhongyigoo@gmail.com
 */
public class RuntimeManager {
	private static Application application;
	private static DisplayMetrics displayMetrics;
	private static Context context;

	public static void setApplicationContext(Context mContext) {
		context = mContext;
		setDisplayMetrics();
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
		return context.getResources().getString(resourcesID);
	}

	public static void setDisplayMetrics() {
		displayMetrics = context.getResources().getDisplayMetrics();
	}

	public static DisplayMetrics getDisplayMetrics() {
		return displayMetrics;
	}

	public static int getWindowWidth() {
		return displayMetrics.widthPixels;
	}

	public static int getWindowHeight() {
		return displayMetrics.heightPixels;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		float scale = displayMetrics.density;
		return (int) (dpValue * scale + 0.5F);
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(float pxValue) {
		float scale = displayMetrics.density;
		return (int) (pxValue / scale + 0.5F);
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp(float pxValue) {
		float fontScale = displayMetrics.scaledDensity;
		return (int) (pxValue / fontScale + 0.5F);
	}
	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
	 */
	public static int sp2px(float spValue) {
		float fontScale = displayMetrics.scaledDensity;
		return (int) (spValue * fontScale + 0.5F);
	}
}
