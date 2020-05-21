package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.MerchantCashSuccessEntity;
import com.yilianmall.merchant.R;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;

/**
 * 营收提取成功页面
 *
 * @author Created by zhaiyaohua on 2018/7/8.
 */

public class MerchantExtractSucActivity extends BaseActivity {
    private MerchantCashSuccessEntity entity;
    private ImageView v3Back;
    private Button btnOk;
    private TextView tvMoney;
    private TextView tvCardNameandnum;
    private TextView tvTime;
    private TextView v3Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_extract_success);
        initView();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.merchant_white), Constants.STATUS_BAR_ALPHA_60);
        entity = getIntent().getParcelableExtra("data");
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        tvCardNameandnum = (TextView) findViewById(R.id.tv_card_nameandnum);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        btnOk = (Button) findViewById(R.id.btn_ok);
        String cash = MyBigDecimal.div3(Double.parseDouble(entity.cashAmount), 100,  BigDecimal.ROUND_DOWN, 2);
        tvMoney.setText(MoneyUtil.add¥Front(cash));
        tvCardNameandnum.setText(entity.cardBank + "(" + subCardNum(entity.cardNumber) + ")");
        tvTime.setText(TimeUtils.millis2String(entity.time * 1000));
        v3Title.setText("提取营收");
    }

    private void initListener() {
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        RxUtil.clicks(btnOk, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
    }

    private String subCardNum(String carNum) {
        if (TextUtils.isEmpty(carNum)) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer();
            char[] chars = carNum.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i < 4) {
                    buffer.append(chars[i]);
                }
            }
            return buffer.toString();
        }
    }
}
