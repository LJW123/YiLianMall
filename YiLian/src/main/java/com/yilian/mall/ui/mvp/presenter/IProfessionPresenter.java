package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IProfessionPresenter extends IBasePresenter {
    public Subscription getProfessionData(Context context);
}
