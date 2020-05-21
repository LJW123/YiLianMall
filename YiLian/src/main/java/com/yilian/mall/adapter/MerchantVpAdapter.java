package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;

/**
 * Created by liuyuqi on 2016/12/8 0008.
 * 商家详情和套餐详情的viewpager
 */
public class MerchantVpAdapter extends PagerAdapter {

    private final DisplayImageOptions options;
    private final ImageLoader imageLoader;
    private final Context mContext;
    private final String[] imagesBeen;
    private final String merchant_id;


    public MerchantVpAdapter(String[] images, DisplayImageOptions options, ImageLoader imageLoader, Context mContext, String merchant_id) {
        this.imagesBeen =images;
        this.options =options;
        this.imageLoader =imageLoader;
        this.mContext =mContext;
        this.merchant_id =merchant_id;
    }

    @Override
    public int getCount() {
        if (imagesBeen.length>0 ){
            return imagesBeen.length+2;
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //如果是第一个位置 --- 添加D (数据源的最后一个位置)
        if (position == 0) {
            position = imagesBeen.length- 1;
        } else if (position == (getCount() - 1)) {
            //如果是最后一个位置(适配器的最后一个位置) --- 添加A (数据源的第一个位置)
            position = 0;
        } else {
            //其他位置-1
            position = position - 1;
        }

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
         String imageUrl= imagesBeen[position];
        Logger.i("imageUrl   "+imageUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            GlideUtil.showImageNoSuffix(mContext,imageUrl,imageView);
        }else {
            imageView.setImageResource(R.mipmap.default_jp);
        }

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //不做点击放大，跳转相册界面
//                Intent intent=new Intent(mContext, MtMerchantPhotoAlbumActivity.class);
//                intent.putExtra("merchant_id",merchant_id);
//                mContext.startActivity(intent);
//            }
//        });
        container.addView(imageView);
        return imageView;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
