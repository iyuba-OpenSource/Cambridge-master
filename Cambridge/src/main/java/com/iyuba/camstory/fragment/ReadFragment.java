package com.iyuba.camstory.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.common.LogUtil;
import com.iyuba.camstory.API.ApiRetrofit;
import com.iyuba.camstory.API.ApiService;
import com.iyuba.camstory.API.data.EvaSendBean;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.SenListAdapter;
import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.EvaluateRecord;
import com.iyuba.camstory.bean.EvaluateResponse;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.AddCreditsRequest;
import com.iyuba.camstory.listener.OnPlayStateChangedListener;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestUpdateStudyRecord;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.popup.CorrectPopup;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.room.EvaluateRecordDao;
import com.iyuba.camstory.room.WordDao;
import com.iyuba.camstory.service.Background;
import com.iyuba.camstory.sqlite.mode.ReadVoiceComment;
import com.iyuba.camstory.sqlite.op.EvaluateRecordOp;
import com.iyuba.camstory.utils.Player;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.common.WebConstant;
import com.iyuba.http.LogUtils;
import com.iyuba.voa.activity.setting.Constant;
import com.iyuba.voa.frame.components.ConfigManagerVOA;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//跟读

@RuntimePermissions
public class ReadFragment extends Fragment implements SenListAdapter.SenListAdapterInteraction {
    public static final String TITLE = "title";
    private List<BookContentResponse.Texts> textsList;
    private String soundUrl = "";
    private BookDetailResponse.ChapterInfo chapterInfo;
    private String mTitle = "Defaut Value";
    private List<VoaDetail> voaDetails = new ArrayList<VoaDetail>();
    private SenListAdapter senListAdapter;
    String str;
    private Context mContext;
    private TextView tv_read_mix, tv_read_score, tv_read_share, imv_current_time, imv_total_time;
    private SeekBar seekBar;
    private String fileName;
    private int totalTime = 0, totalScore; // 进度条每次发生变化所需要的时间， 总得分剩余时间
    //private List<File> pcm_file_list = new ArrayList<>();
    private Boolean isSendSound = false, isMix = false;
    private Player cPlayer;
    private int changeTime = 0;
    private CustomDialog waittingDialog;
    // private Voa voaTemp;
    private String shuoshuoId;
    private int score;
    private HashMap<Integer, Integer> scoreMap = new HashMap<>();
    private HashMap<Integer, Long> timeMap = new HashMap<>();
    private HashMap<Integer, File> fileMap = new HashMap<>();
    List<Map.Entry<Integer, File>> list = new ArrayList<>(fileMap.entrySet());

    public static boolean flag = false;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String beginTime, endTime;
    private String composeVoicePath;//合成接口返回路径
    private ArrayList<String> web_path_list = new ArrayList<>();

    public ReadFragment(List<BookContentResponse.Texts> textsList, BookDetailResponse.ChapterInfo chapterInfo) {
        this.textsList = textsList;
        this.chapterInfo = chapterInfo;
        this.soundUrl = chapterInfo.getSound();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        waittingDialog = new WaittingDialog().wettingDialog(mContext);
        waittingDialog.show();
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }

        WordDao wordDao = AppDatabase.getInstance(requireContext()).getWordDao();
        EvaluateRecordDao evaluateDao = AppDatabase.getInstance(requireContext()).getEvaluateDao();

        for (int i = 0; i < textsList.size(); i++) {
            BookContentResponse.Texts texts = textsList.get(i);
            List<EvaluateRecord> list = evaluateDao.list(
                    AccountManager.getInstance().userId+"",
                    chapterInfo.getVoaid(),
                    texts.getIndex(),
                    chapterInfo.getTypes()
            );
            if(!list.isEmpty()){
                List<EvaluateResponse.Words> list1 = wordDao.list(
                        AccountManager.getInstance().userId+"",
                        chapterInfo.getVoaid(),
                        texts.getIndex(),
                        chapterInfo.getTypes()
                );
                EvaluateResponse evaluateResponse = new EvaluateResponse();
                evaluateResponse.setWords(list1);
                evaluateResponse.setScores(Integer.valueOf(list.get(0).getScore()));
                evaluateResponse.setTotal_score(Double.valueOf(list.get(0).getTotalScore()));
                evaluateResponse.setURL(list.get(0).getUrl());
                texts.setEvaluateResponse(evaluateResponse);
            }
        }

        //设置播放路径
        if (Background.vv!=null){
            Background.vv.setVideoPath("http://static2.iyuba.cn"+soundUrl);
        }
        cPlayer = new Player(mContext, playStateChangedListener);
        waittingDialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        ListView senList = view.findViewById(R.id.id_senlist);
        ImageView emptyBg = view.findViewById(R.id.empty_bg);
        if(textsList.isEmpty()){
            senList.setVisibility(View.GONE);
            emptyBg.setVisibility(View.VISIBLE);
        }

        tv_read_mix = view.findViewById(R.id.tv_read_mix);
        tv_read_share = view.findViewById(R.id.tv_read_share);
        tv_read_score = view.findViewById(R.id.tv_read_score);
        seekBar = view.findViewById(R.id.imv_seekbar_player);
        seekBar.setEnabled(false);
        imv_current_time = view.findViewById(R.id.imv_current_time);
        imv_total_time = view.findViewById(R.id.imv_total_time);
        senListAdapter = new SenListAdapter(getActivity(), textsList,chapterInfo, this,
                tv_read_mix, tv_read_share, tv_read_score, imv_current_time, imv_total_time, seekBar);
        senList.setAdapter(senListAdapter);
        senList.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            senListAdapter.setClickPosition(arg2);
            senListAdapter.notifyDataSetChanged();
        });

        senListAdapter.setOnCorrectClickListener(voaDetail -> {
            if(StoryDataManager.Instance().getCmPlayer().isPlaying()){
                StoryDataManager.Instance().getCmPlayer().pause();
            }
            CorrectPopup correctPopup = new CorrectPopup(mContext, voaDetail);
            correctPopup.showPopupWindow();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Date now = new Date();
        beginTime = sdf.format(now);
        tv_read_mix.setOnClickListener(v -> {
            if (tv_read_mix.getText().toString().equals("合成")) {
                updateStudyRecord();
                Date now1 = new Date();
                beginTime = sdf.format(now1);
                web_path_list.clear();

                for (Map.Entry<Integer, File> mapping : list) {
                    web_path_list.add(mapping.getValue().getPath());
                }
                if (web_path_list.size() <= 1) {
                    ToastUtil.showToast(mContext, "至少新评测两句方可合成！");
                    return;
                }
                //合成
                requestComposeVoice(web_path_list);
                // 算出平均成绩
                tv_read_score.setVisibility(View.VISIBLE);
                tv_read_score.setText(totalScore / web_path_list.size() + "");
                score = totalScore / web_path_list.size();
                // isMix = true;
            } else {
                changeTime = 100;
                tv_read_mix.setEnabled(false);
                playRecord2();
                //totalTime = cPlayer.getDuration() + 300;
                if (cPlayer.isPlaying() && totalTime != 0) {
                    handler.sendEmptyMessageDelayed(0, 100);//计算总时间
                }
            }
        });

        tv_read_share.setOnClickListener(v -> {
            if (tv_read_share.getText().equals("发布")) {
                if (!isMix) {
                    ToastUtil.showToast(mContext, "请先合成后再发布");
                    return;
                }
                sendSound();
            } else {
                showShareSound();
            }
        });

    }

    private void playSetData() {
        if (cPlayer != null && !composeVoicePath.equals("")) {
            if (cPlayer.isIdle()) {
                cPlayer.initialize("http://voa." + WebConstant.IYBHttpHead + "/voa/" + composeVoicePath);
            }
        }
    }

    // 播放合成录音文件
    public void playRecord2() {
        if (cPlayer != null && !composeVoicePath.equals("")) {
            if (cPlayer.isIdle()) {
                cPlayer.prepareAndPlay();
            } else if (cPlayer.isCompleted()) {
                cPlayer.start();
            } else if (cPlayer.isInitialized()) {
                cPlayer.prepareAndPlay();
            } else if (cPlayer.isPausing()) {
                cPlayer.start();
            }
        } else {
            ToastUtil.showToast(mContext, "还未合成成功！");
        }
    }


    //合成语音上发至服务器 发布  不能使用旧版本的post请求
    private void sendSound() {
        //VoaID 奇葩计算方法！！ 不可修改！！！！
        if (AccountManager.getInstance().checkUserLogin()) {
            if (isSendSound) {
                CustomToast.showToast(mContext, "评论发送中，请不要重复提交", 1000);
            } else {
                waittingDialog.show();
                if (composeVoicePath != null && !composeVoicePath.equals("")) {
                    //新的网络请求！！！
                    ApiService service = ApiRetrofit.getInstance().getApiService();
                    String head = "http://voa." + com.iyuba.configation.Constant.IYBHttpHead + "/voa/UnicomApi";
                    service.audioSendApi(head, Constant.TOPICID, "android",
                            "json", "60003", String.valueOf(AccountManager.getInstance().userId),
                            chapterInfo.getVoaid(), String.valueOf(totalScore / web_path_list.size()),
                            "4", composeVoicePath).enqueue(new Callback<EvaSendBean>() {
                        @Override
                        public void onResponse(Call<EvaSendBean> call, Response<EvaSendBean> response) {
                            String result = response.body().getResultCode();
                            shuoshuoId = String.valueOf(response.body().getShuoshuoId());
                            String addscore = String.valueOf(response.body().getAddScore());
                            if (result.equals("501") || result.equals("1")) {
                                waittingDialog.dismiss();
                                Message msg = handler
                                        .obtainMessage();

                                msg.what = 10;
                                msg.arg1 = Integer
                                        .parseInt(addscore);
                                commentHandler
                                        .sendMessage(msg);
                                rankHandler.sendEmptyMessage(3);
                                LogUtils.e("发布成功");
                            } else {
                                LogUtils.e("发布失败1");
                                rankHandler.sendEmptyMessage(4);
                            }
                        }

                        @Override
                        public void onFailure(Call<EvaSendBean> call, Throwable e) {
                            LogUtils.e("发布失败" + e);
                            rankHandler.sendEmptyMessage(4);
                        }
                    });
                } else {
                    LogUtils.e("发布失败，合成数据为空");
                    rankHandler.sendEmptyMessage(5);
                }
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, LoginActivity.class);
            getActivity().startActivity(intent);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler rankHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    ToastUtil.showToast(mContext, "发布成功");
                    tv_read_share.setText("分享");
                    break;
                case 4:
                    ToastUtil.showToast(mContext, "发布失败");
                    break;
                case 5:
                    ToastUtil.showToast(mContext, "发布失败，合成数据为空");
                    break;
                default:
                    break;
            }
        }
    };

    public void showShareSound() {

        String siteUrl = "http://voa." + com.iyuba.configation.Constant.IYBHttpHead + "/voa/play.jsp?id="
                + shuoshuoId + "&addr=" + composeVoicePath + "&apptype=" + Constant.TOPICID;

        String text = "我在爱语吧语音评测中获得了" + (totalScore / web_path_list.size()) + "分";
        String imageUrl = "http://app." + Constant.IYBHttpHead + "/android/images/CamStory/CamStory.png";
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.removeAccount(true);
        ShareSDK.removeCookieOnAuthorize(true);
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(text);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(siteUrl);
        // text是分享文本，所有平台都需要这个字段
        // oks.setText(voaTemp.title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");
        // imageUrl是Web图片路径，sina需要开通权限
        oks.setImageUrl(imageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(siteUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("爱语吧的这款应用" + getResources().getString(R.string.app_name) + "真的很不错啊~推荐！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getResources().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(siteUrl);
        // oks.setDialogMode();
        // oks.setSilent(false);
        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享失败===", arg2.toString());

            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                Log.e("okCallbackonComplete", "onComplete");
                if (AccountManager.getInstance().UserId != null) {
                    Message msg = new Message();
                    msg.obj = arg0.getName();
                    if (arg0.getName().equals("QQ")
                            || arg0.getName().equals("Wechat")
                            || arg0.getName().equals("WechatFavorite")) {
                        msg.what = 49;
                    } else if (arg0.getName().equals("QZone")
                            || arg0.getName().equals("WechatMoments")
                            || arg0.getName().equals("SinaWeibo")
                            || arg0.getName().equals("TencentWeibo")) {
                        msg.what = 19;
                    }
                    handler.sendMessage(msg);
                } else {
                    handler.sendEmptyMessage(13);
                }
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                LogUtils.e("okCallbackonCancel", "onCancel");
            }
        });
        // 启动分享GUI
        oks.show(mContext);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (cPlayer.isPlaying()) {
                        //imv_total_time.setText(getDurationInFormat());
                        seekBar.setMax(totalTime);
                        seekBar.setProgress(cPlayer.getCurrentTime());
                        LogUtils.e("播放 总时间" + totalTime + "进度" + cPlayer.getCurrentTime());
                        if (cPlayer.getCurrentTime() < totalTime) {
                            changeTime = changeTime + 100;
                            imv_current_time.setText(cPlayer.getCurrentTimeInFormat());
                            handler.sendEmptyMessageDelayed(0, 100);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                    break;
                case 1:
                    seekBar.setMax(100);
                    seekBar.setProgress(100);
                    imv_current_time.setText(getDurationInFormat());
                    tv_read_mix.setEnabled(true);
                    break;
                case 8:
                    ToastUtil.showToast(mContext, "合成失败！");
                    isMix = false;
                    break;
                case 9:
                    ToastUtil.showToast(mContext, "合成成功！");
                    tv_read_mix.setText("试听");
                    isMix = true;
                    cPlayer.reset();
                    playSetData();
                    break;
                case 13:
                    Toast.makeText(mContext, "分享成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 19:
                case 49:
                    if (AccountManager.getInstance()
                            .checkUserLogin()) {
                        final ReadVoiceComment rvc = new ReadVoiceComment();
                        RequestCallBack rc = new RequestCallBack() {

                            @Override
                            public void requestResult(
                                    Request result) {
                                AddCreditsRequest rq = (AddCreditsRequest) result;
                                if (rq.isShareFirstlySuccess()) {
                                    String msg = "分享成功，增加了"
                                            + rq.addCredit
                                            + "积分，共有"
                                            + rq.totalCredit
                                            + "积分";
                                    CustomToast.showToast(
                                            mContext, msg,
                                            3000);
                                } else if (rq
                                        .isShareRepeatlySuccess()) {
                                    CustomToast.showToast(
                                            mContext,
                                            "分享成功", 3000);
                                }
                            }
                        };
                        int uid = Integer.parseInt(AccountManager
                                .getInstance().UserId);
                        AddCreditsRequest rq = new AddCreditsRequest(
                                uid, rvc.getVoaRef().voaid,
                                msg.what, rc);
                        RequestQueue queue = Volley
                                .newRequestQueue(mContext);
                        queue.add(rq);
                    }
                    break;
                case 14:
                    ToastUtil.showToast(mContext, "发布失败");
                    if (waittingDialog.isShowing()) {
                        waittingDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 时间格式转化为00：00
     *
     * @return
     */
    private String getDurationInFormat() {
        StringBuffer sb = new StringBuffer("");
        int musicTime = totalTime / 1000;

        String minu = String.valueOf(musicTime / 60);
        if (minu.length() == 1) {
            minu = "0" + minu;
        }
        String sec = String.valueOf(musicTime % 60);
        if (sec.length() == 1) {
            sec = "0" + sec;
        }

        sb.append(minu).append(":").append(sec);
        return sb.toString();
    }

    @Override
    public void onAttach(Activity paramActivity) {
        mContext = paramActivity;
        super.onAttach(paramActivity);
    }

    OnPlayStateChangedListener playStateChangedListener = new OnPlayStateChangedListener() {

        @Override
        public void onPrepared() {
            totalTime = cPlayer.getDuration() + 300;
            LogUtils.e("播放 总时间计算：" + totalTime);
            imv_total_time.setText(getDurationInFormat());
            if (cPlayer.isPlaying()) {
                handler.sendEmptyMessageDelayed(0, 100);//计算总时间
            }
        }

        @Override
        public void playCompletion() {
            handler.sendEmptyMessage(1);//播放完成
        }

        @Override
        public void playFaild() {
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (senListAdapter != null) {
            senListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getIndex(String fileName, int score, long adapterTime, int senIndex) {
        this.fileName = fileName;

        if (fileMap.containsKey(senIndex)) {
            fileMap.remove(senIndex);
            totalScore -= scoreMap.get(senIndex);
            totalTime -= timeMap.get(senIndex);
            scoreMap.remove(senIndex);
            timeMap.remove(senIndex);
        } else {
        }
        fileMap.put(senIndex, new File(fileName));
        scoreMap.put(senIndex, score);
        timeMap.put(senIndex, adapterTime);
        totalScore += score;
        totalTime += adapterTime;
        List<Map.Entry<Integer, File>> list1 = new ArrayList<>(fileMap.entrySet());
        list = list1;
        Collections.sort(list, (o1, o2) -> o1.getKey() - o2.getKey());

    }

    //请求权限调用方法
    @Override
    public void getPermission() {
        ReadFragmentPermissionsDispatcher.openRecordWithPermissionCheck(this);
    }

    @SuppressLint("HandlerLeak")
    Handler commentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String addscore = String.valueOf(msg.arg1);
                    if (addscore.equals("5")) {
                        String mg = "语音成功发送至评论区，恭喜您获得了" + addscore + "分";
                        CustomToast.showToast(mContext, mg, 3000);
                    } else {
                        String mg = "语音成功发送至评论区";
                        CustomToast.showToast(mContext, mg, 3000);
                    }
                    break;
            }
        }
    };

    public void onStop() {
        super.onStop();
        if(cPlayer != null){
            cPlayer.stopPlay();
        }
    }


    public void updateStudyRecord() {
        Date now = new Date();
        endTime = sdf.format(now);

        RequestUpdateStudyRecord request;
        try {
            request = new RequestUpdateStudyRecord(AccountManager.getInstance().userId + "",
                    beginTime, endTime, getResources().getString(R.string.app_name),
                    Integer.valueOf(chapterInfo.getLevel() + chapterInfo.getOrderNumber() + chapterInfo.getChapterOrder()), 1, result -> {
                                RequestUpdateStudyRecord response = (RequestUpdateStudyRecord) result;
                                if (response.isRequestSuccessful()) {
                                    Log.e("uploadStudyRecord", "success");
                                } else {
                                    Log.e("uploadStudyRecord", "测试记录同步失败");
                                }
                            });
            CrashApplication.getInstance().getQueue().add(request);
        } catch (UnsupportedEncodingException e) {
            Log.e("TAG", "updateStudyRecord: ");
            e.printStackTrace();
        }
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openRecord() {

    }

    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForRecord() {
        Toast.makeText(mContext, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ReadFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 合成录音请求
     */
    private void requestComposeVoice(ArrayList<String> pathList) {

        StringBuffer stringBuffer = new StringBuffer();

        for (String str : pathList) {
            stringBuffer.append(str).append(",");
        }
        String path = stringBuffer.toString();
        path = path.substring(0, path.length() - 1);
        Map<String, String> pathParams = new HashMap<String, String>();
        Map<String, String> typeParams = new HashMap<String, String>();

        pathParams.put("audios", path);
        typeParams.put("type", Constant.TOPICID);//familyalbum

        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().
                connectTimeout(15, TimeUnit.SECONDS).
                readTimeout(15, TimeUnit.SECONDS).
                writeTimeout(15, TimeUnit.SECONDS)
                .build();
        //文本类的
        MultipartBody.Builder urlBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (pathParams != null) {
            for (String key : pathParams.keySet()) {
                if (pathParams.get(key) != null) {
                    urlBuilder.addFormDataPart(key, pathParams.get(key));
                }
            }
        }

        if (typeParams != null) {
            for (String key : typeParams.keySet()) {
                if (typeParams.get(key) != null) {
                    urlBuilder.addFormDataPart(key, typeParams.get(key));
                }
            }
        }
        // String actionUrl = "http://speech." + com.iyuba.configation.Constant.IYBHttpHead + "/test/merge/";
        String actionUrl = WebConstant.HTTP_SPEECH_ALL + "/test/merge/";

        LogUtils.e("合成提交 " + "path:" + path + Constant.TOPICID + actionUrl);
        // 构造Request->call->执行
        final okhttp3.Request request = new okhttp3.Request.Builder().headers(new Headers.Builder().build())//extraHeaders 是用户添加头
                .url(actionUrl).post(urlBuilder.build())//参数放在body体里
                .build();

        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                LogUtil.e("合成请求失败" + e);
                //handler.sendEmptyMessage(8);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        LogUtil.e("合成请求返回数据：" + jsonObject);
                        String result = jsonObject.getString("result");
                        if (result.equals("1")) {
                            composeVoicePath = jsonObject.getString("URL");
                            String message = jsonObject.getString("message");
                            LogUtil.e("合成请求请求成功" + composeVoicePath);//返回的试听地址拼接前缀:http://voa.iyuba.cn/voa/
                            handler.sendEmptyMessage(9);
                        } else {
                            handler.sendEmptyMessage(8);
                            LogUtil.e("合成请求请求失败0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e("合成请求请求失败" + e.getMessage());
                        handler.sendEmptyMessage(8);
                    }
                } else {
                    LogUtil.e("合成请求失败2");
                    handler.sendEmptyMessage(8);
                }
            }
        });
    }


}