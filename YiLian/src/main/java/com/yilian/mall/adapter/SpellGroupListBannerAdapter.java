package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.SpellGroupListEntity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/16 0016.
 */
public class SpellGroupListBannerAdapter extends PagerAdapter {

    private List<SpellGroupListEntity.GroupBannerBean> datas;
    private Context mContext;

    public SpellGroupListBannerAdapter(List<SpellGroupListEntity.GroupBannerBean> data, Context mContext) {
        this.datas = data;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        Logger.i("datasize+2  " + datas.size() + 2);
        if (datas==null||datas.size()<1){
            return 1;
        }else{
            return datas.size() + 2;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (position == 0) {
            position = datas.size() - 1;
        } else if (position == getCount() - 1) {
            //如果是最后一个就定位到可见的最后一个
            position = 0;
        } else {
            //其他位置都-1;
            position = position - 1;
        }
        String imageUrl = datas.get(position).img;
        if (!TextUtils.isEmpty(imageUrl)){
            if (imageUrl.contains("https://") || imageUrl.contains("http://")) {
                imageUrl = imageUrl + Constants.ImageSuffix;
            } else {
                imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
            }
        }
//        imageView.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(activityBannerBean.type, activityBannerBean.content);
//        }
//    });



        GlideUtil.showImage(mContext, imageUrl, imageView);
        container.addView(imageView);
        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
