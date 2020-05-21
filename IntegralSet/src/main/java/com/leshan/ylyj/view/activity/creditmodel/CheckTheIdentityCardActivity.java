package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;


/**
 * 查看身份证
 */
public class CheckTheIdentityCardActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout qr_code_rl;//二维码
    private LinearLayout qr_code_ll;//下面二维码
    private ImageView shring_img;//二维码下面的缩小按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_the_identity_card);
        initToolbar();
        setToolbarTitle("身份证");
        hasBack(true);
        initView();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        qr_code_rl = (RelativeLayout) findViewById(R.id.qr_code_rl);
        qr_code_ll = (LinearLayout) findViewById(R.id.qr_code_ll);
        shring_img = (ImageView) findViewById(R.id.shring_img);
    }

    @Override
    protected void initListener() {
        qr_code_rl.setOnClickListener(this);
        shring_img.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.qr_code_rl) {
            qr_code_ll.setVisibility(View.VISIBLE);//显示大二维码
            qr_code_rl.setVisibility(View.GONE);//隐藏二维码按钮

        } else if (i == R.id.shring_img) {
            qr_code_ll.setVisibility(View.GONE);//隐藏大二维码
            qr_code_rl.setVisibility(View.VISIBLE);//显示二维码按钮

        }
    }
}
