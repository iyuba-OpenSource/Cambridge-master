package com.iyuba.voa.activity.setting;

import android.content.Context;
import android.os.Environment;

import com.iyuba.common.WebConstant;
import com.iyuba.voa.frame.components.ConfigManagerVOA;

public class Constant {

	// 从application中初始化常量
	public static void initConstant(Context context) {
		// 根目录
		// envir = context.getExternalFilesDir(null) + "/";
		// 视频目录
		videoAddr = envir + "/audio/" + FILE_DOWNLOAD_TAG;
		// picAddr = context.getExternalCacheDir().toString();
		recordAddr = envir + "/sound";
		voiceCommentAddr = envir + "/voicecomment.amr";
		backgroundColor = ConfigManagerVOA.Instance(context).loadInt("backColor");
		textColor = ConfigManagerVOA.Instance(context).loadInt("textColor");
		textSize = ConfigManagerVOA.Instance(context).loadInt("textSize");
	}

    /**
	 * 积分
	 */
	public final static int coin = 20;
	/*
	 * APP相关常量
	 */
	// 应用名
	private final static String APPName = "剑桥英语馆";
	public final static String TOPICID = "camstory";
	// 爱语吧APPID
	private final static String APPID = "227";
	// 爱语吧APP标记
	private final static String APPTag = "voa";
	// APP文件名字
	private final static String appfile = "voa";
	// 多媒体文件后缀
	private final static String append = ".mp3";
	// 下载中的文件的前缀
	private final static String FILE_DOWNLOAD_TAG = "temp_audio_";

	// 360首发标识
	private final static boolean SHOUFA_360 = false;
	// 应用宝首发标识
	private final static boolean SHOUFA_YINGYONGBAO = false;
	// 华为首发标识
	private final static boolean SHOUFA_HUAWEI = true;
	// ExternalFileDir下的音视频子目录名字
	// private static String videoSubPath = "/audio/";
	// ExternalFileDir下的应用文件子目录名字
	// private static String appSubPath = "/apps/";

	public static String IYBHttpHead= WebConstant.IYBHttpHead;//爱语吧统一请求地址头部，更新于2019.1.10 原为 "+Constant.IYBHttpHead+"  '+Constant.IYBHttpHead+'
	public static String IYBHttpHead2=WebConstant.IYBHttpHead2;


	/*
	 * 各种本地路径
	 */
	// SD卡中音频、视频等文件的根目录-----"/Android/data/package_name/files/"
	public static String envir = "";
	// 音频下载时，临时文件的路径前缀
	private static String videoAddr = envir + "/audio/" + FILE_DOWNLOAD_TAG;
	// 图片所在的文件夹
	private static String picAddr = envir + "/image";
	// 讯飞录音.pcm文件的存放位置
	private static String pcmRecordAddr = envir + "/sound.pcm";
	// 录音（跟读所用）的位置
	private static String recordAddr = envir + "/sound";
	private static String recordTag = ".amr";
	// 录音(语音评论所用）的位置
	private static String voiceCommentAddr = envir + "/voicecomment.amr";
	// 自定义的页面背景色
	private static int backgroundColor = 0xffffffff;
	// 原文的文字颜色
	private static int textColor;
	// 原文的字体大小
	private static int textSize = 14;
	// 截图位置
	private static String screenShotAddr = envir + "/screenshot.jpg";

	/*
	 * url
	 */
	private final static String url = "http://static."+IYBHttpHead+"/video/voa/";
	// 邮箱注册的url
	private final static String email_registUrl = "http://api."+IYBHttpHead2+"/v2/api.iyuba?protocol=11002&app=voa";
	// 手机注册的url
	private final static String phone_registUrl = "http://api."+IYBHttpHead2+"/v2/api.iyuba?platform=android&app=voa&protocol=11002";
	// 获取评论的url
	private final static String commenturl = "http://voa."+IYBHttpHead+"/voa/UnicomApi?protocol=60001&platform=android&voaid=";
	// 发表评论的url
	private final static String expressionurl = "http://voa."+IYBHttpHead+"/voa/UnicomApi?protocol=60002&platform=android&shuoshuotype=0";
	// 音频的url
	private final static String audiourl = "http://static."+IYBHttpHead+"/sounds/voa";
	// 用户头像url
	private final static String userimage = "http://api."+IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid=";
	// VIP的url
	private final static String audiourl_vip = "http://staticvip."+IYBHttpHead+"/sounds/voa";
	// 更新文章阅读次数的url
	private final static String update_read_url = "http://voa."+IYBHttpHead+"/voa/UnicomApi?protocol=70001&format=json";
	// 应用新版检测地址
	private final static String appUpdateUrl = "http://api."+IYBHttpHead+"/mobile/android/camstory/islatest.plain?format=xml&currver=";
	// 文章详细内容的获取地址
	private final static String detailUrl = "http://apps."+IYBHttpHead+"/iyuba/textNewApi.jsp?voaid=";
	// 问题反馈的提交地址
	private final static String feedBackUrl = "http://api."+IYBHttpHead+"/mobile/android/camstory/feedback.plain?uid=";
	// 文章搜索的地址
	private final static String searchUrl = "http://apps."+IYBHttpHead+"/voa/searchApi.jsp?key=";
	// 获取文章列表的地址,获取小于maxid的
	private final static String titleUrl = "http://apps."+IYBHttpHead+"/iyuba/titleApi2.jsp?";
	// 分享应用时是url
	private final static String appShareUrl = "https://a.app.qq.com/o/simple.jsp?pkgname=com.iyuba.camstory";
	// 分享文章或应用，获得积分
	private final static String addCreditsUrl = "http://api."+IYBHttpHead+"/credits/updateScore.jsp?";
	// 推广下载统计
	private final static String promotionDownloadUrl = "http://daxue."+IYBHttpHead+"/appApi/updateInstaller1.jsp?";


	/**
	 * 推广平台
	 */
	// 有道
	public final static String youdao = "youdao";

	// 是否发不到有道上
	public final static boolean isYouDao = true;

	public final static String youdaoAdId = "5542d99e63893312d28d7e49e2b43559";

	public final static int youdaoAdPos = 3;

	/*
	 * 杂
	 */
	// 没用ing
	private static final String wxAPP_ID = "wxe166bb1893501b76";
	// 文章列表已读颜色
	private final static int read_color = 0xff2983c1;
	// 文章列表未读颜色
	private final static int unread_color = 0xff000000;
	// 已选的颜色
	private final static int select_color = 0xffde5e5b;
	// 未选的颜色
	private final static int unselect_color = 0xff444444;
	// 选中的文字的颜色
	private final static int studytext_color = 0xFF95EAFE;
	// 是否有分类可用
	private final static boolean categoryavailable = true;
	// 是否有视频可用
	private final static boolean videoavailable = false;
	// 是否有试题可用
	private final static boolean testavailable = true;
	// 忘了
	private final static int optionItemSelect = 0x7fbdfaf1;
	private final static int optionItemUnSelect = 0xff8ab6da;

	/*
	 * getter && setter
	 */

	public static String getEnvir() {
		return envir;
	}

	public static void setEnvir(String envir) {
		Constant.envir = envir;
	}

	public static String getVideoAddr() {
		return videoAddr;
	}

	public static void setVideoAddr(String videoAddr) {
		Constant.videoAddr = videoAddr;
	}

	public static String getPicAddr() {
		return picAddr;
	}

	public static void setPicAddr(String picAddr) {
		Constant.picAddr = picAddr;
	}

	public static String getRecordAddr() {
		return recordAddr;
	}

	public static void setRecordAddr(String recordAddr) {
		Constant.recordAddr = recordAddr;
	}

	public static String getRecordTag() {
		return recordTag;
	}

	public static String getPcmRecordAddr() {
		return pcmRecordAddr;
	}

	public static void setPcmRecordAddr(String pcmRecordAddr) {
		Constant.pcmRecordAddr = pcmRecordAddr;
	}

	public static String getVoiceCommentAddr() {
		return voiceCommentAddr;
	}

	public static void setVoiceCommentAddr(String voiceCommentAddr) {
		Constant.voiceCommentAddr = voiceCommentAddr;
	}

	public static int getBackgroundColor() {
		return backgroundColor;
	}

	public static void setBackgroundColor(int backgroundColor) {
		Constant.backgroundColor = backgroundColor;
	}

	public static int getTextColor() {
		return textColor;
	}

	public static void setTextColor(int textColor) {
		Constant.textColor = textColor;
	}

	public static int getTextSize() {
		return textSize;
	}

	public static void setTextSize(int textSize) {
		Constant.textSize = textSize;
	}

	public static String getAppname() {
		return APPName;
	}

	public static String getAppid() {
		return APPID;
	}

	public static String getAppfile() {
		return appfile;
	}

	public static String getAppend() {
		return append;
	}

	public static String getFileDownloadTag() {
		return FILE_DOWNLOAD_TAG;
	}

	public static String getUrl() {
		return url;
	}

	public static String getAudiourl() {
		return audiourl;
	}

	public static String getUserimage() {
		return userimage;
	}

	public static String getAudiourlVip() {
		return audiourl_vip;
	}

	public static String getUpdateReadUrl() {
		return update_read_url;
	}

	public static String getWxappId() {
		return wxAPP_ID;
	}

	public static int getReadColor() {
		return read_color;
	}

	public static int getUnreadColor() {
		return unread_color;
	}

	public static int getSelectColor() {
		return select_color;
	}

	public static int getUnselectColor() {
		return unselect_color;
	}

	public static int getOptionitemselect() {
		return optionItemSelect;
	}

	public static int getOptionitemunselect() {
		return optionItemUnSelect;
	}

	public static String getAppupdateurl() {
		return appUpdateUrl;
	}

	public static String getDetailurl() {
		return detailUrl;
	}

	public static String getFeedbackurl() {
		return feedBackUrl;
	}

	public static String getSearchurl() {
		return searchUrl;
	}

	public static String getTitleurl() {
		return titleUrl;
	}

	/*
	 * public static String getVideoSubPath() { return videoSubPath; }
	 */

	public static boolean isCategoryavailable() {
		return categoryavailable;
	}

	public static boolean isVideoavailable() {
		return videoavailable;
	}

	public static boolean isTestavailable() {
		return testavailable;
	}

	public static int getStudytextColor() {
		return studytext_color;
	}

	public static String getCommenturl() {
		return commenturl;
	}

	public static String getExpressionurl() {
		return expressionurl;
	}

	public static String getEmailRegisturl() {
		return email_registUrl;
	}

	public static String getPhoneRegisturl() {
		return phone_registUrl;
	}

	public static boolean isShoufa360() {
		return SHOUFA_360;
	}
	
	public static boolean isShoufaHuawei() {
		return SHOUFA_HUAWEI;
	}

	public static boolean isShoufaYingyongbao() {
		return SHOUFA_YINGYONGBAO;
	}

	public static String getScreenShotAddr() {
		return screenShotAddr;
	}

	public static void setScreenShotAddr(String screenShotAddr) {
		Constant.screenShotAddr = screenShotAddr;
	}

	public static String getAppshareurl() {
		return appShareUrl;
	}

	public static String getAddintegralurl() {
		return addCreditsUrl;
	}

	public static String getPromotiondownloadurl() {
		return promotionDownloadUrl;
	}

	public static String getApptag() {
		return APPTag;
	}

	public static String getPromotionPlatName() {
		String result = null;
		if (isYouDao) {
			result = youdao;
		}
		return result;
	}

	public static String getsimRecordAddr() {
		return Environment
				.getExternalStorageDirectory() + "/iyuba/cambridge"+ "/audio/sound";
	}
}
