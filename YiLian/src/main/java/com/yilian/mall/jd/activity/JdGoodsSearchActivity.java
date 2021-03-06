package com.yilian.mall.jd.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.jd.adapter.JdBrandGoodsListAdapter;
import com.yilian.mall.jd.adapter.JdSearchHistoryAdapter;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.jd.JdBrandGoodsListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 京东商品搜索页面
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class JdGoodsSearchActivity extends BaseActivity {
    /**
     * 价格升序
     */
    public static final String ASC = "asc";
    /**
     * 降序
     */
    public static final String DESC = "desc";
    public static final int COUNT_RETURN_TOP = 10;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int page = Constants.PAGE_INDEX;
    /**
     * 综合id 价格jdPrice 益豆return_bean 销量sale
     */
    private String sort = "id";
    /**
     * 搜索关键字
     */
    private String searchVal;
    /**
     * asc升序 desc降序
     */
    private String orderBy = "desc";
    private JdBrandGoodsListAdapter jdBrandGoodsListAdapter;
    /**
     * 筛选配置按钮
     */
    private RadioGroup jdRgSort;
    private CheckBox jdCbSortPrice;
    private RadioButton curRadio;
    private FrameLayout jdFlPrice;
    private ImageView ivBack;
    private EditText jdEtSearch;
    private List<String> jdSearchDataList;
    private TextView jdTvSearch;
    private FrameLayout jdFlSearch;
    private List<String> notifyList = new ArrayList<>();
    private JdSearchHistoryAdapter searchHistoryAdapter;
    private RecyclerView searchList;
    private View searchLayout;
    private TextView tvClearHistory;
    private VaryViewUtils varyViewUtils;
    private ImageView ivReturnTop;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_search_goods);
        initView();
        initListener();
    }

    private void initView() {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setJdVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                getFirstGoodsData();
            }
        });
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        ivReturnTop = findViewById(R.id.iv_return_top);
        searchLayout = findViewById(R.id.jd_layout_search);
        tvClearHistory = findViewById(R.id.tv_clear_history);
        jdTvSearch = findViewById(R.id.jd_tv_search);
        jdFlSearch = findViewById(R.id.jd_fl_search);
        jdSearchDataList = (List<String>) PreferenceUtils.readObjectConfig(Constants.JD_GOODS_SEARCH_RECORDER, mContext);
        if (null == jdSearchDataList) {
            jdSearchDataList = new ArrayList<>();
        }

        ivBack = findViewById(R.id.iv_back);
        jdFlPrice = findViewById(R.id.jd_fl_sort_price);
        jdRgSort = (RadioGroup) findViewById(R.id.jd_rg_sort);
        jdCbSortPrice = (CheckBox) findViewById(R.id.jd_cb_sort_price);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(mContext, 8, R.color.bg_gray);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(gridLayoutManager);
        jdBrandGoodsListAdapter = new JdBrandGoodsListAdapter();
        recyclerView.setAdapter(jdBrandGoodsListAdapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
        jdEtSearch = findViewById(R.id.jd_et_search);

        searchList = findViewById(R.id.jd_recycler_view);
        searchHistoryAdapter = new JdSearchHistoryAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        searchList.setLayoutManager(manager);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 2, R.color.bg_gray);
        searchList.addItemDecoration(decor);
        searchList.setAdapter(searchHistoryAdapter);
        if (jdSearchDataList != null) {
            searchHistoryAdapter.setNewData(jdSearchDataList);
        }
    }

    /**
     * 搜索商品
     */
    private void searchGoods() {
        String val = jdEtSearch.getText().toString();
        if (TextUtils.isEmpty(val)) {
            searchLayout.setVisibility(View.VISIBLE);
            showToast("请输入搜索商品名称");
        } else {
            searchLayout.setVisibility(View.GONE);
            searchVal = val;
            saveSearchVal();
            startMyDialog();
            getFirstGoodsData();
            jdEtSearch.setSelection(val.length());
        }
    }

    /**
     * 保存搜索的关键字
     */
    private void saveSearchVal() {
        if (!jdSearchDataList.contains(searchVal)) {
            jdSearchDataList.add(searchVal);
            PreferenceUtils.writeObjectConfig(Constants.JD_GOODS_SEARCH_RECORDER, jdSearchDataList, mContext);
        }
    }

    @SuppressWarnings("unchecked")
    private void getGoodsList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .searchJdGoods("jd_goods/jd_goods_search", page, Constants.PAGE_COUNT, searchVal, sort, orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JdBrandGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        if (page == 0) {
                            varyViewUtils.showErrorView();
                        } else if (page > Constants.PAGE_INDEX) {
                            page--;
                        }
                        jdBrandGoodsListAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(JdBrandGoodsListEntity entity) {
                        setData(entity);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(JdBrandGoodsListEntity entity) {
        if (page <= 0) {
            jdBrandGoodsListAdapter.setNewData(entity.getDataList());
            if (entity.getDataList().size() <= 0) {
                varyViewUtils.showEmptyView();
                jdBrandGoodsListAdapter.loadMoreEnd();
            } else {
                if (entity.getDataList().size() < Constants.PAGE_COUNT) {
                    jdBrandGoodsListAdapter.loadMoreEnd();
                } else {
                    jdBrandGoodsListAdapter.loadMoreComplete();
                }
                recyclerView.smoothScrollToPosition(0);
                varyViewUtils.showDataView();
            }
        } else {
            jdBrandGoodsListAdapter.addData(entity.getDataList());
            if (entity.getDataList().size() < Constants.PAGE_COUNT) {
                jdBrandGoodsListAdapter.loadMoreEnd();
            } else {
                jdBrandGoodsListAdapter.loadMoreComplete();
            }

        }
    }

    private void getFirstGoodsData() {
        page = Constants.PAGE_INDEX;
        getGoodsList();
    }

    private void initListener() {
        jdRgSort.check(R.id.jd_rb_sort_default);
        RxUtil.clicks(tvClearHistory, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jdSearchDataList.clear();
                PreferenceUtils.writeObjectConfig(Constants.JD_GOODS_SEARCH_RECORDER, jdSearchDataList, mContext);
                searchHistoryAdapter.setNewData(jdSearchDataList);
                tvClearHistory.setVisibility(View.GONE);
            }
        });
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                jdEtSearch.setText(searchHistoryAdapter.getItem(position));
                searchGoods();
                searchLayout.setVisibility(View.GONE);
            }
        });
        RxUtil.clicks(jdTvSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                searchGoods();
            }
        });
        jdEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchLayout.setVisibility(View.VISIBLE);
                if (jdSearchDataList.size() > 0) {
                    tvClearHistory.setVisibility(View.VISIBLE);
                }
                String searchString = s.toString().trim();
                if (TextUtils.isEmpty(searchString)) {
                    searchHistoryAdapter.setNewData(jdSearchDataList);
                } else {
                    notifyList.clear();
                    for (String val : jdSearchDataList) {
                        if (val.contains(searchString)) {
                            notifyList.add(val);
                        }
                    }
                    searchHistoryAdapter.setNewData(notifyList);
                }
            }
        });

        jdBrandGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDGoodsAbstractInfoEntity entity = (JDGoodsAbstractInfoEntity) adapter.getItem(position);
                if (null != entity) {
                    JumpJdActivityUtils.toGoodsDetail(mContext, entity.skuId);
                }
            }
        });
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        RxUtil.clicks(jdFlPrice, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jdRgSort.clearCheck();
                jdCbSortPrice.setChecked(true);
                sort = "jdPrice";
                if (orderBy.equals(ASC)) {
                    orderBy = DESC;
                    jdCbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
                } else {
                    orderBy = ASC;
                    jdCbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
                }
                startMyDialog();
                getFirstGoodsData();
            }
        });
        jdRgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                curRadio = findViewById(checkedId);
                if (null != curRadio && curRadio.isChecked()) {
                    jdCbSortPrice.setChecked(false);
                    jdCbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                    switch (checkedId) {
                        case R.id.jd_rb_sort_default:
                            sort = "id";
                            break;
                        case R.id.jd_rb_sort_has_rate:
                            sort = "return_bean ";
                            break;
                        case R.id.jd_rb_sort_sale_count:
                            sort = "sale ";
                            break;
                        default:
                            break;
                    }
                    orderBy=DESC;
                    startMyDialog();
                    getFirstGoodsData();
                }

            }
        });
        jdBrandGoodsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstGoodsData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = gridLayoutManager.findFirstVisibleItemPosition();
                if (position <= COUNT_RETURN_TOP) {
                    ivReturnTop.setVisibility(View.GONE);
                } else {
                    ivReturnTop.setVisibility(View.VISIBLE);
                }
            }
        });
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void getNextGoodsData() {
        page++;
        getGoodsList();
    }
}
