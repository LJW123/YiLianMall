package com.yilian.mall.jd.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.jd.adapter.JdBrandSelectedAdapter;
import com.yilian.mall.jd.utils.JDConfigJumpToOtherPage;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JdGoodsBrandSelectedEntity;
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
 * 京东首页精选
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class JdBrandSelectedFragment extends BaseFragment {
    private static final int PAGE_COUNT_10 = 10;
    private SwipeRefreshLayout jdSwipeRefreshLayout;
    private RecyclerView jdRecyclerView;
    private View headView;
    private JdBrandSelectedAdapter jdBrandSelectedAdapter;
    private SwipeRefreshLayout JdSwipeRefreshLayout;
    private View jdFlSearch;
    private Banner jdTopBanner;
    private VaryViewUtils varyViewUtils;
    private int page = 0;
    private ImageView ivReturnTop;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.jd_fragment_goods_brand_selected, null);
        initView(view);
        initListener();
        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getBrandSelectdList("jd_goods/jd_brand_list", page, PAGE_COUNT_10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JdGoodsBrandSelectedEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        jdSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        jdSwipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        if (page <= 0) {
                            varyViewUtils.showErrorView();
                        } else {
                            page--;
                        }
                        jdBrandSelectedAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(JdGoodsBrandSelectedEntity entity) {
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
        varyViewUtils.setJdVaryViewHelper(R.id.vary_content, view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                loadData();
            }
        });
        ivReturnTop = view.findViewById(R.id.iv_return_top);
        jdSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.jd_swipe_refresh_layout);
        jdSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
        jdRecyclerView = (RecyclerView) view.findViewById(R.id.jd_recycler_view);
        gridLayoutManager = new GridLayoutManager(mContext, 1);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.bg_gray);
        jdRecyclerView.addItemDecoration(decor);
        jdRecyclerView.setLayoutManager(gridLayoutManager);
        jdBrandSelectedAdapter = new JdBrandSelectedAdapter(JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON);
        jdRecyclerView.setAdapter(jdBrandSelectedAdapter);
        initHeadView();
        jdSwipeRefreshLayout.setRefreshing(true);
    }

    private void initListener() {
        RxUtil.clicks(jdFlSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpJdActivityUtils.toJdGoodsSearchActivity(mContext);
            }
        });
        jdSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });
        jdBrandSelectedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.jd_option_brand:
                        JumpJdActivityUtils.toJdBrandGoodsListActivity(mContext, jdBrandSelectedAdapter.getItem(position).brandName);
                    default:
                        break;
                }

            }
        });
        jdBrandSelectedAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextData();
            }
        }, jdRecyclerView);
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jdRecyclerView.smoothScrollToPosition(0);
            }
        });
        jdRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        headView = View.inflate(mContext, R.layout.jd_head_goods_brand_seleted, null);
        jdFlSearch = headView.findViewById(R.id.jd_fl_search);
        jdTopBanner = headView.findViewById(R.id.jd_banner);
    }

    private void getFirstData() {
        startMyDialog();
        page = 0;
        loadData();
    }

    private void setData(JdGoodsBrandSelectedEntity entity) {
        if (jdBrandSelectedAdapter.getHeaderLayoutCount() <= 0) {
            jdBrandSelectedAdapter.addHeaderView(headView);
        }
        initTopBanner(entity);
        if (page <= 0) {
            if (entity.getData().size() <= 0) {
                varyViewUtils.showEmptyView();
                jdBrandSelectedAdapter.loadMoreEnd();
            } else {
                jdBrandSelectedAdapter.setNewData(entity.getData());
                if (entity.getData().size() < PAGE_COUNT_10) {
                    jdBrandSelectedAdapter.loadMoreEnd();
                } else {
                    jdBrandSelectedAdapter.loadMoreComplete();
                }

            }
        } else {
            jdBrandSelectedAdapter.addData(entity.getData());
            if (entity.getData().size() < PAGE_COUNT_10) {
                jdBrandSelectedAdapter.loadMoreEnd();
            } else {
                jdBrandSelectedAdapter.loadMoreComplete();
            }

        }
    }

    /**
     * 初始化banner
     */
    private void initTopBanner(JdGoodsBrandSelectedEntity entity) {
        if (null != entity.banner1) {
            List<String> imgList = new ArrayList<>();
            imgList.add(entity.banner1.img);
            jdTopBanner.setImages(imgList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            JDConfigJumpToOtherPage.getInstance(mContext).jumpToOtherPage(entity.banner1.type, entity.banner1.content);
                        }
                    }).start();
        }
    }


}
