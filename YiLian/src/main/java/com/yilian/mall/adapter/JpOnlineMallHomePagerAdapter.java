package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.widgets.AlphaScaleTransformer;
import com.yilian.mall.R;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.OnlineMallActivityEntity;
import com.yilian.networkingmodule.entity.OnlineMallCategoryData;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaiyaohua
 */

public class JpOnlineMallHomePagerAdapter<T extends OnlineMallCategoryData> extends BaseMultiItemQuickAdapter<T, com.chad.library.adapter.base.BaseViewHolder> {

    private Context mContext;

    public JpOnlineMallHomePagerAdapter(Context mContext, List<T> data) {
        super(data);
        this.mContext = mContext;
        //占据比重为4
        addItemType(OnlineMallCategoryData.ITEM_TYPE_WEIT_FOUR, R.layout.item_online_mall_activity_prefecture);
        //占据比重为2
        addItemType(OnlineMallCategoryData.ITEM_TYPE_WEIT_TWO, R.layout.item_online_mall_area_goods1);
        //占据比重为1
        addItemType(OnlineMallCategoryData.ITEM_TYPE_WEIT_ONE, R.layout.item_online_mall_area_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        switch (item.getSpanSize()) {
            case OnlineMallCategoryData.ITEM_SPAN_SIZE_ONE:
                setActivityBottomContent(helper, item);
                break;
            case OnlineMallCategoryData.ITEM_SPAN_SIZE_FOUR:
                //设置头部专区titile数据
                if (item instanceof OnlineMallActivityEntity.Data.Activitys) {
                    helper.getView(R.id.ll_title_layout).setVisibility(View.VISIBLE);
                    helper.getView(R.id.recommend_banner).setVisibility(View.GONE);
                    ImageView subTitilePicLeft = helper.getView(R.id.sub_title_pic_left);
                    ImageView subTitilePicRight = helper.getView(R.id.sub_title_pic_right);
                    if (!TextUtils.isEmpty(((OnlineMallActivityEntity.Data.Activitys) item).subTitlePic)) {
                        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, ((OnlineMallActivityEntity.Data.Activitys) item).subTitlePic, subTitilePicLeft);
                        subTitilePicLeft.setVisibility(View.VISIBLE);
                    } else {
                        subTitilePicLeft.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(((OnlineMallActivityEntity.Data.Activitys) item).subTitlePic)) {
                        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, ((OnlineMallActivityEntity.Data.Activitys) item).subTitlePic, subTitilePicRight);
                        subTitilePicRight.setVisibility(View.VISIBLE);
                    } else {
                        subTitilePicRight.setVisibility(View.GONE);
                    }
                    helper.setText(R.id.sub_title, ((OnlineMallActivityEntity.Data.Activitys) item).subTitle);

                }
                setRecommendBanner(helper, item);
                break;
            case OnlineMallCategoryData.ITEM_SPAN_SIZE_THREE:
                break;
            case OnlineMallCategoryData.ITEM_SPAN_SIZE_TWO:
                setActivityTopContent(helper, item);
                break;
            default:
                break;
        }
    }

    //设置活动专区的头部和底部item
    private void setActivityBottomContent(BaseViewHolder helper, OnlineMallCategoryData item) {
        String ivButtomTitlePic = "";
        String ivButtomTitleContentPic = "";
        if (item instanceof OnlineMallActivityEntity.Data.Activitys.BottomDetails) {
            List<String> contentPic = ((OnlineMallActivityEntity.Data.Activitys.BottomDetails) item).contentPic;
            if (null != contentPic && contentPic.size() > 0) {
                ivButtomTitleContentPic = ((OnlineMallActivityEntity.Data.Activitys.BottomDetails) item).contentPic.get(0);
            }
        }
        ivButtomTitlePic = ((OnlineMallActivityEntity.Data.Activitys.BottomDetails) item).titlePic;
        GlideUtil.showImage(mContext, ivButtomTitleContentPic, helper.getView(R.id.iv_buttom_content_pic));
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, ivButtomTitlePic, helper.getView(R.id.iv_buttom_title_pic));
    }

    //banner图设置
    private void setRecommendBanner(BaseViewHolder helper, OnlineMallCategoryData item) {
        setBanner(helper, item);

    }

    /**
     * 调整banner样式和设置banner数据
     *
     * @param item
     */
    private void setBanner(BaseViewHolder helper, OnlineMallCategoryData item) {
        if (item instanceof OnlineMallActivityEntity.Data.Activitys.CustomRecommendBanner) {
            helper.getView(R.id.ll_title_layout).setVisibility(View.GONE);
            List<OnlineMallActivityEntity.Data.Activitys.RecommendBanner> data = ((OnlineMallActivityEntity.Data.Activitys.CustomRecommendBanner) item).recommendBanner;
            if (null == data || data.size() == 0) {
                return;
            }
            List<String> imageUrlList = new ArrayList<>();
            for (OnlineMallActivityEntity.Data.Activitys.RecommendBanner bannerData : data) {
                imageUrlList.add(bannerData.image);
                imageUrlList.add(bannerData.image);
            }
            if (imageUrlList.size() == 0) {
                helper.getView(R.id.recommend_banner).setVisibility(View.GONE);
                return;
            } else {
                helper.getView(R.id.recommend_banner).setVisibility(View.VISIBLE);
            }
            Banner banner = (Banner) helper.getView(R.id.recommend_banner);
            banner.setOffscreenPageLimit(imageUrlList.size());
            banner.setImages(imageUrlList)
                    .setBannerStyle(BannerConfig.NOT_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                        }
                    }).start();
            if (imageUrlList.size() == 1) {
                return;
            }
            ViewPager mViewPager = null;
            try {
                Field imageViews = Banner.class.getDeclaredField("imageViews");
                imageViews.setAccessible(true);
                List<View> imageViewList = (List<View>) imageViews.get(banner);
                for (View iv : imageViewList) {
                    if (iv instanceof ImageView) {
                        iv.setPadding(10, 10, 20, 10);
                        iv.setBackgroundColor(mContext.getResources().getColor(R.color.none_color));
                    }
                }
                Field viewPager = Banner.class.getDeclaredField("viewPager");
                //设置允许访问私有字段
                viewPager.setAccessible(true);
                mViewPager = (ViewPager) viewPager.get(banner);
                mViewPager.setClipChildren(false);
                banner.setClipChildren(false);
                banner.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
                layoutParams.setMargins(0, 0, 40, 0);
                banner.setPageTransformer(true, new AlphaScaleTransformer());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setActivityTopContent(BaseViewHolder helper, OnlineMallCategoryData item) {
        String ivTopTitlePic = "";
        LinearLayout llContentPic = helper.getView(R.id.ll_content_pic);
        ImageView topTitlePic = helper.getView(R.id.top_title_pic);
        ImageView ivLeftPic = helper.getView(R.id.iv_left_pic);
        ImageView ivMiddlePic = helper.getView(R.id.iv_middle_pic);
        ImageView ivRightPic = helper.getView(R.id.iv_right_pic);
        View goodsDetails = helper.getView(R.id.layout_goods_details);
        if (item instanceof OnlineMallActivityEntity.Data.Activitys.TopDetails) {
            ivTopTitlePic = ((OnlineMallActivityEntity.Data.Activitys.TopDetails) item).titlePic;
            //设置top图片
            GlideUtil.showImageNoSuffixNoPlaceholder(mContext, ivTopTitlePic, topTitlePic);
            llContentPic.setVisibility(View.VISIBLE);
            goodsDetails.setVisibility(View.GONE);
            List<String> imagUrl = ((OnlineMallActivityEntity.Data.Activitys.TopDetails) item).contentPic;
            if (null != imagUrl) {

                for (int i = 0; i < imagUrl.size(); i++) {
                    if (i == 0) {
                        GlideUtil.showImage(mContext, imagUrl.get(i), ivLeftPic);
                    }
                    if (i == 1) {
                        GlideUtil.showImage(mContext, imagUrl.get(i), ivMiddlePic);
                    }
                    if (i == 3) {
                        ivRightPic.setVisibility(View.VISIBLE);
                        GlideUtil.showImage(mContext, imagUrl.get(i), ivRightPic);
                    }
                }
            }
        } else {
            //设置商品详情
            llContentPic.setVisibility(View.GONE);
            goodsDetails.setVisibility(View.VISIBLE);
        }
    }
}
