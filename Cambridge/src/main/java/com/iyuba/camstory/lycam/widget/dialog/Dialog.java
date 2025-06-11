

package com.iyuba.camstory.lycam.widget.dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDialog;

import com.iyuba.camstory.R;
import com.iyuba.camstory.lycam.manager.RuntimeManager;
public class Dialog extends AppCompatDialog {

    private Context context;
    private View contentView;
    private View view;
    private LinearLayout backView;
    private boolean cancleOutSide;
    private OnDismissListener listener;
    private int marginDimsion;

    public Dialog(Context context, View v) {
        super(context, R.style.MyDialogTheme);
        this.context = context;// init Context
        this.contentView = v;
        cancleOutSide = true;
        marginDimsion = 10;
    }

    public Dialog(Context context, View v, boolean cancle) {
        super(context, R.style.MyDialogTheme);
        this.context = context;// init Context
        this.contentView = v;
        this.cancleOutSide = cancle;
        marginDimsion = 10;
    }

    public Dialog(Context context, View v, boolean cancle, int marginDimsion) {
        super(context, R.style.MyDialogTheme);
        this.context = context;// init Context
        this.contentView = v;
        this.cancleOutSide = cancle;
        this.marginDimsion = marginDimsion;
    }

    public Dialog(Context context, View v, boolean cancle, int marginDimsion, OnDismissListener dismissListener) {
        super(context, R.style.MyDialogTheme);
        this.context = context;// init Context
        this.contentView = v;
        this.cancleOutSide = cancle;
        this.listener = dismissListener;
        this.marginDimsion = marginDimsion;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog);
        view = findViewById(R.id.contentDialog);
        backView = findViewById(R.id.dialog_rootView);
        if (cancleOutSide) {
            backView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getX() < view.getLeft()
                            || event.getX() > view.getRight()
                            || event.getY() > view.getBottom()
                            || event.getY() < view.getTop()) {
                        dismiss();
                    }
                    return false;
                }
            });
        } else {
        }
        backView.removeAllViewsInLayout();
        backView.addView(contentView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view = contentView;
        backView.setPadding(RuntimeManager.dip2px(marginDimsion), RuntimeManager.dip2px(marginDimsion), RuntimeManager.dip2px(marginDimsion), RuntimeManager.dip2px(marginDimsion));
        if (listener != null) {
            this.setOnDismissListener(listener);
        }
    }

    @Override
    public void show() {
        // TODO 自动生成的方法存根
        super.show();
        // set dialog enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_main_show_amination));
        backView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_root_show_amination));
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_amination);
        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Dialog.super.dismiss();
                    }
                });

            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amination);
        view.startAnimation(anim);
        backView.startAnimation(backAnim);
    }
}
