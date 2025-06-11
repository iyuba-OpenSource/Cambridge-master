package com.iyuba.camstory.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.iyuba.camstory.ChapterDetailActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.ChapterVersionResponse;
import com.iyuba.camstory.event.OnChapterChangeEvent;
import com.iyuba.camstory.event.OnPlayChangeEvent;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.utils.MusicNotifyUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.iyuba.camstory.fragment.ChapterListFragment.CHAPTER_INFO;

public final class BackGroundMusicService extends Service {
    private MusicNotifyUtil notificationUtils;
    private BroadcastReceiver receiver;
    private String TAG;

    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        initBroadCastReceiver();
        showNotification();
    }

    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification() {
        notificationUtils = new MusicNotifyUtil(this);
        notificationUtils.show();
        startForeground(1, notificationUtils.getNotification());
    }

    private void initBroadCastReceiver() {
        receiver = new MusicBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicNotifyUtil.ACTION_MUSIC);
        registerReceiver(receiver, filter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChapterChange(@NotNull OnChapterChangeEvent event) {
        if (notificationUtils != null && event!=null) {
            String title = "";
            if (event.getChapterInfo()!=null && !TextUtils.isEmpty(event.getChapterInfo().getCname_cn())){
                title = event.getChapterInfo().getCname_cn();
            }
            notificationUtils.updateTitle(title);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayChange(@NotNull OnPlayChangeEvent onPlayChangeEvent) {
        notificationUtils.switchStatus();
    }

    private class MusicBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action == MusicNotifyUtil.ACTION_MUSIC) {
                    switch (intent.getIntExtra(MusicNotifyUtil.NOTIFY_BUTTON_ID, 0)) {
                        case MusicNotifyUtil.CLOSE:
                            StoryDataManager.Instance().getCmPlayer().pause();
                            notificationUtils.cancel();
                            stopForeground(true);
                            break;
                        case MusicNotifyUtil.PLAY:
                            if (StoryDataManager.Instance().getCmPlayer().isPlaying()) {
                                StoryDataManager.Instance().getCmPlayer().pause();
                            } else {
                                StoryDataManager.Instance().getCmPlayer().start();
                            }
                            notificationUtils.switchStatus();
                            break;
                    }
                }
            }
        }
    }
}
