package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/2/12.
 */

public interface ILeDouHomePageModel extends IBaseModel {
    Subscription getData(Context context, int page, int beanType, Observer observer);
}
