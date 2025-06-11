package com.iyuba.camstory.API;

import com.iyuba.camstory.bean.AdBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhangshuai on 2018/1/12.
 */

public interface ADApi {
    /**
     *
     * @param uid 用户id
     * @param appId
     * @param flag 1是开屏。4是banner
     * @return
     */
    @GET("getAdEntryAll.jsp")
    Call<List<AdBean>>  getAdInfo(@Query("uid") int uid,
                                  @Query("appId") String appId,
                                  @Query("flag") String flag);

}
