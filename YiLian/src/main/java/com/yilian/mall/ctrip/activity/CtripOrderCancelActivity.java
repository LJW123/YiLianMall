package com.yilian.mall.ctrip.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 取消订单
 *
 * @author Created by Zg on 2018/9/22.
 */

public class CtripOrderCancelActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvOrderPrice;
    /**
     * 其他APP更便宜
     */
    private TextView tv1;
    /**
     * 酒店质量差
     */
    private TextView tv2;
    /**
     * 行程变更
     */
    private TextView tv3;
    /**
     * 其他途径预订
     */
    private TextView tv4;
    /**
     * 入住信息填写错误
     */
    private TextView tv5;
    /**
     * 重复预订
     */
    private TextView tv6;
    /**
     * 其他
     */
    private TextView tv7;
    /**
     * 确认取消
     */
    private TextView tvConfirm;

    private String InclusiveAmount, ResIDValue,hotelId;
    //取消原因
    private String reason;

    private String CityCode,gdLat,gdLng,dayInclusiveAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_order_cancel);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
            }
        });

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        tvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        tv4 = (TextView) findViewById(R.id.tv_4);
        tv5 = (TextView) findViewById(R.id.tv_5);
        tv6 = (TextView) findViewById(R.id.tv_6);
        tv7 = (TextView) findViewById(R.id.tv_7);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);


    }

    public void initData() {
        tvTitle.setText("取消订单");

        InclusiveAmount = getIntent().getStringExtra("InclusiveAmount");
        ResIDValue = getIntent().getStringExtra("ResIDValue");
        hotelId = getIntent().getStringExtra("hotelId");

        CityCode = getIntent().getStringExtra("CityCode");
        gdLat = getIntent().getStringExtra("gdLat");
        gdLng = getIntent().getStringExtra("gdLng");
        dayInclusiveAmount = getIntent().getStringExtra("dayInclusiveAmount");

        tvOrderPrice.setText(String.format("订单总额¥%s", InclusiveAmount));

        varyViewUtils.showDataView();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

    }

    private void initBtn() {
        tv1.setTextColor(Color.parseColor("#333333"));
        tv1.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
        tv2.setTextColor(Color.parseColor("#333333"));
        tv2.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
        tv3.setTextColor(Color.parseColor("#333333"));
        tv3.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
        tv4.setTextColor(Color.parseColor("#333333"));
        tv4.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
        tv5.setTextColor(Color.parseColor("#333333"));
        tv5.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
        tv6.setTextColor(Color.parseColor("#333333"));
        tv6.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
        tv7.setTextColor(Color.parseColor("#333333"));
        tv7.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_unselect);
    }

    /**
     * 取消订单
     */
    private void cancelCtripOrder() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                cancelCtripOrder("xc_order/xc_cancel_order", ResIDValue,reason).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        EventBus.getDefault().post(new CtripOrderListUpdateModel(CtripOrderListUpdateModel.HandleType_cancle,ResIDValue));

                        JumpCtripActivityUtils.toCtripOrderCancelSuccessful(context,hotelId,CityCode,gdLat,gdLng,dayInclusiveAmount);
                        //关闭订单详情
                        AppManager.getInstance().killActivity(CtripOrderDetailsActivity.class);

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
            case R.id.tv_1:
                initBtn();
                reason = tv1.getText().toString();
                tv1.setTextColor(Color.parseColor("#4289FF"));
                tv1.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_2:
                initBtn();
                reason = tv2.getText().toString();
                tv2.setTextColor(Color.parseColor("#4289FF"));
                tv2.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_3:
                initBtn();
                reason = tv3.getText().toString();
                tv3.setTextColor(Color.parseColor("#4289FF"));
                tv3.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_4:
                initBtn();
                reason = tv4.getText().toString();
                tv4.setTextColor(Color.parseColor("#4289FF"));
                tv4.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_5:
                initBtn();
                reason = tv5.getText().toString();
                tv5.setTextColor(Color.parseColor("#4289FF"));
                tv5.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_6:
                initBtn();
                reason = tv6.getText().toString();
                tv6.setTextColor(Color.parseColor("#4289FF"));
                tv6.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_7:
                initBtn();
                reason = tv7.getText().toString();
                tv7.setTextColor(Color.parseColor("#4289FF"));
                tv7.setBackgroundResource(R.drawable.ctrip_order_cancel_why_bg_selected);
                break;
            case R.id.tv_confirm:
                //确认取消
                if (TextUtils.isEmpty(reason)) {
                    showToast("请选择取消原因");
                    return;
                }
                cancelCtripOrder();
                break;
            default:
                break;
        }
    }
}
