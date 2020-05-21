package com.yilian.mall.jd.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.yilian.mall.jd.activity.goodsdetail.BaseView;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntities;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntities;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;

import rx.Subscription;

/**
 * @author Created by  on 2018/5/27.
 */

public interface JDCommitOrderPresenter {
    /**
     * 提交订单
     *
     * @param context
     * @param skuId
     * @param count
     * @param addressId
     * @param remark
     * @param price
     * @param imgPath
     * @param jdType    0不使用代购券 1 使用代购券
     * @return
     */
    Subscription commitOrder(Context context, @Nullable String skuId, int count, String addressId,
                             @Nullable String remark, @Nullable String price,
                             @Nullable String imgPath, @Nullable String cartList, String useDaiGouQuan,
                             int jdType);

    /**
     * 提交订单前检查库存是否足够
     *
     * @param context
     * @param skuId
     * @param count
     * @param cartList
     * @param addressId
     * @param jdType    0不使用代购券 1 使用代购券
     * @return
     */
    Subscription checkJDStore(Context context, @Nullable String skuId, int count, @Nullable String cartList, String addressId, int jdType);

    /**
     * 提交订单前检查价格是否改变
     *
     * @param context
     * @param skuId
     * @param jdType    0不使用代购券 1 使用代购券
     * @return
     */
    Subscription checkJDPrice(Context context, @Nullable String skuId, @Nullable String cartList,int jdType);

    /**
     * 获取jd默认收货地址
     *
     * @return
     */
    Subscription getDefaultAddress(Context context);

    interface View extends BaseView {
        void commitOrderSuccess(JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity);

        void checkJDPriceSuccess(JDCheckPriceEntity jdCheckPriceEntity);

        void checkJDPriceSuccess(JDCheckPriceEntities jdCheckPriceEntities);

        /**
         * 商品详情下单前检查库存 检查成功回调
         *
         * @param jdGoodsStoreEntity
         */
        void checkJDStoreSuccess(JDGoodsStoreEntity jdGoodsStoreEntity);

        void checkJDStoreSuccess(JDGoodsStoreEntities jdGoodsStoreEntities);

        void getDefaultShippingAddressSuccess(@Nullable JDShippingAddressInfoEntity.DataBean defaultShippingAddress);

    }
}
