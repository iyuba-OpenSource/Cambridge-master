package com.iyuba.dailybonus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * AnimateDarkWindow
 */
public class AnimateDarkWindow extends PopupWindow {
    protected Activity mContext;
    private Window window;
    private ValueAnimator alphaAnimator;

    public AnimateDarkWindow(Activity activity, View contentView, int width, int height) {
        super(contentView, width, height);
        init(activity);
    }

    private void init(Activity context) {
        this.mContext = context;

        window = context.getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        alphaAnimator = new ValueAnimator();
        alphaAnimator.setDuration(250);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.alpha = (float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor) {
        showAnimation();
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        showAnimation();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        showAnimation();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        showAnimation();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        dismissAnimation();
        super.dismiss();
    }

    private void showAnimation() {
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 1.0f;
            window.setAttributes(params);
        }
        alphaAnimator.setFloatValues(1f, 0.6f);
        alphaAnimator.removeAllListeners();
        alphaAnimator.start();
    }

    private void dismissAnimation() {
        alphaAnimator.setFloatValues(0.6f, 1f);
        alphaAnimator.removeAllListeners();
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.dimAmount = 0.0f;
                    window.setAttributes(params);
                }
            }
        });
        alphaAnimator.start();
    }
}
