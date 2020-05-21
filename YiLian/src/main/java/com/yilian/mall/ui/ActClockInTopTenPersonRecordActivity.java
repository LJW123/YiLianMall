package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActClockInMyRecordAdapter;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ActClockInMyRecordEntity;
import com.yilian.networkingmodule.entity.ActClockInTopTenPersonRecordEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  2017年11月23日22:03:40
 *         打卡前十名个人打卡记录
 */
public class ActClockInTopTenPersonRecordActivity extends BaseAppCompatActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private String consumerId;
    private int page;
    private ActClockInMyRecordAdapter actClockInMyRecordAdapter;
    private View headerView;
    private ImageView ivPhoto;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_recyclerview);
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
    }

    private void initData() {
        consumerId = getIntent().getStringExtra("consumerId");
        getData();
    }

    void getFirstPageData() {
        page = 0;
        getData();
    }

    void getNextPageData() {
        page++;
        getData();
    }

    @SuppressWarnings("unchecked")
    void getData() {
        startMyDialog(false);
        swipeRefreshLayout.setRefreshing(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getClockInTopTenPersonRecord("consumer_clockin_record", consumerId, page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActClockInTopTenPersonRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ActClockInTopTenPersonRecordEntity actClockInTopTenPersonRecordEntity) {
                        List<ActClockInMyRecordEntity.RecordListBean> recordList = actClockInTopTenPersonRecordEntity.recordList;
                        if (page == 0) {
                            if (actClockInMyRecordAdapter.getHeaderLayoutCount() <= 0) {
                                actClockInMyRecordAdapter.addHeaderView(headerView);
                            }
                            tvName.setText(actClockInTopTenPersonRecordEntity.name);
                            GlideUtil.showCirImage(mContext, actClockInTopTenPersonRecordEntity.photo, ivPhoto);
                            if (recordList == null || recordList.size() <= Constants.PAGE_COUNT) {
                                actClockInMyRecordAdapter.loadMoreEnd();
                                if (actClockInMyRecordAdapter.getFooterLayoutCount() <= 0 && recordList.size() <= 0) {
                                    ImageView footer = new ImageView(mContext);
                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    footer.setLayoutParams(layoutParams);
                                    footer.setImageResource(R.mipmap.library_module_nothing);
                                    footer.setPadding(0, 50, 0, 5);
                                    actClockInMyRecordAdapter.setFooterView(footer);
                                }
                            } else {
                                actClockInMyRecordAdapter.loadMoreComplete();
                            }
                            actClockInMyRecordAdapter.setNewData(recordList);
                        } else {
                            if (recordList == null || recordList.size() < Constants.PAGE_COUNT) {
                                actClockInMyRecordAdapter.loadMoreEnd();
                            } else {
                                actClockInMyRecordAdapter.loadMoreComplete();
                            }
                            actClockInMyRecordAdapter.addData(recordList);
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        actClockInMyRecordAdapter = new ActClockInMyRecordAdapter(R.layout.item_act_clock_in_my_record);
        actClockInMyRecordAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(actClockInMyRecordAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("个人打卡记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);

        headerView = View.inflate(mContext, R.layout.header_act_clock_in_top_ten_person_record, null);
        ivPhoto = (ImageView) headerView.findViewById(R.id.iv_photo);
        tvName = (TextView) headerView.findViewById(R.id.tv_name);
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

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }
}
