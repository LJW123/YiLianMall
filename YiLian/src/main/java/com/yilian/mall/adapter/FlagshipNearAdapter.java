package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yilian.mall.R;
import com.yilian.mall.entity.FlagshipList;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 */

public class FlagshipNearAdapter extends android.widget.BaseAdapter{
    private Context context;
    private List<FlagshipList.DataBean.SuppliersBean> list;
    private String url;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public FlagshipNearAdapter(Context context, List<FlagshipList.DataBean.SuppliersBean> list) {
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
            convertView = View.inflate(context, R.layout.item_jp_shop, null);
            holder = new ViewHolder();
            holder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_activity_name);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_shop_name1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String urlFromNet = list.get(position).imageUrl;
        if(!TextUtils.isEmpty(urlFromNet)){
            if(urlFromNet.contains("http://") || urlFromNet.contains("https://")){
                url = urlFromNet;
            } else {
                url = Constants.ImageUrl + urlFromNet + BitmapUtil.getBitmapSuffix(context);
            }
            if (url.equals(holder.ivGoods.getTag())) {

            } else {
                imageLoader.displayImage(url, holder.ivGoods, options);
                holder.ivGoods.setTag(url);
            }
        }

        holder.tvName.setText(list.get(position).tagsName);
        holder.tvDesc.setText(list.get(position).supplierName);

        return convertView;
    }

    class ViewHolder {
        ImageView ivGoods;
        TextView tvName;
        TextView tvDesc;
    }
}
