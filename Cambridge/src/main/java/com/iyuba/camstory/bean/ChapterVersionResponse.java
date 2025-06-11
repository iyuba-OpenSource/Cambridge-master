package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

public class ChapterVersionResponse {

    private int result;
    private List<ChapterVersion> data;
    private String message;

    public void setResult(int result) {
        this.result = result;
    }
    public int getResult() {
        return result;
    }

    public void setData(List<ChapterVersion> data) {
        this.data = data;
    }
    public List<ChapterVersion> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }


    /**
     * 章节内句子版本号，根据此服务端返回版本控制本地句子是否更新
     */
    @Entity(tableName = "chapter_version",primaryKeys = {"voaid","types"})
    public static class ChapterVersion implements Serializable {
        @NonNull
        private int voaid;
        private int orderNumber;
        private int level;
        private int chapterOrder;
        private int version;
        @NonNull
        private String types;

        @NonNull
        public String getTypes() {
            return types;
        }

        public void setTypes(@NonNull String types) {
            this.types = types;
        }

        public void setVoaid(int voaid) {
            this.voaid = voaid;
        }
        public int getVoaid() {
            return voaid;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }
        public int getOrderNumber() {
            return orderNumber;
        }

        public void setLevel(int level) {
            this.level = level;
        }
        public int getLevel() {
            return level;
        }

        public void setChapterOrder(int chapterOrder) {
            this.chapterOrder = chapterOrder;
        }
        public int getChapterOrder() {
            return chapterOrder;
        }

        public void setVersion(int version) {
            this.version = version;
        }
        public int getVersion() {
            return version;
        }

        @Override
        public String toString() {
            return "ChapterVersion{" +
                    "voaid=" + voaid +
                    ", orderNumber=" + orderNumber +
                    ", level=" + level +
                    ", chapterOrder=" + chapterOrder +
                    ", version=" + version +
                    ", types='" + types + '\'' +
                    '}';
        }
    }

}
