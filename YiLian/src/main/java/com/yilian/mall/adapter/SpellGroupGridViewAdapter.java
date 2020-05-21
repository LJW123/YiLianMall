package com.yilian.mall.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/5/16 0016.
 * 每日推荐
 */
public class SpellGroupGridViewAdapter extends BaseListAdapter<JPGoodsEntity> {
    public SpellGroupGridViewAdapter(ArrayList<JPGoodsEntity> goodsList) {
        super(goodsList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SpellGroupGridViewAdapter.ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_jp_good, null);
            holder = new SpellGroupGridViewAdapter.ViewHolder();
            holder.ivGoods = (ImageView) view.findViewById(R.id.iv_goods1);
            holder.iv_sold_out = (ImageView) view.findViewById(R.id.iv_sold_out);
            holder.tvName = (TextView) view.findViewById(R.id.tv_goods_name);
            holder.tvReal = (TextView) view.findViewById(R.id.tv_cost_price);
            holder.tvMark = (TextView) view.findViewById(R.id.tv_market_price);
            holder.tvDiscount = (TextView) view.findViewById(R.id.tv_discount);
            holder.tvTag = (TextView) view.findViewById(R.id.tv_tag);
            view.setTag(holder);
        } else {
            holder = (SpellGroupGridViewAdapter.ViewHolder) view.getTag();
        }
        String url = null;
        String urlFromNet = datas.get(position).JPImageUrl;
        if (!TextUtils.isEmpty(urlFromNet)) {
            if (urlFromNet.contains("http://") || urlFromNet.contains("https://")) {
                url = urlFromNet + Constants.ImageSuffix;
            } else {
                url = Constants.ImageUrl + urlFromNet + Constants.ImageSuffix;
            }
            if (url.equals(holder.ivGoods.getTag())) {

            } else {
                imageLoader.displayImage(url, holder.ivGoods, options);
                holder.ivGoods.setTag(url);
            }
        }

        Logger.i("gridView   imageUrl" + url);
        holder.tvName.setText(datas.get(position).JPGoodsName);
        holder.tvName.setTextColor(context.getResources().getColor(R.color.color_h1));
        holder.tvMark.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(datas.get(position).JPGoodsPrice)));
        holder.tvReal.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(datas.get(position).JPGoodsCost)));
        holder.tvMark.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        int num = Integer.parseInt(datas.get(position).JPGoodsPrice) - Integer.parseInt(datas.get(position).JPGoodsCost);
        holder.tvDiscount.setText("还需要" + MoneyUtil.getXianJinQuan(String.valueOf(num)) + "券");

        boolean flag = datas.get(position).JPTagsName == null || "".equals(datas.get(position).JPTagsName);
        if (flag) {
            holder.tvTag.setVisibility(View.GONE);
        } else {
            holder.tvTag.setText(datas.get(position).JPTagsName);
        }

        if ("0".equals(datas.get(position).hasGoods)) {
            holder.iv_sold_out.setVisibility(View.VISIBLE);
        } else {
            holder.iv_sold_out.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {
        ImageView ivGoods;
        ImageView iv_sold_out;
        TextView tvName;
        TextView tvReal;
        TextView tvMark;
        TextView tvDiscount;
        TextView tvTag;
    }
}
