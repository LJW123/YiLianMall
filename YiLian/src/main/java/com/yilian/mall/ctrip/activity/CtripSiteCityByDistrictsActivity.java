package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripSiteCityByDistrictsHotAdapter;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityByDistrictsEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripSiteModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 城市选择  区域
 *
 * @author Created by Zg on 2018/8/28.
 */

public class CtripSiteCityByDistrictsActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvCity;

    /**
     * 列表
     */
    private RecyclerView mRecyclerView;
    private CtripSiteCityByDistrictsHotAdapter mAdapter;

    private String cityId, cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_site_city_by_districts);
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
        tvCity = findViewById(R.id.tv_city);


        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new CtripSiteCityByDistrictsHotAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        cityId = getIntent().getStringExtra("cityId");
        cityName = getIntent().getStringExtra("cityName");

        tvTitle.setText(cityName);
        tvCity.setText(cityName);
        getDistrictsListData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);

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
     * 获取 区县列表
     */
    @SuppressWarnings("unchecked")
    private void getDistrictsListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCtripCityByDistricts("xc_address/xc_address_county", cityId)
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
                        List<CtripSiteCityByDistrictsEntity.DataBean> list = entity.data;
                        CtripSiteCityByDistrictsEntity.DataBean dataBean = new CtripSiteCityByDistrictsEntity.DataBean();
                        dataBean.city = cityId;
                        dataBean.cityname = cityName;
                        dataBean.location = "";
                        dataBean.locationname = "";
                        list.add(0, dataBean);
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
            default:
                break;
        }
    }
}
