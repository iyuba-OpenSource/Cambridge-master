package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.io.Serializable;
import java.util.List;

public class BookListResponse {

    private int result;
    private List<BookInfo> data;
    private String message;
    public void setResult(int result) {
        this.result = result;
    }
    public int getResult() {
        return result;
    }

    public void setData(List<BookInfo> data) {
        this.data = data;
    }
    public List<BookInfo> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }


    @Entity(tableName = "book",primaryKeys = {"orderNumber","level","types"})
    public static class BookInfo implements Serializable {
        @NonNull
        private String orderNumber;
        @NonNull
        private String level;
        private String bookname_en;
        private String author;
        private String about_book;
        private String bookname_cn;
        private String about_interpreter;
        private String wordcounts;
        private String interpreter;
        private String pic;
        private String about_author;
        @NonNull
        private String types;
        private Integer isDown;

        @Ignore
        private Boolean isChecked;

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }

        public Integer getIsDown() {
            return isDown;
        }

        public void setIsDown(Integer isDown) {
            this.isDown = isDown;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
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

        public void setBookname_en(String bookname_en) {
            this.bookname_en = bookname_en;
        }
        public String getBookname_en() {
            return bookname_en;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
        public String getAuthor() {
            return author;
        }

        public void setAbout_book(String about_book) {
            this.about_book = about_book;
        }
        public String getAbout_book() {
            return about_book;
        }

        public void setBookname_cn(String bookname_cn) {
            this.bookname_cn = bookname_cn;
        }
        public String getBookname_cn() {
            return bookname_cn;
        }

        public void setAbout_interpreter(String about_interpreter) {
            this.about_interpreter = about_interpreter;
        }
        public String getAbout_interpreter() {
            return about_interpreter;
        }

        public void setWordcounts(String wordcounts) {
            this.wordcounts = wordcounts;
        }
        public String getWordcounts() {
            return wordcounts;
        }

        public void setInterpreter(String interpreter) {
            this.interpreter = interpreter;
        }
        public String getInterpreter() {
            return interpreter;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
        public String getPic() {
            return pic;
        }

        public void setAbout_author(String about_author) {
            this.about_author = about_author;
        }
        public String getAbout_author() {
            return about_author;
        }

        @Override
        public String toString() {
            return "BookInfo{" +
                    "orderNumber='" + orderNumber + '\'' +
                    ", level='" + level + '\'' +
                    ", bookname_en='" + bookname_en + '\'' +
                    ", author='" + author + '\'' +
                    ", about_book='" + about_book + '\'' +
                    ", bookname_cn='" + bookname_cn + '\'' +
                    ", about_interpreter='" + about_interpreter + '\'' +
                    ", wordcounts='" + wordcounts + '\'' +
                    ", interpreter='" + interpreter + '\'' +
                    ", pic='" + pic + '\'' +
                    ", about_author='" + about_author + '\'' +
                    ", types='" + types + '\'' +
                    ", isDown=" + isDown +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BookListResponse{" +
                "result=" + result +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
