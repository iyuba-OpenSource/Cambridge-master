package com.iyuba.basichdsfavorlibrary.network.api;

import com.iyuba.basichdsfavorlibrary.model.UpdateCollectResultXML;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhangshuai on 2018/8/22.
 */

public interface UpdateHdsCollectApi2 {
    @GET("iyuba/updateCollect.jsp")
    Observable<UpdateCollectResultXML> updateCollectState2(@Query("groupName") String groupName,
                                                          @Query("sentenceFlg") String sentenceFlg,
                                                          @Query("appId") String appId,
                                                          @Query("userId") String userId,
                                                          @Query("type") String type,
                                                          @Query("voaId") String voaId,
                                                          @Query("sentenceId") String sentenceId,
                                                          @Query("topic") String topic,
                                                          @Query("other1") String other1);
}
