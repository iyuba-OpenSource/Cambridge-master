package com.iyuba.camstory.lil.ad.manager;

import com.iyuba.camstory.lil.ad.manager.bean.Ad_result;
import com.iyuba.camstory.lil.ad.manager.bean.Ad_stream_result;
import com.iyuba.camstory.lil.ad.manager.data.StrLibrary;
import com.iyuba.camstory.lil.ad.manager.data.UrlLibrary;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AdService {

    //广告接口
    //http://dev.iyuba.cn/getAdEntryAll.jsp?uid=0&appId=259&flag=4
    @GET()
    Observable<List<Ad_result>> getAd(@Url String url,
                                      @Query(StrLibrary.appId) int appid,
                                      @Query(StrLibrary.uid) int uid,
                                      @Query(StrLibrary.flag) int flag);

    //信息流广告接口
    //http://dev.iyuba.cn/getAdEntryAll.jsp?uid=0&appId=259&flag=4
    @GET()
    Observable<List<Ad_stream_result>> getStreamAd(@Url String url,
                                                   @Query(StrLibrary.appId) int appid,
                                                   @Query(StrLibrary.uid) int uid,
                                                   @Query(StrLibrary.flag) int flag);

    //广告点击奖励接口
    //http://api.iyuba.cn/credits/adClickReward.jsp?uid=6307010&appid=291&platform=1&ad_space=1&timeStr=1709101522&sign=12b04ffaf5390f4bd60e042ca92f85b9
//    @Headers({StrLibrary.urlPrefix+":"+UrlLibrary.HTTP_API,StrLibrary.urlHost+":"+NetHostManager.domain_short})
//    @POST("/credits/adClickReward.jsp")
//    Observable<Ad_click_result> getAdClick(@Query(StrLibrary.uid) int userId,
//                                           @Query(StrLibrary.appid) int appId,
//                                           @Query(StrLibrary.platform) int platform,
//                                           @Query(StrLibrary.ad_space) int adSpace,
//                                           @Query(StrLibrary.timeStr) long timestamp,
//                                           @Query(StrLibrary.sign) String sign);

    //激励广告播放完成后的获取会员接口
    //http://vip.iyuba.cn/openIncentiveVip.jsp?uid=12071118&appid=260&timeStr=1709895188&sign=4f06569e0cba8344c3b2391ac6d7983f
//    @Headers({StrLibrary.urlPrefix+":"+UrlLibrary.HTTP_VIP,StrLibrary.urlHost+":"+NetHostManager.domain_short})
//    @GET("/openIncentiveVip.jsp")
//    Observable<Ad_reward_vip> getAdRewardVip(@Query(StrLibrary.uid) int userId,
//                                             @Query(StrLibrary.appid) int appId,
//                                             @Query(StrLibrary.timeStr) long time,
//                                             @Query(StrLibrary.sign) String sign);

    //定时提交广告数据
    //http://iuserspeech.iyuba.cn:9001/japanapi/addAdInfo.jsp?date_time=1710829085&appid=280&device=HONOR&uid=15300794&package=com.iyuba.talkshow.pappa&os=2&ads=%5B%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814594%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814594%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814594%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814594%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814595%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%224%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814595%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%221%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814595%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%224%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814596%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%221%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814596%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814597%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814597%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%221%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814597%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814597%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710814597%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814598%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814598%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710814598%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710814598%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814598%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710814598%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814599%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814599%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814599%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814599%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%221%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710814599%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%223%22%2C%22date_time%22%3A%221710814600%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%224%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710829075%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829075%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710829075%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829075%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710829076%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829076%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%223%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829076%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%224%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%221%22%2C%22date_time%22%3A%221710829076%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829076%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%222%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829076%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%221%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829077%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%220%22%2C%22type%22%3A%220%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829078%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%221%22%2C%22type%22%3A%221%22%7D%2C%7B%22ad_space%22%3A%222%22%2C%22date_time%22%3A%221710829078%22%2C%22install_package%22%3A%22%22%2C%22platform%22%3A%220%22%2C%22type%22%3A%221%22%7D%5D
    //[{"ad_space":"1","date_time":"1710814598","install_package":"","platform":"3","type":"1"},
    //{"ad_space":"2","date_time":"1710814598","install_package":"","platform":"2","type":"1"},
    //{"ad_space":"3","date_time":"1710814599","install_package":"","platform":"2","type":"0"}]
    //明天添加下这个广告数据提交功能吧，有问题及时沟通
    //接口：http://iuserspeech.iyuba.cn:9001/japanapi/addAdInfo.jsp
    //参数：appid，package，device，deviceid，os，uid，date_time，ads参数如图所示
    //参数含义：
    //date_time	广告加载时间, 秒
    //appid		APPID
    //device		设备或者是浏览器请求
    //deviceid	设备ID
    //uid			登陆Uid
    //package		包名
    //os			系统, ios-1或者android-2
    //
    //ads			是一个jsonarray数组, 对象数据为下面三个参数
    //platform	平台, 百度, 流量汇, 穿山甲, 快手, 顺序1-4
    //ad_space	广告位, banner, 信息流, 开屏, 激励, 插屏, draw, 顺序1-6
    //type		0, 请求；1, 展现；2, 点击；
//    @Headers({StrLibrary.urlPrefix+":"+UrlLibrary.HTTP_IUSERSPEECH,StrLibrary.urlHost+":"+NetHostManager.domain_short,StrLibrary.urlSuffix+":"+UrlLibrary.SUFFIX_9001})
//    @FormUrlEncoded
//    @POST("/japanapi/addAdInfo.jsp")
//    Observable<Ad_clock_submit> submitAdData(@Field(StrLibrary.date_time) String date_time,
//                                             @Field(StrLibrary.appid) int appId,
//                                             @Field(StrLibrary.device) String device,
//                                             @Field(StrLibrary.deviceid) String deviceId,
//                                             @Field(StrLibrary.uid) int userId,
//                                             @Field(StrLibrary.package_fix) String packageName,
//                                             @Field(StrLibrary.os) int os,
//                                             @Field(StrLibrary.ads) String ads);
}
