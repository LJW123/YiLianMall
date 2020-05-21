package com.yilianmall.merchant.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.ComboOrderListDeliveryEntity;
import com.yilian.networkingmodule.entity.UpdateComboStatusEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.activity.MerchantComboOrderDetailActivity2;
import com.yilianmall.merchant.event.UpdateComboOrderItemStatusEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 */
public class MerchantDeliveryComboListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {


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
        swipeRefreshLayout.setRefreshing(true);
        getComboOrderManagerList();
    }

    private void initListener() {
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ComboOrderListDeliveryEntity.ListBean item = (ComboOrderListDeliveryEntity.ListBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, MerchantComboOrderDetailActivity2.class);
                intent.putExtra("position", position);
                intent.putExtra("order_id", item.comboOrderId);
                intent.putExtra("order_type", 2);
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

    private void showUpdateDialog(final String orderId, final String newStatus, final int position, String message, String positiveText, String negativeText) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateComboOrderStatus(orderId, newStatus, position);
                    }
                })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_333));
    }

    @SuppressWarnings("unchecked")
    void updateComboOrderStatus(String orderId, String newStatus, final int position) {
        RetrofitUtils3.getRetrofitService(mContext).updateComboStatus("updMerMealOrderStatus", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext)
                , orderId, newStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateComboStatusEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UpdateComboStatusEntity updateComboStatusEntity) {
                        int newStatus = updateComboStatusEntity.orderStatus;
                        refreshItemStatus(position, newStatus);
                    }
                });

    }

    @Subscribe
    public void onMessageEvent(UpdateComboOrderItemStatusEvent updateComboOrderItemStatusEvent) {
        int position = updateComboOrderItemStatusEvent.position;
        int newStatus = updateComboOrderItemStatusEvent.newStatus;
        refreshItemStatus(position, newStatus);
    }

    /**
     * 更新某个条目的状态
     *
     * @param position
     * @param newStatus
     */
    private void refreshItemStatus(int position, int newStatus) {
        ComboOrderListDeliveryEntity.ListBean item = myAdapter.getItem(position);
        item.status = newStatus;
        myAdapter.notifyItemChanged(position, item);
    }


    class MyAdapter extends BaseQuickAdapter<ComboOrderListDeliveryEntity.ListBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final ComboOrderListDeliveryEntity.ListBean item) {
            final int layoutPosition = helper.getLayoutPosition();
            helper.setText(R.id.merchant_combo_name, item.packageName);
            helper.setText(R.id.merchant_combo_time, DateUtils.timeStampToStr(item.time));
            helper.setText(R.id.merchant_combo_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.price)));
            TextView tvOrderStatus = helper.getView(R.id.merchant_combo_manager_button);
            tvOrderStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (item.status) {
                        case 1:
                            showUpdateDialog(item.orderNumber, String.valueOf(item.status + 1), layoutPosition, "立即接单", "接单", "取消");
                            break;
                        case 2:
                            showUpdateDialog(item.orderNumber, String.valueOf(item.status + 1), layoutPosition, "立即配送", "立即配送", "取消");
                            break;
                        case 3:
                            showUpdateDialog(item.orderNumber, String.valueOf(item.status + 1), layoutPosition, "配送完成", "完成配送", "取消");
                            break;
                        default:
                            break;
                    }
                }
            });
            TextView tvStatus = helper.getView(R.id.merchant_order_status);
            switch (item.status) {
                case 1:
                    tvStatus.setText("待接单");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.library_module_color_red));
                    tvOrderStatus.setText("立即接单");
                    tvOrderStatus.setBackgroundResource(R.drawable.merchant_btn_bg_red_line);
                    tvOrderStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                    tvOrderStatus.setClickable(true);
                    break;
                case 2:
                    tvStatus.setText("待配送");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.library_module_color_red));
                    tvOrderStatus.setText("立即配送");
                    tvOrderStatus.setBackgroundResource(R.drawable.merchant_btn_bg_red);
                    tvOrderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                    tvOrderStatus.setClickable(true);
                    break;
                case 3:
                    tvStatus.setText("配送中");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.merchant_color_999));
                    tvOrderStatus.setText("配送完成");
                    tvOrderStatus.setBackgroundResource(R.drawable.merchant_bg_btn_blue_radious_3);
                    tvOrderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                    tvOrderStatus.setClickable(true);
                    break;
                case 4:
                    tvStatus.setText("已送达");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.merchant_color_999));
                    tvOrderStatus.setText("已送达");
                    tvOrderStatus.setBackgroundResource(R.drawable.merchant_bg_btn_gray_radious_3);
                    tvOrderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                    tvOrderStatus.setClickable(false);
                    break;
                case 5:
                case 6:
                    tvStatus.setText("已取消");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.merchant_color_999));
                    tvOrderStatus.setText("已取消");
                    tvOrderStatus.setBackgroundResource(R.drawable.merchant_bg_btn_gray_radious_3);
                    tvOrderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                    tvOrderStatus.setClickable(false);
                    break;
            }
            TextView tvOrderCount = helper.getView(R.id.merchant_order_num);
            tvOrderCount.setText("×" + item.num);
            tvOrderCount.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.library_module_gray_arrow), null);
            tvOrderCount.setCompoundDrawablePadding(10);
            helper.setText(R.id.merchant_user_address, item.address);
            GlideUtil.showImageWithSuffix(mContext, item.packageIcon, (ImageView) helper.getView(R.id.merchant_combo_imageview));
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

    int page = 0;
    private View emptyView;
    Observer<ComboOrderListDeliveryEntity> subscriber = new Observer<ComboOrderListDeliveryEntity>() {

        @Override
        public void onCompleted() {
            swipeRefreshLayout.setRefreshing(false);
            Logger.i("刷新走了这里1");
        }

        @Override
        public void onError(Throwable e) {
            if (page == 0) {
                myAdapter.setEmptyView(emptyView);
            } else {
                myAdapter.loadMoreFail();
            }
            swipeRefreshLayout.setRefreshing(false);
            showToast(e.getMessage());
        }

        @Override
        public void onNext(ComboOrderListDeliveryEntity comboOrderListDeliveryEntity) {
            Logger.i("刷新走了这里2");

            List<ComboOrderListDeliveryEntity.ListBean> list = comboOrderListDeliveryEntity.list;
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
                getManagerComboOrderList("package/getMerOrderList", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                        "2", String.valueOf(page), "30")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        addSubscribe(subscribe);
    }


}
