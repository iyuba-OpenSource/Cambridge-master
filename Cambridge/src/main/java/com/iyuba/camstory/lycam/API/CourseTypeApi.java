package com.iyuba.camstory.lycam.API;




import com.iyuba.camstory.bean.living.CourseTypeListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/5/23 15:06
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface CourseTypeApi {
	//http://class."+Constant.IYBHttpHead+"/getClass.iyuba?protocol=10103&type=0&sign=806e43f1d3416670861ef3b187f6a27c
	@GET("getClass.iyuba")
	Call<CourseTypeListBean> getCourseTypeList(
			@Query("protocol") String protocol, @Query("type") String type,
			@Query("sign") String sign);
}
