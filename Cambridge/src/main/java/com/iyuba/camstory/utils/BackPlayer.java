package com.iyuba.camstory.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import com.iyuba.camstory.event.OnPlayChangeEvent;
import com.iyuba.camstory.event.PlayerCompletionEvent;
import com.iyuba.camstory.event.PlayerPreparedEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 视频框架
 *
 * @author chentong
 *         <p>
 *         <p>
 *         Displays a video file. The VideoView class can load images from
 *         various sources (such as resources or content providers), takes care
 *         of computing its measurement from the video so that it can be used in
 *         any layout manager, and provides various display options such as
 *         scaling and tinting.
 */
public class BackPlayer implements MediaPlayerControl {
    private Context mContext;
    // settable by the client
    private Uri mUri;
    private int mDuration;
    // All the stuff we need for playing and showing a video
    private MediaPlayer mMediaPlayer = null;
    public boolean mIsPrepared;
    private MediaController mMediaController;
    private OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private int mCurrentBufferPercentage;
    private OnErrorListener mOnErrorListener;
    public boolean isPause = false;
    private ExecutorService executorService;


    public BackPlayer(Context context) {
        executorService = Executors.newFixedThreadPool(3);
        mContext = context;
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        mUri = uri;
        executorService.execute(new OpenVideoRunnable());
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    class OpenVideoRunnable implements Runnable {
        //开启音频是在异步里面进行的
        @Override
        public void run() {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
            } else {
                mMediaPlayer = new MediaPlayer();
            }
            try {
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mIsPrepared = false;
                mDuration = -1;
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mCurrentBufferPercentage = 0;

                mMediaPlayer.setDataSource(mContext, mUri);

                //mMediaPlayer.setDisplay(mSurfaceHolder);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setScreenOnWhilePlaying(true);
                mMediaPlayer.prepareAsync();
                attachMediaController();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setMediaController(MediaController controller) {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            mMediaController.setEnabled(mIsPrepared);
        }
    }

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            // briefly show the mediacontroller
            mIsPrepared = true;
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }
            EventBus.getDefault().post(new PlayerPreparedEvent());
        }
    };

    private OnCompletionListener mCompletionListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            if (mMediaController != null) {
                mMediaController.hide();
            }
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mMediaPlayer);
            }
            if (mIsPrepared)
                EventBus.getDefault().post(new PlayerCompletionEvent());
        }
    };

    private OnErrorListener mErrorListener = new OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            if (mMediaController != null) {
                mMediaController.hide();
            }

			/* If an error handler has been supplied, use it and finish. */
            if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, framework_err,
                        impl_err)) {
                    return true;
                }
            }
            return true;
        }
    };

    private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
        }
    };

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
        mBufferingUpdateListener = l;
    }

    /**
     * Register a callback to be invoked when the media file is loaded and ready
     * to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file has been
     * reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs during playback or
     * setup. If no listener is specified, or if the listener returned false,
     * VideoView will inform the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(OnErrorListener l) {
        mOnErrorListener = l;
    }

    public void start() {
        if (mMediaPlayer != null && mIsPrepared) {
            mMediaPlayer.start();
            EventBus.getDefault().post(new OnPlayChangeEvent());
        }
        isPause = false;
    }

    public void pause() {
        if (mMediaPlayer != null && mIsPrepared) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                EventBus.getDefault().post(new OnPlayChangeEvent());
            }
            isPause = true;
        }
    }

    public int getDuration() {
        if (mMediaPlayer != null && mIsPrepared) {
			/*if (mDuration > 0) {
				return mDuration;
			}*/
            mDuration = mMediaPlayer.getDuration();
            return mDuration;
        }
        mDuration = -1;
        return mDuration;
    }

    /**
     * 获取总时长
     *
     * @return
     */
    public String getAudioAllTime() {
        StringBuffer timeBuffer = new StringBuffer("");
        if (mMediaPlayer != null) {
            int musicTime = mMediaPlayer.getDuration() / 1000;// 绉?
            String minit = "00";// 鍒?
            String second = "00";// 绉?
            if ((musicTime / 60) < 10)// 鍒?
            {
                minit = "0" + String.valueOf(musicTime / 60);
                // timeBuffer.append("0").append(musicTime / 60).append(":")
                // .append(musicTime % 60);
            } else {
                minit = String.valueOf(musicTime / 60);
            }
            if ((musicTime % 60) < 10)// 绉?
            {
                second = "0" + String.valueOf(musicTime % 60);
            } else {
                second = String.valueOf(musicTime % 60);
            }
            timeBuffer.append(minit).append(":").append(second);

        }
        return timeBuffer.toString();
    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null && mIsPrepared) {
            try {
                return mMediaPlayer.getCurrentPosition();
            }catch (Exception e){
                LogUtil.e("崩溃异常getCurrentPosition"+e);
                return 0;
            }
        }
        return 0;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public String getAudioCurrTime() {
        StringBuffer timeBuffer = new StringBuffer("");
        if (mMediaPlayer != null) {
            int musicTime = mMediaPlayer.getCurrentPosition() / 1000;
            String minit = "00";// 鍒?
            String second = "00";// 绉?
            if ((musicTime / 60) < 10)// 鍒?
            {
                minit = "0" + String.valueOf(musicTime / 60);
                // timeBuffer.append("0").append(musicTime / 60).append(":")
                // .append(musicTime % 60);
            } else {
                minit = String.valueOf(musicTime / 60);
            }
            if ((musicTime % 60) < 10)// 绉?
            {
                second = "0" + String.valueOf(musicTime % 60);
            } else {
                second = String.valueOf(musicTime % 60);
            }
            timeBuffer.append(minit).append(":").append(second);
        }
        return timeBuffer.toString();
    }


    public void seekTo(int msec) {
        if (mMediaPlayer != null && mIsPrepared) {
            mMediaPlayer.seekTo(msec);
        } else {
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null && mIsPrepared) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    @Override
    public boolean canPause() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canSeekForward() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getAudioSessionId() {
        // TODO Auto-generated method stub
        return 0;
    }
}
