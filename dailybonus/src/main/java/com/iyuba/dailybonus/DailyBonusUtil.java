package com.iyuba.dailybonus;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 每日签到
 */
public class DailyBonusUtil {
    private Activity mContext;
    private String userID;
    private String appID;
    private int windowWidth;
    private int windowHeight;
    private int signStudyTime;
    private String loadFiledHint = "打卡加载失败";

    public DailyBonusUtil(Activity activity, String userID, String appID) {
        this.mContext = activity;
        this.userID = userID;
        this.appID = appID;
        this.windowWidth = dp2px(300);
        this.windowHeight = dp2px(492);
        this.signStudyTime = 3 * 60;
    }

    public DailyBonusUtil setWidthHeight(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
        return this;
    }

    public DailyBonusUtil setOkHttpClient(OkHttpClient okHttpClient) {
        Http.setClient(okHttpClient);
        return this;
    }

    public DailyBonusUtil setTimeout(int seconds) {
        Http.setTimeout(seconds);
        return this;
    }

    public DailyBonusUtil setSignStudyTime(int seconds) {
        this.signStudyTime = seconds;
        return this;
    }

    public DailyBonusUtil setLoadFiledHint(String loadFiledHint) {
        this.loadFiledHint = loadFiledHint;
        return this;
    }

    public void sign(final DailyBonusCallback callback, final PopupWindow.OnDismissListener onDismissListener) {
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(View.inflate(mContext, R.layout.daily_bonus_progress, null));
        popupWindow.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        String url = String.format(Locale.CHINA,
                "http://daxue."+Constant.IYBHttpHead+"/ecollege/getMyTime.jsp?uid=%s&day=%s", userID, getDays());

        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                popupWindow.dismiss();
                try {
                    StudyTimeBean bean = new Gson().fromJson(response, StudyTimeBean.class);
                    if ("1".equals(bean.getResult())) {
                        int time = Integer.parseInt(bean.getTotalTime());
                        if (time < signStudyTime) {
                            toast(String.format(Locale.CHINA, "打卡失败，当前已学习%d秒，\n满%d分钟可打卡", time, signStudyTime / 60));
                        } else {
                            show(callback, onDismissListener);
                        }
                    } else {
                        toast(loadFiledHint);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(loadFiledHint);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                popupWindow.dismiss();
                toast(loadFiledHint);
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private void show(DailyBonusCallback callback, PopupWindow.OnDismissListener onDismissListener) {
        String url = String.format(Locale.CHINA,
                "http://api."+Constant.IYBHttpHead+"/credits/qiandao.jsp?uid=%s&appid=%s", userID, appID);
        DailyBonusWindow window = new DailyBonusWindow(mContext, windowWidth, windowHeight, url, callback, onDismissListener);
        window.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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

    private int dp2px(float dipValue) {
        return (int) (dipValue * mContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    public interface DailyBonusCallback {
        /**
         * 点击了签到页面广告
         *
         * @param url 广告地址
         */
        void onADClick(String url);

        void umengDailyBonusEvent();
    }
}