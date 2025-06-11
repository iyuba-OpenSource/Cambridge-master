package com.iyuba.camstory.API;

import com.iyuba.camstory.API.data.EvaSendBean;
import com.iyuba.camstory.service.QQGroupResp;
import com.iyuba.camstory.service.QQMessageResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @POST
    Call<EvaSendBean> audioSendApi(@Url String url,
                                   @Field("topic") String topic,
                                   @Field("platform") String platform,
                                   @Field("format") String format,
                                   @Field("protocol") String protocol,
                                   @Field("userid") String userid,
                                   @Field("voaid") String voaid,
                                   @Field("score") String score,
                                   @Field("shuoshuotype") String shuoshuotype,
                                   @Field("content") String content);

    @GET("japanapi/getJpQQ.jsp")
    Call<QQMessageResp> fetchQQMsg(@Query("appid") String appId);
}
