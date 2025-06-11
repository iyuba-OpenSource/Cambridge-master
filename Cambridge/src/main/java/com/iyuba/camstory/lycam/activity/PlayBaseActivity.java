package com.iyuba.camstory.lycam.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import tv.lycam.ijkplayer.utils.MediaUtils;
import tv.lycam.ijkplayer.widget.PlayerView;

/**
 * Created by lycamandroid on 16/7/14.
 * 播放页基类
 */
public class PlayBaseActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    //软键盘监听
    OnSoftKeyboardStateChangedListener mSoftKeyboardStateChangedListener;
    //布局变化监听
    ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    //手势监听
    GestureDetector gestureDetector;
    //软键盘高度
    int minkeyboardHeight;

    protected Context mContext;
    protected PlayerView player;
    private PowerManager.WakeLock wakeLock;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, false);
        /**demo的内容，激活设备常亮状态*/
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gestureDetector = null;
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        /*恢复设备亮度状态*/
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    ////////////////////////////////////keyboard///////////////////////////////////////////////////

    protected interface OnSoftKeyboardStateChangedListener {
        void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    protected interface OnKeyboardChangedExtraListener {
        void OnKeyboardChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    protected void addSoftKeyboardListener() {
        addSoftKeyboardListener(getRootView());
    }

    protected void addSoftKeyboardListener(View view) {
        mSoftKeyboardStateChangedListener = new OnSoftKeyboardStateChangedListener() {
            @Override
            public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
                OnKeyboardChanged(isKeyBoardShow, keyboardHeight);
            }
        };
        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                judgeKeyboard();
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    /**
     * 软键盘弹出
     */
    protected void judgeKeyboard() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        //判断窗口可见区域大小
        Rect r = new Rect();
        getRootView().getWindowVisibleDisplayFrame(r);
        //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
        int heightDifference = screenHeight - r.bottom;//( - r.top)
        boolean isKeyboardShowing = heightDifference > screenHeight / 3;
        mSoftKeyboardStateChangedListener.OnSoftKeyboardStateChanged(isKeyboardShowing, heightDifference);
    }

    protected void removeSoftKeyboardListener() {
        if (mLayoutChangeListener != null) {
            removeLayoutChangeListener();
        }
        if (mSoftKeyboardStateChangedListener != null) {
            mSoftKeyboardStateChangedListener = null;
        }
    }

    private void removeLayoutChangeListener() {
        //移除布局变化监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            this.getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
        mLayoutChangeListener = null;
    }

    /**
     * 获取最外层布局
     * @return
     */
    protected View getRootView() {
        return this.getWindow().getDecorView();
    }

    public void OnKeyboardChanged(boolean isKeyBoardShow, int keyboardHeight) {
    }

}
