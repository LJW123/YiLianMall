package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 查看已有驾驶证
 */
public class CheckHaveDrivingLicenceActivity extends BaseActivity implements View.OnClickListener {

    private TextView particulars_tv, checkName, checkCarId, checkFileno;
    private String name, carId, fileNo;
    private LinearLayout bgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_have_driving_licence);
        initToolbar();
        setToolbarTitle("驾驶证");
        hasBack(true);
        //获取上个界面传来的值
        name = getIntent().getExtras().getString("name");
        carId = getIntent().getExtras().getString("carId");
        fileNo = getIntent().getExtras().getString("fileNo");
        initView();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        particulars_tv = (TextView) findViewById(R.id.particulars_tv);
        checkName = (TextView) findViewById(R.id.check_name);
        checkCarId = (TextView) findViewById(R.id.check_car_id);
        checkFileno = (TextView) findViewById(R.id.check_fileno);
        //赋值  姓名  证件号 档案编号
        checkName.setText(name);
        checkCarId.setText(carId);
        checkFileno.setText(fileNo);
        bgTv = (LinearLayout) findViewById(R.id.bg_tv);
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
        if (i == R.id.bg_tv) {
            finish();
            //跳转到编辑页面
            SkipUtils.toCheckCorrectCarLicense(this);
        }else if (i==R.id.particulars_tv){
            finish();
            //跳转到编辑页面
            SkipUtils.toCheckCorrectCarLicense(this);
        }
    }
}
