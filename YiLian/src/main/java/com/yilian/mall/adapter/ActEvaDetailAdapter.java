package com.yilian.mall.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ActEvaDetailEntity;

/**
 * Created by Ray_L_Pain on 2017/12/8 0008.
 */

public class ActEvaDetailAdapter extends BaseQuickAdapter<ActEvaDetailEntity.ListBean, BaseViewHolder> {

    public ActEvaDetailAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActEvaDetailEntity.ListBean item) {
        helper.addOnClickListener(R.id.tv_zan);

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirImageNoLit(mContext, item.user_photo, iv, item.reply_index);

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.replyer_username)) {
            tvName.setText("暂无昵称");
        } else {
            tvName.setText(item.replyer_username);
        }

        TextView tvZan = helper.getView(R.id.tv_zan);
        tvZan.setText(String.valueOf(item.countPraise));
        Drawable drawable;
        String isParise;
        if ("0".equals(item.is_parise)) {
            //未点赞
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_off);
            isParise = "1";
        } else {
            //已点赞
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_on);
            isParise = "2";
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvZan.setCompoundDrawables(drawable, null, null, null);

        TextView tvContent = helper.getView(R.id.tv_content);
        tvContent.setText(item.reply_content);

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(DateUtils.timeStampToStr2(Long.parseLong(item.replyer_time)));

        TextView tvEvaCount = helper.getView(R.id.tv_evaluate_count);
        tvEvaCount.setVisibility(View.GONE);

        TextView tvDesc = helper.getView(R.id.tv_desc);
        tvDesc.setVisibility(View.GONE);
    }
}
