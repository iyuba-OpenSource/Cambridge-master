package com.iyuba.camstory.bean;

import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.List;

public class BookContentResponse {

    private int result;
    private List<Texts> texts;
    private String message;

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setTexts(List<Texts> texts) {
        this.texts = texts;
    }

    public List<Texts> getTexts() {
        return texts;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Entity(tableName = "sentence", primaryKeys = {"voaid","paraid","index","types"})
    public static class Texts {

        private String BeginTiming;
        @NonNull
        private String voaid;
        @NonNull
        private String paraid;
        private String EndTiming;
        private String image;
        @NonNull
        private String index;
        private String textCH;
        private String textEN;
        private boolean isRead = false;
        @Ignore
        private SpannableStringBuilder readResult;
        private int readScore = 0;
        @NonNull
        private String types;

        //评测结果
        @Ignore
        private EvaluateResponse evaluateResponse;

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public SpannableStringBuilder getReadResult() {
            return readResult;
        }

        public void setReadResult(SpannableStringBuilder readResult) {
            this.readResult = readResult;
        }

        public int getReadScore() {
            return readScore;
        }

        public void setReadScore(int readScore) {
            this.readScore = readScore;
        }

        public EvaluateResponse getEvaluateResponse() {
            return evaluateResponse;
        }

        public void setEvaluateResponse(EvaluateResponse evaluateResponse) {
            this.evaluateResponse = evaluateResponse;
        }

        public void setBeginTiming(String BeginTiming) {
            this.BeginTiming = BeginTiming;
        }

        public String getBeginTiming() {
            return BeginTiming;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }

        public String getVoaid() {
            return voaid;
        }

        public void setParaid(String paraid) {
            this.paraid = paraid;
        }

        public String getParaid() {
            return paraid;
        }

        public void setEndTiming(String EndTiming) {
            this.EndTiming = EndTiming;
        }

        public String getEndTiming() {
            return EndTiming;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getIndex() {
            return index;
        }

        public void setTextCH(String textCH) {
            this.textCH = textCH;
        }

        public String getTextCH() {
            return textCH;
        }

        public void setTextEN(String textEN) {
            this.textEN = textEN;
        }

        public String getTextEN() {
            return textEN;
        }
    }

    @Override
    public String toString() {
        return "BookContentResponse{" +
                "result=" + result +
                ", texts=" + texts +
                ", message='" + message + '\'' +
                '}';
    }
}
