package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iyuba.camstory.bean.BookListResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookListResponse.BookInfo bookInfo);

    @Query("select * from book where level = :level and types = :type")
    Observable<List<BookListResponse.BookInfo>> findByPrimaryKey(Integer level, String type);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(BookListResponse.BookInfo bookInfo);

    @Query("select * from book where level = :level")
    Single<List<BookListResponse.BookInfo>> list(int level);

    @Query("select * from book where isDown = 1")
    Single<List<BookListResponse.BookInfo>> listDown();
}
