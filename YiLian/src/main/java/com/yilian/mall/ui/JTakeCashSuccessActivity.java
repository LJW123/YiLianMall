package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ray_L_Pain on 2017/7/12 0012.
 */

public class JTakeCashSuccessActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView v3Back;
    @ViewInject(R.id.v3Title)
    TextView v3Title;
    @ViewInject(R.id.btn_ok)
    Button btn_ok;
    @ViewInject(R.id.tv_card_nameandnum)
    TextView tv_card_nameandnum;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_money)
    TextView tv_money;

    private String card_name, card_num, card_time, card_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_cash_success);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        v3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        v3Title.setText("领奖励成功");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        card_name = getIntent().getStringExtra("card_name");
        card_num = getIntent().getStringExtra("card_num");
        card_time = getIntent().getStringExtra("card_time");
        card_money = getIntent().getStringExtra("card_money");

        tv_card_nameandnum.setText(card_name + "尾号" + card_num.substring(card_num.length() - 4, card_num.length()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date(Long.valueOf(card_time + "000")));
        tv_time.setText(time);
        tv_money.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(card_money)));
    }
}
