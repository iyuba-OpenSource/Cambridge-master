package com.iyuba.camstory.API;

import com.iyuba.camstory.API.data.SendEvaluateResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.network
 * @class describe
 * @time 2018/12/28 16:14
 * @change
 * @chang time
 * @class describe
 */
public interface EvaluateApi {
    @Multipart
    @POST("eval/")
    Call<SendEvaluateResponse> sendVoice(@PartMap Map<String, RequestBody> optionMap,
                                                @Part MultipartBody.Part file);

   /* @Multipart
    @POST("ai/")
    Call<SendEvaluateResponse> sendVoice(@PartMap Map<String, RequestBody> optionMap,
                                         @Part MultipartBody.Part file);*/

//    @FormUrlEncoded
//    @POST("merge/")
//    Observable<EvaMixBean> audioComposeApi(@Field(value = "audios",encoded = true) String audios, @Field("type") String type);



    interface GetVoa {
        interface Param {
            interface Key {
                String TYPE = "type";
                String SENTENCE = "sentence";
                String USERID = "userId";
                String NEWSID = "newsId";
                String PARAID = "paraId";
                String IDINDEX = "IdIndex";
                String FLG = "flg";
                String APPID = "appId";
                String WORDID = "wordId";
            }

            interface Value {
                String TYPE = "android";
                String FORMAT = "json";

                int PAGE_NUM = 1;
                //                int PAGE_SIZE = 10000;
                int PAGE_SIZE = 600; //减少请求个数
                int RECENT_VOA_ID = 0;
            }
        }
    }
}
