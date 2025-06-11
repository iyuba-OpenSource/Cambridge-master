package com.iyuba.camstory.lil.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.camstory.R;
import com.iyuba.camstory.constant.AdTestKeyData;
import com.iyuba.camstory.lil.ad.show.AdShowUtil;
import com.iyuba.camstory.lil.util.ScreenUtil;
import com.iyuba.configation.Constant;
import com.iyuba.imooclib.IMooc;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;

import java.util.ArrayList;

/**
 * 微课的展示界面
 */
public class MocShowActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent intent = new Intent(context, MocShowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_show);

        initFragment();
    }

    private void initFragment(){
        //设置appId
        ImoocManager.appId = String.valueOf(Constant.APPID);
        //根据要求显示有道广告
        IMooc.setAdAppId(String.valueOf(AdShowUtil.NetParam.AppId));
        IMooc.setStreamAdPosition(AdShowUtil.NetParam.SteamAd_startIndex, AdShowUtil.NetParam.SteamAd_intervalIndex);
        IMooc.setYoudaoId(AdTestKeyData.KeyData.TemplateAdKey.template_youdao);
        IMooc.setYdsdkTemplateKey(AdTestKeyData.KeyData.TemplateAdKey.template_csj,AdTestKeyData.KeyData.TemplateAdKey.template_ylh,AdTestKeyData.KeyData.TemplateAdKey.template_ks,AdTestKeyData.KeyData.TemplateAdKey.template_baidu,AdTestKeyData.KeyData.TemplateAdKey.template_vlion);
        //设置广告自适应
        int adWidth = ScreenUtil.getScreenW(this);
        IMooc.setYdsdkTemplateAdWidthHeight(adWidth,0);

        //显示内容
        ArrayList<Integer> typeIdFilter = new ArrayList<>();
        typeIdFilter.add(-2);//全部
        typeIdFilter.add(-1);//最新
        typeIdFilter.add(2);//四级
        typeIdFilter.add(3);//VOA
        typeIdFilter.add(4);//六级
        typeIdFilter.add(7);//托福
        typeIdFilter.add(8);//考研
        typeIdFilter.add(9);//BBC
        typeIdFilter.add(21);//新概念
        typeIdFilter.add(22);//走遍美国
        typeIdFilter.add(28);//学位
        typeIdFilter.add(52);//考研二
        typeIdFilter.add(61);//雅思
        typeIdFilter.add(91);//中职

        Bundle bundle = MobClassFragment.buildArguments(21,true,typeIdFilter);
        MobClassFragment mobClassFragment = MobClassFragment.newInstance(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.showView,mobClassFragment).show(mobClassFragment).commitNowAllowingStateLoss();
    }
}
