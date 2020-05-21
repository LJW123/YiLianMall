package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;

/**
 *  猜价格-碰运气保证金说明页
 * @author Ray_L_Pain
 * @date 2017/12/12 0012
 */

public class ActCashGuaranteeActivity extends BaseActivity {
    @ViewInject(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_guarantee);
        ViewUtils.inject(this);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
