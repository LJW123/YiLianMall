package com.yilian.mall.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ActClockRankingEntity;

/**
 * Created by ZYH on 2017/12/14 0014.
 */

public class ActClockRankingAdapter extends BaseQuickAdapter<ActClockRankingEntity.Data,BaseViewHolder> {

    public ActClockRankingAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActClockRankingEntity.Data item) {
        int position=helper.getLayoutPosition();
        TextView textView=helper.getView(R.id.iv_level);
        helper.setText(R.id.iv_level,(position+1)+"");
        if (position==0){
            textView.setBackgroundResource(R.mipmap.icon_atc_level_01);
        }else if (position==1){
            textView.setBackgroundResource(R.mipmap.icon_atc_level_02);
        }else if (position==2){
            textView.setBackgroundResource(R.mipmap.icon_atc_level_03);

        }else {
            textView.setBackgroundResource(R.mipmap.icon_atc_level_04);
        }
        if (!TextUtils.isEmpty(item.name)){
            helper.setText(R.id.tv_name,item.name);
        }else {
            helper.setText(R.id.tv_name,"暂无昵称");
        }
        if (!TextUtils.isEmpty(item.givenIntegral)){
            double integral=Double.parseDouble(item.givenIntegral)/100;
            if (integral==0){
                helper.setText(R.id.tv_integral,"0");
            }else {
                helper.setText(R.id.tv_integral,integral+"");
            }
        }else {
            helper.setText(R.id.tv_integral,null);

        }

        com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, item.photo, helper.getView(R.id.iv_icon));
    }
}
