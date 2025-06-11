package com.iyuba.camstory.sqlite.op;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.sqlite.DatabaseService;

/**
 * 获取文章内容数据库
 * 
 * @author ct
 * @time 12.9.27
 * 
 */
public class VoaDetailOp extends DatabaseService {
	public static final String TABLE_NAME = "voadetail";
	public static final String VOAID = "voaid";
	public static final String PARAID = "paraid";
	public static final String IDINDEX = "idindex";
	public static final String STARTTIME = "starttime";
	public static final String ENDTIME = "endtime";
	public static final String SECTENCE = "sentence";
	public static final String IMGPATH = "imgpath";
	public static final String SENTENCECN = "sentence_cn";

	public VoaDetailOp() {
		super();
		// TODO Auto-generated constructor stub
		// CreateTabSql();
	}

	public void CreateTabSql() {
		// TODO Auto-generated method stub
		closeDatabase();
	}

	/**
	 * 批量插入数据
	 */
	public synchronized void saveData(List<VoaDetail> voaDetails) {
		if (voaDetails != null && voaDetails.size() != 0) {
			List<VoaDetail> list = findDataByVoaId(voaDetails.get(0).voaid);
			if (list.size() == voaDetails.size()) {
				return ;
			}
			else if (list.size() > 0) {
				deleteDetail(voaDetails.get(0).voaid);
			}
			database.beginTransaction();
			for (int i = 0; i < voaDetails.size(); i++) {
				VoaDetail tempVoaDetail = voaDetails.get(i);
				database.execSQL(
						"insert into " + TABLE_NAME + " (" + VOAID + ","
								+ PARAID + "," + IDINDEX + "," + STARTTIME
								+ "," + SECTENCE + "," + IMGPATH + ","
								+ SENTENCECN + "," + ENDTIME
								+ ") values(?,?,?,?,?,?,?,?)",
						new Object[] { tempVoaDetail.voaid,
								tempVoaDetail.paraid, tempVoaDetail.idindex,
								tempVoaDetail.startTime,
								tempVoaDetail.sentence, tempVoaDetail.imgpath,
								tempVoaDetail.sentence_cn,
								tempVoaDetail.endTime });

					}
			database.setTransactionSuccessful();
			database.endTransaction();
		closeDatabase();
		}
	}
	
	public synchronized void deleteDetail(int voaid){
		database.delete(TABLE_NAME, VOAID+"=?", new String[]{voaid+""});
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public  synchronized List<VoaDetail> findDataByVoaId(int Voaid) {
		List<VoaDetail> voaDetails = new ArrayList<VoaDetail>();
		VoaDetail voaDetail;
		Cursor cursor = database.rawQuery(
				"select " + VOAID + "," + PARAID + ", " + IDINDEX + ", "
						+ STARTTIME + ", " + SECTENCE + ", " + IMGPATH + ","
						+ SENTENCECN + "," + ENDTIME + " from " + TABLE_NAME
						+ " where " + VOAID + "=? ORDER BY " + STARTTIME
						+ " ASC", new String[] { String.valueOf(Voaid) });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			voaDetail = new VoaDetail();
			voaDetail.voaid = cursor.getInt(0);
			voaDetail.paraid = cursor.getString(1);
			voaDetail.idindex = cursor.getString(2);
			voaDetail.startTime = cursor.getDouble(3);
			voaDetail.sentence = cursor.getString(4);
			voaDetail.imgpath = cursor.getString(5);
			voaDetail.sentence_cn = cursor.getString(6);
			voaDetail.endTime = cursor.getDouble(7);
			voaDetails.add(voaDetail);
		}
		if (cursor!=null) {
			cursor.close();
		}
		closeDatabase();
		return voaDetails;
	}

}
