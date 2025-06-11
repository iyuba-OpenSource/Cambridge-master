package com.iyuba.camstory.API;

import com.iyuba.camstory.bean.TemporaryUserJson;
import com.iyuba.configation.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iyuba on 2017/8/11.
 */

public interface TemporaryAccountApi {

    String URL = "http://api."+Constant.IYBHttpHead2+"/";
    int PROTOCOL = 11003;
    int PROTOCOL2 = 20001;
    String PLATFORM = "Android";
    int APPID = 227;
    String FORMAT = "json";

    @GET("v2/api.iyuba")
     Call<TemporaryUserJson> getTemporaryAccount(@Query("protocol") int protocol,
                                                       @Query("deviceId") String deviceId,
                                                       @Query("platform") String platform,
                                                       @Query("appid") int appid,
                                                       @Query("format") String format,
                                                       @Query("sign") String sign);
}
