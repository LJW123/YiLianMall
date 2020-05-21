package com.yilian.mall.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/8 0008.
 */

public abstract class BaseListAdapter<T> extends android.widget.BaseAdapter {

    protected Object[] object;
    protected List<T> datas;
    protected MyApplication context;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public BaseListAdapter(List<T> datas, Object... objects) {
        this.datas = datas;
        this.object = objects;
        context = MyApplication.getInstance();
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
        if (datas.size() > 0 && null != datas) {
            return datas.size();
        }
        return 0;
    }

    public void setNewData(List<T> datas){
        this.datas=datas;
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
    public abstract View getView(int position, View view, ViewGroup viewGroup);

    public void setNewDatasList(List<T> newDatasList) {
        this.datas = newDatasList;
    }

}
