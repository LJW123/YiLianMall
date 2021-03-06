package com.yilian.mall.jd.presenter;

import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.yilian.mall.jd.activity.goodsdetail.BaseView;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/5/28.
 */

public interface JDOrderPayPresenter {
    /**
     * 支付宝支付成功
     */
    public static final String ALI_PAY_SUCCESS = "9000";
    /**
     * 支付宝支付支付结果待确认
     */
    public static final String ALI_PAY_UN_CONFIRM = "8000";
    /**
     * 没有支付宝插件
     */
    public static final String ALI_PAY_NO_ALI = "4000";
    /**
     * 奖励支付
     */
    public static final int CASH_PAY = 0;
    /**
     * 第三方支付
     */
    public static final int THIRD_PAY = 1;
    /**
     * 混合支付
     */
    public static final int MIX_PAY = 2;

    /**
     * 支付
     *
     * @param payType
     */
    void pay(int payType,float thirdFee);

    interface View extends BaseView {
        boolean hasPassword();
        void paySuccess(HttpResultBean httpResultBean);

        void showSystemV7Dialog(@Nullable String title, @Nullable String message, @Nullable String positiveText
                , @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onClickListener);
    }

}
