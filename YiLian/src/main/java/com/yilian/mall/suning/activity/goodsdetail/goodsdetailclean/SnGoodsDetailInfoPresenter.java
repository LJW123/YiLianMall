package com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean;

import android.content.Context;

import com.yilian.mall.suning.activity.goodsdetail.BaseView;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;

import java.util.Map;

/**
 * @author Created by  on 2018/5/23.
 */

public interface SnGoodsDetailInfoPresenter {
    interface View extends BaseView {
        void getGoodsDetailInfoSuccess(SnGoodsDetailInfoEntity snGoodsDetailInfo);
    }
    void getJdGoodsDetailInfo(Map<String, String> parama, Context mContext);
}
