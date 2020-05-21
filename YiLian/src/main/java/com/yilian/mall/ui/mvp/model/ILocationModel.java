package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/16.
 */

public interface ILocationModel extends IBaseModel {
    /**
     * 获取地址列表
     *
     * @param context
     * @param observer
     * @return
     */
    Subscription getAddressList(Context context, Observer observer);

    /**
     * 根据经纬度获取系统自己维护的位置信息
     *
     * @param context
     * @param observer
     * @return
     */
    Subscription getLocation(Context context, Observer observer);
}
