package com.iyuba.camstory.lycam.API;

import androidx.annotation.NonNull;


import com.iyuba.camstory.lycam.bean.UserModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/7/29 19:22
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface LycamLoginApi {
	@POST("/login")
	Call<UserModel> signin(
			@NonNull @Query("username") String username,
			@NonNull @Query("password") String password
	);
}
