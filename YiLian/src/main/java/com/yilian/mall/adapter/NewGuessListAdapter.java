package com.yilian.mall.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ui.NewGuessDetailActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.widgets.CircleView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.GuessListEntity;
import com.yilianmall.merchant.utils.DateUtils;

/**
 * Created by Ray_L_Pain on 2017/10/13 0013.
 * 大家猜列表的adapter
 */

public class NewGuessListAdapter extends BaseQuickAdapter<GuessListEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {
    public String category;

    public NewGuessListAdapter(@LayoutRes int layoutResId, String category) {
        super(layoutResId);
        this.category = category;
    }

    @Override
    protected void convert(BaseViewHolder helper, GuessListEntity.ListBean item) {

        ImageView ivPic = helper.getView(R.id.iv_pic);
        GlideUtil.showImageNoSuffix(mContext, item.goods_icon, ivPic);

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.goods_name);

        ImageView ivState = helper.getView(R.id.iv_state);
        TextView tvState = helper.getView(R.id.tv_status);
        TextView tvTime = helper.getView(R.id.tv_time);

        LinearLayout goingLayout = helper.getView(R.id.layout_going);
        ProgressBar progressBar = helper.getView(R.id.progressBar);
        TextView tvGoingJoinNum = helper.getView(R.id.tv_going_join_num);
        TextView tvGoingAllNum = helper.getView(R.id.tv_going_all_num);
        TextView tvGoingResidueNum = helper.getView(R.id.tv_going_residue_num);

        LinearLayout finishLayout = helper.getView(R.id.layout_finish);
        CircleView ivFinish = helper.getView(R.id.iv_finish);
        ImageView ivCrown = helper.getView(R.id.iv_crown);
        TextView tvFinishName = helper.getView(R.id.tv_finish_name);
        TextView tvFinishCount = helper.getView(R.id.tv_finish_count);
        TextView tvFinishWinNum = helper.getView(R.id.tv_finish_win_num);

        int allNum = Integer.parseInt(item.total_should);
        switch (item.status) {
            case "1":
                ivState.setBackgroundResource(R.mipmap.icon_guess_state_going);
                tvState.setText("进行中");
                tvTime.setText(DateUtils.formatDate2(Long.parseLong(item.start_at)));
                goingLayout.setVisibility(View.VISIBLE);
                tvGoingJoinNum.setText(item.participants);
                tvGoingAllNum.setText(item.total_should);
                int shouldNum = Integer.parseInt(item.participants);
                tvGoingResidueNum.setText(String.valueOf(allNum - shouldNum));
                progressBar.setProgress(shouldNum);
                progressBar.setMax(allNum);
                finishLayout.setVisibility(View.GONE);
                break;
            case "2":
                ivState.setBackgroundResource(R.mipmap.icon_guess_state_finish);
                tvState.setText("已开奖");
                tvTime.setText(DateUtils.formatDate2(Long.parseLong(item.lottery_at)));
                goingLayout.setVisibility(View.GONE);
                finishLayout.setVisibility(View.VISIBLE);
                GlideUtil.showCirImage(mContext, item.user_photo, ivFinish);
                tvFinishName.setText(item.user_name);
                tvFinishCount.setText(item.total_should);
                tvFinishWinNum.setText("中奖号码：" + item.prize_number);
                break;
        }
        ivState.setVisibility(View.VISIBLE);

        LinearLayout itemLayout = helper.getView(R.id.layout_item);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (item.status) {
                    case "1": //进行中
                        if ("1".equals(category)) {
                            intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra(Constants.WEBVIEW_ICON_EXPLAIN, "VISIBLE");
                            intent.putExtra("url", Constants.PARADISE_GUESS + "?activity_id=" + item.id);
                        } else if ("3".equals(category)) {
                            intent = new Intent(mContext, NewGuessDetailActivity.class);
                            intent.putExtra("id", item.id);
                            intent.putExtra("is_participate", item.status);
                        }

                        switch (category) {
                            case "1":
                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_GOING_ONE, true, mContext);
                                break;
                            case "2":
                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_GOING_TWO, true, mContext);
                                break;
                            case "3":
                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_GOING_THREE, true, mContext);
                                break;
                        }
                        break;
                    case "2": //已完成
                        intent = new Intent(mContext, NewGuessDetailActivity.class);
                        intent.putExtra("id", item.id);
                        intent.putExtra("is_participate", item.status);
                        break;
                }
                mContext.startActivity(intent);
            }
        });

    }
}
