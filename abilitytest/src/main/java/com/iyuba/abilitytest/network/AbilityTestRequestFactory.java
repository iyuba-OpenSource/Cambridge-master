package com.iyuba.abilitytest.network;

import com.iyuba.abilitytest.entity.AddCreditModule;
import com.iyuba.abilitytest.entity.ExamDetail;
import com.iyuba.abilitytest.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 工厂
 * Created by liuzhenli on 2017/5/16.
 */

public class AbilityTestRequestFactory {
    private static final String TAG = "AbilityTestRequestFactory";
    private static OkHttpClient okHttpClient;
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.e(TAG, message);
        }
    });


    private static void initHttpClient() {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .build();
    }

    private static ExamDetailApi examDetailApi;

    public static ExamDetailApi getExamDetailApi() {
        if (examDetailApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue."+com.iyuba.configation.Constant.IYBHttpHead+"/")
                    .build();
            examDetailApi = retrofit.create(ExamDetailApi.class);
        }
        return examDetailApi;
    }

    private static TestQuestionApi testQuestionApi;

    public static TestQuestionApi getTestQuestionApi() {
        if (testQuestionApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://class."+com.iyuba.configation.Constant.IYBHttpHead+"/")
                    .build();
            testQuestionApi = retrofit.create(TestQuestionApi.class);
        }
        return testQuestionApi;
    }

    private static ExamScoreApi examScoreApi;

    public static ExamScoreApi getExamScoreApi() {
        if (examScoreApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue."+com.iyuba.configation.Constant.IYBHttpHead+"/")
                    .build();
            examScoreApi = retrofit.create(ExamScoreApi.class);
        }
        return examScoreApi;
    }

    private static AddCreditApi addCreditApi;

    public static AddCreditApi addCredit() {
        if (addCreditApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue."+com.iyuba.configation.Constant.IYBHttpHead+"/")
                    .build();
            addCreditApi = retrofit.create(AddCreditApi.class);
        }
        return addCreditApi;
    }

}
