package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sound_record")
public class SoundRecord {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;
    private String uid;
    private String voaId;

    private String type;
    private Integer currentPosition;//本条记录听到的音频位置（据此判断听到了哪一句）
    private String beginTime;
    private String endTime;

    public SoundRecord(@NonNull Integer id, String uid, String voaId, String type, Integer currentPosition, String beginTime, String endTime) {
        this.id = id;
        this.uid = uid;
        this.voaId = voaId;
        this.type = type;
        this.currentPosition = currentPosition;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVoaId() {
        return voaId;
    }

    public void setVoaId(String voaId) {
        this.voaId = voaId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SoundRecord{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", voaId='" + voaId + '\'' +
                ", type='" + type + '\'' +
                ", currentPosition=" + currentPosition +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
