package com.iyuba.camstory.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
	private static SimpleDateFormat formatOne = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static SimpleDateFormat formatTwo = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String transformWithSub(String oldDate) {
		String newDate = null;
		try {
			Date date = formatOne.parse(oldDate);
			newDate = formatTwo.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return newDate;
	}

	public static String transformWithoutSub(String oldDate) {
		String newDate = null;
		try {
			Date date = formatTwo.parse(oldDate);
			newDate = formatOne.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return newDate;
	}

	public static String transformDate(String oldDate, SimpleDateFormat fromsdf,
			SimpleDateFormat tosdf) {
		String newDate = null;
		try {
			newDate = tosdf.format(fromsdf.parse(oldDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
	}

}
