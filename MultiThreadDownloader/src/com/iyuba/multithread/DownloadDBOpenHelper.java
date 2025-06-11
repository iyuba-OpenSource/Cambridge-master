package com.iyuba.multithread;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadDBOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "down.db";
	private static final int DB_VERSION = 1;

	public DownloadDBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE filedownlog (id integer primary key autoincrement,")
				.append(" fileid INTEGER, downurl varchar(100), downpath varchar(100),")
				.append(" threadid INTEGER, downlength INTEGER, titallength INTEGER)");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS filedownlog");
		onCreate(db);
	}
}