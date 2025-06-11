package com.iyuba.camstory.lycam.API;


import com.iyuba.camstory.bean.living.LivePayedRecordXML;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/11 16:13
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetPayedRecordApi {
	@GET("pay/apiGetPayRecord.jsp")
	Call<LivePayedRecordXML> getPayedRecord(
			@Query("userId") String userId,
			@Query("appId") String appId,
			@Query("packageId") String packageId,
			@Query("sign") String sign
	);
}
