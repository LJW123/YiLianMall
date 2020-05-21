package com.yilianmall.merchant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.networkingmodule.entity.MerchantShopQrCodeEntity;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商超收款二维码
 */
public class MerchantCollectionRqcodeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private TextView tvMerchantName;
    private TextView tvPayMoney;
    private ImageView ivMerchantRqcode;
    private ImageView ivMerchant;
    private String merchantName;
    private String imageUrl, merchantId, remark;
    private double totalCash, profitCash;

    /**
     * 由于现在条码交易也用到二维码界面
     * 故加入一个type来判断
     */
    private String type;
    private String orderIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_collection_rqcode_activity);
        StatusBarUtils.setStatusBarColor(this, R.color.library_module_color_green, true);
        merchantName = getIntent().getStringExtra("merchantName");
        imageUrl = getIntent().getStringExtra("imageUrl");
        merchantId = getIntent().getStringExtra("merchantId");
        remark = getIntent().getStringExtra("remark");
        totalCash = getIntent().getDoubleExtra("totalCash",0); //单位 元
        profitCash = getIntent().getDoubleExtra("profitCash",0);//单位 元
        type = getIntent().getStringExtra("type");
        orderIndex = getIntent().getStringExtra("order_index");

        initView();
        if ("MerchantShopActivity".equals(type)) {
            initDataFromShop();
        } else {
            initRqCodeData();
        }
    }

    private void initDataFromShop() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .merchantQrCode(orderIndex, new Callback<MerchantShopQrCodeEntity>() {
            @Override
            public void onResponse(Call<MerchantShopQrCodeEntity> call, Response<MerchantShopQrCodeEntity> response) {
                stopMyDialog();
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        MerchantShopQrCodeEntity entity = response.body();
                        imageUrl = entity.merchantImg;
                        GlideUtil.showImage(mContext, imageUrl, ivMerchant);

                        merchantName = entity.merchantName;
                        tvMerchantName.setText(merchantName);

                        totalCash = Double.parseDouble(entity.paymentFee);
                        tvPayMoney.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(totalCash)));

                        String fileName = entity.filename;
                        GlideUtil.showImage(mContext, fileName, ivMerchantRqcode);
                    }
                }
            }

            @Override
            public void onFailure(Call<MerchantShopQrCodeEntity> call, Throwable t) {
                stopMyDialog();
                showToast(R.string.merchant_system_busy);
            }
        });
    }

    private void initRqCodeData() {
        startMyDialog(false);
        GlideUtil.showImage(mContext, imageUrl, ivMerchant);
        tvMerchantName.setText(merchantName);
        tvPayMoney.setText(MoneyUtil.setNoSmall¥Money(String.valueOf(totalCash)));

        String cashResult = DecimalUtil.convertDoubleToString(totalCash, 100);
        String profitResult = DecimalUtil.convertDoubleToString(profitCash, 100);
        Logger.i("totalCash  "+ cashResult);
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                //支付传递的时候价格也是分
                .getShopRetailQrCodeImg(cashResult, profitResult, merchantId, remark, new Callback<UploadImageEnity>() {
                    @Override
                    public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        String filename = response.body().filename;
                                        GlideUtil.showImage(mContext, filename, ivMerchantRqcode);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.library_module_register_back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setTextColor(Color.WHITE);
        v3Title.setText("收款二维码");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundResource(R.color.library_module_color_green);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);
        tvPayMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(0.00)));
        ivMerchantRqcode = (ImageView) findViewById(R.id.iv_merchant_rqcode);
        ivMerchant = (ImageView) findViewById(R.id.iv_merchant);
        v3Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        }
    }
}
