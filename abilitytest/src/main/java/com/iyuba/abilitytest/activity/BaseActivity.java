package com.iyuba.abilitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.FragmentActivity;
import android.view.Window;

import com.iyuba.abilitytest.manager.UserinfoManager;
//import com.iyuba.core.common.activity.Login;
import com.umeng.analytics.MobclickAgent;

/**
 * 应用基类
 */
public abstract class BaseActivity extends com.iyuba.abilitytest.activity.base.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initCommons();
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    private void initCommons() {
        /*Button btn_nav = (Button) findViewById(R.id.btn_nav);
        if (btn_nav != null) {
            btn_nav.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });
        }*/

    }

    /**
     * 返回用于显示界面的id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化变量,包括intent携带的数据和activity内的变量
     */
    protected abstract void initVariables();

    /**
     * 加载layout布局,初始化控件,为事件挂上事件的方法
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 调用mobileAPI
     */
    protected abstract void loadData();

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


    /**
     * 判断用户有没有登录的方法,没有登录去登录呀
     */
    public boolean checkUserLoginAndLogin() {
        // 用户是否登录
        boolean isLogIn = UserinfoManager.getInstance().getLoginState();
        if (isLogIn) {
            return true;
        } else {

            if (true)
                return true;  // to be deleted
//            Intent intent = new Intent();
//            intent.setClass(getApplicationContext(), Login.class);
//            startActivity(intent);
            return false;
        }
    }

    /**
     * 判断用户有没有登录的方法
     */
    public boolean isUserLogin() {
        // 用户是否登录
        boolean isLogIn = UserinfoManager.getInstance().getLoginState();
        return isLogIn;
    }

}
