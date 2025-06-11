package com.iyuba.basichdsdllibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：renzhy on 17/9/20 21:21
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsDLDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "basic_hds_dlpart.db";
    private static final int DB_VERSION = 2;

    private String Id;
    private String Type;
    private String Category;
    private String CategoryName;
    private String CreateTime;
    private String Pic;
    private String Title_cn;
    private String Title;
    private String InsertFrom;

    private String Other1;
    private String Other2;
    private String Other3;

    public BasicHDsDLDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table BasicDLPartHeadlinesList (Id integer,Type text,")
                .append("Category text,CategoryName text,CreateTime text,Pic text,")
                .append("Title_cn text,Title text,InsertFrom text,Other1 text,Other2 text,Other3 text,Downtag text,PRIMARY KEY(Id,Type))");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder();
        switch (newVersion){
            case 2:
                sql.append("create table if not exists BasicDLPartHeadlinesList (Id integer,Type text,")
                        .append("Category text,CategoryName text,CreateTime text,Pic text,")
                        .append("Title_cn text,Title text,InsertFrom text,Other1 text,Other2 text,Other3 text,PRIMARY KEY(Id,Type))");
                sqLiteDatabase.execSQL(sql.toString());
                sql.setLength(0);
                sql.append("alter table BasicDLPartHeadlinesList add Downtag text default \"\"");
                sqLiteDatabase.execSQL(sql.toString());
                sql.setLength(0);
                sql.append("update BasicDLPartHeadlinesList set Downtag = (Type||Id) where Id in (select Id from BasicDLPartHeadlinesList)");
                sqLiteDatabase.execSQL(sql.toString());
            default:
                break;
        }


    }



}
