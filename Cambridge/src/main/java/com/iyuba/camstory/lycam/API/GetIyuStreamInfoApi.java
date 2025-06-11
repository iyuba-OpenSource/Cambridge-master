package com.iyuba.camstory.lycam.API;


import com.iyuba.camstory.bean.living.IyuStreamInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/11 20:51
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetIyuStreamInfoApi {
	@GET("stream/info")
	Call<IyuStreamInfo> getIyuStreamInfo(
			@Query("tid") String tid,
			@Query("uid") String uid,
			@Query(encoded = true, value = "avatarUrl") String avatarUrl,
			@Query("nickname") String nickname,
			@Query("sign") String sign
	);

}
