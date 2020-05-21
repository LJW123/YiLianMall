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
import com.yilian.mall.R;
import com.yilian.mall.entity.LeFenHome;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/19 0019.
 */

public class LefenHomeHozlvAdapter extends android.widget.BaseAdapter{
    private ImageLoader imageLoader;
    private List<LeFenHome.DataBean.ShopListBean> list;
    private Context context;
    private String url;
    private DisplayImageOptions options;

    public LefenHomeHozlvAdapter(Context context, List<LeFenHome.DataBean.ShopListBean> list) {
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
            convertView = View.inflate(context, R.layout.item_lefenhome_hoz, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String urlFromNet = list.get(position).shopImage;
        if(!TextUtils.isEmpty(urlFromNet)){
            if(urlFromNet.contains("http://") || urlFromNet.contains("https://")){
                url = urlFromNet;
            } else {
                url = Constants.ImageUrl + urlFromNet + Constants.ImageSuffix;
            }
            if (url.equals(holder.iv.getTag())) {

            } else {
                imageLoader.displayImage(url, holder.iv, options);
                holder.iv.setTag(url);
            }
        }
        holder.tv_name.setText(list.get(position).shopName);
        holder.tv_address.setText(list.get(position).shopAddress);

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv_name;
        TextView tv_address;
    }
}
