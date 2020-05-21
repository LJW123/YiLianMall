package com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean;

import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;

import rx.Observable;

/**
 * @author Created by  on 2018/5/23.
 */

public interface SnGoodsDetailInfoRepository {
    Observable<SnGoodsDetailInfoEntity> getSnGoodsDetailInfo();
}
