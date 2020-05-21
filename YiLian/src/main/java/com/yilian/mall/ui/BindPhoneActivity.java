package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.LoginSuccessFlow;
import com.yilian.mall.R;
import com.yilian.mall.receiver.SMSBroadcastReceiver;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    public static final int SMSSENDSUCCESS = 1;
    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private EditText etBindPhone;
    private EditText etSmsCode;
    private Button btnGetSmsCode;
    private EditText etReferrerPhone;
    private Button btnBindWxAndPhone;
    private LinearLayout activityBindWxandPhone;
    private String openId;
    private String unionid;
    private String nickname;
    private String headimgurl;
    private boolean sendVoiceCode = false;
    private String loginType;//该次登录是否要进行账号合并
    private String type;//该次合并是微信 或 支付宝  与手机号合并

    private AlertDialog alertDialog;
    private View inflate;
    private ImageView ivImgCode;
    private Button btnDialogConfirm;
    private EditText etImgCode;
    private String inputPhone;
    private TextView tvImgCodeError;
    private ImageView ivImgCodeClose;
    private String verifyType;//未登录状态为3，登录状态为4
    private String verifyCode = "";//默认为空字符串，登录时绑定手机号，改为用户手动输入的图形验证码
    private TextView tv11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        tv11 = (TextView) findViewById(R.id.textView11);

        {//验证码弹窗
            alertDialog = new AlertDialog.Builder(this).create();
            inflate = View.inflate(this, R.layout.login_module_dialog_verification_code, null);
            alertDialog.setView(inflate);
            alertDialog.setCancelable(true);

            etImgCode = (EditText) inflate.findViewById(R.id.et_img_code);
            ivImgCode = (ImageView) inflate.findViewById(R.id.iv_img_code);
            btnDialogConfirm = (Button) inflate.findViewById(R.id.btn_dialog_confirm);
            tvImgCodeError = (TextView) inflate.findViewById(R.id.tv_img_code_error);
            ivImgCodeClose = (ImageView) inflate.findViewById(R.id.iv_close);
        }


        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        etBindPhone = (EditText) findViewById(R.id.et_bind_phone);
        etSmsCode = (EditText) findViewById(R.id.et_sms_code);
        btnGetSmsCode = (Button) findViewById(R.id.btn_get_sms_code);
        etReferrerPhone = (EditText) findViewById(R.id.et_referrer_phone);
        btnBindWxAndPhone = (Button) findViewById(R.id.btn_bind_wx_and_phone);
        activityBindWxandPhone = (LinearLayout) findViewById(R.id.activity_bind_wxand_phone);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("绑定手机号");
        ivRight2.setVisibility(View.GONE);

        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnGetSmsCode.setOnClickListener(this);
        btnBindWxAndPhone.setOnClickListener(this);


    }

    private void initData() {
        Intent intent = getIntent();
        loginType = intent.getStringExtra("loginType");
        openId = intent.getStringExtra("openId");
        unionid = intent.getStringExtra("unionid");
        nickname = intent.getStringExtra("nickname");
        headimgurl = intent.getStringExtra("headimgurl");
        type = intent.getStringExtra("type");
        if (isLogin()) {
            verifyType = "4";
        } else {
            verifyType = "3";
            tv11.setText("*请绑定已有账号的手机号，绑定后即可用微信/支付宝快捷登录乐分APP");
        }
    }

    private boolean canBind = true;

    private void initListener() {

        //图形验证码刷新
        ivImgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgCodeUtil.setImgCode(inputPhone, ivImgCode, BindPhoneActivity.this);
            }
        });
        //        关闭图形验证码界面
        ivImgCodeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAlertDialog();
            }
        });

        //        确定验证码
        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etIMGCode = etImgCode.getText().toString();
                if (TextUtils.isEmpty(etIMGCode)) {
                    showToast("请输入图片中的内容");
                    return;
                }
                verifyCode = etIMGCode;
                getSMSCode();
            }
        });

        etBindPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = etBindPhone.getText().toString().trim();
                if (phone.length() >= 11) {
                    if (!CommonUtils.isPhoneNumer(phone)) {
                        showToast(R.string.phone_number_not_legal);
                        return;
                    }
                    BindPhoneActivity.this.inputPhone = phone;
                    checkPhoneNumber(phone);//检验输入的手机号是否注册过
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.btn_get_sms_code:
                String phone = etBindPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToast("请填写手机号码");
                    break;
                }

                if (!CommonUtils.isPhoneNumer(phone)) {
                    showToast(R.string.phone_number_not_legal);
                    break;
                }
                checkPhoneNumber(phone);
                break;
            case R.id.btn_bind_wx_and_phone:
                //绑定微信和手机号码
                submit();
                break;
        }
    }

    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public void getSMSCode() {
        String phone = etBindPhone.getText().toString().trim();
        if (sendVoiceCode) {
            getSmsVoiceCode(phone);
        } else {
            getSMSCode(phone);
        }
    }

    /**
     * 获取短信验证码
     */
    private void getSMSCode(String phone) {

        if (!CommonUtils.isPhoneNumer(phone)) {
            showToast("手机号码格式错误");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitUtils.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey
                .getDeviceIndex(mContext)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken
                (mContext))
                .getSmsCode(phone, verifyType, verifyCode, null, "8", new Callback<com.yilian
                        .networkingmodule.entity.BaseEntity>() {
                    @Override
                    public void onResponse(Call<com.yilian.networkingmodule.entity.BaseEntity>
                                                   call, Response<com.yilian.networkingmodule
                            .entity.BaseEntity> response) {
                        com.yilian.networkingmodule.entity.BaseEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,
                                body)) {
                            showToast(R.string.service_exception);
                            return;
                        }

                        switch (body.code) {
                            case 0:
                                showAlertDialogAndRefreshImgCode(phone);
                                tvImgCodeError.setVisibility(View.VISIBLE);
                                tvImgCodeError.setText(R.string.login_module_img_code_error);
                                etImgCode.setText("");
                                break;
                            case 1://发送成功，开始倒计时下次发送时间
                                dismissAlertDialog();
                                handler.sendEmptyMessage(SMSSENDSUCCESS);
                                Time = Constants.SMS_COUNT_TIME;
                                //生成广播处理
                                mSMSBroadcastReceiver = new SMSBroadcastReceiver();

                                //实例化过滤器并设置要过滤的广播
                                IntentFilter intentFilter = new IntentFilter(ACTION);
                                intentFilter.setPriority(Integer.MAX_VALUE);
                                //注册广播
                                BindPhoneActivity.this.registerReceiver(mSMSBroadcastReceiver,
                                        intentFilter);

                                mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                                    @Override
                                    public void onReceived(String message) {
                                        String regEx = "[^0-9]";
                                        Pattern p = Pattern.compile(regEx);
                                        Matcher m = p.matcher(message);
                                        // System.out.println(m.replaceAll("").trim());
                                        etSmsCode.setText(m.replaceAll("").trim());
                                        //将接收到的短信写到EditText上面
                                    }
                                });
                                break;
                            case -3://发送失败，可以重新发送
                                showToast("系统繁忙，3-10秒后重试");
                                break;
                            case -1://接口调用频繁
                                showToast(R.string.sixty_time_one);
                                break;
                            case -2://接口调用次数超出限制
                                showToast("系统繁忙，3-10秒后重试");
                                break;
                            case -6://发送失败
                                showToast(R.string.sixty_time_one);
                                break;
                            case -16://账号未注册
                                break;
                            default:
                                showToast("" + body.code);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<com.yilian.networkingmodule.entity.BaseEntity>
                                                  call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                    }
                });
    }


    public int Time = Constants.SMS_COUNT_TIME;//再次获取短信倒计时
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SMSSENDSUCCESS:
                    Logger.i("TIME！：" + Time);
                    if (Time > 0) {
                        Time--;
                        btnGetSmsCode.setText("(" + Time + "s)重新发送");
                        btnGetSmsCode.setClickable(false);
                        handler.sendEmptyMessageDelayed(SMSSENDSUCCESS, 1000);
                        Logger.i("TIME:" + Time);
                    } else {
                        Logger.i("Time0:" + Time);
                        Logger.i("获取语音验证码   ");
                        switchSendVoiceCode();
                        btnGetSmsCode.setClickable(true);
                        btnGetSmsCode.setTextColor(getResources().getColor(R.color.color_red));
                        btnGetSmsCode.setText(R.string.send_verification_code);

                    }
                    break;
            }
        }
    };


    //判断当前验证码输入框是否有内容，没有提示发送语音验证码
    private void switchSendVoiceCode() {
        String phone = etBindPhone.getText().toString();
        Logger.i("走了这里" + phone);
        if (TextUtils.isEmpty(phone) && !CommonUtils.isPhoneNumer(phone)) {
            showToast("手机号码格式错误");
            return;
        }
        if (!TextUtils.isEmpty(etSmsCode.getText().toString().trim())) {
            return;
        }
        Logger.i("走了这里2");
        Logger.i("mSMSBroadcastReceiver  " + mSMSBroadcastReceiver);
        String str = "短信验证码没有收到？发送语音验证码试试吧！";
        showDialog(null, str, null, 0, Gravity.CENTER, "是", "否", true, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //发送语音验证码
                        dialog.dismiss();
                        sendVoiceCode = true;
                        if (isLogin()) {//如果是登录状态，直接请求语音验证码
                            getSMSCode();
                        } else {//未登录状态，弹出图形验证码，输入完图形验证码点击确定后，再请求语音验证码
                            showAlertDialogAndRefreshImgCode(phone);
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        sendVoiceCode = false;
                        dialog.dismiss();
                        break;
                }
            }
        }, mContext);
    }

    /**
     * 获取语音验证码
     *
     * @param phone
     */
    public void getSmsVoiceCode(String phone) {
        if (!CommonUtils.isPhoneNumer(phone)) {
            showToast("手机号码格式错误");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitUtils.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey
                .getDeviceIndex(mContext)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken
                (mContext))
                .getSmsCode(phone, verifyType, verifyCode, "1", "8", new Callback<com.yilian
                        .networkingmodule.entity.BaseEntity>() {
                    @Override
                    public void onResponse(Call<com.yilian.networkingmodule.entity.BaseEntity>
                                                   call, Response<com.yilian.networkingmodule
                            .entity.BaseEntity> response) {
                        com.yilian.networkingmodule.entity.BaseEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,
                                body)) {
                            showToast(R.string.service_exception);
                            return;
                        }

                        switch (body.code) {
                            case 0:
                                showAlertDialogAndRefreshImgCode(phone);
                                tvImgCodeError.setVisibility(View.VISIBLE);
                                tvImgCodeError.setText(R.string.login_module_img_code_error);
                                etImgCode.setText("");
                                break;
                            case 1:
                                dismissAlertDialog();
                                sendVoiceCode = !sendVoiceCode;
                                Time = Constants.SMS_COUNT_TIME;
                                handler.sendEmptyMessage(SMSSENDSUCCESS);
                                btnGetSmsCode.setText("(" + Time + "s)重新发送");
                                break;
                            case -1:
                                showToast(R.string.sixty_time_one);
                                btnGetSmsCode.setEnabled(false);
                                break;
                            case -6:
                                showToast(R.string.sixty_time_one);
                                btnGetSmsCode.setEnabled(true);
                                break;
                            case -3:
                                showToast("系统繁忙，3-10秒后重试");
                                btnGetSmsCode.setEnabled(true);
                                break;
                            default:
                                showToast("" + body.code);
                                break;

                        }
                    }

                    @Override
                    public void onFailure(Call<com.yilian.networkingmodule.entity.BaseEntity>
                                                  call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                    }
                });

    }


    private void submit() {
        if (!canBind) {
            showToast("无效推荐人号码,请重新填写!");
            return;
        }
        String phone = etBindPhone.getText().toString().trim();
        // validate
        if (!CommonUtils.isPhoneNumer(phone)) {
            showToast("手机号码格式错误");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = etSmsCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String referrerPhone = etReferrerPhone.getText().toString().trim();//推荐人手机号
        if (!TextUtils.isEmpty(referrerPhone)) {//推荐人手机号不为空时才检验号码格式
            if (!CommonUtils.isPhoneNumer(referrerPhone)) {//不符合手机号码格式
                showToast("推荐人号码格式错误,请重新填写!");
                return;
            }
        }
        // TODO validate success, do something
        //第三方登录时，与已经存在的手机号进行账号合并 并 登录
        if (!TextUtils.isEmpty(loginType) && loginType.equals("mergeAccount")) {
            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex
                    (mContext)).
                    loginByThirdParyt(unionid, type, "1", nickname, headimgurl, openId, "1",
                            phone, code, new Callback<LoginEntity>() {
                        @Override
                        public void onResponse(Call<LoginEntity> call, Response<LoginEntity>
                                response) {
                            LoginEntity loginEntity = response.body();
                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,
                                    response.body())) {
                                Toast.makeText(mContext, R.string.login_module_service_exception,
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            Logger.i("loginEntity.toString :  " + loginEntity.toString());
                            if (CommonUtils.serivceReturnCode(mContext, loginEntity.code, "",
                                    LeFenPhoneLoginActivity.class)) {
                                switch (loginEntity.code) {

                                    case 1:
                                        LoginSuccessFlow.getInstance().loginsuccessFlow
                                                (loginEntity, mContext, "");
                                        break;
                                    case -5:
                                        break;
                                    default:
                                        break;
                                }
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Call<LoginEntity> call, Throwable t) {
                            Toast.makeText(BindPhoneActivity.this, R.string
                                    .net_work_not_available, Toast.LENGTH_SHORT).show();
                        }
                    });
            return;
        }
        //        已登录的第三方账号，和手机号进行绑定
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext))
                .setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .bindThirdPartyAccount(phone, "0", "", "", "", code, referrerPhone, new
                        Callback<com.yilian.networkingmodule.entity.BaseEntity>() {
                    @Override
                    public void onResponse(Call<com.yilian.networkingmodule.entity.BaseEntity>
                                                   call, Response<com.yilian.networkingmodule
                            .entity.BaseEntity> response) {
                        com.yilian.networkingmodule.entity.BaseEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,
                                response.body())) {
                            showToast(getString(R.string.login_module_service_exception));
                            return;
                        }
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg,
                                LeFenPhoneLoginActivity.class)) {
                            switch (body.code) {
                                case 1:
                                    showToast("绑定成功");
                                    PreferenceUtils.writeStrConfig(Constants.SPKEY_PHONE, phone,
                                            mContext);
                                    finish();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.yilian.networkingmodule.entity.BaseEntity>
                                                  call, Throwable t) {

                    }
                });

    }


    //验证手机号是否有注册过
    private void checkPhoneNumber(String phone) {
        BindPhoneActivity.this.inputPhone = phone;
        RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<com.yilian
                .networkingmodule.entity.CheckUserEntity>() {
            @Override
            public void onResponse(Call<com.yilian.networkingmodule.entity.CheckUserEntity> call,
                                   Response<com.yilian.networkingmodule.entity.CheckUserEntity>
                                           response) {
                com.yilian.networkingmodule.entity.BaseEntity body = response.body();
                if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    showToast(getString(R.string.service_exception));
                    return;
                }
                switch (body.code) {
                    case 1://未注册的手机号，直接绑定
                        if (isLogin()) {//如果是登录状态，则是已登录账号绑定手机号，如果该手机号是新账号，则直接绑定，如果是老账号，则提醒账号进行合并
                            getSMSCode(phone);
                        } else {//如果是未登录状态，则是正在登录账号与手机号账号进行合并，如果是新账号，则无法合并，如果是老账号则提醒合并
                            showToast("亲，该手机号还不是乐分会员，请选择直接登录");
                        }
                        break;
                    case -12://已存在的手机号，提示用户是否进行账号合并
                        if (isLogin()) { //如果是登录状态，则是“已登录”账号绑定已存在手机号，弹窗提示用户是否合并
                            new VersionDialog.Builder(mContext)
                                    .setMessage("该手机号已是乐分会员，是否合并？")
                                    .setPositiveButton("合并", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            getSMSCode(phone);
                                        }
                                    })
                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        } else {//如果是未登录状态，则是“正在登录”账号与已存在手机号进行绑定，弹出图形验证码界面
                            showAlertDialogAndRefreshImgCode(phone);
                        }

                        break;
                }
            }

            @Override
            public void onFailure(Call<com.yilian.networkingmodule.entity.CheckUserEntity> call,
                                  Throwable t) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销短信监听广播
        handler.removeCallbacksAndMessages(null);
        if (mSMSBroadcastReceiver != null) {
            this.unregisterReceiver(mSMSBroadcastReceiver);
        }
    }

    /**
     * 显示图形验证码
     *
     * @param phoneNumber
     */
    private void showAlertDialogAndRefreshImgCode(String phoneNumber) {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
        if (alertDialog.isShowing()) {
            ImgCodeUtil.setImgCode(phoneNumber, ivImgCode, this);
        }
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
}
