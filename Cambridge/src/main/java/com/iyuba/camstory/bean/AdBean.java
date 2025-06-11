package com.iyuba.camstory.bean;

/**
 * Created by zhangshuai on 2018/1/12.
 */

public class AdBean {

    /**
     * result : 1
     * data : {"id":"805","adId":"嗒萌安卓系列banner","startuppic_StartDate":"2017-10-31","startuppic_EndDate":"2018-04-30","startuppic":"upload/1509437069093.png","type":"addam","startuppic_Url":"http://app."+Constant.IYBHttpHead+"/android/index.jsp","classNum":"0"}
     */

    private String result;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 805
         * adId : 嗒萌安卓系列banner
         * startuppic_StartDate : 2017-10-31
         * startuppic_EndDate : 2018-04-30
         * startuppic : upload/1509437069093.png
         * type : addam
         * startuppic_Url : http://app."+Constant.IYBHttpHead+"/android/index.jsp
         * classNum : 0
         */

        private String id;
        private String adId;
        private String startuppic_StartDate;
        private String startuppic_EndDate;
        private String startuppic;
        private String type;
        private String startuppic_Url;
        private String classNum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAdId() {
            return adId;
        }

        public void setAdId(String adId) {
            this.adId = adId;
        }

        public String getStartuppic_StartDate() {
            return startuppic_StartDate;
        }

        public void setStartuppic_StartDate(String startuppic_StartDate) {
            this.startuppic_StartDate = startuppic_StartDate;
        }

        public String getStartuppic_EndDate() {
            return startuppic_EndDate;
        }

        public void setStartuppic_EndDate(String startuppic_EndDate) {
            this.startuppic_EndDate = startuppic_EndDate;
        }

        public String getStartuppic() {
            return startuppic;
        }

        public void setStartuppic(String startuppic) {
            this.startuppic = startuppic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStartuppic_Url() {
            return startuppic_Url;
        }

        public void setStartuppic_Url(String startuppic_Url) {
            this.startuppic_Url = startuppic_Url;
        }

        public String getClassNum() {
            return classNum;
        }

        public void setClassNum(String classNum) {
            this.classNum = classNum;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", adId='" + adId + '\'' +
                    ", startuppic_StartDate='" + startuppic_StartDate + '\'' +
                    ", startuppic_EndDate='" + startuppic_EndDate + '\'' +
                    ", startuppic='" + startuppic + '\'' +
                    ", type='" + type + '\'' +
                    ", startuppic_Url='" + startuppic_Url + '\'' +
                    ", classNum='" + classNum + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AdBean{" +
                "result='" + result + '\'' +
                ", data=" + data +
                '}';
    }
}
