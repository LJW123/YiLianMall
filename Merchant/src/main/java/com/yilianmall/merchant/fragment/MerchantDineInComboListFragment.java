package com.yilianmall.merchant.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.ComboOrderListDineInEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.activity.MerchantComboOrderDetailActivity2;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantDineInComboListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyAdapter myAdapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.merchant_fragment_delivery_combo_list, container, false);
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initData() {
        getComboOrderManagerList();
    }

    private void initListener() {
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ComboOrderListDineInEntity.ListBean item = (ComboOrderListDineInEntity.ListBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, MerchantComboOrderDetailActivity2.class);
                intent.putExtra("order_id", item.comboOrderId);
                intent.putExtra("order_type", 1);
                startActivity(intent);
            }
        });
        myAdapter.setOnLoadMoreListener(this, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        page = 0;
        getComboOrderManagerList();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getComboOrderManagerList();
    }

    class MyAdapter extends BaseQuickAdapter<ComboOrderListDineInEntity.ListBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ComboOrderListDineInEntity.ListBean item) {
            helper.setText(R.id.merchant_combo_name, item.packageName);
            helper.setText(R.id.merchant_combo_time, DateUtils.timeStampToStr(item.buyDate));
            helper.setText(R.id.merchant_combo_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.price)));
            helper.getView(R.id.merchant_order_status).setVisibility(View.INVISIBLE);
            helper.setText(R.id.merchant_order_num, "×" + item.num);
            helper.getView(R.id.merchant_user_address).setVisibility(View.GONE);
            TextView tvOrderCount = helper.getView(R.id.merchant_order_num);
            tvOrderCount.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.library_module_gray_arrow), null);
            tvOrderCount.setCompoundDrawablePadding(10);
            GlideUtil.showImageWithSuffix(mContext, item.packageIcon, (ImageView) helper.getView(R.id.merchant_combo_imageview));
            int unusedCount = 0;//未使用数量
            int usedCount = 0;//已使用数量
            int canceledCount = 0;//已取消数量
            int allCount = item.allCoupon.size();
            for (int i = 0; i < allCount; i++) {
                ComboOrderListDineInEntity.ListBean.AllCouponBean allCouponBean = item.allCoupon.get(i);
                switch (allCouponBean.status) {
                    case 1://未使用
                        unusedCount++;
                        break;
                    case 2://已使用
                        usedCount++;
                        break;
                    default://取消
                        canceledCount++;
                        break;
                }
            }
            TextView tvStatus = helper.getView(R.id.merchant_combo_manager_button);
            if (canceledCount > 0) {
                tvStatus.setText("已取消" + canceledCount + "/" + allCount);
                tvStatus.setBackgroundResource(R.drawable.merchant_bg_btn_gray_radious_3);
            } else if (usedCount > 0) {
                tvStatus.setText("已使用" + usedCount + "/" + allCount);
                tvStatus.setBackgroundResource(R.drawable.merchant_bg_btn_red_50_radious_3);
            } else {
                tvStatus.setText("未使用");
                tvStatus.setBackgroundResource(R.drawable.merchant_bg_btn_gr11n_radious_3);
            }
        }
    }

    private void initView(View view) {
        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_load_error, null);
            emptyView.findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getComboOrderManagerList();
                }
            });
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myAdapter = new MyAdapter(R.layout.merchant_item_combo_manager_order);
        recyclerView.setAdapter(myAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
    }

    boolean isFirst = true;
    int page = 0;
    private View emptyView;
    Observer<ComboOrderListDineInEntity> subscriber = new Observer<ComboOrderListDineInEntity>() {

        @Override
        public void onCompleted() {
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            if (page == 0) {

                myAdapter.setEmptyView(emptyView);
            }
            swipeRefreshLayout.setRefreshing(false);
            myAdapter.loadMoreFail();
            showToast(e.getMessage());
        }

        @Override
        public void onNext(ComboOrderListDineInEntity comboOrderListDeliveryEntity) {
            List<ComboOrderListDineInEntity.ListBean> list = comboOrderListDeliveryEntity.list;
            if (page == 0) {
                if (list.size() <= 0) {
                    myAdapter.setEmptyView(R.layout.library_module_no_data);
                } else {
                    myAdapter.setNewData(list);
                }
            } else {
                if (list.size() < Constants.PAGE_COUNT) {
                    myAdapter.loadMoreEnd();
                } else {
                    myAdapter.loadMoreComplete();
                }
                myAdapter.addData(list);
            }
        }
    };

    @SuppressWarnings("unchecked")
    private void getComboOrderManagerList() {
        Subscription subscribe = RetrofitUtils3.getRetrofitService(mContext).
                getManagerComboOrderDineInList("package/getMerOrderList", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                        "1", String.valueOf(page), "30")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        addSubscribe(subscribe);
    }


}
