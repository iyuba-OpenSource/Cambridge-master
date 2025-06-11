package com.iyuba.basichdsdllibrary.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.basichdsdllibrary.R;
import com.iyuba.basichdsdllibrary.db.BasicHDsDLDBManager;
import com.iyuba.basichdsdllibrary.db.BasicHDsDLPart;
import com.iyuba.basichdsdllibrary.ui.adapter.BasicDLPartSmallPicListAdapter;
import com.iyuba.basichdsdllibrary.widget.DividerItemDecoration;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.dlex.bizs.DLTaskInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/9/21 21:50
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsDLListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "BasicHDsDLListActivity";
    private static final int NOTIFY_LIST = 0;

    private Context mContext;
    public List<BasicHDsDLPart> mBasicHDsList;
    public List<BasicHDsDLPart> mdeleteBdsList;
//    private LinearLayoutManager mLayoutManager;

    private View view;
    View delete;
//    TextView complete;
//    ImageView ivBack;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    public BasicDLPartSmallPicListAdapter adapter;
    private boolean ifshowck = false;
    Handler notifyHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch ( msg.what ){
                case NOTIFY_LIST:
//                    mBasicHDsList = BasicHDsDLDBManager.getInstance(mContext).queryAllBasicHDsDLPart();
                    mBasicHDsList = BasicHDsDLDBManager.getInstance(mContext).queryByPage(10,0);
                    adapter.setItems(mBasicHDsList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        loadDbData(mBasicHDsList.size());
    }

//    public static Intent getIntent2Me(Context context) {
//        Intent intent = new Intent(context, BasicHDsDLListActivity.class);
//        return intent;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_basic_dl_hdslist);
        mContext = getContext();
        mdeleteBdsList = new ArrayList<>();
        mBasicHDsList = new ArrayList<>();
//        mLayoutManager = new LinearLayoutManager(mContext);

        adapter = new BasicDLPartSmallPicListAdapter();
        adapter.setDeleteBdsList(mdeleteBdsList);
//        initWidget();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_basic_dl_hdslist, container, false);
        initWidget(view);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadDbData(0);
    }

    public void initWidget(View view) {
        // ivBack = (ImageView) view.findViewById(R.id.nav_left);
//        delete = view.findViewById(R.id.nav_right_button);
        swipeRefreshLayout = view.findViewById(R.id.sw_basichds_list);
        recyclerView = view.findViewById(R.id.rv_basichds_list);
//        complete = (TextView) view.findViewById(R.id.nav_right_text);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(mContext,"长按可删除下载。",Toast.LENGTH_SHORT).show();
//                complete.setVisibility(View.VISIBLE);
//                adapter.setIfshowchoose(true);
//                delete.setVisibility(View.GONE);
//            }
//        });
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // finish();
//            }
//        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                mContext, R.drawable.basic_headlines_recyclerview_thick_divider, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(mOnScrollListener);
        adapter.setOnItemClickListener(onRecyclerViewItemClickListener);
        adapter.setOnItemCheckedListener(new BasicDLPartSmallPicListAdapter.OnItemCheckedListener() {
            @Override
            public void onItemChecked(boolean ischecked, int position) {
                mdeleteBdsList.clear();
                BasicHDsDLPart basicHDsDLPart = mBasicHDsList.get(position);
                if(ischecked){
                    mdeleteBdsList.add(basicHDsDLPart);
                }else {
                    mdeleteBdsList.remove(basicHDsDLPart);
                }
            }
        });
//        complete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deletefiles(mdeleteBdsList);
//                adapter.setIfshowchoose(false);
//                delete.setVisibility(View.VISIBLE);
//                complete.setVisibility(View.GONE);
//
//            }
//        });
    }
    public void deletefiles(List<BasicHDsDLPart> files){
        if(files!=null&&files.size()>0){
            for (BasicHDsDLPart dsDLPart:files){
                String strTag = dsDLPart.getType() + dsDLPart.getId();
                DLTaskInfo task = DLManager.getInstance().getDLTaskInfo(strTag);
                if(task!=null)
                DLManager.getInstance().cancelTask(task,true);
                //删除共用下载表中的数据
                BasicHDsDLDBManager.getInstance(mContext).deleteBasicHDsDLPart(dsDLPart.getId(),dsDLPart.getType());
                mBasicHDsList.remove(dsDLPart);
            }
            mdeleteBdsList.clear();
            adapter.notifyDataSetChanged();
        }

    }
    private void loadDbData() {
        mBasicHDsList = BasicHDsDLDBManager.getInstance(mContext).queryAllBasicHDsDLPart();
        adapter.setItems(mBasicHDsList);
    }

    private void loadDbData(int maxId){
        CheckDownLoadData();
        if(maxId>0&&isFlielost){
            mBasicHDsList.clear();
            maxId = 0;
            isFlielost = false;
        }
        if (maxId == 0) {
            mBasicHDsList = BasicHDsDLDBManager.getInstance(mContext).queryByPage(10,0);
            adapter.setItems(mBasicHDsList);
        } else {
            mBasicHDsList.addAll(BasicHDsDLDBManager.getInstance(mContext).queryByPage(10,mBasicHDsList.size()));
            adapter.addItems(BasicHDsDLDBManager.getInstance(mContext).queryByPage(10,mBasicHDsList.size()));
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    private boolean isFlielost = false;
    private void CheckDownLoadData(){
        List<BasicHDsDLPart>mBasicHDsLists = BasicHDsDLDBManager.getInstance(mContext).queryAllBasicHDsDLPart();
        if(mBasicHDsLists!=null&&mBasicHDsLists.size()>0){
            for (BasicHDsDLPart dlPart:mBasicHDsLists){
                String strTag = dlPart.getType() + dlPart.getId();
                DLTaskInfo task = DLManager.getInstance().getDLTaskInfo(strTag);
                if(task==null){
                    BasicHDsDLDBManager.getInstance(mContext).deleteBasicHDsDLPart(dlPart.getId(), dlPart.getType());
                }
                else if(task!=null&&!task.isFileExist()) {
                    DLManager.getInstance().cancelTask(task, true);
                    //删除共用下载表中的数据
                    BasicHDsDLDBManager.getInstance(mContext).deleteBasicHDsDLPart(dlPart.getId(), dlPart.getType());
                    isFlielost = true;
                }
            }
        }

    }
    BasicDLPartSmallPicListAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener =
            new BasicDLPartSmallPicListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    EventBus.getDefault().post(mBasicHDsList.get(position));
                }

                @Override
                public void onLongClick(View view, final int position) {
                    final BasicHDsDLPart basicHDsDLPart = mBasicHDsList.get(position);
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);  //先得到构造器
                    builder.setTitle("删除"); //设置标题
                    builder.setMessage("是否删除该资源?"); //设置内容
                    builder.setIcon(R.drawable.basic_headlines_delete);//设置图标，图片id即可
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //找到下载库中的任务，并清除
                            String strTag = basicHDsDLPart.getType() + basicHDsDLPart.getId();
                            DLTaskInfo task = DLManager.getInstance().getDLTaskInfo(strTag);
                            if (task!=null)
                            DLManager.getInstance().cancelTask(task,true);
                            //删除共用下载表中的数据
                            BasicHDsDLDBManager.getInstance(mContext).deleteBasicHDsDLPart(basicHDsDLPart.getId(),basicHDsDLPart.getType());
                            notifyHandler.sendEmptyMessage(NOTIFY_LIST);
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
//            if (newState == RecyclerView.SCROLL_STATE_IDLE
//                    && lastVisibleItem + 1 == adapter.getItemCount()) {
//                loadDbData(mBasicHDsList.size());
//            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };
}
