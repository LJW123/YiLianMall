package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yilian.networkingmodule.entity.BannerEntity;

import java.util.ArrayList;

/**
 * Created by  on 2016/10/18 0018.
 */

public abstract class CategoryBannerViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private ArrayList<BannerEntity> bannerBeanList;

    public CategoryBannerViewPagerAdapter(Context context, ArrayList<BannerEntity> bannerBeanList) {
        this.context = context;
        this.bannerBeanList = bannerBeanList;
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
        BannerEntity bannerEntity = bannerBeanList.get(position);
        String jpImageUrl = bannerEntity.JPImageUrl;
        com.yilian.mylibrary.GlideUtil.showImageNoSuffix(context,jpImageUrl,imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClickJump(bannerEntity);
            }
        });
        container.addView(imageView);
        return imageView;

    }

    protected abstract void bannerClickJump(BannerEntity entity);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
