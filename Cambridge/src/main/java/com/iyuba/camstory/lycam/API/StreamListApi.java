package com.iyuba.camstory.lycam.API;

import androidx.annotation.NonNull;


import com.iyuba.camstory.lycam.bean.StreamsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/7/29 19:51
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface StreamListApi {
	@GET("/user/streams")
	Call<StreamsModel> getStreamList(
			@NonNull @Header("Authorization") String authorization,
			@Query("resultsPerPage") int resultsPerPage,
			@Query("page") int page,
			@Query("userId") String userId,
			@Query("role") String role,
			@Query("status") String status
	);
}
