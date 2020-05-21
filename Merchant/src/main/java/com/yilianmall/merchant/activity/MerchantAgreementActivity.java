package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilianmall.merchant.R;

/**
 * 商家入驻协议—跳转web前界面—
 */
public class MerchantAgreementActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private LinearLayout llPersonage;
    private LinearLayout llEntity;
    private TextView tvPhone;
    private LinearLayout activityMerchantAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_agreement);
        initView();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.GONE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商家入驻协议");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llPersonage = (LinearLayout) findViewById(R.id.ll_personage);
        llEntity = (LinearLayout) findViewById(R.id.ll_entity);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        activityMerchantAgreement = (LinearLayout) findViewById(R.id.activity_merchant_agreement);

        v3Back.setOnClickListener(this);
        llEntity.setOnClickListener(this);
        llPersonage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.ll_personage) {
        } else if (i == R.id.ll_entity) {
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, Constants.MERCHANT_REGISTE,false);
        }
    }
}
