package com.iyuba.camstory.manager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.service.Background;
import com.iyuba.camstory.widget.CustomToast;
import java.util.List;
/**
 * 音频的设置，这里不考虑，在播放原文音频的时候已经设置好了
 *
 * @author qiuku
 *
 */
public class VoaPlayManager {
	private static final String TAG = VoaPlayManager.class.getSimpleName();

	private static VoaPlayManager instance = new VoaPlayManager();
	private Context mContext;

	private BookContentResponse.Texts texts;

	private VoaPlayManager() {

	}
	public static VoaPlayManager getInstance(Context context) {
		instance.mContext = context;
		return instance;
	}
	private long durTime;
	private long curTime;

	public void playSen(BookContentResponse.Texts texts) {
		this.texts = texts;
		if (texts != null) {
			if (Background.vv.isPreparing()) {
				CustomToast.showToast(mContext, "播放器还未准备好，稍等哦~", 2000);
				return;
			} else {
				setSentenceDuration();
				int startPoint = (int) (Double.valueOf(texts.getBeginTiming()) * 1000-400);
				if(startPoint<=0){
					startPoint =0;
				}
				Background.vv.seekTo(startPoint);
				Background.vv.start();
				handler.sendEmptyMessageDelayed(2, 200);
				curTime = 0;
				handler.sendEmptyMessage(1);
			}
		}
	}

	private void setSentenceDuration() {
		durTime = (long) (Double.valueOf(texts.getEndTiming()) * 1000)
				- (long) (Double.valueOf(texts.getBeginTiming()) * 1000);

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (Background.vv != null) {
						Background.vv.stopPlayback2();
					}
					curTime = 0;
					break;
				case 1:
					curTime += 100;
					if (curTime < durTime) {
						handler.sendEmptyMessageDelayed(1, 100);
					} else {
						handler.sendEmptyMessage(0);
					}
					break;
				case 2:
			}
		}
	};

	public void pausePlaySen() {
		handler.removeMessages(1);
		if (Background.vv != null) {
			Background.vv.pause();
		}
	}

	public void rePlaySen() {
		if (Background.vv != null) {
			Background.vv.start();
		}
		handler.sendEmptyMessage(1);
	}

	public void stopPlaySen() {
		handler.removeMessages(1);
		handler.sendEmptyMessage(0);
	}

	public long getCurTime() {
		return curTime;
	}

	public long getDuration() {
		return durTime;
	}

	public boolean isPlaying() {
		if(Background.vv != null){
			return Background.vv.isPlaying();
		}
		return false;
	}

	public boolean isPausing() {
		return Background.vv.isPausing();
	}

	public boolean isStoppedAndCouldPlay() {
		return Background.vv.isPrepared() || Background.vv.isCompleted();
	}

	public int showBackVideoviewState() {
		if (Background.vv!=null)
		return Background.vv.getMediaplayerState();
		return -1;
	}


}
