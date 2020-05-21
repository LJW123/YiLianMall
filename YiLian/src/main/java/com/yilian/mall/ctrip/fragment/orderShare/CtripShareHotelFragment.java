package com.yilian.mall.ctrip.fragment.orderShare;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.CustomRoundAngleImageView;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilian.mylibrary.utils.KeyBordUtils;
import com.yilian.mylibrary.utils.LayoutToBitmapUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderShareEntity;

import java.text.SimpleDateFormat;

/**
 * 订单分享  分享产品
 *
 * @author Created by Zg on 2018/8/20.
 */
@SuppressLint("ValidFragment")
public class CtripShareHotelFragment extends JPBaseFragment implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;

    private CustomRoundAngleImageView ivImg;
    private TextView tvMinPrice;
    private TextView tvHotelName;
    private TextView tvHotelAddress;
    private TextView tvHotelRating;
    private EditText etRemark;
    private TextView tvWordLimit;
    private LinearLayout llRemark;
    /**
     * 顺便捎句话
     */
    private TextView tvShowRemark;
    private boolean isShowRemark = false;

    private ScrollView mScrollView;
    private CustomRoundAngleImageView shareImg;
    private TextView shareMinPrice;
    private TextView shareHotelName;
    private TextView shareHotelAddress;
    private TextView shareHotelRating;
    private TextView shareRemark;
    private LinearLayout shareLlRemark;
    private ImageView shareCode;
    /**
     * 分享数据
     */
    private CtripOrderShareEntity shareData;

    public static CtripShareHotelFragment getInstance(CtripOrderShareEntity shareData) {
        CtripShareHotelFragment sf = new CtripShareHotelFragment();
        sf.shareData = shareData;
        return sf;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.ctrip_fragment_order_share_hotel, null);
        }
        initView();
        initListener();
        initData();
        return rootView;
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setCtripOrderList(R.id.vary_content, rootView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
            }
        });

        ivImg = (CustomRoundAngleImageView) rootView.findViewById(R.id.iv_img);
        tvMinPrice = (TextView) rootView.findViewById(R.id.tv_min_price);
        tvHotelName = (TextView) rootView.findViewById(R.id.tv_hotel_name);
        tvHotelAddress = (TextView) rootView.findViewById(R.id.tv_hotel_address);
        tvHotelRating = (TextView) rootView.findViewById(R.id.tv_hotel_rating);
        etRemark = (EditText) rootView.findViewById(R.id.et_remark);
        tvWordLimit = (TextView) rootView.findViewById(R.id.tv_word_limit);
        llRemark = (LinearLayout) rootView.findViewById(R.id.ll_remark);
        tvShowRemark = (TextView) rootView.findViewById(R.id.tv_show_remark);

        //分享图 布局
        mScrollView =  rootView.findViewById(R.id.mScrollView);
        shareImg = (CustomRoundAngleImageView) rootView.findViewById(R.id.share_img);
        shareMinPrice = (TextView) rootView.findViewById(R.id.share_min_price);
        shareHotelName = (TextView) rootView.findViewById(R.id.share_hotel_name);
        shareHotelAddress = (TextView) rootView.findViewById(R.id.share_hotel_address);
        shareHotelAddress = (TextView) rootView.findViewById(R.id.share_hotel_address);
        shareHotelRating = (TextView) rootView.findViewById(R.id.share_hotel_rating);
        shareRemark = (TextView) rootView.findViewById(R.id.share_remark);
        shareLlRemark = (LinearLayout) rootView.findViewById(R.id.share_ll_remark);
        shareCode = (ImageView) rootView.findViewById(R.id.share_code);

        if (!isShowRemark) {
            llRemark.setVisibility(View.GONE);
            shareLlRemark.setVisibility(View.GONE);
        }

    }

    public void initListener() {
        tvShowRemark.setOnClickListener(this);
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvWordLimit.setText(s.length() + "/50");
            }
        });
    }

    public void initData() {
        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

        GlideUtil.showImgAdapt(mContext, shareData.getImage(), ivImg);
        tvMinPrice.setText(Html.fromHtml(String.format("<font><small><small>¥</small></small> %s<small>起</small></font>", shareData.getMinPrice())));
        tvHotelName.setText(shareData.getHotelName());
        tvHotelAddress.setText(shareData.getAddress());
        tvHotelRating.setText(Html.fromHtml(String.format("<font>%s<small><small>分</small></small></font>", shareData.getCtripUserRating())));

        GlideUtil.showImgAdapt(mContext, shareData.getImage(), shareImg);
        shareMinPrice.setText(Html.fromHtml(String.format("<font><small><small>¥</small></small> %s<small>起</small></font>", shareData.getMinPrice())));
        shareHotelName.setText(shareData.getHotelName());
        shareHotelAddress.setText(shareData.getAddress());
        shareHotelRating.setText(Html.fromHtml(String.format("<font>%s<small><small>分</small></small></font>", shareData.getCtripUserRating())));

        QRCodeUtils.createImage(String.format("%smobi/dist/xieDetails?startTime=%s&endTime=%s&hotelID=%s", Ip.BASE_PAY_URL,
               sdfYMD.format(Long.parseLong(shareData.getTimeSpanStart())*1000),sdfYMD.format( Long.parseLong(shareData.getTimeSpanEnd())*1000),shareData.getHotelId()), shareCode);
        varyViewUtils.showDataView();
    }

    /**
     * 设置分享布局数据
     */
    public void setShareLayoutData() {
        dismissInputMethod();
        if (isShowRemark) {
            if (TextUtils.isEmpty(etRemark.getText().toString())) {
                shareLlRemark.setVisibility(View.GONE);
            } else {
                shareRemark.setText(etRemark.getText().toString());
            }
        }
    }

    /**
     * 分享布局转Bitmap
     */
    public Bitmap createShareBitmap() {
        Bitmap cacheBitmapFromView = LayoutToBitmapUtils.shotScrollView(mScrollView);
        return cacheBitmapFromView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_remark:
                isShowRemark = !isShowRemark;
                if (isShowRemark) {
                    tvShowRemark.setBackgroundResource(R.drawable.ctrip_order_share_btn_selected);
                    tvShowRemark.setTextColor(Color.parseColor("#FFFFFF"));
                    etRemark.requestFocus();
                    showInputMethod();
                    llRemark.setVisibility(View.VISIBLE);
                    shareLlRemark.setVisibility(View.VISIBLE);
                } else {
                    tvShowRemark.setBackgroundResource(R.drawable.ctrip_order_share_btn_unselect);
                    tvShowRemark.setTextColor(Color.parseColor("#4289FF"));
                    dismissInputMethod();
                    llRemark.setVisibility(View.GONE);
                    shareLlRemark.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


}