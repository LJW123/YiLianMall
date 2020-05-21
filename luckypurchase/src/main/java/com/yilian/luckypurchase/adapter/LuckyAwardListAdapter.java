package com.yilian.luckypurchase.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.LuckyAwardListEntity;

/**
 * @author Created by  on 2017/11/4 0004.
 */

public class LuckyAwardListAdapter extends BaseQuickAdapter<LuckyAwardListEntity.SnatchInfoBean, BaseViewHolder> {
    public LuckyAwardListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LuckyAwardListEntity.SnatchInfoBean item) {
        ImageView imageView = (ImageView) helper.getView(R.id.iv_prize);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = layoutParams.width = (ScreenUtils.getScreenWidth(mContext) - 4) / 2;
        imageView.setLayoutParams(layoutParams);
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(item.snatchGoodsUrl, layoutParams.width + "", layoutParams.width + ""), imageView);
        helper.setText(R.id.tv_prize_name, item.snatchName);
        ((TextView) helper.getView(R.id.include_issue).findViewById(R.id.tv_award_title)).setText("期号: ");
        ((TextView) helper.getView(R.id.include_issue).findViewById(R.id.tv_award_content)).setText(item.snatchIssue);
        ((TextView) helper.getView(R.id.include_winner_name).findViewById(R.id.tv_award_title)).setText("获得者: ");
        TextView tvWinnerName = (TextView) helper.getView(R.id.include_winner_name).findViewById(R.id.tv_award_content);
        tvWinnerName.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        tvWinnerName.setText(TextUtils.isEmpty(item.userName) ? "暂无昵称" : item.userName);
        ((TextView) helper.getView(R.id.include_times).findViewById(R.id.tv_award_title)).setText("参与人次: ");
        TextView tvJoinCount = (TextView) helper.getView(R.id.include_times).findViewById(R.id.tv_award_content);
        tvJoinCount.setText(item.joinCount);

        ((TextView) helper.getView(R.id.include_number).findViewById(R.id.tv_award_title)).setText("幸运号码: ");
        ((TextView) helper.getView(R.id.include_number).findViewById(R.id.tv_award_content)).setText(item.winNum);

        ((TextView) helper.getView(R.id.include_le_beans).findViewById(R.id.tv_award_title)).setText("获得益豆: ");
        TextView tvLeBeans = (TextView) helper.getView(R.id.include_le_beans).findViewById(R.id.tv_award_content);
        tvLeBeans.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_333));
        tvLeBeans.setText(MoneyUtil.getLeXiangBiNoZero(item.extraBean));

        ((TextView) helper.getView(R.id.include_out_of_time).findViewById(R.id.tv_award_title)).setText("揭晓时间: ");
        ((TextView) helper.getView(R.id.include_out_of_time).findViewById(R.id.tv_award_content)).setText(DateUtils.timeStampToStr5(item.snatchAnnounceTime));
        if (!(helper.getLayoutPosition() % 2 == 0)) {
            helper.getView(R.id.view_line).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
        }
    }
}
