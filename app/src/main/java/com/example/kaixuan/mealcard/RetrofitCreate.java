package com.example.kaixuan.mealcard;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public  class RetrofitCreate {
    private static final int TIME_OUT =30;

    public RequestUrl getRetrofit(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.126.92.214:8083/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RequestUrl requestUrl = retrofit.create(RequestUrl.class);
        return requestUrl;
    }
}
