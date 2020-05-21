package com.yilian.mall.jd.presenter.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.yilian.mall.alipay.PayResult;
import com.yilian.mall.jd.presenter.JDOrderPayPresenter;
import com.yilian.mall.ui.InitialPayActivity;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.widget.PasswordFinished;
import com.yilian.mylibrary.widget.PayDialog;
import com.yilian.networkingmodule.entity.jd.JDCashPayResult;
import com.yilian.networkingmodule.entity.jd.JDPayInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDThirdPayPreRechargeOrderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.yilian.mylibrary.pay.ThirdPayForType.PAY_FOR_JD_GOODS;

/**
 * @author Created by  on 2018/5/28.
 */

public class JDPayPresenterImpl implements JDOrderPayPresenter, PasswordFinished {
    /**
     * 支付宝
     */
    public static final String ALI_PAY = "1";
    private Context context;
    private View view;
    private String orderIndex;
    private final String mMerchantIncomePayMoney;
    private PayDialog payDialog;
    private CompositeSubscription compositeSubscription;

    public JDPayPresenterImpl(Context context, View view, String orderIndex,String merchantIncomePayMoney) {
        this.context = context;
        this.view = view;
        this.orderIndex = orderIndex;
        mMerchantIncomePayMoney = merchantIncomePayMoney;
    }

    @Override
    public void pay(int payType, float thirdFee) {
        switch (payType) {
            case CASH_PAY:
                payCash();
                break;
            case MIX_PAY:
                payMix(thirdFee);
                break;
            case THIRD_PAY:
                payThird(thirdFee);
                break;
            default:
                break;
        }

    }

    /**
     * 检查是否有支付密码
     */
    private void payCash() {
        if (view.hasPassword()) {
            if (payDialog == null) {
                payDialog = new PayDialog(AppManager.getInstance().getTopActivity(), this);
            }
            payDialog.show();
        } else {
//            跳转设置支付密码页面
            view.showSystemV7Dialog("您还未设置支付密码,请设置支付密码后再支付！", null,
                    "设置", "否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    context.startActivity(new Intent(context, InitialPayActivity.class));
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
    }

    /**
     * 混合支付 只需要进行差额部分预充值操作即可
     *
     * @param thirdFee
     */
    private void payMix(float thirdFee) {
        payThird(thirdFee);
    }

    /**
     * 第三方支付  预充值
     *
     * @param thirdFee
     */
    @SuppressWarnings("unchecked")
    private void payThird(float thirdFee) {
        view.startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .jdPreRecharge("jd_pay/jd_recharge_order", String.valueOf(thirdFee), ALI_PAY, orderIndex, PAY_FOR_JD_GOODS,mMerchantIncomePayMoney)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDThirdPayPreRechargeOrderEntity>() {
                    @Override
                    public void onCompleted() {
                        view.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.stopMyDialog();
                        view.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDThirdPayPreRechargeOrderEntity o) {
                        aliPay(o);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 拉起支付宝支付界面
     *
     * @param o
     */
    private void aliPay(JDThirdPayPreRechargeOrderEntity o) {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(AppManager.getInstance().getTopActivity());
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(o.orderString, true);
                PayResult payResult = new PayResult((Map<String, String>) result);
                String resultStatus = payResult.getResultStatus();
                AppManager.getInstance().getTopActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.i("resultStatus  " + resultStatus);

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, ALI_PAY_SUCCESS)) {
                            view.showToast("支付成功");
                            getPayResult();
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, ALI_PAY_UN_CONFIRM)) {
                                view.showToast("支付结果确认中");
                            } else if (TextUtils.equals(resultStatus, ALI_PAY_NO_ALI)) {
                                view.showToast("请安装支付宝插件");
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                view.showToast("支付失败");
                            }
                        }
                    }
                });

            }
        });
    }

    /**
     * 支付密码输入完成回调
     *
     * @param psw 加密后的密码
     */
    @SuppressWarnings("unchecked")
    @Override
    public void passwordFinished(String psw) {
        view.startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .jdPay("jd_pay/jd_pay_balance", orderIndex, psw,mMerchantIncomePayMoney)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDCashPayResult>() {
                    @Override
                    public void onCompleted() {
                        cancelPayDialog();
                        view.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelPayDialog();
                        view.showToast(e.getMessage());
                        view.stopMyDialog();
                    }

                    @Override
                    public void onNext(JDCashPayResult o) {
                        getPayResult();
                    }
                });
        addSubscription(subscription);
    }

    private void cancelPayDialog() {
        if (payDialog!=null&&payDialog.isShowing()) {
            payDialog.dismiss();
        }
    }

    /**
     * 获取支付结果
     */
    @SuppressWarnings("unchecked")
    private void getPayResult() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getJdPayInfo("pay_info",orderIndex ,PAY_FOR_JD_GOODS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDPayInfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JDPayInfoEntity jdPayInfoEntity) {
                        view.paySuccess(jdPayInfoEntity);
                    }
                });
        addSubscription(subscription);
    }

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void onDestory() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }


}
