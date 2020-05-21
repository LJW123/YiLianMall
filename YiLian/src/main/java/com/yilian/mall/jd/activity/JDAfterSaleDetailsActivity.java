package com.yilian.mall.jd.activity;


import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.dialog.InputLogisticsPopwindow;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleDetialEntity;
import com.yilian.networkingmodule.entity.jd.JDCheckAvailableAfterSaleEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 京东售后详情
 *
 * @author Zg on 2017/5/28.
 */
public class JDAfterSaleDetailsActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态
    /**
     * 服务单环节:申请阶段(10)
     */
    private int afsServiceStep_apply = 10;
    /**
     * 服务单环节:审核不通过 (20)
     */
    private int afsServiceStep_refuse = 20;
    /**
     * 服务单环节:客服审核(21)
     */
    private int afsServiceStep_service_check = 21;
    /**
     * 服务单环节:商家审核 (22)
     */
    private int afsServiceStep_merchant_check = 22;
    /**
     * 服务单环节:京东收货(31)
     */
    private int afsServiceStep_jd_receiving = 31;
    /**
     * 服务单环节:用户确认(40)
     */
    private int afsServiceStep_user_notarize = 40;
    /**
     * 服务单环节:完成 (50)
     */
    private int afsServiceStep_finish = 50;
    /**
     * 服务单环节:取消(60)
     */
    private int afsServiceStep_cancel = 60;
    /**
     * 服务类型码 :退货(10)
     */
    private int customerExpect_sales_return = 10;

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;
    //顶部状态
    private TextView tvAfsServiceStepName, tcServiceStatusTop, tvInputFreightNumber;
    //服务单状态
    private TextView tvServiceNumber, tvApplyTime, tvRefundMoney, tcServiceStatus;
    //退款展示
    private LinearLayout llRefund;
    private TextView tvReturnMoney, tvReturnTicket;
    //售后进度
    private LinearLayout llServiceSracking;
    private TextView tvServiceTrackingContext, tvServiceTrackingHandler, tvServiceTrackingTime, tvServiceTrackingDetails;
    //审核留言
    private TextView tvApproveNotes, tvApproveNotesShowAll;
    //问题描述
    private TextView tvQuestionDesc, tvQuestionDescTime;
    //服务单信息
    private TextView tvServiceType, tvRefundType, tvReturnType, tvLinkPerson, tvLinkPhone;
    //菜单按钮
    private TextView tvCancelApply, tvApplyAfterSale, tvDeleteApply;
    /**
     * 售后服务单号
     */
    private String afsServiceId;
    /**
     * 售后商品id
     */
    private String skuId;
    /**
     * 售后商品数量
     */
    private String skuNum;

    //京东售后详情 信息
    private JDAfterSaleDetialEntity.DataBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_after_sale_details);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new RefreshClickListener());
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        //顶部状态
        tvAfsServiceStepName = findViewById(R.id.tv_afs_service_step_name);
        tcServiceStatusTop = findViewById(R.id.tc_service_status_top);
        tvInputFreightNumber = findViewById(R.id.tv_input_freight_number);
        //服务单状态
        tvServiceNumber = findViewById(R.id.tv_service_number);
        tvApplyTime = findViewById(R.id.tv_apply_time);
        tvRefundMoney = findViewById(R.id.tv_refund_money);
        tvRefundMoney.setVisibility(View.GONE);
        tcServiceStatus = findViewById(R.id.tc_service_status);
        //退款展示
        llRefund = findViewById(R.id.ll_refund);
        llRefund.setVisibility(View.GONE);
        tvReturnMoney = findViewById(R.id.tv_return_money);
        tvReturnTicket = findViewById(R.id.tv_return_ticket);
        //售后进度
        llServiceSracking = findViewById(R.id.ll_service_tracking);
        tvServiceTrackingContext = findViewById(R.id.tv_service_tracking_context);
        tvServiceTrackingHandler = findViewById(R.id.tv_service_tracking_handler);
        tvServiceTrackingTime = findViewById(R.id.tv_service_tracking_time);
        tvServiceTrackingDetails = findViewById(R.id.tv_service_tracking_details);
        //审核留言
        tvApproveNotes = findViewById(R.id.tv_approve_notes);
        tvApproveNotesShowAll = findViewById(R.id.tv_approve_notes_show_all);
        //问题描述
        tvQuestionDesc = findViewById(R.id.tv_question_desc);
        tvQuestionDescTime = findViewById(R.id.tv_question_desc_time);
        //服务单信息
        tvServiceType = findViewById(R.id.tv_service_type);
        tvRefundType = findViewById(R.id.tv_refund_type);
        tvReturnType = findViewById(R.id.tv_return_type);
        tvLinkPerson = findViewById(R.id.tv_link_person);
        tvLinkPhone = findViewById(R.id.tv_link_phone);
        //菜单按钮
        tvCancelApply = findViewById(R.id.tv_cancel_apply);
        tvApplyAfterSale = findViewById(R.id.tv_apply_after_sale);
        tvDeleteApply = findViewById(R.id.tv_delete_apply);


    }

    public void initData() {
        tvTitle.setText("服务单详情");
        afsServiceId = getIntent().getStringExtra("afsServiceId");
        skuId = getIntent().getStringExtra("skuId");
        skuNum = getIntent().getStringExtra("skuNum");
        getJDAfterSaleApplyForData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvInputFreightNumber.setOnClickListener(this);
        tvServiceTrackingDetails.setOnClickListener(this);
        tvApproveNotesShowAll.setOnClickListener(this);
        tvCancelApply.setOnClickListener(this);
        tvApplyAfterSale.setOnClickListener(this);
        tvDeleteApply.setOnClickListener(this);
    }

    /**
     * 京东售后记录详情
     */
    private void getJDAfterSaleApplyForData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJDServiceDetial("jd_aftersale/jd_service_detial", afsServiceId, skuId, skuNum).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAfterSaleDetialEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDAfterSaleDetialEntity entity) {
                        mData = (JDAfterSaleDetialEntity.DataBean) entity.data;
                        if (mData != null) {
                            setData();
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void setData() {
        //初始化 隐藏布局
        tvInputFreightNumber.setVisibility(View.GONE);
        tvRefundMoney.setVisibility(View.GONE);
        tvCancelApply.setVisibility(View.GONE);
        tvApplyAfterSale.setVisibility(View.GONE);
        tvDeleteApply.setVisibility(View.GONE);
        llRefund.setVisibility(View.GONE);
        //顶部状态
        tvAfsServiceStepName.setText(mData.getAfsServiceStepName());
        //服务单状态
        tvServiceNumber.setText("服务单号：" + mData.getAfsServiceId());
        tvApplyTime.setText("申请时间：" + mData.getAfsApplyTime());
        //售后进度
        if (mData.getServiceTrackInfoDTOs() != null && mData.getServiceTrackInfoDTOs().size() > 0) {
            llServiceSracking.setVisibility(View.VISIBLE);
            tvServiceTrackingContext.setText(mData.getServiceTrackInfoDTOs().get(mData.getServiceTrackInfoDTOs().size() - 1).getContext());
            tvServiceTrackingHandler.setText(mData.getServiceTrackInfoDTOs().get(mData.getServiceTrackInfoDTOs().size() - 1).getCreateName());
            tvServiceTrackingTime.setText(mData.getServiceTrackInfoDTOs().get(mData.getServiceTrackInfoDTOs().size() - 1).getCreateDate());
        } else {
            llServiceSracking.setVisibility(View.GONE);
        }
        //审核留言
        tvApproveNotes.setText(mData.getApproveNotes());
        //问题描述
        tvQuestionDesc.setText(mData.getQuestionDesc());
        tvQuestionDescTime.setText(mData.getAfsApplyTime());
        //服务单信息
        tvServiceType.setText(mData.getCustomerExpectStr());
        tvRefundType.setText(mData.getBackTypeName());
        tvReturnType.setText(mData.getPickwareName());
        tvLinkPerson.setText(mData.getServiceCustomerInfoDTO().getCustomerContactName());
        tvLinkPhone.setText(mData.getServiceCustomerInfoDTO().getCustomerTel());

        if (mData.getAfsServiceStep() == afsServiceStep_apply) {
            //顶部状态
            tcServiceStatusTop.setText("您的售后申请正在申请审核");
            //服务单状态
            tcServiceStatus.setText("您的售后申请正在申请审核");
            //菜单按钮
            tvCancelApply.setVisibility(View.VISIBLE);
        } else if (mData.getAfsServiceStep() == afsServiceStep_refuse) {
            //顶部状态
            tcServiceStatusTop.setText("您的售后申请审核不通过");
            //服务单状态
            tcServiceStatus.setText("您的售后申请审核不通过");
            //菜单按钮
            tvApplyAfterSale.setVisibility(View.VISIBLE);
        } else if (afsServiceStep_service_check <= mData.getAfsServiceStep() && mData.getAfsServiceStep() <= afsServiceStep_merchant_check) {
            //顶部状态
            tcServiceStatusTop.setText("您的售后申请正在申请审核");
            //服务单状态
            tcServiceStatus.setText("您的售后申请正在申请审核");
            //菜单按钮
            tvCancelApply.setVisibility(View.VISIBLE);
        } else if (afsServiceStep_jd_receiving <= mData.getAfsServiceStep() && mData.getAfsServiceStep() <= afsServiceStep_user_notarize) {
            //顶部状态
            tvInputFreightNumber.setVisibility(View.VISIBLE);
            tcServiceStatusTop.setText("您的售后申请正在处理");
            //服务单状态
            tcServiceStatus.setText("您的售后申请正在处理");
            //菜单按钮

        } else if (mData.getAfsServiceStep() == afsServiceStep_finish) {
            //顶部状态
            tcServiceStatusTop.setText("服务关闭，如有需要可重新提交服务单");
            //服务单状态
            if (mData.getCustomerExpect() == customerExpect_sales_return) {
                tvRefundMoney.setVisibility(View.VISIBLE);
                tcServiceStatus.setText("¥" + mData.getAfsServicePrice());
                tvReturnMoney.setText("¥" + MyBigDecimal.sub(mData.getAfsServicePrice(), mData.getReturnQuan()));
                tvReturnTicket.setText(mData.getReturnQuan());
                llRefund.setVisibility(View.VISIBLE);
            } else {
                tcServiceStatus.setText("服务关闭，如有需要可重新提交服务单");
            }
            //菜单按钮
            tvDeleteApply.setVisibility(View.VISIBLE);
        } else if (mData.getAfsServiceStep() == afsServiceStep_cancel) {
            //顶部状态
            tcServiceStatusTop.setText("您已取消售后，如有需要可重新提交服务单");
            //服务单状态
            tcServiceStatus.setText("您已取消售后，如有需要可重新提交服务单");
            //菜单按钮
            tvApplyAfterSale.setVisibility(View.VISIBLE);
        }
        varyViewUtils.showDataView();
    }

    /**
     * 按钮的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.tv_input_freight_number:
                //填写运单号
                inputLogistics();
                break;
            case R.id.tv_service_tracking_details:
                //进度详情
                JumpJdActivityUtils.toJDAfterSaleTrack(context, mData.getServiceTrackInfoDTOs());
                break;
            case R.id.tv_approve_notes_show_all:
                //审核留言 显示全部
                if (mState == SPREAD_STATE) {
                    tvApproveNotes.setMaxLines(3);
                    tvApproveNotes.requestLayout();
                    tvApproveNotesShowAll.setText("显示全部");
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    tvApproveNotes.setMaxLines(Integer.MAX_VALUE);
                    tvApproveNotes.requestLayout();
                    tvApproveNotesShowAll.setText("隐藏");
                    mState = SPREAD_STATE;
                }
                break;
            case R.id.tv_cancel_apply:
                //取消申请
                jsAftersaleCancel();
                break;
            case R.id.tv_apply_after_sale:
                //申请售后
                checkAvailableAftersale();
                break;
            case R.id.tv_delete_apply:
                //删除申请
                jdDeleteServiceOrder();
                break;
            default:
                break;
        }
    }

    /**
     * 填写发货信息
     */
    private void inputLogistics() {
        InputLogisticsPopwindow popwindow = new InputLogisticsPopwindow(context);
        popwindow.showPop(tvInputFreightNumber);
//        popwindow.selectLogistics(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showExpressListPickerView(popwindow);
//            }
//        });
        popwindow.cancel(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popwindow.dismiss();
            }
        });
        popwindow.submit(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSendNum(popwindow);
            }
        });
    }


    /**
     * 京东政企售后服务添加运单号
     */
    private void updateSendNum(InputLogisticsPopwindow popwindow) {
        if (TextUtils.isEmpty(popwindow.getLogistics())) {
            showToast("请选择快递公司");
            return;
        }
        if (TextUtils.isEmpty(popwindow.getLogisticsNumbers()) && !RegExUtils.isExpressNum(popwindow.getLogisticsNumbers())) {
            showToast("快递单号为8-18位字符，支持字母、数字");
            return;
        }
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                updateSendNum("jd_aftersale/jd_update_send_num", mData.getAfsServiceId(), popwindow.getLogistics(), popwindow.getLogisticsNumbers()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(HttpResultBean entity) {
                        popwindow.dismiss();
                        //刷新页面数据
                        varyViewUtils.showLoadingView();
                        getJDAfterSaleApplyForData();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 京东政企取消售后订单
     */
    private void jsAftersaleCancel() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                jsAftersaleCancel("jd_aftersale/js_aftersale_cancel", mData.getAfsServiceId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(HttpResultBean entity) {
                        //刷新页面数据
                        varyViewUtils.showLoadingView();
                        getJDAfterSaleApplyForData();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 京东政企删除售后订单
     */
    private void jdDeleteServiceOrder() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                jdDeleteServiceOrder("jd_aftersale/jd_delete_service_order", mData.getAfsServiceId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(HttpResultBean entity) {
                        //刷新页面数据
                        varyViewUtils.showLoadingView();
                        getJDAfterSaleApplyForData();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 京东政企判断订单中的商品是否可以申请售后
     */
    private void checkAvailableAftersale() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                jdCheckAvailableAftersaler("jd_aftersale/jd_check_available_aftersale", mData.getOrderId(), mData.getSkuId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            JumpJdActivityUtils.toJDAfterSaleApplyFor(context, mData.getOrderId(), skuId, skuNum);
                        } else {
                            showToast("暂不可申请售后");
                        }
                    }
                });
        addSubscription(subscription);
    }

    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getJDAfterSaleApplyForData();
                }
            }, 1000);
        }
    }
}
