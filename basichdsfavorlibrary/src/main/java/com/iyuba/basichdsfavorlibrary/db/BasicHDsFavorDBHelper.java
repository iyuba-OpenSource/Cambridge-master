package com.iyuba.basichdsfavorlibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：renzhy on 17/9/20 21:21
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsFavorDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "basic_hds_favorpart.db";
    private static final int DB_VERSION = 1;

    private String Id;
    private String UserId;
    private String Type;
    private String Category;
    private String CategoryName;
    private String CreateTime;
    private String Pic;
    private String Title_cn;
    private String Title;
    private String Synflg;
    private String InsertFrom;

    private String Other1;
    private String Other2;
    private String Other3;
    private String Flag;
    private String CollectTime;
    private String Sound;
    private String Source;

    public BasicHDsFavorDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table BasicFavorPartHeadlinesList (Id integer NOT NULL,UserId integer,Type text,")
                .append("Category text,CategoryName text,CreateTime text,Pic text,")
                .append("Title_cn text,Title text,Synflg text,InsertFrom text,Other1 text,Other2 text,Other3 text,")
                .append("Flag text,CollectTime text,Sound text,Source text,PRIMARY KEY(Id,UserId,Type))");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch ( oldVersion ) {
            default:
                break;
        }
    }
}
