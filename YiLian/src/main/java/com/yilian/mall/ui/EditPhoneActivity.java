package com.yilian.mall.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.yilian.mall.receiver.SMSBroadcastReceiver;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修改用户绑定手机
 */
public class EditPhoneActivity extends BaseActivity {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final int SEND_PHONE_CODE_TIME = 1001;
    static int time = Constants.SMS_COUNT_TIME;
    String phone;
    String oldPhone = "";
    String newPhone;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    @ViewInject(R.id.et_phone)
    private EditText etPhone;
    @ViewInject(R.id.et_code)
    private EditText etCode;
    @ViewInject(R.id.bt_send_code)
    private Button btSendCode;
    @ViewInject(R.id.bt)
    private Button bt;
    @ViewInject(R.id.tv_back)
    private TextView tvBack;
    @ViewInject(R.id.tv_tip)
    private TextView tvTip;
    private Timer timer;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_PHONE_CODE_TIME:
                    time = time - 1;
                    if (time <= 0) {
                        timer.cancel();
                        timer = null;
                        btSendCode.setEnabled(true);
                        btSendCode.setBackgroundResource(R.drawable.bg_red_loop_lien);
                        btSendCode.setTextColor(getResources().getColor(R.color.color_red));
                        time = Constants.SMS_COUNT_TIME;
                        btSendCode.setText("点击发送验证码");

                    } else {
                        btSendCode.setText("(" + time + "s)重新发送");
                        btSendCode.setBackgroundResource(R.drawable.btn_sms_gray_line_white_bg_phone_login);
                        btSendCode.setTextColor(getResources().getColor(R.color.color_red));
                    }

                    break;

            }
        }
    };
    private AccountNetRequest netRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        ViewUtils.inject(this);
        netRequest = new AccountNetRequest(mContext);

        tvBack.setText("原手机号验证");
        phone = sp.getString("phone","0");
        etPhone.addTextChangedListener(new watcher(etPhone));
        etCode.addTextChangedListener(new watcher(etCode));

        if (time != Constants.SMS_COUNT_TIME) {
            startTimer();
        }
        if (phone.length() >= 0) {
            etPhone.setText(phone.substring(0, 3) + "****" + phone.substring(7));
            tvTip.setText(Html.fromHtml("原手机号(尾号为" + phone.substring(7) + ")无法接收短信？请联系客服 <font color=\"#f75a53\">400-152-5159</font> 申请人工修改"));
        }
        etPhone.setEnabled(false);


    }

    /**
     * 拨打 4000910365
     * @param view
     */
    public void serviceTel(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4001525159"));
        startActivity(intent);
    }

    void startTimer(){
        if (timer == null) {
            timer = new Timer(true);
        }
        timer.schedule(new TimerTask(){
            public void run() {
                Message msg=handler.obtainMessage(SEND_PHONE_CODE_TIME);
                handler.sendMessage(msg);
            }
        },1000, 1000);
    }

    @Override
    protected void onDestroy() {
        if (mSMSBroadcastReceiver != null) {
            unregisterReceiver(mSMSBroadcastReceiver);
        }

        if (timer!=null) {
            timer.cancel();
            timer.purge();
        }
        time = Constants.SMS_COUNT_TIME;
        btSendCode.setEnabled(true);
        super.onDestroy();
    }

    /**
     * 点击发送验证码
     * @param view
     */
    public void sendPhoneCode(View view) {
        // 获取验证码
        if (CommonUtils.isPhoneNumer(bt.getText().toString().equals("验 证")?phone:etPhone.getText().toString())) {

            btSendCode.setEnabled(false);
            startMyDialog();
            netRequest.getSmsVerifyCodeNew(bt.getText().toString().equals("验 证") ? phone : etPhone.getText().toString(), bt.getText().toString().equals("验 证") ? 2 : 0, "",BaseEntity.class, new RequestCallBack<BaseEntity>() {
                @Override
                public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                    stopMyDialog();

                    if (CommonUtils.NetworkRequestReturnCode(mContext,responseInfo.result.code + "")){
                        //生成广播处理
                        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
                        //实例化过滤器并设置要过滤的广播
                        IntentFilter intentFilter = new IntentFilter(ACTION);
                        intentFilter.setPriority(Integer.MAX_VALUE);
                        //注册广播
                        EditPhoneActivity.this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

                        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                            @Override
                            public void onReceived(String message) {
                                String regEx = "[^0-9]";
                                Pattern p = Pattern.compile(regEx);
                                Matcher m = p.matcher(message);

                                etCode.setText(m.replaceAll("").trim());

                            }
                        });

                        showToast(R.string.send_success);
                        startTimer();

                    } else {
                        btSendCode.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                    btSendCode.setEnabled(true);
                }
            });
        } else {
            showToast(R.string.phone_number_not_legal);
        }

    }

    /**
     * 验证手机号
     * @param view
     */
    public void retrievePassword(final View view) {
        bt.setClickable(false);

        startMyDialog();

        int step = 1;


        if (bt.getText().toString().equals("验 证")) {
            step = 1;
            oldPhone = phone;
            newPhone = "0";
        } else {
            step = 2;
            newPhone = etPhone.getText().toString();
        }


        netRequest.changePhone(step, oldPhone, newPhone, etCode.getText().toString(), new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                bt.setClickable(true);
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        if (bt.getText().toString().equals("验 证")) {
                            etPhone.setText("");
                            etCode.setText("");
                            etPhone.setHint("请输入新手机号");
                            tvBack.setText("绑定新手机");
                            bt.setText("绑 定");
                            etPhone.setEnabled(true);
                            etPhone.requestFocus();
                            time = 1;
                            tvTip.setVisibility(View.GONE);

                            btSendCode.setEnabled(false);
                            btSendCode.setBackgroundResource(R.drawable.btn_sms_gray_line_white_bg_phone_login);
                            btSendCode.setTextColor(0xffd9d9d9);


                            bt.setEnabled(false);
                            bt.setBackgroundResource(R.drawable.btn_login_not_phone_login);


                        } else {
                            sp.edit().putString("phone",etPhone.getText().toString()).commit();
                            showToast("修改绑定手机成功,请重新登录");
                            AppManager.getInstance().killActivity(UserInfoActivity.class);
                            AppManager.getInstance().killActivity(AccountSecurityActivity.class);
                            startActivity(new Intent(EditPhoneActivity.this, LeFenPhoneLoginActivity.class));
                            finish();
                            sp.edit().putString("token","").commit();
                            sp.edit().putBoolean("state", false).commit();
                        }

                        break;

                    case -4:
                        showToast("登录状态失效");
                        break;

                    case -5:
                        showToast("验证码验证失败，请重新输入验证码");
                        etCode.setText("");
                        break;

                    case -12:
                        showToast("该手机号已经注册");
                        break;

                    case -58:
                        showToast("您输入的手机号不是绑定手机号");
                        break;

                    case -59:
                        showToast("新手机号不能于原手机号一致");
                        break;

                    default:
                        showToast("操作失败，错误代码"+responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                bt.setClickable(true);
                startMyDialog();
                showToast("操作失败，原因：网络链接失败");
            }
        });


    }

    public class watcher implements TextWatcher {

        private EditText edit = null;

        public watcher(EditText ed) {
            this.edit = ed;
        }

        @Override
        public void afterTextChanged(Editable s) {


            if (edit == etPhone) {

                if (CommonUtils.isPhoneNumer(bt.getText().toString().equals("验 证")?phone:etPhone.getText().toString()) && time == Constants.SMS_COUNT_TIME) {
                    btSendCode.setEnabled(true);
                    btSendCode.setTextColor(getResources().getColor(R.color.color_red));
                    btSendCode.setBackgroundResource(R.drawable.bg_empty_red);

                } else {
                    btSendCode.setEnabled(false);
                    btSendCode.setBackgroundResource(R.drawable.btn_sms_gray_line_white_bg_phone_login);
                    btSendCode.setTextColor(0xffd9d9d9);
                }

                if (etPhone.getText().toString().length() == 11 &&
                        !(CommonUtils.isPhoneNumer(bt.getText().toString().equals("验 证")?phone:etPhone.getText().toString()))) {
                    showToast("手机号不合法");
                    showToast(R.string.phone_number_not_legal);
                    btSendCode.setEnabled(false);
                    btSendCode.setBackgroundResource(R.drawable.btn_sms_gray_line_white_bg_phone_login);
                    btSendCode.setTextColor(0xffd9d9d9);
                }

            } else {
                if (etCode.getText().toString().length() == 6) {
                    bt.setEnabled(true);
                    bt.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                } else {
                    bt.setEnabled(false);
                    bt.setBackgroundResource(R.drawable.btn_login_not_phone_login);
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



}
