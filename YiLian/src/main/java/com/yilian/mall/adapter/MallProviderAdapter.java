package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallProviderEntity;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MallProviderAdapter extends android.widget.BaseAdapter {
    private Context context;
    private ArrayList<MallProviderEntity.mallProvider> list;
    private BitmapUtils bitmapUtils;
    private MallProviderEntity.mallProvider data;
    private String imageUrl;

    public MallProviderAdapter(Context context, ArrayList<MallProviderEntity.mallProvider> list) {
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
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mall_provider, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvGoods = (TextView) convertView.findViewById(R.id.tv_goods);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvDetail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.imageView = (CircleImageView1) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        data = list.get(position);
        holder.tvName.setText(data.supplier_name);
        holder.tvGoods.setText(Html.fromHtml("商品：<font color=\"#ef543b\">" + data.count + "</font>件"));
        holder.tvCount.setText(Html.fromHtml("销量：<font color=\"#ef543b\">" + data.total_sale + "</font>"));
//        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MallProviderDetailActivity.class);
//                intent.putExtra("supplier_id",data.supplier_id);
//                mContext.startActivity(intent);
//            }
//        });

        if (data.supplier_icon.contains("http://")||data.supplier_icon.contains("https://")){
            imageUrl = data.supplier_icon;
        }else {
            imageUrl = Constants.ImageUrl + data.supplier_icon;
        }
        bitmapUtils.display(holder.imageView, imageUrl, new BitmapLoadCallBack<CircleImageView1>() {
            @Override
            public void onLoadCompleted(CircleImageView1 circleImageView1, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                circleImageView1.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(CircleImageView1 circleImageView1, String s, Drawable drawable) {
                circleImageView1.setImageResource(R.mipmap.login_module_default_jp);
            }
        });
        return convertView;
    }

   class ViewHolder{
       public CircleImageView1 imageView;
       public TextView tvName;
       public TextView tvGoods;
       public TextView tvCount;
       public TextView tvDetail;
   }
}
