package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IProfessionModel extends IBaseModel {
    public Subscription getProfessionData(Context context, Observer observer);

}
