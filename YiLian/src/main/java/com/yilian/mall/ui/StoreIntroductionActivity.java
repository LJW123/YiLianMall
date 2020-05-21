package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPFlagshipEntity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.MyBigDecimal;

import io.reactivex.functions.Consumer;

/**
 * 店铺简介页面
 *
 * @author Created by zhaiyaohua on 2018/5/9.
 */

public class StoreIntroductionActivity extends BaseActivity {
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    private ImageView v3Back;
    private TextView v3Title;
    private ImageView ivStore;
    private TextView storeName;
    private TextView tvMaxGiveHashRate;
    private TextView tvGoodCommentRate;
    private TextView tvStoreAddress;
    private TextView tvNotice, tvNoticeContent;
    private LinearLayout llNotice;
    private TextView tvServicePhone;
    private JPFlagshipEntity.DataBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_instruction);
        initView();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("店铺简介");
        llNotice = findViewById(R.id.ll_notice);
        ivStore = (ImageView) findViewById(R.id.iv_store);
        storeName = (TextView) findViewById(R.id.store_name);
        tvMaxGiveHashRate = (TextView) findViewById(R.id.tv_max_give_hash_rate);
        tvGoodCommentRate = (TextView) findViewById(R.id.tv_good_comment_rate);
        tvStoreAddress = (TextView) findViewById(R.id.tv_store_address);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        tvNoticeContent = findViewById(R.id.tv_notice_content);
        tvServicePhone = (TextView) findViewById(R.id.tv_service_phone);
        Intent intent = getIntent();
        bean = (JPFlagshipEntity.DataBean) intent.getSerializableExtra("store_data");
        GlideUtil.showImage(mContext, bean.supplierIcon, ivStore);
        storeName.setText(bean.supplierName);
        tvMaxGiveHashRate.setText("最高赠送比例：" + String.format("%.2f", NumberFormat.convertToDouble(MyBigDecimal.mul(bean.maxRatio,"100"),0d)) + "%");
        tvGoodCommentRate.setText("好评率      " + bean.feedBackRate + "%");
        tvStoreAddress.setText("所在地      " + bean.address);
        if (TextUtils.isEmpty(bean.noticeContent)) {
            llNotice.setVisibility(View.GONE);
        } else {
            llNotice.setVisibility(View.VISIBLE);
            tvNotice.setText("店铺" + bean.noticeMark + ": ");
            tvNoticeContent.setText(bean.noticeContent);
        }
        if (!TextUtils.isEmpty(bean.frequentPhone)) {
            String formatPhone = PhoneUtil.formatPhone(bean.frequentPhone);
            String note = "客服电话" + formatPhone;
            SpannableString spannableString = new SpannableString(note);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#1AA2E9"));
            spannableString.setSpan(colorSpan, note.length() - formatPhone.length(), note.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvServicePhone.setText(spannableString);
        }
    }

    private void initListener() {
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        RxUtil.clicks(tvServicePhone, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (null != bean && !TextUtils.isEmpty(bean.frequentPhone)) {
                    PhoneUtil.call(bean.frequentPhone, mContext);
                }
            }
        });
    }
}
