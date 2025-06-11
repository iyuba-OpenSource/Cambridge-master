package com.iyuba.camstory.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.RankListAdapter;
import com.iyuba.camstory.bean.RankUser;
import com.iyuba.camstory.lycam.widget.imageview.CircleImageView;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.utils.GsonUtils;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 排行榜
 */
public class RankFragment extends BaseFragment {
    private static final String TAG = RankFragment.class.getSimpleName();
    private Context mContext;
    private int uid;
    private String type;
    private String total;
    private String start;
    private String mode;
    private String topic;
    private int topicid;
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
    private String message = "";
    private String myName = "";
    private String myWords = "";
    private List<RankUser> rankUsers = new ArrayList<>();
    private RankUser champion;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private String date = sdf.format(new Date());
    private RankListAdapter rankListAdapter;//听力和学习
    private RankListAdapter rankListAdapter1;//学习？？？？
    private RankListAdapter rankListAdapter2;//阅读
    private RankListAdapter rankListAdapter3;//测试
    private RankListAdapter rankListAdapter4;//口语
    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    private Pattern p;
    private Matcher m;

    private CustomDialog waitDialog;

    private LayoutInflater inflater;
    private View listFooter;
    private boolean scorllable = true;
    private static boolean isNight = false;

    private boolean isDateChange = false;

    @BindView(R.id.rank_note)
    TextView note;
    @BindView(R.id.rank_user_image)
    CircleImageView userImage;
    @BindView(R.id.rank_user_image_text)
    TextView userImageText;
    @BindView(R.id.rank_user_name)
    TextView userName;
    @BindView(R.id.username)
    TextView myUsername;
    @BindView(R.id.rank_info)
    TextView userInfo;
    @BindView(R.id.rank_list)
    ListView rankListView;

    @BindView(R.id.my_image)
    CircleImageView myImage;
    @BindView(R.id.champion_read_words)
    TextView cpReadWords;

    private static final String RANKTYPE = "rank_type";
    private int dialog_position = 0;
    private String rankType;
    private View parent;

    public static RankFragment newInstance(String rank_type) {
        Bundle bundle = new Bundle();
        bundle.putString(RANKTYPE, rank_type);
        RankFragment fragment = new RankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (parent == null) {
            parent = inflater.inflate(R.layout.fragment_rank, null);
            isPrepared = true;
            mContext = getActivity();
            ButterKnife.bind(this, parent);

            waitDialog = new WaittingDialog().wettingDialog(mContext);
            rankType = getArguments().getString(RANKTYPE);

            Log.e("RANKTYPE", rankType);
            note.setText("今天");

            uid = AccountManager.getInstance().userId;

            Log.e("<xxxxxxxxxxxxx>", uid + "");
            type = "D";
            total = "30";
            start = "0";
            topic = "camstory";
            topicid = 0;
            listFooter = inflater.inflate(R.layout.comment_footer, null);
            rankListView.addFooterView(listFooter);
            lazyload();
        }
        rankListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            // 当comment不为空且comment.size()不为0且没有完全加载
                            if (scorllable) {
                                changeData();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

        //选择今天，本周，本月
        note.setOnClickListener(v -> choseHeartType(note.getText().toString()));

        return parent;
    }

    private void changeData() {
        Message msg = handler.obtainMessage();
        //msg.what=0;
        if (rankType.equals("听力") || rankType.equals("学习")) {
            setMode();
            msg.what = 0;
        } else if (rankType.equals("阅读")) {
            msg.what = 2;
        } else if (rankType.equals("测试")) {
            msg.what = 7;
        } else if (rankType.equals("口语")) {
            msg.what = 8;
        } else {
            msg.what = 0;
        }
        handler.sendMessage(msg);
    }

    @Override
    protected void lazyload() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        if (rankType.equals("听力") || rankType.equals("学习")) {
            setMode();
            handler.sendEmptyMessage(0);
        } else if (rankType.equals("阅读")) {
            handler.sendEmptyMessage(2);
        } else if (rankType.equals("测试")) {
            handler.sendEmptyMessage(7);
        } else if (rankType.equals("口语")) {
            handler.sendEmptyMessage(8);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitDialog.show();
                    if (rankUsers.size() == 0) {
                        start = "0";
                    } else {
                        start = String.valueOf(rankListAdapter.getCount());
                    }
                    getInfoRead();//听力学习
                    break;
                case 2:
                    waitDialog.show();
                    if (rankUsers.size() == 0) {
                        start = "0";
                    } else {
                        start = String.valueOf(rankListAdapter2.getCount());
                    }
                    getRead();//阅读
                    break;

                case 3:
                    listFooter.setVisibility(View.GONE);
                    break;
                case 4:
                    if (waitDialog.isShowing())
                        waitDialog.dismiss();
                    break;
                case 5:
                    CustomToast.showToast(mContext, "与服务器连接异常", 1000);
                    break;
                case 6:
                    listFooter.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    waitDialog.show();

                    if (rankUsers.size() == 0) {
                        start = "0";
                    } else {
                        start = String.valueOf(rankListAdapter3.getCount());
                    }
                    getTest();//测试
                    break;
                case 8:
                    waitDialog.show();

                    if (rankUsers.size() == 0) {
                        start = "0";
                    } else {
                        start = String.valueOf(rankListAdapter4.getCount());
                    }
                    getSpeak();//口语
                    break;
            }
        }
    };

    private void getSpeak() {
        try {
            RequestFactory.getRankAdApi().getTopicRanking(topic, topicid, uid, type,
                            start, total, MD5.getMD5ofStr(uid + topic + topicid + start +
                                    total + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).
                    enqueue(new retrofit2.Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            handler.sendEmptyMessage(4);
                            try {
                                JSONObject jsonRoot = null;
                                jsonRoot = new JSONObject(new Gson().toJson(response.body()));
                                Log.e("==xxxx==", new Gson().toJson(response.body()));
                                Log.e("message", message);

                                message = jsonRoot.getString("message");
                                if (message.equals("Success")) {

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
//                                        if (jsonRoot.has("totalTime"))
//                                            myWpm = jsonRoot.getString("totalTime");
                                    if (jsonRoot.has("totalEssay"))
                                        myCount = jsonRoot.getString("totalEssay");

//                                        if (jsonRoot.has("totalWord"))
//                                            myWords = jsonRoot.getString("totalWord");
                                    if (jsonRoot.has("myscores"))
                                        myScores = jsonRoot.getString("myscores"); //总分
                                    if (jsonRoot.has("mycount"))
                                        myCount = jsonRoot.getString("mycount"); //总文章数
//                                        if (jsonRoot.has("totalRight"))
//                                            totalRight = jsonRoot.getString("totalRight"); //总分
//                                        if (jsonRoot.has("totalTest"))
//                                            totalTest = jsonRoot.getString("totalTest");

                                    rankUsers = GsonUtils.toObjectList(jsonRoot.getJSONArray("data").toString(), RankUser.class);
                                    if (rankUsers != null && rankUsers.size() > 0) {
                                        myImgOne = rankUsers.get(0).getImgSrc();
                                        myNameOne = rankUsers.get(0).getName();
                                        myUidOne = rankUsers.get(0).getUid();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(isDateChange){
                                isDateChange = false;
                                rankListAdapter4.resetList(rankUsers);
                            }else{
                                if (rankListAdapter4 == null) {
                                    rankListAdapter4 = new RankListAdapter(mContext, rankUsers, rankType);
                                    rankListView.setAdapter(rankListAdapter4);
                                } else {
                                    rankListAdapter4.addList(rankUsers);
                                }
                            }
                            setInfo();
                            mHasLoadedOnce = true;
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getTest() {
        RequestFactory.getRankTestApi().getRankTestInfo(uid, type, total, MD5.getMD5ofStr(uid + type + start + total + date), start).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                handler.sendEmptyMessage(4);
                try {
                    JSONObject jsonRoot = null;

                    jsonRoot = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("==xxxx==", new Gson().toJson(response.body()));
                    message = jsonRoot.getString("message");

                    Log.e("message_message_message", message + "lalala");
                    if (message.equals("Success")) {

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
                        if (jsonRoot.has("totalTime"))
                            myWpm = jsonRoot.getString("totalTime");
                        if (jsonRoot.has("totalEssay"))
                            myCount = jsonRoot.getString("totalEssay");
                        if (jsonRoot.has("totalWord"))
                            myWords = jsonRoot.getString("totalWord");
                        if (jsonRoot.has("myscores"))
                            myScores = jsonRoot.getString("myscores"); //总分
                        if (jsonRoot.has("mycount"))
                            myCount = jsonRoot.getString("mycount"); //总文章数
                        if (jsonRoot.has("totalRight"))
                            totalRight = jsonRoot.getString("totalRight"); //总分
                        if (jsonRoot.has("totalTest"))
                            totalTest = jsonRoot.getString("totalTest");

                        rankUsers = GsonUtils.toObjectList(jsonRoot.getJSONArray("data").toString(), RankUser.class);
                        myImgOne = rankUsers.get(0).getImgSrc();
                        myNameOne = rankUsers.get(0).getName();
                        myUidOne = rankUsers.get(0).getUid();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(isDateChange){
                    isDateChange = false;
                    rankListAdapter3.resetList(rankUsers);
                }else{
                    if (rankListAdapter3 == null) {
                        rankListAdapter3 = new RankListAdapter(mContext, rankUsers, rankType);
                        rankListView.setAdapter(rankListAdapter3);
                    } else {
                        rankListAdapter3.addList(rankUsers);
                    }
                }
                setInfo();
                mHasLoadedOnce = true;
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void getRead() {
        RequestFactory.getRankReadApi().getRankReadInfo(uid, type, total, MD5.getMD5ofStr(uid + type + start + total + date), start).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                handler.sendEmptyMessage(4);
                try {
                    JSONObject jsonRoot = null;
                    jsonRoot = new JSONObject(new Gson().toJson(response.body()));
                    message = jsonRoot.getString("message");
                    if (message.equals("Success")) {

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
                        if (jsonRoot.has("totalTime"))
                            myWpm = jsonRoot.getString("totalTime");
                        if (jsonRoot.has("totalEssay"))
                            myCount = jsonRoot.getString("totalEssay");
                        if (jsonRoot.has("totalWord"))
                            myWords = jsonRoot.getString("totalWord");
                        if (jsonRoot.has("myscores"))
                            myScores = jsonRoot.getString("myscores"); //总分
                        if (jsonRoot.has("mycount"))
                            myCount = jsonRoot.getString("mycount"); //总文章数
                        rankUsers = GsonUtils.toObjectList(jsonRoot.getJSONArray("data").toString(), RankUser.class);
                        myImgOne = rankUsers.get(0).getImgSrc();
                        myNameOne = rankUsers.get(0).getName();
                        myUidOne = rankUsers.get(0).getUid();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                if(isDateChange){
                    isDateChange = false;
                    rankListAdapter2.resetList(rankUsers);
                }else{
                    if (rankListAdapter2 == null) {//阅读
                        rankListAdapter2 = new RankListAdapter(mContext, rankUsers, rankType);
                        rankListView.setAdapter(rankListAdapter2);
                    } else {
                        rankListAdapter2.addList(rankUsers);
                    }
                }
                setInfo();
                mHasLoadedOnce = true;
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    private String getFirstChar(String name) {
        String subString;
        for (int i = 0; i < name.length(); i++) {
            subString = name.substring(i, i + 1);

            p = Pattern.compile("[0-9]*");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[a-zA-Z]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是汉字", Toast.LENGTH_SHORT).show();
                return subString;
            }
        }

        return "A";
    }


    /**
     * 提供给用户选择动态类型的单选列表对话框
     */
    private void choseHeartType(String choose_type) {

        final String typeArray[] = new String[]{"今天", "本周", "本月"};
        for (int i = 0; i < typeArray.length; i++) {
            if (choose_type.equals(typeArray[i])) {
                dialog_position = i;
            }
        }

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setSingleChoiceItems(typeArray,  //装载数组信息
                        //默认选中选项
                        dialog_position,
                        //为列表添加监听事件
                        (dialog, which) -> {
                            isDateChange = true;
                            switch (which) {
                                case 0:
                                    if (dialog_position != 0) {
                                        rankUsers.clear();
                                        type = "D";
                                        note.setText(typeArray[0]);
                                        //handler.sendEmptyMessage(0);
                                        changeData();
                                    }
                                    break;
                                case 1:
                                    if (dialog_position != 1) {
                                        rankUsers.clear();
                                        type = "W";
                                        note.setText(typeArray[1]);
                                        changeData();
                                    }
                                    break;
                                case 2:
                                    if (dialog_position != 2) {
                                        rankUsers.clear();
                                        type = "M";
                                        note.setText(typeArray[2]);
                                        changeData();
                                    }
                                    break;
                            }
                            dialog.cancel();  //用户选择后，关闭对话框
                        })
                .create()
                .show();
    }


    // 2.1 定义用来与外部activity交互，获取到宿主activity
    private RankFragmentInteraction listterner;

    // 1 定义了所有activity必须实现的接口方法
    public interface RankFragmentInteraction {
        void process(String str, String str2, String str3);
    }

    // 当FRagmen被加载到activity的时候会被回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof RankFragmentInteraction) {
            listterner = (RankFragmentInteraction) activity; // 2.2 获取到宿主activity并赋值
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    private void getInfoRead() {

        RequestFactory.getRankAdApi().getRankInfo(uid, type, total, MD5.getMD5ofStr(uid + type + start + total + date), start, mode).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                handler.sendEmptyMessage(4);
                try {
                    JSONObject jsonRoot = null;
                    jsonRoot = new JSONObject(new Gson().toJson(response.body()));
                    message = jsonRoot.getString("message");
                    if (message.equals("Success")) {
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
                        if (jsonRoot.has("totalTime"))
                            myWpm = jsonRoot.getString("totalTime"); // 学习时长
                        if (jsonRoot.has("totalEssay"))
                            myCount = jsonRoot.getString("totalEssay");
                        if (jsonRoot.has("totalWord"))
                            myWords = jsonRoot.getString("totalWord");
                        if (jsonRoot.has("myscores"))
                            myScores = jsonRoot.getString("myscores"); //总分
                        if (jsonRoot.has("mycount"))
                            myCount = jsonRoot.getString("mycount"); //总文章数
                        rankUsers = GsonUtils.toObjectList(jsonRoot.getJSONArray("data").toString(), RankUser.class);
                        myImgOne = rankUsers.get(0).getImgSrc();
                        myNameOne = rankUsers.get(0).getName();
                        myUidOne = rankUsers.get(0).getUid();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isDateChange){
                    isDateChange = false;
                    rankListAdapter.resetList(rankUsers);
                }else{
                    if (rankListAdapter == null) {
                        rankListAdapter = new RankListAdapter(mContext, rankUsers, rankType);
                        rankListView.setAdapter(rankListAdapter);
                    } else {
                        rankListAdapter.addList(rankUsers);
                    }
                }
                setInfo();
                mHasLoadedOnce = true;
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                handler.sendEmptyMessage(4);
                handler.sendEmptyMessage(5);
            }
        });
    }

    private void setInfo() {
        if (getActivity() != null && !getActivity().isDestroyed())
            switch (rankType) {
                case "阅读":
                    myUsername.setText(myName);
                    Glide.with(mContext)
                            .load(myImgSrc)
                            .into(myImage);
                    Glide.with(mContext)
                            .load(myImgOne)
                            .into(userImage);
                    if (myNameOne.equals("")) {
                        userName.setText(myUidOne);
                    } else {
                        userName.setText(myNameOne);
                    }
                    userInfo.setText("单词数:" + myWords + "，文章数:" + myCnt + "\n排名:" + myRanking);
                    break;
                case "听力":
                    myUsername.setText(myName);
                    Glide.with(mContext)
                            .load(myImgSrc)
                            .into(myImage);
                    Glide.with(mContext)
                            .load(myImgOne)
                            .into(userImage);
                    if (myNameOne.equals("")) {
                        userName.setText(myUidOne);
                    } else {
                        userName.setText(myNameOne);
                    }
                    userInfo.setText("学习时长：" + myWpm + ",文章数：" + myCount + "\n单词数：" + myWords + ",排名：" + myRanking);
                    break;
                case "口语":
                    myUsername.setText(myName);
                    Log.e("<===myName===>", myName);
                    if (mContext != null) {
                        Glide.with(mContext)
                                .load(myImgSrc)
                                .into(myImage);
                        Log.e("<===myImgSrc===>", myImgSrc);
                        Glide.with(mContext)
                                .load(myImgOne)
                                .into(userImage);
                        Log.e("<===myImgOne===>", myImgOne);
                    }
                    if (myNameOne.equals("")) {
                        userName.setText(myUidOne);
                    } else {
                        userName.setText(myNameOne);
                    }
                    userInfo.setText("文章数:" + myCount + ",得分:" + myScores + '\n' +
                            "排名:" + myRanking);
                    Log.e("<===userInfo===>", "文章数:" + myCount + ",得分:" + myScores +
                            ",排名:" + myRanking);
                    break;
                case "学习":
                    myUsername.setText(myName);
                    Glide.with(mContext)
                            .load(myImgSrc)
                            .into(myImage);
                    Glide.with(mContext)
                            .load(myImgOne)
                            .into(userImage);
                    if (myNameOne.equals("")) {
                        userName.setText(myUidOne);
                    } else {
                        userName.setText(myNameOne);
                    }
                    userInfo.setText("学习时长：" + myWpm + ",文章数：" + myCount + "\n单词数：" + myWords + ",排名：" + myRanking);
                    break;
                case "测试":
                    myUsername.setText(myName);
                    Glide.with(mContext)
                            .load(myImgSrc)
                            .into(myImage);
                    Glide.with(mContext)
                            .load(myImgOne)
                            .into(userImage);
                    if (myNameOne.equals("")) {
                        userName.setText(myUidOne);
                    } else {
                        userName.setText(myNameOne);
                    }
                    userInfo.setText("正确数：" + totalRight + ",总题数：" + totalTest + "\n排名：" + myRanking);
                    break;

            }

    }

    private void setMode() {
        switch (rankType) {
            case "听力":
                mode = "listening";
                break;
            case "学习":
                mode = "all";
                break;
        }

    }

    @Override
    public void onDestroy() {
        // 活动销毁时停止加载图片
        if(!isDestroy(getActivity())){
            Glide.get(mContext).clearMemory();
            Glide.with(mContext).pauseRequests();
        }
        super.onDestroy();
    }

    private static boolean isDestroy(Activity mActivity) {
        if (mActivity== null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}
