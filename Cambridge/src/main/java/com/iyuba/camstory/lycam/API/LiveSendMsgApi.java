package com.iyuba.camstory.lycam.API;



import com.iyuba.camstory.bean.living.IyuSendMsgRspBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 作者：renzhy on 16/8/12 14:43
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface LiveSendMsgApi {
	@FormUrlEncoded
	@POST("msg/push")
	Call<IyuSendMsgRspBean> sendChatMessage(
			@Field("content") String content,
			@Field("tid") String tid,
			@Field("uid") String uid,
			@Field(encoded = true, value = "avatarUrl") String avatarUrl,
			@Field("nickname") String nickname,
			@Field("sign") String sign
	);
}
