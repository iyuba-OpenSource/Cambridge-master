package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.iyuba.camstory.utils.Constant;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.adpater.CommentAdapter;
import com.iyuba.camstory.bean.CommentList;
import com.iyuba.camstory.protocol.UserCommentRequest;
import com.iyuba.camstory.protocol.UserCommentResponse;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.http.BaseHttpRequest;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.ClientSession;
import com.iyuba.http.ErrorResponse;
import com.iyuba.http.IErrorReceiver;
import com.iyuba.http.IResponseReceiver;

import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView mRvCommentList;
    private CommentAdapter mAdapter;
    private Context mContext;
    private String mVoaId;
    private String mUserId;
    private String mUserPic;
    private String mUserName;
    private ArrayList<CommentList> comments = new ArrayList<>();
    private CustomDialog waitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mContext=this;

        initView();
        initRecycleView();
        getIntentData();
        initData();
    }

    private void initView(){
        // 标题栏
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("评论详情");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        waitDialog = new CustomDialog(mContext);
        mRvCommentList=findViewById(R.id.rv_comment_list);
    }

    private void initRecycleView() {
        //创建LinearLayoutManager 对象
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        //设置RecyclerView 布局
        mRvCommentList.setLayoutManager(mLayoutManager);
        mRvCommentList.addItemDecoration(new DividerItemDecoration(
            mContext, LinearLayoutManager.VERTICAL
        ));
        ((SimpleItemAnimator) Objects.requireNonNull(mRvCommentList.getItemAnimator())).setSupportsChangeAnimations(false);
        //设置Adapter
        mAdapter = new CommentAdapter(mContext);
        mRvCommentList.setAdapter(mAdapter);
    }

    private void getIntentData(){
        mUserId=getIntent().getStringExtra("uid");
        mVoaId=getIntent().getStringExtra("voaId");
        mUserName=getIntent().getStringExtra("userName");
        mUserPic=getIntent().getStringExtra("userPic");
    }

    private void initData(){
        LogUtils.e("VOAID "+mVoaId);
        waitDialog.show();
        ClientSession.getInstance().asynGetResponse(
                new UserCommentRequest(mUserId, Constant.mLesson, mVoaId), new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response,
                                           BaseHttpRequest request, int rspCookie) {//concept camstory
                        UserCommentResponse tr = (UserCommentResponse) response;
                        if (tr.result.equals("true")) {
                            comments.clear();
                            comments.addAll(tr.comments);
                            handler.sendEmptyMessage(1);
                        }
                    }
                }, new IErrorReceiver() {
                    @Override
                    public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                        LogUtils.e("CommentActivity.网络请求错误", "aaa");
                    }
                }, null);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    for(int i = 0;i<comments.size();i++){
                        comments.get(i).username = mUserName;
                        comments.get(i).imgsrc = mUserPic;
                        comments.get(i).userId = mUserId;
                        LogUtils.e("排名音频"+comments.get(i).shuoshuo);
                    }
                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                    }
                    mAdapter.setDate(comments);
                    break;default:
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAdapter != null)
            mAdapter.stopVoices();
    }
}
