package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

public class MerchantChooseDealTypeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvBarcodeDeal;
    private TextView tvWholeOrderDeal;
    private TextView tvCancel;
    private String showText1;
    private String showText2;
    private String phone;
    private String orderIndex;
    private float profitPrice;
    private String merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_choose_deal_type);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        android.view.WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8);
        getWindow().setAttributes(lp);

        merchantId = getIntent().getStringExtra(Constants.MERCHANT_ID);
        showText1 = getIntent().getStringExtra("showText1");
        showText2 = getIntent().getStringExtra("showText2");
        phone = getIntent().getStringExtra("phone");
        orderIndex = getIntent().getStringExtra("orderIndex");
        profitPrice = getIntent().getFloatExtra("profitPrice",0);

        initView();
    }

    private void initView() {
        tvBarcodeDeal = (TextView) findViewById(R.id.tv_barcode_deal);
        tvBarcodeDeal.setText(showText1);
        tvWholeOrderDeal = (TextView) findViewById(R.id.tv_whole_order_deal);
        tvWholeOrderDeal.setText(showText2);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        tvBarcodeDeal.setOnClickListener(this);
        tvWholeOrderDeal.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_barcode_deal) {
            switch (tvBarcodeDeal.getText().toString().trim()) {
                case "条码交易":
                    startActivity(new Intent(mContext, MerchantShopActivity.class));
                    break;
                case "现金付款":
                    Intent intent = new Intent(mContext, MerchantResalePayActivity.class);
                    intent.putExtra("order_index", orderIndex);
                    intent.putExtra("profitCash", MoneyUtil.getLeXiangBiNoZero(profitPrice));
                    intent.putExtra("type", "8");
                    intent.putExtra("from_type", "MerchantShopActivity");
                    intent.putExtra("phone", phone);
                    Logger.i("接受的价格   "+profitPrice);
                    startActivity(intent);
                    break;
            }
        } else if (i == R.id.tv_whole_order_deal) {
            switch (tvWholeOrderDeal.getText().toString().trim()) {
                case "整单交易":
                    if (TextUtils.isEmpty(merchantId)) {
                        merchantId = PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext);
                    }
                    Intent intent = new Intent(mContext, MerchantResaleActivity.class);
                    intent.putExtra("merchantId", merchantId);
                    startActivity(intent);
                    break;
                case "二维码收款":
                    Intent jumpIntent = new Intent(mContext, MerchantCollectionRqcodeActivity.class);
                    jumpIntent.putExtra("order_index", orderIndex);
                    jumpIntent.putExtra("type", "MerchantShopActivity");
                    startActivity(jumpIntent);
                    break;
            }
        } else if (i == R.id.tv_cancel) {
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, false, mContext);
        }
        finish();
    }
}
