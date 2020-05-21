package com.leshan.ylyj.view.activity.Integraltreasuremodel;

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
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RefreshPersonCenter;
import com.yilian.mylibrary.ToastUtil;

import java.math.BigDecimal;
import java.util.Formatter;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.IntegralStatusEntity;


/**
 * 转出
 */
public class TurnOutIntegralActivity extends BaseActivity implements View.OnClickListener {
    private IntegralTreasurePresenter mPresent;
    //转出奖券内容
    private EditText integral_num_et;
    private TextView integral_tv;//总数量
    private TextView balance_integral_tv;//剩余的奖券
    private TextView integral_all;//全部转出

    private Button submit_bt;//立即转出

    private String jfbIntegral;//集分宝奖券余额  单位分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_out_integral);
        initToolbar();
        setToolbarTitle("转出奖券");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {

        //转出奖券内容
        integral_num_et = findViewById(R.id.integral_num_et);
//        SpannableString ss = new SpannableString("填写要转出的奖券数量");
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20, true);
//        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        integral_num_et.setHint(new SpannedString(ss));
        integral_num_et.setHint("填写要转出的奖券数量");
        integral_num_et.setTextSize(20);
        integral_tv = findViewById(R.id.integral_tv);//总数量
        balance_integral_tv = findViewById(R.id.balance_integral_tv);//剩余的奖券
        integral_all = findViewById(R.id.integral_all);//全部转出


        submit_bt = findViewById(R.id.submit_bt);//立即转出
        submit_bt.setEnabled(false);

    }

    @Override
    protected void initListener() {
        integral_num_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    submit_bt.setEnabled(false);
                    integral_all.setText("全部转出");
                    balance_integral_tv.setText(jfbIntegral);
                    integral_num_et.setTextSize(20);
                } else {
                    // 输入的内容变化的监听
                    DecimalUtil.keep2Decimal(s, integral_num_et);
                    integral_num_et.setTextSize(35);
                    Double amount = Double.parseDouble(integral_num_et.getText().toString().trim());
                    if (amount == 0d) {
                        submit_bt.setEnabled(false);
                        integral_all.setText("全部转出");
                        balance_integral_tv.setText(jfbIntegral);
                    } else {
                        submit_bt.setEnabled(true);
                        if (amount > Double.parseDouble(jfbIntegral)) {
                            integral_num_et.setText(jfbIntegral);
                            integral_num_et.setSelection(integral_num_et.getText().toString().length());
                            balance_integral_tv.setText("0.00");
                            integral_all.setText("取消");
                        } else {
                            Double balance = Double.parseDouble(jfbIntegral) - amount;
                            balance_integral_tv.setText(new Formatter().format("%.2f", balance).toString());
                            integral_all.setText("全部转出");
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
        //转出奖券内容
        integral_all.setOnClickListener(this);
        //立即转出
        submit_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        jfbIntegral = getIntent().getExtras().getString("jfbIntegral");
        jfbIntegral = MoneyUtil.getTwoDecimalPlaces(jfbIntegral);//转换单位
        integral_tv.setText(jfbIntegral);
        balance_integral_tv.setText(jfbIntegral);

        mPresent = new IntegralTreasurePresenter(mContext, this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.integral_all) {
            if (integral_all.getText().equals("取消")) {
                integral_num_et.setText("");
                integral_all.setText("全部转出");
                balance_integral_tv.setText(jfbIntegral);
            } else {
                integral_num_et.setText(jfbIntegral);
                integral_num_et.setSelection(integral_num_et.getText().toString().length());
                integral_all.setText("取消");
                balance_integral_tv.setText("0.00");
            }
        } else if (i == R.id.submit_bt) {
            BigDecimal amount = new BigDecimal(integral_num_et.getText().toString().trim());
            amount = amount.multiply(BigDecimal.valueOf(100));
            startMyDialog(false);
            mPresent.setTurnOut(String.valueOf(amount));
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
            SkipUtils.toTurnOutIntegralState(this, ((IntegralStatusEntity) baseEntity).data);
            finish();
        } else {
            Logger.i("获取数据失败");
        }
    }
}
