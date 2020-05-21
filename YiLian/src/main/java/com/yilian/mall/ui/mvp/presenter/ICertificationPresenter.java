package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/17.
 */

public interface ICertificationPresenter extends IBasePresenter {
    /**
     * 添加认证
     *
     * @param context
     * @return
     */
    Subscription addAuth(Context context);

    /**
     * 获取银行卡信息
     */
    Subscription getBankCardInfo(Context context);

    /**
     * 检测银行卡四元素
     */
    Subscription checkBankCard4Element(Context context);


}
