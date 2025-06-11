package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.bean.ChapterVersionResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface ChapterVersionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChapterVersionResponse.ChapterVersion chapterVersion);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ChapterVersionResponse.ChapterVersion chapterVersion);

    @Query("select * from chapter_version where voaid = :voaId")
    Observable<List<ChapterVersionResponse.ChapterVersion>> findByVoaId(int voaId);
}
