package com.iyuba.camstory.lycam.API;

import androidx.annotation.NonNull;

import com.iyuba.camstory.bean.living.UserInfoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/3 15:07
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface PersonalBasicInfoApi {
	@GET("v2/api.iyuba")
	Call<UserInfoBean> getPersonalBasicInfo(
			@Query("protocol") String protocol,
			@Query("platform") String platform,
			@NonNull @Query("id") String id,
			@NonNull @Query("myid") String myid,
			@Query("format") String format,
			@Query("sign") String sign

	);
}
