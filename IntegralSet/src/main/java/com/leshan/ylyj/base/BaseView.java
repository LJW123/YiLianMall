package com.leshan.ylyj.base;


import rxfamily.entity.BaseEntity;

/**
 * 所有的请求model必须继承baseBean
 * @param <T>
 */

public interface BaseView<T extends BaseEntity> {
    /**
     * 对应网络请求的onCompleted()方法
     */
    void onCompleted();

    /**
     * 对应网络请求的onError(Throwable t)方法
     *
     * @param e
     */
    void onError(Throwable e);

    /**
     * 对应网络请求的onError(Throwable t)方法
     */
    void onErrors(int flag,Throwable e);

    /**
     * 对应网络请求的onNext(T t)方法
     * @param t
     */
    void onNext(T t);

}
