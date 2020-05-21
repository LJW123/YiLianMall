package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;

/**
 * Created by Ray_L_Pain on 2017/10/16 0016.
 */

public class MerchantResalePaySuccessActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private TextView tvWay;
    private TextView tvTime;
    private TextView textView7;
    private TextView tvMoney;
    private LinearLayout llConsumeScore;
    private TextView tvIntegral;
    private LinearLayout llScore;
    private Button btnProfit;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_resale_pay_success);

        initView();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        tvTitle.setText("完成支付");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        tvWay = (TextView) findViewById(R.id.tv_way);
        tvWay.setOnClickListener(this);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView7.setOnClickListener(this);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvMoney.setOnClickListener(this);
        llConsumeScore = (LinearLayout) findViewById(R.id.ll_consume_score);
        llConsumeScore.setOnClickListener(this);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvIntegral.setOnClickListener(this);
        llScore = (LinearLayout) findViewById(R.id.ll_score);
        llScore.setOnClickListener(this);
        btnProfit = (Button) findViewById(R.id.btn_profit);
        btnProfit.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        tvWay.setText("");
        tvTime.setText(DateUtils.timeStampToStr(Long.parseLong("")));
        tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(""));

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_profit) {
            //TODO 立即发奖励

        } else if (i == R.id.btn_ok) {
            finish();
        } else if (i == R.id.iv_back) {
            finish();
        }
    }
}
