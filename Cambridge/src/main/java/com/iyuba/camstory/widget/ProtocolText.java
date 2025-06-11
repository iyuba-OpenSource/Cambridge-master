package com.iyuba.camstory.widget;

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
import android.view.View;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.common.WebConstant;
import com.iyuba.configation.Constant;
import com.iyuba.imooclib.ui.web.Web;

public class ProtocolText {

    public static void setText(TextView textView, Context context){

        String name,html;
        if (context.getPackageName().equals("com.iyuba.englishnovels")) {
            name = TextAttr.encode("英文小说");
            html =WebConstant.HTTP_SPEECH_ALL + "/api/ailanguageprotocol.jsp?apptype=" + name ;
        } else {
            name = TextAttr.encode("剑桥英语馆");
            html =WebConstant.HTTP_SPEECH_ALL + "/api/protocol.jsp?apptype=" + name;
        }

        ClickableSpan clickableSpan =new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent =new Intent(context, Web.class);
                intent.putExtra("url",html);
                intent.putExtra("title","用户协议及隐私政策");
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.colorPrimary));
            }
        };

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
        
        String privacy1 = "我已阅读并同意";
        String privacy2 = "使用条款和隐私政策";

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(privacy1);
        strBuilder.append(privacy2);
        strBuilder.setSpan(policyString, privacy1.length(), privacy1.length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(secretString, privacy1.length() + 5, privacy1.length() + privacy2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        textView.setText(strBuilder);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
