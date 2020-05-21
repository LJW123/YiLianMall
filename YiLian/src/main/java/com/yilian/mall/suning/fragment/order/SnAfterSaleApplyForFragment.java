package com.yilian.mall.suning.fragment.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.adapter.SnAfterSaleApplyForAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.suning.utils.SnConstantUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplyForListEntity;
import com.yilian.networkingmodule.entity.suning.snMultiItem.SnAfterSaleListLayoutType;
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
 * @author Created by Zg on 2018/7/31.
 */
@SuppressLint("ValidFragment")
public class SnAfterSaleApplyForFragment extends JPBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    private VaryViewUtils varyViewUtils;

    private int page = Constants.PAGE_INDEX;//页数

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SnAfterSaleApplyForAdapter mAdapter;


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
        varyViewUtils.setVaryViewBySnOrderList(R.id.vary_content, rootView, new RefreshClickListener(), "没有售后申请的订单哦", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_HOME, null);
            }
        });

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SnAfterSaleApplyForAdapter();
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

//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                int itemViewType = adapter.getItemViewType(position);
//                switch (itemViewType) {
//                    case SnAfterSaleListLayoutType.goods:
//                        //商品信息
//                        SnAfterSaleApplyForListEntity.GoodsList bean = (SnAfterSaleApplyForListEntity.GoodsList) adapter.getItem(position);
//                        JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, bean.getSkuId());
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case SnAfterSaleListLayoutType.goods:
                        //商品信息
                        SnAfterSaleApplyForListEntity.GoodsList bean = (SnAfterSaleApplyForListEntity.GoodsList) adapter.getItem(position);
                        switch (view.getId()) {
                            case R.id.tv_apply_for:
                                //申请售后
                                applyAfterSales(bean);
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
     * 申请售后
     */
    private void applyAfterSales(SnAfterSaleApplyForListEntity.GoodsList bean) {
        //是否厂送商品 01-是；02-否
        if ("02".equals(bean.getIsFactorySend())) {
            JumpSnActivityUtils.toSnAfterSaleApplyFor(mContext, bean.getSnOrderId(), bean.getOrderSnPrice(), bean.getCoupon(),
                    bean.getSkuId(), bean.getSkuPic(), bean.getSkuName(), bean.getSnPrice(), bean.getSkuNum());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("请联系苏宁客服电话" + SnConstantUtils.SN_SERVICE_TEL + "完成退货退款流程。");
            builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("打电话", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + SnConstantUtils.SN_SERVICE_TEL);
                    intent.setData(data);
                    startActivity(intent);
                }
            });
            builder.create();
            builder.setCancelable(false);
            builder.show();
        }
    }

    /**
     * 列表
     */
    private void getOrdersList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getSnAfterSaleApplyForList("suning_aftersale/suning_aftersale_list", page, Constants.PAGE_COUNT, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnAfterSaleApplyForListEntity>() {
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
                    public void onNext(SnAfterSaleApplyForListEntity entity) {
                        List<SnAfterSaleApplyForListEntity.DataBean> data = entity.data;
                        if (page == Constants.PAGE_INDEX) {
                            if (data.size() <= 0) {
                                varyViewUtils.showEmptyView();
                            } else {
                                ArrayList<MultiItemEntity> itemDatas = new ArrayList<>();
                                for (SnAfterSaleApplyForListEntity.DataBean dataBean : data) {
                                    itemDatas.add(dataBean);
                                    for (SnAfterSaleApplyForListEntity.GoodsList item : dataBean.getOrderGoods()) {
                                        item.setOrderSnPrice(dataBean.getOrderSnPrice());
                                        item.setCoupon(dataBean.getCoupon());
                                    }
                                    itemDatas.addAll(dataBean.getOrderGoods());
                                }
                                mAdapter.setNewData(itemDatas);
                                varyViewUtils.showDataView();
                            }
                        } else {
                            if (data.size() <= 0 || data.size() < Constants.PAGE_COUNT) {
                                mAdapter.loadMoreEnd();
                            } else {
                                ArrayList<MultiItemEntity> itemDatas = new ArrayList<>();
                                for (SnAfterSaleApplyForListEntity.DataBean dataBean : data) {
                                    itemDatas.add(dataBean);
                                    for (SnAfterSaleApplyForListEntity.GoodsList item : dataBean.getOrderGoods()) {
                                        item.setOrderSnPrice(dataBean.getOrderSnPrice());
                                        item.setCoupon(dataBean.getCoupon());
                                    }
                                    itemDatas.addAll(dataBean.getOrderGoods());
                                }
                                mAdapter.addData(itemDatas);
                            }
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

    @Override
    public void onLoadMoreRequested() {
        Logger.i("getShopsList：onLoadMoreRequested");
        getNextPageData();
        swipeRefreshLayout.setEnabled(false);
    }    //按钮的点击事件

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


}