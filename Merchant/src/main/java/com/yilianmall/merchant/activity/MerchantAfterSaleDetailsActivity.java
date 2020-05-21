package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.AfterSaleBtnClickResultEntity;
import com.yilian.networkingmodule.entity.SupplierDetailEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 售后订单详情
 */
public class MerchantAfterSaleDetailsActivity extends BaseActivity implements View.OnClickListener {

    private static final int REFUND_UPDATE_CODE = 99;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvExchangeGoods;
    private TextView tvRefundGoods;
    private TextView tvOrderId;
    private TextView tvStatus;
    private TextView tvApplyTime;
    private TextView tvConsignee;
    private TextView tvPhone;
    private TextView tvDeliveryAddress;
    private TextView tvRemark;
    private ImageView goodsIcon;
    private TextView tvName;
    private TextView tvCount;
    private TextView tvSku;
    private TextView tvPrice;
    private TextView tvScore;
    private TextView tvIntegral;
    private TextView tvReturnBean;
    private TextView tvRefundParcel;
    private TextView tvLookExpressBtn;
    private TextView tvConfirmBtn;
    private LinearLayout llRefund;
    private TextView tvExchangeParcel;
    private TextView tvLookExpressExchange;
    private LinearLayout llExchange;
    private TextView tvUserInfo;
    private TextView tvLookDetails;
    private TextView tvCheckTime;
    private TextView tvSalesTime;
    private TextView tvLookExpress;
    private LinearLayout llSalesParcel;
    private TextView tvMerchantExpressTime;
    private TextView tvLookMerchantExpress;
    private LinearLayout llExchangeParcel;
    private TextView tvTakeDeliveryTime;
    private ScrollView scrollview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView btnConfirm;
    private String serviceIndex;
    private String type;
    private LinearLayout loadErrorView;
    private TextView tvRefresh;
    private TextView tvOldOrderId;
    private LinearLayout btnRefuse;
    private LinearLayout llBottom;
    private TextView tvRefuseContent;
    private TextView tvMerchantConsigneeTime;
    private SupplierDetailEntity.ListBean orderEntity;
    private String exchangeType = "0";//换货返修
    private String refundType = "1";//退货
    private LinearLayout llImageContent;
    private LinearLayout llGoodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_after_sale_details);
        serviceIndex = getIntent().getStringExtra(Constants.SERVICE_INDEX);
        type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //防止ScrollView和SwipeRefreshLayout滑动冲突
                swipeRefreshLayout.setEnabled(scrollview.getScrollY() == 0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("售后订单详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvExchangeGoods = (TextView) findViewById(R.id.tv_exchangeGoods);
        tvRefundGoods = (TextView) findViewById(R.id.tv_refundGoods);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvApplyTime = (TextView) findViewById(R.id.tv_apply_time);
        tvConsignee = (TextView) findViewById(R.id.tv_consignee);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvDeliveryAddress = (TextView) findViewById(R.id.tv_delivery_address);
        tvRemark = (TextView) findViewById(R.id.tv_remark);
        goodsIcon = (ImageView) findViewById(R.id.iv_icon);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvSku = (TextView) findViewById(R.id.tv_sku);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvReturnBean = (TextView) findViewById(R.id.tv_return_bean);
        tvRefundParcel = (TextView) findViewById(R.id.tv_refund_parcel);
        tvLookExpressBtn = (TextView) findViewById(R.id.tv_look_express_btn);
        tvConfirmBtn = (TextView) findViewById(R.id.tv_confirm_btn);
        llRefund = (LinearLayout) findViewById(R.id.ll_refund);
        tvExchangeParcel = (TextView) findViewById(R.id.tv_exchange_parcel);
        tvLookExpressExchange = (TextView) findViewById(R.id.tv_look_express_exchange);
        llExchange = (LinearLayout) findViewById(R.id.ll_exchange);
        llImageContent = (LinearLayout) findViewById(R.id.ll_image_content);
        tvUserInfo = (TextView) findViewById(R.id.tv_user_info);
        tvOldOrderId = (TextView) findViewById(R.id.tv_old_order_id);
        tvLookDetails = (TextView) findViewById(R.id.tv_look_details);
        tvCheckTime = (TextView) findViewById(R.id.tv_check_time);
        tvSalesTime = (TextView) findViewById(R.id.tv_sales_time);
        tvLookExpress = (TextView) findViewById(R.id.tv_look_express);
        llSalesParcel = (LinearLayout) findViewById(R.id.ll_sales_parcel);
        tvMerchantExpressTime = (TextView) findViewById(R.id.tv_delivery_time);
        tvLookMerchantExpress = (TextView) findViewById(R.id.tv_look_merchant_express);
        llExchangeParcel = (LinearLayout) findViewById(R.id.ll_exchange_parcel);
        tvTakeDeliveryTime = (TextView) findViewById(R.id.tv_take_delivery_time);
        tvRefuseContent = (TextView) findViewById(R.id.tv_refuse_content);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tvMerchantConsigneeTime = (TextView) findViewById(R.id.tv_merchant_consignee_time);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        btnRefuse = (LinearLayout) findViewById(R.id.btn_refuse);
        btnConfirm = (TextView) findViewById(R.id.btn_confirm);
        loadErrorView = (LinearLayout) findViewById(R.id.ll_load_error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        llGoodsInfo = (LinearLayout) findViewById(R.id.ll_goods_info);

        llGoodsInfo.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
        tvLookDetails.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        tvLookMerchantExpress.setOnClickListener(this);
        tvLookExpress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Logger.i("拒绝  v.getId  " + v.getId() + "   拒绝Id  " + R.id.btn_refuse);
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.btn_confirm) {
            //通过、确认收货、确认退款、确认发货、商品已修复/备货
            String contentStr = btnConfirm.getText().toString().trim();
            Logger.i("btnConfirmText   " + contentStr);
            if (contentStr.equals("通过") && orderEntity.status.equals("1")) {
                Logger.i("通过  ");
                serviceCheck(orderEntity.orderId, orderEntity.type);
            } else if (contentStr.equals("确认收货")) {
                Logger.i("确认收货  ");
                showReceiveDialog(orderEntity.type, orderEntity.orderId);
            } else if (contentStr.equals("确认退款") && orderEntity.type.equals(refundType)) {
                Logger.i("确认退款  ");
                showRefundDialog(orderEntity.orderId);
            } else if (contentStr.equals("确认发货") && orderEntity.type.equals(exchangeType)) {
                Logger.i("确认发货  ");
                barterExpress(orderEntity.orderId);
            } else if (contentStr.equals("已修复") || contentStr.equals("备货") && orderEntity.type.equals(exchangeType)) {
                Logger.i("商品已修复/备货  ");
                barterStock(orderEntity.orderId);
            }

        } else if (i == R.id.btn_refuse && orderEntity.status.equals("1")) {
            Logger.i("btnRefresh    " + btnRefuse + "   status   " + orderEntity.status);
            //拒绝
            Intent intent = new Intent(mContext, MerchantAfterSaleRefuseDialogActivity.class);
            intent.putExtra(Constants.CHECK_ID, orderEntity.orderId);
            intent.putExtra(Constants.SERVICE_TYPE, orderEntity.type);
            startActivityForResult(intent, REFUND_UPDATE_CODE);
        } else if (i == R.id.tv_refresh) {
            getData();
        } else if (i == R.id.tv_look_merchant_express) {
            //卖家的重新发货的查看物流
            jumpToExpressWeb(orderEntity.merchantExpressCompany, orderEntity.merchantExpressNumber);
        } else if (i == R.id.tv_look_express) {
            //买家退货的查看物流
            jumpToExpressWeb(orderEntity.userExpressCompany, orderEntity.userExpressNumber);
        } else if (i == R.id.tv_look_details) {
            //跳转订单详情
            Intent intent = new Intent(mContext, MerchantOrderDetailsActivity.class);
            intent.putExtra("orderIndex", orderEntity.oldOrderIndex);
            startActivity(intent);
        } else if (i == R.id.tv_phone) {
            PhoneUtil.call(orderEntity.orderPhone, mContext);
        } else if (i == R.id.ll_goods_info) {
            //跳转商品详情
            if (!TextUtils.isEmpty(orderEntity.goodsId) && !TextUtils.isEmpty(orderEntity.payStyle)) {
                JumpToOtherPageUtil.getInstance().jumpToJPNewCommDetailActivity(mContext, orderEntity.payStyle, orderEntity.goodsId);
            } else {
                showToast("商品信息异常，请刷新后重试!");
            }

        }
    }

    /**
     * 通过审核
     *
     * @param serviceOrder
     * @param type
     * @param_status 审核通过传值“3”
     * @param_reason 通过时给“”
     */
    private void serviceCheck(String serviceOrder, String type) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .supplierServiceCheck(serviceOrder, "3", "", type, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 备货
     *
     * @param serviceOrder 短的订单号
     */
    private void barterStock(String serviceOrder) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .barterStock(serviceOrder, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 确认退款
     *
     * @param serviceOrder
     */
    private void showRefundDialog(final String serviceOrder) {

        showSystemV7Dialog("确认退款", "确认退款后，无法恢复，请核对详细信息!", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        refundMoneyBack(serviceOrder);
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    /**
     * 确认退款
     *
     * @param serviceOrder
     */
    private void refundMoneyBack(String serviceOrder) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .refundMoneyBack(serviceOrder, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        getData();
                                        showToast(response.body().msg);
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_AFTER_lIST, true, mContext);
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 确认收货
     *
     * @param type
     * @param serviceOrder
     */
    private void showReceiveDialog(final String type, final String serviceOrder) {
        showSystemV7Dialog("确认收货", "请确认商品名称，规格信息，以及数量!", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        if (type.equals(exchangeType)) {
                            //换货返修的确认收货
                            exchangeBarterExpressReceive(serviceOrder);
                        } else if (type.equals(refundType)) {
                            //退货的确认收货
                            refundExpressReceive(serviceOrder);
                        }
                        dialog.dismiss();
                        break;
                }

                Logger.i("确认收货弹窗     ");
            }
        });
    }

    /**
     * 发货————————>选择地址的activity的跳转获取数据信息
     *
     * @param serviceOrder
     */
    private void barterExpress(String serviceOrder) {
        Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
        intent.putExtra("orderIndex", serviceOrder);
        intent.putExtra(Constants.JUMP_STATUS, "after");
        startActivityForResult(intent, REFUND_UPDATE_CODE);
    }

    /**
     * 获取拒绝操作的信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REFUND_UPDATE_CODE) {
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_AFTER_lIST, true, mContext);
                    initData();
                }
                break;
        }
    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }

    private void getData() {
        Logger.i("getDATA   2222  ");
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSupplierDetailsData(serviceIndex, type, new Callback<SupplierDetailEntity>() {
                    @Override
                    public void onResponse(Call<SupplierDetailEntity> call, Response<SupplierDetailEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        loadErrorView.setVisibility(View.GONE);
                                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                                        orderEntity = response.body().list;
                                        initViewData(orderEntity);
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<SupplierDetailEntity> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(true);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        loadErrorView.setVisibility(View.VISIBLE);
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    private void initViewData(SupplierDetailEntity.ListBean orderEntity) {
        switch (orderEntity.type) {
            case "0"://换货返修
                switch (orderEntity.barterType) {
                    case "1"://换货
                        tvExchangeGoods.setText("换货");
                        break;
                    case "2"://返修
                        tvExchangeGoods.setText("返修");
                        break;
                }
                tvExchangeGoods.setVisibility(View.VISIBLE);
                tvRefundGoods.setVisibility(View.GONE);
                break;
            case "1"://退货
                tvExchangeGoods.setVisibility(View.GONE);
                tvRefundGoods.setVisibility(View.VISIBLE);
                break;
        }
        tvOrderId.setText("编号: " + orderEntity.serviceOrder);
        switch (orderEntity.status) {
            case "0":
                tvStatus.setText("已取消");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_666));

                llSalesParcel.setVisibility(View.GONE);
                llExchangeParcel.setVisibility(View.GONE);
                tvCheckTime.setVisibility(View.GONE);
                tvSalesTime.setVisibility(View.GONE);
                tvMerchantExpressTime.setVisibility(View.GONE);
                tvTakeDeliveryTime.setVisibility(View.GONE);
                tvMerchantConsigneeTime.setVisibility(View.GONE);
                tvRefuseContent.setVisibility(View.GONE);

                break;
            case "1":
                tvStatus.setText("待审核");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                llBottom.setVisibility(View.VISIBLE);
                btnRefuse.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnConfirm.setText("通过");

                llSalesParcel.setVisibility(View.GONE);
                llExchangeParcel.setVisibility(View.GONE);
                tvCheckTime.setVisibility(View.GONE);
                tvSalesTime.setVisibility(View.GONE);
                tvMerchantExpressTime.setVisibility(View.GONE);
                tvTakeDeliveryTime.setVisibility(View.GONE);
                tvMerchantConsigneeTime.setVisibility(View.GONE);
                tvRefuseContent.setVisibility(View.GONE);
                break;
            case "2":
                tvStatus.setText("审核拒绝");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                llBottom.setVisibility(View.GONE);

                llSalesParcel.setVisibility(View.GONE);
                llExchangeParcel.setVisibility(View.GONE);
                tvCheckTime.setVisibility(View.VISIBLE);
                tvSalesTime.setVisibility(View.GONE);
                tvMerchantExpressTime.setVisibility(View.GONE);
                tvTakeDeliveryTime.setVisibility(View.GONE);
                tvRefuseContent.setVisibility(View.VISIBLE);
                tvMerchantConsigneeTime.setVisibility(View.GONE);
                break;
            case "3":
                tvStatus.setText("待退货");
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));

                llBottom.setVisibility(View.VISIBLE);
                btnRefuse.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnConfirm.setText("确认收货");

                llSalesParcel.setVisibility(View.GONE);
                llExchangeParcel.setVisibility(View.GONE);
                tvCheckTime.setVisibility(View.VISIBLE);
                tvSalesTime.setVisibility(View.GONE);
                tvMerchantExpressTime.setVisibility(View.GONE);
                tvTakeDeliveryTime.setVisibility(View.GONE);
                tvMerchantConsigneeTime.setVisibility(View.GONE);
                tvRefuseContent.setVisibility(View.GONE);
                break;
            case "4":
                if (orderEntity.type.equals("0")) { //换货返修
                    switch (orderEntity.barterType) {
                        case "1"://换货
                            tvStatus.setText("换货中");
                            btnConfirm.setText("备货");
                            break;
                        case "2"://返修
                            tvStatus.setText("维修中");
                            btnConfirm.setText("已修复");
                            break;
                    }
                    llBottom.setVisibility(View.VISIBLE);
                    btnRefuse.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.VISIBLE);
                    tvCheckTime.setVisibility(View.VISIBLE);
                    tvSalesTime.setVisibility(View.VISIBLE);
                    llSalesParcel.setVisibility(View.VISIBLE);
                    tvMerchantExpressTime.setVisibility(View.GONE);
                    llExchangeParcel.setVisibility(View.GONE);
                    tvTakeDeliveryTime.setVisibility(View.GONE);
                    tvMerchantConsigneeTime.setVisibility(View.GONE);
                    tvRefuseContent.setVisibility(View.GONE);

                } else if (orderEntity.type.equals("1")) { //退货
                    tvStatus.setText("待收货");

                    llBottom.setVisibility(View.VISIBLE);
                    btnRefuse.setVisibility(View.GONE);
                    btnConfirm.setText("确认收货");

                    tvCheckTime.setVisibility(View.VISIBLE);
                    tvSalesTime.setVisibility(View.VISIBLE);
                    llSalesParcel.setVisibility(View.VISIBLE);
                    tvMerchantExpressTime.setVisibility(View.GONE);
                    llExchangeParcel.setVisibility(View.GONE);
                    tvTakeDeliveryTime.setVisibility(View.GONE);
                    tvMerchantConsigneeTime.setVisibility(View.GONE);
                    tvRefuseContent.setVisibility(View.GONE);

                }
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "5":
                if (orderEntity.type.equals("0")) { //换货返修
                    tvStatus.setText("待发货");

                    llBottom.setVisibility(View.VISIBLE);
                    btnRefuse.setVisibility(View.GONE);
                    btnConfirm.setText("确认发货");

                    tvCheckTime.setVisibility(View.VISIBLE);
                    tvSalesTime.setVisibility(View.VISIBLE);
                    llSalesParcel.setVisibility(View.VISIBLE);
                    llExchangeParcel.setVisibility(View.GONE);
                    tvMerchantExpressTime.setVisibility(View.GONE);
                    tvTakeDeliveryTime.setVisibility(View.GONE);
                    tvMerchantConsigneeTime.setVisibility(View.GONE);
                    tvRefuseContent.setVisibility(View.GONE);

                } else if (orderEntity.type.equals("1")) {//退货
                    tvStatus.setText("待退款");

                    llBottom.setVisibility(View.VISIBLE);
                    btnRefuse.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.VISIBLE);
                    btnConfirm.setText("确认退款");

                    llSalesParcel.setVisibility(View.VISIBLE);
                    tvCheckTime.setVisibility(View.VISIBLE);
                    tvSalesTime.setVisibility(View.VISIBLE);
                    tvMerchantConsigneeTime.setVisibility(View.VISIBLE);
                    llExchangeParcel.setVisibility(View.GONE);
                    tvMerchantExpressTime.setVisibility(View.GONE);
                    tvTakeDeliveryTime.setVisibility(View.GONE);
                    tvRefuseContent.setVisibility(View.GONE);
                }
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                break;
            case "6":
                if (orderEntity.type.equals("0")) { //换货返修
                    tvStatus.setText("待买家收货");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));

                    llBottom.setVisibility(View.GONE);
                    tvCheckTime.setVisibility(View.VISIBLE);
                    tvSalesTime.setVisibility(View.VISIBLE);
                    llSalesParcel.setVisibility(View.VISIBLE);
                    tvMerchantExpressTime.setVisibility(View.VISIBLE);
                    llExchangeParcel.setVisibility(View.VISIBLE);
                    tvTakeDeliveryTime.setVisibility(View.GONE);
                    tvRefuseContent.setVisibility(View.GONE);
                    tvMerchantConsigneeTime.setVisibility(View.GONE);
                } else if (orderEntity.type.equals("1")) { //退货
                    tvStatus.setText("已完成");
                    tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_666));
                    llBottom.setVisibility(View.GONE);

                    llSalesParcel.setVisibility(View.VISIBLE);
                    llExchangeParcel.setVisibility(View.GONE);
                    tvCheckTime.setVisibility(View.VISIBLE);
                    tvSalesTime.setVisibility(View.VISIBLE);
                    tvMerchantExpressTime.setVisibility(View.GONE);
                    tvTakeDeliveryTime.setVisibility(View.GONE);
                    tvRefuseContent.setVisibility(View.GONE);
                    tvMerchantConsigneeTime.setVisibility(View.VISIBLE);
                }
                break;
            case "7":
                if (orderEntity.type.equals("0")) {//换货返修
                    tvStatus.setText("已完成");
                } else if (orderEntity.type.equals("1")) {//退货
                    tvStatus.setText("");
                }
                tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_666));
                llBottom.setVisibility(View.GONE);

                llSalesParcel.setVisibility(View.VISIBLE);
                llExchangeParcel.setVisibility(View.VISIBLE);
                tvCheckTime.setVisibility(View.VISIBLE);
                tvSalesTime.setVisibility(View.VISIBLE);
                tvMerchantExpressTime.setVisibility(View.VISIBLE);
                tvTakeDeliveryTime.setVisibility(View.VISIBLE);
                tvRefuseContent.setVisibility(View.GONE);
                tvMerchantConsigneeTime.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        tvApplyTime.setText("申请时间: " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.serviceTime)));
        tvConsignee.setText("收 货 人: " + orderEntity.orderContacts);
        tvPhone.setText(orderEntity.orderPhone);
        tvDeliveryAddress.setText("收货地址: " + orderEntity.orderAddress);
        tvRemark.setText("备   注: " + orderEntity.remark);
        GlideUtil.showImageWithSuffix(mContext, orderEntity.goodsIcon, goodsIcon);
        tvName.setText(orderEntity.goodsName);
        tvCount.setText(orderEntity.goodsCount);
        tvSku.setText(orderEntity.goodsNorms);
        //售后图片最多三张
        String[] images = orderEntity.images.split(",");
        Logger.i("imageUrl  " + orderEntity.images + "  imagesLength " + images.length);
        View subView = null;
        ImageView goodsIcon;
        if (null != images && images.length > 0) {
            llImageContent.removeAllViews();
            for (int i = 0; i < images.length; i++) {
                if (i > 2) {
                    continue;
                }
                //最多只显示前三个
                subView = View.inflate(mContext, R.layout.merchant_item_sub_image, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, DPXUnitUtil.dp2px(mContext, 18), 0);
                subView.setLayoutParams(layoutParams);
                goodsIcon = (ImageView) subView.findViewById(R.id.imageView);
                GlideUtil.showImageWithSuffix(mContext, images[i], goodsIcon);
                llImageContent.addView(subView);
            }
        }
        switch (orderEntity.payStyle) {
            case "0"://商城
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(orderEntity.goodsCost)));
                tvScore.setVisibility(View.VISIBLE);
                tvScore.setText(getResources().getString(R.string.library_module_score) + MoneyUtil.getLeXiangBiNoZero(orderEntity.returnIntegral));
                tvIntegral.setVisibility(View.GONE);
                tvReturnBean.setVisibility(View.GONE);
                break;
            case "1"://易划算
                tvPrice.setVisibility(View.GONE);
                tvScore.setVisibility(View.GONE);
                tvIntegral.setVisibility(View.VISIBLE);
                tvIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(orderEntity.goodsIntegral)));
                tvReturnBean.setVisibility(View.GONE);
                break;
            case "2"://奖券够
                tvPrice.setVisibility(View.VISIBLE);
                tvScore.setVisibility(View.GONE);
                tvIntegral.setVisibility(View.VISIBLE);
                tvReturnBean.setVisibility(View.GONE);
                tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(orderEntity.goodsRetail)));
                //奖券够的奖券价格是市场价-供货商的价格
                double integralPrice = Double.parseDouble(orderEntity.goodsPrice) - Double.parseDouble(orderEntity.goodsRetail);
                tvIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(integralPrice)));
                break;
            case "3":
            case GoodsType.CAL_POWER:
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(orderEntity.goodsCost)));
                tvScore.setVisibility(View.GONE);
                tvIntegral.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(orderEntity.returnBean) && Double.parseDouble(orderEntity.returnBean) > 0) {
                    tvReturnBean.setVisibility(View.VISIBLE);
                    tvReturnBean.setText(" +" + MoneyUtil.getLeXiangBiNoZero(orderEntity.returnBean) + Constants.APP_PLATFORM_DONATE_NAME);
                } else {
                    tvReturnBean.setVisibility(View.GONE);
                    tvReturnBean.setText("");
                }
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(orderEntity.name)) {
            tvUserInfo.setText("会员信息: " + orderEntity.phone + "(暂无昵称)");
        } else {
            tvUserInfo.setText("会员信息: " + orderEntity.phone + "(" + orderEntity.name + ")");
        }

        tvOldOrderId.setText("原订单号: " + orderEntity.oldOrder);
        tvRefuseContent.setText("拒绝原因: " + orderEntity.refuse);

        if (null != orderEntity.checkTime && Integer.parseInt(orderEntity.checkTime) > 0) {
            tvCheckTime.setText("审核时间: " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.checkTime)));
        } else {
            tvCheckTime.setText("审核时间: ");
        }
        if (null != orderEntity.userExpressTime && Integer.parseInt(orderEntity.userExpressTime) > 0) {
            tvSalesTime.setText("买家退货时间: " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.userExpressTime)));
        } else {
            tvSalesTime.setText("买家退货时间: ");
        }
        if (null != orderEntity.merchantExpressTime && Integer.parseInt(orderEntity.merchantExpressTime) > 0) {
            tvMerchantExpressTime.setText("卖家发货时间: " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.merchantExpressTime)));
        } else {
            tvMerchantExpressTime.setText("卖家发货时间: ");
        }
        if (null != orderEntity.userConfirmTime && Integer.parseInt(orderEntity.userConfirmTime) > 0) {
            tvTakeDeliveryTime.setText("买家确认收货时间: " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.userConfirmTime)));
        } else {
            tvTakeDeliveryTime.setText("买家确认收货时间: ");
        }
        if (null != orderEntity.merchantConfirmTime && Integer.parseInt(orderEntity.merchantConfirmTime) > 0) {
            tvMerchantConsigneeTime.setText("卖家确认收货时间: " + DateUtils.timeStampToStr(Long.parseLong(orderEntity.merchantConfirmTime)));
        } else {
            tvMerchantConsigneeTime.setText("卖家确认收货时间: ");
        }
    }

    /**
     * 换货 返修的确认收货
     *
     * @param serviceOrder
     */
    private void exchangeBarterExpressReceive(String serviceOrder) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .barterExpressReceive(serviceOrder, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 退货的确认收货
     *
     * @param serviceOrder
     */
    private void refundExpressReceive(String serviceOrder) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .refundExpressReceive(serviceOrder, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 查看物流
     *
     * @param expressCompany
     * @param expressNumber
     */
    private void jumpToExpressWeb(String expressCompany, String expressNumber) {
        if (TextUtils.isEmpty(expressCompany) || TextUtils.isEmpty(expressNumber)) {
            showToast("物流信息异常");
            return;
        }
        JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, Ip.getWechatURL(mContext) + "m/expressInfo.html?com=" +
                expressCompany + "&nu=" + expressNumber, false);
    }

    private void showResultToast(Response<AfterSaleBtnClickResultEntity> response) {
        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                switch (response.body().code) {
                    case 1:
                        getData();
                        showToast(response.body().msg);
                        break;
                }
            }
        }
        swipeRefreshLayout.setRefreshing(false);
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_AFTER_lIST, true, mContext);
    }
}
