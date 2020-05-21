package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 兑换指数
 */
public class ExchangeIndexActivity extends BaseActivity implements View.OnClickListener {

    private TextView exchange_index_tv;
    private ImageView intro_iv;
    private Button submit_bt;

    private String exchangeIndex;//兑换指数  参照现在处理方式（万分之）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_index);
        initToolbar();
        setToolbarTitle("消费指数");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        exchange_index_tv = findViewById(R.id.exchange_index_tv);//指数
        intro_iv = findViewById(R.id.intro_iv);//近7日指数
        submit_bt = findViewById(R.id.submit_bt);//返回首页

    }

    @Override
    protected void initListener() {
        intro_iv.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        exchangeIndex = getIntent().getExtras().getString("exchangeIndex");
        if (TextUtils.isEmpty(exchangeIndex) || Double.valueOf(exchangeIndex) == 0) {
            exchange_index_tv.setText("- - -");
        } else {
            exchange_index_tv.setText(new BigDecimal(exchangeIndex).divide(BigDecimal.valueOf(10000), 4, RoundingMode.HALF_UP) + "");
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.intro_iv) {
            SkipUtils.toExchangeIndexRecord(this);
        } else if (i == R.id.submit_bt) {
            finish();
        } else {
        }
    }
}
