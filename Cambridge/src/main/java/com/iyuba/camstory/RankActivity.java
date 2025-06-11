package com.iyuba.camstory;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.iyuba.camstory.adpater.RankFragmentAdapter;
import com.iyuba.camstory.fragment.RankFragment;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.configation.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by Ivot on 2016/12/29.
 */

public class RankActivity extends FragmentActivity implements RankFragment.RankFragmentInteraction {
    @BindView(R.id.rank_listen)
    TextView rankListen;
    @BindView(R.id.rank_study)
    TextView rankStudy;
    @BindView(R.id.iv_title_back)
    ImageButton buttonBack;
    @BindView(R.id.titlebar_overflow_button)
    ImageButton share;
    private ViewPager viewPager;
    private String shareUrl;
    private String shareType = "听力";
    private List<Fragment> listfragment;
    private String listenUrl, listenText, studyText, studyUrl;

    RankFragment f1 = RankFragment.newInstance("听力");
    RankFragment f3 = RankFragment.newInstance("学习");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank);
        ButterKnife.bind(this);
        viewPager = findViewById(R.id.view_pager);
        shareUrl = "http://m." + Constant.IYBHttpHead + "/i/getRanking.jsp?" + "uid=" + AccountManager.getInstance().userId + "&appId=" + Constant.APPID + "&sign=" + MD5.getMD5ofStr(AccountManager.getInstance().userId + "ranking" + Constant.APPID) + "&topic=&" + "rankingType=listening";


        //设置阅读按钮颜色
        resetBtnColor(R.id.rank_listen);
        // 设置viewpager的滑动事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int viewId;
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        viewId = R.id.rank_listen;
                        resetBtnColor(viewId);
                        break;
                    case 1:
                        viewId = R.id.rank_study;
                        resetBtnColor(viewId);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        listfragment = new ArrayList<Fragment>(); //new一个List<Fragment>

        listfragment.add(f1);
        listfragment.add(f3);

        FragmentManager fm = getSupportFragmentManager();
        RankFragmentAdapter mfpa = new RankFragmentAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数

        viewPager.setAdapter(mfpa);
        viewPager.setCurrentItem(0); //设置当前页是第一页
    }

    /**
     * 重置按钮颜色
     */
    private void resetBtnColor(int viewId) {

        rankListen.setSelected(false);
        rankListen.setTextColor(0xff239bf0);

        rankStudy.setSelected(false);
        rankStudy.setTextColor(0xff239bf0);

        switch (viewId) {
            case R.id.rank_listen:
                rankListen.setSelected(true);
                rankListen.setTextColor(0xffffffff);
                break;
            case R.id.rank_study:
                rankStudy.setSelected(true);
                rankStudy.setTextColor(0xffffffff);
                break;
        }
    }

    @OnClick({R.id.rank_listen, R.id.rank_study, R.id.iv_title_back, R.id.titlebar_overflow_button})
    public void onRankClick(View v) {
        //点击事件

        switch (v.getId()) {
            case R.id.rank_listen:

                if (!rankListen.isSelected()) {
                    resetBtnColor(R.id.rank_listen);
                    viewPager.setCurrentItem(0);
                    // http://m."+Constant.IYBHttpHead+"/i/getRanking.jsp?uid=5085392&appId=148&sign=9ca1e2b36147a78f22049eefe0e2b2eb&topic=&rankingType=reading
                    shareUrl = "http://m." + Constant.IYBHttpHead + "/i/getRanking.jsp?" + "uid=" + AccountManager.getInstance().userId + "&appId=" + Constant.APPID + "&sign=" + MD5.getMD5ofStr(AccountManager.getInstance().userId + "ranking" + Constant.APPID) + "&topic=&" + "rankingType=listening";
                }
                break;
            case R.id.rank_study:
                if (!rankStudy.isSelected()) {
                    resetBtnColor(R.id.rank_study);
                    viewPager.setCurrentItem(2);
                    shareUrl = "http://m." + Constant.IYBHttpHead + "/i/getRanking.jsp?" + "uid=" + AccountManager.getInstance().userId + "&appId=" + Constant.APPID + "&sign=" + MD5.getMD5ofStr(AccountManager.getInstance().userId + "ranking" + Constant.APPID) + "&topic=&" + "rankingType=studying";
                }
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.titlebar_overflow_button:
                String text = "我在剑桥英语馆" + shareType + "排行榜排名";
                String imagePath = "http://static2." + Constant.IYBHttpHead + "/camstory/images/camstory.png";
                String url = com.iyuba.voa.activity.setting.Constant.getAppshareurl();
                String title = com.iyuba.voa.activity.setting.Constant.getAppname();
                showShare(text, imagePath, shareUrl, "排行榜");
                break;


        }

    }

    //分享方法
    private void showShare(String text, String imagePath, String url, String title) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imagePath);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(com.iyuba.voa.activity.setting.Constant.getAppname());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(com.iyuba.voa.activity.setting.Constant.getAppname());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int arg1, Throwable arg2) {
                CustomToast.showToast(RankActivity.this, "分享失败", 1000);
            }

            @Override
            public void onComplete(Platform platform, int arg1,
                                   HashMap<String, Object> arg2) {
                CustomToast.showToast(RankActivity.this, "分享成功", 1000);


            }

            @Override
            public void onCancel(Platform platform, int arg1) {
                //CustomToast.showToast(RankActivity.this, "分享已取消", 1000);
            }
        });

        // 启动分享GUI
        oks.show(this);
    }

    private void gerShare() {

        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.removeAccount(true);
        ShareSDK.removeCookieOnAuthorize(true);
        String text = "", siteUrl = "";

        String imageUrl = "http://app." + Constant.IYBHttpHead + "/android/images/newconcept/newconcept.png";

        if (rankListen.isSelected()) {

            text = listenText;
            siteUrl = shareUrl;
        }
        if (rankStudy.isSelected()) {
            text = studyText;
            siteUrl = studyUrl;
        }

        if (text == null) {
            Log.e("字段为空", "===");
            return;
        }

        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("小说馆排行榜");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(siteUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");
        // imageUrl是Web图片路径，sina需要开通权限
        oks.setImageUrl(imageUrl);
        // url仅在微信（包括好友和朋友圈）中使用

        oks.setUrl(siteUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("爱语吧的这款应用" + getResources().getString(R.string.app_name) + "真的很不错啊~推荐！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getResources().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(siteUrl);
        // oks.setDialogMode();
        // oks.setSilent(false);
        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享失败===", arg2.toString());

            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                Log.e("okCallbackonComplete", "onComplete");
//                if (AccountManager.Instance(RankActivity.this).userId != null) {
                Message msg = new Message();
                msg.obj = arg0.getName();
                if (arg0.getName().equals("QQ")
                        || arg0.getName().equals("Wechat")
                        || arg0.getName().equals("WechatFavorite")) {
                    msg.what = 49;
                } else if (arg0.getName().equals("QZone")
                        || arg0.getName().equals("WechatMoments")
                        || arg0.getName().equals("SinaWeibo")
                        || arg0.getName().equals("TencentWeibo")) {
                    msg.what = 19;
                }
//                    handler.sendMessage(msg);

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                Log.e("okCallbackonCancel", "onCancel");
            }
        });
        // 启动分享GUI
        oks.show(RankActivity.this);

    }

    @Override
    public void process(String str, String str2, String str3) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
