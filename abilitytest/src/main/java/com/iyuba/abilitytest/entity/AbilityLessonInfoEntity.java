package com.iyuba.abilitytest.entity;

import java.io.Serializable;

/**
 * 试题内容 每一个题目的字段
 * Created by liuzhenli on 2017/5/16.
 */

public class AbilityLessonInfoEntity implements Serializable {
    public int lessonId;
    public String lessonName;
    public int bookId;
    public String bookName;

    @Override
    public String toString() {
        return "AbilityLessonInfoEntity{" +
                "lessonId=" + lessonId +
                ", lessonName='" + lessonName + '\'' +
                ", bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
