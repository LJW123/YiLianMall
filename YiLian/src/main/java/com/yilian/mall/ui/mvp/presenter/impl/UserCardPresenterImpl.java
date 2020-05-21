package com.yilian.mall.ui.mvp.presenter.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.impl.UserCardModelImpl;
import com.yilian.mall.ui.mvp.model.impl.UserMoneyModelImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IUserCardPresenter;
import com.yilian.mall.ui.mvp.view.inter.IUserCardView;
import com.yilian.networkingmodule.entity.MyCardEntity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Zg on 2018/11/18.
 */

public class UserCardPresenterImpl implements IUserCardPresenter {

    private final UserCardModelImpl userCardModel;
    private final IUserCardView mUserCardView;

    public UserCardPresenterImpl(IUserCardView mUserCardView) {
        this.mUserCardView = mUserCardView;
        userCardModel = new UserCardModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getUserCard(Context context) {
        mUserCardView.startMyDialog(false);
        return userCardModel.getUserCard(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyCardEntity>() {
                    @Override
                    public void onCompleted() {
                        mUserCardView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mUserCardView.stopMyDialog();
                        mUserCardView.showToast(e.getMessage());
                        mUserCardView.getUserCardError(e.getMessage());
                    }

                    @Override
                    public void onNext(MyCardEntity myCardEntity) {
                        mUserCardView.getUserCardSuccess(myCardEntity);
                    }
                });
    }
}
