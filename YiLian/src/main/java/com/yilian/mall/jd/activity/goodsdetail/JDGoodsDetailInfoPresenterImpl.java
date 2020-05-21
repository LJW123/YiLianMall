package com.yilian.mall.jd.activity.goodsdetail;

import com.yilian.mall.clean.MainThreadImpl;
import com.yilian.mall.clean.ThreadExecutor;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;

/**
 * @author Created by  on 2018/5/23.
 */

public class JDGoodsDetailInfoPresenterImpl implements JDGoodsDetailInfoPresenter, JDGoodsDetailInfoInteractor.Callback {
    private View view;

    public JDGoodsDetailInfoPresenterImpl(JDGoodsDetailInfoPresenter.View view) {
        this.view = view;
    }

    @Override
    public void getJdGoodsDetailInfo(String sku, String lat, String lng,String jdType) {
        view.startMyDialog(false);
        JDGoodsDetailInfoRepositoryImpl jdGoodsDetailRepository = new JDGoodsDetailInfoRepositoryImpl();
        jdGoodsDetailRepository.setParams(sku, lat, lng,jdType);
        JDGoodsDetailInfoInteractor jdGoodsDetailInteractor
                = new JDGoodsDetailInfoInteractorImpl(ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), this, jdGoodsDetailRepository);
        jdGoodsDetailInteractor.execute();
    }

    @Override
    public void successCallback(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
        view.showGoodsDetailInfo(jdGoodsDetailInfoEntity);
        view.stopMyDialog();
    }

    @Override
    public void failedCallback(String error) {
        view.showToast(error);
        view.stopMyDialog();
    }
}
