package com.yilianmall.merchant.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.networkingmodule.entity.BaseBarcodeEntity;
import com.yilian.networkingmodule.entity.ScOrderListEntity;
import com.yilian.networkingmodule.entity.ScOrderListEntityBottomBean;
import com.yilian.networkingmodule.entity.ScOrderListEntityHeadBean;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.List;


/**
 * Created by liuyuqi on 2017/8/30 0030.
 */

public class BarcodeTransactionRecodeAdapter extends BaseMultiItemQuickAdapter<BaseBarcodeEntity, BaseViewHolder> {


    public BarcodeTransactionRecodeAdapter(List<BaseBarcodeEntity> data) {
        super(data);
        addItemType(BaseBarcodeEntity.HEADVIEW, R.layout.merchant_item_barcode_transaction_head);
        addItemType(BaseBarcodeEntity.ITEMVIEW, R.layout.merchant_item_sub_barcode_transaction_recode);
        addItemType(BaseBarcodeEntity.BOTTOMVIEW, R.layout.merchant_item_barcode_transaction_bottom);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseBarcodeEntity item) {
        helper.addOnClickListener(R.id.tv_status);

        switch (helper.getItemViewType()) {
            case BaseBarcodeEntity.HEADVIEW:
                ScOrderListEntityHeadBean headBean=(ScOrderListEntityHeadBean)item;
                helper.setText(R.id.tv_order_id, headBean.orderId);
                helper.setText(R.id.tv_phone, headBean.phone);
                break;
            case BaseBarcodeEntity.ITEMVIEW:
                ScOrderListEntity.DataBean.GoodsInfoBean infoBean=(ScOrderListEntity.DataBean.GoodsInfoBean)item;
                helper.setText(R.id.tv_name,infoBean.goodsName);
                helper.setText(R.id.tv_sku,infoBean.goodsNorms);
                helper.setText(R.id.tv_count, "X " +infoBean.goodsCount);
                helper.setText(R.id.tv_sell_score, MoneyUtil.getLeXiangBiNoZero(infoBean.merchantIntegral)+"分");
                helper.setText(R.id.tv_consume_score, MoneyUtil.getLeXiangBiNoZero(infoBean.userIntegral)+"分");
                break;
            case BaseBarcodeEntity.BOTTOMVIEW:
                ScOrderListEntityBottomBean bottomBean=(ScOrderListEntityBottomBean)item;
                helper.setText(R.id.tv_total_sell_score, MoneyUtil.getLeXiangBiNoZero(bottomBean.merchantIntegral) + "分");
                helper.setText(R.id.tv_total_consume_score, MoneyUtil.getLeXiangBiNoZero(bottomBean.userIntegral) + "分");
                helper.setText(R.id.tv_total_count, "X " + bottomBean.totalCount);
                helper.setText(R.id.tv_total_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(bottomBean.orderGoodsPrice)));
                float profitPrice = Float.parseFloat(bottomBean.orderGoodsPrice) - Float.parseFloat(bottomBean.orderSupplierPrice);
                helper.setText(R.id.tv_profit_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(profitPrice)));

                switch (bottomBean.orderStatus) {
                    case "0":
                        helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_status).setBackgroundResource(R.drawable.merchant_bg_btn_red_radious_3);
                        helper.setText(R.id.tv_status, "去支付");
                        break;
                    case "1":
                        helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_status, "已完成");
                        helper.getView(R.id.tv_status).setBackgroundResource(R.drawable.merchant_bg_btn_gray_radious_3);
                        break;
                }

                break;
        }
    }
}
