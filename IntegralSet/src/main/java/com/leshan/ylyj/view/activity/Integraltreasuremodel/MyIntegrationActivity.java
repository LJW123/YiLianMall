package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;


/**
 * 我的奖券
 */
public class MyIntegrationActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mBackIv;
    private ImageView mQuestionIv;
    private RelativeLayout mTitleRl;
    /**
     * 165434.00
     */
    private TextView mIntegralTv;
    /**
     * 待结算奖券
     */
    private TextView mWaitToPayTv;
    private LinearLayout mTitleBackLl;
    /**
     * 46545465.00
     */
    private TextView mFirstTv;
    /**
     * 46545465.00
     */
    private TextView mSecondTv;
    private ImageView mGame1;
    private ImageView mGame2;
    private ImageView mGame3;
    private LinearLayout mSendLl;
    private LinearLayout record_ll;

    private TextView explain_tv;//奖券说明
    private TextView question_tv;//常见问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_integration);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {

        mBackIv = (ImageView) findViewById(R.id.back_iv);
        mQuestionIv = (ImageView) findViewById(R.id.question_iv);
        mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        mIntegralTv = (TextView) findViewById(R.id.integral_tv);
        mWaitToPayTv = (TextView) findViewById(R.id.wait_to_pay_tv);
        mTitleBackLl = (LinearLayout) findViewById(R.id.title_back_ll);
        mFirstTv = (TextView) findViewById(R.id.first_tv);
        mSecondTv = (TextView) findViewById(R.id.second_tv);
        mGame1 = (ImageView) findViewById(R.id.game1);
        mGame2 = (ImageView) findViewById(R.id.game2);
        mGame3 = (ImageView) findViewById(R.id.game3);
        mSendLl = (LinearLayout) findViewById(R.id.send_ll);
        record_ll = (LinearLayout) findViewById(R.id.record_ll);
        explain_tv = (TextView) findViewById(R.id.explain_tv);
        question_tv = (TextView) findViewById(R.id.question_tv);
    }

    @Override
    protected void initListener() {
        mGame1.setOnClickListener(this);
        mGame2.setOnClickListener(this);
        mGame3.setOnClickListener(this);
        mSendLl.setOnClickListener(this);
        record_ll.setOnClickListener(this);
        explain_tv.setOnClickListener(this);
        question_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.game1) {//奖券小游戏
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.LOVE_DONATION);
            startActivity(intent);
        } else if (id == R.id.game2) {//奖券赚不停
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.GAIN_INTEGRAL);
            startActivity(intent);
        } else if (id == R.id.game3) {//奖券超值购
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.INTERGRAL_GO);
            startActivity(intent);
        } else if (id == R.id.send_ll) {//奖券转赠
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MemberGiftActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.INTEGRAL_SET_PROTOCOL);
            startActivity(intent);
        } else if (id == R.id.record_ll) {//奖券明细
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyListActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.INTEGRAL_SET_PROTOCOL);
            intent.putExtra("type", 1);
            startActivity(intent);
        } else if (id == R.id.explain_tv) {//奖券说明
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyListActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.INTEGRAL_AGREEMENT);
            startActivity(intent);
        } else if (id == R.id.question_tv) {//常见问题
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyListActivity"));
            intent.putExtra(Constants.SPKEY_URL, Ip.getWechatURL(mContext) + Constants.HELP_CENTER);
            startActivity(intent);
        }


    }
}
