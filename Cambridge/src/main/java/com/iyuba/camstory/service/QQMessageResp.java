package com.iyuba.camstory.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QQMessageResp {

    @SerializedName("result")
    private int result;
    @SerializedName("data")
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("editor")
        private int editor;
        @SerializedName("technician")
        private int technician;
        @SerializedName("manager")
        private int manager;

        public int getEditor() {
            return editor;
        }

        public void setEditor(int editor) {
            this.editor = editor;
        }

        public int getTechnician() {
            return technician;
        }

        public void setTechnician(int technician) {
            this.technician = technician;
        }

        public int getManager() {
            return manager;
        }

        public void setManager(int manager) {
            this.manager = manager;
        }
    }
}
