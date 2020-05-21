package com.yilian.mall.ctrip.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseInfoAdapter;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/8/30 11:06
 * 位置距离左侧列表
 */

public class AdapterSearchAddressDistanceLeft extends BaseQuickAdapter<SearchFilterBean.DistSortBean,BaseViewHolder> {

    public AdapterSearchAddressDistanceLeft() {
        super(R.layout.item_filtrate_distance_left);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchFilterBean.DistSortBean distSortBean) {
        TextView tv_address_distance =  helper.getView(R.id.tv_address_distance);
        RelativeLayout al_address_distance = helper.getView(R.id.al_address_distance);
        tv_address_distance.setCompoundDrawables(null,null,null,null);
        if(distSortBean.isCheck() == true){
            al_address_distance.setBackgroundColor(Color.parseColor("#ffffff"));
            tv_address_distance.setTextColor(Color.parseColor("#4289FF"));
        }else{
            al_address_distance.setBackgroundColor(Color.parseColor("#F5F5FA"));
            tv_address_distance.setTextColor(Color.parseColor("#FF666666"));
        }
        if(distSortBean.isCircleShow() == true){
            Drawable drawable= mContext.getResources().getDrawable(R.mipmap.iv_circle);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_address_distance.setCompoundDrawables(drawable,null,null,null);
        }else{
            tv_address_distance.setCompoundDrawables(null,null,null,null);
        }
        tv_address_distance.setText(distSortBean.getTitle());
    }
}
