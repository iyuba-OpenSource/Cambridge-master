package com.iyuba.multithread;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

/**
 * @author chenzefeng
 * 
 *         封装出的管理类
 */
public class MultiThreadDownloadManager {
	private static final String TAG = "MultiThreadDownloadManager";

	private static final String DOWNLOAD_STATE_DOWNLOADING = "isdownloading";
	private static final String DOWNLOAD_STATE_PAUSING = "ispausing";
	private static final String DOWNLOAD_STATE_NO_DOWNLOADING = "nothingisdownloading";
	private static final String DOWNLOAD_STATE_APP_CLOSING = "appisclosing";

	private static String currentState = null;

	/**
	 * 所有的下载任务
	 */
	private static HashMap<Integer, FileDownloader> tasks = new HashMap<Integer, FileDownloader>();

	/**
	 * 数据库操作
	 */
	private static FileService fileService;

	public static void enQueue(final Context context, final int voaid,
			final String downloadUrl, final File fileSaveDir, final int threadNum,
			final DownloadProgressListener listener) {
		Log.v("MultiThreadDownloadManager","MultiThreadDownloadManager1");
		if (fileService == null) {
			Log.v("MultiThreadDownloadManager","MultiThreadDownloadManager2");
			fileService = new FileService(context);
		}
		Log.v("MultiThreadDownloadManager","MultiThreadDownloadManager3");
		DownloadProgressListener progressListener = new DownloadProgressListener() {
			@Override
			public void onProgressUpdate(int id, String downloadurl,
					int fileDownloadSize) {
				Log.v("MultiThreadDownloadManager","progressListener");
				if (listener != null) {
					Log.v("MultiThreadDownloadManager","progressListener不为空1");
					listener.onProgressUpdate(id, downloadUrl, fileDownloadSize);
					Log.v("MultiThreadDownloadManager","progressListener不为空2");
				}
			}

			@Override
			public void onDownloadStoped(int id) {
				if (tasks.isEmpty()) {
					Log.v("MultiThreadDownloadManager","tasks.isEmpty1");
					currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
					Log.v("MultiThreadDownloadManager","tasks.isEmpty2");
				}
				if (listener != null) {
					Log.v("MultiThreadDownloadManager","tasks.isEmpty3");
					listener.onDownloadStoped(id);
					Log.v("MultiThreadDownloadManager","tasks.isEmpty4");
				}
			}

			@Override
			public void onDownloadStart(FileDownloader fileDownloader, int id,
					int fileTotalSize) {
				Log.v("MultiThreadDownloadManager","onDownloadStart1");
				if (!tasks.containsKey(id)) {
					Log.v("MultiThreadDownloadManager","onDownloadStart2");
					tasks.put(id, fileDownloader);
					Log.v("MultiThreadDownloadManager","onDownloadStart3");
				}
				currentState = DOWNLOAD_STATE_DOWNLOADING;
				if (listener != null) {
					Log.v("MultiThreadDownloadManager","onDownloadStart4");
					listener.onDownloadStart(fileDownloader, id, fileTotalSize);
					Log.v("MultiThreadDownloadManager","onDownloadStart5");
				}
			}

			@Override
			public void onDownloadError(int id, Exception e) {
				Log.v("MultiThreadDownloadManager","onDownloadStart6");
				if (!tasks.containsKey(id)) {
					Log.v("MultiThreadDownloadManager","onDownloadStart7");
					tasks.remove(id);
					Log.v("MultiThreadDownloadManager","onDownloadStart8");
				}
				if (listener != null) {
					Log.v("MultiThreadDownloadManager","onDownloadStart9");
					listener.onDownloadError(id, e);
					Log.v("MultiThreadDownloadManager","onDownloadStart10");
				}
			}

			@Override
			public void onDownloadComplete(int id, String savePathFullName) {
				Log.v("MultiThreadDownloadManager","onDownloadStart11");
				tasks.remove(id);
				Log.v("MultiThreadDownloadManager","onDownloadStart12");
				if (tasks.isEmpty()) {
					Log.v("MultiThreadDownloadManager","onDownloadStart3");
					currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
					Log.v("MultiThreadDownloadManager","onDownloadStart14");
				}
				if (listener != null) {
					Log.v("MultiThreadDownloadManager","onDownloadStart15");
					listener.onDownloadComplete(id, savePathFullName);
					Log.v("MultiThreadDownloadManager","onDownloadStart16");
				}
			}
		};
		if (tasks.containsKey(voaid)) {
			Log.v("MultiThreadDownloadManager","onDownloadStart17");
			try {
				if (!tasks.get(voaid).isRunning()) {
					tasks.get(voaid).download(voaid, progressListener);
				} else {
					Log.e("enQueue", "the task+" + voaid + " is running");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.v("MultiThreadDownloadManager","onDownloadStart18");
			Log.e(TAG, "the fucking tasks doesn't contains this voaid : " + voaid);
			final FileDownloader tempDownloader = new FileDownloader(context,
					downloadUrl, fileSaveDir, threadNum);
			Log.v(TAG,"the fucking tasks1"+downloadUrl);
			try {
				Log.v(TAG,"the fucking tasks2");
				tempDownloader.download(voaid, progressListener);
				Log.v(TAG,"the fucking tasks3");
			} catch (Exception e) {
				Log.v(TAG,"the fucking tasks4");
				e.printStackTrace();
			}
		}

	}

	/**
	 * 停止所有下载线程
	 */
	public static void stopDownloads() {
		if (tasks != null) {
			Iterator iterator = tasks.keySet().iterator();
			while (iterator.hasNext()) {
				FileDownloader type = tasks.get(iterator.next());
				if (type.isRunning()) {
					type.cancel();
				}
			}
		}
		currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
	}

	/**
	 * 停止指定下载线程
	 * 
	 * @param id
	 *          任务的id
	 */
	public static void stopDownload(int id) {
		if (tasks != null) {
			FileDownloader type = tasks.get(id);
			type.cancel();
		}
		if (tasks.isEmpty()) {
			currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
		}
	}

	/**
	 * 数据库中是否有未完成任务
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasUnFinishTask(Context context) {
		if (fileService == null) {
			fileService = new FileService(context);
		}
		tasks = fileService.findDownload(context);
		return !tasks.isEmpty();
	}

	/**
	 * 根据id返回百分比
	 * 
	 */
	public static int getTaskPercentage(int id) {
		if (tasks.containsKey(id)) {
			return tasks.get(id).getDownloadPercentage();
		}
		return 0;
	}

	/**
	 * 根据id彻底移除下载任务
	 * 
	 */
	public static void removeTask(int id) {
		if (tasks.containsKey(id)) {
			tasks.remove(id);
		}
		if (tasks.isEmpty()) {
			currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
		}
	}

	/**
	 * 是否有此task
	 * 
	 */
	public static boolean hasTask(int id) {
		return tasks.containsKey(id);
	}

	/**
	 * 彻底移除所有下载任务
	 * 
	 */
	public static void removeAllTask() {
		if (tasks != null) {
			Iterator iterator = tasks.keySet().iterator();
			while (iterator.hasNext()) {
				FileDownloader type = tasks.get(iterator.next());
				type.cancel();
				File file = new File(type.getDownloadUrl());
				if (file.exists()) {
					file.delete();
				}
				fileService.delete(type.getDownloadUrl());
			}
		}
		currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
	}

	/**
	 * 有正在下载（暂停不算）的任务
	 * 
	 * @return
	 */
	public static boolean IsDowning() {
		return DOWNLOAD_STATE_DOWNLOADING.equals(currentState);
	}
}
