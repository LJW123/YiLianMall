package com.yilian.mall.jd.activity;


import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.adapter.JDOrderDetailsGoodsListAdapter;
import com.yilian.mall.jd.enums.JdOrderStatusType;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.jd.JDCheckAvailableAfterSaleEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDOrderDetailEntity;
import com.yilian.networkingmodule.entity.jd.JDOrderLogisticsEntity;
import com.yilian.networkingmodule.entity.jd.jdEventBusModel.JDOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单详情
 *
 * @author Zg on 2017/5/26.
 */
public class JDOrderDetailsActivity extends BaseFragmentActivity implements View.OnClickListener {


    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle, tvRight;
    //警告 提醒
    private LinearLayout llWarnContent, llWarnClose;
    //订单状态
    private LinearLayout llOrderStatus;
    private ImageView ivOrderStatus;
    private TextView tvOrderStatus;
    //待支付倒计时
    private LinearLayout llWaitingPayment;
    private TextView tvOrderTimeRemaining, tvNeedPay;
    //物流信息
    private LinearLayout llOrderLogistic;
    private TextView tvOrderLogistic, tvOrderLogisticTime;
    //收货地址信息 - 顶部
    private LinearLayout llTopReceivingInformation;
    private TextView tvTopReceivingPerson, tvTopReceivingPhone, tvTopReceivingAddress;
    //商品信息
    private RecyclerView rvGoodsList;
    private JDOrderDetailsGoodsListAdapter goodsListAdapter;
    //    private LinearLayout llGoodsInfo;
//    private ImageView ivGoodsImg;
//    private TextView tvGoodsName, tvGoodsPrice, tvApplyAfterSales;
    //订单编号
    private TextView tvOrderNumber;
    //复制 订单编号
    private TextView tvOrderNumberCopy;
    //订单时间
    private TextView tvOrderTime;
    //支付方式
    private TextView tvOrderPayType;
    //收货地址信息 - 中部
    private LinearLayout llCentralReceivingInformation;
    private TextView tvCentralReceivingPerson, tvCentralReceivingPhone, tvCentralReceivingAddress;
    private LinearLayout llReturnBean;
    //               商品总额          平台附加赠送  运费    实付款
    private TextView tvGoodsTotalPrice, tvLeBeans, tvFreight, tvActualPayment;
    //底部菜单栏     删除订单    再次购买         确认收货      取消订单      去支付
    private TextView tvDelete, tvMenuBuyAgain, tvMenuConfirm, tvMenuCancel, tvMenuPay;
    /**
     * 京东订单号
     */
    private String jdOrderId;
    //订单信息
    private JDOrderDetailEntity.DataBean mData;
    //系统时间  等待付款时长
    private long marginTime;
    private TextView tvOrderDaiGouQuanMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_order_detail);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewByJDOrderList(R.id.vary_content, getWindow().getDecorView(), new RefreshClickListener());
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        //警告 提醒
        llWarnContent = findViewById(R.id.ll_warn_content);
        llWarnClose = findViewById(R.id.ll_warn_close);
        //订单状态
        llOrderStatus = findViewById(R.id.ll_order_status);
        ivOrderStatus = findViewById(R.id.iv_order_status);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        //待支付倒计时
        llWaitingPayment = findViewById(R.id.ll_waiting_payment);
        tvOrderTimeRemaining = findViewById(R.id.tv_order_time_remaining);
        tvNeedPay = findViewById(R.id.tv_need_pay);
        //物流信息
        llOrderLogistic = findViewById(R.id.ll_order_logistic);
        tvOrderLogistic = findViewById(R.id.tv_order_logistic);
        tvOrderLogisticTime = findViewById(R.id.tv_order_logistic_time);
        //收货地址信息 - 顶部
        llTopReceivingInformation = findViewById(R.id.ll_top_receiving_information);
        tvTopReceivingPerson = findViewById(R.id.tv_top_receiving_person);
        tvTopReceivingPhone = findViewById(R.id.tv_top_receiving_phone);
        tvTopReceivingAddress = findViewById(R.id.tv_top_receiving_address);
        //商品信息
        rvGoodsList = findViewById(R.id.rv_goods_list);
        rvGoodsList.setLayoutManager(new LinearLayoutManager(context));
        rvGoodsList.setNestedScrollingEnabled(false);
        goodsListAdapter = new JDOrderDetailsGoodsListAdapter();
        rvGoodsList.setAdapter(goodsListAdapter);
//        llGoodsInfo = findViewById(R.id.ll_goods_info);
//        ivGoodsImg = findViewById(R.id.iv_goods_img);
//        tvGoodsName = findViewById(R.id.tv_goods_name);
//        tvGoodsPrice = findViewById(R.id.tv_goods_price);
//        tvApplyAfterSales = findViewById(R.id.tv_apply_after_sales);
        //订单时间
        tvOrderNumber = findViewById(R.id.tv_order_number);
        //复制 订单编号
        tvOrderNumberCopy = findViewById(R.id.tv_order_number_copy);
        //订单时间
        tvOrderTime = findViewById(R.id.tv_order_time);
        //支付方式
        tvOrderPayType = findViewById(R.id.tv_order_pay_type);
        //收货地址信息 - 中部
        llCentralReceivingInformation = findViewById(R.id.ll_central_receiving_information);
        tvCentralReceivingPerson = findViewById(R.id.tv_central_receiving_person);
        tvCentralReceivingPhone = findViewById(R.id.tv_central_receiving_phone);
        tvCentralReceivingAddress = findViewById(R.id.tv_central_receiving_address);
        //商品总额 平台附加赠送  运费   实付款
        tvGoodsTotalPrice = findViewById(R.id.tv_goods_total_price);
        tvOrderDaiGouQuanMoney = findViewById(R.id.tv_dai_gou_quan_money);
        llReturnBean = findViewById(R.id.ll_return_bean);
        tvLeBeans = findViewById(R.id.tv_le_beans);
        tvFreight = findViewById(R.id.tv_freight);
        tvActualPayment = findViewById(R.id.tv_actual_payment);
        //底部菜单栏
        tvDelete = findViewById(R.id.tv_delete);
        tvMenuBuyAgain = findViewById(R.id.tv_menu_buy_again);
        tvMenuConfirm = findViewById(R.id.tv_menu_confirm);
        tvMenuCancel = findViewById(R.id.tv_menu_cancel);
        tvMenuPay = findViewById(R.id.tv_menu_pay);
    }

    public void initData() {
        tvTitle.setText("订单详情");
        jdOrderId = getIntent().getStringExtra("jdOrderId");
        getOrdersDetails();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        llWarnClose.setOnClickListener(this);
        llOrderLogistic.setOnClickListener(this);
        tvOrderNumberCopy.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvMenuBuyAgain.setOnClickListener(this);
        tvMenuConfirm.setOnClickListener(this);
        tvMenuCancel.setOnClickListener(this);
        tvMenuPay.setOnClickListener(this);

        goodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDOrderDetailEntity.GoodsList goods = (JDOrderDetailEntity.GoodsList) adapter.getItem(position);
                //0普通京东商品  1购物卡京东商品
                if (mData.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
                    JumpCardActivityUtils.toGoodsDetail(context, goods.skuId);
                } else {
                    JumpJdActivityUtils.toGoodsDetail(context, goods.skuId);
                }

            }
        });

        goodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                JDOrderDetailEntity.GoodsList goods = (JDOrderDetailEntity.GoodsList) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_apply_after_sales:
                        //申请售后
                        //判断是否可申请售后
                        checkAvailableAftersale(goods.skuId, String.valueOf(goods.skuNum));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 订单详情
     */
    private void getOrdersDetails() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJDOrderetails("jd_orders/jd_order_info", jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDOrderDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDOrderDetailEntity entity) {
                        mData = entity.data;
                        if (mData != null) {
                            setData(mData);
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 京东政企判断订单中的商品是否可以申请售后
     */
    private void checkAvailableAftersale(String skuId, String skuNum) {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                jdCheckAvailableAftersaler("jd_aftersale/jd_check_available_aftersale", mData.jdOrderId, skuId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            JumpJdActivityUtils.toJDAfterSaleApplyFor(context, mData.jdOrderId, skuId, skuNum);
                        } else {
                            showToast("暂不可申请售后");
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 绑定数据
     */
    private void setData(JDOrderDetailEntity.DataBean mData) {
        initDataView();
        // 京东商品类型为 购物卡 时，不显示益豆
        if (mData.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            llReturnBean.setVisibility(View.GONE);
        } else {
            llReturnBean.setVisibility(View.VISIBLE);
        }
        //计算 服务器时间 与 系统时间的差值
        long timeStampSec = System.currentTimeMillis() / 1000;
        marginTime = mData.sysTime - timeStampSec;
        //*************** 商品信息  *************
        goodsListAdapter.type = mData.type;
        goodsListAdapter.setNewData(mData.goodsList);
//        GlideUtil.showImageWithSuffix(context, JDImageUtil.getJDImageUrl_N3(mData.skuPic), ivGoodsImg);
//        tvGoodsName.setText(mData.skuName);
//        tvGoodsPrice.setText("¥" + mData.skuJdPrice);
        //*************** 订单信息  *************
        tvOrderNumber.setText(String.format("订单编号：%s", mData.jdOrderId));
        tvOrderTime.setText(String.format("下单时间：%s", DateUtils.stampToDate(mData.orderTime * 1000)));
        tvOrderPayType.setText(String.format("支付方式：%s", mData.paymentTypeStr));
        tvGoodsTotalPrice.setText(String.format("¥%s", mData.orderJdPrice));
//        tvOrderDaiGouQuanMoney.setText(String.format("¥%s", mData.orderDaiGouQuanMoney));
        tvLeBeans.setText(String.format("+%s", mData.returnBean));
        tvFreight.setText(String.format("+%s", mData.freight));
//        tvActualPayment.setText(String.format("¥%s", new BigDecimal(String.valueOf(mData.orderJdPrice)).add(new BigDecimal(String.valueOf(mData.freight))).subtract(new BigDecimal(mData.orderDaiGouQuanMoney))));
        tvActualPayment.setText(String.format("¥%s", new BigDecimal(String.valueOf(mData.orderJdPrice)).add(new BigDecimal(String.valueOf(mData.freight)))));
        if (mData.type == JdOrderStatusType.waitPay) {
            //*************** 顶部订单状态 *************
            ivOrderStatus.setImageResource(R.mipmap.jd_order_details_top_time);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //此处相当于布局文件中的Android:layout_gravity属性
            param.gravity = Gravity.TOP;
            llOrderStatus.setLayoutParams(param);
            tvOrderStatus.setText("等待付款");
            llWaitingPayment.setVisibility(View.VISIBLE);
            tvNeedPay.setText("¥" + MyBigDecimal.add(mData.orderJdPrice, mData.freight));
            //等待付款倒计时
            toCountdown(mData);
            //*************** 物流信息  *************
            llOrderLogistic.setVisibility(View.GONE);
            //*************** 顶部收货信息  *************
            llTopReceivingInformation.setVisibility(View.VISIBLE);
            tvTopReceivingPerson.setText(mData.linkName);
            tvTopReceivingPhone.setText(mData.linkMobile);
            tvTopReceivingAddress.setText(mData.address);
            //*************** 中部收货信息  *************
            llCentralReceivingInformation.setVisibility(View.GONE);
            //*************** 底部菜单  *************
            tvMenuCancel.setVisibility(View.VISIBLE);
            tvMenuPay.setVisibility(View.VISIBLE);
        } else if (mData.type == JdOrderStatusType.waitReceiving) {
            //*************** 顶部订单状态 *************
            ivOrderStatus.setImageResource(R.mipmap.jd_order_details_top_box);
            tvOrderStatus.setText("等待收货");
            llWaitingPayment.setVisibility(View.GONE);
            //*************** 物流信息  *************
            llOrderLogistic.setVisibility(View.VISIBLE);
            getJDOrderLogistics();
            //*************** 顶部收货信息  *************
            llTopReceivingInformation.setVisibility(View.VISIBLE);
            tvTopReceivingPerson.setText(mData.linkName);
            tvTopReceivingPhone.setText(mData.linkMobile);
            tvTopReceivingAddress.setText(mData.address);
            //*************** 中部收货信息  *************
            llCentralReceivingInformation.setVisibility(View.GONE);
            //*************** 底部菜单  *************
            tvMenuBuyAgain.setVisibility(View.VISIBLE);
            tvMenuBuyAgain.setBackgroundResource(R.drawable.bg_btn_jd_order_details_hollow);
            tvMenuBuyAgain.setTextColor(Color.parseColor("#003333"));
            tvMenuConfirm.setVisibility(View.VISIBLE);
            return;
        } else if (mData.type == JdOrderStatusType.completed) {
            //*************** 顶部订单状态 *************
            ivOrderStatus.setImageResource(R.mipmap.jd_order_details_top_finish);
            tvOrderStatus.setText("完成");
            llWaitingPayment.setVisibility(View.GONE);
            //*************** 是否可结算益豆  *************
            //当京东商品类型 为 0 普通京东商品
            //是否可申请结算 0 不可申请 1可申请 结合订单状态 比如订单完成的情况下 使用这个字段判断
            if (mData.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON && mData.applySettle == 1) {
                tvRight.setVisibility(View.VISIBLE);
            }
            //*************** 物流信息  *************
            llOrderLogistic.setVisibility(View.VISIBLE);
            tvOrderLogistic.setText("感谢您在京东购物，欢迎您下次再次光临！");
            tvOrderLogisticTime.setVisibility(View.GONE);
            //*************** 顶部收货信息  *************
            llTopReceivingInformation.setVisibility(View.GONE);
            //*************** 中部收货信息  *************
            llCentralReceivingInformation.setVisibility(View.VISIBLE);
            tvCentralReceivingPerson.setText(mData.linkName);
            tvCentralReceivingPhone.setText(mData.linkMobile);
            tvCentralReceivingAddress.setText(mData.address);
            //*************** 底部菜单  *************
            tvDelete.setVisibility(View.VISIBLE);
            tvMenuBuyAgain.setVisibility(View.VISIBLE);
        } else if (mData.type == JdOrderStatusType.canceled) {
            //*************** 顶部订单状态 *************
            ivOrderStatus.setImageResource(R.mipmap.jd_order_details_top_cancel);
            tvOrderStatus.setText("已取消");
            llWaitingPayment.setVisibility(View.GONE);
            //*************** 物流信息  *************
            llOrderLogistic.setVisibility(View.GONE);
            //*************** 顶部收货信息  *************
            llTopReceivingInformation.setVisibility(View.VISIBLE);
            tvTopReceivingPerson.setText(mData.linkName);
            tvTopReceivingPhone.setText(mData.linkMobile);
            tvTopReceivingAddress.setText(mData.address);
            //*************** 中部收货信息  *************
            llCentralReceivingInformation.setVisibility(View.GONE);
            //*************** 底部菜单  *************
            tvDelete.setVisibility(View.VISIBLE);
            tvMenuBuyAgain.setVisibility(View.VISIBLE);
        } else if (mData.type == JdOrderStatusType.other) {
            //*************** 顶部订单状态 *************
            llOrderStatus.setVisibility(View.GONE);
            llWaitingPayment.setVisibility(View.GONE);
            //*************** 物流信息  *************
            llOrderLogistic.setVisibility(View.GONE);
            //*************** 顶部收货信息  *************
            llTopReceivingInformation.setVisibility(View.VISIBLE);
            tvTopReceivingPerson.setText(mData.linkName);
            tvTopReceivingPhone.setText(mData.linkMobile);
            tvTopReceivingAddress.setText(mData.address);
            //*************** 中部收货信息  *************
            llCentralReceivingInformation.setVisibility(View.GONE);
            //*************** 底部菜单  *************
            tvMenuBuyAgain.setVisibility(View.VISIBLE);
        }
        //*************** 展示内容页面  *************
        varyViewUtils.showDataView();
    }

    private void initDataView() {
        tvRight.setVisibility(View.GONE);
        //默认隐藏菜单按钮
        tvDelete.setVisibility(View.GONE);
        tvMenuBuyAgain.setVisibility(View.GONE);
        tvMenuBuyAgain.setBackgroundResource(R.drawable.bg_btn_jd_order_details_solid);
        tvMenuBuyAgain.setTextColor(getResources().getColor(R.color.white));
        tvMenuConfirm.setVisibility(View.GONE);
        tvMenuCancel.setVisibility(View.GONE);
        tvMenuPay.setVisibility(View.GONE);
    }

    //等待付款倒计时
    private void toCountdown(JDOrderDetailEntity.DataBean mData) {
        long waitTime = mData.countDown - (mData.sysTime - mData.orderTime);
        if (waitTime > 0) {
            CountDownTimer timer = new CountDownTimer(waitTime * 1000, 1000) {
                /**
                 * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
                 *
                 * @param millisUntilFinished
                 */
                @Override
                public void onTick(long millisUntilFinished) {
                    tvOrderTimeRemaining.setText("剩余：" + formatTime(millisUntilFinished));
                }

                /**
                 * 倒计时完成时被调用
                 */
                @Override
                public void onFinish() {
                    tvOrderTimeRemaining.setText("剩余：00分钟00秒");
                }
            };
            timer.start();
        } else {
            tvOrderTimeRemaining.setText("剩余：00分钟00秒");
        }
    }

    /**
     * 查询物流信息
     */
    private void getJDOrderLogistics() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJDOrderLogistics("jd_orders/jd_orderTrack_info", mData.jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDOrderLogisticsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tvOrderLogistic.setText("获取物流信息失败");
                        tvOrderLogisticTime.setVisibility(View.GONE);
                        varyViewUtils.showDataView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDOrderLogisticsEntity entity) {
                        List<JDOrderLogisticsEntity.DataBean> mData = entity.data;
                        if (mData != null && mData.size() > 0) {
                            tvOrderLogistic.setText(mData.get(0).getContent());
                            tvOrderLogisticTime.setVisibility(View.VISIBLE);
                            tvOrderLogisticTime.setText(mData.get(0).getMsgTime());
                        } else {
                            tvOrderLogistic.setText("获取物流信息失败");
                            tvOrderLogisticTime.setVisibility(View.GONE);
                        }
                        varyViewUtils.showDataView();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 将毫秒转化为 分钟：秒 的格式
     *
     * @param millisecond 毫秒
     * @return
     */
    public String formatTime(long millisecond) {
        int hours;//小时
        int minute;//分钟
        int second;//秒数
        hours = (int) (millisecond / 1000 / 3600);
        minute = (int) ((millisecond / 1000) % 3600 / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return hours + "小时" + "0" + minute + "分钟" + "0" + second + "秒";
            } else {
                return hours + "小时" + "0" + minute + "分钟" + second + "秒";
            }
        } else {
            if (second < 10) {
                return hours + "小时" + minute + "分钟" + "0" + second + "秒";
            } else {
                return hours + "小时" + minute + "分钟" + second + "秒";
            }
        }
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您是否要删除该订单？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                        jdDeleteOrder("jd_orders/jd_delete_order", mData.jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            public void onNext(HttpResultBean bean) {
                                showToast(bean.msg);
                                //EventBus 推送
                                JDOrderListUpdateModel updateModel = new JDOrderListUpdateModel();
                                updateModel.handleType = JDOrderListUpdateModel.HandleType_delete;
                                updateModel.jdOrderId = mData.jdOrderId;
                                /**
                                 * {@link  com.yilian.mall.jd.fragment.order.JDOrderCommonFragment #refreshUserBasicInfo(JDOrderListUpdateModel)}
                                 */
                                EventBus.getDefault().post(updateModel);

                                finish();
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.tv_right:
                //申请结算
                applyOrderSettle();
                break;
            case R.id.ll_warn_close:
                //关闭提醒
                llWarnContent.setVisibility(View.GONE);
                break;
            case R.id.ll_order_logistic:
                //查看物流
                JumpJdActivityUtils.toJDOrderLogistics(context, mData.jdOrderId);
                break;
            case R.id.tv_order_number_copy:
                //复制订单编号
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(mData.jdOrderId);
                showToast("复制成功");
                break;
            case R.id.tv_delete:
                //删除订单
                deleteOrder();
                break;
            case R.id.tv_menu_buy_again:
                //再次购买
                jdBuyAgain();
                break;
            case R.id.tv_menu_confirm:
                //确认收货
                confirmGoods();
                break;
            case R.id.tv_menu_cancel:
                //取消订单
                cancleOrder();
                break;
            case R.id.tv_menu_pay:
                //去支付
                JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity = new JDCommitOrderSuccessEntity();
                jdCommitOrderSuccessEntity.goodsPrice = String.valueOf(mData.orderJdPrice);
                jdCommitOrderSuccessEntity.freight = String.valueOf(mData.freight);
                jdCommitOrderSuccessEntity.orderId = mData.jdOrderId;
                jdCommitOrderSuccessEntity.orderIndex = mData.orderIndex;
                jdCommitOrderSuccessEntity.countDown = String.valueOf(mData.countDown - (System.currentTimeMillis() / 1000 + marginTime - mData.orderTime));
//                jdCommitOrderSuccessEntity.daiGouQuanMoney = mData.orderDaiGouQuanMoney;
                jdCommitOrderSuccessEntity.daiGouQuanMoney = "0";
                if (mData.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
                    //购物卡 京东收银台
                    JumpCardActivityUtils.toJDCashDeskActivity(context, jdCommitOrderSuccessEntity);
                } else {
                    JumpJdActivityUtils.toJDCashDeskActivity(context, jdCommitOrderSuccessEntity);
                }

                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 申请结算益豆
     */
    @SuppressWarnings("unchecked")
    private void applyOrderSettle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("请在收到商品并确认无质量问题，且无需退换货之后，再确认申请益豆，确认后赠送的益豆立即到账，且购买的商品不可申请退款！");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                        jdApplyOrderSettle("jd_orders/jd_apply_order_settle", mData.orderIndex).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            public void onNext(HttpResultBean bean) {
                                showToast(bean.msg);
                                //刷新订单
                                getOrdersDetails();
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    /**
     * 京东再次购买
     */
    private void jdBuyAgain() {
        startMyDialog();
        StringBuilder goods_counts = new StringBuilder("");
        StringBuilder goods_skus = new StringBuilder("");
        for (int i = 0; i < mData.goodsList.size(); i++) {
            if (i < mData.goodsList.size() - 1) {
                goods_counts.append(mData.goodsList.get(i).skuNum).append(",");
            } else {
                goods_counts.append(String.valueOf(mData.goodsList.get(i).skuNum));
            }
            if (i < mData.goodsList.size() - 1) {
                goods_skus.append(mData.goodsList.get(i).skuId).append(",");
            } else {
                goods_skus.append(String.valueOf(mData.goodsList.get(i).skuId));
            }
        }
        String urlKey = "jd_cart/cart_add_more";
        if (mData.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            urlKey = "jd_card_cart/card_cart_add_more";
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                setJDBuyAgain(urlKey, goods_counts.toString(), goods_skus.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(HttpResultBean httpResultBean) {
                        if (mData.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
                            JumpCardActivityUtils.toCardJdShoppingList(context, goods_skus.toString());
                        } else {
                            JumpJdActivityUtils.toJdHomePageActivity(context, Constants.JD_INDEX_SHOPPING_CAR, goods_skus.toString());
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 确认收货
     */
    private void confirmGoods() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您是否收到该订单商品？");
        builder.setNegativeButton("未收货", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("已收货", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                        jdConfirmGoods("jd_orders/jd_confirm_goods", mData.jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            public void onNext(HttpResultBean bean) {
                                showToast(bean.msg);

                                //EventBus 推送
                                JDOrderListUpdateModel updateModel = new JDOrderListUpdateModel();
                                updateModel.handleType = JDOrderListUpdateModel.HandleType_receiving;
                                updateModel.jdOrderId = mData.jdOrderId;
                                /**
                                 * {@link  com.yilian.mall.jd.fragment.order.JDOrderCommonFragment #refreshUserBasicInfo(JDOrderListUpdateModel)}
                                 */
                                EventBus.getDefault().post(updateModel);

                                JumpJdActivityUtils.toJDOrderReceivingSuccess(context);
                                finish();
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    /**
     * 取消订单
     */
    private void cancleOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您是否要取消该订单？");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                        jdCancleOrder("jd_orders/jd_cancle_order", mData.jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            public void onNext(HttpResultBean bean) {
                                showToast(bean.msg);
                                //刷新订单
                                getOrdersDetails();
                                //EventBus 推送
                                JDOrderListUpdateModel updateModel = new JDOrderListUpdateModel();
                                updateModel.handleType = JDOrderListUpdateModel.HandleType_cancle;
                                updateModel.jdOrderId = mData.jdOrderId;
                                /**
                                 * {@link  com.yilian.mall.jd.fragment.order.JDOrderCommonFragment #refreshUserBasicInfo(JDOrderListUpdateModel)}
                                 */
                                EventBus.getDefault().post(updateModel);
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrdersDetails();
                }
            }, 1000);
        }
    }
}
