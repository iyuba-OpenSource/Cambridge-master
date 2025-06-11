package com.iyuba.camstory.lycam.API;



import com.iyuba.camstory.bean.living.RegistResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/8 16:00
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface RegistByEmailApi {
	@GET("v2/api.iyuba")
	Call<RegistResponse> registByEmail(
			@Query("protocol") String protocol,
			@Query("platform") String platform,
			@Query("username") String username,
			@Query("password") String password,
			@Query("email") String email,
			@Query("app") String appName,
			@Query("format") String format,
			@Query("sign") String sign
	);
}
