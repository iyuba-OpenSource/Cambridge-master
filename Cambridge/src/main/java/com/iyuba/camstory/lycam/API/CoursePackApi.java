package com.iyuba.camstory.lycam.API;





import com.iyuba.camstory.bean.living.CoursePackListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/5/20 17:00
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface CoursePackApi {
	//protocol=10102&id=-2&type=1&pageNumber=1&pageCounts=20&sign=8df637ceecb3148d091d06c680561b24
	@GET("getClass.iyuba")
	Call<CoursePackListBean> getCoursePackList(@Query("protocol") String protocol, @Query("id") String id,
											   @Query("type") String type, @Query("pageNumber") int pageNumber,
											   @Query("pageCounts") int pageCounts,
											   @Query("sign") String sign);
}
