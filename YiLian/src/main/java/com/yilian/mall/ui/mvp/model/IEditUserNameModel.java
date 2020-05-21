package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.httpresult.HttpResultBean;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IEditUserNameModel<T extends HttpResultBean> extends IBaseModel {
    Subscription saveUserName(Context context, String data, Observer<T> observer);

    Subscription saveStateOfMind(Context context, String mind, Observer<T> observer);
}
