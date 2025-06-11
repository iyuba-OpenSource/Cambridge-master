package com.iyuba.camstory.manager;



import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.iyuba.camstory.utils.FileCopyUtil;
import com.iyuba.camstory.utils.ResultParse;
import com.iyuba.camstory.utils.WavWriter;
import com.iyuba.voa.activity.setting.Constant;

import java.io.File;
import java.io.IOException;

import result.Result;
import result.xml.XmlResultParser;


//老的评测管理

public class IseManager {
	private static final String TAG = IseManager.class.getSimpleName();

	private static IseManager instance;
	private static Handler handler;

//	private SpeechEvaluator mSpeechEvaluator;

	private String LANGUAGE = "en_us";
	private String ISE_CATEGORY = "read_sentence";
	private String RESULT_LEVEL = "complete";
	private String VAD_BOS = "5000";
	private String VAD_EOS = "1800";
	private String KEY_SPEECH_TIMEOUT = "-1";

	private String resultStr;
	private Result resultEva;

	private String pcmFileName;

	private long time;// 录音时间
	private int senIndex;
	private String sentence;

	public IseManager(Context context) {
//		mSpeechEvaluator = SpeechEvaluator.createEvaluator(context, null);
	}

	public static IseManager getInstance(Context context, Handler h) {
		if (instance == null) {
			instance = new IseManager(context);
		}
		handler = h;
		return instance;
	}

	private void setParams(int senIndex) {
//		pcmFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
//				+ "/msc/" + LANGUAGE + "_" + ISE_CATEGORY + "_"
//				+ System.currentTimeMillis() / 1000 + ".pcm";
		// 同一句录音文件名相同
		pcmFileName = Constant.getRecordAddr() + senIndex + ".pcm";

//		mSpeechEvaluator.setParameter(SpeechConstant.LANGUAGE, LANGUAGE);
//		mSpeechEvaluator.setParameter(SpeechConstant.ISE_CATEGORY, ISE_CATEGORY);
//		mSpeechEvaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
//		mSpeechEvaluator.setParameter(SpeechConstant.SAMPLE_RATE, 16000 + "");
//		mSpeechEvaluator.setParameter(SpeechConstant.VAD_BOS, VAD_BOS);
//		mSpeechEvaluator.setParameter(SpeechConstant.VAD_EOS, VAD_EOS);
//		mSpeechEvaluator.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT,
//				KEY_SPEECH_TIMEOUT);
//		mSpeechEvaluator.setParameter(SpeechConstant.RESULT_LEVEL, RESULT_LEVEL);
//		mSpeechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, pcmFileName);
	}

	// 评测监听接口
//	private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {
//
//		@Override
//		public void onResult(EvaluatorResult result, boolean isLast) {
//			Log.e(TAG, "evaluator result is last? : " + isLast);
//
//			if (isLast) {
//				StringBuilder builder = new StringBuilder();
//				builder.append(result.getResultString());
//				resultStr = builder.toString();
//				resultParse();
//
//				Message msg = handler.obtainMessage();
//				msg.what = 6;
//				msg.arg1 = (int) (resultEva.total_score * 20);
//				msg.arg2 = senIndex;
//				msg.obj = resultEva.is_rejected;
//				Log.e(TAG, "evaluating result : " + msg.arg1);
//				handler.sendMessage(msg);
//				transformPcmToAmr();
////				Pcm2AmrUtil.transform(pcmFileName, Constant.getRecordAddr() + senIndex
////						+ Constant.getRecordTag());
//			}
//		}
//
//		@Override
//		public void onError(SpeechError error) {
//			if (error != null) {
//				// showTip("error:" + error.getErrorCode() + ","
//				// + error.getErrorDescription());
//			} else {
//				Log.d(TAG, "evaluator over");
//			}
//		}
//		@Override
//		public void onBeginOfSpeech() {
//			Log.d(TAG, "evaluator begin");
//		}
//
//		@Override
//		public void onEndOfSpeech() {
//			handler.sendEmptyMessage(3);
//			handlerRead.removeMessages(1);
//			Log.d(TAG, "evaluator stoped");
//		}
//
//		@Override
//		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//			// TODO Auto-generated method stub
//		}
//
//		@Override
//		public void onVolumeChanged(int volume, byte[] arg1) {
//			noticeVolume(volume);
//		}
//
//	};

	public void noticeVolume(int volume) {
		Message msg = handler.obtainMessage();
		msg.what = 2;
		msg.arg1 = volume;
		handler.sendMessage(msg);
	}

	public void startEvaluate(String sen, int senIndex) {
//		if (mSpeechEvaluator != null) {
//			setParams(senIndex);
//			mSpeechEvaluator.startEvaluating(sen, null, mEvaluatorListener);
//			this.senIndex = senIndex;
//			this.sentence = sen;
//			time = 0;
//			handlerRead.sendEmptyMessage(1);
//		}
	}

//	public boolean isEvaluating() {
//		return mSpeechEvaluator.isEvaluating();
//	}

	public void stopEvaluating() {
//		if (mSpeechEvaluator.isEvaluating()) {
//			mSpeechEvaluator.stopEvaluating();
//			handlerRead.removeMessages(1);
//		}
	}

	public void cancelEvaluate(boolean cancel) {
//		mSpeechEvaluator.cancel();
		handlerRead.removeMessages(1);
	}

	// private void showTip(String str) {
	// if (!TextUtils.isEmpty(str)) {
	// mToast.setText(str);
	// mToast.show();
	// }
	// }

	public void resultParse() {
		if (!TextUtils.isEmpty(resultStr)) {
			XmlResultParser resultParser = new XmlResultParser();
			resultEva = resultParser.parse(resultStr.toString());
		}
	}

	public String getResult() {
		return resultStr;
	}

	private Handler handlerRead = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				time += 100;
				handlerRead.sendEmptyMessageDelayed(1, 100);
				break;
			}
		}
	};

	public long getTime() {
		return time;
	}

	/**
	 * 本来是将.pcm转换成.wav文件，但发现改了后缀名也能播放
	 */
	public void transformPcmToAmr() {
		File amrFile = new File(Constant.getRecordAddr() + senIndex
				+ Constant.getRecordTag());
		File pcmFile = new File(pcmFileName);
		FileCopyUtil.fileChannelCopy(pcmFile, amrFile);
		WavWriter myWavWriter = null;
		try {
			myWavWriter = new WavWriter(amrFile, 16000);
			myWavWriter.writeHeader();
			myWavWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		RecordUtil ru = new RecordUtil();
//		ru.pcm2mp3(pcmFileName, Constant.getRecordAddr() + senIndex
//				+ ".mp3");
		// pcmFile.delete();
	}

	public void transformPcmToAmr2(File pcmFile) {
		File amrFile = new File(Constant.getRecordAddr() + "mix"
				+ Constant.getRecordTag());
		FileCopyUtil.fileChannelCopy(pcmFile, amrFile);
		WavWriter myWavWriter = null;
		try {
			myWavWriter = new WavWriter(amrFile, 16000);
			myWavWriter.writeHeader();
			myWavWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		RecordUtil ru = new RecordUtil();
//		ru.pcm2mp3(pcmFileName, Constant.getRecordAddr() + senIndex
//				+ ".mp3");
		// pcmFile.delete();
	}
	//  获取录音文件路径
	public String getFileName(){
		return pcmFileName;
	}
	
	/*
	public void transformPcmToAmrwb() {
		File wavFile = new File(pcmFileName.replace(".pcm", ".wav"));
		File pcmFile = new File(pcmFileName);
		FileCopyUtil.fileChannelCopy(pcmFile, wavFile);
		WavWriter myWavWriter = null;
		try {
			myWavWriter = new WavWriter(wavFile, 16000);
			myWavWriter.writeHeader();
			myWavWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pcmFile.delete();
		wav2amrwb(wavFile.getName());
	}
	
	private void wav2amrwb(String wavFileName){
		File amrFile = new File(Constant.getRecordAddr() + senIndex
				+ Constant.getRecordTag());
		File wavFile = new File(wavFileName);
		if(amrFile.exists())
			amrFile.delete();
		Encoder encoder = new Encoder();
		AudioAttributes audioAttr = new AudioAttributes();
		audioAttr.setCodec("libamr_wb");
		audioAttr.setBitRate(new Integer(8850));
		EncodingAttributes enAttrs = new EncodingAttributes();
		enAttrs.setFormat("amr");
		enAttrs.setAudioAttributes(audioAttr);
		try {
			encoder.encode(wavFile, amrFile, enAttrs);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InputFormatException e1) {
			e1.printStackTrace();
		} catch (EncoderException e1) {
			e1.printStackTrace();
		} finally{
			wavFile.delete();
		}
	}
	*/

	/*
	public void transformPcmToAmr2() {
		File pcmFile = new File(pcmFileName);
		try {
			WavWriter writer = new WavWriter(pcmFile, 16000);
			writer.writeHeader();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wav2amr(pcmFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void wav2amr(String wavFileName) throws IOException {
		InputStream inStream;
		inStream = new FileInputStream(wavFileName);
		AmrInputStream aStream = new AmrInputStream(inStream);

		File amrfile = new File(Constant.getRecordAddr() + senIndex
				+ Constant.getRecordTag());
		if (amrfile.exists()) {
			amrfile.delete();
		}
		amrfile.createNewFile();
		OutputStream out = new FileOutputStream(amrfile);

		byte[] x = new byte[1024];
		int len;
		out.write(0x23);
		out.write(0x21);
		out.write(0x41);
		out.write(0x4D);
		out.write(0x52);
		out.write(0x0A);
		while ((len = aStream.read(x)) > 0) {
			out.write(x, 0, len);
		}

		out.close();
		aStream.close();
	}*/

	public SpannableStringBuilder getSenStyle() {
		Log.i(TAG, String.valueOf(resultEva.is_rejected));
		Log.i(TAG, resultEva.toString());
		return ResultParse.getSenResult(resultEva, sentence);
	}
}
