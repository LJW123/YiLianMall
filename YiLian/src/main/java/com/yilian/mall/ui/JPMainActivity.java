package com.yilian.mall.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.ADEntity;
import com.yilian.mall.entity.Location;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.ui.fragment.HomePageFragment;
import com.yilian.mall.ui.fragment.JPShoppingCartFragment;
import com.yilian.mall.ui.fragment.MTNearFragment;
import com.yilian.mall.ui.fragment.MineFragment;
import com.yilian.mall.ui.fragment.ProfitExprofitFragment;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.utils.UpdateUtil;
import com.yilian.mylibrary.AnimationController;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.widget.NoScrollViewPager;
import com.yilian.networkingmodule.entity.ForceNotice;
import com.yilian.networkingmodule.entity.RedPacketWhetherEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.AppVersion;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 新版商城主Activity
 */
public class JPMainActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static final int LOCATION_REQUEST_CODE = 99;
    private static Boolean isExit = false;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    private RadioButton radioDiKou;
    private RadioButton radioShoppingCar;
    private RadioButton radioPersonalCenter;
    private RadioGroup radioGroup;
    private Context mContext;
    private Intent intent;
    private ImageView ivMainAd;
    private ImageView ivCloseMainAd;
    private RelativeLayout llMainAdv;
    private ADEntity.AdvDataBean advDataBean;
    private RadioButton radiomtHomePage;
    private NoScrollViewPager viewPager;
    private HomePageFragment homePageFragment;
    private MTNearFragment jpDiKouFragment1;
    private JPShoppingCartFragment jpShoppingCartFragment1;
    private MineFragment mineFragment;
    private ProfitExprofitFragment profitExprofitFragment;
    /**
     * 最后一次选中的RadioButton的ID，默认为第一个，用于在点击奖励RadioButton未登录状态下，跳转登录，登录成功，回到主页时，再次点击奖励RadioButton无效的问题
     */
    private int lastRadioGroupPositionId = R.id.radio_mt_home_page;
    private AccountNetRequest request;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
//                发送定位信息广播
                Intent intent = new Intent();
                intent.putExtra("aMapLocation", aMapLocation);
                intent.setAction("com.yilian.mall.aMapLocation");
                sendBroadcast(intent);
                if (aMapLocation.getErrorCode() == 0) {
                    if (request == null) {
                        request = new AccountNetRequest(mContext);
                    }
                    request.location(String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getLongitude()), new RequestCallBack<Location>() {
                        @Override
                        public void onSuccess(ResponseInfo<Location> responseInfo) {
                            Location result = responseInfo.result;
                            if (result.code == 1) {
//                                首次定位成功后，把用户选择地址，和系统定位地址，都赋值为系统自动定位地址
                                Location.location location = result.location;
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_ID, location.cityId, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_CITY_ID, location.cityId, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_NAME, location.cityName, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_CITY_NAME, location.cityName, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, location.countyId, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_COUNTY_ID, location.countyId, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, location.countyName, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_COUNTY_NAME, location.countyName, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID, location.provinceId, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_PROVINCE_ID, location.provinceId, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_NAME, location.provinceName, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_PROVINCE_NAME, location.provinceName, mContext);
                                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION_ADDRESS, location.provinceName + location.cityName + location.countyName, mContext);
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
                    PreferenceUtils.writeStrConfig(Constants.SELF_LOCATION_LAT, String.valueOf(aMapLocation.getLatitude()), mContext);
                    PreferenceUtils.writeStrConfig(Constants.SELF_LOCATION_LNG, String.valueOf(aMapLocation.getLongitude()), mContext);
                    PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, aMapLocation.getDistrict(), mContext);
                    Logger.i("定位log", "定位成功，存储到常量中的定位经纬度： SELF_LOCATION_LAT:" + aMapLocation.getLatitude() + "  SELF_LOCATION_LNG:" + aMapLocation.getLongitude());


                } else {
                    //定位失败时，可通过ErrCode（v3Back）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long time1 = System.currentTimeMillis();
        setContentView(R.layout.activity_jpmain);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            checkActivitySave();
        }
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
        mContext = this;
        processExtraData();

        initLocation();
        initView();
        initData();
        initListener();


        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            startLocation();
        }
//        checkShowRedDialog();


        long time2 = System.currentTimeMillis();
        Logger.i("MainActivity启动时间：" + (time2 - time1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceUtils.readBoolConfig(Constants.IS_CHECKED_UPDATE, mContext, false)) {
            checkUpdate();
            PreferenceUtils.writeBoolConfig(Constants.IS_CHECKED_UPDATE, false, mContext);
        }
        Logger.i("onRsume-" + "Activity");
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        }
        String jpMainActivity = intent.getStringExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY);
        Logger.i("收到的信号：" + jpMainActivity);
        if (!TextUtils.isEmpty(jpMainActivity)) {
            switch (jpMainActivity) {
                case Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT:
                    radioGroup.check(R.id.radio_mt_home_page);
                    break;
                case Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT:
                    radioGroup.check(R.id.radio_di_kou);
                    break;
                case Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT:
                    radioGroup.check(R.id.radio_shopping_car);
                    break;
                case Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT:
                    radioGroup.check(R.id.radio_personal_center);
                    break;
                default:
                    radioGroup.check(R.id.radio_mt_home_page);
                    break;
            }

        }
        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, "");//这里重置跳转到主页时的值，防止回主页时逻辑混乱
    }

    private void checkUpdate() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                UpdateUtil.checkUpDate(getApplicationContext(), false);
            }
        });
    }

    /**
     * 检查手机开发者模式中是否开启了不保留活动
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void checkActivitySave() {
        try {
            int alwaysFinish = Settings.Global.getInt(getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
            if (alwaysFinish == 1) {
                Dialog dialog = null;
                dialog = new AlertDialog.Builder(this)
                        .setMessage(
                                "由于您已开启'不保留活动',导致i呼部分功能无法正常使用.我们建议您点击左下方'设置'按钮,在'开发者选项'中关闭'不保留活动'功能.")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("设置", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                            Intent intent = new Intent(
                                //                                    Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                //                            startActivity(intent);
                                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                                Intent intent = new Intent();
                                intent.setComponent(componentName);
                                intent.setAction("android.intent.action.View");
                                context.startActivity(intent);
                            }
                        }).create();
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processExtraData() {
        intent = getIntent();
    }

    private void initLocation() {
//        请在主线程中声明AMapLocationClient类对象，需要传Context类型的参数。推荐用getApplicationConext()方法获取全进程有效的context。
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void initView() {
        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyViewPagerFragmentAdapter(getSupportFragmentManager()));
        llMainAdv = (RelativeLayout) findViewById(R.id.rl_main_adv);
        ivMainAd = (ImageView) findViewById(R.id.iv_main_ad);
        ivCloseMainAd = (ImageView) findViewById(R.id.iv_close_main_ad);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radiomtHomePage = (RadioButton) findViewById(R.id.radio_mt_home_page);
        radioDiKou = (RadioButton) findViewById(R.id.radio_di_kou);
        radiomtHomePage.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
        radiomtHomePage.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_home_on), null, null);
        radioShoppingCar = (RadioButton) findViewById(R.id.radio_shopping_car);
        radioPersonalCenter = (RadioButton) findViewById(R.id.radio_personal_center);
        setStatusBar(0);
    }

    private void initData() {

        getForceNotice();

//首页弹屏推广图片和公告的显示逻辑，公告优先显示
        ADEntity.HomePageNoticeBean homePageNoticeBean = (ADEntity.HomePageNoticeBean) PreferenceUtils.readObjectConfig(Constants.HOME_PAGE_NOTICE, mContext);
        if (homePageNoticeBean != null && homePageNoticeBean.show == 1) {
            //如果本次请求的公告ID和上次隐藏的ID相同，那么就不显示
            if (PreferenceUtils.readStrConfig(Constants.NOT_SHOW_HOME_PAGE_NOTICE_BEAN_ID, mContext, "").equals(homePageNoticeBean.noticeId)) {
                return;
            }
            new VersionDialog.Builder(mContext)
                    .setTitle(homePageNoticeBean.title)
                    .setMessage(homePageNoticeBean.intro)
                    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PreferenceUtils.writeStrConfig(Constants.NOT_SHOW_HOME_PAGE_NOTICE_BEAN_ID, homePageNoticeBean.noticeId, mContext);
                        }
                    })
                    .setPositiveButton("查看详情", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra(Constants.SPKEY_URL, homePageNoticeBean.content);
                            startActivity(intent);
                        }
                    }).create().show();
        } else {
            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext, Constants.SPKEY_JP_MAIN_ACTIVITY);
            advDataBean = sharedPreferencesUtils.getObject(Constants.JP_MAIN_ACTIVITY_ADV, ADEntity.AdvDataBean.class);
            if (advDataBean != null) {
                Logger.i("2017年7月19日 11:32:57-" + advDataBean.toString());
                if ("1".equals(advDataBean.isShow) && advDataBean.isShow != null) {//0不显示  1显示
                    llMainAdv.setVisibility(View.VISIBLE);
                    String advImageUrl = advDataBean.advImageUrl;
                    if (!TextUtils.isEmpty(advImageUrl)) {
                        Logger.i("主页推广活动图片URL：" + advImageUrl);
                        if (!advImageUrl.contains("http://") || !advImageUrl.contains("https://")) {
                            advImageUrl = Constants.ImageUrl + advImageUrl;
                        }
                        showAdvImage(advImageUrl);
                    }
                }
            }
        }
    }

    private void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
//                        首页
                        AnimationController.tableBarAnimation(mContext, radiomtHomePage);
                        initRadioButton();
                        radiomtHomePage.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radiomtHomePage.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_home_on), null, null);
                        setStatusBar(0);
                        break;
                    case 1:
//                        线上商城
                        AnimationController.tableBarAnimation(mContext, radioDiKou);
                        initRadioButton();
                        radioDiKou.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radioDiKou.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_mall_on), null, null);
                        setStatusBar(1);
                        break;
                    case 2:
//                        购物车
                        AnimationController.tableBarAnimation(mContext, radioShoppingCar);
                        initRadioButton();
                        radioShoppingCar.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radioShoppingCar.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_shop_on), null, null);
                        setStatusBar(2);
                        break;
                    case 3:
//                        我的
                        AnimationController.tableBarAnimation(mContext, radioPersonalCenter);
                        initRadioButton();
                        radioPersonalCenter.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radioPersonalCenter.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_user_on), null, null);
                        setStatusBar(3);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivMainAd.setOnClickListener(this);
        ivCloseMainAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llMainAdv.setVisibility(View.GONE);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_mt_home_page:
                        setRadioLastPositionId(R.id.radio_mt_home_page);
                        viewPager.setCurrentItem(0, false);
                        initRadioButton();
                        radiomtHomePage.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radiomtHomePage.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_home_on), null, null);
                        setStatusBar(0);
                        break;
                    case R.id.radio_di_kou:
                        setRadioLastPositionId(R.id.radio_di_kou);
                        viewPager.setCurrentItem(1, false);
                        initRadioButton();
                        radioDiKou.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radioDiKou.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_mall_on), null, null);
                        setStatusBar(1);
                        break;
                    case R.id.radio_shopping_car:
                        setRadioLastPositionId(R.id.radio_shopping_car);
                        viewPager.setCurrentItem(2, false);
                        initRadioButton();
                        radioShoppingCar.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radioShoppingCar.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_shop_on), null, null);
                        setStatusBar(2);
                        break;
                    case R.id.radio_personal_center:
                        setRadioLastPositionId(R.id.radio_personal_center);
                        viewPager.setCurrentItem(3, false);
                        initRadioButton();
                        radioPersonalCenter.setTextColor(getResources().getColor(R.color.main_bot_tv_on));
                        radioPersonalCenter.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_user_on), null, null);
                        setStatusBar(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void startLocation() {
//启动定位
        mLocationClient.startLocation();
        Logger.i("定位log  定位开始");

    }

    private void setStatusBar(int position) {
        int statusBarHeight = StatusBarUtils.getStatusBarHeight(mContext);
        View v = new View(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        v.setLayoutParams(params);
        v.setBackgroundColor(getResources().getColor(R.color.color_red));

        switch (position) {
            case 0:
                Logger.i("main-" + "走了0");
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.color_white));
//                StatusBarUtil.setTranslucent(this, 60);
                mImmersionBar.transparentStatusBar().statusBarAlpha(0).fitsSystemWindows(false).init();
                break;
            case 1: //白色
                Logger.i("main-" + "走了1");
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.color_white));
//                StatusBarUtil.setTranslucent(this, 60);
                mImmersionBar.statusBarColor(R.color.white,0).fitsSystemWindows(true).init();
                break;
            case 2: //白色
                Logger.i("main-" + "走了2");
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.color_white));
//                StatusBarUtil.setTranslucent(this, 60);
                mImmersionBar.statusBarColor(R.color.white,0).fitsSystemWindows(true).init();

                break;
            case 3: //红色--这里是个人中心
                Logger.i("main-" + "走了3");
//                StatusBarUtil.setTranslucent(this);
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.color_red));
                mImmersionBar.transparentStatusBar().statusBarAlpha(0).fitsSystemWindows(false).init();

                break;
            default:
                break;
        }
    }

    /**
     * 获取强制公告，该公告显示后，用户不能进行任何操作，只能查看公告和退出APP
     */
    @SuppressWarnings("unchecked")
    void getForceNotice() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getForceNotice("notice", AppVersion.getAppVersion(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ForceNotice>() {
                    @Override
                    public void onCompleted() {
//                        do something
//do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        findViewById(R.id.include_force_notice).setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ForceNotice forceNotice) {
                        findViewById(R.id.ll_content).setVisibility(View.GONE);
                        findViewById(R.id.rl_main_adv).setVisibility(View.GONE);
                        View includeForceNotice = findViewById(R.id.include_force_notice);
                        includeForceNotice.setVisibility(View.VISIBLE);
                        TextView tvForceNoticeContent = (TextView) includeForceNotice.findViewById(R.id.tv_force_notice_content);
                        tvForceNoticeContent.setMovementMethod(new ScrollingMovementMethod());
                        tvForceNoticeContent.setText(forceNotice.content);
                        TextView tvForceNoticeTitle = (TextView) includeForceNotice.findViewById(R.id.tv_force_notice_title);
                        tvForceNoticeTitle.setText(forceNotice.title);
                        TextView tvForceNoticeCompanyName = (TextView) includeForceNotice.findViewById(R.id.tv_force_notice_company_name);
                        tvForceNoticeCompanyName.setText(forceNotice.company);
                        TextView tvForceNoticeTime = (TextView) includeForceNotice.findViewById(R.id.tv_force_notice_time);
                        tvForceNoticeTime.setText(forceNotice.time);
                    }
                });
        addSubscription(subscription);
    }

    private void showAdvImage(String advImageUrl) {
        GlideUtil.showImageNoSuffixNoPlaceholder(getApplicationContext(), advImageUrl, ivMainAd);//单张图片，不需要设置tag
    }

    void initRadioButton() {
        radiomtHomePage.setTextColor(getResources().getColor(R.color.main_bot_tv_off));
        radiomtHomePage.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_home_off), null, null);
        radioDiKou.setTextColor(getResources().getColor(R.color.main_bot_tv_off));
        radioDiKou.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_mall_off), null, null);
        radioDiKou.setCompoundDrawablePadding(0);
        radioDiKou.setText(R.string.jifenmall);
        radioShoppingCar.setTextColor(getResources().getColor(R.color.main_bot_tv_off));
        radioShoppingCar.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_shop_off), null, null);
        radioPersonalCenter.setTextColor(getResources().getColor(R.color.main_bot_tv_off));
        radioPersonalCenter.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_bot_user_off), null, null);
    }

    /**
     * 每次选中radioButton时，都将此次选中的radioButtonId记录下来
     *
     * @param positionId
     */
    private void setRadioLastPositionId(int positionId) {
        lastRadioGroupPositionId = positionId;
    }

    private void checkShowRedDialog() {
        /**
         * 来到首页检测是否弹出奖励弹窗
         */
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                RetrofitUtils2 instance = RetrofitUtils2.getInstance(mContext);
                if (instance == null) {
                    return;
                }
                instance.whetherShowRedDialog(new Callback<RedPacketWhetherEntity>() {
                    @Override
                    public void onResponse(Call<RedPacketWhetherEntity> call, Response<RedPacketWhetherEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                RedPacketWhetherEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        boolean isShow;
                                        String redPacketId = "";
                                        switch (entity.isAlert) {
                                            case "0":
                                                isShow = false;
                                                break;
                                            case "1":
                                                isShow = true;
                                                redPacketId = entity.cashBonusId;
                                                startActivity(new Intent(JPMainActivity.this, RedPacketDialog.class));
                                                break;
                                            default:
                                                isShow = false;
                                                break;
                                        }
                                        PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_SHOW, isShow, mContext);
                                        PreferenceUtils.writeStrConfig(Constants.RED_PACKET_ID, redPacketId, mContext);
                                        Logger.i("走了界面销毁---首页" + entity.isAlert);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RedPacketWhetherEntity> call, Throwable t) {
                    }
                });
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case LOCATION_REQUEST_CODE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startLocation();
                    } else {
                        showToast("定位权限被拒绝,无法获取位置信息");
                        Intent intent = new Intent();
                        intent.setAction("com.yilian.mall.location.refuse");
                        sendBroadcast(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_main_ad://首页弹窗广告点击事件
                llMainAdv.setVisibility(View.GONE);//广告点击之后消失
                intent = popwindowOnclickSkip(intent);
                break;
            case R.id.radio_di_kou:
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            exitBy2Click();
        }
        return true;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次返回,退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_FIRST, false, mContext);
            AppManager.getInstance().AppExit(this);
        }
    }

    class MyViewPagerFragmentAdapter extends FragmentPagerAdapter {

        public MyViewPagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
//                    首页
                    if (homePageFragment == null) {
                        homePageFragment = new HomePageFragment();
                        fragmentList.add(homePageFragment);
                    }
                    return homePageFragment;
                case 1:
//                    线上商城
                    if (jpDiKouFragment1 == null) {
                        jpDiKouFragment1 = new MTNearFragment();
                        fragmentList.add(jpDiKouFragment1);
                    }
                    return jpDiKouFragment1;
                case 2:
//                    购物车
                    if (jpShoppingCartFragment1 == null) {
                        jpShoppingCartFragment1 = new JPShoppingCartFragment();
                        fragmentList.add(jpDiKouFragment1);
                    }
                    return jpShoppingCartFragment1;
                case 3:
//                    我的
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                        fragmentList.add(mineFragment);
                    }
                    return mineFragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    /**
     * 弹窗点击跳转事件
     *
     * @param intent
     * @return
     */
    private Intent popwindowOnclickSkip(Intent intent) {
        if (advDataBean != null && !TextUtils.isEmpty(advDataBean.advImageUrl)) {//根据广告对象是否为空，以及广告图片链接是否为空作为判断依据
            if (!TextUtils.isEmpty(advDataBean.type)) {
                switch (advDataBean.type) {//  0URL 1. 商品详情页 2. 商家详情页面（只会是总部商品 兑换中心id 是 0或者1）  3.旗舰店详情
                    case "0":
                        intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra(Constants.SPKEY_URL, advDataBean.advContent);
                        break;
                    case "1":
                        intent = new Intent(context, JPNewCommDetailActivity.class);
                        intent.putExtra("goods_id", advDataBean.advContent);
                        intent.putExtra("filiale_id", "0");
                        break;
                    case "2":
                        intent = new Intent(context, MTMerchantDetailActivity.class);
                        intent.putExtra("merchant_id", advDataBean.advContent);
                        break;
                    case "3":
                        intent = new Intent(context, JPFlagshipActivity.class);
                        intent.putExtra("index_id", advDataBean.advContent);
                        break;
                    default:
                        break;
                }
            }
        }
        return intent;
    }




}
