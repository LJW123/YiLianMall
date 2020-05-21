package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 */

public class JPGoodsAdapter extends android.widget.BaseAdapter{
    private Context context;
    private List<JPGoodsEntity> list;
    private String url;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView tvSoldNumber;

    public JPGoodsAdapter(Context context, List<JPGoodsEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list.size() == 0 || list == null){
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
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_jp_good, null);
            holder = new ViewHolder();
            holder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods1);
            holder.iv_sold_out = (ImageView) convertView.findViewById(R.id.iv_sold_out);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tvReal = (TextView) convertView.findViewById(R.id.tv_cost_price);
            holder.tvMark = (TextView) convertView.findViewById(R.id.tv_market_price);
            holder.tvDiscount = (TextView) convertView.findViewById(R.id.tv_discount);
            holder.tvTag = (TextView) convertView.findViewById(R.id.tv_tag);
            holder.tvSoldNumber = (TextView) convertView.findViewById(R.id.tv_sold_number);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JPGoodsEntity jpGoodsEntity = list.get(position);
        String urlFromNet = jpGoodsEntity.JPImageUrl;
        GlideUtil.showImageNoSuffix(context,urlFromNet,holder.ivGoods);
        holder.tvSoldNumber.setText("已售"+jpGoodsEntity.JPSellCount);
        holder.tvName.setText(jpGoodsEntity.JPGoodsName);
        holder.tvName.setTextColor(context.getResources().getColor(R.color.color_h1));
        holder.tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(jpGoodsEntity.JPGoodsCost)));
        holder.tvMark.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.JPGoodsPrice)));
        holder.tvMark.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        int num = Integer.parseInt(jpGoodsEntity.JPGoodsPrice) - Integer.parseInt(jpGoodsEntity.JPGoodsCost);
        holder.tvDiscount.setText(context.getString(R.string.back_score) + MoneyUtil.getXianJinQuan(jpGoodsEntity.returnIntegral));

        boolean flag = jpGoodsEntity.JPTagsName == null || "".equals(jpGoodsEntity.JPTagsName);
        if(flag){
            holder.tvTag.setVisibility(View.GONE);
        } else {
            holder.tvTag.setText(jpGoodsEntity.JPTagsName);
        }

        if("0".equals(jpGoodsEntity.hasGoods)){
            holder.iv_sold_out.setVisibility(View.VISIBLE);
        } else {
            holder.iv_sold_out.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivGoods;
        ImageView iv_sold_out;
        TextView tvName;
        TextView tvReal;
        TextView tvMark;
        TextView tvDiscount;
        TextView tvTag;
        public TextView tvSoldNumber;
        public TextView tvPrice;
    }
}
