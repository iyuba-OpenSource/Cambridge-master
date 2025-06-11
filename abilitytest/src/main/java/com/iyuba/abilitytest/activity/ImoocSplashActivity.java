package com.iyuba.abilitytest.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityQuestion;

import com.iyuba.abilitytest.manager.UserinfoManager;
import com.iyuba.http.http.Http;
import com.iyuba.http.http.HttpCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.activity
 * @class describe
 * @time 2018/6/5 15:17
 * @change
 * @chang time
 * @class describe
 * 从微课进入答题跳转的Activity,页面简单
 */
public class ImoocSplashActivity extends Activity {


    private String lessonId;
    private String lessonType;

    Dialog waitDialog;

    Context mContext;

    private String getTestUrl;

    public static final String TAG = ImoocSplashActivity.class.getSimpleName();

    private ArrayList<AbilityQuestion.TestListBean> mQuesLists;//题目详情

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mQuesLists = new ArrayList<AbilityQuestion.TestListBean>();
        lessonId = getIntent().getStringExtra("lessonId");
        lessonType = getIntent().getStringExtra("lessonType");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        String date =format.format( new Date());

        String sign = com.iyuba.abilitytest.utils.MD5.getMD5ofStr(lessonType+"A"+date);


        getTestUrl ="http://class."+com.iyuba.configation.Constant.IYBHttpHead+"/getClass.iyuba?protocol=" +
                "20000&format=json&category=A&sign="+sign+"&mode=2&uid="+ UserinfoManager.getInstance().getUSERID()+"&lesson="+lessonType+"&lessonid="+lessonId;

        waitDialog.setContentView(R.layout.dlg_waitting);
        Objects.requireNonNull(waitDialog.getWindow()).setBackgroundDrawableResource(R.color.transparent);


        Log.d(TAG, "onCreate: "+getTestUrl);
        showWaitDialog();
        Http.get(getTestUrl, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {

                AbilityQuestion question = new Gson().fromJson(response,AbilityQuestion.class);
                for (AbilityQuestion.TestListBean bean : question.getTestList()){
                    mQuesLists.add(bean);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext,AbilityTestActivity.class);
                        intent.putExtra("QuestionList",mQuesLists);
                        intent.putExtra("testTime", "10000");
                        intent.putExtra("mode", "1");
                        intent.putExtra("lessonType", lessonType);
//                        intent.putExtra("testCategory", "");
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onError(Call call, Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                        dismissDialog();

                        finish();
                    }
                });

            }
        });

    }

    private void showWaitDialog() {
        waitDialog.show();
    }

    private void dismissDialog() {
        waitDialog.dismiss();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
