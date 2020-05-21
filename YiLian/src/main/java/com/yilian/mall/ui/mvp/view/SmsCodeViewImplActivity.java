package com.yilian.mall.ui.mvp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.mvp.presenter.ICertificationPresenter;
import com.yilian.mall.ui.mvp.presenter.ISmsCodePresenter;
import com.yilian.mall.ui.mvp.presenter.SmsCodePresenterImpl;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.SmsCodeEntity;
import com.yilian.mylibrary.SmsCodeType;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.StopDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.functions.Consumer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * @author
 *         短信验证码验证
 */
public class SmsCodeViewImplActivity extends BaseActivity implements ISmsCodeView {

    private AlertDialog smsCodeDialog;
    private TextView tvSmsCodePhone;
    private EditText etSmsCode;
    private TextView tvGetSmsCode;
    private TextView tvCancel;
    private TextView tvConfirm;
    private String phone;
    private ISmsCodePresenter smsCodePresenter;
    /**
     * 1需要校验图形验证码 2不需要校验图形验证码 其它为登录状态下获取短信验证码
     */
    private int verifyType;
    /**
     * {@link SmsCodeType}
     */
    private int smsType;
    private AlertDialog imgCodeDialog;
    private EditText etImgCode;
    private ImageView ivImgCode;
    private Button btnDialogConfirm;
    private TextView tvImgCodeError;
    private ImageView ivImgCodeClose;
    /**
     * 软键盘弹出时，点击返回隐藏软键盘，没有软键盘弹出时，点击返回直接关闭Activity页面
     */
    private DialogInterface.OnCancelListener dialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (!keyboardIsShowing()) {
                finish();
            }
        }
    };
    private ICertificationPresenter certificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_code_view_impl);
        certificationPresenter = (ICertificationPresenter) getIntent().getSerializableExtra("certificationPresenter");
        phone = getIntent().getStringExtra("phone");
        smsCodePresenter = new SmsCodePresenterImpl(this);
        initView();
        smsCodePresenter.isShowImgCode(mContext);
        StatusBarUtils.setStatusBarColor(this, R.color.white, false);
    }


    @Override
    public void showSmsInputDialog() {
        if (imgCodeDialog != null && imgCodeDialog.isShowing()) {
            imgCodeDialog.dismiss();
        }
        if (smsCodeDialog == null) {
            View inflate = View.inflate(mContext, R.layout.dialog_sms_code, null);
            smsCodeDialog = new AlertDialog.Builder(mContext).create();
            smsCodeDialog.setView(inflate);
            smsCodeDialog.setCanceledOnTouchOutside(false);
            tvSmsCodePhone = (TextView) inflate.findViewById(R.id.tv_sms_code_phone);
            tvSmsCodePhone.setText(RegExUtils.isPhone(phone) ? "输入手机尾号" + phone.substring(7, phone.length()) + "接收到的短信验证码" :"号码错误" );
            etSmsCode = (EditText) inflate.findViewById(R.id.et_sms_code);
            tvGetSmsCode = (TextView) inflate.findViewById(R.id.tv_get_sms_code);
            tvCancel = (TextView) inflate.findViewById(R.id.tv_cancel);
            tvConfirm = (TextView) inflate.findViewById(R.id.tv_confirm);
            initSmsViewClickListener();
        }

        if (!smsCodeDialog.isShowing()) {
            smsCodeDialog.show();
        }
    }


    @Override
    public void showImgCodeDialog() {
        if (smsCodeDialog != null && smsCodeDialog.isShowing()) {
            smsCodeDialog.dismiss();
        }
        if (imgCodeDialog == null) {
            imgCodeDialog = new AlertDialog.Builder(mContext).create();
            View inflate = View.inflate(this, com.yilian.loginmodule.R.layout.login_module_dialog_verification_code, null);
            imgCodeDialog.setView(inflate);
            imgCodeDialog.setCanceledOnTouchOutside(false);
            etImgCode = (EditText) inflate.findViewById(com.yilian.loginmodule.R.id.et_img_code);
            ivImgCode = (ImageView) inflate.findViewById(com.yilian.loginmodule.R.id.iv_img_code);
            btnDialogConfirm = (Button) inflate.findViewById(com.yilian.loginmodule.R.id.btn_dialog_confirm);
            tvImgCodeError = (TextView) inflate.findViewById(com.yilian.loginmodule.R.id.tv_img_code_error);
            ivImgCodeClose = (ImageView) inflate.findViewById(com.yilian.loginmodule.R.id.iv_close);
            initImgCodeViewClickListener();
        }
        if (!imgCodeDialog.isShowing()) {
            imgCodeDialog.show();
        }
        ImgCodeUtil.setImgCode(phone, ivImgCode, SmsCodeViewImplActivity.this);
    }


    @Override
    public void refreshImgCode() {
        showImgCodeDialog();
        ImgCodeUtil.setImgCode(phone, ivImgCode, SmsCodeViewImplActivity.this);
    }

    /**
     * 图片验证码dialog内容点击
     */
    private void initImgCodeViewClickListener() {
//        点击图形验证码确定按钮 验证图形验证码
        RxUtil.clicks(btnDialogConfirm, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!TextUtils.isEmpty(getVerifyCode()) && getVerifyCode().length() >= 4) {
                    Subscription subscription = smsCodePresenter.getSmsCode(mContext);
                    addSubscription(subscription);
                } else {
                    showToast("请填写正确图形码");
                }
            }
        });
//        刷新图形验证码
        RxUtil.clicks(ivImgCode, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                refreshImgCode();
            }
        });
//        关闭图形验证码界面
        RxUtil.clicks(ivImgCodeClose, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        imgCodeDialog.setOnCancelListener(dialogCancelListener);
    }

    @Subscribe
    public void stopDialogBus(StopDialog stopDialog) {
        stopMyDialog();
    }

    /**
     * 发送验证码dialog内容点击
     */
    private void initSmsViewClickListener() {
//        获取验证码
        RxUtil.clicks(tvGetSmsCode, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (RegExUtils.isPhone(getPhone())) {
//                    Subscription subscription = smsCodePresenter.getSmsCode(mContext);
                    Subscription subscription = smsCodePresenter.isShowImgCode(mContext);
                    addSubscription(subscription);
                } else {
                    showToast("请输入正确手机号");
                }
            }
        });
//       使用短信验证码进行验证操作
        RxUtil.clicks(tvConfirm, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                /**
                 * {@link CertificationViewImplActivity 347}
                 */
                startMyDialog();
                EventBus.getDefault().post(new SmsCodeEntity(etSmsCode.getText().toString().trim()));
            }
        });
//        取消验证 页面关闭
        RxUtil.clicks(tvCancel, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
//        RxUtil.clicks(etSmsCode, new Consumer() {
//            @Override
//            public void accept(Object o) throws Exception {
//
//            }
//        });
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String smsCode = etSmsCode.getText().toString();
                if (!TextUtils.isEmpty(smsCode)) {
                    if (smsCode.length() >= 6) {
                        tvConfirm.setClickable(true);
                        tvConfirm.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
                    } else {
                        tvConfirm.setClickable(false);
                        tvConfirm.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        smsCodeDialog.setOnCancelListener(dialogCancelListener);
    }

    @Override
    public void finish() {
        if (smsCodeDialog != null && smsCodeDialog.isShowing()) {
            smsCodeDialog.dismiss();
        }
        if (imgCodeDialog != null && imgCodeDialog.isShowing()) {
            imgCodeDialog.dismiss();
        }
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initView() {

    }


    @Override
    public boolean smsCodeDialogIsShowing() {
        if (smsCodeDialog == null) {
            return false;
        }
        return smsCodeDialog.isShowing();
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setVerifyType(int verifyType) {
        this.verifyType = verifyType;
    }

    @Override
    public int getVerifyType() {
        return verifyType;
    }

    @Override
    public int getSmsType() {
        return getIntent().getIntExtra("smsType", 0);
    }

    @Override
    public String getVoice() {
        return null;
    }

    @Override
    public String getSmsCode() {
        if (etSmsCode == null) {
            return null;
        }
        return etSmsCode.getText().toString().trim();
    }

    /**
     * 获取图形验证码
     *
     * @return
     */
    @Override
    public String getVerifyCode() {
        if (etImgCode == null) {
            return null;
        }
        return etImgCode.getText().toString().trim();
    }

    @Override
    public void startCountDown() {
        tvGetSmsCode.setClickable(false);
        tvGetSmsCode.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
        Subscription subscription = RxUtil.countDown(120)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //                                开始倒计时
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong != 0) {
                            tvGetSmsCode.setText(aLong + "秒后重发");
                        } else {
                            tvGetSmsCode.setClickable(true);
                            tvGetSmsCode.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
                            tvGetSmsCode.setText("发送验证码");
                        }
                    }
                });
        addSubscription(subscription);
    }

    boolean keyboardIsShowing() {
        return ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();
    }
}
