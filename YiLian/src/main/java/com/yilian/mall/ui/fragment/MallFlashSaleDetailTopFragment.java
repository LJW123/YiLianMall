package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BannerViewPager;
import com.yilian.mall.entity.MallFlashGoodsEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.listener.onLoadErrorListener;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.ui.JPCommitOrderActivity;
import com.yilian.mall.ui.MallFlashSaleDetailActivity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.CountdownView.CountdownView;
import com.yilian.mall.widgets.CountdownView.DynamicConfig;
import com.yilian.mall.widgets.CustScrollView;
import com.yilian.mall.widgets.ScreenNumView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Ip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by liuyuqi on 2017/4/13 0013.
 */
public class MallFlashSaleDetailTopFragment extends BaseFragment {

    private ViewPager viewPager;
    private ScreenNumView vpIndicator;
    private TextView tvCostPrice;
    private TextView tvPrice;
    private LinearLayout llDownTimeContent;
    private TextView tvResidue;
    private LinearLayout llAnticipate;
    private TextView tvContainerTag;
    private TextView tvContainerTitle;
    private RatingBar ratingBar;
    private TextView tvContainerGrade;
    private TextView tvContainerAssure1;
    private TextView tvContainerAssure2;
    private TextView tvContainerAssure3;
    private View viewContainerChoose;
    private TextView tvProductName;
    private TextView tvProductFrom;
    private TextView tvProductKnow;
    private LinearLayout llProductKnow;
    private TextView tvProductDesc;
    private TextView tvShopName;
    private CustScrollView customScrollView;
    private MallFlashSaleDetailActivity mactivity;
    private String goodsId;
    private LinearLayout llShoose;
    private TextView tvShoose;
    private onLoadErrorListener onUploadListener;
    private CountdownView countdownView;
    private TextView nowBuyTag;
    private String residue;
    private String filialeId;
    private MallFlashGoodsEntity.DataBean.InfoBean infoBean;
    private String skuContent = "";
    private BannerViewPager adapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flash_detail_top_fragment, container, false);
        initView(view);
        mactivity = (MallFlashSaleDetailActivity) getActivity();
        goodsId = mactivity.goodsId;
        filialeId = mactivity.filialeId;
        initLinstener();
        return view;
    }

    private void initLinstener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int finalPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                finalPosition = position;
                vpIndicator.snapToPage(finalPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (finalPosition == adapter.getCount() - 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (0 == finalPosition) {
                    viewPager.setCurrentItem(adapter.getCount() - 2, false);
                }
            }
        });
        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(boolean isEnd) {
                if (isEnd) {
                    llDownTimeContent.setVisibility(View.GONE);
                    llAnticipate.setVisibility(View.VISIBLE);
                    if ("0".equals(residue)) {
                        nowBuyTag.setText("已抢光");
                        tvResidue.setText("仅剩" + residue + "件");
                        nowBuyTag.setTextColor(getResources().getColor(R.color.color_999));
                    } else {
                        tvResidue.setText("仅剩" + residue + "件");
                        nowBuyTag.setText("马上抢");
                        nowBuyTag.setTextColor(getResources().getColor(R.color.color_residue));
                    }
                } else {
                    llAnticipate.setVisibility(View.GONE);
                    llDownTimeContent.setVisibility(View.VISIBLE);
                }
            }
        });
        llAnticipate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowBuyTag.getText().toString().trim().equals("已抢光")) {
                    showToast("暂无商品");
                    return;
                }
                Intent intent;
                if (isLogin()) {
                    intent = new Intent(mContext, JPCommitOrderActivity.class);
                    ArrayList<OrderSubmitGoods> orderSubmitGoodses = new ArrayList();
                    OrderSubmitGoods orderSubmitGoods = new OrderSubmitGoods();

                    orderSubmitGoods.goodsId = infoBean.goodsId;
                    orderSubmitGoods.goodsType = 0;
                    orderSubmitGoods.goodsName = infoBean.name;
                    orderSubmitGoods.name = infoBean.goodsBrand;
                    orderSubmitGoods.goodsPic = infoBean.goodsAlbum.get(0);
                    orderSubmitGoods.goodsPrice = NumberFormat.convertToDouble(infoBean.marketPrice, 0.00);
                    orderSubmitGoods.goodsCost = NumberFormat.convertToDouble(infoBean.voucherPrice, 0.00);
                    orderSubmitGoods.goodsNorms = skuContent;
                    orderSubmitGoods.goodsProperty = "";
                    orderSubmitGoods.goodsCount = 1;
                    orderSubmitGoods.goodsType = 6;
                    orderSubmitGoods.filiale_id = "1";
                    orderSubmitGoods.supplier_id = infoBean.supplierId;
                    orderSubmitGoods.region_id = "0";
                    orderSubmitGoodses.add(orderSubmitGoods);
                    Logger.i("2016-12-15orderSubmitGoods:" + orderSubmitGoods);
                    intent.putExtra("OrderType", "MallFlashSaleDetailTopFragment");
                    intent.putExtra("orderSubmitGoods", orderSubmitGoodses);
                } else {
                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void loadData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).getMallDetailFlashSale(goodsId, new Callback<MallFlashGoodsEntity>() {
            @Override
            public void onResponse(Call<MallFlashGoodsEntity> call, Response<MallFlashGoodsEntity> response) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                    stopMyDialog();
                    Logger.i("请求本地显示抢购详情 code " + response.body().code);
                    if (null != onUploadListener) {
                        onUploadListener.isError(false);
                    }
                    if (null == response.body().msg) {
                        showToast("暂无数据");
                        return;
                    }
                    if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                        infoBean = response.body().data.info;
                        if (null != infoBean) {
                            initViewData(infoBean);
                            mactivity.picId = infoBean.picId;
                            residue = infoBean.overplus;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MallFlashGoodsEntity> call, Throwable t) {
                t.printStackTrace();
                if (null != onUploadListener) {
                    onUploadListener.isError(true);
                }
                stopMyDialog();

                Logger.i("onFailure  " + onUploadListener);
            }
        });
    }

    private void initViewData(MallFlashGoodsEntity.DataBean.InfoBean infoBean) {
        initViewPager(infoBean.goodsAlbum);
        tvCostPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(infoBean.voucherPrice)));
        tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(infoBean.marketPrice)));
        if (!TextUtils.isEmpty(infoBean.tagsMsg)) {
            tvContainerTag.setVisibility(View.VISIBLE);
            tvContainerTag.setText(infoBean.tagsMsg);
            //因为是帧布局所以要计算字符换长度
            int length = tvContainerTag.getText().toString().length();
            String space = "         ";
            for (int i = 0; i < length; i++) {
                space += " ";
            }
            tvContainerTitle.setText(space + infoBean.name);
        } else {
            tvContainerTitle.setText(infoBean.name);
        }
        if (!TextUtils.isEmpty(infoBean.goodsGrade)) {
            float grade = (Float.parseFloat(infoBean.goodsGrade) / 10);
            ratingBar.setRating((int) grade);
            tvContainerGrade.setText(String.valueOf(grade) + "分");
        } else {
            ratingBar.setRating(5);
            tvContainerGrade.setText(String.valueOf(5.0) + "分");
        }
        switchTimer(infoBean.startAt, String.valueOf(infoBean.time), infoBean.overplus);
        switchSku(infoBean.goodsSku);
        tvProductName.setText(infoBean.goodsBrand);
        tvProductFrom.setText(infoBean.supplier_address);
        tvProductDesc.setText(infoBean.supplierFreightDesc);
        if (null != infoBean.descTags && infoBean.descTags.size() > 0) {
            tvContainerAssure1.setVisibility(View.VISIBLE);
            tvContainerAssure1.setText(infoBean.descTags.get(0));
            tvContainerAssure2.setVisibility(View.VISIBLE);
            tvContainerAssure2.setText(infoBean.descTags.get(1));
            tvContainerAssure3.setVisibility(View.VISIBLE);
            tvContainerAssure3.setText(infoBean.descTags.get(2));
        }

        mactivity.urlOne = Ip.getWechatURL(mContext) + "m/goods/contentPic.php?goods_id=" + infoBean.picId + "&filiale_id=" + filialeId + "&goods_supplier=" + infoBean.supplierId;


    }

    private void initViewPager(List<String> goodsAlbum) {
        if (null != goodsAlbum && goodsAlbum.size() >= 1) {
            adapter = new BannerViewPager(goodsAlbum, imageLoader, options, mContext, true);
            viewPager.setAdapter(adapter);
            vpIndicator.initScreen(goodsAlbum.size());
            viewPager.setCurrentItem(1);
        }
    }

    private void switchSku(List<String> goodsSku) {
        if (null != goodsSku && goodsSku.size() >= 1) {
            llShoose.setVisibility(View.VISIBLE);
            tvShoose.setVisibility(View.VISIBLE);
            for (int i = 0; i < goodsSku.size(); i++) {
                if (i == goodsSku.size() - 1) {
                    skuContent += goodsSku.get(i);
                } else {
                    skuContent += goodsSku.get(i) + ",";
                }
            }
            tvShoose.setText(skuContent);
        }
    }

    private void switchTimer(String buyTime, String time, String residue) {
        if (!TextUtils.isEmpty(buyTime)) {
            llDownTimeContent.setVisibility(View.VISIBLE);
            long differTime = (Long.parseLong(buyTime) - Long.parseLong(time));
            Logger.i("time  " + time + " differtime  " + differTime);
            if (differTime * 1000 < 0) {
                llDownTimeContent.setVisibility(View.GONE);
                llAnticipate.setVisibility(View.VISIBLE);
                if ("0".equals(residue)) {
                    nowBuyTag.setText("已抢光");
                    tvResidue.setTextColor(getResources().getColor(R.color.color_999));
                } else {
                    nowBuyTag.setText("马上抢");
                    tvResidue.setTextColor(getResources().getColor(R.color.color_residue));
                }
                tvResidue.setText("仅剩" + residue + "件");
            } else {
                llAnticipate.setVisibility(View.GONE);
                llDownTimeContent.setVisibility(View.VISIBLE);
                countdownView.start(differTime * 1000);
                countdownView.dynamicShow(new DynamicConfig.Builder().setConvertDaysToHours(true).build());

            }
        }
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vp_comm_img);
        vpIndicator = (ScreenNumView) view.findViewById(R.id.vp_indicator);
        tvCostPrice = (TextView) view.findViewById(R.id.tv_cost_price);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        countdownView = (CountdownView) view.findViewById(R.id.countdownview);
        llDownTimeContent = (LinearLayout) view.findViewById(R.id.ll_down_time_content);
        tvResidue = (TextView) view.findViewById(R.id.tv_residue);
        nowBuyTag = (TextView) view.findViewById(R.id.now_buy_tag);
        llAnticipate = (LinearLayout) view.findViewById(R.id.ll_anticipate);
        tvContainerTag = (TextView) view.findViewById(R.id.tv_container_tag);
        tvContainerTitle = (TextView) view.findViewById(R.id.tv_container_title);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        llShoose = (LinearLayout) view.findViewById(R.id.ll_container_choose);
        tvShoose = (TextView) view.findViewById(R.id.tv_container_choose);
        tvContainerGrade = (TextView) view.findViewById(R.id.tv_container_grade);
        tvContainerAssure1 = (TextView) view.findViewById(R.id.tv_container_assure1);
        tvContainerAssure2 = (TextView) view.findViewById(R.id.tv_container_assure2);
        tvContainerAssure3 = (TextView) view.findViewById(R.id.tv_container_assure3);
        viewContainerChoose = (View) view.findViewById(R.id.view_container_choose);
        tvProductName = (TextView) view.findViewById(R.id.tv_product_name);
        tvProductFrom = (TextView) view.findViewById(R.id.tv_product_from);
        tvProductKnow = (TextView) view.findViewById(R.id.tv_product_know);
        llProductKnow = (LinearLayout) view.findViewById(R.id.ll_product_know);
        tvProductDesc = (TextView) view.findViewById(R.id.tv_product_desc);
        tvShopName = (TextView) view.findViewById(R.id.tv_shop_name);
        customScrollView = (CustScrollView) view.findViewById(R.id.customScrollView);

    }




    public void setOnUploadListener(onLoadErrorListener onUploadListener) {
        this.onUploadListener = onUploadListener;
    }
}
