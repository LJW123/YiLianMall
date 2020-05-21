package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivitySubmitGoods;
import com.yilian.mylibrary.entity.UserAddressLists;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/11/4 0004
 * 商品详情-地址选择弹窗-地址适配器
 */

public class LocationAdapter extends BaseAdapter {
    public Context mContext;
    public List<UserAddressLists> list;
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    public String goodsId, goodsName, supplierName, goodsPic, goodsIntegral, goodsSku, goodsNorms, actOrderId;

    public LocationAdapter(Context mContext, List<UserAddressLists> list, String goodsId, String goodsName, String supplierName, String goodsPic, String goodsIntegral, String goodsSku, String goodsNorms, String actOrderId) {
        this.mContext = mContext;
        this.list = list;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.supplierName = supplierName;
        this.goodsPic = goodsPic;
        this.goodsIntegral = goodsIntegral;
        this.goodsSku = goodsSku;
        this.goodsNorms = goodsNorms;
        this.actOrderId = actOrderId;
    }

    @Override
    public int getCount() {
        if (null == list || list.size() == 0) {
            return 0;
        }
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
        LocationViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_good_location, null);
            holder = new LocationViewHolder();

            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.item = (LinearLayout) convertView.findViewById(R.id.item_layout);
            convertView.setTag(holder);
        } else {
            holder = (LocationViewHolder) convertView.getTag();
        }


        UserAddressLists entity = list.get(position);

        RadioButton btn = (RadioButton) convertView.findViewById(R.id.rb);
        holder.rb = btn;
        holder.rb.setText(entity.fullAddress + entity.address);
        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), btn.isChecked());
                notifyDataSetChanged();

                Intent intent = new Intent(mContext, JPCommitOrderActivity.class);
                ActivitySubmitGoods goods = new ActivitySubmitGoods();
                goods.userName = entity.contacts;
                goods.userPhone = entity.phone;
                goods.addressId = entity.address_id;
                goods.location = entity.province_name + entity.city_name + entity.county_name + entity.fullAddress + entity.address;
                goods.integral = goodsIntegral;
                goods.sku = goodsSku;
                goods.norms = goodsNorms;
                goods.goodsId = goodsId;
                goods.goodsPic = goodsPic;
                goods.goodsName = goodsName;
                goods.supplierName = supplierName;
                goods.orderId = actOrderId;

                intent.putExtra("OrderType", "ActivityDetail");
                intent.putExtra("goods", goods);
                mContext.startActivity(intent);
                Logger.i("2017年11月14日 09:14:31-" + goods.toString());
            }
        });

        boolean res = false;
        if (states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else {
            res = true;
        }
        holder.rb.setChecked(res);

        if (res == true) {
            holder.iv.setVisibility(View.VISIBLE);
        } else {
            holder.iv.setVisibility(View.INVISIBLE);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    class LocationViewHolder {
        LinearLayout item;
        RadioButton rb;
        ImageView iv;
    }
}
