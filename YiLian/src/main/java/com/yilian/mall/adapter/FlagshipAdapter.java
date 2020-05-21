package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;


/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 */

public class FlagshipAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<JPGoodsEntity> list;
    private String url;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public FlagshipAdapter(Context context, ArrayList<JPGoodsEntity> list) {
        this.context = context;
        this.list = list;
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        if (list.size() == 0 || list == null) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_jp_good, null);
            viewHolder = new ViewHolder();
            viewHolder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods1);
            viewHolder.iv_sold_out = (ImageView) convertView.findViewById(R.id.iv_sold_out);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            viewHolder.tvReal = (TextView) convertView.findViewById(R.id.tv_cost_price);
            viewHolder.tvMark = (TextView) convertView.findViewById(R.id.tv_market_price);
            viewHolder.tvDiscount = (TextView) convertView.findViewById(R.id.tv_discount);
            viewHolder.tvSoldNumber = (TextView) convertView.findViewById(R.id.tv_sold_number);
            viewHolder.tvTag = (TextView) convertView.findViewById(R.id.tv_tag);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JPGoodsEntity jpGoodsEntity = list.get(position);
        GlideUtil.showImageNoSuffix(context, jpGoodsEntity.JPImageUrl, viewHolder.ivGoods);
        viewHolder.tvName.setText(jpGoodsEntity.JPGoodsName);
        viewHolder.tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(jpGoodsEntity.JPGoodsCost)));
        viewHolder.tvMark.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.JPGoodsPrice)));
        viewHolder.tvMark.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        viewHolder.tvDiscount.setText(context.getString(R.string.back_score) + MoneyUtil.getLeXiangBi(jpGoodsEntity.returnIntegral));
        viewHolder.tvSoldNumber.setText("已售:" + jpGoodsEntity.JPSellCount);
        boolean flag = jpGoodsEntity.JPTagsName == null || "".equals(jpGoodsEntity.JPTagsName);
        if (flag) {
            viewHolder.tvTag.setVisibility(View.GONE);
        } else {
            viewHolder.tvTag.setText(jpGoodsEntity.JPTagsName);
        }

        if ("0".equals(jpGoodsEntity.hasGoods)) {
            viewHolder.iv_sold_out.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_sold_out.setVisibility(View.GONE);
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
        TextView tvSoldNumber;
        TextView tvPrice;
    }
}
