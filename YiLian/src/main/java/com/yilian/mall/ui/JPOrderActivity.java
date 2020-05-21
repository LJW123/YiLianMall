package com.yilian.mall.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.RayViewHolder;
import com.yilian.mall.entity.AliCustomerServiceInfo;
import com.yilian.mall.entity.CreateServiceOrderParameter;
import com.yilian.mall.entity.DeleteOrderEntity;
import com.yilian.mall.entity.MallCancelMallorderEntity;
import com.yilian.mall.entity.MallConfirmMallorderEntity;
import com.yilian.mall.entity.MallOrderInfoEntity;
import com.yilian.mall.entity.UserRecordGatherEntity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.UserdataNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.AliCustomerServiceChatUtils;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.networkingmodule.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ray_L_Pain on 2016/11/9 0009.
 * 订单详情
 */

public class JPOrderActivity extends BaseActivity implements View.OnClickListener {
    //0普通商品 1中奖商品
    public String mOrderTurn;
    //0未发奖励 1已送奖券
    public int mProifit;
    /**
     * 5 6 为 猜价格 碰运气商品
     */
    public String orderType;
    @ViewInject(R.id.ll_freight)
    LinearLayout llFreight;//运费条目
    @ViewInject(R.id.ll_total_price)
    LinearLayout llTotalPrice;//订单总价条目
    //    @ViewInject(R.id.ll_intergral)
//    LinearLayout llIntergral;
    @ViewInject(R.id.ll_ledou)
    LinearLayout llLeDou;
    @ViewInject(R.id.tv_back_ledou)
    TextView tvBackLeDou;
    @ViewInject(R.id.tv_all_subsidy)
    TextView tvAllSubsidy; //平台加赠益豆
    @ViewInject(R.id.tv_dai_gou_quan)
    TextView tvDaiGouQuan;

//    @ViewInject(R.id.tv_back_score)
//    TextView tvBackScore;//销售奖券
    /**
     * 普通订单下单时间/大转盘订单兑换时间
     */
    @ViewInject(R.id.tv_wheel_order_convert_time)
    TextView tvWheelOrderConvertTime;
    /**
     * 返回
     */
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    /**
     * 右上角
     */
    @ViewInject(R.id.v3Right)
    TextView tvRight;
    @ViewInject(R.id.sv)
    PullToRefreshScrollView sv;
    /**
     * 左上角的状态
     */
    @ViewInject(R.id.tv_state)
    TextView stateTv;
    /**
     * 订单追踪
     */
    @ViewInject(R.id.tv_follow)
    TextView followTv;
    /**
     * 订单号
     */
    @ViewInject(R.id.tv_order_num)
    TextView orderNum;
    /**
     * 商品总价
     */
    @ViewInject(R.id.tv_order_price1)
    TextView orderPrice1;
    /**
     * 奖券价格
     */
    @ViewInject(R.id.tv_order_integral_price)
    TextView orderIntegralPrice1;
    @ViewInject(R.id.tv_score)
    TextView iconFen1;
    @ViewInject(R.id.tv_icon_juan)
    TextView iconJuan1;
    /**
     * 订单运费
     */
    @ViewInject(R.id.tv_order_fare)
    TextView orderFare;
    /**
     * 订单总价
     */
    @ViewInject(R.id.tv_order_money1)
    TextView orderMoney1;
    /**
     * 奖券购/易划算订单总价
     */
    @ViewInject(R.id.tv_order_integral_money2)
    TextView orderIntegralMoney2;
    @ViewInject(R.id.tv_icon_fen2)
    TextView iconFen2;
    @ViewInject(R.id.tv_icon_juan2)
    TextView iconJuan2;
    /**
     * 名字
     */
    @ViewInject(R.id.tv_order_name)
    TextView orderName;
    /**
     * 电话
     */
    @ViewInject(R.id.tv_order_phone)
    TextView orderPhone;
    /**
     * 地址
     */
    @ViewInject(R.id.tv_order_address)
    TextView orderAddress;
    @ViewInject(R.id.listView)
    NoScrollListView listView;
    /**
     * 底部layout
     */
    @ViewInject(R.id.layout_bottom)
    RelativeLayout bottomLayout;
    /**
     * 底部layout里的字
     */
    @ViewInject(R.id.tv_bottom)
    TextView bottomTv;
    @ViewInject(R.id.iv_pay_que)
    ImageView ivPayQue;
    @ViewInject(R.id.tv_contact_customer_service)
    TextView tvConntactCustomerService;
    /**
     * 兑奖时间
     */
    @ViewInject(R.id.ll_user_time)
    LinearLayout llUserTime;
    @ViewInject(R.id.tv_use_time)
    TextView tvUserTime;
    /**
     * 打电话
     */
    @ViewInject(R.id.ll_contact_customer_phone)
    LinearLayout llCallPhone;
    @ViewInject(R.id.tv_contact_customer_phone)
    TextView tvCallPhone;
    @ViewInject(R.id.ll_contact_customer_service)
    LinearLayout llContactCustomerService;
    MallNetRequest request;
    UserdataNetRequest userdataNetRequest;
    @ViewInject(R.id.ll_content)
    private LinearLayout llContent;
    @ViewInject(R.id.iv_nothing)
    private ImageView ivNothing;
    private String orderIndex;
    private String filialeId, orderCreateTime;
    private double buyPrice;
    private int bottomState, givingPrice;
    private String totalLefen;
    private String totalCoupon;
    private MallOrderInfoEntity entity;
    private List<MallOrderInfoEntity.Parcels> outerList = new ArrayList<>();
    private JPOrderOuterAdapter outerAdapter;
    private List<MallOrderInfoEntity.Goods> innerList = new ArrayList<>();
    private long canApplyAfterSaleTime;
    private String customerServicePersonId;
    private String customerServiceGroupId;
    @ViewInject(R.id.tv_apply_after_sale)
    private TextView tvTitleApplyAfterSale;
    private String telPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_jp);
        ViewUtils.inject(this);

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
    }

    private void initListener() {
        tvTitleApplyAfterSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(null, "申请售后操作会将此订单变更为确认收货状态,是否确定此订单已全部收货?", null, 0, Gravity.CENTER, "确定", "取消", true,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
//                                        确认收货
                                        confirmReceipt();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }, mContext);
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {

            private String cancleOrderMessage;

            @Override
            public void onClick(View v) {
                Logger.i("  entity.order_status " + entity.order_status);
                switch (entity.order_status) {//订单状态：0已取消，1待支付2待出库3正在出库4已部分发货5已全部发货&待收货6已完成7换货维修 8.退款
                    case 0:
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
                                            default:
                                                break;
                                        }
                                    }
                                }, mContext);
                        break;
                    case 6:
                        if (entity.supplyStatus == 1 && "0".equals(mOrderTurn)) {
                            showDialog(null, "确认申请结算之后，" + Constants.APP_PLATFORM_DONATE_NAME + "即刻到账，且商品不可申请退款！是否申请立即结算？", null, 0, Gravity.CENTER, "确认", "再想想", false,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.dismiss();
                                                    break;
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    dialog.dismiss();
                                                    applyIntegral();
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }, mContext);
                        }
                        break;
                    default:
                        cancleOrderMessage = null;
                        if ("1".equals(mOrderTurn)) {
                            //中奖订单
                            String time = DateUtils.timeStampToStr(entity.unUseTime);
                            cancleOrderMessage = "该奖品将在" + time + "失效,失效后将不能再兑换,您确定取消该订单吗?";
                        } else if (mOrderTurn.equals("0")) {
                            //普通订单
                            cancleOrderMessage = "您确定取消该订单吗?";
                        }
                        showDialog("取消订单", cancleOrderMessage, null, 0, Gravity.CENTER, "是",
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
                                            default:
                                                break;
                                        }
                                    }
                                }, mContext);
                        break;
                }
            }
        });
    }

    /**
     * 申请发奖励
     */
    private void applyIntegral() {
        com.yilian.networkingmodule.retrofitutil.RetrofitUtils.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext))
                .applyIntegral(entity.order_index, new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        showToast("申请结算成功");
                                        //刷新个人页面标识
                                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                        finish();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    private void initView() {
        orderIndex = getIntent().getStringExtra("order_index");
        tvConntactCustomerService.setOnClickListener(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPOrderActivity.this.finish();
            }
        });
        followTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳到订单追踪
                if (entity == null) {
                    showToast(R.string.service_exception);
                    return;
                }
                Intent intent = new Intent(JPOrderActivity.this, OrderRoutingActivity.class);
                intent.putExtra("title", "订单追踪");
                intent.putExtra("orderTime", entity.order_time);
                intent.putExtra("paymentTime", entity.payment_time);
                intent.putExtra("waitTime", "00");
                intent.putExtra("deliveryTime", entity.parcels.get(entity.parcels.size() - 1).express_time);
                intent.putExtra("confirmTime", entity.confirmTime);

                startActivity(intent);
            }
        });
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bottomState) {
                    case 0:
                        pay();
                        break;
                    case 1:
                        showDialog(null, "您要确认收货吗?", null, 0, Gravity.CENTER, "是",
                                "否", true, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                dialog.dismiss();
                                                confirmReceipt();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.dismiss();
                                                initView();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }, mContext);
                        break;
                    default:
                        bottomLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
        ivPayQue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ActCashGuaranteeActivity.class));
            }
        });

        sv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
                sv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);

        llCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        RequestOftenKey.getUserInfor(mContext, sp);//进入该页面时及时刷新用户信息，以保证用户取消订单时对零购券数量的判断准确
//        getCustomerServiceInfo();
        if (request == null) {
            request = new MallNetRequest(mContext);
        }
        Logger.i("orderIndex  " + orderIndex);
        getMallOrderInfo();
    }

    /**
     * 获取订单详情
     */
    private void getMallOrderInfo() {
        request.mallOrderInfo(orderIndex, MallOrderInfoEntity.class, new RequestCallBack<MallOrderInfoEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MallOrderInfoEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:


                        llContent.setVisibility(View.VISIBLE);
                        ivNothing.setVisibility(View.GONE);
                        entity = responseInfo.result;

                        // DaiGouQuan: 2018/8/7 代购券业务暂时隐藏
                        entity.orderDaiGouQuanPrice="0";

                        orderType = entity.orderType;
                        Logger.i("entity.confirmTime:" + entity.confirmTime + "  entity.serverTime:" + entity.serverTime);
                        canApplyAfterSaleTime = (Long.valueOf(entity.confirmTime) - Long.valueOf(entity.serverTime)) / (3600 * 24);//转换成天数
                        Logger.i("订单天数：" + canApplyAfterSaleTime);
                        filialeId = entity.filiale_id;
                        buyPrice = Integer.parseInt(entity.order_total_price) / 100;//订单商品乐享币和运费乐享币总价
                        orderCreateTime = entity.order_time;//下单时间
                        tvTitleApplyAfterSale.setVisibility(View.GONE);//售后view只有在orderStatus为6才显示，这里统一设置为gone，保证用户操作导致orderStatus改变，刷新界面时，界面正常
                        tvRight.setVisibility(View.GONE);//该图标先设置为Gone 具体显示由orderStatus状态决定，这里统一设置为gone，保证用户操作导致orderStatus改变，刷新界面时，界面正常
                        bottomLayout.setVisibility(View.GONE);//该图标先设置为Gone 具体显示由orderStatus状态决定，这里统一设置为gone，保证用户操作导致orderStatus改变，刷新界面时，界面正常
                        mOrderTurn = entity.orderTurns;
                        mProifit = entity.profitStatus;
                        switch (entity.order_status) {
                            case 0:
                                stateTv.setText("已取消");
                                tvRight.setVisibility(View.VISIBLE);
                                tvRight.setText("删除订单");
                                break;
                            case 1:
                                stateTv.setText("待支付");
                                bottomLayout.setVisibility(View.VISIBLE);
                                switch (entity.orderType) {
                                    case "5":
                                    case "6":
                                        bottomTv.setText("支付押金");
                                        ivPayQue.setVisibility(View.VISIBLE);
                                        break;
                                    default:
                                        bottomTv.setText("立即支付");
                                        ivPayQue.setVisibility(View.GONE);
                                        break;
                                }
                                bottomState = 0;
                                if ("1".equals(mOrderTurn) && entity.unUseTime <= Long.parseLong(entity.serverTime)) {
                                    tvRight.setVisibility(View.INVISIBLE);
                                } else {
                                    tvRight.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 2:
                                stateTv.setText("待出库");
                                if ("1".equals(mOrderTurn) && entity.unUseTime <= Long.parseLong(entity.serverTime)) {
                                    tvRight.setVisibility(View.INVISIBLE);
                                } else {
                                    tvRight.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 3:
                                stateTv.setText("正在出库");
                                tvRight.setVisibility(View.INVISIBLE);
                                break;
                            case 4:
                                stateTv.setText("已部分发货");
                                tvRight.setVisibility(View.GONE);
                                if ("1".equals(mOrderTurn)) {
                                    tvTitleApplyAfterSale.setVisibility(View.INVISIBLE);
                                } else {
                                    tvTitleApplyAfterSale.setVisibility(View.VISIBLE);
                                }
                                bottomLayout.setVisibility(View.VISIBLE);
                                bottomTv.setText("确认收货");
                                bottomState = 1;
                                break;
                            case 5:
                                stateTv.setText("待收货");
                                tvRight.setVisibility(View.GONE);
                                if ("1".equals(mOrderTurn)) {
                                    tvTitleApplyAfterSale.setVisibility(View.INVISIBLE);
                                } else {
                                    tvTitleApplyAfterSale.setVisibility(View.VISIBLE);
                                }
                                bottomLayout.setVisibility(View.VISIBLE);
                                bottomTv.setText("确认收货");
                                bottomState = 1;
                                break;
                            case 6:
                                stateTv.setText("已完成");
                                if (NumberFormat.convertToFloat(entity.orderDaiGouQuanPrice,0)<=0) {
//                                    不使用代购券时才能结算
                                    if (entity.supplyStatus == 1 && entity.flag == 1 && "0".equals(mOrderTurn)) {
                                        tvRight.setText("申请结算");
                                        tvRight.setVisibility(View.VISIBLE);
                                    } else {
                                        if (mProifit == 1) {
                                            stateTv.setText("已结算");
                                        }
                                        tvRight.setVisibility(View.INVISIBLE);
                                    }
                                }else{
                                    tvRight.setVisibility(View.INVISIBLE);
                                }

                                break;
                            case 7:
                                stateTv.setText("换货维修");
                                tvRight.setVisibility(View.INVISIBLE);
                                break;
                            case 8:
                                stateTv.setText("退款");
                                tvRight.setVisibility(View.INVISIBLE);
                                break;
                            default:
                                stateTv.setVisibility(View.GONE);
                                tvRight.setVisibility(View.INVISIBLE);
                                break;
                        }
                        //订单号
                        orderNum.setText(entity.order_id);
                        //订单运费
                        orderFare.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(entity.order_freight_price)));
//                        代购券抵扣
                        tvDaiGouQuan.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(entity.orderDaiGouQuanPrice)));
                        tvWheelOrderConvertTime.setText(DateUtils.timeStampToStr(NumberFormat.convertToLong(entity.order_time, 0L)));
                        //一个订单可能包含很多种类型的商品所以没有type字段

                        Logger.i("2017年12月20日 09:07:31-" + orderType);
                        Logger.i("2017年12月20日 09:07:31商品总价-价格-" + entity.order_goods_price);
                        Logger.i("2017年12月20日 09:07:31商品总价-活动奖券-" + entity.goodsIntegralV2);
                        Logger.i("2017年12月20日 09:07:31商品总价-正常奖券-" + entity.orderIntegralPrice);
                        Logger.i("2017年12月20日 09:07:31订单总价-价格-" + entity.order_total_price);
                        Logger.i("2017年12月20日 09:07:31订单总价-奖券-" + entity.orderIntegralPrice);
                        /**
                         * 商品总价-价格
                         */
                        if (!TextUtils.isEmpty(entity.order_goods_price)) {
                            String orderGoodsPriceAddDaiGouQuanMoney = String.valueOf(new BigDecimal(entity.order_goods_price)
                                    .add(new BigDecimal(entity.orderDaiGouQuanPrice)));
                            if (0 != NumberFormat.convertToFloat(orderGoodsPriceAddDaiGouQuanMoney, 0f)) {
                                orderPrice1.setVisibility(View.VISIBLE);
//                            orderPrice1.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(entity.order_goods_price)));
                                orderPrice1.setText(
                                        MoneyUtil.set¥Money(
                                                MoneyUtil.getLeXiangBi(
                                                        orderGoodsPriceAddDaiGouQuanMoney
                                                )));
                            } else {
                                orderPrice1.setVisibility(View.GONE);
                            }
                        } else {
                            orderPrice1.setVisibility(View.GONE);
                        }
                        /**
                         * 商品总价-奖券
                         * 这里奖券不能不显示，比如猜价格-碰运气有时会为0但是还是要显示
                         */
                        switch (orderType) {
                            case "5":
                            case "6":
                                if (!TextUtils.isEmpty(entity.goodsIntegralV2) && Double.parseDouble(entity.goodsIntegralV2) != 0) {
                                    orderIntegralPrice1.setVisibility(View.VISIBLE);
                                    if (orderPrice1.getVisibility() == View.VISIBLE) {
                                        orderIntegralPrice1.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(entity.goodsIntegralV2)));
                                    } else {
                                        orderIntegralPrice1.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(entity.goodsIntegralV2)));
                                    }
                                }
                                break;
                            default:
                                if (!TextUtils.isEmpty(entity.orderIntegralPrice) && Double.parseDouble(entity.orderIntegralPrice) != 0) {
                                    orderIntegralPrice1.setVisibility(View.VISIBLE);
                                    if (orderPrice1.getVisibility() == View.VISIBLE) {
                                        orderIntegralPrice1.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(entity.orderIntegralPrice)));
                                    } else {
                                        orderIntegralPrice1.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(entity.orderIntegralPrice)));
                                    }
                                } else {
                                    if (orderPrice1.getVisibility() == View.VISIBLE) {
                                        orderIntegralPrice1.setVisibility(View.GONE);
                                    } else {
                                        orderIntegralPrice1.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(entity.orderIntegralPrice)));
                                    }
                                }
                                break;
                        }
                        /**
                         * 订单总价-价格
                         */
                        if (!TextUtils.isEmpty(entity.order_total_price) && !"0".equals(entity.order_total_price)) {
                            orderMoney1.setVisibility(View.VISIBLE);
//                            orderMoney1.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(entity.order_total_price)));
                            orderMoney1.setText(
                                    MoneyUtil.set¥Money(
                                            MoneyUtil.getLeXiangBiNoZero(
                                                    String.valueOf(
                                                            new BigDecimal(entity.order_total_price)
                                                                    .add(new BigDecimal(entity.orderDaiGouQuanPrice))))));
                        } else {
                            orderMoney1.setVisibility(View.GONE);
                        }
                        /**
                         * 订单总价-奖券
                         */
                        if (!TextUtils.isEmpty(entity.orderIntegralPrice) && !"0".equals(entity.orderIntegralPrice)) {
                            orderIntegralMoney2.setVisibility(View.VISIBLE);
                            if (orderMoney1.getVisibility() == View.VISIBLE) {
                                orderIntegralMoney2.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(entity.orderIntegralPrice)));
                            } else {
                                orderIntegralMoney2.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(entity.orderIntegralPrice)));
                            }
                        } else {
                            if (orderMoney1.getVisibility() == View.VISIBLE) {
                                orderIntegralMoney2.setVisibility(View.GONE);
                            } else {
                                orderIntegralMoney2.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(entity.orderIntegralPrice)));
                            }
                        }

//                        tvBackScore.setText(MoneyUtil.getLeXiangBi(entity.returnIntegra));
                        tvBackLeDou.setText(MoneyUtil.getLeXiangBi(entity.allGivenBean));
                        tvAllSubsidy.setText(MoneyUtil.getLeXiangBi(entity.allSubsidy));

                        orderName.setText(entity.order_contacts);
                        telPhone = entity.tel;
                        tvCallPhone.setText(entity.tel);
                        Logger.i("telPhone:::  " + entity.tel);
                        orderPhone.setText(entity.order_phone);
                        orderAddress.setText(entity.order_address);

                        outerList = entity.parcels;
                        innerList = entity.goods;
                        if (null != outerList && null != innerList) {
                            ArrayList<MallOrderInfoEntity.Goods> list;
                            ArrayList<ArrayList<MallOrderInfoEntity.Goods>> outerListArray = new ArrayList<>();
                            for (MallOrderInfoEntity.Parcels parcel : outerList) {
                                list = new ArrayList<MallOrderInfoEntity.Goods>();
                                for (MallOrderInfoEntity.Goods good : innerList) {
                                    if (good.parcel_index.equals(parcel.parcel_index)) {
                                        list.add(good);
                                    }
                                }
                                outerListArray.add(list);
                                outerAdapter = new JPOrderOuterAdapter(mContext, outerList, outerListArray);
                                listView.setAdapter(outerAdapter);
                            }
                        }
                        break;
                    case -23:
                    case -4:
                        showToast(R.string.login_failure);
                        break;
                    case -3:
                        showToast(responseInfo.result.msg);
                        llContent.setVisibility(View.GONE);
                        ivNothing.setVisibility(View.VISIBLE);
                        Logger.i("2017年11月21日 11:28:30-走了这里");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {

        if (request == null) {
            request = new MallNetRequest(mContext);
        }
        request.deleteOrder(orderIndex, "0", new RequestCallBack<DeleteOrderEntity>() {
            @Override
            public void onStart() {
                startMyDialog();
                super.onStart();
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
                            //刷新个人页面标识
                            sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                            finish();
                            break;
                        default:
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

    /**
     * 获取订单客服信息
     */
    private void getCustomerServiceInfo() {
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext).getAliCustomerServiceInfo("2", orderIndex).enqueue(new Callback<AliCustomerServiceInfo>() {
                @Override
                public void onResponse(Call<AliCustomerServiceInfo> call, Response<AliCustomerServiceInfo> response) {
                    AliCustomerServiceInfo body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                            AliCustomerServiceInfo.DataBean data = body.data;
                            customerServicePersonId = data.personId;
                            customerServiceGroupId = data.groupId;
                        }
                    }
                }

                @Override
                public void onFailure(Call<AliCustomerServiceInfo> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Logger.i(e.getMessage());
            e.printStackTrace();
        }
    }

    //取消订单
    private void cancelOrder() {
        if (request == null) {
            request = new MallNetRequest(mContext);
        }
        request.cancelMallorder2(orderIndex, "", MallCancelMallorderEntity.class, new RequestCallBack<MallCancelMallorderEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MallCancelMallorderEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("成功取消订单");
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        finish();
                        break;
                    default:
                        showToast(responseInfo.result.msg);
                        break;

                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    private void pay() {
        if (userdataNetRequest == null) {
            userdataNetRequest = new UserdataNetRequest(mContext);
        }
        userdataNetRequest.getUserRecordGather(UserRecordGatherEntity.class, new RequestCallBack<UserRecordGatherEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserRecordGatherEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        sendNextActivity(responseInfo.result.coupon, orderCreateTime, orderIndex);
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

    private void sendNextActivity(String coupon, String orderCreateTime, String orderIndex) {
        Intent intent = new Intent(this, CashDeskActivity2.class);
        totalLefen = "0";

        intent.putExtra(CashDeskActivity2.PAY_FROM_TAG, PayFrom.GOODS_ORDER);
        intent.putExtra("orderIndex", orderIndex);
        intent.putExtra("order_total_lebi", entity.order_total_price);
        intent.putExtra("payType", "1");//1商城订单 2 商家入驻或续费 3店内支付

        startActivity(intent);
        finish();
    }

    void balanceLack() {
        showDialog("提示", "奖励不足", "请去充值", R.mipmap.ic_warning, Gravity.RIGHT, "确定", "取消", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        if (isLogin()) {
//                            Intent intent = new Intent(JPOrderActivity.this, NMemberChargeActivity.class);
//                            intent.putExtra("type", "chooseCharge");
//                            startActivity(intent);
                            startActivity(new Intent(JPOrderActivity.this, RechargeActivity.class));
                        } else {
                            startActivity(new Intent(JPOrderActivity.this, LeFenPhoneLoginActivity.class));
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                    default:
                        break;
                }

            }
        }, mContext);
    }

    /**
     * 确认收货
     */
    private void confirmReceipt() {
        if (request == null) {
            request = new MallNetRequest(mContext);
        }
        request.confirmMallOrder(orderIndex, new RequestCallBack<MallConfirmMallorderEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MallConfirmMallorderEntity> responseInfo) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                showToast("收货成功");
                                //刷新个人页面标识
                                sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                initData();//刷新订单信息
                                break;
                            default:
                                showToast(responseInfo.result.msg);
                                break;
                        }
                    }
                    stopMyDialog();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    class JPOrderOuterAdapter extends BaseAdapter {
        private Context context;
        private List<MallOrderInfoEntity.Parcels> outerList;
        private ArrayList<ArrayList<MallOrderInfoEntity.Goods>> outerListArray;
        private MallOrderInfoEntity.Parcels parcels;

        public JPOrderOuterAdapter(Context context, List<MallOrderInfoEntity.Parcels> outerList, ArrayList<ArrayList<MallOrderInfoEntity.Goods>> outerListArray) {
            this.context = context;
            this.outerList = outerList;
            this.outerListArray = outerListArray;
        }

        @Override
        public int getCount() {
            if (outerList.size() == 0 || outerList == null) {
                return 0;
            }
            return outerList.size();
        }

        @Override
        public Object getItem(int position) {
            return outerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_order_outer, null);
            }
            TextView tv_mall_from = RayViewHolder.get(convertView, R.id.tv_mall_from);
            TextView tv_mall_state1 = RayViewHolder.get(convertView, R.id.tv_mall_state1);
            TextView tv_mall_state2 = RayViewHolder.get(convertView, R.id.tv_mall_state2);
            NoScrollListView innerListView = RayViewHolder.get(convertView, R.id.listView);

            int num = position + 1;
            tv_mall_from.setText("包裹" + num);
            parcels = outerList.get(position);
            if ("0".equals(outerListArray.get(position).get(0).parcel_index)) {
                tv_mall_state1.setVisibility(View.VISIBLE);
                tv_mall_state2.setVisibility(View.GONE);
            } else {
                tv_mall_state1.setVisibility(View.GONE);
                tv_mall_state2.setVisibility(View.VISIBLE);
                tv_mall_state2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * 查看物流
                         */
                        Intent intent = new Intent(JPOrderActivity.this, LookExpressActivity.class);
                        intent.putExtra("express_number", parcels.express_num);
                        intent.putExtra("express_company", parcels.express_company);
                        intent.putExtra("express_img", outerListArray.get(position).get(0).goods_icon);
                        startActivity(intent);

                    }
                });
            }

            if (innerListView != null) {
                List<MallOrderInfoEntity.Goods> innerListArray = outerListArray.get(position);
                JPOrderInnerAdapter innerAdapter = new JPOrderInnerAdapter(context, innerListArray, orderType);
                innerListView.setAdapter(innerAdapter);
            }
            return convertView;
        }
    }

    class JPOrderInnerAdapter extends BaseAdapter {
        private final String orderType;
        public DisplayImageOptions options;
        private Context context;
        private List<MallOrderInfoEntity.Goods> innerList;
        private ImageLoader imageLoader = ImageLoader.getInstance();

        public JPOrderInnerAdapter(Context context, List<MallOrderInfoEntity.Goods> innerList, String orderType) {
            this.context = context;
            this.innerList = innerList;
            this.orderType = orderType;
            options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                    .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
                    // 这里的三张状态用一张替代
                    .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }

        @Override
        public int getCount() {
            if (innerList.size() == 0 || innerList == null) {
                return 0;
            }
            return innerList.size();
        }

        @Override
        public Object getItem(int position) {
            return innerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_order_submit_goods, null);
                holder = new ViewHolder();
                holder.item = (RelativeLayout) convertView.findViewById(R.id.item);
                holder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);
                holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
                holder.tv_goods_norms = (TextView) convertView.findViewById(R.id.tv_goods_norms);
                holder.tv_goods_count = (TextView) convertView.findViewById(R.id.tv_goods_count);
                holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
//                holder.tv_goods_integral = (TextView) convertView.findViewById(R.id.tv_goods_integral);//奖券价格
//                holder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
                holder.tvLeDou = (TextView) convertView.findViewById(R.id.tv_goods_beans);
                holder.tvSubsidy = (TextView) convertView.findViewById(R.id.tv_subsidy);
                holder.ll_order_over = (LinearLayout) convertView.findViewById(R.id.ll_order_over);
                holder.tv_after_sale = (TextView) convertView.findViewById(R.id.tv_after_sale);
                holder.tv_evaluate = (TextView) convertView.findViewById(R.id.tv_evaluate);
                holder.ivYhsIcon = (ImageView) convertView.findViewById(R.id.iv_yhs_icon);//易划算icon
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MallOrderInfoEntity.Goods goods = innerList.get(position);
            setViewHolderData(holder, goods);
            return convertView;
        }

        private void setViewHolderData(ViewHolder holder, final MallOrderInfoEntity.Goods goods) {
            GlideUtil.showImageWithSuffix(context, goods.goods_icon, holder.iv_goods);
            holder.tv_goods_name.setText(goods.goods_name);
            holder.tv_goods_norms.setText(goods.goods_norms);
            holder.tv_goods_count.setText("x" + goods.goods_count);

            switch (goods.type) {
                case "0"://普通商品
                    holder.tvLeDou.setVisibility(View.GONE);
                    holder.ivYhsIcon.setVisibility(View.GONE);
                    holder.tv_goods_price.setVisibility(View.VISIBLE);
                    holder.tv_goods_price.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(goods.goods_cost)));
//                    holder.tvScore.setVisibility(View.VISIBLE);
//                    holder.tv_goods_integral.setVisibility(View.GONE);
//                    holder.tvScore.setText(context.getString(R.string.back_score) + MoneyUtil.getLeXiangBi(goods.returnIntegral));
                    break;
                case "1"://易划算
                    holder.tvLeDou.setVisibility(View.GONE);
                    holder.ivYhsIcon.setVisibility(View.VISIBLE);
                    holder.ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
//                    holder.tv_goods_integral.setVisibility(View.VISIBLE);
//                    holder.tv_goods_integral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(goods.goodsIntegral)));
                    holder.tv_goods_price.setVisibility(View.GONE);
//                    holder.tvScore.setVisibility(View.GONE);
                    break;
                case "2"://奖券购
                    holder.tvLeDou.setVisibility(View.GONE);
                    holder.ivYhsIcon.setVisibility(View.VISIBLE);
//                    holder.tv_goods_integral.setVisibility(View.VISIBLE);
                    holder.ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
                    holder.tv_goods_price.setVisibility(View.VISIBLE);
                    holder.tv_goods_price.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(goods.goodsRetail)));
//                    float integral = Float.parseFloat(goods.goods_price) - Float.parseFloat(goods.goodsRetail);
//                    holder.tv_goods_integral.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(integral)));
//                    holder.tvScore.setVisibility(View.GONE);
                    break;
                case "3":
                case "4":
                case GoodsType.CAL_POWER: //区块益豆专区商品
                    holder.ivYhsIcon.setVisibility(View.GONE);
                    holder.tv_goods_price.setVisibility(View.VISIBLE);
                    holder.tv_goods_price.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(goods.goods_cost)));
//                    holder.tvScore.setVisibility(View.GONE);
//                    holder.tv_goods_integral.setVisibility(View.GONE);
                    holder.tvLeDou.setVisibility(View.VISIBLE);
                    holder.tvLeDou.setText("送 " + MoneyUtil.getLeXiangBi(goods.givenBean));
//                    if (!TextUtils.isEmpty(goods.subsidy) && Double.valueOf(goods.subsidy) != 0) {
//                        holder.tvSubsidy.setVisibility(View.VISIBLE);
//                        holder.tvSubsidy.setText("平台加赠益豆 " + MoneyUtil.getLeXiangBi(goods.subsidy));
//                    }else {
//                        holder.tvSubsidy.setVisibility(View.GONE);
//                    }
                    break;
                default:
                    break;
            }


            int i = goods.goods_status;//商品名称 0未发货  1已发货 2已完成待评价 3 已评价
            int j = goods.goods_after_sale;//是否已经申请过售后 0 未申请  1申请过

            if (i >= 2) {
                holder.ll_order_over.setVisibility(View.VISIBLE);
                if (i == 2 && j == 0) {
                    holder.tv_evaluate.setVisibility(View.VISIBLE);
                    holder.tv_after_sale.setVisibility(View.VISIBLE);
                }
                if (i == 2 && j == 1) {
                    holder.tv_evaluate.setVisibility(View.VISIBLE);
                    holder.tv_after_sale.setVisibility(View.INVISIBLE);
                }
                if (i == 3 && j == 0) {
                    holder.tv_evaluate.setVisibility(View.GONE);
                    holder.tv_after_sale.setVisibility(View.VISIBLE);
                }
                if (i == 3 && j == 1) {
                    holder.ll_order_over.setVisibility(View.GONE);
                }
            } else {
                holder.ll_order_over.setVisibility(View.GONE);
            }

            /**
             * 中奖的逻辑来判断申请售后是否显示
             */
            if ("1".equals(mOrderTurn)) {
                if (holder.tv_evaluate.getVisibility() == View.VISIBLE) {
                    holder.tv_after_sale.setVisibility(View.INVISIBLE);
                } else {
                    holder.ll_order_over.setVisibility(View.GONE);
                }
            }

            /**
             * 发奖励的逻辑  和是否申请过售后来判断申请售后是否显示
             * 已送奖券且已申请售后则不显示
             */
            if (1 == mProifit && j == 1) {
                if (holder.tv_evaluate.getVisibility() == View.VISIBLE) {
                    holder.tv_after_sale.setVisibility(View.INVISIBLE);
                } else {
                    holder.ll_order_over.setVisibility(View.GONE);
                }
            }

            //申请售后
            holder.tv_after_sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(canApplyAfterSaleTime>7){
//                        showToast("您已过了7天退款时间,可以选择换货或返修");
//                        return;
//                    }
                    Intent intent = new Intent(JPOrderActivity.this, ApplyAfterSaleActivity.class);
                    CreateServiceOrderParameter data = new CreateServiceOrderParameter();
                    data.order_index = goods.goods_id;
                    data.order_goods_index = goods.order_goods_index;
                    data.order_goods_norms = goods.goods_norms;
//                    TODO 这个值需要再次处理 按订单商品价格占订单总价的比例传递
//                    订单商品总价 不带代购券数量
                    intent.putExtra("order_goods_total_price", entity.order_goods_price);
                    // DaiGouQuan: 2018/8/7 代购券业务暂时隐藏
                    intent.putExtra("dai_gou_quan", "0");
//                    intent.putExtra("dai_gou_quan", entity.orderDaiGouQuanPrice);
                    intent.putExtra("order_id", entity.order_index);
                    intent.putExtra("order_goods_index", goods.order_goods_index);
                    intent.putExtra("order_goods_norms", goods.goods_norms);
                    intent.putExtra("goods_img_url", goods.goods_icon);
                    intent.putExtra("goods_name", goods.goods_name);
                    intent.putExtra("goods_count", goods.goods_count);
                    intent.putExtra("givenBean", goods.givenBean);
                    intent.putExtra("subsidy", goods.subsidy);
                    intent.putExtra("goods_price", goods.goods_price);
                    intent.putExtra("goods_cost", goods.goods_cost);
                    intent.putExtra("goods_return_integral", goods.returnIntegral);
//                    intent.putExtra("canAppayAfterSaleTime", canApplyAfterSaleTime);
                    intent.putExtra("after_sale_type", goods.after_sale_type);
                    intent.putExtra("order_left_extra", entity.order_left_extra);////由于抵扣券不足，用户使用乐享币垫付的抵扣券F金额
                    intent.putExtra("payStyle", goods.type);
                    intent.putExtra("goodsRetail", goods.goodsRetail);
                    intent.putExtra("goodsIntegral", goods.goodsIntegral);
                    intent.putExtra("order_type", orderType);
                    Logger.i("传递前的payStyle   " + goods.type);
                    Logger.i("price---" + goods.goods_price + "cost---" + goods.goods_cost + "left_extra---" + entity.order_left_extra + " after_sale_type:" + goods.after_sale_type);
                    startActivity(intent);
                }
            });
            //去评价
            holder.tv_evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch (orderType) {
                        case "5":
                        case "6":
                            intent = new Intent(JPOrderActivity.this, ActEvaluateActivity.class);
                            intent.putExtra("goods_icon", goods.goods_icon);
                            intent.putExtra("goods_index", goods.order_goods_index);
                            break;
                        default:
                            intent = new Intent(JPOrderActivity.this, EvaluateActivity.class);
                            intent.putExtra("goods_id", goods.goods_id);
                            intent.putExtra("orderGoodsIndex", goods.order_goods_index);
                            intent.putExtra("orderId", entity.order_index);
                            intent.putExtra("filiale_id", entity.filiale_id);
                            intent.putExtra("type", goods.type);
                            break;
                    }
                    startActivity(intent);
                }
            });
            //每个item点击跳转商品详情
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击item跳转对应的详情，
                    Intent intent = null;
                    switch (orderType) {
                        case "5":
                            intent = new Intent(JPOrderActivity.this, ActivityDetailActivity.class);
                            intent.putExtra("goods_id", goods.goods_id);
                            intent.putExtra("activity_id", goods.actId);
                            intent.putExtra("activity_type", "2");
                            intent.putExtra("is_show", "0");
                            intent.putExtra("activity_orderId", "0");
                        case "6":
                            intent = new Intent(JPOrderActivity.this, ActivityDetailActivity.class);
                            intent.putExtra("goods_id", goods.goods_id);
                            intent.putExtra("activity_id", goods.actId);
                            intent.putExtra("activity_type", "1");
                            intent.putExtra("is_show", "0");
                            intent.putExtra("activity_orderId", "0");
                            break;
                        default:
                            intent = new Intent(JPOrderActivity.this, JPNewCommDetailActivity.class);
                            intent.putExtra("goods_id", goods.goods_id);
                            intent.putExtra("filiale_id", "");
                            intent.putExtra("classify", goods.type);
                            break;
                    }
                    startActivity(intent);

                }
            });
        }
    }

    class ViewHolder {
        RelativeLayout item;
        ImageView iv_goods;
        TextView tv_goods_name;
        TextView tv_goods_norms;
        TextView tv_goods_count;
        TextView tv_goods_price;
        //        TextView tv_goods_integral;
//        TextView tvScore;
        TextView tvLeDou;
        TextView tvSubsidy;
        LinearLayout ll_order_over;
        TextView tv_after_sale;
        TextView tv_evaluate;
        ImageView ivYhsIcon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_contact_customer_service:
            case R.id.tv_contact_customer_service:
                AliCustomerServiceChatUtils.openServiceChatByActivity(mContext, customerServicePersonId, customerServiceGroupId, orderIndex, "-1");
                break;
        }
    }


}
