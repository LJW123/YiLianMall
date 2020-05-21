package com.yilian.mall.ctrip.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;

import java.util.regex.Pattern;

/**
 * 携程 首页 猜您喜欢列表 适配器
 *
 * @author Created by Zg on 2018/8/17.
 */

public class CtripHomeListAdapter extends BaseQuickAdapter<CtripHotelListEntity.DataBean, BaseViewHolder> {
    /**
     * 1定位出的数据 0没有定位
     */
    private int location = 0;

    public CtripHomeListAdapter() {
        super(R.layout.ctrip_item_home);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripHotelListEntity.DataBean item) {
        if (TextUtils.isEmpty(item.image)) {
            helper.setImageResource(R.id.iv_img, R.mipmap.default_jp);
        } else {
            GlideUtil.showImage(mContext, item.image, helper.getView(R.id.iv_img));
        }
        helper.setText(R.id.tv_name, item.HotelName);
        helper.setText(R.id.tv_level, item.StarRatingDec);
        String score;
        if (!TextUtils.isEmpty(item.CtripUserRating)) {
            score = item.CtripUserRating + "<font><small>分</small></font>  " + item.CtripUserRatingsDec;
        } else {
            score = item.CtripUserRatingsDec;
        }
        helper.setText(R.id.tv_score, Html.fromHtml(score));
        String distance = "";
        if (location == 1) {
            if (!TextUtils.isEmpty(item.juli)) {
                distance = "距您直线" + String.format("%.2f", Double.valueOf(item.juli)) + "公里";
            }
        } else {
            if (!TextUtils.isEmpty(item.Distance)) {
                distance = "距市中心" + String.format("%.2f", Double.valueOf(item.Distance)) + "公里";
            }
        }
        if (!TextUtils.isEmpty(item.AdjacentIntersection)) {
            distance += "·" + item.AdjacentIntersection;
        }
        helper.setText(R.id.tv_distance, distance);

        String price = "<font><small><small>¥</small></small></font>  " + item.MinPrice + "<font color=\"#999999\"><small><small>起</small></small></font>";
        helper.setText(R.id.tv_price, Html.fromHtml(price));
        //处理标签
        helper.getView(R.id.tv_tag_1).setVisibility(View.GONE);
        helper.getView(R.id.tv_tag_2).setVisibility(View.GONE);
        Pattern p = Pattern.compile(",");
        String[] s = p.split(item.ThemesName);
        if (s != null && s.length > 0) {
            helper.setText(R.id.tv_tag_1, s[0]);
            helper.getView(R.id.tv_tag_1).setVisibility(View.VISIBLE);
            if (s.length > 1) {
                helper.setText(R.id.tv_tag_2, s[1]);
                helper.getView(R.id.tv_tag_2).setVisibility(View.VISIBLE);
            }
        }
        helper.setText(R.id.tv_has_rate,String.format("预计%s",item.returnBean));
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
