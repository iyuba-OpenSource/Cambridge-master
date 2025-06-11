package com.iyuba.camstory.sqlite.op;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Log;

import com.iyuba.camstory.bean.EvaluateResponse;
import com.iyuba.camstory.sqlite.DatabaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * 进行评测记录的操作
 */
@SuppressLint("Range")
public class EvaluateRecordOp extends DatabaseService {

    public static final String TAG = EvaluateResponse.class.getSimpleName();

    public void insert(String uid, String level, String orderNumber, String index, EvaluateResponse t) {
        if (uid == null) {
            uid = "-1";
        }
        if (database == null || !database.isOpen()) {
            database = mdbhelper.getReadableDatabase();
        }
        database.execSQL("INSERT OR REPLACE INTO" +
                " \"evaluate_record\"(\"uid\", \"level\"," +
                " \"orderNumber\", \"indexx\", \"BeginTiming\"," +
                " \"EndTiming\", \"textEN\", \"textCH\"," +
                " \"recordUrl\", \"score\", \"totalScore\"," +
                " \"url\") VALUES (?, ?, ?, ?," +
                " ?, ?, ?,?, ?, ?, ?," +
                " ?)", new String[]{uid, level, orderNumber, index, "", "", t.getSentence(),
                "", t.getLocalMP3Path(), t.getScores() + "", t.getTotal_score() + "", "http://iuserspeech.iyuba.cn:9001/voa/" + t.getURL()});
        List<EvaluateResponse.Words> words = t.getWords();
        String sql = "INSERT OR REPLACE INTO" +
                " \"main\".\"word\"(\"uid\", \"level\"," +
                " \"orderNumber\", \"indexx\", \"wordIndex\"," +
                " \"content\", \"pron\", \"pron2\"," +
                " \"user_pron\", \"user_pron2\", \"score\"," +
                " \"insert\", \"delete\", \"substitute_orgi\"," +
                " \"substitute_user\") VALUES (?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        for (EvaluateResponse.Words word : words) {
            database.execSQL(sql, new String[]{uid, level,
                    orderNumber, index, word.getIndex(),
                    word.getContent(), word.getPron(), word.getPron2(),
                    word.getUser_pron(), word.getUser_pron2(), word.getScore(),
                    word.getInsert(), word.getDelete(), word.getSubstitute_orgi(),
                    word.getSubstitute_user()});
        }
        closeDatabase();
    }

    public EvaluateResponse getEvaluateResponse(String uid, String level, String orderNumber, String index) {
        if (database == null || !database.isOpen()) {
            database = mdbhelper.getReadableDatabase();
        }
        if (uid == null) {
            uid = "-1";
        }

        String sql = "SELECT * FROM evaluate_record  WHERE uid = ? and  level = ? and  orderNumber = ? and indexx = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{uid, level, orderNumber, index});
        EvaluateResponse evaluateResponse = new EvaluateResponse();
        while (cursor.moveToNext()) {
            evaluateResponse.setScores(Integer.valueOf(cursor.getString(cursor.getColumnIndex("score"))));
            evaluateResponse.setTotal_score(Double.valueOf(cursor.getString(cursor.getColumnIndex("totalScore"))));
            evaluateResponse.setURL(cursor.getString(cursor.getColumnIndex("url")));
            evaluateResponse.setSentence(cursor.getString(cursor.getColumnIndex("textEN")));
        }
        cursor.close();
        String sql1 = "SELECT * FROM word  WHERE uid = ? and  level = ? and  orderNumber = ? and indexx = ?";
        Cursor cursor2 = database.rawQuery(sql1, new String[]{uid, level, orderNumber, index});
        List<EvaluateResponse.Words> words = new ArrayList<>();
        while (cursor2.moveToNext()) {
            EvaluateResponse.Words word = new EvaluateResponse.Words();
            word.setContent(cursor2.getString(cursor2.getColumnIndex("content")));
            word.setDelete(cursor2.getString(cursor2.getColumnIndex("pron")));
            word.setPron(cursor2.getString(cursor2.getColumnIndex("pron2")));
            word.setIndex(cursor2.getString(cursor2.getColumnIndex("wordIndex")));
            word.setScore(cursor2.getString(cursor2.getColumnIndex("score")));
            word.setPron2(cursor2.getString(cursor2.getColumnIndex("pron2")));
            word.setInsert(cursor2.getString(cursor2.getColumnIndex("insert")));
            word.setSubstitute_orgi(cursor2.getString(cursor2.getColumnIndex("substitute_orgi")));
            word.setUser_pron(cursor2.getString(cursor2.getColumnIndex("user_pron")));
            word.setUser_pron2(cursor2.getString(cursor2.getColumnIndex("user_pron2")));
            word.setSubstitute_user(cursor2.getString(cursor2.getColumnIndex("substitute_user")));
            Log.e(TAG, "getEvaluateResponse: "+word);
            words.add(word);
        }
        evaluateResponse.setWords(words);
        return evaluateResponse;
    }

}
