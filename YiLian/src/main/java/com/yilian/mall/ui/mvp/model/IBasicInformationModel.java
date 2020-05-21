package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.entity.BasicInformationEntity;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IBasicInformationModel extends IBaseModel {
    Subscription getData(Context context, Observer observer);

    Subscription setData(Context context, BasicInformationEntity.DataBean dataBean, Observer observer);
}
