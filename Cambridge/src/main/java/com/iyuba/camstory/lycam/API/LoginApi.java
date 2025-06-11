package com.iyuba.camstory.lycam.API;

import androidx.annotation.NonNull;


import com.iyuba.camstory.bean.living.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/2 21:26
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface LoginApi {
	@GET("v2/api.iyuba")
	Call<LoginResponse> login(
			@Query("protocol") String protocol,
			@Query("platform") String platform,
			@NonNull @Query("username") String username,
			@NonNull @Query("password") String password,
			@Query("x") String locationX,
			@Query("y") String locationY,
			@Query("appid") String appid,
			@Query("format") String format,
			@Query("sign") String sign

	);
}
