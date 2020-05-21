package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.entity.BankCardEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/17.
 */

public interface ICertificationModel extends IBaseModel {
    /**
     * 添加认证
     *
     * @param context
     * @param userName
     * @param idCard
     * @param smsCode
     * @param observer
     * @return
     */
    Subscription addAuth(Context context, String userName,String idCard, String smsCode, Observer<HttpResultBean> observer);

    /**
     * 获取银行卡信息
     */
    Subscription getBankCardInfo(Context context, String bankCardNumber, Observer<BankCardEntity> observer);

    /**
     * 匹配银行卡四元素
     *
     * @param context
     * @param cardNumber
     * @param userName
     * @param idCard
     * @param phone
     * @return
     */
    Subscription checkBankCard4Element(Context context, String cardNumber, String userName, String idCard, String phone, Observer<HttpResultBean> observer);
}
