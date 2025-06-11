package com.iyuba.camstory.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efs.sdk.base.core.util.NetworkUtil;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.camstory.R;
import com.iyuba.camstory.WebActivity;
import com.iyuba.camstory.adpater.RecyclerViewAdapter;
import com.iyuba.camstory.bean.AdBean;
import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.SoundRecord;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.event.OnPlayChangeEvent;
import com.iyuba.camstory.event.PlayerCompletionEvent;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.lil.AdUtil;
import com.iyuba.camstory.lil.GlideUtil;
import com.iyuba.camstory.lil.ad.show.banner.AdBannerShowManager;
import com.iyuba.camstory.lil.ad.show.banner.AdBannerViewBean;
import com.iyuba.camstory.lil.util.LibRxTimer;
import com.iyuba.camstory.listener.RequestUpdateStudyRecord;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.service.BackGroundMusicService;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.utils.ScreenUtils;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.configation.Constant;
import com.umeng.analytics.MobclickAgent;
import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;
import com.youdao.sdk.nativeads.ImageService;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 有原文的小说详情
 */
public class StoryTextFragment extends Fragment {
    private View info;
    private Context mContext;
    private AppCompatSeekBar seekBar;
    private ImageView iv_playcontrol;
    private TextView tv_current_time;
    private TextView tv_duration;
    private TextView tv_transate;
    private Button btn_backward;
    private Button btn_speed;
    private RecyclerView mRecyclerView;
    private StoryDataManager storyDataManager = StoryDataManager.Instance();
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    private LinearLayoutManager mLayoutManager;
    private ArrayList<VoaDetail> data = new ArrayList<VoaDetail>();
    private int pos = 0;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String beginTime, endTime;
    private TextView textView;
    private int changePosition;
    private List<BookContentResponse.Texts> texts;
    private String url;

    private BookDetailResponse.ChapterInfo chapterInfo;

    /*********广告*******/
    private ViewGroup adLayout;
    private RelativeLayout iyubaSdkAdLayout;
    private RelativeLayout webAdLayout;
    private ImageView webAdImage;
    private TextView webAdTips;
    private ImageView webAdClose;

    public StoryTextFragment(List<BookContentResponse.Texts> texts, BookDetailResponse.ChapterInfo chapterInfo) {
        this.texts = texts;
        this.url = chapterInfo.getSound();
        this.chapterInfo = chapterInfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater,
                             ViewGroup paramViewGroup, Bundle paramBundle) {

        info = paramLayoutInflater.inflate(R.layout.fragment_storytext, paramViewGroup, false);
        initView(info);
        adapter.addOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
            @Override
            public void onScrollChange(int position) {
                LogUtils.e("列表滑动到" + position);
            }
        });
        //smoothMoveToPosition(50);
        if(storyDataManager.getCmPlayer().isPlaying()){
            if(storyDataManager.getCurChapterInfo().getCname_cn()
                    .equals(chapterInfo.getCname_cn())){
                refreshSeekBarHandler.sendEmptyMessage(0);
                iv_playcontrol.setImageResource(R.drawable.new_pause);
                initSeekBar();
                tv_duration.setText(storyDataManager.getCmPlayer().getAudioAllTime());
            }else{
                StoryDataManager.Instance().setCurChapterInfo(chapterInfo);
                storyDataManager.getCmPlayer().setVideoPath("http://static2.iyuba.cn" + url);
                storyDataManager.getCmPlayer().setOnPreparedListener(mp -> {
                    initSeekBar();
                    tv_duration.setText(storyDataManager.getCmPlayer().getAudioAllTime());
                    Intent intent = new Intent(requireActivity(), BackGroundMusicService.class);
                    requireActivity().startService(intent);
                    Log.e("TAG", "onCreateView: 启动 s");
                });
            }

        }else{
            StoryDataManager.Instance().setCurChapterInfo(chapterInfo);
            storyDataManager.getCmPlayer().setVideoPath("http://static2.iyuba.cn" + url);
            storyDataManager.getCmPlayer().setOnPreparedListener(mp -> {
                initSeekBar();
                tv_duration.setText(storyDataManager.getCmPlayer().getAudioAllTime());
                Intent intent = new Intent(requireActivity(), BackGroundMusicService.class);
                requireActivity().startService(intent);
                Log.e("TAG", "onCreateView: 启动 s");
            });
        }
        textHandler.post(runnable);
        return info;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("广告显示", "显示这里的数据，查看是否走了两遍");

        //判断加载广告
        if (!AdBlocker.getInstance().shouldBlockAd()
                &&!AccountManager.getInstance().isVip(requireActivity())
                && NetworkUtil.isConnected(requireActivity())){
            refreshAd();
        }
    }

    private ArrayList<String> getDataStartTime2() {
        ArrayList<String> data_Start_time = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            data_Start_time.add(texts.get(i).getBeginTiming());
        }
        return data_Start_time;
    }

    private void initView(View paramView) {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = paramView.findViewById(R.id.my_recycler_view);
        ImageView imgBg = paramView.findViewById(R.id.empty_bg);
        if (texts.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            imgBg.setVisibility(View.VISIBLE);
        }
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(adapter);
        adapter.setTexts(texts);

        tv_current_time = paramView.findViewById(R.id.tv_current_time);
        tv_duration = paramView.findViewById(R.id.tv_duration);
        tv_transate = paramView.findViewById(R.id.switch_textview);
        tv_transate.setOnClickListener(v -> {
            if (adapter.isTranslate) {
                adapter.setTranslate(false);
            } else {
                adapter.setTranslate(true);
            }
        });
        seekBar = paramView.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    StoryDataManager.Instance().getCmPlayer().seekTo(progress);
                }
                if (getActivity() != null && !getActivity().isDestroyed())
                    tv_current_time.setText(storyDataManager.getCmPlayer().getAudioCurrTime());
                // 如果current time等于播放时间播放按钮变为暂停
                if (tv_current_time.getText().toString().equals(tv_duration.getText().toString())) {
                    iv_playcontrol.setImageResource(R.drawable.new_pause);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //播放暂停开始
        iv_playcontrol = paramView.findViewById(R.id.iv_playcontrol);
        iv_playcontrol.setOnClickListener(v -> {
            if (storyDataManager.getCmPlayer().mIsPrepared) {
                if (!storyDataManager.getCmPlayer().isPlaying()) {
                    try {
                        storyDataManager.getCmPlayer().start();
                        refreshSeekBarHandler.sendEmptyMessage(0);
                        iv_playcontrol.setImageResource(R.drawable.new_pause);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else {
                    storyDataManager.getCmPlayer().pause();
                    refreshSeekBarHandler.removeMessages(0);
                    iv_playcontrol.setImageResource(R.drawable.new_play);
                }
            }
        });

        btn_backward = paramView.findViewById(R.id.backward);
        btn_speed = paramView.findViewById(R.id.speed);
        //快退按钮的逻辑
        btn_backward.setOnClickListener(view -> {
            int preTime = StoryDataManager.Instance().getCmPlayer().getCurrentPosition() - 5000;
            if (preTime < 0)
                preTime = 0;
            StoryDataManager.Instance().getCmPlayer().seekTo(preTime);
            refreshSeekBarHandler.sendEmptyMessage(0);
        });
        btn_speed.setOnClickListener(view -> {
            //快退和快进是加上5毫秒或者减去5毫秒
            int nextTime = StoryDataManager.Instance().getCmPlayer().getCurrentPosition() + 5000;
            if (nextTime > StoryDataManager.Instance().getCmPlayer().getDuration())
                nextTime = StoryDataManager.Instance().getCmPlayer().getDuration();
            StoryDataManager.Instance().getCmPlayer().seekTo(nextTime);
            LogUtils.e("nextTime", nextTime + "---");
            refreshSeekBarHandler.sendEmptyMessage(0);
        });
        textView = paramView.findViewById(R.id.tv_speed); //调速功能已经隐藏
        textView.setVisibility(View.GONE);
        textView.setOnClickListener(v -> {
            //mLayoutManager.scrollToPositionWithOffset(changePosition, 200);
            //changePosition++;
        });


        //广告控件
        adLayout = paramView.findViewById(R.id.adLayout);
        iyubaSdkAdLayout = paramView.findViewById(R.id.iyubaSdkAdLayout);
        webAdLayout = paramView.findViewById(R.id.webAdLayout);
        webAdImage = paramView.findViewById(R.id.webAdImage);
        webAdTips = paramView.findViewById(R.id.webAdTips);
        webAdClose = paramView.findViewById(R.id.webAdClose);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //关闭广告
        AdBannerShowManager.getInstance().stopBannerAd();
        //关闭计时器
        stopAdTimer();

        updateStudyRecord();
        EventBus.getDefault().unregister(this);
    }

    private void initSeekBar() {
        seekBar.setMax(StoryDataManager.Instance().getCmPlayer().getDuration());
        seekBar.setProgress(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("StoryTextFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("StoryTextFragment");
    }

    Handler textHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            double playTime = storyDataManager.getCmPlayer().getCurrentPosition() / 1000.0;
            double beanTime = 0;
            ArrayList<String> timeList = getDataStartTime2();
            if (changePosition >= timeList.size() - 1) {
                changePosition = 0;
            } else {
                beanTime = Double.valueOf(timeList.get(changePosition));
                if (playTime >= beanTime) {
                    if (playTime - beanTime < 2) {
                        mLayoutManager.scrollToPositionWithOffset(changePosition, 200);
                        changePosition++;
                    } else {
                        for (int i = 0; i < texts.size(); i++) {
                            ArrayList<String> time = getDataStartTime2();
                            String startTime = time.get(i);
                            if (playTime - Double.valueOf(startTime) > 0 && playTime - Double.valueOf(startTime) < 3) {
                                mLayoutManager.scrollToPositionWithOffset(i, 200);
                                changePosition = i;
                                changePosition++;
                                break;
                            } else {
                                LogUtils.e("播放时间超过数据库获取的时间，文件不完整");
                            }
                        }
                    }
                } else if (beanTime - playTime > 3) {
                    //回退回去的情况，
                    ArrayList<String> list = getDataStartTime2();
                    for (int i = 0; i < texts.size(); i++) {
                        if (playTime - Double.valueOf(list.get(i)) > 0 && playTime - Double.valueOf(list.get(i)) < 2) {
                            mLayoutManager.scrollToPositionWithOffset(i, 200);
                            changePosition = i;
                            changePosition++;
                        }
                    }
                }
                //当前播放的进度,会导致一直刷新
//                adapter.changeColors(StoryDataManager.cmPlayer.getCurrentPosition() / 1000.0,changePosition);
            }
            //smoothByTime();
            textHandler.postDelayed(this, 1000);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler refreshSeekBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    //if(StoryDataManager.cmPlayer!=null&&StoryDataManager.cmPlayer.isPlaying())
                    if (getActivity() != null && !getActivity().isDestroyed()) {
                        seekBar.setProgress(storyDataManager.getCmPlayer().getCurrentPosition());
                        refreshSeekBarHandler.sendEmptyMessageDelayed(0, 200);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 上传学习记录
    public void updateStudyRecord() {
        Date now = new Date();
        endTime = sdf.format(now);

        // TODO: 2023/2/2 存储
        AppDatabase.getInstance(CrashApplication.getInstance()).getSoundRecordDao().insert(
                new SoundRecord(
                        null,
                        AccountManager.getInstance().userId + "",
                        chapterInfo.getVoaid(),
                        chapterInfo.getTypes(),
                        storyDataManager.getCmPlayer().getCurrentPosition(),
                        beginTime,
                        endTime
                )
        );

        RequestUpdateStudyRecord request;
        try {
            request = new RequestUpdateStudyRecord(AccountManager.getInstance().userId + "",
                    beginTime, endTime, getResources().getString(R.string.app_name),
                    Integer.valueOf(chapterInfo.getLevel() +
                            chapterInfo.getOrderNumber() +
                            chapterInfo.getChapterOrder()), 1, result -> {
                RequestUpdateStudyRecord response = (RequestUpdateStudyRecord) result;
                if (response.isRequestSuccessful()) {
                    Log.e("TAG", "uploadStudyRecord" + "测试记录同步成功");
                } else {
                    Log.e("TAG", "updateStudyRecord: 测试记录同步失败");
                }
            });
            CrashApplication.getInstance().getQueue().add(request);
        } catch (Exception e) {
            Log.e("TAG", "updateStudyRecord: " + e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //播放完成监听
    public void onEvent(PlayerCompletionEvent event) {
    }


    /**
     * 原文播放起的event
     * @param onPlayChangeEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayChange(@NotNull OnPlayChangeEvent onPlayChangeEvent) {
        if (!storyDataManager.getCmPlayer().isPlaying()) {
            try {
                refreshSeekBarHandler.sendEmptyMessage(0);
                iv_playcontrol.setImageResource(R.drawable.new_play);
                if(beginTime == null){
                    Date now = new Date();
                    beginTime = sdf.format(now);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            refreshSeekBarHandler.removeMessages(0);
            iv_playcontrol.setImageResource(R.drawable.new_pause);
            updateStudyRecord();
        }
    }

    //暂停播放
    private void pauseReadPlayer(){
        if (storyDataManager.getCmPlayer().mIsPrepared){
            if (storyDataManager.getCmPlayer().isPlaying()){
                storyDataManager.getCmPlayer().pause();
                refreshSeekBarHandler.removeMessages(0);
                iv_playcontrol.setImageResource(R.drawable.new_play);
            }
        }
    }

    /***************************广告计时器***************************/
    //广告定时器
    private static final String timer_ad = "timer_ad";
    //广告间隔时间
    private static final long adScaleTime = 20*1000L;
    //开始计时
    private void startAdTimer() {
        stopAdTimer();
        LibRxTimer.getInstance().multiTimerInMain(timer_ad, 0, adScaleTime, new LibRxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                showBannerAd();
            }
        });
    }
    //停止计时
    private void stopAdTimer() {
        LibRxTimer.getInstance().cancelTimer(timer_ad);
    }

    /*******************************新的banner广告显示**********************/
    //是否已经获取了奖励
    private boolean isGetRewardByClickAd = false;
    //显示的界面模型
    private AdBannerViewBean bannerViewBean = null;
    //显示banner广告
    private void showBannerAd(){
        //请求广告
        if (bannerViewBean==null){
            bannerViewBean = new AdBannerViewBean(iyubaSdkAdLayout, webAdLayout, webAdImage, webAdClose,webAdTips, new AdBannerShowManager.OnAdBannerShowListener() {
                @Override
                public void onLoadFinishAd() {

                }

                @Override
                public void onAdShow(String adType) {
                    adLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdClick(String adType, boolean isJumpByUserClick, String jumpUrl) {
                    pauseReadPlayer();

                    if (isJumpByUserClick){
                        if (TextUtils.isEmpty(jumpUrl)){
                            ToastUtil.showToast(getActivity(),"暂无内容");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.setClass(getActivity(), WebActivity.class);
                        intent.putExtra("url", jumpUrl);
                        startActivity(intent);
                    }
                }

                @Override
                public void onAdClose(String adType) {
                    //关闭界面
                    adLayout.setVisibility(View.GONE);
                    //关闭计时器
                    stopAdTimer();
                    //关闭广告
                    AdBannerShowManager.getInstance().stopBannerAd();
                }

                @Override
                public void onAdError(String adType) {

                }
            });
            AdBannerShowManager.getInstance().setShowData(getActivity(),bannerViewBean);
        }
        AdBannerShowManager.getInstance().showBannerAd();
        //重置数据
        isGetRewardByClickAd = false;
    }
    //配置广告显示
    private void refreshAd(){
        if (!AccountManager.getInstance().isVip(getActivity()) && !AdBlocker.getInstance().shouldBlockAd()) {
            startAdTimer();
        }else {
            adLayout.setVisibility(View.GONE);
            stopAdTimer();
            AdBannerShowManager.getInstance().stopBannerAd();
        }
    }
}