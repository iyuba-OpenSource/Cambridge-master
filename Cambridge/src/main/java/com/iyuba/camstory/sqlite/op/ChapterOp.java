package com.iyuba.camstory.sqlite.op;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iyuba.camstory.sqlite.DatabaseService;

import java.util.ArrayList;

public class ChapterOp extends DatabaseService {
    public static final String CHAPTERNAME = "chapter_name";
    public static final String CHAPTERORDER = "chapter_order";
    public static final String LEVEL = "level";
    public static final String ORDERNUMBER = "ordernumber";
    public static final String TABLE_NAME = "chapter";

    public ChapterOp(Context paramContext) {
        super();
    }

    private final Chapter fillIn(Cursor paramCursor) {
        Chapter localChapter = new Chapter();
        localChapter.level = paramCursor.getInt(0);
        localChapter.bookorder = paramCursor.getInt(1);
        localChapter.chapterorder = paramCursor.getInt(2);
        localChapter.chapterName = paramCursor.getString(3);
        return localChapter;
    }

    public ArrayList<Chapter> findDataByAll(int paramInt1, int paramInt2) {
        try {
            ArrayList localArrayList = new ArrayList();
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            String[] arrayOfString = new String[2];
            arrayOfString[0] = "" + paramInt1;
            arrayOfString[1] = "" + paramInt2;
            Cursor localCursor = localSQLiteDatabase.query("chapter", null,
                    "level=? and ordernumber=?", arrayOfString, null, null,
                    "chapter_order ASC");
            if (localCursor.getCount() == 0) {
                Log.v("findDataByAll", "localCursor为空");
                return localArrayList;
            } else if (localCursor.moveToFirst()) {
                //Log.v("findDataByAll", "localCursor不为空1");
                do {
                   // Log.v("findDataByAll", "localCursor不为空2");
                    localArrayList.add(fillIn(localCursor));

                } while (localCursor.moveToNext());
                localCursor.close();
                return localArrayList;
            }

        } finally {
        }
        return null;
    }
}