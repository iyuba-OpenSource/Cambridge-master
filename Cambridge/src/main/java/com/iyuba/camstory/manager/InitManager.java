package com.iyuba.camstory.manager;

import android.app.Application;
import android.content.Context;

import com.iyuba.camstory.utils.ShareHelper;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.imooclib.IMooc;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.voa.activity.setting.Constant;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.commonsdk.UMConfigure;

import personal.iyuba.personalhomelibrary.PersonalHome;

public class InitManager {
    private static Context mContext;
    private static InitManager instance;

    public static synchronized InitManager getInstance(Context context){
        mContext = context;
        if (instance == null){
            instance = new InitManager();
        }
        return instance;
    }

    public void initLib(){
        ShareHelper.init(mContext);
//        SpeechUtility.createUtility(mContext, SpeechConstant.APPID
//                + "=528db2fa");
        initLiving();
        IHeadlineManager.appId = com.iyuba.configation.Constant.APPID;
        //微课
        DLManager.init(mContext, 8);
        IMooc.init(mContext, Constant.getAppid(),Constant.TOPICID);
        //看一看
        BasicFavorDBManager.init(mContext);
        //视频模块
        BasicDLDBManager.init(mContext);
        BasicFavorDBManager.init(mContext);
        BasicFavorInfoHelper.init(mContext);
        //个人中心模块
        PersonalHome.init(mContext);

        //初始化友盟
        String channel = ChannelReaderUtil.getChannel(mContext);
        UMConfigure.init(mContext,"538d5e0756240beaab073242",channel,UMConfigure.DEVICE_TYPE_PHONE,"");
    }

    private void initLiving() {

//        EngineRunner.init(mContext, null);
//        Mqtt.initialize(mContext, MqttService.class);
//        x.Ext.init((Application) mContext.getApplicationContext());
    }
}
