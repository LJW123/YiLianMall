package com.yilianmall.merchant.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.MerchantCashSuccessEntity;
import com.yilian.networkingmodule.entity.MerchantMyRevenueEntity;
import com.yilian.networkingmodule.entity.TakeCashEntity2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.PublicCardDetailsEntityV2;

/**
 * 提取营收页面
 *
 * @author Created by zhaiyaohua on 2018/4/26.
 */

public class MerchantExtractRevenueActivity extends BaseActivity {

    public static final String DOT = ".";
    /**
     * 最小提取金额
     */
    public static final int MIN_CASH = 100;
    /**
     * 输入数量最大保留位数
     */
    private static final int DIGITAL = 0;
    private TextView tvCardType;
    private LinearLayout llExtractRevenueExplain, llCard;
    private LinearLayout llAccountTime;
    private TextView tvExtractRevenue;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private EditText tvMoney;
    private TextView tvMaxNum;
    private TextView tvAllExtract;
    /**
     * 是否可以提取
     */
    private boolean isCanExtract = false;
    private double maxNum = 0;
    private boolean isFirst = true;
    private TakeCashEntity2 mCashEntity;
    private ImageView ivBankLogo;
    private LinearLayout llToEditCar;
    private String phone = "400-152-5159";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_extract_revenue);
        initView();
        initListener();
    }

    protected void initView() {
        StatusBarUtil.setColor(this, Color.WHITE, Constants.STATUS_BAR_ALPHA_60);
        llToEditCar = findViewById(R.id.ll_to_edit_card);
        ivBankLogo = findViewById(R.id.iv_logo_bank);
        tvMoney = findViewById(R.id.et_money);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvTitle.setText("提取营收");
        tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.merchant_color_333));
        tvRight.setText("提取营收记录");
        ivBack.setImageResource(R.mipmap.merchant_iv_back_black);
        tvRight.setVisibility(View.VISIBLE);

        tvAllExtract = findViewById(R.id.tv_all_extract);
        tvMaxNum = findViewById(R.id.tv_max_num);
        llCard = findViewById(R.id.ll_card);


        tvCardType = (TextView) findViewById(R.id.tv_card_type);
        llExtractRevenueExplain = (LinearLayout) findViewById(R.id.ll_extract_revenue_explain);
        llAccountTime = (LinearLayout) findViewById(R.id.ll_account_time);
        tvExtractRevenue = (TextView) findViewById(R.id.tv_extract_revenue);
        setAccountTimeFormat();
        setExplainFormat();
        setBntStatus();
    }

    protected void initListener() {
        RxUtil.clicks(llToEditCar, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (null != mCashEntity) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.bankmodel.EditPublicCardActivity"));
                    intent.putExtra("card_index", mCashEntity.info.cardIndex);
                    startActivity(intent);
                }
            }
        });
        RxUtil.clicks(tvExtractRevenue, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                takeCash();
            }
        });
        tvMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DecimalUtil.keepDecimal(s, tvMoney, DIGITAL);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String moneyString = s.toString().trim();
                if (TextUtils.isEmpty(moneyString)) {
                    isCanExtract = false;
                } else {
                    BigDecimal b1 = new BigDecimal(moneyString);
                    BigDecimal b2 = new BigDecimal(String.valueOf(MIN_CASH));
                    BigDecimal[] results = b1.divideAndRemainder(b2);
                    if (b1.compareTo(b2) >= 0 && results[1].compareTo(BigDecimal.valueOf(0)) == 0) {
                        isCanExtract = true;
                    } else {
                        isCanExtract = false;
                    }
                }
                setBntStatus();
                limiteDecimalDigital(tvMoney, moneyString, DIGITAL);
            }
        });
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        //提取营收记录
        RxUtil.clicks(tvRight, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent();
                intent.putExtra("type", Constants.TYPE_EXTRACT_REVENUE_108);
                intent.putExtra("screen_type", Constants.TYPE_EXTRACT_REVENUE_108);
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyListActivity"));
                startActivity(intent);
            }
        });
        RxUtil.clicks(tvAllExtract, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                String allNum = new BigDecimal(String.valueOf(maxNum)).setScale(DIGITAL, BigDecimal.ROUND_DOWN).toPlainString();
                tvMoney.setText(allNum);
                tvMoney.setSelection(allNum.length());
            }
        });

    }

    /**
     * 设置到账时间文案样式
     */
    private void setAccountTimeFormat() {
        String noteFirst = "1.工作日提现营收，T+1到银行卡上。";
        String noteSecond = "2.节假日提现营收，顺延节假日后第2 个工作日到账。";
        String noteThird = "3.如有疑问，请拨打" + phone + " 进行咨询。";
        TextView chirldFirst = setNoteView(noteFirst, llAccountTime);
        TextView chirldSecond = setNoteView(noteSecond, llAccountTime);
        TextView chirldThird = setNoteView(noteThird, llAccountTime);
        RxUtil.clicks(chirldThird, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                PhoneUtil.call(phone, mContext);
            }
        });
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.red));
        SpannableString spannableString = new SpannableString(noteFirst);
        int startIndex = noteFirst.indexOf("T");
        spannableString.setSpan(colorSpan, startIndex, startIndex + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        chirldFirst.setText(spannableString);


        spannableString = new SpannableString(noteSecond);
        startIndex = noteSecond.lastIndexOf("2");
        spannableString.setSpan(colorSpan, startIndex, startIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        chirldSecond.setText(spannableString);

        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#4D7BFE"));
        spannableString = new SpannableString(noteThird);
        startIndex = noteThird.indexOf("4");
        spannableString.setSpan(colorSpan1, startIndex, startIndex + 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        chirldThird.setText(spannableString);

    }

    /**
     * 设置营收说明文案和样式
     */
    private void setExplainFormat() {
        String explainStr = "1.每次提现营收金额≥100.00 ，提现营收金额必须是100 的整数倍。";
        TextView explain = setNoteView(explainStr, llExtractRevenueExplain);
        setNoteView("2.对于线上商家提现营收时，系统会保留最多不超过1000.00元的保证金作为商家的服务保证。", llExtractRevenueExplain);
        setNoteView("3.提现营收时平台不会扣除任何手续费。", llExtractRevenueExplain);
        SpannableString spannableString = new SpannableString(explainStr);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.red));
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.red));
        spannableString.setSpan(colorSpan, 10, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan1, 28, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        explain.setText(spannableString);
    }

    /**
     * 设置按钮状态
     */
    private void setBntStatus() {
        if (!isCanExtract) {
            tvExtractRevenue.setBackgroundResource(R.drawable.merchant_bg_solid_red50_90);
        } else {
            tvExtractRevenue.setBackgroundResource(R.drawable.merchant_bg_solid_red_90);
        }

    }

    private void takeCash() {
        String cash = tvMoney.getText().toString().trim();
        if (TextUtils.isEmpty(cash)) {
            showToast("请输入提取金额");
        } else {
            BigDecimal b1 = new BigDecimal(cash);
            if (b1.compareTo(BigDecimal.valueOf(MIN_CASH)) < 0) {
                if (!isCanExtract) {
                    showToast("提取金额不能低于100元");
                }
            } else if (b1.compareTo(BigDecimal.valueOf(MIN_CASH)) > 0) {
                if (!isCanExtract) {
                    showToast("提取金额必须为100的整数倍");
                }
            }
        }
        if (isCanExtract) {
            extractRevenue(cash);
        }
    }

    private void limiteDecimalDigital(EditText editText, String str, int digital) {
        if (!TextUtils.isEmpty(str)) {
            if (str.contains(DOT)) {
                str = 0 + str + 0;
            }
            BigDecimal bigDecimal = new BigDecimal(str);
            BigDecimal maxBigDecimal = new BigDecimal(String.valueOf(maxNum));
            if (bigDecimal.compareTo(maxBigDecimal) > 0) {
                String decimalNum = maxBigDecimal.toPlainString();
                editText.setText(decimalNum);
                editText.setSelection(decimalNum.length());
            } else {
                if (bigDecimal.scale() - 1 > digital) {
                    BigDecimal decimal = bigDecimal.setScale(digital, BigDecimal.ROUND_DOWN);
                    String decimalNum = decimal.toPlainString();
                    editText.setText(decimalNum);
                    editText.setSelection(decimalNum.length());
                }
            }
        }
    }

    /**
     * 设置提示文案
     *
     * @param explain
     * @param parentLayout
     */
    private TextView setNoteView(String explain, LinearLayout parentLayout) {
        TextView textView = new TextView(mContext);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.merchant_color_999));
        textView.setTextSize(12);
        textView.setText(explain);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        parentLayout.addView(textView);
        return textView;
    }

    /**
     * 提取营收接口
     *
     * @param cash
     */
    @SuppressWarnings("unchecked")
    private void extractRevenue(String cash) {
        startMyDialog();
        if (cash.contains(DOT)) {
            cash = "0" + cash + "0";
        }
        BigDecimal bigDecimal = new BigDecimal(cash);
        RetrofitUtils3.getRetrofitService(mContext).takeCash("cashToBalance_v2", bigDecimal.toPlainString(), mCashEntity.info.cardIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantCashSuccessEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();

                    }

                    @Override
                    public void onNext(MerchantCashSuccessEntity entity) {
                        if (1 == entity.code) {
                            EventBus.getDefault().post(new MerchantMyRevenueEntity());
                            Intent intent = new Intent(mContext, MerchantExtractSucActivity.class);
                            intent.putExtra("data", entity);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst && hasFocus) {
            getTakeCashDefaultBankCard();
            isFirst = false;
        }
    }

    /**
     * 新版获取默认提现银行卡
     */
    @SuppressWarnings("unchecked")
    private void getTakeCashDefaultBankCard() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getTakeCashDefaultCard("userBank/my_default_bank")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TakeCashEntity2>() {
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
                    public void onNext(TakeCashEntity2 takeCashEntity2) {
                        mCashEntity = takeCashEntity2;
                        String cash = MyBigDecimal.div3(Double.parseDouble(mCashEntity.availableCash), 100d, null, null);
                        maxNum = Double.parseDouble(cash);
                        tvMaxNum.setText("可提取金额" + MoneyUtil.add¥Front(cash));
                        tvCardType.setText(mCashEntity.info.cardBank + "尾号 " + mCashEntity.info.cardNumber);
                        GlideUtil.showImage(mContext, mCashEntity.info.bankLogo, ivBankLogo);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 编辑完银行卡后刷新银行卡信息
     * {@link com.yilian/com.leshan.ylyj.view.activity.bankmodel.EditPublicCardActivity#onNext(BaseEntity baseEntity)}
     *
     * @param entity
     */
    @Subscribe
    public void updateCarInfo(PublicCardDetailsEntityV2 entity) {
        getTakeCashDefaultBankCard();
    }
}
