package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripKeywordAdapter;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ctrip.widget.GridItemDecoration;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripKeywordEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripKeywordModel;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripSiteModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 商圈
 *
 * @author Created by Zg on 2018/9/31.
 */

public class CtripKeywordActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;

    private LinearLayout llSearch;


    private CardView cvRecord, cvBrand, cvZone, cvLocation;
    private TextView tvClear;
    private ImageView ivBrandMore, ivZoneMore, ivLocationMore;
    private RecyclerView rvRecord, rvBrand, rvZone, rvLocation;
    private CtripKeywordAdapter recordAdapter, brandAdapter, zoneAdapter, locationAdapter;

    private List<String> searchRecordList;

    private List<MultiItemEntity> brandShow = new ArrayList<>();
    private List<MultiItemEntity> brandAll = new ArrayList<>();
    private boolean brandShowAll = false;
    private List<MultiItemEntity> zoneShow = new ArrayList<>();
    private List<MultiItemEntity> zoneAll = new ArrayList<>();
    private boolean zoneShowAll = false;
    private List<MultiItemEntity> locationShow = new ArrayList<>();
    private List<MultiItemEntity> locationAll = new ArrayList<>();
    private boolean locationShowAll = false;
    //默认展示数量
    private int defaultShow = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_keyword);
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
                initData();
            }
        });

        ivBack = findViewById(R.id.iv_back);
        llSearch = findViewById(R.id.ll_search);

        //记录
        cvRecord = findViewById(R.id.cv_record);
        tvClear = findViewById(R.id.tv_clear);
        rvRecord = findViewById(R.id.rv_record);
        recordAdapter = new CtripKeywordAdapter();
        rvRecord.setLayoutManager(new GridLayoutManager(context, 4));
        rvRecord.setNestedScrollingEnabled(false);
        rvRecord.addItemDecoration(new GridItemDecoration(context, 1));
        rvRecord.setAdapter(recordAdapter);
        //品牌
        cvBrand = findViewById(R.id.cv_brand);
        ivBrandMore = findViewById(R.id.iv_brand_more);
        rvBrand = findViewById(R.id.rv_brand);
        brandAdapter = new CtripKeywordAdapter();
        rvBrand.setLayoutManager(new GridLayoutManager(context, 4));
        rvBrand.setNestedScrollingEnabled(false);
        rvBrand.addItemDecoration(new GridItemDecoration(context, 1));
        rvBrand.setAdapter(brandAdapter);
        //商圈
        cvZone = findViewById(R.id.cv_zone);
        ivZoneMore = findViewById(R.id.iv_zone_more);
        rvZone = findViewById(R.id.rv_zone);
        zoneAdapter = new CtripKeywordAdapter();
        rvZone.setLayoutManager(new GridLayoutManager(context, 4));
        rvZone.setNestedScrollingEnabled(false);
        rvZone.addItemDecoration(new GridItemDecoration(context, 1));
        rvZone.setAdapter(zoneAdapter);
        //行政区域
        cvLocation = findViewById(R.id.cv_location);
        ivLocationMore = findViewById(R.id.iv_location_more);
        rvLocation = findViewById(R.id.rv_location);
        locationAdapter = new CtripKeywordAdapter();
        rvLocation.setLayoutManager(new GridLayoutManager(context, 4));
        rvLocation.setNestedScrollingEnabled(false);
        rvLocation.addItemDecoration(new GridItemDecoration(context, 1));
        rvLocation.setAdapter(locationAdapter);

    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        ivBrandMore.setOnClickListener(this);
        ivZoneMore.setOnClickListener(this);
        ivLocationMore.setOnClickListener(this);

        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripKeywordEntity.Record record = (CtripKeywordEntity.Record) adapter.getItem(position);
                JumpCtripActivityUtils.toCtripKeywordBySearch(context, record.name);
            }
        });
        brandAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripKeywordEntity.Brand brand = (CtripKeywordEntity.Brand) adapter.getItem(position);
                EventBus.getDefault().post(new CtripKeywordModel("", brand.BrandCode, brand.BrandName));
                finish();
            }
        });
        zoneAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripKeywordEntity.Zone zone = (CtripKeywordEntity.Zone) adapter.getItem(position);
                EventBus.getDefault().post(new CtripKeywordModel(zone.zone, "", zone.zonename));
                finish();
            }
        });
        locationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripKeywordEntity.Location location = (CtripKeywordEntity.Location) adapter.getItem(position);
                /**
                 * {@link CtripHomePageActivity }
                 */
                EventBus.getDefault().post(new CtripSiteModel(CtripHomePageActivity.mCtripSite.cityId, CtripHomePageActivity.mCtripSite.cityName, location.location, location.locationname, "0", "0"));
                EventBus.getDefault().post(new CtripKeywordModel("", "", location.locationname));
                finish();
            }
        });
    }

    public void initData() {
        getCityListData();
        searchRecordList = (List<String>) PreferenceUtils.readObjectConfig(Constants.CTRIP_KEYWORD_SEARCH_RECORDER, context);
        if (searchRecordList != null && searchRecordList.size() > 0) {
            List<MultiItemEntity> list = new ArrayList<>();
            for (int i = 0; i < defaultShow && i < searchRecordList.size(); i++) {
                list.add(new CtripKeywordEntity.Record(searchRecordList.get(i)));
            }
            recordAdapter.setNewData(list);
            cvRecord.setVisibility(View.VISIBLE);
        } else {
            cvRecord.setVisibility(View.GONE);
        }
    }


    /**
     * 获取 城市列表
     */
    @SuppressWarnings("unchecked")
    private void getCityListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getSearchInfo("xc_goods/xc_search_info", CtripHomePageActivity.mCtripSite.cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripKeywordEntity>() {
                    @Override
                    public void onCompleted() {
                        varyViewUtils.showDataView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CtripKeywordEntity entity) {
                        //品牌
                        if (entity.brand != null && entity.brand.size() > 0) {
                            brandAll.addAll(entity.brand);
                            if (brandAll.size() > defaultShow) {
                                ivBrandMore.setVisibility(View.VISIBLE);
                                for (int i = 0; i < defaultShow; i++) {
                                    brandShow.add(brandAll.get(i));
                                }
                            } else {
                                ivBrandMore.setVisibility(View.GONE);
                                brandShow.addAll(brandAll);
                            }
                            brandAdapter.setNewData(brandShow);
                            cvBrand.setVisibility(View.VISIBLE);
                        } else {
                            cvBrand.setVisibility(View.GONE);
                        }
                        //商圈
                        if (entity.zone != null && entity.zone.size() > 0) {
                            zoneAll.addAll(entity.zone);
                            if (zoneAll.size() > defaultShow) {
                                ivZoneMore.setVisibility(View.VISIBLE);
                                for (int i = 0; i < defaultShow; i++) {
                                    zoneShow.add(zoneAll.get(i));
                                }
                            } else {
                                ivZoneMore.setVisibility(View.GONE);
                                zoneShow.addAll(zoneAll);
                            }
                            zoneAdapter.setNewData(zoneShow);
                            cvZone.setVisibility(View.VISIBLE);
                        } else {
                            cvZone.setVisibility(View.GONE);
                        }
                        //行政区域
                        if (entity.location != null && entity.location.size() > 0) {
                            locationAll.addAll(entity.location);
                            if (locationAll.size() > defaultShow) {
                                ivLocationMore.setVisibility(View.VISIBLE);
                                for (int i = 0; i < defaultShow; i++) {
                                    locationShow.add(locationAll.get(i));
                                }
                            } else {
                                ivLocationMore.setVisibility(View.GONE);
                                locationShow.addAll(locationAll);
                            }
                            locationAdapter.setNewData(locationShow);
                            cvLocation.setVisibility(View.VISIBLE);
                        } else {
                            cvLocation.setVisibility(View.GONE);
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
            case R.id.ll_search:
                //跳转搜索页面
                JumpCtripActivityUtils.toCtripKeywordBySearch(context, null);
                break;
            case R.id.tv_clear:
                //清除搜索记录
                if (searchRecordList == null) {
                    searchRecordList = new ArrayList<>();
                }
                searchRecordList.clear();
                PreferenceUtils.writeObjectConfig(Constants.CTRIP_KEYWORD_SEARCH_RECORDER, searchRecordList, context);
                cvRecord.setVisibility(View.GONE);
                break;
            case R.id.iv_brand_more:
                //品牌 更多
                brandShowAll = !brandShowAll;
                if (brandShowAll) {
                    ivBrandMore.setImageResource(R.mipmap.ctrip_keyword_more_unfold);
                    brandAdapter.setNewData(brandAll);
                } else {
                    ivBrandMore.setImageResource(R.mipmap.ctrip_keyword_more);
                    brandAdapter.setNewData(brandShow);
                }
                break;
            case R.id.iv_zone_more:
                //商圈 更多
                zoneShowAll = !zoneShowAll;
                if (zoneShowAll) {
                    ivZoneMore.setImageResource(R.mipmap.ctrip_keyword_more_unfold);
                    zoneAdapter.setNewData(zoneAll);
                } else {
                    ivZoneMore.setImageResource(R.mipmap.ctrip_keyword_more);
                    zoneAdapter.setNewData(zoneShow);
                }
                break;
            case R.id.iv_location_more:
                //行政区 更多
                locationShowAll = !locationShowAll;
                if (locationShowAll) {
                    ivLocationMore.setImageResource(R.mipmap.ctrip_keyword_more_unfold);
                    locationAdapter.setNewData(locationAll);
                } else {
                    ivLocationMore.setImageResource(R.mipmap.ctrip_keyword_more);
                    locationAdapter.setNewData(locationShow);
                }
                break;

            default:
                break;
        }
    }
}
