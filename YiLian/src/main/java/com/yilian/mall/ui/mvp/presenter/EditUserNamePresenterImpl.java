package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.EditUserNameModelImpl;
import com.yilian.mall.ui.mvp.model.IEditUserNameModel;
import com.yilian.mall.ui.mvp.view.IEditUserNameView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public class EditUserNamePresenterImpl implements IEditUserNamePresenter {
    private IEditUserNameView viewImpl;
    private final IEditUserNameModel iModel;
    private int editType;

    public EditUserNamePresenterImpl(IEditUserNameView iView) {
        this.viewImpl = iView;
        iModel = new EditUserNameModelImpl();
    }

    @Override
    public Subscription saveData(Context context, int editType) {
        viewImpl.startMyDialog();
        this.editType = editType;
        Subscription subscription;
        if (editType == 0) {
            subscription = iModel.saveUserName(context, viewImpl.getUserName(), getObserver(context));
        } else {
            subscription = iModel.saveStateOfMind(context, viewImpl.getStateOfMind(), getObserver(context));
        }
        return subscription;
    }

    Observer getObserver(Context context) {
        Observer<HttpResultBean> observer = new Observer<HttpResultBean>() {
            @Override
            public void onCompleted() {
                viewImpl.stopMyDialog();
                viewImpl.finish();
            }

            @Override
            public void onError(Throwable e) {
                viewImpl.stopMyDialog();
                viewImpl.showToast(e.getMessage());
            }

            @Override
            public void onNext(HttpResultBean httpResultBean) {
                if (editType == 0) {
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
                    PreferenceUtils.writeStrConfig(Constants.USER_NAME, viewImpl.getUserName(), context);
                    viewImpl.showToast("昵称修改成功");
                } else {
                    PreferenceUtils.writeStrConfig(Constants.STATE_OF_MIND, viewImpl.getStateOfMind(), context);
                    viewImpl.showToast("签名更新成功");
                }
            }
        };
        return observer;
    }

    @Override
    public void onDestory() {
        viewImpl = null;
    }
}
