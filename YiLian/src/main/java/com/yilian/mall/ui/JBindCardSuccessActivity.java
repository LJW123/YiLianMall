package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.AppManager;

/**
 * Created by Ray_L_Pain on 2017/7/13 0013.
 */

public class JBindCardSuccessActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView v3Back;
    @ViewInject(R.id.v3Title)
    TextView v3Title;
    @ViewInject(R.id.btn_ok)
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card_success);
        ViewUtils.inject(this);

        v3Back.setVisibility(View.INVISIBLE);
        v3Title.setText("添加银行卡");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AppManager.getInstance().killActivity(JBindCardCodeActivity.class);
                AppManager.getInstance().killActivity(JBindCardActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getInstance().killActivity(JBindCardCodeActivity.class);
        AppManager.getInstance().killActivity(JBindCardActivity.class);
    }
}
