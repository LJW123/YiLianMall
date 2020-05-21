package com.yilian.mylibrary;/**
 * Created by  on 2017/1/16 0016.
 */


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by  on 2017/1/16 0016.
 */
public class GlideUtil {
    public static void showImage(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        try {
            Glide.with(context.getApplicationContext())
                    .load(WebImageUtil.getInstance().getWebImageUrlNOSuffix(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//此属性保证如果图片是gif图可以动起来
                    .placeholder(R.mipmap.library_module_default_jp)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file)
                .into(imageView);

    }

    /**
     * 不压缩的圆角
     *
     * @param context
     * @param imageUrl
     * @param imageView
     * @param radious
     */
    public static void showImageRadius(Context context, String imageUrl, ImageView imageView, int radious) {
        try {
            Glide.with(context)
                    .load(WebImageUtil.getInstance().getWebImageUrlNOSuffix(imageUrl))
                    .bitmapTransform(new RoundedCornersTransformation(context, radious, 0))
                    .placeholder(R.mipmap.library_module_default_jp)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 带压缩的圆角
     *
     * @param context
     * @param imageUrl
     * @param imageView
     * @param radious
     */
    public static void showImageSuffixRadius(Context context, String imageUrl, ImageView imageView, int radious) {

        try {
            Glide.with(context)
                    .load(WebImageUtil.getInstance().getWebImageUrlWithSuffix(imageUrl))
                    .bitmapTransform(new RoundedCornersTransformation(context, radious, 0))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImageWithSuffix(Context context, String imageUrl, ImageView imageView) {
        try {
            Glide.with(context.getApplicationContext())
                    .load(WebImageUtil.getInstance().getWebImageUrlWithSuffix(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.library_module_default_jp)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImageNoSuffix(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        try {
            Glide.with(context.getApplicationContext())
                    .load(WebImageUtil.getInstance().getWebImageUrlNOSuffix(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.library_module_default_jp)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不给默认图的好处：获取成功的图片显示比例不会强制按照默认图的比例
     */
    public static void showImageNoSuffixNoPlaceholder(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        try {
            Glide.with(context.getApplicationContext())
                    .load(WebImageUtil.getInstance().getWebImageUrlNOSuffix(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 圆形加载 带截取
     */
    public static void showCirImage(Context context, String url, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(WebImageUtil.getInstance().getWebImageUrlWithSuffix(url))
                    .centerCrop()
                    .transform(new GlideCircleTransform(context))
                    .error(R.mipmap.library_module_team_pic)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示圆角图片
     * @param context
     * @param dp 圆角弧度
     */
    public static void showRoundImage(Context context, String url, ImageView imageView,int dp){
        try {
            Glide.with(context)
                    .load(WebImageUtil.getInstance().getWebImageUrlWithSuffix(url))
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, dp))
                    .error(R.mipmap.library_module_team_pic)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 圆形不闪烁
     */
    public static void showCirImageNoLit(final Context context, String url, final ImageView iv, final String id) {
        String tag = (String) iv.getTag();

        if (!TextUtils.equals(id, tag)) {
            iv.setImageResource(R.mipmap.library_module_team_pic);
        }
        try {
            Glide.with(context)
                    .load(WebImageUtil.getInstance().getWebImageUrlWithSuffix(url))
                    .centerCrop()
                    .transform(new GlideCircleTransform(context))
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            iv.setTag(id);
                            iv.setImageDrawable(resource);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resumeRequests(Context context) {
        if (false) {
            try {
                Glide.with(context).resumeRequests();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void pauseRequests(Activity activity) {
        if (false) {
            if (Util.isOnMainThread() && activity != null && !activity.isFinishing()) {
                try {
                    Glide.with(activity.getApplicationContext()).pauseRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void pauseRequests(Context context) {
        if (false) {
            try {
                Glide.with(context).pauseRequests();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 圆形加载--原图 占位图为头像
     */
    public static void showCirHeadNoSuffix(Context context, String url, ImageView imageView) {
        if (Util.isOnMainThread()) {
            try {
                Glide.with(context)
                        .load(WebImageUtil.getInstance().getWebImageUrlNOSuffix(url))
                        .centerCrop()
                        .transform(new GlideCircleTransform(context))
                        .error(R.mipmap.library_module_team_pic)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 圆形加载--原图 默认图片为品牌占位图
     */
    public static void showCirIconNoSuffix(Context context, String url, ImageView imageView) {
        if (Util.isOnMainThread()) {
            try {
                Glide.with(context)
                        .load(WebImageUtil.getInstance().getWebImageUrlNOSuffix(url))
                        .centerCrop()
                        .transform(new GlideCircleTransform(context))
                        .error(R.mipmap.library_module_default_jp_circle)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片强制保持宽高比
     * @param context
     * @param url
     * @param imageView
     */
    public static void showImgAdapt(Context context, String url, ImageView imageView) {
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUtil.showImageNoSuffix(context,url, imageView);

    }
}
