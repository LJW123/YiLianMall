package com.yilian.mall.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ui.ActEvaDetailActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.GALRecordEntity;

/**
 * Created by Ray_L_Pain on 2017/11/17 0017.
 */

public class ActRecordAdapter extends BaseQuickAdapter<GALRecordEntity.ListBean, BaseViewHolder> {
    public String actType;

    public ActRecordAdapter(@LayoutRes int layoutResId, String actType) {
        super(layoutResId);
        this.actType = actType;
    }

    @Override
    protected void convert(BaseViewHolder helper, GALRecordEntity.ListBean item) {
        helper.addOnClickListener(R.id.tv_zan);

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirImageNoLit(mContext, item.userPhoto, iv, item.commentIndex);

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.userName)) {
            tvName.setText("暂无昵称");
        } else {
            tvName.setText(item.userName);
        }

        TextView tvZan = helper.getView(R.id.tv_zan);
        tvZan.setText(String.valueOf(item.countPraise));
        Drawable drawable;
        if ("0".equals(item.isParise)) {
            //未点赞
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_off);
        } else {
            //已点赞
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_on);
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvZan.setCompoundDrawables(drawable, null, null, null);

        TextView tvContent = helper.getView(R.id.tv_content);
        switch (actType) {
            case "1":
                tvContent.setText(Html.fromHtml("<font color=\"#FFC12B\">" + item.joinCount + "次" + "</font><font color=\"#666666\">" + "碰撞获得好运商品¥" + MoneyUtil.getLeXiangBiNoZero(item.goodsPrice) + "</font>"));
                break;
            case "2":
                tvContent.setText(Html.fromHtml("<font color=\"#FFC12B\">" + item.joinCount + "次" + "</font><font color=\"#666666\">" + "喊价，将价值¥" + MoneyUtil.getLeXiangBiNoZero(item.goodsPrice) + "</font>的商品免费收入囊中"));
                break;
            default:
                break;
        }

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(DateUtils.timeStampToStr2(Long.parseLong(item.createdAt)));

        TextView tvEvaCount = helper.getView(R.id.tv_evaluate_count);
        tvEvaCount.setText(item.countReply);
        tvEvaCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActEvaDetailActivity.class);
                intent.putExtra("comment_index", item.commentIndex);
                mContext.startActivity(intent);
            }
        });

        TextView tvDesc = helper.getView(R.id.tv_desc);
        tvDesc.setVisibility(View.INVISIBLE);
    }
}
