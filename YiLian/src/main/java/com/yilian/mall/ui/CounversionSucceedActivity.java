package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;

/**
 * Created by liuyuqi on 2017/4/29 0029.
 * 兑换成功
 */
public class CounversionSucceedActivity extends BaseActivity implements View.OnClickListener {
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private TextView tvConversionNumber;
    private TextView tvConversionTime;
    private Button btnConfirm;
    private String orderId;
    private String downTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion_secceed_activity);
        initView();
    }


    private void initView() {

        orderId = getIntent().getStringExtra("orderId");
        downTime = getIntent().getStringExtra("downTime");

        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("兑换成功");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.INVISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tvConversionNumber = (TextView) findViewById(R.id.tv_conversion_number);
        tvConversionNumber.setText(orderId);
        tvConversionTime = (TextView) findViewById(R.id.tv_conversion_time);
        tvConversionTime.setText(DateUtils.timeStampToStr(Long.parseLong(downTime)));
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        tvRight.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
            case R.id.btn_confirm:
                finish();
                break;
        }
    }
}
