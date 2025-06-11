package com.iyuba.camstory.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	private static SimpleDateFormat defaultSDF = new SimpleDateFormat("yyyyMMdd"); 
	
	public static String getNowInString(){
		return defaultSDF.format(new Date());
	}
	
	public static boolean isSameDay(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date day1 = new Date(time1);
		Date day2 = new Date(time2);
		int temp = Integer.parseInt(sdf.format(day1))
				- Integer.parseInt(sdf.format(day2));
        return temp == 0;
	}

	public static boolean isSameYear(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy");
		Date day1 = new Date(time1);
		Date day2 = new Date(time2);
		int temp = Integer.parseInt(sdf.format(day1))
				- Integer.parseInt(sdf.format(day2));
        return temp == 0;
	}
}
