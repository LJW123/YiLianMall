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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.entity.MemberRelationSystemEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.yilian.mylibrary.Constants.SMS_COUNT_TIME;


/**
 * Created by liuyuqi on 2016/11/2 0002.
 * 验证码快捷登录
 */

public class LeFenSmsCodeLoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int SEND_PHONE_CODE_TIME = 1001;
    private int time = SMS_COUNT_TIME;
    private ImageView back;
    private ImageView cancel;
    private TextView textView3;
    private TextView select_phone;
    private EditText et_register_phone;
    private TextView select_code;
    private EditText et_register_code;
    private Button btnComplete;
    private Button btn_code;
    private RadioButton sort_cut;
    private String phoneNumber;
    private Timer timer;
    private int isFristGetCode;
    private boolean isQuickLogin = false;
    private boolean isGetVoiceCode = false;
    private Context context;
    ;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_PHONE_CODE_TIME:
                    time--;
                    if (time <= 0) {
                        et_register_phone.setEnabled(true);
                        sendVriceCode();
                        timer.cancel();
                        timer = null;
                        btn_code.setEnabled(true);
                        btn_code.setBackgroundColor(getResources().getColor(R.color.login_module_color_login_bg));
                        btn_code.setTextColor(getResources().getColor(R.color.login_module_white));
                        time = Constants.SMS_COUNT_TIME;
                        btn_code.setText(R.string.login_module_send_verification_code);

                    } else {
                        btn_code.setText("(" + time + "s)重新发送");
                        //通过30秒后验证码输入框是否有内容来判断验证是否发送成功，如果没有内容重新发送验证码
                        btn_code.setEnabled(false);
                        et_register_phone.setEnabled(false);
                    }

                    break;
            }
        }
    };
    private RadioGroup rgLoginType;
    private TextView tvAccountLogin;
    private EditText etReferrerPhone;
    private RelativeLayout rlReferrerPhone;
    private AlertDialog alertDialog;
    private ImageView ivImgCode;
    private Button btnDialogConfirm;
    private EditText etImgCode;
    private String etIMGCode;//用户手动输入的图形验证码
    private String phone;
    private TextView tvImgCodeError;
    private ImageView ivImgCodeClose;
    private boolean isSavePhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_activity_lefen_smscode_login);
        context = this;
        initView();
        initListener();
        //进来的时候直接去获取上一次存储的手机号
        phone = sp.getString(Constants.SPKEY_PHONE, "");
        Logger.i("获取保存的电话号码  " + phone);
        //初始化完成时去赋值
        if (!TextUtils.isEmpty(phone)) {
            isSavePhone = true;
            et_register_phone.setText(phone);
        }
    }


    private void sendVriceCode() {
        if (isQuickLogin && isFristGetCode >= 1 && time <= 0 && TextUtils.isEmpty(et_register_code.getText().toString())) {
            String str = "短信验证码没有收到？发送语音验证码试试吧！";
            showDialog(null, str, null, 0, Gravity.CENTER, "是", "否", true, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case BUTTON_POSITIVE:
                            //发送语音验证码
                            isGetVoiceCode = true;
                            dialog.dismiss();

//                            getCode(btn_code);
                            showAlertDialogAndRefreshImgCode(phone);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            isGetVoiceCode = false;
                            dialog.dismiss();
                            break;
                    }
                }
            }, context);
        }
    }


    private void initView() {
        TextView tvSelectReferrer = (TextView) findViewById(R.id.select_referrer);
        rlReferrerPhone = (RelativeLayout) findViewById(R.id.rl_referrer_phone);
        etReferrerPhone = (EditText) findViewById(R.id.et_referrer_phone);
        tvAccountLogin = (TextView) findViewById(R.id.tv_account_login);
        tvAccountLogin.setOnClickListener(this);
        rgLoginType = (RadioGroup) findViewById(R.id.rg_login_type);
        back = (ImageView) findViewById(R.id.back);
        cancel = (ImageView) findViewById(R.id.cancel);
        textView3 = (TextView) findViewById(R.id.textView3);
        sort_cut = (RadioButton) findViewById(R.id.sortcut);
        select_phone = (TextView) findViewById(R.id.select_phone);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_register_phone.addTextChangedListener(new watcher(et_register_phone, 11));
        select_code = (TextView) findViewById(R.id.select_code);
        et_register_code = (EditText) findViewById(R.id.et_register_code);
        et_register_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        btn_code = (Button) findViewById(R.id.tv_obtina_code);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        isFristGetCode = 0;

        rgLoginType.check(R.id.sortcut);//账号登录在该页面取消，但是界面内容只是隐藏了，这里默认选中快捷登录
        shortCut(rgLoginType);
        alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle("填写图片中的验证码");
//        alertDialog
        View inflate = View.inflate(this, R.layout.login_module_dialog_verification_code, null);
        alertDialog.setView(inflate);
        alertDialog.setCancelable(true);
        etImgCode = (EditText) inflate.findViewById(R.id.et_img_code);
        ivImgCode = (ImageView) inflate.findViewById(R.id.iv_img_code);
        btnDialogConfirm = (Button) inflate.findViewById(R.id.btn_dialog_confirm);
        tvImgCodeError = (TextView) inflate.findViewById(R.id.tv_img_code_error);
        ivImgCodeClose = (ImageView) inflate.findViewById(R.id.iv_close);

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
                etIMGCode = etImgCode.getText().toString();
                if (TextUtils.isEmpty(etIMGCode)) {
                    showToast("请输入图片中的内容");
                    return;
                }
                getSMSCode();


            }
        });
        ivImgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgCodeUtil.setImgCode(LeFenSmsCodeLoginActivity.this.phone, ivImgCode, LeFenSmsCodeLoginActivity.this);
            }
        });
        etReferrerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    final String referrerPhone = etReferrerPhone.getText().toString().trim();
                    RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                            .getMemberRelationSystem(referrerPhone, new Callback<MemberRelationSystemEntity>() {
                                @Override
                                public void onResponse(Call<MemberRelationSystemEntity> call, Response<MemberRelationSystemEntity> response) {
                                    MemberRelationSystemEntity body = response.body();
                                    if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                                        showToast(getString(R.string.login_module_service_exception));
                                        return;
                                    }
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                                        switch (body.code) {
                                            case 1://查询推荐人信息成功，弹窗显示
//                                    获取推荐人信息
                                                String merchantName = body.merchantName;
                                                String filialeName = body.filialeName;
                                                String userName = body.userName;
                                                TextView textView = new TextView(context);
                                                textView.setText("您的推荐人");
                                                textView.setPadding(20, 20, 20, 20);
                                                textView.setGravity(Gravity.CENTER);
                                                LinearLayout layout = new LinearLayout(context);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                layout.setGravity(Gravity.CENTER);
                                                TextView tv = new TextView(context);
                                                tv.setText((TextUtils.isEmpty(merchantName) ? TextUtils.isEmpty(filialeName) ? TextUtils.isEmpty(userName) ? "暂无昵称" : userName : userName : merchantName));
                                                tv.setPadding(20, 20, 20, 20);
                                                TextView tv1 = new TextView(context);
                                                tv1.setText("(" + referrerPhone + ")");
                                                tv1.setPadding(20, 0, 20, 20);
                                                LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                layout.addView(tv, pm);
                                                layout.addView(tv1, pm);
                                                new AlertDialog.Builder(context)
                                                        .setCustomTitle(textView)
                                                        .setView(layout)
                                                        .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .create().show();
                                                break;
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MemberRelationSystemEntity> call, Throwable t) {
                                    showToast(R.string.login_module_net_work_not_available);
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_register_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    et_register_phone.setFocusableInTouchMode(true);
                    et_register_phone.requestFocus();//设置光标
                    select_phone.setVisibility(View.VISIBLE);
                    et_register_phone.setTextSize(15);
                    et_register_phone.setHintTextColor(getResources().getColor(R.color.login_module_white));
                    et_register_phone.setTextColor(getResources().getColor(R.color.login_module_white));
                } else {
                    select_phone.setVisibility(View.INVISIBLE);
                    et_register_phone.setTextSize(15);
//                    et_register_phone.setTextColor(getResources().getColor(R.color.login_module_color_shorcut_login));
                    et_register_phone.setTextColor(getResources().getColor(R.color.login_module_white));
                    et_register_phone.setHintTextColor(getResources().getColor(R.color.login_module_color_shorcut_login));

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
                    btn_code.setTextColor(getResources().getColor(R.color.login_module_white));
                    et_register_code.setTextSize(15);
                    et_register_code.setTextColor(getResources().getColor(R.color.login_module_white));
                    et_register_code.setHintTextColor(getResources().getColor(R.color.login_module_white));
                    if (!CommonUtils.isPhoneNumer(et_register_phone.getText().toString())) {
                        showToast(getString(R.string.login_module_send_phone_number_not_legal));
                        return;
                    }
                } else {
                    select_code.setVisibility(View.INVISIBLE);
                    btn_code.setTextColor(getResources().getColor(R.color.login_module_login_pwd));
                    select_code.setTextSize(15);
//                    et_register_code.setTextColor(getResources().getColor(R.color.login_module_color_shorcut_login));
                    et_register_code.setTextColor(getResources().getColor(R.color.login_module_white));
                    et_register_code.setHintTextColor(getResources().getColor(R.color.login_module_color_shorcut_login));
                }
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

        } else if (i == R.id.btn_complete) {
            login();

        } else if (i == R.id.tv_account_login) {
            startActivity(new Intent(this, LeFenPhoneLoginActivity.class));
            finish();
        }
    }


    //快捷登录
    public void shortCut(final View view) {
        isQuickLogin = true;
        et_register_phone.setText("");
        et_register_code.setText("");
        et_register_code.setHint("请输入验证码");
        et_register_phone.setEnabled(true);
        et_register_phone.requestFocus();
        et_register_code.clearFocus();
        et_register_code.setHintTextColor(getResources().getColor(R.color.login_module_color_shorcut_login));
        btn_code.setVisibility(View.VISIBLE);
        sort_cut.setTextColor(getResources().getColor(R.color.login_module_white));
        sort_cut.setTextSize(18);
        et_register_code.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    /**
     * 登录
     */
    private void login() {
        if (!CommonUtils.isPhoneNumer(et_register_phone.getText().toString()) ||
                TextUtils.isEmpty(et_register_code.getText().toString())) {
            showToast("请输入正确的信息");
            return;
        }
        final String registerPhone = et_register_phone.getText().toString().trim();
        //快捷登录
        if (CommonUtils.isPhoneNumer(registerPhone)) {
            RetrofitUtils.getInstance(mContext).checkUser(registerPhone, new Callback<CheckUserEntity>() {
                @Override
                public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                    BaseEntity body = response.body();
                    if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,body)) {
                        showToast(R.string.login_module_service_exception);
                        return;
                    }
                    if (body.code == -12) {
                        final String registerCode = et_register_code.getText().toString().trim();
                        String sign = etReferrerPhone.getText().toString().trim();//推荐人手机号
                        if (!TextUtils.isEmpty(sign) && !CommonUtils.isPhoneNumer(sign)) {//如果推荐人手机号码不为空，则判断其格式，若为空则不判断格式
                            showToast("请填写正确推荐人号码");
                            return;
                        }
                        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                                .loginBySmsCode(registerPhone, registerCode, sign, new Callback<LoginEntity>() {
                                    @Override
                                    public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                                        LoginEntity body = response.body();
                                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                                            showToast(getString(R.string.login_module_service_exception));
                                            return;
                                        }
                                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                                            switch (body.code) {
                                                case 1:
//                                    快捷登录成功，发送快捷登录成功广播
                                                    Intent intent = new Intent("com.yilian.smscode.login");
                                                    intent.putExtra("RegisterAccount", body);
                                                    intent.putExtra("phone", registerPhone);
                                                    sendBroadcast(intent);
                                                    finish();
                                                    break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginEntity> call, Throwable t) {
                                        showToast(R.string.login_module_net_work_not_available);
                                    }
                                });
                    } else {
                        showToast(R.string.login_module_phone_not_register);
                        return;
                    }
                }

                @Override
                public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                    showToast(R.string.login_module_net_work_not_available);
                }
            });


        } else {
            showToast(R.string.login_module_send_phone_number_not_legal);
        }
    }


    public void getCode(View view) {
        if (TextUtils.isEmpty(phone)) {
            showToast("请填写手机号码");
            return;
        }
        showAlertDialogAndRefreshImgCode(phone);
    }

    /**
     * 获取验证码
     */
    private void getSMSCode() {
        if (!CommonUtils.isPhoneNumer(et_register_phone.getText().toString())) {
            showToast(R.string.login_module_send_phone_number_not_legal);
        } else if (CommonUtils.isPhoneNumer(et_register_phone.getText().toString())) {
            phoneNumber = et_register_phone.getText().toString();
            getSmsCode(phoneNumber);
        }
    }

    //发送短信或语音验证码
    private void getSmsCode(final String phoneNumber) {
        btn_code.setEnabled(true);
        if (isGetVoiceCode) {//语音验证码
            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .getSmsCode(phoneNumber, "3", etIMGCode, "1", "1", new Callback<BaseEntity>() {
                        @Override
                        public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                            isFristGetCode++;
                            BaseEntity body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,body)){
                                if ("0".equals(body.code)) {//该接口返回0代表图形验证码输入错误，需要额外处理，所以在全局处理返回码方法之前要先处理返回码为0的情况
                                    showAlertDialogAndRefreshImgCode(phoneNumber);
                                    tvImgCodeError.setVisibility(View.VISIBLE);
                                    tvImgCodeError.setText(R.string.login_module_img_code_error);
                                    etImgCode.setText("");
                                    return;
                                }
                                if (-1 == body.code) {
                                    showToast(R.string.login_module_sms_space);
                                    return;
                                }
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                                    switch (body.code) {
                                        case 1:
                                            dismissAlertDialog();
                                            isGetVoiceCode = !isGetVoiceCode;
                                            starTimer();
                                            btn_code.setText("(" + time + "s)重新发送");
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

        } else {//短信验证码
            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .getSmsCode(phoneNumber, "3", etIMGCode, null, "1", new Callback<BaseEntity>() {
                        @Override
                        public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                            isFristGetCode++;
                            BaseEntity body = response.body();
                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                                showToast(getString(R.string.login_module_service_exception));
                                return;
                            }
                            if ("0".equals(body.code)) {//该接口返回0代表图形验证码输入错误，需要额外处理，所以在全局处理返回码方法之前要先处理返回码为0的情况
                                showAlertDialogAndRefreshImgCode(phoneNumber);
                                tvImgCodeError.setVisibility(View.VISIBLE);
                                tvImgCodeError.setText(R.string.login_module_img_code_error);
                                etImgCode.setText("");
                                return;
                            }
                            if (-1 == body.code) {
                                showToast(R.string.login_module_sms_space);
                                return;
                            }
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                                switch (body.code) {
                                    case 1:
                                        dismissAlertDialog();
                                        starTimer();
                                        btn_code.setText("(" + time + "s)重新发送");
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

    /**
     * 弹出图片验证码界面，并刷新图片验证码
     *
     * @param phoneNumber
     */
    private void showAlertDialogAndRefreshImgCode(String phoneNumber) {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
        if (alertDialog.isShowing()) {//刷新图片验证码
            ImgCodeUtil.setImgCode(phoneNumber, ivImgCode, LeFenSmsCodeLoginActivity.this);
        }
    }

    /**
     * 隐藏图形验证码提示框
     */
    public void dismissAlertDialog() {
        tvImgCodeError.setText(R.string.login_module_img_from_number);
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            etImgCode.setText("");
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
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
            Logger.i("onTextChanged:etIMGCode:" + etIMGCode);
            if (edit == et_register_phone) {
                String phone = edit.getText().toString().trim();
                int len = phone.length();
                if (len >= maxlen) {
                    et_register_code.requestFocus();//设置光标
                    if (!CommonUtils.isPhoneNumer(phone)) {
                        showToast(R.string.login_module_send_phone_number_not_legal);
                        return;
                    }
                    if (isSavePhone) {//如果账号是自动填入的，而不是手动输入的，则不弹出图形验证码
                        //注册过可点击
                        isSavePhone = false;
                        btn_code.setEnabled(true);
                        return;
                    }
                    LeFenSmsCodeLoginActivity.this.phone = phone;
                    showAlertDialogAndRefreshImgCode(phone);

                    //验证当前号码是否有注册过
                    RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
                        @Override
                        public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                            BaseEntity body = response.body();
                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                                showToast(getString(R.string.login_module_service_exception));
                                return;
                            }
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                                switch (body.code) {
                                    case 1:
                                        showToast(R.string.login_module_phone_not_register);
                                        //未注册的用户，不能登录
//                                        btn_code.setEnabled(true);
//                                        rlReferrerPhone.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckUserEntity> call, Throwable t) {

                        }
                    });

                }
            } else if (edit == et_register_code) {
                int len = edit.getText().length();
                if (len > maxlen || len < maxlen) {
                    showToast("请输入正确的验证码");
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            etIMGCode = "";//每次改变手机号码，验证码都要重置
            Logger.i("after:etIMGCode:" + etIMGCode);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
