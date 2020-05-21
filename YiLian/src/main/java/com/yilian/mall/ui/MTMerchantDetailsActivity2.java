package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;

/**
 * 重构商家中心界面
 */
public class MTMerchantDetailsActivity2 extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private Button btnClickPraise;
    private Button btnMerchantGo;
    private LinearLayout layoutBottomMenu;
    private TextView tvRefresh;
    private LinearLayout llLoadError;
    private String merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtmerchant_details2);
        merchantId = getIntent().getStringExtra("merchant_id");
        initView();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        btnClickPraise = (Button) findViewById(R.id.btn_click_praise);
        btnMerchantGo = (Button) findViewById(R.id.btn_merchant_go);
        layoutBottomMenu = (LinearLayout) findViewById(R.id.layout_bottom_menu);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        llLoadError = (LinearLayout) findViewById(R.id.ll_load_error);

        tvRight.setOnClickListener(this);
        btnClickPraise.setOnClickListener(this);
        btnMerchantGo.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.btn_click_praise:

                break;
            case R.id.btn_merchant_go:

                break;
            case R.id.tv_refresh:

                break;
        }
    }
}
