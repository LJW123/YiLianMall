package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.MerchantManageOrderBaseEntity;
import com.yilian.networkingmodule.entity.MerchantManageOrderButtonEntity;
import com.yilian.networkingmodule.entity.MerchantManageOrderTitleEntity;
import com.yilian.networkingmodule.entity.MerchantOrderEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.MerchantManageOrderDetailAdapter;
import com.yilianmall.merchant.adapter.OrderDetailAdapter;
import com.yilianmall.merchant.event.ChangeExpressSuccessEvent;
import com.yilianmall.merchant.event.RemoveMerchantManageOrderList;
import com.yilianmall.merchant.event.UpdateMerchantManageOrderList;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilianmall.merchant.event.RemoveMerchantManageOrderList.TYPE_STOCK;

/**
 * 订单详情
 */
public class MerchantOrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvOrderId;
    private TextView tvOrderStatus;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvLeaveMsg;
    private RecyclerView recyclerView;
    private TextView tvStock;
    private LinearLayout llStock;
    private TextView tvMergeDeliver;
    private String orderIndex;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llBottom;
    private LinearLayout loadErrorView;
    private TextView tvRefresh;
    private OrderDetailAdapter adapter;
    private String orderEntityIndex;
    private MerchantManageOrderDetailAdapter merchantManageOrderDetailAdapter;
    private int position;//列表position 用来确定是外部列表哪个位置
    private MerchantOrderEntity orderEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_order_details);
        initView();
        orderIndex = getIntent().getStringExtra("orderIndex");
        position = getIntent().getIntExtra("position", -1);
        initData();
        initListener();
    }

    @Override
    public void refreshMerchantManageOrderDetailActivity(RemoveMerchantManageOrderList removeMerchantManageOrderList) {
        initData();
    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }

    public void getData() {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMallOrderData(orderIndex, new Callback<MerchantOrderEntity>() {
                    @Override
                    public void onResponse(Call<MerchantOrderEntity> call, Response<MerchantOrderEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        loadErrorView.setVisibility(View.GONE);
                                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                                        llBottom.setVisibility(View.VISIBLE);
                                        orderEntity = response.body();
                                        orderEntityIndex = orderEntity.orderIndex;
                                        initViewData(orderEntity);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<MerchantOrderEntity> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        llBottom.setVisibility(View.GONE);
                        loadErrorView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initViewData(MerchantOrderEntity orderEntity) {
        switch (orderEntity.orderStatus) {
            case "0":
                llBottom.setVisibility(View.GONE);
                break;
            case "2":
                llBottom.setVisibility(View.VISIBLE);
                tvStock.setText("备货");
                break;
            case "3":
                llBottom.setVisibility(View.VISIBLE);
                tvStock.setText("取消备货");
                break;
            case "4":
                llBottom.setVisibility(View.VISIBLE);
                llStock.setVisibility(View.GONE);
                break;
            case "5":
                llBottom.setVisibility(View.GONE);
                break;
            case "6":
                llBottom.setVisibility(View.GONE);
                break;
            default:
                break;
        }
//        adapter = new OrderDetailAdapter(R.layout.merchant_order_details_goods, orderEntity.goods, orderEntity.orderStatus, orderEntity);
        merchantManageOrderDetailAdapter = new MerchantManageOrderDetailAdapter(new ArrayList<MerchantManageOrderBaseEntity>(), orderEntity.orderStatus, orderEntity.orderIndex, position, orderEntity.goods.get(0).goodsIcon);
        initHeadView(orderEntity);
        initBottomView(orderEntity);
        List<MerchantOrderEntity.ParcelsBean> parcels = orderEntity.parcels;
        List<MerchantOrderEntity.GoodsBean> goods = orderEntity.goods;
        int packageNum = 1;
        for (int i = 0; i < parcels.size(); i++) {
            MerchantOrderEntity.ParcelsBean parcelsBean = parcels.get(i);
            String parcelIndex = parcelsBean.parcelIndex;
            ArrayList<MerchantOrderEntity.GoodsBean> newGoods = new ArrayList<>();
            for (int j = 0; j < goods.size(); j++) {
                MerchantOrderEntity.GoodsBean goodsBean = goods.get(j);
                if (goodsBean.parcelIndex.equals(parcelIndex)) {
                    newGoods.add(goodsBean);
                }
            }
            if ("0".equals(parcelIndex)) {
                if (newGoods.size() > 0) {
                    //添加未发货title
                    merchantManageOrderDetailAdapter.addData(new MerchantManageOrderTitleEntity(newGoods.size(), "未发货商品"));
                    //添加未发货商品列表
                    merchantManageOrderDetailAdapter.addData(newGoods);
                }
            } else {
                if (newGoods.size() > 0) {
                    //添加未发货title
                    merchantManageOrderDetailAdapter.addData(new MerchantManageOrderTitleEntity(newGoods.size(), "包裹" + packageNum));
                    packageNum++;
                    //添加未发货商品列表
                    merchantManageOrderDetailAdapter.addData(newGoods);
                    merchantManageOrderDetailAdapter.addData(new MerchantManageOrderButtonEntity(orderEntity.orderStatus, newGoods.get(0).goodsStatus, parcelsBean.expressNum, parcelsBean.expressCompany, parcelsBean.parcelIndex));//带物流信息的按钮
                }
            }
        }
        merchantManageOrderDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof MerchantOrderEntity.GoodsBean) {
                    JumpToOtherPageUtil.getInstance().jumpToJPNewCommDetailActivity(mContext, ((MerchantOrderEntity.GoodsBean) item).goodsType, ((MerchantOrderEntity.GoodsBean) item).goodsId);
                }
            }
        });
        recyclerView.setAdapter(merchantManageOrderDetailAdapter);
    }

    private void initHeadView(MerchantOrderEntity orderEntity) {
        View headView = View.inflate(mContext, R.layout.merchant_title_merchant_manage_order_detail, null);
        tvOrderId = (TextView) headView.findViewById(R.id.tv_order_id);
        tvOrderStatus = (TextView) headView.findViewById(R.id.tv_order_status);
        tvName = (TextView) headView.findViewById(R.id.tv_name);
        tvPhone = (TextView) headView.findViewById(R.id.tv_phone);
        tvPhone.setOnClickListener(this);
        tvAddress = (TextView) headView.findViewById(R.id.tv_address);
        tvLeaveMsg = (TextView) headView.findViewById(R.id.tv_leave_msg);

        tvOrderId.setText(orderEntity.orderId);
        tvName.setText(orderEntity.orderContacts);
        tvPhone.setText(orderEntity.orderPhone);
        tvAddress.setText(orderEntity.orderAddress);
        tvLeaveMsg.setText(Html.fromHtml("<font color='#666666'>买家留言  " + orderEntity.orderRemark));
        switch (orderEntity.orderStatus) {
            case "0":
                tvOrderStatus.setText("已取消");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.merchant_color_999));
                break;
            case "1":
                tvOrderStatus.setText("待支付");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.library_module_color_red));
                break;
            case "2":
                tvOrderStatus.setText("待备货");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.library_module_color_red));
                break;
            case "3":
                tvOrderStatus.setText("待发货");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.library_module_color_red));
                break;
            case "4":
                tvOrderStatus.setText("已部分发货");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.library_module_color_red));
                break;
            case "5":
                tvOrderStatus.setText("待买家收货");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.library_module_color_red));
                break;
            case "6":
                switch (orderEntity.profitStatus) {
                    case 1:
                        tvOrderStatus.setText("已结算");
                        tvOrderStatus.setTextColor(getResources().getColor(R.color.library_module_color_red));
                        break;
                    default:
                        tvOrderStatus.setText("已完成");
                        tvOrderStatus.setTextColor(getResources().getColor(R.color.merchant_color_999));
                        break;
                }
                break;
            default:
                break;
        }
        merchantManageOrderDetailAdapter.addHeaderView(headView);
    }

    private void initBottomView(MerchantOrderEntity orderEntity) {
        View footerView = View.inflate(mContext, R.layout.merchant_order_details_list_bottom, null);
        TextView tvTotalPrice = (TextView) footerView.findViewById(R.id.tv_total_price);
        // DaiGouQuan: 2018/8/14 代购券业务暂时隐藏
        orderEntity.daiGouQuanMoney="0";
        tvTotalPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(
                String.valueOf(new BigDecimal(orderEntity.orderTotalPrice).add(new BigDecimal(orderEntity.daiGouQuanMoney))))));
        TextView tvAllGivenBean = (TextView) footerView.findViewById(R.id.tv_all_given_bean);
        tvAllGivenBean.setText("");
        if (orderEntity.allGivenBean > 0) {
            tvAllGivenBean.setText("+" + MoneyUtil.getLeXiangBiNoZero(orderEntity.allGivenBean) + Constants.APP_PLATFORM_DONATE_NAME);
        }
        TextView tvTotalFreight = (TextView) footerView.findViewById(R.id.tv_freight);
        tvTotalFreight.setText("(含运费¥" + MoneyUtil.getLeXiangBiNoZero(orderEntity.orderFreightPrice) + ")");

        TextView tvOrderTime = (TextView) footerView.findViewById(R.id.tv_order_time);
        if (!TextUtils.isEmpty(orderEntity.orderTime)) {
            tvOrderTime.setText("下单时间:  " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.orderTime)));
        }

        TextView tvPaymentTime = (TextView) footerView.findViewById(R.id.tv_payment_time);
        if (!TextUtils.isEmpty(orderEntity.paymentTime)) {
            tvPaymentTime.setVisibility(View.VISIBLE);
            tvPaymentTime.setText("支付时间:  " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.paymentTime)));
        }

        TextView tvDeliveryTime = (TextView) footerView.findViewById(R.id.tv_delivery_time);
        if (!TextUtils.isEmpty(orderEntity.deliveryTime)) {
            tvDeliveryTime.setVisibility(View.VISIBLE);
            tvDeliveryTime.setText("发货时间:  " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.deliveryTime)));
        }
        TextView tvGetGoodsTime = (TextView) footerView.findViewById(R.id.tv_get_goods_time);
        if (!TextUtils.isEmpty(orderEntity.confirmTime)) {
            tvGetGoodsTime.setVisibility(View.VISIBLE);
            tvGetGoodsTime.setText("确认收货时间:  " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.confirmTime)));
        }
        TextView tvCloseTime = (TextView) footerView.findViewById(R.id.tv_close_time);
        if (!TextUtils.isEmpty(orderEntity.profitTime)) {
            tvCloseTime.setVisibility(View.VISIBLE);
            tvCloseTime.setText("结算时间:  " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.profitTime)));
        }
        merchantManageOrderDetailAdapter.addFooterView(footerView);
    }

    @Override
    public void updateMerchantManageOrderList(UpdateMerchantManageOrderList updateMerchantManageOrderList) {
        initData();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Subscribe
    public void RefreshExpressInfo(ChangeExpressSuccessEvent changeExpressSuccessEvent) {
        getData();
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("订单详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tvStock = (TextView) findViewById(R.id.tv_stock);
        llStock = (LinearLayout) findViewById(R.id.ll_stock);
        tvMergeDeliver = (TextView) findViewById(R.id.tv_merge_deliver);
        llBottom = (LinearLayout) findViewById(R.id.ll_btn_bottom);
        loadErrorView = (LinearLayout) findViewById(R.id.ll_load_error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);

        v3Back.setOnClickListener(this);
        tvStock.setOnClickListener(this);
        tvMergeDeliver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_phone) {
            callPhone();
        } else if (i == R.id.tv_stock) {
            String stockStr = tvStock.getText().toString().trim();
            if (stockStr.equals("备货")) {
                showSystemV7Dialog("确定备货?", null, "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                orderExport();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
            } else if (stockStr.equals("取消备货")) {
                showSystemV7Dialog("确定取消备货?", null, "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                cancelExport();
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
            }
        } else if (i == R.id.tv_merge_deliver) {
            //合并发货
            Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
            List<MerchantOrderEntity.GoodsBean> goods = orderEntity.goods;
            StringBuilder orderGoodsIndex = new StringBuilder();
            for (int j = 0; j < merchantManageOrderDetailAdapter.getItemCount(); j++) {
                MerchantManageOrderBaseEntity item = merchantManageOrderDetailAdapter.getItem(j);
                if (item instanceof MerchantOrderEntity.GoodsBean) {
                    MerchantOrderEntity.GoodsBean bean = (MerchantOrderEntity.GoodsBean) item;
                    if (bean.isChecked) {
                        orderGoodsIndex.append(bean.orderGoodsIndex + ",");
                    }
                }
            }
            if (orderGoodsIndex.length() <= 0) {
                showToast("未选择发货商品");
                return;
            }
            orderGoodsIndex.deleteCharAt(orderGoodsIndex.length() - 1);//去掉最后一个逗号
            intent.putExtra("fromPage", "MerchantOrderDetailsActivity");
            intent.putExtra("parcelIndex", orderGoodsIndex.toString());//每个选中发货商品的id，拼接
            intent.putExtra("orderIndex", orderEntity.orderIndex);
            intent.putExtra("position", position);
            intent.putExtra("orderStatus", orderEntity.orderStatus);
            intent.putExtra(Constants.JUMP_STATUS, "order");
            startActivity(intent);
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String phone = tvPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    /**
     * 取消备货
     */
    private void cancelExport() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .cancelOrderExprot(orderEntityIndex, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast("取消备货成功");
                                        initData();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                        stopMyDialog();
                    }
                });
    }

    /**
     * 备货
     */
    private void orderExport() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .orderExport(orderIndex, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast("备货成功");
                                        initData();
                                        /**
                                         * {@link com.yilianmall.merchant.fragment.MerchantOrderListFragment}
                                         */
                                        EventBus.getDefault().post(new RemoveMerchantManageOrderList(position, TYPE_STOCK));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }
}
