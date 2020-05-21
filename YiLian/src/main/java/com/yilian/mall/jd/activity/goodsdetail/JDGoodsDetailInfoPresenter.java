package com.yilian.mall.jd.activity.goodsdetail;

import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;

/**
 * @author Created by  on 2018/5/23.
 */

public interface JDGoodsDetailInfoPresenter {
    interface View extends BaseView {
        void showGoodsDetailInfo(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity);
    }
    void getJdGoodsDetailInfo(String sku,String lat,String lng,String jdType);
}
