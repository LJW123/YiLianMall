package com.yilian.mall.jd.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.yilian.mall.jd.activity.goodsdetail.BaseView;
import com.yilian.networkingmodule.entity.jd.JDFreightEntity;

import rx.Subscription;

/**
 * @author Created by  on 2018/5/27.
 */

public interface JDGetFreightPresenter {
    /**
     * 获取运费
     * @param context
     * @param skuId
     * @param count
     * @param addressId
     * @return
     */
    Subscription getFreight(Context context, @Nullable String skuId, int count, @Nullable String cartList, String addressId,int jdType);

    interface View extends BaseView {
        /**
         * 运费变化后 根据运费显示界面
         * @param jdFreightEntity
         */
        void jdFreightChanged(JDFreightEntity jdFreightEntity);
    }
}
