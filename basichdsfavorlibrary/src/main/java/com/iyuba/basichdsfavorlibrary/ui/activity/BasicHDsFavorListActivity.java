package com.iyuba.basichdsfavorlibrary.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.basichdsfavorlibrary.R;
import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorDBManager;
import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;
import com.iyuba.basichdsfavorlibrary.model.UpdateCollectResultXML;
import com.iyuba.basichdsfavorlibrary.network.FavorHeadlineNetwork;
import com.iyuba.basichdsfavorlibrary.ui.adapter.BasicFavorPartSmallPicListAdapter;
import com.iyuba.basichdsfavorlibrary.util.FavorHeadlineMD5;
import com.iyuba.basichdsfavorlibrary.util.FavorHeadlineNetWorkState;
import com.iyuba.basichdsfavorlibrary.util.FavorTimestampConverter;
import com.iyuba.basichdsfavorlibrary.util.SyncCollectResultToItemsMapper;
import com.iyuba.basichdsfavorlibrary.widget.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.COLLECT_GroupName_FLAG;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.GET_COLLECT_LIST_TOPIC;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.GET_COLLECT_NOT_SENTENCE_FLG;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.GSON_FORMAT;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.HDS_COLLECT_TYPE_DEL;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.HDS_COLLECT_TYPE_INSERT;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.IYUBA_FLAG;

/**
 * 作者：renzhy on 17/9/21 21:50
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsFavorListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "BasicHDsFavorListAt";
    private static final int NOTIFY_LIST = 0;
    private Context mContext;
    private String userId;
    private String appId;
    protected Subscription subscription;
    private boolean first = true;
    private List<BasicHDsFavorPart> mdeletecollects;
    public List<BasicHDsFavorPart> mBasicHDsList;
    public List<BasicHDsFavorPart> syznBasicHDsList;
    private LinearLayoutManager mLayoutManager;
    ProgressDialog diag;
    ImageView ivBack;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ImageView delete;
    TextView complete;
    BasicFavorPartSmallPicListAdapter adapter;
    Handler notifyHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch ( msg.what ){
                case NOTIFY_LIST:
                    mBasicHDsList=BasicHDsFavorDBManager.getInstance(mContext).queryAllBasicHDsFavorPart(userId,"1");
                    if(mBasicHDsList != null && mBasicHDsList.size() > 0 ){
                        for(BasicHDsFavorPart hd : mBasicHDsList){
                            Log.e("mBasicHDsList", hd.getOther1()+"??");
                        }


                        // Log.e("mBasicHDsList", mBasicHDsList.get(0).getOther1()+"??");
                    }
                    Log.e("Tag--basic列表",mBasicHDsList.size()+"---");
                    adapter.setItems(mBasicHDsList);
                    adapter.notifyDataSetChanged();
                    diag.dismiss();
                    break;
            }
        }
    };

    Observer<List<BasicHDsFavorPart>> observer = new Observer<List<BasicHDsFavorPart>>() {
        @Override
        public void onCompleted() {
            notifyHandler.sendEmptyMessage(NOTIFY_LIST);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("Tag--error",e.toString());
        }

        @Override
        public void onNext(List<BasicHDsFavorPart> basicHDsFavorPartList) {
            for(BasicHDsFavorPart basicHDsFavorPart:basicHDsFavorPartList){
                basicHDsFavorPart.setSynflg("1");
                BasicHDsFavorDBManager.getInstance(mContext).insertOrReplaceFavorPart(basicHDsFavorPart);
            }
            Log.d(TAG,basicHDsFavorPartList.toString());
        }
    };

    Observer<UpdateCollectResultXML> updateCollectObserver = new Observer<UpdateCollectResultXML>() {
        @Override
        public void onCompleted() {
            notifyHandler.sendEmptyMessage(NOTIFY_LIST);
        }
        @Override
        public void onError(Throwable e) {

        }
        @Override
        public void onNext(UpdateCollectResultXML updateCollectResult) {
            if(updateCollectResult.msg.equals("Success")){
                if(deleteitem!=null)
                    BasicHDsFavorDBManager.getInstance(mContext).
                            deleteBasicHDsFavorPart(deleteitem.getId(),userId,deleteitem.getType());
                // Toast.makeText(mContext, "成功同步至服务器!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static Intent getIntent2Me(Context context,String userId,String appId) {
        Intent intent = new Intent(context, BasicHDsFavorListActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("appId",appId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_favor_hdslist);
        mContext = this;
        mdeletecollects = new ArrayList<>();
        adapter = new BasicFavorPartSmallPicListAdapter();
        adapter.setMdeletecollects(mdeletecollects);
        initWidget();
        mBasicHDsList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                mContext, R.drawable.basic_headlines_recyclerview_thick_divider, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(mOnScrollListener);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(this);
        syncUnsuccessCollect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncServerCollect();
    }

    private void syncUnsuccessCollect() {
        if(FavorHeadlineNetWorkState.isConnectingToInternet(mContext)){
            syznBasicHDsList = BasicHDsFavorDBManager.getInstance(mContext)
                    .queryAllSyznBasicHDsFavorPart(userId,"0");
            if(syznBasicHDsList.size()>0) {
                Log.e("Tag=为同步成功的","--数量："+syznBasicHDsList.size());
                Log.e("===============", "++++++++++++++");
                for (final BasicHDsFavorPart favorpart : syznBasicHDsList) {
                    FavorHeadlineNetwork.getUpdateCollectApi2().updateCollectState2(
                            COLLECT_GroupName_FLAG,
                            GET_COLLECT_NOT_SENTENCE_FLG,
                            appId, userId,
                            favorpart.getFlag().equals("1")?HDS_COLLECT_TYPE_INSERT:HDS_COLLECT_TYPE_DEL,
                            favorpart.getId(),
                            GET_COLLECT_NOT_SENTENCE_FLG,
                            favorpart.getType(),
                            favorpart.getOther1())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<UpdateCollectResultXML>() {
                                @Override
                                public void onCompleted() {

                                }
                                @Override
                                public void onError(Throwable e) {

                                }
                                @Override
                                public void onNext(UpdateCollectResultXML updateCollectResultXML) {
                                    if (updateCollectResultXML.msg.equals("Success")) {
                                        if (updateCollectResultXML.type.equals("insert")) {
                                            favorpart.setSynflg("1");
                                            BasicHDsFavorDBManager.getInstance(mContext)
                                                    .updateBasicHDsFavorPart(favorpart);
                                            Log.e("TAG-更新收藏成功", "----");
                                        } else if (updateCollectResultXML.type.equals("del")) {
                                            favorpart.setFlag("0");
                                            BasicHDsFavorDBManager.getInstance(mContext).
                                                    deleteBasicHDsFavorPart(favorpart.getId(), userId, favorpart.getType());
                                            Log.e("TAG-删除收藏成功", "----");
                                        }
                                    }
                                }
                            });
                }
            }
        }else {

        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        syncUnsuccessCollect();
        loadDbData(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    public void initWidget() {
        userId = getIntent().getStringExtra("userId");
        appId = getIntent().getStringExtra("appId");
        diag = new ProgressDialog(mContext);
        ivBack = findViewById(R.id.nav_left);
        swipeRefreshLayout = findViewById(R.id.sw_basichds_list);
        recyclerView = findViewById(R.id.rv_basichds_list);
        delete = findViewById(R.id.nav_right_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,"长按可取消收藏。",Toast.LENGTH_SHORT).show();
                complete.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
                adapter.setIfshowcheckbox(true);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        complete = findViewById(R.id.nav_right_text);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCollects(mdeletecollects);
                complete.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                adapter.setIfshowcheckbox(false);
            }
        });
        adapter.setOnItemCheckedListener(new BasicFavorPartSmallPicListAdapter.OnItemCheckedListener() {
            @Override
            public void OnItemChecked(boolean ischeck, int position) {
                if(ischeck){
                    mdeletecollects.add(mBasicHDsList.get(position));
                }else {
                    mdeletecollects.remove(mBasicHDsList.get(position));
                }
            }
        });
        adapter.setOnItemClickListener(onRecyclerViewItemClickListener);
    }
    private void deleteCollects(List<BasicHDsFavorPart> removecollects){
        if(removecollects!=null&&removecollects.size()>0){
            for (final BasicHDsFavorPart hDsFavorPart:removecollects){
                hDsFavorPart.setFlag("0");
                //删除共用下载表中的数据
                BasicHDsFavorDBManager.getInstance(mContext).updateBasicHDsFavorPart(hDsFavorPart);
                notifyHandler.sendEmptyMessage(NOTIFY_LIST);
                subscription = FavorHeadlineNetwork.getUpdateCollectApi2().updateCollectState2(
                        COLLECT_GroupName_FLAG,
                        GET_COLLECT_NOT_SENTENCE_FLG,
                        appId, userId,
                        HDS_COLLECT_TYPE_DEL,
                        hDsFavorPart.getId(),
                        GET_COLLECT_NOT_SENTENCE_FLG,
                        hDsFavorPart.getType(),
                        hDsFavorPart.getOther1())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UpdateCollectResultXML>() {
                            @Override
                            public void onCompleted() {

                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                            @Override
                            public void onNext(UpdateCollectResultXML updateCollectResult) {
                                if(updateCollectResult.msg.equals("Success")){
                                    BasicHDsFavorDBManager.getInstance(mContext).
                                            deleteBasicHDsFavorPart(hDsFavorPart.getId(),userId,hDsFavorPart.getType());
                                    // Toast.makeText(mContext, "成功同步至服务器!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                mBasicHDsList.remove(hDsFavorPart);
            }
            mdeletecollects.clear();
            adapter.notifyDataSetChanged();
        }

    }
    private void syncServerCollect(){
        if(FavorHeadlineNetWorkState.isConnectingToInternet(mContext)&&first){
            loadDbData(mBasicHDsList.size());
            //设置UserId
            SyncCollectResultToItemsMapper.getInstance().setUserId(userId);
            subscription = FavorHeadlineNetwork.getCollectListApi()
                    .getCollectList(
                            userId,
                            GET_COLLECT_LIST_TOPIC,
                            appId,
                            GET_COLLECT_NOT_SENTENCE_FLG,
                            GSON_FORMAT,
                            FavorHeadlineMD5.getMD5ofStr(
                                    IYUBA_FLAG
                                            +userId+GET_COLLECT_LIST_TOPIC+appId
                                            + FavorTimestampConverter.convertTimestamp()

                            )
                    )
                    .map(SyncCollectResultToItemsMapper.getInstance())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);

            Log.e("tag-有网","---同步");
        }else {
            loadDbData(mBasicHDsList.size());
            if(!first){
                syncUnsuccessCollect();
            }
            first = false;

            Log.e("tag-无网","---");
        }
    }

    private void loadDbData() {
        mBasicHDsList = BasicHDsFavorDBManager.getInstance(mContext).queryAllBasicHDsFavorPart(userId,"1");
        adapter.setItems(mBasicHDsList);
    }

    private void loadDbData(int maxId){
        if (maxId == 0) {
            mBasicHDsList.clear();
            mBasicHDsList.addAll(BasicHDsFavorDBManager.getInstance(mContext).queryBasicHDsFavorPartByPage(userId,"1",10,0));
            Log.e("为0","---"+mBasicHDsList.size());
            if(mBasicHDsList != null && mBasicHDsList.size() > 0 ){
                for(BasicHDsFavorPart hd : mBasicHDsList){
                    Log.e("==LOADDATA==", hd.getOther1()+"??");
                }
            }
            adapter.setItems(mBasicHDsList);
        } else {
            mBasicHDsList.addAll(BasicHDsFavorDBManager.getInstance(mContext).queryBasicHDsFavorPartByPage(userId,"1",10,mBasicHDsList.size()));
            adapter.setItems(mBasicHDsList);
            Log.e("不为0","---"+mBasicHDsList.size());
            if(mBasicHDsList != null && mBasicHDsList.size() > 0 ){
                for(BasicHDsFavorPart hd : mBasicHDsList){
                    Log.e("==LOADDATA==", hd.getOther1()+"??");
                }
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    private BasicHDsFavorPart deleteitem ;
    BasicFavorPartSmallPicListAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener =
            new BasicFavorPartSmallPicListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.e("mbasiclist",mBasicHDsList.get(position).toString());
                    EventBus.getDefault().post(mBasicHDsList.get(position));
                }

                @Override
                public void onLongClick(View view, final int position) {
                    final BasicHDsFavorPart basicHDsFavorPart = mBasicHDsList.get(position);
                    deleteitem = basicHDsFavorPart;
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);  //先得到构造器
                    builder.setTitle("删除"); //设置标题
                    builder.setMessage("是否删除该收藏?"); //设置内容
                    builder.setIcon(R.drawable.basic_headlines_delete);//设置图标，图片id即可
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            basicHDsFavorPart.setFlag("0");
                            //删除共用下载表中的数据
                            BasicHDsFavorDBManager.getInstance(mContext).updateBasicHDsFavorPart(basicHDsFavorPart);
                            notifyHandler.sendEmptyMessage(NOTIFY_LIST);

                            subscription = FavorHeadlineNetwork.getUpdateCollectApi2().updateCollectState2(
                                    COLLECT_GroupName_FLAG,
                                    GET_COLLECT_NOT_SENTENCE_FLG,
                                    appId, userId,
                                    HDS_COLLECT_TYPE_DEL,
                                    basicHDsFavorPart.getId(),
                                    GET_COLLECT_NOT_SENTENCE_FLG,
                                    basicHDsFavorPart.getType(),
                                    basicHDsFavorPart.getOther1())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(updateCollectObserver);
                            dialog.dismiss(); //关闭dialog
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    //参数都设置完成了，创建并显示出来
                    builder.create().show();
                }
            };

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()) {
                loadDbData(mBasicHDsList.size());
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
