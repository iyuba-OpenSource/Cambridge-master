package com.iyuba.camstory.lycam.API;



import com.iyuba.camstory.bean.living.PayedResultXML;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/15 20:34
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface PayForCourseApi {
	@GET("pay/payClassApi.jsp")
	Call<PayedResultXML> getPayResult(
			@Query("userId") String userId,
			@Query("amount") int amount,
			@Query("appId") String appId,
			@Query("productId") int productId,
			@Query("packageId") int packageId,
			@Query("sign") String sign,
			@Query("zhibo") String zhibo
	);
}
