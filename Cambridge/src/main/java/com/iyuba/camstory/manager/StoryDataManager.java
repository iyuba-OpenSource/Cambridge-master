package com.iyuba.camstory.manager;


import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.utils.BackPlayer;

public class StoryDataManager {

    private StoryDataManager() {}
    private static StoryDataManager instance = null;
    //静态工厂方法
    public static StoryDataManager Instance() {
        if (instance == null) {
            instance = new StoryDataManager();
        }
        return instance;
    }

    private BookDetailResponse.ChapterInfo curChapterInfo;

    private BackPlayer cmPlayer = new BackPlayer(CrashApplication.getInstance());
    private int currChapter;

    public BookDetailResponse.ChapterInfo getCurChapterInfo() {
        return curChapterInfo;
    }

    public void setCurChapterInfo(BookDetailResponse.ChapterInfo curChapterInfo) {
        this.curChapterInfo = curChapterInfo;
    }

    public int getCurrChapter() {
        return currChapter;
    }

    public void setCurrChapter(int currChapter) {
        this.currChapter = currChapter;
    }

    public BackPlayer getCmPlayer() {
        return cmPlayer;
    }
}
