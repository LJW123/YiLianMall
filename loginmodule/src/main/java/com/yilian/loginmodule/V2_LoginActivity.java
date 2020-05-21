package com.yilian.loginmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V2_LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final int SMS_CODE_SUCCESS = 9;
    private EditText etPhone;
    private Button btnGetSmsCode;
    private EditText etSmsCode;
    private Button btnLogin;
    private TextView tvRegisterLogin;
    private String etIMGCode;
    private ImageView ivBack;
    private Button btnLoginByPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_activity_v2__login);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        btnLoginByPassword.setOnClickListener(this);
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
                getSmsCode(null);


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
    private int smsCodeGap = Constants.SMS_COUNT_TIME;

    @Override
    protected void onPause() {
        super.onPause();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }

    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SMS_CODE_SUCCESS:
                    Logger.i("myHandler:收到了消息");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (smsCodeGap == 0) {
                                btnGetSmsCode.setClickable(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnGetSmsCode.setText("重新发送");
                                        if (etSmsCode.length() <= 0) {
                                            showVoiceDialog();//弹出发送语音验证码提示框
                                        }
                                    }
                                });
                            } else {
                                if (smsCodeGap > 0)
                                    smsCodeGap--;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnGetSmsCode.setText(smsCodeGap + "秒后重新发送");
                                    }
                                });

                                try {
                                    Thread.sleep(1000);
                                    if (myHandler != null) {
                                        myHandler.sendEmptyMessage(SMS_CODE_SUCCESS);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    break;
            }
        }
    };

    private void initView() {
        btnLoginByPassword = (Button) findViewById(R.id.btn_login_by_password);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        TextView tvCodeTitle = (TextView) findViewById(R.id.tv_code_title);
        tvCodeTitle.setText("验证码");
        ImageView ivPhoneIcon = (ImageView) findViewById(R.id.iv_phone_icon);
        ivPhoneIcon.setImageResource(R.mipmap.login_module_icon_account);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPhone.addTextChangedListener(new watcher(etPhone, 11));
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        InputFilter[] filters1 = {new InputFilter.LengthFilter((11))};
        etPhone.setFilters(filters1);
        etPhone.setText(sp.getString("phone", ""));
        btnGetSmsCode = (Button) findViewById(R.id.btn_get_sms_code);
        etSmsCode = (EditText) findViewById(R.id.et_sms_code);
        InputFilter[] filters = {new InputFilter.LengthFilter(6)};
        etSmsCode.setFilters(filters);
        etSmsCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvRegisterLogin = (TextView) findViewById(R.id.tv_register_login);
        Spanned spanned = Html.fromHtml("——   还没有个人账号?<font color=\"#666666\">快速注册</font>   ——");
        tvRegisterLogin.setText(spanned);
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

        ivBack.setOnClickListener(this);
        btnGetSmsCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegisterLogin.setOnClickListener(this);
    }

    private void initData() {
        String phone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_get_sms_code) {
            if (CommonUtils.isPhoneNumer(getInputPhone())) {
                showImgCodeDialog();
            }else{
                showToast(R.string.login_module_send_phone_number_not_legal);
            }
            //获取验证码的逻辑：先要弹出图形验证码的弹框，同时请求当前的图形验证码，并更新UI，当用户输入完成时点击确定
            //再去请求短信的验证码，服务器会对验证码进行判断是否正确，证据返回值。如果不正确继续弹出获取图形验证码。

        } else if (i == R.id.btn_login) {
            login();
        } else if (i == R.id.tv_register_login) {
            Intent intent = new Intent(mContext, V2_RegisterActivity.class);
            intent.putExtra("phone", etPhone.getText().toString().trim());
            startActivity(intent);

        } else if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.btn_login_by_password) {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
            finish();
        }
    }

    /**
     * 登录
     */
    private void login() {

        if (!CommonUtils.isPhoneNumer(getInputPhone())) {
            showToast(R.string.login_module_send_phone_number_not_legal);
            return;
        }
        String code = etSmsCode.getText().toString().trim();
        if (TextUtils.isEmpty(code) && code.length() < 6) {
            showToast("请填写正确的验证码");
            return;
        }
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .loginByQuick(getInputPhone(), code, new Callback<LoginEntity>() {
                    @Override
                    public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent("com.yilian.smscode.login");
//                                        intent.putExtra("RegisterAccount", response.body());
//                                        intent.putExtra("phone", getInputPhone());
//                                        sendBroadcast(intent);

                                        LoginEntity entity = response.body();
                                        entity.phone = getInputPhone();
                                        EventBus.getDefault().post(entity);

                                        //刷新个人页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT_LOADING, true, mContext);
                                        //刷新购物车页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_SHOP_FRAGMENT, true, mContext);
                                        //刷新主页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
                                        //刷新幸运购标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_LUCKY_DETAIL, true, mContext);
                                        //刷新个人页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);

                                        finish();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginEntity> call, Throwable t) {
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    /**
     * 弹出发送语音验证码提示框
     */
    private void showVoiceDialog() {
        new VersionDialog.Builder(mContext)
                .setTitle(null)
                .setMessage("短信验证码没有获取到?试试语音验证码吧")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Logger.i("smsCodeGap  " + smsCodeGap);
                        getSmsCode("1");
                    }
                })
                .create().show();
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
        ImgCodeUtil.setImgCode(getInputPhone(), ivImgCode, this);
    }

    /**
     * 获取短信验证码
     */
    private void getSmsCode(String voice) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getSmsCode(getInputPhone(), "1", getInputImgCode(), voice, "1", new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            dismissAlertDialog();
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                dismissAlertDialog();
                                switch (body.code) {
                                    case 1:
                                        showToast("发送成功");
                                        etPhone.setFocusable(true);
                                        smsCodeGap = Constants.SMS_COUNT_TIME;
                                        if (myHandler != null) {
                                            myHandler.sendEmptyMessage(SMS_CODE_SUCCESS);
                                        }
                                        btnGetSmsCode.setClickable(false);
                                        Logger.i("");
                                        stopMyDialog();
                                        break;
                                    case -3://操作频繁
                                        showToast("系统繁忙，请稍后重试");
                                        stopMyDialog();
                                        break;
                                    case -2:
                                        showToast("操作频繁");
                                        stopMyDialog();
                                        break;
                                    default:
                                        stopMyDialog();
                                        break;
                                }
                            } else {
                                //如果返回码为0正码图形验证码验证错误

                                switch (response.body().code) {
                                    case 0:
                                        showImgCodeDialog();
                                        stopMyDialog();
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.login_module_net_work_not_available);
                    }
                });
    }


    public String getInputPhone() {
        return etPhone.getText().toString().trim();
    }

    public String getInputImgCode() {
        return etImgCode.getText().toString().trim();
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
            String phone = edit.getText().toString().trim();
            int len = phone.length();

            if (len == maxlen) {
                if (!CommonUtils.isPhoneNumer(phone)) {
                    showToast(R.string.login_module_send_phone_number_not_legal);
                    return;
                }
                RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
                    @Override
                    public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            switch (response.body().code) {
                                case 1:
                                    showToast("您还没有注册，请注册");
                                    break;
                                case -12:
                                    //账号已经注册过，直接登录
                                    btnGetSmsCode.setEnabled(true);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                        showToast(R.string.network_module_net_work_error);
                    }
                });
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

}
