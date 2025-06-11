package com.iyuba.dailybonus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * DailyBonusWindow
 */
public class DailyBonusWindow extends AnimateDarkWindow {
    private CircleWebView mWebView;
    private DailyBonusUtil.DailyBonusCallback callback;
    private ProgressBar progressBar;

    public DailyBonusWindow(Activity context, int width, int height, String url, DailyBonusUtil.DailyBonusCallback callback, OnDismissListener onDismissListener) {
        super(context, LayoutInflater.from(context).inflate(R.layout.daily_bonus_pop_window, null)
                , width, height);
        this.callback = callback;
        initView(width, height);
        if (onDismissListener != null) {
            setOnDismissListener(onDismissListener);
        }

        mWebView.addJavascriptInterface(DailyBonusWindow.this, "iyuba");
        mWebView.loadUrl(url);
    }

    private void initView(int width, int height) {
        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(android.R.style.Animation_Dialog);
        setBackgroundDrawable(new ColorDrawable());

        getContentView().findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mWebView = getContentView().findViewById(R.id.web);
        progressBar = getContentView().findViewById(R.id.progressbar);
        mWebView.setRadius(width, height, dp2px(mContext, 10));
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        setWebView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("eeeeeee", "onPageFinished");
                progressBar.setVisibility(View.GONE);
                if (callback != null) {
                    callback.umengDailyBonusEvent();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //支持插件
//        webSettings.setPluginsEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置自适应屏幕，两者合用（下面这两个方法合用）
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    @JavascriptInterface
    public void onFootImgClick(String url) {
        if (callback != null) {
            callback.onADClick(url);
        }
    }

    @JavascriptInterface
    public void onAdSpanClick(String url) {
        if (callback != null) {
            callback.onADClick(url);
        }
    }

    public static int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}