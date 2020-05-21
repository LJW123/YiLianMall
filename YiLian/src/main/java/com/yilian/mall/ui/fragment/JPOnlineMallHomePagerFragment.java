package com.yilian.mall.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JpOnlineMallHomePagerAdapter;
import com.yilian.mall.adapter.OnlineMallIconAdapter;
import com.yilian.mall.adapter.OnlineMallPageViewPagerAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.utils.LanguagePackageUtils;
import com.yilian.mall.widgets.CustomViewPager;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.OnlineMallActivityEntity;
import com.yilian.networkingmodule.entity.OnlineMallBannerJsonEntity;
import com.yilian.networkingmodule.entity.OnlineMallCategoryData;
import com.yilian.networkingmodule.entity.OnlineMallRecommendGoodsEntity;
import com.yilian.networkingmodule.entity.OnlineMallUpData;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 线上商城首页
 *
 * @author zhaiyaohua
 */

public class JPOnlineMallHomePagerFragment extends JPBaseFragment {
    /**
     * 当前显示的是第几页
     */
    int curIndex = 0;
    private MySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    private View mTitileLayout;
    private View mHeaderView;
    private JpOnlineMallHomePagerAdapter mallHomePagerAdapter;
    private ArrayList<OnlineMallCategoryData> data = new ArrayList<>();
    private Banner banner;
    /**
     * viewpager
     */
    private View iconsView;
    private CustomViewPager mPager;
    private LinearLayout mLlDot;
    private ImageView iconsBgIv;
    /**
     * 广告位
     */
    private ImageView ivAdvertUp;
    private ImageView ivAdvertBelow;
    private LinearLayout llActivityArea;
    /**
     *
     */
    private View includeActivityPrefecture;
    private ImageView ivActivityPrefectureLeft, ivActivityPrefectureRight;
    private TextView tvActivityArea;
    private ImageView ivBgActivityPrefecture;
    private View line;
    private ImageView ivLeftPic;
    private ImageView ivToptPic;
    private ImageView ivBelowtPic;
    private OnlineMallBannerJsonEntity bannerJsonEntity;
    private ArrayList mPagerList;
    private int page = 0;
    private boolean isFirst = true;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(mContext, R.layout.fragment_jp_online_mall_home_pager, null);
        initView(rootView);
        initListner();
        return rootView;
    }

    private void initListner() {
        mSwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMallBannerUpdata();
                getOnlineMallActivity();

            }
        });
        mallHomePagerAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();

            }
        }, mRecycleView);
    }

    private void initView(View rootView) {
        initHeadView();
        mTitileLayout = rootView.findViewById(R.id.home_title_layout);
        mSwipeRefreshLayout = (MySwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        mRecycleView.setLayoutManager(manager);
        mallHomePagerAdapter = new JpOnlineMallHomePagerAdapter(mContext, data);
        mallHomePagerAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 2, R.color.color_bg);
        mRecycleView.addItemDecoration(decor);
        mallHomePagerAdapter.addHeaderView(mHeaderView);
        mRecycleView.setAdapter(mallHomePagerAdapter);

    }

    /**
     * 初始化头部
     */
    private void initHeadView() {
        mHeaderView = View.inflate(mContext, R.layout.header_online_mall_home_pager, null);
        ivAdvertUp = (ImageView) mHeaderView.findViewById(R.id.iv_advert_up);
        ivAdvertBelow = (ImageView) mHeaderView.findViewById(R.id.iv_advert_below);
        banner = (Banner) mHeaderView.findViewById(R.id.banner_top);
        iconsView = mHeaderView.findViewById(R.id.include_icons);
        mPager = (CustomViewPager) iconsView.findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) iconsView.findViewById(R.id.ll_dot);
        iconsBgIv = (ImageView) iconsView.findViewById(R.id.icons_bg);
        llActivityArea = (LinearLayout) mHeaderView.findViewById(R.id.ll_activity_area);
        tvActivityArea = (TextView) mHeaderView.findViewById(R.id.tv_activity_area);
        line = mHeaderView.findViewById(R.id.line);
        ivLeftPic = (ImageView) mHeaderView.findViewById(R.id.iv_left_pic);
        ivToptPic = (ImageView) mHeaderView.findViewById(R.id.iv_top_pic);
        ivBelowtPic = (ImageView) mHeaderView.findViewById(R.id.iv_below_pic);
        ivBgActivityPrefecture = (ImageView) mHeaderView.findViewById(R.id.iv_bg_activity_prefecture);
        ivActivityPrefectureLeft = (ImageView) mHeaderView.findViewById(R.id.iv_activity_prefecture_left);
        ivActivityPrefectureRight = (ImageView) mHeaderView.findViewById(R.id.iv_activity_prefecture_right);
        includeActivityPrefecture = mHeaderView.findViewById(R.id.include_activity_prefecture);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getMallBannerUpdata();
                    isFirst = false;
                    getOnlineMallActivity();
                    initRecommendGoodsList();


                }
            }, 500);

        }
    }

    @Override
    protected void loadData() {


    }

    /**
     * 线上商城banner获取
     */

    private void getMallBannerUpdata() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMallBannerUpDate("get_mall_banner_upd", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnlineMallUpData>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(OnlineMallUpData data) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        //读取md5值
                        String onLineMd5 = PreferenceUtils.readStrConfig(Constants.KEY_ONLINE_MALL_BANNER_MD5, mContext);
                        //md5是空或者与保存的md5不一样保存并且进行网络请求
                        if (TextUtils.isEmpty(onLineMd5) || !onLineMd5.equals(data.str)) {
                            //保存MD5
                            PreferenceUtils.writeStrConfig(Constants.KEY_ONLINE_MALL_BANNER_MD5, data.str, mContext);
                            //下载json文件并保存
                            LanguagePackageUtils.createFile(data.url, Constants.FILE_NAME_MANLL_BANNER);
                            //获取文件并转换为字符串
                            String jsonStr = LanguagePackageUtils.getFile(Constants.FILE_NAME_MANLL_BANNER);
                            //gson解析字符串
                            bannerJsonEntity = LanguagePackageUtils.parseJsonWithGson(jsonStr, OnlineMallBannerJsonEntity.class);
                            PreferenceUtils.writeStrConfig(Constants.KEY_ONLINE_MALL_BANNER_DATA, jsonStr, mContext);
                        } else {
                            //直接使用缓存好的json字符串
                            String jsonStr1 = PreferenceUtils.readStrConfig(Constants.KEY_ONLINE_MALL_BANNER_DATA, mContext);
                            bannerJsonEntity = LanguagePackageUtils.parseJsonWithGson(jsonStr1, OnlineMallBannerJsonEntity.class);
                        }
                        updataHeadViewData(bannerJsonEntity);

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 设置图标
     *
     * @param entity1
     */

    private void updataHeadViewData(OnlineMallBannerJsonEntity entity1) {
        if (null == entity1) {
            iconsView.setVisibility(View.GONE);
            return;
        }
        iconsView.setVisibility(View.VISIBLE);
        //icon背景图
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iconsBgIv.getLayoutParams();
        layoutParams.height = (int) ((146 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));
        if (TextUtils.isEmpty(entity1.iconBg.image)) {
            iconsBgIv.setVisibility(View.GONE);
        } else {
            GlideUtil.showImageNoSuffixNoPlaceholder(mContext, entity1.iconBg.image, iconsBgIv);
            iconsBgIv.setVisibility(View.VISIBLE);
        }
        //初始化轮播
        List<String> imagList = new ArrayList<>();
        if (null != entity1.activityBanner) {
            for (OnlineMallBannerJsonEntity.ActivityBanner activityBanner : entity1.activityBanner) {
                imagList.add(activityBanner.image);
            }
            setBanner(imagList);
        }
        //图标设置
        if (null == entity1.iconClass || entity1.iconClass.size() <= 0) {
            return;
        }
        if (mPagerList == null) {
            mPagerList = new ArrayList<>();
        } else {
            mPagerList.clear();
        }
        int size = 10;
        double counts = entity1.iconClass.size() * 1.0 / size;
        //向上取整
        counts = Math.ceil(counts);
        List<OnlineMallBannerJsonEntity.IconClass> dataList = new ArrayList<>();
        for (int i = 0; i <= counts - 1; i++) {
            dataList.clear();
            int maxJ = Math.min((i + 1) * size, entity1.iconClass.size());
            for (int j = i * size; j < maxJ; j++) {
                dataList.add(entity1.iconClass.get(j));
            }
            //设置当前的列表不能滑动
            GridLayoutManager layoutManager1 = new GridLayoutManager(mContext, 5);

            layoutManager1.setOrientation(GridLayoutManager.VERTICAL);
            RecyclerView iconRecycleView1 = (RecyclerView) View.inflate(mContext, R.layout.online_mall_icon_recycleview, null);
            iconRecycleView1.setLayoutManager(layoutManager1);
            OnlineMallIconAdapter adapter = new OnlineMallIconAdapter(R.layout.item_online_mall_home_pager, false);
            iconRecycleView1.setAdapter(adapter);
            adapter.setNewData(dataList);
            adapter.loadMoreComplete();
            mPagerList.add(iconRecycleView1);
        }
        if (mLlDot == null) {
            mLlDot = (LinearLayout) iconsView.findViewById(R.id.ll_dot);
        }
        if (counts <= 1) {//如果只有一页，那么隐藏小圆点
            mLlDot.setVisibility(View.GONE);
        } else {
            mLlDot.removeAllViews();//添加前先移除
            for (int i = 0; i < counts; i++) {
                mLlDot.addView(LayoutInflater.from(mContext).inflate(R.layout.mt_home_page_dot, null));
            }
            // 默认显示第一页
            mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                    .setBackgroundResource(R.drawable.mt_home_page_dot_selected);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageSelected(int position) {
                    // 取消圆点选中
                    mLlDot.getChildAt(curIndex)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.mt_home_page_dot_normal);
                    // 圆点选中
                    mLlDot.getChildAt(position)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.mt_home_page_dot_selected);
                    curIndex = position;
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
        mPager.setAdapter(new OnlineMallPageViewPagerAdapter(mPagerList));
        //设置顶部广告位
        if (null == entity1.advert || TextUtils.isEmpty(entity1.advert.image)) {
            ivAdvertUp.setVisibility(View.GONE);
        } else {
            GlideUtil.showImage(mContext, entity1.advert.image, ivAdvertUp);
            ivAdvertUp.setVisibility(View.VISIBLE);
        }
        //设置底部广告位
        if (null == entity1.advertTwo || TextUtils.isEmpty(entity1.advertTwo.image)) {
            ivAdvertBelow.setVisibility(View.GONE);
        } else {
            GlideUtil.showImage(mContext, entity1.advertTwo.image, ivAdvertBelow);
            ivAdvertBelow.setVisibility(View.VISIBLE);
        }
        //设置活动专区
        if (null == entity1.activityPrefecture) {
            includeActivityPrefecture.setVisibility(View.GONE);
        } else {
            includeActivityPrefecture.setVisibility(View.VISIBLE);
            GlideUtil.showImageNoSuffixNoPlaceholder(mContext, entity1.activityPrefecture.bg.image, ivBgActivityPrefecture);
            if (null == entity1.activityPrefecture.left || TextUtils.isEmpty(entity1.activityPrefecture.left.image)) {
                ivLeftPic.setVisibility(View.GONE);
            } else {
                ivLeftPic.setVisibility(View.VISIBLE);
                GlideUtil.showImage(mContext, entity1.activityPrefecture.left.image, ivLeftPic);
            }
            if (null == entity1.activityPrefecture.rightTop || TextUtils.isEmpty(entity1.activityPrefecture.rightTop.image)) {
                ivToptPic.setVisibility(View.GONE);
            } else {
                ivToptPic.setVisibility(View.VISIBLE);
                GlideUtil.showImage(mContext, entity1.activityPrefecture.rightTop.image, ivToptPic);
            }
            if (null == entity1.activityPrefecture.rightBottom || TextUtils.isEmpty(entity1.activityPrefecture.rightBottom.image)) {
                ivBelowtPic.setVisibility(View.GONE);
            } else {
                ivBelowtPic.setVisibility(View.VISIBLE);
                GlideUtil.showImage(mContext, entity1.activityPrefecture.rightBottom.image, ivBelowtPic);
            }
        }
    }


    /**
     * 活动区获取
     */

    private void getOnlineMallActivity() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getOnlineMallActivity("online_mall_activity")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OnlineMallActivityEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());

                    }

                    @Override
                    public void onNext(OnlineMallActivityEntity entity) {
                        OnlineMallActivityEntity.Data onlineData = entity.data;
                        if (null != onlineData) {
                            data.clear();
                            List<OnlineMallActivityEntity.Data.Activitys> listActivity = new ArrayList<>();
                            if (null != onlineData.activitys && onlineData.activitys.size() != 0) {
                                listActivity.addAll(onlineData.activitys);
                            }
                            for (OnlineMallActivityEntity.Data.Activitys activitys : listActivity) {
                                //头部
                                data.add(activitys);
                                //顶部活动区
                                if (null != activitys.top) {
                                    data.addAll(activitys.top.list);
                                }
                                //底部活动区
                                if (null != activitys.bottom) {
                                    data.addAll(activitys.bottom.list);
                                }
                                //轮播
                                if (null != activitys.recommendBanner) {
                                    activitys.customRecommendBanner = new OnlineMallActivityEntity.Data.Activitys.CustomRecommendBanner();
                                    activitys.customRecommendBanner.recommendBanner.addAll(activitys.recommendBanner);
                                    data.add(activitys.customRecommendBanner);
                                }
                            }
                        }
                        getFirstPageData();


                    }
                });
        addSubscription(subscription);
    }

    /**
     * 设置头部banner--参考首页
     */
    private void setBanner(List<String> imageList) {
        if (imageList.size() == 0) {
            return;
        }
        banner.setImages(imageList)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                    }
                }).start();
    }

    /**
     * 获取为您推荐商品列表
     */
    private void getOnlineMallRecommendGoodsList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getOnlineMallRecommendGoodsList("recommend_goods_list", page, Constants.PAGE_COUNT_20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OnlineMallRecommendGoodsEntity>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        mallHomePagerAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(OnlineMallRecommendGoodsEntity goodsEntity) {
                        setData(goodsEntity);


                    }
                });
        addSubscription(subscription);

    }

    private void setData(OnlineMallRecommendGoodsEntity goodsEntity) {
        List<OnlineMallRecommendGoodsEntity.Data> listData = new ArrayList<>();
        if (goodsEntity.data != null && goodsEntity.data.size() > 0) {
            listData.addAll(goodsEntity.data);
        }
        data.addAll(listData);
        if (page == 0) {
            if (null == listData || listData.size() == 0) {
                mallHomePagerAdapter.loadMoreEnd();
            } else {
                mallHomePagerAdapter.loadMoreComplete();
            }
            mallHomePagerAdapter.setNewData(data);
        } else {
            if (goodsEntity.data.size() < Constants.PAGE_COUNT_20) {
                mallHomePagerAdapter.loadMoreEnd();
            } else {
                mallHomePagerAdapter.loadMoreComplete();
            }
            mallHomePagerAdapter.addData(data);
        }
    }

    private void initRecommendGoodsList() {
        page = 0;
        getOnlineMallRecommendGoodsList();
    }

    private void getFirstPageData() {
        page = 0;
        getOnlineMallRecommendGoodsList();
    }

    private void getNextPageData() {
        page++;
        getOnlineMallRecommendGoodsList();
    }
}
