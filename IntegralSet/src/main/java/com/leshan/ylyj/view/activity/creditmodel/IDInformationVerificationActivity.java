package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 身份证信息验证
 */
public class IDInformationVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView idinformation_cancel;
    private TextView idinformation_confirm_and_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idinformation_verification);
        InitializationView();
        StatusBarUtil.setColor(this, Color.WHITE);

    }

    private void InitializationView() {
        idinformation_cancel = findViewById(R.id.idinformation_cancel);
        idinformation_confirm_and_submit = findViewById(R.id.idinformation_confirm_and_submit);
        idinformation_cancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        idinformation_cancel.getPaint().setAntiAlias(true);
        idinformation_cancel.setOnClickListener(this);
        idinformation_confirm_and_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.idinformation_confirm_and_submit) {
            SkipUtils.toConfirmIdentityInformation(this);

        } else if (i == R.id.idinformation_cancel) {
            finish();

        }
    }
}
