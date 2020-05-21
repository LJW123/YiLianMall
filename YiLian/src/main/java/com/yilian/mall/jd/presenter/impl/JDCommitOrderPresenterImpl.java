package com.yilian.mall.jd.presenter.impl;

import android.content.Context;
import android.support.annotation.Nullable;

import com.yilian.mall.jd.enums.JdShoppingCartType;
import com.yilian.mall.jd.presenter.JDCommitOrderPresenter;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntities;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntities;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.jd.JdDefaultAddressListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/27.
 *         View Dialog的弹出消失逻辑为:
 *         检测库存 startMyDialog 正常继续检查价格 正常再提交订单 订单提交完毕stopMyDialog
 *         中间任何一个步骤出错都stopMyDialog
 */

public class JDCommitOrderPresenterImpl implements JDCommitOrderPresenter {
    private final JdShoppingCartType jdShoppingCartType;
    private View view;

    public JDCommitOrderPresenterImpl(View view, JdShoppingCartType jdShoppingCartType) {
        this.view = view;
        this.jdShoppingCartType = jdShoppingCartType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription commitOrder(Context context, @Nullable String skuId, int count, String addressId,
                                    @Nullable String remark, @Nullable String price,
                                    @Nullable String imgPath, @Nullable String cartList,String useDaiGouQuan,int jdType) {
        Observable observable = null;
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                observable = RetrofitUtils3.getRetrofitService(context)
                        .commitJDOrder("jd_orders/jd_make_order", skuId, count, addressId, remark, price, imgPath,useDaiGouQuan,jdType);
                break;
            case FROM_SHOPPING_CART:
                if(jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
                    //购物卡  检查库存
                    observable = RetrofitUtils3.getRetrofitService(context)
                            .commitCardJDOrderShoppingList("jd_orders/jd_make_order_cardcart", cartList, addressId, useDaiGouQuan);
                }else {
                    observable = RetrofitUtils3.getRetrofitService(context)
                            .commitJDOrderShoppingCart("jd_orders/jd_make_order_cart", cartList, addressId, useDaiGouQuan);
                }
                break;
            default:
                break;
        }

        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDCommitOrderSuccessEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.stopMyDialog();
                        view.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDCommitOrderSuccessEntity o) {
                        view.stopMyDialog();
                        view.commitOrderSuccess(o);
                    }
                })
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription checkJDStore(Context context, String skuId, int count, @Nullable String cartList, String addressId,int jdType) {
        view.startMyDialog(false);
        Observable observable = null;
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                observable = RetrofitUtils3.getRetrofitService(context)
                        .checkJDStore("jd_goods/jd_goods_stock", skuId, count, addressId,jdType);
                break;
            case FROM_SHOPPING_CART:
                if(jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
                    observable = RetrofitUtils3.getRetrofitService(context)
                            .checkJDStoreShoppingCart("jd_goods/jd_goods_stock_cardcart", cartList, addressId);
                }else {
                    observable = RetrofitUtils3.getRetrofitService(context)
                            .checkJDStoreShoppingCart("jd_goods/jd_goods_stock_cart", cartList, addressId);
                }
                break;
            default:
                break;
        }

        Observer observer = new Observer() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                view.stopMyDialog();
                view.showToast(e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                switch (jdShoppingCartType) {
                    case FROM_GOODS_DETAIL:
                        view.checkJDStoreSuccess((JDGoodsStoreEntity) o);
                        break;
                    case FROM_SHOPPING_CART:
                        view.checkJDStoreSuccess((JDGoodsStoreEntities) o);
                        break;
                    default:
                        break;
                }
            }
        };
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription checkJDPrice(Context context, @Nullable String skuId, @Nullable String cartList,int jdType) {
        Observable observable = null;
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                observable = RetrofitUtils3.getRetrofitService(context)
                        .checkJDPrice("jd_goods/jd_goods_price", skuId,jdType);
                break;
            case FROM_SHOPPING_CART:
                if(jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
                    observable = RetrofitUtils3.getRetrofitService(context)
                            .checkJDPriceShoppingCart("jd_goods/jd_goods_price_cardcart", cartList);
                }else {
                    observable = RetrofitUtils3.getRetrofitService(context)
                            .checkJDPriceShoppingCart("jd_goods/jd_goods_price_cart", cartList);
                }
                break;

            default:
                break;
        }

        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.stopMyDialog();
                        view.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        switch (jdShoppingCartType) {
                            case FROM_GOODS_DETAIL:
                                view.checkJDPriceSuccess((JDCheckPriceEntity) o);
                                break;
                            case FROM_SHOPPING_CART:
                                view.checkJDPriceSuccess((JDCheckPriceEntities) o);
                                break;
                            default:
                                break;
                        }
                    }
                })
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getDefaultAddress(Context context) {
        return RetrofitUtils3.getRetrofitService(context)
                .getJdDefaultShippingAddress("jd_address/jd_getdefault_address")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JdDefaultAddressListEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getDefaultShippingAddressSuccess(null);
                    }

                    @Override
                    public void onNext(JdDefaultAddressListEntity addressList) {
                        List<JDShippingAddressInfoEntity.DataBean> data = addressList.data;
                        if (data.size()>0) {
                            view.getDefaultShippingAddressSuccess(data.get(0));
                        }else{
                            view.getDefaultShippingAddressSuccess(null);
                        }
                    }
                })
                ;
    }

}
