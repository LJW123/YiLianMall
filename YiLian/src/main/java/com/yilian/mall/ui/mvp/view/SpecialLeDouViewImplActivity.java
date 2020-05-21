package com.yilian.mall.ui.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.LeDouListAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.mvp.presenter.ISpecialLeDouPresenter;
import com.yilian.mall.ui.mvp.presenter.SpecialLeDouPresenter;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MenuUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.DataBean;
import com.yilian.networkingmodule.entity.LeDouHomePageDataEntity;

import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Subscription;

import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS;
import static com.yilian.mall.ui.mvp.model.LeDouHomePageModelImpl.HOME_PAGE_LE_DOU;

public class SpecialLeDouViewImplActivity extends BaseAppCompatActivity implements ISpecialLeDouView, View.OnClickListener {

    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RadioButton rbSortNewest;
    private RadioButton rbSortPrice;
    private RadioButton rbSortCount;
    private RadioGroup rgSort;
    private CheckBox checkBox;
    private LinearLayout llSort;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ISpecialLeDouPresenter specialLeDouPresenter;
    private int page;
    private String sort = "1000";
    private LeDouListAdapter leDouListAdapter;
    private String ledouType;
    private String ledouTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_le_dou_view_impl);
        specialLeDouPresenter = new SpecialLeDouPresenter(this);
        ledouType = getIntent().getStringExtra("ledou_type");
        ledouTypeName = getIntent().getStringExtra("ledou_type_name");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        leDouListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
        leDouListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DataBean item = (DataBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                intent.putExtra("filiale_id", item.filialeId);
                intent.putExtra("goods_id", item.goodsId);
                intent.putExtra("classify", LE_DOU_GOODS);
                startActivity(intent);
            }
        });
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(SpecialLeDouViewImplActivity.this, R.id.v3Shop);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getFirstPageData();
            }
        });
        rbSortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder(sort);
                char c = stringBuilder.charAt(1);
//                重置第一和第三个条件
                stringBuilder.replace(0, 1, "0");
                stringBuilder.replace(2, 3, "0");

                if (String.valueOf(c).equals("0") || String.valueOf(c).equals("1")) {
                    stringBuilder.replace(1, 2, "2");
                    rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
                } else {
                    stringBuilder.replace(1, 2, "1");
                    rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
                }
                sort = stringBuilder.toString();
                getFirstPageData();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
                    sort = sort.substring(0, sort.length() - 1) + "1";
                } else {
                    checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
                    sort = sort.substring(0, sort.length() - 1) + "0";
                }
                getFirstPageData();
            }
        });
        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_sort_newest:
                        sort = checkBox.isChecked() ? "1001" : "1000";
                        getFirstPageData();
                        rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                        break;
                    case R.id.rb_sort_count:
                        sort = checkBox.isChecked() ? "0011" : "0010";
                        getFirstPageData();
                        rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    private void initData() {
        rgSort.check(R.id.rb_sort_newest);
        getFirstPageData();
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getData() {
        Subscription subscription = specialLeDouPresenter.getData(mContext, page, HOME_PAGE_LE_DOU, sort, ledouType);
        addSubscription(subscription);
    }

    private void getNextPageData() {
        page++;
        getData();
    }


    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText(ledouTypeName);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        rbSortNewest = (RadioButton) findViewById(R.id.rb_sort_newest);
        rbSortNewest.setText("最新");
        rbSortPrice = (RadioButton) findViewById(R.id.rb_sort_price);
        rbSortCount = (RadioButton) findViewById(R.id.rb_sort_count);
        rgSort = (RadioGroup) findViewById(R.id.rg_sort);
        checkBox = (CheckBox) findViewById(R.id.cb_sort_has_data);
        llSort = (LinearLayout) findViewById(R.id.ll_sort);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        leDouListAdapter = new LeDouListAdapter(R.layout.item_gride_le_dou);
        recyclerView.setAdapter(leDouListAdapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    @Override
    public void setData(LeDouHomePageDataEntity leDouHomePageDataEntity) {
        List<DataBean> data = leDouHomePageDataEntity.data;
        if (page == 0) {

            if (data.size() <= 0) {
                leDouListAdapter.loadMoreEnd();
//                TODO 设置空布局
            } else {
                leDouListAdapter.setNewData(data);
                isLoadMore(data);
            }
        } else {
            if (data.size() <= 0) {
                leDouListAdapter.loadMoreEnd();
            } else {
                leDouListAdapter.addData(data);
                isLoadMore(data);
            }
        }

    }    @Override
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

    /**
     * 列表是否加载下一页
     *
     * @param data
     */
    private void isLoadMore(List<DataBean> data) {
        if (data.size() < Constants.PAGE_COUNT) {
            leDouListAdapter.loadMoreEnd();
        } else {
            leDouListAdapter.loadMoreComplete();
        }
    }



}
