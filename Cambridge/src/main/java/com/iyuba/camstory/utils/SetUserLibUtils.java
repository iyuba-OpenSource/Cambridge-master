package com.iyuba.camstory.utils;

import android.content.Context;

import com.iyuba.camstory.MainActivity;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.PersonalType;

public class SetUserLibUtils {

    public static void setUserInfoLib(Context context){
        User user = new User();
        AccountManager manager = AccountManager.getInstance();
        if (manager.checkUserLogin()) {
            user.vipStatus = (manager.isVip(context))? manager.getVipStatus():"0";
            user.uid = manager.userId;
            user.name = manager.userName;
            //传登陆后的用户信息给个人中心模块
            PersonalHome.setAppInfo(Constant.APPID,Constant.AppName);
            PersonalHome.setCategoryType(PersonalType.VOA);
            PersonalHome.setMainPath(MainActivity.class.getName());
            PersonalHome.setSaveUserinfo(user.uid,user.name,user.vipStatus);
            IyuUserManager.getInstance().setCurrentUser(user);

        } else {
            user.vipStatus = "0";
            user.uid = 0;
            user.name = "";
            PersonalHome.setAppInfo(Constant.APPID,Constant.AppName);
            PersonalHome.setCategoryType(PersonalType.VOA);
            PersonalHome.setMainPath(MainActivity.class.getName());
            PersonalHome.setSaveUserinfo(user.uid,user.name,user.vipStatus);
            IyuUserManager.getInstance().logout();
        }

        if (AdBlocker.shouldBlockAd(context)) {
            user.vipStatus = "1";
        }
    }
}
