package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.ISupportBankListModel;
import com.yilian.mall.ui.mvp.model.SupportBankListModel;
import com.yilian.mall.ui.mvp.view.ISupportBankListView;
import com.yilian.networkingmodule.entity.SupportBankListEntity;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/21.
 */

public class SupportBankListPresenter implements ISupportBankListPresenter {
    private final ISupportBankListModel iSupportBankListModel;
    private ISupportBankListView iSupportBankListView;

    @Override
    public void onDestory() {

    }

    public SupportBankListPresenter(ISupportBankListView iSupportBankListView) {
        this.iSupportBankListView = iSupportBankListView;
        iSupportBankListModel = new SupportBankListModel();
    }

    @Override
    public Subscription getSupportBankList(Context context) {
        iSupportBankListView.startMyDialog();
        return iSupportBankListModel.getSupportBankList(context, iSupportBankListView.getType(), new Observer<SupportBankListEntity>() {
            @Override
            public void onCompleted() {
                iSupportBankListView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iSupportBankListView.showToast(e.getMessage());
                iSupportBankListView.stopMyDialog();
            }

            @Override
            public void onNext(SupportBankListEntity supportBankListEntity) {
                iSupportBankListView.setData(supportBankListEntity);
            }
        });
    }
}
