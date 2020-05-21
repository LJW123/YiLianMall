package com.yilian.networkingmodule.retrofitutil;

import android.content.Context;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by  on 2017/8/14 0014.
 * Rxjava请求网络时使用
 */

public class MyGsonResponseBodyConverter2<T extends HttpResultBean> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;
    private Context context;

    public MyGsonResponseBodyConverter2(Gson gson, Type type, Context context) {
        this.gson = gson;
        this.type = type;
        this.context = context;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        HttpResultBean httpResultBean = gson.fromJson(response, HttpResultBean.class);
        try {
            int code = httpResultBean.code;
            if (code == 1) {
                return gson.fromJson(response, type);
            } else if (code == -4 || code == -23) {
                //刷新个人页面标识
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
                throw new CodeException("账号已掉线,请重新登录", code);
            } else {
                CodeException codeException = new CodeException(httpResultBean.msg, code);
                Logger.i("请求错误 codeException:" + codeException.toString());
                throw codeException;
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            value.close();
        }
    }
}
