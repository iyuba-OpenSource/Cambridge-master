package com.iyuba.abilitytest.HttpRequest;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


/**
 * @className:JkApiRequest
 * @classDescription:网络请求
 * @author: leibing
 * @createTime: 2016/8/30
 */
public class HttpRequestAPI {
    // sington
    private static HttpRequestAPI instance;
    // Retrofit object
    private Retrofit retrofit;

    /**
     * Constructor
     *
     * @param
     * @return
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     */
    private HttpRequestAPI() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

    }

    /**
     * sington
     *
     * @param
     * @return
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     */
    public static HttpRequestAPI getInstance() {
        if (instance == null) {
            instance = new HttpRequestAPI();
        }
        return instance;
    }

    /**
     * create api instance
     *
     * @param service api class
     * @return
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     */
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }


}
