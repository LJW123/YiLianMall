package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.yilian.mall.R;
import com.yilian.mall.entity.MerchantEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MerchantListAdapter extends android.widget.BaseAdapter {
    Drawable drawable = null;
    BitmapUtils bitmapUtils;
    private Context context;
    private ArrayList<MerchantEntity.ListEntity> list;

    public MerchantListAdapter(Context context, ArrayList<MerchantEntity.ListEntity> list) {
        this.context = context;
        this.list = list;
        bitmapUtils = new BitmapUtils(context);
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_merchant_list, null);

            holder.goods_icon = (CircleImageView1) convertView.findViewById(R.id.goods_icon);
            holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            holder.goods_ratingbar = (RatingBar) convertView.findViewById(R.id.goods_ratingbar);
            holder.goods_grade = (TextView) convertView.findViewById(R.id.goods_grade);
            holder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            holder.goods_renqi = (TextView) convertView.findViewById(R.id.goods_renqi);
            holder.goods_sale = (TextView) convertView.findViewById(R.id.goods_sale);
            holder.ivGoodsType = (ImageView) convertView.findViewById(R.id.iv_goods_type);
            holder.tv_coupon = (TextView) convertView.findViewById(R.id.tv_coupon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            MerchantEntity.ListEntity data = list.get(position);
            float grade = NumberFormat.convertToFloat(data.goods_grade, 5f) / 10;
            bitmapUtils.display(holder.goods_icon, Constants.ImageUrl + data.goods_icon + Constants.ImageSuffix, null, new BitmapLoadCallBack<CircleImageView1>() {
                @Override
                public void onLoadCompleted(CircleImageView1 circleImageView1, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    circleImageView1.setImageBitmap(bitmap);
                }

                @Override
                public void onLoading(CircleImageView1 container, String uri, BitmapDisplayConfig config, long total, long current) {
                    super.onLoading(container, uri, config, total, current);

                }

                @Override
                public void onLoadFailed(CircleImageView1 circleImageView1, String s, Drawable drawable) {
                    circleImageView1.setImageResource(R.mipmap.login_module_default_jp);
                }
            });


            holder.goods_name.setText(data.goods_name);
            holder.goods_ratingbar.setRating(grade == 0 ? (float) 5.0 : grade);
            holder.goods_grade.setText(String.valueOf((grade == 0 ? 5.0 : grade)));
            holder.goods_renqi.setText(data.goods_popular + "人已浏览");
            holder.goods_sale.setText("累计销量" + data.goods_sale + "笔");
            holder.tv_coupon.setText("可使用"+context.getResources().getString(R.string.dikouquan)+ MoneyUtil.getXianJinQuan(data.coupon));
            if (data.goods_type == 3) {//送区
                int goodsBuyPrice = NumberFormat.convertToInt(data.goods_price, 0);
                holder.goods_price.setText(String.valueOf(goodsBuyPrice));
                holder.ivGoodsType.setImageResource(R.mipmap.ic_goods_giving);
                drawable = context.getResources().getDrawable(R.mipmap.ic_mall_shopping);
            } else if (data.goods_type == 4) {//购区
                float goodsBuyPrice = NumberFormat.convertToFloat(data.goods_price, 0.00f);
                holder.goods_price.setText(String.format("%.2f", goodsBuyPrice / 100));
                holder.ivGoodsType.setImageResource(R.mipmap.ic_goods_buy);
                drawable = context.getResources().getDrawable(R.mipmap.ic_mall_vouchers);
            }

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.goods_price.setCompoundDrawables(null, null, drawable, null);
            }
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView1 goods_icon;
        TextView goods_name;
        RatingBar goods_ratingbar;
        TextView goods_grade;
        TextView goods_price;
        ImageView ivGoodsType;
        TextView goods_renqi;
        TextView goods_sale;
        TextView tv_coupon;

    }
}
