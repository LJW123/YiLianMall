package com.yilian.mall.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.ActCommentRankEntity;

import java.text.SimpleDateFormat;

/**
 * 大家对他的评价
 * Created by ZYH on 2017/12/15 0015.
 */

public class ActCommentAdapter extends BaseQuickAdapter <ActCommentRankEntity.Comment,BaseViewHolder>{
    public ActCommentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActCommentRankEntity.Comment item) {
        TextView name=helper.getView(R.id.tv_name);
        if (!TextUtils.isEmpty(item.name)){
            name.setText(formatName(item.name));
        }else {
            name.setText("暂无昵称");
        }

        com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, item.photo, helper.getView(R.id.iv_photo));
        helper.setText(R.id.tv_comment,item.commentContent);
        helper.setText(R.id.tv_praise_num,item.praiseNum+"");
        ImageView ivZan=helper.getView(R.id.iv_zan);
        if (item.isPraise==1){
            ivZan.setImageResource(R.mipmap.act_zan_on);
        }else {
            ivZan.setImageResource(R.mipmap.act_zan_off);
        }
        if (!TextUtils.isEmpty(item.createAt)){
            helper.setText(R.id.tv_time, TimeUtils.millis2String(Long.parseLong(item.createAt)*1000L,new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss")));
        }else {
            helper.setText(R.id.tv_time, null);
        }
        helper.addOnClickListener(R.id.iv_zan);
    }
    private String formatName(String name){
         name=name.subSequence(0,1)+"**";
        return name;
    }
}
