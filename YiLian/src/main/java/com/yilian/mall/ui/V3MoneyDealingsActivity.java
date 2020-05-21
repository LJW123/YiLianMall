package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V3MoneyDealingsListAdapter;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.V3MoneyDealingsEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.ui.V3MemberGiftActivity.TYPE_DAI_GOU_QUAN;

/**
 * @author  来往记录
 */
public class V3MoneyDealingsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 奖券转赠 网络资源拼接路径
     */
    public static final String ACCOUNT_USER_INTEGRAL_GIVEN = "account/user_integral_given";
    /**
     * 代购券转赠
     */
    public static final String DAI_GOU_QUAN = "quan/user_quan_given";
    /**
     * 益豆转赠  网路资源拼接路径
     */
    public static final String ACCOUNT_USER_BEAN_GIVEN = "account/user_bean_given";
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvMonth;
    private TextView tvIncomeExpend;
    private ImageView ivGoTop;
    private String titleName;
    private String titlePhone;
    private int page;
    private V3MoneyDealingsListAdapter v3MoneyDealingsListAdapter;
    private ArrayList data = new ArrayList();
    private String id;
    private int type;
    private View includeFloatTitle;
    private String from;
    /**
     * 转赠列表类型
     * 0 奖券转赠
     * 1 益豆转赠
     */
    private int donationListType=V3MemberGiftActivity.TYPE_INTEGRAL;

    /**
     * 将所有月份头部存储下来，用于滑动时，悬浮头部的数据适配
     */
    private ArrayList<V3MoneyDealingsEntity.StatisticsBean> statisticsBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleName = getIntent().getStringExtra("titleName");
        titlePhone = getIntent().getStringExtra("titlePhone");
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        from = getIntent().getStringExtra("from");
        donationListType=getIntent().getIntExtra("donation_list_type",0);
        setContentView(R.layout.activity_v3_money_dealings);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initData();
        initListener();
    }

    /**
     * 设置漂浮头部数据
     */
    private void setFloatHeaderData() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (firstVisibleItemPosition < 0) {
                return;
            }
            Object item = v3MoneyDealingsListAdapter.getItem(firstVisibleItemPosition);
            if (item instanceof V3MoneyDealingsEntity.DataBean) {
                V3MoneyDealingsEntity.DataBean item1 = (V3MoneyDealingsEntity.DataBean) item;
                for (int i = 0; i < statisticsBeans.size(); i++) {
                    V3MoneyDealingsEntity.StatisticsBean statisticsBean = statisticsBeans.get(i);
                    if (item1.date.equals(statisticsBean.date)) {
                        String income=MoneyUtil.getLeXiangBiNoZero(statisticsBean.income);
                        String expend=MoneyUtil.getLeXiangBiNoZero(statisticsBean.expend);
                        if (donationListType==V3MemberGiftActivity.TYPE_STRESS){
                            income=V3MoneyDealingsListAdapter.decimalFormat.format(Double.valueOf(income));
                            expend=V3MoneyDealingsListAdapter.decimalFormat.format(Double.valueOf(expend));
                        }
                        tvMonth.setVisibility(View.GONE);
                        if (item1.month == DateUtils.getMonth()) {
                            tvIncomeExpend.setText("本月  收入: " +income + " 支出: " + expend);
                        } else {
                            tvIncomeExpend.setText(item1.month + "月  收入: " + income+ " 支出: " +expend);
                        }
                        tvIncomeExpend.setTextColor(Color.BLACK);
                        includeFloatTitle.setVisibility(View.VISIBLE);
                    }
                }
            } else if (item instanceof V3MoneyDealingsEntity.StatisticsBean) {
                V3MoneyDealingsEntity.StatisticsBean item1 = (V3MoneyDealingsEntity.StatisticsBean) item;
                String month = item1.date.split("-")[1];
                String income=MoneyUtil.getLeXiangBiNoZero(item1.income);
                String expend=MoneyUtil.getLeXiangBiNoZero(item1.expend);
                if (donationListType==V3MemberGiftActivity.TYPE_STRESS){
                    income=V3MoneyDealingsListAdapter.decimalFormat.format(Double.valueOf(income));
                    expend=V3MoneyDealingsListAdapter.decimalFormat.format(Double.valueOf(expend));
                }
                tvMonth.setVisibility(View.GONE);
                if (month.equals(String.valueOf(DateUtils.getMonth()))) {
                    tvIncomeExpend.setText("本月  收入: " +income + " 支出: " +expend);
                } else {
                    tvIncomeExpend.setText(month + "月  收入: " + income + " 支出: " + expend);
                }
                tvIncomeExpend.setTextColor(Color.BLACK);
                includeFloatTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initListener() {
        RxUtil.clicks(ivGoTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollDistance;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDistance += dy;
                Logger.i("recyclerView滑动距离:" + scrollDistance);
                if (scrollDistance > ScreenUtils.getScreenHeight(mContext) * 2) {
                    ivGoTop.setVisibility(View.VISIBLE);
                } else {
                    ivGoTop.setVisibility(View.GONE);
                }
                setFloatHeaderData();
            }
        });
        v3MoneyDealingsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(V3MoneyDealingsActivity.this, R.id.v3Shop);
            }
        });
    }

    private void initData() {
        getFirstPageData();
    }

    private void getFirstPageData() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
//      重置该值，防止刷新或筛选时，第一条月份显示不出来
        lastPositionDate = "";
        if (TextUtils.isEmpty(from)) {
            getV3MoneyDealingsListData();
        } else {
            getV3MoneyDealingsListDataFromGive();
        }
    }

    private void getNextPageData() {
        page++;
        if (TextUtils.isEmpty(from)) {
            getV3MoneyDealingsListData();
        } else {
            getV3MoneyDealingsListDataFromGive();
        }
    }

    /**
     * 每次请求数据列表，最后一条的月份，用于和下一页第一条数据的月份做对比，
     * 如果相同，则下一页第一条不会显示条目头部数据（月份、收入、支出），
     * 如果不同则显示
     */
    private String lastPositionDate = "";

    @SuppressWarnings("unchecked")
    private void getV3MoneyDealingsListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMoneyDealingsData("account/get_user_given", page, Constants.PAGE_COUNT, id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V3MoneyDealingsEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(V3MoneyDealingsEntity v3MoneyDealingsEntity) {
                        List<V3MoneyDealingsEntity.DataBean> list = v3MoneyDealingsEntity.data;
                        List<V3MoneyDealingsEntity.StatisticsBean> statistics = v3MoneyDealingsEntity.statistics;
                        if (page == 0) {
                            if (list == null || list.size() <= 0) {
                                v3MoneyDealingsListAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                            } else {
                                setLoadMoreStatus(list);
//                                清空数据
                                v3MoneyDealingsListAdapter.setNewData(new ArrayList());
                                setItemData(list, statistics);
                                setFloatHeaderData();
                            }
                        } else {
                            setLoadMoreStatus(list);
                            setItemData(list, statistics);
                        }

                    }

                    private void setItemData(List<V3MoneyDealingsEntity.DataBean> list, List<V3MoneyDealingsEntity.StatisticsBean> statistics) {
                        if (list == null || statistics == null || list.size() <= 0 || statistics.size() <= 0) {
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            V3MoneyDealingsEntity.DataBean dataBean = list.get(i);
                            if (i > 0) {
//                                    当该条数据和上条数据date不同时，需先添加一个月份头部
                                V3MoneyDealingsEntity.DataBean lastDataBean = list.get(i - 1);
                                if (!dataBean.date.equals(lastDataBean.date)) {
                                    addHeader(statistics, dataBean);
                                }
                            } else {
                                if (!dataBean.date.equals(lastPositionDate)) {
                                    addHeader(statistics, dataBean);
                                }
                            }
                            v3MoneyDealingsListAdapter.addData(dataBean);
                        }
//                        记录本次请求列表最后一条数据的月份
                        lastPositionDate = list.get(list.size() - 1).date;
                    }

                    private void setLoadMoreStatus(List<V3MoneyDealingsEntity.DataBean> list) {
                        if (list.size() < Constants.PAGE_COUNT) {
                            v3MoneyDealingsListAdapter.loadMoreEnd(true);
                            TextView footerView = new TextView(mContext);
                            footerView.setText("——我是有底线的——");
                            footerView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
                            footerView.setPadding(0, DPXUnitUtil.dp2px(mContext, 30), 0, DPXUnitUtil.dp2px(mContext, 30));
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                            footerView.setLayoutParams(layoutParams);
                            v3MoneyDealingsListAdapter.setFooterView(footerView);
                        } else {
                            v3MoneyDealingsListAdapter.loadMoreComplete();
                        }
                    }

                    private void addHeader(List<V3MoneyDealingsEntity.StatisticsBean> statistics, V3MoneyDealingsEntity.DataBean dataBean) {
                        if (statistics == null || statistics.size() <= 0) {
                            return;
                        }
                        for (int j = 0; j < statistics.size(); j++) {
                            V3MoneyDealingsEntity.StatisticsBean statisticsBean = statistics.get(j);
                            if (statisticsBean.date.equals(dataBean.date)) {
                                v3MoneyDealingsListAdapter.addData(statisticsBean);
                                statisticsBeans.add(statisticsBean);
                            }
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        includeFloatTitle = findViewById(R.id.include_float_title);
        v3Title = (TextView) findViewById(R.id.v3Title);
        if (TextUtils.isEmpty(titlePhone)) {
            v3Title.setText(TextUtils.isEmpty(titleName) ? "暂无昵称" : titleName);
        } else {
            titleName = TextUtils.isEmpty(titleName) ? "暂无昵称" : titleName;
            v3Title.setText(titleName + "\n(" + titlePhone + ")");
        }
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.VISIBLE);
        v3Shop.setImageResource(R.mipmap.merchant_v3_more_bottom);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.merchant_v3back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(Color.WHITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        v3MoneyDealingsListAdapter = new V3MoneyDealingsListAdapter(data);
        recyclerView.setAdapter(v3MoneyDealingsListAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvIncomeExpend = (TextView) findViewById(R.id.tv_income_expend);
        ivGoTop = (ImageView) findViewById(R.id.iv_go_top);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取转赠来往记录
     */
    @SuppressWarnings("unchecked")
    private void getV3MoneyDealingsListDataFromGive() {
        String c;
        if (donationListType==V3MemberGiftActivity.TYPE_INTEGRAL){
           c=ACCOUNT_USER_INTEGRAL_GIVEN;
        }else if(donationListType==TYPE_DAI_GOU_QUAN){
            c=DAI_GOU_QUAN;
        }else {
            c=ACCOUNT_USER_BEAN_GIVEN;
            v3MoneyDealingsListAdapter.setType(donationListType);
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMoneyDealingsDataFromGive(c, page, Constants.PAGE_COUNT, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V3MoneyDealingsEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(V3MoneyDealingsEntity v3MoneyDealingsEntity) {
                        List<V3MoneyDealingsEntity.DataBean> list = v3MoneyDealingsEntity.data;
                        List<V3MoneyDealingsEntity.StatisticsBean> statistics = v3MoneyDealingsEntity.statistics;
                        if (page == 0) {
                            if (list == null || list.size() <= 0) {
                                v3MoneyDealingsListAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                            } else {
                                setLoadMoreStatus(list);
//                                清空数据
                                v3MoneyDealingsListAdapter.setNewData(new ArrayList());
                                setItemData(list, statistics);
                                setFloatHeaderData();
                            }
                        } else {
                            setLoadMoreStatus(list);
                            setItemData(list, statistics);
                        }

                    }

                    private void setItemData(List<V3MoneyDealingsEntity.DataBean> list, List<V3MoneyDealingsEntity.StatisticsBean> statistics) {
                        if (list == null || statistics == null || list.size() <= 0 || statistics.size() <= 0) {
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            V3MoneyDealingsEntity.DataBean dataBean = list.get(i);
                            if (i > 0) {
//                                    当该条数据和上条数据月份不同时，需先添加一个月份头部
                                V3MoneyDealingsEntity.DataBean lastDataBean = list.get(i - 1);
                                if (!dataBean.date.equals(lastDataBean.date)) {
                                    addHeader(statistics, dataBean);
                                }
                            } else {
                                if (!dataBean.date.equals(lastPositionDate)) {
                                    addHeader(statistics, dataBean);
                                }
                            }
                            v3MoneyDealingsListAdapter.addData(dataBean);
                        }
//                        记录本次请求列表最后一条数据的月份
                        lastPositionDate = list.get(list.size() - 1).date;
                    }

                    private void setLoadMoreStatus(List<V3MoneyDealingsEntity.DataBean> list) {
                        if (list.size() < Constants.PAGE_COUNT) {
                            v3MoneyDealingsListAdapter.loadMoreEnd(true);
                            TextView footerView = new TextView(mContext);
                            footerView.setText("——我是有底线的——");
                            footerView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
                            footerView.setPadding(0, DPXUnitUtil.dp2px(mContext, 30), 0, DPXUnitUtil.dp2px(mContext, 30));
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                            footerView.setLayoutParams(layoutParams);
                            v3MoneyDealingsListAdapter.setFooterView(footerView);
                        } else {
                            v3MoneyDealingsListAdapter.loadMoreComplete();
                        }
                    }

                    private void addHeader(List<V3MoneyDealingsEntity.StatisticsBean> statistics, V3MoneyDealingsEntity.DataBean dataBean) {
                        if (statistics == null || statistics.size() <= 0) {
                            return;
                        }
                        for (int j = 0; j < statistics.size(); j++) {
                            V3MoneyDealingsEntity.StatisticsBean statisticsBean = statistics.get(j);
                            if (statisticsBean.date.equals(dataBean.date)) {
                                v3MoneyDealingsListAdapter.addData(statisticsBean);
                                statisticsBeans.add(statisticsBean);
                            }
                        }
                    }
                });
        addSubscription(subscription);
    }
}
