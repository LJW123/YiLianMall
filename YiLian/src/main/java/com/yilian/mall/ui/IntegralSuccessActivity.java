package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.BannerDataEntity;
import com.yilian.networkingmodule.entity.GetBannerDataType;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZYH on 2017/12/9 0009.
 */

public class IntegralSuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final int INTERGRAL_SUCCESS_RESULT_CODE = 0;
    private Banner banner;
    private TextView tvNickName;
    private TextView tvIntegral;
    private String phone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_success);
        initView();
    }


    private void initView() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.white));
        findViewById(R.id.v3Shop).setVisibility(View.GONE);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvNickName = (TextView) findViewById(R.id.tv_nick_name);
        banner = (Banner) findViewById(com.yilian.luckypurchase.R.id.banner);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2.5);
        banner.setLayoutParams(layoutParams);

        findViewById(R.id.v3Back).setVisibility(View.GONE);
        TextView textView = (TextView) findViewById(R.id.v3Title);
        textView.setText("转赠成功");
        TextView V3Right = (TextView) findViewById(R.id.tv_right);
        V3Right.setVisibility(View.VISIBLE);
        V3Right.setText("完成");
        V3Right.setOnClickListener(this);

        Intent intent = getIntent();
        String integral = intent.getStringExtra("integral");
        String nickName = intent.getStringExtra("nick_name");
        String phone=intent.getStringExtra("phone");
        String content=" ("+phone+")"+ " 已收到你的转赠";
        if (TextUtils.isEmpty(nickName)){
            content="暂无昵称"+content;
        }else {
            content=nickName+content;
        }
        tvNickName.setText(content);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");//保留两位小数
        if (integral.contains(".")) {
            int startIndex = integral.indexOf(".");
            if (startIndex == 0) {
                integral = "0" + integral + "0";
            } else {
                integral = integral + "0";
            }

        }
        integral = df.format(Double.parseDouble(integral));
        tvIntegral.setText(integral);
        getBannerResult();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                onBackPressed();
                break;
        }

    }

    private void initBanner(final List<BannerDataEntity.DataBean> banerList) {
        ArrayList<String> images = new ArrayList<String>();
        for (int i = 0; i < banerList.size(); i++) {
            String image = banerList.get(i).image;
            images.add(WebImageUtil.getInstance().getWebImageUrlNOSuffix(image));
        }
        banner.setImages(images)
                .isAutoPlay(false)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        //跳转到通用界面
                        banerList.get(position);
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(banerList.get(position).type, banerList.get(position).content);
                        setResult(0);
                        finish();
                    }
                })
                .start();
    }

    private void getBannerResult() {
        Subscription subscription= RetrofitUtils3.getRetrofitService(mContext)
                    .getJumpBanner("account/get_jump_banner", GetBannerDataType.INTEGRAL_TRANSFER_SUCCESS, GetBannerDataType.FLG)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BannerDataEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(BannerDataEntity banerResult) {
                            if (banerResult == null || banerResult.data == null) return;
                            List<BannerDataEntity.DataBean> banerList = banerResult.data;
                            initBanner(banerList);
                        }
                    });
        addSubscription(subscription);
    }

    @Override
    public void onBackPressed() {
        setResult(INTERGRAL_SUCCESS_RESULT_CODE);
        super.onBackPressed();
    }
}
