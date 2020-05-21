package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.receiver.SMSBroadcastReceiver;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DataCleanManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.PreferenceUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 找回密码界面
 */
public class RetrievePasswordActivity extends BaseActivity {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final int SEND_PHONE_CODE_TIME = 1001;
    static int time = Constants.SMS_COUNT_TIME;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    @ViewInject(R.id.etphone)
    private EditText etphone;
    @ViewInject(R.id.etphonecode)
    private EditText etphonecode;
    @ViewInject(R.id.bt_sendphonecode)
    private Button bt_sendphonecode;
    @ViewInject(R.id.bt_retrievePassword)
    private Button bt_retrievePassword;
    @ViewInject(R.id.etpassword)
    private EditText etpassword;
    @ViewInject(R.id.etpasswordtest)
    private EditText etpasswordtest;
    @ViewInject(R.id.img_retrievepassword)
    private ImageView img_retrievepassword;
    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.tv_send_phone_code)
    private TextView tv_send_phone_code;
    @ViewInject(R.id.ll_retrievepassword_one)
    private LinearLayout ll_retrievepassword_one;
    @ViewInject(R.id.ll_retrievepassword_two)
    private LinearLayout ll_retrievepassword_two;
    private AccountNetRequest accountNetRequest;
    private Timer timer;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_PHONE_CODE_TIME:
                    time = time - 1;
                    if (time <= 0) {
                        timer.cancel();
                        timer = null;
                        bt_sendphonecode.setEnabled(true);
                        bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                        bt_sendphonecode.setTextColor(0xffffffff);
                        time = Constants.SMS_COUNT_TIME;
                        bt_sendphonecode.setText("点击发送验证码");

                    } else {
                        bt_sendphonecode.setText("(" + time + "s)重新发送");
                        bt_sendphonecode.setBackgroundResource(R.mipmap.bgtwo);
                        bt_sendphonecode.setTextColor(0xffacacac);
                    }

                    break;

            }
        }
    };
    private int verifyType = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrievepassword);

        ViewUtils.inject(this);
        initView();
        initListener();
        accountNetRequest = new AccountNetRequest(mContext);

        tv_back.setText(this.getIntent().getStringExtra("title"));

        etphone.addTextChangedListener(new watcher(etphone));
        etphonecode.addTextChangedListener(new watcher(etphonecode));
        etpassword.addTextChangedListener(new watcher(etpassword));
        etpasswordtest.addTextChangedListener(new watcher(etpasswordtest));

        if (this.getIntent().getStringExtra("title").equals("设置密码") && !sp.getString("phone", "1").equals("1")) {
            etphone.setText(sp.getString("phone", ""));
        }
        verifyType = getIntent().getIntExtra("verifyType", 0);
        if (time != Constants.SMS_COUNT_TIME) {
            startTimer();
        }

    }

    private void initListener() {
        ivImgCodeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAlertDialog();

            }
        });
        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {//验证码确定按钮
            @Override
            public void onClick(View v) {
                String imageCode = etImgCode.getText().toString();
                if (TextUtils.isEmpty(imageCode)) {
                    showToast("请输入图片中的内容");
                    return;
                }
             sendSmsCode(imageCode);

            }
        });
        ivImgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshImgCode();
            }
        });
    }

    private AlertDialog alertDialog;
    private View inflate;
    private ImageView ivImgCode;
    private Button btnDialogConfirm;
    private EditText etImgCode;
    private String inputPhone;
    private TextView tvImgCodeError;
    private ImageView ivImgCodeClose;

    private void initView() {
        {//验证码弹窗
            alertDialog = new AlertDialog.Builder(this).create();
            inflate = View.inflate(this, R.layout.login_module_dialog_verification_code, null);
            alertDialog.setView(inflate);
            alertDialog.setCancelable(true);

            etImgCode = (EditText) inflate.findViewById(R.id.et_img_code);
            etImgCode.setInputType(InputType.TYPE_CLASS_PHONE);
            ivImgCode = (ImageView) inflate.findViewById(R.id.iv_img_code);
            btnDialogConfirm = (Button) inflate.findViewById(R.id.btn_dialog_confirm);
            tvImgCodeError = (TextView) inflate.findViewById(R.id.tv_img_code_error);
            ivImgCodeClose = (ImageView) inflate.findViewById(R.id.iv_close);
        }
    }

    void startTimer() {
        if (timer == null) {
            timer = new Timer(true);
        }
        timer.schedule(new TimerTask() {
            public void run() {
                Message msg = handler.obtainMessage(SEND_PHONE_CODE_TIME);
                handler.sendMessage(msg);
            }
        }, 1000, 1000);
    }

    @Override
    protected void onDestroy() {
        if (mSMSBroadcastReceiver != null) {
            unregisterReceiver(mSMSBroadcastReceiver);
        }

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        time = Constants.SMS_COUNT_TIME;
        bt_sendphonecode.setEnabled(true);
        super.onDestroy();
    }

    /**
     * 隐藏图形验证码提示框
     */
    private void dismissAlertDialog() {
        tvImgCodeError.setText(R.string.login_module_img_from_number);
        if (alertDialog != null & alertDialog.isShowing()) {
            alertDialog.dismiss();
            etImgCode.setText("");
        }
    }

    /**
     * 弹出图形验证码界面
     */
    private void showImgCodeDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
        if (alertDialog.isShowing()) {
            refreshImgCode();
        }
    }

    /**
     * 刷新图形验证码
     */
    private void refreshImgCode() {
        ImgCodeUtil.setImgCode(etphone.getText().toString().trim(), ivImgCode, this);
    }

    /**
     * 点击发送验证码
     *
     * @param view
     */
    public void sendPhoneCode(View view) {
        if (verifyType == 3) {
            showImgCodeDialog();
        }else{
            sendSmsCode("");
        }

    }

    private void sendSmsCode(String verifyCode) {
        // 获取验证码
        if (CommonUtils.isPhoneNumer(etphone.getText().toString())) {
            bt_sendphonecode.setEnabled(false);
            startMyDialog();
            accountNetRequest.getSmsVerifyCodeNew(etphone.getText().toString(), verifyType, verifyCode, BaseEntity.class, new RequestCallBack<BaseEntity>() {
                @Override
                public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                    stopMyDialog();

                    if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code + "")) {
                        //生成广播处理
                        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
                        //实例化过滤器并设置要过滤的广播
                        IntentFilter intentFilter = new IntentFilter(ACTION);
                        intentFilter.setPriority(Integer.MAX_VALUE);
                        //注册广播
                        RetrievePasswordActivity.this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

                        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                            @Override
                            public void onReceived(String message) {
                                String regEx = "[^0-9]";
                                Pattern p = Pattern.compile(regEx);
                                Matcher m = p.matcher(message);

                                etphonecode.setText(m.replaceAll("").trim());
                                if (bt_retrievePassword.getText().toString().equals("验 证")) {
                                    retrievePassword(bt_retrievePassword);
                                }
                            }
                        });

                        showToast(R.string.send_success);
                        dismissAlertDialog();
                        img_retrievepassword.setImageResource(R.mipmap.retrievepasswordtwo);
                        startTimer();

                    } else {
                        bt_sendphonecode.setEnabled(true);
                        //如果返回码为0正码图形验证码验证错误

                        switch (responseInfo.result.code) {
                            case 0:
                                showImgCodeDialog();
                                stopMyDialog();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                    bt_sendphonecode.setEnabled(true);
                }
            });
        } else {
            showToast(R.string.phone_number_not_legal);
        }
    }

    /**
     * 验证按钮点击事件
     *
     * @param view
     */
    public void retrievePassword(View view) {

        String password = CommonUtils.getMD5Str(etpassword.getText().toString())
                + CommonUtils.getMD5Str(etphonecode.getText().toString());

        if (!((Button) view).getText().toString().equals("验 证")) {

            if (!etpassword.getText().toString().equals(etpasswordtest.getText().toString())) {//两次密码不一致
                showToast(R.string.password_not_consistent);
                return;
            }

            if (!CommonUtils.passwordVerify(etpassword.getText().toString())) {
                showToast(R.string.password_format);
                return;
            }
        }

        // 找回密码
        if (CommonUtils.isPhoneNumer(etphone.getText().toString())) {

            if (!((Button) view).getText().equals("确定")) {

                accountNetRequest.checkSmsVerifyCode(etphone.getText().toString(), etphonecode.getText().toString(), BaseEntity.class, new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {

                        if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code + "")) {
                            if (bt_retrievePassword.getText().equals("确定")) {
                                logout();//找回密码成功，先退出账号
                                showToast(R.string.password_retrieve_success);
                                startActivity(new Intent(RetrievePasswordActivity.this, LeFenPhoneLoginActivity.class));
//                                AppManager.getInstance().killAllActivity();
                            } else {
                                ll_retrievepassword_one.setVisibility(View.GONE);
                                ll_retrievepassword_two.setVisibility(View.VISIBLE);
                                bt_retrievePassword.setText("确定");
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        stopMyDialog();
                        showToast(R.string.net_work_not_available);
                    }
                });

            } else {

                accountNetRequest.resetLoginPwd(etphone.getText().toString(), password.toLowerCase(), BaseEntity.class, new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                        if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code + "")) {
                            if (bt_retrievePassword.getText().equals("确定")) {
                                logout();//重置密码成功，先退出账号
                                showToast(R.string.password_retrieve_success);
                                startActivity(new Intent(RetrievePasswordActivity.this, LeFenPhoneLoginActivity.class));
                                finish();
                            } else {
                                ll_retrievepassword_one.setVisibility(View.GONE);
                                ll_retrievepassword_two.setVisibility(View.VISIBLE);
                                bt_retrievePassword.setText("确定");
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        stopMyDialog();
                        showToast(R.string.net_work_not_available);
                    }
                });

            }
        } else {
            showToast(R.string.phone_number_not_legal);
        }

    }

    public class watcher implements TextWatcher {

        private EditText edit = null;

        public watcher(EditText ed) {
            this.edit = ed;
        }

        @Override
        public void afterTextChanged(Editable s) {


            if (edit == etphone) {

                if (CommonUtils.isPhoneNumer(etphone.getText().toString()) && time == Constants.SMS_COUNT_TIME) {
                    bt_sendphonecode.setEnabled(true);
                    bt_sendphonecode.setTextColor(0xffffffff);
                    bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);

                } else if (etphone.getText().toString().length() == 11 &&
                        !(CommonUtils.isPhoneNumer(etphone.getText().toString()))) {
                    showToast(R.string.phone_number_not_legal);
                    bt_sendphonecode.setEnabled(false);
                } else {
                    bt_sendphonecode.setEnabled(false);
                }

            } else {
                if (etphonecode.getText().toString().length() == 6 &&
                        CommonUtils.isPhoneNumer(etphone.getText().toString()) &&
                        etpassword.getText().toString().equals(etpasswordtest.getText().toString())) {
                    bt_retrievePassword.setEnabled(true);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    /**
     * 退出
     *
     */
    public void logout() {
        startMyDialog();
        accountNetRequest.LogOut(BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, false, mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_TOKEN, "0", mContext);
                PreferenceUtils.writeStrConfig(Constants.AGENT_STATUS, "0", mContext);//重置申请服务中心状态，保证首页服务中心状态正常
                PreferenceUtils.writeStrConfig(Constants.MERCHANT_LEV, "0", mContext);//重置会员等级，保证首页会员等级状态正常

                showToast("您已退出登录状态");
                Intent intent = new Intent(mContext, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                startActivity(intent);
                finish();
                DataCleanManager.cleanSharedPreference(mContext);
                FileUtils.delFile("/com.yilian/userphoto/userphoto.png");

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                Logger.i("退出登录 Exception" + e + " content " + s);
            }
        });
    }

}
