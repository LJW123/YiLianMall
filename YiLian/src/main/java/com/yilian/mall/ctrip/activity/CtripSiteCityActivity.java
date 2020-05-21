package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripSiteCityAdapter;
import com.yilian.mall.ctrip.adapter.CtripSiteCityHotAdapter;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripSiteModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;


import org.greenrobot.eventbus.EventBus;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 城市选择
 *
 * @author Created by Zg on 2018/8/28.
 */

public class CtripSiteCityActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;

    private LinearLayout llSearch;
    /**
     * 头部部分
     */
    private View headerView;
    private RecyclerView hotCity;
    private CtripSiteCityHotAdapter hotAdapter;
    /**
     * 列表
     */
    private RecyclerView mRecyclerView;
    private CtripSiteCityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_site_city);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
            }
        });

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        llSearch = findViewById(R.id.ll_search);
        //头部布局
        initHeaderView();

        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new CtripSiteCityAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(headerView);
    }

    public void initData() {
        tvTitle.setText("选择城市");
        getCityListData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripSiteCityEntity.HotCity hotCity = (CtripSiteCityEntity.HotCity) adapter.getItem(position);
                /**
                 * {@link CtripHomePageActivity }
                 */
                EventBus.getDefault().post(new CtripSiteModel(hotCity.city, hotCity.cityname, "", "", "0", "0"));
                finish();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripSiteCityEntity.City mData = (CtripSiteCityEntity.City) adapter.getItem(position);
                JumpCtripActivityUtils.toCtripSiteCityByDistricts(context, mData.city, mData.cityname);
            }
        });
    }

    private void initHeaderView() {
        headerView = View.inflate(context, R.layout.ctrip_activity_site_city_header, null);
        hotCity = headerView.findViewById(R.id.hot_city);
        hotAdapter = new CtripSiteCityHotAdapter();
        hotCity.setLayoutManager(new GridLayoutManager(context, 3));
        hotCity.setAdapter(hotAdapter);
    }


    /**
     * 获取 城市列表
     */
    @SuppressWarnings("unchecked")
    private void getCityListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCtripCity("xc_address/xc_address_city")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripSiteCityEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CtripSiteCityEntity entity) {
                        hotAdapter.setNewData(entity.hotCity);
                        mAdapter.setNewData(entity.data);
                        varyViewUtils.showDataView();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.ll_search:
                //跳转搜索页面
                JumpCtripActivityUtils.toCtripSiteCityBySearch(context);
                break;
            default:
                break;
        }
    }
}
