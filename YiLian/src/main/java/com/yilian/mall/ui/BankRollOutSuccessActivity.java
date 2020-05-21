package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.StringFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 乐分宝转出成功界面
 * Created by Administrator on 2016/6/27.
 */
public class BankRollOutSuccessActivity extends BaseActivity {
    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.tv_user_money)
    private TextView tv_user_money;

    @ViewInject(R.id.tv_balance)
    private TextView tv_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_roll_out_success);

        ViewUtils.inject(this);
        tvBack.setText("结果详情");

        init();
    }

    private void init() {
        String avible_lebi = getIntent().getStringExtra("avible_lebi");
        String lebi = getIntent().getStringExtra("lebi");
        String time = getIntent().getStringExtra("time");
        tv_user_money.setText("-"+String.format("%.2f", Float.parseFloat(lebi) / 100));
        tv_balance.setText(Html.fromHtml("<font color=\"#3f4447\" size=42>"+String.format("%.2f", Float.parseFloat(avible_lebi) / 100)+"</font><br/>"+ StringFormat.formatDate(time)));
    }
}
