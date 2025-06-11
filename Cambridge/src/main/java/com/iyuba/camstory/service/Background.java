package com.iyuba.camstory.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.VoaDataManager;
import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.iyuba.camstory.utils.MusicNotifyUtil;
import com.iyuba.camstory.utils.NetWorkStateUtil;
import com.iyuba.camstory.utils.NextVideo;
import com.iyuba.camstory.widget.VideoView;
import com.iyuba.voa.activity.setting.Constant;
import com.iyuba.voa.frame.components.ConfigManagerVOA;

import java.io.File;

public class Background extends Service {
	public static VideoView vv;
	static int voaid;
	public static boolean tobecontinued = false;
	public static boolean changelanguage = false;
	private MyBinder myBinder = new MyBinder();

	private VoaOp voaOp;
	private boolean phoneStop = false;

	private MusicNotifyUtil notificationUtils;

	public class MyBinder extends Binder {

		public Background getService() {
			return Background.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// unRegisterBoradcastReceiver();
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (vv == null) {
			vv = new VideoView(this);
		} else if (tobecontinued) {
			vv.start();
		}
		//voa操作数据库的
		voaOp = new VoaOp();
		//注册一个广播和动作
		registerReceiver(receiver, new IntentFilter("background_close"));
		registerReceiver(nextVideoReceiver, new IntentFilter("video_next"));
		registerReceiver(closeReceiver, new IntentFilter("service_stop"));
		registerBoradcastReceiver();
		tobecontinued = false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// vv.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		unregisterReceiver(nextVideoReceiver);
		unregisterReceiver(closeReceiver);
		if (vv != null) {
			vv.stopPlayback();
			vv = null;
		}
		voaid = -1;
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//如果匹配上改广播那么就把视频关掉如果Intent不为空且程序没有退出,就在开启一个服务该服务的作用是
			if ("background_close".equals(intent.getAction())) {
				if (vv != null) {
					vv.stopPlayback();
				}
				if (intent != null && NotificationService.isAppExit) {
					vv = null;
					Intent intent2 = new Intent(Background.this,
							NotificationService.class);
					intent2.setAction(NotificationService.ACTION_NOTIFICATION_CONTROL);
					intent2.putExtra(NotificationService.COMMAND_KEY,
							NotificationService.KEY_COMMAND_REMOVE);
					startService(intent2);
					stopSelf();
				}
			}
		}
	};

	public VideoView getPlayer() {
		return vv;
	}

	public static int getTag() {
		return voaid;
	}

	public static void setTag(int id) {
		voaid = id;
	}

	public void registerBoradcastReceiver() {
		//android12以下
		PhoneStateListener p = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:
					if (vv != null && vv.isPlaying()) {
						vv.pause();
						phoneStop = true;
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if (vv != null && vv.isPlaying()) {
						vv.pause();
						phoneStop = true;
					}
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					if (vv != null && phoneStop) {
						vv.start();
						phoneStop = false;
					}
					break;
				default:
					break;
				}
			}
        };

		TelephonyManager tm = (TelephonyManager) CrashApplication.getInstance()
				.getSystemService(Service.TELEPHONY_SERVICE);
		if (Build.VERSION.SDK_INT<Build.VERSION_CODES.S){
			tm.listen(p, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	BroadcastReceiver nextVideoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("video_next".equals(intent.getAction())
					&& NotificationService.isStudyExit) {
				nextVideo();
			}
		}
	};

	BroadcastReceiver closeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("service_stop".equals(intent.getAction())) {
				stopSelf();
			}
		}
	};

	private void nextVideo() {
		int mode = ConfigManagerVOA.Instance(this).loadInt("mode");
		if (mode == 0) {
			vv.start();
		} else {
			if (mode == 1) {
				voaid = new NextVideo(voaid, 0, this).following();
			} else if (mode == 2) {
				voaid = new NextVideo(voaid, 0, this).nextVideo();
			}
			Voa voaTemp = voaOp.findDataById(voaid);
			VoaDataManager.getInstance().voaTemp = voaTemp;
			String url;
			if (!AccountManager.getInstance().isVip(CrashApplication.getInstance())) {
				if (Constant.getAppid().equals("213")
						|| Constant.getAppid().equals("217")) {
					url = Constant.getAudiourl() + voaid + Constant.getAppend();
				} else {
					url = Constant.getAudiourl() + voaTemp.sound;
				}
			} else {
				if (Constant.getAppid().equals("213")
						|| Constant.getAppid().equals("217")) {
					url = Constant.getAudiourlVip() + voaid + Constant.getAppend();
				} else {
					url = Constant.getAudiourlVip() + voaTemp.sound;
				}
			}
			int netType = NetWorkStateUtil.getAPNType();
			String pathString = ConfigManagerVOA.Instance(this).loadString(
					"media_saving_path")
					+ voaTemp.voaid + Constant.getAppend();
			File fileTemp = new File(pathString);
			if (fileTemp.exists()) {
				vv.setVideoPath(pathString, null);
			} else if (netType == 0) {
				vv.start();
			} else if (netType == 1) {
				vv.setVideoPath(url, null);
			} else if (netType == 2) {
				vv.setVideoPath(url, null);
			}
		}
	}

	private void playVideo(String url) {
		vv.setVideoPath(url, null);
	}

	public void showNotification() {
		notificationUtils = new MusicNotifyUtil(this);
		notificationUtils.show();
		startForeground(1, notificationUtils.getNotification());
	}
}
