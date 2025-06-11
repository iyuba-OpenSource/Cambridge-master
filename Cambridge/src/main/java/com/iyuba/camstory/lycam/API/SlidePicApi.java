package com.iyuba.camstory.lycam.API;
import com.iyuba.camstory.bean.living.SlideShowListBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/5/17 17:31
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface SlidePicApi {
	@GET("dev/getScrollPicApi.jsp")
	Call<SlideShowListBean> getSlidePicList(@Query("type") String type);
}
