package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 已有身份证
 */
public class ExistingIDCardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout existing_idcard_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_idcard);
        initView();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        existing_idcard_ll = (LinearLayout) findViewById(R.id.existing_idcard_ll);
    }

    @Override
    protected void initListener() {
        existing_idcard_ll.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.existing_idcard_ll) {
            SkipUtils.toCheckTheIdentityCard(this);

        }
    }
}
