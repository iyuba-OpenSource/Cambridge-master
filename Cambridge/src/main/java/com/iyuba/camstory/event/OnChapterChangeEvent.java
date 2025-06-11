package com.iyuba.camstory.event;

import com.iyuba.camstory.bean.BookDetailResponse;

public class OnChapterChangeEvent {
    private BookDetailResponse.ChapterInfo chapterInfo;

    public OnChapterChangeEvent() {

    }

    public OnChapterChangeEvent(BookDetailResponse.ChapterInfo chapterInfo) {
        this.chapterInfo = chapterInfo;
    }

    public BookDetailResponse.ChapterInfo getChapterInfo() {
        return chapterInfo;
    }

    public void setChapterInfo(BookDetailResponse.ChapterInfo chapterInfo) {
        this.chapterInfo = chapterInfo;
    }
}
