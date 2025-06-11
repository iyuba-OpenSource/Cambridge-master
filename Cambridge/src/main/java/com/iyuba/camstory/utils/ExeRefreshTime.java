/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.camstory.utils;


import com.iyuba.camstory.R;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.lycam.manager.RuntimeManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 处理下拉刷新时间显示
 * 
 * @author 陈彤
 */
public class ExeRefreshTime {
	public static String lastRefreshTime(String source) {
		String lastTime = ConfigManager.Instance().loadString(source);
		if (lastTime == null || lastTime.equals("")) {
			lastTime = RuntimeManager.getContext().getString(
					R.string.never_update);
		}
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		ConfigManager.Instance().putString(source,
				formatter.format(currentTime));
		return lastTime;
	}
}
