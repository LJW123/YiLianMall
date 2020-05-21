package com.yilianmall.merchant.utils;

import android.content.Context;

import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.MerchantAfterSaleOrderNumber;
import com.yilian.networkingmodule.entity.MerchantOrderNumber;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by  on 2017/10/11 0011.
 */

public class UpdateMerchantOrderNumber {

    @SuppressWarnings("unchecked")
    public static Subscription getMerchantOrderNumber(final Context context) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).getMerchantOrderNumber("supplier_mallorder_num", RequestOftenKey.getToken(context), RequestOftenKey.getDeviceIndex(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantOrderNumber>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MerchantOrderNumber merchantOrderNumber) {
                        MerchantOrderNumber.DataBean data = merchantOrderNumber.data;
                        /**
                         * {@link com.yilianmall.merchant.activity.MerchantOrderListActivity}
                         */
                        EventBus.getDefault().post(data);

                    }
                });
        return subscription;
    }

    @SuppressWarnings("unchecked")
    public static Subscription getMerchantAfterSaleOrderNumber(final Context context) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getMerchantAfterSaleOrderNumber("supplier_service_num", RequestOftenKey.getToken(context), RequestOftenKey.getDeviceIndex(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantAfterSaleOrderNumber>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MerchantAfterSaleOrderNumber merchantAfterSaleOrderNumber) {
                        MerchantAfterSaleOrderNumber.DataBean data = merchantAfterSaleOrderNumber.data;
                        /**
                         * {@link com.yilianmall.merchant.activity.MerchantAfterSaleListActivity}
                         */
                        EventBus.getDefault().post(data);

                    }
                });
        return subscription;
    }
}
