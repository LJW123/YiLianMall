package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.BasicInformationModelImpl;
import com.yilian.mall.ui.mvp.view.IBasicInfomationView;
import com.yilian.networkingmodule.entity.BasicInformationEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public class BasicInformationPresenterImpl implements IBasicInformationPresenter {
    private final BasicInformationModelImpl basicInformationModel;
    private IBasicInfomationView iBasicInfomationView;

    public BasicInformationPresenterImpl(IBasicInfomationView iBasicInfomationView) {

        this.iBasicInfomationView = iBasicInfomationView;
        basicInformationModel = new BasicInformationModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getData(Context context) {
        iBasicInfomationView.startMyDialog();
        return basicInformationModel.getData(context, new Observer<BasicInformationEntity>() {
            @Override
            public void onCompleted() {
                iBasicInfomationView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iBasicInfomationView.stopMyDialog();
                iBasicInfomationView.showToast(e.getMessage());
            }

            @Override
            public void onNext(BasicInformationEntity basicInformationEntity) {
                iBasicInfomationView.setData(basicInformationEntity);
            }
        });

    }

    @Override
    public Subscription saveData(Context context, BasicInformationEntity.DataBean dataBean) {
        iBasicInfomationView.startMyDialog();
        return basicInformationModel.setData(context, dataBean, new Observer<HttpResultBean>() {
            @Override
            public void onCompleted() {
                iBasicInfomationView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iBasicInfomationView.showToast(e.getMessage());
            }

            @Override
            public void onNext(HttpResultBean httpResultBean) {
                iBasicInfomationView.showToast("保存成功");
                iBasicInfomationView.finish();
            }
        });
    }
}
