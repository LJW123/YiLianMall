package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.IntegralTreasurePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RefreshPersonCenter;
import com.yilian.mylibrary.ToastUtil;

import java.math.BigDecimal;
import java.util.Formatter;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.IntegralStatusEntity;


/**
 * 转入
 */
public class ShiftToActivity extends BaseActivity implements View.OnClickListener {
    private IntegralTreasurePresenter mPresent;

    private TextView toolbar_right;//限额说明

    private EditText integral_num_et;//转入奖券
    private TextView integral_total_tv;//总数量
    private TextView limit_total_tv;//总额度
    private TextView balance_integral_tv;//剩余奖券
    private TextView integral_all_tv;//全部转入 or 取消
    private TextView expect_date_tv;//预期收益时间
    private Button submit_bt;//立即转出

    private String integral;// 用户奖券余额  单位分
    private String limitExplainShow;// 限额说明  0不展示 1展示
    private String limitExplainUrl;//   限额说明H5链接地址
    private String planGiveRed;//   预计发奖励时间（09-26）
    private String isTransferLimit;//  1不限制转入 2限制转入
    private String jfbTransferLimit;// 集分宝转入限额
    private String remainJfbIntegral;// 剩余可转入奖券

    /**
     * 当前用户最多可转入  （用户奖券余额，剩余可转入奖券 最小的一个）
     */
    private Double maxIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_to);
        initToolbar();
        setToolbarTitle("手动转入");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        toolbar_right = findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.GONE);

        integral_num_et = findViewById(R.id.integral_num_et);
        integral_total_tv = findViewById(R.id.integral_total_tv);
        limit_total_tv = findViewById(R.id.limit_total_tv);
        limit_total_tv.setVisibility(View.GONE);//隐藏
        balance_integral_tv = findViewById(R.id.balance_integral_tv);
        integral_all_tv = findViewById(R.id.integral_all_tv);
        expect_date_tv = findViewById(R.id.expect_date_tv);
        submit_bt = findViewById(R.id.submit_bt);//立即转出
        submit_bt.setEnabled(false);

    }

    @Override
    protected void initListener() {
        integral_num_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (TextUtils.isEmpty(s)) {
                    submit_bt.setEnabled(false);
                    integral_all_tv.setText("全部转入");
                    balance_integral_tv.setText("剩余奖券" + integral);
                    integral_num_et.setTextSize(20);
                } else {
                    // 输入的内容变化的监听
                    DecimalUtil.keep2Decimal(s, integral_num_et);
                    integral_num_et.setTextSize(35);
                    Double amount = Double.parseDouble(integral_num_et.getText().toString().trim());
                    if (amount == 0d) {
                        submit_bt.setEnabled(false);
                        integral_all_tv.setText("全部转入");
                        balance_integral_tv.setText("剩余奖券" + integral);
                    } else {
                        submit_bt.setEnabled(true);
                        if (amount > maxIntegral) {
                            integral_num_et.setText(new Formatter().format("%.2f", maxIntegral).toString());
                            integral_num_et.setSelection(integral_num_et.getText().toString().length());
                            Double balance = Double.parseDouble(integral) - maxIntegral;
                            balance_integral_tv.setText("剩余奖券" + new Formatter().format("%.2f", balance).toString());
                            integral_all_tv.setText("取消");
                        } else {
                            Double balance = Double.parseDouble(integral) - amount;
                            balance_integral_tv.setText("剩余奖券" + new Formatter().format("%.2f", balance).toString());
                            integral_all_tv.setText("全部转入");
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });
        toolbar_right.setOnClickListener(this);
        integral_all_tv.setOnClickListener(this);//全部转入 or 取消
        submit_bt.setOnClickListener(this);//立即转入
    }

    @Override
    protected void initData() {
        integral = MoneyUtil.getTwoDecimalPlaces(getIntent().getExtras().getString("integral"));
        limitExplainShow = getIntent().getExtras().getString("limitExplainShow");
        limitExplainUrl = getIntent().getExtras().getString("limitExplainUrl");
        planGiveRed = getIntent().getExtras().getString("planGiveRed");
        isTransferLimit = getIntent().getExtras().getString("isTransferLimit");
        jfbTransferLimit = MoneyUtil.getTwoDecimalPlaces(getIntent().getExtras().getString("jfbTransferLimit"));
        remainJfbIntegral = MoneyUtil.getTwoDecimalPlaces(getIntent().getExtras().getString("remainJfbIntegral"));

        if (limitExplainShow.equals("1"))//限额说明  0不展示 1展示
            toolbar_right.setVisibility(View.VISIBLE);

        integral_total_tv.setText("总数量" + integral);
        if (isTransferLimit.equals("2")) {//  1不限制转入 2限制转入
            limit_total_tv.setVisibility(View.VISIBLE);
            limit_total_tv.setText("总额度" + jfbTransferLimit);
//            SpannableString ss = new SpannableString("剩余可转入" + remainJfbIntegral);
//            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20, true);
//            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            integral_num_et.setHint(new SpannedString(ss));
            integral_num_et.setHint("剩余可转入" + remainJfbIntegral);
        } else {
//            SpannableString ss = new SpannableString("填写要转入的奖券数量");
//            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20, true);
//            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            integral_num_et.setHint(new SpannedString(ss));
            integral_num_et.setHint("填写要转入的奖券数量");
        }
        balance_integral_tv.setText("剩余奖券" + integral);
        expect_date_tv.setText(planGiveRed);
        /**
         * 获取 当前用户最多可转入
         */
        maxIntegral = Double.valueOf(integral);//最大可转 = 奖券余额
        if (isTransferLimit.equals("2")) // 2限制转入
            if (Double.valueOf(remainJfbIntegral) < Double.valueOf(integral))// 剩余可转 < 奖券余额
                maxIntegral = Double.valueOf(remainJfbIntegral);//最大可转 = 剩余可转

        mPresent = new IntegralTreasurePresenter(mContext, this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.toolbar_right) {//限额说明
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, limitExplainUrl);
            startActivity(intent);
        } else if (i == R.id.integral_all_tv) {
            if (integral_all_tv.getText().equals("取消")) {
                integral_num_et.setText("");
                balance_integral_tv.setText("剩余奖券" + integral);
                integral_all_tv.setText("全部转入");
            } else {
                integral_num_et.setText(new Formatter().format("%.2f", maxIntegral).toString());
                integral_num_et.setSelection(integral_num_et.getText().toString().length());
                Double balance = Double.parseDouble(integral) - maxIntegral;
                balance_integral_tv.setText("剩余奖券" + new Formatter().format("%.2f", balance).toString());
                integral_all_tv.setText("取消");
            }
        } else if (i == R.id.submit_bt) {
            BigDecimal amount = new BigDecimal(integral_num_et.getText().toString().trim());
            amount = amount.multiply(BigDecimal.valueOf(100));
            startMyDialog(false);
            mPresent.setShiftTo(String.valueOf(amount));
        } else {
        }
    }

    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof rxfamily.entity.IntegralStatusEntity) {
            Logger.i("获取数据成功");
            RefreshPersonCenter.refresh(mContext);
            SkipUtils.toShiftToState(this, ((IntegralStatusEntity) baseEntity).data);
            finish();
        } else {
            Logger.i("获取数据失败");
        }
    }
}
