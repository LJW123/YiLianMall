package com.yilian.mall.ui.mvp.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.PhonePayPasswordActivity;
import com.yilian.mall.ui.mvp.presenter.CertificationPresenter;
import com.yilian.mall.ui.mvp.presenter.ICertificationPresenter;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.SmsCodeEntity;
import com.yilian.networkingmodule.entity.MerchantMyRevenueEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.functions.Consumer;
import rx.Subscription;

import static com.yilian.mall.ui.mvp.view.CertificationResultImplViewActivity.IS_FROM_USER_INFO_ACTIVITY;
import static com.yilian.mylibrary.SmsCodeType.BIND_BANK_CARD;

/**
 * @author
 *         实名认证
 */
public class CertificationViewImplActivity extends BaseAppCompatActivity implements ICertificationView {

    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvTitle;
    private EditText etContent;
    private ImageView ivRightIcon;
    /**
     * 实名认证相关协议
     */
    private TextView tvAgreement;
    private Button btnNextStep;
    private View includeName;
    private View includeIdNumber;
    private EditText etName;
    private TextView tvNameTitle;
    private TextView tvIdNumberTitle;
    private EditText etIdCardNumber;

    private ICertificationPresenter iCertificationPresenter;
    private String smsCode;
    private String servicePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_view_impl);
        StatusBarUtil.setColor(this, Color.WHITE);
        iCertificationPresenter = new CertificationPresenter(this);
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("实名认证");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);

//姓名
        includeName = findViewById(R.id.include_name);
        tvNameTitle = includeName.findViewById(R.id.tv_title);
        tvNameTitle.setText("真实姓名");
        etName = includeName.findViewById(R.id.et_content);
        etName.setHint("真实姓名");
        etName.setSingleLine(true);
        etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        ImageView nameivRightIcon = includeName.findViewById(R.id.iv_right_icon);
//        身份证号
        includeIdNumber = findViewById(R.id.include_ID_number);
        tvIdNumberTitle = includeIdNumber.findViewById(R.id.tv_title);
        tvIdNumberTitle.setText("身份证号");
        etIdCardNumber = includeIdNumber.findViewById(R.id.et_content);
        etIdCardNumber.setHint("身份证号");
        etIdCardNumber.setSingleLine(true);
        etIdCardNumber.setKeyListener(DigitsKeyListener.getInstance("0123456789xX"));
        etIdCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        ImageView idNumberivRightIcon = includeIdNumber.findViewById(R.id.iv_right_icon);
        showNameView();
    }

    private void initListener() {
        setOnFocusListener(etName, tvNameTitle);
        setOnFocusListener(etIdCardNumber, tvIdNumberTitle);
        setTextChangedListener(etName);
        setTextChangedListener(etIdCardNumber);

        RxUtil.clicks(btnNextStep, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!RegExUtils.isIdCard(getIdCard())) {
                    showToast("请输入正确的身份证号");
                    return;
                }
                if (TextUtils.isEmpty(getUserName())) {
                    showToast("请输入姓名");
                    return;
                }
                startSmsCodeActivity();
            }
        });

        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
    }

    /**
     * editText获取焦点时 textview显示
     * 检测验证元素是否完整
     *
     * @param etContent
     * @param tvTitle
     */
    void setOnFocusListener(EditText etContent, TextView tvTitle) {
        etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    tvTitle.setVisibility(View.VISIBLE);
                    Logger.i("edittext获取了焦点");
                }
                setBtnNextStepStatus();
            }
        });

    }

    /**
     * 判断每个Edittext内容是否为空
     *
     * @param editText
     */
    private void setTextChangedListener(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnNextStepStatus();
            }
        });
    }

    /**
     * 根据各个输入框内容来判断按钮状态
     */
    private void setBtnNextStepStatus() {
        if (!TextUtils.isEmpty(getUserName()) && !TextUtils.isEmpty(getIdCard())) {
            btnNextStep.setClickable(true);
            btnNextStep.setBackgroundResource(R.drawable.jp_bg_btn_red_all_radius1);
        } else {
            btnNextStep.setClickable(false);
            btnNextStep.setBackgroundResource(R.drawable.jp_bg_btn_ccc_all_radius1);
        }
    }

    /**
     * 获取短信验证码后的验证逻辑
     *
     * @param smsCodeEntity
     */
    @Subscribe
    public void checkInfoBySmsCode(SmsCodeEntity smsCodeEntity) {
        smsCode = smsCodeEntity.smsCode;
        Subscription subscription = iCertificationPresenter.addAuth(mContext);
        addSubscription(subscription);
    }

    @Override
    public void startSmsCodeActivity() {
        SkipUtils.toSMSCheck(mContext, getPhone(), BIND_BANK_CARD);
        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
    }

    @Override
    public void startBindSuccessActivity() {
        /**
         * 绑卡成功发送事件刷新营收页面数据
         * {@link com.yilianmall.merchant.activity.MerchantMyRevenueActivity#updateRevenueInfo(MerchantMyRevenueEntity entity)}
         */
        EventBus.getDefault().post(new MerchantMyRevenueEntity());
        finish();
        Intent intent = new Intent(mContext, CertificationResultImplViewActivity.class);
        intent.putExtra(IS_FROM_USER_INFO_ACTIVITY, getIntent().getBooleanExtra(IS_FROM_USER_INFO_ACTIVITY, false));
        startActivity(intent);
    }

    @Override
    public void startSetPasswordActivity() {
        Intent intent = new Intent(mContext, PhonePayPasswordActivity.class);
        intent.putExtra("titleName", "手机支付密码");
        intent.putExtra("pageType", true);
        startActivity(intent);
        finish();
    }

    @Override
    public String getUserName() {
        return etName.getText().toString().trim();
    }

    @Override
    public String getPhone() {
        return PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE, mContext);
    }

    @Override
    public String getIdCard() {
        return etIdCardNumber.getText().toString().trim();
    }

    @Override
    public String getServicePhone() {
        return servicePhone;
    }

    @Override
    public String getSmsCode() {
        return smsCode;
    }

    @Override
    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    /**
     * 显示填写姓名、身份证号view
     */
    @Override
    public void showNameView() {
        includeName.setVisibility(View.VISIBLE);
        includeIdNumber.setVisibility(View.VISIBLE);
        tvAgreement.setText("以上信息用于身份验证");
        RxUtil.clicks(tvAgreement, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                do noting
            }
        });
    }
}
