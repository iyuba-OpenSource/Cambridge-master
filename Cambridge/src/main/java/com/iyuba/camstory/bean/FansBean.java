package com.iyuba.camstory.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yq on 2017/6/20.
 */

public class FansBean {
    /**
     * message : redis failure, access from database
     * result : 560
     * num : 1
     * data : [{"uid":"2561832","vip":"10","username":"liuzhenli","mutual":"0","doing":"","dateline":"1497930410"}]
     */

    private String message;
    private int result;
    private int num;
    private ArrayList<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 2561832
         * vip : 10
         * username : liuzhenli
         * mutual : 0
         * doing :
         * dateline : 1497930410
         */

        private String uid;
        private String vip;
        private String username;
        private String mutual;
        private String doing;
        private String dateline;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMutual() {
            return mutual;
        }

        public void setMutual(String mutual) {
            this.mutual = mutual;
        }

        public String getDoing() {
            return doing;
        }

        public void setDoing(String doing) {
            this.doing = doing;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }
    }
}
