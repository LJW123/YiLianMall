package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/16.
 */

public interface ILocationPresenter extends IBasePresenter {
    /**
     * 获取省市县三级列表数据
     *
     * @param context
     * @return
     */
    Subscription getAddressList(Context context);

    /**
     * 获取系统自己维护的位置信息
     *
     * @param context
     * @return
     */
    Subscription getLocation(Context context);
}
