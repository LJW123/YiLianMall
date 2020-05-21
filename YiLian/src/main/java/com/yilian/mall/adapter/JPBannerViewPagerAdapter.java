package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.mall.entity.JPBannerEntity;

import java.util.ArrayList;

import static com.yilian.mall.MyApplication.sp;

/**
 * Created by  on 2016/10/18 0018.
 */

public abstract class JPBannerViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private ArrayList<JPBannerEntity> bannerBeanList;

    public JPBannerViewPagerAdapter(Context context, ArrayList<JPBannerEntity> bannerBeanList, ImageLoader imageLoader, DisplayImageOptions options) {
        this.context = context;
        this.bannerBeanList = bannerBeanList;
        this.imageLoader = imageLoader;
        this.options = options;
    }

    @Override
    public int getCount() {
        if (bannerBeanList.size() <= 0) {
            return 0;
        }
        return bannerBeanList.size() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        TextView textView = new TextView(mContext);
//        textView.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
//        textView.setText("Test");
//        container.addView(textView);
//        return textView;

        //如果是第一个位置 --- 添加D (数据源的最后一个位置)
        if (position == 0) {
            position = bannerBeanList.size() - 1;
        } else if (position == (getCount() - 1)) {
            //如果是最后一个位置(适配器的最后一个位置) --- 添加A (数据源的第一个位置)
            position = 0;
        } else {
            //其他位置-1
            position = position - 1;
        }

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final JPBannerEntity JPAppBannerBean = bannerBeanList.get(position);
        String jpImageUrl = JPAppBannerBean.JPImageUrl;
        com.yilian.mylibrary.GlideUtil.showImageNoSuffix(context,jpImageUrl,imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClickJump(JPAppBannerBean);
            }
        });
        container.addView(imageView);
        return imageView;

    }

    protected abstract void bannerClickJump(JPBannerEntity entity);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return sp.getBoolean("state", false);
    }
}
