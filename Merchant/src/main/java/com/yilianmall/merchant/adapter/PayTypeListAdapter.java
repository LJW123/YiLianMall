package com.yilianmall.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilianmall.merchant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by  on 2017/7/10 0010.
 */

public class PayTypeListAdapter extends BaseAdapter{
    private  ArrayList<PayTypeEntity.PayData> list;
    private  Context context;

    private Map<Integer, Boolean> selectedMap;//保存checkbox是否被选中的状态

    public PayTypeListAdapter(Context context, ArrayList<PayTypeEntity.PayData> list) {
        this.context = context;
        this.list = list;
        selectedMap = new HashMap<Integer, Boolean>();
        initData();//初始化默认不选中
    }
    public void initData() {

        for (int i = 0; i < list.size(); i++) {
            selectedMap.put(i, false);
        }
    }
    
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return  list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final PayTypeListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.library_module_item_pay_fragment_adapter, null);
            holder = new PayTypeListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PayTypeListAdapter.ViewHolder) convertView.getTag();
        }
        PayTypeEntity.PayData dataBean = list.get(position);
        GlideUtil.showImageNoSuffix(context,dataBean.icon,holder.mIvIcon);
        holder.mTvClassName.setText(dataBean.className);
        holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
        if ("0".equals(dataBean.isuse)) {
            holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.library_module_bac_color));
        }
        if(dataBean.isChecked){
            holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
        }else{
            holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));

        }
//        if (selectedPosition == -1) {
//            holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
//        } else if (selectedPosition == 0) {
//            if (position == 0) {
//                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
//            } else {
//                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
//            }
//        } else {
//            if (selectedPosition == position) {
//                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
//            } else {
//                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
//            }
//        }
        return convertView;
    }


    public class ViewHolder {
        public RelativeLayout mRL;
        public View rootView;
        public ImageView mIvIcon;
        public TextView mTvClassName;
        public TextView mTvClassSubTitle;
        public ImageView selectImg;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mIvIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
            this.mTvClassName = (TextView) rootView.findViewById(R.id.tv_class_name);
            this.mTvClassSubTitle = (TextView) rootView.findViewById(R.id.tv_class_sub_title);
            this.mRL = (RelativeLayout) rootView.findViewById(R.id.rl);
            this.selectImg = (ImageView) rootView.findViewById(R.id.commit_express_icon);
        }
    }
}
