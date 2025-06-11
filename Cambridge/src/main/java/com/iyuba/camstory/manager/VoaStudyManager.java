package com.iyuba.camstory.manager;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.util.Log;

import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestUpdateStudyRecord;
import com.iyuba.camstory.sqlite.mode.StudyRecord;
import com.iyuba.camstory.sqlite.op.StudyRecordOp;
import com.iyuba.camstory.sqlite.op.VoaWordOp;
import com.iyuba.voa.activity.setting.Constant;
public class VoaStudyManager {
	public static int testStatus;// 测试状态，0：只开始听；1：听力完成；2：做题完成',
	public static String currVoaId = "";// 在做哪篇的听力
	public static String startTime;
	private static VoaWordOp vwop;
	private static StudyRecordOp srop;
	public static StudyRecord sr; // 当前学习记录
	private static int lessonid;
	public static int costTime;

	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");// 设置日期格式

	public static void newRecord() {
		if (vwop == null) {
			vwop = new VoaWordOp();
		}
		if (srop == null) {
			srop = new StudyRecordOp();
		}
		sr = new StudyRecord();
		startTime = sdf.format(new Date());
		if (VoaDataManager.getInstance().voaTemp != null) {
			currVoaId = VoaDataManager.getInstance().voaTemp.voaid + "";
		}
		testStatus = 0;
		costTime = 0;
		sr_submitted = false;
	}

	public static void saveLastStudyRecord(int lid) {
		lessonid = lid;
		saveLastStudyRecord();
	}

	public static void saveLastStudyRecord() {
		Log.e("saveLastStudyRecord", "saveLastStudyRecord!!!!!!");
		if (sr_submitted == true) {
			return;
		}
		// 空，返回
		if (sr == null) {
			return;
		}
		// 记录uid
		if (AccountManager.getInstance().checkUserLogin()) {
			sr.uid = Integer.valueOf(AccountManager.getInstance().userId);
		}
		sr.BeginTime = startTime;
		sr.EndTime = sdf.format(new Date());
		if (lessonid != 0) {
			sr.LessonId = lessonid;
		} else if (VoaDataManager.getInstance().voaTemp != null) {
			sr.LessonId = VoaDataManager.getInstance().voaTemp.voaid;
		}
		lessonid = 0;
		sr.EndFlg = testStatus;
		srop.saveStudyRecord(sr);
		sr_submitted = true;
		uploadHistoryStudyRecords(AccountManager.getInstance().userId);
	}

	private static boolean sr_submitted;

	public static void uploadHistoryStudyRecords(int uid) {
		if (srop == null) {
			srop = new StudyRecordOp();
		}
		// 处理学习记录
		srop.clearStudyRecords();
		ArrayList<StudyRecord> records = srop.findAllStudyRecord();
		if (!records.isEmpty()) {
			for (Iterator iterator = records.iterator(); iterator.hasNext();) {
				StudyRecord studyRecord = (StudyRecord) iterator.next();
				if (studyRecord.uid == 0) {
					studyRecord.uid = uid;
				}
				if (studyRecord.Lesson == null || studyRecord.Lesson.equals("")) {
					studyRecord.Lesson = Constant.getAppname();
				}
			}
			Iterator iterator = records.iterator();
			uploadStudyRecord(iterator);
		}
	}

	private static void uploadStudyRecord(final Iterator iterator) {
		if (!iterator.hasNext()) {
			return;
		}
		final StudyRecord studyRecord = (StudyRecord) iterator.next();
		RequestUpdateStudyRecord request;

		try {
			request = new RequestUpdateStudyRecord(studyRecord.uid + "",
					studyRecord.BeginTime, studyRecord.EndTime, studyRecord.Lesson,
					studyRecord.LessonId, studyRecord.EndFlg, new RequestCallBack() {

						@Override
						public void requestResult(Request result) {
							RequestUpdateStudyRecord response = (RequestUpdateStudyRecord) result;
							if (response.isRequestSuccessful()) {
								srop.uploadSuccess(studyRecord.BeginTime);
								uploadStudyRecord(iterator);
								Log.e("uploadStudyRecord", "success");
							} else {
								Log.e("uploadStudyRecord", "测试记录同步失败");
							}
						}
					});
			CrashApplication.getInstance().getQueue().add(request);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
