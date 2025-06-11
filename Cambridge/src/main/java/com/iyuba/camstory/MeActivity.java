package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.facebook.stetho.common.LogUtil;
import com.iyuba.camstory.bean.BrandUtil;
import com.iyuba.camstory.bean.SP;
import com.iyuba.camstory.bean.Util;
import com.iyuba.camstory.bean.living.LoginResponse;
import com.iyuba.camstory.bean.living.LoginSuccessEvent;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.CommonCallBack;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestNewInfo;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.NightModeManager;
import com.iyuba.camstory.manager.SocialDataManager;
import com.iyuba.camstory.manager.UserInfoManager;
import com.iyuba.camstory.protocol.bookstoreui.OrderDetailActivity;
import com.iyuba.camstory.setting.ConfigManagerSet;
import com.iyuba.camstory.setting.SettingConfig;
import com.iyuba.camstory.utils.JsonOrXmlConverterFactory;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.NetStatusUtil;
import com.iyuba.camstory.utils.PwdInputDialog;
import com.iyuba.camstory.utils.ScreenUtils;
import com.iyuba.camstory.utils.SetUserLibUtils;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.utils.logout.ClearUserResponse;
import com.iyuba.camstory.utils.logout.TeacherApiStores;
import com.iyuba.camstory.widget.CircularImageView;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.common.WebConstant;
import com.iyuba.imooclib.ui.web.Web;
import com.iyuba.module.favor.ui.BasicFavorActivity;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import personal.iyuba.personalhomelibrary.event.UserNameChangeEvent;
import personal.iyuba.personalhomelibrary.event.UserPhotoChangeEvent;
import personal.iyuba.personalhomelibrary.ui.groupChat.GroupChatManageActivity;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;
import personal.iyuba.personalhomelibrary.ui.home.SimpleListActivity;
import personal.iyuba.personalhomelibrary.ui.message.MessageActivity;
import personal.iyuba.personalhomelibrary.ui.search.SearchGroupActivity;
import personal.iyuba.personalhomelibrary.ui.search.SearchUserActivity;
import personal.iyuba.personalhomelibrary.utils.ToastFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.iyuba.camstory.lycam.manager.RuntimeManager.getContext;

public class MeActivity extends AppCompatActivity implements OnClickListener {
//    private static final int FOLLOWING = 1;
    private WindowManager mWindowManager;
    private ApiComService mApiComService;
    private NightModeManager nightModeManager;

    private Context mContext;

    private CircularImageView userCircularImageView;
    private ImageView vipImageView, qqButton;
    private TextView attentionTextView, funTextView, creditsTextView, notifitionTextView,
            usernameTextView, moneyTextView;
    private View attentionView, fansView, creditsView, notifitionView, downloadView, historyView,privacyPolicy,
            collectView, messageView, gradeView, gradeshoppingView, collectTvView, meDownloadView;
    private RelativeLayout mRlOrder,mRlFeedBack,mRlMyWord;
    private View userInfoView, unloginView;
    private LinearLayout LlMoney;
    private Button loginButton, logoutButton, backButton,exitButton;
    private ImageView newLetterView;

    private RelativeLayout meGroup;
    private RelativeLayout meSearchGroup;
    private RelativeLayout meSearchFriend;
    private RelativeLayout meOfficialPhone;

    private DisplayImageOptions optionsBuilder = new DisplayImageOptions.Builder()
            .cacheInMemory(false).cacheOnDisk(false).displayer(new FadeInBitmapDisplayer(800))
            .delayBeforeLoading(400).showImageOnFail(R.mipmap.defaultavatar).build();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private String date = sdf.format(new Date());

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                newLetterView.setVisibility(View.VISIBLE);
            } else if (msg.what == 1) {
                newLetterView.setVisibility(View.GONE);
            }
        }
    };
    private String editor_qq;
    private String technician_qq;
    private String manager_qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        mContext = this;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        nightModeManager = new NightModeManager(mWindowManager, mContext);
        //initActionbar();
        findView();
        initOnClickListener();

        //加载头像和用户名
        usernameTextView.setText(AccountManager.getInstance().userName);
        ImageLoader.getInstance().displayImage(
                Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big",
                userCircularImageView, optionsBuilder);

        EventBus.getDefault().register(this);
        editor_qq = (String) SP.get(mContext,"editor_qq","1");
        technician_qq = (String) SP.get(mContext,"technician_qq","2");
        manager_qq = (String) SP.get(mContext,"manager_qq","3");

    }

    private void initOnClickListener() {
        attentionView.setOnClickListener(this);
        fansView.setOnClickListener(this);
        creditsView.setOnClickListener(this);
        notifitionView.setOnClickListener(this);
        LlMoney.setOnClickListener(this);
        userCircularImageView.setOnClickListener(this);
        downloadView.setOnClickListener(this);
        historyView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        collectView.setOnClickListener(this);
        collectTvView.setOnClickListener(this);
        gradeView.setOnClickListener(this);
        messageView.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        gradeshoppingView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        qqButton.setOnClickListener(this);
        meDownloadView.setOnClickListener(this);

        mRlOrder.setOnClickListener(this);
        mRlFeedBack.setOnClickListener(this);
        mRlMyWord.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        meSearchFriend.setOnClickListener(this);
        meSearchGroup.setOnClickListener(this);
        meGroup.setOnClickListener(this);
        meOfficialPhone.setOnClickListener(this);

    }

    private void findView() {
        meGroup = findViewById(R.id.me_group);
        meSearchGroup = findViewById(R.id.me_search_group);
        meSearchFriend = findViewById(R.id.me_search_frinend);
        meOfficialPhone = findViewById(R.id.me_official_phone);
        userCircularImageView = findViewById(R.id.user_image);
        attentionTextView = findViewById(R.id.attention_numb_textview);
        funTextView = findViewById(R.id.funs_numb_textview);
        creditsTextView = findViewById(R.id.credits_numb_textview);
        notifitionTextView = findViewById(R.id.notifition_numb_textview);
        attentionView = findViewById(R.id.linearLayout1);
        fansView = findViewById(R.id.linearLayout2);
        creditsView = findViewById(R.id.linearLayout3);
        notifitionView = findViewById(R.id.linearLayout4);
        LlMoney = findViewById(R.id.linearLayout5);
        usernameTextView = findViewById(R.id.username_textview);
        vipImageView = findViewById(R.id.vip_imageview);
        downloadView = findViewById(R.id.me_local);
        historyView = findViewById(R.id.me_history);
        userInfoView = findViewById(R.id.userInfo_container);
        unloginView = findViewById(R.id.unlogin_layout);
        loginButton = findViewById(R.id.login_button);
        logoutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.button_back);
        qqButton = findViewById(R.id.qq);
        collectView = findViewById(R.id.me_collect);
        collectTvView = findViewById(R.id.me_tv_collect);
        meDownloadView = findViewById(R.id.me_download);
        gradeshoppingView = findViewById(R.id.grade_shopping);
        gradeView = findViewById(R.id.me_grade);
        messageView = findViewById(R.id.me_message);
        newLetterView = findViewById(R.id.newletter);

        moneyTextView = findViewById(R.id.money_textview); // 钱包

        mRlOrder=findViewById(R.id.rl_order);
        mRlFeedBack=findViewById(R.id.rl_feedback);
        mRlMyWord=findViewById(R.id.my_word);
        mRlMyWord.setVisibility(View.GONE);
        exitButton = findViewById(R.id.exitButton);
        exitButton.setVisibility(View.GONE);
        privacyPolicy = findViewById(R.id.privacy_permission);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!AccountManager.getInstance().islinshi) {

            float allMoney = 0;
            if (ConfigManagerSet.Instance().loadString("money") != null && !ConfigManagerSet.Instance().loadString("money").equals("")) {
                LogUtil.e("钱包崩溃" + ConfigManagerSet.Instance().loadString("money"));
                allMoney = Float.parseFloat(ConfigManagerSet.Instance().loadString("money"));
            }
            BigDecimal b = new BigDecimal(allMoney * 0.01f);
            float money = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            moneyTextView.setText(String.valueOf(money));
        }

        MobclickAgent.onResume(this);
        nightModeManager.checkMode();
        //CustomToast.showToast(mContext, R.string.integral_tip, 2000);


        if ((AccountManager.getInstance().checkUserLogin() && NetStatusUtil.isConnected(mContext)) ||
                (AccountManager.getInstance().islinshi && NetStatusUtil.isConnected(mContext))) {
            updateCreditsCounts();
            updateAttationCounts();
            updateFunsCounts();
        } else if (!NetStatusUtil.isConnected(mContext)) {
            CustomToast.showToast(mContext, "无网络连接..", 1000);
        }
        if (AccountManager.getInstance().islinshi) {
            logoutButton.setText("登录|注册");
        } else {
            logoutButton.setText("注销");
        }
        if (AccountManager.getInstance().checkUserLogin() || AccountManager.getInstance().islinshi) {
            userInfoView.setVisibility(View.VISIBLE);
            unloginView.setVisibility(View.GONE);
            if (NetStatusUtil.isConnected(mContext)) {
                RequestNewInfo request = new RequestNewInfo(AccountManager.getInstance().userId,
                        new RequestCallBack() {
                            @Override
                            public void requestResult(Request result) {
                                RequestNewInfo rs = (RequestNewInfo) result;
                                if (rs.letter > 0) {
                                    handler.sendEmptyMessage(0);
                                } else {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        });
                CrashApplication.getInstance().getQueue().add(request);
            }
            logoutButton.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
        } else {
            userInfoView.setVisibility(View.GONE);
            unloginView.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.GONE);
        }
        if (AccountManager.getInstance().isVip(mContext)) {
            vipImageView.setImageResource(R.mipmap.vip);
        } else {
            vipImageView.setImageResource(R.mipmap.no_vip);
        }
    }

    private void updateNotifitionList() {
        if (!SocialDataManager.getInstance().notificationInfos.isEmpty()) {
            notifitionTextView.setText(SocialDataManager.getInstance().notificationInfos.size() + "");
            return;
        }
        if (!NetStatusUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "无网络连接,更新数据失败..", Toast.LENGTH_SHORT).show();
            return;
        }
        SocialDataManager.getInstance().getNotifitionInfos(new CommonCallBack() {
            @Override
            public void onPositive(Object object) {
                if (SocialDataManager.getInstance().notificationInfos != null) {
                    notifitionTextView.post(() -> notifitionTextView.setText(SocialDataManager.getInstance().notificationInfos.size() + ""));
                    return;
                }
            }

            @Override
            public void onNegative(Object object) {
            }
        });
    }

    private void updateFunsCounts() {
        if (UserInfoManager.myInfo != null
                && UserInfoManager.myInfo.uid == AccountManager.getInstance().userId) {
            funTextView.setText(UserInfoManager.myInfo.follower + "");
            return;
        }
        if (!NetStatusUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "无网络连接,更新数据失败..", Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfoManager.getMyInfo(new CommonCallBack() {
            @Override
            public void onPositive(Object object) {
                UserInfoManager.myInfo.uid = AccountManager.getInstance().userId;
                funTextView.post(() -> funTextView.setText(UserInfoManager.myInfo.follower + ""));
            }

            @Override
            public void onNegative(Object object) {
            }
        });
    }

    private void updateCreditsCounts() {
        if (!NetStatusUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "无网络连接,更新数据失败..", Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfoManager.getMyInfo(new CommonCallBack() {
            @Override
            public void onPositive(Object object) {
                UserInfoManager.myInfo.uid = AccountManager.getInstance().userId;
                creditsTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        creditsTextView.setText(UserInfoManager.myInfo.credits + "");
                    }
                });
            }

            @Override
            public void onNegative(Object object) {
            }
        });
    }

    private void updateAttationCounts() {
        if (UserInfoManager.myInfo != null
                && UserInfoManager.myInfo.uid == AccountManager.getInstance().userId) {
            attentionTextView.setText(UserInfoManager.myInfo.following + "");
            return;
        }
        if (!NetStatusUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "无网络连接,更新数据失败..", Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfoManager.getMyInfo(new CommonCallBack() {
            @Override
            public void onPositive(Object object) {
                UserInfoManager.myInfo.uid = AccountManager.getInstance().userId;
                attentionTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        attentionTextView.setText(UserInfoManager.myInfo.following + "");
                    }
                });
            }

            @Override
            public void onNegative(Object object) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.linearLayout1) {

            int uid =AccountManager.getInstance().userId;
            AccountManager manager = AccountManager.getInstance();
            if (manager.checkUserLogin() && !manager.islinshi) {
//                Intent intent1 = new Intent(MeActivity.this, AttentionCenter.class);
//                intent1.putExtra("userid",uid);
//                startActivity(intent1);
                startActivity(SimpleListActivity.buildIntent(this, 1, uid));
            } else {
                ToastFactory.showShort(getContext(), getString(R.string.please_login));
            }

        } else if (v.getId() == R.id.linearLayout2) {
            int uid =AccountManager.getInstance().userId;
            AccountManager manager = AccountManager.getInstance();
            if (manager.checkUserLogin() && !manager.islinshi) {
//                Intent intent2 = new Intent(MeActivity.this, FansCenter.class);
//                intent2.putExtra("userid",uid);
                startActivity(SimpleListActivity.buildIntent(this, 0, uid));
            } else {
                ToastFactory.showShort(getContext(), getString(R.string.please_login));
            }
        } else if (v.getId() == R.id.linearLayout3) {
            intent = new Intent();
            intent.setClass(MeActivity.this, WebActivity.class);
            // 积分规则api
            intent.putExtra("url", "http://api." + Constant.IYBHttpHead + "/credits/useractionrecordmobileList1.jsp?uid="
                    + AccountManager.getInstance().userId + "&curPage=0");
            intent.putExtra("title", "积分规则");
            startActivity(intent);
        } else if (v.getId() == R.id.user_image) {
            if (NetStatusUtil.isConnected(mContext)) {
                startActivity(PersonalHomeActivity.buildIntent(mContext,
                        AccountManager.getInstance().userId,AccountManager.getInstance().userName,
                        0));
            }
        } else if (v.getId() == R.id.me_local) {
            startActivity(new Intent(mContext, FragmentHolderActivity.class).putExtra("fragmentname",
                    "localdownload"));
        } else if (v.getId() == R.id.me_history) {
            startActivity(new Intent(mContext, FragmentHolderActivity.class).putExtra("fragmentname",
                    "history"));
        } else if (v.getId() == R.id.login_button) {
            startActivity(new Intent(mContext, LoginActivity.class));
        } else if (v.getId() == R.id.exitButton) {
            if (AccountManager.getInstance().islinshi) {
                startActivity(new Intent(mContext, LoginActivity.class));
            } else {
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.alert))
                        .setMessage(getResources().getString(R.string.setting_logout_alert))
                        .setPositiveButton(getResources().getString(R.string.alert_btn_ok),
                                (dialog, whichButton) -> {
                                    SettingConfig.Instance().setAutoLogin(false);
                                    AccountManager.getInstance().loginOut();
                                    SetUserLibUtils.setUserInfoLib(mContext);
                                    CustomToast.showToast(mContext, R.string.setting_loginout_success, 1000);
                                    finish();
                                })
                        .setNeutralButton(getResources().getString(R.string.alert_btn_cancel),
                                (dialog, which) -> {
                                }).show();
            }
        }else if(v.getId() == R.id.logoutButton) {
            if (AccountManager.getInstance().checkUserLogin()) {
                TextView title = new TextView(mContext);
                title.setGravity(Gravity.CENTER);
                title.setText("提示");
                title.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                title.setTextSize(20);
                title.setPadding(0, 20, 0, 0);
                new AlertDialog.Builder(mContext)
                        .setMessage("注销账号后将无法登录，并且将不再会保存个人信息，账户信息将会被清除，确定要注销账号吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SettingConfig.Instance().setAutoLogin(false);
                                showContinueWrittenOffDialog();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null)
                        .setCustomTitle(title)
                        .create()
                        .show();
            } else {
                Toast.makeText(mContext, "未登录，请先登录", Toast.LENGTH_SHORT).show();
            }

        } else if(v.getId() == R.id.privacy_permission){
            showPermissionDialog();
        } else if (v.getId() == R.id.me_collect) {
            startActivity(new Intent(mContext, FragmentHolderActivity1.class).putExtra(
                    "fragmentname","collect"));
        } else if (v.getId() == R.id.me_tv_collect) {
            int userId = AccountManager.getInstance().getUId();
            startActivity(BasicFavorActivity.buildIntent(mContext));
        } else if (v.getId() == R.id.me_download) {
            startActivity(new Intent(mContext, DownloadActivity.class));
        } else if (v.getId() == R.id.me_message) {
            if (AccountManager.getInstance().checkUserLogin()&&!AccountManager.getInstance().islinshi) {
                startActivity(new Intent(mContext,MessageActivity.class));
            } else{
                Toast.makeText(mContext, "请登录账号", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.me_grade) {
            startActivity(new Intent(mContext, FragmentHolderActivity.class).putExtra("fragmentname",
                    "grade"));
        } else if (v.getId() == R.id.grade_shopping) {
            if (AccountManager.getInstance().islinshi) {
                Toast.makeText(mContext, "请登录账号", Toast.LENGTH_LONG).show();
            } else {
                Intent intent1 = new Intent(mContext, WebActivity.class);
                intent1.putExtra("url", "http://m."+ com.iyuba.configation.Constant.IYBHttpHead+"/mall/index.jsp?"
                        + "&uid=" + AccountManager.getInstance().userId
                        + "&sign=" + MD5.getMD5ofStr("iyuba" + AccountManager.getInstance().userId + "camstory")
                        + "&username=" + AccountManager.getInstance().userName
                        + "&platform=android&appid="
                        + com.iyuba.configation.Constant.APPID
                        + "&username=" + TextAttr.encode(AccountManager.getInstance().userName)
                );
                //intent1.putExtra("url", "http://m." + Constant.IYBHttpHead + "/mall/index.jsp");
                intent1.putExtra("title", "积分商城");
                startActivity(intent1);
            }
        } else if (v.getId() == R.id.button_back) {
            finish();

        } else if (v.getId() == R.id.qq) {
            showQQDialog(mContext, v);
        } else if (v.getId() == R.id.linearLayout5) {
            String moneyStr = moneyTextView.getText().toString();
            new AlertDialog.Builder(Objects.requireNonNull(mContext))
                    .setTitle(R.string.alert)
                    .setMessage(mContext.getString(R.string.money_tips, moneyStr))
                    .setPositiveButton(R.string.alert_btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }else if (v.getId() == R.id.rl_order){
            startActivity(new Intent(this, OrderDetailActivity.class));
        }else if (v.getId() == R.id.rl_feedback){
            startActivity(new Intent(mContext, Feedback.class));
        }else if (v.getId() == R.id.my_word){
            startActivity(new Intent(mContext, FragmentHolderActivity.class).putExtra("fragmentname","word"));
        }else if(v.getId() == R.id.me_group){
            GroupChatManageActivity.start(MeActivity.this, 9933, "爱语吧官方群", true);
        }else if(v.getId() == R.id.me_search_group){
            startActivity(new Intent(MeActivity.this, SearchGroupActivity.class));
        }else if(v.getId() == R.id.me_search_frinend){
            startActivity(new Intent(MeActivity.this, SearchUserActivity.class));
        }else if(v.getId() == R.id.me_qq_group){

        }else if(v.getId() == R.id.me_official_phone){
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008881905"));
            startActivity(callIntent);
        }

    }

    public boolean checkReadPermission(String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    private void showPermissionDialog() {
        final String[] items = {"隐私政策","使用协议"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(items, (dialog, which) -> {
            if (which == 0){
                Intent intent =new Intent(MeActivity.this, Web.class);
                intent.putExtra("url", WebConstant.HTTP_SPEECH_ALL+"/api/protocolpri.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=1");
                intent.putExtra("title","隐私政策");
                startActivity(intent);
            }else if(which == 1){
                Intent intent =new Intent(MeActivity.this, Web.class);
                intent.putExtra("url",WebConstant.HTTP_SPEECH_ALL+"/api/protocoluse666.jsp?apptype="+ getResources().getString(R.string.app_name)+"&company=1");
                intent.putExtra("title","用户协议");
                startActivity(intent);
            }
        });
        builder.show();
    }

    private void showContinueWrittenOffDialog() {
        final PwdInputDialog dialogTip = new PwdInputDialog(mContext);
        dialogTip.setTitleStr("输入密码注销账号");
        dialogTip.setCancelStr("取消");
        dialogTip.setConfirmStr("确认注销");
        dialogTip.setShowInputPwd(true);
        dialogTip.setListener(new PwdInputDialog.OnBtnClickListener() {
            @Override
            public void onCancelClick() {
                dialogTip.dismiss();
            }

            @Override
            public void onConfirmClick() {
                dialogTip.dismiss();
            }

            @Override
            public void onCheckInputPassWord(final String password) {

                String[] nameAndPwd = AccountManager.getInstance().getUserNameAndPwd(mContext);
                final String userName = nameAndPwd[0];
                final String userPwd = nameAndPwd[1];
                if (TextUtils.equals(password, userPwd)) {
                    clearUser(userName, userPwd);
//                    ToastUtil.showToast(mContext, "frist vertify");
                } else {
                    // 用 输入 的密码登录验证 输入的密码是否正确
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                            .baseUrl("http://api."+WebConstant.IYBHttpHead2+"/")
                            .client(client)
                            .addConverterFactory(JsonOrXmlConverterFactory.create())
                            .build();
                    TeacherApiStores apiService = retrofit.create(TeacherApiStores.class);
                    try {
                        apiService.login("11001", URLEncoder.encode(userName, "UTF-8"), MD5.getMD5ofStr(password), "0", "0", com.iyuba.configation.Constant.APPID,
                                MD5.getMD5ofStr("11001" + userName + MD5.getMD5ofStr(password) + "iyubaV2"), "json")
                                .enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            LoginResponse userMsg = response.body();
                                            if (TextUtils.equals("101", userMsg.getResult())) {
                                                // 登陆成功，证明输入的密码正确
//                                                Log.e(TAG, "second  "+"101" );
                                                clearUser(userName, password);
//                                                ToastUtil.showToast(mContext, "验证中......");
                                            } else if (TextUtils.equals("102", userMsg.getResult())) {
                                                ToastUtil.showToast(mContext, "验证失败，请稍后重试！");
                                            } else if (TextUtils.equals("103", userMsg.getResult())) {
                                                ToastUtil.showToast(mContext, "输入的密码有误，请重试！");
                                            } else {
                                                ToastUtil.showToast(mContext, getString(R.string.login_fail));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                                        ToastUtil.showToast(mContext, "验证失败，请稍后重试！");
                                    }
                                });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialogTip.show();
    }

    private void clearUser(String username, String password) {
        AccountManager.getInstance().loginOut();
        Observable.create((ObservableOnSubscribe<Boolean>) observableEmitter -> {
            Response<ClearUserResponse> execute = null;
            execute = clearDataUser(username, password).execute();
            observableEmitter.onNext(execute != null);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean){
                    clearSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public Call<ClearUserResponse> clearDataUser(String username, String password) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        OkHttpClient client = builder.build();
        SimpleXmlConverterFactory xmlFactory = SimpleXmlConverterFactory.create();
        GsonConverterFactory gsonFactory = GsonConverterFactory.create();
        RxJava2CallAdapterFactory rxJavaFactory = RxJava2CallAdapterFactory.create();

        mApiComService = ApiComService.Creator.createService(client, gsonFactory, rxJavaFactory);

        String protocol = "11005";
        String format = "json";
        String passwordMD5 = com.iyuba.module.toolbox.MD5.getMD5ofStr(password);
        String sign = buildV2Sign(protocol, username, passwordMD5, "iyubaV2");
        return mApiComService.clearUserInfo(protocol, username, passwordMD5, sign, format);

    }

    private String buildV2Sign(String... stuffs) {
        StringBuilder sb = new StringBuilder();
        for (String stuff : stuffs) {
            sb.append(stuff);
        }
        return com.iyuba.module.toolbox.MD5.getMD5ofStr(sb.toString());
    }

    public interface ApiComService {

        String ENDPOINT = "http://api."+WebConstant.IYBHttpHead2+"/";

        @GET("v2/api.iyuba")
        Call<ClearUserResponse> clearUserInfo(@Query("protocol") String protocol,
                                              @Query("username") String username,
                                              @Query("password") String password,
                                              @Query("sign") String sign,
                                              @Query("format") String format);



        class Creator {
            public static ApiComService createService(OkHttpClient client, GsonConverterFactory gsonFactory, RxJava2CallAdapterFactory rxJavaFactory) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ENDPOINT)
                        .client(client)
                        .addConverterFactory(gsonFactory)
                        .addCallAdapterFactory(rxJavaFactory)
                        .build();
                return retrofit.create(ApiComService.class);
            }
        }
    }

    public void clearSuccess(){
        SettingConfig.Instance().setHighSpeed(false);
        ConfigManager.Instance().putString("userId", "0");
        ConfigManager.Instance().putString("userName", "");
        ConfigManager.Instance().putInt("isvip", 0);
//        EventBus.getDefault().post(new ChangeVideoEvent(true));

//        CustomToast.showToast(mContext, R.string.loginout_success);
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("账户被注销！账户信息清除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(MeActivity.this,MainActivity.class));
                    }
                })
                .show();
    }

    public void finish() {
        super.finish();
        nightModeManager.remove();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



    public void showQQDialog(final Context mContext, View v) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tv1 = new TextView(mContext);
        TextView tv2 = new TextView(mContext);
        TextView tv3 = new TextView(mContext);
        TextView tv4 = new TextView(mContext);

        tv1.setTextSize(14);
        tv2.setTextSize(14);
        tv3.setTextSize(14);
        tv4.setTextSize(14);

        tv1.setPadding(10, ScreenUtils.dp2px(mContext, 10), 10, 0);
        tv2.setPadding(10, ScreenUtils.dp2px(mContext, 20), 10, ScreenUtils.dp2px(mContext, 20));
        tv3.setPadding(10, 10, 10, ScreenUtils.dp2px(mContext, 20));
        tv4.setPadding(10, 0, 10, ScreenUtils.dp2px(mContext, 10));

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);

        BrandUtil.requestQQGroupNumber(mContext);

        tv1.setText(String.format("%s用户群: %s", BrandUtil.getBrandChinese(), BrandUtil.getQQGroupNumber(mContext)));
        tv2.setText("内容QQ: "+editor_qq);
        tv3.setText("技术QQ: "+technician_qq);
        tv4.setText("举报QQ: "+manager_qq);




        linearLayout.addView(tv1);
        linearLayout.addView(tv2);
        linearLayout.addView(tv3);
        linearLayout.addView(tv4);

        tv1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startQQGroup(mContext, BrandUtil.getQQGroupKey(mContext));
            }
        });

        tv2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startQQ(mContext, editor_qq);
            }
        });

        tv3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startQQ(mContext, technician_qq);
            }
        });

        tv4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startQQ(mContext, manager_qq);
            }
        });


        BubbleLayout bl = new BubbleLayout(this);
        new BubbleDialog(mContext)
                .addContentView(linearLayout)
                .setClickedView(v)
                .setPosition(BubbleDialog.Position.BOTTOM)
                .calBar(true)
                .setBubbleLayout(bl)
                .show();


        bl.setLook(BubbleLayout.Look.TOP);



       /* final PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_home_qq, popupMenu.getMenu());

        popupMenu.getMenu().getItem(0).setTitle(
                String.format("%s用户群: %s", BrandUtil.getBrandChinese(), BrandUtil.getQQGroupNumber(mContext)));
        popupMenu.getMenu().getItem(1).setTitle("客服QQ: 3099007489");
        popupMenu.getMenu().getItem(2).setTitle("技术QQ: 1593972703");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.qq_group) {
                    Util.startQQGroup(mContext, BrandUtil.getQQGroupKey(mContext));
                    return true;
                } else if (i == R.id.qq_server) {
                    Util.startQQ(mContext, "3099007489");
                    return true;
                } else if (i == R.id.qq_tech) {
                    Util.startQQ(mContext, "1593972703");
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();*/
    }

    private String floatToString(float fNumber) {

        fNumber = (float) (fNumber * 0.01);

        DecimalFormat myformat = new java.text.DecimalFormat("0.00");
        String str = myformat.format(fNumber);
        return str;
    }

    //改变头像加载新的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserPhotoChangeEvent event) {
        ImageLoader.getInstance().displayImage(
                Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big",
                userCircularImageView, optionsBuilder);
    }

    //改变用户名加载新的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserNameChangeEvent event) {
        usernameTextView.setText(AccountManager.getInstance().userName);
    }


    //登陆成功加载用户名和头像
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginSuccessEvent event) {
        ImageLoader.getInstance().displayImage(
                Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big",
                userCircularImageView, optionsBuilder);
        usernameTextView.setText(AccountManager.getInstance().userName);
    }


}


