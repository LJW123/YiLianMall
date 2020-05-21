package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.RegisterAccount;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Constants;

/**
 * Created by liuyuqi on 2016/11/2 0002.
 */
public class JPRegisterTwoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView cancel;
    private TextView textView3;
    private TextView select_phone;
    private EditText et_register_phone;
    private TextView select_code;
    private EditText et_register_code;
    private AccountNetRequest request;
    private String sms_code;
    private String phone;
    private Context context;
    private Button btnCompile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_activity_register_two);
        context = this;
        initView();
        initListener();
    }


    private void initView() {
        sms_code = getIntent().getStringExtra("SMS_code");
        phone = getIntent().getStringExtra("phone");
        back = (ImageView) findViewById(R.id.back);
        cancel = (ImageView) findViewById(R.id.cancel);
        textView3 = (TextView) findViewById(R.id.textView3);
        select_phone = (TextView) findViewById(R.id.select_phone);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        select_code = (TextView) findViewById(R.id.select_code);
        et_register_code = (EditText) findViewById(R.id.et_register_code);
        btnCompile = (Button) findViewById(R.id.button4);
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initListener() {
        et_register_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    et_register_phone.setFocusableInTouchMode(true);
                    et_register_phone.requestFocus();//设置光标
                    select_phone.setVisibility(View.VISIBLE);
                    et_register_phone.setTextSize(16);
                    et_register_phone.setHintTextColor(getResources().getColor(R.color.white));
                    et_register_phone.setTextColor(getResources().getColor(R.color.white));
                } else {
                    select_phone.setVisibility(View.INVISIBLE);
                    et_register_phone.setTextSize(15);
                    et_register_phone.setTextColor(getResources().getColor(R.color.regist_code));
                    et_register_phone.setHintTextColor(getResources().getColor(R.color.regist_code));

                }
            }
        });

        et_register_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    et_register_code.requestFocus();
                    et_register_code.setFocusableInTouchMode(true);
                    select_code.setVisibility(View.VISIBLE);
                    et_register_code.setTextSize(16);
                    et_register_code.setTextColor(getResources().getColor(R.color.white));
                    et_register_code.setHintTextColor(getResources().getColor(R.color.white));
                } else {
                    select_code.setVisibility(View.INVISIBLE);
                    select_code.setTextSize(15);
                    et_register_code.setTextColor(getResources().getColor(R.color.white));
                    et_register_code.setHintTextColor(getResources().getColor(R.color.regist_code));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (request != null) {
            request = null;
        }
    }

    public void complete(View view) {
        btnCompile.setEnabled(false);
        String pwd1 = et_register_phone.getText().toString().trim();
        String pwd2 = et_register_code.getText().toString().trim();
        if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            showToast("密码不能为空");
            return;
        }
        if (!pwd1.equals(pwd2)) {
            showToast(R.string.password_not_consistent);
            return;
        }
        if (!CommonUtils.passwordVerify(pwd1) || !CommonUtils.passwordVerify(pwd2)) {
            showToast("密码格式不正确,密码应是6-18位数字加字母组合");
            return;
        }
        if (pwd1.equals(pwd2)) {
            request = new AccountNetRequest(this);
            //用户设置的登录密码(16位md5值+SMS_code的md5值=伪装成32位md5值)
            String pwdMd5Str = CommonUtils.getMD5Str(pwd1) + CommonUtils.getMD5Str(sms_code);

            Logger.i("注册的加密密码pwdMd5Str" + pwdMd5Str);
            //注册
            startMyDialog();
            request.registerAccoiunt(phone, sms_code, pwdMd5Str, RegisterAccount.class, new RequestCallBack<RegisterAccount>() {

                @Override
                public void onSuccess(ResponseInfo<RegisterAccount> responseInfo) {
                    stopMyDialog();
                    RegisterAccount result = responseInfo.result;
                    switch (result.code) {
                        case 1:
                            //注册成功保存当前的数据
                            PreferenceUtils.writeStrConfig("token", result.token, context);
                            PreferenceUtils.writeStrConfig("given_integral", result.given_integral, context);
                            PreferenceUtils.writeStrConfig("integral_balance", result.integral_balance, context);
                            PreferenceUtils.writeStrConfig("given_coupon", result.given_coupon, context);

                            PreferenceUtils.writeBoolConfig("state", true, context);
                            RequestOftenKey.getUserInfor(context, sp);
                            et_register_phone.setEnabled(false);
                            et_register_code.setEnabled(false);
                            Intent intent = new Intent(context, JPMainActivity.class);
                            intent.putExtra(Constants.JPMAINREGISTER, true);
                            startActivity(intent);
                            finish();
                            break;
                        case -3:
                            showToast("系统繁忙，3-10秒后重试");
                            btnCompile.setEnabled(true);
                            break;
                        case -5:
                            showToast("验证码失效");
                            btnCompile.setEnabled(true);
                            break;
                        default:
                            showToast("" + result.code);
                            break;

                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                }
            });

        }
    }
}
