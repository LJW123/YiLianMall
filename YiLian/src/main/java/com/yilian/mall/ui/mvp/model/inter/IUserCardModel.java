package com.yilian.mall.ui.mvp.model.inter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.IBaseModel;

import rx.Observable;

/**
 * @author Zg on 2018/1/8.
 */

public interface IUserCardModel extends IBaseModel {

    Observable getUserCard(Context context);

}
