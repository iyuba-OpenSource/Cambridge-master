package com.iyuba.camstory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.listener.OnResultListener;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.PayManager;
import com.iyuba.camstory.pay.PayOrderActivity;
import com.iyuba.camstory.setting.ConfigManagerSet;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.NetStatusUtil;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.vip.BuyIyubiActivity;
import com.iyuba.camstory.vip.BuyVIPAdapter;
import com.iyuba.camstory.vip.BuyVIPItem;
import com.iyuba.camstory.vip.BuyVIPParser;
import com.iyuba.camstory.widget.CircularImageView;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 会员中心金色版
 * create 2019-03-06 zh
 */
public class VipCenterGoldActivity extends AppCompatActivity {

    public static final String VIP_STATIC="vip_static";

    @BindView(R.id.user_photo)
    CircularImageView userPhoto;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_vip_time)
    TextView tvVipTime;
    @BindView(R.id.tv_vip_introduce)
    TextView tvVipIntroduce;
    @BindView(R.id.tv_vip_all)
    TextView tvVipAll;
    @BindView(R.id.tv_vip_only)
    TextView tvVipOnly;
    @BindView(R.id.tv_vip_gold)
    TextView tvVipGold;
    @BindView(R.id.gv_vip)
    GridView gvVip;
    @BindView(R.id.rv_vip_select_list)
    RecyclerView rvVipSelectList;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_go_buy)
    TextView tvGoBuy;
    @BindView(R.id.tv_vip_function)
    TextView tvFunction;
    @BindView(R.id.tv_buy_iyubi)
    TextView tvBuyIyubi;
    @BindView(R.id.tv_vip_iyubi)
    TextView tvIyubiNumber;

    @BindView(R.id.ll_function)
    LinearLayout llFunction;

    private Context mContext;
    private int[] icon = {R.drawable.ic_vip_function1, R.drawable.ic_vip_function2,
            R.drawable.ic_vip_function3, R.drawable.ic_vip_function4, R.drawable.ic_vip_function5,
            R.drawable.ic_vip_function6, R.drawable.ic_vip_function7, R.drawable.ic_vip_function8,
            R.drawable.ic_vip_function9};
    private String[] iconName = {"无广告(开屏除外)", "尊贵标识", "调节语速", "高速无限下载", "查看解析",
            "智慧化评测", "PDF导出", "全部应用", "换话费"};
    private String[] iconInfo = {"去除所有烦人的广告(开屏除外)", "亮着VIP尊贵标识", "选择自由调节语速",
            "享受VIP高速通道，无限下载", "查看考试类所有试题答案解析", "享受智能化无限语音评测", "文章pdf无限导出",
            "使用app.iyuba.cn旗下所有APP", "积分商城换取不同价值手机充值卡"};
    private List<Map<String, Object>> data_list;
    private SimpleAdapter simAdapter;

    private BuyVIPAdapter mAdapter;

    private List<BuyVIPItem> siteVipItems;
    private List<BuyVIPItem> goldenVipItems;
    private List<BuyVIPItem> appVipItems;

    private String validity;
    private String iyubiNumber;
    private int vipStatus;
    private static final String TAG = "VipCenterGoldActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vip_center_layout);
        ButterKnife.bind(this);

        mContext = this;

        tvVipAll.setSelected(false);
        tvVipOnly.setSelected(true);
        tvVipGold.setSelected(false);
        llFunction.setVisibility(View.GONE);

        BuyVIPParser parser = new BuyVIPParser(this);
        siteVipItems = parser.parse(R.xml.buy_site_vip_items);
        goldenVipItems = parser.parse(R.xml.buy_golden_vip_items);
        appVipItems = parser.parse(R.xml.buy_app_vip_items);
        initListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        rvVipSelectList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BuyVIPAdapter();
        rvVipSelectList.setAdapter(mAdapter);
        mAdapter.setData(siteVipItems);

        int vipSelect=getIntent().getIntExtra(VIP_STATIC,0);
//        if (vipSelect==2){
//            tvVipGold.performClick();
//        }
        switch (vipSelect){
            case 0:
                tvFunction.setText(getResources().getString(R.string.app_vip_description));
                break;
            case 1:
                tvFunction.setText(getResources().getString(R.string.all_vip_description));
                break;
            case 2:
                tvFunction.setText(getResources().getString(R.string.golden_vip_description));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoRefresh();//刷新数据
    }

    private void initListener() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        data_list = new ArrayList<Map<String, Object>>();
        String[] from = {"image", "text"};
        int[] to = {R.id.iv_icon, R.id.tv_text};
        getData();
        simAdapter = new SimpleAdapter(this, data_list, R.layout.item_gridview, from, to);
        gvVip.setAdapter(simAdapter);
        gvVip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence message = iconInfo[position];
                if (position==7){
                    message = Html.fromHtml("使用于<a href=http://app.iyuba.cn>app.iyuba.cn</a>旗下所有APP");
                }
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_web,null);
                TextView textView =view1.findViewById(R.id.tv_message_web);
                textView.setText(message);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                new AlertDialog.Builder(mContext)
                        .setView(view1)
                        .setPositiveButton(getString(R.string.sure),null)
                        .create()
                        .show();
            }
        });
        gvVip.setVisibility(View.GONE);

        //设置
        llFunction.setVisibility(View.VISIBLE);
        //全站会员
        tvVipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVipAll.setSelected(true);
                tvVipOnly.setSelected(false);
                tvVipGold.setSelected(false);
                mAdapter.mSelectedItem=null;
                gvVip.setVisibility(View.GONE);
                llFunction.setVisibility(View.VISIBLE);
                tvFunction.setText(R.string.all_vip_description);
                tvVipIntroduce.setText(getString(R.string.vip_explain_no));
                mAdapter.setData(appVipItems);
            }
        });
        //本应用会员
        tvVipOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVipAll.setSelected(false);
                tvVipOnly.setSelected(true);
                tvVipGold.setSelected(false);
                mAdapter.mSelectedItem=null;
                gvVip.setVisibility(View.GONE);
                llFunction.setVisibility(View.VISIBLE);
                tvFunction.setText(R.string.app_vip_description);
                tvVipIntroduce.setText(getString(R.string.vip_explain_no));
                mAdapter.setData(appVipItems);
            }
        });
        //黄金会员
        tvVipGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVipAll.setSelected(false);
                tvVipOnly.setSelected(false);
                tvVipGold.setSelected(true);
                mAdapter.mSelectedItem=null;
                gvVip.setVisibility(View.GONE);
                llFunction.setVisibility(View.VISIBLE);
                tvFunction.setText(R.string.golden_vip_description);
                mAdapter.setData(goldenVipItems);
                tvVipIntroduce.setText(getString(R.string.vip_explain));
            }
        });
        //购买爱语币
        tvBuyIyubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BuyIyubiActivity.buildIntent(mContext));
            }
        });

//        tvVipIntroduce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, WebActivity.class);
//                intent.putExtra("url", "http://vip." + Constant.IYBHttpHead + "/vip/vip.html?"
//                        + "&uid=" + AccountManager.getInstance().getUId()
//                        + "&sign=" + MD5.getMD5ofStr("iyuba" + AccountManager.getInstance().getUId() + "camstory")
//                        + "&username=" + AccountManager.getInstance().userName
//                        + "&platform=android&appid="
//                        + Constant.getAppid());
//                intent.putExtra("title", "vip说明");
//                startActivity(intent);
//            }
//        });

        //购买会员

        //购买
        tvGoBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyVIPItem item = mAdapter.getSelectedItem();
                if (item == null) {
                    ToastUtil.showToast(mContext, "请选择要开通的VIP!");
                } else {
                    boolean isLogin = AccountManager.getInstance().checkUserLogin();
                    if (!isLogin || AccountManager.getInstance().islinshi) {
                        ToastUtil.showToast(mContext, "请使用正式账号登录后再购买!");
                    } else {
                        String info = getString(R.string.buy_app_vip_body_info, item.name);
                        String subject = "";
                        if (item.productId == 0) {
                            subject = "全站vip";
                        } else if (item.productId == 10) {
                            subject = "永久vip";
                        } else {
                            subject = "黄金vip";
                        }
                        Intent intent = new Intent(mContext, PayOrderActivity.class);
                        String price = String.valueOf(item.price);
                        //String price = "0.01";
                        intent.putExtra("month", item.month);
                        intent.putExtra("type", item.productId);
                        intent.putExtra("out_trade_no", getOutTradeNo());
                        intent.putExtra("subject", subject);//"全站vip"
                        intent.putExtra("body", info);//"花费" + price + "元购买全站vip"
                        intent.putExtra("price", price + "");  //价格
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            map.put("detail", iconInfo[i]);
            data_list.add(map);
        }
        return data_list;
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + Math.abs(r.nextInt());
        key = key.substring(0, 15);
        return key;
    }

    private void Assignment() {
        AccountManager accountManager = AccountManager.getInstance();
        tvUserName.setText(accountManager.userName);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(800)).delayBeforeLoading(400)
                .showImageOnFail(R.mipmap.defaultavatar).build();
        ImageLoader.getInstance()
                .displayImage(Constant.getUserimage() + accountManager.userId + "&size=big",
                        userPhoto, options);

        if (vipStatus >= 1) {
            tvVipTime.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_vip_logo_white), null, null, null);
            tvVipTime.setCompoundDrawablePadding(10);
            tvVipTime.setText(validity);//vip时间
        } else {
            tvVipTime.setText(R.string.person_common_user);//普通用户
        }

        tvIyubiNumber.setText("爱语币：" + iyubiNumber);
    }

    private void autoRefresh() {
        LogUtils.e("isvip", com.iyuba.camstory.lycam.manager.ConfigManager.Instance().loadInt("isvip") + "");
        LogUtils.e("isvipo", AccountManager.getInstance().getVipStatus());
        LogUtils.e("isvipooo", ConfigManagerSet.Instance().loadInt("isvip") + "");
        vipStatus = Integer.valueOf(AccountManager.getInstance().getVipStatus());
        validity = ConfigManagerSet.Instance().loadString("validity");
        iyubiNumber = ConfigManagerSet.Instance().loadString("iyubi");

        LogUtils.e("vip到期时间", validity + "=====");
        if (!NetStatusUtil.isConnected(mContext)) {
            LogUtils.e(TAG, "no net");
            ToastUtil.showToast(mContext, "连接失败，请检查网络");
//            if (AccountManager.getInstance().checkUserLogin() || AccountManager.getInstance().islinshi) {
//                Assignment();
//            }
        } else if (AccountManager.getInstance().checkUserLogin() || AccountManager.getInstance().islinshi) {
            Assignment();
            LogUtils.e(TAG, "after checkAmount");
            checkAmount();
            AccountManager.getInstance().VipRefresh(mContext, new AccountManager.VipRefreshListener() {
                @Override
                public void onVipRefresh(boolean isVip) {
                    Assignment();
                }
            });
        }
    }

    //查询余额
    public void checkAmount() {
        if (NetStatusUtil.isConnected(mContext)) {
            PayManager.getInstance(mContext).checkAmount(AccountManager.getInstance().userId,
                    new OnResultListener() {

                        @Override
                        public void OnSuccessListener(String msg) {
                            ConfigManagerSet.Instance().putString("currUserAmount", msg);
                        }

                        @Override
                        public void OnFailureListener(String msg) {
                            ConfigManagerSet.Instance().putString("currUserAmount", "0");
                        }
                    });
        }
    }
}
