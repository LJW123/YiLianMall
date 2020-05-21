package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.yilian.networkingmodule.entity.V3MoneyDetailEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 提取益豆到钱包页面
 *
 * @author zhaiyaohua
 */
public class ExtractLeDouToPurseActivity extends BaseActivity {
    private ImageView v3Back;
    private TextView v3Title, tvRight;
    /**
     * 益豆提取数量
     */
    private EditText extractLeDouNumbers;
    /**
     * 益豆钱包地址
     */
    private EditText extractWallet;
    private TextView tvIntroduce;
    private Button extractBtnConfirm;
    private String leDou, wallet;
    private TextView tvServiceCharge, tvToAccount;

    private String allLeDou;
    private int rateRateAngel;
    private double angelConversionRatio;
    private TextView tvRatioRecommend;

    /**
     * 重新提取益豆
     *
     * @param savedInstanceState
     */
    private String id;
    /**
     * 是否是重新提取
     * true--修改钱包地址即重新提取
     * false--提取页面
     */
    private boolean isReExtract = false;
    private PopupWindow mPupWindow;
    private TextView tvNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_le_dou_to_purse);
        initView();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        Intent intent = getIntent();
        allLeDou = intent.getStringExtra("leDou");
        rateRateAngel = intent.getIntExtra("rate", 0);
        angelConversionRatio = getIntent().getDoubleExtra("angelConversionRatio", 0d);
        extractWallet = (EditText) findViewById(R.id.extract_wallet);


        tvRatioRecommend = findViewById(R.id.tv_ratio_recommend);
        v3Back = (ImageView) findViewById(com.yilianmall.merchant.R.id.v3Back);
        v3Title = (TextView) findViewById(com.yilianmall.merchant.R.id.v3Title);
        v3Title.setText("提取到钱包地址");
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

        extractLeDouNumbers = (EditText) findViewById(R.id.extract_ledou_numbers);
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);

        extractBtnConfirm = (Button) findViewById(R.id.extract_btn_confirm);

        tvServiceCharge = findViewById(R.id.tv_serviceCharge);
        tvToAccount = findViewById(R.id.tv_toAccount);
        setReExtractData();
    }

    private void initListener() {
        extractLeDouNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double cash = 0;
                double serviceCharge = 0;
                double toAccount = 0;
                if (!TextUtils.isEmpty(charSequence)) {
                    if (DecimalUtil.keep2DecimalByReturn(charSequence, extractLeDouNumbers)) {
                        BigDecimal num = new BigDecimal(charSequence.toString().trim());
                        BigDecimal val = new BigDecimal(allLeDou).divide(new BigDecimal("100"));
                        if (num.compareTo(val) > 0) {
                            extractLeDouNumbers.setText(val + "");
                            extractLeDouNumbers.setSelection(extractLeDouNumbers.getText().length());
                        } else {
                            cash = num.doubleValue();
                            //向上取整
                            serviceCharge = MyBigDecimal.div1(MyBigDecimal.mul(cash, (double) rateRateAngel), 100, 2);
                            //手续费
                            tvServiceCharge.setText(BigDecimal.valueOf(serviceCharge).toPlainString());
                            //向下取整
                            if (angelConversionRatio <= 0) {
                                tvToAccount.setText("比率获取异常");
                            } else {
                                toAccount = MyBigDecimal.sub(num.doubleValue(), serviceCharge);
                                tvToAccount.setText(new BigDecimal(Double.toString(toAccount)).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                            }
                        }
                    }
                } else {
                    //解决 删除输入数量时 展示信息错误
                    tvServiceCharge.setText(String.valueOf(serviceCharge));
                    tvToAccount.setText(String.valueOf(toAccount));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                leDou = extractLeDouNumbers.getText().toString().trim();
                wallet = extractWallet.getText().toString().trim();
                if (TextUtils.isEmpty(leDou)) {
                    showToast("请输入益豆提取数量");
                } else if (TextUtils.isEmpty(wallet)) {
                    showToast("请输入提取的钱包地址");
                } else {
                    showPopupWindow();

                }
            }
        });
    }

    /**
     * 设置传递过来的重新提交数据
     */
    private void setReExtractData() {
        Intent intent = getIntent();
        isReExtract = intent.getBooleanExtra("isReExtract", false);
        V3MoneyDetailEntity v3MoneyDetailEntity = intent.getParcelableExtra("data");
        if (isReExtract && v3MoneyDetailEntity != null) {
            String address = v3MoneyDetailEntity.address;
            id = v3MoneyDetailEntity.id;
            String amount = v3MoneyDetailEntity.amount;
//            2018年11月02日 产品提出需求 重新提交时 不再显示旧的地址
//            extractWallet.setText(address);
            String amounts = MyBigDecimal.div3(Double.parseDouble(amount), 100, BigDecimal.ROUND_DOWN, 2);
            extractLeDouNumbers.setText(amounts);
            tvToAccount.setText(amounts);
            extractLeDouNumbers.setEnabled(false);
            tvToAccount.setEnabled(false);
        }
    }

    /**
     * 二次确认弹窗
     */
    private void showPopupWindow() {
        if (mPupWindow == null) {
            View contentWindow = View.inflate(mContext, R.layout.layout_pup_risk_notice, null);
            mPupWindow = new PopupWindow(contentWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
            mPupWindow.setOutsideTouchable(true);
            TextView tvWriteAgain = contentWindow.findViewById(R.id.tv_write_again);
            TextView tvNoWrong = contentWindow.findViewById(R.id.tv_no_wrong);
            tvNotice = contentWindow.findViewById(R.id.tv_notice);
            RxUtil.clicks(tvWriteAgain, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    mPupWindow.dismiss();
                }
            });
            RxUtil.clicks(tvNoWrong, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    if (isReExtract) {
                        reExtractLeDou();
                    } else {
                        getData();
                    }
                }
            });
//            mPupWindow.setAnimationStyle(R.style.popup_window_anim);
        }
        String notice = "请确保您填写的益豆钱包地址：" + wallet + "是正确的钱包地址。若填写错误，将导致您的益豆丢失，请认真核对。";
        SpannableString spannableString = new SpannableString(notice);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#5D96FF"));
        int startIndex = notice.indexOf("：") + 1;
        int endIndex = startIndex + wallet.length();
        spannableString.setSpan(colorSpan, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNotice.setText(spannableString);
        mPupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 重新提取接口即修改钱包地址
     */
    @SuppressWarnings("unchecked")
    private void reExtractLeDou() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .reExtractLeDou("extract_bean_modify", id, wallet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {

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
                    public void onNext(HttpResultBean bean) {
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        showToast(bean.msg);
                        /**
                         *
                         * {@link com.yilian.mall.ui.BeansListActivity#getReExtractEvent(V3MoneyDetailEntity)}
                         * {@link com.yilian.mall.ui.V3MoneyDetailActivity#getReExtractEvent(V3MoneyDetailEntity)}
                         */
                        EventBus.getDefault().post(new V3MoneyDetailEntity(Parcel.obtain()));
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 提取益豆
     */
    @SuppressWarnings("unchecked")
    private void getData() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .extractLeDouToPurse("beanToBalance",
                        (new BigDecimal(leDou).multiply(new BigDecimal("100")).toPlainString()), wallet)
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
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    protected void onDestroy() {
        hidePop();
        super.onDestroy();
    }

    /**
     * 隐藏弹窗
     */
    private boolean hidePop() {
        if (null != mPupWindow && mPupWindow.isShowing()) {
            mPupWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!hidePop()) {
            super.onBackPressed();
        }
    }
}
