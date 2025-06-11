package com.iyuba.camstory.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.facebook.stetho.common.LogUtil;
import com.iyuba.camstory.bean.EvaluatBean;
import com.iyuba.camstory.bean.EvaluateResponse;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.common.WebConstant;
import com.iyuba.module.toolbox.GsonUtils;
import com.iyuba.voa.activity.setting.Constant;

import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.apache.commons.cli.TypeHandler.createFile;

/**
 * 新的评测管理，需要自己录音
 * 12.21  ZH
 */
public class ReadEvaluateManager {
    private Context mContext;
    private static ReadEvaluateManager instance;
    private AudioRecord mAudioRecord;
    private int mRecorderBufferSize;
    private byte[] mAudioData;

    /*默认数据*/
    private int mSampleRateInHZ = 8000; //采样率
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;  //位数
    private int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;   //声道


    public boolean isRecording = false; //是否录音中，默认否
    private String mTmpFileAbs = "";
    private String mWavPath = "";
    public String mMp3Path;
    private Map<String, String> maps;
    public Handler mHandler;

    private String endTime;
    private long startDate, endDate;

    //private Result resultEva; //没用
    private String sentence;
    private int senIndex;
    private String resultStr;
    public int position;

    private MediaRecorder mediaRecorder;

    private String recordPath;


    private ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    private ReadEvaluateManager(Context context) {
        this.mContext = context;
    }

    public static ReadEvaluateManager getInstance(Context context) {
        if (instance == null) {
            instance = new ReadEvaluateManager(context);
        }
        return instance;
    }

    private void initRecord() {
        mRecorderBufferSize = AudioRecord.getMinBufferSize(mSampleRateInHZ, mChannelConfig, mAudioFormat);
        mAudioData = new byte[320];
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, mSampleRateInHZ,
                mChannelConfig, mAudioFormat, mRecorderBufferSize);
    }

    /**
     * 本来是录音保存为wav然后转mp3，因为AndroidAudioConverter导入不了，所以只能修改以前的录音方法
     *
     * @param clickPosition
     * @param sen
     * @param senIndex
     * @param params
     * @param handler
     */
    public void startRecord(int clickPosition, String sen, int senIndex, Map<String, String> params, final Handler handler) throws ParseException {
        sentence = sen;
        this.senIndex = clickPosition;//senIndex
        maps = params;
        mHandler = handler;
        position = clickPosition;
        isRecording = true;//评测为开启状态
        startDate = new Date().getTime();//开始讲话
        checkPermission();
        if (isRecording) {
            LogUtil.e("录音 已开始");
        }
        recordPath = Constant.getRecordAddr() + senIndex + clickPosition;
        try {

            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            } else {
                mediaRecorder.reset();
            }
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(recordPath);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        if (!isRecording) {
            LogUtil.e("已结束");
            return;
        }
        endDate = new Date().getTime();
        //结束讲话
        isRecording = false;
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
            mediaRecorder.reset();
        }
        String path = WebConstant.HTTP_SPEECH_ALL + "/test/ai/";
        try {
            post(path, maps, recordPath, mHandler);
            LogUtils.i("录音 地址mp3" + recordPath);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("评测请求失败" + e);
        }
    }

    //开始评测
    public void startEvaluate(int clickPosition, String sen, int senIndex, Map<String, String> params, final Handler handler) throws ParseException {
        sentence = sen;
        this.senIndex = clickPosition;//senIndex
        maps = params;
        mHandler = handler;
        position = clickPosition;
        isRecording = true;//评测为开启状态
        startDate = new Date().getTime();//开始讲话
        initRecord();
        checkPermission();
        if (isRecording) {
            LogUtil.e("录音 已开始");
        }
        //拼接的录音地址
        String tmpName = Constant.getRecordAddr() + senIndex + clickPosition;
        final File tmpFile = createFile(tmpName + ".pcm");
        final File tmpOutFile = createFile(tmpName + ".wav");
        mTmpFileAbs = tmpFile.getAbsolutePath();
        mWavPath = tmpOutFile.getAbsolutePath();
        LogUtil.e("录音 pcm文件地址：" + mTmpFileAbs);
        LogUtil.e("录音 wav文件地址：" + mWavPath);
        //mLogTv.setText(tmpFile.getAbsolutePath());
        //控件开始录音
        mAudioRecord.startRecording();
        mExecutor.execute(() -> {
            try {
                FileOutputStream outputStream = new FileOutputStream(tmpFile.getAbsoluteFile());

                while (isRecording) {
                    int readSize = 0;
                    int r = 0;
                    if (mAudioRecord != null) {
                        r = mAudioRecord.read(mAudioData, 0, mAudioData.length);
                    } else {
                        return;
                    }
                    //从内存获取数据
                    outputStream.write(mAudioData);
                    int v = getVolume(r, mAudioData);

                    Message msg = mHandler.obtainMessage();
                    msg.what = 104;
                    msg.arg1 = v;
                    mHandler.sendMessage(msg);
                    LogUtils.i("run: ------>" + readSize + "分贝" + r);
                }
                outputStream.close();
                pcmToWave(tmpFile.getAbsolutePath(), tmpOutFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopEvaluate() {
        if (!isRecording) {
            LogUtil.e("已结束");
            return;
        }
        endDate = new Date().getTime();
        //结束讲话
        isRecording = false;
        if (mAudioRecord != null)
            mAudioRecord.stop();
    }

    public void stopEvaluating() {
        if (isRecording) {
            stopEvaluate();
        }
    }

    //评测取消
    public void cancelEvaluate(boolean cancel) {
        //mSpeechEvaluator.cancel();
        stopEvaluate();
    }

    public String getEndTime() {
        return endTime;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void post(String actionUrl, Map<String, String> params, final String filePath, final Handler handler) {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().
                connectTimeout(15, TimeUnit.SECONDS).
                readTimeout(15, TimeUnit.SECONDS).
                writeTimeout(15, TimeUnit.SECONDS)
                .build();
        //一：文本类的
        MultipartBody.Builder urlBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    urlBuilder.addFormDataPart(key, params.get(key));
                }
            }
        }
        //二种：文件请求体
        MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
        File file1 = new File(filePath);
        RequestBody fileBody = RequestBody.create(type, file1);
        urlBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + filePath + "\""), fileBody);

        // 构造Request->call->执行
        final Request request = new Request.Builder().headers(new Headers.Builder().build())//extraHeaders 是用户添加头
                .url(actionUrl).post(urlBuilder.build())//参数放在body体里
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("录音评测失败连接" + e);//每次出现
                Message msg = handler.obtainMessage();
                msg.what = 12;
                msg.arg1 = 404;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();

                if (response.isSuccessful()) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                    endTime = df.format(new Date());// new Date()为获取当前系统时间

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(resultStr);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String result = jsonObject.getString("result");
                        if (data != null && result.equals("1")) {
                            deletePCM();//删除PCM
                            resultStr = data.toString();
                            EvaluateResponse evaluateResponse = GsonUtils.toObject(resultStr,EvaluateResponse.class);
                            evaluateResponse.setPosition(position);
                            evaluateResponse.setLocalMP3Path(filePath);
                            Message msg = handler.obtainMessage();
                            msg.what = 6;
                            msg.arg1 = (int) (Double.valueOf(evaluateResponse.getTotal_score()) * 20);//分数
                            msg.arg2 = senIndex;
                            //msg.obj = data.toString();
                            msg.obj = evaluateResponse;

                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(12);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(12);
                    }

                } else {
                    handler.sendEmptyMessage(12);
                }

            }
        });

    }

    private void deletePCM() {
        String filePath = Constant.getEnvir();
        File file = new File(filePath);

        if (file.isDirectory()) {//是文件夹 目录
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                String fPath = f.getPath();
                LogUtils.e("文件后缀名" + fPath + " 文件" + fPath.substring(fPath.length() - 4));
                if ((fPath.substring(fPath.length() - 4)).equals(".pcm") || (fPath.substring(fPath.length() - 4)).equals(".wav")) {
                    //deleteFile(f);
                    f.delete();
                }
            }
            //file.delete();//如要保留文件夹，只删除文件，请注释这行
        }
    }

    private void pcmToWave(String inFileName, String outFileName) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long longSampleRate = mSampleRateInHZ;
        long totalDataLen = totalAudioLen + 36;
        int channels = 1;//你录制是单声道就是1 双声道就是2（如果错了声音可能会急促等）
        long byteRate = 16 * longSampleRate * channels / 8;

        byte[] data = new byte[mRecorderBufferSize];
        try {
            in = new FileInputStream(inFileName);
            out = new FileOutputStream(outFileName);

            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            writeWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取录音的声音分贝值
     *
     * @return
     */
    private int getVolume(int r, byte[] bytes_pkg) {
        //way 1
        int v = 0;
//      将 buffer 内容取出，进行平方和运算
        for (byte aBytes_pkg : bytes_pkg) {
            // 这里没有做运算的优化，为了更加清晰的展示代码
            v += aBytes_pkg * aBytes_pkg;
        }
//      平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
        int volume = (int) (v / (float) r);
        return volume;
    }

    private boolean checkPermission() {
        if (!isHasRecordPermission()) {
            requestRecordPermission();
            return false;
        }
        return true;
    }

    private boolean isHasRecordPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestRecordPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isHasRecordPermission(mContext)) {
                ActivityCompat.requestPermissions((Activity) mContext, PermissionManager.PERMISSION_RECORD, PermissionManager.REQUEST_RECORD);
            }
        }
    }

    private boolean isHasRecordPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    /*
   任何一种文件在头部添加相应的头文件才能够确定的表示这种文件的格式，wave是RIFF文件结构，每一部分为一个chunk，其中有RIFF WAVE chunk，
   FMT Chunk，Fact chunk,Data chunk,其中Fact chunk是可以选择的，
    */
    private void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                     int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (1 * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

//    private void convertAudio(File file) {
//        // File wavFile = new File(Environment.getExternalStorageDirectory(), path);
//        IConvertCallback callback = new IConvertCallback() {
//            @Override
//            public void onSuccess(File convertedFile) {
//                mMp3Path = convertedFile.getPath();
//                String path = WebConstant.HTTP_SPEECH_ALL +"/test/eval/";
//                //String path = "https://speech." + IYBHttpHead + "/test/eval/";//爱语吧评测接口请求头
//                try {
//                    post(path, maps, convertedFile.getPath(), mHandler);
//                    LogUtils.i("录音 地址mp3" + convertedFile.getPath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LogUtil.i("评测请求失败" + e);
//                }
//                //停止录制
//                try {
//                    // 防止某些手机崩溃，例如联想
//                    mAudioRecord.stop();
//                    // 彻底释放资源
//                    mAudioRecord.release();
//                    mAudioRecord = null;
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Exception error) {
////                LogUtil.e("录音 ERROR: " + error.getMessage());
//                Log.e("TAG", "录音 ERROR: "+error.getMessage() );
//
//            }
//        };

    /**
     * 原本是利用AndroidAudioConverter将录音文件转换位mp3
     */
//        AndroidAudioConverter.with(mContext)
//                .setFile(file)
//                .setFormat(cafe.adriel.androidaudioconverter.model.AudioFormat.MP3)
//                .setCallback(callback)
//                .convert();
//    }
}
