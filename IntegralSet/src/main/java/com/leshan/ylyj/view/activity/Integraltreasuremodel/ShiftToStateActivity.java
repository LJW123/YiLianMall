package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.yilian.mylibrary.MoneyUtil;

import rxfamily.entity.IntegralStatusEntity;


/**
 * 转入 状态
 */
public class ShiftToStateActivity extends BaseActivity implements View.OnClickListener {
    //              转出时间  转出奖券   手续费
    private TextView time_tv, money_tv, service_charge_tv;
    private Button submit_bt;

    private IntegralStatusEntity.Data mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_to_state);
        initToolbar();
        setToolbarTitle("手动转入");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        time_tv = findViewById(R.id.time_tv);
        money_tv = findViewById(R.id.money_tv);
        service_charge_tv = findViewById(R.id.service_charge_tv);
        submit_bt = findViewById(R.id.submit_bt);
    }

    @Override
    protected void initListener() {
        submit_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mData = (IntegralStatusEntity.Data) getIntent().getExtras().getSerializable("IntegralStatusEntity");
        if (mData == null)
            return;
        time_tv.setText(mData.getServerTime());
        money_tv.setText(MoneyUtil.getTwoDecimalPlaces(mData.getAmount()));
        service_charge_tv.setText(mData.getFeePercent() + "%");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.submit_bt) {
            finish();

        } else {
        }
    }
}
