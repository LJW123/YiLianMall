package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;

import java.math.BigDecimal;

/**
 * 求赏通过
 * Created by ZYH on 2017/12/17 0017.
 */

public class ActPassRewardDialogActivity extends BaseDialogActivity implements View.OnClickListener {
    private TextView tvPassReward;
    private String reward;
    private Button bntLookReward;
    private Button btnClose3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_pass_reward_dialog);
        initView();
        initListner();
    }

    private void initListner() {
        btnClose3= (Button) findViewById(R.id.btn_close3);
        bntLookReward.setOnClickListener(this);
        btnClose3.setOnClickListener(this);
    }

    private void initView() {
        bntLookReward= (Button) findViewById(R.id.bnt_reward);
        tvPassReward= (TextView) findViewById(R.id.tv_come_integral);
        reward=getIntent().getStringExtra("reward");
        String content="你很幸运，得到"+reward+"奖券打赏";
        SpannableString spannableString=new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FE8019")),content.indexOf("到")+1,content.indexOf("积"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPassReward.setText(spannableString);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bnt_reward:
                //跳转到查看奖券明细的页面
                Intent intent = new Intent(mContext, V3MoneyListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_close3:
                finish();
                break;
                default:
                break;

        }

    }
}
