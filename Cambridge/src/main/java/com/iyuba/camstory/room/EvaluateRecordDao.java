package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iyuba.camstory.bean.EvaluateRecord;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface EvaluateRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EvaluateRecord evaluateRecord);

    @Query("select * from evaluate_record where uid = :uid and voaId = :voaId and indexX = :indexX and type = :type")
    List<EvaluateRecord> list(String uid, String voaId,String indexX,String type);

    @Query("select count(*) from evaluate_record where uid = :uid and voaId = :voaId and type = :type")
    Observable<Integer> countEva(String uid, String voaId, String type);
}
