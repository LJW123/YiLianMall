package com.yilian.networkingmodule.retrofitutil;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.NetworkUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.Utils;
import com.yilian.networkingmodule.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by  on 2017/8/16 0016.
 */

public class RetrofitUtils3 {

    private static RetrofitService retrofitService;

    public static RetrofitService getRetrofitService(final Context context) {
        if (retrofitService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            if (BuildConfig.DEBUG) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)

                    .writeTimeout(30, TimeUnit.SECONDS)
// add your other interceptors …
//            网络不可用拦截器
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Utils.init(context);
                            boolean connected = NetworkUtils.isConnected();
                            if (connected) {
                                return chain.proceed(chain.request());
                            } else {
                                throw new NoNetworkException();
                            }
                        }
                    })
//                    统一参数拦截器
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request oldRequest = chain.request();
                            HttpUrl.Builder builder = oldRequest.url().newBuilder()
                                    .scheme(oldRequest.url().scheme())
                                    .host(oldRequest.url().host())
                                    .addQueryParameter("token", RequestOftenKey.getToken(context))
                                    .addQueryParameter("device_index", RequestOftenKey.getDeviceIndex(context));
                            Request newRequest = oldRequest.newBuilder().method(oldRequest.method(), oldRequest.body())
                                    .url(builder.build())
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    // add logging as last interceptor
                    .addInterceptor(logging) ; // <-- this is the important line!

//            TODO 为Retrofit添加统一请求参数
             Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Ip.getURL(context))
                    .addConverterFactory(MyGsonConverterFactory2.create(context.getApplicationContext()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }
}
