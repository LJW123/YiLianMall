package com.yilian.mall.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.MTMerchantDetailEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/8 0008.
 * 特色套餐的adpter
 */
public class SpecialComboAdapter extends BaseListAdapter<MTMerchantDetailEntity.InfoBean.PackageBean> {


    public SpecialComboAdapter(List<MTMerchantDetailEntity.InfoBean.PackageBean> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ComboHolder holder;
        if (view==null){
            view=View.inflate(context, R.layout.mt_item_merchant_body,null);
            holder=new ComboHolder();
            holder.comboIcon= (ImageView) view.findViewById(R.id.iv_merchant_icon);
            holder.comboTitle= (TextView) view.findViewById(R.id.tv_merchant_name);
            holder.comboPrice= (TextView) view.findViewById(R.id.tv_merchant_price);
            holder.sellCount= (TextView) view.findViewById(R.id.tv_sell_count);
            holder.tvIntegral = (TextView) view.findViewById(R.id.tv_integral);
            view.setTag(holder);
        }else{
            holder= (ComboHolder) view.getTag();
        }

        MTMerchantDetailEntity.InfoBean.PackageBean packageBean = datas.get(position);
        holder.comboTitle.setText(packageBean.name);
        imageLoader.displayImage(Constants.ImageUrl+packageBean.packageIcon+ Constants.ImageSuffix,holder.comboIcon);
        holder.comboPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(packageBean.price)));
        holder.tvIntegral.setVisibility(View.VISIBLE);
        holder.tvIntegral.setText("可得" + context.getResources().getString(R.string.integral) + MoneyUtil.getLeXiangBiNoZero(packageBean.returnIntegral));
        holder.sellCount.setText("已售" + packageBean.sellCount);

        return view;
    }

    class ComboHolder {
        ImageView comboIcon;
        TextView comboTitle;
        TextView comboPrice;
        TextView sellCount;
        TextView tvIntegral;
    }
}
