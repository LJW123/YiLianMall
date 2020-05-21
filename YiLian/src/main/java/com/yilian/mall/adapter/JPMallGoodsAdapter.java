package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPMallGoodsListEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2016/10/22 0022.
 */

public class JPMallGoodsAdapter extends android.widget.BaseAdapter {
    private final Context mContext;
    private final List<JPMallGoodsListEntity.ListBean> data;
    private final ImageLoader imageLoader;
    Drawable drawable = null;
    private DisplayImageOptions options;

    public JPMallGoodsAdapter(Context context, List<JPMallGoodsListEntity.ListBean> allGoodsLists, ImageLoader imageLoader) {
        this.mContext = context;
        this.data = allGoodsLists;
        this.imageLoader = imageLoader;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//              .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                .build();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MyViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_jp_good, null);
            holder = new MyViewHolder();
            holder.iv_goods1 = (ImageView) convertView.findViewById(R.id.iv_goods1);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_cost_price = (TextView) convertView.findViewById(R.id.tv_cost_price);
            holder.tv_market_price = (TextView) convertView.findViewById(R.id.tv_market_price);
            holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
            holder.tv_tag = (TextView) convertView.findViewById(R.id.tv_tag);
            holder.iv_null_goods = (ImageView) convertView.findViewById(R.id.iv_sold_out);
            holder.tvPrice = (TextView)convertView.findViewById(R.id.tv_price);
            holder.tvSoldNumber =(TextView) convertView.findViewById(R.id.tv_sold_number);
            //背包
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        JPMallGoodsListEntity.ListBean mallGoodsLists = data.get(position);

        String imageUrl = mallGoodsLists.goodsIcon;
        Logger.i(" 商品搜索  URL  "+imageUrl);
        if (!TextUtils.isEmpty(imageUrl)){
            GlideUtil.showImageNoSuffix(mContext,imageUrl,holder.iv_goods1);
        }else {
            holder.iv_goods1.setImageResource(R.mipmap.default_jp);
        }
        Logger.i("精确搜素mallGoodsLists.hasGoods" + mallGoodsLists.hasGoods);
        if ("0".equals(mallGoodsLists.hasGoods)) {
            holder.iv_null_goods.setVisibility(View.VISIBLE);
        } else {
            holder.iv_null_goods.setVisibility(View.GONE);
        }

        holder.tv_goods_name.setText(mallGoodsLists.goodsName);
        if (!TextUtils.isEmpty(mallGoodsLists.goodsPrice)&&!mallGoodsLists.goodsPrice.equals("false")){
            holder.tv_market_price.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(mallGoodsLists.goodsPrice)));//市场价
        }
        holder.tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(mallGoodsLists.goodsCost)));//售价
        holder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tv_discount.setText(mContext.getString(R.string.back_score) + MoneyUtil.getLeXiangBiNoZero(mallGoodsLists.returnIntegral) );
        holder.tv_tag.setText(mallGoodsLists.tagsName);
        holder.tvSoldNumber.setText("已售:"+mallGoodsLists.sellCount);
        return convertView;

    }


    class MyViewHolder {
        ImageView iv_null_goods;
        ImageView iv_goods1;
        TextView tv_goods_name;
        TextView tv_cost_price;
        TextView tv_market_price;
        TextView tv_discount;
        TextView tv_tag;

        public TextView tvPrice;
        public TextView tvSoldNumber;
    }

}


