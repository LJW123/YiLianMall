package com.yilian.mall.ctrip.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseInfoAdapter;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/8/30 11:06
 */

public class AdapterFiltrateRight extends BaseInfoAdapter<SearchFilterBean.SSortBean.SortsBeanXX> {
    SearchFilterBean.SSortBean.SortsBeanXX sortsBeanXX;

    public AdapterFiltrateRight(Context context, List <SearchFilterBean.SSortBean.SortsBeanXX>list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<SearchFilterBean.SSortBean.SortsBeanXX> list, int resId, int position, View convertView) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, resId, null);
            vh = new ViewHolder();
            vh.initView(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        sortsBeanXX= list.get(position);
        vh.initData(context, sortsBeanXX);
        return convertView;
    }

    class ViewHolder {
       TextView tv_filtrate_right_distance;
       ImageView iv_check;
        private void initView(View v) {
            tv_filtrate_right_distance = (TextView) v.findViewById(R.id.tv_filtrate_right_distance);
            iv_check  = v.findViewById(R.id.iv_check);
        }
        private void initData(Context context, SearchFilterBean.SSortBean.SortsBeanXX sortsBeanXX ) {
            if(sortsBeanXX.isCheck() == true){
                iv_check.setBackgroundResource(R.mipmap.iv_cb_checked);
            }else{
                iv_check.setBackgroundResource(R.mipmap.iv_cb_not_checked);
            }
            tv_filtrate_right_distance.setText(sortsBeanXX.getTitle());
        }
    }
}
