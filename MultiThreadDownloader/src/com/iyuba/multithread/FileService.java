package com.iyuba.multithread;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileService {
	private DownloadDBOpenHelper openHelper;

	public FileService(Context context) {
		openHelper = new DownloadDBOpenHelper(context);
	}

	/**
	 * 获取每条线程已经下载的文件长度
	 * 
	 * @param path
	 * @return
	 */
	public synchronized Map<Integer, Integer> getData(String path) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select threadid, downlength from filedownlog where downurl=?",
				new String[] { path });
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		while (cursor.moveToNext()) {
			data.put(cursor.getInt(0), cursor.getInt(1));
			if (data.size() == 2) {
				break;
			}
		}
		cursor.close();
		db.close();
		return data;
	}

	public synchronized boolean isInDownloadDB(String path) {
		boolean result = false;
		try {
			SQLiteDatabase db = openHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(
					"select threadid, downlength from filedownlog where downurl = ?", new String[] {path});
			result = cursor.getCount() > 0;
			cursor.close();
			db.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public synchronized boolean isInDownloadDB(int id) {
		boolean result = false;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select threadid, downlength from filedownlog where fileid=?",
				new String[] { id + "" });
		result = cursor.getCount() > 0;
		cursor.close();
		db.close();
		return result;
	}

	// 找出下载使用的url
	public synchronized String findDownloadUrl(int id) {
		String url;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select downurl from filedownlog where fileid=?", new String[] { id
						+ "" });
		cursor.moveToFirst();
		url = cursor.getString(0);
		cursor.close();
		db.close();
		return url;
	}

	/**
	 * 保存每条线程已经下载的文件长度
	 * 
	 * @param path
	 * @param map
	 *          <threadid, position>
	 */
	public synchronized void save(String path, Map<Integer, Integer> map) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();

		try {
			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
				db.execSQL(
						"insert into filedownlog(downurl, threadid, downlength) values(?,?,?)",
						new Object[] { path, entry.getKey(), entry.getValue() });
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		db.close();
	}

	/**
	 * 保存每个任务总的文件长度
	 * 
	 * @param path
	 * @param filesize
	 */
	public synchronized void saveFileSize(String path, int filesize) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("update filedownlog set titallength=" + filesize
				+ " where downurl='" + path + "'");
		db.close();
	}

	/**
	 * 保存每个任务的标示ID（如voaid)
	 * 
	 * @param url
	 * @param id
	 */
	public synchronized void saveFileId(String url, int id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("update filedownlog set fileid=" + id + " where downurl='" + url
				+ "'");
		db.close();
	}

	/**
	 * 获取每个任务的标示ID（如voaid)
	 */
	public synchronized ArrayList<Integer> findFileId() {
		ArrayList<Integer> idsArrayList = new ArrayList<Integer>();
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Cursor cursor = db.query("filedownlog", new String[] { "fileid" }, null,
				null, "fileid", null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			idsArrayList.add(cursor.getInt(0));
		}
		cursor.close();
		db.close();
		return idsArrayList;
	}

	/**
	 * 保存每个任务的本地文件地址
	 * 
	 * @param path
	 * @param url
	 */
	public synchronized void saveFilePath(String path, String url) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("update filedownlog set downpath='" + path + "' where downurl='"
				+ url + "'");
		db.close();
	}

	/**
	 * 实时更新每条线程已经下载的文件长度
	 * 
	 * @param path
	 * @param map
	 */
	public synchronized void update(String path, Map<Integer, Integer> map) {
		try {
			SQLiteDatabase db = openHelper.getWritableDatabase();//????
			db.beginTransaction();
			try {
				for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
					db.execSQL(
							"update filedownlog set downlength=? where downurl=? and threadid=?",
							new Object[] { entry.getValue(), path, entry.getKey() });
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();//？？
		}
	}

	/**
	 * 当文件下载完成后，删除对应的下载记录
	 * 
	 * @param path
	 */
	public synchronized void delete(String path) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from filedownlog where downurl=?", new Object[] { path });
		db.close();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public synchronized HashMap<Integer, FileDownloader> findDownload(
			Context context) {
		HashMap<Integer, FileDownloader> tasks = new HashMap<Integer, FileDownloader>();
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Cursor cursor = db.query("filedownlog", new String[] { "fileid", "downurl",
				"downpath", "SUM(downlength)", "titallength" }, null, null, "fileid",
				null, "fileid DESC");
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			FileDownloader tempDownloadTask = new FileDownloader(context,
					cursor.getString(1), new File(cursor.getString(2)), 2);
			tempDownloadTask.setTaskid(cursor.getInt(0));
			tempDownloadTask.setFileSize(cursor.getInt(4));
			tempDownloadTask.setDownloadSize(cursor.getInt(3));
			tasks.put(tempDownloadTask.taskid, tempDownloadTask);
		}
		cursor.close();
		db.close();
		return tasks;
	}

}
