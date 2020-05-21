package com.yilianmall.merchant.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MerchantOrderEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantExpressSelectActivity;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.List;

/**
 * Created by LYQ on 2017/10/9.
 */

public class OrderDetailAdapter extends BaseQuickAdapter<MerchantOrderEntity.GoodsBean, BaseViewHolder> {
    private final MerchantOrderEntity orderEntity;
    private String orderStatus;

    public OrderDetailAdapter(int layoutId, List<MerchantOrderEntity.GoodsBean> goods, String orderStatus, MerchantOrderEntity orderEntity) {
        super(layoutId, goods);
        this.orderStatus = orderStatus;
        this.orderEntity = orderEntity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MerchantOrderEntity.GoodsBean item) {
        helper.setText(R.id.tv_good_name, item.goodsName);
        helper.setText(R.id.tv_sku, item.goodsSku);
        helper.setText(R.id.tv_count, "X " + item.goodsCount);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        GlideUtil.showImageWithSuffix(mContext, item.goodsIcon, ivIcon);

        switch (item.type) {
            case "0"://商城
                helper.getView(R.id.tv_integral).setVisibility(View.GONE);
                helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.goodsPrice)));

                if (TextUtils.isEmpty(item.returnIntegral) && Double.parseDouble(item.returnIntegral) > 0) {
                    helper.getView(R.id.tv_score).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_score, mContext.getResources().getString(R.string.library_module_score) + MoneyUtil.getLeXiangBiNoZero(item.returnIntegral));
                } else {
                    helper.getView(R.id.tv_score).setVisibility(View.GONE);
                    helper.setText(R.id.tv_score, "");
                }
                break;
            case "1"://易划算
                helper.getView(R.id.tv_price).setVisibility(View.GONE);
                helper.getView(R.id.tv_score).setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.goodsIntegral) && Double.parseDouble(item.goodsIntegral) > 0) {
                    helper.getView(R.id.tv_integral).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(item.goodsIntegral)));
                } else {
                    helper.getView(R.id.tv_integral).setVisibility(View.GONE);
                    helper.setText(R.id.tv_integral, "");
                }
                break;
            case "2"://奖券够
                helper.getView(R.id.tv_score).setVisibility(View.GONE);
                helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.goodsPrice)));
                if (!TextUtils.isEmpty(item.goodsIntegral) && Double.parseDouble(item.goodsIntegral) > 0) {
                    helper.getView(R.id.tv_integral).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(item.goodsIntegral)));
                } else {
                    helper.getView(R.id.tv_integral).setVisibility(View.GONE);
                    helper.setText(R.id.tv_integral, "");
                }
                break;
        }
        if (orderStatus.equals("0")) {
            //已取消的订单
            helper.setText(R.id.tv_order_status, "未发货");
            helper.getView(R.id.tv_revise_logistics).setVisibility(View.GONE);
            helper.getView(R.id.tv_look_logistics).setVisibility(View.GONE);
        } else {
            switch (item.goodsStatus) {
                case "0":
                    helper.setText(R.id.tv_order_status, "未发货");
                    helper.getView(R.id.tv_look_logistics).setVisibility(View.GONE);
                    helper.getView(R.id.tv_revise_logistics).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_revise_logistics, "发 货");
                    helper.getView(R.id.tv_revise_logistics).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            发货
                            Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
                            intent.putExtra("parcelIndex", item.orderGoodsIndex);
                            intent.putExtra("orderIndex", orderEntity.orderIndex);
                            intent.putExtra("fromPage", "OrderDetailAdapter");
                            intent.putExtra(Constants.JUMP_STATUS, "order");

                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case "1":
                    helper.setText(R.id.tv_order_status, "已发货");
                    helper.getView(R.id.tv_look_logistics).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_look_logistics, "查看物流");
                    helper.getView(R.id.tv_revise_logistics).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_revise_logistics, "修改物流");
                    break;
                case "2":
                    helper.setText(R.id.tv_order_status, "待评价");
                    break;
                case "3":
                    helper.setText(R.id.tv_order_status, "已评价");
                    break;
            }

        }

    }
}
