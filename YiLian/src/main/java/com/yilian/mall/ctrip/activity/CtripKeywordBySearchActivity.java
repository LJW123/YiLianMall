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
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripKeywordBySearchAdapter;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.utils.KeyBordUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripKeywordBySearchEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripKeywordModel;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripSiteModel;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripKeywordLayoutType;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 关键字搜索
 *
 * @author Created by Zg on 2018/9/31.
 */

public class CtripKeywordBySearchActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;

    private ImageView ivBack;
    private EditText etSearch;
    private TextView tvSearch;

    private RecyclerView recyclerView;
    private CtripKeywordBySearchAdapter mAdapter;

    private List<String> searchRecordList;

    //搜索关键字
    private String keywordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_keyword_by_search);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setCtripSearch(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(keywordStr)) {
                    varyViewUtils.showLoadingView();
                    saveSearchVal();
                    getKeywordBySearch();
                } else {
                    showToast("请输入搜索内容");
                }
            }
        });
        varyViewUtils.showDataView();

        ivBack = findViewById(R.id.iv_back);
        etSearch = findViewById(R.id.et_search);
        tvSearch = findViewById(R.id.tv_search);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new CtripKeywordBySearchAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        searchRecordList = (List<String>) PreferenceUtils.readObjectConfig(Constants.CTRIP_KEYWORD_SEARCH_RECORDER, context);
        if (searchRecordList == null) {
            searchRecordList = new ArrayList<>();
        }
        keywordStr = getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(keywordStr)) {
            etSearch.setText(keywordStr);
            varyViewUtils.showLoadingView();
            getKeywordBySearch();
        }
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity itemEntity = (MultiItemEntity) adapter.getItem(position);
                switch (itemEntity.getItemType()) {
                    case CtripKeywordLayoutType.location:
                        CtripKeywordBySearchEntity.Location location = (CtripKeywordBySearchEntity.Location) itemEntity;
                        /**
                         * {@link CtripHomePageActivity }
                         */
                        EventBus.getDefault().post(new CtripSiteModel(CtripHomePageActivity.mCtripSite.cityId, CtripHomePageActivity.mCtripSite.cityName, location.location, location.locationname, "0", "0"));
                        EventBus.getDefault().post(new CtripKeywordModel("", "", location.locationname));
                        break;
                    case CtripKeywordLayoutType.zone:
                        CtripKeywordBySearchEntity.Zone zone = (CtripKeywordBySearchEntity.Zone) itemEntity;
                        EventBus.getDefault().post(new CtripKeywordModel(zone.zone, "", zone.zonename));
                        break;
                    case CtripKeywordLayoutType.brand:
                        CtripKeywordBySearchEntity.Brand brand = (CtripKeywordBySearchEntity.Brand) itemEntity;
                        EventBus.getDefault().post(new CtripKeywordModel("", brand.BrandCode, brand.BrandName));
                        break;
                    case CtripKeywordLayoutType.hotel:
                        CtripKeywordBySearchEntity.Hotel hotel = (CtripKeywordBySearchEntity.Hotel) itemEntity;
                        JumpCtripActivityUtils.toCtripHotelDetail(context, hotel.HotelID, null, null);
                        break;
                    default:
                        break;
                }
                AppManager.getInstance().killActivity(CtripKeywordActivity.class);
                finish();
            }
        });

    }

    /**
     * 保存搜索的关键字
     */
    private void saveSearchVal() {
        if (!searchRecordList.contains(keywordStr)) {
            searchRecordList.add(0, keywordStr);
            PreferenceUtils.writeObjectConfig(Constants.CTRIP_KEYWORD_SEARCH_RECORDER, searchRecordList, context);
        }
    }

    /**
     * 获取 酒店搜索结果接口
     */
    @SuppressWarnings("unchecked")
    private void getKeywordBySearch() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getKeywordBySearch("xc_goods/xc_hotel_sort", CtripHomePageActivity.mCtripSite.cityId, keywordStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripKeywordBySearchEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CtripKeywordBySearchEntity entity) {
                        List<MultiItemEntity> list = new ArrayList<>();
                        if (entity.location != null && entity.location.size() > 0) {
                            list.addAll(entity.location);
                        }
                        if (entity.brand != null && entity.brand.size() > 0) {
                            list.addAll(entity.brand);
                        }
                        if (entity.zone != null && entity.zone.size() > 0) {
                            list.addAll(entity.zone);
                        }
                        if (entity.hotel != null && entity.hotel.size() > 0) {
                            list.addAll(entity.hotel);
                        }
                        if(list.size()>0) {
                            mAdapter.setNewData(list);
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
                keywordStr = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(keywordStr)) {
                    varyViewUtils.showLoadingView();
                    saveSearchVal();
                    getKeywordBySearch();
                } else {
                    showToast("请输入搜索内容");
                }
                break;
            default:
                break;
        }
    }


}
