package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;

import java.math.BigDecimal;

/**
 * 打卡失败弹窗
 * 每天提醒一次
 * @author ASUS
 */
public class ActClockInFailedDialogActivity extends BaseDialogActivity implements View.OnClickListener {
    private Button bntNoticeUp;
    private Button btnClose3;
    private TextView tvCaveIntegral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_act_clock_in_failed_dialog);
        initView();
        initListner();
    }

    private void initListner() {
        btnClose3.setOnClickListener(this);
        bntNoticeUp.setOnClickListener(this);
    }

    private void initView() {
        bntNoticeUp= (Button) findViewById(R.id.bnt_notice_up);
        btnClose3= (Button) findViewById(R.id.btn_close3);
        tvCaveIntegral= (TextView) findViewById(R.id.tv_carve_integral);
        String histotyIntegral=getIntent().getStringExtra("history_integral");
        tvCaveIntegral.setText(formatHistoryIntegral(histotyIntegral));
    }

    private Spannable  formatHistoryIntegral(String integral){
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        if (!TextUtils.isEmpty(integral)){
            BigDecimal bd = new BigDecimal(integral);
            integral = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));
            integral="起的早的人一共瓜分了"+integral;
            Spannable spannable=new SpannableString(integral);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FE8019")),integral.indexOf("了")+1,integral.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(18,true),integral.indexOf("了")+1,integral.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close3:
            case R.id.bnt_notice_up:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
