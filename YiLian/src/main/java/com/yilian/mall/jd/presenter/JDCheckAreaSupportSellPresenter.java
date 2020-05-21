package com.yilian.mall.jd.presenter;

import android.content.Context;

import com.yilian.mall.jd.activity.goodsdetail.BaseView;

import rx.Subscription;

/**
 * @author Created by  on 2018/5/29.
 */

public interface JDCheckAreaSupportSellPresenter {
    Subscription checkAreaSupportSell(Context context, String skuIds, String provinceId, String cityId, String countyId, String townId);

    interface View  extends BaseView{
        void supportAreaSell();

        void unSupportAreaSell();
    }
}
