package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.DateUtils;
import com.yilian.networkingmodule.entity.PayToMerchantExchangeEntity;

public class PayExchangeToMerchantSuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final String TAG = "PayExchangeToMerchantSuccessActivity";
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView ivRight;
    private TextView tvWay;
    private TextView tvTime;
    private TextView textView7;
    private TextView tvMoney;
    private LinearLayout llConsumeScore;
    private Button btnProfit;
    private PayToMerchantExchangeEntity payToMerchantExchangeEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_exchange_to_merchant_success);
        initView();
        initData();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("完成兑换");
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        tvWay = (TextView) findViewById(R.id.tv_way);
        tvTime = (TextView) findViewById(R.id.tv_time);
        textView7 = (TextView) findViewById(R.id.textView7);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        llConsumeScore = (LinearLayout) findViewById(R.id.ll_consume_score);
        btnProfit = (Button) findViewById(R.id.btn_profit);

        btnProfit.setOnClickListener(this);
    }

    private void initData() {
        payToMerchantExchangeEntity = (PayToMerchantExchangeEntity) getIntent().getSerializableExtra(TAG);
        tvWay.setText(payToMerchantExchangeEntity.exchangeType);
        tvTime.setText(DateUtils.timeStampToStr(payToMerchantExchangeEntity.exchangeTime));
        tvMoney.setText(MoneyUtil.getLeXiangBi(payToMerchantExchangeEntity.exchangeQuan));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_profit:
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
