package com.yilian.mylibrary;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * @author Created by  on 2017/11/21 0021.
 */

public class RxUtil {


    /**
     * 防止连点 两次点击间隔 1S
     *-
     * @param view
     * @param consumer
     * @return
     */
    public static Disposable clicks(View view, Consumer consumer) {
        return RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(consumer);
    }

    /**
     * @param view     设置间隔时间
     * @param second   单位秒
     * @param consumer
     * @return
     */
    public static Disposable clicks(View view, int second, Consumer consumer) {
        return RxView.clicks(view)
                .throttleFirst(second, TimeUnit.SECONDS)
                .subscribe(consumer);
    }
    /**
     * @param view     设置间隔时间
     * @param second   单位秒
     * @param consumer
     * @return
     */
    public static Disposable clicks(View view, long second, Consumer consumer) {
        return RxView.clicks(view)
                .throttleFirst(second, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }
    /**
     * 计时器
     *
     * @param durationSecond 倒计时时长 单位秒
     * @return
     */
    public static Observable<Long> countDown(int durationSecond) {
        if (durationSecond < 0) {
            durationSecond = 0;
        }
        final int finalTimeStamp = durationSecond;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return finalTimeStamp - aLong;
                    }
                })
                .take(finalTimeStamp + 1);
    }

}
