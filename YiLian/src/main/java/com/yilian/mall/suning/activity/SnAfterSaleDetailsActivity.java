package com.yilian.mall.suning.activity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleDetialEntity;
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
public class SnAfterSaleDetailsActivity extends BaseFragmentActivity implements View.OnClickListener {


    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;

    /**
     * 售后服务单号
     */
    private String id;

    //顶部状态
    private TextView tvOrderStatus;
    private TextView tvOrderTime;
    //状态描述
    private TextView tvOrderStatusDescribe;
    //退款成功 状态信息展示
    private LinearLayout llStatusSuccess;
    private TextView tvRefundTotal;
    private TextView tvRefundMoney;
    private TextView tvRefundDaigou;
    //退货信息
    private ImageView ivGoodsPic;
    private TextView tvGoodsName;
    private TextView tvGoodsPrice;
    private TextView tvGoodsNum;
    private TextView tvApplyNum;
    private TextView tvSpplyMoney;
    private TextView tvApplyDaigou;
    private TextView tvApplyTime;
    private TextView tvApplyPeople;
    private TextView tvApplyPhone;
    private TextView tvApplyAddress;

    //苏宁售后详情 信息
    private SnAfterSaleDetialEntity.DataBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_activity_after_sale_details);
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
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        //状态描述
        tvOrderStatusDescribe = (TextView) findViewById(R.id.tv_order_status_describe);
        //退款成功 状态信息展示
        llStatusSuccess = (LinearLayout) findViewById(R.id.ll_status_success);
        tvRefundTotal = (TextView) findViewById(R.id.tv_refund_total);
        tvRefundMoney = (TextView) findViewById(R.id.tv_refund_money);
        tvRefundDaigou = (TextView) findViewById(R.id.tv_refund_daigou);
        //退货信息
        ivGoodsPic = (ImageView) findViewById(R.id.iv_goods_pic);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);
        tvGoodsNum = (TextView) findViewById(R.id.tv_goods_num);
        tvApplyNum = (TextView) findViewById(R.id.tv_apply_num);
        tvSpplyMoney = (TextView) findViewById(R.id.tv_apply_money);
        tvApplyDaigou = (TextView) findViewById(R.id.tv_apply_daigou);
        tvApplyTime = (TextView) findViewById(R.id.tv_apply_time);
        tvApplyPeople = (TextView) findViewById(R.id.tv_apply_people);
        tvApplyPhone = (TextView) findViewById(R.id.tv_apply_phone);
        tvApplyAddress = (TextView) findViewById(R.id.tv_apply_address);
    }

    public void initData() {
        tvTitle.setText("退货详情");
        id = getIntent().getStringExtra("id");
        getSnAfterSaleApplyForData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);

    }

    /**
     * 苏宁售后记录详情
     */
    private void getSnAfterSaleApplyForData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getSnServiceDetial("suning_aftersale/suning_aftersale_info", id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnAfterSaleDetialEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SnAfterSaleDetialEntity entity) {
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

    private void setData() {
        tvOrderStatusDescribe.setVisibility(View.GONE);
        llStatusSuccess.setVisibility(View.GONE);
        if (mData.getIsRefund().equals("1")) {
            //退款成功
            tvOrderStatus.setText("退款成功");
            tvOrderTime.setText(DateUtils.timeStampToStrByChinese(mData.getRefundTime()));

            llStatusSuccess.setVisibility(View.VISIBLE);
            tvRefundTotal.setText(String.format("¥ %s", MyBigDecimal.add(mData.getPrice(), mData.getReturnQuan())));
            tvRefundMoney.setText(String.format("¥ %s", mData.getPrice()));
            tvRefundDaigou.setText(mData.getReturnQuan());
        } else {
            tvOrderTime.setText(DateUtils.timeStampToStrByChinese(mData.getApplyTime()));
            if (mData.getApplyStatus().equals("1")) {
                //已通过
                tvOrderStatus.setText("已通过");
                tvOrderStatusDescribe.setVisibility(View.VISIBLE);
                tvOrderStatusDescribe.setText("您的退货申请已通过，请耐心等待正在为您办理退款。");
            } else {
                //退货请求失败
                tvOrderStatus.setText("退货请求失败");
                tvOrderStatusDescribe.setVisibility(View.VISIBLE);
                tvOrderStatusDescribe.setText(String.format("退货失败原因：%s", mData.getReason()));
            }
        }
        GlideUtil.showImageWithSuffix(context, mData.getSkuPic(), ivGoodsPic);
        tvGoodsName.setText(mData.getSkuName());
        tvGoodsPrice.setText(String.format("¥ %s", mData.getSnPrice()));
        tvGoodsNum.setText(String.format("x %s", mData.getNum()));

        tvApplyNum.setText(mData.getNum());
        tvSpplyMoney.setText(mData.getPrice());
        tvApplyDaigou.setText(mData.getReturnQuan());
        tvApplyTime.setText(DateUtils.stampToDate(mData.getApplyTime() * 1000));
        tvApplyPeople.setText(mData.getName());
        tvApplyPhone.setText(mData.getMobile());
        tvApplyAddress.setText(mData.getAddress());

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
            default:
                break;
        }
    }


    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSnAfterSaleApplyForData();
                }
            }, 1000);
        }
    }
}
