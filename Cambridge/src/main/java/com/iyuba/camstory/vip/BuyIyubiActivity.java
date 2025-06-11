package com.iyuba.camstory.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.iyuba.camstory.R;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.pay.PayOrderActivity;
import com.iyuba.camstory.utils.ResourcesUtil;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.WaittingDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyIyubiActivity extends AppCompatActivity {

    private static final int REQUEST_PAY_ORDER = 233;

    public static Intent buildIntent(Context context) {
        return new Intent(context, BuyIyubiActivity.class);
    }

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private CustomDialog waitingDialog;


    int iyubiProductId = 1;

    //UserRefreshPresenter mPresenter;

    private IyubiAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_iyubi);
        ButterKnife.bind(this);

        mContext =this;
        waitingDialog = new WaittingDialog().wettingDialog(mContext);

        Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle(R.string.app_name);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new IyubiAdapter();
        mAdapter.setBuyIyubiListener(mBuyListener);
        mRecyclerView.setAdapter(mAdapter);

        String[] prices = getResources().getStringArray(R.array.iyubi_prices);
        int[] amounts = getResources().getIntArray(R.array.iyubi_amounts);
        int[] imageIds = ResourcesUtil.fromTypedArray(this, R.array.iyubi_images);
        mAdapter.setData(prices, amounts, imageIds);
    }

    private IyubiAdapter.BuyIyubiListener mBuyListener = new IyubiAdapter.BuyIyubiListener() {

        @Override
        public void onBuy(String otn, String subject, String body, int amount, String price) {
            if (AccountManager.getInstance().islinshi||!AccountManager.getInstance().checkUserLogin()) {
                Util.showTempUserHint(BuyIyubiActivity.this);
            } else {
                String description = "本次购买" + amount + "爱语币";
                //price ="0.01";
                //amount=101;
                Intent intent = new Intent(mContext, PayOrderActivity.class);
                intent.putExtra("month", amount);
                intent.putExtra("type", iyubiProductId);
                intent.putExtra("out_trade_no", getOutTradeNo());
                intent.putExtra("subject", subject);
                intent.putExtra("body", body);//"花费" + price + "元购买全站vip"
                intent.putExtra("price", price + "");  //价格
                startActivity(intent);
            }
        }
    };

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + Math.abs(r.nextInt());
        key = key.substring(0, 15);
        return key;
    }
}
