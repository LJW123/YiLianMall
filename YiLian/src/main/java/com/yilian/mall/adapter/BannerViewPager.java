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
import com.yilian.mall.R;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2017/3/24 0024.
 */
public class BannerViewPager extends PagerAdapter {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;
    private boolean isCirculation;
    private List<String> listData;

    public BannerViewPager(List<String> albums, ImageLoader imageLoader, DisplayImageOptions options, Context context, boolean isCirculation) {
        this.listData = albums;
        this.imageLoader = imageLoader;
        this.options = options;
        this.context = context;
        this.isCirculation = isCirculation;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (position == 0) {
            position = listData.size() - 1;
        } else if (position == getCount() - 1) {
            //如果是最后一个就定位到可见的最后一个
            position = 0;
        } else {
            //其他位置都-1;
            position = position - 1;
        }

        String imageUrl = listData.get(position);
        if (!TextUtils.isEmpty(imageUrl)) {
            GlideUtil.showImageNoSuffix(context,imageUrl,mImageView);

        } else {
            mImageView.setImageResource(R.mipmap.login_module_default_jp);
        }

        int finalPosition = position;
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("imageView.setOnClickListener  ");
                imageBrower(finalPosition, (String[]) listData.toArray(new String[listData.size()]));
            }
        });

        container.addView(mImageView);
        return mImageView;
    }


    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (null != listData && listData.size() >= 1) {
            if (isCirculation) {
                return listData.size() + 2;
            } else {
                return listData.size();
            }
        } else {
            return 0;
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
