package com.iyuba.camstory.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.LoginBean;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.LoginRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.UserInfoRefreshRequest;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.setting.ConfigManagerSet;
import com.iyuba.camstory.sqlite.mode.UserInfo;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.NetStatusUtil;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.configation.ConfigManagerHead;
import com.iyuba.voa.activity.setting.Constant;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户管理 用于用户信息的保存及权限判断
 *
 * @author chentong
 */
public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();
    public static final String ACTION_LOGIN = "user_login";
    public static final String EXTRAS_UID = "uid";
    public static final String EXTRAS_UNAME = "username";
    public static final String EXTRAS_VIPSTATUS = "vipStatus";
    public boolean islinshi = false;
    private static AccountManager instance;
    private static final int LOGIN_STATUS_UNLOGIN = 0;
    private static final int LOGIN_STATUS_LOGIN = 1;
    public int loginStatus = LOGIN_STATUS_UNLOGIN; // 用户登录状态,默认为未登录状态
    public int userId; // 用户ID
    private String money;
    public String userName; // 用户姓名
    private String userImg; // 用户头像
    public String userPwd; // 用户密码
    public String latitude;
    public String longitude;
    private static boolean isvip;// 是否VIP //shite
    private String validity;// 有效期
    // private String registError = "登录失败,请检查用户名和密码";
    public String amount;// 爱语币
    private LoginRequest loginRequest;
    private boolean loginSuccess = false;

    public String vipStatus;//shite

    public Context mContext;
    public UserInfo userInfo;
    public String UserId;


    public void setVipStatus(String vipStatus) {
        this.vipStatus = vipStatus;
    }

    public void setLoginState(int state) {
        loginStatus = state;
        UserId = ConfigManagerSet.Instance().loadString("userId");
        userId = Integer.valueOf(ConfigManagerSet.Instance().loadString("userId"));
        String[] nameAndPwd = getUserNameAndPwd(mContext);
        userName = nameAndPwd[0];
        userPwd = nameAndPwd[1];
        //userInfo = new UserInfoOp(mContext).selectData(userId);
        if (userInfo != null) {
            ConfigManagerHead.Instance().putInt("isvip",
                    Integer.parseInt(userInfo.vipStatus));
        } else {
            ConfigManagerHead.Instance().putInt("isvip", 0);
        }

    }



    public static synchronized AccountManager getInstance() {

        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    /**
     * 检查当前用户是否登录
     *
     * @return
     */
    public boolean checkUserLogin() {
        return loginStatus == LOGIN_STATUS_LOGIN&&!islinshi;
    }

    public boolean checkOffLineLogin(Context context) {
        if (!"".equals(ConfigManagerSet.Instance().loadString("userName"))
                && !"".equals(ConfigManagerSet.Instance().loadString("userPwd"))) {
            userName = ConfigManagerSet.Instance().loadString("userName");
            return true;
        }
        return false;
    }

    // hastoast：是否显示“欢迎回来XXX”的toast，默认有，可以在网络变化登录中取消
    public boolean login(final Context context, final String userName,
                         String userPwd, final RequestCallBack rc) {
        return login(context, userName, userPwd, rc, true);
    }

    //新用户登录
    public boolean login(final Context context, final String userName, String userPwd) {

        Call<LoginBean> call = RequestFactory.getLoginApi().getLogin(userName, MD5.getMD5ofStr(userPwd), Constant.getAppid(), Constant.getAppname(),
                MD5.getMD5ofStr("11001" + userName + MD5.getMD5ofStr(this.userPwd) + "iyubaV2"));
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                if (response != null && response.body().getResult().equals("101")) {
                    //登录成功
                    LoginBean bean = response.body();
                    Log.e("login----", bean.toString());
                    RefreshData(context, bean);
                    Log.e("登录成功", "loginStatus" + loginStatus);
                } else {
                    //登录失败,用户名密码错误
                    handler.sendEmptyMessage(1);
                    loginSuccess = false;
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                //登录失败,网络错误
                handler.sendEmptyMessage(2);
                loginSuccess = false;
            }
        });
        return false;
    }

    private void RefreshData(Context context, LoginBean body) {
        userId = body.getUid();
        userName = body.getUsername();
        vipStatus = body.getVipStatus();

        Log.e("vipStatus", vipStatus);
//        ConfigManager.Instance(context).putInt("userId", userId);
//        ConfigManager.Instance(context).putString("userName", userName);

        loginSuccess = true;
        loginStatus = LOGIN_STATUS_LOGIN;

        final Date date = new Date(Long.parseLong(body.getExpireTime() + "") * 1000l);
        final SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        ConfigManagerSet.Instance()
                .putInt("isvip", Integer.parseInt(vipStatus));
        validity = sdfDateFormat.format(date);
        if (!vipStatus.equals("0")) {
            ConfigManagerSet.Instance().putBoolean(
                    "highspeed_download", true);
            ConfigManagerSet.Instance().putString("validity", validity);
        } else {
            ConfigManagerSet.Instance().putString("validity", "");
            ConfigManagerSet.Instance().putBoolean(
                    "highspeed_download", false);
        }
        String iyubi=body.getAmount();
        ConfigManagerSet.Instance().putString("iyubi", iyubi.equals("")?"0":iyubi);
    }


    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public boolean login(final Context context, final String userName,
                         final String userPwd, final RequestCallBack rc, final boolean hastoast) {
       // String[] strings = GetLocation.getInstance(context).getLocation();
        String latitude;
        String longitude;

        //这里取消登陆时的用户位置数据，直接设为0
//        Log.e("地理位置权限", PermissionUtils.hasSelfPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION) + "");
//        if (PermissionUtils.hasSelfPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            Pair<Double, Double> location = GetLocation.getLocation(context);
//            latitude = String.valueOf(location.first);
//            longitude = String.valueOf(location.second);
//        } else {
            latitude = "0";
            longitude = "0";
            LogUtils.e("地理位置信息获取失败");
//        }
        try {
            loginRequest = new LoginRequest(userName, userPwd, latitude,
                    longitude, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {

                    } else {

                    }
                }

            }, new RequestCallBack() {
                @Override
                public void requestResult(Request result) {
                    final LoginRequest rr = (LoginRequest) result;
                    if (rr.isLoginSuccess()) {
                        changeName(userName,userPwd);
                        Log.e("vipdate", rr.toString());
                        userId = rr.uid; // 成功返回用户ID
                        Log.e("userid===", userId + "");
                        ConfigManagerSet.Instance()
                                .putString("money", rr.money); //钱包
                        ConfigManagerSet.Instance()
                                .putString("userId", String.valueOf(rr.uid)); //用户名
                        ConfigManagerSet.Instance()
                                .putString("userName", rr.username); //name
                        ConfigManagerSet.Instance()
                                .putString("userPic", rr.imgsrc); //pic
                        ConfigManagerSet.Instance()
                                .putString("isvip", rr.vip); //Vip
//                        UserinfoManager.getInstance().init(Constant.getAppid(), rr.uid + "",
//                                rr.vip, true, AccountManager.getInstance().getUserNameAndPwd(mContext)[0]);
                        loginSuccess = true;
                        loginStatus = LOGIN_STATUS_LOGIN;
                        userImg = rr.imgsrc;
                        AccountManager.this.userName = rr.username;
                        if (islinshi) {
                            ConfigManagerSet.Instance().putBoolean("islinshi", false);
                            islinshi = false;
                        }
                        vipStatus = rr.vip;
                        ConfigManagerSet.Instance().putString("iyubi", rr.amount.equals("")?"0":rr.amount);

                        Log.e("vipStatus_vipStatus", rr.vip);
                        if (!"0".equals(rr.vip)) {
                            isvip = true;
                            Log.e(TAG, "Login success and set vip");
                            final Date date = new Date(Long.parseLong(rr.validity) * 1000l);
                            final SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                                    "yyyy-MM-dd");
                            ConfigManagerSet.Instance()
                                    .putInt("isvip", Integer.parseInt(rr.vip));
                            validity = sdfDateFormat.format(date);
                            ConfigManagerSet.Instance().putBoolean(
                                    "highspeed_download", true);
                            ConfigManagerSet.Instance().putString("validity", validity);
                            Log.e("vipStatusvipStatus", validity);
                        } else {
                            ConfigManagerSet.Instance().putInt("isvip", 0);
                            ConfigManagerSet.Instance().putString("validity", "");
                            ConfigManagerSet.Instance().putBoolean(
                                    "highspeed_download", false);
                        }
                        //User set

                        Intent intent = new Intent(ACTION_LOGIN);
                        intent.putExtra(EXTRAS_UNAME, AccountManager.this.userName);
                        intent.putExtra(EXTRAS_UID, AccountManager.this.userId);
                        intent.putExtra(EXTRAS_VIPSTATUS, AccountManager.this.vipStatus);
                        context.sendBroadcast(new Intent(ACTION_LOGIN));
                        ConfigManagerSet.Instance().putString("currUserAmount",
                                rr.amount);
                        ConfigManagerSet.Instance().putString("lastLogin",
                                System.currentTimeMillis() + "");
                        if (hastoast) {
                            handler.sendEmptyMessage(4);
                        }
                        if (rc != null) {
                            rc.requestResult(loginRequest);
                        }

                    } else {
                        // 登录失败
                        int errNum = Integer.parseInt(rr.result);
                        Log.i(TAG, "error result : " + errNum);
                        Message msg = handler.obtainMessage(1);
                        msg.obj = errNum;
                        handler.sendMessage(msg);
                        loginSuccess = false;
                        loginStatus = LOGIN_STATUS_UNLOGIN;
                        if (rc != null) {
                            rc.requestResult(loginRequest);
                        }
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CrashApplication.getInstance().getQueue().add(loginRequest);
//        loginStatus = 1;
        return loginSuccess;
    }


    /**
     * 用户登出
     *
     * @return
     */
    public boolean loginOut() {
        loginStatus = LOGIN_STATUS_UNLOGIN;
        userId = 0; // 用户ID
        userName = null; // 用户姓名
        userImg = null; // 用户昵称
        userPwd = null; // 用户密码
        isvip = false;
        validity = "";
        vipStatus = "0";
        //SettingConfig.Instance(CrashApplication.getInstance()).setAutoLogin(false);
        //saveUserNameAndPwd("", "");

        ConfigManagerSet.Instance().putInt("isvip",
                0);
        ConfigManagerSet.Instance().putString(
                "validity", validity);
        ConfigManagerSet.Instance().putInt("userId", 0);
        ConfigManagerSet.Instance().putString("userName", null);
        ConfigManagerSet.Instance().putString("userPwd", null);
        ConfigManagerSet.Instance().putString("iyubi", "0");

        if (islinshi) {
            ConfigManagerSet.Instance().putBoolean("islinshi", false);
            islinshi = false;
        }
        return true;
    }

    /**
     * 更换用户
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public boolean replaceUserLogin(Context context, String userName,
                                    String userPwd) {
        if (loginOut()) { // 登出
            if (login(context, userName, userPwd, null)) { // 登录
                return true;
            }
        }
        return false;
    }

    /**
     * 保存账户密码
     *
     * @param userName
     * @param userPwd
     */
    public void saveUserNameAndPwd(String userName, String userPwd) {
        ConfigManagerSet.Instance().putString(
                "userName", userName);
        ConfigManagerSet.Instance().putString("userPwd",
                userPwd);
    }

    /**
     * 获取用户名及密码
     *
     * @return string[2] [0]=userName,[1]=userPwd
     */
    public String[] getUserNameAndPwd(Context mContext) {
        String[] nameAndPwd = new String[]{
                ConfigManagerSet.Instance().loadString("userName"),
                ConfigManagerSet.Instance().loadString("userPwd")};
        return nameAndPwd;
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1: // 弹出错误信息
                    CustomToast.showToast(CrashApplication.getInstance(), R.string.login_fail, 1000);
                    break;
                case 2:
                    CustomToast.showToast(CrashApplication.getInstance(),
                            R.string.login_faild, 1000);
                    break;
                case 3:
                    CustomToast.showToast(CrashApplication.getInstance(),
                            R.string.person_vip_limit, 1000);
                    break;
                case 4:
                    CustomToast.showToast(CrashApplication.getInstance(), CrashApplication
                            .getInstance().getResources().getString(R.string.welcome)
                            + userName, 1000);
                    break;
            }
        }
    };

    /**
     * 刷新VIP状态
     *
     * @param context
     * @param listener
     */
    public void VipRefresh(final Context context,
                           final VipRefreshListener listener) {

        Log.e("刷新vip状态", "===");
        //lbl加的
        if (islinshi) {
            ConfigManagerSet.Instance().putInt("isvip", 0);
            ConfigManagerSet.Instance().putString("validity", "");
            ConfigManagerSet.Instance().putString("iyubi", "0");

            validity = "";
            isvip = false;
        } else {
            UserInfoRefreshRequest urq = new UserInfoRefreshRequest(userName, Constant.getAppid(), new RequestCallBack() {
                @Override
                public void requestResult(Request result) {
                    UserInfoRefreshRequest res = (UserInfoRefreshRequest) result;
                    if (res.isRequestSuccessful()) {
                        ConfigManagerSet.Instance().putString("iyubi", String.valueOf(res.amount));

                        if (res.vipStatus) {
                            isvip = true;
                            final Date date = new Date(res.expireTime * 1000l);
                            final SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                                    "yyyy-MM-dd");


                            int iVipStatus = Integer.parseInt(vipStatus);
                            ConfigManagerSet.Instance().putInt("isvip", iVipStatus);

                            validity = sdfDateFormat.format(date);
                            Log.e("vip time", validity);
                            ConfigManagerSet.Instance().putString("validity", validity);
                            if (listener != null) {
                                listener.onVipRefresh(true);
                            }
                        } else {
                            ConfigManagerSet.Instance().putInt("isvip", 0);
                            ConfigManagerSet.Instance().putString("validity", "");
                            if (listener != null) {
                                listener.onVipRefresh(false);
                            }
                        }
                        Log.e(TAG, "" + res.amount);
                        ConfigManagerSet.Instance().putString("currUserAmount",
                                res.amount + "");
                    }
                }
            });
            CrashApplication.getInstance().getQueue().add(urq);
        }
    }

    /**
     * 综合判断是否VIP
     *
     * @return
     */
    public boolean isVip(Context context) {
        if (!NetStatusUtil.isConnected(context) && checkOffLineLogin(context)) {
            return ConfigManagerSet.Instance().loadInt("isvip") >= 1;
        } else if (NetStatusUtil.isConnected(context)) {
            return isvip;
        }
        return false;
    }

    public String getVipStatus() {
        if (vipStatus == null) {
            vipStatus = String.valueOf(ConfigManagerSet.Instance().loadInt("isvip"));
        }
        return vipStatus;

    }

    public int getUId() {
        return userId;
    }


    public interface VipRefreshListener {

        void onVipRefresh(boolean isVip);
    }

    @Override
    public String toString() {
        return "AccountManager{" +
                "islinshi=" + islinshi +
                ", loginStatus=" + loginStatus +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userImg='" + userImg + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", validity='" + validity + '\'' +
                ", amount='" + amount + '\'' +
                ", loginRequest=" + loginRequest +
                ", loginSuccess=" + loginSuccess +
                ", vipStatus='" + vipStatus + '\'' +
                ", handler=" + handler +
                '}';
    }
    private void changeName(String userName,String psd){
        this.userName = userName;
        this.userPwd = psd;
    }
}
