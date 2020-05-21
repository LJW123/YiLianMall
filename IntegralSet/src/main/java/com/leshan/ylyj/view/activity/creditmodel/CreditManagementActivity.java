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
 * 信用管理
 */
public class CreditManagementActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout credit_personal_information_ll, credit_my_white_ll, credit_my_guard_ll, credit_my_footprint_ll, credit_my_cross_check_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_management);
        initView();
        initListener();
        initData();
        initToolbar();
        setToolbarTitle("信用管理");
        StatusBarUtil.setColor(this, Color.WHITE);
        hasBack(true);
    }

    @Override
    protected void initView() {
        credit_personal_information_ll = (LinearLayout) findViewById(R.id.credit_personal_information_ll);
        credit_my_white_ll = (LinearLayout) findViewById(R.id.credit_my_white_ll);
        credit_my_guard_ll = (LinearLayout) findViewById(R.id.credit_my_guard_ll);
        credit_my_footprint_ll = (LinearLayout) findViewById(R.id.credit_my_footprint_ll);
        credit_my_cross_check_ll = (LinearLayout) findViewById(R.id.credit_my_cross_check_ll);

    }

    @Override
    protected void initListener() {
        credit_my_cross_check_ll.setOnClickListener(this);
        credit_my_footprint_ll.setOnClickListener(this);
        credit_my_guard_ll.setOnClickListener(this);
        credit_my_white_ll.setOnClickListener(this);
        credit_personal_information_ll.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.credit_personal_information_ll) {
            SkipUtils.toMyPersonalInfor(this, "2");

        } else if (i == R.id.credit_my_white_ll) {
            SkipUtils.toMyWhite(this);

        } else if (i == R.id.credit_my_guard_ll) {//守护
            SkipUtils.toMyCreditGuard(this);

        } else if (i == R.id.credit_my_footprint_ll) {//足迹
            SkipUtils.toMyCreditFootMark(this);

        } else if (i == R.id.credit_my_cross_check_ll) {
            SkipUtils.toCreditCheckEachOther(this);

        }
    }
}
