package com.yilian.mall.jd.presenter.impl;

import android.content.Context;

import com.yilian.mall.jd.presenter.JDShippingAddressManagerPresenter;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressList;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/27.
 */

public class JDShippingAddressManagerPresenterImpl implements JDShippingAddressManagerPresenter {
    private View view;

    public JDShippingAddressManagerPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    /**
     * 设置JD默认收货地址
     * @param addressId
     * @param isDefault 1设置为默认地址 0取消设置为默认地址
     */
    public Subscription setDefaultAddress(Context context, String addressId, int isDefault) {
        view.startMyDialog(false);
        return RetrofitUtils3.getRetrofitService(context)
                .setJDDefaultShippingAddress("jd_address/jd_setdefault_address", addressId, isDefault)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        view.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.stopMyDialog();
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        view.updateShippingAddressStatus(addressId, isDefault);
                    }
                });
    }
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getShippingAddressList(Context context) {
        view.startRefresh();
        return RetrofitUtils3.getRetrofitService(context)
                .getJDShippingAddressList("jd_address/jd_useraddress_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDShippingAddressList>() {
                    @Override
                    public void onCompleted() {
                        view.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast(e.getMessage());
                        view.stopRefresh();
                    }

                    @Override
                    public void onNext(JDShippingAddressList o) {
                        view.refreshData(o.list);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription deleteShippingAddress(Context context, String addressId) {
        view.startMyDialog(false);
        return RetrofitUtils3.getRetrofitService(context)
                .deleteJdShippingAddress("jd_address/jd_delete_address", addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        view.stopMyDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.stopMyDialog();
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        view.deleteShippingAddress(addressId);
                    }
                })
                ;
    }


}
