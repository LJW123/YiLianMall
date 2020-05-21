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
import com.yilian.mall.entity.MainActivityEntity;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * 主页分类图标适配器
 * Created by Administrator on 2016/7/28.
 */
public class MainActivityAdapter extends android.widget.BaseAdapter {


    public Context context;
    public ArrayList<MainActivityEntity.iconClass> list;
    BitmapUtils bitmapUtils;


    public MainActivityAdapter(Context context, ArrayList<MainActivityEntity.iconClass> list) {
        this.context = context;
        this.list = list;

        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_icon, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        if (list!=null){
            holder.textView.setText(list.get(position).name);
            bitmapUtils.display(holder.imageView, Constants.ImageUrl + list.get(position).image_url, new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    holder.imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                    holder.imageView.setImageResource(R.mipmap.login_module_default_jp);
                }
            });
        }

        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
