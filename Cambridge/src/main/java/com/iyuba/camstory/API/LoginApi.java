package com.iyuba.camstory.API;


import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.bean.LoginBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/15 16:37
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface LoginApi {
    @GET("v2/api.iyuba?protocol=11001&format=json&")
    Call<LoginBean> getLogin(
            @Query("username") String userName,
            @Query("password") String password,
            @Query("appid") String appid,
            @Query("app") String appName,
            @Query("sign") String sign
    );
}
