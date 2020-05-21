package com.yilian.networkingmodule.retrofitutil;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by  on 2017/8/14 0014.
 * Rxjava请求网络时使用
 */

public class MyGsonConverterFactory2 extends Converter.Factory {
    private static Context context;
    private final Gson gson;

    private MyGsonConverterFactory2(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson==null");
        }
        this.gson = gson;
    }

    public static MyGsonConverterFactory2 create(Context context) {
        MyGsonConverterFactory2.context = context;
        return create(new Gson());
    }

    public static MyGsonConverterFactory2 create(Gson gson) {
        return new MyGsonConverterFactory2(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//        return new MyGsonResponseBodyConverter2<>(gson, type, context);
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonResponseBodyConverter<>(gson, adapter, context);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson,adapter);
    }
}
