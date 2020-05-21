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
import com.yilian.mall.entity.NoticeModel;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 */

public class NoticeNewListAdapter extends BaseQuickAdapter<NoticeModel.NewsBean, com.chad.library.adapter.base.BaseViewHolder>{
    public NoticeNewListAdapter(@LayoutRes int layoutId) {
        super(layoutId);

    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeModel.NewsBean item) {
        TextView tvStatusName = helper.getView(R.id.tv_title);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvDesc=helper.getView(R.id.tv_desc);
        tvDesc.setMaxLines(2);
        tvDesc.setLines(2);
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvStatusName.setText(item.name);
        tvStatusName.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        helper.getView(R.id.tv_subTitle).setVisibility(View.GONE);
        String todayDate = DateUtils.getTodayDate();
        String time = DateUtils.timeStampToStr(Long.parseLong(item.time));
        if (time.contains(todayDate)){
            helper.setText(R.id.tv_date, DateUtils.getTime(Long.parseLong(item.time)));
        }else {
            helper.setText(R.id.tv_date, time);
        }
        helper.setText(R.id.tv_desc,item.intro);
        helper.getView(R.id.tv_subDesc).setVisibility(View.INVISIBLE);
        helper.getView(R.id.tv_read_count).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_read_count,item.pv);
        GlideUtil.showImageWithSuffix(mContext, item.image, ivIcon);
    }
}
