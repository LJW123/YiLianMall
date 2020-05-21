package com.yilian.mall.suning.activity.goodsdetail.addressclean;

import android.content.Context;

import com.yilian.mall.clean.MainThreadImpl;
import com.yilian.mall.clean.ThreadExecutor;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;

/**
 * @author xiaofo on 2018/7/18.
 */

public class SnDefaultShippingAddressPresenterImpl implements
        SnDefaultShippingAddressContract.ISnDefaultShippingAddressInfoPresenter,
        ISnDefaultShippingAddressInfoInteractor.Callback {
    private final SnDefaultShippingAddressContract.IView mView;

    public SnDefaultShippingAddressPresenterImpl(SnDefaultShippingAddressContract.IView iView) {
        mView = iView;
    }

    @Override
    public void getSnDefaultShippingAddressInfo(Context context) {
        mView.startMyDialog(false);
        SnDefaultShippingAddressInfoInteractorImpl snDefaultShippingAddressInfoInteractor
                = new SnDefaultShippingAddressInfoInteractorImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new SnDefaultShippingAddressRepositoryImpl(context));
        snDefaultShippingAddressInfoInteractor.execute();

    }

    @Override
    public void getShippingAddressSuccess(SnShippingAddressInfoEntity snShippingAddressInfoEntity) {
        mView.stopMyDialog();
        mView.getSnDefaultShippingAddressInfoSuccess(snShippingAddressInfoEntity);
    }

    @Override
    public void getShippingAddressError(String errorMsg) {
        mView.stopMyDialog();
        mView.showToast(errorMsg);
    }
}
