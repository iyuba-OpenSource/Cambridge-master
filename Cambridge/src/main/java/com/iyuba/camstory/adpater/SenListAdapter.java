package com.iyuba.camstory.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.common.LogUtil;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.EvaluateRecord;
import com.iyuba.camstory.bean.EvaluateResponse;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.listener.AddCreditsRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.ReadEvaluateManager;
import com.iyuba.camstory.manager.RecordManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.manager.VoaPlayManager;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.room.WordDao;
import com.iyuba.camstory.sqlite.mode.ReadVoiceComment;
import com.iyuba.camstory.sqlite.op.EvaluateRecordOp;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.utils.NetWorkStateUtil;
import com.iyuba.camstory.utils.ResultParse;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.utils.UtilPostFile;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.RoundProgressBar;
import com.iyuba.camstory.widget.WaittingDialog;

import com.iyuba.voa.activity.setting.Constant;

import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.PermissionUtils;


public class SenListAdapter extends BaseAdapter {
    private static final String TAG = SenListAdapter.class.getSimpleName();
    private Context mContext;
    private List<VoaDetail> mList1;
    private List<BookContentResponse.Texts> mList;
    private BookDetailResponse.ChapterInfo chapterInfo;
    /**
     * 用来记录是否分享，是否发布过音频
     */
    private HashMap<Integer, ReadVoiceComment> mMap;
    private LayoutInflater mInflater;
    public int clickPosition = 0;
    private RecordManager recordManager;//评测播放管理？
    private VoaPlayManager voaPlayManager;
    //private IseManager iseManager;
    private ReadEvaluateManager mEManager;//新的评测管理类
    private int senIndex;
    private BookContentResponse.Texts clickDetail;
    private ViewHolder clickViewHolder;
    private String fileName;
    private boolean isSendSound = false;
    private WaittingDialog waittingDialog;
    private String shuoshuoId;
    private int voaId;
    private TextView mixView;
    private TextView shareView;
    private TextView scoreView;
    private TextView currenTime;
    private TextView totalTime;
    private SeekBar seekBar;
    private EvaluateResponse evaluateResponse;
    private EvaluateRecordOp evaluateRecordOp = new EvaluateRecordOp();

    public SenListAdapter(Context mContext, List<BookContentResponse.Texts> mList, BookDetailResponse.ChapterInfo chapterInfo,
                          SenListAdapterInteraction senListAdapterInteraction, TextView mixView,
                          TextView shareView, TextView scoreView, TextView currenTime, TextView totalTime, SeekBar seekBar) {
        this.mContext = mContext;
        this.mList = mList;
        this.chapterInfo = chapterInfo;
        mInflater = LayoutInflater.from(mContext);
        mMap = new HashMap<>();
        recordManager = RecordManager.getInstance(mContext);
        recordManager.initPlayer();
        voaPlayManager = VoaPlayManager.getInstance(mContext);
        mEManager = ReadEvaluateManager.getInstance(mContext);
        waittingDialog = new WaittingDialog();
        this.senListAdapterInteraction = senListAdapterInteraction;
        this.mixView = mixView;
        this.shareView = shareView;
        this.scoreView = scoreView;
        this.currenTime = currenTime;
        this.totalTime = totalTime;
        this.seekBar = seekBar;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BookContentResponse.Texts curDetail = mList.get(position);
        final int curPosition = position;
        final ViewHolder curViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_read, null);
            curViewHolder = new ViewHolder();
            curViewHolder.senIndex = convertView
                    .findViewById(R.id.sen_index);
            curViewHolder.senEn = convertView.findViewById(R.id.sen_en);
            curViewHolder.senZh = convertView.findViewById(R.id.sen_zh);
            curViewHolder.senPlay = convertView
                    .findViewById(R.id.sen_play);//原文播放按钮¬
            curViewHolder.senIRead = convertView
                    .findViewById(R.id.sen_i_read);//评测按钮
            curViewHolder.senReadPlayButton = convertView
                    .findViewById(R.id.sen_read_button);
            curViewHolder.senReadPlay = convertView
                    .findViewById(R.id.sen_read_play);//录音播放按钮
            curViewHolder.senReadPlaying = convertView
                    .findViewById(R.id.sen_read_playing);
            curViewHolder.senReadCorrect = convertView
                    .findViewById(R.id.sen_read_correct);
            curViewHolder.senReadSend = convertView
                    .findViewById(R.id.sen_read_send);
            curViewHolder.senReadCollect = convertView
                    .findViewById(R.id.sen_read_collect);//分享
            // curViewHolder.senReadCollect.setVisibility(View.GONE);
            curViewHolder.senReadResult = convertView
                    .findViewById(R.id.sen_read_result);
            curViewHolder.sepLine = convertView
                    .findViewById(R.id.sep_line);
            curViewHolder.bottomView = convertView.findViewById(R.id.bottom_view);

            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }

        if (curPosition ==  clickPosition) {
            clickViewHolder = curViewHolder;
            clickDetail = curDetail;
        }

        if (mMap.containsKey(curPosition)) {
            curViewHolder.senReadCollect.setVisibility(View.VISIBLE);
        } else {
            curViewHolder.senReadCollect.setVisibility(View.GONE);
        }

        curViewHolder.senIndex.setText((position + 1) + "");
        curViewHolder.senZh.setText(curDetail.getTextCH());

        /**
         * 如果已经评测过设置成绩
         */
        if(curDetail.getEvaluateResponse() != null && curDetail.getEvaluateResponse().getURL() != null){
            String[] floats = new String[curDetail.getEvaluateResponse().getWords().size()];
            for (int i = 0; i < curDetail.getEvaluateResponse().getWords().size(); i++) {
                floats[i] = String.valueOf(curDetail.getEvaluateResponse().getWords().get(i).getScore());
            }
            curDetail.setRead(true);
            curDetail.getTextEN();
            curDetail.setReadResult(ResultParse.getSenResultLocal(floats, curDetail.getTextEN()));
            curDetail.setReadScore(curDetail.getEvaluateResponse().getScores());//分数
        }

        if (curDetail.isRead()) {
            curViewHolder.senEn.setText(curDetail.getReadResult());
            if (curDetail.getReadScore() < 50) {
                curViewHolder.senReadResult.setBackgroundResource(R.drawable.sen_score_lower60);
                curViewHolder.senReadResult.setText("");
            } else if (curDetail.getReadScore() > 80) {
                curViewHolder.senReadResult.setText(curDetail.getReadScore() + "");
                curViewHolder.senReadResult.setBackgroundResource(R.drawable.sen_score_higher_80);
            } else {
                curViewHolder.senReadResult.setText(curDetail.getReadScore() + "");
                curViewHolder.senReadResult.setBackgroundResource(R.drawable.sen_score_60_80);
            }
        } else {
            curViewHolder.senEn.setText(curDetail.getTextEN());
        }

        curViewHolder.bottomView.getParent().requestDisallowInterceptTouchEvent(
                true);
        if (curPosition != clickPosition) {
            curViewHolder.sepLine.setVisibility(View.GONE);
            curViewHolder.bottomView.setVisibility(View.GONE);
        } else {
            curViewHolder.sepLine.setVisibility(View.VISIBLE);
            curViewHolder.bottomView.setVisibility(View.VISIBLE);
            if (curDetail.isRead()) {
                curViewHolder.senReadPlayButton.setVisibility(View.VISIBLE);
                curViewHolder.senReadSend.setVisibility(View.VISIBLE);
                curViewHolder.senReadResult.setVisibility(View.VISIBLE);
                curViewHolder.senReadCorrect.setVisibility(View.VISIBLE);

            } else {
                curViewHolder.senReadPlayButton.setVisibility(View.INVISIBLE);
                curViewHolder.senReadSend.setVisibility(View.INVISIBLE);
                curViewHolder.senReadResult.setVisibility(View.INVISIBLE);
                curViewHolder.senReadCorrect.setVisibility(View.INVISIBLE);
            }
        }

        //原文播放
        curViewHolder.senPlay.setOnClickListener(view -> {
            if(StoryDataManager.Instance().getCmPlayer().isPlaying()){
                StoryDataManager.Instance().getCmPlayer().pause();
            }
            if (mEManager.isRecording) {
                mEManager.stopEvaluating();
                handler.sendEmptyMessage(3);
            }
            if (recordManager.isPlaying()) {
                recordManager.stopPlayRecord2();
                handler.removeMessages(4);
                handler.sendEmptyMessage(5);
            }
            if (voaPlayManager==null){
                return;
            }
            if (voaPlayManager.isStoppedAndCouldPlay()) {
                curViewHolder.senPlay.setBackgroundResource(R.mipmap.sen_stop);
                senIndex = clickPosition;
                voaPlayManager.playSen(curDetail);
                Message msg=handler.obtainMessage();
                msg.what=0;
                msg.arg1=position;
                handler.sendEmptyMessage(0);
            } else if (voaPlayManager.isPlaying()) {
                curViewHolder.senPlay.setBackgroundResource(R.mipmap.sen_play);
                voaPlayManager.pausePlaySen();
                handler.removeMessages(0);
            } else if (voaPlayManager.isPausing()) {
                curViewHolder.senPlay.setBackgroundResource(R.mipmap.sen_stop);
                voaPlayManager.rePlaySen();
                handler.sendEmptyMessage(0);
            }
        });

        //评测按钮
        curViewHolder.senIRead.setOnClickListener(view -> {
            if (AccountManager.getInstance().loginStatus == 0) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return;
            }
            //权限请求
            final String[] PERMISSION_OPENRECORD = new String[] {"android.permission.RECORD_AUDIO","android.permission.WRITE_EXTERNAL_STORAGE"};
            if (!PermissionUtils.hasSelfPermissions(mContext, PERMISSION_OPENRECORD)) {
                if (senListAdapterInteraction != null) {
                    senListAdapterInteraction.getPermission();
                    return;
                }
            }
            if (!NetWorkStateUtil.isConnectingToInternet()) {
                CustomToast.showToast(mContext, R.string.evaluate_nonetwork, 1000);
                return;
            }
            //播放控制
            if (voaPlayManager.isPlaying()) {
                voaPlayManager.stopPlaySen();
                handler.removeMessages(0);
                handler.sendEmptyMessage(1);
            }
            if (recordManager.isPlaying()) {
                recordManager.stopPlayRecord2();
                handler.removeMessages(4);
                handler.sendEmptyMessage(5);
            }

            if (!mEManager.isRecording) {
                if(StoryDataManager.Instance().getCmPlayer().isPlaying()){
                    StoryDataManager.Instance().getCmPlayer().pause();
                }
                senIndex = clickPosition;
                curViewHolder.senIRead.setBackgroundResource(R.mipmap.sen_i_stop);
                try {
                    //开始录音
                    handler.sendEmptyMessage(16);//录音中
                    Map<String, String> textParams = new HashMap<>();
                    textParams.put("type", Constant.TOPICID);
                    textParams.put("userId", String.valueOf(AccountManager.getInstance().userId));
                    textParams.put("newsId", curDetail.getVoaid() + "");
                    textParams.put("paraId", curDetail.getParaid() + "");
                    textParams.put("IdIndex", curDetail.getIndex());
                    String sentence = URLEncoder.encode(curDetail.getTextEN(), "UTF-8").replace("+", "%20");
                    textParams.put("sentence", sentence);
                    textParams.put("wordId", "0");
                    textParams.put("flg", "0");
                    textParams.put("appId", Constant.getAppid());

                    mEManager.startRecord(position, curDetail.getTextEN(), Integer.valueOf(curDetail.getVoaid()), textParams, handler);
                   //handler.sendEmptyMessage(104);
                    int time=(int)((Double.valueOf(clickDetail.getEndTiming())-Double.valueOf(clickDetail.getBeginTiming())) * 1.7 * 1000);
                    if (time<4000){
                        time=4000;
                    }
                    Message msg=handler.obtainMessage();
                    msg.what=11;
                    msg.arg1=position;
                    handler.sendMessageDelayed(msg, time);//延时1.5倍关闭·
                } catch (ParseException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                notifyDataSetChanged();
            } else {
                curViewHolder.senIRead.setBackgroundResource(R.mipmap.sen_i_read);
                clickViewHolder.senIRead.setMax(20000);

                Message msg=handler.obtainMessage();
                msg.what=11;
                msg.arg1=position;
                handler.sendMessage(msg);
                handler.sendEmptyMessage(3);
            }
            if (!mixView.getText().toString().equals("合成")) {
                mixView.setText("合成");
                scoreView.setVisibility(View.GONE);
                //currenTime.setText("00:00");
                //totalTime.setText("00:00");
                seekBar.setProgress(0);
            }
            if (!shareView.getText().toString().equals("发布")) {
                shareView.setText("发布");
            }

        });

        //录音播放按钮
        curViewHolder.senReadPlayButton.setOnClickListener(view -> {
            if(StoryDataManager.Instance().getCmPlayer().isPlaying()){
                StoryDataManager.Instance().getCmPlayer().pause();
            }
            if (voaPlayManager.isPlaying()) {
                voaPlayManager.stopPlaySen();
                handler.removeMessages(0);
                handler.sendEmptyMessage(1);
            }
            if (mEManager.isRecording) {
                mEManager.stopEvaluating();
                handler.sendEmptyMessage(3);
            }
            LogUtils.i(TAG, "rmPlayer state : " + recordManager.showPlayerState());
            if (recordManager.isStoppedAndCouldPlay()) {
                //clickViewHolder.senReadPlay.setVisibility(View.GONE);
                curViewHolder.senReadPlay.setVisibility(View.GONE);
                //clickViewHolder.senReadPlaying.setVisibility(View.VISIBLE);
                curViewHolder.senReadPlaying.setVisibility(View.VISIBLE);
                senIndex = clickPosition;
               // recordManager.playRecord2(senIndex);
                if (evaluateResponse!=null&&evaluateResponse.getLocalMP3Path()!=null) {
                    //recordManager.playRecord2("http://voa."+Constant.IYBHttpHead+"/voa/"+evaluatBean.getURL());
                    recordManager.playRecord2(evaluateResponse.getLocalMP3Path());
                }
                Message msg=handler.obtainMessage();
                msg.what=4;
                msg.arg1=position;
                //handler.sendEmptyMessage(4);
                handler.sendMessage(msg);
                // notifyDataSetChanged();
            } else if (recordManager.isPlaying()) {
                curViewHolder.senReadPlaying.setVisibility(View.GONE);
                curViewHolder.senReadPlay.setVisibility(View.VISIBLE);
                recordManager.pausePlayRecord();
                Message msg=handler.obtainMessage();
                msg.what=4;
                msg.arg1=position;
                handler.removeMessages(4);
                //notifyDataSetChanged();
            } else if (recordManager.isPausing()) {
                curViewHolder.senReadPlay.setVisibility(View.GONE);
                curViewHolder.senReadPlaying.setVisibility(View.VISIBLE);
                recordManager.rePlayRecord();
                Message msg=handler.obtainMessage();
                msg.what=4;
                msg.arg1=position;
                handler.sendEmptyMessage(4);
                //notifyDataSetChanged();
            }
        });

        //纠音按钮
        curViewHolder.senReadCorrect.setOnClickListener(view->{
            if(this.correctClickListener != null){
                correctClickListener.onClick(mList.get(position));
            }
        });
        //分享
        curViewHolder.senReadCollect.setOnClickListener(view -> {
            if (!mMap.containsKey(curPosition))
                return;

            String siteUrl = "http://voa."+Constant.IYBHttpHead+"/voa/play.jsp?id=" + shuoshuoId;

            String text = "我在爱语吧语音评测中获得了" + curDetail.getReadScore() + "分";
            String imageUrl = "http://app."+Constant.IYBHttpHead+"/android/images/CamStory/CamStory.png";
            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.removeAccount(true);
            ShareSDK.removeCookieOnAuthorize(true);
            OnekeyShare oks = new OnekeyShare();
            // 关闭sso授权
            oks.disableSSOWhenAuthorize();
            oks.setTitle(text);
            oks.setTitleUrl(siteUrl);
            oks.setImageUrl(imageUrl);
            oks.setUrl(siteUrl);
            oks.setComment("爱语吧的这款应用" + mContext.getResources().getString(R.string.app_name) + "真的很不错啊~推荐！");
            oks.setSite(mContext.getResources().getString(R.string.app_name));
            oks.setSiteUrl(siteUrl);
            mMap.remove(curPosition);
            oks.setCallback(new PlatformActionListener() {
                @Override
                public void onError(Platform arg0, int arg1, Throwable arg2) {
                    Log.e("--分享失败===", arg2.toString());
                }
                @Override
                public void onComplete(Platform arg0, int arg1,
                                       HashMap<String, Object> arg2) {
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
                }
            });
            // 启动分享GUI
            oks.show(mContext);

        });

        //语音发送
        // TODO: 2023/1/7 语音发送，需要修改
        curViewHolder.senReadSend.setOnClickListener(view -> {
            voaId = Integer.valueOf(chapterInfo.getVoaid());
            if (mMap.containsKey(curPosition)) {
                CustomToast.showToast(mContext, "请勿重复发送同一语音", 1000);
                return;
            }
            if (AccountManager.getInstance().checkUserLogin()) {
                if (isSendSound) {
                    CustomToast.showToast(mContext, "评论发送中，请不要重复提交", 1000);
                } else {
                    waittingDialog.wettingDialog(mContext);
                    Thread threadsend = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> textParams = new HashMap<>();
                            Map<String, File> fileParams = new HashMap<>();
//                                File file = new File(Constant.getRecordAddr() + senIndex
//                                        + Constant.getRecordTag());
                            File file=null;
                            if (evaluateResponse!=null){
                             file =new File(evaluateResponse.getLocalMP3Path());
                            }
                            textParams.put("topic", "camstory");
                            textParams.put("idIndex", curDetail.getIndex());// idIndex表示句子行数
                            //textParams.put("paraid", curDetail.getParaid());
                            fileParams.put("content.acc", file);

                            if (file != null && file.exists()) {
                                try {
                                    isSendSound = true;
                                    String response = UtilPostFile
                                            .post("http://voa."+Constant.IYBHttpHead+"/voa/UnicomApi?topic=camstory"
                                                    + "&platform=android&format=json&protocol=60003"
                                                    + "&userid="
                                                    + AccountManager
                                                    .getInstance().userId
                                                    + "&voaid="
                                                    + voaId
                                                    + "&paraid="
                                                    + curDetail.getParaid()
                                                    + "&idIndex=" + curDetail.getIndex()
                                                    + "&score="
                                                    + curDetail.getReadScore()
                                                    + "&shuoshuotype=2", textParams, fileParams);
                                    LogUtils.v("评测发布地址 actionUrl http://voa."+Constant.IYBHttpHead+"/voa/UnicomApi?topic=camstory"
                                            + "&platform=android&format=json&protocol=60003"
                                            + "&userid="
                                            + AccountManager
                                            .getInstance().userId
                                            + "&voaid="
                                            + voaId
                                            + "&paraid="
                                            + voaId
                                            + "&idIndex=" + curDetail.getIndex()
                                            + "&score="
                                            + curDetail.getReadScore()
                                            + "&shuoshuotype=2");
                                    LogUtil.e("sendRank"+response);
                                    isSendSound = false;

                                    JSONObject jsonObjectRoot;
                                    jsonObjectRoot = new JSONObject(
                                            response);
                                    String result = jsonObjectRoot
                                            .getInt("ResultCode") + "";
                                    ReadVoiceComment rvc = new ReadVoiceComment(mList.get(curPosition));

                                    rvc.shuoshuo = jsonObjectRoot
                                            .getString("FilePath");
                                    mMap.put(curPosition, rvc);


                                    shuoshuoId = jsonObjectRoot
                                            .getInt("ShuoShuoId") + "";
                                    String addscore = jsonObjectRoot
                                            .getString("AddScore");

                                    if (result.equals("1")) {
                                        mMap.put(curPosition, rvc);
                                        Message msg = commentHandler.obtainMessage();
                                        msg.what = 10;
                                        msg.arg1 = Integer
                                                .parseInt(addscore);
                                        commentHandler.sendMessage(msg);
                                    }else {
                                        isSendSound = false;
                                        Message msg = commentHandler.obtainMessage();
                                        msg.what = 10;
                                        msg.arg1 = 404;
                                        LogUtil.e("发送失败");
                                        commentHandler.sendMessage(msg);
                                    }
                                } catch (Exception e) {
                                    isSendSound = false;
                                    LogUtil.e("发送失败"+e);
                                    Message msg = commentHandler.obtainMessage();
                                    msg.what = 10;
                                    msg.arg1 = 404;
                                    commentHandler.sendMessage(msg);
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                    threadsend.start();
                }
            } else {
                CustomToast.showToast(mContext, "请先登录", 1000);
            }
        });
        return convertView;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            RoundProgressBar tempBar = null;
            switch (msg.what) {
                case 0:// to change the progress bar of play button with handler
                    tempBar = clickViewHolder.senPlay;//curViewHolder clickViewHolder
                    tempBar.setCricleProgressColor(0xff66a6e8);
                    if ((int) voaPlayManager.getDuration()>=0) {
                        tempBar.setMax((int) voaPlayManager.getDuration());
                    }else {
                        tempBar.setMax(0);
                    }
                    tempBar.setProgress((int) voaPlayManager.getCurTime());
                    if (voaPlayManager.isPlaying()) {
                        LogUtils.d("原文播放进度改变"+voaPlayManager.getCurTime());
                        if (msg.arg1<4){
                            notifyDataSetChanged();
                        }
                        handler.sendEmptyMessageDelayed(0, 100);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                    break;
                case 1:// reset the progress bar of play button when the playing is over
                    clickViewHolder.senPlay.setBackgroundResource(R.mipmap.sen_play);
                    tempBar = clickViewHolder.senPlay;
                    tempBar.setCricleProgressColor(0xff66a6e8);
                    tempBar.setMax(100);
                    tempBar.setProgress(0);
                    break;
                case 2:// set the read button progress bar with value of voice volume
                    tempBar = clickViewHolder.senIRead;
                    int db = msg.arg1;
                    tempBar.setCricleProgressColor(0xff87c973);
                    tempBar.setMax(100);
                    tempBar.setProgress(db);
                    break;
                case 3: // reset the read button progress bar
                    clickViewHolder.senIRead.setBackgroundResource(R.mipmap.sen_i_read);
                    tempBar = clickViewHolder.senIRead;
                    tempBar.setCricleProgressColor(0xff87c973);
                    tempBar.setMax(100);
                    tempBar.setProgress(0);
                    break;
                case 4:// 播放自己录音时的progressbar
                    LogUtils.e("recordManager是否在播放"+recordManager.isPlaying());
                    tempBar = clickViewHolder.senReadPlaying;
                    tempBar.setCricleProgressColor(0xff66a6e8);
                    tempBar.setMax(recordManager.getDuration() - 250);
                    tempBar.setProgress(recordManager.getCurrentTime());
                    if (recordManager.isPlaying()) {
                        LogUtils.d("播放进行中recordManager"+recordManager.getCurrentTime());
                        if (msg.arg1<4){
                            notifyDataSetChanged();
                        }
                        handler.sendEmptyMessageDelayed(4, 100);
                    } else {
                        clickViewHolder.senReadPlayButton.setVisibility(View.VISIBLE  );
                        handler.sendEmptyMessage(5);
                    }
                    break;
                case 5:// 重置播放录音的progressbar
                    clickViewHolder.senReadPlay.setVisibility(View.VISIBLE);
                    clickViewHolder.senReadPlaying.setVisibility(View.GONE);
                    tempBar = clickViewHolder.senReadPlaying;
                    tempBar.setCricleProgressColor(0xff66a6e8);
                    tempBar.setMax(100);
                    tempBar.setProgress(0);
                    break;
                case 6://评测成功
                    //评测成功显示纠音按钮
                    clickViewHolder.senReadCorrect.setVisibility(View.VISIBLE);
                    int score = msg.arg1;//分数
                    int index = msg.arg2;//句子？
                    evaluateResponse = (EvaluateResponse) msg.obj;

                    /**
                     * 评测的结果进行本地存储
                     */
                    AppDatabase.getInstance(mContext).getEvaluateDao().insert(new EvaluateRecord(
                            AccountManager.getInstance().userId+"",
                            chapterInfo.getVoaid(),
                            "0",
                            chapterInfo.getLevel(),
                            chapterInfo.getOrderNumber(),
                            chapterInfo.getChapterOrder(),
                            index+"",
                            chapterInfo.getTypes(),
                            evaluateResponse.getLocalMP3Path(),
                            evaluateResponse.getScores()+"",
                            evaluateResponse.getTotal_score()+"",
                            "http://iuserspeech.iyuba.cn:9001/voa/"+evaluateResponse.getURL()
                    ));
                    WordDao wordDao = AppDatabase.getInstance(mContext).getWordDao();

                    for (EvaluateResponse.Words word : evaluateResponse.getWords()) {
                        word.setUid(AccountManager.getInstance().userId+"");
                        word.setVoaId(chapterInfo.getVoaid());
                        word.setSenIndex(index+"");
                        word.setType(chapterInfo.getTypes());
                        wordDao.insert(word);
                    }

                    mMap.remove(index);
                    recordManager.initPlayer();
                    //clickDetail.isRead = true;
                    //分数//字体变色   //拼接之后的数据
                    String[] floats = new String[evaluateResponse.getWords().size()];
                    for (int i = 0; i < evaluateResponse.getWords().size(); i++) {
                        floats[i] = String.valueOf(evaluateResponse.getWords().get(i).getScore());
                    }
                    mList.get(index).setRead(true);
                    mList.get(index).setReadResult(ResultParse.getSenResultLocal(floats, evaluateResponse.getSentence()));
                    mList.get(index).setReadScore(score);//分数
                    //将评测结果放进适配器的数据中，方便纠音的时候使用
                    mList.get(index).setEvaluateResponse(evaluateResponse);

                    CustomToast.showToast(mContext, "评测成功", 1800);

                    fileName = evaluateResponse.getURL();//wav 网络地址
                    senListAdapterInteraction.getIndex(evaluateResponse.getURL(), score,  //getLocalMP3Path 替换为网络地址
                            0l, senIndex);
                    notifyDataSetChanged();
                    break;
                case 11:
                    //录音终止 进入评测
                    Log.d("录音终止操作", "显示是否执行--"+mEManager.isRecording+"----"+(msg.arg1==mEManager.position));
                    if (msg.arg1==mEManager.position&&mEManager.isRecording) {
                        ToastUtil.showToast(mContext,"评测中");
                        dismissDia();//关闭录音
                    }
                    break;
                case 12:
                    if (msg.arg1==404){
                        ToastUtil.showToast(mContext, "评测失败\n未连接到服务器，请检查网络！");
                    }else {
                        ToastUtil.showToast(mContext, "评测失败");
                    }
                    LogUtil.e("评测失败");
                    break;
                case 13:
                    Toast.makeText(mContext, "分享成功！", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    break;
                case 14:
                    clickViewHolder.senIRead.setBackgroundResource(R.mipmap.sen_i_read);//录音按钮恢复初始蓝色
                    clickViewHolder.senIRead.setProgress(0);
                    break;
                case 15:
                    if (mEManager.isRecording) {
                        dismissDia();//关闭录音
                        clickViewHolder.senIRead.setProgress(0);
                        clickViewHolder.senIRead.setBackgroundResource(R.mipmap.sen_i_read);//录音按钮恢复初始蓝色
                    }
                    break;
                case 16:
                    ToastUtil.showToast(mContext,"录音中");
                    break;
                case 104:
                    if (!mEManager.isRecording){
                        LogUtil.e("录音104,关闭");
                        return;
                    }
                    tempBar = clickViewHolder.senIRead;
                    tempBar.setMax(10000);
                    if (msg.arg1>0)
                    tempBar.setProgress(msg.arg1);
                    LogUtil.e("录音104 ，音量"+msg.arg1);
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
                    notifyDataSetChanged();
                    break;
                    default:break;
            }

        }
    };

    private void setReadScoreViewContent(int score) {
        if (score < 60) {
            clickViewHolder.senReadResult
                    .setBackgroundResource(R.drawable.sen_score_lower60);
        } else if (score > 80) {
            clickViewHolder.senReadResult.setText(score + "");
            clickViewHolder.senReadResult
                    .setBackgroundResource(R.drawable.sen_score_higher_80);
        } else {
            clickViewHolder.senReadResult.setText(score + "");
            clickViewHolder.senReadResult
                    .setBackgroundResource(R.drawable.sen_score_60_80);
        }
    }

    public void setClickPosition(int clickPosition) {
        stopAll();
        this.clickPosition = clickPosition;
        recordManager.resetPlayRecord();
    }

    public void stopAll() {
        Log.e(TAG, "run stopAll()");
        if (voaPlayManager.isPlaying() || voaPlayManager.isPausing()) {
            voaPlayManager.stopPlaySen();
        }
        if (mEManager.isRecording) {
            mEManager.cancelEvaluate(true);
            //实际上进入了评测
        }
        if (recordManager.isPlaying()) {
            recordManager.stopPlayRecord2();
        }
        if (clickViewHolder != null && clickDetail != null) {
            handler.removeMessages(0);
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessage(3);
            handler.removeMessages(4);
            handler.sendEmptyMessage(5);
        }
    }
    //关闭 录音
    public void dismissDia() {
        //soundDialog.dismiss();
//        mEManager.cancelEvaluate(true);
        mEManager.stopRecord();//关闭录音
        mEManager.mHandler.sendEmptyMessage(14);
    }

    public void releaseRecordManagerPlayer() {
        recordManager.stopAndReleasePlayer();
    }

    // TODO: 2023/1/7 暂时注释
    /*@SuppressLint("HandlerLeak")
    Handler handler_sendVoice = new Handler() {
        public void handleMessage(Message msg) {
            String jsonStr = (String) msg.obj;
            int index = msg.arg1;
            try {
                JSONObject json = new JSONObject(jsonStr);
                ReadVoiceComment rvc = new ReadVoiceComment(
                        VoaDataManager.getInstance().voaTemp, mList.get(index));
                rvc.id = json.getInt("ShuoShuoId") + "";
                rvc.shuoshuo = json.getString("FilePath");
                mMap.put(index, rvc);
                notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };*/
    @SuppressLint("HandlerLeak")
    Handler commentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    if (msg.arg1!=404) {
                        String addscore = String.valueOf(msg.arg1);
                        if (addscore.equals("5")) {
                            String mg = "语音成功发送至评论区，恭喜您获得了" + addscore + "分";
                            CustomToast.showToast(mContext, mg, 3000);
                        } else {
                            String mg = "语音成功发送至评论区";
                            CustomToast.showToast(mContext, mg, 3000);
                        }
                        notifyDataSetChanged();
                    }else {
                        CustomToast.showToast(mContext, "语音发送失败" ,1000);
                    }
                    break;
            }
        }
    };

    public static class ViewHolder {
        TextView senIndex;
        TextView senEn;
        TextView senZh;
        RoundProgressBar senPlay;
        View senReadPlayButton;
        RoundProgressBar senIRead;
        RoundProgressBar senReadPlaying;
        ImageView senReadCorrect;
        ImageView senReadPlay;
        ImageView senReadSend;
        ImageView senReadCollect;
        TextView senReadResult;
        ImageView sepLine;
        View bottomView;
    }

    public interface SenListAdapterInteraction {
        void getIndex(String fileName, int score, long time, int senIndex);

        void getPermission();
    }

    private SenListAdapterInteraction senListAdapterInteraction;

    public interface CorrectClickListener{
        void onClick(BookContentResponse.Texts texts);
    }

    private CorrectClickListener correctClickListener;

    public void setOnCorrectClickListener(CorrectClickListener correctClickListener){
        this.correctClickListener = correctClickListener;
    }

}
