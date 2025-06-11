package com.iyuba.camstory.lycam.util;

import android.content.Context;
import android.widget.Toast;

import com.iyuba.camstory.lycam.manager.RuntimeManager;


/**
 * 作者：renzhy on 16/7/7 15:26
 * 邮箱：renzhongyigoo@gmail.com
 */
public class T {
	public static void s(Context context, String msg){
		Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
	}
	public static void s(Context context, int resId){
		Toast.makeText(context, RuntimeManager.getString(resId),Toast.LENGTH_SHORT).show();
	}

}