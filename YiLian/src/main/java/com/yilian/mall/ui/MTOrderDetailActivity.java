package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTComboAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.DeleteOrderEntity;
import com.yilian.mall.entity.MTCodesEntity;
import com.yilian.mall.entity.MTEntity;
import com.yilian.mall.entity.MTOrderEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DistanceUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.utils.NumberFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Ray_L_Pain on 2016/12/5 0005.
 * 美团-订单详情
 */

public class MTOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final int MT_REFUND = 0;
    public static final int MT_ADDRESS = 1;
    public static final int MT_EVA = 2;
    public static final int OUT_TIME = 48;

    @ViewInject(R.id.sv)
    PullToRefreshScrollView sv;
    @ViewInject(R.id.tv_right)
    TextView tvRight;
    @ViewInject(R.id.v3Layout)
    LinearLayout v3Layout;
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.btn_receipt)
    Button btnReceipt;
    @ViewInject(R.id.rl_receipt)
    RelativeLayout rlReceipt;
    @ViewInject(R.id.v3Shop)
    ImageView v3Shop;
    @ViewInject(R.id.rl_service_phone)
    RelativeLayout rlServicePhone;
    @ViewInject(R.id.tv_service_phone)
    TextView tvServicePhone;
    /**
     * goods
     */
    @ViewInject(R.id.ll_go_combo)
    LinearLayout comboLayout;
    @ViewInject(R.id.iv_goods)
    ImageView ivGoods;
    @ViewInject(R.id.tv_goods_name)
    TextView tvGoodsName;
    @ViewInject(R.id.tv_goods_explain)
    TextView tvGoodsExplain;
    @ViewInject(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @ViewInject(R.id.tv_goods_gift)
    TextView tvGoodsGift;//可得奖券字段
    @ViewInject(R.id.tv_assure1)
    TextView tvAssure1;
    @ViewInject(R.id.tv_assure2)
    TextView tvAssure2;
    @ViewInject(R.id.tv_assure3)
    TextView tvAssure3;
    @ViewInject(R.id.tv_assure4)
    TextView tvAssure4;
    @ViewInject(R.id.ratingBar)
    RatingBar ratingBar;
    @ViewInject(R.id.ll_go_eva)
    LinearLayout evaLayout;
    /**
     * ticket
     */
    @ViewInject(R.id.ticket_layout)
    LinearLayout ticketLayout;
    @ViewInject(R.id.tv_btn_first)
    TextView tvBtnFirst;
    @ViewInject(R.id.tv_btn_second)
    TextView tvBtnSecond;
    @ViewInject(R.id.ll_shipping_state)
    LinearLayout shippingLayout;
    @ViewInject(R.id.tv_ticket_state)
    TextView tvTicketState;
    @ViewInject(R.id.tv_ticket_time)
    TextView tvTicketTime;
    @ViewInject(R.id.listView_ticket)
    NoScrollListView ticketListView;
    MTTicketAdapter ticketAdapter;
    PopupWindow popupWindow;
    LinearLayout ticketDialog;
    ImageView iv_QRcode, iv_close;
    /**
     * shop
     */
    @ViewInject(R.id.shop_layout)
    LinearLayout shopLayout;
    @ViewInject(R.id.tv_shop_name)
    TextView tvShopName;
    @ViewInject(R.id.tv_shop_distance)
    TextView tvShopDistance;
    @ViewInject(R.id.tv_shop_address)
    TextView tvShopAddress;
    @ViewInject(R.id.iv_call)
    ImageView ivCall;
    /**
     * combo
     */
    @ViewInject(R.id.view_line)
    View viewLine;
    @ViewInject(R.id.ll_go_more)
    LinearLayout moreLayout;
    @ViewInject(R.id.listView_combo)
    NoScrollListView comboListView;
    MTComboAdapter comboAdapter;
    /**
     * order
     */
    @ViewInject(R.id.tv_order_num)
    TextView tvOrderNum;
    @ViewInject(R.id.tv_order_phone)
    TextView tvOrderPhone;
    @ViewInject(R.id.tv_order_time)
    TextView tvOrderTime;
    @ViewInject(R.id.tv_order_count)
    TextView tvOrderCount;
    @ViewInject(R.id.tv_order_money)
    TextView tvOrderMoney;
    @ViewInject(R.id.tv_pay_time)
    TextView tvPayTime;
    @ViewInject(R.id.ll_profit)
    LinearLayout profitLayout;
    @ViewInject(R.id.tv_profit)
    TextView tvProfit;
    @ViewInject(R.id.tv_sale_integral)
    TextView tvSaleIntegral;
    @ViewInject(R.id.ll_load_error)
    LinearLayout llLoadError;
    @ViewInject(R.id.tv_refresh)
    TextView tvRefresh;
    /**
     * contact
     */
    @ViewInject(R.id.include_delivery_info)
    LinearLayout includeDeliveryInfo;//配送信息
    @ViewInject(R.id.tv_contact_address_info)
    TextView tvContactAddress;
    @ViewInject(R.id.tv_contact_name)
    TextView tvContactName;
    @ViewInject(R.id.tv_contact_phone)
    TextView tvContactPhone;

    private MTNetRequest mtNetRequest;
    private String merchantId; //商家id
    private String packageOrderId;  //订单id
    private String packageId; //套餐id
    private String status;  //套餐状态 0待付款 1未使用/待接单 2待配送 3配送中 4已使用已送达 5已退款 6已取消
    private String imgUrl;  //图
    private String isDel;   //是否配送 0到店 1外卖
    private String isEva;   //是否评价  0未评价 1已经评价
    private long cantCancleTime; //购买时间

    //请求获取的数据
    private String orderInfoType;
    private MTOrderEntity info;
    private ArrayList<MTCodesEntity> codesList;
    private MTOrderEntity.MerchantInfo merchantInfo;
    private MTOrderEntity.OrderInfo orderInfo;
    private ArrayList<MTOrderEntity.PackageInfo> packageList;
    private String shippingMoney; //配送费
    private String shopTel, serviceTel = "400-152-5159", distance; //商家电话,客服电话,距商家距离

    private String money; //单价的钱
    private String amount; //套餐的钱
    private String profit; //鼓励金
    private String addressId;

    /**
     * 团购转外卖
     */
    private float userMoney;//用户奖励
    private PayDialog paydialog; //密码框
    private GridPasswordView pwdView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.EXECUTE_SUCCESS:
                    dismissInputMethod();
                    getOrder();
                    break;
            }
        }
    };
    private boolean allowDelivery;
    private MallNetRequest mallRequest;
    private String count;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.v3Back:
                MTOrderDetailActivity.this.finish();
                break;
            case R.id.tv_btn_first:
                if ("申请退款".equals(tvBtnFirst.getText())) {
                    if ("1".equals(status)) {
                        intent = new Intent(MTOrderDetailActivity.this, MTRefundActivity.class);
                        intent.putExtra("index_id", packageOrderId);
                        intent.putExtra("img_url", imgUrl);
                        intent.putExtra("type", orderInfoType);
                        startActivityForResult(intent, MT_REFUND);
                    } else {
                        intent = new Intent(MTOrderDetailActivity.this, MTRefundActivity2.class);
                        intent.putExtra("index_id", packageOrderId);
                        intent.putExtra("shop_tel", shopTel);
                        startActivityForResult(intent, MT_REFUND);
                    }
                } else if ("取消订单".equals(tvBtnFirst.getText())) {
                    showDialog(null, "您确定取消该订单吗？", null, 0, Gravity.CENTER, "是",
                            "否", true, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            dialog.dismiss();
                                            cancelOrder();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            initView();
                                            break;
                                    }
                                }
                            }, mContext);
                } else if ("再来一单".equals(tvBtnFirst.getText())) {
                    intent = new Intent(MTOrderDetailActivity.this, MTComboDetailsActivity.class);
                    intent.putExtra("package_id", packageId);
                    intent.putExtra("merchant_id", packageOrderId);
                    startActivity(intent);
                } else if ("已送奖券".equals(tvBtnFirst.getText())) {

                } else {
                    intent = new Intent(MTOrderDetailActivity.this, MTRefundStateActivity.class);
                    intent.putExtra("index_id", packageOrderId);
                    startActivity(intent);
                }
                break;
            case R.id.tv_btn_second:
                if ("要求配送".equals(tvBtnSecond.getText())) {
                    selectAddress();
                } else if ("去付款".equals(tvBtnSecond.getText())) {
                    intent = new Intent(MTOrderDetailActivity.this, CashDeskActivity.class);
                    intent.putExtra("orderIndex", packageOrderId);
                    intent.putExtra("type", "MTPackage");
                    intent.putExtra("payType", "6");
                    intent.putExtra("order_total_lebi", amount);
                    intent.putExtra("order_total_coupon", "0");
                    intent.putExtra("orderNumber", count);
                    startActivity(intent);
                } else {
                    //如果还未配送的情况下
                    showDialog(null, "您确定取消配送吗？", null, 0, Gravity.CENTER, "是",
                            "否", true, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            dialog.dismiss();
                                            translateOrder(packageOrderId, "0", "0", "2", "");
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            }, mContext);
                }
                break;
            case R.id.ll_go_eva:
                intent = new Intent(MTOrderDetailActivity.this, MTPackageCommentActivity.class);
                intent.putExtra(Constants.SPKEY_MT_PACKAGE_ORDER_ID, packageOrderId);
                startActivityForResult(intent, MT_EVA);
                break;
            case R.id.iv_call:
                if (TextUtils.isEmpty(shopTel)) {
                    showToast(R.string.no_phone);
                } else {
                    showTelDialog(shopTel);
                }
                break;
            case R.id.ll_go_more:
                String loadUrl = Constants.PHOTO_TEXT_URL + packageId;
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", loadUrl);
                startActivity(intent);
                break;
            case R.id.btn_receipt:
                showSystemV7Dialog(null, "确认已送达?", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                receipt();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.rl_service_phone:
            case R.id.tv_service_phone:
                showTelDialog(serviceTel);
                break;
            case R.id.iv_close:
                popupWindow.dismiss();
                break;
            case R.id.ll_go_combo:
                intent = new Intent(MTOrderDetailActivity.this, MTComboDetailsActivity.class);
                intent.putExtra("package_id", packageId);
                intent.putExtra("merchant_id", merchantId);
                startActivity(intent);
                break;
            case R.id.shop_layout:
                intent = new Intent(MTOrderDetailActivity.this, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", merchantId);
                startActivity(intent);
                break;
            case R.id.tv_right:

                break;
            case R.id.tv_refresh:
                getOrder();
                break;
        }
    }

    /**
     * 确认收货
     */
    @SuppressWarnings("unchecked")
    private void receipt() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .confirmIMerMealOrder("confirmMerMealOrder", com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext), com.yilian.mylibrary.RequestOftenKey.getToken(mContext), orderInfo.orderNumber, "4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
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
                    public void onNext(HttpResultBean baseEntity) {
                        getOrder();
                        showToast(baseEntity.msg);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {
        if (mallRequest == null) {
            mallRequest = new MallNetRequest(mContext);
        }
        mallRequest.deleteOrder(packageOrderId, "1", new RequestCallBack<DeleteOrderEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<DeleteOrderEntity> responseInfo) {
                DeleteOrderEntity result = responseInfo.result;

                if (CommonUtils.serivceReturnCode(mContext, result.code, result.msg)) {
                    switch (result.code) {
                        case 0:
                            showToast("非取消订单,不能删除");
                            break;
                        case 1:
                            showToast(R.string.delete_order_success);
                            finish();
                            break;
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

    @Override
    protected void onResume() {
        super.onResume();
        getOrder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_mt);
        ViewUtils.inject(this);

        initView();
    }


    private void initView() {
        tvServicePhone.setText("咨询电话: " + serviceTel);
        packageOrderId = getIntent().getStringExtra("index_id");
        v3Layout.setFocusable(true);
        v3Layout.setFocusableInTouchMode(true);
        ivBack.setOnClickListener(this);
        tvTitle.setText("订单详情");
        tvBtnFirst.setOnClickListener(this);
        tvBtnSecond.setOnClickListener(this);
        evaLayout.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        comboLayout.setOnClickListener(this);
        shopLayout.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);

        //这里评价只是起视图作用，不可点击
        ratingBar.setRating(0);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ticketDialog = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_ticket, null);
        iv_QRcode = (ImageView) ticketDialog.findViewById(R.id.iv_qrCode);
        iv_close = (ImageView) ticketDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        btnReceipt.setOnClickListener(this);
        tvServicePhone.setOnClickListener(this);
        rlServicePhone.setOnClickListener(this);
        sv.setVisibility(View.GONE);
        sv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getOrder();
                sv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        viewLine.setVisibility(View.VISIBLE);
    }

    private void getOrder() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.getMTOrder(packageOrderId, new RequestCallBack<MTOrderEntity>() {

            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTOrderEntity> responseInfo) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                info = responseInfo.result;
                                codesList = info.codes;
                                merchantInfo = info.merchantInfo;
                                orderInfo = info.orderInfo;
                                packageList = info.packageInfo;
                                getOrderStatus();
                                initData();

                                sv.setVisibility(View.VISIBLE);
                                llLoadError.setVisibility(View.GONE);
                                break;
                            default:
                                sv.setVisibility(View.GONE);
                                llLoadError.setVisibility(View.VISIBLE);
                                break;
                        }
                    } else {
                        sv.setVisibility(View.GONE);
                        llLoadError.setVisibility(View.VISIBLE);
                    }
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                sv.setVisibility(View.GONE);
                llLoadError.setVisibility(View.VISIBLE);
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 判断订单状态，如果有不是“已使用”状态，那么要求配送按钮取消
     */
    private void getOrderStatus() {
        for (MTCodesEntity mtCodesEntity : info.codes) {
            if (!"1".equals(mtCodesEntity.status)) {
                //如果有任何一个订单 不是 “已使用”状态 该订单都不能转配送
                allowDelivery = false;
                tvBtnSecond.setVisibility(View.GONE);
                return;
            }

        }
    }

    private void initData() {
//        userMoney = NumberFormat.convertToFloat(sp.getString("lebi", "0"), 0);//未处理的奖励
        //未处理的奖励不能直接从sp中去取
        initUserMoney();


        Logger.i("2017-1-5:" + userMoney);
        //根据状态来显示不同的控件
//        status = "4"; //套餐状态 0待付款 1未使用待接单 2待配送 3配送中 4已使用已送达 5已退款 6已取消
//        isDel = "2";//是否配送 0不支持 1支持
//        isEva = "0";//是否评价 0未评价 1已经评价
//        orderInfoType = ""; // 1到店消费 2订单外卖
        /**
         * 现在用网络获取的
         */
        status = info.status;
        if ("3".equals(status) && "2".equals(orderInfo.type)) {//只有外面订单，且在配送过程中，才显示确认收货按钮
            rlReceipt.setVisibility(View.VISIBLE);
        } else {
            rlReceipt.setVisibility(View.GONE);
        }
        isDel = info.merchantInfo.isDelivery;
        isEva = info.isEvaluate;
        orderInfoType = info.orderInfo.type;
        packageId = info.packageId;
        merchantId = info.merchantInfo.merchantId;
        cantCancleTime = (Long.valueOf(info.orderInfo.verifyDate) - Long.valueOf(info.orderInfo.serverTime)) / 3600;//转换成天数
//        Logger.i("2017-1-7:" + Long.valueOf(info.orderInfo.verifyDate));
//        Logger.i("2017-1-7:" + Long.valueOf(info.orderInfo.serverTime));
//        Logger.i("2017-1-7:" + cantCancleTime);

        imgUrl = Constants.ImageUrl + info.packageIcon + BitmapUtil.getBitmapSuffix(mContext);
        imageLoader.displayImage(imgUrl, ivGoods, options);
        tvGoodsName.setText(info.name);
        tvGoodsExplain.setText("");
        money = info.price;
        tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(money)));
        tvGoodsGift.setVisibility(View.VISIBLE);
//        tvGoodsGift.setText("可得" + getResources().getString(R.string.integral) + String.format("%.2f", NumberFormat.convertToDouble(info.returnIntegral, 0.0) / 100));
        tvGoodsGift.setText("可得" + getResources().getString(R.string.integral) + MoneyUtil.getLeXiangBiNoZero(info.returnIntegral));
        tvAssure1.setText("随时退");
        tvAssure2.setText("赠" + getResources().getString(R.string.integral));
        tvAssure3.setText("过期自动退");
        String assureStr = null;
        if ("1".equals(isDel)) {
            assureStr = "满" + MoneyUtil.getLeXiangBiNoZero(info.fullMinusFee) + "免费送";
        } else {
            assureStr = "不支持配送";
        }
        tvAssure4.setText(assureStr);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date(Long.valueOf(info.endTime + "000")));
        tvTicketTime.setText(time);
        ticketAdapter = new MTTicketAdapter(mContext, info.codes);
        ticketListView.setAdapter(ticketAdapter);
        ticketListView.setFocusable(false);
        ticketListView.setFocusableInTouchMode(false);

        List<MTCodesEntity> list = info.codes;
        int vis_flag = 0;
        for (int i = 0; i < list.size(); i++) {
            if ("1".equals(list.get(i).status)) {
                vis_flag = vis_flag + 0;
            } else {
                vis_flag = vis_flag + 1;
            }
            vis_flag += vis_flag;
        }
        Logger.i("2017-1-11:   " + vis_flag);


        String lat = merchantInfo.merchantLatitude;
        String log = merchantInfo.merchantLongitude;

        double lat_d = MyApplication.getInstance().getLatitude();
        double log_d = MyApplication.getInstance().getLongitude();

        if (log == null || lat == null) {
            distance = "计算距离失败";
        } else {
            distance = (int) DistanceUtil.getDistanceOfMeter(Double.parseDouble(lat), Double.parseDouble(log), lat_d, log_d) / 1000 + "km";
        }
//        Logger.i("2017-1-10:" + lat_d);
//        Logger.i("2017-1-10:" + log_d);
//        Logger.i("2017-1-10:" + lat);
//        Logger.i("2017-1-10:" + log);
//        Logger.i("2017-1-10:" + distance);

        tvShopName.setText(merchantInfo.merchantName + "(" + merchantInfo.merchantDesp + ")");
        tvShopAddress.setText(merchantInfo.merchantAddress);
        tvShopDistance.setText(distance);
        shopTel = merchantInfo.merchantTel;

        comboAdapter = new MTComboAdapter(mContext, packageList);
        comboListView.setAdapter(comboAdapter);
        comboListView.setFocusable(false);
        comboListView.setFocusableInTouchMode(false);

        tvOrderNum.setText(orderInfo.orderNumber);
        tvOrderPhone.setText(orderInfo.phone);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = sdf1.format(new Date(Long.valueOf(orderInfo.payTime + "000")));
        tvOrderTime.setText(time1);
        tvOrderCount.setText("× " + orderInfo.count);
        amount = orderInfo.amount;
        count = orderInfo.count;

        tvContactAddress.setText(orderInfo.contactAddress);
        tvContactName.setText(orderInfo.contactName);
        tvContactPhone.setText(orderInfo.contactPhone);
        //配送信息  如果为null不显示
        if (TextUtils.isEmpty(orderInfo.contactAddress)&&TextUtils.isEmpty(orderInfo.contactName)&&TextUtils.isEmpty(orderInfo.contactPhone)){
            includeDeliveryInfo.setVisibility(View.GONE);
        }else {
            includeDeliveryInfo.setVisibility(View.VISIBLE);
        }
        tvOrderMoney.setText("¥" + MoneyUtil.getLeXiangBiNoZero(amount));
        tvSaleIntegral.setText(MoneyUtil.getLeXiangBiNoZero(Float.parseFloat(info.returnIntegral) * Float.parseFloat(orderInfo.count)) + getResources().getString(R.string.integral));
        //鼓励金
        profit = orderInfo.profit;
        if (null != profit && !profit.equals("0") && status.equals("4")) {
            profitLayout.setVisibility(View.VISIBLE);
            tvProfit.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(profit)));
        } else {
            profitLayout.setVisibility(View.GONE);
        }
        switch (status) {
            case "0":
                ticketLayout.setVisibility(View.VISIBLE);
                tvBtnFirst.setVisibility(View.VISIBLE);
                tvBtnSecond.setVisibility(View.VISIBLE);
                tvBtnFirst.setText("取消订单");
                tvBtnSecond.setText("去付款");
                break;
            case "1":
                if ("0".equals(isDel)) {  //不配送
                    tvBtnFirst.setVisibility(View.VISIBLE);
                    tvBtnSecond.setVisibility(View.GONE);
                } else {
                    tvBtnFirst.setVisibility(View.VISIBLE);
                    tvBtnSecond.setVisibility(View.VISIBLE);
                    if ("1".equals(orderInfoType)) {//到店消费订单
                        if (info.profitStatus == 1) {
                            tvBtnFirst.setText("已结算");
                            tvBtnFirst.setBackgroundColor(Color.WHITE);
                            tvBtnSecond.setVisibility(View.GONE);
                        } else {
                            tvBtnFirst.setText("申请退款");
                        }
//                        已送奖券订单不能申请售后
                        if (info.profitStatus == 1) {
                            tvBtnSecond.setVisibility(View.GONE);

                        } else {
                            //新需求  只要有一个券码不是未兑换状态或该订单已申请发奖励就不能要求配送
                            if (vis_flag == 0) {
                                tvBtnSecond.setVisibility(View.VISIBLE);
                                tvBtnSecond.setText("要求配送");
                            } else {
                                tvBtnSecond.setVisibility(View.GONE);
                            }
                        }

                        shippingLayout.setVisibility(View.GONE);
                    } else {//外卖订单
                        tvBtnSecond.setText("取消配送");
                        shippingLayout.setVisibility(View.VISIBLE);
                        tvTicketState.setText("待接单");
                    }
                }
                ticketLayout.setVisibility(View.VISIBLE);
                break;
            case "2":
                tvBtnSecond.setVisibility(View.GONE);
                if ("0".equals(isDel)) {
                    tvBtnFirst.setVisibility(View.VISIBLE);
                } else {
                    if (cantCancleTime > OUT_TIME) {
                        tvBtnFirst.setVisibility(View.GONE);
                    } else {
                        tvBtnFirst.setVisibility(View.VISIBLE);
                    }
                    tvBtnFirst.setText("申请退款");
                    if ("1".equals(orderInfoType)) {
                        tvBtnSecond.setText("要求配送");
                        shippingLayout.setVisibility(View.GONE);
                    } else {
                        tvBtnSecond.setText("取消配送");
                        shippingLayout.setVisibility(View.VISIBLE);
                        tvTicketState.setText("待配送");
                    }
                }
                ticketLayout.setVisibility(View.VISIBLE);
                break;
            case "3":
                if ("0".equals(isDel)) {
                    tvBtnFirst.setVisibility(View.VISIBLE);
                    tvBtnSecond.setVisibility(View.GONE);
                } else {
                    tvBtnSecond.setVisibility(View.VISIBLE);
                    if (cantCancleTime > OUT_TIME) {
                        tvBtnFirst.setVisibility(View.GONE);
                    } else {
                        tvBtnFirst.setVisibility(View.VISIBLE);
                    }
                    tvBtnFirst.setText("申请退款");
                    if ("1".equals(orderInfoType)) {
                        tvBtnSecond.setText("要求配送");
                        shippingLayout.setVisibility(View.GONE);
                    } else {
                        tvBtnSecond.setVisibility(View.GONE);
                        //tvBtnSecond.setText("取消配送");
                        shippingLayout.setVisibility(View.VISIBLE);
                        tvTicketState.setText("配送中");
                    }
                }
                ticketLayout.setVisibility(View.VISIBLE);
                break;
            case "4":
                if ("2".equals(orderInfoType)) {//配送订单
                    if ("0".equals(isEva)) {
                        evaLayout.setVisibility(View.VISIBLE);
                        tvBtnFirst.setText("申请退款");
                    } else {
                        evaLayout.setVisibility(View.GONE);
                        tvBtnFirst.setText("再来一单");
                    }
                } else {//到店消费
                    evaLayout.setVisibility(View.GONE);
                    tvBtnFirst.setText("再来一单");
                }
                if (cantCancleTime > OUT_TIME) {
                    tvBtnFirst.setVisibility(View.GONE);
                } else {
                    tvBtnFirst.setVisibility(View.VISIBLE);
                }
                tvBtnSecond.setVisibility(View.GONE);
                if ("1".equals(orderInfoType)) {
                    shippingLayout.setVisibility(View.GONE);
                    tvTicketState.setText("已送达");
                } else {
                    shippingLayout.setVisibility(View.VISIBLE);
                }
                ticketLayout.setVisibility(View.VISIBLE);
                break;
            case "5":
                tvBtnFirst.setVisibility(View.VISIBLE);
                tvBtnSecond.setVisibility(View.GONE);
                tvBtnFirst.setText("退款详情");
                ticketLayout.setVisibility(View.VISIBLE);
                break;
            case "6":
                tvBtnFirst.setVisibility(View.VISIBLE);
                tvBtnSecond.setVisibility(View.GONE);
                tvBtnFirst.setText("再来一单");

                v3Shop.setVisibility(View.GONE);
                break;
        }

//        申请发奖励按钮逻辑
        if (info.supplyStatus == 1 && "1".equals(orderInfo.type) && !"6".equals(status)) {//只有到点消费订单(且是非取消订单) 才有申请发奖励
            tvRight.setVisibility(View.VISIBLE);
            v3Shop.setVisibility(View.GONE);
            tvRight.setText("申请发奖励");
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSystemV7Dialog("确认立即申请奖券之后,奖券即可到账,且套餐将不可申请退款！是否立即申请奖券？", null, "申请发奖励", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    applyIntegral();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });
                }
            });
        } else if ("6".equals(status)) {
            tvRight.setVisibility(View.VISIBLE);
            v3Shop.setVisibility(View.GONE);
            tvRight.setText("删除订单");
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(null, "您确定删除订单吗?", null, 0, Gravity.CENTER, "是",
                            "否", true, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            dialog.dismiss();
                                            deleteOrder();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            initView();
                                            break;
                                    }
                                }
                            }, mContext);
                }
            });
        } else {
            v3Shop.setVisibility(View.INVISIBLE);
            tvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 申请发奖励
     */
    @SuppressWarnings("unchecked")
    private void applyIntegral() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                packageApplyIntegral("package_apply_integral", com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext), com.yilian.mylibrary.RequestOftenKey.getToken(mContext),
                        orderInfo.orderNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        showToast(httpResultBean.msg);
                        getOrder();
                    }
                });
        addSubscription(subscription);
    }

    private void initUserMoney() {
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).getMyBalance(new Callback<MyBalanceEntity2>() {
            @Override
            public void onResponse(Call<MyBalanceEntity2> call, Response<MyBalanceEntity2> response) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                    if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                        switch (response.body().code) {
                            case 1:
                                userMoney = NumberFormat.convertToFloat(response.body().lebi, 0);
                                break;
                        }
                    }
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(Call<MyBalanceEntity2> call, Throwable t) {
                showToast(R.string.network_module_net_work_error);
                stopMyDialog();
            }
        });
    }

    private void selectAddress() {
        Intent intent = new Intent(mContext, AddressManageActivity.class);
        intent.putExtra("FLAG", "comboIn");//判断是从下订单进入还是从我的地址进入
        if (addressId != null) {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, addressId);//选中的标示
        } else {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
        }
        startActivityForResult(intent, MT_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MT_REFUND) {
            MTOrderDetailActivity.this.finish();
        }

        if (resultCode == RESULT_OK && requestCode == MT_ADDRESS) {
            if (null == data.getExtras().getSerializable("USE_RADDRESS_LIST")) {
                showToast("未选择地址，无法配送!!!");
            } else {
                UserAddressLists userAddressList = (UserAddressLists) data.getExtras().getSerializable("USE_RADDRESS_LIST");
                addressId = userAddressList.address_id;
                translateOrder(packageOrderId, "0", addressId, "3", "");
                Logger.i("2016-12-21:" + packageOrderId);
                Logger.i("2016-12-21:" + addressId);
            }
        }

        if (requestCode == RESULT_OK && requestCode == MT_EVA) {
            MTOrderDetailActivity.this.finish();
        }
    }

    private void cancelOrder() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.cancelOrder(packageOrderId, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        //刷新个人页面
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);

                        showToast("成功取消订单");
                        finish();
                        break;
                    default:
                        showToast(responseInfo.result.msg);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 不配送转配送
     *
     * @param id
     * @param count
     * @param address
     * @param type
     * @param pwd
     */
    private void translateOrder(String id, String count, String address, final String type, String pwd) {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.switchMT(id, count, address, type, pwd, new RequestCallBack<MTEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        String money = responseInfo.result.price;
                        shippingMoney = MoneyUtil.getLeXiangBiNoZero(money);
                        //团购转配送
                        //type=1支付配送费
                        if ("1".equals(type)) {
                            showToast("支付成功");
                            dismissJP();
                            paydialog.dismiss();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what = Constants.EXECUTE_SUCCESS;
                                    handler.sendMessage(message);
                                }
                            }).start();
                        }
                        //配送转团购 返还用户配送费
                        if ("2".equals(type)) {
                            getOrder();
                        }
                        //查询此单的配送费
                        if ("3".equals(type)) {
                            String content = "";
                            if ("0".equals(shippingMoney)) {
                                content = "亲,该订单是免配送费的哦! 请您输入密码进行验证";
                            } else {
                                content = "本次配送需支付配送费 " + shippingMoney + " 元，是否继续？";
                            }
                            showDialog(null, content, null, 0, Gravity.CENTER, "好的",
                                    "算了", false, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    dialog.dismiss();
                                                    //TODO 判断奖励是否充足 如果够则支付 不够 跳转收银台
                                                    if (userMoney >= Float.parseFloat(money)) {
                                                        pay(money);
                                                    } else {
                                                        Intent intent = new Intent(mContext, CashDeskActivity.class);
                                                        intent.putExtra("order_total_lebi", money);
                                                        intent.putExtra("order_total_coupon", "0");
                                                        intent.putExtra("orderIndex", id);
                                                        intent.putExtra("type", "MTPackage");
                                                        intent.putExtra("orderNumber", count);
                                                        intent.putExtra("addressId", addressId);
                                                        intent.putExtra("payType", "7");//团购转配送
                                                        startActivity(intent);
                                                    }
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.dismiss();
                                                    break;
                                            }
                                        }
                                    }, mContext);
                        }
                        break;
                    case -3:
                        paydialog.dismiss();
                        showToast("系统繁忙，请稍后再试");
                        break;
                    case -4:
                        mContext.startActivity(new Intent(MTOrderDetailActivity.this, LeFenPhoneLoginActivity.class));
                        break;
                    case -5:
                        pwdView.clearPassword();
                        Toast.makeText(mContext, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        break;
                    case -13:
                        Toast.makeText(mContext, "奖励不足", Toast.LENGTH_SHORT).show();
                        paydialog.dismiss();
                        if (isLogin()) {
//                            Intent intent0 = new Intent(mContext, NMemberChargeActivity.class);
//                            intent0.putExtra("type", "chooseCharge");
//                            startActivity(intent0);

                            startActivity(new Intent(mContext, RechargeActivity.class));
                        } else {
                            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                        }
                        break;
                    case -70:
                        showToast("超出配送范围");
                        break;
                    case -74:
                        showToast("商家不支持配送");
                        break;
                    case -81:
                        showToast("商家已接单，请耐心等待商家配送哦");
                        break;
                    default:
                        showToast( responseInfo.result.msg);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    public void pay(String money) {
        paydialog = new PayDialog(mContext, packageOrderId, handler);
        paydialog.show();
    }

    public class PayDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;

        private Context context;
        private Handler handler;
        private String orderIndexs;

        public PayDialog(Context context, String orderIndexs, Handler handler) {
            super(context, R.style.GiftDialog);
            this.context = context;
            this.handler = handler;
            this.orderIndexs = orderIndexs;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);

            initView();
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {

                }

                @Override
                public void onMaxLength(String psw) {
                    payRunMoney(pwdView.getPassWord());
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, InitialPayActivity.class));
                }
            });


            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }

        /**
         * 支付配送费
         */
        private void payRunMoney(String pwd) {
            //支付密码
            final String password = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
            translateOrder(packageOrderId, "0", addressId, "1", password);
        }

    }

    /**
     * 关闭软键盘
     */
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打电话弹窗
     */
    private void showTelDialog(String tel) {
        showDialog(null, tel, null, 0, Gravity.CENTER, "呼叫",
                "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + tel);
                                intent.setData(data);
                                startActivity(intent);
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                }, mContext);
    }

    /**
     * 券码adapter
     */
    class MTTicketAdapter extends android.widget.BaseAdapter {
        private Context context;
        private List<MTCodesEntity> list;

        public MTTicketAdapter(Context context, List<MTCodesEntity> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list.size() == 0 || list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MTTicketAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_ticket_mt, null);
                holder = new MTTicketAdapter.ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
                convertView.setTag(holder);
            } else {
                holder = (MTTicketAdapter.ViewHolder) convertView.getTag();
            }

            int num = position + 1;
            holder.tv.setText("券码" + num + "：");

            holder.tvNum.setText(list.get(position).code);

            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow = new PopupWindow();
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setFocusable(false);
                    popupWindow.setContentView(ticketDialog);
                    popupWindow.setAnimationStyle(R.style.merchant_AnimationFade);
                    popupWindow.showAtLocation(holder.iv, Gravity.CENTER, 0, 0);  //屏幕居中显示
                    //点击背景变暗
                    ColorDrawable cd = new ColorDrawable(0x000000);
                    popupWindow.setBackgroundDrawable(cd);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.4f;
                    getWindow().setAttributes(lp);

                    String qrString = QRCodeUtils.getInstance(context).getMTPackageTicketQRCodePrefix(packageOrderId) + list.get(position).code;
                    QRCodeUtils.createImage(qrString, iv_QRcode);

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });
                }
            });

            String stateStr = null;
            switch (list.get(position).status) {
                case "1":
                    stateStr = "未消费";
                    holder.tvNum.setTextColor(getResources().getColor(R.color.color_h1));
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.color_h1));
                    holder.iv.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    stateStr = "已消费";
                    holder.tvNum.setTextColor(getResources().getColor(R.color.color_999));
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.color_999));
                    holder.iv.setVisibility(View.INVISIBLE);
                    break;
                case "3":
                    stateStr = "待退款";
                    holder.tvNum.setTextColor(getResources().getColor(R.color.color_999));
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.color_999));
                    holder.iv.setVisibility(View.INVISIBLE);
                    break;
                case "4":
                    stateStr = "已退款";
                    holder.tvNum.setTextColor(getResources().getColor(R.color.color_999));
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.color_999));
                    holder.iv.setVisibility(View.INVISIBLE);
                    break;
                case "":
                    stateStr = "";
                    holder.iv.setVisibility(View.INVISIBLE);
                    break;
            }
            if ("2".equals(info.orderInfo.type)) {
                holder.iv.setVisibility(View.INVISIBLE);
            } else {
                holder.iv.setVisibility(View.VISIBLE);
            }
            holder.tvState.setText(stateStr);

            return convertView;
        }

        class ViewHolder {
            TextView tv;
            TextView tvNum;
            ImageView iv;
            TextView tvState;
        }
    }


}
