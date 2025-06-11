package com.iyuba.camstory.sqlite.op;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import com.iyuba.camstory.sqlite.DatabaseService;
import com.iyuba.camstory.sqlite.mode.Word;


/**
 * 获取单词表数据库
 * 
 * @author ct
 * @time 12.9.27
 * 
 */
/**
 * @author chenzefeng
 *
 */

public class WordOp extends DatabaseService {
	public static final String TABLE_NAME_WORD = "word";
	public static final String _ID = "user";
	public static final String KEY = "key";
	public static final String LANG = "lang";
	public static final String AUDIOURL = "audiourl";
	public static final String PRON = "pron";
	public static final String DEF = "def";
	public static final String EXAMPLES = "examples";
	public static final String CREATEDATE = "createdate";
	public static final String ISDELETE = "isdelete";

	public WordOp() {
		super();
	}

	/**
	 * 批量插入数据
	 */
	public synchronized void saveData(Word word) {
		String dateTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTime = sdf.format(new Date());
		Cursor cursor = database.rawQuery(
				"select * from " + TABLE_NAME_WORD + " where key='" + word.key
						+ "' AND user='" + word.userid + "'", new String[] {});
		int databaseHasNum = cursor.getCount();
		closeDatabase();
		if (databaseHasNum == 0) {
			database.execSQL(
					"insert into " + TABLE_NAME_WORD + " (" + _ID + "," + KEY
							+ "," + LANG + "," + AUDIOURL + "," + PRON + ","
							+ DEF + "," + EXAMPLES + "," + CREATEDATE + ","
							+ ISDELETE + ") values(?,?,?,?,?,?,?,?,?)",
					new Object[] { word.userid, word.key, word.lang,
							word.audioUrl, word.pron, word.def, word.examples,
							dateTime, "0" });
			closeDatabase();
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public synchronized List<Word> findDataByAll(int userid) {
		List<Word> words = new ArrayList<Word>();
		Cursor cursor = database.rawQuery(
				"select " + _ID + "," + KEY + "," + LANG + "," + AUDIOURL + ","
						+ PRON + "," + DEF + "," + EXAMPLES + "," + CREATEDATE
						+ "," + ISDELETE + " from " + TABLE_NAME_WORD
						+ " where user='" + userid + "' AND " + ISDELETE
						+ "='0'" + " ORDER BY " + _ID + " DESC",
				new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Word word = new Word();
			word.userid = cursor.getInt(0);
			word.key = cursor.getString(1);
			word.lang = cursor.getString(2);
			word.audioUrl = cursor.getString(3);
			word.pron = cursor.getString(4);
			word.def = cursor.getString(5);
			word.examples = cursor.getString(6);
			word.createDate = cursor.getString(7);
			word.delete = cursor.getString(8);
			words.add(word);
		}
		if (cursor!=null) {
			cursor.close();
		}
		closeDatabase();
		if (words.size() != 0) {
			return words;
		}
		return null;
	}
	
	
	
 /**
 * @param userid
 * @param page
 * @return
 */
public synchronized List<Word> findDataByPage(int userid,int page) {
		List<Word> words = new ArrayList<Word>();
//		Cursor cursor = database.query(TABLE_NAME_WORD, null, "user=? AND ?=0", new String[]{userid+"", ISDELETE+""}
//		, null, null, _ID+" DESC", 30*page+",30");
		Cursor cursor = database.rawQuery(
				"select " + _ID + "," + KEY + "," + LANG + "," + AUDIOURL + ","
						+ PRON + "," + DEF + "," + EXAMPLES + "," + CREATEDATE
						+ "," + ISDELETE + " from " + TABLE_NAME_WORD
						+ " where user='" + userid + "' AND " + ISDELETE
						+ "='0'" + " ORDER BY " + _ID + " ASC"+"  Limit " + 30 + " Offset " + page*30,
				new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Word word = new Word();
			word.userid = cursor.getInt(0);
			word.key = cursor.getString(1);
			word.lang = cursor.getString(2);
			word.audioUrl = cursor.getString(3);
			word.pron = cursor.getString(4);
			word.def = cursor.getString(5);
			word.examples = cursor.getString(6);
			word.createDate = cursor.getString(7);
			word.delete = cursor.getString(8);
			words.add(word);
		}
		if (cursor!=null) {
			cursor.close();
		}
		closeDatabase();
		if (words.size() != 0) {
			return words;
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public synchronized List<Word> findDataByDelete(String userid) {
		List<Word> words = new ArrayList<Word>();
		Cursor cursor = database.rawQuery(
				"select " + _ID + "," + KEY + "," + LANG + "," + AUDIOURL + ","
						+ PRON + "," + DEF + "," + EXAMPLES + "," + CREATEDATE
						+ "," + ISDELETE + " from " + TABLE_NAME_WORD
						+ " where user='" + userid + "' AND " + ISDELETE
						+ "='1'", new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Word word = new Word();
			word.userid = cursor.getInt(0);
			word.key = cursor.getString(1);
			word.lang = cursor.getString(2);
			word.audioUrl = cursor.getString(3);
			word.pron = cursor.getString(4);
			word.def = cursor.getString(5);
			word.examples = cursor.getString(6);
			word.createDate = cursor.getString(7);
			word.delete = cursor.getString(8);
			words.add(word);
		}
		if (cursor!=null) {
			cursor.close();
		}
		closeDatabase();
		if (words.size() != 0) {
			return words;
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 *            ID集合，以“,”分割,每项加上""
	 * @return
	 */
	public synchronized boolean deleteItemWord(String userid) {
		try {
			database.execSQL(
					"delete from " + TABLE_NAME_WORD + " where " + _ID + "='"
							+ userid + "' AND " + ISDELETE + "='1'");
			closeDatabase();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean tryToDeleteItemWord(String keys, String userid) {
		try {
			database.execSQL(
					"update " + TABLE_NAME_WORD + " set " + ISDELETE
							+ "='1' where " + _ID + "='" + userid + "' AND "
							+ KEY + " in (" + keys + ")");
			closeDatabase();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
