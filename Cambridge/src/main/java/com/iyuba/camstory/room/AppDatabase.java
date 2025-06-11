package com.iyuba.camstory.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.bean.ChapterVersionResponse;
import com.iyuba.camstory.bean.Collect;
import com.iyuba.camstory.bean.EvaluateRecord;
import com.iyuba.camstory.bean.EvaluateResponse;
import com.iyuba.camstory.bean.SoundRecord;

@Database(entities = {
        BookListResponse.BookInfo.class,
        BookDetailResponse.ChapterInfo.class,
        BookContentResponse.Texts.class,
        ChapterVersionResponse.ChapterVersion.class,
        Collect.class,
        EvaluateRecord.class,
        EvaluateResponse.Words.class,
        SoundRecord.class
},version = 3,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "book.db";
    private static AppDatabase mInstance;
    public static synchronized AppDatabase getInstance(Context context){
        if(mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return mInstance;
    }
    public abstract BookDao getBookInfoDao();
    public abstract ChapterDao getChapterDao();
    public abstract SentenceDao getSentenceDao();
    public abstract ChapterVersionDao getChapterVersionDao();
    public abstract CollectDao getCollectDao();
    public abstract EvaluateRecordDao getEvaluateDao();
    public abstract WordDao getWordDao();
    public abstract SoundRecordDao getSoundRecordDao();
}

