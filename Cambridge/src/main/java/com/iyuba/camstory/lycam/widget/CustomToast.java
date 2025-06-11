package com.iyuba.camstory.lycam.widget;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.iyuba.camstory.lycam.manager.RuntimeManager;


/**
 * 重载后toast 可同时触发
 *
 * @author 陈彤
 */
public class CustomToast {
    public static final int LENTH_SHORT = 1000;
    public static final int LENTH_MEDIUM = 2000;
    public static final int LENTH_LONG = 3000;
    public static CustomToast customToast;
    private Toast mToast;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mToast != null) {
                mToast.cancel();
            }
            return false;
        }
    });

    public CustomToast() {
    }

    public static CustomToast getInstance() {
        if (customToast == null) {
            customToast = new CustomToast();
        }
        return customToast;
    }

    public void showToast(String text) {
        showToast(text, LENTH_SHORT);
    }


    public void showToast(int resId, int duration) {
        showToast(RuntimeManager.getString(resId), duration);
    }

    public void showToast(int resId) {
        showToast(RuntimeManager.getString(resId), LENTH_SHORT);
    }

    public void showToast(String text, int duration) {
        mHandler.removeMessages(0);
        if (mToast != null) {
            mToast.setText(text);
            mToast.setDuration(duration);
        } else {
            mToast = Toast.makeText(RuntimeManager.getContext(), text, duration);
        }
        mHandler.sendEmptyMessageDelayed(0, duration);
        mToast.show();
    }
}
