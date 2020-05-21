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
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilian.mylibrary.utils.LayoutToBitmapUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderShareEntity;

import java.text.SimpleDateFormat;

/**
 * 订单分享  分享订单
 *
 * @author Created by Zg on 2018/8/20.
 */
@SuppressLint("ValidFragment")
public class CtripShareOrderFragment extends JPBaseFragment implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private TextView tvHotelName;
    private TextView tvHotelAddress;
    private TextView tvDateStart;
    private TextView tvDateEnd;
    private TextView tvDateDescribe;
    //订单价格
    private LinearLayout llOrderPrice;
    private TextView tvOrderPrice;
    //捎句话
    private LinearLayout llRemark;
    private EditText etRemark;
    private TextView tvWordLimit;
    /**
     * 显示订单总额
     */
    private TextView tvShowPrice;
    private boolean isShowPrice = false;
    /**
     * 顺便捎句话
     */
    private TextView tvShowRemark;
    private boolean isShowRemark = false;

    private ScrollView mScrollView;
    private TextView shareHotelName;
    private TextView shareHotelAddress;
    private TextView shareDateStart;
    private TextView shareDateEnd;
    private TextView shareDateDescribe;
    private TextView shareOrderPrice;
    private LinearLayout shareLlOrderPrice;
    private TextView shareRemark;
    private LinearLayout shareLlRemark;
    private ImageView shareCode;

    /**
     * 分享数据
     */
    private CtripOrderShareEntity shareData;

    public static CtripShareOrderFragment getInstance(CtripOrderShareEntity shareData) {
        CtripShareOrderFragment sf = new CtripShareOrderFragment();
        sf.shareData = shareData;
        return sf;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.ctrip_fragment_order_share_order, null);
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

        tvHotelName = (TextView) rootView.findViewById(R.id.tv_hotel_name);
        tvHotelAddress = (TextView) rootView.findViewById(R.id.tv_hotel_address);
        tvDateStart = (TextView) rootView.findViewById(R.id.tv_date_start);
        tvDateEnd = (TextView) rootView.findViewById(R.id.tv_date_end);
        tvDateDescribe = (TextView) rootView.findViewById(R.id.tv_date_describe);
        tvOrderPrice = (TextView) rootView.findViewById(R.id.tv_order_price);
        llOrderPrice = (LinearLayout) rootView.findViewById(R.id.ll_order_price);
        etRemark = (EditText) rootView.findViewById(R.id.et_remark);
        tvWordLimit = (TextView) rootView.findViewById(R.id.tv_word_limit);
        llRemark = (LinearLayout) rootView.findViewById(R.id.ll_remark);
        tvShowPrice = (TextView) rootView.findViewById(R.id.tv_show_price);
        tvShowRemark = (TextView) rootView.findViewById(R.id.tv_show_remark);

        //分享图 布局
        mScrollView = rootView.findViewById(R.id.mScrollView);
        shareHotelName = (TextView) rootView.findViewById(R.id.share_hotel_name);
        shareHotelAddress = (TextView) rootView.findViewById(R.id.share_hotel_address);
        shareDateStart = (TextView) rootView.findViewById(R.id.share_date_start);
        shareDateEnd = (TextView) rootView.findViewById(R.id.share_date_end);
        shareDateDescribe = (TextView) rootView.findViewById(R.id.share_date_describe);
        shareOrderPrice = (TextView) rootView.findViewById(R.id.share_order_price);
        shareLlOrderPrice = (LinearLayout) rootView.findViewById(R.id.share_ll_order_price);
        shareRemark = (TextView) rootView.findViewById(R.id.share_remark);
        shareLlRemark = (LinearLayout) rootView.findViewById(R.id.share_ll_remark);
        shareCode = (ImageView) rootView.findViewById(R.id.share_code);

        if (!isShowPrice) {
            llOrderPrice.setVisibility(View.GONE);
            shareLlOrderPrice.setVisibility(View.GONE);
        }
        if (!isShowRemark) {
            llRemark.setVisibility(View.GONE);
            shareLlRemark.setVisibility(View.GONE);
        }
    }

    public void initListener() {
        tvShowPrice.setOnClickListener(this);
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
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

        tvHotelName.setText(shareData.getHotelName());
        tvHotelAddress.setText(shareData.getAddress());
        tvDateStart.setText(sdf.format(Long.parseLong(shareData.getTimeSpanStart()) * 1000));
        tvDateEnd.setText(sdf.format(Long.parseLong(shareData.getTimeSpanEnd()) * 1000));
        tvDateDescribe.setText(String.format("%s晚 x%s间", shareData.getNightNum(), shareData.getNumberOfUnits()));
        tvOrderPrice.setText(Html.fromHtml(String.format("<font><small>¥</small>%s</font>", shareData.getInclusiveAmount())));

        shareHotelName.setText(shareData.getHotelName());
        shareHotelAddress.setText(shareData.getAddress());
        shareDateStart.setText(sdf.format(Long.parseLong(shareData.getTimeSpanStart()) * 1000));
        shareDateEnd.setText(sdf.format(Long.parseLong(shareData.getTimeSpanEnd()) * 1000));
        shareDateDescribe.setText(String.format("%s晚 x%s间", shareData.getNightNum(), shareData.getNumberOfUnits()));
        shareOrderPrice.setText(Html.fromHtml(String.format("<font><small>¥</small>%s</font>", shareData.getInclusiveAmount())));

        QRCodeUtils.createImage(String.format("%smobi/dist/xieDetails?startTime=%s&endTime=%s&hotelID=%s", Ip.BASE_PAY_URL,
                sdfYMD.format(Long.parseLong(shareData.getTimeSpanStart()) * 1000), sdfYMD.format(Long.parseLong(shareData.getTimeSpanEnd()) * 1000), shareData.getHotelId()), shareCode);

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
//            case R.id.head_ll_praise://热帖 点赞
//                setFirstHotPostFabulous(firstHotPost.getPostID());
//                break;
//            default:
//                break;
            case R.id.tv_show_price:
                isShowPrice = !isShowPrice;
                if (isShowPrice) {
                    tvShowPrice.setBackgroundResource(R.drawable.ctrip_order_share_btn_selected);
                    tvShowPrice.setTextColor(Color.parseColor("#FFFFFF"));
                    llOrderPrice.setVisibility(View.VISIBLE);
                    shareLlOrderPrice.setVisibility(View.VISIBLE);
                } else {
                    tvShowPrice.setBackgroundResource(R.drawable.ctrip_order_share_btn_unselect);
                    tvShowPrice.setTextColor(Color.parseColor("#4289FF"));
                    llOrderPrice.setVisibility(View.GONE);
                    shareLlOrderPrice.setVisibility(View.GONE);
                }
                break;
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
        }
    }

}