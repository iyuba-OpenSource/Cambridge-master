package com.iyuba.camstory.lycam.manager;

import android.os.Environment;

import com.iyuba.configation.Constant;

import java.io.File;

/**
 * 作者：renzhy on 16/6/15 13:39
 * 邮箱：renzhongyigoo@gmail.com
 */
public class ConstantManager {
	//课程详情页面的相关URL基址
	public final static String LIVE_CONTENT_BGPIC_BASE = "http://static3."+Constant.IYBHttpHead+"/resource/categoryIcon/";
	public final static String LIVE_CONTENT_TEACHER_IMG_BASE = "http://static3."+Constant.IYBHttpHead+"/resource/teacher/";
	public final static String USER_IMAGE_BASE = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=middle&uid=";

	public final static String USER_AVATAR_PREFIX = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid=";
	public final static String USER_AVATAR_SUFFIX = "&size=middle";

	//登录请求的相关常量
	public final static String LOGIN_PROTOCOL = "11001";
	public final static String LIVE_PACK_LIST_PROTOCOL = "10102";
	public final static String REGIST_EMAIL_PROTOCOL = "10002";
	public final static String REQUEST_MY_LIVEPACK_PROTOCOL = "10105";
	public final static String REGIST_PHONE_PROTOCOL = "11002";
	public final static String PERSONAL_BASIC_INFO_PROTOCOL = "20001";
	public final static String PERSONAL_DETAIL_INFO_PROTOCOL = "20001";
	//直播相关
	public final static String REQUEST_ZHIBO_SIGN = "1";
	public final static String LIVE_STATUS_READY = "ready";
	public final static String LIVE_STATUS_LIVE = "live";
	public final static String LIVE_STATUS_OVER = "over";

	public final static String REQUEST_LIVE_PACK_LIST_TYPE = "1";
	public final static int DEFAULT_PAGE_COUNTS = 20;


	public final static String PLATFORM_ANDROID = "android";
	public final static String FORMAT_XML = "xml";
	public final static String FORMAT_JSON = "json";
	//咨询的QQ
//	public final static String QQ_CONSULT = "3287508401";
//	public final static String QQ_CONSULT = "4008881905";
	public final static String QQ_CONSULT = "2274389535";
	//短信验证码
	public final static String SMSAPPKEY = "15bbe225bad60";
	public final static String SMSAPPSECRET = "5576f22f10b260b8a4b8798879aa3d04";

	private static ConstantManager sInstance;
	private String envir;
	private String appId;
	private String appName;
	private String appEnglishName;
	private String updateFolder;
	private String crashFolder;
	private String picCacheFolder;
	private String imgFile;

	public ConstantManager(){
		envir = Environment.getExternalStorageDirectory() + "/iyuba/iyulive222";//文件夹路径
		appId = "247";				//应用ID
		appName = "爱语课堂";				//应用名称
		appEnglishName = "iYuLive222";	//应用英文名
		updateFolder = envir + File.separator + "update";
		crashFolder = envir + File.separator + "crash";
		imgFile = envir + File.separator + "image";

		picCacheFolder = RuntimeManager.getContext().getExternalCacheDir().getAbsolutePath();
	}

	public static ConstantManager getInstance(){
		if(sInstance == null){
			sInstance = new ConstantManager();
		}
		return sInstance;
	}

	public String getEnvir() {
		return envir;
	}

	public void setEnvir(String envir) {
		this.envir = envir;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppEnglishName() {
		return appEnglishName;
	}

	public void setAppEnglishName(String appEnglishName) {
		this.appEnglishName = appEnglishName;
	}

	public String getUpdateFolder() {
		return updateFolder;
	}

	public void setUpdateFolder(String updateFolder) {
		this.updateFolder = updateFolder;
	}

	public String getCrashFolder() {
		return crashFolder;
	}

	public void setCrashFolder(String crashFolder) {
		this.crashFolder = crashFolder;
	}

	public String getPicCacheFolder() {
		return picCacheFolder;
	}

	public void setPicCacheFolder(String picCacheFolder) {
		this.picCacheFolder = picCacheFolder;
	}
	public String getImgFile() {
		return imgFile;
	}

	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}
}
