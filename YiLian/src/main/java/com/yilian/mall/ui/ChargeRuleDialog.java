package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;

/**
 * @author Ray_L_Pain
 * @date 2017/11/16 0016
 */

public class ChargeRuleDialog extends BaseActivity {

    private ImageView ivClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_charge_rule);


        initView();
    }

    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
