package com.yilian.mall.ui.mvp.presenter.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.impl.PayTypeModelImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IGetPayTypePresenter;
import com.yilian.mall.ui.mvp.view.inter.IPayTypeView;
import com.yilian.networkingmodule.entity.PayTypeEntity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author xiaofo on 2018/7/8.
 */

public class GetPayTypePresenterImpl implements IGetPayTypePresenter {
    private final PayTypeModelImpl userMoneyModel;
    private final IPayTypeView mPayTypeView;

    public GetPayTypePresenterImpl(IPayTypeView payTypeView) {
        this.mPayTypeView = payTypeView;
        userMoneyModel = new PayTypeModelImpl();
    }
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getPayType(Context context) {
        mPayTypeView.startMyDialog();
        return userMoneyModel.getPayType(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PayTypeEntity>() {
                    @Override
                    public void onCompleted() {
                        mPayTypeView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPayTypeView.stopMyDialog();
                        mPayTypeView.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(PayTypeEntity payTypeEntity) {
                        mPayTypeView.getPayTypeListSuccess(payTypeEntity);
                    }
                }) ;
    }

    @Override
    public void onDestory() {
        
    }
}
