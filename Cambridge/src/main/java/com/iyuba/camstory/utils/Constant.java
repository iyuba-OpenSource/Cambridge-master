package com.iyuba.camstory.utils;

import android.os.Environment;

import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.lycam.manager.RuntimeManager;
import com.iyuba.common.WebConstant;

import java.io.File;

public class Constant {
    public final static Boolean DEBUG = true;
    // TODO: cet4 或者 cet6 注释一个
    //要修改的---------------------------------------------------------------------------------要修改的
    // !!!! CET4  !!!!!

    public final static String VIP_STATUS = "2";
    public static final String APPName = "cet4";// 应用名称
    public static String mLesson = "camstory";// 应用名称
    public static final String AppName = "剑桥英语馆";
    public static final String APPID = "207";// 爱语吧id
    public static String mListen = "cet4";
    //四级听力的短信验证
    public final static String SMSAPPID = "f4a1b4b4e95c";
    public final static String SMSAPPSECRET = "61689583013e110d210d721946a04049";
    //嗒萌appkey
    //public final static String ADDAM_APPKEY = "c7e98006aa2fbbfdb9dcfc11d41c6388";

    public final static String PACKAGE_NAME = "com.iyuba.cet4";

    public static String soundBaseUrl = "http://cetsounds."+Constant.IYBHttpHead+"/4/";
    public static String soundBaseUrl_vip = "http://cetsoundsvip."+Constant.IYBHttpHead+"/4/";

    public static int lastVersion = 15;
    public static int currentVersion = 16;

    public static String appDownLoadUrl ="http://android.myapp.com/myapp/detail.htm?apkName=com.iyuba.cet4";


    public static String IYBHttpHead= WebConstant.IYBHttpHead;//爱语吧统一请求地址头部，更新于2019.1.10 原为 "+Constant.IYBHttpHead+"  '+Constant.IYBHttpHead+'
    public static String IYBHttpHead2=WebConstant.IYBHttpHead2;

    //要修改的---------------------------------------------------------------------------------要修改的

    // !!!!!  CET6  !!!!!

//    public final static String VIP_STATUS = "4";
//    public static final String  APPName = "cet6";// 应用名称
//    public static String mLesson = "cet6";// 应用名称
//    public static final String AppName = "英语六级听力";
//    public static final String APPID = "208";// 爱语吧id
//    //六级听力的短信验证
//    public final static String SMSAPPID = "f50f78e90c82";
//    public final static String SMSAPPSECRET = "ca9c29d4564dd0f0a88684becff001cc";
//
//    //嗒萌appkey
//    public final static String ADDAM_APPKEY = "52f07af5dd1d9599c653d9005f5eee82";
//
//    public final static String PACKAGE_NAME = "com.iyuba.cet6";
//    public static String mListen = "cet6";
//    public static String soundBaseUrl = "http://cetsounds."+Constant.IYBHttpHead+"/6/";
//    public static String soundBaseUrl_vip = "http://cetsoundsvip."+Constant.IYBHttpHead+"/6/";
//
//    public static int lastVersion = 20;
//    public static int currentVersion = 21;
//    public static String appDownLoadUrl ="http://android.myapp.com/myapp/detail.htm?apkName=com.iyuba.cet6";

    //要修改的---------------------------------------------------------------------------------要修改的

    public static int TOTALWORDS = 4500;

    public static String basicReadInfoUrl = "http://cms."+Constant.IYBHttpHead+"/newsApi/getUserInfo.jsp?";
    public static String recentInfoUrl = "http://cms."+Constant.IYBHttpHead+"/newsApi/getRecentRV.jsp?";
    public static String imageUrl = "http://static."+Constant.IYBHttpHead+"/cms/news/image/";
    public static String titleIdsUrl = "http://cms."+Constant.IYBHttpHead+"/cmsApi/getNews.jsp?";
    public static String proportionUrl = "http://cms."+Constant.IYBHttpHead+"/newsApi/getCatRate.jsp?";
    public static String logoUrl = "http://app."+Constant.IYBHttpHead+"/ios/images/bignews/bignews.png";
    public static int normalWPM = 600;
    public final static String APPID_DOWNLOAD = "27";// 托福听力在爱语吧官网下载的id


    public static int mode;// 播放模式
    public static int type;// 听歌播放模式
    public static int download;// 是否下载
    public static int recordId;// 学习记录篇目id，用于主程序
    public static String recordStart;// 学习记录开始时间，用于主程序
    public static String appUpdateUrl = "http://api."+Constant.IYBHttpHead+"/mobile/android/iyumusic/islatest.plain?currver=";// 升级地址
    public static String detailUrl = "http://cms."+Constant.IYBHttpHead+"/cmsApi/getText.jsp?";// 原文地址
    public static String lrcUrl = "http://apps."+Constant.IYBHttpHead+"/afterclass/getLyrics.jsp?SongId=";// 原文地址，听歌专用
    public static String searchUrl = "http://apps."+Constant.IYBHttpHead+"/afterclass/searchApi.jsp?key=";// 查询
    public static String titleUrl = "http://apps."+Constant.IYBHttpHead+"/afterclass/getSongList.jsp?maxId=";// 新闻列表，主程序用
    public static String vipurl = "http://staticvip."+Constant.IYBHttpHead+"/sounds/song/";// vip地址
    public static String songurl = "http://static."+Constant.IYBHttpHead+"/sounds/song/";// 普通地址
    public static String soundurl = "http://static2."+Constant.IYBHttpHead+"/go/musichigh/";// 1000之前解析地址
    public static String userimage = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid=";// 用户头像获取地址

    // 移动课堂所需的相关API
    public final static String MOB_CLASS_DOWNLOAD_PATH = "http://static3."+Constant.IYBHttpHead+"/resource/";
    public final static String MOB_CLASS_PAYEDRECORD_PATH = "http://app."+Constant.IYBHttpHead+"/pay/apiGetPayRecord.jsp?";
    public final static String MOB_CLASS_PACK_IMAGE = "http://static3."+Constant.IYBHttpHead+"/resource/packIcon/";
    public final static String MOB_CLASS_PACK_TYPE_IMAGE = "http://static3."+Constant.IYBHttpHead+"/resource/nmicon/";
    public final static String MOB_CLASS_COURSE_IMAGE = "http://static3."+Constant.IYBHttpHead+"/resource/";
    public final static String MicroClassReqPackId = "-2";
    public final static String reqPackDesc = "class.all";
    public final static String PIC_BASE_URL = "http://dev."+Constant.IYBHttpHead+"/";
    public final static int IO_BUFFER_SIZE = 100 * 1024;
    public final static String MOB_CLASS_COURSE_RESOURCE_DIR = "http://static3."+Constant.IYBHttpHead+"/resource/package";
    public final static String MOB_CLASS_COURSE_RESOURCE_APPEND = ".zip";
    public final static String MOB_CLASS_PACK_BGPIC = "http://static3."+Constant.IYBHttpHead+"/resource/categoryIcon/";

    // mp3文件存放sd卡上文件夹

    // assets目录下图片存放文件夹
    public static final String ASSETS_IMAGE_PATH = "images";
    /**
     * assets目录下图片存放文件夹  存放的是雅思宝典课本后面的答案图片
     */
    public static final String ASSETS_ANSWER_IMAGE_PATH = "answerimage";
    // images转存到sd卡上文件夹
    public static final String SDCARD_IMAGE_PATH = "image";
    // mp3文件存放sd卡上文件夹
    public static final String SDCARD_AUDIO_PATH = "audio";
    public static final String ASSETS_AUDIO_PATH = "audio";
    public static final String SDCARD_APK_PATH = "apk";
    public static final String PLATFORM = "android";
    public static final String EXERCISE_MODE = "exerciseMode";

    // 语音下载服务器地址 http://static."+Constant.IYBHttpHead+"/sounds/toefl/TPO3_C1.m4a

    // 从2015年1月27日17:32:37 下载地址全部改成高速下载
    // public static final String SERVER_PATH =
    // "http://staticvip."+Constant.IYBHttpHead+"/sounds/toefl/";
    public static final String SERVER_PATH = "http://static."+Constant.IYBHttpHead+"/sounds/toefl/";
    public static final String SERVER_VIP_PATH = "http://staticvip."+Constant.IYBHttpHead+"/sounds/toefl/";

    // 语音的格式
    public static final String AUDIO_FORMATE = ".m4a";
    public static final String AUDIO_FORMATE_2 = ".mp3";
    // 播放音频时快进，前进的时间
    public static final int SEEK_NEXT = 5000;
    // 播放音频时快退，后退的时间
    public static final int SEEK_PRE = -5000;
    // 保存收藏单词发音的地址
    public static final String SDCARD_FAVWORD_AUDIO_PATH = "word";

    // 背景音乐测试
    public static String bgMusicUrl = "http://static."+Constant.IYBHttpHead+"/sounds/song/";
    public static String addIntegralUrl = "http://api."+Constant.IYBHttpHead+"/credits/updateScore.jsp?srid=38&uid=";
    public static String reduceCoinUrl = "http://api."+Constant.IYBHttpHead+"/credits/updateScore.jsp?srid=39&uid=";
    // 有道
    public final static String youdao = "youdao";
    // 是否发不到有道上
    public final static boolean isYouDao = true;
    public final static String youdaoAdId = "b932187c3ec9f01c9ef45ad523510edd";

    public final static String youdaoAdId2 = "5542d99e63893312d28d7e49e2b43559";
    // 日志音频地址 ，非VIP
    public final static String AUDIO_ADD = "http://static."+Constant.IYBHttpHead+"/sounds";
    // 日志音频地址 ，VIP
    public final static String AUDIO_VIP_ADD = "http://staticvip."+Constant.IYBHttpHead+"/sounds";

    // 日志视频地址 ，VIP
    public final static String VIDEO_VIP_ADD = "http://staticvip."+Constant.IYBHttpHead+"/video";
    // 日志视频地址 ，非VIP
    public final static String VIDEO_ADD = "http://staticvip."+Constant.IYBHttpHead+"/video";
    public final static String IMAGE_DOWN_PATH = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=big&uid=";
    public final static String PIC_ABLUM__ADD = "http://static1."+com.iyuba.configation.Constant.IYBHttpHead+"/data/attachment/album/";
    public final static String youdaoId = "9fd89acf83d739b1d058fc4ed42be530";
    //////////////////////

    public static final String authority = "com.iyuba.core.downloadprovider." + APPName + ".cetdownlaod";
    public static final String PERMISSION_ACCESS = "com.iyuba.core." + APPName + ".permission.ACCESS_DOWNLOAD_MANAGER";
    public static final String PERMISSION_ACCESS_ADVANCED = "com.iyuba.core." + APPName + ".permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED";
    public static final String PERMISSION_SEND_INTENTS = "com.iyuba.core." + APPName + ".permission.SEND_DOWNLOAD_COMPLETED_INTENTS";

//    public static String envir = ConfigManager.Instance().loadString("envir", Environment.getExternalStorageDirectory()
//            + "/iyuba/" + APPName + "/");// 文件夹路径
public static String envir = Environment.getExternalStorageDirectory()
        + "/iyuba/" + APPName + "/";// 文件夹路径
    // 程序存放在sd卡上地址
    public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory() + "/iyuba/" + APPName + "/";
    public static final String feedBackUrl = "http://api."+com.iyuba.configation.Constant.IYBHttpHead+"/mobile/android/" + APPName + "/feedback.plain?uid=";// 反馈

    // mp3文件存放sd卡上文件夹
    public static final String SDCARD_ATTACH_DIR = "abilityTest/";

    public static String picSrcAddr = envir + "/pic/";// 音频文件存储位置
    public static String appfile = "music";// 更新时的前缀名
    public static String append = ".mp3";// 文件append
    public static String videoAddr = envir + "/audio/";// 音频文件存储位置
    public static File picAddr = RuntimeManager.getContext()
            .getExternalCacheDir();// imagedownloader默认缓存图片位置

    public static String userAddr = envir + "/user.jpg";// 用户头像，已废弃
    public static String recordAddr = envir + "/sound.amr";// 跟读音频
    public static String voiceCommentAddr = envir + "/voicecomment.amr";// 语音评论
    public static String screenShotAddr = envir + "/screenshot.jpg";// 截图位置
    public static String voiceSingAddr = envir + "/voice/";
    //	public static int price = 600;// 应用内终身价格
    public static double price = 0.01;// 应用内终身价格

    public static void reLoadData() {
        envir = ConfigManager.Instance().loadString("envir");// 文件夹路径
        videoAddr = envir + "/audio/";// 音频文件存储位置
        recordAddr = envir + "/sound.amr";// 跟读音频
        voiceCommentAddr = envir + "/voicecomment.amr";// 语音评论
        screenShotAddr = envir + "/screenshot.jpg";// 截图位置
    }


    /**
     * 上传能力测评使用的url
     */
    public static final String url_updateExamRecord = "http://daxue."+com.iyuba.configation.Constant.IYBHttpHead+"/ecollege/updateExamRecord.jsp";

    /**
     * 雅思宝典 图片上传前缀
     */
    public static final String IELTSWRITE_AUDIO_URL_PRE = "http://www."+com.iyuba.configation.Constant.IYBHttpHead+"/question/answerQuestion.jsp?&format=json";

    /**
     * 雅思宝典短信验证 Mob 上 appkey
     */
    public static final String SMSAPPKEY = "14eed5e0f4b5f";
    /**
     * 雅思宝典短信验证 Mob上 appsecret
     */
    //public static final String SMSAPPSECRET = "a4a5f2fe504db5e84c99454c05c018d4";


    /**
     * 雅思宝典 听力url前缀
     */
    public static final String IELTSLISTEN_AUDIO_URL_PRE = "http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/sounds/ielts/listening/";

    //public static String lesson = "IELTS";


    /**
     * 雅思能力测试 听力url前缀  http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/IELTS/sounds/16819.mp3
     */
    public static final String ABILITY_AUDIO_URL_PRE = "http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/" + mListen + "/sounds/";
    /**
     * 雅思能力测试 附件attach前缀 http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/IELTS/attach/9081.txt
     */
    public static final String ABILITY_ATTACH_URL_PRE = "http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/" + mListen + "/attach/";
    /**
     * 雅思能力测试 图片attach前缀  http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/IELTS/images/
     */
    public static final String ABILITY_IMAGE_URL_PRE = "http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/" + mListen + "/images/";
    /**
     * 说+ 口语前缀
     */
    public static final String IELTSSPEAK_AUDIO_URL_PRE = "http://static2."+com.iyuba.configation.Constant.IYBHttpHead+"/sounds/ielts/speaking/";

    private static String simRecordAddr = envir + "/sound";

    public static String getsimRecordAddr() {
        return simRecordAddr;
    }

    private static String recordTag = ".amr";// 录音（跟读所用）的位置

    public static String getrecordTag() {
        return recordTag;
    }


    public static String[] ABILITY_TYPE_ARR = {"写作", "单词", "语法", "听力", "口语", "阅读"};//雅思听力测试类型

    /**
     * 写作能力测试代码
     */
    public static final int ABILITY_TETYPE_WRITE = 0;
    public static final String ABILITY_WRITE = "X";
    public static final String[] WRITE_ABILITY_ARR = {"写作表达", "写作结构", "写作逻辑", "写作素材"};
    public static final int TIME_ABILITY_TEST_X = 15 * 60;//写作测试时间

    /**
     * 单词测试代码
     */
    public static final int ABILITY_TETYPE_WORD = 1;
    public static final String ABILITY_WORD = "W";

    //public static final String[] WORD_ABILITY_ARR = {"中英力", "英中力", "发音力", "音义力", "拼写力", "应用力"};//雅思

    public static final String[] WORD_ABILITY_ARR = {"中英力", "英中力", "发音力", "音义力", "拼写力", "应用力", "词根词缀"};//四级听力

    public static final int TIME_ABILITY_TEST_W = 20 * 60;//听力测试时间

    /**
     * 语法能力测试代码
     */
    public static final int ABILITY_TETYPE_GRAMMAR = 2;
    public static final String ABILITY_GRAMMAR = "G";
    public static final String[] GRAM_ABILITY_ARR = {"实词", "虚词", "引语", "被动语态", "句子", "时态"};
    public static final int TIME_ABILITY_TEST_G = 15 * 60;//语法测试时间
    /**
     * 听力能力测试代码
     */
    public static final int ABILITY_TETYPE_LISTEN = 3;
    public static final String ABILITY_LISTEN = "L";
    //public static final String[] LIS_ABILITY_ARR = {"单项选择", "多项选择", "判断正误", "简答", "完成句子", "信息填空", "听写句子"};//雅思
    public static final String[] LIS_ABILITY_ARR = {"听力篇章", "短篇新闻", "长对话"};//四级
    public static final int TIME_ABILITY_TEST_L = 30 * 60;//听力测试时间


    /**
     * 口语能力测试代码
     */
    public static final int ABILITY_TETYPE_SPEAK = 4;
    public static final String ABILITY_SPEAK = "S";
    public static final String[] SPEAK_ABILITY_ARR = {"口语发音", "口语表达", "口语素材", "口语逻辑"};
    public static final int TIME_ABILITY_TEST_S = 10 * 60;//口语测试时间
    /**
     * 阅读能力测试代码
     */
    public static final int ABILITY_TETYPE_READ = 5;
    public static final String ABILITY_READ = "R";
    public static final String[] READ_ABILITY_ARR = {"单项选择", "判断正误", "标题对应", "配对题", "简答题", "完成句子"};
    public static final int TIME_ABILITY_TEST_R = 60 * 60;//阅读测试时间

    /**
     * 单选能力测试
     */
    public static final int ABILITY_TESTTYPE_SINGLE = 1;
    /**
     * 填空题
     */
    public static final int ABILITY_TESTTYPE_BLANK = 2;
    /**
     * 选择填空
     */
    public static final int ABILITY_TESTTYPE_BLANK_CHOSE = 3;
    /**
     * 图片选择
     */
    public static final int ABILITY_TESTTYPE_CHOSE_PIC = 4;

    /**
     * 语音评测
     */
    public static final int ABILITY_TESTTYPE_VOICE = 5;
    /**
     * 多选
     */
    public static final int ABILITY_TESTTYPE_MULTY = 6;
    /**
     * 判断题目
     */
    public static final int ABILITY_TESTTYPE_JUDGE = 7;
    /**
     * 单词拼写
     */
    public static final int ABILITY_TESTTYPE_BLANK_WORD = 8;

    public static final String[] WORD_TEST_ARR = {"中英力", "英中力", "音义力"};//英语四级练习题目的类型
    public static final String[] LISTEN_TEST_ARR = {"短篇新闻", "长对话", "听力篇章"};//英语四级练习题目的类型 听力
    public static final String[] READ_TEST_ARR = {"选词填空", "快速阅读", "仔细阅读"};//英语四级听力阅读 练习题目 300道
    public static final String[] WRITE_TEST_ARR = {"写作表达", "写作结构", "写作逻辑", "写作素材"};
    public static final String[] GRAM_TEST_ARR = {"实词", "虚词", "时态", "引语", "句子", "被动语态"};
    public static final String[] SPEAK_TEST_ARR = {"口语发音", "口语表达", "口语逻辑", "口语素材"};




}
