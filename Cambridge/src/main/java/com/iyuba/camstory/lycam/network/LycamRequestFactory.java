package com.iyuba.camstory.lycam.network;


import com.iyuba.camstory.lycam.API.LycamLoginApi;
import com.iyuba.camstory.lycam.API.StreamDetailApi;
import com.iyuba.camstory.lycam.API.StreamModelApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：renzhy on 16/6/22 16:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LycamRequestFactory {

	private static LycamLoginApi lycamLoginApi;
	private static StreamModelApi streamModelApi;
	private static StreamDetailApi streamDetailApi;

	private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
	private static OkHttpClient okHttpClient = new OkHttpClient();
	private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();

	public static void initOkHttpClient(){
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build();
	}

	public static LycamLoginApi getLycamLoginApi(){
		if(lycamLoginApi == null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api-sbkt.lycam.tv")
					.addConverterFactory(gsonConverterFactory)
					.build();

			lycamLoginApi = retrofit.create(LycamLoginApi.class);
		}
		return lycamLoginApi;
	}

	public static StreamModelApi getStreamModel(){
		if(streamModelApi == null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api-sbkt.lycam.tv")
					.addConverterFactory(gsonConverterFactory)
					.build();

			streamModelApi = retrofit.create(StreamModelApi.class);
		}
		return streamModelApi;
	}

	public static StreamDetailApi getStreamDetail(){
		if(streamDetailApi == null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api-sbkt.lycam.tv")
					.addConverterFactory(gsonConverterFactory)
					.build();

			streamDetailApi = retrofit.create(StreamDetailApi.class);
		}
		return streamDetailApi;
	}
}
