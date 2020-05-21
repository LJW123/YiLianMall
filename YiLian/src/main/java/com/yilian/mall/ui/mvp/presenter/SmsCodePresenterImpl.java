package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.SmsCodeModelImpl;
import com.yilian.mall.ui.mvp.view.ISmsCodeView;
import com.yilian.mylibrary.CodeException;
import com.yilian.networkingmodule.entity.IsShowImgCode;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/18.
 */

public class SmsCodePresenterImpl implements ISmsCodePresenter {
    /**
     * 不需要校验图形验证码
     */
    public static final int VERIFY_TYPE_2 = 2;
    /**
     * 需要校验图形验证码
     */
    public static final int VERIFY_TYPE_1 = 1;
    private final SmsCodeModelImpl smsCodeModel;
    private ISmsCodeView iSmsCodeView;

    public SmsCodePresenterImpl(ISmsCodeView iSmsCodeView) {

        this.iSmsCodeView = iSmsCodeView;
        smsCodeModel = new SmsCodeModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @Override
    public Subscription getSmsCode(Context context) {
        iSmsCodeView.startMyDialog();
        return smsCodeModel.getSmsCode(context, iSmsCodeView.getPhone(), iSmsCodeView.getVerifyType(),
                iSmsCodeView.getSmsType(), iSmsCodeView.getVoice(), iSmsCodeView.getVerifyCode(), new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        iSmsCodeView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof CodeException) {
                            if (((CodeException) e).code == 0) {
//                                图形验证码错误
                                iSmsCodeView.refreshImgCode();
                            }
                        }
                        iSmsCodeView.stopMyDialog();
                        iSmsCodeView.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
//                        图形验证码界面点击确定，该接口请求成功后，需要隐藏图形验证码弹窗，并弹出短信验证码弹窗
                        iSmsCodeView.showSmsInputDialog();
                        iSmsCodeView.startCountDown();
                    }
                });
    }

    /**
     * 是否显示图形验证码
     * 如果需要则弹出验证码
     * 如果不需要 分两种情况 1：验证码弹窗没有弹出的情况下 弹出获取验证码弹窗 2：验证码弹窗弹出的情况下直接发送验证码
     *
     * @param context
     * @return
     */
    @Override
    public Subscription isShowImgCode(Context context) {
        iSmsCodeView.startMyDialog();
        return smsCodeModel.isShowImgCode(context, new Observer<IsShowImgCode>() {
            @Override
            public void onCompleted() {
                iSmsCodeView.stopMyDialog();

            }

            @Override
            public void onError(Throwable e) {
                iSmsCodeView.stopMyDialog();
                iSmsCodeView.showToast(e.getMessage());
            }

            @Override
            public void onNext(IsShowImgCode isShowImgCode) {
                //1代表直接发送短信  0代表需要弹图形验证码
                if (isShowImgCode.isShow == 0) {
                    iSmsCodeView.setVerifyType(VERIFY_TYPE_1);
                    iSmsCodeView.showImgCodeDialog();
                } else {
                    iSmsCodeView.setVerifyType(VERIFY_TYPE_2);
                    iSmsCodeView.showSmsInputDialog();
                    getSmsCode(context);
                }
            }
        });
    }

    @Override
    public Subscription checkSmsCode(Context context) {
        iSmsCodeView.startMyDialog();
        return smsCodeModel.checkSmsCode(context, iSmsCodeView.getPhone(), iSmsCodeView.getSmsCode(), new Observer<HttpResultBean>() {
            @Override
            public void onCompleted() {
                iSmsCodeView.stopMyDialog();

            }

            @Override
            public void onError(Throwable e) {
                iSmsCodeView.stopMyDialog();
                iSmsCodeView.showToast(e.getMessage());
                Subscription subscription = isShowImgCode(context);
            }


            @Override
            public void onNext(HttpResultBean httpResultBean) {
                iSmsCodeView.showToast("验证码验证成功");
//处理验证逻辑
            }
        });
    }
}
