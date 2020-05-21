package com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean;

import android.content.Context;

import com.yilian.mall.clean.MainThreadImpl;
import com.yilian.mall.clean.ThreadExecutor;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;

import java.util.Map;

/**
 * @author Created by  on 2018/5/23.
 */

public class SnGoodsDetailInfoPresenterImpl implements
        SnGoodsDetailInfoPresenter, SnGoodsDetailInfoInteractor.Callback {
    private View view;

    public SnGoodsDetailInfoPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public void getJdGoodsDetailInfo(Map<String, String> params, Context mContext) {
        view.startMyDialog(false);
        SnGoodsDetailInfoRepositoryImpl jdGoodsDetailRepository = new SnGoodsDetailInfoRepositoryImpl(mContext);
        jdGoodsDetailRepository.setParams(params);
        SnGoodsDetailInfoInteractor jdGoodsDetailInteractor
                = new SnGoodsDetailInfoInteractorImpl(ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), this, jdGoodsDetailRepository);
        jdGoodsDetailInteractor.execute();
    }

    @Override
    public void successCallback(SnGoodsDetailInfoEntity snGoodsDetailInfo) {
        view.getGoodsDetailInfoSuccess(snGoodsDetailInfo);
        view.stopMyDialog();
    }

    @Override
    public void failedCallback(String error) {
        view.showToast(error);
        view.stopMyDialog();
    }
}
