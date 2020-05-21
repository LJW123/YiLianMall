package com.yilian.loginmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.entity.RecommendInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class V2_RegisterActivity extends BaseActivity implements View.OnClickListener {

    public static final int SMS_CODE_SUCCESS = 9;
    private Button btnCommit;
    private TextView tvLogin;
    private CheckBox checkbox;
    private EditText etPhone;
    private Button btnGetSmsCode;
    private EditText etSmsCode;
    private EditText etReferrerPhone;
    private TextView tvAgreement;
    private com.yilian.mylibrary.widget.JHCircleView ivReferrerHead;
    private TextView tvReferrerName;
    private LinearLayout llReferrer;
    private String etIMGCode;
    private ImageView ivBack;
    private boolean isSuccessReferrerPhone = false;
    private boolean isRegister = false;
    private boolean isShowDialog = false;
    private EditText etPassword;
    private EditText etPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_activity_v2__register);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        String phone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
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

    private void initView() {
        InputFilter[] filters1 = {new InputFilter.LengthFilter(18)};

        View includePassword = findViewById(R.id.include_password);
        ImageView ivPassword = (ImageView) includePassword.findViewById(R.id.iv_icon);
        etPassword = (EditText) includePassword.findViewById(R.id.et_sms_code);
        etPassword.setHint(getString(R.string.login_module_password_set_format));
        etPassword.setFilters(filters1);
        etPassword.setSingleLine();
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置为密码输入框

        View includePassword2 = findViewById(R.id.include_password2);
        ImageView ivPassword2 = (ImageView) includePassword.findViewById(R.id.iv_icon);
        etPassword2 = (EditText) includePassword2.findViewById(R.id.et_sms_code);
        etPassword2.setHint("请再次确认密码");
        etPassword2.setSingleLine();
        etPassword2.setFilters(filters1);
        etPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置为密码输入框


        View includePhone = findViewById(R.id.include_phone);
        ImageView ivPhoneIcon = (ImageView) includePhone.findViewById(R.id.iv_phone_icon);
        ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.login_module_icon_phone));
        TextView tvAccountTitle = (TextView) includePhone.findViewById(R.id.tv_account_title);
        tvAccountTitle.setText("手机号");
        etPhone = (EditText) includePhone.findViewById(R.id.et_phone);//手机号
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        InputFilter[] filterPhone = {new InputFilter.LengthFilter(11)};
        etPhone.setFilters(filterPhone);
        etPhone.addTextChangedListener(new watcher(etPhone, 11));
        btnGetSmsCode = (Button) includePhone.findViewById(R.id.btn_get_sms_code);//获取验证码按钮
        btnGetSmsCode.setOnClickListener(this);
        View includeVerify = findViewById(R.id.include_verify);
        ImageView ivIcon = (ImageView) includeVerify.findViewById(R.id.iv_icon);
        ivIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.login_module_icon_qr));
        TextView tvCodeTitle = (TextView) includeVerify.findViewById(R.id.tv_code_title);
        tvCodeTitle.setText("验证码");
        etSmsCode = (EditText) includeVerify.findViewById(R.id.et_sms_code);//验证码输入框
        etSmsCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = {new InputFilter.LengthFilter(6)};
        etSmsCode.setFilters(filters);//限制输入长度6
        View includeReferrer = findViewById(R.id.include_referrer);
        ImageView ivReferrerIcon = (ImageView) includeReferrer.findViewById(R.id.iv_icon);
        ivReferrerIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.login_module_icon_account));
        TextView tvReferrerCodeTitle = (TextView) includeReferrer.findViewById(R.id.tv_code_title);
        tvReferrerCodeTitle.setText("推荐人");
        etReferrerPhone = (EditText) includeReferrer.findViewById(R.id.et_sms_code);
        InputFilter[] filtersReferrer = {new InputFilter.LengthFilter(11)};
        etPhone.setFilters(filtersReferrer);
        etReferrerPhone.setHint("请输入您的推荐人手机号");
        etReferrerPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        etReferrerPhone.addTextChangedListener(new watcher(etReferrerPhone, 11));

        btnCommit = (Button) findViewById(R.id.btn_commit);//提交按钮
        tvLogin = (TextView) findViewById(R.id.tv_login);//跳转登录界面按钮
        Spanned spanned = Html.fromHtml("——   已有个人账号?<font color='#666666'>我要登录</font>   ——");
        tvLogin.setText(spanned);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setChecked(true);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        {//验证码弹窗
            alertDialog = new AlertDialog.Builder(this).create();
            inflate = View.inflate(this, R.layout.login_module_dialog_verification_code, null);
            alertDialog.setView(inflate);
            alertDialog.setCancelable(true);

            etImgCode = (EditText) inflate.findViewById(R.id.et_img_code);
            ivImgCode = (ImageView) inflate.findViewById(R.id.iv_img_code);
            ivImgCode.setOnClickListener(this);
            btnDialogConfirm = (Button) inflate.findViewById(R.id.btn_dialog_confirm);
            tvImgCodeError = (TextView) inflate.findViewById(R.id.tv_img_code_error);
            ivImgCodeClose = (ImageView) inflate.findViewById(R.id.iv_close);
            ivImgCodeClose.setOnClickListener(this);
        }

        btnCommit.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        Spanned spanned1 = Html.fromHtml("我已阅读并接受<font color='#fe5062'><u>益联益家用户协议</u></font>");
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        tvAgreement.setText(spanned1);
        tvAgreement.setOnClickListener(this);
        ivReferrerHead = (com.yilian.mylibrary.widget.JHCircleView) findViewById(R.id.iv_referrer_head);
        tvReferrerName = (TextView) findViewById(R.id.tv_referrer_name);
        llReferrer = (LinearLayout) findViewById(R.id.ll_referrer);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_get_sms_code) {
            Logger.i("isPhoneNumer " + CommonUtils.isPhoneNumer(getInputPhone()));
            if (CommonUtils.isPhoneNumer(getInputPhone())) {
                showImgCodeDialog();
            } else {
                showToast(R.string.login_module_send_phone_number_not_legal);
            }
        } else if (i == R.id.btn_commit) {
            //验证数据的格式是否正确（推荐人呢是必填项）
            submit();
        } else if (i == R.id.tv_login) {
            Intent intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
            startActivity(intent);
            finish();
        } else if (i == R.id.tv_agreement) {
            //通过广播跳转web界面
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext,Constants.REGISTER_HTTP,false);
        } else if (i == R.id.iv_img_code) {//图形验证码图片
            refreshImgCode();
        } else if (i == R.id.iv_close) {//关闭图形验证码
            dismissAlertDialog();
        } else if (i == R.id.iv_back) {
            finish();
        }
    }

    private MyHander myHandler = new MyHander(new WeakReference<V2_RegisterActivity>(this));

    private class MyHander extends Handler {
        private WeakReference<V2_RegisterActivity> mRegisterActivity;

        public MyHander(WeakReference<V2_RegisterActivity> mRegisterActivity) {
            this.mRegisterActivity = mRegisterActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SMS_CODE_SUCCESS:
                    ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                        @Override
                        public void run() {

                            if (smsCodeGap == 0) {
                                btnGetSmsCode.setClickable(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnGetSmsCode.setText("重新发送");
                                        Logger.i("smscodeGrp  " + smsCodeGap);
                                        if (etSmsCode.length() <= 0) {
                                            showVoiceDialog();//弹出发送语音验证码提示框
                                        }
                                    }
                                });
                            } else {
                                if (smsCodeGap > 0) {
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
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private int smsCodeGap = Constants.SMS_COUNT_TIME;

    /**
     * 弹出发送语音验证码提示框
     */
    private void showVoiceDialog() {
        Logger.i("etSmsCode Leight " + etSmsCode.length());
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
                        getSmsCode("1");
                    }
                })
                .create().show();
    }


    /**
     * 获取短信验证码
     */
    private void getSmsCode(String voice) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSmsCode(getInputPhone(), "0", getInputImgCode(), voice, "1", new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        stopMyDialog();
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            dismissAlertDialog();
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        etPhone.setFocusable(true);
                                        etPhone.setEnabled(true);
                                        etPhone.setClickable(true);
                                        showToast("发送成功");
                                        smsCodeGap = Constants.SMS_COUNT_TIME;
                                        if (myHandler != null) {
                                            myHandler.sendEmptyMessage(SMS_CODE_SUCCESS);
                                        }
                                        btnGetSmsCode.setClickable(false);
                                        break;
                                    case -2:
                                        showToast("操作频繁");
                                        break;
                                    case -3://操作频繁
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                //如果返回码为0正码图形验证码验证错误

                                switch (response.body().code) {
                                    case 0:
                                        showImgCodeDialog();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.login_module_net_work_not_available);
                    }
                });
    }

    @NonNull
    private String getInputImgCode() {
        return etImgCode.getText().toString().trim();
    }

    /**
     * 弹出图形验证码界面
     */
    private void showImgCodeDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
            Logger.i("alertDialog .isSHOW ");
        }
        if (alertDialog.isShowing()) {
            refreshImgCode();
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

    /**
     * 刷新图形验证码
     */
    private void refreshImgCode() {
        ImgCodeUtil.setImgCode(getInputPhone(), ivImgCode, this);
    }

    @NonNull
    private String getInputPhone() {
        return etPhone.getText().toString().trim();
    }

    private void submit() {

        String phone = getInputPhone();
        if (TextUtils.isEmpty(phone) && !CommonUtils.isPhoneNumer(phone)) {
            showToast(R.string.login_module_send_phone_number_not_legal);
            return;
        }
        if (isRegister) {
            showToast("当前手机号已注册，不可重复注册");
            return;
        }
        String code = etSmsCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPassword2.getText().toString().trim();
        if (!CommonUtils.passwordVerify(password)) {
            showToast(R.string.login_module_password_format);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            showToast("请再次输入密码");
            return;
        }
        if (!password.equals(passwordConfirm)) {
            showToast("两次输入密码不一致");
            etPassword.setText("");
            etPassword.setHint("请输入密码");
            etPassword2.setText("");
            etPassword2.setHint("请再次输入密码");
            return;
        }
        if (!checkbox.isChecked()) {
            showToast("请阅读并同意用户协议");
            return;
        }
        //如果验证手机号空 弹框
        String reFerrerPhone = etReferrerPhone.getText().toString().trim();

        if (TextUtils.isEmpty(reFerrerPhone) && !isShowDialog) { //默认第一次是要弹出显示框的
            reFerrerPhone = "";//没有推荐人的话可以注册过但是要弹窗提示用户
            showDialog("温馨提示", "推荐人手机号为空，系统将自动为您指定推荐人。(后期不可更改号码)", "", 0, Gravity.CENTER, "知道了", null, false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            isShowDialog = true;
                            break;
                    }
                }
            }, mContext);
            return;
        }
        Logger.i("checkReferrer(reFerrerPhone)" + isSuccessReferrerPhone + "  " + !TextUtils.isEmpty(reFerrerPhone));
        if (!TextUtils.isEmpty(reFerrerPhone) && !isSuccessReferrerPhone) {
            //不为空 不是手机号，不是数据库正确手机号
            showToast("请输入正确的推荐人手机号");
            return;
        }

        register(phone, code, password, reFerrerPhone);
    }

    private void register(String phone, String code, String password, String reFerrerPhone) {
        password = CommonUtils.getMD5Str(password)
                + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
        Logger.i("注册密码：" + password);
        startMyDialog();

        final RetrofitUtils retrofitUtils = RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext));
        retrofitUtils.getRegisterAccount(phone, code, password, reFerrerPhone, new Callback<LoginEntity>()

                {
                    @Override
                    public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
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

                                        //发送广播存储信息
//                                        Intent intent = new Intent();
//                                        intent.setAction("com.yilian.smscode.login");
//                                        intent.putExtra("RegisterAccount", response.body());
//                                        intent.putExtra("phone", getInputPhone());
//                                        sendBroadcast(intent);

                                        LoginEntity entity = response.body();
                                        entity.phone = getInputPhone();
                                        EventBus.getDefault().post(entity);

                                        AppManager.getInstance().killActivity(LeFenPhoneLoginActivity.class);
                                        AppManager.getInstance().killActivity(V2_LoginActivity.class);
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
                        stopMyDialog();
                    }
                }
        );
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
                Logger.i("deitText  " + edit + "  " + etReferrerPhone + " " + etPhone);
                if (edit.getId() == etPhone.getId()) {
                    RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
                        @Override
                        public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                switch (response.body().code) {
                                    case 1:
                                        isRegister = false;
                                        break;
                                    case -12:
                                        //账号已经注册过，直接登录
                                        new VersionDialog.Builder(mContext).setMessage("亲,手机号已存在，前往登录吧!")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                                                        intent.putExtra("phone", etPhone.getText().toString().trim());
                                                        startActivity(intent);
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create().show();
                                        isRegister = true;
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                            showToast(R.string.network_module_net_work_error);
                        }
                    });
                } else if (edit.getId() == etReferrerPhone.getId()) {
                    checkReferrer(phone);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

    }

    /**
     * 检查推荐人是否存在
     *
     * @param phone
     */
    private void checkReferrer(String phone) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .verifyRecommendInfo(phone, new Callback<RecommendInfoEntity>() {
                    @Override
                    public void onResponse(final Call<RecommendInfoEntity> call, Response<RecommendInfoEntity> response) {
                        stopMyDialog();
                        final RecommendInfoEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        isSuccessReferrerPhone = true;//有推荐人;
                                        llReferrer.setVisibility(View.VISIBLE);
                                        tvReferrerName.setText(TextUtils.isEmpty(body.userName) ? "暂无昵称" : body.userName);
                                        if (!TextUtils.isEmpty(body.photo)) {
                                            ImageLoader.getInstance().displayImage(WebImageUtil.getInstance().getWebImageUrlNOSuffix(body.photo), ivReferrerHead);
                                        } else {

                                        }
                                        break;
                                    default:
                                        isSuccessReferrerPhone = false;
                                        break;
                                }
                            } else {
                                isSuccessReferrerPhone = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RecommendInfoEntity> call, Throwable t) {
                        isSuccessReferrerPhone = false;
                        stopMyDialog();
                        showToast("检查推荐人失败,请稍后再试");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeMessages(SMS_CODE_SUCCESS);
            myHandler = null;
        }
    }
}
