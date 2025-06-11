package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "collect",primaryKeys = {"uid","level","order_number","type"})
public class Collect {
    @NonNull
    private String uid;
    @NonNull
    private String level;
    @NonNull
    @ColumnInfo(name = "order_number")
    private String orderNumber;
    @NonNull
    private String type;

    public Collect(String uid, String level, String orderNumber, String type) {
        this.uid = uid;
        this.level = level;
        this.orderNumber = orderNumber;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
