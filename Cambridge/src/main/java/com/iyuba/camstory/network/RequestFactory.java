package com.iyuba.camstory.network;


import android.util.Log;

import com.iyuba.camstory.API.ADApi;
import com.iyuba.camstory.API.AttentionApi;
import com.iyuba.camstory.API.BookApi;
import com.iyuba.camstory.API.EvaluateApi;
import com.iyuba.camstory.API.FansApi;
import com.iyuba.camstory.API.LoginApi;
import com.iyuba.camstory.API.RankApi;
import com.iyuba.camstory.API.SoundCorrectApi;
import com.iyuba.camstory.API.TemporaryAccountApi;
import com.iyuba.common.WebConstant;
import com.iyuba.voa.activity.setting.Constant;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 作者：renzhy on 16/6/22 16:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class RequestFactory {

	private static AttentionApi attentionApi;
	private static FansApi fansApi;
	private static LoginApi loginApi;
	private static TemporaryAccountApi temporaryAccountApi;
	private static ADApi adApi;
	private static RankApi rankApi;
	private static RankApi rankApi1;
	private static RankApi rankApiTest;

	private static final String TAG = "RequestFactory";
	private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
		@Override
		public void log(String message) {
			Log.e(TAG, message);
		}
	});
	private static OkHttpClient okHttpClient = new OkHttpClient();
	private static OkHttpClient okHttpClient333;
	private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();

	public static void initOkHttpClient() {
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(15, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build();
	}

	public static void initOkHttpClient3333() {
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient333 = new OkHttpClient.Builder()
				.connectTimeout(3, TimeUnit.SECONDS)
				.readTimeout(3, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build();
	}

	public static AttentionApi getAttentionApi() {
		if (attentionApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			attentionApi = retrofit.create(AttentionApi.class);
		}
		return attentionApi;
	}

	public static FansApi getFansApi() {
		if (fansApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			fansApi = retrofit.create(FansApi.class);
		}
		return fansApi;
	}

	public static LoginApi getLoginApi() {
		if (loginApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			loginApi = retrofit.create(LoginApi.class);
		}
		return loginApi;
	}

	public static TemporaryAccountApi getTemporaryAccountApi(){

		if(temporaryAccountApi==null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			temporaryAccountApi = retrofit.create(TemporaryAccountApi.class);
		}
		return temporaryAccountApi;
	}

	public static ADApi getAdApi(){

		if(adApi==null){
            initOkHttpClient3333();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient333)
					.baseUrl("http://app."+Constant.IYBHttpHead+"/dev/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			adApi = retrofit.create(ADApi.class);
		}
		return adApi;
	}

	public static RankApi getRankAdApi(){

		if(rankApi==null){
			initOkHttpClient();

			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://daxue."+Constant.IYBHttpHead+"/ecollege/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			rankApi = retrofit.create(RankApi.class);
		}
		return rankApi;
	}

	public static RankApi getRankReadApi(){

		if(rankApi1==null){
			initOkHttpClient();

			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					//http://app."+Constant.IYBHttpHead+"/dev/
					.baseUrl("http://cms."+Constant.IYBHttpHead+"/newsApi/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			rankApi1 = retrofit.create(RankApi.class);
		}
		return rankApi1;
	}
	public static RankApi getRankTestApi(){

		if(rankApiTest==null){
			initOkHttpClient();

			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					//http://app."+Constant.IYBHttpHead+"/dev/
					.baseUrl("http://daxue."+Constant.IYBHttpHead+"/ecollege/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			rankApiTest = retrofit.create(RankApi.class);
		}
		return rankApiTest;
	}
	public static OkHttpClient  getOkhttpClient(){
		return okHttpClient;
	}

	static SoundCorrectApi soundCorrectApi;
	public static SoundCorrectApi getSoundCorrectApi(){
		if (soundCorrectApi == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.addConverterFactory(gsonConverterFactory)
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl("http://word.iyuba.cn/words/")
					.build();
			soundCorrectApi = retrofit.create(SoundCorrectApi.class);
		}
		return soundCorrectApi;
	}

	static EvaluateApi evaluateApi;
	public static EvaluateApi getEvaluateApi(){
		if (evaluateApi == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.addConverterFactory(gsonConverterFactory)
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl(WebConstant.HTTP_SPEECH_ALL +"/test/")
					.build();
			evaluateApi = retrofit.create(EvaluateApi.class);
		}
		return evaluateApi;
	}

	static BookApi bookApi;
	public static BookApi getBookApi(){
		if (bookApi == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.addConverterFactory(gsonConverterFactory)
					//这个不知道是干什么得，会导致报错
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl(BookApi.BASE_URL)
					.build();
			bookApi = retrofit.create(BookApi.class);
		}
		return bookApi;
	}

	public static RequestBody fromString(String text) {
		return RequestBody.create(MediaType.parse("text/plain"), text);
	}

	public static MultipartBody.Part fromFile(File file) {
		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
		return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
	}
}
