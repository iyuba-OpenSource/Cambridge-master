package com.iyuba.camstory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.stetho.common.LogUtil;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.VipCenterGoldActivity;
import com.iyuba.camstory.constant.AdTestKeyData;
import com.iyuba.camstory.lil.ad.show.AdShowUtil;
import com.iyuba.camstory.lil.util.ScreenUtil;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.SetUserLibUtils;
import com.iyuba.configation.Constant;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.event.HeadlineGoVIPEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivity;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.iyuba.headlinelibrary.ui.title.HolderType;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * VideoActivity
 * 侧边栏视频
 * @author wayne
 * @date 2017/11/14
 */
public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        EventBus.getDefault().register(this);

        //User set
        SetUserLibUtils.setUserInfoLib(this);

        //初始化配置
        initConfig();

        String[] types = new String[]{
                //HeadlineType.ALL,
                //HeadlineType.ALL,
//                HeadlineType.MEIYU,
                HeadlineType.VOAVIDEO,
                HeadlineType.TED,
                HeadlineType.BBCWORDVIDEO,
                HeadlineType.TOPVIDEOS,
                HeadlineType.SMALLVIDEO
        };

        //注意参数：
        //HolderType.LARGE--表示大图显示
        //HolderType.SMALL--表示小图显示
        Bundle bundle = DropdownTitleFragmentNew.buildArguments(10, HolderType.LARGE,types,false);

        DropdownTitleFragmentNew fragment = DropdownTitleFragmentNew.newInstance(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,fragment).show(fragment).commitNowAllowingStateLoss();
    }

    //初始化配置
    private void initConfig(){
        //替换为小视频
        IHeadlineManager.appId = String.valueOf(Constant.APPID);
        IHeadlineManager.appName = "camstory";
        //设置广告类型
        IHeadline.setAdAppId(String.valueOf(AdShowUtil.NetParam.AppId));
        IHeadline.setStreamAdPosition(AdShowUtil.NetParam.SteamAd_startIndex,AdShowUtil.NetParam.SteamAd_intervalIndex);
        IHeadline.setYoudaoStreamId(AdTestKeyData.KeyData.TemplateAdKey.template_youdao);
        IHeadline.setYdsdkTemplateKey(AdTestKeyData.KeyData.TemplateAdKey.template_csj,AdTestKeyData.KeyData.TemplateAdKey.template_ylh,AdTestKeyData.KeyData.TemplateAdKey.template_ks,AdTestKeyData.KeyData.TemplateAdKey.template_baidu,AdTestKeyData.KeyData.TemplateAdKey.template_vlion);
        //设置广告自适应
        int adWidth = ScreenUtil.getScreenW(this);
        IHeadline.setYdsdkTemplateAdWidthHeight(adWidth,0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BasicDLPart event) {
        jumpToCorrectDLActivityByCate(this, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent dlEvent) {
        //视频下载后点击
        BasicDLPart dlPart = dlEvent.items.get(dlEvent.position);
        switch (dlPart.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(this,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivity.getIntent2Me(this,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
        }

    }

    @Subscribe
    public void onEvent(HeadlineGoVIPEvent event){
        if (AccountManager.getInstance().islinshi) {
            Toast.makeText(this, "请登录账号", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, VipCenterGoldActivity.class));
        }
    }

    public void jumpToCorrectDLActivityByCate(Context context, BasicDLPart basicHDsDLPart) {
        switch (basicHDsDLPart.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(context,
                        basicHDsDLPart.getCategoryName(), basicHDsDLPart.getTitle(),basicHDsDLPart.getTitleCn(),
                        basicHDsDLPart.getPic(), basicHDsDLPart.getType(), basicHDsDLPart.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "japanvideos":
            case "topvideos":
                startActivity(VideoContentActivity.getIntent2Me(context,
                        basicHDsDLPart.getCategoryName(), basicHDsDLPart.getTitle(),
                        basicHDsDLPart.getTitleCn(),
                        basicHDsDLPart.getPic(), basicHDsDLPart.getType(), basicHDsDLPart.getId()));
                break;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ChangeVideoEvnet videoEvnet) {
//        LogUtil.e("Event刷新ChangeVideoEvnet"+" 用户名"+ConfigManager.Instance().loadString("userName"));
//        IHeadlineManager.userId = Integer.parseInt(ConfigManager.Instance().loadString("userId"));
//        IHeadlineManager.username = ConfigManager.Instance().loadString("userName");
//        IHeadlineManager.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
//
//        if (isTime()) {
//            IHeadlineManager.vipStatus = "1";
//        }
//        LogUtil.e("时间 是不是VIP：" + IHeadlineManager.isVip());
//
//        ImoocManager.userId = ConfigManager.Instance().loadString("userId");
//        ImoocManager.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
//    }

    private boolean isTime() {
        long time = System.currentTimeMillis() / 1000;
        long flagTime = 1546922614;//1546922614 1547030614
        if (flagTime - time > 0) {
            long i = flagTime - time;
            LogUtil.e("时间还没到，剩余时间：" + i);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
