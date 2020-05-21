package com.yilian.mall.ui.mvp.presenter.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.impl.UserMoneyModelImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IUserMoneyPresenter;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.MyCardEntity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author xiaofo on 2018/7/8.
 */

public class UserMoneyPresenterImpl implements IUserMoneyPresenter {

    private final UserMoneyModelImpl userMoneyModel;
    private final IUserMoneyView mUserMoneyView;

    public UserMoneyPresenterImpl(IUserMoneyView userMoneyView) {
        this.mUserMoneyView = userMoneyView;
        userMoneyModel = new UserMoneyModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getUserMoney(Context context) {
        mUserMoneyView.startMyDialog(false);
        return userMoneyModel.getUserMoney(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyBalanceEntity2>() {
                    @Override
                    public void onCompleted() {
                        mUserMoneyView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mUserMoneyView.stopMyDialog();
                        mUserMoneyView.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MyBalanceEntity2 myBalanceEntity2) {
                        mUserMoneyView.getUserMoneySuccess(myBalanceEntity2);
                    }
                });
    }
}
