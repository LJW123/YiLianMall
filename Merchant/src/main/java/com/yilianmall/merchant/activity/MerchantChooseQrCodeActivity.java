package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mylibrary.RxUtil;
import com.yilianmall.merchant.R;

import io.reactivex.functions.Consumer;

import static com.yilian.networkingmodule.entity.MerchantCenterInfo.MerchantInfoBean.OPEN_POWER;
import static com.yilian.networkingmodule.entity.MerchantCenterInfo.MerchantInfoBean.UN_OPEN_POWER;
import static com.yilianmall.merchant.activity.MerchantQrCodeActivity.GIFT_TYPE;

public class MerchantChooseQrCodeActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_OPEN_POWER = "isOpenPower";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private ImageView ivType1;
    private ImageView ivType2;
    private int isOpenPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_choose_qr_code);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        isOpenPower = getIntent().getIntExtra(IS_OPEN_POWER, UN_OPEN_POWER);
    }

    private void initListener() {
        RxUtil.clicks(ivType1, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//奖券赠送二维码
                Intent intent = new Intent(mContext, MerchantQrCodeActivity.class);
                intent.putExtra(GIFT_TYPE,MerchantQrCodeActivity.TYPE_1);
                intent.putExtra(IS_OPEN_POWER,isOpenPower);
                startActivity(intent);
            }
        });
        RxUtil.clicks(ivType2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//益豆赠送二维码
                if (isOpenPower != OPEN_POWER) {
                    showToast("暂无权限");
                    return;
                }
                Intent intent = new Intent(mContext, MerchantQrCodeActivity.class);
                intent.putExtra(GIFT_TYPE,MerchantQrCodeActivity.TYPE_2);
                intent.putExtra(IS_OPEN_POWER,isOpenPower);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("选择赠送类型");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        ivType1 = (ImageView) findViewById(R.id.iv_type_1);
        ivType2 = (ImageView) findViewById(R.id.iv_type_2);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_left) {
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
        } else if (i == R.id.v3Back) {
            finish();
        }
    }
}
