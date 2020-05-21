package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 已有驾驶证
 */
public class HaveDrivingLicenceActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout driving_licence_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_driving_licence);
        InitializationView();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    private void InitializationView() {
        driving_licence_ll = (LinearLayout) findViewById(R.id.driving_licence_ll);
        driving_licence_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.driving_licence_ll) {
            SkipUtils.toCheckHaveDrivingLicence(this);

        }
    }
}
