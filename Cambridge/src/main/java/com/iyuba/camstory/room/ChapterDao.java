package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookDetailResponse.ChapterInfo chapterInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BookDetailResponse.ChapterInfo chapterInfo);

    @Query("select * from chapter where level = :level and orderNumber = :orderNumber and types = :type")
    List<BookDetailResponse.ChapterInfo> list(int level, int orderNumber, String type);
}
