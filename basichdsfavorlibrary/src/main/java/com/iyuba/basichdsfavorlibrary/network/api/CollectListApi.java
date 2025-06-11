package com.iyuba.basichdsfavorlibrary.network.api;

import com.iyuba.basichdsfavorlibrary.model.SyncCollectResutl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：renzhy on 17/10/20 09:50
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface CollectListApi {
    @GET("dataapi/jsp/getCollect.jsp")
    Observable<SyncCollectResutl> getCollectList(@Query("userId") String userId,
                                                 @Query("topic") String topic,
                                                 @Query("appid") String appid,
                                                 @Query("sentenceFlg") String sentenceFlg,
                                                 @Query("format") String format,
                                                 @Query("sign") String sign);
}
