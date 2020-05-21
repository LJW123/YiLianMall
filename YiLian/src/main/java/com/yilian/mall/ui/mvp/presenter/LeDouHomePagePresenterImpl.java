package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.ILeDouHomePageModel;
import com.yilian.mall.ui.mvp.model.LeDouHomePageModelImpl;
import com.yilian.mall.ui.mvp.view.ILeDouHomePageView;
import com.yilian.networkingmodule.entity.LeDouHomePageDataEntity;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/2/12.
 */

public class LeDouHomePagePresenterImpl implements ILeDouHomePagePresenter {
    private final ILeDouHomePageModel leDouHomePageModel;
    private ILeDouHomePageView iLeDouHomePageView;

    public LeDouHomePagePresenterImpl(ILeDouHomePageView iLeDouHomePageView) {
        this.iLeDouHomePageView = iLeDouHomePageView;
        leDouHomePageModel = new LeDouHomePageModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @Override
    public Subscription getData(Context context, int page, int beanType) {
        iLeDouHomePageView.startMyDialog();
        return leDouHomePageModel.getData(context, page, beanType, new Observer<LeDouHomePageDataEntity>() {
            @Override
            public void onCompleted() {
                iLeDouHomePageView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iLeDouHomePageView.stopMyDialog();
                iLeDouHomePageView.showToast(e.getMessage());
            }

            @Override
            public void onNext(LeDouHomePageDataEntity leDouHomePageDataEntity) {
                iLeDouHomePageView.setData(leDouHomePageDataEntity);
            }
        });
    }
}
