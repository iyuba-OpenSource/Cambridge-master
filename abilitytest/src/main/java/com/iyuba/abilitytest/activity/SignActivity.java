package com.iyuba.abilitytest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.SignBean;
import com.iyuba.abilitytest.entity.StudyTimeBeanNew;
import com.iyuba.abilitytest.manager.UserinfoManager;
import com.iyuba.abilitytest.utils.Constant;
import com.iyuba.abilitytest.widget.CustomDialog;
import com.iyuba.abilitytest.widget.WaittingDialog;
import com.iyuba.http.http.Http;
import com.iyuba.http.http.HttpCallback;


import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

@SuppressLint({"SimpleDateFormat","SetTextI18n"})
public class SignActivity extends Activity {

    private ImageView imageView;
    private ImageView qrImage;
    private TextView tv1, tv2, tv3;
    private Context mContext;
    private TextView sign;
    private CircleImageView userIcon;
    private TextView tvShareMsg;
    private int signStudyTime = 3 * 60;
    private String loadFiledHint = "打卡加载失败";

    String shareTxt;
    String getTimeUrl;
    WaittingDialog mWaittingDialogBuilder;
    LinearLayout ll;
    CustomDialog mWaittingDialog;
    String addCredit = "";//Integer.parseInt(bean.getAddcredit());
    String days = "";//Integer.parseInt(bean.getDays());
    String totalCredit = "";//bean.getTotalcredit();
    String money="";
//    private int QR_HEIGHT = 77;
//    private int QR_WIDTH = 77;

    private TextView tvUserName;
    private TextView tvAppName;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mContext = this;
        mWaittingDialogBuilder = new WaittingDialog();

//        mWaittingDialogBuilder.wettingDialog(mContext);
        mWaittingDialog = mWaittingDialogBuilder.wettingDialog(mContext);
        mWaittingDialogBuilder.setText("正在加载学习时间，请稍后");

        setContentView(R.layout.activity_sign);


        initView();

        initData();

        initBackGround();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void initData() {

        mWaittingDialog.show();

        String uid = UserinfoManager.getInstance().getUSERID();


        final String url = String.format(Locale.CHINA,
                "http://daxue."+com.iyuba.configation.Constant.IYBHttpHead+"/ecollege/getMyTime.jsp?uid=%s&day=%s&flg=1", uid, getDays());
        Log.d("dddd", url);
        getTimeUrl = url;

        Http.get(url, new HttpCallback() {

            @Override
            public void onSucceed(Call call, String response) {
                try {
                    if (null != mWaittingDialog) {
                        if (mWaittingDialog.isShowing()) {
                            mWaittingDialog.dismiss();
                        }
                    }


                    final StudyTimeBeanNew bean = new Gson().fromJson(response, StudyTimeBeanNew.class);
                    Log.d("dddd", response);
                    if ("1".equals(bean.getResult())) {
                        final int time = Integer.parseInt(bean.getTotalTime());
                        int totaltime = Integer.parseInt(bean.getTotalDaysTime());
                        tv1.setText("学习天数:\n" + bean.getTotalDays() + "");
                        tv2.setText("单词数:\n" + bean.getTotalWords() + "");
                        int rankRate =100-Integer.parseInt(bean.getRanking())*100/Integer.parseInt(bean.getTotalUser());
                        tv3.setText("超越了：\n" + rankRate+"%同学");
                        shareTxt = bean.getSentence() + "我在爱语吧坚持学习了" + bean.getTotalDays() + "天,积累了" + bean.getTotalWords()
                                + "单词如下";

                        if (time < signStudyTime) {
                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    toast(String.format(Locale.CHINA, "打卡失败，当前已学习%d秒，\n满%d分钟可打卡", time, signStudyTime / 60));

                                }
                            });
                        } else {
                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    qrImage.setVisibility(View.VISIBLE);

                                    sign.setVisibility(View.GONE);

                                    tvShareMsg.setVisibility(View.VISIBLE);

                                    tvShareMsg.setText("长按图片识别二维码");
                                    findViewById(R.id.tv_desc).setVisibility(View.VISIBLE);
                                    ((TextView)findViewById(R.id.tv_desc)).setText("刚刚在 "+ Constant.AppName+" 上完成了打卡");
                                    tvShareMsg.setBackground(getResources().getDrawable(R.drawable.sign_bg_yellow));


                                    findViewById(R.id.tv_desc).setVisibility(View.INVISIBLE);
                                    writeBitmapToFile();

                                    showShareOnMoment(mContext, UserinfoManager.getInstance().getUSERID(), Constant.APPID);

//                                        shareUrl(shareDownloadUrl,shareTxt,bitmap,"我的学习记录", SendMessageToWX.Req.WXSceneTimeline);
                                }
                            });
//                            startShareInterface(); //TODO 票圈分享
                        }
                    } else {
                        toast(loadFiledHint + bean.getResult());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(loadFiledHint + "！！");
                }
            }

            @Override
            public void onError(Call call, Exception e) {

            }
        });
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


    public void getTime(){

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


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initBackGround() {

        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        String url = "http://static."+com.iyuba.configation.Constant.IYBHttpHead+"/images/mobile/" + day + ".jpg";

        Glide.with(mContext).load(url).placeholder(R.drawable.place).dontAnimate().into(imageView);
        String userIconUrl = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid="
                + UserinfoManager.getInstance().getUSERID() + "&size=middle";
        Glide.with(mContext).load(userIconUrl).placeholder(R.drawable.noavatar_small).into(userIcon);
        Log.d("diao", "initBackGround: " + UserinfoManager.getInstance().getUSERID() + ":" + UserinfoManager.getInstance().getUserName());
        if (TextUtils.isEmpty(UserinfoManager.getInstance().getUserName())){
            tvUserName.setText(UserinfoManager.getInstance().getUSERID());
        } else {
            tvUserName.setText(UserinfoManager.getInstance().getUserName());
        }
        tvAppName.setText(Constant.AppName + "--下载即送历年考试真题书");
       // Glide.with(mContext).load("").placeholder(R.mipmap.qrcode).into(qrImage);
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

/**
 *  @author 
 *  @time
 *  @describe  启动获取积分(红包的接口)
 */

    private void startInterfaceADDScore(String userID, String appid) {

        Date currentTime = new Date();

         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        final String time = new String (Base64.encodeBase64(dateString.getBytes()));
//                Base64Coder.encode(dateString);

        String url = "http:api."+com.iyuba.configation.Constant.IYBHttpHead+"/credits/updateScore.jsp?srid=81&mobile=1&flag=" + time + "&uid=" + userID
                + "&appid=" + appid;
        Http.get(url, new HttpCallback() {

            @Override
            public void onSucceed(Call call, String response) {
                final SignBean bean = new Gson().fromJson(response, SignBean.class);
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
                            if (moneyThisTime>0) {
                                float moneyTotal = Float.parseFloat(totalCredit);
//                                AccountManager.Instace(mContext).userInfo.money = money;

                                Toast.makeText(mContext, "打卡成功,"+"您已连续打卡" + days + "天,  获得" +  moneyThisTime *0.01+ "元,总计: " + moneyTotal*0.01 + "元,"+"满10元可在\"爱语课吧\"公众号提现", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(mContext,"打卡成功，连续打卡"+days+"天,获得"+ addCredit+"积分，总积分: "+totalCredit, Toast.LENGTH_SHORT).show();                            }
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "打卡失败，您今天已经打过卡了", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }


            @Override
            public void onError(Call call, Exception e) {

            }
        });


    }

    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setPlatform(WechatMoments.NAME);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setImagePath(Environment.getExternalStorageDirectory() + "/aaa.png");


        oks.setSilent(true);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore(userID, AppId);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享失败===", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享取消===", "....");
            }
        });
        // 启动分享GUI
        oks.show(context);
    }

}



