package com.bdstar.taxi.net;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public enum  APIService {

    INSTANCE;

    private static final String API_URL = "http://47.112.105.76:6699/";
    //private static final String API_URL = "http://192.168.0.101:8080/";
    public static final String IMG_URL = "http://47.112.105.76:8080/";
    private Retrofit retrofit;

    APIService(){
        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    Log.e("JSON", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("JSON", message);
                }
            }
        });*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T createAPI(Class<T> api){

        return retrofit.create(api);
    }
}
