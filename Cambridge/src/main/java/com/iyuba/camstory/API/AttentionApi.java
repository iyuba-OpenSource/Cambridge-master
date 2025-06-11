package com.iyuba.camstory.API;


import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.sqlite.mode.Attention;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/15 16:37
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface AttentionApi {
    @GET("v2/api.iyuba?")
    Call<AttentionBean> getAttention(
            @Query("protocol") String protocol,
            @Query("uid") String uid,
            @Query("pageCounts") String pageCounts,
            @Query("sign") String sign
    );
}
