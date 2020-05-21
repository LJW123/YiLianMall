package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BarCodeScanResultEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BarCodeScanGoodResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvGoodName;
    private TextView tvPrice;
    private TextView tvSpec;
    private TextView tvCode;
    private TextView tvCheckout;
    private TextView tvContinue;
    private BarCodeScanResultEntity.DataBean barCodeScanResultEntity;
    private String goodsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_scan_good_result);
        initView();
        initData();
    }

    private void initData() {
        barCodeScanResultEntity = (BarCodeScanResultEntity.DataBean) getIntent().getSerializableExtra("barCodeScanResultEntity");
        if (barCodeScanResultEntity == null) {
            finish();
        } else {
            tvGoodName.setText(barCodeScanResultEntity.goodsName);
            tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(barCodeScanResultEntity.goodsCost)));
            tvSpec.setText("规格:" + barCodeScanResultEntity.goodsNorms);
            goodsCode = barCodeScanResultEntity.goodsCode;
            tvCode.setText(goodsCode);
        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvGoodName = (TextView) findViewById(R.id.tv_good_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvSpec = (TextView) findViewById(R.id.tv_spec);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvCheckout = (TextView) findViewById(R.id.tv_checkout);
        tvContinue = (TextView) findViewById(R.id.tv_continue);

        tvTitle.setOnClickListener(this);
        tvCheckout.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i==R.id.iv_close) {
            finish();
        } else if (i == R.id.tv_checkout) {
            type = 0;
            addShoppingCart();

        } else if (i == R.id.tv_continue) {
            type = 1;
            addShoppingCart();
        }
    }

    int type = 1;//0结账 2继续扫

    @SuppressWarnings("unchecked")
    void addShoppingCart() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).addGoodsToShoppingCart("sc_goods_add_cart", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                goodsCode, "1").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<HttpResultBean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        if (type == 0) {
                            startActivity(new Intent(mContext, MerchantShopActivity.class));
                        }
                        finish();
                    }
                });
        addSubscription(subscription);
    }
}
