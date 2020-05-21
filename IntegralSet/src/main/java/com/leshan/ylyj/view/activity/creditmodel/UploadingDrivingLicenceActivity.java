package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 上传驾驶证
 */
public class UploadingDrivingLicenceActivity extends BaseActivity implements View.OnClickListener {

    private TextView uploading_tv;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading_driving_licence);
        str = getIntent().getExtras().getString("str");//获取上个页面传过来的标识
        initView();
        initListener();
        initToolbar();
        if ("licence".equals(str)) {
            setToolbarTitle("驾驶证");
        } else {
            setToolbarTitle("行驶证");
        }
        hasBack(true);

        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        uploading_tv = findViewById(R.id.uploading_tv);
    }

    @Override
    protected void initListener() {
        uploading_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.uploading_tv) {
            if ("licence".equals(str)) {//根据上级页面传过来的数据判断驾驶证或行车
                SkipUtils.toMyCarLicense(this);//驾驶证
            } else {
                SkipUtils.toDrivingLicense(this);//行车证
            }

        }
    }
}
