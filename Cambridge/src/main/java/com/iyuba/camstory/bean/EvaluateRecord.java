package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "evaluate_record",primaryKeys = {"uid","voaId","indexX"})
public class EvaluateRecord {
    @NonNull
    private String uid;
    @NonNull
    private String voaId;
    private String paraId;
    private String level;
    private String orderNumber;
    private String chapterOrder;
    @NonNull
    private String indexX;
    private String type;
    private String recordUrl;
    private String score;
    private String totalScore;
    private String url;

    public EvaluateRecord(@NonNull String uid, @NonNull String voaId, @NonNull String paraId, String level, String orderNumber, String chapterOrder, @NonNull String indexX, String type, String recordUrl, String score, String totalScore, String url) {
        this.uid = uid;
        this.voaId = voaId;
        this.paraId = paraId;
        this.level = level;
        this.orderNumber = orderNumber;
        this.chapterOrder = chapterOrder;
        this.indexX = indexX;
        this.type = type;
        this.recordUrl = recordUrl;
        this.score = score;
        this.totalScore = totalScore;
        this.url = url;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getVoaId() {
        return voaId;
    }

    public void setVoaId(@NonNull String voaId) {
        this.voaId = voaId;
    }

    @NonNull
    public String getParaId() {
        return paraId;
    }

    public void setParaId(@NonNull String paraId) {
        this.paraId = paraId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(String chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    @NonNull
    public String getIndexX() {
        return indexX;
    }

    public void setIndexX(@NonNull String indexX) {
        this.indexX = indexX;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
