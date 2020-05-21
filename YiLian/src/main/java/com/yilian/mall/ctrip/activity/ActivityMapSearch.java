package com.yilian.mall.ctrip.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.layoutManager.FullyLinearLayoutManager;
import com.yilian.mall.ctrip.adapter.CtripHomeListAdapter;
import com.yilian.mall.ctrip.bean.FiltrateEventBean;
import com.yilian.mall.ctrip.bean.MarketBean;
import com.yilian.mall.ctrip.bean.MenuListBean;
import com.yilian.mall.ctrip.bean.PriceAndLevelSelectBean;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ctrip.util.MarkOverlayUtil;
import com.yilian.mall.ctrip.widget.dropdownmenu.DropDownMenu;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.networkingmodule.entity.SearchFilterBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripKeywordModel;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripPriceModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.utils.StringUtils;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static com.vondear.rxtools.view.RxToast.showToast;


/**
 * 作者：马铁超 on 2018/9/19 17:37
 * 地图展示 酒店数据  默认显示前20 家
 *
 */

public class ActivityMapSearch extends BaseAppCompatActivity implements MyItemClickListener, AMap.InfoWindowAdapter, LocationSource, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, AMapLocationListener, PoiSearch.OnPoiSearchListener {


    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    MapView mapView;
    RelativeLayout rlContentShow;
    DropDownMenu dropDownMenu;
    TextView v3Title;
    ImageView v3Back;
    List<String> getStarLevelId = new ArrayList<>();
    private RecyclerView rv_hotel_search_content;
    //AMap是地图对象
    private AMap aMap;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mListener对象，定位监听器
    private OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private List<MenuListBean> menuList = new ArrayList<>();
    private List<SearchFilterBean.ComSortBean> comSortBeans;
    private List<SearchFilterBean.DistSortBean> distSortBeans;
    private List<SearchFilterBean.PSortBean> pSortBeans;
    private List<SearchFilterBean.SSortBean> sSortBeans;
    private List<View> popupViews = new ArrayList<>();
    private View fifthView;
    private String addressName;
    private String cityCode;
    private int currentPage;
    private LatLonPoint latLonPoint;
    private GeocodeSearch geocoderSearch;
    private AMapLocationClient mlocationClient;
    /**
     * 添加多个market
     */
    private List<MarketBean> marketList = new ArrayList<>();
    private LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
    //  默认排序由近到远
    private String cityid = "", comsort = "Distance ASC", distance = "50", zoneid = "", locationid = "", minprice = "0", maxprice = "100000000", starlevel = "", themeid = "", brandid = "", facilityid = "", bedid = "", grade = "", keyword = "", gdLat = "", gdLng = "", pageCount = "20";
    private int page = Constants.PAGE_INDEX;
    private CtripHomeListAdapter listAdapter;
    private CtripHotelListEntity hotelListEntity;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private CtripKeywordModel mCtripKeyword;
    private CtripPriceModel mCtripPrice;
    private SearchFilterBean filterDataBean;
    private String distSortKey = "", filtrateKey = "", getZoneId, getBrandId, getMinePrice, getMaxPrice, getMinRange, getMaxRange, getPriceAndLeavelResult, comSortShow, distanceShow, filtrateShow;
    private ActivityFilterSec secView;
    private ActivityFiltrateOne filtrateOne;
    private ActivityFiltrateFour filtrate_four_view;
    //currentLat获取当前位置纬度 获取当前位置经度
    double currentLat, currentLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview_search);
        getIntentData();
        initView();
        initListener();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initData();
        initAmap();

    }


    /**
     * 获取传过来的数据
     */
    private void getIntentData() {
        //  城市id
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.cityId)) {
            cityid = CtripHomePageActivity.mCtripSite.cityId;
        } else {
            cityid = "0";
        }
//      区县id
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.countryId)) {
            locationid = CtripHomePageActivity.mCtripSite.countryId;
        } else {
            locationid = "";
        }
        // 经纬度
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.gdLat)) {
            gdLat = CtripHomePageActivity.mCtripSite.gdLat;
        } else {
            gdLat = "0";
        }
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.gdLng)) {
            gdLng = CtripHomePageActivity.mCtripSite.gdLng;
        } else {
            gdLng = "0";
        }
        mCtripKeyword = (CtripKeywordModel) getIntent().getSerializableExtra("CtripKeywordModel");
        mCtripPrice = (CtripPriceModel) getIntent().getSerializableExtra("CtripPriceModel");
        filterDataBean = (SearchFilterBean) getIntent().getSerializableExtra("FiltrateData");
        if (mCtripKeyword != null) {
            String name = mCtripKeyword.showName;
//            商业区id
            getZoneId = mCtripKeyword.zoneId;
//            品牌id
            getBrandId = mCtripKeyword.brandId;
            if (!StringUtils.isEmpty(getBrandId)) {
                brandid = getBrandId;
            }
            if (!StringUtils.isEmpty(getZoneId)) {
                zoneid = getZoneId;
            }
            if (!StringUtils.isEmpty(getZoneId) && !StringUtils.isEmpty(locationid)) {
                locationid = "";
            }
        }
        if (mCtripPrice != null) {
//          最低价
            getMinePrice = mCtripPrice.minprice;
//           最高价
            getMaxPrice = mCtripPrice.maxprice;
//          星级id
            getStarLevelId = mCtripPrice.starlevel;
            if (!StringUtils.isEmpty(getMinePrice)) {
                minprice = getMinePrice;
            }
            if (!StringUtils.isEmpty(getMaxPrice)) {
                maxprice = getMaxPrice;
            }
            if (!StringUtils.isEmpty(mCtripPrice.showStr)) {
                getPriceAndLeavelResult = mCtripPrice.showStr;
            }
            if (getStarLevelId != null && getStarLevelId.size() > 0) {
                starlevel = getStarLevelId.toString();
                starlevel = starlevel.replace("[", "").trim();
                starlevel = starlevel.replace("]", "").trim();
            }
        }
    }

    private void initListener() {

        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JumpCtripActivityUtils.toCtripHotelDetail(mContext, hotelListEntity.data.get(position).HotelID, null, null);
            }
        });


        v3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(v);
            }
        });

        //      recyclerView滑动监听
        rv_hotel_search_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
//              newState=0时，RecyclerView在停止状态中
                    if (newState == SCROLL_STATE_IDLE) {
                        int currentPosition = 0;
                        if (recyclerView != null && recyclerView.getChildCount() > 0) {
                            currentPosition = ((RecyclerView.LayoutParams) recyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                            Logger.i("currentPosition------------->" + currentPosition);
                        }
                        //获取当前RecyclerView完全显示出的最后一个条目的位置
                        int mLastCompletelyVisibleItemPosition = fullyLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                        //获取当前RecyclerView完全显示出的第一个条目的位置
                        int mFirstCompletelyVisibleItemPosition = fullyLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        BigDecimal maxlat = new BigDecimal(hotelListEntity.data.get(currentPosition).gdLat);
                        double lat = maxlat.doubleValue();
                        BigDecimal maxlng = new BigDecimal(hotelListEntity.data.get(currentPosition).gdLng);
                        double lng = maxlng.doubleValue();
                        MarkOverlayUtil.filtrateMarker(new LatLng(lat, lng), aMap);
                        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));

                    }
                } catch (Exception e) {
                    Logger.e("RecyclerView_Scroll_Exception", e);
                    showToast("出现错误，请刷新地图");
                }
            }
        });
    }

    private void initView() {
        dropDownMenu = findViewById(R.id.dropDownMenu);
        v3Title = findViewById(R.id.v3Title);
        v3Back = findViewById(R.id.v3Back);
        fifthView = LayoutInflater.from(mContext).inflate(R.layout.view_map_content, null);
        mapView = fifthView.findViewById(R.id.mapview);
        rv_hotel_search_content = fifthView.findViewById(R.id.rv_hotel_search_content);
        v3Title.setText("展示前20家");
        rv_hotel_search_content.setFocusable(false);
        rv_hotel_search_content.setNestedScrollingEnabled(false);
        fullyLinearLayoutManager = new FullyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv_hotel_search_content.setLayoutManager(fullyLinearLayoutManager);
        listAdapter = new CtripHomeListAdapter();
        rv_hotel_search_content.setAdapter(listAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rv_hotel_search_content);
    }

    private void initData() {
        MenuListBean menuListBean = new MenuListBean();
        menuListBean.setTitle("智能排序");
        menuListBean.setCheck(false);
        MenuListBean menuListBean1 = new MenuListBean();
        menuListBean1.setTitle("位置距离");
        menuListBean1.setCheck(false);
        MenuListBean menuListBean2 = new MenuListBean();
        menuListBean2.setTitle("价格/星级");
        menuListBean2.setCheck(false);
        MenuListBean menuListBean3 = new MenuListBean();
        menuListBean3.setTitle("筛选");
        menuListBean3.setCheck(false);
        menuList.add(menuListBean);
        menuList.add(menuListBean1);
        menuList.add(menuListBean2);
        menuList.add(menuListBean3);
        getFiltrateData();
    }

    private void initAmap() {
        if (aMap == null) {
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            //            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.current_position_point));// 设置小蓝点的图标
            // 搜索范围圈 样式
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
            aMap = mapView.getMap();
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听
            aMap.setOnCameraChangeListener(this);//高德地图获取当前屏幕中心点的经纬度
            // 地图的定位标志是否可见
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            // 当地图绘制完成 请求接口
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
                }
            });
            //marker 点击事件
            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (!marker.getObject().equals("curruntPosition")) {
                        MarkOverlayUtil.resetMarkers(marker, aMap);
                    }
                    rv_hotel_search_content.setVisibility(View.VISIBLE);
//                    LatLng  latLng =  marker.getPosition();
                    Object id = marker.getObject();
                    getAddress(id);
                    return false;
                }
            });
        }
    }

    private void location() {
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


    //  获取筛选条件数据
    private void getFiltrateData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSarchCriteria("xc_goods/xc_sort_condition", cityid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchFilterBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SearchFilterBean allDataBean) {
                        String msg = allDataBean.getMsg();
                        int code = allDataBean.getCode();
                        if (code == 1) {
                            if (filterDataBean != null) {
                                comSortBeans = filterDataBean.getComSort();
                                distSortBeans = filterDataBean.getDistSort();
                                pSortBeans = filterDataBean.getPSort();
                                sSortBeans = filterDataBean.getSSort();
                            } else {
                                comSortBeans = allDataBean.getComSort();
                                distSortBeans = allDataBean.getDistSort();
                                pSortBeans = allDataBean.getPSort();
                                sSortBeans = allDataBean.getSSort();
                            }
                            initPopuContent();
                            getFiltrateShowStr();
                        } else {
                            ToastUtil.showMessage(mContext, msg);
                        }
                    }
                });
        addSubscription(subscription);
    }

    //  获取酒店数据
    private void getListData(String cityid, String comsort, String distance, String zoneid, String locationid, String minprice, String maxprice, String starlevel, String themeid, String brandid, String facilityid, String bedid, String grade, String keyword, String gdLat, String gdLng, int page, String pageCount) {
        startMyDialog();
        /*CtripHomePageActivity.mCtripSite.cityId*/
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getHotelList("xc_goods/xc_hotel_list", cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelListEntity>() {

                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CtripHotelListEntity entity) {
                        hotelListEntity = entity;
                        setHotelListDataToView(hotelListEntity);
                    }
                });
        addSubscription(subscription);
    }

    //  获取当前marker点击的 酒店详情
    private void getAddress(Object id) {
//      点击marker 移动列表到指定位置
        if (hotelListEntity != null && hotelListEntity.data.size() > 0) {
            for (int i = 0; i < hotelListEntity.data.size(); i++) {
                if (id.equals(hotelListEntity.data.get(i).HotelName + hotelListEntity.data.get(i).HotelID)) {
                    fullyLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                    fullyLinearLayoutManager.setStackFromEnd(true);
                }
            }
        }
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
        }
    }

    @SuppressLint("WrongViewCast")
    private void initPopuContent() {
        //智能排序
        // 默认综合排序选中
        filtrateOne = new ActivityFiltrateOne(mContext, comSortBeans);
        filtrateOne.setListener(this);
        filtrateOne.setDefaultSelect(comsort);
        popupViews.add(filtrateOne.oneView());
        menuList.get(0).setCheck(true);
        menuList.get(0).setTitle("综合排序");
        //位置距离
        distanceShow = "";
        String tag = "";
        if (!StringUtils.isEmpty(getZoneId) || !StringUtils.isEmpty(locationid)) {
            if (distSortBeans != null && distSortBeans.size() > 0) {
                for (int i = 0; i < distSortBeans.size(); i++) {
                    if (!StringUtils.isEmpty(getZoneId)) {
                        //商业区
                        if ("zoneid".equals(distSortBeans.get(i).getKey())) {
                            distSortBeans.get(i).setCheck(true);
                            for (int j = 0; j < distSortBeans.get(i).getSorts().size(); j++) {
                                if (distSortBeans.get(i).getSorts().get(j).sorts.equals(getZoneId)) {
                                    distSortBeans.get(i).setCircleShow(true);
                                    distSortBeans.get(i).getSorts().get(j).setCheck(true);
                                    distanceShow = distSortBeans.get(i).getSorts().get(j).getTitle();
                                    tag = "1";
                                }
                            }
                        }
                    }
                    //行政区
                    if (!StringUtils.isEmpty(locationid)) {
                        if ("locationid".equals(distSortBeans.get(i).getKey())) {
                            distSortBeans.get(i).setCheck(true);
                            for (int j = 0; j < distSortBeans.get(i).getSorts().size(); j++) {
                                if (distSortBeans.get(i).getSorts().get(j).sorts.equals(locationid)) {
                                    distSortBeans.get(i).setCircleShow(true);
                                    distSortBeans.get(i).getSorts().get(j).setCheck(true);
                                    distanceShow = distSortBeans.get(i).getSorts().get(j).getTitle();
                                    tag = "2";
                                }
                            }
                        }
                    }
                }
            }
        }
        secView = new ActivityFilterSec(mContext, distSortBeans, tag);
        secView.setListener(this);
        popupViews.add(secView.secView());
        //星级价格
        PriceAndLevelSelectBean priceAndLevelSelectBean = null;
        if (!StringUtils.isEmpty(getMaxPrice) && !StringUtils.isEmpty(getMinePrice) || getStarLevelId.size() > 0) {
            priceAndLevelSelectBean = new PriceAndLevelSelectBean();
            priceAndLevelSelectBean.MaxPrice = getMaxPrice;
            priceAndLevelSelectBean.MinPrice = getMinePrice;
            priceAndLevelSelectBean.levelId = getStarLevelId;
            priceAndLevelSelectBean.minRange = getMinePrice;
            priceAndLevelSelectBean.maxRange = getMaxPrice;
        }
        ActivityFilterThird thirdView = new ActivityFilterThird(mContext, pSortBeans, priceAndLevelSelectBean);
        thirdView.setListener(this);
        popupViews.add(thirdView.thirdView());
        //综合筛选
        //      从上个页面传过来的品牌id
        filtrateShow = "";
        if (!StringUtils.isEmpty(getBrandId)) {
            if (sSortBeans != null && sSortBeans.size() > 0) {
                for (int i = 0; i < sSortBeans.size(); i++) {
                    if ("brandid".equals(sSortBeans.get(i).key)) {
                        for (int j = 0; j < sSortBeans.get(i).getSorts().size(); j++) {
                            if (sSortBeans.get(i).getSorts().get(j).getSorts().equals(getBrandId)) {
                                sSortBeans.get(i).getSorts().get(j).setCheck(true);
                                filtrateShow = sSortBeans.get(i).getSorts().get(j).getTitle();
                            }
                        }
                    }
                }
            }
        }
        filtrate_four_view = new ActivityFiltrateFour(mContext, sSortBeans);
        filtrate_four_view.setListener(this);
        popupViews.add(filtrate_four_view.fourView());
        dropDownMenu.setDropDownMenu(menuList, popupViews, fifthView);
        getIntentResetTab();
    }

    //  获取每个筛选条件显示内容
    private void getFiltrateShowStr() {
        if (comSortBeans != null && comSortBeans.size() > 0) {
            for (int i = 0; i < comSortBeans.size(); i++) {
                if (comSortBeans.get(i).isCheck() == true) {
                    comSortShow = comSortBeans.get(i).getTitle();
                    comsort = comSortBeans.get(i).getSorts();
                }
            }
        }
        distSortKey = "";
        if (distSortBeans != null && distSortBeans.size() > 0) {
            for (SearchFilterBean.DistSortBean bean : distSortBeans) {
                if ("distance".equals(bean.getKey())) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                distance = bean.getSorts().get(i).getSorts();
                                distSortKey = bean.getKey();
                            }
                        }
                    }
                }
                if ("zoneid".equals(bean.getKey())) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                zoneid = bean.getSorts().get(i).getSorts();
                                getZoneId = zoneid;
                                distSortKey = bean.getKey();
                            }
                        }
                    }
                }
                if ("locationid".equals(bean.getKey())) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                locationid = bean.getSorts().get(i).getSorts();
                                distSortKey = bean.getKey();
                            }
                        }
                    }
                }
                for (int j = 0; j < bean.getSorts().size(); j++) {
                    if (bean.getSorts().get(j).isCheck() == true) {
                        distanceShow = bean.getSorts().get(j).getTitle();
                    }
                }
            }
        }
        secView.refreshAdapter(distSortKey);
        //      价格
        String priceTitle = "", startLevel = "", priceSorts = "";
        if (pSortBeans.get(0).getSorts() != null && pSortBeans.get(0).getSorts().size() > 0) {
            for (SearchFilterBean.PSortBean.SortsBeanX sortsBeanX : pSortBeans.get(0).getSorts()) {
                if (sortsBeanX.isCheck == true) {
                    priceTitle = sortsBeanX.getTitle();
                    priceSorts = sortsBeanX.getSorts();
                    int indexOf = priceSorts.indexOf(",");
                    minprice = priceSorts.substring(0, indexOf);
                    maxprice = priceSorts.substring(indexOf + 1, priceSorts.length());
                }
            }
        }
//      星级
        ArrayList<String> start_Level_list = new ArrayList<>();
        if (pSortBeans.get(1).getSorts() != null && pSortBeans.get(1).getSorts().size() > 0) {
            for (SearchFilterBean.PSortBean.SortsBeanX sortsBeanX : pSortBeans.get(1).getSorts()) {
                if (sortsBeanX.isCheck == true) {
                    startLevel = sortsBeanX.getTitle();
                    start_Level_list.add(sortsBeanX.getSorts());
                }
            }
        }
        if (start_Level_list.size() > 0) {
            starlevel = start_Level_list.toString();
            starlevel = starlevel.replace("[", "").trim();
            starlevel = starlevel.replace("]", "").trim();
        }
        if (!StringUtils.isEmpty(startLevel)) {
            getPriceAndLeavelResult = startLevel;
        } else if (!StringUtils.isEmpty(priceTitle)) {
            getPriceAndLeavelResult = priceTitle;
        } else if (!StringUtils.isEmpty(startLevel) && !StringUtils.isEmpty(priceTitle)) {
            getPriceAndLeavelResult = priceTitle + startLevel;
        }
//      综合筛选
        filtrateKey = "";
        if (sSortBeans != null && sSortBeans.size() > 0) {
            for (SearchFilterBean.SSortBean bean : sSortBeans) {
                if (bean.getKey().equals(themeid)) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                themeid = bean.getSorts().get(i).getSorts();
                                filtrateKey = bean.getKey();
                            }
                        }
                    }
                }

                if (bean.getKey().equals(brandid)) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                brandid = bean.getSorts().get(i).getSorts();
                                filtrateKey = bean.getKey();
                            }
                        }
                    }
                }

                if ("facilityid".equals(bean.getKey())) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                facilityid = bean.getSorts().get(i).getSorts();
                                filtrateKey = bean.getKey();
                            }
                        }
                    }
                }
                if ("bedid".equals(bean.getKey())) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                bedid = bean.getSorts().get(i).getSorts();
                                filtrateKey = bean.getKey();
                            }
                        }
                    }
                }
                if ("grade".equals(bean.getKey())) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                grade = bean.getSorts().get(i).getSorts();
                                filtrateKey = bean.getKey();
                            }
                        }
                    }
                }
                for (int i = 0; i < bean.getSorts().size(); i++) {
                    if (bean.getSorts().get(i).isCheck() == true) {
                        filtrateShow = bean.getSorts().get(i).getTitle();
                        filtrateKey = bean.getKey();
                    }
                }
            }
        }
        filtrate_four_view.refreshAdapter(filtrateKey);
        getIntentResetTab();
    }

    private void setHotelListDataToView(CtripHotelListEntity entity) {
        if (null == entity.data || entity.data.size() == 0) {
            //放置空页面
            listAdapter.setNewData(entity.data);
            listAdapter.loadMoreEnd();
            listAdapter.setEmptyView(R.layout.view_hotel_no_data, rv_hotel_search_content);
            listAdapter.notifyDataSetChanged();
            MarkOverlayUtil.clearAllMarker();
        } else {
            listAdapter.setNewData(entity.data);
            listAdapter.loadMoreComplete();
//          添加marker
            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    addMark(entity);
                }
            });
        }
    }

    private void getIntentResetTab() {
        if (!StringUtils.isEmpty(comSortShow)) {
            menuList.get(0).setCheck(true);
            menuList.get(0).setTitle(comSortShow);
        }
        //从上个页面传值 之后 改变选中状态  需要在  setDropDownMenu 之后  执行
        if (!StringUtils.isEmpty(distanceShow)) {
            menuList.get(1).setCheck(true);
            menuList.get(1).setTitle(distanceShow);
        }
        if (!StringUtils.isEmpty(getPriceAndLeavelResult)) {
            menuList.get(2).setCheck(true);
            menuList.get(2).setTitle(getPriceAndLeavelResult);
        }
        if (!StringUtils.isEmpty(filtrateShow)) {
            menuList.get(3).setCheck(true);
            menuList.get(3).setTitle(filtrateShow);
        }
        dropDownMenu.reSetTabCheck();
    }

    //  获取数据后绘制marker
    private void addMark(CtripHotelListEntity entity) {
        marketList = new ArrayList<>();
        for (int i = 0; i < entity.data.size(); i++) {
            MarketBean marketBean = new MarketBean();
            if (StringUtils.isNumber(entity.data.get(i).gdLat + "") == true && StringUtils.isNumber(entity.data.get(i).gdLng + "") == true) {
                try {
                    LatLng latLng = new LatLng(entity.data.get(i).gdLat, entity.data.get(i).gdLng);
//                    BigDecimal maxlat = new BigDecimal(entity.data.get(i).gdLat);
//                    double lat = maxlat.doubleValue();
//                    BigDecimal maxlng = new BigDecimal(entity.data.get(i).gdLng);
//                    double lng = maxlng.doubleValue();
                    marketBean.setLongitude(entity.data.get(i).gdLng);
                    marketBean.setLatitude(entity.data.get(i).gdLat);
                    marketBean.setTitle(entity.data.get(i).HotelName + entity.data.get(i).HotelID);
                    marketList.add(marketBean);
                } catch (NumberFormatException e) {

                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MarkOverlayUtil.addMarkerList(marketList, aMap, boundsBuilder);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mapView != null) {
            mapView.onDestroy();
        }
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端。
            MarkOverlayUtil.clearAllMarker();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
        //     停止定位
        deactivate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    /**
     * @param v
     */
    @Override
    public void onBack(View v) {
        super.onBack(v);
        if (filterDataBean != null) {
            /**
             * {@link   HotelQueryActivity#receiveFiliteEvent(com.yilian.networkingmodule.entity.SearchFilterBean)}
             */
            EventBus.getDefault().post(filterDataBean);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack(v3Back);
        }
        return true;
    }

    //  激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (mlocationClient == null) {
            mListener = onLocationChangedListener;
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                currentLat = aMapLocation.getLatitude();//获取纬度
                currentLng = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                cityCode = aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                // isFirstLoc第一次定位成功
                if (isFirstLoc) {
                    //清空缓存位置
                    aMap.clear();
                    mListener.onLocationChanged(aMapLocation);
                    isFirstLoc = false;
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    // 设置当前位置marker
                    MarkOverlayUtil.setCurrentPositionMark(mContext, aMap, currentLat, currentLng);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }

        }
    }

    // 移动地图到当前位置
    public void mouveToCurrentPosition() {

    }


    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    //  查询区域发生改变回调
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                cityCode = result.getRegeocodeAddress().getCityCode();
                Logger.i("addressName:" + addressName);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    //监听地图状态改变
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

    }

    //  poi搜索获取搜索结果回调
    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
//        if (code == 1000) {
//            ArrayList<PoiItem> pois = poiResult.getPois();
//            Logger.e("pois.toString():  " + pois.toString());
//            if (pois != null && pois.size() > 0) {
//                ThreadUtil.getThreadPollProxy().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                            addMarkerList(pois);
//                    }
//                });
//             }
//        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onOneClick(View view, int position, String title, String comsortID, int pag, String keyWord) {
        if (rv_hotel_search_content.getVisibility() == View.VISIBLE) {
            rv_hotel_search_content.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(title)) {
            menuList.get(0).setCheck(true);
            menuList.get(0).setTitle(title);
        } else {
            menuList.get(0).setCheck(false);
            menuList.get(0).setTitle("智能排序");
        }
        if (dropDownMenu.isShowing()) {
            if (!StringUtils.isEmpty(title)) {
                menuList.get(0).setCheck(true);
                dropDownMenu.closeMenu();
            } else {
                menuList.get(0).setCheck(false);
                dropDownMenu.closeMenu();
            }
        }
        if (!StringUtils.isEmpty(comsortID)) {
            comsort = comsortID;
        }
        page = 0;
        MarkOverlayUtil.clearAllMarker();
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        rv_hotel_search_content.scrollToPosition(0);
        filtrateOne.refreshAdapter();
    }

    @Override
    public void onTwoClick(String title, String distanceID, String zoneID, String locationID, String keywordID) {
        if (rv_hotel_search_content.getVisibility() == View.VISIBLE) {
            rv_hotel_search_content.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(distanceID) || !StringUtils.isEmpty(zoneID) || !StringUtils.isEmpty(locationID) || !StringUtils.isEmpty(keywordID)) {
            menuList.get(1).setCheck(true);
            if (!StringUtils.isEmpty(title)) {
                menuList.get(1).setTitle(title);
            } else {
                menuList.get(1).setTitle("位置距离");
            }
            //          距离
            if ("distance".equals(keywordID)) {
                distance = distanceID;
                //          商业区
            } else if ("zoneid".equals(keywordID)) {
                zoneid = zoneID;
                //           行政区
            } else if ("locationid".equals(keywordID)) {
                locationid = locationID;
            }

        } else {
            menuList.get(1).setCheck(false);
            menuList.get(1).setTitle("位置距离");
        }
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        }

        page = 0;
        MarkOverlayUtil.clearAllMarker();
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        rv_hotel_search_content.scrollToPosition(0);
    }

    @Override
    public void onThereClick(boolean priceIsCheck, String minpriceID, String maxpriceID, ArrayList<String> startlevelList, String showStr) {
        if (rv_hotel_search_content.getVisibility() == View.VISIBLE) {
            rv_hotel_search_content.setVisibility(View.GONE);
        }
        if (startlevelList.size() > 0) {
            starlevel = startlevelList.toString();
            starlevel = starlevel.replace("[", "").trim();
            starlevel = starlevel.replace("]", "").trim();
        }
        if (priceIsCheck == true || startlevelList.size() > 0) {
            menuList.get(2).setTitle(showStr);
            menuList.get(2).setCheck(true);
        } else {
            menuList.get(2).setCheck(false);
            menuList.get(2).setTitle("价格/星级");
        }
        if (dropDownMenu.isShowing()) {
            if (!StringUtils.isEmpty(maxprice) || startlevelList.size() > 0) {
                dropDownMenu.closeMenu();
            }
        }
        if (!StringUtils.isEmpty(minprice) && !StringUtils.isEmpty(maxprice)) {
            minprice = minpriceID;
            maxprice = maxpriceID;
        }
        page = 0;
        MarkOverlayUtil.clearAllMarker();
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        rv_hotel_search_content.scrollToPosition(0);
    }

    @Override
    public void onFourClick(String themeTitle, String mThemeID, String mBrandid, String mFacilityid, String mBedid, String mGrade, String keyWord) {
        if (rv_hotel_search_content.getVisibility() == View.VISIBLE) {
            rv_hotel_search_content.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(themeTitle)) {
            menuList.get(3).setCheck(true);
            menuList.get(3).setTitle(themeTitle);
        } else {
            menuList.get(3).setCheck(false);
            menuList.get(3).setTitle("筛选");
        }
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        }
        if (!StringUtils.isEmpty(mThemeID)) {
            themeid = mThemeID;
        }
        if (!StringUtils.isEmpty(mBrandid)) {
            brandid = mBrandid;
        }
        if (!StringUtils.isEmpty(mFacilityid)) {
            facilityid = mFacilityid;
        }
        if (!StringUtils.isEmpty(mBedid)) {
            bedid = mBedid;
        }
        if (!StringUtils.isEmpty(mGrade)) {
            grade = mGrade;
        }
        page = 0;
        MarkOverlayUtil.clearAllMarker();
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        rv_hotel_search_content.scrollToPosition(0);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBus(FiltrateEventBean eventBean) {
        if (eventBean != null) {
            switch (eventBean.getTag()) {
                case 0:
                    menuList.get(0).setCheck(eventBean.isCheck());
                    menuList.get(0).setTitle(eventBean.getName());
                    break;
                case 1:
                    menuList.get(1).setCheck(eventBean.isCheck());
                    menuList.get(1).setTitle(eventBean.getName());
                    break;
                case 2:
                    menuList.get(2).setCheck(eventBean.isCheck());
                    menuList.get(2).setTitle(eventBean.getName());
                    break;
                case 3:
                    menuList.get(3).setCheck(eventBean.isCheck());
                    menuList.get(3).setTitle(eventBean.getName());
                    break;
            }
        }
    }


}
