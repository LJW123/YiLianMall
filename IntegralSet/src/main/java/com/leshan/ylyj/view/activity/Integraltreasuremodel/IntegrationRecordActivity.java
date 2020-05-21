package com.leshan.ylyj.view.activity.Integraltreasuremodel;

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
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.RecordAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.IntegralTreasurePresenter;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.IntegralRecordEntity;


/**
 * 集分宝明细
 */
public class IntegrationRecordActivity extends BaseActivity implements View.OnClickListener {
    private IntegralTreasurePresenter mPresent;
    private SwipeRefreshLayout swipe_refresh_layout;
    private LinearLayout sticky_header_ll;//吸顶 （顶部固定布局）
    private TextView sticky_date_tv;//吸顶 - 年月
    private TextView sticky_total1_tv;//吸顶 - 统计1
    private TextView sticky_total2_tv;//吸顶 - 统计2
    private LinearLayout unfold_ll;//
    private ImageView unfold_iv;
    private LinearLayout sticky_date_picker_ll;//吸顶 - 时间选择

    private RecyclerView record_rv;//记录列表
    private RecordAdapter recordAdapter;
    private List<IntegralRecordEntity.Record> mDatas;

    private int type = 0;//查询所需类型 0 全部  101 拆 奖励

    private String currentDate = "";//当前浮动 日期
    private int page = 0;//页数
    private int count = 20;//每页展示的内容数量
    private String startTime = "";// 筛选开始时间
    private Boolean isFiltrate = false;// 是否为筛选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);

        initToolbar();
        setToolbarTitle("集分宝明细");
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
        sticky_total1_tv = findViewById(R.id.sticky_total1_tv);
        sticky_total2_tv = findViewById(R.id.sticky_total2_tv);
        unfold_ll = findViewById(R.id.unfold_ll);
        unfold_iv = findViewById(R.id.unfold_iv);
        sticky_date_picker_ll = findViewById(R.id.sticky_date_picker_ll);
        sticky_date_picker_ll.setVisibility(View.VISIBLE);
        record_rv = findViewById(R.id.record_rv);
        record_rv.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter = new RecordAdapter();
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
                    IntegralRecordEntity.Record total = new Gson().fromJson(String.valueOf(stickyInfoView.getContentDescription()), IntegralRecordEntity.Record.class);
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

    /**
     * 设置漂浮头部数据
     */
    private Boolean isUnfold = false;

    private void setFloatHeaderData(final IntegralRecordEntity.Record total) {
        currentDate = total.getDate();
        sticky_date_tv.setText(total.getYear() + "年" + total.getMonth() + "月");
        sticky_total1_tv.setText("转入奖券 " + MoneyUtil.getTwoDecimalPlaces(total.getIncome()) + "   转出奖券 " + MoneyUtil.getTwoDecimalPlaces(total.getExpend()));
        sticky_total2_tv.setText("发奖励扣除奖券 " + MoneyUtil.getTwoDecimalPlaces(total.getRedExpend()));
        if (total.getUnfold()) {
            sticky_total2_tv.setVisibility(View.VISIBLE);
            unfold_iv.setImageResource(R.mipmap.arrows_up_gray);
        } else {
            sticky_total2_tv.setVisibility(View.GONE);
            unfold_iv.setImageResource(R.mipmap.arrows_down_gray);
        }
        isUnfold = total.getUnfold();
        unfold_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUnfold = !isUnfold;
                recordAdapter.unfold(total.getDate(), isUnfold);
                if (isUnfold) {
                    sticky_total2_tv.setVisibility(View.VISIBLE);
                    unfold_iv.setImageResource(R.mipmap.arrows_up_gray);
                } else {
                    sticky_total2_tv.setVisibility(View.GONE);
                    unfold_iv.setImageResource(R.mipmap.arrows_down_gray);
                }
            }
        });

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
        mPresent.getRecord(page, count, startTime, type);

    }

    private void getNextPageData() {
        page++;
        mPresent.getRecord(page, count, startTime, type);
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
        if (baseEntity instanceof IntegralRecordEntity) {
            IntegralRecordEntity entity = (IntegralRecordEntity) baseEntity;
            for (IntegralRecordEntity.Record record : entity.list) {
                for (IntegralRecordEntity.Statistics statistics : entity.statistics) {
                    if (record.getDate().equals(statistics.getDate())) {
                        record.setExpend(statistics.getExpend());
                        record.setIncome(statistics.getIncome());
                        record.setRedExpend(statistics.getRedExpend());
                    }
                }
                if (record.getDate().equals(currentDate) && isUnfold && !isFiltrate) {
                    record.setUnfold(true);
                } else {
                    record.setUnfold(false);
                }
            }
            mDatas = entity.list;
            if (page == 0) {
                if (isFiltrate) {
                    isFiltrate = false;
                    if (entity.statistics.size() > 0) {
                        IntegralRecordEntity.Statistics statistics = entity.statistics.get(0);
                        IntegralRecordEntity.Record record = new IntegralRecordEntity.Record();
                        String[] str = statistics.getDate().split("-");
                        record.setYear(str[0]);
                        record.setMonth(str[1]);
                        record.setIncome(statistics.getIncome());
                        record.setExpend(statistics.getExpend());
                        record.setRedExpend(statistics.getRedExpend());
                        record.setUnfold(false);
                        setFloatHeaderData(record);
                    } else {
                        IntegralRecordEntity.Record record = new IntegralRecordEntity.Record();
                        String[] str = startTime.split("-");
                        record.setYear(str[0]);
                        record.setMonth(str[1]);
                        record.setIncome("0");
                        record.setExpend("0");
                        record.setRedExpend("0");
                        record.setUnfold(false);
                        setFloatHeaderData(record);
                    }
                }
                if (mDatas.size() > 0) {
                    recordAdapter.setNewData(mDatas);
                } else {
                    Logger.i("获取记录走了这里");
//                    recordAdapter.notifyDataSetChanged();
                    recordAdapter.setNewData(new ArrayList<IntegralRecordEntity.Record>());
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
