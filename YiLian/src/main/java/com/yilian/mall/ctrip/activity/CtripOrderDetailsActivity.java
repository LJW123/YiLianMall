package com.yilian.mall.ctrip.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripHomeListAdapter;
import com.yilian.mall.ctrip.fragment.order.CtripOrderCommonFragment;
import com.yilian.mall.ctrip.popupwindow.CtripOrderCancelPolicyPopwindow;
import com.yilian.mall.ctrip.popupwindow.CtripOrderCostPopwindow;
import com.yilian.mall.ctrip.popupwindow.CtripOrderHouseTypePopwindow;
import com.yilian.mall.ctrip.popupwindow.CtripWarnCommonPopwindow;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.ctrip.CtripCommitOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单详情
 *
 * @author Zg on 2018/9/12.
 */
public class CtripOrderDetailsActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle, tvRight;

    /**
     * 列表
     */
    private int page = Constants.PAGE_INDEX;
    private RecyclerView mRecyclerView;
    private CtripHomeListAdapter homeListAdapter;
    /**
     * 头部 - 状态
     */
    //                   未提交        确认中             已确认               已消费                已取消
    private View headerStatusSubmit, headerStatusAffirm, headerStatusConfirmed, headerStatusSpending, headerStatusCancel;
    private View headerPrice;
    private View headerRefund;
    private View headerButton;
    private View headerInfo;
    //确认中            描述
    private TextView submitDescribe;
    //未提交            详情
    private TextView affirmProcess;
    //已确认               详情          描述              取消政策
    private TextView confirmedProcess, confirmedDescribe, confirmedCancelPolicy;
    //已确认              取消政策                  入住凭证
    private LinearLayout confirmedLlCancelPolicy, confirmedVoucher;
    //已使用
    private TextView spendingTimeCreate, spendingTimeVerify, spendingTimeCheck;
    //价格模块
    private TextView tvOrderPrice, tvReturnBean, tvCostDetail;
    //退款进度模块
    private TextView refundReturnMoney, refundTimeAccepted, refundReturnText, refundTimeReturn;
    private ImageView refundReturnDot;
    //菜单 按钮模块
    private TextView buttonReminder, buttonSubmit, buttonCancel, buttonDelete, buttonAgain;
    //
    private TextView tvHotelName, tvHotelDetails, tvHotelAddress, tvHotelEmail, tvHotelMap, tvHotelPhone, tvHouseTypeName, tvHouseTypeDetails, tvHouseTypeDescribe, tvCheckPerson,
            tvLinkman, tvPhone, tvOrderNum, tvReserveTime, tvLatestTime;
    private LinearLayout llHotelEmail;

    private CtripWarnCommonPopwindow reminderPop, deletePop, phonePop;
    private CtripOrderCancelPolicyPopwindow cancelPolicyPop;
    private CtripOrderCostPopwindow costPop;
    private CtripOrderHouseTypePopwindow orderHouseTypePop;
    //订单详情 数据
    private CtripOrderDetailEntity.DataBean mData;
    //订单号
    private String ResIDValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_order_detail);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
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

        mRecyclerView = findViewById(R.id.recycler_view);
        homeListAdapter = new CtripHomeListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        homeListAdapter.bindToRecyclerView(mRecyclerView);

        //头部 - 状态
        headerStatusSubmit = View.inflate(context, R.layout.ctrip_activity_order_detail_header_status_submit, null);
        submitDescribe = headerStatusSubmit.findViewById(R.id.submit_describe);

        headerStatusAffirm = View.inflate(context, R.layout.ctrip_activity_order_detail_header_status_affirm, null);
        affirmProcess = headerStatusAffirm.findViewById(R.id.affirm_process);


        headerStatusConfirmed = View.inflate(context, R.layout.ctrip_activity_order_detail_header_status_confirmed, null);
        confirmedProcess = headerStatusConfirmed.findViewById(R.id.confirmed_process);
        confirmedDescribe = headerStatusConfirmed.findViewById(R.id.confirmed_describe);
        confirmedLlCancelPolicy = headerStatusConfirmed.findViewById(R.id.confirmed_ll_cancel_policy);
        confirmedCancelPolicy = headerStatusConfirmed.findViewById(R.id.confirmed_cancel_policy);
        confirmedVoucher = headerStatusConfirmed.findViewById(R.id.confirmed_voucher);

        headerStatusSpending = View.inflate(context, R.layout.ctrip_activity_order_detail_header_status_spending, null);
        spendingTimeCreate = headerStatusSpending.findViewById(R.id.spending_time_create);
        spendingTimeVerify = headerStatusSpending.findViewById(R.id.spending_time_verify);
        spendingTimeCheck = headerStatusSpending.findViewById(R.id.spending_time_check);

        headerStatusCancel = View.inflate(context, R.layout.ctrip_activity_order_detail_header_status_cancel, null);

        headerPrice = View.inflate(context, R.layout.ctrip_activity_order_detail_header_price, null);
        tvOrderPrice = headerPrice.findViewById(R.id.tv_order_price);
        tvReturnBean = headerPrice.findViewById(R.id.tv_return_bean);
        tvCostDetail = headerPrice.findViewById(R.id.tv_cost_detail);

        headerRefund = View.inflate(context, R.layout.ctrip_activity_order_detail_header_refund, null);
        refundReturnMoney = headerRefund.findViewById(R.id.refund_return_money);
        refundTimeAccepted = headerRefund.findViewById(R.id.trefund_time_accepted);
        refundReturnDot = headerRefund.findViewById(R.id.refund_return_dot);
        refundReturnText = headerRefund.findViewById(R.id.refund_return_text);
        refundTimeReturn = headerRefund.findViewById(R.id.refund_time_return);

        headerButton = View.inflate(context, R.layout.ctrip_activity_order_detail_header_button, null);
        buttonReminder = headerButton.findViewById(R.id.button_reminder);
        buttonSubmit = headerButton.findViewById(R.id.button_submit);
        buttonCancel = headerButton.findViewById(R.id.button_cancel);
        buttonDelete = headerButton.findViewById(R.id.button_delete);
        buttonAgain = headerButton.findViewById(R.id.button_again);

        headerInfo = View.inflate(context, R.layout.ctrip_activity_order_detail_header_info, null);
        tvHotelName = headerInfo.findViewById(R.id.tv_hotel_name);
        tvHotelDetails = headerInfo.findViewById(R.id.tv_hotel_details);
        tvHotelAddress = headerInfo.findViewById(R.id.tv_hotel_address);
        llHotelEmail = headerInfo.findViewById(R.id.ll_hotel_email);
        tvHotelEmail = headerInfo.findViewById(R.id.tv_hotel_email);
        tvHotelMap = headerInfo.findViewById(R.id.tv_hotel_map);
        tvHotelPhone = headerInfo.findViewById(R.id.tv_hotel_phone);
        tvHouseTypeName = headerInfo.findViewById(R.id.tv_house_type_name);
        tvHouseTypeDetails = headerInfo.findViewById(R.id.tv_house_type_details);
        tvHouseTypeDescribe = headerInfo.findViewById(R.id.tv_house_type_describe);
        tvCheckPerson = headerInfo.findViewById(R.id.tv_check_person);
        tvLinkman = headerInfo.findViewById(R.id.tv_linkman);
        tvPhone = headerInfo.findViewById(R.id.tv_phone);
        tvOrderNum = headerInfo.findViewById(R.id.tv_order_num);
        tvReserveTime = headerInfo.findViewById(R.id.tv_reserve_time);
        tvLatestTime = headerInfo.findViewById(R.id.tv_latest_time);

    }

    public void initData() {
        tvTitle.setText("订单详情");
        ResIDValue = getIntent().getStringExtra("ResIDValue");
        getOrdersDetails();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);

        buttonReminder.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonAgain.setOnClickListener(this);

        tvHotelDetails.setOnClickListener(this);
        tvHotelMap.setOnClickListener(this);
        tvHotelPhone.setOnClickListener(this);
        tvHouseTypeDetails.setOnClickListener(this);
    }


    /**
     * 订单详情
     */
    private void getOrdersDetails() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getCtripOrderDetails("xc_order/xc_order_info", ResIDValue).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripOrderDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CtripOrderDetailEntity entity) {
                        mData = entity.data;
                        if (mData != null) {
                            setData();
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 绑定数据
     */
    private void setData() {
        homeListAdapter.removeAllHeaderView();

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        if (mData.status.equals(CtripOrderCommonFragment.OrderType_submit)) {
            /**未提交*/
            homeListAdapter.addHeaderView(headerStatusSubmit);
            submitDescribe.setText(String.format("订单为您保留%s分钟，请在%s点前完成支付， 超过此时限需要重新预订", mData.stayTimeLength, new SimpleDateFormat("HH:mm").format(mData.deadTime * 1000)));

        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_process)) {
            /**确认中*/
            homeListAdapter.addHeaderView(headerStatusAffirm);
            affirmProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进度
                    JumpCtripActivityUtils.toCtripOrderProgress(context, mData.process);
                }
            });
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_affirm)) {
            /**已确认*/
            homeListAdapter.addHeaderView(headerStatusConfirmed);
            //订单类型 0普通房型订单 1钟点房订单
            if (mData.orderType.equals("0")) {
                confirmedDescribe.setText("房间将保留整晚，请尽快入住");
            } else if (mData.orderType.equals("1")) {
                confirmedDescribe.setText("房间为钟点房，请在规定时间段内入住");
            }
            confirmedCancelPolicy.setText(mData.cancelMsg);
            confirmedLlCancelPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消政策
                    if (cancelPolicyPop == null) {
                        cancelPolicyPop = new CtripOrderCancelPolicyPopwindow(context);
                        cancelPolicyPop.setContent(mData.cancelMsgDetail);
                    }
                    cancelPolicyPop.showPop(confirmedLlCancelPolicy);
                }
            });
            confirmedProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进度
                    JumpCtripActivityUtils.toCtripOrderProgress(context, mData.process);
                }
            });
            confirmedVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //入住凭证
                    JumpCtripActivityUtils.toCtripVoucher(context, mData);
                }
            });
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_expense)) {
            /**已消费*/
            homeListAdapter.addHeaderView(headerStatusSpending);
            spendingTimeCreate.setText(sdf.format(mData.CreateDateTime * 1000));
            spendingTimeVerify.setText(sdf.format(mData.confirmTime * 1000));
            spendingTimeCheck.setText(sdf.format(mData.checkInTime * 1000));
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_cancel)) {
            /**已取消*/
            homeListAdapter.addHeaderView(headerStatusCancel);
        }
        homeListAdapter.addHeaderView(headerPrice);
        tvOrderPrice.setText(mData.InclusiveAmount);
        tvReturnBean.setText(mData.ReturnBean);
        tvCostDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //明细
                if (costPop == null) {
                    costPop = new CtripOrderCostPopwindow(context);
                    costPop.setContent(mData.dayInclusiveAmount, mData.TimeSpanStart, mData.TimeSpanEnd, mData.Breakfast, mData.InclusiveAmount);
                }
                costPop.showPop(tvCostDetail);
            }
        });

        if (mData.status.equals(CtripOrderCommonFragment.OrderType_cancel)) {
            /**已取消*/
            homeListAdapter.addHeaderView(headerRefund);
            refundReturnMoney.setText(String.format("退回益联益家账户¥%s", mData.returnMoney));
            refundTimeAccepted.setText(sdf.format(mData.cancelTime * 1000));
            if (mData.returnTime > 0) {
                refundReturnDot.setImageResource(R.mipmap.ctrip_activity_order_detail_header_refund_progress_green);
                refundReturnText.setTextColor(Color.parseColor("#32B16C"));
                refundTimeReturn.setText(sdf.format(mData.returnTime * 1000));
            }
        }
        homeListAdapter.addHeaderView(headerButton);
        buttonReminder.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.GONE);
        if (mData.status.equals(CtripOrderCommonFragment.OrderType_submit)) {
            /**未提交*/
            //是否有效  0有效   1无效 （携程未提交订单 系统保留30分钟之后置位无效）
            if (mData.isEffective.equals("0")) {
                buttonSubmit.setVisibility(View.VISIBLE);
                buttonDelete.setVisibility(View.VISIBLE);
            }
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_process)) {
            /**确认中*/
            buttonReminder.setVisibility(View.VISIBLE);
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_affirm)) {
            /**已确认*/
            buttonCancel.setVisibility(View.VISIBLE);
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_expense)) {
            /**已消费*/
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_cancel)) {
            /**已取消*/
        }
        homeListAdapter.addHeaderView(headerInfo);

        tvHotelName.setText(mData.hotelName);
        tvHotelAddress.setText(mData.hotelAddress);
        if (TextUtils.isEmpty(mData.hotelEmail)) {
            llHotelEmail.setVisibility(View.GONE);
        } else {
            llHotelEmail.setVisibility(View.VISIBLE);
            tvHotelEmail.setText(mData.hotelEmail);
        }
        tvHouseTypeName.setText(mData.RoomName);
        String breakfast = "无早餐";
        if (mData.Breakfast == 1) {
            breakfast = "单份早餐";
        } else if (mData.Breakfast == 2) {
            breakfast = "双份早餐";
        }
        SimpleDateFormat sdfMD = new SimpleDateFormat("MM月dd日");
        tvHouseTypeDescribe.setText(String.format("%s-%s | 共%s晚 | %s间 | %s\n %s",
                sdfMD.format(mData.TimeSpanStart * 1000), sdfMD.format(mData.TimeSpanEnd * 1000), mData.nightNum, mData.NumberOfUnits, mData.RoomBedName, breakfast));
        tvCheckPerson.setText(mData.users);
        tvLinkman.setText(mData.ContactName);
        tvPhone.setText(mData.phone);
        tvOrderNum.setText(mData.ResIDValue);
        tvReserveTime.setText(DateUtils.timeStampToStr(mData.CreateDateTime));
        tvLatestTime.setText(DateUtils.timeStampToStr(mData.LateArrivalTime));

        if (mData.status.equals(CtripOrderCommonFragment.OrderType_cancel)) {
            homeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    getNextGoodsData();
                }
            }, mRecyclerView);
            //获取第一页推荐
            getFirstMaybeLike();
        }

        varyViewUtils.showDataView();
    }

    /**
     * 获取首页为您推荐数据
     */
    private void getFirstMaybeLike() {
        page = Constants.PAGE_INDEX;
        getMaybeLikeData();
    }

    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        page++;
        getMaybeLikeData();
    }

    /**
     * 猜你喜欢
     **/
    private void getMaybeLikeData() {
        String maxPric = "500";
        if(!TextUtils.isEmpty(mData.dayInclusiveAmount)){
            maxPric = MyBigDecimal.add(mData.dayInclusiveAmount, maxPric);
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getHotelList("xc_goods/xc_hotel_list", mData.CityCode, "comSort DESC", "100000000", "", "", "0", maxPric, "",
                        "", "", "", "", "", "", mData.gdLat, mData.gdLng, page, String.valueOf(Constants.PAGE_COUNT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelListEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        if (page != Constants.PAGE_INDEX) {
                            page--;
                        }
                    }

                    @Override
                    public void onNext(CtripHotelListEntity entity) {
                        if (page == Constants.PAGE_INDEX) {
                            if (entity.data != null && entity.data.size() > 0) {
                                homeListAdapter.addHeaderView(View.inflate(context, R.layout.ctrip_activity_order_detail_header_status_cancel_like, null));
                                homeListAdapter.setLocation(entity.location);
                                homeListAdapter.setNewData(entity.data);
                            }
                        } else {
                            homeListAdapter.addData(entity.data);
                        }
                        if (entity.data.size() < Constants.PAGE_COUNT) {
                            homeListAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 删除订单
     */
    private void deleteCtripOrder() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                deleteCtripOrder("xc_order/xc_del_order", ResIDValue).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean entity) {
                        /**
                         * {@link com.yilian.mall.ctrip.fragment.order.CtripOrderCommonFragment}
                         */
                        EventBus.getDefault().post(new CtripOrderListUpdateModel(CtripOrderListUpdateModel.HandleType_delete, mData.ResIDValue));

                        deletePop.dismiss();
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.button_reminder:
                //催确认
                if (reminderPop == null) {
                    reminderPop = new CtripWarnCommonPopwindow(context);
                    reminderPop.setContent("已收到您的催单需求，请稍作等待多数订单会在10分钟内确认，请耐心等待。");
                    reminderPop.setLeftColor("#4289FF");
                    reminderPop.setLeft("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reminderPop.dismiss();
                        }
                    });
                }
                reminderPop.showPop(buttonReminder);
                break;
            case R.id.button_submit:
                //继续提交
                CtripCommitOrderEntity entity = new CtripCommitOrderEntity();
                entity.hotelId = mData.hotelId;
                entity.roomId = mData.RoomID;
                entity.orderIndex = mData.id;
                entity.orderPrice = mData.InclusiveAmount;
                entity.returnBean = mData.ReturnBean;
                entity.ResID_Value = mData.ResIDValue;
                entity.hotelName = mData.hotelName;
                entity.roomType = mData.RoomName;
                entity.bedName = mData.RoomInfo.bedName;
                entity.netMsg = mData.RoomInfo.netMsg;
                entity.breakfast = mData.Breakfast;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                entity.checkIn = sdf.format(mData.TimeSpanStart * 1000);
                entity.checkOut = sdf.format(mData.TimeSpanEnd * 1000);
                entity.duration = mData.nightNum;
                entity.number = mData.NumberOfUnits;
                entity.checkInPerson = mData.users;
                entity.linkman = mData.ContactName;
                entity.phone = mData.phone;
                JumpCtripActivityUtils.toCtripOrderPayment(context, entity);
                break;
            case R.id.button_cancel:
                //取消订单
                //0不可取消  1可取消
                if (mData.cancel == 0) {
                    if (cancelPolicyPop == null) {
                        cancelPolicyPop = new CtripOrderCancelPolicyPopwindow(context);
                        cancelPolicyPop.setContent(mData.cancelMsgDetail);
                    }
                    cancelPolicyPop.showPop(confirmedLlCancelPolicy);
                } else {
                    JumpCtripActivityUtils.toCtripOrderCancel(context, mData.InclusiveAmount, mData.ResIDValue, mData.hotelId,
                            mData.CityCode, mData.gdLat, mData.gdLng, mData.dayInclusiveAmount);
                }

                break;
            case R.id.button_delete:
                //删除订单
                if (deletePop == null) {
                    deletePop = new CtripWarnCommonPopwindow(context);
                    deletePop.setTitle("删除此订单记录不等于取消预订");
                    deletePop.setContent("删除后订单记录无法还原和查询，确定删除吗？");
                    deletePop.setLeft("点错了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePop.dismiss();
                        }
                    });
                    deletePop.setRight("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteCtripOrder();
                        }
                    });
                }
                deletePop.showPop(buttonDelete);
                break;
            case R.id.button_again:
                //再次预定
                JumpCtripActivityUtils.toCtripHotelDetail(context, mData.hotelId, null, null);
                finish();
                break;
            case R.id.tv_hotel_details:
                //酒店详情
                JumpCtripActivityUtils.toCtripHotelDetail(context, mData.hotelId, null, null);
                break;
            case R.id.tv_hotel_map:
                //地图
                JumpCtripActivityUtils.toMapSearchHotelInfo(context, mData.hotelId);
                break;
            case R.id.tv_hotel_phone:
                //酒店电话
                if (phonePop == null) {
                    phonePop = new CtripWarnCommonPopwindow(context);
                    String tips = String.format("酒店电话：%s", mData.Telephone);
                    SpannableString spannableString = new SpannableString(tips);
                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + mData.Telephone);
                            intent.setData(data);
                            startActivity(intent);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#4289FF"));
                            ds.setUnderlineText(false);
                        }
                    }, tips.length() - mData.Telephone.length(), tips.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    phonePop.setContent(spannableString);
                    phonePop.setLeftColor("#4289FF");
                    phonePop.setLeft("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            phonePop.dismiss();
                        }
                    });
                }
                phonePop.showPop(tvHotelPhone);
                break;
            case R.id.tv_house_type_details:
                //房型详情
                if (orderHouseTypePop == null) {
                    orderHouseTypePop = new CtripOrderHouseTypePopwindow(context);
                    orderHouseTypePop.setTitle(mData.RoomName);
                    orderHouseTypePop.setContent(mData.RoomInfo);
                }
                orderHouseTypePop.showPop(tvHouseTypeDetails);
                break;
            default:
                break;
        }
    }

}
