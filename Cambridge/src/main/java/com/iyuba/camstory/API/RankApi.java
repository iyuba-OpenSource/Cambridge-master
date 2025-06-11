package com.iyuba.camstory.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhangshuai on 2018/4/20.
 */

public interface RankApi {

    @GET("getStudyRanking.jsp?")
    Call<Object> getRankInfo(@Query("uid") int uid,
                                     @Query("type") String type,
                                     @Query("total") String total,
                                     @Query("sign") String sign,
                                     @Query("start") String start,
                                     @Query("mode") String mode);

    @GET("getNewsRanking.jsp?")
    Call<Object> getRankReadInfo(@Query("uid") int uid,
                             @Query("type") String type,
                             @Query("total") String total,
                             @Query("sign") String sign,
                             @Query("start") String start);

    @GET("getTestRanking.jsp")
    Call<Object> getRankTestInfo(@Query("uid") int uid,
                                 @Query("type") String type,
                                 @Query("total") String total,
                                 @Query("sign") String sign,
                                 @Query("start") String start);

    @GET("getTopicRanking.jsp")
    Call<Object> getTopicRanking(@Query("topic") String topic,
                                 @Query("topicid") int topicid,
                                 @Query("uid") int uid,
                                 @Query("type") String type,
                                 @Query("start") String start,
                                 @Query("total") String total,
                                 @Query("sign") String sign);

}
