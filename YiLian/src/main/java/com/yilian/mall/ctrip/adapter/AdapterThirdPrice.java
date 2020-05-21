package com.yilian.mall.ctrip.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseInfoAdapter;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/8/31 11:17
 * 携程_筛选_价格
 */

public class AdapterThirdPrice extends BaseInfoAdapter<SearchFilterBean.PSortBean.SortsBeanX> {
    SearchFilterBean.PSortBean.SortsBeanX sortsBeanX;

    public AdapterThirdPrice(Context context, List list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<SearchFilterBean.PSortBean.SortsBeanX> list, int resId, int position, View convertView) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, resId, null);
            vh = new ViewHolder();
            vh.initView(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        sortsBeanX = list.get(position);
        vh.initData(context, sortsBeanX);
        return convertView;
    }

    class ViewHolder {
        TextView tv_third_price;

        private void initView(View v) {
            tv_third_price = (TextView) v.findViewById(R.id.tv_third_price);
        }

        private void initData(Context context, SearchFilterBean.PSortBean.SortsBeanX sortsBeanX) {
            if(sortsBeanX.isCheck() == true){

                tv_third_price.setBackgroundColor(Color.parseColor("#E7F3FF"));
                tv_third_price.setTextColor(ContextCompat.getColor(context,R.color.color_4289FF));
            }else{
                tv_third_price.setBackgroundColor(Color.parseColor("#F5F5FA"));
                tv_third_price.setTextColor(Color.parseColor("#333333"));
            }
            tv_third_price.setText(sortsBeanX.getTitle());
        }
    }
}
