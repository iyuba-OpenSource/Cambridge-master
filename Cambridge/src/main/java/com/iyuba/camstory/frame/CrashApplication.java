package com.iyuba.camstory.frame;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.camstory.BuildConfig;
import com.iyuba.camstory.R;
import com.iyuba.camstory.SplashActivity;
import com.iyuba.camstory.lil.OAIDNewHelper;
import com.iyuba.camstory.lil.util.DateUtil;
import com.iyuba.camstory.lil.util.ResUtil;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.lycam.manager.ConstantManager;
import com.iyuba.camstory.lycam.manager.RuntimeManager;
import com.iyuba.camstory.sqlite.ImportDatabase;
import com.iyuba.camstory.utils.AppFrontBackHelper;
import com.iyuba.camstory.utils.ShareHelper;
import com.iyuba.common.WebConstant;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.imooclib.IMooc;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.privacy.IPrivacy;
import com.iyuba.voa.activity.setting.Constant;
import com.iyuba.voa.activity.setting.SettingConfig;
import com.iyuba.voa.frame.components.ConfigManagerVOA;
import com.iyuba.widget.unipicker.IUniversityPicker;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.OAIDHelper;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;

import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import personal.iyuba.personalhomelibrary.PersonalHome;
import tv.lycam.gift.runner.EngineRunner;
import tv.lycam.mqtt.Mqtt;
import tv.lycam.mqtt.MqttService;

public class CrashApplication extends Application {
    private static final String TAG = CrashApplication.class.getSimpleName();

    private static CrashApplication mInstance = null;
    public boolean m_bKeyRight = true;
    // public static String ini="012347";
    private ExecutorService costTimeExecutors;
    private RequestQueue queue;
    private int lastVersion;
    private int currentVersion;
    // 显示参数
    private DisplayMetrics displayMetrics;
    // Universal使用的全局设置
    private Builder defaultDesplayImageOptionsBuilder;

    // !!-----------just temp----------------!!
    // Advertisement Infostream temp variable
    public String firstLevel = "2";
    public String picUrl = "";
    public String url = "";
    public String description = "";
    // !!-----------just temp----------------!!

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //设置oaid的预加载操作
        OAIDNewHelper.loadLibrary();
        //初始化资源类
        ResUtil.getInstance().setApplication(this);

        costTimeExecutors = Executors.newFixedThreadPool(2);
        queue = Volley.newRequestQueue(this);

        RuntimeManager.setApplication(this);
        RuntimeManager.setApplicationContext(this);
        com.iyuba.configation.RuntimeManager.setApplicationContext(getApplicationContext());
        com.iyuba.configation.RuntimeManager.setApplication(this);

        AppFrontBackHelper helper = AppFrontBackHelper.getInstance();
        helper.register(CrashApplication.this);

        //设置广告屏蔽时间
        try {
            Date compileDate = new SimpleDateFormat(DateUtil.YMDHMS, Locale.CHINA).parse(BuildConfig.COMPILE_DATETIME);
            //华为需要屏蔽广告
            Date adBlockDate = new Date(compileDate.getTime()-7*24*60*60*1000);
            if (ChannelReaderUtil.getChannel(this).toLowerCase().equals("huawei")){
                adBlockDate = new Date(compileDate.getTime()+365*24*60*60*1000);
            }
            AdBlocker.getInstance().setBlockStartDate(adBlockDate);
        }catch (Exception e){

        }
    }

    public void initLazy(){
        //友盟预初始化
        String channel = ChannelReaderUtil.getChannel(this);
        UMConfigure.preInit(mInstance,"538d5e0756240beaab073242",channel);

        if (!ConfigManager.Instance().loadString("short1").equals("")){
            WebConstant.IYBHttpHead = ConfigManager.Instance().loadString("short1");
            WebConstant.IYBHttpHead2  = ConfigManager.Instance().loadString("short2");
        }

        // TODO: 2022/6/23 调整初始化位置(需要时可以放开)
        IPrivacy.init(getApplicationContext(), WebConstant.HTTP_SPEECH_ALL+"/api/protocoluse666.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=爱语吧",
                WebConstant.HTTP_SPEECH_ALL+"/api/protocolpri.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=1");
        IHeadline.init(getApplicationContext(), Constant.getAppid(), Constant.TOPICID,true);
        IHeadline.setEnableGoStore(false);
        IHeadline.setEnableShare(true);
        //开启视频的配音功能
        IHeadline.setEnableSmallVideoTalk(true);
        IHeadline.setExtraMseUrl(WebConstant.HTTP_SPEECH_ALL+"/test/ai/");
        IHeadline.setExtraMergeAudioUrl(WebConstant.HTTP_SPEECH_ALL+"/test/merge/");
//        IHeadline.setTitleHolderType(HolderType.SMALL);
        //MobclickAgent.openActivityDurationTrack(false);
        if (!BuildConfig.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            // 注册crashHandler
            crashHandler.init(getApplicationContext());
            // 发送以前没发送的报告(可选)
            crashHandler.sendPreviousReportsToServer();
        }
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
        initConfig();
        IUniversityPicker.init(getApplicationContext());
        initUniversalImageLoader();
        prepareForApp(getApplicationContext());
        if (ConfigManager.Instance().loadBoolean("PrivacyDialog")) {
            ShareHelper.init(getApplicationContext());
            initLiving();
            /**
             * 通用模块
             */
            //微课
            DLManager.init(getApplicationContext(), 8);
            IMooc.init(getApplicationContext(), Constant.getAppid(), Constant.TOPICID);
            //看一看
            BasicFavorDBManager.init(getApplicationContext());
//            MobSDK.init(getApplicationContext());
            //IMovies.init(getApplicationContext(),Constant.getAppid(),Constant.TOPICID);
            //视频模块
            BasicDLDBManager.init(getApplicationContext());
            BasicFavorDBManager.init(getApplicationContext());

            BasicFavorInfoHelper.init(getApplicationContext());
            //个人中心模块
            PersonalHome.init(getApplicationContext());

            //有道广告初始化
            // TODO: 2025/1/14 因为有道4.3.2的问题，暂时关闭手动初始化 
            //先初始化
            /*OAIDHelper.getInstance().init(getApplicationContext());*/
            //oaid2.0的升级操作（证书需要每年进行更新，2022-12-14）
            /*OAIDNewHelper oaidNewHelper = new OAIDNewHelper(new OAIDNewHelper.AppIdsUpdater() {
                @Override
                public void onIdData(boolean isSupported, boolean isLimited, String oaid, String vaid, String aaid) {
                    if (isSupported && !isLimited){
                        OAIDHelper.getInstance().setOAID(oaid);
                    }
                }
            },"msaoaidsec","com.iyuba.camstor.cert.pem");
            oaidNewHelper.getDeviceIds(getApplicationContext(),true,false,false);*/
            //设置有道sdk的一些功能
            YouDaoAd.getNativeDownloadOptions().setConfirmDialogEnabled(true);
            YouDaoAd.getYouDaoOptions().setAppListEnabled(false);
            YouDaoAd.getYouDaoOptions().setPositionEnabled(false);
            YouDaoAd.getYouDaoOptions().setSdkDownloadApkEnabled(true);
            YouDaoAd.getYouDaoOptions().setDeviceParamsEnabled(false);
            YouDaoAd.getYouDaoOptions().setWifiEnabled(false);
            YouDaoAd.getYouDaoOptions().setCanObtainAndroidId(false);
            YoudaoSDK.init(getApplicationContext());
        }

        //爱语吧广告初始化
        YdConfig.getInstance().init(getApplicationContext(), com.iyuba.configation.Constant.APPID);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initLiving() {
        RuntimeManager.setApplicationContext(this);
        RuntimeManager.setApplication(this);
        com.iyuba.configation.RuntimeManager.setApplicationContext(getApplicationContext());
        com.iyuba.configation.RuntimeManager.setApplication(this);
        EngineRunner.init(this, null);
        Mqtt.initialize(this, MqttService.class);
        x.Ext.init(this);
    }

    private void prepareForApp(Context context) {
        RuntimeManager.setApplication(this);
        RuntimeManager.setApplicationContext(context);
        com.iyuba.configation.RuntimeManager.setApplicationContext(context);
        com.iyuba.configation.RuntimeManager.setApplication(this);
        File file = new File(ConstantManager.getInstance().getEnvir());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void initConfig() {
        // 设置根目录路径
        try {
            if (getExternalFilesDir(null) != null) {
                Log.e(TAG, "getExternalFilesDir(null) != null");
                Constant.setEnvir(getExternalFilesDir(null).toString());
                Constant.setPicAddr(getExternalCacheDir().toString());
                Log.e(TAG, "Constant.envir : " + getExternalFilesDir(null).toString());
                Log.e(TAG, "Constant.picAddr : " + getExternalCacheDir().toString());
            } else {
                Log.e(TAG, "getExternalFilesDir(null) == null");
                File file;
                file = new File("/data/data/" + getPackageName() + "/files/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                Constant.setEnvir(file.getAbsolutePath());
                File file2 = new File("/data/data/" + getPackageName() + "/cache/");
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                Constant.setPicAddr(file2.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            File file;
            file = new File("/data/data/" + getPackageName() + "/files/");
            if (!file.exists()) {
                file.mkdirs();
            }
            Constant.setEnvir(file.getAbsolutePath());
            File file2 = new File("/data/data/" + getPackageName() + "/cache/");
            if (!file2.exists()) {
                file2.mkdirs();
            }

            Constant.setPicAddr(file2.getAbsolutePath());
        }

        // 初始化各路径
        Constant.initConstant(this);

        // 初始化数据库
        ImportDatabase db = new ImportDatabase(getApplicationContext());
        //升级数据库
        db.setVersion(this, 4, 2);//更新数据库！！！！
        db.copyDatabase(ImportDatabase.DB_PATH + File.separator + ImportDatabase.DB_NAME);

        // 判断版本
        try {
            lastVersion = ConfigManagerVOA.Instance(this).loadInt("VERSION_CODE");
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            lastVersion = 0;
            currentVersion = 1;
            e.printStackTrace();
        }

        // 新装
        if (lastVersion == 0) {
            SettingConfig.Instance(this).setScreenLit(true);
            SettingConfig.Instance(this).setSyncho(true);
            //SettingConfig.Instance(this).setAutoLogin(false);
            ConfigManagerVOA.Instance(this).putString("currUserAmount", "0");
            ConfigManagerVOA.Instance(this).putBoolean("showChinese", true);
            ConfigManagerVOA.Instance(this).putInt("lately", 1500);
            if (Constant.getAppid().equals("215")
                    || Constant.getAppid().equals("221")) {
                ConfigManagerVOA.Instance(this).putInt("lately", 130);
            } else if (Constant.getAppid().equals("212")
                    || Constant.getAppid().equals("217")) {
                ConfigManagerVOA.Instance(this).putInt("lately", 2301);
            }
            ConfigManagerVOA.Instance(this).putBoolean("firstuse", true);
            ConfigManagerVOA.Instance(this).putInt("textColor", 0xff2983c1);
            Constant.setTextColor(0xff2983c1);
            ConfigManagerVOA.Instance(this).putInt("textSize", 18);
            Constant.setTextSize(18);
            ConfigManagerVOA.Instance(this).putInt("backColor", 0xffe3e4e6);
            Constant.setBackgroundColor(0xffe3e4e6);
            ConfigManagerVOA.Instance(this).putInt("mode", 1);
            ConfigManagerVOA.Instance(this).putBoolean("autoplay", false);
            ConfigManagerVOA.Instance(this).putBoolean("autostop", true);
            ConfigManagerVOA.Instance(this).putString("applanguage", "zh");
            ConfigManagerVOA.Instance(this).putBoolean("autonotice", false);// 后台自动提示新文章
            ConfigManagerVOA.Instance(this).putBoolean("bell_is_on", false);// VOA闹钟是否开启
            ConfigManagerVOA.Instance(this).putBoolean("bankuser", false);
            ConfigManagerVOA.Instance(this).putInt("banknumb", 0);
            ConfigManagerVOA.Instance(this).putBoolean("highspeed_download", false);
            ConfigManagerVOA.Instance(this).putString("media_saving_path", Constant.getEnvir() + "/audio/");
        }
    }

    // universal-imageloader的初始化
    private void initUniversalImageLoader() {
        // 默认的displayoptions
        defaultDesplayImageOptionsBuilder = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(800)).delayBeforeLoading(400);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultDesplayImageOptionsBuilder.build())
                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
                // .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    // 可以在第一个ACTIVITY设置，显示值
    public void setDisplayMetrics(Activity activity) {
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    // 获取屏幕宽
    public int getWindowWidth() {
        return displayMetrics.widthPixels;
    }

    // 获取屏幕高
    public int getWindowHeight() {
        return displayMetrics.heightPixels;
    }

    // 该版本的第一次启动
    public boolean isFirstBegin() {
        return currentVersion > ConfigManagerVOA.Instance(this)
                .loadInt("VERSION_CODE");
    }

    // 新装用户（包括卸载后重装）第一次启动
    public boolean isFirstInstall() {
        return lastVersion == 0;
    }

    // 可以在非activity(如工具类)中获取context
    public static CrashApplication getInstance() {
        return mInstance;
    }

    public Builder getDefaultDisplayImageOptionsBuilder() {
        return defaultDesplayImageOptionsBuilder;
    }

    // 全局线程池，用于耗时操作
    public ExecutorService getCostTimeExecutors() {
        return costTimeExecutors;
    }

    // 全局volley请求队列队列
    public RequestQueue getQueue() {
        return queue;
    }

    private long startTime, endTime;
    private int mFinalCount;
    private class SwitchBackgroundCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mFinalCount++;
            //如果mFinalCount ==1，说明是从后台到前台
            if (mFinalCount == 1) {
                //说明从后台回到了前台
                endTime = new Date().getTime();
                long timeload = endTime - startTime;
                if (startTime != 0 && timeload / 1000 >= 180) {
                    Intent intent = new Intent(activity, SplashActivity.class);
                    intent.putExtra("onActivityStarted", true);
                    activity.startActivity(intent);
                }
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            mFinalCount--;
            //如果mFinalCount ==0，说明是前台到后台
            if (mFinalCount == 0) {
                //说明从前台回到了后台
                startTime = new Date().getTime();
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}