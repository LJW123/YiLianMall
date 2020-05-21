package com.yilian.mall.suning.activity.goodsdetail.addressclean;

import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/18.
 */

public interface ISnDefaultShippingAddressInfoRepository {
    Observable<SnShippingAddressInfoEntity> getSnDefaultShippingAddress();

}
