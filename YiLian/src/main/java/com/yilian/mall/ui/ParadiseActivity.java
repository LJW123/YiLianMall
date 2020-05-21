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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ParadiseAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.ParadiseEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/10/12 0012.
 */

public class ParadiseActivity extends BaseActivity  {
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

    private View emptyView, errorView;
    private TextView tvRefresh, tvNoData;
    private int page = 0;
    private ParadiseAdapter adapter;
    private ArrayList<ParadiseEntity.DataBean.ListBean> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paradise);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvTitle.setText("奖券乐园");
        tvRight.setText("中奖纪录");

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
            tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
            tvNoData.setText("暂无更多数据");
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                }
            });
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ParadiseAdapter(R.layout.item_paradise);
            adapter.setFooterView(new View(mContext));
        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.PARADISE_RECORD);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
            .getActiviesLists(new Callback<ParadiseEntity>() {
                @Override
                public void onResponse(Call<ParadiseEntity> call, Response<ParadiseEntity> response) {
                    HttpResultBean body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                            ParadiseEntity entity = response.body();
                            switch (entity.code) {
                                case 1:
                                    ArrayList<ParadiseEntity.DataBean.ListBean> newList = entity.data.list;
                                    Logger.i("okhttp-" + newList.size());
                                    if (newList != null && newList.size() > 0) {
                                        adapter.setNewData(newList);
                                    } else {
                                        adapter.setEmptyView(emptyView);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    netRequestEnd();
                }

                @Override
                public void onFailure(Call<ParadiseEntity> call, Throwable t) {
                    if (page == 0) {
                        adapter.setEmptyView(errorView);
                    } else if (page > 0) {
                        page--;
                    }
                    adapter.loadMoreFail();
                    netRequestEnd();
                    showToast(R.string.merchant_module_service_exception);
                }
            });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }
}
