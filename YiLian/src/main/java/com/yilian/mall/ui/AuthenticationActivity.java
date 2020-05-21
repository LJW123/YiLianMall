package com.yilian.mall.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.receiver.SMSBroadcastReceiver;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设置支付密码时的身份验证
 */
public class AuthenticationActivity extends BaseActivity {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final int SEND_PHONE_CODE_TIME = 1001;
    static int time = Constants.SMS_COUNT_TIME;
    @ViewInject(R.id.tv_phone_text)
    TextView tvPhoneText;
    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.etphonecode)
    private EditText etphonecode;
    @ViewInject(R.id.bt_sendphonecode)
    private Button bt_sendphonecode;
    private Timer timer;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_PHONE_CODE_TIME:
                    time = time - 1;
                    if (time <= 0) {
                        timer.cancel();
                        bt_sendphonecode.setEnabled(true);
                        bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                        bt_sendphonecode.setTextColor(0xffffffff);
                        time = Constants.SMS_COUNT_TIME;
                        bt_sendphonecode.setText("发送验证码");

                    } else {
                        bt_sendphonecode.setText("(" + time + "s)重新发送");
                    }

                    break;

            }
        }
    };
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private AccountNetRequest accountNetRequest;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        ViewUtils.inject(this);
        tv_back.setText("身份验证");
        accountNetRequest = new AccountNetRequest(mContext);
        phone = sp.getString("phone", "0");
        if (!TextUtils.isEmpty(phone)) {
            String newphone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
            tvPhoneText.setText("为账户安全，需验证手机号：" +newphone);
        }

    }

    public void Next(View view) {
        if (etphonecode.getText().toString().length() != 6) {
            showToast("请输入六位验证码");
            return;
        }
        startMyDialog();

        accountNetRequest.checkSmsVerifyCode(phone, etphonecode.getText().toString(), BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code + "")) {
                    stopMyDialog();
                    Intent intent = new Intent(AuthenticationActivity.this, PhonePayPasswordActivity.class);
                    intent.putExtra("titleName", "手机支付密码");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("请输入支付密码");
                stopMyDialog();
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param view
     */
    public void sendPhoneCode(final View view) {
        startMyDialog();

        accountNetRequest.getSmsVerifyCodeNew(phone, 2, "", BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                Logger.i("修改密码界面" + responseInfo.result.code);
                switch (responseInfo.result.code) {
                    case 1:
                        view.setEnabled(false);
                        ((TextView) view).setTextColor(0xffb3b3b3);
                        ((TextView) view).setBackgroundResource(R.mipmap.bgtwo);
                        showToast("发送成功");
                        timer = new Timer(true);
                        timer.schedule(new TimerTask() {
                            public void run() {
                                Message msg = handler.obtainMessage(SEND_PHONE_CODE_TIME);
                                handler.sendMessage(msg);
                            }
                        }, 1000, 1000);
                        break;
                    case -1:
                        showToast(R.string.sixty_time_one);
                        view.setEnabled(true);
                        view.setBackgroundResource(R.mipmap.button_three);
                        bt_sendphonecode.setTextColor(0xffffffff);
                        bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                        break;
                    case -2:
                        showToast("系统限制");
                        view.setEnabled(true);
                        view.setBackgroundResource(R.mipmap.button_three);
                        bt_sendphonecode.setTextColor(0xffffffff);
                        bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                        break;
                    case -6:
                    case -3:
                        showToast("发送失败，请重新发送");
                        view.setEnabled(true);
                        view.setBackgroundResource(R.mipmap.button_three);
                        bt_sendphonecode.setTextColor(0xffffffff);
                        bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                        break;
                    default:
                        showToast("异常");
                        view.setEnabled(true);
                        view.setBackgroundResource(R.mipmap.button_three);
                        bt_sendphonecode.setTextColor(0xffffffff);
                        bt_sendphonecode.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
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

    @Override
    protected void onStart() {
        super.onStart();
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {

                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(message);

                etphonecode.setText(m.replaceAll("").trim());

                Next(etphonecode);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

}
