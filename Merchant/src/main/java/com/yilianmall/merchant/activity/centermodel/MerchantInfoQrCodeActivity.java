package com.yilianmall.merchant.activity.centermodel;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.BaseActivity;

/**
 *  商家店铺个人二维码
 * @author Ray_L_Pain
 * @date 2018/2/6 0006
 */

public class MerchantInfoQrCodeActivity extends BaseActivity {

    private ImageView ivBack;
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvAddress;
    private ImageView ivQrCode;

    private String proviceId, cityId;
    public String intentImg, intentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_info_qr_code);
        initView();

        StatusBarUtil.setColor(mContext, Color.BLACK);
    }

    private void initView() {
        proviceId = getIntent().getStringExtra("pro_id");
        cityId = getIntent().getStringExtra("city_id");
        intentImg = getIntent().getStringExtra("img");
        intentTitle = getIntent().getStringExtra("name");

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        ivQrCode = (ImageView) findViewById(R.id.iv_qr_code);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        GlideUtil.showImage(mContext,intentImg, ivPhoto);
        tvName.setText(intentTitle);
        tvAddress.setText(proviceId + "   " + cityId);

        String qrCodeUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext);
        Logger.i("2018年2月8日 14:11:54-" + qrCodeUrl);
        QRCodeUtils.createQRLogoImage(qrCodeUrl, ivQrCode.getWidth(), ivQrCode.getHeight(), BitmapFactory.decodeResource(getResources(), R.mipmap.library_module_yilianlogo),ivQrCode);
    }
}
