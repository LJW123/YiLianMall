package com.yilian.mall.ctrip.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.exception.HttpException;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.fragment.home.CtripHomeFragment;
import com.yilian.mall.ctrip.fragment.home.CtripHourRoomFragment;
import com.yilian.mall.ctrip.fragment.home.CtripOrderFragment;
import com.yilian.mall.ctrip.popupwindow.CtripWarnCommonPopwindow;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ctrip.widget.CtripTabLayout;
import com.yilian.mall.entity.Location;
import com.yilian.mall.utils.AMapLocationSuccessListener;
import com.yilian.mall.utils.AMapLocationUtil;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.entity.TabEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripAddressTransformEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripSiteModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mylibrary.Constants.SPKEY_SELECT_CITY_NAME;
import static com.yilian.mylibrary.Constants.SPKEY_SELECT_PROVINCE_NAME;

/**
 * 携程 首页
 *
 * @author Created by Zg on 2018/8/15.
 */

public class CtripHomePageActivity extends BaseFragmentActivity {
    public static final int LOCATION_REQUEST_CODE = 99;
    /**
     * 携程 首页底部tab
     */
    public static final int TAB_HOME = 0;
    //    public static final int TAB_HOUR = 1;
//    public static final int TAB_ORDER = 2;
    public static final int TAB_ORDER = 1;
    public static CtripSiteModel mCtripSite;
    /**
     * 底部导航栏
     */
//    String[] mTitles = {"首页", "钟点房", "我的订单"};
//    int[] mIconUnselectIds = {R.mipmap.ctrip_tab_home_unselect, R.mipmap.ctrip_tab_hour_unselect, R.mipmap.ctrip_tab_order_unselect};
//    int[] mIconSelectIds = {R.mipmap.ctrip_tab_home_select, R.mipmap.ctrip_tab_hour_select, R.mipmap.ctrip_tab_order_select};

    String[] mTitles = {"首页", "我的订单"};
    int[] mIconUnselectIds = {R.mipmap.ctrip_tab_home_unselect, R.mipmap.ctrip_tab_order_unselect};
    int[] mIconSelectIds = {R.mipmap.ctrip_tab_home_select, R.mipmap.ctrip_tab_order_select};

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CtripTabLayout mTabLayout_2;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvRight;
    private LinearLayout llQuery;
    private CtripHomeFragment ctripHomeFragment;
    private CtripHourRoomFragment ctripHourRoomFragment;
    private CtripOrderFragment ctripOrderFragment;
    private CtripWarnCommonPopwindow ctripWarnCommonPopwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_home_page);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        llQuery = (LinearLayout) findViewById(R.id.ll_query);
        llQuery.setVisibility(View.GONE);

        //首页
        ctripHomeFragment = new CtripHomeFragment();
        mFragments.add(ctripHomeFragment);
        //钟点房
//        ctripHourRoomFragment = new CtripHourRoomFragment();
//        mFragments.add(new CtripHourRoomFragment());
        //我的订单
        ctripOrderFragment = new CtripOrderFragment();
        mFragments.add(new CtripOrderFragment());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        /** with ViewPager */
        mTabLayout_2 = findViewById(R.id.tl_2);
        mTabLayout_2.setTabData(mTabEntities, this, R.id.fl_change, mFragments);
        mTabLayout_2.setCurrentTab(0);

    }

    public void initData() {
        int tab = getIntent().getIntExtra("tab", TAB_HOME);
        setTabTitle(tab);
        mTabLayout_2.setCurrentTab(tab);

        //原逻辑 优先获取本地缓存 地址信息
//        String city = PreferenceUtils.readStrConfig(SPKEY_SELECT_CITY_NAME, context, "");
//        String province = PreferenceUtils.readStrConfig(SPKEY_SELECT_PROVINCE_NAME, context, "");
//        if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(province)) {
//            getAddressTransform(province, city, "0", "0");
//        } else {
//            showChooseSite();
//        }

        //改为 定位 获取位置
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            if(isOPen(context)){
                startMyDialog();
                AMapLocationUtil.getInstance().initLocation(context)
                        .location(new AMapLocationSuccessListener() {
                            @Override
                            public void amapLocationSuccessListener(AMapLocation aMapLocation, Location.location location) {
                                getAddressTransform(aMapLocation.getProvince(), aMapLocation.getCity(), String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getLongitude()));
                            }

                            @Override
                            public void amapLocationFailureListener(HttpException e, String s) {
                                stopMyDialog();
                                showChooseSite();
                            }
                        }).startLocation();
            }else {
                showChooseSite();
            }
        }

    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    private void initListener() {
        RxUtil.clicks(mIvBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                //按返回键当前非主页时展示主页，否则finish
                onBackPressed();
            }
        });
        RxUtil.clicks(llQuery, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(context, Constants.CTRIP_INSTRUCTIONS_TEXT, false);
            }
        });

        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                setTabTitle(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    /**
     * 不同的TAB 显示标题
     *
     * @param tab
     */
    private void setTabTitle(int tab) {
        switch (tab) {
            case TAB_HOME:
                mTvTitle.setText("携程");
                llQuery.setVisibility(View.VISIBLE);
                break;
//            case TAB_HOUR:
//                mTvTitle.setText("郑州");
//                break;
            case TAB_ORDER:
                mTvTitle.setText("我的订单");
                llQuery.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    /**
     * 携程 根据城市和区县名称查找对应的id接口
     **/
    public void getAddressTransform(String province, String city, String lat, String lng) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getAddressTransform("xc_address/xc_address_transform", province, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripAddressTransformEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        showChooseSite();
                    }

                    @Override
                    public void onNext(CtripAddressTransformEntity entity) {
                        if (entity.data != null && entity.data.size() > 0) {
                            CtripAddressTransformEntity.DataBean dataBean = entity.data.get(0);
                            mCtripSite = new CtripSiteModel(dataBean.city, dataBean.cityname, "", "", lat, lng);
                            //存贮 取携程用户地理位置信息经纬度/城市/区域/商业区
                            PreferenceUtils.writeStrConfig(Constants.CTRIP_LOCATION, LocationUtil.getXcUserLocationInfo(
                                    mCtripSite.cityId, mCtripSite.cityName, "", mCtripSite.countryId,
                                    mCtripSite.gdLat, mCtripSite.gdLng), context);
                            toLoad();
                        } else {
                            showChooseSite();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 弹出框 选择城市
     */
    public void showChooseSite() {
        if (ctripWarnCommonPopwindow == null) {
            ctripWarnCommonPopwindow = new CtripWarnCommonPopwindow(context);
            ctripWarnCommonPopwindow.setCloseActivity();
            ctripWarnCommonPopwindow.setContent("请先选择城市或开启手机定位后再 查看酒店哦");
            ctripWarnCommonPopwindow.setLeft("返回", new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    finish();
                    ctripWarnCommonPopwindow.dismiss();
                }
            });
            ctripWarnCommonPopwindow.setRight("选择城市", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpCtripActivityUtils.toCtripSiteCity(context);
                }
            });
        }
        findViewById(R.id.tv_title).post(new Runnable() {
            @Override
            public void run() {
                ctripWarnCommonPopwindow.showPop(mTvTitle);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mTabLayout_2.getCurrentTab() != TAB_HOME) {
            mTabLayout_2.setCurrentTab(TAB_HOME);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 加载首页，钟点房的数据
     */
    private void toLoad() {
        ctripHomeFragment.initData();
//        ctripHourRoomFragment.initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ctripWarnCommonPopwindow != null && ctripWarnCommonPopwindow.isShowing()) {
            ctripWarnCommonPopwindow.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }



    @Subscribe()
    public void receiveEvent(CtripSiteModel resultModel) throws PackageManager.NameNotFoundException {
        mCtripSite = resultModel;
        //存贮 取携程用户地理位置信息经纬度/城市/区域/商业区
        PreferenceUtils.writeStrConfig(Constants.CTRIP_LOCATION, LocationUtil.getXcUserLocationInfo(
                mCtripSite.cityId, mCtripSite.cityName, "", mCtripSite.countryId,
                mCtripSite.gdLat, mCtripSite.gdLng), context);

        if (ctripWarnCommonPopwindow != null) {
            ctripWarnCommonPopwindow.dismiss();
        }
        toLoad();
    }

    /**
     * @author Created by Zg on 2018/8/15
     */
//    @IntDef({TAB_HOME, TAB_HOUR, TAB_ORDER})
    @IntDef({TAB_HOME, TAB_ORDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CtripHomeTab {
        public class TAB_BRAND_SELECTED {
        }
    }
}
