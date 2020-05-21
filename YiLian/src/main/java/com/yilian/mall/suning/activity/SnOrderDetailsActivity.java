package com.yilian.mall.suning.activity;


import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.suning.adapter.SnOrderDetailsGoodsListAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.suning.utils.SnConstantUtils;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mall.suning.fragment.order.SnOrderCommonFragment;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.suning.SnCommitOrderEntity;
import com.yilian.networkingmodule.entity.suning.SnOrderDetailEntity;
import com.yilian.networkingmodule.entity.suning.snEventBusModel.SnOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单详情
 *
 * @author Zg on 2017/7/28.
 */
public class SnOrderDetailsActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle, tvRight;

    private TextView tvOrderStatus;
    private TextView tvOrderStatusDescribe;
    //订单号信息
    private TextView tvOrderNum;
    private TextView tvCopy;
    //订单地址信息
    private TextView tvReceivePeople;
    private TextView tvReceivePhone;
    private TextView tvReceiveAddress;
    //订单商品列表
    private RecyclerView rvGoodsList;
    private SnOrderDetailsGoodsListAdapter goodsListAdapter;
    //订单支付信息
    private TextView tvGoodsPrice;
    private TextView tvDeductionTicket;
    private TextView tvFreight;
    private TextView tvBeans;
    private TextView tvPracticalMoney;
    private TextView tvOrderTime;
    //操作按钮
    private TextView tvDelete;
    private TextView tvCancel;
    private TextView tvPay;
    private TextView tvBuyAgain;
    private TextView tvApplyBalance;

    /**
     * 订单id
     */
    private String snOrderId;
    /**
     * 系统时间  等待付款时长
     */
    private long marginTime;
    //系统时间  付款限制时间
    private long systemTime, time;
    /**
     * 订单详情信息
     */
    private SnOrderDetailEntity.DataBean mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_activity_order_detail);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setSnVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
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
        });
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);

        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvOrderStatusDescribe = (TextView) findViewById(R.id.tv_order_status_describe);
        tvOrderNum = (TextView) findViewById(R.id.tv_order_num);
        tvCopy = (TextView) findViewById(R.id.tv_copy);
        tvReceivePeople = (TextView) findViewById(R.id.tv_receive_people);
        tvReceivePhone = (TextView) findViewById(R.id.tv_receive_phone);
        tvReceiveAddress = (TextView) findViewById(R.id.tv_receive_address);
        //订单商品列表
        rvGoodsList = (RecyclerView) findViewById(R.id.rv_goods_list);
        goodsListAdapter = new SnOrderDetailsGoodsListAdapter();
        rvGoodsList.setLayoutManager(new LinearLayoutManager(context));
        rvGoodsList.setNestedScrollingEnabled(false);
        rvGoodsList.setAdapter(goodsListAdapter);

        tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);
        tvDeductionTicket = (TextView) findViewById(R.id.tv_deduction_ticket);
        tvFreight = (TextView) findViewById(R.id.tv_freight);
        tvBeans = (TextView) findViewById(R.id.tv_beans);
        tvPracticalMoney = (TextView) findViewById(R.id.tv_practical_money);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        tvBuyAgain = (TextView) findViewById(R.id.tv_buy_again);
        tvApplyBalance = (TextView) findViewById(R.id.tv_apply_balance);
    }

    public void initData() {
        tvTitle.setText("订单详情");
        snOrderId = getIntent().getStringExtra("snOrderId");
        getOrdersDetails();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        goodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SnOrderDetailEntity.GoodsList goodsInfo = (SnOrderDetailEntity.GoodsList) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.ll_goods_info:
                        //商品详情
                        JumpSnActivityUtils.toSnGoodsDetailActivity(context, goodsInfo.getSkuId());
                        break;
                    case R.id.tv_affirm:
                        //确认收货
                        confirmGoods(goodsInfo.getSnOrderId(), goodsInfo.getSkuId());
                        break;
                    case R.id.tv_logistics:
                        //查看物流
                        JumpSnActivityUtils.toSnOrderLogistics(context, goodsInfo.getSnOrderId(), goodsInfo.getChildOrderId(), goodsInfo.getSkuId());
                        break;
                    case R.id.tv_apply_after_sales:
                        //申请售后
                        applyAfterSales(goodsInfo);

                        break;
                    default:
                        break;
                }
            }
        });
        tvCopy.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tvBuyAgain.setOnClickListener(this);
        tvApplyBalance.setOnClickListener(this);

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
                        snDeleteOrder("suning_orders/suning_delete_order", mData.snOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                                SnOrderListUpdateModel updateModel = new SnOrderListUpdateModel(SnOrderListUpdateModel.HandleType_delete, mData.snOrderId);
                                /**
                                 * {@link  SnOrderCommonFragment #refreshUserBasicInfo(SnOrderListUpdateModel)}
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

    /**
     * 申请售后
     */
    private void applyAfterSales(SnOrderDetailEntity.GoodsList goodsInfo) {
        //是否厂送商品 01-是；02-否
        if ("02".equals(goodsInfo.getIsFactorySend())) {
            JumpSnActivityUtils.toSnAfterSaleApplyFor(context, goodsInfo.getSnOrderId(), mData.orderSnPrice, mData.coupon,
                    goodsInfo.getSkuId(), goodsInfo.getSkuPic(), goodsInfo.getSkuName(), goodsInfo.getSnPrice(), goodsInfo.getSkuNum());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        snCancleOrder("suning_orders/suning_cancle_order", mData.snOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                                SnOrderListUpdateModel updateModel = new SnOrderListUpdateModel(SnOrderListUpdateModel.HandleType_cancle, mData.snOrderId);
                                /**
                                 * {@link  SnOrderCommonFragment #refreshUserBasicInfo(SnOrderListUpdateModel)}
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

    /**
     * 苏宁再次购买
     */
    private void snBuyAgain() {
        startMyDialog();
        StringBuilder goodsCounts = new StringBuilder("");
        StringBuilder goodsSkus = new StringBuilder("");
        for (int i = 0; i < mData.goodsList.size(); i++) {
            if (i < mData.goodsList.size() - 1) {
                goodsCounts.append(mData.goodsList.get(i).getSkuNum()).append(",");
            } else {
                goodsCounts.append(String.valueOf(mData.goodsList.get(i).getSkuNum()));
            }
            if (i < mData.goodsList.size() - 1) {
                goodsSkus.append(mData.goodsList.get(i).getSkuId()).append(",");
            } else {
                goodsSkus.append(String.valueOf(mData.goodsList.get(i).getSkuId()));
            }
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                putInShoppingCartMore("suning_cart/suning_cart_add_more", goodsCounts.toString(), goodsSkus.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("加入购物车成功，是否去结算？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("去购物车", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JumpSnActivityUtils.toSnHomePageActivity(context, SnHomePageActivity.TAB_CART, goodsSkus.toString());
                            }
                        });
                        builder.create();
                        builder.setCancelable(false);
                        builder.show();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 申请结算益豆
     */
    @SuppressWarnings("unchecked")
    private void applyOrderSettle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //是否大于等于7天 1 是 0 否 判断是否执行申请结算请求
        if (mData.is7day == 1) {
            //大于7天可申请售后
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
                            snApplyOrderSettle("suning_orders/suning_apply_order_settle", mData.orderIndex).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        } else {
            //小于7天可申请售后
            builder.setMessage("确认收货7天之后，可以申请益豆，请在七天后申请。");
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    /**
     * 确认收货
     */
    private void confirmGoods(String snOrderId, String skuId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("请收到货后，再确认收货！否则您可能钱货两空哦");
        builder.setNegativeButton("未收到货", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("已收到货", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                        snConfirmGoods("suning_orders/suning_confirm_goods", snOrderId, skuId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                                SnOrderListUpdateModel updateModel = new SnOrderListUpdateModel(SnOrderListUpdateModel.HandleType_receiving, mData.snOrderId);
                                /**
                                 * {@link  SnOrderCommonFragment #refreshUserBasicInfo(JDOrderListUpdateModel)}
                                 */
                                EventBus.getDefault().post(updateModel);

                                showToast("收货成功");
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
     * 订单详情
     */
    private void getOrdersDetails() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getSnOrderetails("suning_orders/suning_order_info", snOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnOrderDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SnOrderDetailEntity entity) {
                        systemTime = entity.systemTime;
                        time = entity.time;
                        mData = entity.data;
                        if (mData != null) {
                            setData(mData);
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }    //按钮的点击事件

    /**
     * 绑定数据
     *
     * @param mData 订单信息
     */
    private void setData(SnOrderDetailEntity.DataBean mData) {
        initDataView();
        //计算 服务器时间 与 系统时间的差值
        long timeStampSec = System.currentTimeMillis() / 1000;
        marginTime = systemTime - timeStampSec;

        tvOrderNum.setText(mData.snOrderId);
        //*************** 地址信息  *************
        tvReceivePeople.setText(mData.name);
        tvReceivePhone.setText(mData.mobile);
        tvReceiveAddress.setText(Html.fromHtml("<font color='#999999'>收货地址：</font>" + mData.province + mData.city + mData.county + mData.address));
        //*************** 商品信息  *************
        if (mData.goodsList != null && mData.goodsList.size() > 0) {
            goodsListAdapter.setSettleStatus(mData.settleStatus);
            goodsListAdapter.setNewData(mData.goodsList);
        }
        //*************** 订单支付信息  *************
        tvGoodsPrice.setText(mData.orderSnPrice);

//        tvDeductionTicket.setText(mData.coupon);
        tvFreight.setText(mData.freight);
        tvBeans.setText(mData.returnBean);
        tvPracticalMoney.setText(mData.payment);
        tvOrderTime.setText(DateUtils.stampToDate(mData.orderTime * 1000));


        if (mData.orderStatus == 1 || mData.orderStatus == 9) {
            /** 待支付*/
            tvOrderStatus.setText(String.format("待支付：¥ %s", mData.payment));
            tvOrderStatusDescribe.setVisibility(View.VISIBLE);
            toCountdown(mData, systemTime, time);
            tvCancel.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.VISIBLE);
        } else if (mData.orderStatus == 2) {
            /** 待发货*/
            tvOrderStatus.setText("待发货");
            tvBuyAgain.setTextColor(getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else if (mData.orderStatus == 3) {
            /** 待收货*/
            tvOrderStatus.setText("待收货");
            tvBuyAgain.setTextColor(getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else if (mData.orderStatus == 4) {
            /** 已完成*/
            tvOrderStatus.setText("已完成");
            tvDelete.setVisibility(View.VISIBLE);
            tvBuyAgain.setVisibility(View.VISIBLE);
            if (mData.settleStatus == 0 && mData.applySettle == 1) {
                tvApplyBalance.setTextColor(getResources().getColor(R.color.color_main_suning));
                tvApplyBalance.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
                tvApplyBalance.setClickable(true);
            } else {
                tvApplyBalance.setTextColor(getResources().getColor(R.color.notice_text_color));
                tvApplyBalance.setBackgroundResource(R.drawable.sn_order_bt_bg_black);
                tvApplyBalance.setClickable(false);
            }
            tvApplyBalance.setVisibility(View.VISIBLE);
        } else if (mData.orderStatus == 5 || mData.orderStatus == 8) {
            /** 已取消*/
            tvOrderStatus.setText("已取消");
            tvDelete.setVisibility(View.VISIBLE);
            tvBuyAgain.setTextColor(getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else {
            tvOrderStatus.setVisibility(View.GONE);
            if (mData.orderStatus == 6) {
                /** 已退货*/
                tvOrderStatus.setText("已退货");
                tvOrderStatus.setVisibility(View.VISIBLE);
            }
            if (mData.orderStatus == 7) {
                /** 待处理*/
                tvOrderStatus.setText("待处理");
                tvOrderStatus.setVisibility(View.VISIBLE);
            }
            tvBuyAgain.setTextColor(getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        }
        //*************** 展示内容页面  *************
        varyViewUtils.showDataView();
    }

    private void initDataView() {
        tvRight.setVisibility(View.GONE);
        //默认隐藏菜单按钮
        tvOrderStatusDescribe.setVisibility(View.GONE);
        tvDelete.setVisibility(View.GONE);
        tvCancel.setVisibility(View.GONE);
        tvPay.setVisibility(View.GONE);
        tvBuyAgain.setVisibility(View.GONE);
        tvApplyBalance.setVisibility(View.GONE);
    }

    //等待付款倒计时
    private void toCountdown(SnOrderDetailEntity.DataBean mData, long systemTime, long time) {
        long waitTime = time - (systemTime - mData.orderTime);
        if (waitTime > 0) {
            CountDownTimer timer = new CountDownTimer(waitTime * 1000, 1000) {
                /**
                 * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
                 *
                 * @param millisUntilFinished
                 */
                @Override
                public void onTick(long millisUntilFinished) {
                    String format = "HH小时mm分钟ss秒";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                    String video_time = sdf.format(Long.valueOf(millisUntilFinished));
                    tvOrderStatusDescribe.setText(Html.fromHtml("将于<font color='#FFD700'>" + video_time + "</font>后自动取消"));
                }

                /**
                 * 倒计时完成时被调用
                 */
                @Override
                public void onFinish() {
                    tvOrderStatusDescribe.setText(Html.fromHtml("将于<font color='#FFD700'>0时00分钟00秒</font>后自动取消"));
                }
            };
            timer.start();
        } else {
            tvOrderStatusDescribe.setText(Html.fromHtml("将于<font color='#FFD700'>0时00分钟00秒</font>后自动取消"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.tv_copy:
                //复制订单编号
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(mData.snOrderId);
                showToast("复制成功");
                break;
            case R.id.tv_delete:
                //删除订单
                deleteOrder();
                break;
            case R.id.tv_cancel:
                //取消订单
                cancleOrder();
                break;
            case R.id.tv_pay:
                //去支付
                SnCommitOrderEntity snCommitOrderEntity = new SnCommitOrderEntity();
                snCommitOrderEntity.orderId = mData.orderId;
                snCommitOrderEntity.snOrderId = mData.snOrderId;
//                snCommitOrderEntity.amount = MyBigDecimal.add(MyBigDecimal.sub(String.valueOf(mData.payment), String.valueOf(mData.freight)), mData.coupon);
                snCommitOrderEntity.amount = MyBigDecimal.sub(String.valueOf(mData.payment), String.valueOf(mData.freight));
                snCommitOrderEntity.freight = String.valueOf(mData.freight);
                snCommitOrderEntity.id = mData.orderIndex;
//                snCommitOrderEntity.daiGouQuanMoney = mData.coupon;
                snCommitOrderEntity.daiGouQuanMoney = "0";
                //剩余支付时间 = 等待支付限制时间 - （本地时间 + 本地时间和后台系统时间的差值 - 订单时间）
                snCommitOrderEntity.time = String.valueOf(time - (System.currentTimeMillis() / 1000 + marginTime - mData.orderTime));
                JumpSnActivityUtils.toSnCashDeskActivity(context, snCommitOrderEntity);

                finish();
                break;
            case R.id.tv_buy_again:
                //再次购买
                snBuyAgain();
                break;
            case R.id.tv_apply_balance:
                //申请结算
                applyOrderSettle();
                break;
            default:
                break;
        }
    }


}
