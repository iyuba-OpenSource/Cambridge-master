package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.voa.activity.setting.Constant;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends Activity {
    private static final String TAG = WebActivity.class.getSimpleName();
    private Context mContext;

    private Button backButton;
    private WebView web;
    private TextView textView;
    private ImageButton imageButton;
    private CustomDialog waitingDialog;
    private String url = null;
    private String title = null;
    private String purpose = "null";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gift_web_layout);

        Intent intent = this.getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        textView = findViewById(R.id.titlebar_title);
        if (!title.equals(null)) {
            textView.setText(title);
        }
        web = findViewById(R.id.webView_gift);
        web.loadUrl(url);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //backButton = (Button) findViewById(R.id.button_back);
        //textView = (TextView) findViewById(R.id.play_title_info);

        //textView.setText(Constant.getAppname());
        waitingDialog = new WaittingDialog().wettingDialog(mContext);


        web.addJavascriptInterface(this, "Android");

        web.getSettings().setJavaScriptEnabled(true);

        web.requestFocus();
        web.getSettings().setBuiltInZoomControls(true);// 显示放大缩小
        web.getSettings().setSupportZoom(true);// 可放大
        web.getSettings().setRenderPriority(RenderPriority.HIGH);// 提高渲染,加快加载速度
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!waitingDialog.isShowing()) {
                    waitingDialog.show();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                waitingDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                waitingDialog.dismiss();
            }
        });
        web.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        // regToWx();
    }

    public static void start(Context context , String url , String title ){
        Intent intent  = new Intent(context , WebActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

	/*
     * private void regToWx(){ api = WXAPIFactory.createWXAPI(this,
	 * Constant.wxAPP_ID, false); api.registerApp(Constant.wxAPP_ID); }
	 */

    @Override
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            web.goBack(); // goBack()表示返回webView的上一页面
            return true;
        } else if (!web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        if (waitingDialog != null)
            waitingDialog.dismiss();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (waitingDialog != null)
            waitingDialog.dismiss();
        super.onDestroy();
    }



    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        imageButton = findViewById(R.id.iv_title_back);

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @JavascriptInterface
    public void successOnAndroid() {
        WebActivity.this.finish();
    }

}
