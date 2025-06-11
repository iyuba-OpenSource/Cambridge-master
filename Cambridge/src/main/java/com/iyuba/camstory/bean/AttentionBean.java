package com.iyuba.camstory.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yq on 2017/6/20.
 */

public class AttentionBean {

    /**
     * message : redis failure, access from database
     * result : 550
     * num : 1
     * data : [{"fusername":"kudou","vip":"0","mutual":"0","status":"0","doing":"","dateline":"1492062996","followuid":"113273"}]
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
         * fusername : kudou
         * vip : 0
         * mutual : 0
         * status : 0
         * doing :
         * dateline : 1492062996
         * followuid : 113273
         */

        private String fusername;
        private String vip;
        private String mutual;
        private String status;
        private String doing;
        private String dateline;
        private String followuid;

        public String getFusername() {
            return fusername;
        }

        public void setFusername(String fusername) {
            this.fusername = fusername;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getMutual() {
            return mutual;
        }

        public void setMutual(String mutual) {
            this.mutual = mutual;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getFollowuid() {
            return followuid;
        }

        public void setFollowuid(String followuid) {
            this.followuid = followuid;
        }
    }
}
