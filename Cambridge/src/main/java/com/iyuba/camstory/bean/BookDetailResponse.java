package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

public class BookDetailResponse {

    private BookListResponse.BookInfo bookInfo;
    private int result;
    private List<ChapterInfo> chapterInfo;
    private String message;
    public void setBookInfo(BookListResponse.BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
    public BookListResponse.BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setChapterInfo(List<ChapterInfo> chapterInfo) {
        this.chapterInfo = chapterInfo;
    }

    public List<ChapterInfo> getChapterInfo() {
        return chapterInfo;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }


    @Entity(tableName = "chapter",primaryKeys = {"voaid","types"})
    public static class ChapterInfo implements Serializable {
        @NonNull
        private String voaid;
        private String orderNumber;
        private String level;
        private String chapterOrder;
        private String sound;
        private int show;
        private String cname_cn;
        private String cname_en;
        private String version;
        @NonNull
        private String types;

        @Ignore
        private String evaProgress;
        @Ignore
        private String soundProgress;

        @Ignore
        private boolean isShowProgress;

        @Override
        public String toString() {
            return "ChapterInfo{" +
                    "voaid='" + voaid + '\'' +
                    ", orderNumber='" + orderNumber + '\'' +
                    ", level='" + level + '\'' +
                    ", chapterOrder='" + chapterOrder + '\'' +
                    ", sound='" + sound + '\'' +
                    ", show=" + show +
                    ", cname_cn='" + cname_cn + '\'' +
                    ", cname_en='" + cname_en + '\'' +
                    ", version='" + version + '\'' +
                    ", types='" + types + '\'' +
                    ", evaProgress='" + evaProgress + '\'' +
                    ", soundProgress='" + soundProgress + '\'' +
                    '}';
        }

        public String getEvaProgress() {
            return evaProgress;
        }

        public void setEvaProgress(String evaProgress) {
            this.evaProgress = evaProgress;
        }

        public String getSoundProgress() {
            return soundProgress;
        }

        public void setSoundProgress(String soundProgress) {
            this.soundProgress = soundProgress;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }
        public String getVoaid() {
            return voaid;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }
        public String getOrderNumber() {
            return orderNumber;
        }

        public void setLevel(String level) {
            this.level = level;
        }
        public String getLevel() {
            return level;
        }

        public void setChapterOrder(String chapterOrder) {
            this.chapterOrder = chapterOrder;
        }
        public String getChapterOrder() {
            return chapterOrder;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }
        public String getSound() {
            return sound;
        }

        public void setShow(int show) {
            this.show = show;
        }
        public int getShow() {
            return show;
        }

        public void setCname_cn(String cname_cn) {
            this.cname_cn = cname_cn;
        }
        public String getCname_cn() {
            return cname_cn;
        }

        public void setCname_en(String cname_en) {
            this.cname_en = cname_en;
        }
        public String getCname_en() {
            return cname_en;
        }

        public boolean isShowProgress() {
            return isShowProgress;
        }

        public void setShowProgress(boolean showProgress) {
            isShowProgress = showProgress;
        }
    }

    @Override
    public String toString() {
        return "BookDetailResponse{" +
                "bookInfo=" + bookInfo +
                ", result=" + result +
                ", chapterInfo=" + chapterInfo +
                ", message='" + message + '\'' +
                '}';
    }
}
