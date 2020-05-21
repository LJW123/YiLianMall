package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.Constants;

/**
 * 支付成功页面
 */
public class NOrderPaySuccessActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_remain_shopping_vouchers)
    TextView tvPayWay;
    MallNetRequest mallNetRequest;
    @ViewInject(R.id.ll_gave_power)
    private LinearLayout llGavePower;

    @ViewInject(R.id.tv_gave_power)
    private TextView tvGavePower;
    @ViewInject(R.id.v3Back)
    private ImageView iv_back;
    @ViewInject(R.id.v3Title)
    private TextView tv_title;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.tv_lexiangbi)
    private TextView tv_lexiangbi;
    @ViewInject(R.id.tv_tishi)
    private TextView tv_tishi;
    @ViewInject(R.id.btn_surePay)
    private Button btn_surePay;

    @ViewInject(R.id.tv_dai_gou_quan)
    private TextView tvDaiGouQuan;
    @ViewInject(R.id.tv_ledou)
    private TextView leDou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_norder_pay_success_v3);
        ViewUtils.inject(this);
        initView();
        initData();
        initListener();
        //刷新个人页面标识
        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
    }

    private void initView() {
        tv_title.setText("支付成功");
        Intent intent = getIntent();
        tvPayWay.setText("奖励支付");
        String time = intent.getStringExtra("deal_time");
        String lebi = intent.getStringExtra("lebi");

        String totalTotalBean = intent.getStringExtra("totalTotalBean");
        String daiGouQuanMoney = intent.getStringExtra("order_quan");
        if (TextUtils.isEmpty(daiGouQuanMoney)) {
            tvDaiGouQuan.setText("0");
        }else{
            tvDaiGouQuan.setText(MoneyUtil.getLeXiangBiNoZero(daiGouQuanMoney));
        }
        String returnBean = intent.getStringExtra("returnBean");
        if (TextUtils.isEmpty(returnBean)) {
            leDou.setText("0");
        } else {
            leDou.setText(MoneyUtil.getLeXiangBi(returnBean));
        }
        String subsidy = intent.getStringExtra("subsidy");
        if (TextUtils.isEmpty(subsidy) || com.yilian.mylibrary.NumberFormat.convertToInt(subsidy, 0) == 0) {
            llGavePower.setVisibility(View.GONE);
        } else {
            llGavePower.setVisibility(View.VISIBLE);
            tvGavePower.setText(MoneyUtil.getLeXiangBi(subsidy));
        }


        String coupon = intent.getStringExtra("coupon");
        if (TextUtils.isEmpty(coupon)) {
            coupon = "";
        }
        String giveVouncher = intent.getStringExtra("giveVouncher");
        int orderNumber = NumberFormat.convertToInt(intent.getStringExtra("orderNumber"), 1);

        if (orderNumber > 1) {
            tv_tishi.setVisibility(View.VISIBLE);
        } else {
            tv_tishi.setVisibility(View.GONE);
        }
        tv_time.setText(StringFormat.formatDate(time));
        tv_lexiangbi.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(lebi)));


    }

    private void initData() {

    }

    private void initListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                startActivity(intent);
            }
        });

        btn_surePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    Intent intent = new Intent(mContext, MyMallOrderListActivity2.class);
                    intent.putExtra("order_Type", 2);
                    startActivity(intent);
                    finish();
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, JPMainActivity.class);
        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }


    }

}
