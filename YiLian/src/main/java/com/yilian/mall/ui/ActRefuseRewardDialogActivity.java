package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;

/**
 *
 * 求赏被拒
 * Created by ZYH on 2017/12/17 0017.
 */

public class ActRefuseRewardDialogActivity extends BaseDialogActivity implements View.OnClickListener {
    private TextView tvDaka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_refuse_reward_dialog);
        initView();
        initData();
        initListner();
    }

    private void initData() {
        String content=tvDaka.getText().toString();
        SpannableString spannableString=new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF8019")),content.length()-6,content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDaka.setText(spannableString);
    }

    private void initListner() {
        tvDaka.setOnClickListener(this);
    }

    private void initView() {
        tvDaka= (TextView) findViewById(R.id.tv_come_integral);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_come_integral:
                finish();
                break;
        }
    }
}
