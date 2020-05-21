package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPLocation;
import com.yilian.mylibrary.GlideUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/26 0026.
 */

public class JPLocationItemAdapter extends android.widget.BaseAdapter{
    private Context context;
    private List<JPLocation.ListBean> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public JPLocationItemAdapter(Context context, List<JPLocation.ListBean> list) {
        this.context = context;
        this.list = list;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
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
            convertView = View.inflate(context, R.layout.item_location_jp, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String url = list.get(position).activityPrizeUrl;

        GlideUtil.showImageWithSuffix(context,url,holder.iv_left);
        holder.tv_name.setText(list.get(position).activityName);
        holder.tv_from.setText(list.get(position).activitySponsorName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(Long.valueOf(list.get(position).activityStartTime + "000")));
        holder.tv_time.setText(time);

        String state = list.get(position).activityStatus;
        switch (state){
            case "0":
                holder.iv_right.setImageResource(R.mipmap.location_state0);
                break;
            case "1":
                holder.iv_right.setImageResource(R.mipmap.location_state1);
                break;
            case "2":
                holder.iv_right.setImageResource(R.mipmap.location_state2);
                break;
        }

        return convertView;
    }

    class ViewHolder {
        ImageView iv_left;
        TextView tv_name;
        TextView tv_from;
        TextView tv_time;
        ImageView iv_right;
    }
}
