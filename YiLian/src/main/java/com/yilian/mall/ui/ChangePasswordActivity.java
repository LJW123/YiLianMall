package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.AppManager;

/**
 * 修改登录密码
 */
public class ChangePasswordActivity extends BaseActivity {
    @ViewInject(R.id.tv_back)
    private TextView tv_back;

    @ViewInject(R.id.etpassword)
    private EditText etpassword;

    @ViewInject(R.id.etnewpassword)
    private EditText etnewpassword;

    @ViewInject(R.id.etnewpasswordtest)
    private EditText etnewpasswordtest;

    private AccountNetRequest accountNetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ViewUtils.inject(this);
        tv_back.setText("修改密码");
        accountNetRequest = new AccountNetRequest(mContext);
    }

    public void changePassword(View view) {

        HttpUtils http = new HttpUtils();
        // 修改密码
        if (!etnewpassword.getText().toString().equals(etnewpasswordtest.getText().toString())) {
            showToast(R.string.password_not_consistent);
            return;
        }
        if (!CommonUtils.passwordVerify(etnewpassword.getText().toString())) {
            showToast(R.string.password_format);
            return;
        }
        startMyDialog();

        String oldPassword = CommonUtils.getMD5Str(etpassword.getText().toString()).toLowerCase();
        String newPassword = (CommonUtils.getMD5Str(etnewpassword.getText().toString()) + CommonUtils.getMD5Str(sp.getString("server_salt", "0"))).toLowerCase();

        accountNetRequest.modifyLoginPwd(oldPassword, newPassword, BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();

                if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code + "")) {
                    showToast("修改成功，请您重新登录");
                    sp.edit().putBoolean("state", false).commit();
                    startActivity(new Intent(ChangePasswordActivity.this, LeFenPhoneLoginActivity.class));
//                    AppManager.getInstance().killAllActivity();
                    AppManager.getInstance().killActivity(UserInfoActivity.class);
                    AppManager.getInstance().killActivity(AccountSecurityActivity.class);
                    AppManager.getInstance().killActivity(ChangePasswordActivity.class);
//                    finish();
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
