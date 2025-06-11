package com.iyuba.camstory.protocol.web;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.utils.ToastUtil;

public class WebContent extends BasisActivity {
    private ImageView backButton;
    private WebView web;
    private TextView textView;
    private ProgressBar progressIndictor;
    private TextView tv_web_more;
    private ContextMenu context_menu_web;
    private Context mContext;
    private String url;
    private String tilte;
    private String resource;//来源

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//q
        setContentView(R.layout.activity_web_content);
        mContext = this;
        setProgressBarVisibility(true);

        url = getIntent().getStringExtra("url");
        tilte = getIntent().getStringExtra("title");
        resource = getIntent().getStringExtra("resource");
        intViews();
        loadData();
        initWebChrom();
    }

    private void initWebChrom() {
        WebSettings websettings = web.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        setWebViewIndictror();
        web.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        web.loadUrl(url);
        textView.setText(this.getIntent().getStringExtra(tilte));
        tv_web_more.setVisibility(View.VISIBLE);
        tv_web_more.setOnClickListener(moreButtonClickListener());
    }

    private View.OnClickListener moreButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context_menu_web.setText(mContext.getResources().getStringArray(R.array.web_more));
                context_menu_web.setCallback(new ResultIntCallBack() {
                    @Override
                    public void setResult(int result) {
                        switch (result) {
                            case 0:
                                ClipboardManager cpm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                cpm.setText(url);
                                ToastUtil.showToast(mContext, "链接复制成功");
                                break;
                            case 1:
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                intent.setData(content_url);
                                startActivity(intent);
                                break;
                            case 3:
                                web.reload();
                                break;
                        }
                        context_menu_web.dismiss();
                    }
                });
                context_menu_web.show();
            }
        };
    }

    private void intViews() {
        backButton = findViewById(R.id.iv_title_back);
        textView = findViewById(R.id.tv_nav_title);
        progressIndictor = findViewById(R.id.pb_progress_indictor);
        web = findViewById(R.id.webView);
        tv_web_more = findViewById(R.id.tv_web_more);
        context_menu_web = findViewById(R.id.context_menu_web);
    }

    /**
     * 设置顶部进度条的可见性
     */
    private void setWebViewIndictror() {
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressIndictor.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == progressIndictor.getVisibility()) {
                        progressIndictor.setVisibility(View.VISIBLE);
                    }
                    progressIndictor.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }

    @Override
    public void onBackPressed() {
        if ((resource != null && resource.equals("welcome")) || !web.canGoBack()) {
            setResult(0);
            finish();
        } else if (web.canGoBack()) {
            web.goBack(); // goBack()表示返回webView的上一页面
        }
    }
}
