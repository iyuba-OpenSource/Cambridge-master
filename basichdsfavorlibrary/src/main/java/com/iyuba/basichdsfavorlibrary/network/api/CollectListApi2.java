package com.iyuba.basichdsfavorlibrary.network.api;

import com.iyuba.basichdsfavorlibrary.model.SyncCollectResutl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhangshuai on 2018/8/22.
 */

public interface CollectListApi2 {
    @GET("dataapi/jsp/getCollect.jsp")
    Observable<SyncCollectResutl> getCollectList2(@Query("userId") String userId,
                                                 @Query("topic") String topic,
                                                 @Query("appid") String appid,
                                                 @Query("sentenceFlg") String sentenceFlg,
                                                 @Query("format") String format,
                                                 @Query("sign") String sign,
                                                 @Query("other1") String other1);
}
