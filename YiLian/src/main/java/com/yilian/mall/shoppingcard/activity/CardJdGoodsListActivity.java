package com.yilian.mall.shoppingcard.activity;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.shoppingcard.adapter.CardGoodsListAdapter;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.jd.JdBrandGoodsListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 购物卡 京东商品列表
 *
 * @author Created by Zg on 2018/11/14.
 */

public class CardJdGoodsListActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private int page = Constants.PAGE_INDEX;//页数
    private ImageView ivBack;
    private TextView tvTitle;

    //筛选条件
    private LinearLayout llFilterPrice, llFilterAvailable;
    private TextView tvFilterComposite, tvFilterPrice, tvFilterSales, tvFilterAvailable;
    /**
     * 综合id 价格jdPrice 益豆return_bean 销量sale
     */
    private String SORT = "id";
    /**
     * state 0不选 1有货
     */
    private String STATE = "1";
    private String catId = "";
    private String title = "商品列表";
    /**
     * asc升序 desc降序
     */
    private String orderBy = "desc";


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CardGoodsListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_jd_goods_list);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新页面
                getFirstData();
            }
        });
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        tvFilterComposite = findViewById(R.id.tv_filter_composite);
        llFilterPrice = findViewById(R.id.ll_filter_price);
        tvFilterPrice = findViewById(R.id.tv_filter_price);
        tvFilterSales = findViewById(R.id.tv_filter_sales);
        llFilterAvailable = findViewById(R.id.ll_filter_available);
        tvFilterAvailable = findViewById(R.id.tv_filter_available);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.red));
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mAdapter = new CardGoodsListAdapter();
        mAdapter.bindToRecyclerView(mRecyclerView);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(context, DPXUnitUtil.dp2px(context, 4), R.color.color_bg);
        mRecyclerView.addItemDecoration(decor);


    }

    private void initData() {
        catId = getIntent().getStringExtra("catId");
        if (TextUtils.isEmpty(catId)) {
            catId = "";
        }
        title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText("商品列表");
        } else {
            tvTitle.setText(title);
        }
        getFirstData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvFilterComposite.setOnClickListener(this);
        llFilterPrice.setOnClickListener(this);
        tvFilterSales.setOnClickListener(this);
        llFilterAvailable.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
                mAdapter.setEnableLoadMore(false);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getGoodsListData();
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDGoodsAbstractInfoEntity entity = (JDGoodsAbstractInfoEntity) mAdapter.getItem(position);
                JumpCardActivityUtils.toGoodsDetail(context, entity.skuId);

            }
        });
    }


    /**
     * 获取第一页商品数据
     */
    private void getFirstData() {
        page = 0;
        getGoodsListData();
    }


    /**
     * 获取商品数据
     */
    private void getGoodsListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCardJdThirdClassifyGoodsList("shoppingGoodsList", page, Constants.PAGE_COUNT, catId, SORT, orderBy, STATE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JdBrandGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        stopMyDialog();

                        if (page == Constants.PAGE_INDEX) {
                            varyViewUtils.showEmptyView();
                        } else {
                            mAdapter.loadMoreFail();
                            page--;
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JdBrandGoodsListEntity data) {
                        if (page == Constants.PAGE_INDEX) {
                            if (data.getDataList().size() > 0) {
                                mAdapter.setNewData(data.getDataList());
                                mRecyclerView.smoothScrollToPosition(0);
                                varyViewUtils.showDataView();
                            }
                        } else {
                            mAdapter.addData(data.getDataList());
                        }
                        if (data.getDataList().size() < Constants.PAGE_COUNT) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_filter_composite:
                if (SORT.equals("id")) {
                    return;
                }
                //综合排序
                initFiltrate();
                tvFilterComposite.setTextColor(getResources().getColor(R.color.price));
                SORT = "id";
                orderBy = "desc";
                startMyDialog(false);
                getFirstData();
                break;
            case R.id.ll_filter_price:
                //价格
                initFiltrate();
                tvFilterPrice.setTextColor(getResources().getColor(R.color.price));
                SORT = "jdPrice";
                if (orderBy.equals("asc")) {
                    orderBy = "desc";
                    tvFilterPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.price_buttom), null);
                } else {
                    orderBy = "asc";
                    tvFilterPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.price_top), null);
                }
                startMyDialog(false);
                getFirstData();
                break;
            case R.id.tv_filter_sales:
                if (SORT.equals("sale")) {
                    return;
                }
                //销量
                initFiltrate();
                tvFilterSales.setTextColor(getResources().getColor(R.color.price));
                SORT = "sale";
                orderBy = "desc";
                startMyDialog(false);
                getFirstData();
                break;
            case R.id.ll_filter_available:
                //只看有货
                //0不选 1有货
                if (STATE.equals("0")) {
                    STATE = "1";
                    tvFilterAvailable.setTextColor(getResources().getColor(R.color.price));
                    tvFilterAvailable.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.card_jd_goods_list_filter_available_select), null);
                } else {
                    STATE = "0";
                    tvFilterAvailable.setTextColor(getResources().getColor(R.color.color_333));
                    tvFilterAvailable.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.card_jd_goods_list_filter_available_unselect), null);
                }
                startMyDialog(false);
                getFirstData();
                break;
            default:
                break;
        }
    }

    public void initFiltrate() {
        tvFilterComposite.setTextColor(getResources().getColor(R.color.color_333));
        tvFilterPrice.setTextColor(getResources().getColor(R.color.color_333));
        tvFilterPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.price_default), null);
        tvFilterSales.setTextColor(getResources().getColor(R.color.color_333));
    }

}
