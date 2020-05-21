package com.yilian.mall.ctrip.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.umeng.socialize.utils.Log;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.InfoWinAdapter;
import com.yilian.mall.ctrip.bean.MapHotelInfoBean;
import com.yilian.mall.ctrip.mvp.CtripHotelDetailContract;
import com.yilian.mall.ctrip.mvp.presenter.CtripHotelDetailPresenterImpl;
import com.yilian.mall.ctrip.util.MarkOverlayUtil;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.ui.V5NavigationActivity;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MapUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.networkingmodule.entity.Hotel_Rooms_FiltrateBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelFilterEntity;

import io.reactivex.functions.Consumer;
import rx.Subscription;

import static com.alibaba.mobileim.YWChannel.getResources;

/**
 * 作者：马铁超 on 2018/10/31 10:34
 * 地图查看酒店信息
 */

public class MapSearchHotelInfoActivity extends BaseAppCompatActivity implements AMapLocationListener, LocationSource, CtripHotelDetailContract.View {

    private ImageView ivBack;
    private MapView mapviewHotelinfo;
    private AMap aMap;
    private MapHotelInfoBean mapHotelInfoBean;
    private String title, info, grade, checkInTime, checkOutTime, hotelAddress;
    private TextView tvHotelTitle, tvHotelInfo, tvGrade, tvReserve;
    private InfoWinAdapter InfoWinAdapter;
    private double gdLat, gdLong, myLat, mylng;
    private boolean isAMapExists, isTencentExists, isBaiduExists, isSoGouExists;
    private TextView tvNavigation;
    private TextView tvOtherMap;
    private CtripHotelDetailContract.Presenter ctripHotelDetailPresenter;
    private String mHotelId;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    private boolean isFirstLoc = true;
    private boolean isFirstInto = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotelinfo);
        getItentData();
        initView();
        mapviewHotelinfo.onCreate(savedInstanceState);
        initAmap();
        initListener();
        initExists();

    }

    /**
     * 接收数据
     */
    private void getItentData() {
//        infoBean = (MapHotelInfoBean) getIntent().getSerializableExtra("hotelInfoBean");
        mHotelId = getIntent().getStringExtra("hotelId");
    }

    private void startLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mapviewHotelinfo = (MapView) findViewById(R.id.mapview_hotelinfo);
        tvNavigation = (TextView) findViewById(R.id.tv_navigation);
        tvOtherMap = (TextView) findViewById(R.id.tv_other_map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapviewHotelinfo.onPause();
        // 停止定位
        deactivate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapviewHotelinfo.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mapviewHotelinfo != null) {
            mapviewHotelinfo.onDestroy();
            MarkOverlayUtil.clearAllMarker();
        }
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }

    /**
     * 初始化地图数据
     */
    private void initAmap() {
        if (aMap == null) {
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.current_position_point));// 设置小蓝点的图标
            aMap = mapviewHotelinfo.getMap();
            UiSettings settings = aMap.getUiSettings();
//            aMap.setLocationSource(this);//设置了定位的监听
            aMap.setLocationSource(this);
            isFirstInto = false;
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(false);
            aMap.setMyLocationEnabled(false);//显示定位层并且可以触发定位,默认是flase
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            /**
             * 地图绘制完成 请求酒店数据
             */
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    if (ctripHotelDetailPresenter == null) {
                        ctripHotelDetailPresenter = new CtripHotelDetailPresenterImpl(MapSearchHotelInfoActivity.this);
                    }
                    Subscription subscription = ctripHotelDetailPresenter.getCtripHotelDetailData(mContext, mHotelId, "", "");
                    addSubscription(subscription);
                }
            });
        }
    }

    /**
     * 控件点击
     */
    private void initListener() {
        //返回
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });
        // 其他地图
        RxUtil.clicks(tvOtherMap, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                goTo("other");
            }
        });
        //高德地图 导航
        RxUtil.clicks(tvNavigation, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                goTo("gd");
            }
        });
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (isFirstLoc) {
                    isFirstLoc = false;
                    MarkOverlayUtil.setCurrentPositionMark(mContext,aMap,aMapLocation.getLatitude(),aMapLocation.getLongitude());
//                    mListener.onLocationChanged(aMapLocation);
                }
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getAccuracy();//获取精度信息
                myLat = aMapLocation.getLatitude();//获取纬度
                mylng = aMapLocation.getLongitude();//获取经度
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }

        }
    }

    /**
     * 判断手机上的地图是否安装
     */
    private void initExists() {
        if (MapUtil.isAvilible(mContext, Constants.PACKAGE_AMAP_MAP)) {
            isAMapExists = true;
        } else {
            isAMapExists = false;
        }

        if (MapUtil.isAvilible(mContext, Constants.PACKAGE_BAIDU_MAP)) {
            isBaiduExists = true;
        } else {
            isBaiduExists = false;
        }

        if (MapUtil.isAvilible(mContext, Constants.PACKAGE_TENCENT_MAP)) {
            isTencentExists = true;
        } else {
            isTencentExists = false;
        }
        isSoGouExists = false;
    }


    /**
     * 判断用地图打开方式
     *
     * @param type
     */
    private void goTo(String type) {
        if (type.equals("other")) {
            if (isAMapExists || isBaiduExists || isTencentExists || isSoGouExists) {
                showMapDialog();
            } else {
                goToBrowser();
            }
        }
        if (type.equals("gd")) {
            if (isAMapExists) {
                showMapDialog();
            } else {
                goToBrowser();
            }
        }
    }

    /**
     * 浏览器打开地图
     */
    private void goToBrowser() {
        Uri mapUri = Uri.parse(MapUtil.getWebBaiduMapUri(String.valueOf(gdLat), String.valueOf(gdLong), "我的位置",
                String.valueOf(gdLat), String.valueOf(gdLong),
                hotelAddress, PreferenceUtils.readStrConfig(Constants.SPKEY_LOCATION_CITY_NAME, mContext), "益联益家"));
        Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
        startActivity(loction);
    }

    /**
     * 打开手机地图
     */
    private void showMapDialog() {
        Intent intent = new Intent(mContext, V5NavigationActivity.class);
        intent.putExtra("isAMapExists", isAMapExists);
        intent.putExtra("isBaiduExists", isBaiduExists);
        intent.putExtra("isTencentExists", isTencentExists);
        intent.putExtra("isSoGouExists", isSoGouExists);
        intent.putExtra("lat", gdLat + "");
        intent.putExtra("lng", gdLong + "");
        intent.putExtra("my_lat", gdLat + "");
        intent.putExtra("my_lng", gdLong + "");
        intent.putExtra("address", hotelAddress);
        startActivity(intent);
    }


    /**
     * 请求酒店详情回调
     *
     * @param ctripHotelDetailEntity
     */
    @Override
    public void showCtripHotelDetail(CtripHotelDetailEntity ctripHotelDetailEntity) {
        gdLat = ctripHotelDetailEntity.data.gdLat;
        gdLong = ctripHotelDetailEntity.data.gdLng;
        hotelAddress = ctripHotelDetailEntity.data.address;
        InfoWinAdapter = new InfoWinAdapter(mContext, ctripHotelDetailEntity);
        aMap.setInfoWindowAdapter(InfoWinAdapter);
        MarkOverlayUtil.setHotelInfoMark(aMap, gdLat, gdLong);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(gdLat, gdLong)));
    }

    @Override
    public void showCtripFilter(CtripHotelFilterEntity ctripHotelFilterEntity) {
    }

    @Override
    public void resetFilterList(Hotel_Rooms_FiltrateBean ctripRoomTypeInfo) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if(isFirstInto){
            startLocation();
        }
        isFirstInto = true;
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
}
