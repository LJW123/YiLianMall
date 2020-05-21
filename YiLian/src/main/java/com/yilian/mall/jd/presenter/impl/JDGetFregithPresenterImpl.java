package com.yilian.mall.jd.presenter.impl;

import android.content.Context;
import android.support.annotation.Nullable;

import com.yilian.mall.jd.enums.JdShoppingCartType;
import com.yilian.mall.jd.presenter.JDGetFreightPresenter;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.networkingmodule.entity.jd.JDFreightEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitService;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/27.
 */

public class JDGetFregithPresenterImpl implements JDGetFreightPresenter {
    private final JdShoppingCartType jdShoppingCartType;
    private View view;

    public JDGetFregithPresenterImpl(View view, JdShoppingCartType jdShoppingCartType) {
        this.view = view;
        this.jdShoppingCartType = jdShoppingCartType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getFreight(Context context, @Nullable String skuId, int count, @Nullable String cartList, String addressId,int jdType) {
        view.startMyDialog(false);
        Observer<JDFreightEntity> freightChangedObserver = new Observer<JDFreightEntity>() {
            @Override
            public void onCompleted() {
                view.stopMyDialog();

            }

            @Override
            public void onError(Throwable e) {
                view.stopMyDialog();
                view.showToast(e.getMessage());
            }

            @Override
            public void onNext(JDFreightEntity o) {
                view.jdFreightChanged(o);
            }
        };
        RetrofitService retrofitService = RetrofitUtils3.getRetrofitService(context);
        Observable jdFreightData = null;
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                jdFreightData = retrofitService
                        .getJDFreightData("jd_orders/jd_order_freight", skuId, count, addressId);
                break;
            case FROM_SHOPPING_CART:
                if(jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
                    jdFreightData = retrofitService
                            .getJDFreightDataFromShoppingCart("jd_orders/jd_order_freight_cardcart", cartList, addressId);
                }else {
                    jdFreightData = retrofitService
                            .getJDFreightDataFromShoppingCart("jd_orders/jd_order_freight_cart", cartList, addressId);
                }
                break;

            default:
                break;
        }
        return jdFreightData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(freightChangedObserver);
    }
}
