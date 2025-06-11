package com.iyuba.camstory.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.manager.InitManager;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.common.WebConstant;
import com.iyuba.configation.ConfigManagerHead;
import com.iyuba.configation.Constant;
import com.iyuba.imooclib.ui.web.Web;

public class PrivacyDialog {

    public static boolean agreeStatus = false;


    public static void showDialog(Context context) {
        String privacy1 = "1.为了更方便您使用我们的软件，我们回根据您使用的具体功能时申请必要的权限，如摄像头，存储权限，录音权限等。\n";
        String privacy2 = "2.使用本app需要您了解并同意";
        String privacy3 = "用户协议及隐私政策";
        String privacy4 = "，点击同意即代表您已阅读并同意该协议";

        ClickableSpan secretString = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent =new Intent(context, Web.class);
                intent.putExtra("url", WebConstant.HTTP_SPEECH_ALL+"/api/protocolpri.jsp?apptype=" + context.getResources().getString(R.string.app_name) + "&company=1");
                intent.putExtra("title","隐私政策");
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ContextCompat.getColor(context,R.color.app_color));
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan policyString = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent =new Intent(context, Web.class);
                intent.putExtra("url",WebConstant.HTTP_SPEECH_ALL+"/api/protocoluse666.jsp?apptype="+context.getResources().getString(R.string.app_name)+"&company=1");
                intent.putExtra("title","用户协议");
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(context,R.color.app_color));
            }
        };

        int start=privacy1.length()+privacy2.length();
        int end= start+privacy3.length();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(privacy1);
        strBuilder.append(privacy2);
        strBuilder.append(privacy3);
        strBuilder.append(privacy4);
        strBuilder.setSpan(policyString,start,start+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(secretString,start+5,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        final AlertDialog dialog =new AlertDialog.Builder(context)
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
                ToastUtil.showToast(context,"请同意");
            }
        });

        agree.setOnClickListener(v -> {
            //延迟初始化
            CrashApplication.getInstance().initLazy();

            ConfigManagerHead.Instance().putBoolean("PrivacyDialog",true);
            InitManager.getInstance(context).initLib();
            agreeStatus = true;
            dialog.dismiss();
        });

    }
}
