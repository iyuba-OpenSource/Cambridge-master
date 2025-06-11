package com.iyuba.camstory;

import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.common.WebConstant;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.manager.InitManager;


public class PrepareActivity extends Activity {
    String TAG = "PrepareActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        Log.e(TAG, "onCreate: -- " );
        if (ConfigManager.Instance().loadBoolean("PrivacyDialog", false)) {
            Log.e(TAG, "onCreate: init" );
            //延迟初始化
            CrashApplication.getInstance().initLazy();
            goToInit(false);
        } else {
            Log.e(TAG, "onCreate: dialog" );
            showDialog(this);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        registerReceiver(finishBroadcastReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (finishBroadcastReceiver!=null){
            try{
                unregisterReceiver(finishBroadcastReceiver);
            }catch(Exception e){
                if(e.getMessage().contains("java.lang.IllegalArgumentException")){
                    Log.e(TAG, "onDestroy: Receiver not registered: com.iyuba.camstory.PrepareActivity" );
                }
            }
        }
    }

    public void showDialog(Context context) {
        String privacy1 = "1.为了更方便您使用我们的软件，我们回根据您使用的具体功能时申请必要的权限，如摄像头，存储权限，录音权限等。\n";
        String privacy2 = "2.使用本app需要您了解并同意";
        String privacy3 = "隐私政策及用户协议";
        String privacy4 = "，点击同意即代表您已阅读并同意该协议";

        ClickableSpan secretString = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                goToSecret(context);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(context.getResources().getColor( R.color.app_color));
            }
        };

        ClickableSpan policyString = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                goToPolicy(context);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(context.getResources().getColor( R.color.app_color));
            }
        };
        int start = privacy1.length() + privacy2.length();
        int end = start + privacy3.length();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(privacy1);
        strBuilder.append(privacy2);
        strBuilder.append(privacy3);
        strBuilder.append(privacy4);
        strBuilder.setSpan(secretString, start, start + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(policyString, start + 5, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .create();

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_privacy, null);
        dialog.setView(view);
        dialog.show();

        TextView textView = view.findViewById(R.id.text_link);

        textView.setText(strBuilder);

        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        TextView agreeNo = view.findViewById(R.id.text_no_agree);
        TextView agree = view.findViewById(R.id.text_agree);

        agreeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        agree.setOnClickListener(v -> {
            //延迟初始化
            CrashApplication.getInstance().initLazy();

            ConfigManager.Instance().putBoolean("PrivacyDialog", true);
            ConfigManager.Instance().putLong("firstStartTime",System.currentTimeMillis());
            InitManager.getInstance(context).initLib();
            dialog.dismiss();
            goToInit(true);
        });

    }

    private void goToInit(boolean isFirstShow) {
//        startActivity(new Intent(PrepareActivity.this, SplashActivity.class));
        SplashActivity.start(this,isFirstShow);
    }

    private void goToSecret(Context context) {
        WebActivity.start(context, WebConstant.HTTP_SPEECH_ALL+"/api/protocolpri.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=1", "用户隐私政策");
    }

    private void goToPolicy(Context context) {
        WebActivity.start(context, WebConstant.HTTP_SPEECH_ALL+"/api/protocoluse.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=1", "用户使用协议");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public BroadcastReceiver finishBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            ((Activity) context).finish();
        }
    };
}
