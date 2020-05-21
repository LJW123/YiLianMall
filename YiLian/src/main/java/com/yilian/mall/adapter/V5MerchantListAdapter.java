package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.V5MerchantListEntity;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.SlantedTextView;

/**
 * Created by Ray_L_Pain on 2017/12/21 0021.
 */

public class V5MerchantListAdapter extends BaseQuickAdapter<V5MerchantListEntity.MerchantListBean, com.chad.library.adapter.base.BaseViewHolder> {

    public V5MerchantListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, V5MerchantListEntity.MerchantListBean item) {
        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showImage(mContext, item.merchantImage, iv);

        SlantedTextView tvTag = helper.getView(R.id.tv_tag);
        if ("1".equals(item.isReserve)) {
            tvTag.setVisibility(View.VISIBLE);
            if ("1".equals(item.isDelivery)) {
                tvTag.setText("外卖");
            } else {
                tvTag.setText("套餐");
            }
        } else {
            tvTag.setVisibility(View.GONE);
        }

        TextView tvTuijian = helper.getView(R.id.tv_tuijian);
        if (item.merchantSortTime > 0) {
            tvTuijian.setVisibility(View.VISIBLE);
        } else {
            tvTuijian.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_name, item.merchantName);

        LinearLayout percentLayout = helper.getView(R.id.layout_percent);
        if (!TextUtils.isEmpty(item.merDiscount)) {
            percentLayout.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_percent, item.merDiscount + "%");
        } else {
            percentLayout.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_renqi, "人气" + item.renqi);

        helper.setText(R.id.tv_address, item.merchantAddress);

        TextView tvDistance = helper.getView(R.id.tv_distance);
        if (!TextUtils.isEmpty(item.formatServiceMerDistance)) {
            tvDistance.setText(item.formatServiceMerDistance);
        } else {
            tvDistance.setText("计算距离失败");
        }

        View viewLine = helper.getView(R.id.view_line);

    }

}
