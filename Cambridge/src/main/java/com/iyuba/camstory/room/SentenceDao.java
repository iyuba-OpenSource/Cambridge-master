package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iyuba.camstory.bean.BookContentResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface SentenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookContentResponse.Texts texts);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BookContentResponse.Texts texts);

    @Query("select * from sentence where voaid = :voaId and types = :type order by cast(`paraid` as '9999') asc,cast(`index` as '9999') asc")
    Observable<List<BookContentResponse.Texts>> list(String voaId,String type);

    @Query("select count(*) from sentence where voaid = :voaId and types = :type")
    Observable<Integer> count(String voaId,String type);

    @Query("select COALESCE(max(cast(endTiming as DOUBLE)),0) from sentence where voaid = :voaId and types = :type")
    Observable<Double> selectDuration(String voaId,String type);
}
