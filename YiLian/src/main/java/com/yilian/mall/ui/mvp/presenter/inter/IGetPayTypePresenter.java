package com.yilian.mall.ui.mvp.presenter.inter;

import android.content.Context;

import com.yilian.mall.ui.mvp.presenter.IBasePresenter;

import rx.Subscription;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IGetPayTypePresenter extends IBasePresenter {
    Subscription getPayType(Context context);
}
