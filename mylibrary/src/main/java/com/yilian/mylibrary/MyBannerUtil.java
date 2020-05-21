package com.yilian.mylibrary;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by  on 2018/5/3.
 */

public class MyBannerUtil {
    /**
     *
     * @param banner
     * @param content
     * @param mContext
     * @param widthDP 宽 单位dp
     * @param heightDP 高 单位dp
     * @param <T>
     */
    public static  <T extends MyBannerData> void setBannerData(Banner banner, final List<T> content, final Context mContext,int widthDP,int heightDP) {
        if (content == null || content.size() <= 0) {
            banner.setVisibility(View.GONE);
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.height = (int) ((heightDP * 0.1) * ScreenUtils.getScreenWidth(mContext) / (widthDP * 0.1));
        ArrayList<String> imageList = new ArrayList<String>();
        int size = content.size();
        for (int i = 0; i < size; i++) {
            String image = content.get(i).getImgUrl();
            imageList.add(com.yilian.mylibrary.WebImageUtil.getInstance().getWebImageUrlNOSuffix(image));
        }
        banner.setImages(imageList)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        T t = content.get(position);
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(t.getType(), t.getContent());
                    }
                }).start();
    }
}
