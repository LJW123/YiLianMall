package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.CertificationModelImpl;
import com.yilian.mall.ui.mvp.model.ICertificationModel;
import com.yilian.mall.ui.mvp.view.ICertificationView;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.StopDialog;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.HttpException;
import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/17.
 */

public class CertificationPresenter implements ICertificationPresenter {
    private final ICertificationModel iCertificationModel;
    private ICertificationView iCertificationView;

    public CertificationPresenter(ICertificationView iCertificationView) {
        this.iCertificationView = iCertificationView;
        iCertificationModel = new CertificationModelImpl();
    }

    @Override
    public void onDestory() {

    }

    /**
     * 添加认证
     *
     * @param context
     * @return
     */
    @Override
    public Subscription addAuth(Context context) {
        iCertificationView.startMyDialog();
        return iCertificationModel.addAuth(context,  iCertificationView.getUserName(), iCertificationView.getIdCard(),
              iCertificationView.getSmsCode(), new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new StopDialog());
                        iCertificationView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new StopDialog());
                        iCertificationView.stopMyDialog();

                        iCertificationView.showToast(e.getMessage());
                        if (e instanceof IOException) {

                        } else if (e instanceof HttpException) {

                        } else {
                            CodeException codeException = (CodeException) e;
                            if (codeException.code == -1) {
                            } else {
                                iCertificationView.showNameView();
                                AppManager.getInstance().killTopActivity();
                            }
                        }
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {

                        PreferenceUtils.writeStrConfig(Constants.IS_CERT, "1", context);
                        iCertificationView.showToast("添加认证通过");
                        EventBus.getDefault().post(new StopDialog());
                        AppManager.getInstance().killTopActivity();
                        iCertificationView.startBindSuccessActivity();
                    }
                });
    }

    @Override
    public Subscription getBankCardInfo(Context context) {
        return null;
    }

    @Override
    public Subscription checkBankCard4Element(Context context) {
        return null;
    }


}
