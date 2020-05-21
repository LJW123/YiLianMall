package com.yilianmall.merchant.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;
import com.yilian.networkingmodule.entity.SupplierListEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;

/**
 * Created by LYQ on 2017/10/10.
 */

public class AfterSaleAdapter extends BaseQuickAdapter<SupplierListEntity.ListBean, BaseViewHolder> {
    public AfterSaleAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SupplierListEntity.ListBean item) {

        helper.addOnClickListener(R.id.ll_content);
        helper.addOnClickListener(R.id.tv_look_express_exchange);
        helper.addOnClickListener(R.id.tv_look_express_btn);
        helper.addOnClickListener(R.id.tv_confirm_btn);

        switch (item.type) {
            case "0": //换货 返修
                switch (item.barterType) {
                    case "1"://换货
                        helper.setText(R.id.tv_exchangeGoods, "换货");
                        break;
                    case "2"://返修
                        helper.setText(R.id.tv_exchangeGoods, "返修");
                        break;
                }
                helper.getView(R.id.tv_refundGoods).setVisibility(View.GONE);
                helper.getView(R.id.tv_exchangeGoods).setVisibility(View.VISIBLE);
                break;
            case "1"://退货
                helper.getView(R.id.tv_exchangeGoods).setVisibility(View.GONE);
                helper.getView(R.id.tv_refundGoods).setVisibility(View.VISIBLE);
                break;
        }

        helper.setText(R.id.tv_order_time, DateUtils.timeStampToStr(Long.parseLong(item.serviceTime)));
        TextView tvStatus = helper.getView(R.id.tv_status);
        switch (item.status) {
            case "0":
                helper.setText(R.id.tv_status, "已取消");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_666));
                helper.getView(R.id.ll_refund).setVisibility(View.GONE);
                helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                break;
            case "1":
                helper.setText(R.id.tv_status, "待审核");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));

                helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_refund_parcel).setVisibility(View.INVISIBLE);
                helper.getView(R.id.tv_look_express_btn).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_look_express_btn, "拒绝");
                helper.getView(R.id.tv_confirm_btn).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_confirm_btn, "通过");
                helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);
                break;
            case "2":
                helper.setText(R.id.tv_status, "审核拒绝");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                helper.getView(R.id.ll_refund).setVisibility(View.GONE);
                helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                break;
            case "3":
                helper.setText(R.id.tv_status, "待退货");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_refund_parcel).setVisibility(View.INVISIBLE);
                helper.getView(R.id.tv_look_express_btn).setVisibility(View.GONE);
                helper.getView(R.id.tv_confirm_btn).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_confirm_btn, "确认收货");
                helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);

                helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);
                break;
            case "4":
                if (item.type.equals("0")) { //换货返修
                    switch (item.barterType) {
                        case "1"://换货
                            helper.setText(R.id.tv_status, "换货中");
                            helper.setText(R.id.tv_confirm_btn, "备货");
                            break;
                        case "2"://返修
                            helper.setText(R.id.tv_status, "维修中");
                            helper.setText(R.id.tv_confirm_btn, "已修复");
                            break;
                    }
                    helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_refund_parcel).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_look_express_btn).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_look_express_btn, "查看物流");
                    helper.getView(R.id.tv_confirm_btn).setVisibility(View.VISIBLE);

                    helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);
                    helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                } else if (item.type.equals("1")) { //退货
                    helper.setText(R.id.tv_status, "待收货");

                    helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_refund_parcel).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_look_express_btn).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_look_express_btn, "查看物流");
                    helper.getView(R.id.tv_confirm_btn).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_confirm_btn, "确认收货");
                    helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                    helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);
                }
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "5":
                if (item.type.equals("0")) { //换货返修
                    helper.setText(R.id.tv_status, "待发货");

                    helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_refund_parcel).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_look_express_btn).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_look_express_btn, "查看物流");
                    helper.getView(R.id.tv_confirm_btn).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_confirm_btn, "确认发货");
                    helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                    helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);
                } else if (item.type.equals("1")) { //退货
                    helper.setText(R.id.tv_status, "待退款");

                    helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_refund_parcel).setVisibility(View.INVISIBLE);
                    helper.getView(R.id.tv_look_express_btn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_confirm_btn).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_confirm_btn, "确认退款");
                    helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                    helper.getView(R.id.tv_look_express_exchange).setVisibility(View.GONE);
                }
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "6":
                if (item.type.equals("0")) {//换货返修
                    helper.setText(R.id.tv_status, "待买家收货");
                    initWaitForReceive(helper);
                    tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                } else if (item.type.equals("1")) {//退货
                    helper.setText(R.id.tv_status, "已完成");
                    helper.getView(R.id.ll_refund).setVisibility(View.GONE);
                    helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                    tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_666));
                }
                break;
            case "7":
                if (item.type.equals("0")) {//换货返修
                    helper.setText(R.id.tv_status, "已完成");
                } else if (item.type.equals("1")) {//退货
                    helper.setText(R.id.tv_status, "");
                }
                helper.getView(R.id.ll_refund).setVisibility(View.GONE);
                helper.getView(R.id.ll_exchange).setVisibility(View.GONE);
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_666));
                break;
            default:
                break;
        }
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        GlideUtil.showImageWithSuffix(mContext, item.goodsIcon, ivIcon);
        helper.setText(R.id.tv_name, item.goodsName);
        helper.setText(R.id.tv_sku, item.goodsNorms);
        helper.setText(R.id.tv_count, "×" + item.goodsCount);
        switch (item.payStyle) {
            case "0"://商城
                helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.goodsCost)));
                helper.getView(R.id.tv_score).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_score, mContext.getResources().getString(R.string.library_module_score) + MoneyUtil.getLeXiangBiNoZero(item.returnIntegral));
                helper.getView(R.id.tv_integral).setVisibility(View.GONE);
                helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                break;
            case "1"://易划算
                helper.getView(R.id.tv_price).setVisibility(View.GONE);
                helper.getView(R.id.tv_score).setVisibility(View.GONE);
                helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                helper.getView(R.id.tv_integral).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(item.goodsIntegral)));
                break;
            case "2"://奖券够
                helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_price, item.goodsRetail);
                helper.getView(R.id.tv_score).setVisibility(View.GONE);
                helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                helper.getView(R.id.tv_integral).setVisibility(View.VISIBLE);
                //奖券够的奖券价格是市场价-供货商的价格
                double integralPrice = Double.parseDouble(item.goodsPrice) - Double.parseDouble(item.goodsRetail);
                helper.setText(R.id.tv_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(integralPrice)));
                break;
            case "3":
            case GoodsType.CAL_POWER:
                helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.goodsCost)));
                helper.getView(R.id.tv_score).setVisibility(View.GONE);
                helper.getView(R.id.tv_integral).setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.returnBean) && Double.parseDouble(item.returnBean) > 0) {
                    helper.getView(R.id.tv_return_bean).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_return_bean, " +" + MoneyUtil.getLeXiangBiNoZero(item.returnBean) + Constants.APP_PLATFORM_DONATE_NAME);
                } else {
                    helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                    helper.setText(R.id.tv_return_bean, "");
                }
                break;
            default:
                break;
        }


    }

    private void initWaitForReceive(BaseViewHolder helper) {
        helper.getView(R.id.ll_refund).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_refund_parcel).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_look_express_btn).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_look_express_btn, "查看物流");
        helper.getView(R.id.tv_confirm_btn).setVisibility(View.GONE);
        helper.getView(R.id.ll_exchange).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_look_express_exchange).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_look_express_exchange, "查看物流");
    }


}
