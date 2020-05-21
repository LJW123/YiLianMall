package com.yilian.mall.ctrip.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.AdapterSecData;
import com.yilian.mall.ctrip.adapter.CtripHomeListAdapter;
import com.yilian.mall.ctrip.bean.FiltrateEventBean;
import com.yilian.mall.ctrip.bean.MenuListBean;
import com.yilian.mall.ctrip.bean.PriceAndLevelSelectBean;
import com.yilian.mall.ctrip.util.CtripHomeUtils;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ctrip.widget.cosmocalendar.CalendarViewActivity;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.dropdownmenu.DropDownMenu;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.MarginDecoration;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.SearchFilterBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripKeywordModel;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripPriceModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/9/1 17:40
 */

public class HotelQueryActivity extends BaseFragmentActivity implements MyItemClickListener, View.OnClickListener {
    public static final int LOCATION_PERMISSION = 999;//请求定位权限请求码
    ImageView back, iv_location;
    EditText editQuery, edit_query;
    TextView search, tv_checkin_time, tv_leave_time;
    DropDownMenu dropDownMenu;
    LinearLayout ll_calendar, ll_to_search;
    private RecyclerView recyclerView, rv_hotellist_content;
    private String headers[] = {"高价优先", "4公里内 ", "￥500以上", "筛选"};
    private List<View> popupViews = new ArrayList<>();
    private JPNetRequest jpNetRequest;
    private List<SearchFilterBean.ComSortBean> comSortBeans;
    private List<SearchFilterBean.DistSortBean> distSortBeans;
    private List<SearchFilterBean.PSortBean> pSortBeans;
    private List<SearchFilterBean.SSortBean> sSortBeans;
    private List<SearchFilterBean.SecSortBean> secSortBeans;
    private SearchFilterBean filterBean;
    private AdapterSecData adapter_secData;
    private CtripHotelListEntity dataBean;
    private CtripHomeListAdapter listAdapter;
    private int page = Constants.PAGE_INDEX;
    private View fifthView;
    private String getMaxPrice, getMinePrice, getBrandId, getZoneId, cityid = "", comsort = "comSort DESC", distance = "100000000", zoneid = "", locationid = "", minprice = "0", maxprice = "100000000", starlevel = "", themeid = "", brandid = "", facilityid = "", bedid = "", grade = "", keyword = "", gdLat = "", gdLng = "", pageCount = "10";
    private String getPriceAndLeavelResult = "", filtrate_sec_key = "", comsortShowStr = "";
    private List<String> keywords = new ArrayList<>();
    private List<MenuListBean> menuList = new ArrayList<>();
    private SwipeRefreshLayout refreshable_view;
    private String getMinRange, getMaxRange, checkIn, checkOut;
    //关键字 数据
    private CtripKeywordModel mCtripKeyword = new CtripKeywordModel();
    //星级 价格
    private CtripPriceModel mCtripPrice = new CtripPriceModel();
    private String distanceShow = "", filtrateShow = "";
    private SearchFilterBean filterDataBean;
    private ActivityFilterSec secView;
    private ActivityFiltrateOne filtrateOne;
    private ActivityFilterThird thirdView;
    private ActivityFiltrateFour filtrate_four_view;
    private List<String> getStarLevelId = new ArrayList<>();
    private VaryViewUtils varyViewUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_hotel_query);
        getIntentData();
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
        initView();
        initData();
        initListener();
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
        CtripKeywordModel mCtripKeyword = (CtripKeywordModel) getIntent().getSerializableExtra("CtripKeywordModel");
        CtripPriceModel mCtripPrice = (CtripPriceModel) getIntent().getSerializableExtra("CtripPriceModel");
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

    // 跳转酒店详情
    private void initListener() {
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripHotelListEntity.DataBean item = (CtripHotelListEntity.DataBean) adapter.getItem(position);
                JumpCtripActivityUtils.toCtripHotelDetail(context, item.HotelID, checkIn, checkOut);
            }
        });
        //     跳转地图
        RxUtil.clicks(iv_location, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (dropDownMenu.isShowing()) {
                    dropDownMenu.closeMenu();
                }
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    JumpCtripActivityUtils.toActivity_Map_Search(context, null, null, filterDataBean);
                } else {
                    ActivityCompat.requestPermissions(HotelQueryActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
                }
            }
        });
        //关键字/位置/品牌/酒店名
        RxUtil.clicks(edit_query, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (dropDownMenu.isShowing()) {
                    dropDownMenu.closeMenu();
                }
                JumpCtripActivityUtils.toCtripKeyword(context);
            }
        });
    }


    private void initView() {
        edit_query = findViewById(R.id.edit_query);
        ll_to_search = findViewById(R.id.ll_to_search);
        iv_location = findViewById(R.id.iv_location);
        ll_calendar = findViewById(R.id.ll_calendar);
        tv_checkin_time = findViewById(R.id.tv_checkin_time);
        tv_leave_time = findViewById(R.id.tv_leave_time);
        back = findViewById(R.id.back);
        editQuery = findViewById(R.id.edit_query);
        dropDownMenu = findViewById(R.id.dropDownMenu);
        fifthView = LayoutInflater.from(HotelQueryActivity.this).inflate(R.layout.view_dropdownmenu_buttom, null);
        refreshable_view = fifthView.findViewById(R.id.refreshable_view);
        rv_hotellist_content = fifthView.findViewById(R.id.rv_hotellist_content);
        rv_hotellist_content.setFocusable(false);
        rv_hotellist_content.setNestedScrollingEnabled(false);
        rv_hotellist_content.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listAdapter = new CtripHomeListAdapter();
        rv_hotellist_content.setAdapter(listAdapter);

        //上拉加载更多
        listAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, rv_hotellist_content);
        refreshable_view.setColorSchemeColors(ContextCompat.getColor(context, R.color.cFF4289FF));
        refreshable_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
            }
        });
        recyclerView = fifthView.findViewById(R.id.rv_dropdownmenu_buttom);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new MarginDecoration(context));
        editQuery.setCompoundDrawablePadding(0);
        ll_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择日期
                CalendarViewActivity.getInstance(new CalendarViewActivity.OnDaysSelected() {
                    @Override
                    public void daysSelected(Pair<Day, Day> days) {
//                        showToast("选择日期：" + "入住时间：" + days.first.toString() + "   离店时间：" + days.first.toString());
                        initReserveDate(CtripHomeUtils.getDateStr(days.first.getCalendar().getTime()), CtripHomeUtils.getDateStr(days.second.getCalendar().getTime()));
                    }
                }).show(getSupportFragmentManager(), CalendarViewActivity.TAG);
            }
        });
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                initData();
            }
        });
        varyViewUtils.showDataView();
    }


    //  获取数据
    private void initData() {
        getFiltrateData();
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        checkIn = getIntent().getStringExtra("checkIn");
        checkOut = getIntent().getStringExtra("checkOut");
        String timeDDMMCheckin = checkIn.substring(checkIn.indexOf("-") + 1, checkIn.length());
        String timeDDMMCheckout = checkOut.substring(checkOut.indexOf("-") + 1, checkIn.length());
        tv_checkin_time.setText(timeDDMMCheckin);
        tv_leave_time.setText(timeDDMMCheckout);
    }

    //获取酒店列表
    private void getListData(String cityid, String comsort, String distance, String zoneid, String locationid, String minprice, String maxprice, String starlevel, String themeid, String brandid, String facilityid, String bedid, String grade, String keyword, String gdLat, String gdLng, int page, String pageCount) {
        startMyDialog();
        refreshable_view.setRefreshing(false);
        /*CtripHomePageActivity.mCtripSite.cityId*/
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getHotelList("xc_goods/xc_hotel_list", cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelListEntity>() {

                    @Override
                    public void onCompleted() {
                        varyViewUtils.showDataView();
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                        listAdapter.loadMoreFail();
                        if (HotelQueryActivity.this.page == Constants.PAGE_INDEX) {
                            HotelQueryActivity.this.page--;
                        }
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CtripHotelListEntity entity) {
                        setHotelListDataToView(entity);
                    }
                });
        addSubscription(subscription);
    }

    //  酒店筛选列表
    private void setHotelListDataToView(CtripHotelListEntity entity) {
        if (page <= Constants.PAGE_INDEX) {
            if (null == entity.data || entity.data.size() == 0) {
                //放置空页面
                listAdapter.setNewData(entity.data);
                listAdapter.loadMoreEnd();
                listAdapter.setEmptyView(R.layout.view_hotel_no_data);
                listAdapter.notifyDataSetChanged();
            } else {
                listAdapter.setNewData(entity.data);
                listAdapter.setLocation(entity.location);
                listAdapter.loadMoreComplete();
            }
        } else {
            if (Constants.PAGE_COUNT <= entity.data.size()) {
                listAdapter.loadMoreComplete();
            } else {
                listAdapter.loadMoreEnd();
            }
            listAdapter.addData(entity.data);
            listAdapter.setLocation(entity.location);
        }
        varyViewUtils.showDataView();
    }

    //  获取筛选条件数据
    private void getFiltrateData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
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
                        filterDataBean = allDataBean;
                        String msg = filterDataBean.getMsg();
                        int code = filterDataBean.getCode();
                        if (code == 1) {
                            comSortBeans = filterDataBean.getComSort();
                            distSortBeans = filterDataBean.getDistSort();
                            pSortBeans = filterDataBean.getPSort();
                            sSortBeans = filterDataBean.getSSort();
                            secSortBeans = filterDataBean.getSecSort();
                            initPopuContent();
                            setSecDataToView(secSortBeans);
                        } else {
                            ToastUtil.showMessage(context, msg);
                        }
                    }
                });
        addSubscription(subscription);
    }

    @SuppressLint("WrongViewCast")
    private void initPopuContent() {
        if (popupViews != null && popupViews.size() > 0) {
            popupViews.clear();
        }
        //智能排序
        // 默认综合排序选中
        filtrateOne = new ActivityFiltrateOne(HotelQueryActivity.this, comSortBeans);
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
                        if (distSortBeans.get(i).getKey().equals("zoneid")) {
                            for (int j = 0; j < distSortBeans.get(i).getSorts().size(); j++) {
                                if (distSortBeans.get(i).getSorts().get(j).sorts.equals(getZoneId)) {
                                    distSortBeans.get(i).getSorts().get(j).setCheck(true);
                                    distanceShow = distSortBeans.get(i).getSorts().get(j).getTitle();
                                    tag = "1";
                                }
                            }
                        }
                    }
                    //行政区
                    if (!StringUtils.isEmpty(locationid)) {
                        if (distSortBeans.get(i).getKey().equals("locationid")) {
                            for (int j = 0; j < distSortBeans.get(i).getSorts().size(); j++) {
                                if (distSortBeans.get(i).getSorts().get(j).sorts.equals(locationid)) {
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
        secView = new ActivityFilterSec(HotelQueryActivity.this, distSortBeans, tag);
        secView.setListener(this);
        popupViews.add(secView.secView());
        //价格/星级
        PriceAndLevelSelectBean priceAndLevelSelectBean = new PriceAndLevelSelectBean();
        priceAndLevelSelectBean.MaxPrice = getMaxPrice;
        priceAndLevelSelectBean.MinPrice = getMinePrice;
        priceAndLevelSelectBean.levelId = getStarLevelId;
        priceAndLevelSelectBean.minRange = getMinePrice;
        priceAndLevelSelectBean.maxRange = getMaxPrice;
        if (!StringUtils.isEmpty(getMinePrice)) {
            pSortBeans.get(0).minRange = getMinePrice;
        }
        if (!StringUtils.isEmpty(getMaxPrice)) {
            pSortBeans.get(0).maxRange = getMaxPrice;
        }
        thirdView = new ActivityFilterThird(HotelQueryActivity.this, pSortBeans, priceAndLevelSelectBean);
        thirdView.setListener(this);
        popupViews.add(thirdView.thirdView());
        //综合筛选
        //      从上个页面传过来的品牌id
        filtrateShow = "";
        if (!StringUtils.isEmpty(getBrandId)) {
            if (sSortBeans != null && sSortBeans.size() > 0) {
                for (int i = 0; i < sSortBeans.size(); i++) {
                    if (sSortBeans.get(i).key.equals("brandid")) {
                        sSortBeans.get(i).setCheck(true);
                        sSortBeans.get(i).setCircleShow(true);
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
        filtrate_four_view = new ActivityFiltrateFour(HotelQueryActivity.this, sSortBeans);
        filtrate_four_view.setListener(this);
        popupViews.add(filtrate_four_view.fourView());
        /**
         * Dropdownmenu下面的主体部分
         * */
        dropDownMenu.setDropDownMenu(menuList, popupViews, fifthView);
        getIntentResetTab();

    }


    /**
     * 获取数据后相应改变tab状态
     */
    private void getIntentResetTab() {
        //从上个页面传值 之后 改变选中状态  需要在  setDropDownMenu 之后  执行
        if (!StringUtils.isEmpty(comsortShowStr)) {
            menuList.get(0).setCheck(true);
            menuList.get(0).setTitle(comsortShowStr);
        }
        //距离
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

    /**
     * 横向滑动筛选列表
     *
     * @param secSortBeans
     */
    private void setSecDataToView(List<SearchFilterBean.SecSortBean> secSortBeans) {
        if (secSortBeans != null) {
            adapter_secData = new AdapterSecData(context, secSortBeans, R.layout.item_sec_content);
            recyclerView.setAdapter(adapter_secData);
            adapter_secData.setItemClickListener(new AdapterSecData.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    keyword = "";
                    if (keywords != null && keywords.size() > 0) {
                        keywords.clear();
                    }
                    if (secSortBeans.get(position).isCheck() == true) {
                        secSortBeans.get(position).setCheck(false);
                    } else {
                        filtrate_sec_key = secSortBeans.get(position).getKey();
                        secSortBeans.get(position).setCheck(true);
                        List<String> startLevelList = new ArrayList<>();
                        for (int i = 0; i < secSortBeans.size(); i++) {
                            if (position != i && secSortBeans.get(i).getKey().equals(filtrate_sec_key)) {
                                secSortBeans.get(i).setCheck(false);
                            }
                            if (secSortBeans.get(i).isCheck() == true) {
                                if (secSortBeans.get(i).getKey().equals("distance")) {
                                    int num = Integer.parseInt(secSortBeans.get(i).getSorts());
                                    double num1 = Double.valueOf(num) / 1000.0;
                                    String distanceKord = String.format("%.2f", num1);
                                    distance = distanceKord;
                                } else if (secSortBeans.get(i).getKey().equals("themeid")) {
                                    themeid = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("bedid")) {
                                    bedid = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("comsort")) {
                                    comsort = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("zoneid")) {
                                    zoneid = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("locationid")) {
                                    locationid = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("price")) {
                                    int indexOf = secSortBeans.get(i).getSorts().indexOf(",");
                                    minprice = secSortBeans.get(i).getSorts().substring(0, indexOf);
                                    maxprice = secSortBeans.get(i).getSorts().substring(indexOf + 1, secSortBeans.get(i).getSorts().length());
                                } else if (secSortBeans.get(i).getKey().equals("starlevel")) {
                                    startLevelList.add(secSortBeans.get(i).getSorts());
                                } else if (secSortBeans.get(i).getKey().equals("brandid")) {
                                    brandid = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("facilityid")) {
                                    facilityid = secSortBeans.get(i).getSorts();
                                } else if (secSortBeans.get(i).getKey().equals("grade")) {
                                    grade = secSortBeans.get(i).getSorts();
                                }
                            }
                        }
                        // 如果 此时 下拉框中已经选择星级以最后横向滑动列表选择的 为准
                        if (startLevelList != null && startLevelList.size() > 0) {
                            starlevel = startLevelList.toString();
                            starlevel = starlevel.replace("[", "").trim();
                            starlevel = starlevel.replace("]", "").trim();
                        }
                        // 关键字
                        if (keywords != null && keywords.size() > 0) {
                            keyword = keywords.toString();
                            keyword = keyword.replace("[", "").trim();
                            keyword = keyword.replace("]", "").trim();
                        }
                    }
                    keywordSearch_ResetMenuSelect(filtrate_sec_key, secSortBeans.get(position).getSorts(), secSortBeans.get(position).getTitle(), secSortBeans.get(position).isCheck());
                    page = 0;
                    getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
                    recyclerView.scrollToPosition(0);
                    adapter_secData.notifyDataSetChanged();
                }
            });
        }
    }


    //  根据关键字搜索  改变Menu选中状态
    private void keywordSearch_ResetMenuSelect(String filtrate_sec_key, String sortsId, String title, boolean isCheck) {
//      价格优先
        boolean containPriceKey = false;
        for (int i = 0; i < comSortBeans.size(); i++) {
            if (comSortBeans.get(i).getKey().equals(filtrate_sec_key) && isCheck == true) {
                if (comSortBeans.get(i).getSorts().trim().equals(sortsId.trim()) && isCheck == true) {
                    comSortBeans.get(i).setCheck(true);
                } else {
                    comSortBeans.get(i).setCheck(false);
                }
            } else if (comSortBeans.get(i).getSorts().trim().equals(sortsId.trim()) && isCheck == false) {
                comSortBeans.get(i).setCheck(false);
            }
            if (comSortBeans.get(i).isCheck() == true) {
                containPriceKey = true;
            }
        }
        if (containPriceKey == true) {
            menuList.get(0).setCheck(true);
            menuList.get(0).setTitle("智能排序");
        } else {
            menuList.get(0).setCheck(false);
            menuList.get(0).setTitle("智能排序");
        }
        filtrateOne.refreshAdapter();
//      距离筛选
        boolean containDisKey = false;
        for (SearchFilterBean.DistSortBean bean : distSortBeans) {
            for (int i = 0; i < bean.getSorts().size(); i++) {
                if (bean.getKey().equals(filtrate_sec_key) && isCheck == true) {
                    if (bean.getSorts().get(i).getSorts().equals(sortsId) && isCheck == true) {
                        bean.getSorts().get(i).setCheck(true);
                    } else {
                        bean.getSorts().get(i).setCheck(false);
                    }
                } else if (bean.getSorts().get(i).getSorts().equals(sortsId) && isCheck == false) {
                    bean.getSorts().get(i).setCheck(false);
                }
                if (bean.getSorts().get(i).isCheck() == true) {
                    containDisKey = true;
                }
            }
        }

        if (containDisKey == true) {
            menuList.get(1).setCheck(true);
            menuList.get(1).setTitle("位置距离");
        } else {
            menuList.get(1).setCheck(false);
            menuList.get(1).setTitle("位置距离");
        }
        secView.refreshAdapter(filtrate_sec_key);

//      星级价格
        boolean containpSortKey = false;
        for (SearchFilterBean.PSortBean bean : pSortBeans) {
            for (int i = 0; i < bean.getSorts().size(); i++) {
                if (bean.getKey().equals(filtrate_sec_key) && isCheck == true) {
                    if (bean.getSorts().get(i).getSorts().equals(sortsId) && isCheck == true) {
                        bean.getSorts().get(i).setCheck(true);
                    } else {
                        bean.getSorts().get(i).setCheck(false);
                    }
                } else if (bean.getSorts().get(i).getSorts().equals(sortsId) && isCheck == false) {
                    bean.getSorts().get(i).setCheck(false);
                }

                if (bean.getSorts().get(i).isCheck() == true) {
                    containpSortKey = true;
                }
            }
        }

        if (containpSortKey == true) {
            menuList.get(2).setCheck(true);
            menuList.get(2).setTitle("价格/星级");
        } else {
            menuList.get(2).setCheck(false);
            menuList.get(2).setTitle("价格/星级");
        }
//      综合筛选
        boolean containSecKey = false;
        for (SearchFilterBean.SSortBean bean : sSortBeans) {
            for (int i = 0; i < bean.getSorts().size(); i++) {
                if (bean.getKey().equals(filtrate_sec_key) && isCheck == true) {
                    if (bean.getSorts().get(i).getSorts().equals(sortsId) && isCheck == true) {
                        bean.getSorts().get(i).setCheck(true);
                        bean.setCheck(true);
                    } else {
                        bean.getSorts().get(i).setCheck(false);
                        bean.setCheck(false);
                    }
                } else if (bean.getSorts().get(i).getSorts().equals(sortsId) && isCheck == false) {
                    bean.getSorts().get(i).setCheck(false);
                    bean.setCheck(false);
                }
                if (bean.getSorts().get(i).isCheck() == true) {
                    containSecKey = true;
                }
            }
        }
        if (containSecKey == true) {
            menuList.get(3).setCheck(true);
            menuList.get(3).setTitle("筛选");
        } else {
            menuList.get(3).setCheck(false);
            menuList.get(3).setTitle("筛选");
        }
        filtrate_four_view.refreshAdapter(filtrate_sec_key);
        dropDownMenu.reSetTabCheck();
    }

    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        page++;
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBack(View v) {
        super.onBack(v);
    }


    //  价格排序点击回调
    @Override
    public void onOneClick(View view, int position, String title, String comsortID, int page, String keyWord) {
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
        if (!StringUtils.isEmpty(comsortID)) {
            for (int i = 0; i < secSortBeans.size(); i++) {
                if (comsortID.equals(secSortBeans.get(i).getSorts())) {
                    if (secSortBeans.get(i).isCheck() == false) {
                        secSortBeans.get(i).setCheck(true);
                    } else {
                        if (secSortBeans.get(i).getKey().equals(keyWord)) {
                            if (secSortBeans.get(i).isCheck() == true) {
                                secSortBeans.get(i).setCheck(false);
                            }
                        }
                    }
                }
            }
        }
        adapter_secData.notifyDataSetChanged();
        page = 0;
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        recyclerView.scrollToPosition(0);
    }


    //  距离筛选
    @Override
    public void onTwoClick(String title, String distanceID, String zoneID, String locationID, String keywordID) {
        //          距离
        if (keywordID.equals("distance")) {
            if (!StringUtils.isEmpty(distanceID)) {
                int num = Integer.parseInt(distanceID);
                double num1 = Double.valueOf(num) / 1000.0;
                distance = String.format("%.2f", num1);
            } else {
                distance = "100000000 ";
            }
            //          商业区
        } else if (keywordID.equals("zoneid")) {
            zoneid = zoneID;
            //           行政区
        } else if (keywordID.equals("locationid")) {
            locationid = locationID;
        }
        if (!StringUtils.isEmpty(distanceID) || !StringUtils.isEmpty(zoneID) || !StringUtils.isEmpty(locationID)) {
            menuList.get(1).setCheck(true);
            if (!StringUtils.isEmpty(title)) {
                menuList.get(1).setTitle(title);
            } else {
                menuList.get(1).setTitle("位置距离");
            }
        } else {
            menuList.get(1).setCheck(false);
            menuList.get(1).setTitle("位置距离");
        }
        if (!StringUtils.isEmpty(distanceID) || !StringUtils.isEmpty(zoneID) || !StringUtils.isEmpty(locationID)) {
            for (int i = 0; i < secSortBeans.size(); i++) {
                String sorts = secSortBeans.get(i).getSorts();
                if (sorts.equals(distanceID) || sorts.equals(zoneID) || sorts.equals(locationID)) {
                    if (secSortBeans.get(i).isCheck() == true) {
                        secSortBeans.get(i).setCheck(false);
                    } else {
                        secSortBeans.get(i).setCheck(true);
                    }
                } else {
                    if (secSortBeans.get(i).getKey().equals(keywordID)) {
                        if (secSortBeans.get(i).isCheck() == true) {
                            secSortBeans.get(i).setCheck(false);
                        }
                    }
                }
            }
        }
        adapter_secData.notifyDataSetChanged();
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        }
        page = 0;
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        recyclerView.scrollToPosition(0);
        //商业区id
        if (!StringUtils.isEmpty(zoneID)) {
            mCtripKeyword.zoneId = zoneID;
        }
    }

    //   星级价格筛选结果回调
    @Override
    public void onThereClick(boolean priceIsCheck, String minpriceID, String maxpriceID, ArrayList<String> startlevelList, String showStr) {
        if (startlevelList.size() > 0) {
            starlevel = startlevelList.toString();
            starlevel = starlevel.replace("[", "").trim();
            starlevel = starlevel.replace("]", "").trim();
        }
        if (priceIsCheck == true) {
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

        if (!StringUtils.isEmpty(minpriceID)) {
            mCtripPrice.minprice = minpriceID;
        }
        if (!StringUtils.isEmpty(maxpriceID)) {
            mCtripPrice.maxprice = maxpriceID;
        }
        // 判断星级价格 有选中 筛选横向滑动列表 分别判断星级和价格
        if (priceIsCheck == true) {
            if (secSortBeans != null && secSortBeans.size() > 0) {
                if (!StringUtils.isEmpty(minpriceID) && !StringUtils.isEmpty(maxpriceID)) {
                    for (int i = 0; i < secSortBeans.size(); i++) {
                        String sorts = secSortBeans.get(i).getSorts();
                        if (sorts.equals(minpriceID + "," + maxpriceID)) {
                            if (secSortBeans.get(i).isCheck() == true) {
                                secSortBeans.get(i).setCheck(false);
                            } else {
                                secSortBeans.get(i).setCheck(true);
                            }
                        } else {
                            if (secSortBeans.get(i).getKey().equals("price")) {
                                if (secSortBeans.get(i).isCheck() == true) {
                                    secSortBeans.get(i).setCheck(false);
                                }
                            }
                        }
                    }
                }
                if (startlevelList != null && startlevelList.size() > 0) {
                    for (int j = 0; j < secSortBeans.size(); j++) {
                        String sorts = secSortBeans.get(j).getSorts();
                        for (int i = 0; i < startlevelList.size(); i++) {
                            if (sorts.equals(startlevelList.get(i))) {
                                if (secSortBeans.get(i).isCheck() == true) {
                                    secSortBeans.get(i).setCheck(false);
                                } else {
                                    secSortBeans.get(i).setCheck(true);
                                }
                            } else {
                                if (secSortBeans.get(i).getKey().equals("starlevel")) {
                                    if (secSortBeans.get(i).isCheck() == true) {
                                        secSortBeans.get(i).setCheck(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (adapter_secData != null) {
            adapter_secData.notifyDataSetChanged();
        }
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        }
        page = 0;
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        recyclerView.scrollToPosition(0);
    }

    //   综合筛选回调
    @Override
    public void onFourClick(String themeTitle, String mThemeID, String mBrandid, String mFacilityid, String mBedid, String mGrade, String keyWord) {
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

        if (!StringUtils.isEmpty(mThemeID) || !StringUtils.isEmpty(mBrandid) || !StringUtils.isEmpty(mFacilityid) || !StringUtils.isEmpty(mBedid) || !StringUtils.isEmpty(mGrade)) {
            if (secSortBeans != null && secSortBeans.size() > 0) {
                for (int i = 0; i < secSortBeans.size(); i++) {
                    String sorts = secSortBeans.get(i).getSorts();
                    if (sorts.equals(mThemeID) || sorts.equals(mBrandid) || sorts.equals(mFacilityid) || sorts.equals(mBedid) || sorts.equals(mGrade)) {
                        if (secSortBeans.get(i).isCheck() == true) {
                            secSortBeans.get(i).setCheck(false);
                        } else {
                            secSortBeans.get(i).setCheck(true);
                        }
                    } else {
                        if (secSortBeans.get(i).getKey().equals(keyWord)) {
                            if (secSortBeans.get(i).isCheck() == true) {
                                secSortBeans.get(i).setCheck(false);
                            }
                        }
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(brandid)) {
            mCtripKeyword.brandId = brandid;
        }
        if (adapter_secData != null) {
            adapter_secData.notifyDataSetChanged();
        }
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        }
        page = 0;
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//           打开日历
            case R.id.ll_calendar:
                //选择日期
                CalendarViewActivity.getInstance(new CalendarViewActivity.OnDaysSelected() {
                    @Override
                    public void daysSelected(Pair<Day, Day> days) {
//                        showToast("选择日期：" + "入住时间：" + days.first.toString() + "   离店时间：" + days.first.toString());
                        initReserveDate(CtripHomeUtils.getDateStr(days.first.getCalendar().getTime()), CtripHomeUtils.getDateStr(days.second.getCalendar().getTime()));
                    }
                }).show(getSupportFragmentManager(), CalendarViewActivity.TAG);
                break;
        }
    }

    /**
     * 接收页面 筛选结果
     *
     * @param eventBean
     */
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

    /**
     * 接收商业区id 品牌id
     *
     * @param resultModel
     * @throws PackageManager.NameNotFoundException
     */
    @Subscribe()
    public void receiveEvent(CtripKeywordModel resultModel) throws PackageManager.NameNotFoundException {

        if (!StringUtils.isEmpty(resultModel.brandId)) {
            mCtripKeyword.brandId = resultModel.brandId;
            brandid = resultModel.brandId;
        }
        if (!StringUtils.isEmpty(resultModel.zoneId)) {
            mCtripKeyword.zoneId = resultModel.zoneId;
            zoneid = resultModel.zoneId;
        }
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.countryId)) {
            locationid = CtripHomePageActivity.mCtripSite.countryId;
        } else {
            locationid = "";
        }
        if (!StringUtils.isEmpty(locationid) && !StringUtils.isEmpty(resultModel.zoneId)) {
            locationid = "";
        }

//     如果商业区不为空  重置 列表
        if (!StringUtils.isEmpty(resultModel.zoneId)) {
            if (distSortBeans != null && distSortBeans.size() > 0) {
                for (int i = 0; i < distSortBeans.size(); i++) {
                    if (distSortBeans.get(i).getKey().equals("zoneid")) {
                        for (int j = 0; j < distSortBeans.get(i).getSorts().size(); j++) {
                            distSortBeans.get(i).getSorts().get(j).setCheck(false);
                            if (distSortBeans.get(i).getSorts().get(j).sorts.equals(resultModel.zoneId)) {
                                distSortBeans.get(i).getSorts().get(j).setCheck(true);
                                distanceShow = distSortBeans.get(i).getSorts().get(j).getTitle();
                            }
                        }
                    }
                }
            }
        }
        //行政区
        if (!StringUtils.isEmpty(locationid)) {
            if (distSortBeans != null && distSortBeans.size() > 0) {
                for (int i = 0; i < distSortBeans.size(); i++) {
                    if (distSortBeans.get(i).getKey().equals("locationid")) {
                        for (int j = 0; j < distSortBeans.get(i).getSorts().size(); j++) {
                            distSortBeans.get(i).getSorts().get(j).setCheck(false);
                            if (distSortBeans.get(i).getSorts().get(j).sorts.equals(locationid)) {
                                distSortBeans.get(i).getSorts().get(j).setCheck(true);
                                distanceShow = distSortBeans.get(i).getSorts().get(j).getTitle();
                            }
                        }
                    }
                }
            }
        }
        secView.refreshAdapter("zoneid");
        //综合筛选
        //      从上个页面传过来的品牌id
        filtrateShow = "";
        if (!StringUtils.isEmpty(resultModel.brandId)) {
            if (sSortBeans != null && sSortBeans.size() > 0) {
                for (int i = 0; i < sSortBeans.size(); i++) {
                    if (sSortBeans.get(i).key.equals("brandid")) {
                        for (int j = 0; j < sSortBeans.get(i).getSorts().size(); j++) {
                            sSortBeans.get(i).getSorts().get(j).setCheck(false);
                            if (sSortBeans.get(i).getSorts().get(j).getSorts().equals(resultModel.brandId)) {
                                sSortBeans.get(i).getSorts().get(j).setCheck(true);
                                filtrateShow = sSortBeans.get(i).getSorts().get(j).getTitle();
                            }
                        }
                    }
                }
            }
        }
        filtrate_four_view.refreshAdapter("brandid");
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
        getIntentResetTab();
    }

    /**
     * 接收地图页面 拿回来的筛选数据
     * 将原来的数据列表清空并重新填充
     * 重置tab显示
     * {@link com.yilian.mall.ctrip.activity.ActivityMapSearch#onBack(android.view.View)}
     */
    @Subscribe()
    public void receiveFiliteEvent(SearchFilterBean filterDataBean) {
        // 智能排序
        if (comSortBeans != null && comSortBeans.size() > 0 && filterDataBean.comSort != null && filterDataBean.comSort.size() > 0) {
            comSortBeans.clear();
            comSortBeans.addAll(filterDataBean.getComSort());
        }
        if (filterDataBean.comSort.size() > 0 && filterDataBean.comSort != null) {
            for (int i = 0; i < filterDataBean.getComSort().size(); i++) {
                if (filterDataBean.getComSort().get(i).isCheck() == true) {
                    comsort = filterDataBean.getComSort().get(i).getSorts();
                    comsortShowStr = filterDataBean.getComSort().get(i).getTitle();
                }
            }
        }
        // 距离排序
        String key = "";
        List<SearchFilterBean.DistSortBean> getDistSortBeans = filterDataBean.distSort;
        if (getDistSortBeans != null && getDistSortBeans.size() > 0 && distSortBeans != null && distSortBeans.size() > 0) {
            distSortBeans.clear();
            distSortBeans.addAll(getDistSortBeans);
        }
        if (getDistSortBeans != null && getDistSortBeans.size() > 0) {
            for (SearchFilterBean.DistSortBean distSortBean : getDistSortBeans) {
                if (distSortBean.isCheck() == true) {
                    key = distSortBean.getKey();
                }
                if (distSortBean.getKey().equals("distance")) {
                    for (int i = 0; i < distSortBean.getSorts().size(); i++) {
                        if (distSortBean.getSorts().get(i).isCheck() == true) {
                            distance = distSortBean.getSorts().get(i).getSorts();
                            distanceShow = distSortBean.getTitle();
                        }
                    }
                }
                if (distSortBean.getKey().equals("zoneid")) {
                    for (int i = 0; i < distSortBean.getSorts().size(); i++) {
                        if (distSortBean.getSorts().get(i).isCheck() == true) {
                            zoneid = distSortBean.getSorts().get(i).getSorts();
                            distanceShow = distSortBean.getTitle();
                        }
                    }
                }

                if (distSortBean.getKey().equals("locationid")) {
                    for (int i = 0; i < distSortBean.getSorts().size(); i++) {
                        if (distSortBean.getSorts().get(i).isCheck() == true) {
                            locationid = distSortBean.getSorts().get(i).getSorts();
                            distanceShow = distSortBean.getTitle();
                        }
                    }
                }
            }
        }


        // 最高 最低 价格
        getPriceAndLeavelResult = "";
        String getPrice = "";
        String getStarlevelTitle = "";
        if (filterDataBean.pSort != null && filterDataBean.pSort.size() > 0) {
            if (pSortBeans != null && pSortBeans.size() > 0) {
                pSortBeans.get(0).getSorts().clear();
                pSortBeans.get(0).getSorts().addAll(filterDataBean.pSort.get(0).getSorts());
                pSortBeans.get(1).getSorts().clear();
                pSortBeans.get(1).getSorts().addAll(filterDataBean.pSort.get(1).getSorts());
            }
            if (filterDataBean.pSort.get(0).getSorts().size() > 0) {
                for (int i = 0; i < filterDataBean.pSort.get(0).getSorts().size(); i++) {
                    if (filterDataBean.pSort.get(0).getSorts().get(i).isCheck == true) {
                        String price = filterDataBean.pSort.get(0).price;
                        int indexOf = price.indexOf(",");
                        minprice = price.substring(0, indexOf);
                        maxprice = price.substring(indexOf + 1, price.length());
                        getPrice = filterDataBean.pSort.get(0).getSorts().get(i).getTitle();
                    }
                }
            }
            //星级
            ArrayList<String> startlevelList = new ArrayList<>();
            String title = "";
            ArrayList<String> startleveTitlelList = new ArrayList<>();
            if (filterDataBean.pSort.get(1).getSorts().size() > 0) {
                for (int i = 0; i < filterDataBean.pSort.get(1).getSorts().size(); i++) {
                    if (filterDataBean.pSort.get(1).getSorts().get(i).isCheck == true) {
                        startlevelList.add(filterDataBean.pSort.get(1).getSorts().get(i).getSorts());
                        startleveTitlelList.add(filterDataBean.pSort.get(1).getSorts().get(i).getTitle());
                        title = startleveTitlelList.toString();
                        title = title.replace("[", "").trim();
                        title = title.replace("]", "").trim();
                        starlevel = starlevel.replace("[", "").trim();
                        starlevel = starlevel.replace("]", "").trim();
                        getStarlevelTitle = title;
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(getPrice) || !StringUtils.isEmpty(getStarlevelTitle)) {
            getPriceAndLeavelResult = getPrice + getStarlevelTitle;
        }
        String filtrateKey = "";
        filtrateShow = "";
        if (filterDataBean.sSort != null) {
            List<SearchFilterBean.SSortBean> getSortBeans = filterDataBean.sSort;
            if (getSortBeans != null && getSortBeans.size() > 0 && sSortBeans != null && sSortBeans.size() > 0) {
                sSortBeans.clear();
                sSortBeans.addAll(getSortBeans);
            }
            for (SearchFilterBean.SSortBean bean : getSortBeans) {
                for (int i = 0; i < bean.getSorts().size(); i++) {
                    if (bean.getSorts().get(i).isCheck() == true) {
                        if (bean.getKey().equals("themeid")) {
                            themeid = bean.getSorts().get(i).getSorts();
                        } else if (bean.getKey().equals("brandid")) {
                            brandid = bean.getSorts().get(i).getSorts();
                        } else if (bean.getKey().equals("facilityid")) {
                            facilityid = bean.getSorts().get(i).getSorts();
                        } else if (bean.getKey().equals("bedid")) {
                            bedid = bean.getSorts().get(i).getSorts();
                        } else if (bean.getKey().equals("grade")) {
                            grade = bean.getSorts().get(i).getSorts();
                        }
                        filtrateShow = bean.getSorts().get(i).getTitle();
                        filtrateKey = bean.getKey();
                    }
                }
            }
        }
        getIntentResetTab();
        filtrateOne.refreshAdapter();
        secView.refreshAdapter(key);
        thirdView.refreshAdapter(filterDataBean.pSort);
        filtrate_four_view.refreshAdapter(filtrateKey);
        getListData(cityid, comsort, distance, zoneid, locationid, minprice, maxprice, starlevel, themeid, brandid, facilityid, bedid, grade, keyword, gdLat, gdLng, page, pageCount);
    }

    /**
     * 预定时间
     *
     * @param getCheckIn  入住时间   yyyy-MM-dd
     * @param getCheckOut 离店时间   yyyy-MM-dd
     */
    private void initReserveDate(String getCheckIn, String getCheckOut) {
        checkIn = getCheckIn;
        checkOut = getCheckOut;
        String timeDDMMCheckin = checkIn.substring(checkIn.indexOf("-") + 1, checkIn.length());
        String timeDDMMCheckout = checkOut.substring(checkOut.indexOf("-") + 1, checkIn.length());
        tv_checkin_time.setText(timeDDMMCheckin);
        tv_leave_time.setText(timeDDMMCheckout);
    }
}

