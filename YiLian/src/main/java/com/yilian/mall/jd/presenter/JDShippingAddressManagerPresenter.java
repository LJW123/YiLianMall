package com.yilian.mall.jd.presenter;

import android.content.Context;

import com.yilian.mall.jd.activity.goodsdetail.BaseView;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;

import java.util.List;

import rx.Subscription;

/**
 * @author Created by  on 2018/5/27.
 */

public interface JDShippingAddressManagerPresenter {

    Subscription setDefaultAddress(Context context, String addressId, int isDefault);

    Subscription getShippingAddressList(Context context);

    Subscription deleteShippingAddress(Context context, String addressId);

    interface View extends BaseView {

        void getShippingAddressList();

        void startRefresh();

        void updateShippingAddressStatus(String addressId, int isDefault);

        void deleteShippingAddress(String addressId);

        void stopRefresh();

        void refreshData(List<JDShippingAddressInfoEntity.DataBean> o);
    }

}
