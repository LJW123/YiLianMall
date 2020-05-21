package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.MTComboDetailsEntity;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/10 0010.
 */
public class ComboAdapter extends PagerAdapter {

    private final DisplayImageOptions options;
    private final ImageLoader imageLoader;
    private final Context mContext;
    private List<MTComboDetailsEntity.DataBean.PackageIconBean> packageIcon;
    private String DetailsimageUrl;

    public ComboAdapter(List<MTComboDetailsEntity.DataBean.PackageIconBean> packageIcon, DisplayImageOptions options, ImageLoader imageLoader, Context mContext) {
        this.packageIcon =packageIcon;
        this.options =options;
        this.imageLoader =imageLoader;
        this.mContext =mContext;
    }

    @Override
    public int getCount() {
        //不需要自动轮播
        if (packageIcon.size()>0){
            return packageIcon.size()+2;
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //如果是第一个位置 --- 添加D (数据源的最后一个位置)
        if (position == 0) {
            position = packageIcon.size() - 1;
        } else if (position == (getCount() - 1)) {
            //如果是最后一个位置(适配器的最后一个位置) --- 添加A (数据源的第一个位置)
            position = 0;
        } else {
            //其他位置-1
            position = position - 1;
        }

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
         DetailsimageUrl = packageIcon.get(position).path;
        if (!TextUtils.isEmpty(DetailsimageUrl)) {
            GlideUtil.showImageNoSuffix(mContext,DetailsimageUrl,imageView);
        }
        int finalPosition = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("imageView.setOnClickListener  ");

                String [] array=new String[packageIcon.size()];
                for (int i = 0; i < packageIcon.size(); i++) {
                    array[i]= String.valueOf(packageIcon.get(i).path);
                }
                imageBrower(finalPosition, array);
            }
        });
        container.addView(imageView);
        return imageView;
    }
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
