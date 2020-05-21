package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.widget.ClearEditText;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.TakeLedouSuccessEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Ray_L_Pain
 * @date 2018/2/12 0012
 */

public class TakeLedouActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;
    @ViewInject(R.id.et)
    ClearEditText et;
    @ViewInject(R.id.tv_most)
    TextView tvMost;
    @ViewInject(R.id.et_address)
    ClearEditText etAddress;
    @ViewInject(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @ViewInject(R.id.tv_real_charge)
    TextView tvRealCharge;
    @ViewInject(R.id.check_box)
    CheckBox checkBox;
    @ViewInject(R.id.tv_to_web)
    TextView tvToWeb;
    @ViewInject(R.id.tv_ok)
    TextView tvOk;

    private String allLedou;
    private int rateLedou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_ledou);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        allLedou = getIntent().getStringExtra("ledou");
        rateLedou = getIntent().getIntExtra("rate", 0);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvTitle.setText("提取到交易平台");

        tvRight.setText("提取说明");
        tvRight.setTextColor(mContext.getResources().getColor(R.color.color_red));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.LEDOU_TAKE);
                startActivity(intent);
            }
        });

        tvMost.setText("最多可提现" + MoneyUtil.getLeXiangBiNoZero(allLedou) + Constants.APP_PLATFORM_DONATE_NAME);

        tvToWeb.setText(Html.fromHtml("<font color='#333333'>我已同意</font><font color='#F72D42'>《"+Constants.APP_PLATFORM_DONATE_NAME+"提取说明》</font>"));
        tvToWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.LEDOU_TAKE);
                startActivity(intent);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                take();
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double cash = 0;
                double serviceCharge = 0;
                double realCharge = 0;
                if (!TextUtils.isEmpty(et.getText().toString().trim())) {
                    cash = Double.parseDouble(et.getText().toString().trim());
                    serviceCharge = MyBigDecimal.div(MyBigDecimal.mul(cash, (double) rateLedou), 100, 2);
                    realCharge = MyBigDecimal.div(MyBigDecimal.mul(cash, (100 - (double) rateLedou)), 100, 2);
                }
                tvServiceCharge.setText(String.valueOf(serviceCharge));
                tvRealCharge.setText(String.valueOf(realCharge));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void take() {
        String cash = et.getText().toString().trim();
        if (TextUtils.isEmpty(cash)) {
            showToast("请输入您要提取的"+Constants.APP_PLATFORM_DONATE_NAME+"数量");
            return;
        }

        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            showToast("请输入钱包地址");
            return;
        }

        float cashF = Float.parseFloat(cash) * 100;

        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .takeBean("beanToBalance", String.valueOf(rateLedou), String.valueOf(cashF), etAddress.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TakeLedouSuccessEntity>() {
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
                    public void onNext(TakeLedouSuccessEntity entity) {
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        Intent intent = new Intent(mContext, TakeLedouSuccessActivity.class);
                        intent.putExtra("amount", entity.actualAmount);
                        startActivity(intent);
                    }
                });
        addSubscription(subscription);
    }
}
