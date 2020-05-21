package com.yilian.mall.jd.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.networkingmodule.entity.jd.JDPayInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnPayInfoEntity;

public class JDPaySuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final String TAG = "JDPaySuccessActivity";
    public static final String PAY_SIDE_TAG = "pay_side_tag";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvPayType;
    private TextView tvPayOrderPrice;
    private LinearLayout llReturnBean;
    private TextView tvGavePower;
    private Button btnLookOverOrderDetail;
    private JDPayInfoEntity jdPayResultInfo;
    private TextView tvDaiGouQuanMoney;
    private PaySide paySide;
    private SnPayInfoEntity snPayInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdpay_success);
        initView();
        initData();
    }

    private void initView() {
        tvDaiGouQuanMoney = findViewById(R.id.tv_pay_dai_gou_quan);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("订单支付成功");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        tvPayOrderPrice = (TextView) findViewById(R.id.tv_pay_order_price);
        llReturnBean = findViewById(R.id.ll_return_bean);
        tvGavePower = (TextView) findViewById(R.id.tv_gave_power);
        btnLookOverOrderDetail = (Button) findViewById(R.id.btn_look_over_order_detail);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnLookOverOrderDetail.setOnClickListener(this);
    }

    private void initData() {
        paySide = (PaySide) getIntent().getSerializableExtra(PAY_SIDE_TAG);
        switch (paySide) {
            case JD:
                setJDPayResultView();
                break;
            case Card_JD:
                setCardJDPayResultView();
                break;
            case SN:
                setSnPayResultView();
                break;
            default:
                break;
        }
    }

    private void setJDPayResultView() {
        jdPayResultInfo = (JDPayInfoEntity) getIntent().getSerializableExtra(TAG);
        tvPayType.setText(jdPayResultInfo.payTypeStr);
        tvPayOrderPrice.setText(String.format("¥%s", jdPayResultInfo.totalCash));
        tvGavePower.setText(jdPayResultInfo.returnBean);
        tvDaiGouQuanMoney.setText(jdPayResultInfo.orderDaiGouQuanMoney);
    }

    private void setCardJDPayResultView() {
        jdPayResultInfo = (JDPayInfoEntity) getIntent().getSerializableExtra(TAG);
        tvPayType.setText(jdPayResultInfo.payTypeStr);
        tvPayOrderPrice.setText(String.format("¥%s", jdPayResultInfo.totalCash));
        llReturnBean.setVisibility(View.GONE);
    }

    private void setSnPayResultView() {

        snPayInfoEntity = (SnPayInfoEntity) getIntent().getSerializableExtra(TAG);
        tvPayType.setText(snPayInfoEntity.paymentTypeStr);
        tvPayOrderPrice.setText(String.format("¥%s", snPayInfoEntity.totalCash));
        tvGavePower.setText(snPayInfoEntity.returnBean);
        tvDaiGouQuanMoney.setText(snPayInfoEntity.coupon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_look_over_order_detail:
                finish();
                switch (paySide) {
                    case SN:
                        JumpSnActivityUtils.toSnOrderDetails(mContext, snPayInfoEntity.snOrderId);
                        break;
                    case JD:
                    case Card_JD:
                        JumpJdActivityUtils.toJDOrderDetails(mContext, jdPayResultInfo.jdOrderId);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 支付来源
     */
    public enum PaySide {
        /**
         * 京东支付
         */
        JD,
        /**
         * 苏宁支付
         */
        SN,
        /**
         * 购物车 京东支付
         */
        Card_JD
    }
}
