package com.yilian.mall.suning.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.suning.adapter.SnAfterSaleApplicationRecordAdapter;
import com.yilian.mall.suning.adapter.SnAfterSaleApplyForAdapter;
import com.yilian.mall.suning.adapter.SnSearchHistoryAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.ClearEditText;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplicationRecordListEntity;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplyForListEntity;
import com.yilian.networkingmodule.entity.suning.snMultiItem.SnAfterSaleListLayoutType;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SnAfterSaleSearchActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SnAfterSaleSearchActivity";
    /**
     * 搜索记录最大数量
     */
    public static final int SEARCH_RECORD_MAX_SIZE = 8;
    private AfterSaleType afterSaleType;
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private ClearEditText etInputSearchKey;
    private RecyclerView recyclerViewSearchHistory;
    private LinearLayout llClearSearchHistory;
    private LinearLayout llSearch;
    private RecyclerView recyclerViewSearchResult;
    private TextView tvSearch;
    private SnSearchHistoryAdapter searchHistoryAdapter;
    /**
     * 搜索历史
     */
    private ArrayList<String> searchHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sn_after_sale_search);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvSearch = findViewById(R.id.tv_search);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        etInputSearchKey = (ClearEditText) findViewById(R.id.et_input_search_key);
//        搜索历史列表
        recyclerViewSearchHistory = (RecyclerView) findViewById(R.id.recycler_view_search_history);
        recyclerViewSearchHistory.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewSearchHistory.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL_LIST, 1, Color.parseColor("#e7e7e7")));
        searchHistoryAdapter = new SnSearchHistoryAdapter();
        recyclerViewSearchHistory.setAdapter(searchHistoryAdapter);


        llClearSearchHistory = (LinearLayout) findViewById(R.id.ll_clear_search_history);

        llSearch = (LinearLayout) findViewById(R.id.ll_search);
//        搜索结果列表
        recyclerViewSearchResult = (RecyclerView) findViewById(R.id.recycler_view_search_result);
        recyclerViewSearchResult.setLayoutManager(new LinearLayoutManager(mContext));


        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    private void initData() {
        afterSaleType = (AfterSaleType) getIntent().getSerializableExtra(TAG);
        searchHistory = (ArrayList<String>) PreferenceUtils.readObjectConfig(afterSaleType.name(), mContext);
        if (searchHistory == null) {
//            避免第一次为空的情况
            searchHistory = new ArrayList<>();
        }
        switch (afterSaleType) {
            case TYPE_CAN_APPLY_AFTER_SALE:
                v3Title.setText("售后申请");
                break;
            case TYPE__APPLY_AFTER_SALE_RECORD:
                v3Title.setText("申请记录");
                break;
            default:
                break;
        }
        searchHistoryAdapter.setNewData(searchHistory);
    }

    private void initListener() {
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String item = (String) adapter.getItem(position);
                search(item);
            }
        });
        RxUtil.clicks(llClearSearchHistory, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                List<String> clearSearchHistory = new ArrayList<>();
                PreferenceUtils.writeObjectConfig(afterSaleType.name(), clearSearchHistory, mContext);
                searchHistoryAdapter.setNewData(clearSearchHistory);
            }
        });
        RxUtil.clicks(tvSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                String searchKey = etInputSearchKey.getText().toString().trim();
                search(searchKey);
            }
        });
    }

    private void search(String searchKey) {
        if (TextUtils.isEmpty(searchKey)) {
            showToast("请搜索商品名称或订单号");
            return;
        }
        switch (afterSaleType) {
            case TYPE_CAN_APPLY_AFTER_SALE:
                searchCanApplyAfterSale(searchKey);
                break;
            case TYPE__APPLY_AFTER_SALE_RECORD:
                searchAfterSaleRecord(searchKey);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void searchCanApplyAfterSale(String searchKey) {
        startMyDialog();
        if (searchHistory.contains(searchKey)) {
            searchHistory.remove(searchKey);
//            将最新输入的添加到首位
        }
        searchHistory.add(0, searchKey);
        if (searchHistory.size()>SEARCH_RECORD_MAX_SIZE) {
            searchHistory.remove(SEARCH_RECORD_MAX_SIZE);
        }
//存储搜索记录
        PreferenceUtils.writeObjectConfig(afterSaleType.name(), searchHistory, mContext);

        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getSnAfterSaleApplyForList("suning_aftersale/suning_aftersale_list",
                        0, Constants.PAGE_COUNT, searchKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnAfterSaleApplyForListEntity>() {
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
                    public void onNext(SnAfterSaleApplyForListEntity snAfterSaleApplyForListEntity) {
                        llSearch.setVisibility(View.GONE);
                        recyclerViewSearchResult.setVisibility(View.VISIBLE);
                        SnAfterSaleApplyForAdapter afterSaleRecordAdapter = new SnAfterSaleApplyForAdapter();
                        afterSaleRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                int itemViewType = adapter.getItemViewType(position);
                                switch (itemViewType) {
                                    case SnAfterSaleListLayoutType.goods:

                                        switch (view.getId()) {
                                            case R.id.tv_apply_for:
                                                //商品信息
                                                SnAfterSaleApplyForListEntity.GoodsList bean = (SnAfterSaleApplyForListEntity.GoodsList) adapter.getItem(position);
                                                //退货退款
                                                JumpSnActivityUtils.toSnAfterSaleApplyFor(mContext, bean.getSnOrderId(), bean.getOrderSnPrice(), bean.getCoupon(),
                                                        bean.getSkuId(), bean.getSkuPic(), bean.getSkuName(), bean.getSnPrice(), bean.getSkuNum());
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    default:
                                        break;
                                }

                            }
                        });
                        recyclerViewSearchResult.setAdapter(afterSaleRecordAdapter);
                        if (snAfterSaleApplyForListEntity.data.size() > 0) {
                            for (SnAfterSaleApplyForListEntity.DataBean datum : snAfterSaleApplyForListEntity.data) {
                                afterSaleRecordAdapter.addData(datum);
                                for (SnAfterSaleApplyForListEntity.GoodsList goodsList : datum.getOrderGoods()) {
                                    goodsList.setOrderSnPrice(datum.getOrderSnPrice());
                                    goodsList.setCoupon(datum.getCoupon());
                                    afterSaleRecordAdapter.addData(goodsList);
                                }
                            }
                        } else {
                            View emptyView = getEmptyView();
                            afterSaleRecordAdapter.setEmptyView(emptyView);
                        }

                    }
                });
        addSubscription(subscription);
    }

    @SuppressWarnings("unchecked")
    private void searchAfterSaleRecord(String searchKey) {

        startMyDialog();
        if (searchHistory.contains(searchKey)) {
            searchHistory.remove(searchKey);
//            将最新输入的添加到首位
        }
        searchHistory.add(0, searchKey);
        if (searchHistory.size()>SEARCH_RECORD_MAX_SIZE) {
            searchHistory.remove(SEARCH_RECORD_MAX_SIZE);
        }
//存储搜索记录
        PreferenceUtils.writeObjectConfig(afterSaleType.name(), searchHistory, mContext);

        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getSnAfterSaleApplicationRecordList("suning_aftersale/suning_aftersale_record",
                        0, Constants.PAGE_COUNT, searchKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnAfterSaleApplicationRecordListEntity>() {
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
                    public void onNext(SnAfterSaleApplicationRecordListEntity snAfterSaleApplicationRecordListEntity) {
                        llSearch.setVisibility(View.GONE);
                        recyclerViewSearchResult.setVisibility(View.VISIBLE);
                        SnAfterSaleApplicationRecordAdapter snAfterSaleApplicationRecordAdapter = new SnAfterSaleApplicationRecordAdapter();
                        snAfterSaleApplicationRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                //售后详情
                                SnAfterSaleApplicationRecordListEntity.DataBean item = (SnAfterSaleApplicationRecordListEntity.DataBean) adapter.getItem(position);
                                JumpSnActivityUtils.toSnAfterSaleDetails(mContext, item.getId());
                            }
                        });
                        recyclerViewSearchResult.setAdapter(snAfterSaleApplicationRecordAdapter);
                        if (snAfterSaleApplicationRecordListEntity.data.size() > 0) {
                            snAfterSaleApplicationRecordAdapter.setNewData(snAfterSaleApplicationRecordListEntity.data);
                        } else {
                            View emptyView = getEmptyView();
                            snAfterSaleApplicationRecordAdapter.setEmptyView(emptyView);
                        }

                    }
                });
        addSubscription(subscription);
    }

    @NonNull
    private View getEmptyView() {
        View emptyView = View.inflate(mContext, R.layout.sn_after_sale_search_empty_view, null);
        RxUtil.clicks(emptyView.findViewById(R.id.btn_go_to_sn_home_page), new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_HOME, null);
            }
        });
        return emptyView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
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
    public void onBackPressed() {
        if (recyclerViewSearchResult.getVisibility() == View.VISIBLE && llSearch.getVisibility() == View.GONE) {
            recyclerViewSearchResult.setVisibility(View.GONE);
            llSearch.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 搜索类型
     */
    public enum AfterSaleType {
        /**
         * 搜索可申请售后订单
         */
        TYPE_CAN_APPLY_AFTER_SALE,
        /**
         * 搜索申请过售后的订单
         */
        TYPE__APPLY_AFTER_SALE_RECORD

    }

}
