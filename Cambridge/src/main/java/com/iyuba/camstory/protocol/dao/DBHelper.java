package com.iyuba.camstory.protocol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String DB_NAME = "mydb.db";
    private static final int DB_VERSION = 7;

    private static DBHelper sInstance = null;

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }
    private static final String CREATE_UPVOTE_TABLE = "create table if not exists " + CommentAgreeDao.TABLE_NAME + " (" +
            CommentAgreeDao.UID + " text," +
            CommentAgreeDao.COMMENTID + " text,"+
            CommentAgreeDao.AGREE + " text)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table shop_cart (appImg text,appInfo text,appName text," +
                "appPrice text,authorImg text,authorInfo text,bookAuthor text," +
                "bookPrice text,classImg text,classInfo text,classPrice text," +
                "contentImg text,contentInfo text,createTime text,desc text," +
                "editImg text,editInfo text,flg text,groups text,id text not null," +
                "imgSrc text,name text,pkg text,publishHouse text," +
                "totalPrice text,types text,updateTime text,bookId text," +
                "uid text not null,number integer default 0,primary key (id,uid))");
        db.execSQL(sb.toString());
        db.execSQL(CREATE_UPVOTE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();
        switch (oldVersion) {
            case 1:
                onCreate(db);
                //transferOldCollectionData(db);
                break;
//            case 2:
//                sb.setLength(0);
//                sb.append("create table Have_Read (NewsId integer,Date text,IsRead integer default 0,UserId integer,primary key(NewsId,UserId) )");
//                db.execSQL(sb.toString());
//
//                sb.setLength(0);
//                sb.append("CREATE TABLE [" + "PersonTable" + "] ([date1] TEXT,[isselct] TEXT,[userid] TEXT)");
//                db.execSQL(sb.toString());
//            case 3:
//                sb.setLength(0);
//                sb.append("ALTER TABLE Have_Read RENAME TO __Have_Read");
//                db.execSQL(sb.toString());
//
//                sb.setLength(0);
//                sb.append("create table Have_Read (NewsId integer,Date text,IsRead integer default 0,UserId integer,primary key(NewsId,UserId) )");
//                db.execSQL(sb.toString());
//
//                sb.setLength(0);
//                sb.append("INSERT INTO Have_Read (NewsId) SELECT NewsId FROM __Have_Read");
//                db.execSQL(sb.toString());
//
//                sb.setLength(0);
//                sb.append("DROP TABLE IF EXISTS __Have_Read");
//                db.execSQL(sb.toString());
////                break;
//            case 4:
//                sb.setLength(0);
//                sb.append("create table comment_on_comment (uid text,comment_id text,agree text)");
//                db.execSQL(sb.toString());

            case 5:
                sb.setLength(0);
                sb.append("create table shop_cart (appImg text,appInfo text,appName text," +
                        "appPrice text,authorImg text,authorInfo text,bookAuthor text," +
                        "bookPrice text,classImg text,classInfo text,classPrice text," +
                        "contentImg text,contentInfo text,createTime text,desc text," +
                        "editImg text,editInfo text,flg text,groups text,id text not null," +
                        "imgSrc text,name text,pkg text,publishHouse text," +
                        "totalPrice text,types text,updateTime text,bookId text," +
                        "uid text not null,number integer default 0,primary key (id,uid))");
                db.execSQL(sb.toString());

//            case 6:
//                sb.setLength(0);
//                sb.append("create table search_history (uid text,content text,primary key(uid,content))");
//                db.execSQL(sb.toString());
//                break;
            default:
                //transferOldCollectionData(db);
                break;
        }
        Log.e(TAG, "upgrade database from version " + oldVersion + " to version " + newVersion);
    }

    private void transferOldCollectionData(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // get data from old table
            List<Integer> userIds = new ArrayList<Integer>();
            List<Integer> newsIds = new ArrayList<Integer>();
            Cursor cursor = db.query("NewsId", new String[]{"user_id", "news_id"}, null, null, null,
                    null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    userIds.add(cursor.getInt(0));
                    newsIds.add(cursor.getInt(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            // insert data into new table
            for (int i = 0; i < userIds.size(); i++) {
                ContentValues values = new ContentValues();
                values.put("user_id", userIds.get(i));
                values.put("news_id", newsIds.get(i));
                long now = System.currentTimeMillis();
                values.put("created_at", now);
                values.put("updated_at", now);
                db.insert("User_Article", null, values);
            }
            // after transfer drop old table
            deleteOldTable(db);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void deleteOldTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists Data");
        db.execSQL("drop table if exists Data01");
        db.execSQL("drop table if exists NewsId");
        db.execSQL("drop table if exists Details");
    }

}
