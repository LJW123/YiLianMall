package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.entity.ShopsEntity;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.NumberFormat;

import java.util.List;

/**
 * Created by  on 2017/8/1 0001.
 */

public class HomePageShopListAdapter extends BaseQuickAdapter<ShopsEntity.MerchantListBean, BaseViewHolder> {
    public HomePageShopListAdapter(@LayoutRes int layoutResId, @Nullable List<ShopsEntity.MerchantListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopsEntity.MerchantListBean item) {
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(item.merchantImage), helper.getView(R.id.merchant_iv_prize));
        helper.setText(R.id.merchant_name, item.merchantName);
        helper.setText(R.id.merchant_address, item.merchantAddress);
        helper.setRating(R.id.merchant_ratingBar, NumberFormat.convertToFloat(item.graded, 0f));
        helper.setText(R.id.tv_presence_count, item.renqi + "人光临");
        helper.setText(R.id.merchant_distance, item.formatServiceMerDistance);
        helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
//        if ("1".equals(item.isReserve)) {
//            helper.getView(R.id.tv_sustain_reserve).setVisibility(View.VISIBLE);
//        } else {
//            helper.getView(R.id.tv_sustain_reserve).setVisibility(View.GONE);
//        }
        if (!TextUtils.isEmpty(item.merDiscount)){
            helper.getView(R.id.tv_privilege).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_privilege, Html.fromHtml(item.merDiscount+"<font><small><small>%</small></small></font>"));
        }else{
            helper.getView(R.id.tv_privilege).setVisibility(View.GONE);
        }
        if ("1".equals(item.isReserve)) {
            if ("1".equals(item.isDelivery)) {
                helper.getView(R.id.tv_identity_out).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_identity_combo).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.tv_identity_out).setVisibility(View.GONE);
                helper.getView(R.id.tv_identity_combo).setVisibility(View.VISIBLE);
            }
        } else {
            helper.getView(R.id.tv_identity_out).setVisibility(View.GONE);
            helper.getView(R.id.tv_identity_combo).setVisibility(View.GONE);
        }
        if (item.merchantSortTime > 0) {
            helper.getView(R.id.tv_recommend).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_recommend).setVisibility(View.GONE);
        }
    }
}
