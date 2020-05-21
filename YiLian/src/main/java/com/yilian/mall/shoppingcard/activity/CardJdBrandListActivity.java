package com.yilian.mall.shoppingcard.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.jd.adapter.JdBrandSelectedAdapter;
import com.yilian.mall.jd.utils.JDConfigJumpToOtherPage;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.view.NestRecyclerView.FeedRootRecyclerView;
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
 * 购物卡 京东品牌精选
 *
 * @author Created by Zg on 2018/77/17.
 */

public class CardJdBrandListActivity extends BaseFragmentActivity {
    private static final int PAGE_COUNT_10 = 10;

    private int page = Constants.PAGE_INDEX;//页数
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivTitle;

    private SwipeRefreshLayout jdSwipeRefreshLayout;
    private FeedRootRecyclerView jdRecyclerView;
    private View headView;
    private JdBrandSelectedAdapter jdBrandSelectedAdapter;

    private VaryViewUtils varyViewUtils;
    private ImageView ivReturnTop;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_jd_brand_list);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setJdVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                initData();
            }
        });
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivTitle = findViewById(R.id.iv_title);

        ivReturnTop = findViewById(R.id.iv_return_top);
        jdSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.jd_swipe_refresh_layout);
        jdSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.red));
        jdRecyclerView = findViewById(R.id.jd_recycler_view);
        gridLayoutManager = new GridLayoutManager(context, 1);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(context, DPXUnitUtil.dp2px(context, 8), R.color.bg_gray);
        jdRecyclerView.addItemDecoration(decor);
        jdRecyclerView.setLayoutManager(gridLayoutManager);
        jdBrandSelectedAdapter = new JdBrandSelectedAdapter(JumpToOtherPageUtil.JD_GOODS_TYPE_CARD);
        jdRecyclerView.setAdapter(jdBrandSelectedAdapter);
        headView = View.inflate(context, R.layout.card_activity_jd_brand_list_header, null);
        jdSwipeRefreshLayout.setRefreshing(true);
    }

    private void initData() {
        tvTitle.setVisibility(View.GONE);
        ivTitle.setImageResource(R.mipmap.jd_icon_title_brand_seleted);
        ivTitle.setVisibility(View.VISIBLE);
        getFirstData();
    }

    protected void getBrandList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCardJdBrandList("shoppingCardBrandList", page, PAGE_COUNT_10)
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

    private void setData(JdGoodsBrandSelectedEntity entity) {
        if (jdBrandSelectedAdapter.getHeaderLayoutCount() <= 0) {
            jdBrandSelectedAdapter.addHeaderView(headView);
        }
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


    private void initListener() {
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
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
                        JumpCardActivityUtils.toCardJdBrandGoodsList(context, jdBrandSelectedAdapter.getItem(position).brandName);
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
        getBrandList();
    }

    private void getFirstData() {
//        startMyDialog();
        page = 0;
        getBrandList();
    }


}
