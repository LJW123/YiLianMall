package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.entity.BankCardEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/1/17.
 */

public class CertificationModelImpl implements ICertificationModel {
    @SuppressWarnings("unchecked")
    @Override
    public Subscription addAuth(Context context, String userName,String idCard, String smsCode, Observer<HttpResultBean> observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .addAuthV2("userAuth/IDCard_Auth_v2",  userName,  idCard,  smsCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }

    /**
     * 根据银行卡号获取银行卡信息
     *
     * @param context
     * @param bankCardNumber
     * @param observer
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getBankCardInfo(Context context, String bankCardNumber, Observer<BankCardEntity> observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .getBankCardInfo("userAuth/bank_card_info", bankCardNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }

    /**
     * 检查银行卡四元素
     *
     * @param context
     * @param cardNumber
     * @param userName
     * @param idCard 身份证号
     * @param phone
     * @param observer
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Subscription checkBankCard4Element(Context context, String cardNumber, String userName, String idCard, String phone, Observer<HttpResultBean> observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .checkBankCard4Element("userAuth/verify_bank_card4", cardNumber, userName, idCard, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }
}
