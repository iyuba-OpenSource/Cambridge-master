package com.iyuba.abilitytest.utils.download;

import android.content.Context;
import android.util.Log;


import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author http://www.cnblogs.com/hanyonglu/archive/2012/02/20/2358801.html
 * 
 */
public class FileDownloader {
	private int DOWNLOAD_STATE = -1;
	private static final int DOWNLOAD_STATE_DOWNLOADING = 1;
	private static final int DOWNLOAD_STATE_STOP = 2;
	private static final String TAG = "FileDownloader";
	private Context context;
	private FileService fileService;


	private int downloadSize = 0;


	private int threadNum = 1;


	private int fileSize = 0;


	private DownloadThread[] threads;


	private File saveFile;


	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();


	private int block;


	private String downloadUrl;


	public int taskid;
	
	

	public int getThreadSize() {
		return threads.length;
	}


	public int getFileSize() {
		return fileSize;
	}


	protected synchronized void append(int size) {
		downloadSize += size;
	}


	protected synchronized void update(int threadId, int pos) {
		this.data.put(threadId, pos);
		this.fileService.update(this.downloadUrl, this.data);
	}


	public FileDownloader(Context context, String downloadUrl,
			File fileSaveDir, int threadNum) {
		this.context = context;
		this.downloadUrl = downloadUrl;
		fileService = new FileService(this.context);
		/* if(!fileSaveDir.exists()) fileSaveDir.mkdirs(); */
		this.saveFile = fileSaveDir;//
		this.threadNum = threadNum;
	}


	public void init() {
		try {
			URL url = new URL(this.downloadUrl);
			threads = new DownloadThread[threadNum];
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", downloadUrl);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			printResponseHeader(conn);

			if (conn.getResponseCode() == 200) {
				this.fileSize = conn.getContentLength();
				if (this.fileSize <= 0)
					throw new RuntimeException("Unkown file size ");


				Map<Integer, Integer> logdata = fileService
						.getData(downloadUrl);

				if (logdata.size() > 0) {
					for (Map.Entry<Integer, Integer> entry : logdata.entrySet())
						data.put(entry.getKey(), entry.getValue());
				}


				this.block = (this.fileSize % this.threads.length)==0?(this.fileSize / this.threads.length):(this.fileSize / this.threads.length + 1);
			} else {
				throw new RuntimeException("server no response ");
			}
		} catch (Exception e) {
			print(e.toString());
			throw new RuntimeException("don't connection this url");
		}
	}

	public void cancle() {
		for (int i = 0; i < threads.length; i++) {
			DownloadThread array_element = threads[i];
			if (array_element != null) {
				array_element.interrupt();
			}
		}
		DOWNLOAD_STATE = DOWNLOAD_STATE_STOP;
	}

	public boolean isRunning() {
		return DOWNLOAD_STATE == DOWNLOAD_STATE_DOWNLOADING;
	}



	private String getFileName(HttpURLConnection conn) {
		String filename = this.downloadUrl.substring(this.downloadUrl
				.lastIndexOf('/') + 1);

		if (filename == null || "".equals(filename.trim())) {
			for (int i = 0;; i++) {
				String mine = conn.getHeaderField(i);

				if (mine == null)
					break;

				if ("content-disposition".equals(conn.getHeaderFieldKey(i)
						.toLowerCase())) {
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(
							mine.toLowerCase());
					if (m.find())
						return m.group(1);
				}
			}

			filename = UUID.randomUUID() + ".tmp";
		}

		return filename;
	}


	public void download(final int id, final DownloadProgressListener listener)
			throws Exception {
		

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO �Զ���ɵķ������
				try {
					int bufferSize = 0;
					if (NetStatusUtil.isWifi(context)||NetStatusUtil.is4G(context)) {
						bufferSize = 8 * 1024;
					}
					else if (NetStatusUtil.is3G(context)) {
						bufferSize = 3 * 1024;
					}
					else {
						bufferSize = 1024;
					}
					init();
					RandomAccessFile randOut = new RandomAccessFile(
							FileDownloader.this.saveFile, "rw");
					if (fileSize > 0)
						randOut.setLength(fileSize);
					randOut.close();
					URL url = new URL(downloadUrl);
					if (data.size() != threads.length) {
						data.clear();
						print("data.size() != threads.length");
						for (int i = 0; i < threads.length; i++) {
							data.put(i + 1, 0);
						}
					}
					DOWNLOAD_STATE = DOWNLOAD_STATE_DOWNLOADING;
					if (listener != null) {
						listener.onDownloadStart(FileDownloader.this,id, getFileSize());
					}
					for (int i = 0; i < threads.length; i++) {
						int downLength = data.get(i + 1);
						if (downLength < block
								&& downloadSize < fileSize) {
							threads[i] = new DownloadThread(
									FileDownloader.this, url,
									saveFile,
									block,
									data.get(i + 1), i + 1,
									bufferSize);
							threads[i].setPriority(7);
							threads[i].start();
						} else {
							print("threads  :" +i);
							print("downLength < block   :"+(downLength < block));
							print("downloadSize < fileSize   :"+(downloadSize < fileSize));
							threads[i] = null;
						}
					}
					taskid = id;
					fileService.save(downloadUrl,data);
					fileService.saveFileSize(downloadUrl, getFileSize());
					fileService.saveFileId(downloadUrl, id);
					fileService.saveFilePath(saveFile.getAbsolutePath(),downloadUrl);
					boolean notFinish = true;
					while (notFinish) {
						Thread.sleep(900);
						notFinish = false;

						for (int i = 0; i < threads.length; i++) {
							if (threads[i] != null
									&& !threads[i].isFinish()) {
								notFinish = true;

								if (threads[i]
										.getDownLength() == -1) {
									threads[i] = new DownloadThread(
											FileDownloader.this,
											url,
											saveFile,
											block,
											data.get(i + 1),
											i + 1,
											bufferSize);
									threads[i]
											.setPriority(7);
									threads[i].start();
								}
							}
						}

						if (listener != null)
							listener.onProgressUpdate(id, downloadUrl,downloadSize);
						
						if (DOWNLOAD_STATE == DOWNLOAD_STATE_STOP) {
							break;
						}
					}
					if (DOWNLOAD_STATE == DOWNLOAD_STATE_STOP) {
						if (listener != null) {
							listener.onDownloadStoped(id);
						}
					} else {
						fileService.delete(downloadUrl);
						if (listener != null) {
							listener.onDownloadComplete(id,
									saveFile.getAbsolutePath());
						}
					}
				} catch (Exception e) {
					print(e.toString());
					e.printStackTrace();
					if (listener != null) {
						listener.onDownloadError(e.getMessage());
					}
				}
			}
		});
		thread.start();
	}

	/**
	 * ��ȡHttp��Ӧͷ�ֶ�
	 * 
	 * @param http
	 * @return
	 */
	public static Map<String, String> getHttpResponseHeader(
			HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();

		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;
			header.put(http.getHeaderFieldKey(i), mine);
		}

		return header;
	}


	public static void printResponseHeader(HttpURLConnection http) {
		Map<String, String> header = getHttpResponseHeader(http);

		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() + ":" : "";
			print(key + entry.getValue());
		}
	}


	private static void print(String msg) {
		Log.i(TAG, msg);
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public String getSaveFullPath() {
		return saveFile.getAbsolutePath();
	}

	public int getDownloadSize() {
		return downloadSize;
	}

	protected void setDownloadSize(int downloadSize) {
		this.downloadSize = downloadSize;
	}

	public int getDownloadPercentage() {
		return (downloadSize *100 / fileSize);
	}
	
	protected void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getTaskid() {
		return taskid;
	}

	protected void setTaskid(int taskid) {
		this.taskid = taskid;
	}
}
