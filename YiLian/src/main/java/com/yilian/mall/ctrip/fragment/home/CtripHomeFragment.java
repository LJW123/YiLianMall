package com.yilian.mall.ctrip.fragment.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.activity.CtripHomePageActivity;
import com.yilian.mall.ctrip.adapter.CtripHomeListAdapter;
import com.yilian.mall.ctrip.popupwindow.CtripHomePricePopwindow;
import com.yilian.mall.ctrip.util.CtripHomeUtils;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ctrip.util.JumpCtripToOtherPage;
import com.yilian.mall.ctrip.widget.cosmocalendar.CalendarViewActivity;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.entity.Location;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.mall.ui.MTChooseAddressActivity;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mall.utils.AMapLocationSuccessListener;
import com.yilian.mall.utils.AMapLocationUtil;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.SearchFilterBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHomeTop;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripKeywordModel;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripPriceModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mylibrary.Constants.CTRIP_PEYMENT_WARM_PROMPT;


/**
 * 携程 首页
 *
 * @author Created by Zg on 2018/8/15.
 */
public class CtripHomeFragment extends JPBaseFragment implements View.OnClickListener {
    public static final int LOCATION_REQUEST_CODE = 99;
    public static final int LOCATION_PERMISSION = 999;//请求定位权限请求码
    private VaryViewUtils varyViewUtils;
    /**
     * 头部部分
     */
    private View headerView;
    private Banner banner;
    private TextView tvCity;
    private LinearLayout llCity;
    private ImageButton ibLocation;
    private TextView tvCheckIn;
    private TextView tvCheckInDescribe;
    private TextView tvDays;
    private TextView tvCheckOut;
    private TextView tvCheckOutDescribe;
    private LinearLayout llReserveDate;
    private TextView tvKeyword;
    private ImageView ivKeywordClear;
    private TextView tvPrice;
    private ImageView ivPriceClear;
    private TextView tvSearch;
    private LinearLayout llMap;
    //活动
    private LinearLayout llActivity;
    private ImageView ivActivity1, ivActivity2, ivActivity3, ivActivity4, ivActivity5;
    /**
     * 列表
     */
    private int page = Constants.PAGE_INDEX;
    private RecyclerView mRecyclerView;
    private CtripHomeListAdapter homeListAdapter;
    //入住时间,离店时间
    private String checkIn, checkOut;
    //关键字 数据
    private CtripKeywordModel mCtripKeyword;
    //星级 价格
    private CtripPriceModel mCtripPrice;
    private List<SearchFilterBean.PSortBean> pSortBeans;
    private CtripHomePricePopwindow pricePopwindow;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.ctrip_fragment_home, null);
        }
        EventBus.getDefault().register(this);
        initView();
        initListener();
        return rootView;
    }

    private void initView() {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, rootView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //头部布局
        initHeaderView();

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        homeListAdapter = new CtripHomeListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(homeListAdapter);
        homeListAdapter.addHeaderView(headerView);

    }

    private void initListener() {
        llCity.setOnClickListener(this);
        ibLocation.setOnClickListener(this);
        llReserveDate.setOnClickListener(this);
        tvKeyword.setOnClickListener(this);
        ivKeywordClear.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
        ivPriceClear.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        llMap.setOnClickListener(this);

        homeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, mRecyclerView);

        homeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripHotelListEntity.DataBean mData = (CtripHotelListEntity.DataBean) adapter.getItem(position);
                JumpCtripActivityUtils.toCtripHotelDetail(mContext, mData.HotelID, checkIn, checkOut);
            }
        });
        //查询
        RxUtil.clicks(tvSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpCtripActivityUtils.toHotelQuery(mContext, mCtripKeyword, mCtripPrice, checkIn, checkOut);
            }
        });
    }

    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.ctrip_fragment_home_header, null);
        banner = (Banner) headerView.findViewById(R.id.banner);
        tvCity = (TextView) headerView.findViewById(R.id.tv_city);
        llCity = (LinearLayout) headerView.findViewById(R.id.ll_city);
        ibLocation = headerView.findViewById(R.id.ib_location);
        tvCheckIn = (TextView) headerView.findViewById(R.id.tv_check_in);
        tvCheckInDescribe = (TextView) headerView.findViewById(R.id.tv_check_in_describe);
        tvDays = (TextView) headerView.findViewById(R.id.tv_days);
        tvCheckOut = (TextView) headerView.findViewById(R.id.tv_check_out);
        tvCheckOutDescribe = (TextView) headerView.findViewById(R.id.tv_check_out_describe);
        llReserveDate = (LinearLayout) headerView.findViewById(R.id.ll_reserve_date);
        tvKeyword = (TextView) headerView.findViewById(R.id.tv_keyword);
        ivKeywordClear = headerView.findViewById(R.id.iv_keyword_clear);
        ivKeywordClear.setVisibility(View.GONE);
        tvPrice = (TextView) headerView.findViewById(R.id.tv_price);
        ivPriceClear = headerView.findViewById(R.id.iv_price_clear);
        ivPriceClear.setVisibility(View.GONE);
        tvSearch = (TextView) headerView.findViewById(R.id.tv_search);
        llMap = (LinearLayout) headerView.findViewById(R.id.ll_map);
        llActivity = headerView.findViewById(R.id.ll_activity);
        ivActivity1 = headerView.findViewById(R.id.iv_activity_1);
        ivActivity2 = headerView.findViewById(R.id.iv_activity_2);
        ivActivity3 = headerView.findViewById(R.id.iv_activity_3);
        ivActivity4 = headerView.findViewById(R.id.iv_activity_4);
        ivActivity5 = headerView.findViewById(R.id.iv_activity_5);
    }

    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        page++;
        getMaybeLikeData();
    }

    /**
     * 猜你喜欢
     **/
    private void getMaybeLikeData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMaybeLike("xc_goods/xc_maybe_like", CtripHomePageActivity.mCtripSite.cityId,
                        CtripHomePageActivity.mCtripSite.countryId, CtripHomePageActivity.mCtripSite.gdLat,
                        CtripHomePageActivity.mCtripSite.gdLng, page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelListEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        if (page != Constants.PAGE_INDEX) {
                            page--;
                        }
                    }

                    @Override
                    public void onNext(CtripHotelListEntity entity) {
                        if (page == Constants.PAGE_INDEX) {
                            if (entity.data != null && entity.data.size() > 0) {
                                homeListAdapter.setLocation(entity.location);
                                homeListAdapter.setNewData(entity.data);
                            }
                        } else {
                            homeListAdapter.addData(entity.data);
                        }
                        if (entity.data.size() < Constants.PAGE_COUNT) {
                            homeListAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void initData() {
        //清除 关键字/位置/品牌/酒店名
        mCtripKeyword = null;
        tvKeyword.setText("");
        ivKeywordClear.setVisibility(View.GONE);
        //清除  价格/星级
        mCtripPrice = null;
        tvPrice.setText("");
        ivPriceClear.setVisibility(View.GONE);
        if (pricePopwindow != null) {
            pricePopwindow.resetContent();
        }
        homeListAdapter.setNewData(null);

        tvCity.setText(CtripHomePageActivity.mCtripSite.cityName + CtripHomePageActivity.mCtripSite.countryName);
        getHeaderData();
        getFirstMaybeLike();
        getFiltrateData(false);
    }

    /**
     * 获取列表头部数据
     */
    @SuppressWarnings("unchecked")
    private void getHeaderData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCtripHomeIndex("xc_goods/xc_home_index")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHomeTop>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CtripHomeTop o) {
                        setHeaderData(o);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取首页为您推荐数据
     */
    private void getFirstMaybeLike() {
        startMyDialog();
        page = Constants.PAGE_INDEX;
        getMaybeLikeData();
    }

    //  获取筛选数据
    private void getFiltrateData(Boolean showPop) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSarchCriteria("xc_goods/xc_sort_condition", CtripHomePageActivity.mCtripSite.cityId)
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
                    public void onNext(SearchFilterBean bean) {
                        String msg = bean.getMsg();
                        int code = bean.getCode();
                        if (code == 1) {
                            pSortBeans = bean.getPSort();
                            pricePopwindow = new CtripHomePricePopwindow(getContext(), pSortBeans);
                            if (showPop) {
                                pricePopwindow.showPop(tvPrice);
                            }
                        } else {
                            ToastUtil.showMessage(mContext, msg);
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void setHeaderData(CtripHomeTop homeTop) {
        initTopBanner(homeTop.data.banner);
        initReserveDate(CtripHomeUtils.getDateStr(homeTop.checkIn), CtripHomeUtils.getDateStr(homeTop.checkOut));
        initActivityData(homeTop.data.activity);
        varyViewUtils.showDataView();
    }

    /**
     * 初始化banner
     *
     * @param bannerList
     */
    private void initTopBanner(List<CtripHomeTop.BannerBean> bannerList) {
        if (null == bannerList || bannerList.size() <= 0) {
            banner.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
            List<String> imageList = new ArrayList<>();
            for (CtripHomeTop.BannerBean entity : bannerList) {
                String url = entity.img;
                imageList.add(url);
            }
            banner.setImages(imageList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            CtripHomeTop.BannerBean entity = bannerList.get(position);
                            JumpCtripToOtherPage.getInstance(mContext).jumpToOtherPage(entity.content);
                        }
                    }).start();
        }

    }

    /**
     * 预定时间
     *
     * @param checkIn  入住时间   yyyy-MM-dd
     * @param checkOut 离店时间   yyyy-MM-dd
     */
    private void initReserveDate(String checkIn, String checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        tvCheckIn.setText(CtripHomeUtils.getDateByMMdd(checkIn));
        tvCheckInDescribe.setText(CtripHomeUtils.getWeek(checkIn));
        tvCheckOut.setText(CtripHomeUtils.getDateByMMdd(checkOut));
        tvCheckOutDescribe.setText(CtripHomeUtils.getWeek(checkOut));
        tvDays.setText(CtripHomeUtils.getTimeDistance(checkIn, checkOut) + "晚");
    }

    /**
     * 活动区
     *
     * @param activityList
     */
    private void initActivityData(List<CtripHomeTop.ActivityBean> activityList) {
        if (activityList != null && activityList.size() >= 5) {
            llActivity.setVisibility(View.VISIBLE);
            //动态设置 imgView
            int screenWidth = ScreenUtils.getScreenWidth(mContext);
            int widthSmall = (screenWidth - DPXUnitUtil.dp2px(mContext, (20 + 24 + 8 + 8))) / 3;
            int widthBig = widthSmall * 2 + DPXUnitUtil.dp2px(mContext, 8);
            int height = widthSmall * 85 / 105 ;

            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ivActivity1.getLayoutParams();
            params1.height = height;
            params1.width = widthBig;
            ivActivity1.setLayoutParams(params1);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ivActivity2.getLayoutParams();
            params2.height = height;
            params2.width = widthSmall;
            ivActivity2.setLayoutParams(params2);
            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) ivActivity3.getLayoutParams();
            params3.height = height;
            params3.width = widthSmall;
            ivActivity3.setLayoutParams(params3);
            LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) ivActivity4.getLayoutParams();
            params4.height = height;
            params4.width = widthSmall;
            ivActivity4.setLayoutParams(params4);
            LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) ivActivity5.getLayoutParams();
            params5.height = height;
            params5.width = widthSmall;
            ivActivity5.setLayoutParams(params5);

            GlideUtil.showImage(mContext, activityList.get(0).img, ivActivity1);
            GlideUtil.showImage(mContext, activityList.get(1).img, ivActivity2);
            GlideUtil.showImage(mContext, activityList.get(2).img, ivActivity3);
            GlideUtil.showImage(mContext, activityList.get(3).img, ivActivity4);
            GlideUtil.showImage(mContext, activityList.get(4).img, ivActivity5);

            ivActivity1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, activityList.get(0).content, false);
                }
            });
            ivActivity2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, activityList.get(1).content, false);
                }
            });
            ivActivity3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, activityList.get(2).content, false);
                }
            });
            ivActivity4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, activityList.get(3).content, false);
                }
            });
            ivActivity5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, activityList.get(4).content, false);
                }
            });

        } else {
            llActivity.setVisibility(View.GONE);
        }

    }

    /**
     * 接收选择的商业区数据
     *
     * @param resultModel
     * @throws PackageManager.NameNotFoundException
     */
    @Subscribe()
    public void receiveEvent(CtripKeywordModel resultModel) throws PackageManager.NameNotFoundException {
        mCtripKeyword = resultModel;
        tvKeyword.setText(mCtripKeyword.showName);
        ivKeywordClear.setVisibility(View.VISIBLE);
        //存贮 取携程用户地理位置信息经纬度/城市/区域/商业区
        PreferenceUtils.writeStrConfig(Constants.CTRIP_LOCATION, LocationUtil.getXcUserLocationInfo(
                CtripHomePageActivity.mCtripSite.cityId, CtripHomePageActivity.mCtripSite.cityName, mCtripKeyword.zoneId, CtripHomePageActivity.mCtripSite.countryId,
                CtripHomePageActivity.mCtripSite.gdLat, CtripHomePageActivity.mCtripSite.gdLng), mContext);
    }


    /**
     * 接收价格星级数据
     *
     * @param resultModel
     * @throws PackageManager.NameNotFoundException
     */
    @Subscribe()
    public void receiveEvent(CtripPriceModel resultModel) throws PackageManager.NameNotFoundException {
        mCtripPrice = resultModel;
        tvPrice.setText(mCtripPrice.showStr);
        ivPriceClear.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_city:
                //目的地
                JumpCtripActivityUtils.toCtripSiteCity(mContext);
                break;
            case R.id.ib_location:
                //当前位置
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                } else {
                    ((CtripHomePageActivity) getActivity()).startMyDialog();
                    AMapLocationUtil.getInstance().initLocation(mContext)
                            .location(new AMapLocationSuccessListener() {
                                @Override
                                public void amapLocationSuccessListener(AMapLocation aMapLocation, Location.location location) {
                                    ((CtripHomePageActivity) getActivity()).getAddressTransform(aMapLocation.getProvince(), aMapLocation.getCity(), String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getLongitude()));
                                }

                                @Override
                                public void amapLocationFailureListener(HttpException e, String s) {
                                    ((CtripHomePageActivity) getActivity()).stopMyDialog();
                                    ((CtripHomePageActivity) getActivity()).showChooseSite();
                                }
                            }).startLocation();
                }
                break;
            case R.id.ll_reserve_date:
                //选择日期
                CalendarViewActivity.getInstance(new CalendarViewActivity.OnDaysSelected() {
                    @Override
                    public void daysSelected(Pair<Day, Day> days) {
//                        showToast("选择日期：" + "入住时间：" + days.first.toString() + "   离店时间：" + days.first.toString());
                        initReserveDate(CtripHomeUtils.getDateStr(days.first.getCalendar().getTime()), CtripHomeUtils.getDateStr(days.second.getCalendar().getTime()));
                    }
                }).show(getFragmentManager(), CalendarViewActivity.TAG);
                break;
            case R.id.tv_keyword:
                //关键字/位置/品牌/酒店名
                JumpCtripActivityUtils.toCtripKeyword(mContext);
                break;
            case R.id.iv_keyword_clear:
                //清除 关键字/位置/品牌/酒店名
                mCtripKeyword = null;
                tvKeyword.setText("");
                ivKeywordClear.setVisibility(View.GONE);
                break;
            case R.id.tv_price:
                //价格/星级
                if (pricePopwindow != null) {
                    pricePopwindow.showPop(tvPrice);
                } else {
                    //获取筛选数据
                    startMyDialog();
                    getFiltrateData(true);
                }
                break;
            case R.id.iv_price_clear:
                //清除  价格/星级
                mCtripPrice = null;
                tvPrice.setText("");
                ivPriceClear.setVisibility(View.GONE);
                if (pricePopwindow != null) {
                    pricePopwindow.resetContent();
                }
                break;
            case R.id.ll_map:
                //地图
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    JumpCtripActivityUtils.toActivity_Map_Search(mContext, mCtripKeyword, mCtripPrice, null);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
                }
                break;
            default:
                break;
        }
    }


}
