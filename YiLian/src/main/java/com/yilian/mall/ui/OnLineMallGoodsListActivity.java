package com.yilian.mall.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.OnlineMallGoodsListAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSubClassfiyGoodsEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * @author zhaiyaohua
 * @date 2016/10/18 0018
 * 小分类界面
 */


public class OnLineMallGoodsListActivity extends BaseActivity implements View.OnClickListener {
    private String classId;
    private String failedId;
    private TextView v3Titile;
    private ImageView v3Back;
    private ImageView v3Shop;
    private int page;
    private int count = Constants.PAGE_COUNT;
    private String sort;
    private JPNetRequest subClassfiyGoodsRequest;
    private RadioGroup rgSort;
    private RadioButton rbDefault;
    private RadioButton rbSortPrice;
    private CheckBox cbSortHasData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnlineMallGoodsListAdapter mAdapter;
    private int lastCheckedRadioButton;
    private View emptyView;
    private ImageView ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_online_mall_goods_list);
        mContext = this;
        initView();
        initData();
        initListener();
    }


    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        classId = getIntent().getStringExtra("class_id");
        String goods_title = getIntent().getStringExtra("goods_classfiy");
        failedId = getIntent().getStringExtra("filiale_id");
        if (null == failedId) {
            failedId = "0";
        }
        ivSearch = findViewById(R.id.v3Share);
        ivSearch.setVisibility(View.VISIBLE);
        ivSearch.setImageResource(R.mipmap.icon_search60);
        ivSearch.setOnClickListener(this);
        v3Titile = (TextView) findViewById(R.id.v3Title);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        ImageView v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.GONE);
        v3Shop.setVisibility(View.VISIBLE);
        v3Shop.setImageDrawable(getResources().getDrawable(R.mipmap.iv_shopingcar));
        v3Shop.setOnClickListener(this);
        rgSort = (RadioGroup) findViewById(R.id.rg_sort);
        rbDefault = (RadioButton) findViewById(R.id.rb_sort_newest);
        rbDefault.setChecked(true);
        rbSortPrice = (RadioButton) findViewById(R.id.rb_sort_price);
        cbSortHasData = (CheckBox) findViewById(R.id.cb_sort_has_data);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new OnlineMallGoodsListAdapter(mContext, new ArrayList<JPGoodsEntity>());
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_line, false);
        recyclerView.addItemDecoration(decor);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        v3Titile.setText(goods_title);
        page = 0;
        sort = "0000";
    }

    private void initListener() {
        v3Back.setOnClickListener(this);
        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                boolean isChecked = cbSortHasData.isChecked();
                getSort(checkedId, isChecked);
            }
        });

        //是否有货 作为排序条件监听
        cbSortHasData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
                    sort = sort.substring(0, sort.length() - 1) + "1";
                } else {
                    cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
                    sort = sort.substring(0, sort.length() - 1) + "0";
                    Logger.i("只看有货的排序" + sort);
                }
                page = 0;
                initData();
            }
        });
        rbSortPrice.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextData();
            }
        }, recyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JPGoodsEntity item = (JPGoodsEntity) adapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JP_GOODS_ID, item.JPGoodsId);
                intent.putExtra("classify", item.goodsType);
                mContext.startActivity(intent);
            }
        });
    }

    private void getSort(int checkedId, boolean isChecked) {
        if (isChecked) {
            sort = "1";
        } else {
            sort = "0";
        }
        switch (checkedId) {
            case R.id.rb_sort_newest:
                lastCheckedRadioButton = R.id.rb_sort_newest;
                sort = "100" + sort;
                rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                //排序都是从第0页开始
                page = 0;
                initData();
                break;
            case R.id.rb_sort_count:
                lastCheckedRadioButton = R.id.rb_sort_count;
                sort = "001" + sort;
                rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                page = 0;
                initData();
                break;
            default:
                break;
        }
    }

    private void getFirstData() {
        page = 0;
        initData();
    }

    private void getNextData() {
        page++;
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_sort_price:
                setPriceSort();
                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Shop:
                //跳转到首页购物车
                Intent intent = new Intent(this, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                startActivity(intent);
                break;
            case R.id.v3Share:
                Intent searchIntent = new Intent(mContext, SearchCommodityActivity.class);
                startActivity(searchIntent);
                break;
            default:
                break;
        }
    }

    private void setPriceSort() {
        //处理sort之前先把逗号去掉，在进行网络请求时再加上
        sort = sort.replace(",", "");
        String substring = sort.substring(0, sort.length() - 1);
        String subEnd = sort.substring(sort.length() - 1);
        if ("010".equals(substring)) {
            sort = "020" + subEnd;
            rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
        } else {
            sort = "010" + subEnd;
            rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
        }
        Logger.i("价格只看有货排序" + sort);
        page = 0;
        initData();
    }

    private void initData() {
        if (subClassfiyGoodsRequest == null) {
            subClassfiyGoodsRequest = new JPNetRequest(mContext);
        }
        startMyDialog();
        subClassfiyGoodsRequest.getJpGoodsList(sort, classId, failedId, page, count, new RequestCallBack<JPSubClassfiyGoodsEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSubClassfiyGoodsEntity> responseInfo) {
                stopMyDialog();
                swipeRefreshLayout.setRefreshing(false);
                
                switch (responseInfo.result.code) {
                    case 1:
                        JPSubClassfiyGoodsEntity entity = responseInfo.result;
                        setData(entity);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                swipeRefreshLayout.setRefreshing(false);
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 设置列表数据
     *
     * @param entity
     */
    private void setData(JPSubClassfiyGoodsEntity entity) {
        ArrayList<JPGoodsEntity> goods = entity.data.goods;
        if (page == 0) {
            if (null == goods || goods.size() <= 0) {
                emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
                mAdapter.setEmptyView(emptyView);
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.setNewData(goods);
                if (goods.size() < count) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
            }
        } else {
            mAdapter.addData(goods);
            if (goods.size() < count) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
    }
}
