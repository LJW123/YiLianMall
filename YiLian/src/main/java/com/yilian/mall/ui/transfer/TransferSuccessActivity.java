package com.yilian.mall.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;

public class TransferSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TransferSuccessActivity";
    public static final String TAG_TITLE = "TransferSuccessActivityTitle";
    public static final String TAG_AMOUNT = "TransferSuccessActivityAmount";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private ImageView ivSuccessIcon;
    private TextView tvTransferContent;
    private Button btnFinish;
    private TextView tvTransferAmount;
    private String transferAmount;
    private String transferContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_success);
        initView();
        initData();
    }

    private void initView() {
        tvTransferAmount = findViewById(R.id.tv_transfer_amount);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        ivSuccessIcon = (ImageView) findViewById(R.id.iv_success_icon);
        tvTransferContent = (TextView) findViewById(R.id.tv_transfer_content);
        btnFinish = (Button) findViewById(R.id.btn_finish);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        transferContent = intent.getStringExtra(TAG);
        tvTransferContent.setText(transferContent);
        transferAmount = intent.getStringExtra(TAG_AMOUNT);
        tvTransferAmount.setText(transferAmount);
        v3Title.setText(intent.getStringExtra(TAG_TITLE));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_finish:
                finish();
                break;
            default:
                break;
        }
    }
}
