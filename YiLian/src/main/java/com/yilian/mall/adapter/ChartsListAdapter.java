package com.yilian.mall.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.networkingmodule.entity.ChartsEntity;

import java.util.List;

/**
 * Created by liuyuqi on 2017/8/25 0025.
 */

public class ChartsListAdapter extends BaseListAdapter<ChartsEntity.DataBean.ChartsBean> {

    public ChartsListAdapter(List<ChartsEntity.DataBean.ChartsBean> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            view=View.inflate(context, R.layout.inner_item_discount2,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.tvName.setText(datas.get(position).name);
        holder.tvValue.setText(MoneyUtil.getLeXiangBi(datas.get(position).value));
        switch (position){
            case 0:
                holder.ivMedal.setImageResource(R.mipmap.icon_2_1);
                break;
            case 1:
                holder.ivMedal.setImageResource(R.mipmap.icon_2_2);
                break;
            case 2:
                holder.ivMedal.setImageResource(R.mipmap.icon_2_3);
                break;
        }
        return view;
    }

    class ViewHolder{
        ImageView ivMedal;
        TextView tvName;
        TextView tvValue;
        public ViewHolder(View view) {
            this.ivMedal = (ImageView) view.findViewById(R.id.iv_medal);
            this.tvName= (TextView) view.findViewById(R.id.tv_title);
            this.tvValue= (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
