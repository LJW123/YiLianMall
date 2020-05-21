package com.yilian.mall.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.GoodsChargeForPayResultEntity;
import com.yilian.mall.entity.ShowMTPackageTicketEntity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.ui.CashDeskActivity;
import com.yilian.mall.ui.ChargeSuccessActivity;
import com.yilian.mall.ui.MTPayForStoreResult;
import com.yilian.mall.ui.NMemberChargeActivity;
import com.yilian.mall.ui.NOrderPaySuccessActivity;
import com.yilian.mall.ui.RechargeActivity;
import com.yilian.mall.ui.V5PaySuccessActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    MallNetRequest mallNetRequest;
    private String wxPayOrderNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.i("微信支付回调1");
        switch (resp.errCode) {
            case 0: // 支付成功
                Logger.i("微信支付回调2");
                if (sp.getBoolean(Constants.WXCHARGEFORPAY, false)) {//如果该笔微信支付是直接支付订单的，则跳转到支付成功界面
                    Logger.i("微信支付回调3");
                    sp.edit().putBoolean(Constants.WXCHARGEFORPAY, false).commit();//将微信支付是用于订单支付的状态重置为false，防止下次如果微信支付是充值，走了这里
                    chargeForPay();
                } else {//微信充值
                    if (sp.getString("type", "").equals("chooseCharge")) {
                        AppManager.getInstance().killActivity(NMemberChargeActivity.class);
                        AppManager.getInstance().killActivity(RechargeActivity.class);
                    }
                    startActivity(new Intent(mContext, ChargeSuccessActivity.class));
                }
                finish();
                break;
//			case 1: // 订单重复
//				showToast("订单重复");
//				finish();
//				break;
//
//			case 2: // 用户取消
//				showToast("用户取消");
//				finish();
//				break;
            case -1:
                showToast("支付失败");
                sp.edit().putString("type", "").commit();
                finish();
                break;
            case -2:
                showToast("取消支付");
                sp.edit().putString("type", "").commit();
                finish();
                break;
        }
    }

    /**
     * 如果该笔微信支付是用于订单支付，则成功后跳转到支付成功界面
     */
    private void chargeForPay() {
        String orderIndex = sp.getString(Constants.WXPAYORDER, "");
        String payType1 = sp.getString(Constants.WXPAYTYPE1, "");
        wxPayOrderNumber = sp.getString(Constants.WXPAYORDERNUMBER, "1");
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        Logger.i("获取支付结果OrderId:" + orderIndex + " payType1:" + payType1);
        getWxPayResult(orderIndex, payType1);
    }

    private int getPayResultTimes = 0;

    /**
     * 充值用于直接支付时，支付后，获取支付结果
     *
     * @param orderIndex
     * @param payType1
     */
    private void getWxPayResult(final String orderIndex, String payType1) {
        mallNetRequest.getPayResult(orderIndex, payType1, new RequestCallBack<GoodsChargeForPayResultEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<GoodsChargeForPayResultEntity> responseInfo) {
                GoodsChargeForPayResultEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        Logger.i("多余resultname"+result.name+"result.lebi:" + result.lebi + "  coupon:" + result.coupon + "  integral:" + result.integral + "  orderIndex:" + orderIndex + "  dealTime:" + result.dealTime);
                        AppManager.getInstance().killActivity(CashDeskActivity.class);
                        jumpToNOrderPaySuccessActivity(result,result.lebi, result.coupon, result.integral, orderIndex, result.dealTime,result.voucher);
                        break;

                    case -3://系统繁忙
                        showToast(R.string.system_busy);
                        break;
                    case -14://订单失效
                        showToast("订单失效");
                        break;
                    case -4://token失效
                    case -23://设备ID验证失败
                        showToast(R.string.login_failure);
                        startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                        break;
                    case -100://若失败则重新回调，最多回调五次
                        if (getPayResultTimes < 5) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        sleep(2000);
                                        getWxPayResult(orderIndex, payType1);
                                        getPayResultTimes++;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                        break;
                    default:
                        showToast("错误码WX2:" + result.code);
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);

            }
        });
    }

    /**
     * 支付成功后跳转到支付成功界面
     *
     * @param result
     * @param lebi
     * @param coupon
     * @param integral
     * @param dealId
     * @param dealTime
     */
    private void jumpToNOrderPaySuccessActivity(GoodsChargeForPayResultEntity result, String lebi, String coupon, String integral, String dealId, String dealTime,String voucher) {
        Intent intent = null;
        String paySource = sp.getString(Constants.PAY_SOURCE, "");//支付来源，
        String payType1 = sp.getString("payType1", "");
        switch (payType1){
            case "3":
                intent = new Intent(mContext, NOrderPaySuccessActivity.class);
                intent.putExtra("lebi", lebi);
                intent.putExtra("coupon", coupon);
                intent.putExtra("integral", integral);
                intent.putExtra("deal_time", dealTime);
                intent.putExtra("payType1",payType1);
                intent.putExtra("dealId", dealId);
                intent.putExtra("orderNumber", String.valueOf(wxPayOrderNumber));//用于判断订单数量
                break;
            case "4":
                intent = new Intent(mContext, MTPayForStoreResult.class);
                float aFloat = sp.getFloat(Constants.PAY_MONEY, 0f);
                Logger.i("跳转方法获取的金额:"+lebi+"  coupon:"+coupon);
                Logger.i("取出(并跳转)的微信支付金额："+aFloat);
                intent.putExtra("order_id",dealId);
                intent.putExtra("orderPrice", NumberFormat.convertToFloat(MoneyUtil.getLeXiangBi(lebi),0f));//这里传递给下个界面的是处理过的金额，第二个方案  只能用于  微信支付成功回调
                intent.putExtra("voucher", voucher);//赠送的零购券
                intent.putExtra("backCoupon", result.giveCoupon);
                intent.putExtra("orderTime",dealTime);
                intent.putExtra("backCoupon", result.giveCoupon);//赠送的抵扣券
                intent.putExtra("payType","4");
                break;
            case "5"://向兑换中心扫码支付，只会消耗抵扣券，和零购券没有关系，也不会赠送零购券和抵扣券
                intent = new Intent(mContext, MTPayForStoreResult.class);
                float bFloat = sp.getFloat(Constants.PAY_MONEY, 0f);
                Logger.i("跳转方法获取的金额:"+lebi+"  coupon:"+coupon);
                Logger.i("取出(并跳转)的微信支付金额："+bFloat);
                intent.putExtra("order_id",dealId);
                intent.putExtra("orderPrice", NumberFormat.convertToFloat(MoneyUtil.getLeXiangBi(lebi),0f));//这里传递给下个界面的是处理过的金额，第二个方案  只能用于  微信支付成功回调
                intent.putExtra("costCoupon",coupon);//消费的抵扣券
                intent.putExtra("orderTime",dealTime);
                intent.putExtra("payType","5");
                break;
            case "6":
//                intent = new Intent(mContext,MTPackagePayForSuccess.class);
                intent = new Intent(mContext, V5PaySuccessActivity.class);
                ShowMTPackageTicketEntity entity = new ShowMTPackageTicketEntity();
                entity.codes = result.codes;
                entity.packageName = result.name;
                entity.packageOrderId=result.orderId;
                entity.usableTime=result.usableTime;
                entity.voucher = result.voucher;//赠送零购券
                entity.giveCoupon=result.giveCoupon;//赠送抵扣券
                entity.deliveryPrice = result.deliveryPrice;//运费
                intent.putExtra("ShowMTPackageTicketEntity",entity);
                break;
            case "2"://商家缴费 支付成功提交资料
//                startActivity(new Intent(mContext, MerchantSettledActivity.class));
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}