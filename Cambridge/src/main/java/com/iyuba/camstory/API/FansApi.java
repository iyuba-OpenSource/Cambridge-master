package com.iyuba.camstory.API;


import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.bean.FansBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/15 16:37
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface FansApi {
    @GET("v2/api.iyuba?")
    Call<FansBean> getFans(
            @Query("platform") String platform,
            @Query("format") String format,
            @Query("protocol") String protocol,
            @Query("uid") String uid,
            @Query("pageCounts") String pageCounts,
            @Query("pageNumber") String pageNumber,
            @Query("sign") String sign
    );
}
