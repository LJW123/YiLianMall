package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * 套餐退款成功界面
 */
public class MTRefundSucceedActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.btn_mt_back_order)
    Button btnMtBackOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtrefund_succeed);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                MTRefundSucceedActivity.this.finish();
            }
        });
        tvTitle.setText("申请退款");
        btnMtBackOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mt_back_order:
                setResult(RESULT_OK);
                MTRefundSucceedActivity.this.finish();
                break;
        }
    }
}
