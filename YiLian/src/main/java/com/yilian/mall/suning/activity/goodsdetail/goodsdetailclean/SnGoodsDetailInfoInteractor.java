package com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean;

import com.yilian.mall.clean.Interactor;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;

/**
 * @author Created by  on 2018/5/23.
 */

public interface SnGoodsDetailInfoInteractor extends Interactor {
    interface Callback{
        void successCallback(SnGoodsDetailInfoEntity snGoodsDetailInfo);
        void failedCallback(String error);
    }

}
