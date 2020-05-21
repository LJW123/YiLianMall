package com.yilian.mall.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.entity.MerchantList;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.WebImageUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2017/3/20 0020.
 */

public class NewMerchantListAdapter extends BaseQuickAdapter<MerchantList, com.chad.library.adapter.base.BaseViewHolder> {


    public NewMerchantListAdapter(int layoutResId, @Nullable List<MerchantList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantList merchantList) {
        if (merchantList.merchantSortTime > 0) {
            helper.getView(R.id.tv_recommend).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_recommend).setVisibility(View.GONE);
        }
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(merchantList.merchantImage), helper.getView(R.id.merchant_iv_prize));
        ((TextView) helper.getView(R.id.merchant_name)).setText(merchantList.merchantName);
        ((TextView) helper.getView(R.id.merchant_address)).setText(merchantList.merchantAddress);
        ((RatingBar) helper.getView(R.id.merchant_ratingBar)).setRating(Float.parseFloat(merchantList.graded) / 10);
        if (!TextUtils.isEmpty(merchantList.praiseCount)) {
            ((TextView) helper.getView(R.id.tv_presence_count)).setText(merchantList.renQi + "人光临");
        }
        //通过经纬度计算距离
        if (TextUtils.isEmpty(merchantList.formatServiceMerDistance)) {
            ((TextView) helper.getView(R.id.merchant_distance)).setText("计算距离失败");
        } else {
            ((TextView) helper.getView(R.id.merchant_distance)).setText(merchantList.formatServiceMerDistance);
        }
        TextView tvPrivilege = (TextView) helper.getView(R.id.tv_privilege);
        //商家让利的百分比
        if (!TextUtils.isEmpty(merchantList.merDiscount)) {
            tvPrivilege.setVisibility(View.VISIBLE);
            tvPrivilege.setText(Html.fromHtml(merchantList.merDiscount + "<font><small><small>%</small></small></font>"));
        } else {
            tvPrivilege.setVisibility(View.GONE);
        }
        TextView tvIdentityOut = (TextView) helper.getView(R.id.tv_identity_out);
        TextView tvIdentityCombo = (TextView) helper.getView(R.id.tv_identity_combo);
        if ("1".equals(merchantList.isReserve)) {
            if ("1".equals(merchantList.isDelivery)) {
                tvIdentityOut.setVisibility(View.VISIBLE);
                tvIdentityCombo.setVisibility(View.GONE);
            } else {
                tvIdentityCombo.setVisibility(View.VISIBLE);
                tvIdentityOut.setVisibility(View.GONE);
            }
        } else {
            tvIdentityOut.setVisibility(View.GONE);
            tvIdentityCombo.setVisibility(View.GONE);
        }
    }

}
