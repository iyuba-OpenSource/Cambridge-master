package com.iyuba.camstory.lycam.network;

import com.iyuba.camstory.lycam.API.CheckIyubiApi;
import com.iyuba.camstory.lycam.API.CheckPhoneApi;
import com.iyuba.camstory.lycam.API.GetIyuStreamInfoApi;
import com.iyuba.camstory.lycam.API.GetPayedRecordApi;
import com.iyuba.camstory.lycam.API.LiveContentApi;
import com.iyuba.camstory.lycam.API.LivePackApi;
import com.iyuba.camstory.lycam.API.LiveSendMsgApi;
import com.iyuba.camstory.lycam.API.LoginApi;
import com.iyuba.camstory.lycam.API.MyLivePackApi;
import com.iyuba.camstory.lycam.API.PayForCourseApi;
import com.iyuba.camstory.lycam.API.PersonalBasicInfoApi;
import com.iyuba.camstory.lycam.API.PersonalDetailInfoApi;
import com.iyuba.camstory.lycam.API.RegistByEmailApi;
import com.iyuba.camstory.lycam.API.RegistByPhoneApi;
import com.iyuba.camstory.lycam.API.SubmitFeedbackApi;
import com.iyuba.configation.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
/**
 * 作者：renzhy on 16/6/22 16:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class IyuLiveRequestFactory {

	private static LivePackApi livePackApi;
	private static LiveContentApi liveContentApi;
	private static LoginApi loginApi;
	private static PersonalBasicInfoApi personalBasicInfoApi;
	private static PersonalDetailInfoApi personalDetailInfoApi;
	private static CheckPhoneApi checkPhoneApi;
	private static RegistByPhoneApi registByPhoneApi;
	private static RegistByEmailApi registByEmailApi;
	private static MyLivePackApi myLivePackApi;
	private static GetPayedRecordApi getPayedRecordApi;
	private static GetIyuStreamInfoApi getIyuStreamInfoApi;
	private static LiveSendMsgApi liveSendMsgApi;
	private static CheckIyubiApi checkIyubiApi;
	private static PayForCourseApi payForCourseApi;
	private static SubmitFeedbackApi submitFeedbackApi;

	private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
	private static OkHttpClient okHttpClient = new OkHttpClient();
	private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
	private static Converter.Factory xmlConverterFactory = SimpleXmlConverterFactory.create();

	public static void initOkHttpClient() {
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(15, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build();
	}

	public static LivePackApi getLivePackApi() {
		if (livePackApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://class."+Constant.IYBHttpHead+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			livePackApi = retrofit.create(LivePackApi.class);
		}
		return livePackApi;
	}

	public static LiveContentApi getLiveContentApi() {
		if (liveContentApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://class."+Constant.IYBHttpHead+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			liveContentApi = retrofit.create(LiveContentApi.class);
		}
		return liveContentApi;
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

	public static PersonalBasicInfoApi getPersonalBasicInfoApi() {
		if (personalBasicInfoApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			personalBasicInfoApi = retrofit.create(PersonalBasicInfoApi.class);
		}
		return personalBasicInfoApi;
	}

	public static PersonalDetailInfoApi getPersonalDetailInfoApi() {
		if (personalDetailInfoApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			personalDetailInfoApi = retrofit.create(PersonalDetailInfoApi.class);
		}
		return personalDetailInfoApi;
	}

	public static CheckPhoneApi getCheckPhoneApi() {
		if (checkPhoneApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			checkPhoneApi = retrofit.create(CheckPhoneApi.class);
		}
		return checkPhoneApi;
	}

	public static RegistByPhoneApi getRegistByPhoneApi() {
		if (registByPhoneApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			registByPhoneApi = retrofit.create(RegistByPhoneApi.class);
		}
		return registByPhoneApi;
	}

	public static RegistByEmailApi getRegistByEmailApi() {
		if (registByEmailApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://api."+Constant.IYBHttpHead2+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			registByEmailApi = retrofit.create(RegistByEmailApi.class);
		}
		return registByEmailApi;
	}

	public static MyLivePackApi getMyLivePackApi() {
		if (myLivePackApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://class."+Constant.IYBHttpHead+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			myLivePackApi = retrofit.create(MyLivePackApi.class);
		}
		return myLivePackApi;
	}

	public static GetPayedRecordApi getPayedRecordApi() {
		if (getPayedRecordApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://app."+Constant.IYBHttpHead+"/")
					.addConverterFactory(xmlConverterFactory)
					.build();
			getPayedRecordApi = retrofit.create(GetPayedRecordApi.class);
		}
		return getPayedRecordApi;
	}

	public static GetIyuStreamInfoApi getIyuStreamInfoApi() {
		if (getIyuStreamInfoApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
//					.baseUrl("http://iyuba-app-server-dev-515601597.cn-north-1.elb.amazonaws.com.cn/")
					.baseUrl("http://iyuba-app-server-prod-1095596486.cn-north-1.elb.amazonaws.com.cn")
					.addConverterFactory(gsonConverterFactory)
					.build();
			getIyuStreamInfoApi = retrofit.create(GetIyuStreamInfoApi.class);
		}
		return getIyuStreamInfoApi;
	}

	public static LiveSendMsgApi getLiveSendMsgApi() {
		if (liveSendMsgApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
//					.baseUrl("http://iyuba-app-server-dev-515601597.cn-north-1.elb.amazonaws.com.cn/")
					.baseUrl("http://iyuba-app-server-prod-1095596486.cn-north-1.elb.amazonaws.com.cn")
					.addConverterFactory(gsonConverterFactory)
					.build();
			liveSendMsgApi = retrofit.create(LiveSendMsgApi.class);
		}
		return liveSendMsgApi;
	}

	public static CheckIyubiApi getCheckIyubiApi() {
		if (checkIyubiApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://app."+Constant.IYBHttpHead+"/")
					.addConverterFactory(gsonConverterFactory)
					.build();
			checkIyubiApi = retrofit.create(CheckIyubiApi.class);
		}
		return checkIyubiApi;
	}

	public static PayForCourseApi getPayForCourseApi() {
		if (payForCourseApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://app."+Constant.IYBHttpHead+"/")
					.addConverterFactory(xmlConverterFactory)
					.build();
			payForCourseApi = retrofit.create(PayForCourseApi.class);
		}
		return payForCourseApi;
	}

	public static SubmitFeedbackApi getSubmitFeedbackApi() {
		if (submitFeedbackApi == null) {
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
//					.addConverterFactory(gsonConverterFactory)
					.baseUrl("http://api."+Constant.IYBHttpHead+"/")
					.build();
			submitFeedbackApi = retrofit.create(SubmitFeedbackApi.class);
		}
		return submitFeedbackApi;
	}
}
