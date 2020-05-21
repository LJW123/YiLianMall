package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.RecordListAdapter;
import com.yilian.mall.utils.JumpYlActivityUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.ExchangeTicketRecordEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Zg
 *         明细列表
 */
public class RecordListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 统计描述文字
     */
    public static String statisticsIncome, statisticsExpend;
    private ImageView v3Back;
    private TextView v3Title;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecordListAdapter mRecordListAdapter;
    private LinearLayout llStickyHeader;//吸顶 （顶部固定布局）
    private TextView tvStickyDate;//吸顶 - 年月
    private TextView tvStickyTotal;//吸顶 - 统计
    private LinearLayout llStickyDatePicker;//吸顶 - 时间选择
    private int page = Constants.PAGE_INDEX;//页数
    private String startTime = "0";// 筛选开始时间
    /**
     * 记录列表类型
     */
    private int recordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.merchant_v3back);
        v3Title = (TextView) findViewById(R.id.v3Title);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecordListAdapter = new RecordListAdapter();
        mRecordListAdapter.bindToRecyclerView(recyclerView);

        llStickyHeader = (LinearLayout) findViewById(R.id.ll_sticky_header);//吸顶 （顶部固定布局）
        tvStickyDate = (TextView) findViewById(R.id.tv_sticky_date);
        tvStickyTotal = (TextView) findViewById(R.id.tv_sticky_total);
        llStickyDatePicker = (LinearLayout) findViewById(R.id.ll_sticky_date_picker);
        llStickyDatePicker.setVisibility(View.VISIBLE);
    }

    private void initData() {
        recordType = getIntent().getIntExtra("recordType", -1);
        if (recordType == -1) {
            showToast("数据处理异常");
            return;
        }
        switch (recordType) {
            case RecordListRetention.EXCHANGE_MINE:
                v3Title.setText("兑换券明细");
                statisticsIncome = "获得兑换券";
                statisticsExpend = "使用兑换券";
                break;
            case RecordListRetention.EXCHANGE_VERIFICATION:
                v3Title.setText("待核销兑换券明细");
                statisticsIncome = "收取兑换券";
                statisticsExpend = "核销兑换券";
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                v3Title.setText("待提取天使明细");
                statisticsIncome = "产生天使";
                statisticsExpend = "提取天使";
                break;
            default:
                break;
        }
        swipeRefreshLayout.setRefreshing(true);
        getFirstPageData();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        v3Back.setOnClickListener(this);
        llStickyDatePicker.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                llStickyHeader.setVisibility(View.VISIBLE);
                View stickyInfoView = recyclerView.findChildViewUnder(llStickyHeader.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    switch (recordType) {
                        case RecordListRetention.EXCHANGE_MINE:
                        case RecordListRetention.EXCHANGE_VERIFICATION:
                        case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                            ExchangeTicketRecordEntity.ListBean mData = new Gson().fromJson(String.valueOf(stickyInfoView.getContentDescription()), ExchangeTicketRecordEntity.ListBean.class);
                            setFloatHeaderData(mData);
                            break;
                        default:
                            break;
                    }


                }
                View transInfoView = recyclerView.findChildViewUnder(llStickyHeader.getMeasuredWidth() / 2, llStickyHeader.getMeasuredHeight() + 1);
                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - llStickyHeader.getMeasuredHeight();
                    if (transViewStatus == RecordListAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            llStickyHeader.setTranslationY(dealtY);
                        } else {
                            llStickyHeader.setTranslationY(0);
                        }
                    } else if (transViewStatus == RecordListAdapter.NONE_STICKY_VIEW) {
                        llStickyHeader.setTranslationY(0);
                    }
                }
            }
        });
        mRecordListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
        mRecordListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case RecordListRetention.EXCHANGE_MINE:
                        ExchangeTicketRecordEntity.ListBean mData = (ExchangeTicketRecordEntity.ListBean) adapter.getItem(position);
                        if (mData.type == 101||mData.type== RecordListRetention.TYPE_105||mData.type== RecordListRetention.TYPE_4) {
                            MultiItemEntity multiItemEntity = (MultiItemEntity) adapter.getItem(position);
                            JumpYlActivityUtils.toRecordDetails(mContext, RecordListRetention.EXCHANGE_MINE, multiItemEntity);
                        }
                        break;
                    case RecordListRetention.EXCHANGE_VERIFICATION:
                        break;
                    case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                        MultiItemEntity multiItemEntity = (MultiItemEntity) adapter.getItem(position);
                        JumpYlActivityUtils.toRecordDetails(mContext, RecordListRetention.WAIT_EXTRACT_LE_ANGEL,multiItemEntity);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getFirstPageData() {
        page = Constants.PAGE_INDEX;
        getRecordListData();
    }

    /**
     * 加载记录列表 根据recordType
     */
    @SuppressWarnings("unchecked")
    private void getRecordListData() {
        Subscription subscription = null;
        switch (recordType) {
            case RecordListRetention.EXCHANGE_MINE:
            case RecordListRetention.EXCHANGE_VERIFICATION:
                subscription = getExchangeTicket();
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                subscription = getExtractAngel();
                break;
            default:
                break;
        }
        addSubscription(subscription);
    }

    /**
     * 兑换券收支明细
     *
     * @return
     */
    private Subscription getExchangeTicket() {
        //是否是服务中心明细 0不是（普通会员明细），1是
        String isAgency = "";
        switch (recordType) {
            case RecordListRetention.EXCHANGE_MINE:
                isAgency = "0";
                break;
            case RecordListRetention.EXCHANGE_VERIFICATION:
                isAgency = "1";
                break;
            default:
                break;
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getxchangeTicketRecord("quan/user_quan_change", page, Constants.PAGE_COUNT, startTime, isAgency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExchangeTicketRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == Constants.PAGE_INDEX) {
                            View errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
                            View tvRefresh = errorView.findViewById(R.id.tv_refresh);
                            RxUtil.clicks(tvRefresh, new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    getFirstPageData();
                                }
                            });
                            mRecordListAdapter.setEmptyView(errorView);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(ExchangeTicketRecordEntity entity) {
                        List<MultiItemEntity> mDatas = new ArrayList<>();
                        for (ExchangeTicketRecordEntity.ListBean record : entity.list) {
                            for (ExchangeTicketRecordEntity.StatisticsBean statistics : entity.statistics) {
                                if (record.date.equals(statistics.date)) {
                                    record.expend = statistics.expend;
                                    record.income = statistics.income;
                                    record.setItemType(recordType);
                                    mDatas.add(record);
                                }
                            }
                        }
                        if (page == Constants.PAGE_INDEX) {
                            if (mDatas.size() > 0) {
                                mRecordListAdapter.setNewData(mDatas);
                                setFloatHeaderData(mDatas.get(0));
                            } else {
                                ExchangeTicketRecordEntity.ListBean mData = new ExchangeTicketRecordEntity.ListBean();
                                if (TextUtils.isEmpty(startTime) || "0".equals(startTime)) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                                    mData.date = format.format(new Date());
                                } else {
                                    mData.date = startTime;
                                }
                                mData.income = "0";
                                mData.expend = "0";
                                setFloatHeaderData(mData);
                                mRecordListAdapter.setNewData(new ArrayList<MultiItemEntity>());
//                                mRecordListAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                                mRecordListAdapter.setEmptyView(R.layout.library_module_no_data);
                            }
                        } else {
                            mRecordListAdapter.addData(mDatas);
                        }
                        if (mDatas.size() < Constants.PAGE_COUNT_20) {
                            mRecordListAdapter.loadMoreEnd();
                        } else {
                            mRecordListAdapter.loadMoreComplete();
                        }
                    }
                });
        return subscription;
    }

    /**
     * 商家天使变更明细
     *
     * @return
     */
    private Subscription getExtractAngel() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getExtractAngelRecord("account/user_angel_change", page, Constants.PAGE_COUNT, startTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExchangeTicketRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == Constants.PAGE_INDEX) {
                            View errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
                            View tvRefresh = errorView.findViewById(R.id.tv_refresh);
                            RxUtil.clicks(tvRefresh, new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    getFirstPageData();
                                }
                            });
                            mRecordListAdapter.setEmptyView(errorView);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(ExchangeTicketRecordEntity entity) {
                        List<MultiItemEntity> mDatas = new ArrayList<>();
                        for (ExchangeTicketRecordEntity.ListBean record : entity.list) {
                            for (ExchangeTicketRecordEntity.StatisticsBean statistics : entity.statistics) {
                                if (record.date.equals(statistics.date)) {
                                    record.expend = statistics.expend;
                                    record.income = statistics.income;
                                    record.setItemType(recordType);
                                    mDatas.add(record);
                                }
                            }
                        }
                        if (page == Constants.PAGE_INDEX) {
                            if (mDatas.size() > 0) {
                                mRecordListAdapter.setNewData(mDatas);
                                setFloatHeaderData(mDatas.get(0));
                            } else {
                                ExchangeTicketRecordEntity.ListBean mData = new ExchangeTicketRecordEntity.ListBean();
                                if (TextUtils.isEmpty(startTime) || "0".equals(startTime)) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                                    mData.date = format.format(new Date());
                                } else {
                                    mData.date = startTime;
                                }
                                mData.income = "0";
                                mData.expend = "0";
                                setFloatHeaderData(mData);
                                mRecordListAdapter.setNewData(new ArrayList<MultiItemEntity>());
//                                mRecordListAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                                mRecordListAdapter.setEmptyView(R.layout.library_module_no_data);
                            }
                        } else {
                            mRecordListAdapter.addData(mDatas);
                        }
                        if (mDatas.size() < Constants.PAGE_COUNT_20) {
                            mRecordListAdapter.loadMoreEnd();
                        } else {
                            mRecordListAdapter.loadMoreComplete();
                        }
                    }
                });
        return subscription;
    }

    /**
     * 设置吸顶栏 数据
     */
    private void setFloatHeaderData(MultiItemEntity entity) {
        String[] str;
        switch (recordType) {
            case RecordListRetention.EXCHANGE_MINE:
            case RecordListRetention.EXCHANGE_VERIFICATION:
                ExchangeTicketRecordEntity.ListBean mData = (ExchangeTicketRecordEntity.ListBean) entity;
                str = mData.date.split("-");
                tvStickyDate.setText(str[0] + "年" + str[1] + "月");
                tvStickyTotal.setText(statisticsIncome + MoneyUtil.getLeXiangBiNoZero(mData.income) + "  " + statisticsExpend + MoneyUtil.getLeXiangBiNoZero(mData.expend));
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                mData = (ExchangeTicketRecordEntity.ListBean) entity;
                str = mData.date.split("-");
                tvStickyDate.setText(str[0] + "年" + str[1] + "月");
                tvStickyTotal.setText(statisticsIncome + mData.income + "  " + statisticsExpend + mData.expend);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.ll_sticky_date_picker:
                showDataPicker();
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
                startTime = format.format(date);
                startMyDialog();
                getFirstPageData();
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(calendar)
//                设置起止时间
                .setRangDate(calendarStart, calendar)
                .build()
                .show();
    }

    private void getNextPageData() {
        page++;
        getRecordListData();
    }
}
