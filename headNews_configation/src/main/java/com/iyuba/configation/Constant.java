package com.iyuba.configation;

//网络请求地址的集合
import android.os.Environment;

import com.iyuba.common.WebConstant;

public class Constant {
    public static String envir = ConfigManagerHead.Instance().loadString("envir",
            Environment.getExternalStorageDirectory() + "/cambridge");// 文件夹路径
    public final static String MOB_CLASS_PACK_IMAGE = "http://static3.iyuba.com/resource/packIcon/";

    public final static String MOB_CLASS_PACK_TYPE_IMAGE = "http://static3.iyuba.com/resource/nmicon/";

    public final static String MOB_CLASS_PACK_BGPIC = "http://static3.iyuba.com/resource/categoryIcon/";

    public final static String VIP_STATUS = "-1" ;

    public final static int IO_BUFFER_SIZE = 100 * 1024;
    // 应用名称
    public static String APPName = "剑桥英语馆";
    // 爱语吧承认的英文缩写
    public static String AppName = "剑桥英语馆";
    // iyuba app id
//    public static String APPID = "227";
    public static String APPID = "222";

    public static String TOPCIC = "camstory";


    // normal read speed limit: words per minute
    public static int normalWPM = 600;
    public final static String VIDEO_VIP_ADD = "http://staticvip."+ WebConstant.IYBHttpHead +"/video";

    public final static int VERSION_CODE = 12;
    public final static String VERSION_NAME = "2.0";

    public final static String ANDROID = "android";
    public final static String JSON = "json";

    // 日志音频地址 ，VIP
    public final static String AUDIO_VIP_ADD = "http://staticvip."+WebConstant.IYBHttpHead+"/sounds";

    // 日志音频地址 ，非VIP
    public final static String AUDIO_ADD = WebConstant.HTTP_STATIC_ALL+"/sounds";

    public static String appfile = "Cambridge";// 更新时的前缀名
    public static String append = ".mp3";// 文件append
    // audio files location address
    public static String videoAddr = envir + "/audio/";
    // imageloader default picture cache location address
//    public static String picAddr = RuntimeManager.getContext().getExternalCacheDir()
//            .getAbsolutePath();
    //    public static String picAddr = envir + "/cache";
    public static String recordAddr = envir + "/sound.amr";// 跟读音频
    // 语音评论
    public static String voiceCommentAddr = envir + "/voicecomment.amr";
    // 截图位置
    public static String screenShotAddr = envir + "/screenshot.jpg";
    // vip price
    public static final int price = 0;
    // youdao Ad position
    public static final int youdaoAdpos = 3;
    // youdao Ad ID
    public static final String youdaoAdId = "badabcce3f8d6498161b2b7943224dca";  //"a7f0cba947ab018429e76b2377944e85";

    // before SMSSDK 2.0
//	public final static String SMSAPPKEY = "68a3346ac7e8";
//	public final static String SMSAPPSECRET = "f7479ae6d33e28942c960de584efd046";
    // after SMSSDK 2.0
//    public final static String SMSAPPKEY = "dbbb79f88fa6";
//    public final static String SMSAPPSECRET = "253a211621337d4de6a5d95a52895f4b";
//    public final static String SHARESDKAPPID = "7036c58a1527";
//    public final static String SHARESDKAPPSECRET = "98c8a85235f47bec2762ce0a2e91ccae";

    public static int mode;// 播放模式
    public static int type;// 听歌播放模式
    public static int download;// 是否下载

    public static int recordId;// 学习记录篇目id，用于主程序
    public static String recordStart;// 学习记录开始时间，用于主程序

    public final static String IYBHttpHead="iyuba.cn";//爱语吧统一请求地址头部，更新于2019.1.10 原为 iyuba.com  '+Constant.IYBHttpHead+'
    public final static String IYBHttpHead2="iyuba.com.cn";  //"+com.iyuba.configation.Constant.IYBHttpHead+"

    public static final String BASE_URL_STATIC = "http://static2.iyuba.cn";
    // splash picture
//	public static String startUpPicUrl = "http://app."+IYBHttpHead+"/dev/getStartPicApi.jsp?appId=" + APPID;
    public static String startUpPicUrl = "http://app."+IYBHttpHead+"/dev/getAdEntryAll.jsp?flag=1&appId=" + APPID+"&uid=";
    // app update
    public static String appUpdateUrl = "http://api."+IYBHttpHead+"/mobile/android/headline/islatest.plain?";
    // article
    public static String titleUrl = "http://cms."+IYBHttpHead+"/cmsApi/getMyNewsList.jsp?";
    // articles from ids
    public static String titleIdsUrl = "http://cms."+IYBHttpHead+"/cmsApi/getNews.jsp?";
    // article Detail
    public static String detailUrl = "http://cms."+IYBHttpHead+"/cmsApi/getText.jsp?";
    // feedback
    public static String feedBackUrl = "http://api."+IYBHttpHead+"/mobile/android/headline/feedback.plain?";
    // article image base url
    public static String imageUrl = "http://static."+IYBHttpHead+"/cms/news/image/";
    // user recent read information url
    public static String recentInfoUrl = "http://cms."+IYBHttpHead+"/newsApi/getRecentRV.jsp?";
    // proportion info url
    public static String proportionUrl = "http://cms."+IYBHttpHead+"/newsApi/getCatRate.jsp?";
    // user's basic read info url
    public static String basicReadInfoUrl = "http://cms."+IYBHttpHead+"/newsApi/getUserInfo.jsp?";
    // agree comment url
    public static String agreeCommentUrl = "http://daxue."+IYBHttpHead+"/appApi/UnicomApi?protocol=61001";
    // disagree comment url
    public static String disagreeCommentUrl = "http://daxue."+IYBHttpHead+"/appApi/UnicomApi?protocol=61002";
    public static String logoUrl = "http://app."+IYBHttpHead+"/ios/images/bignews/bignews.png";
    public static String lrcUrl = "http://apps."+IYBHttpHead+"/afterclass/getLyrics.jsp?SongId=";// 原文地址，听歌专用
    //	public static String searchUrl = "http://apps."+IYBHttpHead+"/afterclass/searchApi.jsp?key=";// 查询
    public static String searchUrl = "http://cms."+IYBHttpHead+"/search/searchNewsApi.jsp?tag=";
    public static String vipurl = "http://staticvip."+IYBHttpHead+"/sounds/song/";// vip地址
    public static String songurl = "http://static."+IYBHttpHead+"/sounds/song/";// 普通地址
    public static String soundurl = "http://static2."+IYBHttpHead+"/go/musichigh/";// 1000之前解析地址
    public static String userimage = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid=";// 用户头像获取地址
    public static String mWeiXinKey = "wxfd2bf5322fe07130";
    public static String linshiaccouturl ="http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=11003&" +
            "deviceId=wx123232233&platform=android&appid=240&format=json&sign=59e1e581fc00095e0b6b9d637ddae23a";
    public static void reLoadData() {
        envir = ConfigManagerHead.Instance().loadString("envir");// 文件夹路径
        videoAddr = envir + "/audio/";// 音频文件存储位置
        recordAddr = envir + "/sound.amr";// 跟读音频
        voiceCommentAddr = envir + "/voicecomment.amr";// 语音评论
        screenShotAddr = envir + "/screenshot.jpg";// 截图位置
    }

    public final static String PIC_ABLUM__ADD = "http://static1."+IYBHttpHead+"/data/attachment/album/";


    // 程序存放在sd卡上地址
    public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory() +
            "/iyuba/cambridge-cambridge/";
    public static final String SDCARD_AUDIO_PATH = "audio";
    public final static String SDCARD_ATTACH_DIR = "readingaudio/";//

    public final static String SMSAPPID = "15812f538a4c4";
    public final static String SMSAPPSECRET = "52496a74798c15b02f042c475802f03c";

    public static final String WECHAT_APP_KEY = "wxe166bb1893501b76";
    public static final String WECHAT_APP_SECRET = "4229529f86159304a37dd1d6b707a0e0";
    public static final String SINA_APP_KEY = "4007182761";
    public static final String SINA_APP_SECRET = "b0a88ea9d903e0979b61af9b374ebf2e";
    public static final String QQ_APP_KEY = "1102057305";
    public static final String QQ_APP_SECRET = "qlHjMAfDJuEK99ic";


    /******************广告显示********************/
    //有道广告
    public static final String YOUDAO_SPLASH_CODE = "9755487e03c2ff683be4e2d3218a2f2b";
    public static final String YOUDAO_STREAM_CODE = "";
    public static final String YOUDAO_BANNER_CODE = "230d59b7c0a808d01b7041c2d127da95";

    //爱语吧广告
    //广告位 key 已配置如下
    //com.iyuba.camstory 开屏 0083
    //com.iyuba.camstory banner 0084
    //接口数据可以直接在浏览器上查看 http://ai.iyuba.cn/mediatom/server/adplace?placeid=0083
    public static final String SDK_SPLASH_CODE = "0083";
    public static final String SDK_BANNER_CODE = "0084";
}

