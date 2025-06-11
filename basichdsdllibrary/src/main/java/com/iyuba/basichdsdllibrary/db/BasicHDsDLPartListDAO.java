package com.iyuba.basichdsdllibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 作者：renzhy on 17/9/20 21:18
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsDLPartListDAO implements IBasicHDsDLPartListDAO{
    private BasicHDsDLDBHelper dbHelper;
    private static final String TABLE_BASIC_HDS_PART_LIST = "BasicDLPartHeadlinesList";
    private static final String ID = "Id";
    private static final String TYPE = "Type";
    private static final String CATEGORY = "Category";
    private static final String CATEGORYNAME = "CategoryName";
    private static final String CREATETIME = "CreateTime";
    private static final String PIC = "Pic";
    private static final String TITLE_CN = "Title_cn";
    private static final String TITLE = "Title";
    private static final String INSERTFROM = "InsertFrom";
    private static final String OTHER1 = "Other1";
    private static final String OTHER2 = "Other2";
    private static final String OTHER3 = "Other3";
    private static final String DOWNTAG = "Downtag";

    public BasicHDsDLPartListDAO(Context context){
        dbHelper = new BasicHDsDLDBHelper(context);
    }

    @Override
    public void insertBasicHDsDLPart(BasicHDsDLPart basicDLPart) {
        if(basicDLPart == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,basicDLPart.getId());
        values.put(TYPE,basicDLPart.getType());
        values.put(CATEGORY,basicDLPart.getCategory());
        values.put(CATEGORYNAME,basicDLPart.getCategoryName());
        values.put(CREATETIME,basicDLPart.getCreateTime());
        values.put(PIC,basicDLPart.getPic());
        values.put(TITLE_CN,basicDLPart.getTitle_cn());
        values.put(TITLE,basicDLPart.getTitle());
        values.put(INSERTFROM,basicDLPart.getInsertFrom());
        values.put(OTHER1,basicDLPart.getOther1());
        values.put(OTHER2,basicDLPart.getOther2());
        values.put(OTHER3,basicDLPart.getOther3());
        values.put(DOWNTAG,basicDLPart.getDowntag());
        db.replace(TABLE_BASIC_HDS_PART_LIST,null,values);
        db.close();
    }

    @Override
    public void insertBasicHDsDLPart(List<BasicHDsDLPart> basicDLPartList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            Iterator<BasicHDsDLPart> it = basicDLPartList.iterator();
            while (it.hasNext()){
                BasicHDsDLPart basicDLPart = it.next();
                ContentValues values = new ContentValues();
                values.put(ID,basicDLPart.getId());
                values.put(TYPE,basicDLPart.getType());
                values.put(CATEGORY,basicDLPart.getCategory());
                values.put(CATEGORYNAME,basicDLPart.getCategoryName());
                values.put(CREATETIME,basicDLPart.getCreateTime());
                values.put(PIC,basicDLPart.getPic());
                values.put(TITLE_CN,basicDLPart.getTitle_cn());
                values.put(TITLE,basicDLPart.getTitle());
                values.put(INSERTFROM,basicDLPart.getInsertFrom());
                values.put(OTHER1,basicDLPart.getOther1());
                values.put(OTHER2,basicDLPart.getOther2());
                values.put(OTHER3,basicDLPart.getOther3());
                values.put(DOWNTAG,basicDLPart.getDowntag());
                db.replace(TABLE_BASIC_HDS_PART_LIST,null,values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void deleteBasicHDsDLPart(String iyubaId, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_BASIC_HDS_PART_LIST, ID + " = ? and " + TYPE + " = ?",
                new String[]{iyubaId,type});
        db.close();
    }

    @Override
    public void updateBasicHDsDLPart(String iyubaId, String type, BasicHDsDLPart basicDLPart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,basicDLPart.getId());
        values.put(TYPE,basicDLPart.getType());
        values.put(CATEGORY,basicDLPart.getCategory());
        values.put(CATEGORYNAME,basicDLPart.getCategoryName());
        values.put(CREATETIME,basicDLPart.getCreateTime());
        values.put(PIC,basicDLPart.getPic());
        values.put(TITLE_CN,basicDLPart.getTitle_cn());
        values.put(TITLE,basicDLPart.getTitle());
        values.put(INSERTFROM,basicDLPart.getInsertFrom());
        values.put(OTHER1,basicDLPart.getOther1());
        values.put(OTHER2,basicDLPart.getOther2());
        values.put(OTHER3,basicDLPart.getOther3());
        values.put(DOWNTAG,basicDLPart.getDowntag());
        db.update(TABLE_BASIC_HDS_PART_LIST,values,ID + " = ? and " + TYPE + " = ?",
                new String[]{iyubaId,type});
        db.close();
    }

    @Override
    public BasicHDsDLPart queryBasicHDsDLPart(String iyubaId, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        BasicHDsDLPart basicHDsDLPart = null;
        Cursor cursor = db.query(TABLE_BASIC_HDS_PART_LIST,new String[]{ID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
            PIC,TITLE_CN,TITLE,INSERTFROM,OTHER1,OTHER2,OTHER3,DOWNTAG},ID + " = ? and " + TYPE + " = ?",
                new String[]{iyubaId,type},null,null,"1");
        if(cursor.moveToFirst()){
            basicHDsDLPart = new BasicHDsDLPart();
            basicHDsDLPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
            basicHDsDLPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
            basicHDsDLPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
            basicHDsDLPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
            basicHDsDLPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
            basicHDsDLPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
            basicHDsDLPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
            basicHDsDLPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            basicHDsDLPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
            basicHDsDLPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
            basicHDsDLPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
            basicHDsDLPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
            basicHDsDLPart.setDowntag(cursor.getString(cursor.getColumnIndex(DOWNTAG)));
            Log.e("basdi",basicHDsDLPart.toString());
        }
        cursor.close();
        db.close();
        return basicHDsDLPart;
    }

    @Override
    public List<BasicHDsDLPart> queryAllBasicHDsDLPart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsDLPart> basicHDsDLPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_PART_LIST,new String[]{ID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                        PIC,TITLE_CN,TITLE,INSERTFROM,OTHER1,OTHER2,OTHER3,DOWNTAG},null, null,null,null,null);

        if(cursor.moveToFirst()){
            do{
                BasicHDsDLPart basicHDsDLPart;
                basicHDsDLPart = new BasicHDsDLPart();
                basicHDsDLPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsDLPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsDLPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsDLPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsDLPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsDLPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsDLPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsDLPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsDLPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsDLPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsDLPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsDLPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsDLPart.setDowntag(cursor.getString(cursor.getColumnIndex(DOWNTAG)));
                Log.e("basdi",basicHDsDLPart.toString());
                basicHDsDLPartList.add(basicHDsDLPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsDLPartList;
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByCategory(String category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsDLPart> basicHDsDLPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_PART_LIST,new String[]{ID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                        PIC,TITLE_CN,TITLE,INSERTFROM,OTHER1,OTHER2,OTHER3,DOWNTAG},CATEGORY + " = ? ",
                new String[]{category},null,null,null);
        if(cursor.moveToFirst()){
            do{
                BasicHDsDLPart basicHDsDLPart;
                basicHDsDLPart = new BasicHDsDLPart();
                basicHDsDLPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsDLPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsDLPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsDLPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsDLPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsDLPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsDLPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsDLPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsDLPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsDLPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsDLPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsDLPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsDLPart.setDowntag(cursor.getString(cursor.getColumnIndex(DOWNTAG)));
                Log.e("basdi",basicHDsDLPart.toString());
                basicHDsDLPartList.add(basicHDsDLPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsDLPartList;
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByType(String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsDLPart> basicHDsDLPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_PART_LIST,new String[]{ID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                        PIC,TITLE_CN,TITLE,INSERTFROM,OTHER1,OTHER2,OTHER3,DOWNTAG},TYPE + " = ? ",
                new String[]{type},null,null,null);
        if(cursor.moveToFirst()){
            do{
                BasicHDsDLPart basicHDsDLPart;
                basicHDsDLPart = new BasicHDsDLPart();
                basicHDsDLPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsDLPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsDLPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsDLPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsDLPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsDLPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsDLPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsDLPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsDLPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsDLPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsDLPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsDLPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsDLPart.setDowntag(cursor.getString(cursor.getColumnIndex(DOWNTAG)));
                Log.e("basdi",basicHDsDLPart.toString());
                basicHDsDLPartList.add(basicHDsDLPart);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsDLPartList;
    }

    @Override
    public List<BasicHDsDLPart> queryByPage(int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsDLPart> basicHDsDLPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_PART_LIST +
            " order by " + CREATETIME + " desc"
            +" Limit ? Offset ?",new String[]{String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsDLPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsDLPartList.size() != 0){
            return basicHDsDLPartList;
        }
        return basicHDsDLPartList;
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByPageAndCate(String category, int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsDLPart> basicHDsDLPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_PART_LIST + " where " + CATEGORY +
                " = ? order by " + CREATETIME + " desc, " + ID + " desc"
                +" Limit ? Offset ?",new String[]{String.valueOf(category),
                String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsDLPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsDLPartList.size() != 0){
            return basicHDsDLPartList;
        }
        return basicHDsDLPartList;
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByPageAndType(String type, int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsDLPart> basicHDsDLPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_PART_LIST + " where " + TYPE +
                " = ? order by " + CREATETIME + " desc, " + ID + " desc"
                +" Limit ? Offset ?",new String[]{String.valueOf(type),
                String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsDLPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsDLPartList.size() != 0){
            return basicHDsDLPartList;
        }
        return basicHDsDLPartList;
    }

    private BasicHDsDLPart fillIn(Cursor cursor) {
        BasicHDsDLPart basicHDsDLPart = new BasicHDsDLPart();
        basicHDsDLPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
        basicHDsDLPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
        basicHDsDLPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
        basicHDsDLPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
        basicHDsDLPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
        basicHDsDLPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
        basicHDsDLPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
        basicHDsDLPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        basicHDsDLPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
        basicHDsDLPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
        basicHDsDLPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
        basicHDsDLPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
        basicHDsDLPart.setDowntag(cursor.getString(cursor.getColumnIndex(DOWNTAG)));
        Log.e("basdi",basicHDsDLPart.toString());
        return basicHDsDLPart;
    }
}
