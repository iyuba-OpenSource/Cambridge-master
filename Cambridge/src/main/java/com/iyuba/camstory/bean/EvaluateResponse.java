package com.iyuba.camstory.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.List;

public class EvaluateResponse {


    private String sentence;
    private List<Words> words;
    private int scores;
    private double total_score;
    private String filepath;
    private String URL;

    /**
     * 后来加的字段，不属于服务器返回
     */
    private int position;
    private String localMP3Path;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLocalMP3Path() {
        return localMP3Path;
    }

    public void setLocalMP3Path(String localMP3Path) {
        this.localMP3Path = localMP3Path;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public void setWords(List<Words> words) {
        this.words = words;
    }

    public List<Words> getWords() {
        return words;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getScores() {
        return scores;
    }

    public void setTotal_score(double total_score) {
        this.total_score = total_score;
    }

    public double getTotal_score() {
        return total_score;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    @Entity(tableName = "word",primaryKeys = {"uid","voaId","senIndex","index","type"})
    public static class Words {
        @NonNull
        private String uid;
        @NonNull
        private String voaId;
        @NonNull
        private String type;
        @NonNull
        private String senIndex;
        @NonNull
        private String index;
        private String content;
        private String pron;
        private String pron2;
        private String user_pron;
        private String user_pron2;
        private String score;
        private String insert;
        private String delete;
        private String substitute_orgi;
        private String substitute_user;

        @NonNull
        public String getType() {
            return type;
        }

        public void setType(@NonNull String type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVoaId() {
            return voaId;
        }

        public void setVoaId(String voaId) {
            this.voaId = voaId;
        }

        public String getSenIndex() {
            return senIndex;
        }

        public void setSenIndex(String senIndex) {
            this.senIndex = senIndex;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getIndex() {
            return index;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setPron(String pron) {
            this.pron = pron;
        }

        public String getPron() {
            return pron;
        }

        public void setPron2(String pron2) {
            this.pron2 = pron2;
        }

        public String getPron2() {
            return pron2;
        }

        public void setUser_pron(String user_pron) {
            this.user_pron = user_pron;
        }

        public String getUser_pron() {
            return user_pron;
        }

        public void setUser_pron2(String user_pron2) {
            this.user_pron2 = user_pron2;
        }

        public String getUser_pron2() {
            return user_pron2;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getScore() {
            return score;
        }

        public void setInsert(String insert) {
            this.insert = insert;
        }

        public String getInsert() {
            return insert;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }

        public String getDelete() {
            return delete;
        }

        public void setSubstitute_orgi(String substitute_orgi) {
            this.substitute_orgi = substitute_orgi;
        }

        public String getSubstitute_orgi() {
            return substitute_orgi;
        }

        public void setSubstitute_user(String substitute_user) {
            this.substitute_user = substitute_user;
        }

        public String getSubstitute_user() {
            return substitute_user;
        }

        @Override
        public String toString() {
            return "Words{" +
                    "index='" + index + '\'' +
                    ", content='" + content + '\'' +
                    ", pron='" + pron + '\'' +
                    ", pron2='" + pron2 + '\'' +
                    ", user_pron='" + user_pron + '\'' +
                    ", user_pron2='" + user_pron2 + '\'' +
                    ", score='" + score + '\'' +
                    ", insert='" + insert + '\'' +
                    ", delete='" + delete + '\'' +
                    ", substitute_orgi='" + substitute_orgi + '\'' +
                    ", substitute_user='" + substitute_user + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "EvaluateResponse{" +
                "sentence='" + sentence + '\'' +
                ", words=" + words +
                ", scores=" + scores +
                ", total_score=" + total_score +
                ", filepath='" + filepath + '\'' +
                ", URL='" + URL + '\'' +
                ", position=" + position +
                ", localMP3Path='" + localMP3Path + '\'' +
                '}';
    }
}
