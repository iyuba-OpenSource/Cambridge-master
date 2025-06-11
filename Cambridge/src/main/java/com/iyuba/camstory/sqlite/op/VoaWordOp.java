package com.iyuba.camstory.sqlite.op;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.iyuba.camstory.sqlite.DatabaseService;
import com.iyuba.camstory.sqlite.mode.VoaWord;

public class VoaWordOp extends DatabaseService {
	
	public static final String TABLE_NAME = "voaword";
	public static final String _ID = "_id";
	public static final String VOAID = "voaid";
	public static final String DEF = "def";
	public static final String WORD = "word";
	public static final String INDEXID = "indexId";
	public static final String AUDIO = "audio";
/*	public static final String ORIG = "orig";
	public static final String TRANS = "trans";*/
	public static final String PRON = "pron";
	public static final String EXAMPLE = "example";
	public VoaWordOp() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	/**
	 * 批量插入数据
	 */
	public synchronized void saveData(List<VoaWord> voaWords) {
		
		if (voaWords != null && voaWords.size() != 0) {
			for (int i = 0; i < voaWords.size(); i++) {
				VoaWord tempVoa = voaWords.get(i);
					database.execSQL(
							"insert into " + TABLE_NAME + " (" + VOAID + ","
									+ DEF + "," + WORD + "," + INDEXID+"," + AUDIO +
									","  + PRON+
									","  + EXAMPLE
									+ ") values(?,?,?,?,?,?,?)",
							new Object[] { tempVoa.voaid, tempVoa.def,
									tempVoa.word, tempVoa.indexId,tempVoa.audio,tempVoa.pron,
									tempVoa.example});
					closeDatabase();
			}
		}
		
	}
	
	/**
	 * 
	 * 查找
	 * @return
	 */
	public synchronized List<VoaWord> findDataByVoaid(String voaid) {
		List<VoaWord> voaWords = new ArrayList<VoaWord>();
		Cursor cursor = database.rawQuery(
				"select " +VOAID+ "," + DEF + "," + WORD + "," + INDEXID + "," + AUDIO
				+ "," + PRON+ "," + EXAMPLE
				+ " from " + TABLE_NAME
						+ " where voaid='" + voaid  +"' ORDER BY " + INDEXID + " ASC",
				new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			VoaWord voaWord = new VoaWord();
			voaWord.voaid = cursor.getString(0);
			voaWord.def = cursor.getString(1);
			voaWord.word = cursor.getString(2);
			voaWord.indexId = cursor.getInt(3);
			voaWord.audio = cursor.getString(4);
			/*voaWord.orig = cursor.getString(5);
			voaWord.trans = cursor.getString(6);*/
			voaWord.pron = cursor.getString(5);
			voaWord.example = cursor.getString(6);
			voaWords.add(voaWord);
		}
		if (cursor!=null) {
			cursor.close();
		}
		closeDatabase();
		if (voaWords.size() != 0) {
			return voaWords;
		}
		return null;
	}
	
	/**
	 * 修改
	 * 
	 * @param voas
	 */
	public void updateData(VoaWord voaWord) {
		database.execSQL(
				"update " + TABLE_NAME + " set " + PRON + " = \""
						+ voaWord.pron + "\"," + EXAMPLE + " = \"" + voaWord.example + "\","
						+ AUDIO + " = '" + voaWord.audio + "' where " + WORD + " = '"
						+ voaWord.word + "'");
		closeDatabase();
	}

}
