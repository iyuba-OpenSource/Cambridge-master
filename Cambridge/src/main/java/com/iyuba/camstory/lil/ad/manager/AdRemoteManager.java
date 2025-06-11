package com.iyuba.camstory.lil.ad.manager;

import com.iyuba.camstory.lil.ad.manager.bean.Ad_result;
import com.iyuba.camstory.lil.ad.manager.bean.Ad_stream_result;
import com.iyuba.camstory.lil.ad.manager.util.RemoteManager;
import com.iyuba.configation.Constant;

import java.util.List;

import io.reactivex.Observable;

public class AdRemoteManager {

    //接口-获取广告信息(开屏、插屏、banner)
    //开屏：0，banner：4
    public static Observable<List<Ad_result>> getAd(int uid, int adFlag, int appId){
        String url = "http://dev."+ Constant.IYBHttpHead +"/getAdEntryAll.jsp";

        AdService commonService = RemoteManager.getInstance().createJson(AdService.class);
        return commonService.getAd(url,appId,uid,adFlag);
    }

    //接口-获取信息流广告数据
    public static Observable<List<Ad_stream_result>> getTemplateAd(int userId, int flag, int appId){
        String url = "http://dev."+Constant.IYBHttpHead+"/getAdEntryAll.jsp";

        AdService adService = RemoteManager.getInstance().createJson(AdService.class);
        return adService.getStreamAd(url,appId,userId,flag);
    }

    //接口-点击广告获取奖励
//    public static Observable<Ad_click_result> getAdClickReward(int uid, int platform, int adSpace){
//        int appId = App.APP_ID;
//        long timestamp = System.currentTimeMillis()/1000L;
//        String sign = SignUtil.getAdClickSign(uid,appId,timestamp);
//
//        AdService commonService = RemoteManager.getInstance().createJson(AdService.class);
//        return commonService.getAdClick(uid,appId,platform,adSpace,timestamp,sign);
//    }

    //接口-激励广告获取vip
//    public static Observable<Ad_reward_vip> getAdRewardVip(int uid){
//        int appId = App.APP_ID;
//        long timestamp = System.currentTimeMillis()/1000L;
//        String sign = SignUtil.getRewardAdVipSign(timestamp,uid,appId);
//
//        AdService commonService = RemoteManager.getInstance().createJson(AdService.class);
//        return commonService.getAdRewardVip(uid,appId,timestamp,sign);
//    }

    //接口-提交广告数据
//    public static Observable<Ad_clock_submit> submitAdData(int userId, String device, String deviceId, String packageName, String ads){
//        int appId = App.APP_ID;
//        long timestamp = System.currentTimeMillis()/1000L;
//        int os = 2;
//
//        AdService commonService = RemoteManager.getInstance().createJson(AdService.class);
//        return commonService.submitAdData(String.valueOf(timestamp),appId,device,deviceId,userId,packageName,os,ads);
//    }
}
