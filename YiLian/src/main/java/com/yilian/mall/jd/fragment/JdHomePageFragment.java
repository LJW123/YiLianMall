package com.yilian.mall.jd.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.orhanobut.logger.Logger;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.jd.activity.JdHomePageActivity;
import com.yilian.mall.jd.adapter.JdHomeListAdapter;
import com.yilian.mall.jd.adapter.JdHomePageHeadIconAdapter;
import com.yilian.mall.jd.utils.JDConfigJumpToOtherPage;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDBannerEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsListEntity;
import com.yilian.networkingmodule.entity.jd.JDHomePageTop;
import com.yilian.networkingmodule.entity.jd.JDIconsEntity;
import com.yilian.networkingmodule.entity.jd.JDJumpEntity;
import com.yilian.networkingmodule.entity.jd.JD_ADEntity;
import com.yilian.networkingmodule.entity.jd.JdHomePageNoticeEntity;
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
 * 首页fragment
 *
 * @author Created by zhaiyaohua on 2018/5/22.
 */

public class JdHomePageFragment extends BaseFragment {
    /**
     * 首页头部icon列数
     */
    public static final int SPAN_COUNT = 5;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    /**
     * 头部部分
     */
    private Banner banner;
    private LinearLayout llIndicator;
    private RecyclerView headIconRecyclerView;
    private ImageView ivAd;
    private JdHomePageHeadIconAdapter headIconAdapter;
    private JdHomeListAdapter jdHomeListAdapter;
    private View headerView;
    private int page = 0;
    private List<JDGoodsAbstractInfoEntity> goodsList = new ArrayList<>();
    private JdHomePageActivity jdHomePageActivity;
    private ImageView jdIvClassify;
    private String jdAdUrl;
    private FrameLayout jdFlSearch;
    private VaryViewUtils varyViewUtils;
    private ImageView ivReturnTop;
    private GridLayoutManager manager;
    private View includeJdOuterNotice, includeJdInnerNotice;
    private int[] location = new int[2];

    private void initView(View view) {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setJdVaryViewHelper(R.id.vary_content, view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                getHeaderData();
                getFirstGoodsData();
            }
        });
        ivReturnTop = view.findViewById(R.id.iv_return_top);
        includeJdOuterNotice = view.findViewById(R.id.include_jd_outer_notice);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        manager = new GridLayoutManager(mContext, 2);

        recyclerView.setLayoutManager(manager);
        jdHomeListAdapter = new JdHomeListAdapter(goodsList);
        initHeaderView();
        recyclerView.setAdapter(jdHomeListAdapter);
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
        RxUtil.clicks(jdFlSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpJdActivityUtils.toJdGoodsSearchActivity(mContext);
            }
        });
        RxUtil.clicks(jdIvClassify, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jdHomePageActivity.switchFragment(jdHomePageActivity.jdGoodsClassifyFragment);
            }
        });
        RxUtil.clicks(ivAd, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!TextUtils.isEmpty(jdAdUrl)) {
                    JumpJdActivityUtils.toWebViewActivity(mContext, jdAdUrl);
                }

            }
        });
        headIconAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDJumpEntity entity = headIconAdapter.getItem(position);
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
        jdHomeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, recyclerView);
        jdHomeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (null != jdHomeListAdapter.getItem(position) && jdHomeListAdapter.getItem(position) instanceof JDGoodsAbstractInfoEntity) {
                    JDGoodsAbstractInfoEntity entity = (JDGoodsAbstractInfoEntity) jdHomeListAdapter.getItem(position);
                    JumpJdActivityUtils.toGoodsDetail(mContext, entity.skuId);
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
                //关闭提示 改变 （京东首页提示信息展示）值为 false
                PreferenceUtils.writeBoolConfig(Constants.KEY_JD_HOME_PAGE_NOTICE, false, mContext);
                includeJdOuterNotice.setVisibility(View.GONE);
                includeJdInnerNotice.setVisibility(View.GONE);
            }
        };
        RxUtil.clicks(includeJdOuterNotice.findViewById(R.id.fl_cancel), consumer);
        RxUtil.clicks(includeJdInnerNotice.findViewById(R.id.fl_cancel), consumer);
    }

    private void setIvReturnTopStatus() {
        int position = manager.findFirstVisibleItemPosition();
        if (position >= JdHomePageActivity.COUNT_RETURN_TOP) {
            ivReturnTop.setVisibility(View.VISIBLE);
        } else {
            ivReturnTop.setVisibility(View.GONE);
        }
    }

    private void setNoticeStatus() {
        if (PreferenceUtils.readBoolConfig(Constants.KEY_JD_HOME_PAGE_NOTICE, mContext)) {
            jdHomePageActivity.getJdHomePageTitleHeight();
            includeJdInnerNotice.getLocationOnScreen(location);
            int locationY = location[1];
            if (locationY - StatusBarUtils.getStatusBarHeight(mContext) <= jdHomePageActivity.titleHeight) {
                includeJdOuterNotice.setVisibility(View.VISIBLE);
                includeJdInnerNotice.setVisibility(View.GONE);
            } else {
                includeJdOuterNotice.setVisibility(View.GONE);
                includeJdInnerNotice.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.jd_header_home_page, null);
        includeJdInnerNotice = headerView.findViewById(R.id.include_jd_inner_notice);
        TextView tvTitle = headerView.findViewById(R.id.tv_title);
        tvTitle.setText("精品推荐");
        jdIvClassify = headerView.findViewById(R.id.jd_iv_classify);
        jdFlSearch = headerView.findViewById(R.id.jd_fl_search);
        banner = (Banner) headerView.findViewById(R.id.banner);
        llIndicator = (LinearLayout) headerView.findViewById(R.id.ll_indicator);
        headIconRecyclerView = (RecyclerView) headerView.findViewById(R.id.head_recycler_view);
        ivAd = (ImageView) headerView.findViewById(R.id.iv_ad);
        headIconAdapter = new JdHomePageHeadIconAdapter();
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
     * 获取首页为您推荐数据
     */
    private void getFirstGoodsData() {
        page = 0;
        getJdRecommendGoodsData();
    }

    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        page++;
        getJdRecommendGoodsData();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.jd_fragment_home_page, null);
        initView(view);
        initListener();
        jdHomePageActivity = (JdHomePageActivity) getActivity();
        return view;
    }

    @Override
    protected void loadData() {
        if (PreferenceUtils.readBoolConfig(Constants.KEY_JD_HOME_PAGE_NOTICE, mContext)) {
            getJdHomePageNotice();
        }
        getHeaderData();

        swipeRefreshLayout.setRefreshing(true);
    }

    @SuppressWarnings("unchecked")
    private void getJdHomePageNotice() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getJdHomePageNotice("jd_goods/jd_notice")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JdHomePageNoticeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JdHomePageNoticeEntity entity) {
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
                .getJdHomePageTop("jd_goods/jd_home_index_v1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDHomePageTop>() {
                    @Override
                    public void onCompleted() {
                        getFirstGoodsData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());

                    }

                    @Override
                    public void onNext(JDHomePageTop o) {
                        setHeaderData(o);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 首页为您推荐列表
     */
    @SuppressWarnings("unchecked")
    private void getJdRecommendGoodsData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getJDGoodsListData("jd_goods/jd_maybe_like", page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JDGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (page == Constants.PAGE_INDEX) {
                            varyViewUtils.showErrorView();
                        } else if (page > Constants.PAGE_INDEX) {
                            page--;
                        }
                        showToast(e.getMessage());
                        jdHomeListAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(JDGoodsListEntity o) {
                        if (null != o) {
                            setGoodsData(o);
                        }
                    }
                });
        addSubscription(subscription);

    }

    private void setHomePageNotice(JdHomePageNoticeEntity entity) {
        if (null == entity.data || entity.data.size() <= 0) {
            PreferenceUtils.writeBoolConfig(Constants.KEY_JD_HOME_PAGE_NOTICE, false, mContext);
        } else {
            TextView tvJdInnerNotice = includeJdInnerNotice.findViewById(R.id.tv_notice);
            TextView tvJdOuterNotice = includeJdOuterNotice.findViewById(R.id.tv_notice);
            tvJdInnerNotice.setText(entity.data.get(0).title);
            tvJdOuterNotice.setText(entity.data.get(0).title);
        }
    }

    private void setHeaderData(JDHomePageTop topDatas) {
        initTopBanner(topDatas.data.banner);
        initIconData(topDatas.data.icon);
        initAd(topDatas.data.adv);
    }

    private void setGoodsData(JDGoodsListEntity entity) {
        if (page <= 0) {
            if (entity.getData().size() == 0) {
                jdHomeListAdapter.loadMoreEnd();
                varyViewUtils.showEmptyView();
            } else {
                varyViewUtils.showDataView();
                goodsList.clear();
                goodsList.addAll(entity.getData());
                jdHomeListAdapter.setNewData(goodsList);
                if (entity.getData().size() < Constants.PAGE_COUNT) {
                    jdHomeListAdapter.loadMoreEnd();
                } else {
                    jdHomeListAdapter.loadMoreComplete();
                }
                if (jdHomeListAdapter.getHeaderLayoutCount() <= 0) {
                    jdHomeListAdapter.addHeaderView(headerView);
                }
            }

        } else {
            goodsList.addAll(entity.getData());
            jdHomeListAdapter.addData(entity.getData());
            if (entity.getData().size() < Constants.PAGE_COUNT) {
                jdHomeListAdapter.loadMoreEnd();
            } else {
                jdHomeListAdapter.loadMoreComplete();
            }

        }
    }

    /**
     * 初始化banner
     *
     * @param bannerEntityList
     */
    private void initTopBanner(List<JDBannerEntity> bannerEntityList) {
        if (null == bannerEntityList || bannerEntityList.size() <= 0) {
            banner.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
            List<String> imageList = new ArrayList<>();
            for (JDBannerEntity entity : bannerEntityList) {
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
                            JDBannerEntity entity = bannerEntityList.get(position);
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
    private void initIconData(List<JDIconsEntity> icon) {
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
    private void initAd(List<JD_ADEntity> adv) {
        if (null == adv || adv.size() <= 0) {
            ivAd.setVisibility(View.GONE);
            jdAdUrl = null;
        } else {
            ivAd.setVisibility(View.VISIBLE);
            GlideUtil.showImage(mContext, adv.get(0).img, ivAd);
            jdAdUrl = adv.get(0).content;
        }
    }

    /**
     * 京东通用跳转
     * type:  3 切换至京东首页品牌精选  6 切换至京东首页分类页面
     *
     * @param type
     * @param content
     */
    private void jumpToOtherPage(int type, String content) {
        JDConfigJumpToOtherPage.getInstance(mContext).jumpToOtherPage(type, content);
    }
}
