package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.ActRewardRecorderEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by ${zhaiyaohua} on 2017/12/28 0028.
 * @author zhaiyaohua
 */

public class ActRewardRecorderAdapter extends BaseQuickAdapter<ActRewardRecorderEntity.Data, com.chad.library.adapter.base.BaseViewHolder> {
    private String type="0";

    public ActRewardRecorderAdapter(@LayoutRes int layoutResId, String type) {
        super(layoutResId);
        this.type=type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActRewardRecorderEntity.Data item) {
        TextView tvNickName=helper.getView(R.id.tvNickName);
        ImageView ivPhoto=helper.getView(R.id.ivPhoto);
        GlideUtil.showCirHeadNoSuffix(mContext,item.photo,ivPhoto);
        if (!TextUtils.isEmpty(item.name)){
            tvNickName.setText(item.name);
        }else {
            tvNickName.setText("暂无昵称");
        }
        double integral=item.awardIntegral/100;
        DecimalFormat decimalFormat=new DecimalFormat("######0.00");
        String numIntegral=decimalFormat.format(integral);
        helper.setText(R.id.tvIntegral,numIntegral);
        if (type.equals("1")){
            helper.getView(R.id.tvStatus).setVisibility(View.VISIBLE);
            helper.getView(R.id.btnReward).setVisibility(View.GONE);
            switch (item.status){
                case 0:
                    //待打赏
                    helper.setText(R.id.tvStatus,"待打赏");
                    break;
                case 1:
                    //已打赏
                    helper.setText(R.id.tvStatus,"赏金已到账");
                    break;
                case 2:
                    //拒绝
                    helper.setText(R.id.tvStatus,"拒绝打赏");
                    break;

            }
        }else {
            TextView btnReward= helper.getView(R.id.btnReward);
            helper.getView(R.id.tvStatus).setVisibility(View.GONE);
            btnReward.setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.btnReward);
            switch (item.status){
                case 0:
                    btnReward.setBackgroundResource(R.drawable.bg_btn_reward);
                    btnReward.setTextColor(mContext.getResources().getColor(R.color.white));
                    btnReward.setText("打赏");
                    break;
                case 1:
                    btnReward.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    btnReward.setTextColor(mContext.getResources().getColor(R.color.color_999));
                    btnReward.setText("已打赏");
                    break;
                default:
                    btnReward.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    btnReward.setTextColor(mContext.getResources().getColor(R.color.color_999));
                    btnReward.setText("已拒绝");
                    break;
            }
        }
        helper.setText(R.id.tvTime, TimeUtils.millis2String(item.createAt*1000L,new SimpleDateFormat("MM-dd HH:mm:ss")));
    }
}
