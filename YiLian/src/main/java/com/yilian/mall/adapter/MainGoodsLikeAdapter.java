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
import com.yilian.mall.entity.MallGoodsListEntity;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 *主页猜你喜欢的商品适配器
 * Created by Administrator on 2016/8/1.
 */
public class MainGoodsLikeAdapter  extends android.widget.BaseAdapter {

    private final BitmapUtils bitmapUtils;
    Drawable drawable = null;
    ViewHolder holder;
    private List<MallGoodsListEntity.MallGoodsLists> datas;
    private Context context;
    public MainGoodsLikeAdapter(Context context, List<MallGoodsListEntity.MallGoodsLists> datas) {
        this.context=context;
        this.datas=datas;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_goods_like,null);
            holder=new ViewHolder();
            holder.iv_goods= (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.iv_goods_type= (ImageView) convertView.findViewById(R.id.iv_goods_type);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_sales_volume= (TextView) convertView.findViewById(R.id.tv_sales_volume);
            holder.tv_goods_price= (TextView) convertView.findViewById(R.id.tv_goods_price);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        MallGoodsListEntity.MallGoodsLists data = datas.get(position);
        bitmapUtils.display(holder.iv_goods, Constants.ImageUrl + data.goods_icon + Constants.ImageSuffix, null, new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
                super.onLoading(container, uri, config, total, current);
                container.setImageResource(R.mipmap.login_module_default_jp);
            }

            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                imageView.setImageResource(R.mipmap.login_module_default_jp);

            }
        });
        holder.tv_goods_name.setText(data.goods_name);
        holder.tv_sales_volume.setText("销量:" + data.goods_sale);

        switch (data.goods_type) {
            case 3: // goodsType 为3时 显示赠送价格
                holder.tv_goods_price.setText(data.goods_price + "");
                holder.iv_goods_type.setImageResource(R.mipmap.ic_goods_giving);
                drawable = context.getResources().getDrawable(R.mipmap.ic_mall_shopping);
                break;

            case 4: // goodsType 为4时 显示购买价格
                float goodsBuyPrice = NumberFormat.convertToFloat(data.goods_price, 0f);
                holder.tv_goods_price.setText(String.format("%.2f", goodsBuyPrice / 100));
                holder.iv_goods_type.setImageResource(R.mipmap.ic_goods_buy);
                drawable = context.getResources().getDrawable(R.mipmap.ic_mall_vouchers);
                break;

            default:

                break;
        }

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_goods_price.setCompoundDrawables(null, null, drawable, null);
        }


        return convertView;
    }

    public class ViewHolder{
        ImageView iv_goods,iv_goods_type;
        TextView tv_goods_name,tv_sales_volume,tv_goods_price;
    }
}