package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ActClockRuleDialogActivity extends BaseDialogActivity implements View.OnClickListener {


    private ImageView ivTitle1;
    private ImageView ivTitle2;
    private TextView tvShowDetailRule;
    private RelativeLayout rlContent;
    private Button btnClose2;
    private TextView tvOpenRewardNotice;
    private String applyIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_clock_rule_dialog);
        initView();
        initListener();

    }

    private void initListener() {
        RxUtil.clicks(tvShowDetailRule, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.ActClockRules);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        applyIntegral=getIntent().getStringExtra("applyIntegral");
        tvOpenRewardNotice= (TextView) findViewById(R.id.tv_open_rewardnotice);
        tvOpenRewardNotice.setText("1.每天"+applyIntegral+"奖券参与挑战\n"+"2.早上5:00-8:00打卡"+"\n3.按时间打卡，可随机瓜分当日全部奖励金");
        ivTitle1 = (ImageView) findViewById(R.id.iv_title1);
        ivTitle2 = (ImageView) findViewById(R.id.iv_title2);
        tvShowDetailRule = (TextView) findViewById(R.id.tv_show_detail_rule);
        rlContent = (RelativeLayout) findViewById(R.id.rl_content);
        btnClose2 = (Button) findViewById(R.id.btn_close2);

        btnClose2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close2:
                finish();
                break;
            default:
                break;
        }
    }
}
