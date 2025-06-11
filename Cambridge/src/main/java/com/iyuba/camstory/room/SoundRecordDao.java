package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iyuba.camstory.bean.SoundRecord;

import io.reactivex.Observable;

@Dao
public interface SoundRecordDao {
    @Insert
    void insert(SoundRecord soundRecord);

    @Query("select COALESCE(max(currentPosition),0) from sound_record where uid = :uid and voaId = :voaId and type = :type")
    Observable<Integer> selectMaxProgressByUidAndVoaId(String uid,String voaId,String type);
}
