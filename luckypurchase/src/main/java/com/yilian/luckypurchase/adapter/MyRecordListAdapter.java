package com.yilian.luckypurchase.adapter;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.SnatchAwardListEntity;

import java.util.List;

/**
 * @author Created by LYQ on 2017/10/28.
 */

public class MyRecordListAdapter extends BaseMultiItemQuickAdapter<SnatchAwardListEntity.SnatchInfoBean, BaseViewHolder> {
    public static final int UNDERWAY = 1;
    public static final int ANNOUNCE = 2;
    public static final int DOWN_LINE = 3;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyRecordListAdapter(List<SnatchAwardListEntity.SnatchInfoBean> data) {
        super(data);
        addItemType(UNDERWAY, R.layout.lucky_item_record_underway);
        addItemType(ANNOUNCE, R.layout.lucky_item_record_announced);
        addItemType(DOWN_LINE, R.layout.lucky_item_record_down_line);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnatchAwardListEntity.SnatchInfoBean item) {
        switch (helper.getItemViewType()) {
            case DOWN_LINE:
//                已下架
                helper.addOnClickListener(R.id.ll_content);
                helper.addOnClickListener(R.id.tv_look_detail);
                helper.setText(R.id.tv_goods_name, item.snatchName);
                helper.setText(R.id.tv_date, "期号: " + item.snatchIssue);
                Spanned spanned0 = Html.fromHtml("<font color='#999999'>我已参与: </font><font color='#fe5062'>" + item.mycount + "</font><font color='#999999'>人次</font>");
                helper.setText(R.id.tv_involved_count, spanned0);
                GlideUtil.showImageWithSuffix(mContext, item.goodsUrl, (ImageView) helper.getView(R.id.iv_icon));
                break;
            case UNDERWAY:
                //进行中
                helper.addOnClickListener(R.id.ll_content);
                helper.addOnClickListener(R.id.tv_add);
                helper.addOnClickListener(R.id.tv_look_detail);
                helper.setText(R.id.tv_goods_name, item.snatchName);
                helper.setText(R.id.tv_date, "期号: " + item.snatchIssue);
                Spanned spanned = Html.fromHtml("<font color='#999999'>我已参与: </font><font color='#fe5062'>" + item.mycount + "</font><font color='#999999'>人次</font>");
                helper.setText(R.id.tv_involved_count, spanned);
                helper.setText(R.id.tv_total_count, "总需人次:  " + item.totalCount);
                helper.setText(R.id.tv_residue_count, "剩余人次:  " + (Integer.parseInt(item.totalCount) - Integer.parseInt(item.joinCount)));
                ProgressBar progressBar = helper.getView(R.id.progress);
                progressBar.setMax(Integer.parseInt(item.totalCount));
                progressBar.setProgress(Integer.parseInt(item.joinCount));
                ImageView ivIcon = helper.getView(R.id.iv_icon);
                GlideUtil.showImageWithSuffix(mContext, item.goodsUrl, ivIcon);
                break;
            case ANNOUNCE:
                //已揭晓
                helper.addOnClickListener(R.id.ll_content);
                helper.addOnClickListener(R.id.tv_add);
                helper.addOnClickListener(R.id.tv_look_detail);
                helper.setText(R.id.tv_goods_name, item.snatchName);
                helper.setText(R.id.tv_date, "期号: " + item.snatchIssue);
                Spanned spanned1 = Html.fromHtml("<font color='#999999'>我已参与: </font><font color='#fe5062'>" + item.mycount + "</font><font color='#999999'>人次</font>");
                helper.setText(R.id.tv_involved_count, spanned1);
                TextView userName = helper.getView(R.id.tv_name);
                if (TextUtils.isEmpty(item.userName)) {
                    userName.setText(R.string.lucky_user_no_name);
                } else {
                    userName.setText(item.userName);
                }
                helper.setText(R.id.tv_count, item.awardCount + "人次");
                ImageView ivUserIcon = helper.getView(R.id.iv_user_icon);
                GlideUtil.showCirImage(mContext, item.photo, ivUserIcon);
                ImageView ivGoodIcon = helper.getView(R.id.iv_icon);
                GlideUtil.showImageWithSuffix(mContext, item.goodsUrl , ivGoodIcon);
                break;
            default:
                break;
        }
    }
}
