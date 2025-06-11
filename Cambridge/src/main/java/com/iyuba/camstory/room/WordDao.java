package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iyuba.camstory.bean.EvaluateResponse;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EvaluateResponse.Words word);

    @Query("select * from word where uid = :uid and voaId = :voaId and senIndex = :senIndex and type = :type")
    List<EvaluateResponse.Words> list(String uid,String voaId,String senIndex,String type);
}
