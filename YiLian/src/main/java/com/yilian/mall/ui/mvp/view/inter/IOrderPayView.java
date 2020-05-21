package com.yilian.mall.ui.mvp.view.inter;

import com.yilian.mall.ui.mvp.view.IBaseView;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.networkingmodule.entity.GoodsChargeForPayResultEntity;
import com.yilian.networkingmodule.entity.PayOkEntity;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IOrderPayView extends IBaseView {
    void cashPaySuccess(PayOkEntity payOkEntity);
    void payFailed(String msg);
    void createGoodsPrepareThirdPayOrder(JsPayClass paymentIndexEntity);
    void getPayResultSuccess(GoodsChargeForPayResultEntity goodsChargeForPayResultEntity);
}
