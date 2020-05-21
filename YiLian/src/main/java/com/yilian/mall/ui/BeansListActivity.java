package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BeansAdapter;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.networkingmodule.entity.BeanEntity;
import com.yilian.networkingmodule.entity.V3MoneyDetailEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 区块益豆明细页面
 *
 * @author zhaiyaohua on 2018/2/13 0013.
 */

public class BeansListActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRvView;
    private TextView mTvMonth;
    private TextView mTvIncomeExpend;
    private LinearLayout mStickyDatePickerLl;
    private ImageView v3Back;
    private BeansAdapter mAdapter;
    private List<BeanEntity.BeansData> mListBeans = new ArrayList<>();
    private String date = "";
    private int page = 0;
    private LinearLayoutManager mManager;
    private TextView titileView;
    private View errorView;
    private View tvRefresh;
    private ImageView v3Shop;
    private View includeItemListTitile;
    /**
     * 设置区块益豆数据
     */

    private BeanEntity.Statistics mStatistics;
    private View emptyView;
    private List<String> timeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_list);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initListner();
        initData();
    }

    private void initData() {
        mSwipeRefresh.setRefreshing(true);
        page = 0;
        getBeansListData();
    }

    private void initListner() {
        v3Shop.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        mStickyDatePickerLl.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转区块益豆详情页面
                BeanEntity.BeansData beansData = (BeanEntity.BeansData) adapter.getItem(position);
                if (beansData instanceof BeanEntity.DataList) {
                    BeanEntity.DataList dataList = (BeanEntity.DataList) beansData;
                    Intent intent = new Intent(mContext, V3MoneyDetailActivity.class);
                    intent.putExtra("orderId", dataList.id);
                    intent.putExtra("type", V3MoneyDetailActivity.TYPE_2);
                    startActivity(intent);
                }

            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstListData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextListData();
            }
        }, mRvView);

        mRvView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mAdapter.getItemCount() > 0) {
                    int positon = mManager.findFirstVisibleItemPosition();
                    BeanEntity.BeansData beansData = mAdapter.getItem(positon);
                    if (beansData instanceof BeanEntity.Statistics) {
                        BeanEntity.Statistics statistics = (BeanEntity.Statistics) beansData;
                        mTvMonth.setText(statistics.date);
                        mTvIncomeExpend.setText(("收入" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(statistics.income) + " 支出" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(statistics.expend)));
                    } else if (beansData instanceof BeanEntity.DataList) {
                        BeanEntity.DataList dataList = (BeanEntity.DataList) mAdapter.getItem(positon);
                        mTvMonth.setText(dataList.date);
                        mTvIncomeExpend.setText(("收入" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(dataList.income) + " 支出" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(dataList.expend)));
                    }
                }

            }
        });
    }

    private void initView() {
        includeItemListTitile = findViewById(R.id.include_item_list_titile);
        includeItemListTitile.setVisibility(View.VISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.merchant_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Back = findViewById(R.id.v3Back);
        titileView = findViewById(R.id.v3Title);
        titileView.setText(Constants.APP_PLATFORM_DONATE_NAME + "明细");
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRvView = (RecyclerView) findViewById(R.id.rv_view);
        mTvMonth = (TextView) findViewById(R.id.tv_month);
        mTvIncomeExpend = (TextView) findViewById(R.id.tv_income_expend);
        mStickyDatePickerLl = (LinearLayout) findViewById(R.id.sticky_date_picker_ll);
        mStickyDatePickerLl.setVisibility(View.VISIBLE);
        mSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mAdapter = new BeansAdapter(R.layout.item_beans_list);

        mManager = new LinearLayoutManager(mContext);

        mRvView.setLayoutManager(mManager);

        mRvView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.sticky_date_picker_ll:
                showDataPicker();
                break;
            case R.id.v3Shop:
                MenuUtil.showMenu(this, R.id.v3Shop);
                break;
            default:
                break;
        }
    }

    /**
     * 时间选择的滚轮
     */
    private void showDataPicker() {
//        开始时间限制2017年7月1日
        Calendar calendarStart = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarStart.set(Calendar.MONTH, 7 - 1);
        calendarStart.set(Calendar.YEAR, 2017);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
//        Calendar calendarEnd= Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
//        calendarEnd.set(Calendar.MONTH, 11);
//        calendarEnd.set(Calendar.YEAR,2037);
//        默认和截止时间限制 当天
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendar.set(Calendar.MINUTE, 0);
        //设置默认当前的时间
//        String title = "选择时间";

        new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中时间回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                BeansListActivity.this.date = format.format(date);
                startMyDialog();
                getFirstListData();
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(calendar)
                .setRangDate(calendarStart, calendar)//设置起止时间
                .build()
                .show();
    }

    /**
     * 获取区块益豆明细
     */
    @SuppressWarnings("unchecked")
    private void getBeansListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getBeansListData("user_bean_change", date, Constants.PAGE_COUNT_20, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeanEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        mSwipeRefresh.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                        mSwipeRefresh.setRefreshing(false);
                        if (page > 0) {
                            page--;
                        } else {
                            if (errorView == null) {
                                errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
                                tvRefresh = errorView.findViewById(R.id.tv_refresh);
                                tvRefresh.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startMyDialog();
                                        getFirstListData();
                                    }
                                });
                            }
                            mListBeans.clear();
                            mAdapter.setNewData(mListBeans);
                            includeItemListTitile.setVisibility(View.INVISIBLE);
                            mAdapter.setEmptyView(errorView);
                        }
                        mAdapter.loadMoreFail();


                    }

                    @Override
                    public void onNext(BeanEntity entity) {
                        setBeansData(entity);

                    }
                });
        addSubscription(subscription);
    }

    private void getFirstListData() {
        page = 0;
        getBeansListData();
    }

    private void getNextListData() {
        page++;
        getBeansListData();
    }

    private void setBeansData(BeanEntity entity) {
        includeItemListTitile.setVisibility(View.VISIBLE);
        if (page == 0) {
            timeList.clear();
            if (entity.list.size() <= 0) {
                mListBeans.clear();
                mAdapter.setNewData(mListBeans);
                if (emptyView == null) {
                    emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
                }
                mAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                mAdapter.loadMoreEnd();
            } else {
                List<BeanEntity.BeansData> dataList = new ArrayList<>();
                for (int i = 0; i < entity.statistics.size(); i++) {
                    mStatistics = entity.statistics.get(i);
                    timeList.add(mStatistics.date);
                    dataList.add(mStatistics);
                    for (BeanEntity.DataList listData : entity.list) {
                        if (mStatistics.date.equals(listData.date)) {
                            listData.income = mStatistics.income;
                            listData.expend = mStatistics.expend;
                            dataList.add(listData);
                        }
                    }
                }
                mAdapter.setNewData(dataList);
                if (entity.list.size() < Constants.PAGE_COUNT_20) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }

            }
        } else {

            List<BeanEntity.BeansData> dataList = new ArrayList<>();
            for (int i = 0; i < entity.statistics.size(); i++) {
                mStatistics = entity.statistics.get(i);
                for (int j = 0; j < timeList.size(); j++) {
                    if (!timeList.contains(mStatistics.date)) {
                        dataList.add(mStatistics);
                        timeList.add(mStatistics.date);
                    }
                }
                for (BeanEntity.DataList listData : entity.list) {
                    if (mStatistics.date.equals(listData.date)) {
                        listData.income = mStatistics.income;
                        listData.expend = mStatistics.expend;
                        dataList.add(listData);
                    }
                }
            }
            mAdapter.addData(dataList);
            if (entity.list.size() < Constants.PAGE_COUNT_20) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
    }

    /**
     * 修改提取益豆钱包地址成功
     * {@link ExtractLeDouToPurseActivity#reExtractLeDou()}
     */
    @Subscribe
    public void getReExtractEvent(V3MoneyDetailEntity v3MoneyDetailEntity) {
        finish();
    }
}
