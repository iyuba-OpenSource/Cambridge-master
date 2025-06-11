package com.iyuba.camstory;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.adpater.AttentionListAdapter;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.manager.AccountManager;
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
 * Created by yq on 2017/6/19.
 */

public class AttentionCenter extends AppCompatActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private Context mContext;
    private String currUserid;
    private Toolbar toolBar;
    private CustomDialog waitingDialog;
    private ListView fansList;
    private PullToRefreshView refreshView;
    private AttentionListAdapter adapter;
    private ArrayList<AttentionBean.DataBean> attentionArrayList = new ArrayList<AttentionBean.DataBean>();
    private List<AttentionBean.DataBean> dataList = new ArrayList<>();
    private Boolean isLastPage = false;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    public int num;

    private TextView title_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fanslist);
        mContext = this;
        currUserid = this.getIntent().getStringExtra("userid");
        initWidget();
        waitingDialog =
                new WaittingDialog().wettingDialog(mContext);
    }

    private void initWidget() {
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initFansView();

        title_text.setText("关注");
    }

    private void initFansView() {
        fansList = findViewById(R.id.list);
        refreshView = findViewById(R.id.listview);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        title_text = findViewById(R.id.title_text);
        adapter = new AttentionListAdapter(mContext);
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
                    attentionArrayList.clear();
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
        fansList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if(AccountManager.getInstance().islinshi){
                    showNormalDialog();
                }else {
                    AttentionBean.DataBean fans = SocialDataManager.getInstance().attentions.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("friendid", fans.getFollowuid());
                    intent.putExtra("currentname", fans.getFusername());
                    intent.setClass(mContext, MessageLetterContentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getData() {
        Call<AttentionBean> call = RequestFactory.getAttentionApi().getAttention(
                "51001", currUserid, "20", MD5.getMD5ofStr("51001" + currUserid + "iyubaV2")
        );
        call.enqueue(new retrofit2.Callback<AttentionBean>() {
            @Override
            public void onResponse(Call<AttentionBean> call, retrofit2.Response<AttentionBean> response) {
                if (response != null && response.body().getResult() == 550) {
                    dataList.clear();
                    dataList.addAll(response.body().getData());
                    SocialDataManager.getInstance().attentions = response.body().getData();
                }
                setData();
            }

            @Override
            public void onFailure(Call<AttentionBean> call, Throwable t) {
                handler.sendEmptyMessage(3);
                handler.sendEmptyMessage(5);
            }
        });
    }
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final android.app.AlertDialog.Builder normalDialog =
                new android.app.AlertDialog.Builder(mContext);
        normalDialog.setIcon(R.drawable.iyubi_icon);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("临时用户无法使用私信功能！");
        normalDialog.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Intent intent = new Intent();
                        intent.setClass(mContext, LoginActivity.class);
                        startActivity(intent);
                        AttentionCenter.this.finish();
                    }
                });
        normalDialog.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do

                    }
                });
        // 显示
        normalDialog.show();
    }

    private void setData() {
        attentionArrayList.addAll(dataList);
        adapter.setData(attentionArrayList);
        if (attentionArrayList.size() >= num) {
            isLastPage = true;
        }
        handler.sendEmptyMessage(4);
    }

}
