package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTChooseAddressAdapter;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;

public class MTChooseAddressActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener {
    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker marker;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private ListView lvSearchedBuildings;
    private EditText tvInputBuildings;
    private int currentPage;
    private LatLonPoint latLonPoint;
    private String cityCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtchoose_address);
        initView();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initListener();
    }

    private void initListener() {
        ivLeft1.setOnClickListener(this);
        tvInputBuildings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MTChooseAddressActivity.this, MTSearchAddressActivity.class);
                intent.putExtra("cityCode",cityCode);
                startActivity(intent);
                finish();
            }
        });
        lvSearchedBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem itemAtPosition = (PoiItem) parent.getItemAtPosition(position);
                Intent newIntent = new Intent();
                newIntent.putExtra("PoiItem",itemAtPosition);
                setResult(RESULT_OK,newIntent);
                finish();
            }
        });
    }

    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        mapView = (MapView) findViewById(R.id.map_view);
        lvSearchedBuildings = (ListView) findViewById(R.id.lv_searched_buildings);
        tvInputBuildings = (EditText) findViewById(R.id.tv_input_buildings);
        tvInputBuildings.setFocusable(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(ScreenUtils.getScreenWidth(mContext)/1.8));
        mapView.setLayoutParams(params);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("选择地址");
        tvTitle.setVisibility(View.VISIBLE);
        ivRight2.setVisibility(View.GONE);
    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_input_buildings:

                break;
            case R.id.iv_left1:
                finish();
                break;
        }
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.merchant_point4));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnCameraChangeListener(this);//高德地图获取当前屏幕中心点的经纬度
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));//设置缩放级别
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        // aMap.setMyLocationType()
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            Logger.i("定位地址：long:"+amapLocation.getLongitude()+"  lat:"+amapLocation.getLatitude());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {


    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
        Logger.i(target.latitude + "jinjin------" + target.longitude);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        latLonPoint = new LatLonPoint(target.latitude, target.longitude);
        RegeocodeQuery query1 = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);

        geocoderSearch.getFromLocationAsyn(query1);
//移除marker 添加之前先移除上次添加的marker
        if(marker!=null){
            marker.remove();
        }
//        添加marker
        marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(target));
    }



    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                cityCode = result.getRegeocodeAddress().getCityCode();
                Logger.i("addressName:"+addressName);
                PoiSearch.Query query = new PoiSearch.Query("","商务住宅", cityCode);
                query.setPageSize(20);// 设置每页最多返回多少条poiitem
                query.setPageNum(currentPage);//设置查询页码
                PoiSearch poiSearch = new PoiSearch(mContext, query);
                poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint,5000,true));
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();

//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        AMapUtil.convertToLatLng(latLonPoint), 15));
//                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
//                ToastUtil.show(ReGeocoderActivity.this, addressName);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            ArrayList<PoiItem> pois = result.getPois();
            Logger.i("pois.toString():  "+pois.toString());
            if(pois!=null){
                lvSearchedBuildings.setAdapter(new MTChooseAddressAdapter(mContext,pois));
            }
            if (pois.size()>0) {
                PoiItem poiItem = pois.get(0);
                Logger.i("pois:  poiItem.getAdCode(): "+ poiItem.getAdCode()+"  poiItem.getAdName():"+poiItem.getAdName()+" poiItem.getBusinessArea():" +
                        " "+poiItem.getBusinessArea()+"  poiItem.getCityCode():"+poiItem.getCityCode()
                +"  poiItem.getCityName():"+poiItem.getCityName()+" "+poiItem.getLatLonPoint());
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {

    }
}
