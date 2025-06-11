package com.iyuba.camstory.sqlite.op;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.iyuba.camstory.sqlite.DatabaseService;
import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.voa.activity.setting.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取文章列表数据
 * 
 * @author ct
 * @time 12.9.27
 * 
 */
public class VoaOp extends DatabaseService {
	private static final String TAG = VoaOp.class.getSimpleName();

	public static final String TABLE_NAME = "voa";
	public static final String VOAID = "voaid";
	public static final String TITLE = "title";
	public static final String DESCCN = "desccn";
	public static final String TITLECN = "title_cn";
	public static final String CATEGORY = "category";
	public static final String SOUND = "sound";
	public static final String URL = "url";
	public static final String PIC = "pic";
	public static final String CREATTIME = "creattime";
	public static final String FAVOURITE = "favourite";
	public static final String READCOUNT = "readcount";
	public static final String HOTFLAG = "hotflg";
	public static final String ISREAD = "isread";
	public static final String DOWNLOAD = "download";

	public VoaOp() {
		super();
	}

	/**
	 * 批量插入数据
	 */
	public synchronized void saveData(List<Voa> voas) {
		if (voas != null && voas.size() != 0) {
			database.beginTransaction();
			try {
				for (int i = 0; i < voas.size(); i++) {
					Voa tempVoa = voas.get(i);
					Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where voaid="
							+ tempVoa.voaid, new String[] {});
					int databaseHasNum = cursor.getCount();
					if (databaseHasNum == 0) {
						database.execSQL("insert into " + TABLE_NAME + " (" + VOAID + "," + TITLE + ","
								+ DESCCN + "," + TITLECN + "," + CATEGORY + "," + SOUND + "," + URL + "," + PIC
								+ "," + CREATTIME + "," + FAVOURITE + "," + READCOUNT + "," + HOTFLAG + ","
								+ ISREAD + "," + DOWNLOAD + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
								tempVoa.voaid, tempVoa.title, tempVoa.desccn, tempVoa.title_cn, tempVoa.category,
								tempVoa.sound, tempVoa.url, tempVoa.pic, tempVoa.creattime, tempVoa.favourite,
								tempVoa.readcount, tempVoa.hotflg, "0", tempVoa.isDownloaded ? 1 : 0 });
					}
					cursor.close();
					cursor = null;
				}
				database.setTransactionSuccessful();
			} catch (Exception e) {
				Log.e(TAG, "Exception message: " + e.getMessage());
			} finally {
				database.endTransaction();
			}
		}
	}

	public synchronized void saveOrUpdateData(List<Voa> voas) {
		if (voas != null && voas.size() != 0) {
			database.beginTransaction();
			try {
				for (int i = 0; i < voas.size(); i++) {
					Voa tempVoa = voas.get(i);
					Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where voaid="
							+ tempVoa.voaid, new String[] {});
					int databaseHasNum = cursor.getCount();
					if (databaseHasNum == 0) {
						database.execSQL("insert into " + TABLE_NAME + " (" + VOAID + "," + TITLE + ","
								+ DESCCN + "," + TITLECN + "," + CATEGORY + "," + SOUND + "," + URL + "," + PIC
								+ "," + CREATTIME + "," + FAVOURITE + "," + READCOUNT + "," + HOTFLAG + ","
								+ ISREAD + "," + DOWNLOAD + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
								tempVoa.voaid, tempVoa.title, tempVoa.desccn, tempVoa.title_cn, tempVoa.category,
								tempVoa.sound, tempVoa.url, tempVoa.pic, tempVoa.creattime, tempVoa.favourite,
								tempVoa.readcount, tempVoa.hotflg, "0", tempVoa.isDownloaded ? 1 : 0 });
					} else {
						ContentValues values = new ContentValues();
						values.put(READCOUNT, tempVoa.readcount);
						database.update(TABLE_NAME, values, "voaid = " + tempVoa.voaid, null);
						Log.e(TAG, "voaid : " + tempVoa.voaid + " readcount : " + tempVoa.readcount);
					}
					cursor.close();
					cursor = null;
				}
				database.setTransactionSuccessful();
			} catch (Exception e) {
				Log.e(TAG, "Exception message: " + e.getMessage());
			} finally {
				database.endTransaction();
			}
		}
	}

	public synchronized void updateFav(int voaid, int operation) {
		ContentValues values = new ContentValues();
		values.put(FAVOURITE, operation);
		database.update(TABLE_NAME, values, "voaid=" + voaid, null);
	}

	/**
	 * 批量修改
	 * 
	 * @param voas
	 */
	public synchronized void updateData(List<Voa> voas) {
		if (voas != null && voas.size() != 0) {
			database.beginTransaction();
			for (int i = 0; i < voas.size(); i++) {
				Voa tempVoa = voas.get(i);
				database.execSQL("update " + TABLE_NAME + " set " + TITLE + "=' " + tempVoa.title + "',"
						+ DESCCN + "='" + tempVoa.desccn + "', " + TITLECN + "='" + tempVoa.title_cn + "',"
						+ CATEGORY + "='" + tempVoa.category + "', " + SOUND + "='" + tempVoa.sound + "',"
						+ URL + "='" + tempVoa.url + "'," + PIC + "='" + tempVoa.pic + "'," + CREATTIME + "='"
						+ tempVoa.creattime + "'," + FAVOURITE + "='" + tempVoa.favourite + "'," + READCOUNT
						+ "='" + tempVoa.readcount + "'," + HOTFLAG + "='" + tempVoa.hotflg + "' where "
						+ VOAID + "=" + tempVoa.voaid);
				closeDatabase();
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		}
	}

	public synchronized void deleteHistory(List<Voa> voas) {
		if (voas != null && voas.size() != 0) {
			database.beginTransaction();
			for (int i = 0; i < voas.size(); i++) {
				Voa tempVoa = voas.get(i);
				database.execSQL("update " + TABLE_NAME + " set " + ISREAD + "=0" + " where " + VOAID + "="
						+ tempVoa.voaid);
				closeDatabase();
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		}
	}

	public synchronized void deleteOneHistory(Voa voa) {
		if (voa != null) {
			database.execSQL("update " + TABLE_NAME + " set " + ISREAD + "=0" + " where " + VOAID + "="
					+ voa.voaid);
			closeDatabase();
		}
	}

	/**
	 * 单一修改
	 * 
	 * @paramvoas
	 */
	public void updateData(int voaid) {
		database.execSQL("update " + TABLE_NAME + " set " + ISREAD + "=" + System.currentTimeMillis()
				+ " where " + VOAID + "=" + voaid);
		closeDatabase();
	}

	/**
	 * 查找历史记录
	 * 
	 */
	public ArrayList<Voa> findReadDataAll() {
		ArrayList<Voa> voas = new ArrayList<Voa>();
		Cursor cursor = database.query(TABLE_NAME, null, ISREAD + "<>0", null, null, null, ISREAD
				+ " desc");

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		if (voas.size() != 0) {
			return voas;
		}
		return null;
	}

	public void updateReadCount(int voaid, int readCount) {
		database.execSQL("update " + TABLE_NAME + " set " + READCOUNT + "=" + readCount + " where "
				+ VOAID + "=" + voaid);
		closeDatabase();
	}

	/**
	 * 单一修改
	 * 
	 */
	public void updateCountData(int voaid, int count) {
		database.execSQL("update " + TABLE_NAME + " set " + READCOUNT + "=" + count + " where " + VOAID
				+ "=" + voaid);
		closeDatabase();
	}

	/**
	 * 查询全部数据
	 * 
	 * @return
	 */
	public synchronized List<Voa> findDataByAll() {
		List<Voa> voas = new ArrayList<Voa>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + 
				" order by " + CREATTIME + " desc, " + VOAID + " desc", new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		if (voas.size() != 0) {
			return voas;
		}
		return null;
	}

	public List<Voa> findAllCollection() {
		List<Voa> voas = new ArrayList<Voa>();
		String sql = "select * from " + TABLE_NAME + " where " + FAVOURITE +
				" = ? order by " + CREATTIME + " desc, " + VOAID + " desc";
		Cursor cursor = database.rawQuery(sql, new String[] { "1" });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		cursor.close();
		return voas;
	}

	public List<Voa> findDataByCategory(int limit, int category) {
		List<Voa> voas = new ArrayList<Voa>();
		String sql = "select * from " + TABLE_NAME + " where " + CATEGORY + " = ? " + " order by "
				+ CREATTIME + " desc, " + VOAID + " desc limit " + limit;
		Cursor cursor = database.rawQuery(sql, new String[] { category + "" });
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				voas.add(fillIn(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return voas;
	}

	/**
	 * 查询数据分页
	 */
	public synchronized List<Voa> findDataByPage(int count, int offset) {
		List<Voa> voas = new ArrayList<Voa>();
		Cursor cursor;
		// bbc英语的排序不同
		if (!Constant.getAppid().equals("221")) {
			cursor = database.rawQuery("select * from "	+ TABLE_NAME +
					" ORDER BY " + CREATTIME + " DESC ," + VOAID + " DESC" + "  Limit "
					+ count + " Offset " + offset, new String[] {});
		} else {
			cursor = database.rawQuery("select * from "	+ TABLE_NAME + 
					" ORDER BY " + CREATTIME + " DESC" + "  Limit " + count + " Offset "
					+ offset, new String[] {});
		}
		if (cursor.getCount() == 0) {
			if (cursor != null) {
				cursor.close();
			}
			closeDatabase();
			return voas;
		} else {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				voas.add(fillIn(cursor));
			}
			if (cursor != null) {
				cursor.close();
			}
			closeDatabase();
			return voas;
		}
	}

	/**
	 * 查询数据分页
	 * 
	 * @return
	 */
	public synchronized List<Voa> findDataByPageAndCategory(int category, int count, int offset) {
		List<Voa> voas = new ArrayList<Voa>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + CATEGORY + 
				" = ? order by " + CREATTIME + " desc, " + VOAID + " desc"
				+ "  Limit ? Offset ?", new String[] { String.valueOf(category), String.valueOf(count),
				String.valueOf(offset) });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		if (voas.size() != 0) {
			return voas;
		}
		return null;
	}

	/**
	 * 根据void主键查询
	 * 
	 * @param voaid
	 * @return
	 */
	public synchronized Voa findDataById(int voaid) {
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + VOAID + " = ? ",
				new String[] { String.valueOf(voaid) });
		if (cursor.moveToNext()) {
			Voa voa = fillIn(cursor);
			if (cursor != null) {
				cursor.close();
			}
			closeDatabase();
			return voa;
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		return null;
	}

	/**
	 * 按类别查询
	 * 
	 * @param value
	 * @return
	 */
	public synchronized List<Voa> findDataByCategory(String value) {
		List<Voa> voas = new ArrayList<Voa>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + CATEGORY + " = ? ",
				new String[] { value });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		if (voas.size() != 0) {
			return voas;
		}
		return null;
	}

	/**
	 * 查询下载
	 * 
	 * @return
	 */
	public synchronized List<Voa> findDataByDownload() {
		List<Voa> voas = new ArrayList<Voa>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + 
				" where " + DOWNLOAD + " ='1'", new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		if (voas.size() != 0) {
			return voas;
		}
		return null;
	}

	/**
	 * 添加下载
	 * 
	 * @return
	 */
	public synchronized void updateDataByDownload(int voaid) {
		database.execSQL("update " + TABLE_NAME + " set " + DOWNLOAD + "='1' where " + VOAID + "="
				+ voaid);
		closeDatabase();
	}

	/**
	 * 删除下载
	 * 
	 * @return
	 */
	public synchronized void deleteDataByDownload(int voaid) {
		database.execSQL("update " + TABLE_NAME + " set " + DOWNLOAD + "='0' where " + VOAID + "="
				+ voaid);
		closeDatabase();
	}

	/**
	 * 查询收藏
	 * 
	 * @return
	 */
	public synchronized List<Voa> findDataByCollection() {
		List<Voa> voas = new ArrayList<Voa>();
		Cursor cursor = database.rawQuery("select * from "
				+ TABLE_NAME + " where " + FAVOURITE + " ='1'", new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voas.add(fillIn(cursor));
		}
		if (cursor != null) {
			cursor.close();
		}
		closeDatabase();
		if (voas.size() != 0) {
			return voas;
		}
		return null;
	}

	/**
	 * 添加收藏
	 * 
	 * @return
	 */
	public synchronized void updateDataByCollection(int voaid) {
		database.execSQL("update " + TABLE_NAME + " set " + FAVOURITE + "='1' where " + VOAID + "="
				+ voaid);
		closeDatabase();
	}

	/**
	 * 删除收藏
	 * 
	 * @return
	 */
	public synchronized void deleteDataByCollection(int voaid) {
		database.execSQL("update " + TABLE_NAME + " set " + FAVOURITE + "='0' where " + VOAID + "="
				+ voaid);
		closeDatabase();
	}

	public synchronized void deleteAllCollection() {
		database.execSQL("update " + TABLE_NAME + " set " + FAVOURITE + "='0'  ");
		closeDatabase();
	}

	public List<Voa> findOlderArticles(int voaid, int limit) {
		List<Voa> voas = new ArrayList<Voa>();
		String sql = "select * from " + TABLE_NAME + " where " + VOAID + " < ?" + " order by " + CREATTIME
				+ " desc, " + VOAID + " desc limit " + limit;
		Cursor cursor = database.rawQuery(sql, new String[] { voaid + "" });
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				voas.add(fillIn(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return voas;
	}

	public List<Voa> findOlderArticlesByCategory(int voaid, int limit, int category) {
		List<Voa> voas = new ArrayList<Voa>();
		String sql = "select * from " + TABLE_NAME + " where " + VOAID + " < ? and " + CATEGORY
				+ " = ?" + " order by " + CREATTIME + " desc, " + VOAID + " desc limit " + limit;
		Cursor cursor = database.rawQuery(sql, new String[] { voaid + "", category + "" });
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				voas.add(fillIn(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return voas;
	}

	public List<Voa> findOlderArticlesInTimeOrder(int voaid, int limit) {
		List<Voa> voas = new ArrayList<Voa>();
		String sql = "select * from " + TABLE_NAME + " where " + CREATTIME + " < datetime((select "
				+ CREATTIME + " from " + TABLE_NAME + " where " + VOAID + " = ?))" + " order by datetime("
				+ CREATTIME + ") desc limit " + limit;
		Cursor cursor = database.rawQuery(sql, new String[] { voaid + "" });
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				voas.add(fillIn(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return voas;
	}

	public List<Voa> findOlderArticlesByCategoryInTimeOrder(int voaid, int limit, int category) {
		List<Voa> voas = new ArrayList<Voa>();
		String sql = "select * from " + TABLE_NAME + " where " + CREATTIME + " < datetime((select "
				+ CREATTIME + " from " + TABLE_NAME + " where " + VOAID + " = ?)) and " + CATEGORY
				+ " = ? order by datetime(" + CREATTIME + ") desc limit " + limit;
		Cursor cursor = database.rawQuery(sql, new String[] { voaid + "", category + "" });
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				voas.add(fillIn(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return voas;
	}

	private Voa fillIn(Cursor cursor) {
		Voa voa = new Voa();
		voa.voaid = cursor.getInt(0);
		voa.title = cursor.getString(1);
		voa.desccn = cursor.getString(2);
		voa.title_cn = cursor.getString(3);
		voa.category = cursor.getInt(4);
		voa.sound = cursor.getString(5);
		voa.url = cursor.getString(6);
		voa.pic = cursor.getString(7);
		voa.creattime = cursor.getString(8);
		voa.favourite = cursor.getInt(9) == 1;
		voa.readcount = cursor.getInt(10);
		voa.hotflg = cursor.getString(11);
		voa.isRead = cursor.getLong(12);
		voa.isDownloaded = cursor.getInt(13) == 1;

		return voa;
	}
}
