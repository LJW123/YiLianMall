package com.yilian.mall.shoppingcard.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BeansAdapter;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.ui.V3MoneyDetailActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.BeanEntity;
import com.yilian.networkingmodule.entity.shoppingcard.CardChangeDetailEntity;
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
import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/11/15 14:37
 * 购物卡明细页面
 */

public class CardDetailActivity extends BaseAppCompatActivity {

    /**
     * 筛选选择的key值
     */
    public static final String RESULT_TYPEKEY = "typeKey";
    private String date = "", type = "0";
    private TextView tvFiltrate;
    private ImageView v3Back;
    private RelativeLayout rlHead;
    private RecyclerView rvView;
    private TextView tvMonth;
    private TextView tvIncomeExpend;
    private LinearLayout stickyDatePickerLl;
    private SwipeRefreshLayout swipeRefresh;
    private BeansAdapter mAdapter;
    private int page = 0;
    private VaryViewUtils varyViewUtils;
    private LinearLayoutManager mManager;
    private List<BeanEntity.BeansData> mListBeans = new ArrayList<>();
    private View includeItemListTitile, emptyView;
    private BeanEntity.Statistics mStatistics;
    private List<String> timeList = new ArrayList<>();
    private int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new RefreshClickListener());
        tvFiltrate = (TextView) findViewById(R.id.tv_filtrate);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        rlHead = (RelativeLayout) findViewById(R.id.rl_head);
        rvView = (RecyclerView) findViewById(R.id.rv_view);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvIncomeExpend = (TextView) findViewById(R.id.tv_income_expend);
        stickyDatePickerLl = (LinearLayout) findViewById(R.id.sticky_date_picker_ll);
        stickyDatePickerLl.setVisibility(View.VISIBLE);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        includeItemListTitile = findViewById(R.id.include_item_list_titile);

        mManager = new LinearLayoutManager(mContext);
        rvView.setLayoutManager(mManager);
        mAdapter = new BeansAdapter(R.layout.item_beans_list);
        rvView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(rvView);
    }


    private void initData() {
        swipeRefresh.setRefreshing(true);
        page = 0;
        requestChangDetailList();
    }

    private void getFirstListData() {
        page = 0;
        type = "0";
        date = "";
        requestChangDetailList();
    }


    private void getNextListData() {
        page++;
        requestChangDetailList();
    }


    private void initListener() {
        //类型筛选
        RxUtil.clicks(tvFiltrate, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpCardActivityUtils.toCardTypeFiltrateActivity(CardDetailActivity.this, requestCode);
            }
        });

        //返回
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });

        RxUtil.clicks(stickyDatePickerLl, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showDataPicker();
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        }, rvView);

        // 滑动列表 改变头部
        rvView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mAdapter.getItemCount() > 0) {
                    int positon = mManager.findFirstVisibleItemPosition();
                    BeanEntity.BeansData beansData = mAdapter.getItem(positon);
                    if (beansData instanceof BeanEntity.Statistics) {
                        BeanEntity.Statistics statistics = (BeanEntity.Statistics) beansData;
                        tvMonth.setText(statistics.date);
                        tvIncomeExpend.setText(("收入" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(statistics.income) + " 支出" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(statistics.expend)));
                    } else if (beansData instanceof BeanEntity.DataList) {
                        BeanEntity.DataList dataList = (BeanEntity.DataList) mAdapter.getItem(positon);
                        tvMonth.setText(dataList.date);
                        tvIncomeExpend.setText(("收入" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(dataList.income) + " 支出" + Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(dataList.expend)));
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转购物卡明细详情
                BeanEntity.BeansData beansData = (BeanEntity.BeansData) adapter.getItem(position);
                if (beansData instanceof BeanEntity.DataList) {
                    BeanEntity.DataList dataList = (BeanEntity.DataList) beansData;
                    String img = dataList.img;
                    String orderId = dataList.order;
                    JumpCardActivityUtils.toV3MoneyDetailActivity(mContext, V3MoneyDetailActivity.CARD_DETAIL_INFO_TYPE, img, orderId);
                }
            }
        });


    }

    /**
     * 请求明细列表
     */
    private void requestChangDetailList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCardChangeDetail("card/user_card_change", page, Constants.PAGE_COUNT_20, date, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardChangeDetailEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                        swipeRefresh.setRefreshing(false);
                        if (page > 0) {
                            page--;
                        } else {
                            mListBeans.clear();
                            mAdapter.setNewData(mListBeans);
                            includeItemListTitile.setVisibility(View.INVISIBLE);
                            varyViewUtils.showErrorView();
                        }
                        mAdapter.loadMoreFail();
                        includeItemListTitile.setVisibility(View.INVISIBLE);
                    }


                    @Override
                    public void onNext(CardChangeDetailEntity entity) {
                        setDetailListDataToView(entity);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 明细列表
     *
     * @param entity
     */
    private void setDetailListDataToView(CardChangeDetailEntity entity) {
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
                    mStatistics = new BeanEntity.Statistics();
                    mStatistics.date = entity.statistics.get(i).date;
                    mStatistics.expend = entity.statistics.get(i).expend;
                    mStatistics.income = entity.statistics.get(i).income;
                    timeList.add(mStatistics.date);
                    dataList.add(mStatistics);
                    setListData(dataList,entity);

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
                mStatistics.date = entity.statistics.get(i).date;
                for (int j = 0; j < timeList.size(); j++) {
                    if (!timeList.contains(mStatistics.date)) {
                        dataList.add(mStatistics);
                        timeList.add(mStatistics.date);
                    }
                }
                setListData(dataList,entity);
            }
            mAdapter.addData(dataList);
            if (entity.list.size() < Constants.PAGE_COUNT_20) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
        varyViewUtils.showDataView();
    }

    private void setListData(List<BeanEntity.BeansData> dataList, CardChangeDetailEntity entity) {
        if (entity.list != null && entity.list.size() > 0) {
            for (int j = 0; j < entity.list.size(); j++) {
                if (mStatistics.date.equals(entity.list.get(j).date)) {
                    BeanEntity.DataList listData = new BeanEntity.DataList();
                    listData.income = mStatistics.income;
                    listData.expend = mStatistics.expend;
                    listData.id = entity.list.get(j).id;
                    listData.type = entity.list.get(j).type;
                    listData.amount = entity.list.get(j).amount;
                    listData.order = entity.list.get(j).order;
                    listData.time = entity.list.get(j).time;
                    listData.img = entity.list.get(j).img;
                    listData.date = entity.list.get(j).date;
                    listData.month = entity.list.get(j).month;
                    listData.year = entity.list.get(j).year;
                    listData.typeMsg = entity.list.get(j).type_msg;
                    dataList.add(listData);
                }
            }
        }
    }


    /**
     * 时间选择的滚轮
     */
    private void showDataPicker() {
        Calendar calendarStart = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarStart.set(Calendar.MONTH, 7 - 1);
        calendarStart.set(Calendar.YEAR, 2017);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendar.set(Calendar.MINUTE, 0);
        new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中时间回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                CardDetailActivity.this.date = format.format(date);
                startMyDialog();
                getFirstListData();
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(calendar)
                .setRangDate(calendarStart, calendar)
                .build()
                .show();
    }

    /**
     * 数据加载失败重新加载数据
     */
    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startMyDialog();
            getFirstListData();
        }
    }

    /**
     * 筛选完后重新请求数据
     *
     * @param requestCode
     * @param resultCode
     * @param data        从筛选页面取回来的 type 值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            if (data != null) {
                if (!StringUtils.isEmpty(data.getStringExtra(RESULT_TYPEKEY))) {
                    type = data.getStringExtra(RESULT_TYPEKEY);
                    page = 0;
                    requestChangDetailList();
                }
            }
        }
    }
}
