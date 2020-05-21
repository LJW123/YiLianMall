package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 支付成功
 */
public class PayOKActivity extends BaseActivity {

    @ViewInject(R.id.store_name)
    private TextView storeName;

    @ViewInject(R.id.tv_type)
    TextView tvPayType;

    @ViewInject(R.id.deal_id)
    private TextView dealId;

    @ViewInject(R.id.deal_time)
    private TextView dealTime;

    @ViewInject(R.id.deal_intrgral)
    private TextView dealIntrargl;

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.right_textview)
    private TextView rightTextView;

    private String mType;//支付方式
    private String mSum;//支付金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ok);
        ViewUtils.inject(this);
        init();
    }

    public void rightTextview(View view) {
        finish();
    }

    public void onBack(View view) {
        finish();
    }

    private void init() {
        tvBack.setText("交易详情");
        rightTextView.setText("完成");

        storeName.setText(this.getIntent().getStringExtra("name"));
        dealId.setText(this.getIntent().getStringExtra("deal_id"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(Long.valueOf(this.getIntent().getStringExtra("deal_time") + "000")));
        dealTime.setText(time);
        dealIntrargl.setText(Html.fromHtml(this.getIntent().getStringExtra("notice")));
        mType = this.getIntent().getStringExtra("type");
        mSum = this.getIntent().getStringExtra("sum");
        switch (Integer.parseInt(mType)) {
            case 0:
                tvPayType.setText("手机支付");
                //dealIntrargl.setText(mSum + "乐分币");
                break;
            case 5:
            case 7:
                tvPayType.setText("手机支付");
                //dealIntrargl.setText(mSum + "乐享币");
                break;
            case 8:
                tvPayType.setText("手机支付");
                //dealIntrargl.setText(mSum + "抵扣券");
                break;

            default:
                break;
        }
    }
};
