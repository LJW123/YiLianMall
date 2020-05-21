package com.yilian.luckypurchase.adapter;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.LuckySnathNewsList;

/**
 * Created by Ray_L_Pain on 2017/11/22 0022.
 */

public class LuckyNewsListAdapter extends BaseQuickAdapter<LuckySnathNewsList.DataBean, BaseViewHolder> {
    public String from;

    public LuckyNewsListAdapter(@LayoutRes int layoutResId, String from) {
        super(layoutResId);
        this.from = from;
    }

    @Override
    protected void convert(BaseViewHolder helper, LuckySnathNewsList.DataBean item) {

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirHeadNoSuffix(mContext, item.photo, iv);

        TextView tvMsg = helper.getView(R.id.tv_msg);
        TextView tvTime = helper.getView(R.id.tv_time);
        switch (from) {
            case "prize":
                tvMsg.setText(Html.fromHtml("<font color=\"#333333\">恭喜" + item.ipAddress + "</font><font color=\"#fe5062\">" + item.userName + "</font> <font color=\"#333333\">获得" + item.goodsName + "</font>"));
                tvTime.setText("获奖时间：" + DateUtils.timeStampToStr6(Long.parseLong(item.time)));
                break;
            case "send":
                tvMsg.setText(Html.fromHtml("<font color=\"#333333\">" + item.awardCity + "</font><font color=\"#fe5062\">" + item.awardLinkman + "（" + item.userName + "）</font> <font color=\"#333333\">的" + item.goodsName + "</font>已发货"));
                tvTime.setText("发货时间：" + DateUtils.timeStampToStr6(Long.parseLong(item.expressTime)));
                break;
            default:
                break;
        }


    }
}
