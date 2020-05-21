package com.leshan.ylyj.view.activity.creditmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.leshan.ylyj.testfor.R;


/**
 * 信用互查
 */
public class CreditCheckEachOtherActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_check_each_other);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        }
    }
}
