package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 查看已有行驶证
 */
public class CheckHaveDrivingLicenseActivity extends BaseActivity implements View.OnClickListener {
    private TextView particulars_tv, checkDrivingName, checkDrivingCarNo, checkDrivingVin;
    private String name, carNo, vin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_have_driving_license);
        initToolbar();
        setToolbarTitle("行驶证");
        hasBack(true);
        //String name, String carNo, String vin
        name = getIntent().getStringExtra("name");
        carNo = getIntent().getStringExtra("carNo");
        vin = getIntent().getStringExtra("vin");
        initView();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);

    }

    @Override
    protected void initView() {
        particulars_tv = (TextView) findViewById(R.id.particulars_tv);
        checkDrivingName = (TextView) findViewById(R.id.check_driving_name);
        checkDrivingCarNo = (TextView) findViewById(R.id.check_driving_car_no);
        checkDrivingVin = (TextView) findViewById(R.id.check_driving_vin);
        checkDrivingName.setText(name);
        checkDrivingCarNo.setText(carNo);
        checkDrivingVin.setText(vin);
    }

    @Override
    protected void initListener() {
        particulars_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.particulars_tv) {
            SkipUtils.toCheckCorrectDrivingLicense(this);

        }
    }
}
