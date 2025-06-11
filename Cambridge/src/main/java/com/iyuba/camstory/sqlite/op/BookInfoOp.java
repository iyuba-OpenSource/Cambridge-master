package com.iyuba.camstory.sqlite.op;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.iyuba.camstory.sqlite.DatabaseService;

import java.util.ArrayList;

//该类是用于读取数据
public class BookInfoOp extends DatabaseService {

    public static final String TABLE_NAME = "bookinfo";

    public BookInfoOp(Context paramContext) {
        super();
    }

    private final BookInfo fillIn(Cursor paramCursor) {
        BookInfo localBookInfo = new BookInfo();
        localBookInfo.level = paramCursor.getInt(0);
        localBookInfo.bookorder = paramCursor.getInt(1);
        localBookInfo.bookName_cn = paramCursor.getString(2);
        localBookInfo.bookName_en = paramCursor.getString(3);
        localBookInfo.wordcount = paramCursor.getString(4);
        localBookInfo.author = paramCursor.getString(5);
        localBookInfo.interpreter = paramCursor.getString(6);
        localBookInfo.aboutAuthor = paramCursor.getString(7);
        localBookInfo.aboutinterpreter = paramCursor.getString(8);
        localBookInfo.aboutBook = paramCursor.getString(9);
        localBookInfo.chapter1_cn = paramCursor.getString(10);
        localBookInfo.chapter1_en = paramCursor.getString(11);
        localBookInfo.publicationdate = paramCursor.getString(12);
        //localBookInfo.collect = paramCursor.getInt(13);
        Log.v("BookIfo is name", localBookInfo.bookName_cn);
        return localBookInfo;

    }

    public ArrayList<BookInfo> findCollected() {
        try {
            ArrayList localArrayList = new ArrayList();
            Cursor localCursor = mdbhelper.getReadableDatabase().query(
                    "bookinfo", null, "iscollected=1", null, null, null,
                    "ordernumber ASC");
            localCursor.moveToFirst();
            while (true) {
                if (localCursor.isAfterLast()) {
                    if (localCursor != null)
                        localCursor.close();
                    closeDatabase();
                    return localArrayList;
                }
                localArrayList.add(fillIn(localCursor));
                localCursor.moveToNext();
            }
        } finally {
        }
    }

    public ArrayList<BookInfo> findDataByKey(int i) {
        Cursor localCursor = null;
        try {
            ArrayList localArrayList = new ArrayList();
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            switch (i) {
                case 0:
                case 3:
                    String[] arrayOfstr0 = new String[2];
                    arrayOfstr0[0] = "0";
                    arrayOfstr0[1] = "3";
                    localCursor = localSQLiteDatabase.query("bookinfo", null,
                            "level=? or level=?", arrayOfstr0, null, null, "level ASC");
                    break;
                case 1:
                case 4:
                    String[] arrayOfstr1 = new String[2];
                    arrayOfstr1[0] = "1";
                    arrayOfstr1[1] = "4";
                    localCursor = localSQLiteDatabase.query("bookinfo", null,
                            "level=? or level=?", arrayOfstr1, null, null, "level ASC");
                    break;
                case 2:
                case 5:
                    Log.v("BookInfo", "第2级");
                    String[] arrayOfstr2 = new String[2];
                    arrayOfstr2[0] = "2";
                    arrayOfstr2[1] = "5";
                    localCursor = localSQLiteDatabase.query("bookinfo", null,
                            "level=? or level=?", arrayOfstr2, null, null, "level ASC");
                    break;
                case 7:
                    String[] arrayOfstr3 = new String[1];
                    arrayOfstr3[0] = "7";

                    localCursor = localSQLiteDatabase.query("bookinfo", null,
                            "level=?", arrayOfstr3, null, null, "level ASC");
                    break;
                case 8:
                    String[] arrayOfstr4 = new String[1];
                    arrayOfstr4[0] = "8";

                    localCursor = localSQLiteDatabase.query("bookinfo", null,
                            "level=?", arrayOfstr4, null, null, "level ASC");
                    break;
                case 6:
                    String[] arrayOfstr6 = new String[1];
                    arrayOfstr6[0] = "6";

                    localCursor = localSQLiteDatabase.query("bookinfo", null,
                            "level=?", arrayOfstr6, null, null, "level ASC");
                    break;

                default:

            }
            if (localCursor.moveToFirst()) {
                do {
                    localArrayList.add(fillIn(localCursor));
                } while (localCursor.moveToNext());
                localCursor.close();
                return localArrayList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<BookInfo> findCollect() {
        try {
            Cursor localCursor;
            ArrayList localArrayList1 = new ArrayList();
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            String[] arrayOfstr11 = new String[1];
            arrayOfstr11[0] = "1";
            Log.v("findCollect", arrayOfstr11[0]);
            localCursor = localSQLiteDatabase.query("bookinfo", null,
                    "collect=?", arrayOfstr11, null, null, "collect ASC");
            Log.v("findCollect", "after query");
            if (localCursor.getCount() == 0) {
                Log.v("localCursor", "localCursor空");
                return localArrayList1;

            } else if (localCursor.moveToFirst()) {

                do {

                    localArrayList1.add(fillIn(localCursor));
                } while (localCursor.moveToNext());

                localCursor.close();

                return localArrayList1;
            }

        } catch (Exception e) {

            Log.e("BookInfoOp", "Exception e");

        }
        return null;

    }

    public ArrayList<BookInfo> findDownload() {
        try {
            Cursor localCursor;
            ArrayList localArrayList1 = new ArrayList();
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            String[] arrayOfstr11 = new String[1];
            arrayOfstr11[0] = "1";
            Log.v("findDownload", arrayOfstr11[0]);
            localCursor = localSQLiteDatabase.query("bookinfo", null,
                    "download=?", arrayOfstr11, null, null, "download ASC");
            Log.v("findDownload", "after query");
            if (localCursor.getCount() == 0) {
                Log.v("localCursor", "localCursor空");
                return localArrayList1;

            } else if (localCursor.moveToFirst()) {

                do {

                    localArrayList1.add(fillIn(localCursor));
                } while (localCursor.moveToNext());

                localCursor.close();

                return localArrayList1;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void setCollected(String paramInt1, String paramInt2) {
        try {
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("iscollected", "1");
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            String[] arrayOfString = new String[2];
            arrayOfString[0] = paramInt1;
            arrayOfString[1] = paramInt2;
            localSQLiteDatabase.update("bookinfo", localContentValues,
                    "level=? and ordernumber=?", arrayOfString);
            closeDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUnCollected(String paramInt1, String paramInt2) {
        try {
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("iscollected", "0");
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            String[] arrayOfString = new String[2];
            arrayOfString[0] = paramInt1;
            arrayOfString[1] = paramInt2;
            localSQLiteDatabase.update("bookinfo", localContentValues,
                    "level=? and ordernumber=?", arrayOfString);
            closeDatabase();
            // return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteDataByCollection(int ordernumber, int level) {
        if (database == null || !database.isOpen()) {
            database = mdbhelper.getReadableDatabase();
        }
        database.execSQL("update " + TABLE_NAME + " set " + "collect" + "='0' where " + "ordernumber=" + ordernumber
                + " and level=" + level);
        closeDatabase();
    }

    public synchronized void updateDataByCollection(int ordernumber, int level) {
        if (database == null || !database.isOpen()) {
            database = mdbhelper.getReadableDatabase();
        }
        database.execSQL("update " + TABLE_NAME + " set " + "collect" + "='1' where " + "ordernumber=" + ordernumber
                + " and level=" + level);
        closeDatabase();
    }

    public synchronized void deleteDataByDownload(int ordernumber, int level) {
        if (database == null || !database.isOpen()) {
            database = mdbhelper.getReadableDatabase();
        }
        database.execSQL("update " + TABLE_NAME + " set " + "download" + "='0' where " + "ordernumber=" + ordernumber
                + " and level=" + level);
        closeDatabase();
    }

    public synchronized void updateDataByDownload(int orderNumber, int level) {
        if (database == null || !database.isOpen()) {
            database = mdbhelper.getReadableDatabase();
        }
        database.execSQL("update " + TABLE_NAME + " set " + "download" + "='1' where " + "ordernumber=" + orderNumber
                + " and level=" + level);
        closeDatabase();
    }
}
