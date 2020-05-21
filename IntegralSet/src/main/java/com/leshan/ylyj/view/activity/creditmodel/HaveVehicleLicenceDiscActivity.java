package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;


/**
 * 已有行驶证
 */
public class HaveVehicleLicenceDiscActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout vehicle_licence_disc_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_vehicle_licence_disc);
        InitializationView();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    private void InitializationView() {
        vehicle_licence_disc_ll = findViewById(R.id.vehicle_licence_disc_ll);
        vehicle_licence_disc_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.vehicle_licence_disc_ll) {//                SkipUtils.toCheckHaveDrivingLicense(this);

        }
    }
}
