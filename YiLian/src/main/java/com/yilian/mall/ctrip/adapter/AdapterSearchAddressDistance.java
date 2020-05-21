package com.yilian.mall.ctrip.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseInfoAdapter;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/8/30 11:06
 */

public class AdapterSearchAddressDistance extends BaseInfoAdapter<SearchFilterBean.SSortBean> {
    SearchFilterBean.SSortBean bean;

    public AdapterSearchAddressDistance(Context context, List<SearchFilterBean.SSortBean> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<SearchFilterBean.SSortBean> list, int resId, int position, View convertView) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, resId, null);
            vh = new ViewHolder();
            vh.initView(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        bean = list.get(position);
        vh.initData(context, bean);
        return convertView;
    }

    class ViewHolder {
        TextView tv_address_distance;
        private RelativeLayout al_address_distance;

        private void initView(View v) {
            tv_address_distance = (TextView) v.findViewById(R.id.tv_address_distance);
            al_address_distance = v.findViewById(R.id.al_address_distance);
            tv_address_distance.setCompoundDrawables(null, null, null, null);
        }

        private void initData(Context context,SearchFilterBean.SSortBean bean) {
            if(bean.isCheck() == true){
                al_address_distance.setBackgroundColor(Color.parseColor("#ffffff"));
                tv_address_distance.setTextColor(Color.parseColor("#4289FF"));
            } else {
                al_address_distance.setBackgroundColor(Color.parseColor("#F5F5FA"));
                tv_address_distance.setTextColor(Color.parseColor("#FF666666"));
            }
            if (bean.isCircleShow() == true) {
                Drawable drawable = context.getResources().getDrawable(R.mipmap.iv_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_address_distance.setCompoundDrawables(drawable, null, null, null);
            } else {
                tv_address_distance.setCompoundDrawables(null, null, null, null);
            }
            tv_address_distance.setText(bean.getTitle());
        }
    }
}
