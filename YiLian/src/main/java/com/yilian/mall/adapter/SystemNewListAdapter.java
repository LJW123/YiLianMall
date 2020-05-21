package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.SystemNewListEntity;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 */

public class SystemNewListAdapter extends BaseQuickAdapter<SystemNewListEntity.DataBean, com.chad.library.adapter.base.BaseViewHolder> {
    public SystemNewListAdapter(@LayoutRes int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemNewListEntity.DataBean item) {
        TextView tvStatusName = helper.getView(R.id.tv_title);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        helper.getView(R.id.ll_bottom).setVisibility(View.GONE);
        tvStatusName.setText(item.title);
        tvStatusName.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        GlideUtil.showImageWithSuffix(mContext, item.image, ivIcon);

        String todayDate = DateUtils.getTodayDate();
        String time = DateUtils.timeStampToStr(Long.parseLong(item.pushTime));
        if (time.contains(todayDate)){
            helper.setText(R.id.tv_date, DateUtils.getTime(Long.parseLong(item.pushTime)));
        }else {
            helper.setText(R.id.tv_date, time);
        }
        helper.setText(R.id.tv_desc,item.desc);
        helper.getView(R.id.tv_read_count).setVisibility(View.GONE);
        helper.getView(R.id.tv_subTitle).setVisibility(View.GONE);
        helper.getView(R.id.tv_subDesc).setVisibility(View.GONE);
    }
}
