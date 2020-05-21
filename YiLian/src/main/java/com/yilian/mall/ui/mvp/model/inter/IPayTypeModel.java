package com.yilian.mall.ui.mvp.model.inter;

import android.content.Context;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IPayTypeModel {
    Observable getPayType(Context context);

}
