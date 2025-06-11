package com.iyuba.camstory.protocol.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CommentAgreeDao {
    public static final String TABLE_NAME = "comment_on_comment";
    public static final String UID = "uid";
    public static final String COMMENTID = "comment_id";
    public static final String AGREE = "agree";

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public CommentAgreeDao(Context context) {
        this.mDBHelper = DBHelper.getInstance(context);
        this.mDB = mDBHelper.getWritableDatabase();
    }

    public synchronized int findDataByAll(String commentid, String uid) {
        Cursor cursor = mDB.rawQuery(
                "select " + COMMENTID + " , " + UID + "," + AGREE + " from "
                        + TABLE_NAME + " where " + COMMENTID + " = ? AND "
                        + UID + " = ?", new String[] { commentid, uid });
        if (cursor != null && cursor.getCount() == 0) {
            cursor.close();
            return 0;
        } else {
            int temp = 0;
            cursor.moveToFirst();
            if (cursor.getString(2).equals("against")) {
                temp = 2;
            } else {
                temp = 1;
            }
            cursor.close();
            return temp;
        }
    }

    public synchronized void saveData(String commentid, String uid, String agree) {
        if (commentid != null && uid != null) {
            mDB.execSQL(
                    "insert or replace into " + TABLE_NAME + " (" + COMMENTID
                            + "," + UID + "," + AGREE + ") values(?,?,?)",
                    new Object[] { commentid, uid, agree });
        }
    }
}
