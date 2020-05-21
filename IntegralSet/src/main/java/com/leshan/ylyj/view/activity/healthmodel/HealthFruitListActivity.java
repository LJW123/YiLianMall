package com.leshan.ylyj.view.activity.healthmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.HealthFruitAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.HealthPresenter;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;

import java.util.List;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.HealthFruitListBean;

/**
 * 健康果列表
 *
 * @author Ray_L_Pain
 * @date 2018/1/17 0017
 */

public class HealthFruitListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView ivRight;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private HealthPresenter presenter;
    private View errorView;
    private TextView tvRefresh;
    private HealthFruitAdapter adapter;
    private int page = 0;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_fruit_list);
        presenter = new HealthPresenter(mContext, this, 0);

        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        userId = "8888888888810001";

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("健康果明细");
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData();
                }
            });
        }

        if (adapter == null) {
            adapter = new HealthFruitAdapter(R.layout.item_health_result_layout);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        recyclerView.setFocusableInTouchMode(false);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
    }

    @Override
    protected void initListener() {
        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    @Override
    protected void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
    }

    @Override
    public void onCompleted() {
        netRequestEnd();
    }

    @Override
    public void onError(Throwable e) {
        if (page == 0) {
            adapter.setEmptyView(errorView);
        } else if (page > 0) {
            page--;
        }
        adapter.loadMoreFail();
        netRequestEnd();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        if (baseEntity instanceof HealthFruitListBean) {
            HealthFruitListBean listBean = (HealthFruitListBean) baseEntity;

            List<HealthFruitListBean.DataBean.ListBean> newList = listBean.data.list;
            if (page <= 0) {
                adapter.setNewData(newList);
                if (newList.size() >= Constants.PAGE_COUNT) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }
            } else {
                if (newList.size() >= Constants.PAGE_COUNT) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }
                adapter.addData(newList);
            }
        }

        netRequestEnd();
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page ++;
        getData();
    }

    private void getData() {
        Logger.i("ray---666");
        presenter.getHealFruitList(String.valueOf(page), String.valueOf(Constants.PAGE_COUNT));
    }

    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }
}
