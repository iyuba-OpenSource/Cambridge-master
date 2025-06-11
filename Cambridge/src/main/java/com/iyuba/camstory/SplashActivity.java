package com.iyuba.camstory;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.camstory.bean.AdBean;
import com.iyuba.camstory.lil.AdUtil;
import com.iyuba.camstory.lil.GlideUtil;
import com.iyuba.camstory.lil.RxTimer;
import com.iyuba.camstory.lil.ad.AdLogUtil;
import com.iyuba.camstory.lil.ad.show.AdShowUtil;
import com.iyuba.camstory.lil.ad.show.spread.AdSpreadShowManager;
import com.iyuba.camstory.lil.ad.show.spread.AdSpreadViewBean;
import com.iyuba.camstory.lil.util.LibRxTimer;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.utils.Utils;
import com.iyuba.configation.Constant;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdSpread;
import com.youdao.sdk.nativeads.ImageService;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//开屏页
public class SplashActivity extends Activity{

    private static final String FirstShowTag = "FirstShowTag";
    private boolean isJump = true;

    private RelativeLayout adLayout;
    private ImageView imageAd;
    private TextView btnSkip;
    private TextView textTips;

    public static void start(Context context,boolean isFirstShow){
        Intent intent = new Intent();
        intent.setClass(context, SplashActivity.class);
        intent.putExtra(FirstShowTag,isFirstShow);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        adLayout = findViewById(R.id.adLayout);
        imageAd = findViewById(R.id.adImage);
        btnSkip = findViewById(R.id.adSkip);
        textTips = findViewById(R.id.adTips);

        //显示弹窗
        boolean isFirstShow = getIntent().getBooleanExtra(FirstShowTag,false);
        if (isFirstShow){
            jump();
        }else {
            setAdShow();
        }


        //handler.post(runnable);
    }

    //设置广告显示
    private void setAdShow(){
        //设置广告显示
        if (!AdBlocker.getInstance().shouldBlockAd()){
            showSpreadAd();
        }else {
            btnSkip.setVisibility(View.VISIBLE);
            btnSkip.setOnClickListener(view->{
                jump();
            });
            openTimer();
        }
    }

    private void jump() {
        // 如果当前应用在前台则跳转，解决当显示开屏广告时，用户点击home键进入后台，此时仍会跳转的问题
        boolean background = Utils.isBackground(getApplicationContext());
        if (isJump && !background) {
            // 如果跳转成功过则不再跳转
            isJump = false;
            startActivity(new Intent(SplashActivity.this,
                    MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        closeTimer();
        AdSpreadShowManager.getInstance().stopSpreadAd();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        //点击广告返回后直接跳转
        if (isClickAd){
            isClickAd = false;
            jump();
        }
    }

    /********************************无广告的计时器操作***************************/
    //计时器名称
    private static final String timer_adShow = "adShowTimer";
    //默认倒计时
    private int adTime = 5;

    private void openTimer(){
        closeTimer();

        LibRxTimer.getInstance().multiTimerInMain(timer_adShow,0, 1000L, number -> {
            AdLogUtil.showDebug("广告显示", "倒计时--"+number);

            long time = adTime - number;
            btnSkip.setText(String.format("跳过(%1$s秒)",time));

            if (time<=0){
                closeTimer();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    private void closeTimer(){
        LibRxTimer.getInstance().cancelTimer(timer_adShow);
    }
    /*****************************新的开屏广告操作************************/
    //开屏广告接口是否完成
    private boolean isSplashAdLoaded = false;
    //是否已经点击了广告
    private boolean isClickAd = false;
    //是否已经获取了奖励
    private boolean isGetRewardByClickAd = false;
    //广告倒计时时间
    private static final int AdDownTime = 5;
    //操作倒计时时间
    private static final int OperateTime = 5;
    //界面数据
    private AdSpreadViewBean spreadViewBean = null;

    //展示广告
    private void showSpreadAd(){
        if (spreadViewBean==null){
            spreadViewBean = new AdSpreadViewBean(imageAd, btnSkip, textTips, adLayout, new AdSpreadShowManager.OnAdSpreadShowListener() {
                @Override
                public void onLoadFinishAd() {
                    isSplashAdLoaded = true;
                    AdSpreadShowManager.getInstance().stopOperateTimer();
                }

                @Override
                public void onAdShow(String adType) {

                }

                @Override
                public void onAdClick(String adType, boolean isJumpByUserClick, String jumpUrl) {
                    if (isJumpByUserClick){
                        if (TextUtils.isEmpty(jumpUrl)){
                            ToastUtil.showToast(SplashActivity.this, "暂无内容");
                            return;
                        }

                        //设置点击
                        isClickAd = true;
                        //关闭计时器
                        AdSpreadShowManager.getInstance().stopAdTimer();
                        //跳转界面
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, WebActivity.class);
                        intent.putExtra("url", jumpUrl);
                        startActivity(intent);
                    }
                }

                @Override
                public void onAdClose(String adType) {
                    //关闭广告
                    AdSpreadShowManager.getInstance().stopSpreadAd();
                    //跳出
                    jump();
                }

                @Override
                public void onAdError(String adType) {

                }

                @Override
                public void onAdShowTime(boolean isEnd, int lastTime) {
                    if (isEnd){
                        //跳转
                        jump();
                    }else {
                        //开启广告计时器
                        btnSkip.setText("跳过("+lastTime+"s)");
                    }
                }

                @Override
                public void onOperateTime(boolean isEnd, int lastTime) {
                    if (isEnd){
                        //跳转到下一个
                        jump();
                        return;
                    }

                    if (isSplashAdLoaded){
                        AdSpreadShowManager.getInstance().stopOperateTimer();
                        return;
                    }

                    AdLogUtil.showDebug(AdSpreadShowManager.TAG,"操作定时器时间--"+lastTime);
                }
            },AdDownTime,OperateTime);
            AdSpreadShowManager.getInstance().setShowData(this,spreadViewBean);
        }
        AdSpreadShowManager.getInstance().showSpreadAd();
    }
}
