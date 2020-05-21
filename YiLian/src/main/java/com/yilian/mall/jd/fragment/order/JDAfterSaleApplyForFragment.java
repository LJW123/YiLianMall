package com.yilian.mall.jd.fragment.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.jd.adapter.JDAfterSaleApplyForAdapter;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForListEntity;
import com.yilian.networkingmodule.entity.jd.JDCheckAvailableAfterSaleEntity;
import com.yilian.networkingmodule.entity.jd.jdMultiItem.JdAfterSaleListLayoutType;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 售后申请
 *
 * @author Created by Zg on 2018/5/22.
 */
@SuppressLint("ValidFragment")
public class JDAfterSaleApplyForFragment extends JPBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    private VaryViewUtils varyViewUtils;

    private int page = Constants.PAGE_INDEX;//页数

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private JDAfterSaleApplyForAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.jd_fragment_order_common, null);
        }
        initView();
        initListener();
        return rootView;
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewByJDOrderList(R.id.vary_content, rootView, new RefreshClickListener());

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new JDAfterSaleApplyForAdapter();
        mAdapter.bindToRecyclerView(mRecyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);

    }

    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
                mAdapter.setEnableLoadMore(false);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case JdAfterSaleListLayoutType.goods:
                        //商品信息
                        JDAfterSaleApplyForListEntity.GoodsList bean = (JDAfterSaleApplyForListEntity.GoodsList) adapter.getItem(position);
                        JumpJdActivityUtils.toGoodsDetail(mContext, bean.getSkuId());
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case JdAfterSaleListLayoutType.goods:
                        //商品信息
                        JDAfterSaleApplyForListEntity.GoodsList bean = (JDAfterSaleApplyForListEntity.GoodsList) adapter.getItem(position);
                        switch (view.getId()) {
                            case R.id.tv_apply_for:
                                //申请售后
                                checkAvailableAftersale(bean.getJdOrderId(), bean.getSkuId(), bean.getSkuNum());
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }


            }
        });

    }

    private void getFirstPageData() {
        page = Constants.PAGE_INDEX;
        getOrdersList();
    }

    /**
     * 京东政企判断订单中的商品是否可以申请售后
     *
     * @param jdOrderId 京东订单号
     */
    private void checkAvailableAftersale(String jdOrderId, String skuId, String skuNum) {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                jdCheckAvailableAftersaler("jd_aftersale/jd_check_available_aftersale", jdOrderId, skuId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDCheckAvailableAfterSaleEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDCheckAvailableAfterSaleEntity bean) {
                        if (bean.num > 0) {
                            JumpJdActivityUtils.toJDAfterSaleApplyFor(mContext, jdOrderId, skuId, skuNum);
                        } else {
                            showToast("暂不可申请售后");
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 列表
     */
    private void getOrdersList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getJDAfterSaleApplyForList("jd_aftersale/js_aftersale_list", page, Constants.PAGE_COUNT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAfterSaleApplyForListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == Constants.PAGE_INDEX) {
                            varyViewUtils.showErrorView();
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDAfterSaleApplyForListEntity entity) {
                        List<JDAfterSaleApplyForListEntity.DataBean> data = entity.data;
                        if (page == Constants.PAGE_INDEX) {
                            if (data.size() <= 0) {
                                varyViewUtils.showEmptyView();
                            } else {
                                ArrayList<MultiItemEntity> itemDatas = new ArrayList<>();
                                for (JDAfterSaleApplyForListEntity.DataBean dataBean : data) {
                                    itemDatas.add(dataBean);
                                    itemDatas.addAll(dataBean.getGoodsList());
                                }
                                mAdapter.setNewData(itemDatas);
                                varyViewUtils.showDataView();
                            }
                        } else {
                                ArrayList<MultiItemEntity> itemDatas = new ArrayList<>();
                                for (JDAfterSaleApplyForListEntity.DataBean dataBean : data) {
                                    itemDatas.add(dataBean);
                                    itemDatas.addAll(dataBean.getGoodsList());
                                }
                                mAdapter.addData(itemDatas);
                        }
                        if (data.size() < Constants.PAGE_COUNT) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    protected void loadData() {
        if (varyViewUtils != null) {
            varyViewUtils.showLoadingView();
        }
        getFirstPageData();
    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.head_ll_praise://热帖 点赞
//                setFirstHotPostFabulous(firstHotPost.getPostID());
//                break;
//            default:
//                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        Logger.i("getShopsList：onLoadMoreRequested");
        getNextPageData();
        swipeRefreshLayout.setEnabled(false);
    }

    private void getNextPageData() {
        page++;
        getOrdersList();
    }

    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrdersList();
                }
            }, 1000);
        }
    }
}