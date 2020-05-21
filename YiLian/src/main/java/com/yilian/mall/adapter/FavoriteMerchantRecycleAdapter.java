package com.yilian.mall.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.AMapDistanceUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.FavoriteEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by liuyuqi on 2017/8/20 0020.
 */

public class FavoriteMerchantRecycleAdapter extends BaseQuickAdapter<FavoriteEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {

    public FavoriteMerchantRecycleAdapter(int item_favorite_merchant2, ArrayList<FavoriteEntity.ListBean> merchantList) {
        super(item_favorite_merchant2, merchantList);
    }

    @Override
    protected void convert(BaseViewHolder helper, FavoriteEntity.ListBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        CheckBox cbCheck= helper.getView(R.id.cb_select);
        GlideUtil.showImageWithSuffix(mContext, item.collectIcon, ivIcon);
        helper.setText(R.id.tv_name, item.collectName);
        helper.addOnClickListener(R.id.cb_select);//绑定控件的点击事件
        if ("0".equals(item.status)) {
            helper.getView(R.id.iv_non).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_non).setVisibility(View.GONE);
        }

        if (item.isShowCheck) {
            cbCheck.setVisibility(View.VISIBLE);
            if (item.isCheck){
                cbCheck.setChecked(true);
            }else {
                cbCheck.setChecked(false);
            }
        } else {
            cbCheck.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.gradeCount)) {
            float grade = Float.parseFloat(item.gradeScore) / 10.0f;
            helper.setRating(R.id.goods_ratingbar, grade == 0 ? (float) 5.0 : grade);
            helper.setText(R.id.goods_grade, item.gradeCount + "赞");
        } else {
            helper.setRating(R.id.goods_ratingbar, 5.0f);
            helper.setText(R.id.goods_grade,  "0赞");
        }
        if (!TextUtils.isEmpty(item.merDiscount)) {
            helper.getView(R.id.tv_privilege).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_privilege, Html.fromHtml(item.merDiscount + "<font><small><small>%</small></small></font>"));
        } else {
            helper.getView(R.id.tv_privilege).setVisibility(View.GONE);
        }

        //根据经纬度获得距离差
        float distance = AMapDistanceUtils.getSingleDistance2(item.latitude, item.longitude);
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if (TextUtils.isEmpty(String.valueOf(distance))) {
            helper.setText(R.id.tv_distance, "及算距离失败");
        } else {
            if (distance > 1000) {
                helper.setText(R.id.tv_distance, decimalFormat.format(distance / 1000) + "km");
            } else {
                helper.setText(R.id.tv_distance, (int)distance + "m");
            }
        }
        helper.setText(R.id.tv_industry, item.industry);
        helper.setText(R.id.tv_location, item.address);
    }
}
