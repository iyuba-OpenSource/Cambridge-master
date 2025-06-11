package com.iyuba.basichdsfavorlibrary.network;

import com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager;
import com.iyuba.basichdsfavorlibrary.network.api.CollectListApi;
import com.iyuba.basichdsfavorlibrary.network.api.CollectListApi2;
import com.iyuba.basichdsfavorlibrary.network.api.UpdateHdsCollectApi;
import com.iyuba.basichdsfavorlibrary.network.api.UpdateHdsCollectApi2;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 作者：renzhy on 17/2/16 15:10
 * 邮箱：renzhongyigoo@gmail.com
 */
public class FavorHeadlineNetwork {

    private static UpdateHdsCollectApi updateHdsCollectApi;
    private static UpdateHdsCollectApi2 updateHdsCollectApi2;
    private static CollectListApi collectListApi;
    private static CollectListApi2 collectListApi2;

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static Converter.Factory xmlConverterFactory = SimpleXmlConverterFactory.create();


    public static void initOkHttpClient(){
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    public static UpdateHdsCollectApi getUpdateCollectApi(){
        if(updateHdsCollectApi == null){
            initOkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BasicHDsFavorConstantManager.HEADLINES_COLLECT_ENDPOINT)
                    .addConverterFactory(xmlConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            updateHdsCollectApi = retrofit.create(UpdateHdsCollectApi.class);
        }
        return updateHdsCollectApi;
    }

    public static UpdateHdsCollectApi2 getUpdateCollectApi2(){
        if(updateHdsCollectApi2 == null){
            initOkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BasicHDsFavorConstantManager.HEADLINES_COLLECT_ENDPOINT)
                    .addConverterFactory(xmlConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            updateHdsCollectApi2 = retrofit.create(UpdateHdsCollectApi2.class);
        }
        return updateHdsCollectApi2;
    }

    public static CollectListApi getCollectListApi(){
        if(collectListApi == null){
            initOkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BasicHDsFavorConstantManager.IYUBA_CMS_ENDPOINT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            collectListApi = retrofit.create(CollectListApi.class);
        }
        return collectListApi;
    }

    public static CollectListApi2 getCollectListApi2(){
        if(collectListApi2 == null){
            initOkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BasicHDsFavorConstantManager.IYUBA_CMS_ENDPOINT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            collectListApi2 = retrofit.create(CollectListApi2.class);
        }
        return collectListApi2;
    }
}
