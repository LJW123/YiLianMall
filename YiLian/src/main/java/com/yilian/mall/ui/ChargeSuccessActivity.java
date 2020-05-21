package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilian.mylibrary.AppManager;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.ChargeSuccessEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;

/**
 * 充值成功界面
 */
public class ChargeSuccessActivity extends BaseActivity {

    @ViewInject(R.id.tv_charge_number)
    private TextView tvMoney;

    @ViewInject(R.id.tv_lefen)
    private TextView tvLefen;

    @ViewInject(R.id.tv_lexiang)
    private TextView tv_lexiang;

    @ViewInject(R.id.tv_level)
    private TextView tv_level;

    private MyIncomeNetRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_success);

        ViewUtils.inject(this);

        chargeOrder(sp.getString("payType", "0"), sp.getString("price", "0"));

        initView();
    }

    private void initView() {
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }

        startMyDialog();
        request.chargeSuccess(new RequestCallBack<ChargeSuccessEntity>() {
            @Override
            public void onSuccess(ResponseInfo<ChargeSuccessEntity> responseInfo) {
                Logger.json(responseInfo.result.toString());
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:

                        if (responseInfo.result.lev1 == 0) {
                            tv_level.setText("当前等级：普通会员");
                        }
                        if (responseInfo.result.lev2 == 0) {
                            tv_level.setText("当前等级：VIP会员");
                        }
                        if (responseInfo.result.lev3 == 0) {
                            tv_level.setText("当前等级：至尊会员");
                        }
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                                          stopMyDialog();
            }
        });
    }

    /**
     * 支付宝支付成功执行充值
     *
     * @param type             //0支付宝 1微信 2微信公共账号
     * @param paymentFee//支付总价
     */
    public void chargeOrder(String type, String paymentFee) {
        switch (sp.getString("chooseFrom", "")) {
            case "lexiangbi":
                tvMoney.setText(paymentFee+"元");
                tvLefen.setText("0.00");
                tvLefen.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.ic_mall_shopping), null);
                tv_lexiang.setText(String.format("%.2f", Float.parseFloat(paymentFee)));
                break;
            case "balance":
                tvMoney.setText(paymentFee+"元");
                tvLefen.setText(String.format("%.2f", Float.parseFloat(paymentFee) * 1.25));
                tvLefen.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.ic_mall_shopping), null);
                tv_lexiang.setText(String.format("%.2f", Float.parseFloat(paymentFee) * 0.75));
                break;
            case "xianjin":
                tvMoney.setText(paymentFee+"元");
                tvLefen.setText(String.format("%.2f", Float.parseFloat(paymentFee) * 11 / 10));
                tvLefen.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.xianjinquan), null);
                tv_lexiang.setText(String.format("%.2f", Float.parseFloat(paymentFee) * 9 / 10));
                break;
        }
        sp.edit().putString("type", "").commit();
    }

    public void onBack(View v) {
        AppManager.getInstance().killActivity(NMemberChargeActivity.class);
        AppManager.getInstance().killActivity(RechargeActivity.class);
        finish();
    }

    public void surePay(View view) {
        AppManager.getInstance().killActivity(NMemberChargeActivity.class);
        AppManager.getInstance().killActivity(RechargeActivity.class);
        finish();
    }

    public void jumpToRecord(View view) {
        AppManager.getInstance().killActivity(NMemberChargeActivity.class);
        AppManager.getInstance().killActivity(V3MoneyActivity.class);
        AppManager.getInstance().killActivity(RechargeActivity.class);


        Intent intent = new Intent(this, V3MoneyActivity.class);
        intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
        startActivity(intent);
//        之前都是查看充值记录时，关闭该界面，但是V3版，测试提出意见，此处不再关闭，而是可以再新打开的页面中再次返回该页面，所以此处不再关闭
//        finish();
    }

    public void jumpToRule(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", Ip.getHelpURL() + "agreementforios/recharge.html");
        startActivity(intent);
    }
}
