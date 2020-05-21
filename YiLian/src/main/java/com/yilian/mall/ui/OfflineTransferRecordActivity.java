package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.OfflineTransferRecordAdapter;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.OfflineTransferCardInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/9/4 0004.
 */

public class OfflineTransferRecordActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;

    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.iv_return_top)
    ImageView ivReturnTop;

    private String fromType;

    private int page = 0;
    OfflineTransferRecordAdapter adapter;

    private View emptyView, errorView, headView;
    private TextView tvRefresh, tvNoData;
    private boolean getFirstPageDataFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_transfer_record);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        fromType = getIntent().getStringExtra("from_type");
        if ("OfflineTransferVoucherActivity".equals(fromType)) {
            tvTitle.setText("选择银行卡");
        } else if ("OfflineTransferActivity".equals(fromType)) {
            tvTitle.setText("提交记录");
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvRight.setVisibility(View.GONE);

        emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
        tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
        tvNoData.setText("暂无更多数据");
        errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
        if (headView == null) {
            headView = View.inflate(mContext, R.layout.view_tv, null);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
//        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
//        recyclerView.addItemDecoration(decor);
        if ("OfflineTransferVoucherActivity".equals(fromType)) {
            adapter = new OfflineTransferRecordAdapter(R.layout.item_offline_transfer_history_record, "OfflineTransferVoucherActivity");
        } else if ("OfflineTransferActivity".equals(fromType)) {
            adapter = new OfflineTransferRecordAdapter(R.layout.item_offline_transfer_record, "OfflineTransferActivity");
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getData();
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                if (scrollDy > ScreenUtils.getScreenHeight(mContext) * 3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        ivReturnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OfflineTransferCardInfoEntity.DataBean data = (OfflineTransferCardInfoEntity.DataBean) adapter.getItem(position);
                Intent intent = null;
                switch (fromType) {
                    case "OfflineTransferVoucherActivity":
                        intent = new Intent();
                        intent.putExtra("card_name", data.transName);
                        intent.putExtra("card_num", data.transCard);
                        intent.putExtra("card_bank", data.transBank);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "OfflineTransferActivity":
                        intent = new Intent(mContext, OfflineTransferRecordDetailActivity.class);
                        intent.putExtra("id", data.transId);
                        startActivity(intent);
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getFirstPageDataFlag = true;
                getData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    private void getData() {
        if ("OfflineTransferVoucherActivity".equals(fromType)) {
            RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .cardInfo1("1", "", new Callback<OfflineTransferCardInfoEntity>() {
                        @Override
                        public void onResponse(Call<OfflineTransferCardInfoEntity> call, Response<OfflineTransferCardInfoEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    switch (body.code) {
                                        case 1:
                                            OfflineTransferCardInfoEntity offlineBody = response.body();
                                            ArrayList<OfflineTransferCardInfoEntity.DataBean> newList = offlineBody.data;
                                            if (null != newList && newList.size() > 0) {
                                                adapter.setNewData(newList);
                                            } else {
                                                if (page == 0) {
                                                    adapter.setEmptyView(emptyView);
                                                }
                                            }
                                            if (adapter.getHeaderLayoutCount() == 0) {
                                                adapter.addHeaderView(headView);
                                            }
                                            adapter.loadMoreEnd();//获取银行卡信息记录没有上拉加载
                                            break;
                                    }
                                }
                            }
                            netRequestEnd();
                        }

                        @Override
                        public void onFailure(Call<OfflineTransferCardInfoEntity> call, Throwable t) {
                            if (page == 0) {
                                adapter.setEmptyView(errorView);
                            } else if (page > 0) {
                                page--;
                            }
                            adapter.loadMoreFail();
                            netRequestEnd();
                            showToast(R.string.net_work_not_available);
                        }
                    });
        } else if ("OfflineTransferActivity".equals(fromType)) {
            RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .transferList(String.valueOf(page), "30", new Callback<OfflineTransferCardInfoEntity>() {
                        @Override
                        public void onResponse(Call<OfflineTransferCardInfoEntity> call, Response<OfflineTransferCardInfoEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    switch (body.code) {
                                        case 1:
                                            OfflineTransferCardInfoEntity offlineBody = response.body();
                                            ArrayList<OfflineTransferCardInfoEntity.DataBean> newList = offlineBody.data;
                                            if (null != newList && newList.size() > 0) {
                                                if (getFirstPageDataFlag) {
                                                    adapter.setNewData(newList);
                                                    getFirstPageDataFlag = false;
                                                } else {
                                                    adapter.addData(newList);
                                                }
                                                if (newList.size() < Constants.PAGE_COUNT) {
                                                    adapter.loadMoreEnd();
                                                } else {
                                                    adapter.loadMoreComplete();
                                                }
                                            } else {
                                                if (page == 0) {
                                                    adapter.setEmptyView(emptyView);
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                            netRequestEnd();
                        }

                        @Override
                        public void onFailure(Call<OfflineTransferCardInfoEntity> call, Throwable t) {
                            if (page == 0) {
                                adapter.setEmptyView(errorView);
                            } else if (page > 0) {
                                page--;
                            }
                            adapter.loadMoreFail();
                            netRequestEnd();
                            showToast(R.string.net_work_not_available);
                        }
                    });
        }
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        page++;
        getData();
    }
}
