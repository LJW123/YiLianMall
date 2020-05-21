package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.RecordExchangeAdapter;
import com.leshan.ylyj.presenter.IntegralTreasurePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.IntegralExchangeRecordEntity;


/**
 * 兑换明细
 */
public class IntegralRecordActivity extends BaseActivity implements View.OnClickListener {
    private IntegralTreasurePresenter mPresent;
    private SwipeRefreshLayout swipe_refresh_layout;
    private LinearLayout sticky_header_ll;//吸顶 （顶部固定布局）
    private TextView sticky_date_tv;//吸顶 - 年月
    private LinearLayout sticky_date_picker_ll;//吸顶 - 时间选择

    private RecyclerView record_rv;//记录列表
    private RecordExchangeAdapter recordAdapter;
    private List<IntegralExchangeRecordEntity.Data> mDatas;

    private String currentDate = "";//当前浮动 日期
    private int page = 0;//页数
    private int count = 20;//每页展示的内容数量
    private String startTime = "";// 筛选开始时间
    private Boolean isFiltrate = false;// 是否为筛选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integration_record);

        initToolbar();
        setToolbarTitle("累计兑换");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);//刷新控件
        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipe_refresh_layout.setProgressViewEndTarget(true, 500);
        sticky_header_ll = findViewById(R.id.sticky_header_ll);//吸顶 （顶部固定布局）
        sticky_date_tv = findViewById(R.id.sticky_date_tv);
        sticky_date_picker_ll = findViewById(R.id.sticky_date_picker_ll);
        sticky_date_picker_ll.setVisibility(View.VISIBLE);
        record_rv = findViewById(R.id.record_rv);
        record_rv.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter = new RecordExchangeAdapter();
        record_rv.setAdapter(recordAdapter);
    }

    @Override
    protected void initListener() {
        record_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                sticky_header_ll.setVisibility(View.VISIBLE);
                View stickyInfoView = recyclerView.findChildViewUnder(sticky_header_ll.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    IntegralExchangeRecordEntity.Data total = new Gson().fromJson(String.valueOf(stickyInfoView.getContentDescription()), IntegralExchangeRecordEntity.Data.class);
                    setFloatHeaderData(total);
                }

                View transInfoView = recyclerView.findChildViewUnder(sticky_header_ll.getMeasuredWidth() / 2, sticky_header_ll.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {

                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - sticky_header_ll.getMeasuredHeight();

                    if (transViewStatus == recordAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            sticky_header_ll.setTranslationY(dealtY);
                        } else {
                            sticky_header_ll.setTranslationY(0);
                        }
                    } else if (transViewStatus == recordAdapter.NONE_STICKY_VIEW) {
                        sticky_header_ll.setTranslationY(0);
                    }
                }
            }
        });
        sticky_date_picker_ll.setOnClickListener(this);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });

        recordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, record_rv);
    }

    @Override
    protected void initData() {
        //绑定model网络请求所要回调的view控件
        mPresent = new IntegralTreasurePresenter(mContext, this);
        startMyDialog(false);
        getFirstPageData();
    }

    private void setFloatHeaderData(final IntegralExchangeRecordEntity.Data total) {
        currentDate = total.getDate();
        sticky_date_tv.setText(total.getYear() + "年" + total.getMonth() + "月");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sticky_date_picker_ll) {
            showDataPicker();
        } else {
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
                isFiltrate = true;
                getFirstPageData();
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


    private void getFirstPageData() {
        //执行网络请求发法
        page = 0;
        mPresent.getExchangeRecord(page, count, startTime);
    }

    private void getNextPageData() {
        page++;
        mPresent.getExchangeRecord(page, count, startTime);
    }


    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
        swipe_refresh_layout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable e) {
        if (page == 0) {
            View errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
            View tvRefresh = errorView.findViewById(R.id.tv_refresh);
            RxUtil.clicks(tvRefresh, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    getFirstPageData();
                }
            });
            sticky_header_ll.setVisibility(View.GONE);
            recordAdapter.setEmptyView(errorView);
        }
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
        swipe_refresh_layout.setRefreshing(false);
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof IntegralExchangeRecordEntity) {
            mDatas = ((IntegralExchangeRecordEntity) baseEntity).dataList;
            if (page == 0) {
                if (isFiltrate) {
                    isFiltrate = false;
                    if (mDatas.size() > 0) {
                        setFloatHeaderData(mDatas.get(0));
                    } else {
                        IntegralExchangeRecordEntity.Data entity = new IntegralExchangeRecordEntity.Data();
                        String[] str = startTime.split("-");
                        entity.setYear(str[0]);
                        entity.setMonth(str[1]);
                        setFloatHeaderData(entity);
                    }
                }
                if (mDatas.size() > 0) {
                    recordAdapter.setNewData(mDatas);
                } else {
                    recordAdapter.setNewData(new ArrayList<IntegralExchangeRecordEntity.Data>());
                    recordAdapter.setEmptyView(R.layout.library_module_no_data);
                }
            } else {
                recordAdapter.addData(mDatas);
            }
            if (mDatas.size() > 0 && mDatas.size() < count) {
                recordAdapter.loadMoreEnd();
            } else {
                recordAdapter.loadMoreComplete();
            }
            Logger.i("获取数据成功");
        } else {
            Logger.i("获取数据失败");
        }
    }
}
