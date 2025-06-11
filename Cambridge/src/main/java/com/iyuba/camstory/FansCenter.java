package com.iyuba.camstory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iyuba.camstory.adpater.FansListAdapter;
import com.iyuba.camstory.bean.FansBean;
import com.iyuba.camstory.manager.SocialDataManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.utils.ExeRefreshTime;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.PullToRefreshView;
import com.iyuba.camstory.widget.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by yq on 2017/6/20.
 */

public class FansCenter extends AppCompatActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private static final String TAG = "FansCenter";
    private Context mContext;
    private String currUserid;
    private Toolbar toolBar;
    private CustomDialog waitingDialog;
    private ListView fansList;
    private PullToRefreshView refreshView;
    private FansListAdapter adapter;
    private ArrayList<FansBean.DataBean> fansArrayList = new ArrayList<FansBean.DataBean>();
    private List<FansBean.DataBean> dataList = new ArrayList<>();
    private Boolean isLastPage = false;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    public int num;
    private int curPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fanslist);
        mContext = this;
        currUserid = this.getIntent().getStringExtra("userid");
        initWidget();
        waitingDialog = new WaittingDialog().wettingDialog(mContext);
    }

    private void initWidget() {
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        toolBar.setNavigationOnClickListener(v -> onBackPressed());
        initFansView();
    }

    private void initFansView() {
        fansList = findViewById(R.id.list);
        refreshView = findViewById(R.id.listview);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        adapter = new FansListAdapter(mContext);
        fansList.setAdapter(adapter);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        handler.sendEmptyMessage(0);
        refreshView.setLastUpdated(ExeRefreshTime
                .lastRefreshTime("AttentionCenter"));
        isTopRefresh = true;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (!isLastPage) {
            handler.sendEmptyMessage(1);
            isFootRefresh = true;
        } else {
            refreshView.onFooterRefreshComplete();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    fansArrayList.clear();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    getData();  //获取关注信息
                    break;
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    handler.sendEmptyMessage(3);
                    adapter.notifyDataSetChanged();
                    if (isTopRefresh) {
                        refreshView.onHeaderRefreshComplete();
                    } else if (isFootRefresh) {
                        refreshView.onFooterRefreshComplete();
                    }
                    setListener();
                    break;
                case 5:
                    Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    };

    private void setListener() {
        fansList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            FansBean.DataBean fans = SocialDataManager.getInstance().fans.get(position);
            Intent intent = new Intent();
            intent.putExtra("friendid", fans.getUid());
            intent.putExtra("currentname", fans.getUsername());
            intent.setClass(mContext, MessageLetterContentActivity.class);
            startActivity(intent);
        });
    }

    private void getData() {
        Call<FansBean> call = RequestFactory.getFansApi().getFans("android", "json", "51002",
                currUserid, "20", String.valueOf(curPage), MD5.getMD5ofStr("51002" + currUserid + "iyubaV2")
        );
        call.enqueue(new retrofit2.Callback<FansBean>() {
            @Override
            public void onResponse(Call<FansBean> call, retrofit2.Response<FansBean> response) {
                if (response != null && response.body().getResult() == 560) {
                    dataList.clear();
                    dataList.addAll(response.body().getData());
                    SocialDataManager.getInstance().fans = response.body().getData();
                }
                setData();
            }

            @Override
            public void onFailure(Call<FansBean> call, Throwable t) {
                handler.sendEmptyMessage(3);
                handler.sendEmptyMessage(5);
            }
        });
    }

    private void setData() {
        fansArrayList.addAll(dataList);
        adapter.setData(fansArrayList);
        if (fansArrayList.size() >= num) {
            isLastPage = true;
        }
        handler.sendEmptyMessage(4);
    }
}
