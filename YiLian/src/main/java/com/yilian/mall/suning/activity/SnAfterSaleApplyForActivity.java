package com.yilian.mall.suning.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForDataEntity;
import com.yilian.networkingmodule.entity.suning.snEventBusModel.SnOrderTabModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 苏宁售后申请
 *
 * @author Zg on 2017/8/1.
 */
public class SnAfterSaleApplyForActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvOrderTime;
    private ImageView ivGoodsPic;
    private TextView tvGoodsName;
    private TextView tvGoodsPrice;
    private TextView tvGoodsNum;
    private TextView tvRefundMoney;
    private TextView tvRefundDaigou;

    /**
     * 传递过来的信息
     */
    private String snOrderId, orderSnPrice, coupon, skuId, skuPic, skuName, goodsPrice, goodsNum;
    /**
     * 提交申请
     */
    private TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_activity_after_sale_apply_for);
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

        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        ivGoodsPic = (ImageView) findViewById(R.id.iv_goods_pic);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);
        tvGoodsNum = (TextView) findViewById(R.id.tv_goods_num);
        tvRefundMoney = (TextView) findViewById(R.id.tv_refund_money);
        tvRefundDaigou = (TextView) findViewById(R.id.tv_refund_daigou);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
    }

    public void initData() {
        tvTitle.setText("退货申请");
        //京东订单号
        snOrderId = getIntent().getStringExtra("snOrderId");
        //订单金额
        orderSnPrice = getIntent().getStringExtra("orderSnPrice");
        //订单使用的代购券
        coupon = getIntent().getStringExtra("coupon");
        //商品id
        skuId = getIntent().getStringExtra("skuId");
        //商品图片
        skuPic = getIntent().getStringExtra("skuPic");
        //商品名
        skuName = getIntent().getStringExtra("skuName");
        //商品单价
        goodsPrice = getIntent().getStringExtra("goodsPrice");
        //商品数量
        goodsNum = getIntent().getStringExtra("goodsNum");
        setData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }


    private void setData() {
        //商品信息
        GlideUtil.showImageWithSuffix(context, skuPic, ivGoodsPic);
        tvGoodsName.setText(skuName);
        tvGoodsPrice.setText(String.format("¥ %s", goodsPrice));
        tvGoodsNum.setText(String.format("x %s", goodsNum));
        //可退回代购券
        BigDecimal orderTotalPrice = new BigDecimal(goodsPrice)
                .multiply(new BigDecimal(goodsNum));
        BigDecimal ticket = orderTotalPrice
                .multiply(new BigDecimal(coupon))
                .divide(new BigDecimal(orderSnPrice), 2, BigDecimal.ROUND_DOWN);

        String money = orderTotalPrice.subtract(new BigDecimal(ticket.toPlainString())).toPlainString();

        tvRefundMoney.setText(String.format("¥ %s", money));
        tvRefundDaigou.setText(ticket.toPlainString());

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
            case R.id.tv_submit:
                //提交
                setAftersaleAction();
                break;
            default:
                break;
        }
    }


    /**
     * 苏宁申请售后
     */
    private void setAftersaleAction() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                setSnAftersaleAction("suning_aftersale/suning_aftersale_apply",
                        skuId, goodsNum, snOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        //显示申请记录tab
                        JumpSnActivityUtils.toSnHomePageActivity(context, SnHomePageActivity.TAB_ORDER, null);
                    }
                });
        addSubscription(subscription);
    }


    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
