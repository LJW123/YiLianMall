package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.widgets.NoScrollListView;

/**
 * 申请退款  该界面重复 已废弃
 */
public class MTApplyForRefundActivity extends BaseActivity {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private NoScrollListView nslvApplyRefundContent;
    private LinearLayout activityMtapplyForRefund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtapply_for_refund);
        initView();
    }

    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        nslvApplyRefundContent = (NoScrollListView) findViewById(R.id.nslv_apply_refund_content);
        activityMtapplyForRefund = (LinearLayout) findViewById(R.id.activity_mtapply_for_refund);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.apply_for_refund));
        ivRight2.setVisibility(View.GONE);
    }
}
