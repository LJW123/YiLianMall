package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.OrderNewListEntity;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 */

public class OrderNewListAdapter extends BaseQuickAdapter<OrderNewListEntity.DataBean, com.chad.library.adapter.base.BaseViewHolder> {

    public OrderNewListAdapter(@LayoutRes int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderNewListEntity.DataBean item) {
        TextView tvStatusName = helper.getView(R.id.tv_title);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvDesc=helper.getView(R.id.tv_desc);
        tvDesc.setMaxLines(2);
        tvDesc.setLines(2);
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        switch (item.regionType) {
            case 1:
                tvStatusName.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
                tvStatusName.setText(item.title);
                break;
            case 2:
                tvStatusName.setText(item.title);
                tvStatusName.setTextColor(ContextCompat.getColor(mContext, R.color.color_green));
                break;
        }
        helper.getView(R.id.tv_subTitle).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_subTitle, "("+item.subTitle+")");
        helper.setText(R.id.tv_desc, item.desc);
        helper.getView(R.id.tv_subDesc).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_subDesc, item.subDesc);

        String todayDate = DateUtils.getTodayDate();//yyyy-MM-dd
        String time = DateUtils.timeStampToStr(Long.parseLong(item.pushTime));
        if (time.contains(todayDate)){
            helper.setText(R.id.tv_date, DateUtils.getTime(Long.parseLong(item.pushTime)));
        }else {
            helper.setText(R.id.tv_date, time);
        }
        helper.getView(R.id.tv_read_count).setVisibility(View.GONE);
        GlideUtil.showImageWithSuffix(mContext, item.image, ivIcon);
    }
}
