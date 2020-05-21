package com.yilian.loginmodule;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2016/11/2 0002.
 * 找回密码的设置新密码界面界面
 */
public class LeFenResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView cancel;
    private TextView textView3;
    private TextView select_phone;
    private EditText et_register_phone;
    private Button complie;
    private String phone;
    private String sms_code;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_activity_reset_password);
        context = this;
        initView();

    }


    private void initView() {
        phone = getIntent().getStringExtra("phone");
        sms_code = getIntent().getStringExtra("SMS_code");
        back = (ImageView) findViewById(R.id.back);
        cancel = (ImageView) findViewById(R.id.cancel);
        textView3 = (TextView) findViewById(R.id.textView3);
        select_phone = (TextView) findViewById(R.id.select_phone);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        complie = (Button) findViewById(R.id.complie);
        et_register_phone.setTextColor(getResources().getColor(android.R.color.white));

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        complie.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.cancel) {
            finish();

        } else if (i == R.id.complie) {
            complie();

        }
    }

    private void complie() {
        String pwd = et_register_phone.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空");
            return;
        }
        if (!CommonUtils.passwordVerify(pwd)) {
            showToast(R.string.login_module_password_format);
            return;
        }
        final String password = CommonUtils.getMD5Str(pwd) + CommonUtils.getMD5Str(sms_code);
        RetrofitUtils.getInstance(mContext).resetLoginPassword(phone, password, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                BaseEntity body = response.body();
                if(!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                    showToast(R.string.login_module_service_exception);
                    return;
                }
                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                    switch (body.code) {
                        case 1:
                            showToast("修改成功，请您重新登录");
                            sp.edit().putBoolean("state", false).commit();
                            startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                            finish();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                showToast(R.string.login_module_net_work_not_available);
            }
        });
    }

}
