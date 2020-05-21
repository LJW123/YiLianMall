package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.ExtractLeTaoAngleEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExtractLeTaoAngleActivity extends BaseActivity {
    private ImageView v3Back;
    private TextView v3Title, tvRight;
    private EditText extractLedouNumbers, extractWallet;//区块益豆提取数量，手机号码
    private TextView tvIntroduce;//兑换比例介绍
    private Button extractBtnConfirm;   //确认
    private String ledou, wallet;

    //                手续费          到账数量
    private TextView tvServiceCharge, tvToAccount;

    private String allLedou;
    private int rateRateAngel;
    private double angelConversionRatio;
    private TextView tvRatioRecommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_le_tao_angle);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        allLedou = getIntent().getStringExtra("ledou");
        rateRateAngel = getIntent().getIntExtra("rateRateAngel", 0);
        angelConversionRatio = getIntent().getDoubleExtra("angelConversionRatio", 0d);

        tvRatioRecommend = findViewById(R.id.tv_ratio_recommend);
        v3Back = (ImageView) findViewById(com.yilianmall.merchant.R.id.v3Back);
        v3Title = (TextView) findViewById(com.yilianmall.merchant.R.id.v3Title);
        v3Title.setText("提取到乐淘天使");
        tvRight = (TextView) findViewById(com.yilianmall.merchant.R.id.tv_right2);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提取说明");
        tvRight.setTextColor(Color.parseColor("#F22424"));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.LEDOU_TAKE);
                startActivity(intent);
            }
        });

        extractLedouNumbers = (EditText) findViewById(R.id.extract_ledou_numbers);
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
        double div = MyBigDecimal.div(angelConversionRatio, 100, 2);
        tvIntroduce.setText("每" + div + "个"+Constants.APP_PLATFORM_DONATE_NAME+"可兑换1个乐淘天使");
        tvRatioRecommend.setText("1.成功提交后，您的益联账户内的"+Constants.APP_PLATFORM_DONATE_NAME+"将以" + div + ":1的比例，提取至您的乐淘天使账号内。");
        extractWallet = (EditText) findViewById(R.id.extract_wallet);
        extractBtnConfirm = (Button) findViewById(R.id.extract_btn_confirm);

        tvServiceCharge = findViewById(R.id.tv_serviceCharge);
        tvToAccount = findViewById(R.id.tv_toAccount);
    }

    private void initData() {

    }

    private void initListener() {
        extractLedouNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(extractLedouNumbers.getText().toString().trim())) {
                    DecimalUtil.keep2Decimal(charSequence, extractLedouNumbers);
                    BigDecimal num = new BigDecimal(extractLedouNumbers.getText().toString().trim());
                    BigDecimal val =   new BigDecimal(allLedou).divide(new BigDecimal("100"));
                    if (num.compareTo(val) > 0) {
                        extractLedouNumbers.setText(val + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                double cash = 0;
                double serviceCharge = 0;
                double toAccount = 0;
                String trim = extractLedouNumbers.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    BigDecimal num = new BigDecimal(trim);
                    cash = Double.parseDouble(trim);
                    //向上取整
                    serviceCharge = MyBigDecimal.div1(MyBigDecimal.mul(cash, (double) rateRateAngel), 100, 2);
                    //手续费
                    tvServiceCharge.setText(BigDecimal.valueOf(serviceCharge).toPlainString());
                    //向下取整
                    if (angelConversionRatio <= 0) {
                        tvToAccount.setText("比率获取异常");
                    } else {
                        toAccount = MyBigDecimal.sub(num.doubleValue(), serviceCharge);
                        tvToAccount.setText(BigDecimal.valueOf(toAccount).toPlainString());
                    }

                } else {
                    //解决 删除输入数量时 展示信息错误
                    tvServiceCharge.setText(String.valueOf(serviceCharge));
                    tvToAccount.setText(String.valueOf(toAccount));
                }
            }
        });


        //返回键
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        //确认点击
        RxUtil.clicks(extractBtnConfirm, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                ledou = extractLedouNumbers.getText().toString().trim();
                wallet = extractWallet.getText().toString().trim();
                if (TextUtils.isEmpty(ledou)) {
                    showToast("请输入"+Constants.APP_PLATFORM_DONATE_NAME+"提取数量");
                } else if (TextUtils.isEmpty(wallet)) {
                    showToast("请输入提取的钱包地址");
                } else {
                    Log.i("TAG", "ledou:" + ledou + "  wallet:" + wallet);
                    getData();
                }
            }
        });
    }
@SuppressWarnings("unchecked")
    private void getData() {
    startMyDialog(false);
    Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .extractLeDou("beanToBalanceAngel",
                        String.valueOf(new BigDecimal(ledou).multiply(new BigDecimal("100"))), wallet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExtractLeTaoAngleEntity>() {

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
                    public void onNext(ExtractLeTaoAngleEntity extractLeTaoAngleEntity) {
                        String msg = extractLeTaoAngleEntity.msg;
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        showToast(msg);
                        Log.i("TAG", "ExtractLeTaoAngleEntity: msg-" + msg);
                        finish();
                    }
                });
        addSubscription(subscription);
    }


}
