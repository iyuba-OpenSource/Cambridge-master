package com.iyuba.basichdsfavorlibrary.network.api;

import com.iyuba.basichdsfavorlibrary.model.UpdateCollectResultXML;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：renzhy on 17/10/20 09:23
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface UpdateHdsCollectApi {
    @GET("iyuba/updateCollect.jsp")
    Observable<UpdateCollectResultXML> updateCollectState(@Query("groupName") String groupName,
                                                          @Query("sentenceFlg") String sentenceFlg,
                                                          @Query("appId") String appId,
                                                          @Query("userId") String userId,
                                                          @Query("type") String type,
                                                          @Query("voaId") String voaId,
                                                          @Query("sentenceId") String sentenceId,
                                                          @Query("topic") String topic);

}
