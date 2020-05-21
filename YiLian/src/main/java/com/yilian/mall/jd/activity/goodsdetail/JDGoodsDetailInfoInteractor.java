package com.yilian.mall.jd.activity.goodsdetail;

import com.yilian.mall.clean.Interactor;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;

/**
 * @author Created by  on 2018/5/23.
 */

public interface JDGoodsDetailInfoInteractor extends Interactor {
    interface Callback{
        void successCallback(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity);
        void failedCallback(String error);
    }

}
