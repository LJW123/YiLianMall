package com.yilianmall.merchant.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MerchantOrderListEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantOrderDetailsActivity;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by LYQ on 2017/9/28.
 */

public class OrderListAdapter extends BaseQuickAdapter<MerchantOrderListEntity.ListBean, BaseViewHolder> {

    public OrderListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MerchantOrderListEntity.ListBean item) {
        helper.getView(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantOrderListEntity.ListBean itemBean = (MerchantOrderListEntity.ListBean) item;
                final String orderIndex = itemBean.orderIndex;
                Intent intent = new Intent(mContext, MerchantOrderDetailsActivity.class);
                intent.putExtra("orderIndex", orderIndex);
                intent.putExtra("position", helper.getLayoutPosition());
                mContext.startActivity(intent);
            }
        });
        helper.addOnClickListener(R.id.ll_goods_info);
        helper.addOnClickListener(R.id.btn_order_status);
        helper.addOnClickListener(R.id.tv_phone);

        helper.setText(R.id.tv_order_id, item.orderId);
        helper.setText(R.id.tv_name, item.orderContacts);
        helper.setText(R.id.tv_phone, item.orderPhone);
        helper.setText(R.id.tv_address, item.orderAddress);
        helper.setText(R.id.tv_goods_count, "共" + item.goodsNum + "件商品");
        // DaiGouQuan: 2018/8/14 代购券业务暂时隐藏
        item.daiGouQuanMoney="0";
        helper.setText(R.id.tv_total_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(
                String.valueOf(new BigDecimal(item.orderTotalPrice).add(new BigDecimal(item.daiGouQuanMoney)))
        )));
        if (!TextUtils.isEmpty(item.orderTotalBean) && Double.parseDouble(item.orderTotalBean) > 0) {
            helper.getView(R.id.tv_total_bean).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_total_bean, "+" + MoneyUtil.getLeXiangBiNoZero(item.returnean) + Constants.APP_PLATFORM_DONATE_NAME);
        } else {
            helper.getView(R.id.tv_total_bean).setVisibility(View.GONE);
            helper.setText(R.id.tv_total_bean, "");
        }

        TextView tvStatus = helper.getView(R.id.tv_order_status);
        helper.setText(R.id.tv_freight, "(含运费" + MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.orderFreightPrice)) + ")");
        switch (item.orderStatus) {
            case "0":
                helper.setText(R.id.tv_order_status, "已取消");
                helper.getView(R.id.btn_order_status).setVisibility(View.GONE);
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_999));
                break;
            case "1":
                helper.setText(R.id.tv_order_status, "待支付");
                helper.getView(R.id.btn_order_status).setVisibility(View.GONE);
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "2":
                helper.setText(R.id.tv_order_status, "待备货");
                helper.getView(R.id.btn_order_status).setVisibility(View.VISIBLE);
                helper.setText(R.id.btn_order_status, "备货");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "3":
                helper.setText(R.id.tv_order_status, "待发货");
                helper.getView(R.id.btn_order_status).setVisibility(View.VISIBLE);
                helper.setText(R.id.btn_order_status, "整单发货");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "4":
                helper.setText(R.id.tv_order_status, "已部分发货");
                helper.getView(R.id.btn_order_status).setVisibility(View.VISIBLE);
                helper.setText(R.id.btn_order_status, "整单发货");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "5":
                helper.setText(R.id.tv_order_status, "待买家收货");
                helper.getView(R.id.btn_order_status).setVisibility(View.GONE);
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "6":
                switch (item.profitType) {
                    case 1:
                        helper.setText(R.id.tv_order_status, "已结算");
                        tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                        break;
                    default:
                        helper.setText(R.id.tv_order_status, "已完成");
                        tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_999));
                        break;
                }
                helper.getView(R.id.btn_order_status).setVisibility(View.GONE);
                break;
            default:
                break;
        }

        List<MerchantOrderListEntity.ListBean.GoodsListBean> goodsList = item.goodsList;
        //默认最多显示3张图片
        LinearLayout llImageContent = helper.getView(R.id.ll_image_content);

        //默认最多显示3张图片
        View subView = null;
        ImageView goodsIcon;
        TextView tvGoodsCount;
        if (null != goodsList && goodsList.size() > 0) {
            llImageContent.removeAllViews();
            for (int i = 0; i < goodsList.size(); i++) {
                if (i > 2) {
                    continue;
                }
                //最多只显示前三个
                subView = View.inflate(mContext, R.layout.merchant_item_sub_image, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, DPXUnitUtil.dp2px(mContext, 18), 0);
                subView.setLayoutParams(layoutParams);
                goodsIcon = (ImageView) subView.findViewById(R.id.imageView);
                GlideUtil.showImageWithSuffix(mContext, goodsList.get(i).goodsIcon, goodsIcon);
                tvGoodsCount = (TextView) subView.findViewById(R.id.tv_count);
                tvGoodsCount.setVisibility(View.VISIBLE);
                tvGoodsCount.setText("x" + goodsList.get(i).goodsCount);

                llImageContent.addView(subView);
            }
        }

    }
}
