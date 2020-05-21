package com.yilian.luckypurchase.adapter;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.SnatchAwardListEntity;


/**
 * @authorCreated by LYQ on 2017/10/30.
 * 中奖记录的adapter
 */

public class SnatchAwardAdapter extends BaseQuickAdapter<SnatchAwardListEntity.SnatchInfoBean, BaseViewHolder> {
    public SnatchAwardAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnatchAwardListEntity.SnatchInfoBean item) {

        helper.addOnClickListener(R.id.tv_share);
        helper.addOnClickListener(R.id.tv_show_line);
        helper.addOnClickListener(R.id.ll_content);

        ImageView goodsIcon = helper.getView(R.id.iv_icon);
        GlideUtil.showImageWithSuffix(mContext, item.goodsUrl, goodsIcon);
        helper.setText(R.id.tv_name, item.snatchName);
        helper.setText(R.id.tv_date, "期号: " + item.snatchIssue);
        helper.setText(R.id.tv_enter_count, "参与人次: " + item.joinCount + "人次");
        Spanned luckyNumber = Html.fromHtml("<font color='#999999'>幸运号码: </color></font><font color=\"#fe5062\">" + item.winNum + "</font>");
        helper.setText(R.id.tv_luck_number, luckyNumber);
        if (!TextUtils.isEmpty(item.snatchAnnounceTime)) {
            helper.setText(R.id.tv_announce_number, "揭晓时间: " + DateUtils.timeStampToStr(Long.parseLong(item.snatchAnnounceTime)));
        } else {
            helper.setText(R.id.tv_announce_number, "揭晓时间:  暂无");
        }
        TextView tv = helper.getView(R.id.tv_show_line);

        /**
         * 商品状态 0标识未开奖 1表示未设置收货地址 2待备货 3已发货 4已完成 5待发货
         */
        String status = "";
        switch (item.awardStatus) {
            case "0":
                status = "未开奖";
                helper.getView(R.id.tv_share).setVisibility(View.GONE);
                helper.getView(R.id.tv_show_line).setVisibility(View.GONE);
                break;
            case "1":
                status = "未设置收货地址";
                //未设置收货地址
                helper.getView(R.id.tv_share).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
                tv.setBackgroundResource(R.drawable.library_module_bg_style_red_empty_15);
                helper.setText(R.id.tv_show_line, "收货地址");
                break;
            case "2":
                status = "等待备货";
                //未设置收货地址
                helper.getView(R.id.tv_share).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
                tv.setBackgroundResource(R.drawable.library_module_bg_style_red_empty_15);
                helper.setText(R.id.tv_show_line, "收货地址");
                break;
            case "3":
                status = "已发货";
                helper.getView(R.id.tv_share).setVisibility(View.GONE);
                tv.setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
                tv.setBackgroundResource(R.drawable.library_module_bg_style_red_empty_15);
                helper.getView(R.id.tv_show_line).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_show_line, "确认收货");
                break;
            case "4":
                status = "已完成";
                helper.getView(R.id.tv_share).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_share, "分享");
                tv.setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
                tv.setBackgroundResource(R.drawable.library_module_bg_style_red_empty_15);
                helper.getView(R.id.tv_show_line).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_show_line, "晒单");
                break;
            case "5":
                status = "等待发货";
                helper.getView(R.id.tv_share).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("待收货");
                tv.setTextColor(mContext.getResources().getColor(R.color.library_module_gray));
                tv.setBackgroundResource(R.drawable.library_module_bg_style_grey_empty_15);
                break;
            default:
                break;
        }
        helper.setText(R.id.tv_goods_status, "商品状态: " + status);
    }
}
