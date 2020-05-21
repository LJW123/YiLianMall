package com.yilian.mall.suning.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
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
import com.yilian.mall.suning.adapter.SnBrandGoodsListAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.suning.SnBrandGoodsListEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 苏宁品牌分类第三级商品页面
 *
 * @author Created by Zg on 2018/7/17.
 */

public class SnThirdClassifyGoodsListActivity extends BaseActivity {
    /**
     * 价格升序
     */
    public static final String ASC = "asc";
    /**
     * 几个降序
     */
    public static final String DESC = "desc";
    public static final int COUNT_RETURN_TOP = 10;
    private VaryViewUtils varyViewUtils;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int page = Constants.PAGE_INDEX;
    /**
     * 综合id 价格jdPrice 益豆return_bean 销量sale
     */
    private String sort = "id";
    private String catId;
    private String title;
    /**
     * asc升序 desc降序
     */
    private String orderBy = "desc";
    private SnBrandGoodsListAdapter snBrandGoodsListAdapter;
    /**
     * 筛选配置按钮
     */
    private RadioGroup jdRgSort;
    private CheckBox jdCbSortPrice;
    private RadioButton curRadio;
    private FrameLayout jdFlPrice;
    private ImageView ivBack;
    private TextView jdBrandNameTitle;
    private ImageView ivReturnTop;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_layout_goods_filter);
        initView();
        initListener();
    }

    private void initView() {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setSnVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                getFirstGoodsData();
            }
        });

        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        ivReturnTop = findViewById(R.id.iv_return_top);
        ivBack = findViewById(R.id.iv_back);
        jdFlPrice = findViewById(R.id.jd_fl_sort_price);
        jdRgSort = (RadioGroup) findViewById(R.id.jd_rg_sort);
        jdCbSortPrice = (CheckBox) findViewById(R.id.jd_cb_sort_price);
        jdBrandNameTitle = findViewById(R.id.jd_tv_title);
        jdBrandNameTitle.setVisibility(View.VISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(mContext, 8, R.color.bg_gray);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(gridLayoutManager);
        snBrandGoodsListAdapter = new SnBrandGoodsListAdapter();
        recyclerView.setAdapter(snBrandGoodsListAdapter);
        catId = getIntent().getStringExtra("catId");
        title = getIntent().getStringExtra("title");
        jdBrandNameTitle.setText(title);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
    }

    private void initListener() {
        snBrandGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnGoodsAbstractInfoEntity entity = (SnGoodsAbstractInfoEntity) adapter.getItem(position);
                if (null != entity) {
                    // 商品详情
                    JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, entity.skuId);
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
                sort = "snPrice";
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
                    orderBy = DESC;
                    startMyDialog();
                    getFirstGoodsData();
                }

            }
        });


        snBrandGoodsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = gridLayoutManager.findFirstVisibleItemPosition();
                if (COUNT_RETURN_TOP <= position) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
            }
        });
        jdRgSort.check(R.id.jd_rb_sort_default);
    }

    private void getNextGoodsData() {
        page++;
        getGoodsList();
    }

    private void getFirstGoodsData() {
        page = Constants.PAGE_INDEX;
        getGoodsList();
    }

    @SuppressWarnings("unchecked")
    private void getGoodsList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnThirdClassifyGoodsList("suning_goods/suning_goods_list", page, Constants.PAGE_COUNT, catId, sort, orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnBrandGoodsListEntity>() {
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
                        snBrandGoodsListAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(SnBrandGoodsListEntity entity) {
                        setData(entity);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(SnBrandGoodsListEntity entity) {
        if (page <= 0) {
            snBrandGoodsListAdapter.setNewData(entity.getDataList());
            if (entity.getDataList().size() <= 0) {
                varyViewUtils.showEmptyView();
                snBrandGoodsListAdapter.loadMoreEnd();
            } else {
                if (entity.getDataList().size() < Constants.PAGE_COUNT) {
                    snBrandGoodsListAdapter.loadMoreEnd();
                } else {
                    snBrandGoodsListAdapter.loadMoreComplete();
                }
                recyclerView.smoothScrollToPosition(0);
                varyViewUtils.showDataView();
            }
        } else {
            snBrandGoodsListAdapter.addData(entity.getDataList());
            if (entity.getDataList().size() < Constants.PAGE_COUNT) {
                snBrandGoodsListAdapter.loadMoreEnd();
            } else {
                snBrandGoodsListAdapter.loadMoreComplete();
            }

        }
    }
}
