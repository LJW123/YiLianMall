package com.yilian.mall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.PayTypeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.yilian.networkingmodule.entity.PayTypeEntity.PAY_TYPE_ISUSE;

/**
 * @author xiaofo on 2018/7/8.
 */

public class PayTypeSelectorAdapter extends android.widget.BaseAdapter {
    private final ArrayList<PayTypeEntity.PayData> list;
    private final Context context;

    private Map<Integer, Boolean> selectedMap;//保存checkbox是否被选中的状态


    public PayTypeSelectorAdapter(Context context, ArrayList<PayTypeEntity.PayData> list) {
        this.context = context;
        this.list = list;
        selectedMap = new HashMap<Integer, Boolean>();
        initData();
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PayTypeSelectorAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_pay_fragment_adapter, null);
            holder = new PayTypeSelectorAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PayTypeSelectorAdapter.ViewHolder) convertView.getTag();
        }
        PayTypeEntity.PayData dataBean = list.get(position);
        GlideUtil.showImageNoSuffix(context, dataBean.icon, holder.mIvIcon);
        holder.mTvClassName.setText(dataBean.className);
        holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
        if (PAY_TYPE_ISUSE.equals(dataBean.isuse)) {
            holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.bac_color));
        }
        if (dataBean.isChecked) {
            holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
        } else {
            holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
        }
//            if (selectedPosition == -1) {
//                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
//            } else if (selectedPosition == 0) {
//                if (position == 0) {
//                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
//                } else {
//                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
//                }
//            } else {
//                if (selectedPosition == position) {
//                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
//                } else {
//                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
//                }
//            }
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
