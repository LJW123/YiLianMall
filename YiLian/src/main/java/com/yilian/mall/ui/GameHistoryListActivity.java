package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseHistoryEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.activity.BaseActivity;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class GameHistoryListActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int page = 0;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GameHistoryAdapter gameHistoryAdapter;
    private View emptyView;
    private View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history_list);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        gameHistoryAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getFirstPageData();
    }

    void getFirstPageData() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        getGameHistory();
    }

    void getNextPageData() {
        page++;
        getGameHistory();
    }

    @SuppressWarnings("unchecked")
    void getGameHistory() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getGameHistoryList("gameLogs", RequestOftenKey.getToken(mContext), RequestOftenKey.getDeviceIndex(mContext), String.valueOf(page), String.valueOf(Constants.PAGE_COUNT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseHistoryEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                        gameHistoryAdapter.loadMoreFail();
                        if (page > 0) {
                            page--;
                        } else {
                            if (errorView == null) {
                                errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
                            }
                            errorView.findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getFirstPageData();
                                }
                            });
                            gameHistoryAdapter.setEmptyView(errorView);
                        }
                    }

                    @Override
                    public void onNext(BaseHistoryEntity baseHistoryEntity) {
                        List<BaseHistoryEntity.DataBean> data = baseHistoryEntity.data;
                        if (page == 0) {
                            if (data.size() <= 0) {
                                if (emptyView == null) {
                                    emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
                                }
                                gameHistoryAdapter.setEmptyView(emptyView);
                            } else {
                                gameHistoryAdapter.setNewData(data);
                            }

                        } else {
                            gameHistoryAdapter.addData(data);
                        }
                        if (data.size() < Constants.PAGE_COUNT) {
                            gameHistoryAdapter.loadMoreEnd();
                        } else {
                            gameHistoryAdapter.loadMoreComplete();
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }

    class GameHistoryAdapter extends BaseQuickAdapter<BaseHistoryEntity.DataBean, com.chad.library.adapter.base.BaseViewHolder> {

        public GameHistoryAdapter(@LayoutRes int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, BaseHistoryEntity.DataBean item) {
            View includeTimeName = helper.getView(R.id.include_time_name);
            TextView tvTime = (TextView) includeTimeName.findViewById(R.id.tv_key);
            tvTime.setText(DateUtils.timeStampToStr(item.time));
            tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
            tvTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

            TextView tvGameName = (TextView) includeTimeName.findViewById(R.id.tv_value);
            tvGameName.setText(item.gameName);
            tvGameName.setVisibility(View.VISIBLE);
            tvGameName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tvGameName.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));

            View includeCostIntegral = helper.getView(R.id.include_cost_integral);
            TextView tvTitle1 = (TextView) includeCostIntegral.findViewById(R.id.tv_key);
            tvTitle1.setText("消耗奖券");
            tvTitle1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            tvTitle1.setTextColor(ContextCompat.getColor(mContext, R.color.color_666));

            TextView tvCostIntegral = (TextView) includeCostIntegral.findViewById(R.id.tv_value);
            tvCostIntegral.setText(MoneyUtil.getLeXiangBiNoZero(item.expendIntegral) + " 奖券");
            tvCostIntegral.setVisibility(View.VISIBLE);
            tvCostIntegral.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tvCostIntegral.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));


            View includeGetIntegral = helper.getView(R.id.include_get_integral);
            TextView tvTitle2 = (TextView) includeGetIntegral.findViewById(R.id.tv_key);
            tvTitle2.setText("获得奖券");
            tvTitle2.setTextColor(ContextCompat.getColor(mContext, R.color.color_666));
            tvTitle2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

            TextView tvGetIntegral = (TextView) includeGetIntegral.findViewById(R.id.tv_value);
            tvGetIntegral.setText(MoneyUtil.getLeXiangBiNoZero(item.gainIntegral) + " 奖券");
            tvGetIntegral.setVisibility(View.VISIBLE);
            tvGetIntegral.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tvGetIntegral.setTextColor(ContextCompat.getColor(mContext, R.color.color_666));



            View includeTotalIntegral = helper.getView(R.id.include_total_integral);
            TextView tvTitle3 = (TextView) includeTotalIntegral.findViewById(R.id.tv_key);
            tvTitle3.setText("最终战果");
            tvTitle3.setTextColor(ContextCompat.getColor(mContext, R.color.color_666));
            tvTitle3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

            TextView tvTotalIntegral = (TextView) includeTotalIntegral.findViewById(R.id.tv_value);
            long totalIntegral = item.gainIntegral - item.expendIntegral;
            if (totalIntegral > 0) {
                tvTotalIntegral.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
                tvTotalIntegral.setText("+" + MoneyUtil.getLeXiangBiNoZero(totalIntegral) + " 奖券");
            } else {
                tvTotalIntegral.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_18c99d));
                tvTotalIntegral.setText(MoneyUtil.getLeXiangBiNoZero(totalIntegral) + " 奖券");
            }
            tvTotalIntegral.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tvTotalIntegral.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("游戏记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        gameHistoryAdapter = new GameHistoryAdapter(R.layout.item_game_history);
        recyclerView.setAdapter(gameHistoryAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext,R.color.color_red));

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
        }
    }
}
