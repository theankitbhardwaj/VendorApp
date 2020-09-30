package com.ganesh.vendorapp.api;

import com.ganesh.vendorapp.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://sambalpurihaat.com/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();


    private RetrofitClient() {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public APIs getApi() {
        return retrofit.create(APIs.class);
    }
}
