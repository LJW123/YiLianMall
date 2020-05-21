package com.yilian.luckypurchase.adapter;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.LuckyGoodsListEntity;

import java.util.List;

/**
 * @atuhor Created by  on 2017/10/24 0024.
 */

public class LuckyGoodsListAdapter extends BaseQuickAdapter<LuckyGoodsListEntity.ListBean, BaseViewHolder> {
    /**
     * 矫正分割线的显示
     */
    private boolean haveHeader;

    private String flag = "";

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LuckyGoodsListAdapter(List data) {
        super(data);
    }

    public LuckyGoodsListAdapter(@LayoutRes int layoutResId, boolean haveHeader) {
        super(layoutResId);
        this.haveHeader = haveHeader;
    }

    public LuckyGoodsListAdapter(@LayoutRes int layoutResId, @Nullable List<LuckyGoodsListEntity.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LuckyGoodsListEntity.ListBean item) {

        byHashrate(helper, item);

        ImageView imageView = (ImageView) helper.getView(R.id.iv_prize);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = layoutParams.width = (ScreenUtils.getScreenWidth(mContext) - DPXUnitUtil.dp2px(mContext, 4)) / 2;
        imageView.setLayoutParams(layoutParams);
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(item.snatchGoodsUrl, layoutParams.width + "", layoutParams.width + ""), imageView);
//        GlideUtil.showImageWithSuffix(mContext, item.snatchGoodsUrl, imageView);

        if (helper.getLayoutPosition() % 2 == 0) {
            if (haveHeader) {
                helper.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
        } else {
            if (haveHeader) {
                helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.view_line).setVisibility(View.GONE);
            }
        }

        helper.getView(R.id.btn_snatch).setOnClickListener(new View.OnClickListener() {

            private Intent intent;

            @Override
            public void onClick(View v) {
                if (PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, mContext, false)) {
                    intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                    intent.putExtra("activity_id", item.activityId);
                    intent.putExtra("type", "0");
                    intent.putExtra("show_window", "1");
                } else {
                    intent = new Intent();
                    intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                }
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 算力
     *
     * @param helper
     * @param item
     */
    private void byHashrate(BaseViewHolder helper, final LuckyGoodsListEntity.ListBean item) {
        //商品名
        TextView tv_prize_name = helper.getView(R.id.tv_prize_name);
        tv_prize_name.setSingleLine(true);
        tv_prize_name.setLines(1);
        tv_prize_name.setEllipsize(TextUtils.TruncateAt.END);
        tv_prize_name.setText(item.snatchName);
        //显示送算力
        LinearLayout ll_hashrate = helper.getView(R.id.ll_hashrate);
        ll_hashrate.setVisibility(View.VISIBLE);
        //送益豆
        helper.setText(R.id.tv_hashrate, "送" + MoneyUtil.getLeXiangBiNoZero(item.extraBean));
        //进度条
        final ProgressBar lucky_progressBar = helper.getView(R.id.lucky_progressBar);
        lucky_progressBar.setProgress(((int) ((item.snatchPlayCount * 1.0) / (item.snatchTotalCount * 1.0) * 100)));
        //进度条 指示
        final TextView tv_prize_progress = helper.getView(R.id.tv_prize_progress);
        tv_prize_progress.setBackgroundResource(R.mipmap.percentage);
        tv_prize_progress.setTextColor(Color.parseColor("#FF385D"));
        tv_prize_progress.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        tv_prize_progress.setText(new StringBuffer().append(lucky_progressBar.getProgress()).append("%"));
        tv_prize_progress.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams progesssParams = (ViewGroup.MarginLayoutParams) lucky_progressBar.getLayoutParams();
                progesssParams.rightMargin = DPXUnitUtil.dp2px(mContext, 22);
                progesssParams.width = (ScreenUtils.getScreenWidth(mContext) - DPXUnitUtil.dp2px(mContext, 4)) / 2 - DPXUnitUtil.dp2px(mContext, 60 + 10 + 22 + 10);
                progesssParams.height = DPXUnitUtil.dp2px(mContext, 5);
                lucky_progressBar.setLayoutParams(progesssParams);
                int marginLeft = progesssParams.leftMargin;
                int w = progesssParams.width;
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv_prize_progress.getLayoutParams();
                int pro = lucky_progressBar.getProgress();
                int tW = tv_prize_progress.getWidth();
                params.leftMargin = (int) (w * pro / 100 - tW * 0.5) + marginLeft;
                tv_prize_progress.setLayoutParams(params);
            }
        });
    }
}
