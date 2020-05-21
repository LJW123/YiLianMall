package com.yilian.mall.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.AMapDistanceUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.FavoriteEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/8/20 0020.
 */

public class FavoriteComboRecycleAdapter extends BaseQuickAdapter<FavoriteEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {
    public FavoriteComboRecycleAdapter(int item_favorite_combo, ArrayList<FavoriteEntity.ListBean> comboList) {
        super(item_favorite_combo, comboList);
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
            cbCheck .setVisibility(View.VISIBLE);
            if (item.isCheck){
                cbCheck.setChecked(true);
            }else {
                cbCheck.setChecked(false);
            }
        } else {
            cbCheck.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_cost_price, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(item.collectPrice)));
        helper.setText(R.id.tv_cash_quan, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(item.returnIntegral)));
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");

        //给距离赋值
        if (!TextUtils.isEmpty(item.latitude)&&!TextUtils.isEmpty(item.longitude)){
            if (item.latitude instanceof String&& item.longitude instanceof  String){
                float distance = AMapDistanceUtils.getSingleDistance2(item.latitude, item.longitude);
                if (TextUtils.isEmpty(String.valueOf(distance))) {
                    helper.setText(R.id.tv_distance, "及算距离失败");
                } else {
                    if (distance > 1000) {
                        helper.setText(R.id.tv_distance, decimalFormat.format(distance / 1000) + "km");
                    } else {
                        helper.setText(R.id.tv_distance, (int)distance + "m");
                    }
                }
            }
        }
    }
}
