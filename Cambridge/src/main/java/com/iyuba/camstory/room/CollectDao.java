package com.iyuba.camstory.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.bean.Collect;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CollectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Collect collect);

    @Delete
    void delete(Collect collect);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Collect collect);

    @Query("select b.orderNumber, b.level, b.types, b.about_author, b.about_book," +
            " b.about_book,b.about_interpreter,b.author,b.bookname_cn,b.bookname_en," +
            "b.interpreter,b.pic,b.wordcounts,b.isDown from collect a left join book b on a.type = b.types " +
            "and a.level = b.level and a.order_number = b.orderNumber where uid = :uid")
    Single<List<BookListResponse.BookInfo>> list(int uid);

    @Query("select * from collect where uid = :uid and level = :level and order_number = :orderNumber and type = :type")
    Single<List<Collect>> findByBookInfo(String uid,String level,String orderNumber,String type);
}
