package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.mall.R;
import com.yilian.mall.entity.ShopsConsumeEntity;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * author LiXiuRen  PRAY NO BUG
 * Created by Administrator on 2016/7/8.
 */
public class ShopsConsumeAdapter extends android.widget.BaseAdapter {
    private final Context context;
    private final ArrayList list;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private String url;

    public ShopsConsumeAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(false) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//              .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                .build();
        imageLoader = ImageLoader.getInstance();
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
        ViewHolder holder ;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shops_consume, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopsConsumeEntity.LogBean listBean = (ShopsConsumeEntity.LogBean) list.get(position);
        holder.tv_consumer_name.setText(TextUtils.isEmpty(listBean.userName) ? "暂无昵称" : listBean.userName);
        Spanned spanned = Html.fromHtml("消费币种<font color=\"#f1604f\">奖励</font>");
        holder.tv_consume_style.setText(spanned);
        Spanned spanned1 = Html.fromHtml("到店消费<font color=\"#f1604f\">1</font>次");
        holder.tv_consume_times.setText(spanned1);
        holder.tv_consume_time.setText(StringFormat.formatDate(listBean.dealTime));
        ImageLoader imageLoader = ImageLoader.getInstance();
        String photoUrl = listBean.photo;
        if(!TextUtils.isEmpty(photoUrl)){
            if(photoUrl.contains("http://")||photoUrl.contains("https://")){
                url = photoUrl;
            }else{
                url = Constants.ImageUrl + photoUrl + Constants.ImageSuffix;
            }
            imageLoader.displayImage(url,holder.cv_head_photo,options);
        }else{
            holder.cv_head_photo.setImageResource(R.mipmap.ic_evaluate_user);
        }
        holder.cv_head_photo.setTag(url);

        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public JHCircleView cv_head_photo;
        public TextView tv_consumer_name;
        public TextView tv_consume_style;
        public TextView tv_consume_times;
        public TextView tv_consume_time;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.cv_head_photo = (JHCircleView) rootView.findViewById(R.id.cv_head_photo);
            this.tv_consumer_name = (TextView) rootView.findViewById(R.id.tv_consumer_name);
            this.tv_consume_style = (TextView) rootView.findViewById(R.id.tv_consume_style);
            this.tv_consume_times = (TextView) rootView.findViewById(R.id.tv_consume_times);
            this.tv_consume_time = (TextView) rootView.findViewById(R.id.tv_consume_time);
        }

    }
}
