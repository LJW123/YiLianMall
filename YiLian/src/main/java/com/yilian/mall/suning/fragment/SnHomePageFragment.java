package com.yilian.mall.suning.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.adapter.SnHomeListAdapter;
import com.yilian.mall.suning.adapter.SnHomePageHeadIconAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.suning.utils.SnConfigJumpToOtherPage;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.suning.SnAdvEntity;
import com.yilian.networkingmodule.entity.suning.SnBannerEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsListEntity;
import com.yilian.networkingmodule.entity.suning.SnHomePageIcon;
import com.yilian.networkingmodule.entity.suning.SnHomePageNoticeEntity;
import com.yilian.networkingmodule.entity.suning.SnHomePageTop;
import com.yilian.networkingmodule.entity.suning.SnJumpEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 苏宁首页fragment
 *
 * @author Created by zhaiyaohua on 2018/7/14
 */

public class SnHomePageFragment extends JPBaseFragment {
    /**
     * 首页头部icon列数
     */
    public static final int SPAN_COUNT = 4;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    /**
     * 头部部分
     */
    private Banner banner;
    private RecyclerView headIconRecyclerView;
    private ImageView ivAd;
    private SnHomePageHeadIconAdapter headIconAdapter;
    private SnHomeListAdapter snHomeListAdapter;
    private View headerView;
    private int page =  Constants.PAGE_INDEX;
    private List<SnGoodsAbstractInfoEntity> goodsList = new ArrayList<>();
    private SnHomePageActivity snHomePageActivity;
    private String snAdUrl;
    private VaryViewUtils varyViewUtils;
    private ImageView ivReturnTop;
    private GridLayoutManager manager;
    private View includeSnOuterNotice, includeSnInnerNotice;
    private int[] location = new int[2];
    private View includeSnSearch;

    private void initView(View view) {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setSnVaryViewHelper(R.id.vary_content, view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                getHeaderData();
                getFirstGoodsData();
            }
        });
        ivReturnTop = view.findViewById(R.id.iv_return_top);
        includeSnOuterNotice = view.findViewById(R.id.include_sn_outer_notice);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        manager = new GridLayoutManager(mContext, 2);

        recyclerView.setLayoutManager(manager);
        snHomeListAdapter = new SnHomeListAdapter(goodsList);
        initHeaderView();
        recyclerView.setAdapter(snHomeListAdapter);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg, true);
        recyclerView.addItemDecoration(decor);
    }

    private void initListener() {
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        RxUtil.clicks(includeSnSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpSnActivityUtils.toSnGoodsSearch(mContext);
            }
        });
        RxUtil.clicks(ivAd, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!TextUtils.isEmpty(snAdUrl)) {
                    JumpSnActivityUtils.toWebViewActivity(mContext, snAdUrl);
                }

            }
        });
        headIconAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnJumpEntity entity = headIconAdapter.getItem(position);
                int type = entity.type;
                String content = entity.content;
                jumpToOtherPage(type, content);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHeaderData();
                getFirstGoodsData();
            }
        });
        snHomeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, recyclerView);
        snHomeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (null != snHomeListAdapter.getItem(position) && snHomeListAdapter.getItem(position) instanceof SnGoodsAbstractInfoEntity) {
                    SnGoodsAbstractInfoEntity entity = (SnGoodsAbstractInfoEntity) snHomeListAdapter.getItem(position);
                    JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, entity.skuId);
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setIvReturnTopStatus();
                setNoticeStatus();
            }
        });
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                //关闭提示 改变 （苏宁首页提示信息展示）值为 false
                PreferenceUtils.writeBoolConfig(Constants.KEY_SN_HOME_PAGE_NOTICE, false, mContext);
                includeSnOuterNotice.setVisibility(View.GONE);
                includeSnInnerNotice.setVisibility(View.GONE);
            }
        };
        RxUtil.clicks(includeSnOuterNotice.findViewById(R.id.fl_cancel), consumer);
        RxUtil.clicks(includeSnInnerNotice.findViewById(R.id.fl_cancel), consumer);
    }

    private void setIvReturnTopStatus() {
        int position = manager.findFirstVisibleItemPosition();
        if (position >= SnHomePageActivity.COUNT_RETURN_TOP) {
            ivReturnTop.setVisibility(View.VISIBLE);
        } else {
            ivReturnTop.setVisibility(View.GONE);
        }
    }

    private void setNoticeStatus() {
        if (PreferenceUtils.readBoolConfig(Constants.KEY_SN_HOME_PAGE_NOTICE, mContext)) {
            snHomePageActivity.getJdHomePageTitleHeight();
            includeSnInnerNotice.getLocationOnScreen(location);
            int locationY = location[1];
            if (locationY - StatusBarUtils.getStatusBarHeight(mContext) <= snHomePageActivity.titleHeight) {
                includeSnOuterNotice.setVisibility(View.VISIBLE);
                includeSnInnerNotice.setVisibility(View.GONE);
            } else {
                includeSnOuterNotice.setVisibility(View.GONE);
                includeSnInnerNotice.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.sn_header_home_page, null);
        includeSnSearch = headerView.findViewById(R.id.include_sn_search);
        includeSnInnerNotice = headerView.findViewById(R.id.include_sn_inner_notice);
        banner = (Banner) headerView.findViewById(R.id.banner);
        headIconRecyclerView = (RecyclerView) headerView.findViewById(R.id.head_recycler_view);
        ivAd = (ImageView) headerView.findViewById(R.id.iv_ad);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivAd.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth(mContext);
        params.height = (int)(ScreenUtils.getScreenWidth(mContext)/5);
        ivAd.setLayoutParams(params);

        headIconAdapter = new SnHomePageHeadIconAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, SPAN_COUNT) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        headIconRecyclerView.setLayoutManager(gridLayoutManager);
        headIconRecyclerView.setAdapter(headIconAdapter);
    }

    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        getSnRecommendGoodsData();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.sn_fragment_home_page, null);
        initView(view);
        initListener();
        snHomePageActivity = (SnHomePageActivity) getActivity();
        return view;
    }

    @Override
    protected void loadData() {
        if (PreferenceUtils.readBoolConfig(Constants.KEY_SN_HOME_PAGE_NOTICE, mContext)) {
            getSnHomePageNotice();
        }
        getHeaderData();
        swipeRefreshLayout.setRefreshing(true);
    }

    @SuppressWarnings("unchecked")
    private void getSnHomePageNotice() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnHomePageNotice("suning_goods/suning_notice")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnHomePageNoticeEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SnHomePageNoticeEntity entity) {
                        setHomePageNotice(entity);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取列表头部数据
     */
    @SuppressWarnings("unchecked")
    private void getHeaderData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnHomePageTop("suning_goods/suning_home_index")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnHomePageTop>() {
                    @Override
                    public void onCompleted() {
                        getFirstGoodsData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());

                    }

                    @Override
                    public void onNext(SnHomePageTop o) {
                        setHeaderData(o);
                    }
                });
        addSubscription(subscription);
    }

    private void setHomePageNotice(SnHomePageNoticeEntity entity) {
        if (null == entity.data || entity.data.size() <= 0) {
            PreferenceUtils.writeBoolConfig(Constants.KEY_SN_HOME_PAGE_NOTICE, false, mContext);
        } else {
            TextView tvJdInnerNotice = includeSnInnerNotice.findViewById(R.id.tv_notice);
            TextView tvJdOuterNotice = includeSnOuterNotice.findViewById(R.id.tv_notice);
            tvJdInnerNotice.setText(entity.data.get(0).content);
            tvJdOuterNotice.setText(entity.data.get(0).content);
        }
    }

    /**
     * 获取首页为您推荐数据
     */
    private void getFirstGoodsData() {
        page =  Constants.PAGE_INDEX;
        getSnRecommendGoodsData();
    }

    private void setHeaderData(SnHomePageTop topDatas) {
        initTopBanner(topDatas.data.banner);
        initIconData(topDatas.data.icon);
        initAd(topDatas.data.adv);
    }

    /**
     * 首页为您推荐列表
     */
    @SuppressWarnings("unchecked")
    private void getSnRecommendGoodsData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnGoodsListData("suning_goods/suning_maybe_like", page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SnGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (page == Constants.PAGE_INDEX) {
                            varyViewUtils.showErrorView();
                        }
                        showToast(e.getMessage());
                        snHomeListAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(SnGoodsListEntity o) {
                        if (null != o) {
                            setGoodsData(o);
                        }
                    }
                });
        addSubscription(subscription);

    }

    /**
     * 初始化banner
     *
     * @param bannerEntityList
     */
    private void initTopBanner(List<SnBannerEntity> bannerEntityList) {
        if (null == bannerEntityList || bannerEntityList.size() <= 0) {
            banner.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
            List<String> imageList = new ArrayList<>();
            for (SnBannerEntity entity : bannerEntityList) {
                String url = entity.img;
                Logger.i("zyh +" + url);
                imageList.add(url);

            }
            banner.setImages(imageList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            SnBannerEntity entity = bannerEntityList.get(position);
                            jumpToOtherPage(entity.type, entity.content);
                        }
                    }).start();
        }

    }

    /**
     * 头部的icon列表
     *
     * @param icon
     */
    private void initIconData(List<SnHomePageIcon> icon) {
        if (null == icon || icon.size() <= 0) {
            headIconRecyclerView.setVisibility(View.GONE);
        } else {
            headIconRecyclerView.setVisibility(View.VISIBLE);
            headIconAdapter.setNewData(icon);
        }

    }

    /**
     * 初始化广告位
     *
     * @param adv
     */
    private void initAd(List<SnAdvEntity> adv) {
        if (null == adv || adv.size() <= 0) {
            ivAd.setVisibility(View.GONE);
            snAdUrl = null;
        } else {
            ivAd.setVisibility(View.VISIBLE);
            GlideUtil.showImage(mContext, adv.get(0).img, ivAd);
            snAdUrl = adv.get(0).content;
        }
    }

    private void setGoodsData(SnGoodsListEntity entity) {
        if (page <= 0) {
            if (entity.getData().size() == 0) {
                snHomeListAdapter.loadMoreEnd();
                varyViewUtils.showEmptyView();
            } else {
                varyViewUtils.showDataView();
                goodsList.clear();
                goodsList.addAll(entity.getData());
                snHomeListAdapter.setNewData(goodsList);
                if (entity.getData().size() < Constants.PAGE_COUNT) {
                    snHomeListAdapter.loadMoreEnd();
                } else {
                    snHomeListAdapter.loadMoreComplete();
                }
                if (snHomeListAdapter.getHeaderLayoutCount() <= 0) {
                    snHomeListAdapter.addHeaderView(headerView);
                }
                page = entity.page;
            }

        } else {
            goodsList.addAll(entity.getData());
            snHomeListAdapter.addData(entity.getData());
            if (entity.getData().size() < Constants.PAGE_COUNT) {
                snHomeListAdapter.loadMoreEnd();
            } else {
                snHomeListAdapter.loadMoreComplete();
            }
            page = entity.page;

        }
    }

    /**
     * 苏宁通用跳转
     *
     * @param type
     * @param content
     */
    private void jumpToOtherPage(int type, String content) {
        SnConfigJumpToOtherPage.getInstance(mContext).jumpToOtherPage(type, content);
    }
}
