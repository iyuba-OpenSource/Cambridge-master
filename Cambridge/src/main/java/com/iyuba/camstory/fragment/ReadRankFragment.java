package com.iyuba.camstory.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.RankListAdapter;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.RankUser;
import com.iyuba.camstory.lycam.widget.imageview.CircleImageView;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.utils.GsonUtils;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.PullToRefreshView;
import com.iyuba.camstory.widget.WaittingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zhangshuai on 2018/7/26.
 */

public class ReadRankFragment extends Fragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private Context mContext;
    private int uid;
    private String type;
    private String total;
    private String start;
    private int topicid;
    private String topic;

    private String myWpm = "";
    private String myImgSrc = "";
    private String myImgOne = "";
    private String myNameOne = "";
    private String myUidOne = "";
    private String myId = "";
    public String myRanking = "";
    private String myCnt = "";
    private String result = "";
    private String myScores = "";
    private String totalRight = "";
    private String totalTest = "";
    private String myCount = "";
    private String myName = "";
    private String myWords = "";
    private boolean isInit = true;


    private String message = "";

    private View listFooter;
    public int mVoaId;

    private boolean scorllable = true;

    private List<RankUser> rankUsers = new ArrayList<>();

    private CustomDialog waitDialog;

    private RankListAdapter rankListAdapter;

    @BindView(R.id.rank_user_image2)
    CircleImageView userImage;
    @BindView(R.id.rank_user_image_text2)
    TextView userImageText;
    @BindView(R.id.rank_user_name2)
    TextView userName;
    @BindView(R.id.username2)
    TextView myUsername;
    @BindView(R.id.rank_info2)
    TextView userInfo;
    @BindView(R.id.rank_list2)
    ListView rankListView;

    @BindView(R.id.my_image2)
    CircleImageView myImage;
    @BindView(R.id.champion_read_words2)
    TextView cpReadWords;

    private PullToRefreshView refreshView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_read_rank, null);
        ButterKnife.bind(this, view);

        mContext = getActivity();
        waitDialog = new WaittingDialog().wettingDialog(mContext);

        uid = AccountManager.getInstance().userId;
        type = "D";
        start = "0";
        total = "30";

        String service = requireActivity().getIntent().getStringExtra("SERVICE");
        BookDetailResponse.ChapterInfo chapterInfo;
        if(service != null){
            chapterInfo = StoryDataManager.Instance().getCurChapterInfo();
        }else{
            chapterInfo = (BookDetailResponse.ChapterInfo) requireActivity().getIntent().getSerializableExtra(ChapterListFragment.CHAPTER_INFO);
        }

        topicid = Integer.parseInt(chapterInfo.getVoaid());
        topic = "camstory";


        refreshView=view.findViewById(R.id.list_view_refresh);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        listFooter = inflater.inflate(R.layout.comment_footer,null);//加载更多
        rankListView.addFooterView(listFooter);
        getReadRank(true);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @SuppressLint("HandlerLeak")
    public Handler rankHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitDialog.show();
                    if (msg.arg1==100) {//如果刷新才为0
                        start = "0";
                        getReadRank(true);
                    }else {
                        start = String.valueOf(rankListAdapter.getCount());
                        getReadRank(false);
                    }
                    break;
            }
        }
    };
    @SuppressLint("SimpleDateFormat")
    private void getReadRank(final boolean isRefresh){
        RequestFactory.getRankAdApi().getTopicRanking(topic, topicid, uid, type,
                start, total, MD5.getMD5ofStr(uid +topic + topicid + start +
                        total + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).
                enqueue(new retrofit2.Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response){
                        waitDialog.dismiss();
                        try{
                            JSONObject jsonRoot = null;
                            jsonRoot = new JSONObject(new Gson().toJson(response.body()));

                            message = jsonRoot.getString("message");
                            if (message.equals("Success")) {

                                listFooter.setVisibility(View.GONE);//刷新

                                if (jsonRoot.has("myimgSrc"))
                                    myImgSrc = jsonRoot.getString("myimgSrc");//---
                                if (jsonRoot.has("myid")) //-----
                                    myId = jsonRoot.getString("myid");

                                if (jsonRoot.has("myranking"))
                                    myRanking = jsonRoot.getString("myranking"); //---

                                if (jsonRoot.has("result"))
                                    result = jsonRoot.getString("result");//---
                                if (jsonRoot.has("myname"))
                                    myName = jsonRoot.getString("myname");
                                if (jsonRoot.has("totalEssay"))
                                    myCount = jsonRoot.getString("totalEssay");

                                if (jsonRoot.has("myscores"))
                                    myScores = jsonRoot.getString("myscores"); //总分
                                if (jsonRoot.has("mycount"))
                                    myCount = jsonRoot.getString("mycount"); //总文章数

                                rankUsers = GsonUtils.toObjectList(jsonRoot.getJSONArray("data").toString(), RankUser.class);
                                if(rankUsers != null && rankUsers.size() > 0){
                                    myImgOne = rankUsers.get(0).getImgSrc();
                                    myNameOne = rankUsers.get(0).getName();
                                    myUidOne = rankUsers.get(0).getUid();
                                }else {
//                                        ToastUtil.showToast(mContext,"没有更多了");
                                }
                            setData(isRefresh);
                            }
                        }catch(JSONException e){
                            Log.e("TAG", "onResponse: "+e.getMessage());
                            setData(isRefresh);
                        }
                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        setData(isRefresh);
                    }
                });

    }
    private void setData(boolean isRefresh){
        if (rankListAdapter==null) {
            rankListAdapter = new RankListAdapter(mContext, rankUsers, "语音");
            rankListView.setAdapter(rankListAdapter);
            refreshView.onHeaderRefreshComplete();
        }else if (isRefresh){
            rankListAdapter.resetList(rankUsers);
            refreshView.onHeaderRefreshComplete();
        }else {
            rankListAdapter.addList(rankUsers);
            refreshView.onFooterRefreshComplete();//完成加载
        }
        myUsername.setText(myName);
        if (getActivity()!=null&&!getActivity().isDestroyed()) {
            Glide.with(mContext)
                    .load(myImgSrc)
                    .placeholder(R.drawable.noavatar_small)
                    .into(myImage);
            Glide.with(mContext)
                    .load(myImgOne)
                    .placeholder(R.drawable.noavatar_small)
                    .into(userImage);
        }

        if (myNameOne.equals("")){
            userName.setText(myUidOne);
        }else {
            userName.setText(myNameOne);

        }
        String Count="0";
        String Scores="0";
        String Ranking="0";
        if (myCount!=null&&!myCount.equals("")) {
            StringTokenizer token1 = new StringTokenizer(myCount, ".");
            Count =token1.nextToken();
        }
        if (myScores!=null&&!myScores.equals("")) {
            StringTokenizer token2 = new StringTokenizer(myScores, ".");
            Scores =token2.nextToken();
        }
        if (myRanking!=null&&!myRanking.equals("")) {
            StringTokenizer token3 = new StringTokenizer(myRanking, ".");
            Ranking =token3.nextToken();
        }

        userInfo.setText(MessageFormat.format("句子总数:{0},得分:{1}\n排名:{2}",
                Count, Scores, Ranking));
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        rankHandler.sendEmptyMessage(0);//加载
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        Message msg=rankHandler.obtainMessage();
        msg.what=0;
        msg.arg1= 100;
        rankHandler.sendMessage(msg);//刷新
    }
}
