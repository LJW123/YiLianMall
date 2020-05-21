package com.yilianmall.merchant.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.MerchantMyRevenueEntity;
import com.yilianmall.merchant.R;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;

/**
 * 提取营收选择绑定银行卡页面
 *
 * @author Created by zhaiyaohua on 2018/4/26.
 */

public class MerchantRevenueBindCardActivity extends BaseActivity {
    public static final int REQUEST_CODE = 0;
    /**
     * 个人银行卡的申请状态
     */
    private ImageView iconAdd;


    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_revenue_bind_card);
        initView();
        initListener();
    }

    protected void initView() {
        StatusBarUtil.setColor(this, Color.WHITE, Constants.STATUS_BAR_ALPHA_60);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setImageResource(R.mipmap.merchant_iv_back_black);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("添加对公卡");
        tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.merchant_c333333));
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.GONE);
        iconAdd = findViewById(R.id.icon_add);
        findViewById(R.id.title).setBackgroundResource(R.color.white);
    }

    protected void initListener() {
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        RxUtil.clicks(findViewById(R.id.ll_add_pub_car), new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.bankmodel.BindPublicBindCardActivity"));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /**
             * 绑卡成功发送事件刷新营收页面数据
             * {@link com.yilianmall.merchant.activity.MerchantMyRevenueActivity#updateRevenueInfo(MerchantMyRevenueEntity entity)}
             */
            EventBus.getDefault().post(new MerchantMyRevenueEntity());
            finish();
        }
    }
}
