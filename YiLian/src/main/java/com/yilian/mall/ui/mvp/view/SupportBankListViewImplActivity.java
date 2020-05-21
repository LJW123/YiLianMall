package com.yilian.mall.ui.mvp.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.SupportBankListAdapter;
import com.yilian.mall.ui.mvp.presenter.ISupportBankListPresenter;
import com.yilian.mall.ui.mvp.presenter.SupportBankListPresenter;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.SupportBankListEntity;

import java.util.List;

import io.reactivex.functions.Consumer;

public class SupportBankListViewImplActivity extends BaseAppCompatActivity implements View.OnClickListener, ISupportBankListView {
    /**
     * 私有银行卡
     */
    public static final int PRIVATE_CARD = 0;
    /**
     * 对公银行卡
     */
    public static final int PUBLIC_CARD = 1;

    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ISupportBankListPresenter iSupportBankListPresenter;
    private SupportBankListAdapter supportBankListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_bank_list_view_impl);
        iSupportBankListPresenter = new SupportBankListPresenter(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getData();
    }

    @Override
    public void setData(SupportBankListEntity supportBankListEntity) {
        List<SupportBankListEntity.DataBean> data = supportBankListEntity.data;
        supportBankListAdapter.setNewData(data);
    }

    void getData() {
        iSupportBankListPresenter.getSupportBankList(mContext);
    }

    private void initListener() {
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(SupportBankListViewImplActivity.this, R.id.v3Shop);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getData();
            }
        });
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("支持身份证认证的个人卡");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.merchant_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        supportBankListAdapter = new SupportBankListAdapter(R.layout.item_support_bank_list);
        recyclerView.setAdapter(supportBankListAdapter);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
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
        }
    }

    @Override
    public int getType() {
        return PRIVATE_CARD;
    }
}
