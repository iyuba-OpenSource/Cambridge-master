package com.iyuba.abilitytest.activity.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iyuba.abilitytest.utils.SimpleNightMode;
import com.jaeger.library.StatusBarUtil;

/**
 * BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mContext;
    protected SwipeBackHelper swipeBackHelper;
    protected SimpleNightMode simpleNightMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        if (isSwipeBackEnable()) {
            swipeBackHelper = new SwipeBackHelper(this);
        }
        super.onCreate(savedInstanceState);
        simpleNightMode = new SimpleNightMode(this);
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }





    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (swipeBackHelper != null && isSwipeBackEnable()) {
            swipeBackHelper.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleNightMode.onResume();
    }

    @Override
    protected void onDestroy() {
        simpleNightMode.close();
        super.onDestroy();
    }

    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColor(this, color);
    }

    protected void setSwipeBStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColorForSwipeBack(this, color);
    }

}
