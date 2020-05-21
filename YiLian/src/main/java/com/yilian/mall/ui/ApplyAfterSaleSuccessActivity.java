package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;

/**
 * 提交售后服务申请成功界面
 */
public class ApplyAfterSaleSuccessActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.tv)
    private TextView mTv;

    @ViewInject(R.id.tv_order_id)
    private TextView mTvOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_after_sale_success);
        ViewUtils.inject(this);

        tvBack.setText("申请提交成功　");
        mTv.setText("申请时间　"+ getIntent().getStringExtra("time")+
                "\n申请类型　"+getIntent().getStringExtra("type")+
                "\n售后编号　"+getIntent().getStringExtra("serviceOrder")+
                "\n申请理由　"+getIntent().getStringExtra("serviceRemark"));
        mTvOrderId.setText("订单号："+getIntent().getStringExtra("orderId"));
        //刷新个人页面标识
        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
    }

    public void backMain(View view){
        Intent intent = new Intent(this, JPMainActivity.class);
        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
        startActivity(intent);

    }

    public void onBack(View view){
        finish();
    }

}
