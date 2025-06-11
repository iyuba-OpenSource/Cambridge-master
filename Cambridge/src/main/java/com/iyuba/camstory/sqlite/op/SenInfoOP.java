package com.iyuba.camstory.sqlite.op;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.sqlite.DatabaseService;

import java.util.ArrayList;

public class SenInfoOP extends DatabaseService {

	/*public static final String level = "level";
    public static final String  ordernumber = "ordernumber";
	public static final String  indexnumber = "indexnumber";
	public static final String  Timing = "Timing";
	public static final String  EndTiming = "EndTiming";
	public static final String  English = "English";
	public static final String  Chinese = "Chinese";*/

    public SenInfoOP(Context mContext) {
        super();
    }

    private final VoaDetail fillIn(Cursor paramCursor) {
        VoaDetail voaDetail = new VoaDetail();
        voaDetail.level = paramCursor.getString(0);
        voaDetail.ordernumber = paramCursor.getInt(1);
        voaDetail.indexnumber = paramCursor.getInt(2);
        voaDetail.startTime = paramCursor.getFloat(3);
        voaDetail.endTime = paramCursor.getFloat(4);
        voaDetail.sentence = paramCursor.getString(5);
        voaDetail.sentence_cn = paramCursor.getString(6);
        return voaDetail;
    }

    public ArrayList<VoaDetail> findDataByAll(int paramInt1, int paramInt2) {
        try {
            ArrayList localArrayList = new ArrayList();
            SQLiteDatabase localSQLiteDatabase = mdbhelper
                    .getReadableDatabase();
            String[] arrayOfString = new String[2];
            arrayOfString[0] = "" + paramInt1;
            arrayOfString[1] = "" + paramInt2;
            Cursor localCursor = localSQLiteDatabase.query("evaluate", null,
                    "level=? and ordernumber=?", arrayOfString, null, null,
                    "indexx ASC");
            if (localCursor.getCount() == 0) {
                Log.v("findDataByAll", "localCursor为空");
                localCursor.close();
                localSQLiteDatabase.close();
                return localArrayList;
            } else if (localCursor.moveToFirst()) {
                //Log.v("findDataByAll", "localCursor不为空1");
                do {
                    //Log.v("findDataByAll", "localCursor不为空2");
                    localArrayList.add(fillIn(localCursor));

                } while (localCursor.moveToNext());
                localSQLiteDatabase.close();

                localCursor.close();
                return localArrayList;
            }

        } finally {
        }
        return null;
    }

}
