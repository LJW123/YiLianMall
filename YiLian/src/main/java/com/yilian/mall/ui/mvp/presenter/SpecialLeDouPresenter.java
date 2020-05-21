package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.ISpecialLeDouModel;
import com.yilian.mall.ui.mvp.model.SpecialLeDouModelImpl;
import com.yilian.mall.ui.mvp.view.ISpecialLeDouView;
import com.yilian.networkingmodule.entity.LeDouHomePageDataEntity;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/2/12.
 */

public class SpecialLeDouPresenter implements ISpecialLeDouPresenter {
    private final ISpecialLeDouModel specialLeDouModel;
    private ISpecialLeDouView iSpecialLeDouView;

    public SpecialLeDouPresenter(ISpecialLeDouView iSpecialLeDouView) {
        this.iSpecialLeDouView = iSpecialLeDouView;
        specialLeDouModel = new SpecialLeDouModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @Override
    public Subscription getData(Context context, int page, int beanType, String sort, String classId) {
        iSpecialLeDouView.startMyDialog();
        return specialLeDouModel.getData(context, page, beanType, sort, classId, new Observer<LeDouHomePageDataEntity>() {
            @Override
            public void onCompleted() {
                iSpecialLeDouView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iSpecialLeDouView.stopMyDialog();
                iSpecialLeDouView.showToast(e.getMessage());
            }

            @Override
            public void onNext(LeDouHomePageDataEntity leDouHomePageDataEntity) {
                iSpecialLeDouView.setData(leDouHomePageDataEntity);
            }
        });
    }
}
