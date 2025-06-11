package com.iyuba.camstory.API.data;

import java.util.List;

public class WordCorrectResponse {

    private int result;
    private String userPron;
    private String oriPron;
    private List<List<Integer>> matchIdx;
    private List<List<Integer>> insertId;
    private List<List<Integer>> deleteId;
    private List<List<Integer>> substituteId;
    private String key;
    private String audio;
    private String pron;
    private String proncode;
    private String def;
    private List<SentDTO> sent;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUserPron() {
        return userPron;
    }

    public void setUserPron(String userPron) {
        this.userPron = userPron;
    }

    public String getOriPron() {
        return oriPron;
    }

    public void setOriPron(String oriPron) {
        this.oriPron = oriPron;
    }

    public List<List<Integer>> getMatchIdx() {
        return matchIdx;
    }

    public void setMatchIdx(List<List<Integer>> matchIdx) {
        this.matchIdx = matchIdx;
    }

    public List<List<Integer>> getInsertId() {
        return insertId;
    }

    public void setInsertId(List<List<Integer>> insertId) {
        this.insertId = insertId;
    }

    public List<List<Integer>> getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(List<List<Integer>> deleteId) {
        this.deleteId = deleteId;
    }

    public List<List<Integer>> getSubstituteId() {
        return substituteId;
    }

    public void setSubstituteId(List<List<Integer>> substituteId) {
        this.substituteId = substituteId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getProncode() {
        return proncode;
    }

    public void setProncode(String proncode) {
        this.proncode = proncode;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public List<SentDTO> getSent() {
        return sent;
    }

    public void setSent(List<SentDTO> sent) {
        this.sent = sent;
    }

    public static class SentDTO {
        private int number;
        private String orig;
        private String trans;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }
    }
}
