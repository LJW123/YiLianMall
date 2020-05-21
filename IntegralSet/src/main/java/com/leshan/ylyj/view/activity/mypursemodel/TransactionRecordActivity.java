package com.leshan.ylyj.view.activity.mypursemodel;

import android.content.ComponentName;
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
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.RecordTransactionAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.TransactionRecordEntity;


/**
 * 交易记录
 */
public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {
    private BankPresenter mPresent;
    private ImageView credit_bill_img;//信用账单
    private SwipeRefreshLayout swipe_refresh_layout;
    private LinearLayout sticky_header_ll;//吸顶 （顶部固定布局）
    private TextView sticky_date_tv;//吸顶 - 年月
    private LinearLayout sticky_date_picker_ll;//吸顶 - 时间选择

    private RecyclerView record_rv;//记录列表
    private RecordTransactionAdapter recordAdapter;
    private List<TransactionRecordEntity.ListBean> mDatas;

    private int page = 0;//页数
    private int count = 20;//每页展示的内容数量
    private String startTime = "";// 筛选开始时间
    private Boolean isFiltrate = false;// 是否为筛选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);

        initToolbar();
        setToolbarTitle("交易记录");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        credit_bill_img = findViewById(R.id.credit_bill_img);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);//刷新控件
        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipe_refresh_layout.setProgressViewEndTarget(true, 500);
        sticky_header_ll = findViewById(R.id.sticky_header_ll);//吸顶 （顶部固定布局）
        sticky_date_tv = findViewById(R.id.sticky_date_tv);
        sticky_date_picker_ll = findViewById(R.id.sticky_date_picker_ll);
        sticky_date_picker_ll.setVisibility(View.VISIBLE);
        record_rv = findViewById(R.id.record_rv);
        record_rv.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter = new RecordTransactionAdapter();
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
                    TransactionRecordEntity.ListBean total = new Gson().fromJson(String.valueOf(stickyInfoView.getContentDescription()), TransactionRecordEntity.ListBean.class);
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
        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TransactionRecordEntity.ListBean item = (TransactionRecordEntity.ListBean) adapter.getItem(position);
                if (item instanceof TransactionRecordEntity.ListBean) {
                    Intent intent = new Intent();
                    if (item.getType().equals("108")) {
                        // 1奖券变更  0余额变更
                        if (item.getFlag().equals("0")) {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyDetailActivity_With_Draw"));
                        } else {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyDetailActivity"));
                        }
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyDetailActivity"));
                    }
                    intent.putExtra("img", item.getImg());
                    intent.putExtra("type", Integer.valueOf(item.getFlag()));
                    intent.putExtra("orderId", item.getId());
                    startActivity(intent);
                }
            }
        });

        credit_bill_img.setOnClickListener(this);
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
        mPresent = new BankPresenter(mContext, this);
        startMyDialog(false);
        getFirstPageData();
    }

    private void setFloatHeaderData(final TransactionRecordEntity.ListBean total) {
        sticky_date_tv.setText(total.getYear() + "年" + total.getMonth() + "月");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sticky_date_picker_ll) {
            showDataPicker();
        } else if (i == R.id.credit_bill_img) {//信用账单
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.CREDIT_BILL);
            startActivity(intent);
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
        mPresent.getTransactionRecordPresenter(page, count, startTime);
    }

    private void getNextPageData() {
        page++;
        mPresent.getTransactionRecordPresenter(page, count, startTime);
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
        if (baseEntity instanceof TransactionRecordEntity) {
            mDatas = ((TransactionRecordEntity) baseEntity).data.getList();
            if (page == 0) {
                if (isFiltrate) {
                    isFiltrate = false;
                    if (mDatas.size() > 0) {
                        setFloatHeaderData(mDatas.get(0));
                    } else {
                        TransactionRecordEntity.ListBean entity = new TransactionRecordEntity.ListBean();
                        String[] str = startTime.split("-");
                        entity.setYear(str[0]);
                        entity.setMonth(str[1]);
                        setFloatHeaderData(entity);
                    }
                }
                if (mDatas.size() > 0) {
                    recordAdapter.setNewData(mDatas);
                } else {
                    Logger.i("获取记录走了这里");
//                    recordAdapter.notifyDataSetChanged();
                    recordAdapter.setNewData(new ArrayList<TransactionRecordEntity.ListBean>());
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
