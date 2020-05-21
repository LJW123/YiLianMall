package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/21.
 */

public interface ISupportBankListPresenter extends IBasePresenter {
    Subscription getSupportBankList(Context context);
}
