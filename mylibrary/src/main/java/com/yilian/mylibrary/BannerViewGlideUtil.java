package com.yilian.mylibrary;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by  on 2017/8/24 0024.
 */

public class BannerViewGlideUtil extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideUtil.showImageNoSuffixNoPlaceholder(context, String.valueOf(path), imageView);
    }
}
