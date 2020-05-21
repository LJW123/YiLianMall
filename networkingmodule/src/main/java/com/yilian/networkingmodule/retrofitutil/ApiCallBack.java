package com.yilian.networkingmodule.retrofitutil;

import android.support.annotation.Nullable;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by Ray_L_Pain on 2017/9/27 0027.
 */

public abstract class ApiCallBack<M> extends Subscriber<M> {
    public abstract void onSuccess(M model);
    public abstract void onFailure(@Nullable String msg);
    public abstract void onFinish();

    @Override
    public void onCompleted() {
        onFinish();
    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            String msg = httpException.message();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后重试";
            }
            onFailure(msg);
        } else {
            onFailure(e.toString());
        }
        onFinish();
    }
}
