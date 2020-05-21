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
import android.text.style.ForegroundColorSpan;
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
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.ToastUtil;

import java.math.BigDecimal;

import es.dmoral.toasty.Toasty;
import rxfamily.entity.BaseEntity;


/**
 * 设置保留奖券
 */
public class SetRetainedIntegraActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_right;//限额说明

    private EditText integral_num_et;//保留奖券数量
    private TextView hint_front_tv;//提示信息
    private TextView limit_integral_tv;//转入限额
    private Button submit_bt;//确认

    private IntegralTreasurePresenter mPresent;

    private int type = 0;//0 设置开启自动转入 1 编辑保留奖券
    private String jfbAutoTime = "";
    private String integralNum = "";

    private String limitExplainShow;// 限额说明  0不展示 1展示
    private String limitExplainUrl;//   限额说明H5链接地址
    private String isTransferLimit;//  1不限制转入 2限制转入
    private String jfbTransferLimit;// 集分宝转入限额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_retained_integra);
        initToolbar();
        setToolbarTitle("自动转入");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        toolbar_right = findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.GONE);

        integral_num_et = findViewById(R.id.integral_num_et);
//        SpannableString ss = new SpannableString("可设置0.00奖券");
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20, true);
//        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        integral_num_et.setHint(new SpannedString(ss));
        integral_num_et.setHint("请设置保留奖券数");
        integral_num_et.setTextSize(20);
        hint_front_tv = findViewById(R.id.hint_front_tv);
        limit_integral_tv = findViewById(R.id.limit_integral_tv);
        limit_integral_tv.setVisibility(View.GONE);

        submit_bt = findViewById(R.id.submit_bt);
        submit_bt.setEnabled(false);
    }

    @Override
    protected void initListener() {
        integral_num_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                DecimalUtil.keep2Decimal(s, integral_num_et);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length()>=8) {
//                    integral_num_et.setText(integralNumMax + "");
//                    showToast("保留奖券不能超过" + integralNumMax);
//                }
//                String strStart;
//                String strEnd;
                // 输入后的监听
                if (TextUtils.isEmpty(s)) {
//                    strStart = "每天" + jfbAutoTime + "点奖券余额";
//                    strEnd = "";
                    submit_bt.setEnabled(false);
                    integral_num_et.setTextSize(20);
                } else if (new BigDecimal(integral_num_et.getText().toString().trim()).compareTo(BigDecimal.valueOf(0)) == 0) {
//                    strStart = "每天" + jfbAutoTime + "点奖券余额";
//                    strEnd = "全部奖券";
                    submit_bt.setEnabled(true);
                    integral_num_et.setTextSize(35);
                } else {
//                    strStart = "每天" + jfbAutoTime + "点奖券中超过";
//                    strEnd = integral_num_et.getText().toString().trim() + "奖券";
                    submit_bt.setEnabled(true);
                    integral_num_et.setTextSize(35);
                }
//                SpannableString spannableString = new SpannableString(strStart + strEnd + "自动转入集分宝");
//                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff532a")), strStart.length(), spannableString.length() - 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                hint_front_tv.setText(spannableString);
            }
        });
        toolbar_right.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        type = getIntent().getExtras().getInt(" type");//0 设置开启自动转入 1 编辑保留奖券
        jfbAutoTime = getIntent().getExtras().getString("jfbAutoTime");//后台设置 集分宝自动转入时间
        limitExplainShow = getIntent().getExtras().getString("limitExplainShow");
        limitExplainUrl = getIntent().getExtras().getString("limitExplainUrl");
        isTransferLimit = getIntent().getExtras().getString("isTransferLimit");
        jfbTransferLimit = getIntent().getExtras().getString("jfbTransferLimit");

        if (limitExplainShow.equals("1"))//限额说明  0不展示 1展示
            toolbar_right.setVisibility(View.VISIBLE);
        if (isTransferLimit.equals("2")) {//  1不限制转入 2限制转入
            limit_integral_tv.setVisibility(View.VISIBLE);
            limit_integral_tv.setText("总额度" + MoneyUtil.getTwoDecimalPlaces(jfbTransferLimit) + "奖券");
        }
        hint_front_tv.setText("预计发奖励时间:次日" + jfbAutoTime + "之前（节假日顺延）");

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
        } else if (i == R.id.submit_bt) {
            //处理获取 输入的 保存奖券
            integralNum = integral_num_et.getText().toString().trim();
            if (integralNum.contains(".")) {
                String[] strList = integralNum.split(".");
                if (strList.length > 1) {
                    String str1 = strList[1];
                    if (str1.length() == 1) {
                        integralNum += "0";
                    }
                }
            } else {
                integralNum += ".00";
            }
            BigDecimal amount = new BigDecimal(integralNum);
            amount = amount.multiply(BigDecimal.valueOf(100));


            startMyDialog();
            //0 设置开启自动转入 1 编辑保留奖券
            if (type == 0) {
                //type 类型 0开启自动转入 1关闭自动转入 2编辑自动转入奖券金额
                mPresent.setRetainedIntegra("0", String.valueOf(amount));
            } else {
                //type 类型 0开启自动转入 1关闭自动转入 2编辑自动转入奖券金额
                mPresent.setRetainedIntegra("2", String.valueOf(amount));
            }
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
        if (baseEntity instanceof rxfamily.entity.BaseEntity) {
            Logger.i("获取数据成功");
//            SkipUtils.toSetIntegraNum(this, jfbAutoTime, integralNum, limitExplainShow, limitExplainUrl, isTransferLimit, jfbTransferLimit);
            AppManager.getInstance().killActivity(SetIntegraNumActivity.class);
            finish();
        } else {
            Logger.i("获取数据失败");
        }
    }
}
