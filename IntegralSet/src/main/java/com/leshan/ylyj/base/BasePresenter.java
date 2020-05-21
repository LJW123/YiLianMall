package com.leshan.ylyj.base;

import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rxfamily.entity.BaseEntity;


/**
 * @author ASUS
 */
public class BasePresenter<T extends BaseEntity> {

    protected CompositeSubscription mCompositeSubscription;

    protected BaseView mView;

    public int mFlag;

    public int getFlag() {
        return mFlag;
    }

    public void attachView(BaseView iView) {
        mView = iView;
    }

    public void detachView() {
        if (mView != null) {
            mView = null;
        }
        onUnsubscribe();
    }

    /**
     * RXjava取消注册，以避免内存泄露
     */
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
            Log.d("BasePresenter", "onUnsubscribe");
        }
    }

    @SuppressWarnings("unchecked")
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        Subscription subscribe = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        mCompositeSubscription.add(subscribe);
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
    /**
     *  @net 抽离观察者
     *  执行顺序：
     *  1.成功onNext--onComplate
     *  2.错误：onError
     *
     */
    protected Observer observer=new Observer<T>() {
        @Override
        public void onCompleted() {
            mView.onCompleted();
        }

        @Override
        public void onError(Throwable e) {

            mView.onError(e);
            mView.onErrors(mFlag,e);
        }

        @Override
        public void onNext(T baseBean) {
            mView.onNext(baseBean);
        }
    };

}