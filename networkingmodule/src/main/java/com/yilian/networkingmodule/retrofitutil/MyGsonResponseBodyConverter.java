package com.yilian.networkingmodule.retrofitutil;

import com.google.gson.Gson;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by  on 2017/8/14 0014.
 */

public class MyGsonResponseBodyConverter<T extends HttpResultBean> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public MyGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            HttpResultBean httpResultBean = gson.fromJson(response, HttpResultBean.class);
            int code = httpResultBean.code;
            if (code == 1) {
                return gson.fromJson(response, type);
            } else {
                return (T) httpResultBean;
            }
        } finally {
            value.close();
        }
    }
}
