package com.yilianmall.merchant.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.MerchantManagerListEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantChooseDialogActivity;
import com.yilianmall.merchant.activity.MerchantManagerDetailActivity;
import com.yilianmall.merchant.utils.MoneyUtil;


/**
 * Created by Ray_L_Pain on 2017/9/29 0029.
 */

public class MerchantManagerListAdapter extends BaseQuickAdapter<MerchantManagerListEntity.DataBean, BaseViewHolder> {

    public MerchantManagerListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantManagerListEntity.DataBean item) {
        final MerchantManagerListEntity.DataBean entity = item;

        ImageView iv = helper.getView(R.id.iv_pic);
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(entity.goods_icon), iv);

        helper.setText(R.id.tv_title, entity.goods_name);

        helper.setText(R.id.tv_sales, "销量 " + entity.goods_sale);

        helper.setText(R.id.tv_inventory, "库存 " + entity.goods_sku_count);

//        TextView tvMarkNormal = helper.getView(R.id.tv_mark_normal);
//        if ("1".equals(entity.is_normal)) {
//            tvMarkNormal.setVisibility(View.VISIBLE);
//        } else {
//            tvMarkNormal.setVisibility(View.GONE);
//        }
//
//        TextView tvMarkYhs = helper.getView(R.id.tv_mark_yhs);
//        if ("1".equals(entity.is_yhs)) {
//            tvMarkYhs.setVisibility(View.VISIBLE);
//        } else {
//            tvMarkYhs.setVisibility(View.GONE);
//        }
//
//        TextView tvMarkJfg = helper.getView(R.id.tv_mark_jfg);
//        if ("1".equals(entity.is_jfg)) {
//            tvMarkJfg.setVisibility(View.VISIBLE);
//        } else {
//            tvMarkJfg.setVisibility(View.GONE);
//        }

        TextView tvStatus = helper.getView(R.id.tv_status);
//        TextView tvBtn = helper.getView(R.id.tv_btn);
//        LinearLayout layoutZone = helper.getView(R.id.layout_zone);
        switch (entity.goods_status) {
            case "0":
                if ("0".equals(entity.submit_status)) {
                    tvStatus.setText("待提交");
                    tvStatus.setTextColor(mContext.getResources().getColor(R.color.merchant_color_red));
                } else if ("1".equals(entity.submit_status)) {
                    tvStatus.setText("待审核");
                    tvStatus.setTextColor(mContext.getResources().getColor(R.color.merchant_color_999));
                }
                tvStatus.setCompoundDrawables(null, null, null, null);
//                tvBtn.setVisibility(View.GONE);
//                layoutZone.setVisibility(View.INVISIBLE);
                break;
            case "1":
                tvStatus.setText("审核通过");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.merchant_color_999));
                tvStatus.setCompoundDrawables(null, null, null, null);
//                tvBtn.setVisibility(View.VISIBLE);
//                layoutZone.setVisibility(View.VISIBLE);
                break;
            case "2":
                tvStatus.setText("审核通过");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.merchant_color_999));
                tvStatus.setCompoundDrawables(null, null, null, null);
//                tvBtn.setVisibility(View.VISIBLE);
//                layoutZone.setVisibility(View.VISIBLE);
                break;
            case "3":
                tvStatus.setText("审核通过");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.merchant_color_999));
                tvStatus.setCompoundDrawables(null, null, null, null);
//                tvBtn.setVisibility(View.VISIBLE);
//                layoutZone.setVisibility(View.VISIBLE);
                break;
            case "4":
                tvStatus.setText("审核拒绝");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.merchant_color_red));
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.merchant_audit_refuse);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStatus.setCompoundDrawables(null, null, drawable, null);
//                tvBtn.setVisibility(View.GONE);
//                layoutZone.setVisibility(View.INVISIBLE);

                tvStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog builder = new AlertDialog.Builder(mContext).create();
                        builder.setTitle("拒绝原因：");
                        builder.setMessage(entity.refuse_reason);
                        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.merchant_color_333));
                    }
                });
                break;
            default:
                break;
        }

        TextView tvPriceMarker = helper.getView(R.id.tv_price_marker);
//        tvPriceMarker.setText(Html.fromHtml("<font color=\\\"#999999\\\">市场价(元)      </font><font color=\\\"#333333\\\"><big>" + MoneyUtil.getLeXiangBiNoZero(entity.goods_price) + "</big></font>"));
        tvPriceMarker.setText(MoneyUtil.getLeXiangBiNoZero(entity.goods_price));
//        TextView tvPriceYhs = helper.getView(R.id.tv_price_yhs);
//        tvPriceYhs.setText(Html.fromHtml("<font color=\\\"#999999\\\">易划算价(元)      </font><font color=\\\"#333333\\\"><big>" + MoneyUtil.getLeXiangBiNoZero(entity.goods_integral) + "</big></font>"));

        TextView tvPriceSale = helper.getView(R.id.tv_price_sale);
//        tvPriceSale.setText(Html.fromHtml("<font color=\\\"#999999\\\">售价(元)      </font><font color=\\\"#333333\\\"><big>" + MoneyUtil.getLeXiangBiNoZero(entity.goods_cost) + "</big></font>"));
        tvPriceSale.setText(MoneyUtil.getLeXiangBiNoZero(entity.goods_cost));
        
        TextView tvPriceReal = helper.getView(R.id.tv_price_real);
//        tvPriceReal.setText(Html.fromHtml("<font color=\\\"#999999\\\">成本价(元)      </font><font color=\\\"#333333\\\"><big>" + MoneyUtil.getLeXiangBiNoZero(entity.retail_price) + "</big></font>"));
        tvPriceReal.setText(MoneyUtil.getLeXiangBiNoZero(entity.retail_price));

//        tvBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MerchantChooseDialogActivity.class);
//                intent.putExtra("goods_id", entity.goods_index);
//                intent.putExtra("is_normal", entity.is_normal);
//                intent.putExtra("is_yhs", entity.is_yhs);
//                intent.putExtra("is_jfg", entity.is_jfg);
//                intent.putExtra("authority", entity.authority);
//                mContext.startActivity(intent);
//            }
//        });

        LinearLayout itemLayout = helper.getView(R.id.item_layout);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ("1".equals(entity.goods_status) || "2".equals(entity.goods_status) || "3".equals(entity.goods_status) ) {
                Intent intent = new Intent(mContext, MerchantManagerDetailActivity.class);
                intent.putExtra("goods_id", entity.goods_index);
                intent.putExtra("is_normal", entity.is_normal);
                intent.putExtra("is_yhs", entity.is_yhs);
                intent.putExtra("is_jfg", entity.is_jfg);
                mContext.startActivity(intent);
//                } else {
//                    Toast.makeText(mContext, R.string.merchant_shop_cant_show, Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

}
