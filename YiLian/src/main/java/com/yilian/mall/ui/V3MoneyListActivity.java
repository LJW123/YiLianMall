package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V3MoneyListAdapter;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.MoneyListEntity;
import com.yilian.networkingmodule.entity.V3MoneyListBaseData;
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
 * @author
 *         提取营收、奖励、奖券明细列表
 */
public class V3MoneyListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 跳转V3MoneyDetailActivity_With_Draw页面
     */
    public static final int TO_DETAILS_WITH_DRAW = 888;
    /**
     * 跳转筛选界面
     */
    private static final int REQUEST_SCREEN = 99;
    public Observer<MoneyListEntity> observable;
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
    private ImageView ivGoTop;
    private int page;
    /**
     * 0 奖励 1 奖券  108提取营收页面  3代购券
     */
    private int type;
    /**
     * 108 提取营收明细接口上传参数
     * 0  提取营收记录
     */
    private int screenType;
    /**
     * 筛选类型
     */
    private String filterType;
    /**
     * 筛选类型对应字符
     */
    private String screenTypeValue;
    /**
     * 将所有月份头部存储下来，用于滑动时，悬浮头部的数据适配
     */
    private ArrayList<MoneyListEntity.StatisticsBean> statisticsBeans = new ArrayList<>();
    private ArrayList<V3MoneyListBaseData> v3MoneyListData = new ArrayList<>();
    private V3MoneyListAdapter v3MoneyListAdapter;
    private TextView tvSelectScreenType;
    private TextView tvSelectTime;
    private TextView tvExpenditure;
    private TextView tvIncome;
    private LinearLayout llExpend;
    private LinearLayout llScreen;
    private TextView tvMonth;
    private TextView tvIncomeExpend;
    private View includeFloatTitle;
    /**
     * 月份选择
     */
    private LinearLayout sticky_date_picker_ll;
    /**
     * 时间条件
     */
    private String startTime = "";
    /**
     * 每次请求数据列表，最后一条的月份、年份，用于和下一页第一条数据的月份、年份做对比，
     * 如果相同，则下一页第一条不会显示条目头部数据（月份、收入、支出），
     * 如果不同则显示
     */
    private int lastPositionMonth, lastPositionYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v3_money_list);
        StatusBarUtil.setColor(this, Color.WHITE);
        type = getIntent().getIntExtra("type", 0);
        screenType = getIntent().getIntExtra("screen_type", 0);
        initView();
        initData();
        initListener();
    }

    public void jumpToScreenActivity() {
        Intent intent = new Intent(mContext, BalanceScreenActivity1.class);
        intent.putExtra("type", String.valueOf(type));
        //传递数据为了下个界面有选择过筛选条件的显示状态，没有就为空
        intent.putExtra("screenType", filterType);
        intent.putExtra("screenTypeValue", screenTypeValue);
        startActivityForResult(intent, REQUEST_SCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("RequestCode  " + requestCode + "  resultCode  " + resultCode);
        switch (requestCode) {
            case REQUEST_SCREEN:
                if (resultCode == RESULT_OK) {
//                    startTime = data.getStringExtra("startTime");
//                    endTime = data.getStringExtra("endTime");
                    filterType = data.getStringExtra("screenType");
                    screenTypeValue = data.getStringExtra("screenTypeValue");
//                    Logger.i("RESULTCODE  starTime  " + startTime + "  endTIme  " + endTime + " screenType " + filterType + "  screenVALUE  " + screenTypeValue);
                    Logger.i("RESULTCODE  screenType " + filterType + "  screenVALUE  " + screenTypeValue);
                }
                getFirstPageData();
                break;
            case TO_DETAILS_WITH_DRAW:
                if (resultCode == RESULT_OK) {
                    getFirstPageData();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置头部筛选条件UI
     */
    private void setHeadFilterData() {
        if (TextUtils.isEmpty(screenTypeValue)) {
            tvSelectScreenType.setVisibility(View.GONE);
        } else {
            tvSelectScreenType.setVisibility(View.VISIBLE);
            tvSelectScreenType.setText(screenTypeValue);
        }
        tvSelectTime.setVisibility(View.GONE);
    }

    private void initListener() {
        v3MoneyListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //提取营收记录点击无效果
                if (type == Constants.TYPE_EXTRACT_REVENUE_108 && screenType == Constants.TYPE_EXTRACT_REVENUE_108) {
                    return;
                }
                Object item = adapter.getItem(position);
                if (item instanceof MoneyListEntity.ListBean) {
                    Intent intent;
                    MoneyListEntity.ListBean bean = (MoneyListEntity.ListBean) item;
                    if (bean.type == Constants.TYPE_EXTRACT_REVENUE_108) {
                        if (type == 0 || type == Constants.TYPE_EXTRACT_REVENUE_108) {
                            intent = new Intent(mContext, V3MoneyDetailActivity_With_Draw.class);
                        } else {
                            intent = new Intent(mContext, V3MoneyDetailActivity.class);
                        }
                    } else {
                        intent = new Intent(mContext, V3MoneyDetailActivity.class);
                    }
                    intent.putExtra("img", bean.img);
                    intent.putExtra("type", type);
                    intent.putExtra("orderId", bean.id);
                    startActivityForResult(intent, TO_DETAILS_WITH_DRAW);
                }
            }
        });
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(V3MoneyListActivity.this, R.id.v3Shop);
            }
        });
        RxUtil.clicks(tvRight, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpToScreenActivity();
            }
        });
        RxUtil.clicks(ivGoTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        // 根据滑动距离返回顶部按钮显示隐藏
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();

            }
        });
        v3MoneyListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
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
            Object item = v3MoneyListAdapter.getItem(firstVisibleItemPosition);
            String thisMonth = String.valueOf(com.yilian.mall.utils.DateUtils.getMonth());
            String thisYear = String.valueOf(com.yilian.mall.utils.DateUtils.getYear());
            if (thisMonth.length() < 2) {
                thisMonth = "0" + thisMonth;
            }
            String sign = "奖券";
            if (type == 0) {
                //0 奖励 1 奖券
                sign = "奖励 ¥";
            } else if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
                if (screenType == Constants.TYPE_EXTRACT_REVENUE_108) {
                    sign = "营收金额 ¥";
                } else {
                    sign = "营收额 ¥";
                }
            }
            if (item instanceof MoneyListEntity.ListBean) {
                MoneyListEntity.ListBean item1 = (MoneyListEntity.ListBean) item;
                for (int i = 0; i < statisticsBeans.size(); i++) {
                    MoneyListEntity.StatisticsBean statisticsBean = statisticsBeans.get(i);
                    if (item1.date.equals(statisticsBean.date)) {
                        String[] split = item1.date.split("-");
                        String year = split[0];
                        String month = split[1];
//                        if (year.equals(thisYear)) {
//                            if (month.equals(thisMonth)) {
//                                tvMonth.setText("本月");
//                            } else {
//                                tvMonth.setText(month + "月");
//                            }
//                        } else {
                        tvMonth.setText(year + "年" + month + "月");
//                        }
                        setFloatHeaderData(sign, statisticsBean.expend, statisticsBean.income);
                        includeFloatTitle.setVisibility(View.VISIBLE);
                    }
                }
            } else if (item instanceof MoneyListEntity.StatisticsBean) {
                MoneyListEntity.StatisticsBean item1 = (MoneyListEntity.StatisticsBean) item;
                String[] split = item1.date.split("-");
                String year = split[0];
                String month = split[1];
//                if (year.equals(thisYear)) {
//                    if (month.equals(thisMonth)) {
//                        tvMonth.setText("本月");
//                    } else {
//                        tvMonth.setText(month + "月");
//                    }
//                } else {
                tvMonth.setText(year + "年" + month + "月");
//                }
                setFloatHeaderData(sign, item1.expend, item1.income);
                includeFloatTitle.setVisibility(View.VISIBLE);
            } else {
                int year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                int month = Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
                tvMonth.setText(year + "年" + month + "月");
                setFloatHeaderData(sign, "0", "0");
            }
        }
    }

    private void setFloatHeaderData(String sign, String expend, String income) {
        if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
            if (screenType == Constants.TYPE_EXTRACT_REVENUE_108) {
                tvIncomeExpend.setText("提取" + sign + MoneyUtil.getLeXiangBiNoZero(expend));
            } else {
                tvIncomeExpend.setText("收入" + sign + MoneyUtil.getLeXiangBiNoZero(income) + " 支出" + sign + MoneyUtil.getLeXiangBiNoZero(expend));
            }
        } else if (type == V3MoneyDetailActivity.TYPE_3) {
            tvIncomeExpend.setText("获得代购券" + MoneyUtil.getLeXiangBiNoZero(income) + " 使用代购券" + MoneyUtil.getLeXiangBiNoZero(expend));
        } else {
            tvIncomeExpend.setText("收入" + sign + MoneyUtil.getLeXiangBiNoZero(income) + " 支出" + sign + MoneyUtil.getLeXiangBiNoZero(expend));
        }

    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getFirstPageData();
    }

    /**
     * type== Constants.TYPE_EXTRACT_REVENUE_108  提取营收列表，
     * screenType Constants.TYPE_EXTRACT_REVENUE_108  提取营收记录 其他提取营收明细
     * <p>
     * <p>
     * type！= Constants.TYPE_EXTRACT_REVENUE_108  获取奖励、奖券的变更记录列表数据
     */
    @SuppressWarnings("unchecked")
    private void getMoneyListData() {
        if (null == observable) {
            observable = new Observer<MoneyListEntity>() {
                @Override
                public void onCompleted() {
                    swipeRefreshLayout.setRefreshing(false);
                    stopMyDialog();
                }

                @Override
                public void onError(Throwable e) {
                    View errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
                    View tvRefresh = errorView.findViewById(R.id.tv_refresh);
                    RxUtil.clicks(tvRefresh, new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                            getFirstPageData();
                        }
                    });
                    v3MoneyListAdapter.setEmptyView(errorView);
                    includeFloatTitle.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    showToast(e.getMessage());
                    stopMyDialog();
                }

                @Override
                public void onNext(MoneyListEntity moneyListEntity) {
//                        setHeaderMoneyData(moneyListEntity);
                    List<MoneyListEntity.ListBean> list = moneyListEntity.list;
                    for (MoneyListEntity.ListBean o : list) {
                        o.setIsMoney(String.valueOf(type));
                    }
                    List<MoneyListEntity.StatisticsBean> statistics = moneyListEntity.statistics;
                    for (MoneyListEntity.StatisticsBean o : statistics) {
                        o.setIsMoney(String.valueOf(type));
                    }
                    if (page == 0) {
//                         清空数据
                        v3MoneyListAdapter.setNewData(new ArrayList());
                        if (list.size() <= 0) {
                            Logger.i("重新筛选");
                            v3MoneyListAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
//                                includeFloatTitle.setVisibility(View.INVISIBLE);
                            String thisMonth = String.valueOf(com.yilian.mall.utils.DateUtils.getMonth());
                            String thisYear = String.valueOf(com.yilian.mall.utils.DateUtils.getYear());
                            if (!TextUtils.isEmpty(startTime)) {
                                String[] split = startTime.split("-");
                                String year = split[0];
                                String month = split[1];
                                tvMonth.setText(year + "年" + month + "月");
                            }
//                                if (year.equals(thisYear)) {
//                                    if (month.equals(thisMonth)) {
//                                        tvMonth.setText("本月");
//                                    } else {
//                                        tvMonth.setText(month + "月");
//                                    }
//                                } else {
//                                tvMonth.setText(year + "年" + month + "月");
//                                }
                            if (type == 0) {//0 奖励 1 奖券
                                tvIncomeExpend.setText("收入奖励 ¥0  支出奖励 ¥0");
                            } else if (type == V3MoneyDetailActivity.TYPE_3) {
                                tvIncomeExpend.setText("获得代购券0 使用代购券0");
                            } else if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
                                if (screenType == Constants.TYPE_EXTRACT_REVENUE_0) {
                                    tvIncomeExpend.setText("收入营收额 ¥0  支出营收额 ¥0");
                                } else {
                                    tvIncomeExpend.setText("提取营收金额：¥0");
                                }
                            } else {
                                tvIncomeExpend.setText("收入奖券 0 支出奖券 0");
                            }
                        } else {
                            setLoadMoreStatus(list);
                            v3MoneyListAdapter.notifyDataSetChanged();
                            setItemData(list, statistics);
                            setFloatHeaderData();
                        }
                    } else {
                        setLoadMoreStatus(list);
                        setItemData(list, statistics);
                    }
                }

                /**
                 * 设置加载状态
                 * @param list
                 */
                private void setLoadMoreStatus(List<MoneyListEntity.ListBean> list) {
                    if (list.size() < Constants.PAGE_COUNT) {
                        v3MoneyListAdapter.loadMoreEnd();
                    } else {
                        v3MoneyListAdapter.loadMoreComplete();
                    }
                }

                /**
                 * 设置每条数据
                 * @param list
                 * @param statistics
                 */
                private void setItemData(List<MoneyListEntity.ListBean> list, List<MoneyListEntity.StatisticsBean> statistics) {
                    if (list == null || statistics == null || list.size() <= 0 || statistics.size() <= 0) {
                        return;
                    }
                    for (int position = 0; position < list.size(); position++) {
                        MoneyListEntity.ListBean listBean = list.get(position);
                        if (position > 0) {
                            MoneyListEntity.ListBean lastListBean = list.get(position - 1);
//                                当该条数据和上条数据月份不同时，需先添加一个月份头部
                            if (listBean.month != lastListBean.month || listBean.year != lastListBean.year) {
                                addHeader(statistics, listBean);
                            }
                        } else {
                            if (listBean.month != lastPositionMonth || listBean.year != lastPositionYear) {
                                addHeader(statistics, listBean);
                            }
                        }
                        v3MoneyListAdapter.addData(listBean);
                    }
//                        记录本次请求列表最后一条数据的月份
                    lastPositionMonth = list.get(list.size() - 1).month;
                    lastPositionYear = list.get(list.size() - 1).year;
                }

                /**
                 * 设置每条头部数据
                 * @param statistics
                 * @param listBean+
                 */
                private void addHeader(List<MoneyListEntity.StatisticsBean> statistics, MoneyListEntity.ListBean listBean) {
                    if (statistics == null || statistics.size() <= 0) {
                        return;
                    }
                    for (int j = 0; j < statistics.size(); j++) {
                        MoneyListEntity.StatisticsBean statisticsBean = statistics.get(j);
                        String month = String.valueOf(listBean.month);
                        if (month.length() < 2) {
                            month = "0" + month;
                        }
                        if (statisticsBean.date.split("-")[1].equals(month)) {
                            v3MoneyListAdapter.addData(statisticsBean);
                            statisticsBeans.add(statisticsBean);
                        }
                    }
                }
            };

        }
        Subscription subscription = null;
        if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
            subscription = RetrofitUtils3.getRetrofitService(mContext)
                    .getMerchantRevneueList("account/user_money_change", page, Constants.PAGE_COUNT, startTime, "" + screenType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observable);
        } else if (type == V3MoneyDetailActivity.TYPE_3) {
            //代购券明细
            subscription = RetrofitUtils3.getRetrofitService(mContext)
                    .getMerchantRevneueList("account/user_quan_change", page, Constants.PAGE_COUNT, startTime, "" + filterType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observable);
        } else {
            subscription = RetrofitUtils3.getRetrofitService(mContext)
                    .getMoneyListData("jfb/user_balance_change_v3", page, Constants.PAGE_COUNT, startTime, type, filterType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observable);
        }
        addSubscription(subscription);
    }

    /**
     * 设置头部金额数据
     *
     * @param moneyListEntity
     */
    private void setHeaderMoneyData(MoneyListEntity moneyListEntity) {
        if (!TextUtils.isEmpty(startTime)) {
            String expend = moneyListEntity.expend;
            String income = moneyListEntity.income;
            if (!TextUtils.isEmpty(expend)) {
                tvExpenditure.setText("支出：" + MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(expend)));
            }
            if (!TextUtils.isEmpty(income)) {
                tvIncome.setText("收入：" + MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(income)));
            }
            llExpend.setVisibility(View.VISIBLE);
        } else {
            llExpend.setVisibility(View.GONE);
        }
    }


    private void initView() {
        View viewHeader = findViewById(R.id.include_header);
        v3Title = (TextView) findViewById(R.id.v3Title);
        switch (type) {
            case 0:
                v3Title.setText("奖励明细");
                break;
            case 1:
                v3Title.setText("奖券明细");
                break;
            case 2:
                v3Title.setText(Constants.APP_PLATFORM_DONATE_NAME + "明细");
                break;
            case V3MoneyDetailActivity.TYPE_3:
                v3Title.setText("代购券明细");
                break;
            case 108:
                if (screenType == Constants.TYPE_EXTRACT_REVENUE_108) {
                    v3Title.setText("提取营收记录");
                } else {
                    v3Title.setText("明细");
                }
                break;
            default:
                break;
        }


        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setText("筛选");
        if (type != 3 && type != Constants.TYPE_EXTRACT_REVENUE_108) {
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.merchant_v3back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) viewHeader.findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        v3MoneyListAdapter = new V3MoneyListAdapter<>(v3MoneyListData, type);
        recyclerView.setAdapter(v3MoneyListAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        ivGoTop = (ImageView) findViewById(R.id.iv_go_top);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);

        tvSelectScreenType = (TextView) findViewById(R.id.tv_select_screen_type);
        tvSelectScreenType.setOnClickListener(this);
        tvSelectTime = (TextView) findViewById(R.id.tv_select_time);
        tvSelectTime.setOnClickListener(this);
        tvExpenditure = (TextView) findViewById(R.id.tv_expenditure);
        tvExpenditure.setOnClickListener(this);
        tvIncome = (TextView) findViewById(R.id.tv_income);
        tvIncome.setOnClickListener(this);
        llExpend = (LinearLayout) findViewById(R.id.ll_expend);
        llExpend.setOnClickListener(this);
        llScreen = (LinearLayout) findViewById(R.id.ll_screen);
        llScreen.setOnClickListener(this);
        includeFloatTitle = findViewById(R.id.include_float_title);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvMonth.setOnClickListener(this);
        sticky_date_picker_ll = (LinearLayout) findViewById(R.id.sticky_date_picker_ll);
        sticky_date_picker_ll.setVisibility(View.VISIBLE);
        sticky_date_picker_ll.setOnClickListener(this);
        tvIncomeExpend = (TextView) findViewById(R.id.tv_income_expend);
        tvIncomeExpend.setOnClickListener(this);
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
            case R.id.sticky_date_picker_ll:
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

    private void getFirstPageData() {
        setHeadFilterData();
        page = 0;
//      重置该值，防止刷新或筛选时，第一条月份显示不出来
        lastPositionMonth = 0;
        lastPositionYear = 0;
        getMoneyListData();
    }

    private void getNextPageData() {
        page++;
        getMoneyListData();
    }
}
