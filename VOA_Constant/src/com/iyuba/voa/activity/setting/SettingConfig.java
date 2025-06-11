package com.iyuba.voa.activity.setting;

import android.content.Context;

import com.iyuba.voa.frame.components.ConfigManagerVOA;

public class SettingConfig {


	private volatile static SettingConfig instance;

	private boolean screenLit;
	private boolean syncho;
	private boolean autonotice;
	private boolean autoLogin;
	private boolean autoPlay;
	private boolean autoStop;
	private final static String SCREEN_LIT_TAG = "screenLit";
	private final static String SYNCHO_TAG = "syncho";
	private final static String AUTO_LOGIN_TAG = "autoLogin";
	private final static String AUTO_NOTICE_TAG = "autonotice";
	private final static String AUTO_PLAY_TAG = "autoplay";
	private final static String AUTO_STOP_TAG = "autostop";
	
	private Context mContext;

	private SettingConfig(Context context) {

		this.mContext = context;
		
	}

	public static SettingConfig Instance(Context context) {

		if (instance == null) {
			synchronized (SettingConfig.class) {
				if (instance == null) {
					instance = new SettingConfig(context);
				}
			}

		}
		return instance;
	}

	/**
	 * 获取是否播放时屏幕常量
	 * 
	 * @return
	 */
	public boolean isScreenLit() {
		try {
			screenLit = ConfigManagerVOA.Instance(mContext).loadBoolean(SCREEN_LIT_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			screenLit =true;
		}
		return screenLit;
	}

	/**
	 * 设置屏幕常亮
	 * 
	 * @param automaticDownload
	 */
	public void setScreenLit(boolean screenLit) {
		ConfigManagerVOA.Instance(mContext).putBoolean(SCREEN_LIT_TAG, screenLit);
	}
	
	
	/**
	 * 获取是否播放时同步
	 * 
	 * @return
	 */
	public boolean isSyncho() {
		try {
			syncho = ConfigManagerVOA.Instance(mContext).loadBoolean(SYNCHO_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			syncho = true;
		}
		return syncho;
	}

	/**
	 * 设置屏幕常亮
	 * 
	 * @param automaticDownload
	 */
	public void setSyncho(boolean syncho) {
		ConfigManagerVOA.Instance(mContext).putBoolean(SYNCHO_TAG, syncho);
	}


	/**
	 * 获取是否自动登录
	 * 
	 * @return
	 */
	public boolean isAutoLogin() {
		try {
			autoLogin = ConfigManagerVOA.Instance(mContext).loadBoolean(AUTO_LOGIN_TAG,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			autoLogin = false;
		}
		return autoLogin;
	}

	/**
	 * 设置是否自动登录
	 * 
	 * @param automaticDownload
	 */
	public void setAutoLogin(boolean AutoLogin) {
		ConfigManagerVOA.Instance(mContext).putBoolean(AUTO_LOGIN_TAG, AutoLogin);
	}
	
	/**
	 * 获取是否自动登录
	 * 
	 * @return
	 */
	public boolean isAutoPlay() {
		try {
			autoPlay = ConfigManagerVOA.Instance(mContext).loadBoolean(AUTO_PLAY_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			autoPlay = false;
		}
		return autoPlay;
	}

	/**
	 * 设置是否自动登录
	 * 
	 * @param automaticDownload
	 */
	public void setAutoPlay(boolean AutoPlay) {
		ConfigManagerVOA.Instance(mContext).putBoolean(AUTO_PLAY_TAG, AutoPlay);
	}
	
	/**
	 * 获取是否自动登录
	 * 
	 * @return
	 */
	public boolean isAutoStop() {
		try {
			autoStop = ConfigManagerVOA.Instance(mContext).loadBoolean(AUTO_STOP_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			autoStop = false;
		}
		return autoStop;
	}

	/**
	 * 设置是否自动登录
	 * 
	 * @param automaticDownload
	 */
	public void setAutoStop(boolean AutoStop) {
		ConfigManagerVOA.Instance(mContext).putBoolean(AUTO_STOP_TAG, AutoStop);
	}

	
	/**
	 * 获取是否后台自动提示新文章
	 * 
	 * @return
	 */
	public boolean isAutoNotice() {
		try {
			autonotice = ConfigManagerVOA.Instance(mContext).loadBoolean(AUTO_NOTICE_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			autonotice = true;
		}
		return autonotice;
	}
	
	/**
	 * 设置是否后台自动提示新文章
	 * 
	 * @param 
	 */
	public void setAutoNotice(boolean AutoNotice) {
		ConfigManagerVOA.Instance(mContext).putBoolean(AUTO_NOTICE_TAG, AutoNotice);
	}
	

}
