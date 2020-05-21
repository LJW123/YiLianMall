package com.yilian.mall.jd.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;

/**
 * 确认收货成功 页面
 */
public class JDOrderReceivingSuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {

    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_order_receiving_success);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
    }

    private void initData() {
        tvTitle.setText("确认收货成功");
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
