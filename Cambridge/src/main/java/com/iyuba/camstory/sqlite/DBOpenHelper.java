package com.iyuba.camstory.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库更新表
 *
 * @author chentong
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = DBOpenHelper.class.getSimpleName();

    //	public static final String DB_NAME = "voa_database.sqlite";// 数据库名称
    public static final String DB_NAME = "fiction_data.sqlite";// 数据库名称
    public static final int DB_VERSION = 6;// 数据库版本，如果要更新数据库，在此版本号的基础上+1

    //2019-6-11 更新 5 ---> 6
    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBOpenHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 保留下载信息
        switch (oldVersion) {
            case 1:
            case 2:
            case 3:// 更新学习记录表
                db.execSQL("Drop TABLE StudyRecord");
                db.execSQL("Drop TABLE TestRecord");
                db.execSQL("CREATE TABLE IF NOT EXISTS StudyRecord (uid integer,"
                        + "BeginTime varchar,EndTime varchar,Lesson varchar,LessonId integer"
                        + ",EndFlg integer,uploadFlag integer,PRIMARY KEY (uid,BeginTime))");
                db.execSQL("CREATE TABLE IF NOT EXISTS TestRecord (uid integer,"
                        + "BeginTime varchar,TestTime varchar,LessonId integer,"
                        + "TestNumber integer,UserAnswer varchar,RightAnswer varchar,"
                        + "AnswerResult integer,uploadFlag integer,PRIMARY KEY "
                        + "(uid,BeginTime,TestNumber))");
            case 4:// clear old voa exam data, let user download new stuff
                db.beginTransaction();
                try {
                    db.execSQL("delete from test");
                    db.execSQL("delete from TestRecord where uploadFlag = 1");
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "transaction failed!");
                } finally {
                    db.endTransaction();
                }
            case 5:
            case 6:
                //evaluate  好像不能更新数据库，只能在基础上操作。
                break;
            default:
        }
    }

}
