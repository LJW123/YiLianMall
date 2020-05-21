package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V5NavigationAdapter;
import com.yilian.mylibrary.MapUtil;
import com.yilianmall.merchant.utils.NumberFormat;

import java.util.ArrayList;

/**
 *  V5导航弹窗
 * @author Ray_L_Pain
 * @date 2018/1/1 0001
 */

public class V5NavigationActivity extends BaseActivity {
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    V5NavigationAdapter adapter;
    ArrayList<String> list = new ArrayList<>();

    private boolean isAMapExists, isBaiduExists, isTencentExists, isSoGouExists;
    private String longitude, latitude, address;
    private double lat, lng, myLat, myLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v5_navigation);
        ViewUtils.inject(this);

        initView();
        initListener();
    }

    private void initView() {
        isAMapExists = getIntent().getBooleanExtra("isAMapExists", false);
        isBaiduExists = getIntent().getBooleanExtra("isBaiduExists", false);
        isTencentExists = getIntent().getBooleanExtra("isTencentExists", false);
        isSoGouExists = getIntent().getBooleanExtra("isSoGouExists", false);
        longitude = getIntent().getStringExtra("lng");
        latitude = getIntent().getStringExtra("lat");
        lat = NumberFormat.convertToDouble(latitude, 0d);
        lng = NumberFormat.convertToDouble(longitude, 0d);
        myLat = getIntent().getDoubleExtra("my_lay", 0d);
        myLng = getIntent().getDoubleExtra("my_lng", 0d);
        address = getIntent().getStringExtra("address");

        Logger.i("2018年1月1日 14:55:31-" + isAMapExists);
        Logger.i("2018年1月1日 14:55:31-" + isBaiduExists);
        Logger.i("2018年1月1日 14:55:31-" + isTencentExists);
        Logger.i("2018年1月1日 14:55:31-" + isSoGouExists);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (adapter == null) {
            adapter = new V5NavigationAdapter(R.layout.item_v5_navigation);
        }
        recyclerView.setAdapter(adapter);
        if (isAMapExists) {
            list.add("高德地图");
        }
        if (isBaiduExists) {
            list.add("百度地图");
        }
        if (isTencentExists) {
            list.add("腾讯地图");
        }
        if (isSoGouExists) {
            list.add("搜狗地图");
        }
        if (list.size() <= 0) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
        }
        adapter.setNewData(list);
        adapter.loadMoreComplete();


        LatLng mLatLng = new LatLng(lat, lng);
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(mLatLng)
                .snippet("lala")
                .draggable(false);
    }

    private MarkerOptions markerOption;

    private void initListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String item = (String) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.item_layout:
                        switch (item) {
                            case "高德地图":
                                MapUtil.jumpToGaoDe(mContext, lat, lng);
                                finish();
                                break;
                            case "百度地图":
                                MapUtil.jumpToBaidu(mContext, markerOption);
                                finish();
                                break;
                            case "腾讯地图":
                                MapUtil.jumpToTencent(mContext, myLat, myLng, lat, lng, address);
                                finish();
                                break;
                            case "搜狗地图":
                                showToast("去搜狗");
                                finish();
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
