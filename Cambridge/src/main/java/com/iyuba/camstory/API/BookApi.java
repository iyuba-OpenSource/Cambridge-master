package com.iyuba.camstory.API;

import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.bean.ChapterVersionResponse;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {

    String BASE_URL = "http://apps.iyuba.cn/book/";

    @GET("getStroryInfo.jsp?types=home")
    Observable<BookListResponse> getBookList(@Query("from")String from,@Query("level")Integer level);

    @GET("getStroryInfo.jsp?types=book")
    Observable<BookDetailResponse> getBookInfo(@Query("from")String from,@Query("level") String level, @Query("orderNumber")String orderNumber);

    @GET("getStroryInfo.jsp?types=detail")
    Observable<BookContentResponse> getBookContent(@Query("from")String from, @Query("level")String level, @Query("orderNumber")String orderNumber, @Query("chapterOrder")String chapterOrder);

    @GET("getStoryVersion.jsp")
    Observable<ChapterVersionResponse> getUpdateVersion(@Query("from")String from,@Query("level")String level, @Query("orderNumber")String orderNumber);
}
