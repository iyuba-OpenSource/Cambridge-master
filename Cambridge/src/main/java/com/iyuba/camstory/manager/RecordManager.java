package com.iyuba.camstory.manager;

import android.content.Context;
import android.media.MediaRecorder;

import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.R;
import com.iyuba.camstory.listener.OnPlayStateChangedListener;
import com.iyuba.camstory.utils.Player;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.voa.activity.setting.Constant;

import java.io.File;

/**
 * 在语音评测时，不使用录音，只是用player播放转换后的文件
 * 
 * @author qiuku
 * 
 */
public class RecordManager {
	private static final String TAG = RecordManager.class.getSimpleName();

	private static RecordManager instance = new RecordManager();
	private Context mContext;

	private RecordManager() {
	}

	public static RecordManager getInstance(Context context) {
		instance.mContext = context;
		return instance;
	}

	public void releaseContext() {
		instance.mContext = null;
	}

	private MediaRecorder mediaRecorder;
	private Player mPlayer;
	private File soundFile;
	// private int time;//录音时间
	// private int curTime = 0;

	public void startRecord() {
		try {
			soundFile = new File(Constant.getRecordAddr());
			if (mediaRecorder == null) {
				mediaRecorder = new MediaRecorder();
			} else {
				mediaRecorder.reset();
			}
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(Constant.getRecordAddr());
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// time = 0;
		// handler_read.sendEmptyMessage(1);//计时
		// handler_read.sendEmptyMessage(2);//声音大小
	}

	public void stopRecord() {
		if (soundFile != null && soundFile.exists()) {
			if (mediaRecorder != null) {
				mediaRecorder.stop();
				mediaRecorder.reset();
			}
		}
	}
	
	private OnPlayStateChangedListener opscl = new OnPlayStateChangedListener() {

		@Override
		public void playFaild() {
			CustomToast.showToast(mContext, R.string.read_play_fail, 2000);
		}

		@Override
		public void onPrepared() {
		}

		@Override
		public void playCompletion() {
		}
	};

	public void initPlayer() {
		if (mPlayer == null || mPlayer.isReleased()) {
			mPlayer = new Player(mContext, opscl);
		} else {
			mPlayer.reset();
		}
	}

	public void playRecord2(int senIndex) {
		if (mPlayer != null) {
			if (mPlayer.isIdle()){
				String filepath = Constant.getRecordAddr() + senIndex
						+ Constant.getRecordTag();
				mPlayer.initialize(filepath);
				mPlayer.prepareAndPlay();
			} else if (mPlayer.isCompleted()) {
				mPlayer.start();
			} else if (mPlayer.isInitialized()) {
				mPlayer.prepareAndPlay();
			}
		}
	}
	public void playRecord2(String localMp3Path) {
		if (mPlayer != null) {
			LogUtils.e("录音 播放器状态"+mPlayer.getPlayerState());
			LogUtils.e("录音 播放地址"+localMp3Path);
			if (mPlayer.isIdle()||mPlayer.getPlayerState()==6){
				String filepath = localMp3Path;
				mPlayer.initialize(filepath);
				mPlayer.prepareAndPlay();
			} else if (mPlayer.isCompleted()) {
				mPlayer.start();
			} else if (mPlayer.isInitialized()) {
				mPlayer.prepareAndPlay();
			}
		}
	}

	public void playRecord(int senIndex) {
		if (new File(Constant.getRecordAddr() + senIndex + Constant.getRecordTag())
				.exists() && mPlayer != null) {
			mPlayer.playUrl(Constant.getRecordAddr() + senIndex
					+ Constant.getRecordTag());
		} else {
			return;
		}
	}
	
	public void resetPlayRecord(){
		if(mPlayer != null){
			mPlayer.reset();
		}
	}

	public void stopAndReleasePlayer() {
		if (mPlayer != null && mPlayer.isAlreadyGetPrepared()) {
			mPlayer.stopAndRelease();
		}
	}
	
	public void stopPlayRecord2(){
		if(mPlayer != null && mPlayer.isInPlayingBackState()){
			mPlayer.stopPlay();
		}
	}

	public void pausePlayRecord() {
		if (mPlayer != null && mPlayer.isInPlayingBackState()) {
			mPlayer.pause();
		}
	}

	public void rePlayRecord() {
		if (mPlayer != null && mPlayer.isPausing()) {
			mPlayer.restart();
		}
	}

	/**
	 * 更新话筒状态 (不用，用讯飞提供的)
	 * 
	 */
	private int BASE = 1;

	public double getMicStatus() {
		double db = 0;// 分贝
		if (mediaRecorder != null) {
			double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
			if (ratio > 1) {
				db = 20 * Math.log10(ratio);
			}
		}
		return db;
	}

	public int getCurrentTime() {
		return mPlayer.getCurrentTime();
	}

	public int getDuration() {
		return mPlayer.getDuration();
	}

	public boolean isPlaying() {
		if (mPlayer != null) {
			return mPlayer.isPlaying();
		}
		return false;
	}

	public boolean isPausing() {
		if (mPlayer != null) {
			return mPlayer.isPausing();
		}
		return false;
	}

	public boolean isStoppedAndCouldPlay() {
		if (mPlayer != null) {
			return mPlayer.isIdle() || mPlayer.isCompleted()
					|| mPlayer.isInitialized();
		}
		return false;
	}
	
	public int showPlayerState(){
		return mPlayer.getPlayerState();
	}
}
