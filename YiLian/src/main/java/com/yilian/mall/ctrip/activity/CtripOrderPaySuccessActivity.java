package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripPayInfoEntity;


public class CtripOrderPaySuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tvLeft;
    private TextView v3Title;
    private TextView tvRight;
    private TextView tvRight2;
    private ImageView v3Back;
    private TextView tvPayType;
    private TextView tvPayOrderPrice;
    private TextView tvGavePower;
    private Button btnLookOverOrderDetail;
    private CtripPayInfoEntity ctripPayInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrip_order_pay_success);
        initView();
        initData();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);

        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("订单支付成功");
        tvRight = (TextView) findViewById(R.id.tv_right);

        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        tvPayOrderPrice = (TextView) findViewById(R.id.tv_pay_order_price);
        tvGavePower = (TextView) findViewById(R.id.tv_gave_power);

        btnLookOverOrderDetail = (Button) findViewById(R.id.btn_look_over_order_detail);


        v3Back.setOnClickListener(this);
        btnLookOverOrderDetail.setOnClickListener(this);
    }

    private void initData() {
        ctripPayInfoEntity = (CtripPayInfoEntity) getIntent().getSerializableExtra("ctripPayInfoEntity");
        tvPayType.setText(ctripPayInfoEntity.paymentTypeStr);
        tvPayOrderPrice.setText(String.format("¥%s", ctripPayInfoEntity.totalCash));
        tvGavePower.setText(ctripPayInfoEntity.returnBean);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_look_over_order_detail:
                JumpCtripActivityUtils.toCtripOrderDetails(mContext,ctripPayInfoEntity.orderId);
                finish();
                break;
            default:
                break;
        }
    }


}
