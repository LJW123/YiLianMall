package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallCategoryHotListEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MallCategoryHotAdapter extends android.widget.BaseAdapter {
    Drawable drawable = null;
    BitmapUtils bitmapUtils;
    private Context context;
    private List<MallCategoryHotListEntity.MallCategoryHot> mallGoodsListses;

    public MallCategoryHotAdapter(Context context, List<MallCategoryHotListEntity.MallCategoryHot> mallGoodsListses) {
        this.context = context;
        this.mallGoodsListses = mallGoodsListses;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        if (mallGoodsListses != null) {
            return mallGoodsListses.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mallGoodsListses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_goods_like, null);
            holder = new ViewHolder();
            holder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.iv_goods_type = (ImageView) convertView.findViewById(R.id.iv_goods_type);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_sales_volume = (TextView) convertView.findViewById(R.id.tv_sales_volume);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_coupon = (TextView) convertView.findViewById(R.id.tv_coupon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MallCategoryHotListEntity.MallCategoryHot data = mallGoodsListses.get(position);
        bitmapUtils.display(holder.iv_goods, Constants.ImageUrl + data.goods_icon + Constants.ImageSuffix, null, new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
                super.onLoading(container, uri, config, total, current);
            }

            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                imageView.setImageResource(R.mipmap.login_module_default_jp);

            }
        });
        holder.tv_goods_name.setText(data.goods_name);
        holder.tv_sales_volume.setText("销量:" + data.goods_sale);
        holder.tv_coupon.setText("可抵扣:" + MoneyUtil.getXianJinQuan(data.coupon));
//        switch (data.goods_type) {
//            case "3": // goodsType 为3时 显示赠送价格
//                holder.tv_goods_price.setText(data.goods_price + "");
//                holder.iv_goods_type.setImageResource(R.mipmap.ic_goods_giving);
//                drawable = mContext.getResources().getDrawable(R.mipmap.ic_mall_shopping);
//                break;
//
//            case "4": // goodsType 为4时 显示购买价格
        float goodsBuyPrice = NumberFormat.convertToFloat(data.goods_price, 0f);
        holder.tv_goods_price.setText(String.format("%.2f", goodsBuyPrice / 100));
        holder.iv_goods_type.setImageResource(R.mipmap.ic_goods_buy);
        drawable = context.getResources().getDrawable(R.mipmap.ic_mall_vouchers);
//                break;

//            default:

//                break;
//        }

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_goods_price.setCompoundDrawables(null, null, drawable, null);
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView iv_goods, iv_goods_type;
        TextView tv_goods_name, tv_sales_volume, tv_goods_price, tv_coupon;
    }
}
