package com.iyuba.camstory.popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.provider.UserDictionary;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.API.EvaluateApi;
import com.iyuba.camstory.API.data.SendEvaluateResponse;
import com.iyuba.camstory.API.data.WordCorrectResponse;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.EvaluatBean;
import com.iyuba.camstory.bean.EvaluateResponse;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.view.SelectWordTextView;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CorrectPopup extends BasePopupWindow {

    private SelectWordTextView tv_correct_sen;
    private TextView tv_correct_word;
    private ImageView correct_close;
    private ImageView correct_play_word;
    private TextView user_pron;

    private TextView correct_explain;
    private ImageView correct_play_ori;
    /**
     * 评测单词
     */
    private ImageView correct_rec;
    private ImageView correct_play_user;
    private TextView right_pron;

    private LinearLayout correct_ll_audio;
    /**
     * 用户测评的原音和分数
     */
    private LinearLayout correct_score_ly;

    /**
     * 用户测评分数
     */
    private TextView correct_score;

    private ImageView goto_micro;

    private MediaPlayer player;

    private MediaRecorder mediaRecorder;

    private SpannableString spannableString;

    /**
     * 录制音频的存储路径
     */
    private String saveFileStr;

    /**
     * 是否录音
     */
    private boolean isRecorder = false;

    /**
     * audio目录
     */
    private final String audioDirPath;

    //录音动画
    private Drawable recDrawable;

    //与分贝有关
    private int BASE = 1;

    /**
     * 单词信息，首次弹窗为第一个单词，之后以点击为准
     */
    private WordCorrectResponse wordCorrectResponse;
    private String examTime;
    private SendEvaluateResponse sendEvaluateResponse;
    private BookContentResponse.Texts texts;
    private EvaluateResponse evaluateResponse;

    /**
     * 构造方法
     *
     * @param context
     */
    public CorrectPopup(Context context, BookContentResponse.Texts texts) {
        super(context);
        this.texts = texts;
        this.evaluateResponse = texts.getEvaluateResponse();
        View view = createPopupById(R.layout.correct_dialog);
        setContentView(view);
        audioDirPath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        player = new MediaPlayer();
        mediaRecorder = new MediaRecorder();
        initView(view);
        tv_correct_sen.setText(texts.getReadResult());
        initOperation();
        initWord();
    }

    private void initWord(){
        boolean t = false;
        for (EvaluateResponse.Words wordsBean : evaluateResponse.getWords()) {
            if(Double.valueOf(wordsBean.getScore())<2.5){
                initWord(wordsBean);
                t = true;
                break;
            }
        }
        if (evaluateResponse.getWords().size() > 0 && !t) {
            EvaluateResponse.Words evalWord = evaluateResponse.getWords().get(0);
            initWord(evalWord);
        }
    }

    /**
     * 绑定控件
     *
     * @param view
     */
    private void initView(View view) {
        tv_correct_word = view.findViewById(R.id.tv_correct_word);
        correct_close = view.findViewById(R.id.correct_close);
        tv_correct_sen = view.findViewById(R.id.tv_correct_sen);
        correct_play_word = view.findViewById(R.id.correct_play_word);
        user_pron = view.findViewById(R.id.user_pron);
        correct_explain = view.findViewById(R.id.correct_explain);
        correct_play_ori = view.findViewById(R.id.correct_play_ori);
        correct_rec = view.findViewById(R.id.correct_rec);
        correct_play_user = view.findViewById(R.id.correct_play_user);
        right_pron = view.findViewById(R.id.right_pron);
        correct_ll_audio = view.findViewById(R.id.correct_ll_audio);
        correct_score_ly = view.findViewById(R.id.correct_score_ly);
        correct_score = view.findViewById(R.id.correct_score);
        goto_micro = view.findViewById(R.id.goto_micro);
    }

    /**
     * 初始化点击事件
     */
    private void initOperation() {
        correct_close.setOnClickListener(v -> dismiss());
        //播放
        correct_play_word.setOnClickListener(v -> playAudio());
        player.setOnPreparedListener(mp -> player.start());
        player.setOnCompletionListener(mp -> {

        });

        correct_play_ori.setOnClickListener(v -> playAudio());
        //评测单词
        correct_rec.setOnClickListener(v -> {
            //需要判断权限
            if (isRecorder) {//
                mediaRecorder.stop();
                isRecorder = false;
                handler.removeMessages(1);
                //动画
                if (recDrawable != null) {
                    recDrawable.setLevel(0);
                }
                // TODO: 2022/6/29 上传新的音频
                eval();

            } else {//开启录音
                try {
                    mediaRecorder.reset();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mediaRecorder.setAudioSamplingRate(44100);
                    mediaRecorder.setAudioEncodingBitRate(128000);
                    mediaRecorder.setAudioChannels(2);
                    saveFileStr = audioDirPath + File.separator + System.currentTimeMillis() + ".3gp";
                    mediaRecorder.setOutputFile(saveFileStr);
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    isRecorder = true;
                    //设置动画和分贝
                    recDrawable = correct_rec.getDrawable();
                    updateMicStatus();
                    Toast.makeText(CrashApplication.getInstance(), "开始录音，再次点击结束录音并打分", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        correct_play_user.setOnClickListener(v -> {
            try {
                player.reset();
                player.setDataSource("http://iuserspeech.iyuba.cn:9001" + "/voa/" + sendEvaluateResponse.getData().getURL());
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        goto_micro.setOnClickListener(v -> {

            ArrayList<Integer> integerList = new ArrayList<>();
            integerList.add(3);
            Intent intent = MobClassActivity.buildIntent(CrashApplication.getInstance(), 3, true, integerList);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CrashApplication.getInstance().startActivity(intent);
        });
        tv_correct_sen.setOnClickWordListener(word -> {
            tv_correct_word.setText(word);
            //隐藏用户测评原音按钮
            correct_score_ly.setVisibility(View.INVISIBLE);
            correct_score_ly.setClickable(false);
            //查找此单词
            getWordInfo(word);
        });
    }


    /**
     * 处理EvalWord，去除标点符号
     *
     * @return
     */
    private String removeSymbol(String content) {

        StringBuilder stringBuilder = new StringBuilder();
        for (char c : content.toCharArray()) {//去掉非英文字符

            if (Character.isLetterOrDigit(c)) {

                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 播放单词音频
     * 小喇叭大喇叭共用
     */
    private void playAudio() {

        if (wordCorrectResponse != null) {//存在则直接播放

            try {
                if(wordCorrectResponse.getAudio() != null){
                    player.reset();
                    player.setDataSource(wordCorrectResponse.getAudio());
                    player.prepareAsync();
                }else{
                    ToastUtil.showToast(CrashApplication.getInstance(),"单词音频不存在，请播放其他单词");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 计算分贝
     */
    private void updateMicStatus() {

        if (mediaRecorder != null) {
            double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                //设置动画
                recDrawable.setLevel((int) db * 75);
            }
            handler.sendEmptyMessageDelayed(1, 200);
        }
    }

    private final Handler handler = new Handler(msg -> {

        if (isRecorder) {

            updateMicStatus();
        }
        return false;
    });

    private EvaluateResponse.Words getWordInfo(String word){
        EvaluateResponse.Words t = null;
        if (evaluateResponse != null) {

            for (int i = 0; i < evaluateResponse.getWords().size(); i++) {
                EvaluateResponse.Words evalWord = evaluateResponse.getWords().get(i);
                char[] chars = evalWord.getContent().toCharArray();
                StringBuilder stringBuilder = new StringBuilder();
                for (char c : chars) {//去掉非英文字符
                    if (Character.isLetterOrDigit(c)) {
                        stringBuilder.append(c);
                    }
                }
                String dealWord = stringBuilder.toString();
                if (word.equals(dealWord)) {
                    t = evalWord;
                    break;
                }
            }
            //获取此单词的信息
            if (t != null) {
                right_pron.setText("正确发音:[" + t.getPron2() + "]");
                user_pron.setText("你的发音:[" + t.getUser_pron2() + "]");
                soundCorrect(word, t.getUser_pron2(), t.getPron2());
            }
        }
        return t;
    }

    /**
     * 请求纠音接口获取单词原型及释义
     */
    public void soundCorrect(String q, String user_pron, String ori_pron) {
        Call<WordCorrectResponse> call = null;
        try {
            call = RequestFactory.getSoundCorrectApi().apiWordAi(q, URLEncoder.encode(user_pron, "utf-8"),
                    URLEncoder.encode(ori_pron, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        call.enqueue(new Callback<WordCorrectResponse>() {
            @Override
            public void onResponse(Call<WordCorrectResponse> call, Response<WordCorrectResponse> response) {
                wordCorrectResponse =  response.body();
                if(wordCorrectResponse == null){
                    return;
                }
                //判断是否要隐藏喇叭
                if (wordCorrectResponse.getAudio() == null || wordCorrectResponse.getAudio().equals("")) {
                    correct_ll_audio.setVisibility(View.INVISIBLE);
                    correct_ll_audio.setClickable(false);
                } else {
                    correct_ll_audio.setVisibility(View.VISIBLE);
                    correct_ll_audio.setClickable(true);
                }
                Log.e("TAG", "onResponse: "+response.body().getKey());
                if(response.body().getKey() != null){
                    tv_correct_word.setText(response.body().getKey());
                }
                if(response.body().getDef()!= null){
                    correct_explain.setText("单词释义:"+response.body().getDef());
                }else{
                    correct_explain.setText("单词释义: 暂无");
                }
            }

            @Override
            public void onFailure(Call<WordCorrectResponse> call, Throwable t) {
                ToastUtil.showToast(CrashApplication.getInstance(),"请求失败");
            }
        });
    }

    private void initWord(EvaluateResponse.Words wordsBean){
        Log.e("TAG", "initWord: "+wordsBean.getContent());
        //标题单词
        if(wordsBean.getContent() != null){
            tv_correct_word.setText(wordsBean.getContent());
        }
        //正确发音
        right_pron.setText("正确发音：[" + wordsBean.getPron2() + "]");
        //用户发音
        user_pron.setText("用户发音：[" + wordsBean.getUser_pron2() + "]");
        soundCorrect(wordsBean.getContent(), wordsBean.getUser_pron2(), wordsBean.getPron2());
    }

    /**
     * 请求评测接口评测单词
     */
    public void eval(){
        /**
         * 获取单词index
         */
        int index = -1;
        List<EvaluateResponse.Words> evalWordList = evaluateResponse.getWords();
        for (int i = 0; i < evalWordList.size(); i++) {
            EvaluateResponse.Words evalWord = evalWordList.get(i);
            if (removeSymbol(evalWord.getContent()).equals(wordCorrectResponse.getKey())) {
                index = Integer.valueOf(evalWord.getIndex());
                break;
            }
        }
        Map<String, RequestBody> map = new HashMap<>(6);
        map.put(EvaluateApi.GetVoa.Param.Key.SENTENCE, RequestFactory.fromString(wordCorrectResponse.getKey().replaceAll("\\+", "%20")));
        map.put(EvaluateApi.GetVoa.Param.Key.IDINDEX, RequestFactory.fromString(index+""));
        map.put(EvaluateApi.GetVoa.Param.Key.NEWSID, RequestFactory.fromString(texts.getVoaid()+""));
        map.put(EvaluateApi.GetVoa.Param.Key.PARAID, RequestFactory.fromString(texts.getParaid()));
        map.put(EvaluateApi.GetVoa.Param.Key.TYPE, RequestFactory.fromString(com.iyuba.voa.activity.setting.Constant.TOPICID));
        map.put(EvaluateApi.GetVoa.Param.Key.USERID, RequestFactory.fromString(AccountManager.getInstance().userId+""));
        map.put(EvaluateApi.GetVoa.Param.Key.APPID, RequestFactory.fromString(com.iyuba.voa.activity.setting.Constant.getAppid()+""));
        map.put(EvaluateApi.GetVoa.Param.Key.FLG, RequestFactory.fromString("2"));
        map.put(EvaluateApi.GetVoa.Param.Key.WORDID, RequestFactory.fromString(index+""));
        Call<SendEvaluateResponse> call = RequestFactory.getEvaluateApi().sendVoice(map, RequestFactory.fromFile(new File(saveFileStr)));
        call.enqueue(new Callback<SendEvaluateResponse>() {
            @Override
            public void onResponse(Call<SendEvaluateResponse> call, final Response<SendEvaluateResponse> response) {
                if (response.body().getResult().equals("1")) {
                    correct_score_ly.setVisibility(View.VISIBLE);
                    sendEvaluateResponse = response.body();
                    int score = (int)(Double.valueOf(response.body().getData().getTotal_score())*20);
                    correct_score.setText(score + "");
                } else {
                    ToastUtil.showToast(CrashApplication.getInstance(),"评测失败");
                }
            }

            @Override
            public void onFailure(Call<SendEvaluateResponse> call, Throwable t) {
                ToastUtil.showToast(CrashApplication.getInstance(),"评测失败");
            }
        });
    }

}
