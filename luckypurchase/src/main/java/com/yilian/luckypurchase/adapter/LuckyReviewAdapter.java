package com.yilian.luckypurchase.adapter;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.LuckyRecordListEntity;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/26 0026
 * 往期回顾适配器
 */

public class LuckyReviewAdapter extends BaseQuickAdapter<LuckyRecordListEntity.SnatchInfo, BaseViewHolder> {

    public LuckyReviewAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LuckyRecordListEntity.SnatchInfo item) {

        TextView tvIssue = helper.getView(R.id.tv_issue);
        tvIssue.setText("期号 " + item.snatch_issue);

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText("（揭晓时间" + DateUtils.luckyTime(item.snatch_announce_time) + "）");

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirImage(mContext, item.photo, iv);

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.user_name)) {
            tvName.setText("获奖者（暂无昵称）");
        } else {
            tvName.setText("获奖者（" + item.user_name + "）");
        }

        TextView tvPrizeNum = helper.getView(R.id.tv_prize_num);
        tvPrizeNum.setText("获奖号码" + item.win_num);

        TextView tvPeopleNum = helper.getView(R.id.tv_people_num);
        tvPeopleNum.setText(item.join_count);
    }
}
