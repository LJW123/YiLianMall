package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;
import static com.yilian.mall.R.id.webView;

/**
 * Created by liuyuqi on 2016/12/10 0010.
 * 图文详情
 */
public class MTPhotoTextMoreActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvExplain;
    private TextView tvComboPrice;
    private TextView tvZeng;
    private Button btnPromptlyBuy;
    private LinearLayout llComboHead;
    private WebView mWebView;
    private String package_id;
    private String price;
    private String orderName;
    private String isDelivery;
    private String mtMerchantId;
    private String fullMinusFee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_activity_photo_text);
        initView();
        initWebView();
    }

    private void initView() {

        package_id = getIntent().getStringExtra("package_id");
        price = getIntent().getStringExtra("price");
        orderName = getIntent().getStringExtra("orderName");
        isDelivery = getIntent().getStringExtra("isDelivery");
        mtMerchantId = getIntent().getStringExtra("mtMerchantId");
        fullMinusFee = getIntent().getStringExtra("fullMinusFee");

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvExplain = (TextView) findViewById(R.id.tv_explain);
        tvComboPrice = (TextView) findViewById(R.id.tv_combo_price);
        tvComboPrice.setText(MoneyUtil.getLeXiangBi(price));
        tvZeng = (TextView) findViewById(R.id.tv_zeng);
        btnPromptlyBuy = (Button) findViewById(R.id.btn_promptly_buy);
        llComboHead = (LinearLayout) findViewById(R.id.ll_combo_head);
        mWebView = (WebView) findViewById(webView);
        String userAgentString = mWebView.getSettings().getUserAgentString();
//        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        mWebView.getSettings().setUserAgentString(userAgentString+" lefen_native_ android");
        ivBack.setOnClickListener(this);
        tvExplain.setOnClickListener(this);
        btnPromptlyBuy.setOnClickListener(this);
    }

    private void initWebView() {
        //图文详情的WebView链接
        String loadUrl= Constants.PHOTO_TEXT_URL+package_id;
        mWebView.loadUrl(loadUrl);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(LOAD_CACHE_ELSE_NETWORK);

        mWebView.requestFocus();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
               MTPhotoTextMoreActivity. this.finish();
                break;
            case R.id.tv_explain:
               //送券说明
                Intent intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("url", Constants.HowToGetCoupon);
                startActivity(intent);
                break;
            case R.id.btn_promptly_buy:
                initNowBuy();
                break;
        }
    }

    private void initNowBuy()  {
        Intent intent;
        if (!isLogin()){
            intent=new Intent(this,LeFenPhoneLoginActivity.class);
            startActivity(intent);
        }else{
            intent=new Intent(this,MTSubmitOrderActivity.class);
            intent.putExtra("package_id",package_id);
            intent.putExtra("price",price);
            intent.putExtra("orderName",orderName);
            intent.putExtra("isDelivery",isDelivery);
            intent.putExtra("mtMerchantId", mtMerchantId);
            intent.putExtra("fullMinusFee",fullMinusFee);
            startActivity(intent);
        }
        finish();
    }


}
