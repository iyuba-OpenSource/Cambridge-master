package com.iyuba.camstory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.iyuba.camstory.API.ApiService;
import com.iyuba.camstory.API.TemporaryAccountApi;
import com.iyuba.camstory.activity.VideoActivity;
import com.iyuba.camstory.adpater.ContentViewPagerAdapter;
import com.iyuba.camstory.bean.SP;
import com.iyuba.camstory.bean.TemporaryUserJson;
import com.iyuba.camstory.fragment.ContentFragment;
import com.iyuba.camstory.fragment.ReadFragment;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.lil.ui.MocShowActivity;
import com.iyuba.camstory.listener.AddCreditsRequest;
import com.iyuba.camstory.listener.AppUpdateCallBack;
import com.iyuba.camstory.listener.PagerChangeListener;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.StudyTimeBeanRequest;
import com.iyuba.camstory.living.FindCourseActivity;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.lycam.util.NetWorkState;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.manager.VersionManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.protocol.bookstoreui.OrderDetailActivity;
import com.iyuba.camstory.service.Background;
import com.iyuba.camstory.service.NotificationService;
import com.iyuba.camstory.service.QQMessageResp;
import com.iyuba.camstory.service.UpdateServiceApi;
import com.iyuba.camstory.service.UpdateServiceResp;
import com.iyuba.camstory.setting.SettingConfig;
import com.iyuba.camstory.sqlite.mode.StudyTimeBeanNew;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.SetUserLibUtils;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.widget.CallPlatform1;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.ShareDialog;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.common.WebConstant;
import com.iyuba.configation.ConfigManagerHead;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.iyuba.module.favor.event.FavorItemEvent;
import com.iyuba.multithread.DownloadProgressListener;
import com.iyuba.multithread.FileDownloader;
import com.iyuba.multithread.MultiThreadDownloadManager;
import com.iyuba.voa.activity.setting.Constant;
import com.iyuba.voa.frame.components.ConfigManagerVOA;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.OkHttpClient;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import personal.iyuba.personalhomelibrary.data.model.HeadlineTopCategory;
import personal.iyuba.personalhomelibrary.data.model.Voa;
import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.iyuba.camstory.VipCenterGoldActivity.VIP_STATIC;
import static com.iyuba.configation.Constant.APPID;
import static com.umeng.analytics.MobclickAgent.onKillProcess;

//import com.iyuba.module.movies.event.IMovieGoVipCenterEvent;
//import com.iyuba.module.movies.ui.movie.MovieActivity;

//import cn.sharesdk.onekeyshare.OnekeyShare;
//该app存在两个数据库一个用来存储所有的文本信息.一个是用来存储保存的文本信息.所有的文章内容在bookinfo里面的aboutbook里面
//程序主入口,
//bindService()里面开启两个服务
//在initContentView();里面进行对布局现实以为vp添加Fragment显示布局
// 在checkAppUpdate();里面进行版本判断是否更新版本
@RuntimePermissions
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AppUpdateCallBack {
    private static final String TAG = MainActivity.class.getName();
    private String[] tabNames = new String[]{"入门", "一级", "二级", "三级", "四级", "五级", "六级"};
    private Intent backgroundIntent;
    private Intent notificationIntent;
    private ShareDialog shareDialog;
    private Button userhome_button;
    private ImageView user_image;
    private PagerChangeListener pagerChangeListener;
    private ContentFragment contentFragment;
    private int curPosition;
    private String version_code;
    private String appUpdateUrl;
    private ProgressBar progressBar_download;
    private Context mContext;
    private TextView username_textview;
    private String userName, userPwd;
    private boolean sense = false;
    private String loadFiledHint = "打卡加载失败";
    private CustomDialog waitingDialog;
    private DrawerLayout drawer;
    private long touchTime;

    private TextView bookTypeCommon;
    private TextView bookTypeColor;

    public enum BookType{
        newCamstory,
        newCamstoryColor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookTypeCommon = findViewById(R.id.boot_type_common);
        bookTypeColor = findViewById(R.id.boot_type_color);
        mContext = this;

        initQQMsg();
        waitingDialog = new WaittingDialog().wettingDialog(mContext);

        //设置自动登录
        if (SettingConfig.Instance().isAutoLogin()) { // 自动登录
            String[] nameAndPwd = AccountManager.getInstance()
                    .getUserNameAndPwd(mContext);
            userName = nameAndPwd[0];
            userPwd = nameAndPwd[1];

            if (userName != null && !userName.equals("")) {
                AccountManager.getInstance().login(mContext, userName, userPwd,
                        result -> {
                            ImageLoader.getInstance().displayImage(
                                    Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big",
                                    user_image, optionsBuilder);
                            SetUserLibUtils.setUserInfoLib(mContext);
                            setUserName();
                        });
            }

        }

        ImageView banner=findViewById(R.id.banner);
        //banner.setOnClickListener(v -> startActivity(new Intent(mContext,Main2Activity.class)));
        AutoLogin();
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar_download = findViewById(R.id.progressBar_update);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        drawer = findViewById(R.id.drawer_layout);


        //设置侧边界面
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);

        username_textview = headView.findViewById(R.id.username_textview);

        user_image = headView.findViewById(R.id.user_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountManager.getInstance().checkUserLogin() || AccountManager.getInstance().islinshi){
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }

                startActivity(new Intent(MainActivity.this, MeActivity.class));
            }
        });
        userhome_button = headView.findViewById(R.id.userhome_button);
        userhome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountManager.getInstance().checkUserLogin() || AccountManager.getInstance().islinshi){
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }

                startActivity(new Intent(MainActivity.this, MeActivity.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        EventBus.getDefault().register(this);
        bindService();
        bookTypeCommon.setTextColor(Color.WHITE);
        initContentView(BookType.newCamstory);
        //checkAppUpdate(); 可用功能，后台更新损坏，19-7-19注释
        //User set
        try{
            SetUserLibUtils.setUserInfoLib(mContext);
        }catch(Exception e){
            Log.e(TAG, "onCreate: "+e.getMessage());
        }

        bookTypeCommon.setOnClickListener( v->{
            bookTypeCommon.setBackground(getDrawable(R.drawable.shape_green_btn));
            bookTypeCommon.setTextColor(Color.WHITE);
            bookTypeColor.setTextColor(getResources().getColor(R.color.colorText));
            bookTypeColor.setBackground(getDrawable(R.drawable.shape_light_gray_btn));
            initContentView(BookType.newCamstory);
        });

        bookTypeColor.setOnClickListener(v->{
            bookTypeColor.setBackground(getDrawable(R.drawable.shape_green_btn));
            bookTypeColor.setTextColor(Color.WHITE);
            bookTypeCommon.setTextColor(getResources().getColor(R.color.colorText));
            bookTypeCommon.setBackground(getDrawable(R.drawable.shape_light_gray_btn));
            initContentView(BookType.newCamstoryColor);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initQQMsg() {
        ApiService apiService = new Retrofit.Builder().client(new OkHttpClient()).baseUrl(WebConstant.HTTP_SPEECH_ALL)
                .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService.class);
        apiService.fetchQQMsg(APPID).enqueue(new Callback<QQMessageResp>() {
            @Override
            public void onResponse(Call<QQMessageResp> call, Response<QQMessageResp> response) {
                if (response.isSuccessful()){
                    SP.put(mContext,"editor_qq",String.valueOf(response.body().getData().get(0).getEditor()));
                    SP.put(mContext,"technician_qq",String.valueOf(response.body().getData().get(0).getTechnician()));
                    SP.put(mContext,"manager_qq",String.valueOf(response.body().getData().get(0).getManager()));
                }
            }

            @Override
            public void onFailure(Call<QQMessageResp> call, Throwable t) {
                ToastUtil.showToast(mContext, "获取QQ信息失败");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    private void getLsUser() {
        AccountManager.getInstance().islinshi = ConfigManagerVOA.Instance(mContext).
                loadBoolean("islinshi", true);
        if (AccountManager.getInstance().islinshi) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MainActivityPermissionsDispatcher.initLocationWithPermissionCheck(MainActivity.this);
            } else {
                getUid();
            }
        }
    }

    private void getUid() {
        String lsuid = ConfigManagerVOA.Instance(mContext).loadString("lsuid");
        if ("".equals(lsuid)||"50000021".equals(lsuid)) {
            if (CheckNetWork()) {
                String deviceId = "";//Build.SERIAL
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    deviceId = Build.getSerial();//PHONE_STATE_PERMISSION
                } else {
                    deviceId = Build.SERIAL;
                }
                LogUtils.e("设备唯一标识码：" + deviceId);

                Call<TemporaryUserJson> call = RequestFactory.getTemporaryAccountApi()
                        .getTemporaryAccount(TemporaryAccountApi.PROTOCOL,deviceId,
                                TemporaryAccountApi.PLATFORM, TemporaryAccountApi.APPID,
                                TemporaryAccountApi.FORMAT, MD5.getMD5ofStr(deviceId +
                                        TemporaryAccountApi.APPID + TemporaryAccountApi.PLATFORM +
                                        getTimeDate() + "iyubaV2"));
                call.enqueue(new Callback<TemporaryUserJson>() {
                    @Override
                    public void onResponse(Call<TemporaryUserJson> call,
                                           Response<TemporaryUserJson> response) {
                        if (response.isSuccessful()) {
                            TemporaryUserJson json = response.body();
                            Log.e("json", json + "");
                            ConfigManagerVOA.Instance(mContext).putString("lsuid", json.getUid() + "");
                            AccountManager.getInstance().userId = json.getUid();
                            AccountManager.getInstance().userName = json.getUid() + "";
                            Log.e("Tag", json.getUid() + "");
                            handler.sendEmptyMessage(0x111);
                        } else {
                            AccountManager.getInstance().islinshi = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<TemporaryUserJson> call, Throwable t) {
                        AccountManager.getInstance().islinshi = false;
                        handler.sendEmptyMessage(0x112);
                    }
                });
            } else {
                AccountManager.getInstance().islinshi = false;
                ToastUtil.showToast(mContext, "请检查你的网络！");
            }

        } else {
            AccountManager.getInstance().userId = Integer.parseInt(lsuid);
            AccountManager.getInstance().userName = lsuid;
            AccountManager.getInstance().isVip(mContext);
        }


    }

    private void bindService() {
        //后台控制video播放，关停，以及下一首的，暂时没看懂，貌似是双进程保护，使service不被杀死
        backgroundIntent = new Intent(MainActivity.this, Background.class);
        notificationIntent = new Intent(MainActivity.this, NotificationService.class);
        startService(backgroundIntent);
        startService(notificationIntent);
    }

    public void setPagerChangeListener(PagerChangeListener pagerChangeListener) {
        this.pagerChangeListener = pagerChangeListener;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        setUserName();//刷新用户信息
    }


    private void setUserName(){
        if (AccountManager.getInstance().checkUserLogin()) {
            Log.e("onResume", "true");
            //头像
            ImageLoader.getInstance().displayImage(
                    Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big",
                    user_image, optionsBuilder);
            Log.e("头像路径", Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big");

            //用户名
            username_textview.setText(AccountManager.getInstance().userName);
        } else if (AccountManager.getInstance().islinshi) {
            user_image.setImageResource(R.mipmap.defaultavatar);
            username_textview.setText(AccountManager.getInstance().userName);
        } else {
            Log.e("onResume", "false");
            user_image.setImageResource(R.mipmap.defaultavatar);
            username_textview.setText("未登录");
        }
    }

    private void initContentView(BookType bookType) {

        //创建出来viewpager的adapter,由于需要向里面传入一个Fragment所以传入了一个manager
        ContentViewPagerAdapter contentViewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager(),tabNames,bookType);
        //找到tablayout
        TabLayout tabs = findViewById(R.id.tabs);
        //找到viewpager
        ViewPager content_viewpager = findViewById(R.id.content_viewpager);
        //为tab指示器设置标签
        for (int i = 0; i < tabNames.length; i++) {
            tabs.addTab(tabs.newTab().setText(tabNames[i]));
        }
        //为viwpager设置adapter
        content_viewpager.setAdapter(contentViewPagerAdapter);
        //把viewpager添加到tablayout里面
        tabs.setupWithViewPager(content_viewpager);
        //为viewpager设置滑动监听事件,但是没有用到该监听
        content_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置初始显示vp为第一个vp
        content_viewpager.setCurrentItem(0);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long currentTime = System.currentTimeMillis();
            long waitTime = 2000;
            if((currentTime - touchTime) >= waitTime) {
                //让Toast的显示时间和等待时间相同
                Toast.makeText(this, "再按一次退出", (int) waitTime).show();
                touchTime = currentTime;
            }else {
//                onKillProcess(this);
                sendBroadcast(new Intent().setAction("finish"));
                 MainActivity.this.finish();
//                System.exit(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getApplication().getPackageName().equals("com.iyuba.camstory")) {
            MenuItem item=menu.findItem(R.id.nav_suggest);
            if (item!=null) item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId()==R.id.nav_about) {
           if (getApplication().getPackageName().equals("com.iyuba.englishnovels")) {
               item.setTitle("关于爱语言");
           }
       }
        return super.onOptionsItemSelected(item);
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private String date = sdf.format(new Date());

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //为侧边栏设置点击监听
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        if(id==R.id.vip_center){
            if (AccountManager.getInstance().islinshi) {
                Toast.makeText(mContext, "请登录账号", Toast.LENGTH_LONG).show();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(mContext, VipCenterGoldActivity.class));//VipCenterActivity
            }
        }else if (id == R.id.sign_in) {
            if (AccountManager.getInstance().islinshi) {
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
            } else if (AccountManager.getInstance().loginStatus == 0) {
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
            } else {
                signGo();
            }
        } else if (id == R.id.update_service) {
            updateService();
        } else if (id == R.id.show_tv) {
            //User set
            startActivity(new Intent(mContext, VideoActivity.class));
            if(StoryDataManager.Instance().getCmPlayer().isPlaying()){
                StoryDataManager.Instance().getCmPlayer().pause();
            }
        } else if (id == R.id.study) {
            //微课
            MocShowActivity.start(this);
            //暂停播放
            if(StoryDataManager.Instance().getCmPlayer().isPlaying()){
                StoryDataManager.Instance().getCmPlayer().pause();
            }
        } else if (id == R.id.rl_discover_media_book) {
            //如果点击意见就不开启意见界面
            intent.setClass(this, BookMarketActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            intent.setClass(this, Feedback.class);
            startActivity(intent);

        }
        else if (id == R.id.study_ranking) {
            if (AccountManager.getInstance().loginStatus == 0) {
                Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
            } else {
                intent.setClass(this, RankActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.rl_discover_gift) {
            //如果点击意见就不开启意见界面
            if (AccountManager.getInstance().checkUserLogin() || AccountManager.getInstance().islinshi) {
                Intent intent2 = new Intent();
                intent2.setClass(mContext, WebActivity.class);

                String url = "http://vip."+Constant.IYBHttpHead+"/mycode.jsp?"
                        + "uid=" + AccountManager.getInstance().userId
                        + "&appid=" + TemporaryAccountApi.APPID
                        + "&sign=" + MD5.getMD5ofStr(AccountManager.getInstance().userId + "iyuba" +
                        TemporaryAccountApi.APPID + date);

                intent2.putExtra("url", url);

                intent2.putExtra("title", mContext.getString(R.string.discover_gift));
                startActivity(intent2);
            } else {
                Intent intent2 = new Intent();
                intent2.setClass(mContext, LoginActivity.class);
                startActivity(intent2);
            }

        } else if (id == R.id.rl_discover_order_detail) {
            intent.setClass(this, OrderDetailActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_living) {
            //如果点击的是直播就开启直播界面
            intent.setClass(this, FindCourseActivity.class);
            startActivity(intent);

        }   else if (id == R.id.nav_about) {
            intent.setClass(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_suggest) {
            String text = "讲真、你与学霸只有一个"
                    + "剑桥英语馆" + "的距离 ";

            String imagePath = "http://static2."+Constant.IYBHttpHead+"/camstory/images/camstory.png";
            LogUtils.e("imagePath", imagePath);
            String url = Constant.getAppshareurl();
            String title = Constant.getAppname();
            showShare(text, imagePath, url, title);
        }

        return true;
    }

    private void updateService() {
        UpdateServiceApi updateServiceApi = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://111.198.52.105:8085").client(new OkHttpClient()).build()
                .create(UpdateServiceApi.class);
        updateServiceApi.requestNewService(APPID, WebConstant.IYBHttpHead,WebConstant.IYBHttpHead2).enqueue(new Callback<UpdateServiceResp>() {
            @Override
            public void onResponse(retrofit2.Call<UpdateServiceResp> call, Response<UpdateServiceResp> response) {
                UpdateServiceResp body = response.body();
                if (body.getUpdateflg() == 1){
                    ConfigManager.Instance().putString("short1",body.getShort1());
                    ConfigManager.Instance().putString("short2",body.getShort2());
                    ToastUtil.showToast(mContext,"已更新至最新服务");
                }else{
                    ToastUtil.showToast(mContext,"当前已是最新服务");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UpdateServiceResp> call, Throwable t) {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent event) {
        BasicDLPart item = event.items.get(event.position);
        jumpToCorrectDLActivityByCate(mContext, item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {
       Intent intent =new Intent(mContext,VipCenterGoldActivity.class);
       intent.putExtra(VIP_STATIC,2);
       startActivity(intent);
    }


    public void jumpToCorrectDLActivityByCate(Context context, BasicDLPart item) {

        boolean isvip = false;

        if (ConfigManagerHead.Instance().loadInt("isvip") > 0) {
            isvip = true;
        }

        switch (item.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(context,item.getCategoryName(), item.getTitle()
                        , item.getTitleCn(),item.getPic(), item.getType(), item.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "japanvideos":
            case "topvideos":
                startActivity(VideoContentActivity.getIntent2Me(context,
                        item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(),item.getPic(), item.getType(), item.getId()));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FavorItemEvent event) {
        BasicFavorPart item = event.items.get(event.position);
        jumpToCorrectFavorActivityByCate(mContext, item);
    }

    public void jumpToCorrectFavorActivityByCate(Context context, BasicFavorPart item) {
        int mUserId = AccountManager.getInstance().getUId();
        Log.e("headline-source", "" + item.toString());

        boolean isvip = false;

        if (ConfigManagerHead.Instance().loadInt("isvip") > 0) {
            isvip = true;
        }

        switch (item.getType()) {
            case "news":
                startActivity(TextContentActivity.getIntent2Me(context,
                        item.getId(),item.getTitle(), item.getTitleCn(), item.getType(),
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource()));
                break;
            case "headnews":
                startActivity(TextContentActivity.getIntent2Me(context,
                        item.getId(),item.getTitle(), item.getTitleCn(), "news",
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource()));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(),item.getPic(), item.getType(), item.getId(), item.getSound()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "japanvideos":
            case "bbcwordvideo":
            case "topvideos":
                startActivity(VideoContentActivity.getIntent2Me(context,
                        item.getCategoryName(), item.getTitle(), item.getTitleCn(),
                        item.getPic(), item.getType(), item.getId()));
                break;
            case "series":
//                startActivity(new Intent( context, OneMvSerisesView.class)
//                        .putExtra("serisesid",item.getOther1())
//                        .putExtra("voaid",item.getId()));
                Log.v("========serisesid=", item.getOther1()+"NULL");
                break;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void goVipCenter(IMovieGoVipCenterEvent event){
//        startActivity(new Intent(mContext, VipCenterActivity.class));
//    }


    //分享方法
    private void showShare(String text, String imagePath, String url, String title) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imagePath);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(Constant.getAppname());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(Constant.getAppname());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int arg1, Throwable arg2) {
                CustomToast.showToast(MainActivity.this, "分享失败", 1000);
            }

            @Override
            public void onComplete(Platform platform, int arg1,
                                   HashMap<String, Object> arg2) {
                CustomToast.showToast(MainActivity.this, "分享成功", 1000);


            }

            @Override
            public void onCancel(Platform platform, int arg1) {
                CustomToast.showToast(MainActivity.this, "分享已取消", 1000);
            }
        });

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 检查新版本
     */
    public void checkAppUpdate() {
//		Log.e("checkupdate","checkupdate-----------------------------");
        //检查版本更新
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

    }

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
                (dialog, which) -> progressBar_download.setVisibility(View.INVISIBLE));
        alert.show();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    progressBar_download.setVisibility(View.VISIBLE);
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
                                    Log.e("download", Constant.getEnvir() + Constant.getAppfile() + "/"
                                            + "/appUpdate/" + Constant.getAppfile() + "_"
                                            + version_code + ".apk");
                                }
                            });
                    break;
                case 1:
                    CustomToast.showToast(getBaseContext(), Constant.getAppname()
                            + getString(R.string.about_update_isnew), 1000);
                    break;
                case 3:
                    CustomToast.showToast(getBaseContext(), R.string.about_error, 1000);
                    break;
                case 0x111:
                    username_textview.setText(AccountManager.getInstance().userName);
                    break;
                case 0x112:
                    ToastUtil.showToast(mContext, "服务器异常");
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
//                    appUpdateBtn.setEnabled(true);
                    progressBar_download.setVisibility(View.INVISIBLE);
                    FailOpera.getInstance(MainActivity.this).openFile(mContext,
                            savePathFullName);
                }
            });
        }

    };



    //弹出分享对话框
    private void showShareDialog(final String text, final String imagePath, final String url, final String title) {
        shareDialog = new ShareDialog(this);
        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });
        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, Object> item = (HashMap<String, Object>) parent
                        .getItemAtPosition(position);
                shareDialog.dismiss();
                // 调用分享平台
//				if (StoryDataManager.Instance().currBookInfo != null) {
                new CallPlatform1(item,
                        imagePath, text, url, title, pal);
//				} else {
//					Log.v(TAG, "The fucking currentBookInfo is null");
//				}
            }
        });
    }

    private PlatformActionListener pal = new PlatformActionListener() {

        @Override
        public void onError(Platform platform, int arg1, Throwable arg2) {
            CustomToast.showToast(MainActivity.this, "分享失败", 1000);
        }

        @Override
        public void onComplete(Platform platform, int arg1,
                               HashMap<String, Object> arg2) {

            int srid = 19;
            String name = platform.getName();
            if (name.equals(QQ.NAME) || name.equals(Wechat.NAME)
                    || name.equals(WechatFavorite.NAME)) {
                srid = 41;
            } else if (name.equals(QZone.NAME)
                    || name.equals(WechatMoments.NAME)
                    || name.equals(SinaWeibo.NAME)) {
                srid = 19;
            }
            if (AccountManager.getInstance().checkUserLogin()) {
                RequestCallBack rc = new RequestCallBack() {

                    @Override
                    public void requestResult(Request result) {
                        AddCreditsRequest rq = (AddCreditsRequest) result;
                        if (rq.isShareFirstlySuccess()) {
                            String msg = "分享成功，增加了" + rq.addCredit + "积分，共有"
                                    + rq.totalCredit + "积分";
                            CustomToast.showToast(MainActivity.this, msg, 1500);
                        } else if (rq.isShareRepeatlySuccess()) {
                            CustomToast.showToast(MainActivity.this, "分享成功", 1500);
                        }
                    }
                };
                int uid = AccountManager.getInstance().userId;
                // AddCreditsRequest rq = new AddCreditsRequest(uid,
                // sharedVoa.voaid,
                // srid, rc);
                // CrashApplication.getInstance().getQueue().add(rq);
            }
        }

        @Override
        public void onCancel(Platform platform, int arg1) {
            CustomToast.showToast(MainActivity.this, "分享已取消", 1000);
        }
    };

    public long getTimeDate() {
        Date date = new Date();
        long unixTimestamp = date.getTime() / 1000 + 3600 * 8; //东八区;
        long days = unixTimestamp / 86400;
        return days;
    }

    private boolean CheckNetWork() {
        return NetWorkState.isConnectingToInternet() && NetWorkState.getAPNType() != 1;

    }

    private void AutoLogin() {
        if (AccountManager.getInstance().checkUserLogin()&&SettingConfig.Instance().isAutoLogin()) { // 自动登录
            String[] nameAndPwd = AccountManager.getInstance().getUserNameAndPwd(mContext);
            userName = nameAndPwd[0];
            userPwd = nameAndPwd[1];
            if (NetWorkState.isConnectingToInternet()) {
                if (!TextUtils.isEmpty(userName)) {
                    AccountManager.getInstance().login(mContext, userName, userPwd, null);
                    Log.e("lalala", "laiguo");
                    SetUserLibUtils.setUserInfoLib(mContext);
                }
            } else if (userName != null && !userName.equals("")) {
                AccountManager.getInstance().setLoginState(1);
            }
        }
    }

    private DisplayImageOptions optionsBuilder = new DisplayImageOptions.Builder()
            .cacheInMemory(false).cacheOnDisk(false).displayer(new FadeInBitmapDisplayer(800))
            .delayBeforeLoading(400).showImageOnFail(R.mipmap.defaultavatar).build();


    /**
     * 打卡判断
     */
    private void signGo() {

        if (AccountManager.getInstance().checkUserLogin()) {
            waitingDialog.show();

            String uid = AccountManager.getInstance().userId + "";

            final String url = String.format(Locale.CHINA,
                    "http://daxue."+Constant.IYBHttpHead+"/ecollege/getMyTime.jsp?uid=%s&day=%s&flg=1", uid, getDays());

            StudyTimeBeanRequest studyTimeBeanRequest = new StudyTimeBeanRequest(url, result -> {
                StudyTimeBeanRequest sr = (StudyTimeBeanRequest) result;
                try {
                    if (null != waitingDialog) {
                        if (waitingDialog.isShowing()) {
                            waitingDialog.dismiss();
                        }
                    }
                    Log.e("打卡数据", sr.json.toString());
                    final StudyTimeBeanNew bean = new Gson().fromJson(sr.json.toString(), StudyTimeBeanNew.class);

                    if ("1".equals(bean.getResult())) {
                        final int time = Integer.parseInt(bean.getTotalTime());
                        int signStudyTime = 3 * 60;
                        if (time < signStudyTime && !ReadFragment.flag) {
                            toast(String.format(Locale.CHINA, "当前已学习%d秒，\n满%d分钟可打卡", time, signStudyTime / 60));
                        } else {
                            //打卡
                            Intent intent = new Intent(MainActivity.this, SignActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        toast(loadFiledHint);
                    }
                } catch (Exception e) {
                    Log.e("异常", e.toString());
                    e.printStackTrace();
                    toast(loadFiledHint + "！！");
                }
            });


            CrashApplication.getInstance().getQueue().add(studyTimeBeanRequest);

        } else {
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 101);
        }

    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private long getDays() {
        //东八区;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(1970, 0, 1, 0, 0, 0);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        long intervalMilli = now.getTimeInMillis() - cal.getTimeInMillis();
        long xcts = intervalMilli / (24 * 60 * 60 * 1000);
        return xcts;
    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public void initLocation() {
        getUid();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public void locationDenied() {

        CustomToast.showToast(MainActivity.this, "权限开通才可以正常使用app，请到系统设置中开启", 3000);
        getUid();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArtDataSkipEvent event) {
        Voa voa = event.voa;
        //文章跳转
        switch (event.type) {
            case "news":
                HeadlineTopCategory topCategory = event.headline;
                startActivity(TextContentActivity.getIntent2Me(mContext,
                        topCategory.id, topCategory.Title, topCategory.TitleCn, topCategory.type
                        , topCategory.Category, topCategory.CreatTime, topCategory.getPic(), topCategory.Source));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivityNew.getIntent2Me(mContext,
                        voa.categoryString, voa.title, voa.title_cn,
                        voa.getPic(),event.type, String.valueOf(voa.voaid), voa.sound));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivityNew.getIntent2Me(mContext,
                        voa.categoryString, voa.title, voa.title_cn, voa.getPic(),
                        event.type, String.valueOf(voa.voaid), voa.sound));//voa.getVipAudioUrl()
                break;
        }
    }


}
