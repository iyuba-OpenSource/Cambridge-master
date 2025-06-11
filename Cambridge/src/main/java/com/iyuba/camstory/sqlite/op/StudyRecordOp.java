package com.iyuba.camstory.sqlite.op;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.iyuba.camstory.sqlite.DatabaseService;
import com.iyuba.camstory.sqlite.mode.StudyRecord;
import com.iyuba.camstory.utils.DateFormatUtil;

public class StudyRecordOp extends DatabaseService {
	private static final String TAG = StudyRecordOp.class.getSimpleName();
	
	private static final String TABLE_NAME = "StudyRecord";
	private static final String UID = "uid";
	private static final String BEGINTIME = "BeginTime";
	private static final String ENDTIME = "EndTime";
	private static final String LESSON = "Lesson";
	private static final String LESSONID = "LessonId";
	private static final String ENDFLG = "EndFlg";
	private static final String UPLOADFLAG = "uploadFlag";

	public StudyRecordOp() {
		super();
	}

	public void transformWithoutSub(StudyRecord records) {
		Log.e(TAG, "transformWithoutSub.BeginTime : " + records.BeginTime);
		records.BeginTime = DateFormatUtil
				.transformWithoutSub(records.BeginTime);
		records.EndTime = DateFormatUtil
				.transformWithoutSub(records.EndTime);
	}

	public void transformWithSub(StudyRecord records) {
		Log.e(TAG, "transformWithSub : " + records.BeginTime);
		records.BeginTime = DateFormatUtil
				.transformWithSub(records.BeginTime);
		records.EndTime = DateFormatUtil.transformWithSub(records.EndTime);
		
		Log.e(TAG, "transformWithSub.BeginTime" + records.BeginTime);
	}

	// 本地化TestRecord
	public void saveStudyRecord(StudyRecord records) {
		if (records != null) {
			transformWithoutSub(records);
			StudyRecord tempRecord = records;
			Cursor cursor = database.rawQuery("select * from " + TABLE_NAME
					+ " where BeginTime=" + tempRecord.BeginTime,
					new String[] {});
			int databaseHasNum = cursor.getCount();
			closeDatabase();
			if (databaseHasNum == 0) {
				database.execSQL("insert into " + TABLE_NAME + " (" + UID + ","
						+ BEGINTIME + "," + LESSON + "," + LESSONID + ","
						+ ENDTIME + "," + ENDFLG + "," + UPLOADFLAG
						+ ") values(?,?,?,?,?,?,?)", new Object[] {
						tempRecord.uid, tempRecord.BeginTime,
						tempRecord.Lesson, tempRecord.LessonId,
						tempRecord.EndTime, tempRecord.EndFlg, 0 });
				closeDatabase();
			}
			cursor.close();
			cursor = null;
		}
	}

	// 找所有没上传过的
	public ArrayList<StudyRecord> findAllStudyRecord() {
		ArrayList<StudyRecord> records = new ArrayList<StudyRecord>();
		Cursor cursor = database.query(TABLE_NAME, new String[] { UID,
				BEGINTIME, LESSON, LESSONID, ENDTIME, ENDFLG }, UPLOADFLAG
				+ "=0", null, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			StudyRecord tRecord = new StudyRecord();
			tRecord.uid = cursor.getInt(0);
			tRecord.BeginTime = cursor.getString(1);
			tRecord.Lesson = cursor.getString(2);
			tRecord.LessonId = cursor.getInt(3);
			tRecord.EndTime = cursor.getString(4);
			tRecord.EndFlg = cursor.getInt(5);
			transformWithSub(tRecord);
			records.add(tRecord);
		}
		return records;
	}

	// 标记已上传
	public void uploadSuccess(String beginTime) {
		beginTime = DateFormatUtil.transformWithoutSub(beginTime);
		ContentValues values = new ContentValues();
		values.put(UPLOADFLAG, 1);
		database.update(TABLE_NAME, values, BEGINTIME + "=?",
				new String[] { beginTime });
	}

	// 清除标记为1的记录
	public void clearStudyRecords() {
		database.delete(TABLE_NAME, UPLOADFLAG + "=1", null);
	}
}
