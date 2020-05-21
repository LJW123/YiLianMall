package com.yilian.mall.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MallOrderAdapter;
import com.yilian.mall.entity.DeleteOrderEntity;
import com.yilian.mall.entity.MallOrderListEntity;
import com.yilian.mall.entity.UserRecordGatherEntity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.UserdataNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.ui.CashDeskActivity2;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.pay.PayFrom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by liuyuqi on 2017/8/1 0001.
 * 订单的fragment
 */
public class MyMallOrderFragment extends BaseFragment {

    private ListView listView;
    private ImageView ivNothing;
    private int orderType, page;
    private MallOrderAdapter adapter;
    private ArrayList<MallOrderListEntity.MallOrderLists> listData = new ArrayList<>();
    private UserdataNetRequest userdataNetRequest;
    private String totalLefen;
    private String totalCoupon;
    private MallNetRequest mallRequest;
    private MySwipeRefreshLayout refreshLayout;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mall_order_list, container, false);
        refreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
//        refreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);//Mode错误
        refreshLayout.setMode(MySwipeRefreshLayout.Mode.BOTH);
//        refreshLayout.setColorSchemeColors(R.color.red);//此处是获取Color值，而不是资源id值
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));

        listView = (ListView) view.findViewById(R.id.pull_listView);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);
        page = 0;
        Logger.i("page   " + page + " orderType :: " + orderType);

//        refreshData();//获取数据放到setUserVisibleHint方法中，1：只获取当前界面数据，不获取其他界面数据，优化界面流畅性、优化流量、避免多个接口请求返回码同为-4时多次打开登录界面
        initListener();
        return view;
    }

    private void refreshData() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
        initData();
        Logger.i("ray-走了这里拉拉2");
    }

    boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        Logger.i("ray-走了这里拉拉1");
        Bundle bundle = getArguments();
        orderType = bundle.getInt("orderType");

        if (isVisibleToUser && isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isFirst = false;
                                    refreshData();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
//        处理账号被挤掉，后点击能够打开订单列表，但是获取数据时，由于返回码-4，会打开登录界面，此处处理登录页面登录成功，回到该界面时的数据刷新逻辑
        if (PreferenceUtils.readBoolConfig(Constants.REFRESH_USER_FRAGMENT, mContext, false)) {
            refreshData();
        }
        super.onResume();
    }

    private void initData() {

        try {
            Logger.i("2017年8月7日 14:15:36-" + orderType);
            RetrofitUtils.getInstance(mContext).setContext(mContext)
                    .getMallOrderList(String.valueOf(orderType), String.valueOf(page), new Callback<MallOrderListEntity>() {
                @Override
                public void onResponse(Call<MallOrderListEntity> call, Response<MallOrderListEntity> response) {
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                        if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                            switch (response.body().code) {
                                case 1:
                                    ArrayList<MallOrderListEntity.MallOrderLists> list = response.body().list;
                                    if (null != list && list.size() > 0) {
                                        ivNothing.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                        if (page > 0) {
                                            listData.addAll(list);
                                        } else {
                                            listData.clear();
                                            listData.addAll(list);
                                        }
                                        if (null == adapter) {
                                            adapter = new MallOrderAdapter(listData, orderType, getActivity());
                                            listView.setAdapter(adapter);
                                        } else {
                                            adapter.notifyDataSetChanged();
                                        }
                                        adapterListener();
                                    } else {
                                        if (page > 0) {
                                            showToast(R.string.no_more_data);
                                        } else {
                                            listView.setVisibility(View.GONE);
                                            ivNothing.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        refreshLayout.setRefreshing(false);
                        refreshLayout.setPullUpRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<MallOrderListEntity> call, Throwable t) {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setPullUpRefreshing(false);
//                    Logger.e(getClass().getSimpleName() + t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                initData();
            }
        });
        refreshLayout.setOnPullUpRefreshListener(new MySwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                page++;
                initData();
            }
        });
    }

    private void jumpToPay(MallOrderListEntity.MallOrderLists mallOrderLists) {

        if (userdataNetRequest == null) {
            userdataNetRequest = new UserdataNetRequest(mContext);
        }
        userdataNetRequest.getUserRecordGather(UserRecordGatherEntity.class, new RequestCallBack<UserRecordGatherEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserRecordGatherEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        sendNextActivity(mallOrderLists, responseInfo.result.coupon, mallOrderLists.order_time, mallOrderLists.order_index, mallOrderLists.order_type);
                        break;
                    case -23:
                    case -4:
                        showToast(R.string.login_failure);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取奖励失败，请检查网络");
            }
        });
    }

    /**
     * 删除条目
     *
     * @param position
     */
    private void delectItem(int position) {
        if (mallRequest == null) {
            mallRequest = new MallNetRequest(mContext);
        }
        startMyDialog();
        mallRequest.deleteOrder(listData.get(position).order_index, "0", new RequestCallBack<DeleteOrderEntity>() {
            @Override
            public void onSuccess(ResponseInfo<DeleteOrderEntity> responseInfo) {
                DeleteOrderEntity result = responseInfo.result;
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, result.code, result.msg)) {
                        switch (result.code) {
                            case 0:
                                showToast("非取消订单,不能删除");
                                break;
                            case 1:
                                listData.remove(position);
                                adapter.notifyDataSetChanged();
                                showToast(R.string.delete_order_success);
                                break;
                        }
                    }
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void sendNextActivity(MallOrderListEntity.MallOrderLists entity, String coupon, String orderCreateTime, String orderIndex, String orderType) {
        Intent intent = new Intent(mContext, CashDeskActivity2.class);

        switch (entity.order_type) {
            case "3":
                totalLefen = String.valueOf(entity.order_coupon);
                totalCoupon = "0";
                intent.putExtra("order_total_lebi", entity.order_total_price);
                intent.putExtra("order_total_integral", Double.parseDouble(entity.order_integral_price));//传递所支付奖券 单位分
                intent.putExtra(CashDeskActivity2.PAY_FROM_TAG, PayFrom.GOODS_ORDER);
                break;
            case "5":
            case "6":
                totalLefen = "0";
                totalCoupon = String.valueOf(entity.order_coupon);intent.putExtra("order_total_lebi", "0");
                intent.putExtra("order_total_lebi", "0");
                intent.putExtra("order_total_integral", Double.parseDouble(entity.comment_deposit));
                intent.putExtra("type", "ActivityDetail");
                break;
            default:
                totalLefen = "0";
                totalCoupon = String.valueOf(entity.order_coupon);
                intent.putExtra("order_total_lebi", entity.order_total_price);
                intent.putExtra("order_total_integral", Double.parseDouble(entity.order_integral_price));//传递所支付奖券 单位分
                intent.putExtra(CashDeskActivity2.PAY_FROM_TAG, PayFrom.GOODS_ORDER);
                break;
        }
        intent.putExtra("payType", "1");//1商城订单 2 商家入驻或续费 3店内支付
        intent.putExtra("orderIndex", orderIndex);

        intent.putExtra("order_type", entity.order_type);// 4代表零购券区商品订单，否则是其他区商品订单
        intent.putExtra("order_total_price", entity.order_total_price);
        intent.putExtra("order_total_lefen", totalLefen);
        intent.putExtra("order_total_coupon", totalCoupon);
        intent.putExtra("orderCreateTime", orderCreateTime);
        intent.putExtra("orderName", entity.order_name);
        intent.putExtra("orderId", entity.order_id);
        intent.putExtra("user_coupon", coupon);
        intent.putExtra("order_total_coucher", entity.order_type);
        Logger.i("跳转收银台前的奖券MYmall  " + entity.order_integral_price);
        startActivity(intent);
        if (null != getActivity()) {
            getActivity().finish();
        }

        Logger.i("提交订单界面order Type   " + orderType);
    }

    private void adapterListener() {
        adapter.setOnDelectOderItemListener(new MallOrderAdapter.onDelecteOrderListener() {
            @Override
            public void delectOrderItem(int position) {

                Logger.i("setOnDelectOderItemListener    111111");

                MallOrderListEntity.MallOrderLists mallOrderLists = listData.get(position);
                switch (mallOrderLists.order_status) {
                    case 0:
                        showDialog(null, "您确定删除订单吗?", null, 0, Gravity.CENTER, "是",
                                "否", true, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                dialog.dismiss();
                                                delectItem(position);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                }, mContext);
                        break;
                    case 1:
                        jumpToPay(mallOrderLists);
                        break;
                    default:
                        break;
                }

            }
        });
    }
}
