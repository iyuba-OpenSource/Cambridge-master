package com.iyuba.camstory.widget;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;

import com.iyuba.camstory.R;
import com.iyuba.camstory.manager.VoaStudyManager;

/**
 * 视频框架
 * 
 * @author chentong
 * 
 * 
 *         Displays a video file. The VideoView class can load images from
 *         various sources (such as resources or content providers), takes care
 *         of computing its measurement from the video so that it can be used in
 *         any layout manager, and provides various display options such as
 *         scaling and tinting.
 */
public class VideoView implements MediaPlayerControl {
	private static final String TAG = VideoView.class.getSimpleName();

	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;

	private Context mContext;
	// settable by the client
	private Uri mUri;
	private int mDuration;
	// All the stuff we need for playing and showing a video
	private SurfaceHolder mSurfaceHolder = null;
	public MediaPlayer mMediaPlayer = null;
	private boolean mIsPrepared;
	private MediaController mMediaController;
	private OnCompletionListener mOnCompletionListener;
	private OnPreparedListener mOnPreparedListener;
	private OnErrorListener mOnErrorListener;
	private int mCurrentBufferPercentage;
	private int mMediaplayerState;
	private final ExecutorService executorService;

	public VideoView(Context context) {
		mContext = context;
		executorService = Executors.newFixedThreadPool(3);
	}

	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
		Log.e(TAG, "media url : " + path);
	}

	public void setVideoPath(String path, SurfaceHolder surfaceHolder) {
		mSurfaceHolder = surfaceHolder;
		VoaStudyManager.newRecord();
		setVideoURI(Uri.parse(path));
	}

	public void setVideoURI(Uri uri) {
		mUri = uri;
		//openVideo();
		executorService.execute(new OpenVideoRunnable());
	}
	class OpenVideoRunnable implements  Runnable{
		@Override
		public void run() {
			if (mMediaPlayer != null) {
				mMediaPlayer.reset();
				mMediaplayerState = STATE_IDLE;
			} else {
				mMediaPlayer = new MediaPlayer();
				mMediaplayerState = STATE_IDLE;
			}
			try {
				mMediaPlayer.setOnPreparedListener(mPreparedListener);
				mIsPrepared = false;
				mDuration = -1;
				mMediaPlayer.setOnCompletionListener(mCompletionListener);
				mMediaPlayer.setOnErrorListener(mErrorListener);
				mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
				//mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
				mCurrentBufferPercentage = 0;
				mMediaPlayer.setDataSource(mContext, mUri);
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.setScreenOnWhilePlaying(true);
				if (mSurfaceHolder != null) {
					mSurfaceHolder.setKeepScreenOn(true);
					if (mSurfaceHolder.getSurface().isValid()) {
						if (mMediaPlayer != null && mMediaplayerState == STATE_IDLE) {
							try {
								mMediaPlayer.prepareAsync();
								mMediaplayerState = STATE_PREPARING;
							} catch (IllegalStateException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					} else {
						mSurfaceHolder.addCallback(newCallback);
					}
					// mMediaPlayer.setDisplay(mSurfaceHolder);
				} else {
					mMediaPlayer.prepareAsync();
					mMediaplayerState = STATE_PREPARING;
				}
				attachMediaController();
			} catch (IOException ex) {
				if (!mUri.toString().contains("http")) {
					Looper.prepare();
					Toast.makeText(mContext, R.string.voa_badfile, Toast.LENGTH_LONG)
							.show();
					Looper.loop();
					// CustomToast.showToast(mContext, R.string.voa_badfile, 5000);
				} else {
					Toast.makeText(mContext, R.string.voa_badnet, Toast.LENGTH_LONG).show();
				}
				// mMediaPlayer = null;
				ex.printStackTrace();
				return;
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}
	/*private synchronized void openVideo() {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaplayerState = STATE_IDLE;
		} else {
			mMediaPlayer = new MediaPlayer();
			mMediaplayerState = STATE_IDLE;
		}
		try {
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mIsPrepared = false;
			mDuration = -1;
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			//mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
			mCurrentBufferPercentage = 0;
			mMediaPlayer.setDataSource(mContext, mUri);
			Log.e(TAG, "Videoview mediaplayer uri : " + mUri.toString());
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			Log.e(TAG, "mSurfaceHolder == null is " + (mSurfaceHolder == null));
			if (mSurfaceHolder != null) {
				mSurfaceHolder.setKeepScreenOn(true);
				if (mSurfaceHolder.getSurface().isValid()) {
					if (mMediaPlayer != null && mMediaplayerState == STATE_IDLE) {
						try {
							mMediaPlayer.prepareAsync();
							mMediaplayerState = STATE_PREPARING;
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					}
				} else {
					mSurfaceHolder.addCallback(newCallback);
				}
				// mMediaPlayer.setDisplay(mSurfaceHolder);
			} else {
				mMediaPlayer.prepareAsync();
				mMediaplayerState = STATE_PREPARING;
			}
			attachMediaController();
		} catch (IOException ex) {
			if (!mUri.toString().contains("http")) {
				Toast.makeText(mContext, R.string.voa_badfile, Toast.LENGTH_LONG)
						.show();
				// CustomToast.showToast(mContext, R.string.voa_badfile, 5000);
			} else {
				Toast.makeText(mContext, R.string.voa_badnet, Toast.LENGTH_LONG).show();
			}
			// mMediaPlayer = null;
			ex.printStackTrace();
			return;
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			return;
		}
	}*/

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
      mMediaPlayer.release();
      mMediaPlayer = null;
			mMediaplayerState = STATE_IDLE;
		}
	}

	// WATCH OUT!! this is a cheat here!!
	public void stopPlayback2() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
			mMediaPlayer.seekTo(0);
			mMediaplayerState = STATE_PLAYBACK_COMPLETED;
		}
	}

	public void setMediaController(MediaController controller) {
		if (mMediaController != null) {
			mMediaController.hide();
		}
		mMediaController = controller;
		attachMediaController();
	}

	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			mMediaController.setEnabled(mIsPrepared);
		}
	}

	OnPreparedListener mPreparedListener = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			// briefly show the mediacontroller
			mIsPrepared = true;
			mMediaplayerState = STATE_PREPARED;
			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(mMediaPlayer);
			}
			if (mMediaController != null) {
				mMediaController.setEnabled(true);
			}
		}
	};

	SurfaceHolder.Callback newCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.e("mSurfaceHolder", "ondes");
			if (mMediaPlayer != null) {
				mMediaPlayer.setDisplay(null);
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.e("mSurfaceHolder", "surfaceCreated");
			// mSurfaceHolder=holder;
			if (mMediaPlayer != null && mSurfaceHolder.getSurface().isValid()) {
				mMediaPlayer.setDisplay(mSurfaceHolder);
			}
			if (mMediaPlayer != null && mMediaplayerState == STATE_IDLE) {
				try {
					mMediaPlayer.prepareAsync();
					mMediaplayerState = STATE_PREPARING;
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}
	};

	private OnCompletionListener mCompletionListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			mMediaplayerState = STATE_PLAYBACK_COMPLETED;
			if (mMediaController != null) {
				mMediaController.hide();
			}
			if (mOnCompletionListener != null) {
				mOnCompletionListener.onCompletion(mMediaPlayer);
			}
		}
	};

	private OnErrorListener mErrorListener = new OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			mMediaplayerState = STATE_ERROR;
			if (mMediaController != null) {
				mMediaController.hide();
			}

			if (mOnErrorListener != null) {
				if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
					return true;
				}
			}
			return true;
		}
	};

	private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
		}
	};
	
	/*private OnSeekCompleteListener mSeekCompleteListener = new OnSeekCompleteListener() {		
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			if(mSeekComListener != null){
				mSeekComListener.onSeekComplete(mp);
			}
		}
	};*/
	public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
		mBufferingUpdateListener = l;
	}
	/**
	 * Register a callback to be invoked when the media file is loaded and ready
	 * to go.
	 * 
	 * @param l
	 *          The callback that will be run
	 */
	public void setOnPreparedListener(OnPreparedListener l) {
		mOnPreparedListener = l;
	}
	/**
	 * Register a callback to be invoked when the end of a media file has been
	 * reached during playback.
	 * 
	 * @param l
	 *          The callback that will be run
	 */
	public void setOnCompletionListener(OnCompletionListener l) {
		mOnCompletionListener = l;
	}

	/**
	 * Register a callback to be invoked when an error occurs during playback or
	 * setup. If no listener is specified, or if the listener returned false,
	 * VideoView will inform the user of any errors.
	 * 
	 * @param l
	 *          The callback that will be run
	 */
	public void setOnErrorListener(OnErrorListener l) {
		mOnErrorListener = l;
	}
	
//	public void setOnSeekCompleteListener(OnSeekCompleteListener l){
//		mOnSeekCompleteListener = l;
//	}

	public void start() {
		if (mMediaPlayer != null && mIsPrepared) {
			mMediaPlayer.start();
			mMediaplayerState = STATE_PLAYING;
		} else {
			return;
		}
	}

	public void pause() {
		if (mMediaPlayer != null && mIsPrepared) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mMediaplayerState = STATE_PAUSED;
			}
		}
	}

	public int getDuration() {
		if (mMediaPlayer != null && mIsPrepared) {
			mDuration = mMediaPlayer.getDuration();
			return mDuration;
		}
		mDuration = -1;
		return mDuration;
	}

	public int getCurrentPosition() {
		if (mMediaPlayer != null && mIsPrepared) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public void seekTo(int msec) {
		if (mMediaPlayer != null && mIsPrepared) {
			mMediaPlayer.seekTo(msec);
		} else {
			return;
		}
	}
	
//	public void seekToAndPlay(int msec){
//		if (mMediaPlayer != null && mIsPrepared) {
//			mMediaplayerState = STATE_PREPARED;
//			mMediaPlayer.seekTo(msec);
//		} else {
//			return;
//		}
//	}

	public boolean isPlaying() {
		if (mMediaPlayer != null && mIsPrepared) {
			return mMediaPlayer.isPlaying();
		}
		return false;
	}

	public boolean isPausing() {
		return mMediaplayerState == STATE_PAUSED;
	}

	public boolean isPreparing() {
		return mMediaplayerState == STATE_PREPARING;
	}

	public boolean isPrepared() {
		return mMediaplayerState == STATE_PREPARED;
	}

	public boolean isCompleted() {
		return mMediaplayerState == STATE_PLAYBACK_COMPLETED;
	}

	public int getMediaplayerState() {
		return mMediaplayerState;
	}

	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	@Override
	public boolean canSeekBackward() {
		return false;
	}

	@Override
	public boolean canSeekForward() {
		return false;
	}

	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			mSurfaceHolder.setFixedSize(w, h);
			if (mMediaPlayer != null && mIsPrepared) {
				// mMediaPlayer.seekTo(mSeekWhenPrepared);
			}
			mMediaPlayer.start();
			mMediaplayerState = STATE_PLAYING;
			if (mMediaController != null) {
				mMediaController.show();
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
			//openVideo();
			executorService.submit(new OpenVideoRunnable());
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			if (mMediaController != null)
				mMediaController.hide();
			if (mMediaPlayer != null) {
				mMediaPlayer.reset();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}
	};

	public void setmSurfaceHolder(SurfaceHolder mSurfaceHolder) {
		this.mSurfaceHolder = mSurfaceHolder;
		if (mSurfaceHolder != null) {
			mSurfaceHolder.addCallback(newCallback);
		}
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public boolean canPause() {
		return false;
	}
}
