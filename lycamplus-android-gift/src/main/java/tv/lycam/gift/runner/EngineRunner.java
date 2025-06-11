package tv.lycam.gift.runner;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import tv.lycam.gift.consts.GEventCode;
import tv.lycam.gift.core.AndroidEventManager;
import tv.lycam.gift.core.EventManager;
import tv.lycam.gift.entity.GiftInfo;
import tv.lycam.gift.entity.Result;
import tv.lycam.gift.widget.danmaku.ScaleUtil;

/**
 * Created by lycamandroid on 16/5/31.
 */
public abstract class EngineRunner implements EventManager.OnEventRunner {
	public static final ApiEngine API_ENGINE;
	public static final AppEngine APP_ENGINE;
	public static Context context;
	protected static String apiServer = "http://api-236221752.cn-north-1.elb.amazonaws.com.cn";
	//protected static String appServer = "http://api.appserver.lycam.tv";
//	protected static String appServer = "http://54.222.136.211:3000";
	protected static String appServer = "http://api-sbkt.lycam.tv";
	public static GiftInfo gifts;


	static {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		API_ENGINE = new Retrofit.Builder()
				.baseUrl(apiServer)
				.addConverterFactory(JacksonConverterFactory.create(objectMapper))
				.build()
				.create(ApiEngine.class);
		APP_ENGINE = new Retrofit.Builder()
				.baseUrl(appServer)
				.addConverterFactory(JacksonConverterFactory.create(objectMapper))
				.build()
				.create(AppEngine.class);
	}

	public static void init(Context context, String apiServer) {
		AndroidEventManager manager = AndroidEventManager.getInstance();
		manager.registerEventRunner(GEventCode.Http_GetGifts, new GetGiftsRunner());
		manager.registerEventRunner(GEventCode.Http_SendChat, new SendChatRunner());
		manager.registerEventRunner(GEventCode.Http_SendLike, new SendLikeRunner());
		manager.registerEventRunner(GEventCode.Http_SendGift, new SendGiftRunner());
		manager.registerEventRunner(GEventCode.Http_SendBarrage, new SendBarrageRunner());
		manager.registerEventRunner(GEventCode.Http_DownloadZip, new DownloadRunner());
		if (!TextUtils.isEmpty(apiServer))
			EngineRunner.apiServer = apiServer;
		ScaleUtil.init(context);
		EngineRunner.context = context;
	}

	public static void init(Context context, String apiServer, String appServer) {
		init(context, apiServer);
		if (!TextUtils.isEmpty(appServer)) {
			EngineRunner.appServer = appServer;
		}
	}

	public interface Api {
		//        String getGiftList = "/gifts";
		// GET
		String getGiftList = "/v1/gifts";
		// POST
		String sendMessage = "/v1/rtm/chat";
		String sendGift = "/v1/gifts/send";
		String sendLike = "/v1/rtm/like";
		String sendBarrage = "/v1/rtm/barrage";
	}

	public interface App {
		// POST
		String sendMessage = "/rtm/chat";
		String sendLike = "/rtm/like";
		String sendBarrage = "/rtm/barrage";
	}

	public interface ApiEngine {
		/**
		 * 获得礼物列表
		 *
		 * @param authorization
		 * @return
		 */
		@GET(Api.getGiftList)
		Call<GiftInfo> getGiftList(
				@NonNull @Header("Authorization") String authorization
		);

		/**
		 * 发送消息
		 *
		 * @param authorization
		 * @return
		 */
		@POST(Api.sendMessage)
		Call<JSONObject> sendMessage(
				@NonNull @Header("Authorization") String authorization,
				@Query("topic") String topic,
				@Query("msg") String msg,
				@Query("userId") String userId
		);

		/**
		 * 送礼物
		 *
		 * @param authorization
		 * @return
		 */
		@POST(Api.sendGift)
		Call<Result> sendGift(
				@NonNull @Header("Authorization") String authorization,
				@Query("userId") String userId,
				@Query("giftId") String giftId,
				@Query("receiver") String receiver,
				@Query("topic") String topic
		);

		/**
		 * 点赞
		 *
		 * @param authorization
		 * @param topic
		 * @return
		 */
		@POST(Api.sendLike)
		Call<JSONObject> sendLike(
				@NonNull @Header("Authorization") String authorization,
				@Query("topic") String topic,
				@Query("userId") String userId
		);

		/**
		 * 发送弹幕
		 *
		 * @param authorization
		 * @param topic
		 * @return
		 */
		@POST(Api.sendBarrage)
		Call<JSONObject> sendBarrage(
				@NonNull @Header("Authorization") String authorization,
				@Query("topic") String topic,
				@Query("msg") String msg,
				@Query("userId") String userId
		);

		/**
		 * 下载zip动画集
		 *
		 * @param path
		 * @return
		 */
		@GET
		Call<ResponseBody> downloadZip(@Url String path);
	}

	public interface AppEngine {
		/**
		 * 发送消息
		 *
		 * @param authorization token
		 * @return
		 */
		@POST(App.sendMessage)
		Call<JSONObject> sendMessage(
				@NonNull @Header("Authorization") String authorization,
				@Query("topic") String topic,
				@Query("msg") String msg
		);

		/**
		 * 点赞
		 *
		 * @param authorization
		 * @param topic
		 * @return
		 */
		@POST(App.sendLike)
		Call<JSONObject> sendLike(
				@NonNull @Header("Authorization") String authorization,
				@Query("topic") String topic,
				@Query("userId") String userId
		);

		/**
		 * 发送弹幕
		 *
		 * @param authorization
		 * @param topic
		 * @return
		 */
		@POST(App.sendBarrage)
		Call<JSONObject> sendBarrage(
				@NonNull @Header("Authorization") String authorization,
				@Query("topic") String topic,
				@Query("msg") String msg
		);
	}
}
