package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/2/12.
 */

public interface ILeDouHomePagePresenter extends IBasePresenter {
    Subscription getData(Context context, int page, int beanType);
}
