package com.yilian.mall.suning.activity.goodsdetail.addressclean;

import com.yilian.mall.clean.Interactor;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;

/**
 * @author xiaofo on 2018/7/18.
 */

public interface ISnDefaultShippingAddressInfoInteractor extends Interactor {
    interface Callback{
        void getShippingAddressSuccess(SnShippingAddressInfoEntity snShippingAddressInfoEntity);
        void getShippingAddressError(String errorMsg);
    }
}
