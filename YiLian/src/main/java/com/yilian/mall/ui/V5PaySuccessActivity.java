package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.ShowMTPackageTicketEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.youth.banner.Banner;

/**
 *  V5版本套餐支付成功页
 * @author Ray_L_Pain
 * @date 2017/12/25 0025
 */

public class V5PaySuccessActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;

    @ViewInject(R.id.tv_pay_money)
    TextView tvPayMoney;
    @ViewInject(R.id.tv_integral)
    TextView tvIntegral;
    @ViewInject(R.id.tv_way)
    TextView tvWay;
    @ViewInject(R.id.tv_merchant_name)
    TextView tvMerchantName;
    @ViewInject(R.id.tv_eva_state_1)
    TextView tvEva1;
    @ViewInject(R.id.tv_eva_state_2)
    TextView tvEva2;
    @ViewInject(R.id.tv_eva_state_3)
    TextView tvEva3;
    @ViewInject(R.id.tv_eva_state_4)
    TextView tvEva4;
    @ViewInject(R.id.banner)
    Banner banner;

    private ShowMTPackageTicketEntity showMTPackageTicketEntity;
    private String packageName, packageOrderId, packageIntegral, packageMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v5_pay_success);
        ViewUtils.inject(this);

        initView();
    }

    // TODO 现在传递的参数 少：套餐价格 收款方姓名 收款方电话   界面接口应该再多个轮播的
    private void initView() {
        showMTPackageTicketEntity = (ShowMTPackageTicketEntity) getIntent().getSerializableExtra("ShowMTPackageTicketEntity");
        packageName = showMTPackageTicketEntity.packageName;
        packageOrderId = showMTPackageTicketEntity.packageOrderId;
        packageIntegral = showMTPackageTicketEntity.integral;
        packageMoney = showMTPackageTicketEntity.lebi;

        tvPayMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(packageMoney)));
        tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(packageIntegral) + "奖券");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().killActivity(CashDeskActivity.class);
                finish();
            }
        });

        tvTitle.setText("支付成功");

        tvRight.setText("完成");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                startActivity(intent);
            }
        });

        tvEva1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, V5EvaActivity.class);
                intent.putExtra("title", packageName);
                intent.putExtra("checked_id", 1);
                intent.putExtra("order_id", packageOrderId);
                startActivity(intent);
            }
        });
        tvEva2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, V5EvaActivity.class);
                intent.putExtra("title", packageName);
                intent.putExtra("checked_id", 2);
                intent.putExtra("order_id", packageOrderId);
                startActivity(intent);
            }
        });
        tvEva3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, V5EvaActivity.class);
                intent.putExtra("title", packageName);
                intent.putExtra("checked_id", 3);
                intent.putExtra("order_id", packageOrderId);
                startActivity(intent);
            }
        });
        tvEva4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, V5EvaActivity.class);
                intent.putExtra("title", packageName);
                intent.putExtra("checked_id", 4);
                intent.putExtra("order_id", packageOrderId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AppManager.getInstance().killActivity(CashDeskActivity.class);
    }
}
