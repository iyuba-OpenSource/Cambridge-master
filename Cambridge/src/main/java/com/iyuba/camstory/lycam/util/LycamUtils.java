package com.iyuba.camstory.lycam.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;


/**
 * Created by lycamandroid on 16/6/13.
 * 项目工具类
 */
public class LycamUtils {

    /**
     * 判断视频是否为直播状态
     * @param status
     * @return
     */
    public static boolean isLive(String status){
        if(status == null){
            return false;
        }else if(status.equals("live")){
            return true;
        }else if(status.equals("over")){
            return false;
        }else{
            return false;
        }
    }

    public static boolean isReady(String status){
        if(status == null){
            return false;
        }else return status.equals("ready");
    }

    /**
     * 是否为老师
     * @param role
     * @return
     */
    public static boolean isTeacher(String role){
        return "teacher".equals(role);
    }

    /**
     * 判断环信是否断开连接，断了则重连
     */
    public static void judgeEM(){
//        if(!EMClient.getInstance().isConnected()){
//            App.getInstance().reLoginEM();
//        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前ip地址
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
    }

    /**
     * 图片地址是否为空或默认
     * @param url
     * @return
     */
    public static boolean isThumb(String url){
        return TextUtils.isEmpty(url) || "http://resource-aws.lycam.tv/images/thumb.jpg".equals(url);
    }
}
