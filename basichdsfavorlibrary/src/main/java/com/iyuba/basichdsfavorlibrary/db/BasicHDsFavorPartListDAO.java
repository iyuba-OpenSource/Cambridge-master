package com.iyuba.basichdsfavorlibrary.db;

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
public class BasicHDsFavorPartListDAO implements IBasicHDsFavorPartListDAO{
    private BasicHDsFavorDBHelper dbHelper;
    private static final String TABLE_BASIC_HDS_FAVOR_PART_LIST = "BasicFavorPartHeadlinesList";

    private static final String ID = "Id";
    private static final String USERID = "UserId";
    private static final String TYPE = "Type";
    private static final String CATEGORY = "Category";
    private static final String CATEGORYNAME = "CategoryName";
    private static final String CREATETIME = "CreateTime";
    private static final String PIC = "Pic";
    private static final String TITLE_CN = "Title_cn";
    private static final String TITLE = "Title";
    private static final String SYNFLG = "Synflg";
    private static final String INSERTFROM = "InsertFrom";
    private static final String OTHER1 = "Other1";
    private static final String OTHER2 = "Other2";
    private static final String OTHER3 = "Other3";
    private static final String FLAG = "Flag";
    private static final String COLLECTTIME = "CollectTime";
    private static final String SOUND = "Sound";
    private static final String SOURCE = "Source";

    public BasicHDsFavorPartListDAO(Context context){
        dbHelper = new BasicHDsFavorDBHelper(context);
    }

    public void insertOrReplaceFavorPart(BasicHDsFavorPart basicFavorPart){
        if (basicFavorPart != null&&basicFavorPart.getId()!=null&&!"".equals(basicFavorPart.getId())) {
            String sqlString="insert or replace into " + TABLE_BASIC_HDS_FAVOR_PART_LIST + " (" + ID + ","
                    + USERID + "," + TYPE + "," + CATEGORY + "," + CATEGORYNAME + ","
                    + CREATETIME + ","+ PIC + "," + TITLE_CN + "," + TITLE + "," + SYNFLG + ","
                    + INSERTFROM + "," + OTHER1+ "," + OTHER2 + "," + OTHER3 + ","
                    + FLAG + "," + COLLECTTIME + "," + SOUND + ","+ SOURCE  +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Object[] objects = new Object[]{basicFavorPart.getId(),basicFavorPart.getUserId(),basicFavorPart.getType(),
                    basicFavorPart.getCategory(),basicFavorPart.getCategoryName(),basicFavorPart.getCreateTime(),
                    basicFavorPart.getPic(),basicFavorPart.getTitle_cn(),basicFavorPart.getTitle(),basicFavorPart.getSynflg(),
                    basicFavorPart.getInsertFrom(),basicFavorPart.getOther1(),basicFavorPart.getOther2(),basicFavorPart.getOther3(),
                    basicFavorPart.getFlag(),basicFavorPart.getCollectTime(),basicFavorPart.getSound(),basicFavorPart.getSource()};
            Log.e("===insertOrReplace",basicFavorPart.getOther1() + "??");

            dbHelper.getWritableDatabase().execSQL(sqlString,objects);

            dbHelper.getWritableDatabase().close();
        }
    }

    @Override
    public void insertBasicHDsFavorPart(BasicHDsFavorPart basicFavorPart) {
        if(basicFavorPart != null&&basicFavorPart.getId()!=null&&!"".equals(basicFavorPart.getId())) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ID,basicFavorPart.getId());
            values.put(USERID,basicFavorPart.getUserId());
            values.put(TYPE,basicFavorPart.getType());
            values.put(CATEGORY,basicFavorPart.getCategory());
            values.put(CATEGORYNAME,basicFavorPart.getCategoryName());
            values.put(CREATETIME,basicFavorPart.getCreateTime());
            values.put(PIC,basicFavorPart.getPic());
            values.put(TITLE_CN,basicFavorPart.getTitle_cn());
            values.put(TITLE,basicFavorPart.getTitle());
            values.put(SYNFLG,basicFavorPart.getInsertFrom());
            values.put(INSERTFROM,basicFavorPart.getInsertFrom());
            values.put(OTHER1,basicFavorPart.getOther1());
            values.put(OTHER2,basicFavorPart.getOther2());
            values.put(OTHER3,basicFavorPart.getOther3());
            values.put(FLAG,basicFavorPart.getFlag());
            values.put(COLLECTTIME,basicFavorPart.getCollectTime());
            values.put(SOUND,basicFavorPart.getSound());
            values.put(SOURCE,basicFavorPart.getSource());
            db.insert(TABLE_BASIC_HDS_FAVOR_PART_LIST,null,values);
            db.close();
        }
    }

    @Override
    public void insertBasicHDsFavorPart(List<BasicHDsFavorPart> basicFavorPartList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Iterator<BasicHDsFavorPart> it = basicFavorPartList.iterator();
            while (it.hasNext()){
                BasicHDsFavorPart basicFavorPart = it.next();
                if(basicFavorPart != null&&basicFavorPart.getId()!=null&&!"".equals(basicFavorPart.getId())) {
                    ContentValues values = new ContentValues();
                    values.put(ID,basicFavorPart.getId());
                    values.put(USERID,basicFavorPart.getUserId());
                    values.put(TYPE,basicFavorPart.getType());
                    values.put(CATEGORY,basicFavorPart.getCategory());
                    values.put(CATEGORYNAME,basicFavorPart.getCategoryName());
                    values.put(CREATETIME,basicFavorPart.getCreateTime());
                    values.put(PIC,basicFavorPart.getPic());
                    values.put(TITLE_CN,basicFavorPart.getTitle_cn());
                    values.put(TITLE,basicFavorPart.getTitle());
                    values.put(SYNFLG,basicFavorPart.getSynflg());
                    values.put(INSERTFROM,basicFavorPart.getInsertFrom());
                    values.put(OTHER1,basicFavorPart.getOther1());
                    values.put(OTHER2,basicFavorPart.getOther2());
                    values.put(OTHER3,basicFavorPart.getOther3());
                    values.put(FLAG,basicFavorPart.getFlag());
                    values.put(COLLECTTIME,basicFavorPart.getCollectTime());
                    values.put(SOUND,basicFavorPart.getSound());
                    values.put(SOURCE,basicFavorPart.getSource());
                    db.replace(TABLE_BASIC_HDS_FAVOR_PART_LIST,null,values);
                }
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
    public void deleteBasicHDsFavorPart(String iyubaId, String userId,String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_BASIC_HDS_FAVOR_PART_LIST, ID + " = ? and " + USERID + " = ? and " + TYPE + " = ?",
                new String[]{iyubaId,userId,type});
        Log.e("tag--底层数据库","删除了！");
        db.close();
    }

    @Override
    public void updateBasicHDsFavorPart(BasicHDsFavorPart basicFavorPart) {
        if(basicFavorPart!=null&&basicFavorPart.getId()!=null&&!"".equals(basicFavorPart.getId())){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ID,basicFavorPart.getId());
            values.put(USERID,basicFavorPart.getUserId());
            values.put(TYPE,basicFavorPart.getType());
            values.put(CATEGORY,basicFavorPart.getCategory());
            values.put(CATEGORYNAME,basicFavorPart.getCategoryName());
            values.put(CREATETIME,basicFavorPart.getCreateTime());
            values.put(PIC,basicFavorPart.getPic());
            values.put(TITLE_CN,basicFavorPart.getTitle_cn());
            values.put(TITLE,basicFavorPart.getTitle());
            values.put(SYNFLG,basicFavorPart.getSynflg());
            values.put(INSERTFROM,basicFavorPart.getInsertFrom());
            values.put(OTHER1,basicFavorPart.getOther1());
            Log.e("===OTHER1=UPDATE==", basicFavorPart.getOther1());
            values.put(OTHER2,basicFavorPart.getOther2());
            values.put(OTHER3,basicFavorPart.getOther3());
            values.put(FLAG,basicFavorPart.getFlag());
            values.put(COLLECTTIME,basicFavorPart.getCollectTime());
            values.put(SOUND,basicFavorPart.getSound());
            values.put(SOURCE,basicFavorPart.getSource());
            db.update(TABLE_BASIC_HDS_FAVOR_PART_LIST,values,ID + " = ? and " + USERID + " = ? and " + TYPE + " = ?",
                    new String[]{basicFavorPart.getId(),basicFavorPart.getUserId(),basicFavorPart.getType()});
            db.close();
        }
    }

    @Override
    public BasicHDsFavorPart queryBasicHDsFavorPart(String iyubaId, String userId,String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        BasicHDsFavorPart basicHDsFavorPart = null;
        Cursor cursor = db.query(TABLE_BASIC_HDS_FAVOR_PART_LIST,new String[]{ID,USERID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                        PIC,TITLE_CN,TITLE,SYNFLG,INSERTFROM,OTHER1,OTHER2,OTHER3,FLAG,COLLECTTIME,SOUND,SOURCE},
                ID + " = ? and "+ USERID + " = ? and " + TYPE + " = ?",
                new String[]{iyubaId,userId,type},null,null,"1");
        if(cursor.moveToFirst()){
            basicHDsFavorPart = new BasicHDsFavorPart();
            basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
            basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
            basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
            basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
            basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
            basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
            basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
            basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
            basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
            basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
            basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
            basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
            basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
            basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
            basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
            basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
            basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
        }
        cursor.close();
        db.close();
        return basicHDsFavorPart;
    }

    @Override
    public List<BasicHDsFavorPart> queryAllBasicHDsFavorPart(String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_FAVOR_PART_LIST,new String[]{ID,USERID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                PIC,TITLE_CN,TITLE,SYNFLG,INSERTFROM,OTHER1,OTHER2,OTHER3,FLAG,COLLECTTIME,SOUND,SOURCE},USERID + " = ?", new String[]{userId},null,null,null);

        if(cursor.moveToFirst()){
            do{
                BasicHDsFavorPart basicHDsFavorPart;
                basicHDsFavorPart = new BasicHDsFavorPart();
                basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
                basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
                basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
                basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
                basicHDsFavorPartList.add(basicHDsFavorPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryAllBasicHDsFavorPart(String userId, String flag) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_FAVOR_PART_LIST,new String[]{ID,USERID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                PIC,TITLE_CN,TITLE,SYNFLG,INSERTFROM,OTHER1,OTHER2,OTHER3,FLAG,COLLECTTIME,SOUND,SOURCE},USERID + " = ? and Flag = ?", new String[]{userId,flag},null,null,null);

        if(cursor.moveToFirst()){
            do{
                BasicHDsFavorPart basicHDsFavorPart;
                basicHDsFavorPart = new BasicHDsFavorPart();
                basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
                basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
                basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
                basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
                basicHDsFavorPartList.add(basicHDsFavorPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryAllSyznBasicHDsFavorPart(String userId, String Synflg) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_FAVOR_PART_LIST,new String[]{ID,USERID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                PIC,TITLE_CN,TITLE,SYNFLG,INSERTFROM,OTHER1,OTHER2,OTHER3,FLAG,COLLECTTIME,SOUND,SOURCE},USERID + " = ? and Synflg = ?", new String[]{userId,Synflg},null,null,null);

        if(cursor.moveToFirst()){
            do{
                BasicHDsFavorPart basicHDsFavorPart;
                basicHDsFavorPart = new BasicHDsFavorPart();
                basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
                basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
                basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
                basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
                basicHDsFavorPartList.add(basicHDsFavorPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByCategory(String userId,String category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_FAVOR_PART_LIST,new String[]{ID,USERID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                        PIC,TITLE_CN,TITLE,SYNFLG,INSERTFROM,OTHER1,OTHER2,OTHER3,FLAG,COLLECTTIME,SOUND,SOURCE},
                USERID + " = ? and " + CATEGORY + " = ? ",
                new String[]{userId,category},null,null,null);
        if(cursor.moveToFirst()){
            do{
                BasicHDsFavorPart basicHDsFavorPart;
                basicHDsFavorPart = new BasicHDsFavorPart();
                basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
                basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
                basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
                basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
                basicHDsFavorPartList.add(basicHDsFavorPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByType(String userId,String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BASIC_HDS_FAVOR_PART_LIST,new String[]{ID,USERID,TYPE,CATEGORY,CATEGORYNAME,CREATETIME,
                        PIC,TITLE_CN,TITLE,SYNFLG,INSERTFROM,OTHER1,OTHER2,OTHER3,FLAG,COLLECTTIME,SOUND,SOURCE},
                USERID + " = ? and " + TYPE + " = ? ",
                new String[]{userId,type},null,null,null);
        if(cursor.moveToFirst()){
            do{
                BasicHDsFavorPart basicHDsFavorPart;
                basicHDsFavorPart = new BasicHDsFavorPart();
                basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
                basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
                basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
                basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
                basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
                basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
                basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
                basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
                basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
                basicHDsFavorPartList.add(basicHDsFavorPart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPage(String userId,int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_FAVOR_PART_LIST + " where " + USERID +
                " = ? order by " + COLLECTTIME + " desc, " + ID + " desc"
                +" Limit ? Offset ?",new String[]{String.valueOf(userId),String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsFavorPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsFavorPartList.size() != 0){
            return basicHDsFavorPartList;
        }
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPage(String userId, String flag, int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_FAVOR_PART_LIST + " where " + USERID +
                " = ? and "+FLAG+" = ? order by " + COLLECTTIME + " desc, " + ID + " desc"
                +" Limit ? Offset ?",new String[]{String.valueOf(userId),flag,String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsFavorPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsFavorPartList.size() != 0){
            return basicHDsFavorPartList;
        }
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPageAndCate(String userId,String category, int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_FAVOR_PART_LIST + " where " + USERID + " = ? and "
                + CATEGORY + " = ? order by " + CREATETIME + " desc, " + ID + " desc"
                +" Limit ? Offset ?",new String[]{String.valueOf(userId),String.valueOf(category),
                String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsFavorPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsFavorPartList.size() != 0){
            return basicHDsFavorPartList;
        }
        return basicHDsFavorPartList;
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPageAndType(String userId,String type, int count, int offset) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_BASIC_HDS_FAVOR_PART_LIST + " where " + USERID + " = ? and "
                + TYPE + " = ? order by " + CREATETIME + " desc, " + ID + " desc"
                +" Limit ? Offset ?",new String[]{String.valueOf(userId),String.valueOf(type),
                String.valueOf(count),String.valueOf(offset)});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            basicHDsFavorPartList.add(fillIn(cursor));
        }
        if(cursor != null){
            cursor.close();
        }
        db.close();
        if(basicHDsFavorPartList.size() != 0){
            return basicHDsFavorPartList;
        }
        return basicHDsFavorPartList;
    }

    private BasicHDsFavorPart fillIn(Cursor cursor) {
        BasicHDsFavorPart basicHDsFavorPart = new BasicHDsFavorPart();
        basicHDsFavorPart.setId(cursor.getString(cursor.getColumnIndex(ID)));
        basicHDsFavorPart.setUserId(cursor.getString(cursor.getColumnIndex(USERID)));
        basicHDsFavorPart.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
        basicHDsFavorPart.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
        basicHDsFavorPart.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
        basicHDsFavorPart.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
        basicHDsFavorPart.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
        basicHDsFavorPart.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLE_CN)));
        basicHDsFavorPart.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        basicHDsFavorPart.setSynflg(cursor.getString(cursor.getColumnIndex(SYNFLG)));
        basicHDsFavorPart.setInsertFrom(cursor.getString(cursor.getColumnIndex(INSERTFROM)));
        basicHDsFavorPart.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
        basicHDsFavorPart.setOther2(cursor.getString(cursor.getColumnIndex(OTHER2)));
        basicHDsFavorPart.setOther3(cursor.getString(cursor.getColumnIndex(OTHER3)));
        basicHDsFavorPart.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
        basicHDsFavorPart.setCollectTime(cursor.getString(cursor.getColumnIndex(COLLECTTIME)));
        basicHDsFavorPart.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
        basicHDsFavorPart.setSource(cursor.getString(cursor.getColumnIndex(SOURCE)));
        return basicHDsFavorPart;
    }
}
