package com.iyuba.basichdsfavorlibrary.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：renzhy on 17/2/19 14:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class FavorTimestampConverter {

    public static String convertTimestamp(){
        long timeStamp = new Date().getTime()/1000+3600*8; //东八区;
        long days=timeStamp/86400;
        return Long.toString(days);
    }

    /**
     * 返回美国时间格式 26 Apr 2006
     *
     * @param str
     * @return
     */
    public static String getUSDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        String j = strtodate.toString();
        String[] k = j.split(" ");
        return k[2] + " " + k[1]+ " " + k[5].substring(0, 4);
    }

    public static String getFormatTime(){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        return sdf.format(new Date());
    }
}
