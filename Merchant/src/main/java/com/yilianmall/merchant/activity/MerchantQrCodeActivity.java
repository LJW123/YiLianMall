package com.yilianmall.merchant.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DownLoadImageService;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ImageDownLoadCallBack;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.MerchantQrCodeEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.networkingmodule.entity.MerchantCenterInfo.MerchantInfoBean.OPEN_POWER;
import static com.yilian.networkingmodule.entity.MerchantCenterInfo.MerchantInfoBean.UN_OPEN_POWER;
import static com.yilianmall.merchant.activity.MerchantChooseQrCodeActivity.IS_OPEN_POWER;

public class MerchantQrCodeActivity extends BaseActivity implements View.OnClickListener {
    public static final String GIFT_TYPE = "gift_type";
    //赠送奖券
    public static final int TYPE_1 = 1;
    //赠送益豆
    public static final int TYPE_2 = 2;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private ImageView ivMerchantImg;
    private TextView tvMerchantName;
    private TextView tvMerchantCoupon;
    private TextView tvMerchantChangeCoupon;
    private ImageView ivQrCode;
    private Button btnSaveQrCode;
    private String qrCodeUrl;
    private TextView tvChangeModel;
    /**
     * 该页面展示的赠送类型
     */
    private int giftType;
    /**
     * 是否有赠送益豆权限
     */
    private int isOpenPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_qr_code);
        initView();
        initData();
    }

    private void initData() {
        giftType = getIntent().getIntExtra(GIFT_TYPE, TYPE_1);
        isOpenPower = getIntent().getIntExtra(IS_OPEN_POWER, UN_OPEN_POWER);
        if (isOpenPower==OPEN_POWER) {
            tvChangeModel.setVisibility(View.VISIBLE);
            setModelText();
        }else{
            tvChangeModel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置下方切换按钮文字
     */
    private void setModelText() {
        switch (giftType) {
            case TYPE_2:
                tvChangeModel.setText("切换至赠送奖券");
                break;
            default:
                tvChangeModel.setText("切换至赠送"+ Constants.APP_PLATFORM_DONATE_NAME);
                break;
        }
    }

    /**
     * 是否刷新页面
     */
    boolean isRefreshPage = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshPage) {
            getData();
        }
        isRefreshPage = true;//每次回到该页面 重置该值
    }

    void getData() {
        switch (giftType) {
            case TYPE_2:
                getMerchantQrQcodeInfo("merchant/happyBeanQrCodeImg");
                break;
            default:
                getMerchantQrQcodeInfo("merchant/payQrCodeImg");
                break;
        }
    }

    private void getMerchantQrQcodeInfo(String c) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantQrCodeInfo(c, new Callback<MerchantQrCodeEntity>() {
                    @Override
                    public void onResponse(Call<MerchantQrCodeEntity> call, Response<MerchantQrCodeEntity> response) {
                        stopMyDialog();
                        MerchantQrCodeEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        setData(body);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantQrCodeEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    private void setData(MerchantQrCodeEntity body) {
        setModelText();
        tvMerchantName.setText(body.merchantName);
        tvMerchantCoupon.setText("让利折扣:" + body.merchantPercent + "折");
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(body.merchantImg), ivMerchantImg);
        qrCodeUrl = WebImageUtil.getInstance().getWebImageUrlNOSuffix(body.filename);
        Logger.i("2018年2月8日 14:11:54-" + qrCodeUrl);
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(qrCodeUrl), ivQrCode);
    }

    private void initView() {
        tvChangeModel = (TextView) findViewById(R.id.tv_change_model);
        tvChangeModel.setOnClickListener(this);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("收款二维码");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("收款记录");
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.GONE);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        ivMerchantImg = (ImageView) findViewById(R.id.iv_merchant_head_img);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvMerchantCoupon = (TextView) findViewById(R.id.tv_merchant_discount);
        tvMerchantChangeCoupon = (TextView) findViewById(R.id.tv_merchant_change_discount);
        ivQrCode = (ImageView) findViewById(R.id.iv_qr_code);
        btnSaveQrCode = (Button) findViewById(R.id.btn_save_qr_code);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvMerchantChangeCoupon.setOnClickListener(this);
        btnSaveQrCode.setOnClickListener(this);
    }

    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
            startActivity(new Intent(this, MerchantMoneyRecordActivity2.class));
        } else if (i == R.id.tv_merchant_change_discount) {
            startActivity(new Intent(this, MerchantChangeDiscountActivity.class));
        } else if (i == R.id.btn_save_qr_code) {
            Logger.i("保存图片开始");
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Logger.i("保存图片请求权限");
                isRefreshPage = false;//此处设置为false 避免请求权限后刷新页面
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            } else {
                Logger.i("保存图片开始下载图片");
                onDownLoad(qrCodeUrl);
            }
        } else if (i == R.id.tv_change_model) {
//            切换赠送模式
            if (giftType == TYPE_1) {
                giftType = TYPE_2;
            } else if (giftType == TYPE_2) {
                giftType = TYPE_1;
            }
            getData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        onDownLoad(qrCodeUrl);
                    } else {
                        showToast("请打开存储权限");
                    }
                }
                break;
        }
    }

    /**
     * 启动图片下载线程
     */
    private void onDownLoad(String url) {
        if (TextUtils.isEmpty(url)) {
            showToast("保存失败");
            return;
        }
        DownLoadImageService service = new DownLoadImageService(getApplicationContext(),
                url,"益联商家收款二维码",
                new ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(File file) {
                        // 在这里执行图片保存方法
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("图片已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "益联商家收款二维码文件夹");
                            }
                        });
                    }

                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        // 在这里执行图片保存方法
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("图片已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "益联商家收款二维码文件夹");
                            }
                        });
                    }

                    @Override
                    public void onDownLoadFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("保存失败");
                            }
                        });
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }
}
