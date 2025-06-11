package com.iyuba.camstory.lycam.API;




import com.iyuba.camstory.bean.living.LivePackListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/7/19 17:59
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface LivePackApi {
	@GET("getClass.iyuba")
	Call<LivePackListBean> getLivePackList(@Query("protocol") String protocol,
										   @Query("zhibo") String isLive,
										   @Query("id") String id,
										   @Query("type") String type,
										   @Query("pageNumber") int pageNumber,
										   @Query("pageCounts") int pageCounts,
										   @Query("sign") String sign);
}
