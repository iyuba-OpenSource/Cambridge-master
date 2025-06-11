package com.iyuba.camstory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.iyuba.camstory.lil.GlideUtil;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.fragment.ReadFragment;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.SignBeanRequest;
import com.iyuba.camstory.listener.StudyTimeBeanRequest;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.setting.ConfigManagerSet;
import com.iyuba.camstory.sqlite.mode.SignBean;
import com.iyuba.camstory.sqlite.mode.StudyTimeBeanNew;
import com.iyuba.camstory.utils.Base64Coder;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.configation.Constant;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import me.drakeet.materialdialog.MaterialDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 打卡页面
 */
@RuntimePermissions
public class SignActivity extends Activity {


    private ImageView imageView;
    private ImageView qrImage;
    private TextView tv1, tv2, tv3;
    private Context mContext;
    private TextView sign;
    private ImageView userIcon;
    private TextView tvShareMsg;
    private int signStudyTime = 60 * 3;
    private String loadFiledHint = "打卡加载失败";

    String shareTxt;
    String getTimeUrl;
    LinearLayout ll;

    private CustomDialog wettingDialog;
    String addCredit = "";//Integer.parseInt(bean.getAddcredit());
    String days = "";//Integer.parseInt(bean.getDays());
    String totalCredit = "";//bean.getTotalcredit();
    String money = "";
//    private int QR_HEIGHT = 77;
//    private int QR_WIDTH = 77;

    private TextView tvUserName;
    private TextView tvAppName;
    private TextView tv_finish;

    private ImageView btn_close;
    private MaterialDialog dialog, dialog_share;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;

        wettingDialog = new WaittingDialog().wettingDialog(mContext);
        setContentView(R.layout.activity_sign);

        //状态栏处理
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

            //android手机小于5.0的直接全屏显示，防止截图留白边
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initView();

        initData();

        initBackGround();

        SignActivityPermissionsDispatcher.openCarmerWithPermissionCheck(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initData() {

        wettingDialog.show();

        String uid = AccountManager.getInstance().userId + "";


        final String url = String.format(Locale.CHINA,
                "http://daxue." + Constant.IYBHttpHead + "/ecollege/getMyTime.jsp?uid=%s&day=%s&flg=1", uid, getDays());
        LogUtils.d("分享dddd", url);
        getTimeUrl = url;
        StudyTimeBeanRequest studyTimeBeanRequest = new StudyTimeBeanRequest(url, new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                StudyTimeBeanRequest sr = (StudyTimeBeanRequest) result;

                try {
                    if (null != wettingDialog) {
                        if (wettingDialog.isShowing()) {
                            wettingDialog.dismiss();
                        }
                    }

                    final StudyTimeBeanNew bean = new Gson().fromJson(sr.json.toString(), StudyTimeBeanNew.class);

                    LogUtils.e("分享ppp====", bean.toString());
                    if ("1".equals(bean.getResult())) {
                        final int time = Integer.parseInt(bean.getTotalTime());
                        int totaltime = Integer.parseInt(bean.getTotalDaysTime());

                        tv1.setText(bean.getTotalDays() + ""); //学习天数
                        tv2.setText(bean.getTotalWord() + "");//今日单词f

                        int nowRank = Integer.parseInt(bean.getRanking());
                        double allPerson = Double.parseDouble(bean.getTotalUser());
                        double carry;
                        String over = null;
                        if (allPerson != 0) {
                            carry = 1 - nowRank / allPerson;
                            DecimalFormat df = new DecimalFormat("0.00");
                            LogUtils.e("百分比", df.format(carry) + "--" + nowRank + "--" + allPerson);

                            over = df.format(carry).substring(2, 4);
                        }

                        tv3.setText(over + "%同学"); //超越了
                        shareTxt = bean.getSentence() + "我在爱语吧坚持学习了" + bean.getTotalDays() + "天,积累了" + bean.getTotalWords()
                                + "单词如下";

                        if (time >= signStudyTime || ReadFragment.flag) {

                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    qrImage.setVisibility(View.VISIBLE);
                                    sign.setVisibility(View.GONE);
                                    tvShareMsg.setVisibility(View.VISIBLE);
                                    tv_finish.setVisibility(View.VISIBLE);
                                    tvShareMsg.setText("长按图片识别二维码");
                                    tvShareMsg.setBackground(getResources().getDrawable(R.drawable.sign_bg_yellow));
                                    writeBitmapToFile();
                                    showShareOnMoment(SignActivity.this, AccountManager.getInstance().userId + "", Constant.APPID);
//                                        shareUrl(shareDownloadUrl,shareTxt,bitmap,"我的学习记录", SendMessageToWX.Req.WXSceneTimeline);
                                }
                            });
                           // startShareInterface(); //TODO 票圈分享
                        } else {
                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toast(String.format(Locale.CHINA, "打卡失败，当前已学习%d秒，\n满%d分钟可打卡", time, signStudyTime / 60));
                                }
                            });
                        }
                    } else {
                        toast(loadFiledHint + bean.getResult());
                    }
                } catch (Exception e) {
                    LogUtils.e("分享异常", e.toString());
                    e.printStackTrace();
                    toast(loadFiledHint + "！！");
                }
            }

        });
        CrashApplication.getInstance().getQueue().add(studyTimeBeanRequest);
    }

    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

    private void initView() {

        imageView = findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        sign = findViewById(R.id.tv_sign);
        ll = findViewById(R.id.ll);
        qrImage = findViewById(R.id.tv_qrcode);
        userIcon = findViewById(R.id.iv_userimg);
        tvUserName = findViewById(R.id.tv_username);
        tvAppName = findViewById(R.id.tv_appname);
        tvShareMsg = findViewById(R.id.tv_sharemsg);

        btn_close = findViewById(R.id.btn_close);
        tv_finish = findViewById(R.id.tv_finish);
        tv_finish.setText(" 刚刚在『" + "剑桥小说馆" + "』上完成了打卡");
        tv_finish.setVisibility(View.INVISIBLE);

        //关闭打卡页面弹出提示
        dialog = new MaterialDialog(SignActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("点击下边的打卡按钮，成功分享至微信朋友圈才算成功打卡，才能领取红包哦！确定退出么？");
        dialog.setPositiveButton("继续打卡", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("去意已决", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

            }
        });


        //当天再次打卡成功后显示
        dialog_share = new MaterialDialog(SignActivity.this);
        dialog_share.setTitle("提醒");
//        dialog_share.setMessage("今日已打卡，不能再次获取红包或积分哦！");
        dialog_share.setPositiveButton("好的", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share.dismiss();
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initBackGround() {

        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        String url = "http://static." + Constant.IYBHttpHead + "/images/mobile/" + day + ".jpg";

//        bitmap = returnBitMap(url);
        Glide.with(mContext).load(url).placeholder(R.drawable.sign_background).error(R.drawable.sign_background).into(imageView);
        final String userIconUrl = "http://api." + Constant.IYBHttpHead2 + "/v2/api.iyuba?protocol=10005&uid="
                + AccountManager.getInstance().userId + "&size=middle";


        GlideUtil.loadImg(mContext,userIconUrl,R.drawable.icon_default_user,userIcon);


        Log.d("diao", "initBackGround: " + AccountManager.getInstance().userId + ":" + AccountManager.getInstance().userName);
        if (TextUtils.isEmpty(AccountManager.getInstance().userName)) {
            tvUserName.setText(AccountManager.getInstance().userId);
        } else {
            tvUserName.setText(AccountManager.getInstance().userName);
        }
        tvAppName.setText("『" + "剑桥小说馆" + "』" + "-  英语学习必备软件");

        //Glide.with(mContext).load("").placeholder(R.drawable.qrcode).into(qrImage);
        qrImage.setImageResource(R.drawable.qr_code);

    }


    public void writeBitmapToFile() {
        View view = getWindow().getDecorView();
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.measure(0, 0);
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        Log.d("diao", "writeBitmapToFile: "+view.getMeasuredWidth()+":"+view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return;
        }

        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();

        File newpngfile = new File(Environment.getExternalStorageDirectory(), "aaa.png");
        if (newpngfile.exists()) {
            newpngfile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(newpngfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_finish.setVisibility(View.GONE);
    }


    private void startInterfaceADDScore(String userID, String appid) {
        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        String time = Base64Coder.encode(dateString);

        String url = "http://api." + Constant.IYBHttpHead + "/credits/updateScore.jsp?srid=81&mobile=1" + "&uid=" + userID
                + "&appid=" + appid + "&flag=" + time;
        SignBean bean;

        SignBeanRequest signBeanRequest = new SignBeanRequest(url, new RequestCallBack() {
            @Override
            public void requestResult(Request result) {

                SignBeanRequest sr = (SignBeanRequest) result;


                final SignBean bean = new Gson().fromJson(sr.json.toString(), SignBean.class);
                if (bean.getResult().equals("200")) {
                    money = bean.getMoney();
                    addCredit = bean.getAddcredit();
                    days = bean.getDays();
                    totalCredit = bean.getTotalcredit();

                    //打卡成功,您已连续打卡xx天,获得xx元红包,关注[爱语课吧]微信公众号即可提现!
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float moneyThisTime = Float.parseFloat(money);

                            if (moneyThisTime > 0) {
                                float allmoney = Float.parseFloat(totalCredit);


                                ConfigManagerSet.Instance()
                                        .putString("money", totalCredit); //钱包


                                MobclickAgent.onEvent(SignActivity.this, "dailybonus"); //友盟打卡统计
                                dialog_share.setMessage("打卡成功," + "您已连续打卡" + days + "天,获得" + floatToString(moneyThisTime) + "元,总计: " + floatToString(allmoney) + "元," + "满十元可在\"爱语课吧\"公众号提现");
                                dialog_share.show();
//                                Toast.makeText(mContext, "打卡成功," + "您已连续打卡" + days + "天,  获得" + moneyThisTime * 0.01 + "元,总计: " + allmoney * 0.01 + "元," + "满十元可在\"爱语课吧\"公众号提现", Toast.LENGTH_LONG).show();
                            } else {
                                dialog_share.setMessage("打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit);
                                dialog_share.show();
//                                Toast.makeText(mContext, "打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit, Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog_share.setMessage("今日已打卡，重复打卡不能再次获取红包或积分哦！");
                            dialog_share.show();
//                            Toast.makeText(mContext, "您今天已经打过卡了,", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        CrashApplication.getInstance().getQueue().add(signBeanRequest);
    }


    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();


//        Bitmap bitmap = BitmapFactory.decodeResource(R.drawable.abc_ab_share_pack_holo_dark, )

        oks.setPlatform(WechatMoments.NAME);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setImagePath(Environment.getExternalStorageDirectory() + "/aaa.png");//aaa


        oks.setSilent(true);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtils .e("--分享成功===", "....");
                startInterfaceADDScore(userID, AppId);
                tv_finish.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.e("okCallbackonError", "onError");
                LogUtils.e("--分享失败===", throwable.toString());
                tv_finish.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.e("okCallbackonError", "onError");
                LogUtils.e("--分享取消===", "....");
                tv_finish.setVisibility(View.INVISIBLE);
            }
        });
        // 启动分享GUI
        oks.show(context);
    }


    private String floatToString(float fNumber) {

        fNumber = (float) (fNumber * 0.01);

        DecimalFormat myformat = new java.text.DecimalFormat("0.00");
        String str = myformat.format(fNumber);
        return str;
    }

    @NeedsPermission({ android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE})
    void openCarmer() {
        //获取权限后操作
    }

    @OnPermissionDenied({android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SignActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}



