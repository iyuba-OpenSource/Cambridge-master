package com.iyuba.camstory.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.android.exoplayer2.util.NotificationUtil;
import com.iyuba.camstory.R;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.iyuba.camstory.utils.MusicNotifyUtil;


public class NotificationService extends Service {
	public static final String ACTION_NOTIFICATION_CONTROL = "action_notification_control";
	public static final String COMMAND_KEY = "cmd_key";
	public static final String TITLE = "title";
	public static final String SOURCE = "source";
	public static final String KEY_COMMAND_SHOW = "show_notification";
	public static final String KEY_COMMAND_REMOVE = "remove_notification";
	public static final String KEY_COMMAND_APPCLOSE = "app_close";
	private static final int NOTIFICATIN_ID = 999;

	private Notification mNotification;
	private String title;
	private int source;
	NotificationManager mNotificationManager;
	public static boolean isAppExit;
	public static boolean isStudyExit;
	public static boolean isNotificationCreated;
	VoaOp voaOp;

	private MusicNotifyUtil notificationUtils;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		isAppExit = false;
		isStudyExit = false;
		voaOp = new VoaOp();
		registerReceiver(closeBroadcastReceiver, new IntentFilter("service_stop"));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(closeBroadcastReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return 0;
		}

		String action = intent.getAction();
		if (ACTION_NOTIFICATION_CONTROL.equals(action)) {
			String cmd = intent.getStringExtra(COMMAND_KEY);
			title = intent.getStringExtra(TITLE);
			source = intent.getIntExtra(SOURCE, 0);
			if (KEY_COMMAND_SHOW.equals(cmd)) {
				showNotification();
			} else if (KEY_COMMAND_APPCLOSE.equals(cmd)) {
				isAppExit = true;
			} else if (KEY_COMMAND_REMOVE.equals(cmd)) {
				removeNotification();
			}
		} else {

		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void showNotification() {
		createNotification();
	}

	public void removeNotification() {
		isNotificationCreated = false;
		mNotificationManager.cancel(NOTIFICATIN_ID);
		stopForeground(true);
		stopSelf();
	}

	public void createNotification() {
//		isNotificationCreated = true;
//		Intent intent = new Intent(this, StudyActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//				| Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra(SOURCE, source);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//		Notification.Builder builder = new Notification.Builder(this);
//		builder.setOngoing(true).setContentText(title).setContentTitle("正在播放")
//				.setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_launcher) // 设置小型图标
//				.setTicker(title);
//		Intent intent2 = new Intent("background_close");
//		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2,
//				0);
//		Intent intent3 = new Intent("video_next");
//		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 0, intent3,
//				0);
//		builder.addAction(android.R.drawable.ic_media_next, "下一曲", pendingIntent3);
//		builder.addAction(android.R.drawable.ic_lock_power_off, "停止",
//				pendingIntent2);
//		mNotification = null;
//		mNotification = builder.build();
//		mNotificationManager.cancel(NOTIFICATIN_ID);
//		mNotificationManager.notify(NOTIFICATIN_ID, mNotification);
//		startForeground(NOTIFICATIN_ID, mNotification);
	}

	BroadcastReceiver closeBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("service_stop".equals(intent.getAction())) {
				removeNotification();
			}
		}
	};

	public void showNotificationNew() {
		notificationUtils = new MusicNotifyUtil(this);
		notificationUtils.show();
		startForeground(1, notificationUtils.getNotification());
	}
}