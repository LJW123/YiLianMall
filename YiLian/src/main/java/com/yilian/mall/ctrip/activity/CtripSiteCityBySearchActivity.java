package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripSiteCityBySearchAdapter;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.utils.KeyBordUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityByDistrictsEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripSiteModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 城市选择 搜索
 *
 * @author Created by Zg on 2018/8/28.
 */

public class CtripSiteCityBySearchActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;

    private EditText etKeyword;
    private TextView tvSearch;

    /**
     * 列表
     */
    private RecyclerView mRecyclerView;
    private CtripSiteCityBySearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_site_city_by_search);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setCtripSearch(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        varyViewUtils.showDataView();

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        etKeyword = findViewById(R.id.et_keyword);
        tvSearch = findViewById(R.id.tv_search);

        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new CtripSiteCityBySearchAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);

    }

    public void initData() {
        tvTitle.setText("选择城市");
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripSiteCityByDistrictsEntity.DataBean dataBean = (CtripSiteCityByDistrictsEntity.DataBean) adapter.getItem(position);
                /**
                 * {@link CtripHomePageActivity }
                 */
                EventBus.getDefault().post(new CtripSiteModel(dataBean.city, dataBean.cityname, dataBean.location, dataBean.locationname, "0", "0"));
                AppManager.getInstance().killActivity(CtripSiteCityActivity.class);
                finish();
            }
        });
    }


    /**
     * 获取 城市列表
     */
    @SuppressWarnings("unchecked")
    private void getCityListBySearchData(String keyWord) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCtripCityBySearch("xc_address/xc_address_search", keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripSiteCityByDistrictsEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CtripSiteCityByDistrictsEntity entity) {
                        if(entity!=null && entity.data!=null && entity.data.size()>0) {
                            mAdapter.setNewData(entity.data);
                            varyViewUtils.showDataView();
                        }else {
                            varyViewUtils.showEmptyView();
                        }
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
            case R.id.tv_search:
                KeyBordUtils.closeKeyBord(context);
                //搜索
                if (!TextUtils.isEmpty(etKeyword.getText().toString().trim())) {
                    varyViewUtils.showLoadingView();
                    getCityListBySearchData(etKeyword.getText().toString().trim());
                } else {
                    showToast("请输入搜索内容");
                }
                break;
            default:
                break;
        }
    }
}
