package com.iyuba.camstory.bean;

import android.text.SpannableStringBuilder;

public class VoaDetail {
	public int voaid;
	public String level;
	public int ordernumber;
	public int indexnumber;//句子index
	public String paraid = ""; // 段落ID
	public String idindex = ""; // 句子行数
	public double startTime; // 时间
	public double endTime; // 时间
	public String sentence = ""; // 句子内容
	public String imgpath = ""; // 图片
	public String sentence_cn = ""; // 中文句子内容
	public String imgwords = "";

	public boolean isRead = false;
	public SpannableStringBuilder readResult;
	private int readScore = 0;

	//评测结果
	private EvaluateResponse evaluateResponse;

	public void setReadScore(int score) {
		this.readScore = score;
	}

	public int getReadScore() {
		return readScore;
	}

	@Override
	public String toString() {
		return "VoaDetail{" +
				"voaid=" + voaid +
				", level='" + level + '\'' +
				", ordernumber=" + ordernumber +
				", indexnumber=" + indexnumber +
				", paraid='" + paraid + '\'' +
				", idindex='" + idindex + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", sentence='" + sentence + '\'' +
				", imgpath='" + imgpath + '\'' +
				", sentence_cn='" + sentence_cn + '\'' +
				", imgwords='" + imgwords + '\'' +
				", isRead=" + isRead +
				", readResult=" + readResult +
				", readScore=" + readScore +
				'}';
	}

	public int getVoaid() {
		return voaid;
	}

	public String getLevel() {
		return level;
	}

	public int getOrdernumber() {
		return ordernumber;
	}

	public int getIndexnumber() {
		return indexnumber;
	}

	public String getParaid() {
		return paraid;
	}

	public String getIdindex() {
		return idindex;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public String getSentence(){

		return sentence;
	}

	public String getImgpath() {
		return imgpath;
	}

	public String getSentence_cn() {
		return sentence_cn;
	}

	public String getImgwords() {
		return imgwords;
	}

	public boolean isRead() {
		return isRead;
	}

	public SpannableStringBuilder getReadResult() {
		return readResult;
	}

	public EvaluateResponse getEvaluateResponse() {
		return evaluateResponse;
	}

	public void setEvaluateResponse(EvaluateResponse evaluateResponse) {
		this.evaluateResponse = evaluateResponse;
	}
}
