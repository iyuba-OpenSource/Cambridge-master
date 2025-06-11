package com.iyuba.camstory.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


import com.iyuba.voa.frame.components.ConfigManagerVOA;

import java.util.ArrayList;

public class NightModeManager {
	private WindowManager mWindowManager;
	private ArrayList<View> myView;
	private static boolean isNight;
	private boolean isAdd;
	
	private Context mContext;
	
	public NightModeManager(WindowManager mWindowManager, Context mContext) {
		this.mContext = mContext;
		this.mWindowManager = mWindowManager;
		myView = new ArrayList<View>();
	}
	
	public void checkMode() {
		if(isNight && !isAdd) {
			night();
		} else if(!isNight){
			day();
		}
	}
	
	public void night() {
		ConfigManagerVOA.Instance(mContext).putBoolean("night_mode", true);
		isNight = true;
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
				LayoutParams.TYPE_APPLICATION,
				LayoutParams.FLAG_NOT_TOUCHABLE
						| LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.BOTTOM;
		params.y = 10;
		View temp = new View(mContext);
		temp.setBackgroundColor(0x80000000);
		try {
			mWindowManager.addView(temp, params);
			myView.add(temp);
			isAdd = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void day() {
		ConfigManagerVOA.Instance(mContext).putBoolean("night_mode", false);
		isNight = false;
		remove();
	}

	public void remove() {
		int index = myView.size();
		if (myView != null && index != 0) {
			mWindowManager.removeView(myView.get(index - 1));
			myView.remove(index - 1);
			isAdd = false;
		}
	}
}
