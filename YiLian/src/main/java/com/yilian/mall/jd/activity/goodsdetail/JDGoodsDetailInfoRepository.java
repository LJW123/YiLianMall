package com.yilian.mall.jd.activity.goodsdetail;

import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;

import rx.Observable;

/**
 * @author Created by  on 2018/5/23.
 */

public interface JDGoodsDetailInfoRepository {
    Observable<JDGoodsDetailInfoEntity> getJDGoodsDetailInfo();
}
