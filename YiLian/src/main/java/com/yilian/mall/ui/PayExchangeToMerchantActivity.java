package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.widget.PasswordFinished;
import com.yilian.mylibrary.widget.PayDialog;
import com.yilian.networkingmodule.entity.MerchantExchangeEntity;
import com.yilian.networkingmodule.entity.PayToMerchantExchangeEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.math.BigDecimal;

import rx.Observer;
import rx.Subscription;

public class PayExchangeToMerchantActivity extends BaseAppCompatActivity implements View.OnClickListener, PasswordFinished {
    public static final String TAG_MERCHANT_ID = "PayExchangeToMerchantActivity";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private EditText etExchangeNum;
    private ImageView ivMerchantPhoto;
    private TextView tvMerchantName;
    private Button btnCommit;
    private String mMerchantId;
    private PayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_exchange_to_merchant);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        etExchangeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DecimalUtil.keep2Decimal(s,etExchangeNum);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("兑换券兑换");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        etExchangeNum = (EditText) findViewById(R.id.et_exchange_num);
        etExchangeNum.setEnabled(false);
        ivMerchantPhoto = (ImageView) findViewById(R.id.iv_merchant_photo);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        btnCommit = (Button) findViewById(R.id.btn_commit);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    private void initData() {
        mMerchantId = getIntent().getStringExtra(TAG_MERCHANT_ID);
        getMerchantInfo();
    }

    @SuppressWarnings("unchecked")
    private void getMerchantInfo() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMerchantInfo("agent/agentQuanPay", mMerchantId)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantExchangeEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MerchantExchangeEntity merchantExchangeEntity) {
                        etExchangeNum.setEnabled(true);
                        GlideUtil.showCirImage(mContext, merchantExchangeEntity.merchantImg, ivMerchantPhoto);
                        tvMerchantName.setText(merchantExchangeEntity.merchantName);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_commit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        // validate
        String num = etExchangeNum.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            showToast("请输入兑换券数量");
            return;
        }

        // TODO validate success, do something
        if (hasPassword()) {
            if (payDialog == null) {
                payDialog = new PayDialog(mContext, this);
            }
            payDialog.show();
        } else {
            showSystemV7Dialog("您还未设置支付密码,请设置支付密码后再支付！", null,
                    "设置", "否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    startActivity(new Intent(mContext, InitialPayActivity.class));
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void passwordFinished(String psw) {
        String inputExchange = etExchangeNum.getText().toString().trim();

        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .payExchangeToMerchant("agent/agentQuanPayOrder",
                        mMerchantId,
                        new BigDecimal(inputExchange).multiply(new BigDecimal("100")).toBigInteger(),
                        psw)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<PayToMerchantExchangeEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(PayToMerchantExchangeEntity payToMerchantExchangeEntity) {
                        finish();
                        Intent intent = new Intent(mContext, PayExchangeToMerchantSuccessActivity.class);
                        intent.putExtra(PayExchangeToMerchantSuccessActivity.TAG, payToMerchantExchangeEntity);
                        startActivity(intent);
                    }
                });
        addSubscription(subscription);
    }
}
