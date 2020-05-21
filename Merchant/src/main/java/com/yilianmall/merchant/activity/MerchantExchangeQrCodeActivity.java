package com.yilianmall.merchant.activity;

import android.Manifest;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DownLoadImageService;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ImageDownLoadCallBack;
import com.yilian.networkingmodule.entity.MerchantExchangeQrCodeEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;

import java.io.File;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MerchantExchangeQrCodeActivity extends BaseActivity implements View.OnClickListener {

    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private ImageView ivBgWhite;
    private ImageView ivQrCode;
    private ImageView ivMerchantPhoto;
    private TextView tvMerchantName;
    private Button btnSaveQrCode;
    private String imgDownUrl;
    private String saveDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_exchange_qr_code);
        initView();
        initData();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("收券二维码");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        ivBgWhite = (ImageView) findViewById(R.id.iv_bg_white);
        ivQrCode = (ImageView) findViewById(R.id.iv_qr_code);
        ivMerchantPhoto = (ImageView) findViewById(R.id.iv_merchant_photo);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        btnSaveQrCode = (Button) findViewById(R.id.btn_save_qr_code);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnSaveQrCode.setOnClickListener(this);
    }

    private void initData() {
        getMerchantExchangeQrCodeData();
    }

    @SuppressWarnings("unchecked")
    private void getMerchantExchangeQrCodeData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMerchantExchangeQrCode("agent/quanQrCodeImg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantExchangeQrCodeEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MerchantExchangeQrCodeEntity merchantExchangeQrCodeEntity) {
                        GlideUtil.showImage(mContext, merchantExchangeQrCodeEntity.filename, ivQrCode);
                        GlideUtil.showCirImage(mContext, merchantExchangeQrCodeEntity.merchantImg, ivMerchantPhoto);
                        tvMerchantName.setText(merchantExchangeQrCodeEntity.merchantName);
                        imgDownUrl = Constants.ImageUrl + merchantExchangeQrCodeEntity.filename;
                    }
                });
        addSubscription(subscription);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_left) {
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
        } else if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.btn_save_qr_code) {
            saveImg();
        }
    }

    private void saveImg() {
        Logger.i("保存图片开始");
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Logger.i("保存图片请求权限");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
        } else {
            Logger.i("保存图片开始下载图片");
            onDownLoad(imgDownUrl);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        onDownLoad(imgDownUrl);
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
        saveDir = "益联商家收券二维码";
        DownLoadImageService service = new DownLoadImageService(getApplicationContext(),
                url, saveDir,
                new ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(File file) {
                        // 在这里执行图片保存方法
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("图片已保存至" +
                                        Environment.getExternalStorageDirectory().getAbsolutePath() +
                                        File.separator +
                                        saveDir + "文件夹");
                            }
                        });
                    }

                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        // 在这里执行图片保存方法
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("图片已保存至" +
                                        Environment.getExternalStorageDirectory().getAbsolutePath() +
                                        File.separator +
                                        saveDir + "文件夹");
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
