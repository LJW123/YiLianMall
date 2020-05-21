package com.yilian.mall.shoppingcard.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.activity.CardCommodityDetailActivity;
import android.support.v4.app.Fragment;

import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.widgets.CustScrollView;
import com.yilian.mall.widgets.ScreenNumView;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.GoodsSkuPrice;
import com.yilian.networkingmodule.entity.GoodsSkuProperty;
import com.yilian.networkingmodule.entity.JPCommodityDetail;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @auther 马铁超 on 2018/11/22 10:15
 * 购物卡详情上半部分
 */

public class CardCommodityTopPageFragment extends BaseFragment {
    private View rootView;
    private ViewPager viewPager;
    private ScreenNumView llDot;
    private TextView tvGoYhs, tvShopName, tvProductDesc, tvProductKnow, tvProductFrom, tvProductName, tvContainerChoose, tvContainerAssure4, tvContainerAssure3, tvContainerAssure2, tvContainerAssure1, tvContainerSale, tvContainerGrade, tvContainerQuestionJfg, tvContainerJuanJfg, tvContainerQuestion, tvContainerLedou, tvContainerJuan, tvJifen, tvGavePower, tvContainerTag, tvContainerTitle, tvContainerMarker, tvContainerPrice, tvContainerJifen;
    private RelativeLayout rlGavePower;
    private ImageView ivClassify;
    private LinearLayout llContainerMarker;
    private LinearLayout llJfg;
    private RatingBar ratingBar;
    private LinearLayout llContainerChoose;
    private View viewContainerChoose;
    private LinearLayout llProductKnow;
    private LinearLayout llShop;
    private CustScrollView customScrollView;
    private CardCommodityDetailActivity activity;
    private Context mContext;
    private int width;
    /**
     * 商品id
     */
    public String goodsId;
    public DisplayImageOptions options;
    protected int screenWidth;
    public Map<String, GoodsSkuProperty> mSelectedProperty;
    public Map<String, GoodsSkuPrice> mSelectedPropertyPrice;
    private CardCommodityTopPageFragment.onUpLoadErrorListener onUpLoadErrorListener;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_commdetail_top, null);
        activity = (CardCommodityDetailActivity) getActivity();
        mContext = getContext();
        initView();
        getCommodityDetails();
        onUpLoadErrorListener.isError(false);
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    /**
     * 获取购物卡详情数据
     */
    private void getCommodityDetails() {
        goodsId = activity.goodsId;
        /*if(!TextUtils.isEmpty(goodsId)){
            Subscription subscription = RetrofitUtils3.getRetrofitService(context).getV3MallGoodInfo("goods/mallgood_info_v3",
                    filialeId, goodsId, regional_type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JPCommodityDetail>() {
                        @Override
                        public void onCompleted() {
                            stopMyDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            stopMyDialog();
                            if (null != onUpLoadErrorListener) {
                                onUpLoadErrorListener.isError(true);
                            }
                        }

                        @Override
                        public void onNext(JPCommodityDetail o) {
                            if (null != onUpLoadErrorListener) {
                                onUpLoadErrorListener.isError(false);
                            }
                            goodInfo = o.data.goodInfo;
                            initData();
                        }
                    });
            if (activity != null) {
                activity.addSubscription(subscription);
            }
        }*/
    }

    private void initView() {
        Logger.i("ray-" + "走了top的initView");
        if (activity.layoutNormal.getVisibility() == View.GONE) {
            activity.layoutNormal.setVisibility(View.VISIBLE);
        }
        if (activity.layoutQuestion.getVisibility() == View.VISIBLE) {
            activity.layoutQuestion.setVisibility(View.GONE);
        }
        screenWidth = ScreenUtils.getScreenWidth(mContext);
        customScrollView = (CustScrollView) rootView.findViewById(R.id.customScrollView);
        customScrollView.scrollTo(0, 1000);
        tvContainerTag = (TextView) rootView.findViewById(R.id.tv_container_tag);
        tvContainerTitle = (TextView) rootView.findViewById(R.id.tv_container_title);
        tvContainerPrice = (TextView) rootView.findViewById(R.id.tv_container_price);
        llContainerMarker = (LinearLayout) rootView.findViewById(R.id.ll_container_marker);
        tvContainerMarker = (TextView) rootView.findViewById(R.id.tv_container_marker);
        tvContainerJuan = (TextView) rootView.findViewById(R.id.tv_container_juan);
        tvContainerLedou = rootView.findViewById(R.id.tv_container_ledou);
        tvContainerQuestion = (TextView) rootView.findViewById(R.id.tv_container_question);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        tvContainerGrade = (TextView) rootView.findViewById(R.id.tv_container_grade);
        tvContainerSale = (TextView) rootView.findViewById(R.id.tv_container_sale);
        tvContainerAssure1 = (TextView) rootView.findViewById(R.id.tv_container_assure1);
        tvContainerAssure2 = (TextView) rootView.findViewById(R.id.tv_container_assure2);
        tvContainerAssure3 = (TextView) rootView.findViewById(R.id.tv_container_assure3);
        tvContainerAssure4 = (TextView) rootView.findViewById(R.id.tv_container_assure4);
        viewContainerChoose = rootView.findViewById(R.id.view_container_choose);
        llContainerChoose = (LinearLayout) rootView.findViewById(R.id.ll_container_choose);
        tvContainerChoose = (TextView) rootView.findViewById(R.id.tv_container_choose);
        tvProductName = (TextView) rootView.findViewById(R.id.tv_product_name);
        tvProductFrom = (TextView) rootView.findViewById(R.id.tv_product_from);
        tvProductDesc = (TextView) rootView.findViewById(R.id.tv_product_desc);
        llProductKnow = (LinearLayout) rootView.findViewById(R.id.ll_product_know);
        tvProductKnow = (TextView) rootView.findViewById(R.id.tv_product_know);
        llShop = (LinearLayout) rootView.findViewById(R.id.ll_shop);
        tvShopName = (TextView) rootView.findViewById(R.id.tv_shop_name);
        //轮播图
        viewPager = (ViewPager) rootView.findViewById(R.id.vp_comm_img);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new ViewPager.LayoutParams());
        params.height = (int) (screenWidth / 1);
        viewPager.setLayoutParams(params);
        rlGavePower = rootView.findViewById(R.id.rl_gave_power);
        tvGavePower = rootView.findViewById(R.id.tv_gave_power);
        llDot = (ScreenNumView) rootView.findViewById(R.id.vp_indicator);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                llDot.snapToPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();// 获取屏幕宽度
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        mSelectedProperty = new HashMap<>();
        mSelectedPropertyPrice = new HashMap<>();
        tvJifen = (TextView) rootView.findViewById(R.id.tv_jifen);
        tvGoYhs = (TextView) rootView.findViewById(R.id.tv_go_yhs);
        tvContainerJifen = (TextView) rootView.findViewById(R.id.tv_container_jifen);
        ivClassify = (ImageView) rootView.findViewById(R.id.iv_classify);
        llJfg = (LinearLayout) rootView.findViewById(R.id.ll_jfg);
        tvContainerJuanJfg = (TextView) rootView.findViewById(R.id.tv_container_juan_jfg);
        tvContainerQuestionJfg = (TextView) rootView.findViewById(R.id.tv_container_question_jfg);

    }
    public void setOnUpLoadErorrListener(onUpLoadErrorListener onUpLoadErorrListener) {
        this.onUpLoadErrorListener = onUpLoadErorrListener;
    }
    public interface onUpLoadErrorListener {
        void isError(boolean isError);
    }
}


