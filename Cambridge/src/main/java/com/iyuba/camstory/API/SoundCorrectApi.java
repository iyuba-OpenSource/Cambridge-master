package com.iyuba.camstory.API;

import com.iyuba.camstory.API.data.WordCorrectResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SoundCorrectApi {
    @GET("apiWordAi.jsp")
    Call<WordCorrectResponse> apiWordAi(@Query("q") String q, @Query("user_pron") String user_pron, @Query("ori_pron") String ori_pron);
}
