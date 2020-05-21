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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.suning.adapter.SnBrandSelectedAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.suning.utils.SnConfigJumpToOtherPage;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.suning.SnGoodsBrandSelectedEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 苏宁首页精选
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class SnBrandSelectedFragment extends JPBaseFragment {
    private static final int PAGE_COUNT_10 = 10;
    private SwipeRefreshLayout snSwipeRefreshLayout;
    private RecyclerView snRecyclerView;
    private View headView;
    private SnBrandSelectedAdapter snBrandSelectedAdapter;
    private View snFlSearch;
    private Banner snTopBanner;
    private VaryViewUtils varyViewUtils;
    private int page = 0;
    private ImageView ivReturnTop;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.sn_fragment_goods_brand_selected, null);
        initView(view);
        initListener();
        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnBrandSelectdList("suning_goods/suning_brand_list", page, PAGE_COUNT_10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnGoodsBrandSelectedEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        snSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        snSwipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        if (page <= 0) {
                            varyViewUtils.showErrorView();
                        } else {
                            page--;
                        }
                        snBrandSelectedAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(SnGoodsBrandSelectedEntity entity) {
                        if (null != entity) {
                            varyViewUtils.showDataView();
                            setData(entity);
                        }
                    }
                });
        addSubscription(subscription);

    }

    private void initView(View view) {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setSnVaryViewHelper(R.id.vary_content, view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                loadData();
            }
        });
        ivReturnTop = view.findViewById(R.id.iv_return_top);
        snSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sn_swipe_refresh_layout);
        snSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
        snRecyclerView = (RecyclerView) view.findViewById(R.id.sn_recycler_view);
        gridLayoutManager = new GridLayoutManager(mContext, 1);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.bg_gray);
        snRecyclerView.addItemDecoration(decor);
        snRecyclerView.setLayoutManager(gridLayoutManager);
        snBrandSelectedAdapter = new SnBrandSelectedAdapter();
        snRecyclerView.setAdapter(snBrandSelectedAdapter);
        initHeadView();
        snSwipeRefreshLayout.setRefreshing(true);
    }

    private void initListener() {
        RxUtil.clicks(snFlSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpSnActivityUtils.toSnGoodsSearch(mContext);
            }
        });
        snSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });
        snBrandSelectedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.sn_more:
                        JumpSnActivityUtils.toSnBrandGoodsList(mContext, snBrandSelectedAdapter.getItem(position).brand);
                    default:
                        break;
                }

            }
        });
        snBrandSelectedAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextData();
            }
        }, snRecyclerView);
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                snRecyclerView.smoothScrollToPosition(0);
            }
        });
        snRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int position = gridLayoutManager.findFirstVisibleItemPosition();
//                if (position > JdHomePageActivity.COUNT_RETURN_TOP) {
//                    ivReturnTop.setVisibility(View.VISIBLE);
//                } else {
//                    ivReturnTop.setVisibility(View.GONE);
//                }
            }
        });
    }

    private void getNextData() {
        page++;
        loadData();
    }

    private void initHeadView() {
        headView = View.inflate(mContext, R.layout.sn_head_goods_brand_seleted, null);
        snFlSearch = headView.findViewById(R.id.sn_fl_search);
        headView.findViewById(R.id.sn_tv_search).setVisibility(View.VISIBLE);
        snTopBanner = headView.findViewById(R.id.sn_banner);
    }

    private void getFirstData() {
        startMyDialog();
        page = 0;
        loadData();
    }

    private void setData(SnGoodsBrandSelectedEntity entity) {
        if (snBrandSelectedAdapter.getHeaderLayoutCount() <= 0) {
            snBrandSelectedAdapter.addHeaderView(headView);
        }
        initTopBanner(entity);
        if (page <= 0) {
            if (entity.getData().size() <= 0) {
                varyViewUtils.showEmptyView();
                snBrandSelectedAdapter.loadMoreEnd();
            } else {
                snBrandSelectedAdapter.setNewData(entity.getData());
                if (entity.getData().size() < PAGE_COUNT_10) {
                    snBrandSelectedAdapter.loadMoreEnd();
                } else {
                    snBrandSelectedAdapter.loadMoreComplete();
                }
            }
        } else {
            snBrandSelectedAdapter.addData(entity.getData());
            if (entity.getData().size() < PAGE_COUNT_10) {
                snBrandSelectedAdapter.loadMoreEnd();
            } else {
                snBrandSelectedAdapter.loadMoreComplete();
            }

        }
    }

    /**
     * 初始化banner
     */
    private void initTopBanner(SnGoodsBrandSelectedEntity entity) {
        if (null != entity.banner) {
            List<String> imgList = new ArrayList<>();
            imgList.add(entity.banner.img);
            snTopBanner.setImages(imgList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            if (TextUtils.isEmpty(entity.banner.type)) {
                                SnConfigJumpToOtherPage.getInstance(mContext).jumpToOtherPage(Integer.valueOf(entity.banner.type), entity.banner.content);
                            }
                        }
                    }).start();
        }
    }


}
