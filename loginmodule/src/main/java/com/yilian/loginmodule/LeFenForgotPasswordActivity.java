package com.yilian.loginmodule;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2016/11/1 0001.
 */

public class LeFenForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final int SEND_PHONE_CODE_TIME = 1001;
    public static final int CHECK_PHONE_REGISTER_FINISH = 1002;
    private int time = Constants.SMS_COUNT_TIME;
    private ImageView back;
    private ImageView cancel;
    private TextView textView3;
    private TextView select_phone;
    private EditText etPhone;
    private TextView select_code;
    private EditText etSmsCode;
    private Button obtinaCode;
    private Timer timer;
    private Context mContext;
    private String phoneNumber;
    private int isFristHsaCode = 0;
    private boolean isGetVoiceCode = false;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_PHONE_CODE_TIME:
                    time--;
                    if (time <= 0) {
                        timer.cancel();
                        timer = null;
                        sendVoriceCode();
                        obtinaCode.setEnabled(true);
                        obtinaCode.setBackgroundColor(getResources().getColor(R.color.login_module_color_forget_pwd_bg));
                        time = Constants.SMS_COUNT_TIME;
                        obtinaCode.setText(R.string.login_module_send_verification_code);
                        etPhone.setEnabled(true);

                    } else {
                        obtinaCode.setText("(" + time + "s)重新发送");
                        obtinaCode.setEnabled(false);
                    }
                    break;
                case CHECK_PHONE_REGISTER_FINISH:
//                    检查完手机号是否注册过后，根据结果处理图形验证码弹窗是否显示
                    Boolean obj = (Boolean) msg.obj;
                    if (obj) {//注册过
                        showAlertDialogAndRefreshImgCode(phone);
                        etSmsCode.requestFocus();//设置光标
                    } else {//没有注册过
                        showToast(getString(R.string.login_module_phone_not_register));
                    }
                    break;

            }
        }
    };
    private Button btnNext;

    //获取语音验证码
    private void sendVoriceCode() {

        if (isFristHsaCode >= 1 && time <= 0 && TextUtils.isEmpty(etSmsCode.getText().toString())) {
            /**
             * 显示提示框
             * @param title 标题
             * @param msg 内容
             * @param detailMsg 详细内容
             * @param iconId 内容 图标 没有就传0
             * @param gravity 图标和内容位置
             * @param positiveText 确定字样
             * @param negativetext 取消字样
             * @param cancelable 点击屏幕意外或者返回键是否消失
             * @param listener 按键监听
             */
            String str = "短信验证码没有收到？发送语音验证码试试吧！";
            showDialog(null, str, null, 0, Gravity.CENTER, "是", "否", true, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //发送语音验证码
                            dialog.dismiss();
                            isGetVoiceCode = true;
                            showAlertDialogAndRefreshImgCode(phone);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            isGetVoiceCode = false;
                            dialog.dismiss();
                            break;
                    }
                }
            }, mContext);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_activity_forgot_password);
        mContext = this;

        initView();
        initListener();
    }

    private AlertDialog alertDialog;
    private View inflate;
    private ImageView ivImgCode;
    private Button btnDialogConfirm;
    private EditText etImgCode;
    private String etIMGCode;//用户手动输入的图形验证码
    private String phone;
    private TextView tvImgCodeError;
    private ImageView ivImgCodeClose;

    private void initView() {
        boolean is_from2login = getIntent().getBooleanExtra("is_from2login", false);
        back = (ImageView) findViewById(R.id.back);
        if (is_from2login) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.GONE);
        }
        cancel = (ImageView) findViewById(R.id.cancel);
        if (is_from2login) {
            cancel.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
        }
        LinearLayout ll_content = (LinearLayout) findViewById(R.id.ll_content);
        textView3 = (TextView) findViewById(R.id.textView3);
        select_phone = (TextView) findViewById(R.id.select_phone);
        etPhone = (EditText) findViewById(R.id.et_register_phone);
        String savePhone= sp.getString(Constants.SPKEY_PHONE, "");
        if (!TextUtils.isEmpty(savePhone)){
            etPhone.setText(savePhone);
        }
        etPhone.addTextChangedListener(new watcher(etPhone, 11));
        select_phone.setVisibility(View.VISIBLE);
        select_code = (TextView) findViewById(R.id.select_code);
        etSmsCode = (EditText) findViewById(R.id.et_register_code);
        etSmsCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        etSmsCode.addTextChangedListener(new watcher(etSmsCode, 6));
        obtinaCode = (Button) findViewById(R.id.tv_obtina_code);
        if(TextUtils.isEmpty(etPhone.getText().toString().trim())){
            obtinaCode.setTextColor(getResources().getColor(R.color.login_module_forget_pwd_code));
        }
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.button3);
        ll_content.setBackgroundColor(getResources().getColor(R.color.login_module_color_forget_pwd_bg));
        textView3.setText("找回密码");
        btnNext.setTextColor(getResources().getColor(R.color.login_module_color_forget_pwd_bg));
        obtinaCode.setBackgroundColor(getResources().getColor(R.color.login_module_color_forget_pwd_bg));
        etSmsCode.setHintTextColor(getResources().getColor(R.color.login_module_forget_pwd_code));

        alertDialog = new AlertDialog.Builder(LeFenForgotPasswordActivity.this).create();
        inflate = View.inflate(LeFenForgotPasswordActivity.this, R.layout.login_module_dialog_verification_code, null);
        alertDialog.setView(inflate);
        alertDialog.setCancelable(true);
        etImgCode = (EditText) inflate.findViewById(R.id.et_img_code);
        ivImgCode = (ImageView) inflate.findViewById(R.id.iv_img_code);
        btnDialogConfirm = (Button) inflate.findViewById(R.id.btn_dialog_confirm);
        tvImgCodeError = (TextView) inflate.findViewById(R.id.tv_img_code_error);
        ivImgCodeClose = (ImageView) inflate.findViewById(R.id.iv_close);
    }

    private void initListener() {
        //验证码刷新
        ivImgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgCodeUtil.setImgCode(phone, ivImgCode, LeFenForgotPasswordActivity.this);
            }
        });
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
                etIMGCode = etImgCode.getText().toString();
                if (TextUtils.isEmpty(etIMGCode)) {
                    showToast("请输入图片中的内容");
                    return;
                }
                getSmsCode();
            }
        });

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    etPhone.setFocusableInTouchMode(true);
                    etPhone.requestFocus();//设置光标
                    select_phone.setVisibility(View.VISIBLE);
                    etPhone.setTextSize(15);
                    etPhone.setHintTextColor(getResources().getColor(R.color.login_module_white));
                    etPhone.setTextColor(getResources().getColor(R.color.login_module_white));
                } else {

                    etPhone.setTextColor(getResources().getColor(R.color.login_module_white));
                    etPhone.setHintTextColor(getResources().getColor(R.color.login_module_forget_pwd_code));
                    select_phone.setVisibility(View.INVISIBLE);
                    etPhone.setTextSize(15);

                }
            }
        });

        etSmsCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    etSmsCode.requestFocus();
                    etSmsCode.setFocusableInTouchMode(true);
                    select_code.setVisibility(View.VISIBLE);
                    obtinaCode.setTextColor(getResources().getColor(R.color.login_module_white));
                    etSmsCode.setTextSize(15);
                    etSmsCode.setTextColor(getResources().getColor(R.color.login_module_white));
                    etSmsCode.setHintTextColor(getResources().getColor(R.color.login_module_white));
                } else {
                    etSmsCode.setTextColor(getResources().getColor(R.color.login_module_white));
                    etSmsCode.setHintTextColor(getResources().getColor(R.color.login_module_forget_pwd_code));
                    select_code.setVisibility(View.INVISIBLE);
                    obtinaCode.setTextColor(getResources().getColor(R.color.login_module_forget_pwd_code));
                    select_code.setTextSize(15);
                }
            }
        });

    }


    //倒计时
    private void starTimer() {
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

    //获取手机号验证码
    public void getCode(View view) {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("请填写手机号码");
            return;
        }
        checkPhoneNumber(phone);
    }

    private void getSmsCode() {
        if (!CommonUtils.isPhoneNumer(etPhone.getText().toString())) {
            showToast(R.string.login_module_send_phone_number_not_legal);
        } else if (CommonUtils.isPhoneNumer(etPhone.getText().toString())) {
            phoneNumber = etPhone.getText().toString();
            getSmsCode(phoneNumber);
        }

    }

    private void getSmsCode(final String phoneNumber) {
        String voice;
        if (isGetVoiceCode) {
            voice = "1";
        } else {
            voice = null;
        }
        obtinaCode.setEnabled(true);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSmsCode(phoneNumber, "3", etIMGCode, voice, "4", new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (0 == body.code) {//此处的0代表图形验证码错误，需要独立逻辑处理，所以不能使用通用的code判断
                                showAlertDialogAndRefreshImgCode(phoneNumber);
                                tvImgCodeError.setVisibility(View.VISIBLE);
                                tvImgCodeError.setText(R.string.login_module_img_code_error);
                                etImgCode.setText("");
                                return;
                            }
                            if(-1==body.code){
                                showToast(R.string.login_module_sms_space);
                                return;
                            }
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                                switch (body.code) {
                                    case 1:
                                        dismissAlertDialog();
                                        isGetVoiceCode = !isGetVoiceCode;
                                        starTimer();
                                        obtinaCode.setText("(" + time + "s)重新发送");
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        showToast(R.string.login_module_net_work_not_available);
                    }
                });

    }

    private void showAlertDialogAndRefreshImgCode(String phoneNumber) {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
        if (alertDialog.isShowing()) {
            ImgCodeUtil.setImgCode(phoneNumber, ivImgCode, LeFenForgotPasswordActivity.this);
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

    public void next(View view) {
        if (!CommonUtils.isPhoneNumer(etPhone.getText().toString()) || TextUtils.isEmpty(etSmsCode.getText().toString())) {
            showToast("请填写正确的信息");
            return;
        }
        btnNext.setEnabled(false);
        final String smsCode = etSmsCode.getText().toString();
        if (TextUtils.isEmpty(smsCode)) {
            showToast("验证码不能为空");
            return;
        }
        //跳转下一页之前先去验证证码是否正确,如果是注册界面验证成功需要多加一个type=1;，忘记密码不要加type=1;
            RetrofitUtils.getInstance(mContext).checkSmsCode(phoneNumber, smsCode, new Callback<BaseEntity>() {
                @Override
                public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                    BaseEntity body = response.body();
                    if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                        showToast(R.string.login_module_service_exception);
                        return;
                    }
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                        switch (body.code) {
                            case 1:
                                btnNext.setEnabled(true);
                                Intent intent = new Intent(mContext, LeFenResetPasswordActivity.class);
                                intent.putExtra("phone", phoneNumber);
                                intent.putExtra("SMS_code", smsCode);
                                startActivity(intent);
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.cancel) {
            finish();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        time = Constants.SMS_COUNT_TIME;
        obtinaCode.setEnabled(true);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    boolean isRegistered;

    //验证手机号是否有注册过
    private void checkPhoneNumber(String phone) {

        RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
            @Override
            public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                BaseEntity body = response.body();
                if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                    showToast(R.string.login_module_service_exception);
                }
                if (-12 == body.code) {//手机号注册过，需要独立处理，不能使用通用的验证码验证逻辑
                    isRegistered = true;
                }else{
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                        switch (body.code) {
                            case 1://手机号没有注册过
                                isRegistered = false;
                                break;

                        }
                    }
                }

                Message message = Message.obtain();
                message.obj = isRegistered;
                message.what = CHECK_PHONE_REGISTER_FINISH;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                showToast(R.string.login_module_net_work_not_available);
            }
        });
    }

    class watcher implements TextWatcher {

        private EditText edit;
        private int maxlen;

        public watcher(EditText et_register_phone, int len) {
            this.edit = et_register_phone;
            this.maxlen = len;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            etIMGCode = "";//每次改变手机号码，验证码都要重置
            if (edit == etPhone) {
                String phone = edit.getText().toString();
                int len = phone.length();
                if (len >= maxlen) {

                    etSmsCode.requestFocus();//设置光标
                    if (!CommonUtils.isPhoneNumer(phone)) {
                        showToast(getString(R.string.login_module_send_phone_number_not_legal));
                        return;
                    }
                    LeFenForgotPasswordActivity.this.phone = phone;
                    checkPhoneNumber(phone);
                }
            } else if (edit == etSmsCode) {
                int len = edit.getText().length();
                if (len > maxlen) {
                    showToast("请输入正确的验证码");
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
