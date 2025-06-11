package com.iyuba.camstory.lycam.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/16 14:57
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface SubmitFeedbackApi {
	@GET("mobile/android/iyuba/feedback.plain")
	Call<ResponseBody> submitFeedback(
			@Query("uid") String uid,
			@Query("content") String content,
			@Query("email") String email
	);

}
