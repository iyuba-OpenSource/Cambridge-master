package com.iyuba.camstory.lycam.API;


import com.iyuba.camstory.bean.living.LiveContentBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/7/21 10:22
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface LiveContentApi {
	@GET("getClass.iyuba")
	Call<LiveContentBean> getLiveContent(@Query("protocol") String protocol,
										 @Query("zhibo") int isLive,
										 @Query("ocid") int pcakid,
										 @Query("sign") String sign);
}
