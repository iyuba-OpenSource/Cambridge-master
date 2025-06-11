package com.iyuba.camstory.lycam.API;




import com.iyuba.camstory.bean.living.CheckPhoneResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/4 16:20
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface CheckPhoneApi {
	@GET("sendMessage3.jsp")
	Call<CheckPhoneResult> checkPhone(
			@Query("userphone") String userphone
	);
}
