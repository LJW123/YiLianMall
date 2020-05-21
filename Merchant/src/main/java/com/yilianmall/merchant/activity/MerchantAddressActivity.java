package com.yilianmall.merchant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
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
import com.bigkoo.pickerview.OptionsPickerView;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.MerchantAddressInfo;
import com.yilian.networkingmodule.entity.RegionEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.NumberFormat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MerchantAddressActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, View.OnClickListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {

    CameraUpdate cameraUpdate;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private TextView tvKey;
    private TextView tvValue;
    private EditText etMerchantAddress;
    private MapView mapView;
    private Button btnSaveAddress;
    private double longitude;
    private double finalLongitude;
    private double latitude;
    private double finalLatitude;
    private AMap aMap;
    private ArrayList<RegionEntity.ProvincesBean> provinceList = new ArrayList<>();
    private ArrayList<ArrayList<RegionEntity.ProvincesBean.CitysBean>> cityList = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<RegionEntity.ProvincesBean.CitysBean.CountysBean>>> countyList = new ArrayList<>();
    private String provinceId;
    private String cityId;
    private String countyId;
    private OnLocationChangedListener mListener;
    private GeocodeSearch geocoderSearch;
    private LatLonPoint latLonPoint;
    private Marker marker;
    private int count = 0;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private String addressName;
    private String cityCode;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_address);
        initView();
        init();
        initData();
        initListener();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("门店地址");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        tvKey = (TextView) findViewById(R.id.tv_key);
        tvKey.setText("所在区域:");
        tvValue = (TextView) findViewById(R.id.tv_value);
        tvValue.setVisibility(View.VISIBLE);
        tvValue.setHint("请选择省市区");
        etMerchantAddress = (EditText) findViewById(R.id.et_merchant_address);
        mapView = (MapView) findViewById(R.id.map_view);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenWidth(mContext));
//        mapView.setLayoutParams(params);
        btnSaveAddress = (Button) findViewById(R.id.btn_save_address);

        v3Back.setOnClickListener(this);
        tvValue.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnSaveAddress.setOnClickListener(this);
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

    private void initData() {
        getMerchantAddress();
        getRegion();
    }

    private void initListener() {
        etMerchantAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });
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
        Logger.i("ray走了这里-显示定位点");
    }

    private void getMerchantAddress() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantAddressInfo(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), new Callback<MerchantAddressInfo>() {
                    @Override
                    public void onResponse(Call<MerchantAddressInfo> call, Response<MerchantAddressInfo> response) {
                        stopMyDialog();
                        MerchantAddressInfo body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (body.code == 0) {
                                tvValue.setClickable(true);
                                tvValue.setEnabled(true);
                                return;
                            }
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        //有保存的区域的时候不可在重复选择
                                        tvValue.setClickable(false);
                                        tvValue.setEnabled(false);
                                        setData(body);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantAddressInfo> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    /**
     * 获取省市县
     */
    private void getRegion() {
        RetrofitUtils.getInstance(mContext).getRegion(new Callback<RegionEntity>() {

            private List<RegionEntity.ProvincesBean> list;

            @Override
            public void onResponse(Call<RegionEntity> call, Response<RegionEntity> response) {
                RegionEntity body = response.body();
                if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    showToast(R.string.network_module_service_exception);
                    return;
                }
                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                    switch (body.code) {
                        case 1:
                            list = body.list;
                            for (int i = 0; i < list.size(); i++) {
                                RegionEntity.ProvincesBean provinceBean = list.get(i);
                                if (provinceBean != null && provinceBean.citys != null) {
                                    provinceList.add(provinceBean);//省份列表
                                    cityList.add(provinceBean.citys);//市列表
                                    ArrayList<ArrayList<RegionEntity.ProvincesBean.CitysBean.CountysBean>> countysBeen = new ArrayList<>();
                                    for (int j = 0; j < provinceBean.citys.size(); j++) {
                                        if (provinceBean.citys.get(j).countys != null) {
                                            countysBeen.add(provinceBean.citys.get(j).countys);
                                        } else {
                                            //预防 区为null时 轮子崩溃
                                            RegionEntity.ProvincesBean.CitysBean.CountysBean bean = new RegionEntity.ProvincesBean.CitysBean.CountysBean();
                                            bean.regionId = "";
                                            bean.parentId = "";
                                            bean.regionType = "-1";
                                            bean.regionName = "未知";
                                            bean.agencyId = "";
                                            ArrayList<RegionEntity.ProvincesBean.CitysBean.CountysBean> list = new ArrayList<>();
                                            list.add(bean);
                                            countysBeen.add(list);
                                        }
                                    }
                                    countyList.add(countysBeen);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<RegionEntity> call, Throwable t) {

            }
        });
    }

    private void setData(MerchantAddressInfo body) {
        MerchantAddressInfo.InfoBean info = body.info;
        String merchantProvince = info.merchantProvince;
        if (!TextUtils.isEmpty(merchantProvince)) {
            MerchantAddressInfo.InfoBean.RegionBean region = info.region;
            tvValue.setText(region.province.name + region.city.name + region.county.name);
            provinceId = region.province.id;
            cityId = region.city.id;
            countyId = region.county.id;
        }
        String merchantAddress = info.merchantAddress;
        if (!TextUtils.isEmpty(merchantAddress)) {
            etMerchantAddress.setText(merchantAddress);
            etMerchantAddress.setSelection(merchantAddress.length());
        }

        String merchantLongitude = info.merchantLongitude;
        String merchantLatitude = info.merchantLatitude;
        if (!TextUtils.isEmpty(merchantLongitude) && !TextUtils.isEmpty(merchantLatitude)) {
            longitude = NumberFormat.convertToDouble(merchantLongitude, 0d);
            latitude = NumberFormat.convertToDouble(merchantLatitude, 0d);
            //设置一个红色的坐标为之前定位的店铺
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            // 设置Marker的坐标，为我们点击地图的经纬度坐标
            markerOptions.position(latLng);
            // 设置Marker点击之后显示的标题
            markerOptions.title("您的店铺");
            // 设置Marker的图标样式
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // 设置Marker是否可以被拖拽，
            markerOptions.draggable(false);
            // 设置Marker的可见性
            markerOptions.visible(true);
            //将Marker添加到地图上去
            Marker marker = aMap.addMarker(markerOptions);
            marker.showInfoWindow();
            //把屏幕中心移到坐标点
            cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 17, 0, 30));
            aMap.moveCamera(cameraUpdate);
            Logger.i("ray走了这里-显示老位置");
        }
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_value) {
            showArea();
        } else if (i == R.id.btn_save_address) {
            submit();
        }
    }

    /**
     * 弹出省市县选择器
     */
    private void showArea() {
        if (provinceList == null || provinceList.size() <= 0 || cityList == null || cityList.size() <= 0 || countyList == null || countyList.size() <= 0) {
            showToast("请稍后再试");
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                RegionEntity.ProvincesBean provinceBean = provinceList.get(options1);
                provinceId = provinceBean.regionId;
                RegionEntity.ProvincesBean.CitysBean citysBean = cityList.get(options1).get(options2);
                cityId = citysBean.regionId;
                RegionEntity.ProvincesBean.CitysBean.CountysBean countysBean = countyList.get(options1).get(options2).get(options3);
                countyId = countysBean.regionId;
                tvValue.setText(provinceBean.getPickerViewText() + citysBean.getPickerViewText() + countysBean.getPickerViewText());
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(provinceList, cityList, countyList);//三级选择器
        pvOptions.show();
    }

    private void submit() {
        // validate
        String value = tvValue.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(this, "请选择省市区", Toast.LENGTH_SHORT).show();
            return;
        }

        String address = etMerchantAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请输入门店详细地址信息", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        setMerchantAddress(address);
    }

    private void setMerchantAddress(String address) {
        Logger.i("ray-end-" + longitude);
        Logger.i("ray-end-" + latitude);
        Logger.i("经度" + longitude + "纬度:" + latitude);
        Logger.i("经度" + finalLongitude + "纬度:" + finalLatitude);
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .setMerchantAddress(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), provinceId, cityId, countyId, address, String.valueOf(finalLongitude), String.valueOf(finalLatitude), new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        stopMyDialog();
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        showToast("保存成功");
                                        AppManager.getInstance().killActivity(MerchantEditInformationActivity.class);
                                        finish();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Logger.i("ray-" + "走了onLocationChanged");
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                longitude = aMapLocation.getLongitude();
                latitude = aMapLocation.getLatitude();
                Logger.i("经度" + longitude + "纬度:" + latitude);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
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
        RegeocodeQuery query1 = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query1);

        if (marker != null) {
            marker.remove();
        }
        //添加marker
        marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(target));
        finalLatitude = target.latitude;
        finalLongitude = target.longitude;
        if (count <= 2) {
            marker.setVisible(false);
        } else {
            marker.setVisible(true);
        }
        count++;
        Logger.i("ray走了这里-标记");
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
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
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {

        if (rCode == 1000) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                    && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                cityCode = regeocodeResult.getRegeocodeAddress().getCityCode();
                Logger.i("addressName:" + addressName);
                PoiSearch.Query query = new PoiSearch.Query("", "商务住宅", cityCode);
                query.setPageSize(20);// 设置每页最多返回多少条poiitem
                query.setPageNum(currentPage);//设置查询页码
                PoiSearch poiSearch = new PoiSearch(mContext, query);
                poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 5000, true));
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
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {

//        if (rCode == 1000) {
//            ArrayList<PoiItem> pois = poiResult.getPois();
//            Logger.i("pois.toString():  "+pois.toString());
//            if(pois!=null){
//                lvSearchedBuildings.setAdapter(new MTChooseAddressAdapter(mContext,pois));
//            }
//            if (pois.size()>0) {
//                PoiItem poiItem = pois.get(0);
//                Logger.i("pois:  poiItem.getAdCode(): "+ poiItem.getAdCode()+"  poiItem.getAdName():"+poiItem.getAdName()+" poiItem.getBusinessArea():" +
//                        " "+poiItem.getBusinessArea()+"  poiItem.getCityCode():"+poiItem.getCityCode()
//                        +"  poiItem.getCityName():"+poiItem.getCityName()+" "+poiItem.getLatLonPoint());
//            }
//
//        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
