package com.iyuba.camstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iyuba.camstory.listener.AppUpdateCallBack;
import com.iyuba.camstory.manager.VersionManager;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.multithread.DownloadProgressListener;
import com.iyuba.multithread.FileDownloader;
import com.iyuba.multithread.MultiThreadDownloadManager;
import com.iyuba.voa.activity.setting.Constant;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * 关于界面
 *
 * @author chentong
 */
public class AboutActivity extends Activity implements AppUpdateCallBack {
    private static final String TAG = AboutActivity.class.getSimpleName();
    private Context mContext;

    private View background;
    private Button backBtn;
    private View url;
    private View appUpdateBtn;
    private View appNewImg;
    private TextView currVersionName;
    private String version_code;
    private String appUpdateUrl;
    private ProgressBar progressBar_download; // 下载进度条
    private TextView weixinButton;
    private String appName;
    private String company;
    private ImageView title;
    private ImageView logo;
    private TextView tvContent;

    //测试-渠道号点击次数
    private int channelCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about);
        mContext = this;
        background = findViewById(R.id.backlayout);
        background.setBackgroundColor(Constant.getBackgroundColor());

        title = findViewById(R.id.imageView1_title);
        logo=findViewById(R.id.imageView2_logo);
        tvContent =findViewById(R.id.textView1_content);

        String packageName =getApplication().getPackageName();
        if(packageName.equals("com.iyuba.camstory")){
            appName="剑桥英语馆";
            company="爱语吧";
        }else if(packageName.equals("com.iyuba.englishnovels")){
            appName="英文小说";
            company="爱语言";
            title.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            tvContent.setText(R.string.about_app_novels);

        }else {
            appName="英语小说";
            company="爱语言";

        }
        weixinButton = findViewById(R.id.weixin_button);
        weixinButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Intent localIntent = new Intent("android.intent.action.VIEW");
                 * //paramString是要关注的账号的二维码字符串：
                 * //如（麦芒公众号导航）：http://weixin.qq.com/r/UHXNwjzEwgK9hy2hnyB0
                 * //可以通过解析二维码图片获得 localIntent.setData(Uri.parse(
                 * "http://weixin.qq.com/r/YExeRIPEIQ7qrRCI9xIy"));
                 * localIntent.setPackage("com.tencent.mm");
                 * localIntent.putExtra(Intent.EXTRA_SUBJECT,"Share");
                 * localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 * mContext.startActivity(localIntent);
                 */
            }
        });
        if (Constant.isShoufa360()) {
            findViewById(R.id.relativeLayout3).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.relativeLayout3).setVisibility(View.GONE);
        }
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = findViewById(R.id.imageView_url_btn1);
        // 下载其他应用
        url.setVisibility(View.GONE);
        String brand = android.os.Build.BRAND;
        if(brand.contains("huawei")||brand.equals("honor")
                || brand.contains("nova") || brand.contains("mate")){//Meizu
            url.setVisibility(View.GONE);
        }

        url.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AboutActivity.this, WebActivity.class);
                intent.putExtra("title", appName) ;
                intent.putExtra("url", "http://app." + Constant.IYBHttpHead + "/android");
                startActivity(intent);
            }
        });
        appUpdateBtn = findViewById(R.id.relativeLayout1);
        appUpdateBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "appUpdateBtn1");
                checkAppUpdate();
                Log.v(TAG, "appUpdateBtn2");
            }
        });
        appNewImg = findViewById(R.id.imageView_new_app);
        currVersionName = findViewById(R.id.app_version);
        currVersionName.setText(getResources().getString(R.string.about_version)
                + VersionManager.VERSION_NAME);
        progressBar_download = findViewById(R.id.progressBar_update);
        checkAppUpdate();


        //测试-增加显示渠道号
        findViewById(R.id.app_title).setOnClickListener(v->{
            channelCount++;

            if (channelCount>=6){
                String channel = ChannelReaderUtil.getChannel(this);
                currVersionName.setText(channel);
            }
        });
    }

    /**
     * 检查新版本
     */
    public void checkAppUpdate() {
//		Log.e("checkupdate","checkupdate-----------------------------");
        if (getPackageName().equals("com.iyuba.camstory")) {
            VersionManager.getInstance().checkNewVersion(VersionManager.VERSION_CODE,
                    this);
        }
    }

    @Override
    public void appUpdateSave(String version_code, String newAppNetworkUrl) {
        this.version_code = version_code;
        this.appUpdateUrl = newAppNetworkUrl;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void appUpdateFaild() {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void appUpdateBegin(String newAppNetworkUrl) {
        // TODO Auto-generated method stub
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (appNewImg != null) {
                        appNewImg.setVisibility(View.VISIBLE);
                        appUpdateBtn.setEnabled(false);
                        progressBar_download.setVisibility(View.VISIBLE);
                    }
                    showAlertAndCancel(
                            getResources().getString(R.string.about_update_alert_1)
                                    + version_code
                                    + getResources().getString(R.string.about_update_alert_2),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 下载更新
                                    File file = new File(Constant.getEnvir()
                                            + Constant.getAppfile() + "/" + "/appUpdate");
                                    if (!file.exists()) {
                                        file.mkdirs();
                                    }
                                    MultiThreadDownloadManager.enQueue(mContext, -1, appUpdateUrl,
                                            new File(Constant.getEnvir() + Constant.getAppfile() + "/"
                                                    + "/appUpdate/" + Constant.getAppfile() + "_"
                                                    + version_code + ".apk"), 3, dpl);
                                    Log.v(TAG, Constant.getEnvir() + Constant.getAppfile() + "/"
                                            + "/appUpdate/" + Constant.getAppfile() + "_"
                                            + version_code + ".apk");
                                }
                            });
                    break;
                case 1:
                    if (appNewImg != null) {
                        appNewImg.setVisibility(View.INVISIBLE);
                        progressBar_download.setVisibility(View.INVISIBLE);
                    }
                    CustomToast.showToast(getBaseContext(), Constant.getAppname()
                            + getString(R.string.about_update_isnew), 1000);
                    break;
                case 3:
                    CustomToast.showToast(getBaseContext(), R.string.about_error, 1000);
                    break;
            }
        }
    };

    DownloadProgressListener dpl = new DownloadProgressListener() {

        @Override
        public void onProgressUpdate(int id, String downloadurl,
                                     final int fileDownloadSize) {
            runOnUiThread(new Runnable() {
                public void run() {
                    progressBar_download.setProgress(fileDownloadSize);
                }
            });
        }

        @Override
        public void onDownloadStoped(int id) {
            // TODO 自动生成的方法存根
        }

        @Override
        public void onDownloadStart(FileDownloader fileDownloader, int id,
                                    final int fileTotalSize) {
            runOnUiThread(new Runnable() {
                public void run() {
                    progressBar_download.setMax(fileTotalSize);
                }
            });
        }

        @Override
        public void onDownloadError(int id, Exception e) {
            handler.sendEmptyMessage(3);
        }

        @Override
        public void onDownloadComplete(int id, final String savePathFullName) {
            runOnUiThread(new Runnable() {
                public void run() {
                    progressBar_download.setProgress(progressBar_download.getMax());
                    appUpdateBtn.setEnabled(true);
                    progressBar_download.setVisibility(View.INVISIBLE);
                    FailOpera.getInstance(AboutActivity.this).openFile(mContext,
                            savePathFullName);
                }
            });
        }

    };

    private void showAlertAndCancel(String msg,
                                    DialogInterface.OnClickListener ocl) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(msg);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.alert_btn_ok), ocl);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appUpdateBtn.setEnabled(true);
                        progressBar_download.setVisibility(View.INVISIBLE);
                    }
                });
        alert.show();
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
    }
}
