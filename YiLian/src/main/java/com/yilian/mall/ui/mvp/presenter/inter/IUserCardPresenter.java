package com.yilian.mall.ui.mvp.presenter.inter;

import android.content.Context;

import com.yilian.mall.ui.mvp.presenter.IBasePresenter;

import rx.Subscription;

/**
 * @author Zg on 2018/11/18.
 */

public interface IUserCardPresenter extends IBasePresenter {


    Subscription getUserCard(Context context);
}
