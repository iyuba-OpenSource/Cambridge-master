package com.iyuba.camstory.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.iyuba.camstory.ChapterDetailActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.manager.StoryDataManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.iyuba.camstory.fragment.ChapterListFragment.CHAPTER_INFO;

public class MusicNotifyUtil {
    private NotificationManager manager;
    private final StoryDataManager mediaPlayerManager;
    public Notification notification;
    public RemoteViews remoteViews;
    private Context context;
    private static final String CHANNEL_NAME = "music";
    private static final int CODE_CLOSE = 1;
    private static final int CODE_PAUSE = 2;
    public static final String ACTION_MUSIC = "music";
    public static final int CLOSE = 7;
    public static final int PLAY = 4;
    public static final String NOTIFY_BUTTON_ID = "notify_button";
    public static final int CHANNELED_ID = 1;

    public MusicNotifyUtil(@NotNull Context context) {
        this.context = context;
        this.mediaPlayerManager = StoryDataManager.Instance();
    }

    public final Notification getNotification() {
        return notification;
    }

    public final void setNotification(@NotNull Notification notification) {
        this.notification = notification;
    }

    @NotNull
    public final RemoteViews getRemoteViews() {
        return remoteViews;
    }

    public final void setRemoteViews(@NotNull RemoteViews var1) {
        this.remoteViews = var1;
    }

    @NotNull
    public final Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "music");
        // TODO: 2023/2/7 暂时不加点击状态栏跳转
        Intent intent1 = new Intent();
        intent1.setClass(this.context, ChapterDetailActivity.class);
        intent1.putExtra("SERVICE","SERVICE");
        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= 31) {
            pendingIntent = PendingIntent.getActivity(this.context, 0, intent1, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this.context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setChannelId("music")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_launcher)
                .setCustomContentView(this.createContentView());
        return builder.build();
    }

    private final void setCommonClickPending(RemoteViews view) {
        Intent playOrPause = new Intent(ACTION_MUSIC);
        playOrPause.putExtra(NOTIFY_BUTTON_ID, PLAY);

        Intent close = new Intent(ACTION_MUSIC);
        close.putExtra(NOTIFY_BUTTON_ID, CLOSE);

        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flag = PendingIntent.FLAG_MUTABLE;
        } else {
            flag = PendingIntent.FLAG_UPDATE_CURRENT;
        }

        PendingIntent intentClose = PendingIntent.getBroadcast(this.context, CODE_CLOSE, close, flag);
        view.setOnClickPendingIntent(R.id.close, intentClose);


        PendingIntent intentPause = PendingIntent.getBroadcast(this.context, CODE_PAUSE, playOrPause, flag);
        view.setOnClickPendingIntent(R.id.pause, intentPause);
    }

    public final void show() {
        this.manager  = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_NAME,CHANNEL_NAME , NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(false);
            this.manager.createNotificationChannel(channel);
        }

        notification = createNotification();

        manager.notify(CHANNELED_ID, notification);
    }

    private final RemoteViews createContentView() {
        RemoteViews view = new RemoteViews(this.context.getPackageName(), R.layout.notify_music_player);
        BookDetailResponse.ChapterInfo chapterInfo = mediaPlayerManager.getCurChapterInfo();
        String title = "";
        if (chapterInfo!=null && !TextUtils.isEmpty(chapterInfo.getCname_cn())){
            title = chapterInfo.getCname_cn();
        }
        view.setTextViewText(R.id.title,title);
        view.setImageViewResource(R.id.pause, R.drawable.notify_player_play);
        this.setCommonClickPending(view);
        this.remoteViews = view;
        return view;
    }

    public final void switchStatus() {
        if (this.mediaPlayerManager.getCmPlayer().isPlaying()) {
            this.remoteViews.setImageViewResource(R.id.pause,R.drawable.notify_player_pause);
        } else {
            remoteViews.setImageViewResource(R.id.pause,R.drawable.notify_player_play);
        }
        this.notifyNotification();
    }

    public final void updateTitle(@Nullable String title) {

    }

    public final void notifyNotification() {
        manager.notify(CHANNELED_ID, notification);
    }

    public final void cancel() {
        manager.cancel(CHANNELED_ID);
    }

}
