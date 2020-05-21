package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.IProfessionModel;
import com.yilian.mall.ui.mvp.model.ProfessionModelImpl;
import com.yilian.mall.ui.mvp.view.IProfessionView;
import com.yilian.networkingmodule.entity.ProfessionEntity;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public class ProfessionPresenterImpl implements IProfessionPresenter {
    private final IProfessionModel iProfessionModel;
    private IProfessionView iProfessionView;

    public ProfessionPresenterImpl(IProfessionView iProfessionView) {
        this.iProfessionView = iProfessionView;
        iProfessionModel = new ProfessionModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @Override
    public Subscription getProfessionData(Context context) {
        iProfessionView.startMyDialog();
        return iProfessionModel.getProfessionData(context, new Observer<ProfessionEntity>() {
            @Override
            public void onCompleted() {
                iProfessionView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iProfessionView.stopMyDialog();
                iProfessionView.showToast(e.getMessage());
            }

            @Override
            public void onNext(ProfessionEntity professionEntity) {
                iProfessionView.setData(professionEntity);
            }
        });
    }
}
