package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.networkingmodule.entity.BasicInformationEntity;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IBasicInformationPresenter extends IBasePresenter {
    Subscription getData(Context context);

    Subscription saveData(Context context, BasicInformationEntity.DataBean dataBean);
}
