package com.yilian.mall.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.ta.utdid2.android.utils.NetworkUtils;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.HomePageJdBrandSelectedAdapter;
import com.yilian.mall.adapter.MTHomePageGridViewAdapter;
import com.yilian.mall.adapter.MTHomePageViewPagerAdapter;
import com.yilian.mall.ctrip.activity.CtripHomePageActivity;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.entity.Location;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.ui.AreaSelectionActivity;
import com.yilian.mall.ui.InformationActivity;
import com.yilian.mall.ui.MipcaActivityCapture;
import com.yilian.mall.ui.SearchCommodityActivity;
import com.yilian.mall.utils.AMapLocationSuccessListener;
import com.yilian.mall.utils.AMapLocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mall.widgets.CustomViewPager;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mall.widgets.NoticeView2;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.MyBannerUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.Utils;
import com.yilian.networkingmodule.entity.HomePageAreaEntity;
import com.yilian.networkingmodule.entity.MTHomePageEntity;
import com.yilian.networkingmodule.entity.NoticeViewEntity;
import com.yilian.networkingmodule.entity.jd.JdBrandSelectedMultiItem;
import com.yilian.networkingmodule.entity.jd.JdGoodsBrandSelectedEntity;
import com.yilian.networkingmodule.retrofitutil.AppVersion;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * @author
 */
public class HomePageFragment extends JPBaseFragment implements View.OnClickListener {


    public static final int LOCATION_REQUEST_CODE = 99;
    /**
     * recyclerView滑动高度
     */
    int scrollDy = 0;
    int page = 0;
    /**
     * 当前显示的是第几页
     */
    int curIndex = 0;
    ArrayList<TextView> areaOneMainTitleList = new ArrayList<>();
    ArrayList<TextView> areaOneSubTitleList = new ArrayList<>();
    ArrayList<ImageView> areaOneImage1List = new ArrayList<>();
    ArrayList<ImageView> areaOneImage2List = new ArrayList<>();
    ArrayList<TextView> areaTwoMainTitleList = new ArrayList<>();
    ArrayList<TextView> areaTwoSubTitleList = new ArrayList<>();
    ArrayList<ImageView> areaTwoImageList = new ArrayList<>();
    ArrayList<ImageView> areaTwoLogoImageList = new ArrayList<>();
    ArrayList<TextView> areaTwoShopNameList = new ArrayList<>();

    /**
     * 精选专区 每块的View集合 用于点击跳转
     */
    ArrayList<View> areaOneIncludeView = new ArrayList<>();
    /**
     * 品牌专区 每块的View集合 用于点击跳转
     */
    ArrayList<View> areaTwoIncludeView = new ArrayList<>();
    /**
     * 淘宝专区 每块的View集合 用于点击跳转
     */
    ArrayList<View> areaTaoBaoIncludeView = new ArrayList<>();
    ArrayList<ImageView> areaTaoBaoImgList = new ArrayList<>();
    ArrayList<TextView> areaTaoBaoTitleList = new ArrayList<>();
    ArrayList<ImageView> areaJDImgList = new ArrayList<>();
    ArrayList<TextView> areaJDTitleList = new ArrayList<>();
    ArrayList<ImageView> areaSnImgList = new ArrayList<>();
    ArrayList<TextView> areaSnTitleList = new ArrayList<>();
    private View rootView;
    private TextView tvAddress;
    private TextView tvSearch;
    private ImageView ivQrCodeScan;
    private ImageView ivMessage;
    private RecyclerView recyclerView;
    private boolean isFirst = true;
    private String cityId = "0";
    private String countyId = "0";
    private ImageView ivReturnTop;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 定位拒绝广播
     */
    BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.i("接收到定位拒绝广播");
            tvAddress.setText("未定位");
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    /**
     * 定位广播是否已注册
     */
    private boolean locationBroadCastReceivered = false;
    /**
     * 网络广播是否已注册
     */
    private boolean netWorkTypeChangedBroadcastReceivered = false;
    /**
     * 获取第一页数据标记(包括头部数据和列表第一页)
     */
    private boolean getFirstPageDataFlag = true;
    /**
     * 新的title
     */
    private LinearLayout homeTitleLayout;
    private View headerView;
    /**
     * banner
     */
    private View bannerView;
    private Banner banner;
    /**
     * img top
     */
    private View imgTopView;
    private FrameLayout topFrameLayout;
    private ImageView imgTop;
    private View topView1, topView2, topView3;
    /**
     * viewpager
     */
    private View iconsView;
    private CustomViewPager mPager;
    private LinearLayout mLlDot;
    private ImageView iconsBgIv;
    /**
     * img bot
     */
    private View imgBotView;
    private FrameLayout botFrameLayout;
    private ImageView imgBot;
    private View botView1, botView2, botView3;
    private View newsView;
    private NoticeView2 noticeViewJpMain;
    private TextView tvTitle;
    private View emptyView;
    private TextView tvRefresh;
    private Activity activity;//将activity保存下来，防止getActivity获取时为null
    /**
     * 网络状态变化监听，监听到变化后，如果没有定位信息，则进行重新定位
     * isFirst的加入，是因为首次启动APP第一次打开该界面，就会检测到网络的变化，会和setUserVisiableHint重复请求数据，因此这里加入isFirst判断
     */
    BroadcastReceiver netWorkTypeChangedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.init(mContext);
            boolean connected = com.yilian.mylibrary.NetworkUtils.isConnected();
            Logger.i("connected:" + connected);
            if (connected && !isFirst) {
                if ("0".equals(cityId) && "0".equals(countyId)) {
                    Logger.i("dataNetworkInfo: cityId:" + cityId + "  countyId:" + countyId);
                    //重新定位
                    reLocation();
                }
            }
        }
    };
    private ImageView ivMessageBot;
    private String lat = "0";
    private String lng = "0";
    private String oldCityId = "";
    private String oldCountyId = "";
    private AccountNetRequest request;
    private View llAreaContent1_1;
    private View llAreaContent1_2;
    private View llAreaContent2_1;
    private View llAreaContent2_2;
    //    第一块Title
    private View includeAreaTitleOne;
    private ImageView ivAreaTitleOne;
    private ImageView ivAreaTitleOneMore;
    //    第一块第一个图片
    private TextView tvContentOne1MainTitle;
    private TextView tvContentOne1SubTitle;
    private ImageView ivContentOne1_1;
    private ImageView ivContentOne1_2;
    private View includeContentOne1;
    //    第一块第二个图片
    private View includeContentOne2;
    private TextView tvContentOne2MainTitle;
    private TextView tvContentOne2SubTitle;
    private ImageView ivContentOne2_1;
    private ImageView ivContentOne2_2;
    //    第一块第三个图片
    private View includeContentOne3;
    private TextView tvContentOne3MainTitle;
    private TextView tvContentOne3SubTitle;
    private ImageView ivContentOne3_1;
    private ImageView ivContentOne3_2;
    //    第一块第四个图片
    private View includeContentOne4;
    private TextView tvContentOne4MainTitle;
    private TextView tvContentOne4SubTitle;
    private ImageView ivContentOne4_1;
    private ImageView ivContentOne4_2;
    //    第一块第五个图片
    private View includeContentOne5;
    private TextView tvContentOne5MainTitle;
    private TextView tvContentOne5SubTitle;
    private ImageView ivContentOne5_1;
    private ImageView ivContentOne5_2;
    //    第一块第六个图片
    private View includeContentOne6;
    private TextView tvContentOne6MainTitle;
    private TextView tvContentOne6SubTitle;
    private ImageView ivContentOne6_1;
    private ImageView ivContentOne6_2;
    //第二块Title
    private View includeAreaTitleTwo;
    private ImageView ivAreaTitleTwo;
    private ImageView ivAreaTitleTwoMore;
    //    第二块的第一个
    private View includeContentTwo1;
    private TextView tvContentTwo1MainTitle;
    private TextView tvContentTwo1SubTitle;
    private ImageView ivContentTwo1Image;
    private ImageView ivContentTwo1Logo;
    private TextView tvContentTwo1ShopName;
    //    第二块的第二个
    private View includeContentTwo2;
    private TextView tvContentTwo2MainTitle;
    private TextView tvContentTwo2SubTitle;
    private ImageView ivContentTwo2Image;
    private ImageView ivContentTwo2Logo;
    private TextView tvContentTwo2ShopName;
    //    第二块的第三个
    private View includeContentTwo3;
    private TextView tvContentTwo3MainTitle;
    private TextView tvContentTwo3SubTitle;
    private ImageView ivContentTwo3Image;
    private ImageView ivContentTwo3Logo;
    private TextView tvContentTwo3ShopName;
    //    第二块的第四个
    private View includeContentTwo4;
    private TextView tvContentTwo4MainTitle;
    private TextView tvContentTwo4SubTitle;
    private ImageView ivContentTwo4Image;
    private ImageView ivContentTwo4Logo;
    private TextView tvContentTwo4ShopName;
    /**
     * banner
     */
    private View bannerView2;
    private Banner banner2;
    //第三块Title
    private View includeAreaTitleTaoBao;
    private ImageView ivAreaTitleThree;
    private ImageView ivAreaTitleThreeMore;
    private View includeTaoBaoArea;
    private ImageView ivTaoBao1;
    private TextView tvTaoBaoTitle1;
    private ImageView ivTaoBao2;
    private TextView tvTaoBaoTitle2;
    private ImageView ivTaoBao3;
    private TextView tvTaoBaoTitle3;
    private ImageView ivTaoBao4;
    private TextView tvTaoBaoTitle4;
    private ImageView ivTaoBao5;
    private TextView tvTaoBaoTitle5;
    //第五块Title
    private View includeAreaTitleJD;
    private ImageView ivAreaTitleFive;
    private ImageView ivAreaTitleFiveMore;
    private View includeJDArea;
    private ImageView ivJD1;
    private TextView tvJDTitle1;
    private ImageView ivJD2;
    private TextView tvJDTitle2;
    private ImageView ivJD3;
    private TextView tvJDTitle3;
    private ImageView ivJD4;
    private TextView tvJDTitle4;
    private ImageView ivJD5;
    private TextView tvJDTitle5;
    //第六块Title
    private View includeAreaTitleSn;
    private ImageView ivAreaTitleSix;
    private ImageView ivAreaTitleSixMore;
    private View includeSnArea;
    private ImageView ivSn1;
    private TextView tvSnTitle1;
    private ImageView ivSn2;
    private TextView tvSnTitle2;
    private ImageView ivSn3;
    private TextView tvSnTitle3;
    private ImageView ivSn4;
    private TextView tvSnTitle4;
    private ImageView ivSn5;
    private TextView tvSnTitle5;
    private HomePageJdBrandSelectedAdapter brandSelectedAdapter;

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onResume() {
        registerLocationBroadCastReceiver();
        registerNetworkBroadCastReveiver();
        /*
      判断是否刷新的标志
      首页刷新标识：
      1.定位  其中包括：查找的城市、最近访问城市、进入省份选择的城市
      2.登陆
      3.退出
      4.成为个体店/实体店
     */
        boolean isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_HOME_FRAGMENT, mContext, false);
        Logger.i("isRefresh-" + isRefresh);
        if (isRefresh) {
            getSPAddressInfo();
//            setUserStatus();
//            刷新头部数据，处理头部有关用户信息同步的问题
            getHeaderData();
            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, isRefresh, mContext);
        }
        super.onResume();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        }
        initView(rootView);
        initData();
        initListener();
        return rootView;
    }

    /**
     * 注册定位广播
     */
    private void registerLocationBroadCastReceiver() {
        try {
            locationBroadCastReceivered = true;
            mContext.registerReceiver(locationBroadcastReceiver, new IntentFilter("com.yilian.mall.location.refuse"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册网络环境变化广播
     */
    private void registerNetworkBroadCastReveiver() {
        try {
            netWorkTypeChangedBroadcastReceivered = true;
            mContext.registerReceiver(netWorkTypeChangedBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地SP值的地址信息（用于手动定位时 刷新界面地址信息）
     */
    private void getSPAddressInfo() {
        cityId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
        countyId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");
        tvAddress.setText(sp.getString(Constants.SPKEY_SELECT_COUNTY_NAME, "未定位"));
        Logger.i("oldCityId:" + oldCityId + " oldCityId:" + oldCountyId + "    cityid:" + cityId + " countyId:" + countyId);
        oldCountyId = countyId;
        oldCityId = cityId;
    }

    @SuppressWarnings("unchecked")
    private void getHeaderData() {
        Subscription subscription1 = RetrofitUtils3.getRetrofitService(mContext).
                getHomePageHeaderData("appindex_v10", cityId, countyId,
                        AppVersion.getAppVersion(mContext), null, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MTHomePageEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MTHomePageEntity o) {
                        MTHomePageEntity.DataBean headerData = o.data;
                        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                                .getHomePageAreaData("app_index")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<HomePageAreaEntity>() {
                                    @Override
                                    public void onCompleted() {
                                        getFirstBrandSelected();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        showToast(e.getMessage());

                                    }

                                    @Override
                                    public void onNext(HomePageAreaEntity homePageAreaEntity) {
                                        setHeaderData(headerData, homePageAreaEntity);
                                    }
                                });
                        addSubscription(subscription);

                    }
                });
        addSubscription(subscription1);


    }

    private void getFirstBrandSelected() {
        page = 0;
        getBrandSelectedList();
    }

    private void setHeaderData(MTHomePageEntity.DataBean headerData, HomePageAreaEntity homePageAreaEntity) {
        initHeadView(headerData, homePageAreaEntity);

//        homeTitleLayout.setVisibility(VISIBLE);
        //快报
        ArrayList<NoticeViewEntity> news = headerData.news;

        if (newsView == null) {
            newsView = headerView.findViewById(R.id.include_news);
        }
        if (news == null || news.size() <= 0) {
            newsView.setVisibility(GONE);
        } else {
            newsView.setVisibility(VISIBLE);
            if (noticeViewJpMain == null) {
                noticeViewJpMain = (NoticeView2) newsView.findViewById(R.id.noticeView_jp_main);
            }
            noticeViewJpMain.getPublicNotices(2, news);
        }
        if (tvTitle == null) {
            tvTitle = (TextView) headerView.findViewById(R.id.tv_list_title_name);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(mContext, R.mipmap.icon_home_page_brand_selected)
                    , null, null, null);
        }
        headerView.findViewById(R.id.tv_list_title_sub_name).setVisibility(GONE);
        if (brandSelectedAdapter.getHeaderLayoutCount() == 0) {
            brandSelectedAdapter.addHeaderView(headerView);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition >= 0) {
                brandSelectedAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取品牌精选列表
     */
    @SuppressWarnings("unchecked")
    protected void getBrandSelectedList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getBrandSelectdList("jd_goods/home_brand_list", page, Constants.PAGE_COUNT_10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JdGoodsBrandSelectedEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        swipeRefreshLayout.setRefreshing(false);
                        brandSelectedAdapter.setEnableLoadMore(true);
                        swipeRefreshLayout.setEnabled(true);

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        if (page > 0) {
                            page--;
                        }
                        brandSelectedAdapter.setEnableLoadMore(true);
                        swipeRefreshLayout.setEnabled(true);
                        brandSelectedAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(JdGoodsBrandSelectedEntity entity) {
                        if (null != entity) {
                            setData(entity);
                        }
                    }
                });
        addSubscription(subscription);

    }

    private void initHeadView(MTHomePageEntity.DataBean data, HomePageAreaEntity homePageAreaEntity) {
        Logger.i("initHeadView走了这里1");
        if (headerView == null) {
            Logger.i("initHeadView走了这里2");
            headerView = View.inflate(mContext, R.layout.header_view_home_page, null);
        }
        if (bannerView == null) {
            bannerView = headerView.findViewById(R.id.include_banner);
        }
        if (banner == null) {
            banner = (Banner) bannerView.findViewById(R.id.banner);
        }

        if (imgTopView == null) {
            imgTopView = headerView.findViewById(R.id.include_img_top);
        }
        if (topFrameLayout == null) {
            topFrameLayout = (FrameLayout) imgTopView.findViewById(R.id.frame_layout_top);
        }
        if (imgTop == null) {
            imgTop = (ImageView) imgTopView.findViewById(R.id.iv_home_img_top);
        }
        if (topView1 == null) {
            topView1 = imgTopView.findViewById(R.id.view_top_1);
        }
        if (topView2 == null) {
            topView2 = imgTopView.findViewById(R.id.view_top_2);
        }
        if (topView3 == null) {
            topView3 = imgTopView.findViewById(R.id.view_top_3);
        }

        if (iconsView == null) {
            iconsView = headerView.findViewById(R.id.include_icons);
        }
        if (mPager == null) {
            mPager = (CustomViewPager) iconsView.findViewById(R.id.viewpager);
        }
        if (mLlDot == null) {
            mLlDot = (LinearLayout) iconsView.findViewById(R.id.ll_dot);
        }
        if (iconsBgIv == null) {
            iconsBgIv = (ImageView) iconsView.findViewById(R.id.icons_bg);
        }

        if (imgBotView == null) {
            imgBotView = headerView.findViewById(R.id.include_img_bot);
        }
        if (botFrameLayout == null) {
            botFrameLayout = (FrameLayout) imgBotView.findViewById(R.id.frame_layout_bot);
        }
        if (imgBot == null) {
            imgBot = (ImageView) imgBotView.findViewById(R.id.iv_home_img_bot);
        }
        if (botView1 == null) {
            botView1 = imgBotView.findViewById(R.id.view_bot_1);
        }
        if (botView2 == null) {
            botView2 = imgBotView.findViewById(R.id.view_bot_2);
        }
        if (botView3 == null) {
            botView3 = imgBotView.findViewById(R.id.view_bot_3);
        }
        Logger.i("initHeadView走了这里3");
        initAreaView(homePageAreaEntity);
        setBannerData(data.activityBanner);
        setTopImageView(data.advert);
        setViewpager(data.iconClass, data.iconBg);
        setBotImageView(data.advertTwo);
    }

    private void setData(JdGoodsBrandSelectedEntity entity) {
        if (page <= 0) {
            getFirstPageDataFlag = false;
            if (entity.getData().size() <= 0) {
                brandSelectedAdapter.setNewData(new ArrayList<JdBrandSelectedMultiItem>());
                brandSelectedAdapter.loadMoreEnd();
            } else {
                List<JdBrandSelectedMultiItem> dataList = initBrandSelectedList(entity);
                brandSelectedAdapter.setNewData(dataList);
                if (entity.getData().size() < Constants.PAGE_COUNT_10) {
                    brandSelectedAdapter.loadMoreEnd();
                } else {
                    brandSelectedAdapter.loadMoreComplete();
                }
            }
        } else {
            List<JdBrandSelectedMultiItem> dataList = initBrandSelectedList(entity);
            brandSelectedAdapter.addData(dataList);
            if (entity.getData().size() < Constants.PAGE_COUNT_10) {
                brandSelectedAdapter.loadMoreEnd();
            } else {
                brandSelectedAdapter.loadMoreComplete();
            }

        }
    }

    private void initAreaView(HomePageAreaEntity homePageAreaEntity) {
        if (homePageAreaEntity == null || homePageAreaEntity.data == null) {
            showAreaOneView(GONE);
            showAreaTwoView(GONE);
            showBanner2View(GONE);
            showAreaTaoBaoView(GONE);
            return;
        }

        if (homePageAreaEntity.data.index1 != null && homePageAreaEntity.data.index1.isShow == HomePageAreaEntity.SHOW
                && homePageAreaEntity.data.index1.titleContent != null) {
            initAreaOneView();
            showAreaOneView(VISIBLE);
            setAreaOneData(homePageAreaEntity.data.index1);
        } else {
            showAreaOneView(GONE);
        }
        if (homePageAreaEntity.data.index2 != null && homePageAreaEntity.data.index2.isShow == HomePageAreaEntity.SHOW
                && homePageAreaEntity.data.index2.titleContent != null) {
            initAreaTwoView();
            showAreaTwoView(VISIBLE);
            setAreaTwoData(homePageAreaEntity.data.index2);
        } else {
            showAreaTwoView(GONE);
        }
        if (homePageAreaEntity.data.adv1 != null && homePageAreaEntity.data.adv1.isShow == HomePageAreaEntity.SHOW
                && homePageAreaEntity.data.adv1.titleContent != null) {
            initBanner2View();
            showBanner2View(VISIBLE);
            setBanner2Data(homePageAreaEntity.data.adv1.titleContent);
        } else {
            showBanner2View(GONE);
        }
//        taobao
        if (homePageAreaEntity.data.index3 != null && homePageAreaEntity.data.index3.isShow == HomePageAreaEntity.SHOW
                && homePageAreaEntity.data.index3.titleContent != null) {
            initAreaTaoBaoView();
            showAreaTaoBaoView(VISIBLE);
            setAreaTaoBaoData(homePageAreaEntity.data.index3);
        } else {
            showAreaTaoBaoView(GONE);
        }
//        JD
        if (homePageAreaEntity.data.index4 != null && homePageAreaEntity.data.index4.isShow == HomePageAreaEntity.SHOW
                && homePageAreaEntity.data.index4.titleContent != null) {
            initAreaJDView();
            showAreaJDView(VISIBLE);
            setAreaJDData(homePageAreaEntity.data.index4);
        } else {
            showAreaJDView(GONE);
        }
//        苏宁
        if (homePageAreaEntity.data.index5 != null && homePageAreaEntity.data.index5.isShow == HomePageAreaEntity.SHOW
                && homePageAreaEntity.data.index5.titleContent != null) {

            initAreaSnView();
            showAreaSnView(VISIBLE);
            setAreaSnData(homePageAreaEntity.data.index5);

        } else {
            showAreaSnView(GONE);
        }
    }

    private void setBannerData(List<MTHomePageEntity.DataBean.ActivityBannerBean> bannerList) {
        MyBannerUtil.setBannerData(banner, bannerList, mContext, 375, 175);
    }

    private void setTopImageView(MTHomePageEntity.DataBean.AdvertBean advertBean) {
        if (advertBean == null) {
            topFrameLayout.setVisibility(GONE);
            imgTop.setVisibility(GONE);
            return;
        }
        if ("1".equals(advertBean.display) && !TextUtils.isEmpty(advertBean.image)) {
            if (null != advertBean && "1".equals(advertBean.display) && !TextUtils.isEmpty(advertBean.image)) {
                topFrameLayout.setVisibility(VISIBLE);
                imgTop.setVisibility(VISIBLE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topFrameLayout.getLayoutParams();
                layoutParams.height = (int) ((100 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));

                GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(advertBean.image), imgTop);

                switch (advertBean.hotType) {
                    case "1":
                        topView1.setVisibility(VISIBLE);
                        topView2.setVisibility(GONE);
                        topView3.setVisibility(GONE);
                        break;
                    case "2":
                        topView1.setVisibility(VISIBLE);
                        topView2.setVisibility(GONE);
                        topView3.setVisibility(VISIBLE);
                        break;
                    case "3":
                        topView1.setVisibility(VISIBLE);
                        topView2.setVisibility(VISIBLE);
                        topView3.setVisibility(VISIBLE);
                        break;
                    default:
                        break;
                }

                List<MTHomePageEntity.DataBean.AdvertBean.HotContentBean> list = advertBean.hotContent;

                topView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size() > 0) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(0).type, list.get(0).content);
                        }
                    }
                });
                topView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size() == 3) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(1).type, list.get(1).content);
                        }
                    }
                });
                topView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size() == 2) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(1).type, list.get(1).content);
                        } else if (list.size() == 3) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(2).type, list.get(2).content);
                        }
                    }
                });
            } else {
                imgTop.setVisibility(GONE);
            }
        }
    }

    private void setViewpager(List<MTHomePageEntity.DataBean.ActivityBannerBean> iconList, MTHomePageEntity.DataBean.IconBg iconBg) {
        if (iconList == null || iconList.size() <= 0) {
            iconsView.setVisibility(GONE);
        } else {
            iconsView.setVisibility(VISIBLE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iconsBgIv.getLayoutParams();
            layoutParams.height = (int) ((146 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));

            /**
             *设置icons的背景
             */
            if (iconBg == null) {
                iconsBgIv.setVisibility(GONE);
            } else {
                if (!TextUtils.isEmpty(iconBg.image) && "1".equals(iconBg.display)) {
                    iconsBgIv.setVisibility(VISIBLE);
                    GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(iconBg.image), iconsBgIv);
                } else {
                    iconsBgIv.setVisibility(GONE);
                }
            }


            /**
             * 每一页显示ICON的个数
             */
            int pageSize = 10;

            /**
             * 总的页数
             */
            int pageCount = (int) Math.ceil(iconList.size() * 1.0 / pageSize);
            List<View> mPagerList = new ArrayList<>();

            for (int i = 0; i < pageCount; i++) {
                //每个页面都是inflate出一个新实例
                NoScrollGridView gridView = (NoScrollGridView) LayoutInflater.from(mContext).inflate(R.layout.mt_home_page_gridview, mPager, false);
                String display;
                if (iconBg == null) {
                    display = "0";
                } else {
                    display = iconBg.display;
                }
                gridView.setAdapter(new MTHomePageGridViewAdapter(mContext, iconList, i, pageSize, display));
                mPagerList.add(gridView);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int pos = position + curIndex * pageSize;
                        MTHomePageEntity.DataBean.ActivityBannerBean activityBannerBean = iconList.get(pos);
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(activityBannerBean.type, activityBannerBean.content);
                    }
                });
            }
            if (mLlDot == null) {
                mLlDot = (LinearLayout) iconsView.findViewById(R.id.ll_dot);
            }
            if (pageCount <= 1) {//如果只有一页，那么隐藏小圆点
                mLlDot.setVisibility(GONE);
            } else {
                mLlDot.removeAllViews();//添加前先移除
                for (int i = 0; i < pageCount; i++) {
                    mLlDot.addView(LayoutInflater.from(mContext).inflate(R.layout.mt_home_page_dot, null));
                }
                // 默认显示第一页
                mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.mt_home_page_dot_selected);
                mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // 取消圆点选中
                        mLlDot.getChildAt(curIndex)
                                .findViewById(R.id.v_dot)
                                .setBackgroundResource(R.drawable.mt_home_page_dot_normal);
                        // 圆点选中
                        mLlDot.getChildAt(position)
                                .findViewById(R.id.v_dot)
                                .setBackgroundResource(R.drawable.mt_home_page_dot_selected);
                        curIndex = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });
            }
            mPager.setAdapter(new MTHomePageViewPagerAdapter(mPagerList));
        }
    }

    private void setBotImageView(MTHomePageEntity.DataBean.AdvertBean advertBean) {
        if (advertBean == null) {
            botFrameLayout.setVisibility(GONE);
            imgBot.setVisibility(GONE);
            return;
        }
        if ("1".equals(advertBean.display) && !TextUtils.isEmpty(advertBean.image)) {
            if (null != advertBean && "1".equals(advertBean.display) && !TextUtils.isEmpty(advertBean.image)) {
                botFrameLayout.setVisibility(VISIBLE);
                imgBot.setVisibility(VISIBLE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) botFrameLayout.getLayoutParams();
                layoutParams.height = (int) ((141 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));

                GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(advertBean.image), imgBot);

                switch (advertBean.hotType) {
                    case "1":
                        botView1.setVisibility(VISIBLE);
                        botView2.setVisibility(GONE);
                        botView3.setVisibility(GONE);
                        break;
                    case "2":
                        botView1.setVisibility(VISIBLE);
                        botView2.setVisibility(GONE);
                        botView3.setVisibility(VISIBLE);
                        break;
                    case "3":
                        botView1.setVisibility(VISIBLE);
                        botView2.setVisibility(VISIBLE);
                        botView3.setVisibility(VISIBLE);
                        break;
                    default:
                        break;
                }

                List<MTHomePageEntity.DataBean.AdvertBean.HotContentBean> list = advertBean.hotContent;

                botView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size() > 0) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(0).type, list.get(0).content);
                        }
                    }
                });
                botView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size() == 3) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(1).type, list.get(1).content);
                        }
                    }
                });
                botView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size() == 2) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(1).type, list.get(1).content);
                        } else if (list.size() == 3) {
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(2).type, list.get(2).content);
                        }
                    }
                });
            } else {
                botFrameLayout.setVisibility(GONE);
                imgBot.setVisibility(GONE);
            }
        }
    }

    /**
     * @param entity
     * @return
     */
    private List<JdBrandSelectedMultiItem> initBrandSelectedList(JdGoodsBrandSelectedEntity entity) {
        List<JdBrandSelectedMultiItem> dataList = new ArrayList<>();
        for (int i = 0; i < entity.getData().size(); i++) {
            dataList.add(entity.getData().get(i));
            List<JdGoodsBrandSelectedEntity.Data.Goods> goodsList = entity.getData().get(i).goodsList;
            int max = goodsList.size();
            for (int pos = 0; pos < max; pos++) {
                if (pos == 1) {
                    goodsList.get(pos).setTag(1);
                } else {
                    goodsList.get(pos).setTag(0);
                }
                dataList.add(goodsList.get(pos));
            }
        }
        return dataList;
    }

    private void showAreaOneView(@Visibility int visibility) {
        //        精选专区
        if (includeAreaTitleOne != null) {
            includeAreaTitleOne.setVisibility(visibility);
        }
        if (llAreaContent1_1 != null) {
            llAreaContent1_1.setVisibility(visibility);
        }
        if (llAreaContent1_2 != null) {
            llAreaContent1_2.setVisibility(visibility);
        }
    }

    private void showAreaTwoView(@Visibility int visibility) {
        //        品牌专区
        if (includeAreaTitleTwo != null) {

            includeAreaTitleTwo.setVisibility(visibility);
        }
        if (llAreaContent2_1 != null) {

            llAreaContent2_1.setVisibility(visibility);
        }
        if (llAreaContent2_2 != null) {

            llAreaContent2_2.setVisibility(visibility);
        }
    }

    private void showBanner2View(@Visibility int visibility) {
        //        banner2
        if (bannerView2 != null) {
            bannerView2.setVisibility(visibility);
        }
    }

    private void showAreaTaoBaoView(@Visibility int visibility) {
        //        淘宝专区
        if (includeAreaTitleTaoBao != null) {
            includeAreaTitleTaoBao.setVisibility(visibility);
        }
        if (includeTaoBaoArea != null) {
            includeTaoBaoArea.setVisibility(visibility);
        }
    }

    private void initAreaOneView() {
        //        第一块Title
        if (includeAreaTitleOne == null) {
            Logger.i("initHeadView走了这里4");
            includeAreaTitleOne = headerView.findViewById(R.id.include_area_title_one);
        }
        if (llAreaContent1_1 == null) {
            llAreaContent1_1 = headerView.findViewById(R.id.ll_area_content_1_1);
        }
        if (llAreaContent1_2 == null) {
            llAreaContent1_2 = headerView.findViewById(R.id.ll_area_content_1_2);
        }
        if (ivAreaTitleOne == null) {
            Logger.i("initHeadView走了这里5");
            ivAreaTitleOne = includeAreaTitleOne.findViewById(R.id.iv_area_title);
        }
        if (ivAreaTitleOneMore == null) {
            ivAreaTitleOneMore = includeAreaTitleOne.findViewById(R.id.iv_area_more);
        }

//        第一个图片位置
        if (includeContentOne1 == null) {
            includeContentOne1 = headerView.findViewById(R.id.include_content_one_1);
            areaOneIncludeView.add(includeContentOne1);
        }
        if (tvContentOne1MainTitle == null) {
            tvContentOne1MainTitle = includeContentOne1.findViewById(R.id.tv_main_title);
            areaOneMainTitleList.add(tvContentOne1MainTitle);
        }
        if (tvContentOne1SubTitle == null) {
            tvContentOne1SubTitle = includeContentOne1.findViewById(R.id.tv_sub_title);
            areaOneSubTitleList.add(tvContentOne1SubTitle);
        }
        if (ivContentOne1_1 == null) {
            ivContentOne1_1 = includeContentOne1.findViewById(R.id.iv_1);
            areaOneImage1List.add(ivContentOne1_1);
        }
        if (ivContentOne1_2 == null) {
            ivContentOne1_2 = includeContentOne1.findViewById(R.id.iv_2);
            areaOneImage2List.add(ivContentOne1_2);
        }

        //        第二个图片位置
        if (includeContentOne2 == null) {
            includeContentOne2 = headerView.findViewById(R.id.include_content_one_2);
            areaOneIncludeView.add(includeContentOne2);
        }
        if (tvContentOne2MainTitle == null) {
            tvContentOne2MainTitle = includeContentOne2.findViewById(R.id.tv_main_title);
            areaOneMainTitleList.add(tvContentOne2MainTitle);
        }
        if (tvContentOne2SubTitle == null) {
            tvContentOne2SubTitle = includeContentOne2.findViewById(R.id.tv_sub_title);
            areaOneSubTitleList.add(tvContentOne2SubTitle);
        }
        if (ivContentOne2_1 == null) {
            ivContentOne2_1 = includeContentOne2.findViewById(R.id.iv_1);
            areaOneImage1List.add(ivContentOne2_1);
        }
        if (ivContentOne2_2 == null) {
            ivContentOne2_2 = includeContentOne2.findViewById(R.id.iv_2);
            areaOneImage2List.add(ivContentOne2_2);
        }
        //        第三个图片位置
        if (includeContentOne3 == null) {
            includeContentOne3 = headerView.findViewById(R.id.include_content_one_3);
            areaOneIncludeView.add(includeContentOne3);
        }
        if (tvContentOne3MainTitle == null) {
            tvContentOne3MainTitle = includeContentOne3.findViewById(R.id.tv_main_title);
            areaOneMainTitleList.add(tvContentOne3MainTitle);
        }
        if (tvContentOne3SubTitle == null) {
            tvContentOne3SubTitle = includeContentOne3.findViewById(R.id.tv_sub_title);
            areaOneSubTitleList.add(tvContentOne3SubTitle);
        }
        if (ivContentOne3_1 == null) {
            ivContentOne3_1 = includeContentOne3.findViewById(R.id.iv_1);
            areaOneImage1List.add(ivContentOne3_1);
        }
        if (ivContentOne3_2 == null) {
            ivContentOne3_2 = includeContentOne3.findViewById(R.id.iv_2);
            areaOneImage2List.add(ivContentOne3_2);
        }
        //        第四个图片位置
        if (includeContentOne4 == null) {
            includeContentOne4 = headerView.findViewById(R.id.include_content_one_4);
            areaOneIncludeView.add(includeContentOne4);
        }
        if (tvContentOne4MainTitle == null) {
            tvContentOne4MainTitle = includeContentOne4.findViewById(R.id.tv_main_title);
            areaOneMainTitleList.add(tvContentOne4MainTitle);
        }
        if (tvContentOne4SubTitle == null) {
            tvContentOne4SubTitle = includeContentOne4.findViewById(R.id.tv_sub_title);
            areaOneSubTitleList.add(tvContentOne4SubTitle);
        }
        if (ivContentOne4_1 == null) {
            ivContentOne4_1 = includeContentOne4.findViewById(R.id.iv_1);
            areaOneImage1List.add(ivContentOne4_1);
        }
        if (ivContentOne4_2 == null) {
            ivContentOne4_2 = includeContentOne4.findViewById(R.id.iv_2);
            areaOneImage2List.add(ivContentOne4_2);
        }
        //        第五个图片位置
        if (includeContentOne5 == null) {
            includeContentOne5 = headerView.findViewById(R.id.include_content_one_5);
            areaOneIncludeView.add(includeContentOne5);
        }
        if (tvContentOne5MainTitle == null) {
            tvContentOne5MainTitle = includeContentOne5.findViewById(R.id.tv_main_title);
            areaOneMainTitleList.add(tvContentOne5MainTitle);
        }
        if (tvContentOne5SubTitle == null) {
            tvContentOne5SubTitle = includeContentOne5.findViewById(R.id.tv_sub_title);
            areaOneSubTitleList.add(tvContentOne5SubTitle);
        }
        if (ivContentOne5_1 == null) {
            ivContentOne5_1 = includeContentOne5.findViewById(R.id.iv_1);
            areaOneImage1List.add(ivContentOne5_1);
        }
        if (ivContentOne5_2 == null) {
            ivContentOne5_2 = includeContentOne5.findViewById(R.id.iv_2);
            areaOneImage2List.add(ivContentOne5_2);
        }
        //        第六个图片位置
        if (includeContentOne6 == null) {
            includeContentOne6 = headerView.findViewById(R.id.include_content_one_6);
            areaOneIncludeView.add(includeContentOne6);
        }
        if (tvContentOne6MainTitle == null) {
            tvContentOne6MainTitle = includeContentOne6.findViewById(R.id.tv_main_title);
            areaOneMainTitleList.add(tvContentOne6MainTitle);
        }
        if (tvContentOne6SubTitle == null) {
            tvContentOne6SubTitle = includeContentOne6.findViewById(R.id.tv_sub_title);
            areaOneSubTitleList.add(tvContentOne6SubTitle);
        }
        if (ivContentOne6_1 == null) {
            ivContentOne6_1 = includeContentOne6.findViewById(R.id.iv_1);
            areaOneImage1List.add(ivContentOne6_1);
        }
        if (ivContentOne6_2 == null) {
            ivContentOne6_2 = includeContentOne6.findViewById(R.id.iv_2);
            areaOneImage2List.add(ivContentOne6_2);
        }
    }

    private void setAreaOneData(HomePageAreaEntity.DataBean.Index1Bean data) {
        final HomePageAreaEntity.DataBean.Index1Bean index1 = data;
        RxUtil.clicks(includeAreaTitleOne, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                精选专区更多跳转
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(index1.indexType, index1.indexContent);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index1.titleIcon, ivAreaTitleOne);
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index1.moreUrl, ivAreaTitleOneMore);
        List<HomePageAreaEntity.DataBean.Index1Bean.TitleContentBean> content1 = index1.titleContent;
        if (content1 != null) {
            for (int i = 0; i < content1.size(); i++) {
                HomePageAreaEntity.DataBean.Index1Bean.TitleContentBean titleContentBean = content1.get(i);
                areaOneMainTitleList.get(i).setText(titleContentBean.title);
                areaOneSubTitleList.get(i).setText(titleContentBean.subTitle);
                GlideUtil.showImageNoSuffix(mContext, titleContentBean.img1, areaOneImage1List.get(i));
                GlideUtil.showImageNoSuffix(mContext, titleContentBean.img2, areaOneImage2List.get(i));
                RxUtil.clicks(areaOneIncludeView.get(i), new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(titleContentBean.type, titleContentBean.content);

                    }
                });
            }
        }
    }

    private void initAreaTwoView() {
        //第二块Title
        if (includeAreaTitleTwo == null) {
            Logger.i("initHeadView走了这里4");
            includeAreaTitleTwo = headerView.findViewById(R.id.include_area_title_two);
        }
        if (llAreaContent2_1 == null) {
            llAreaContent2_1 = headerView.findViewById(R.id.ll_area_content_2_1);
        }
        if (llAreaContent2_2 == null) {
            llAreaContent2_2 = headerView.findViewById(R.id.ll_area_content_2_2);
        }
        if (ivAreaTitleTwo == null) {
            Logger.i("initHeadView走了这里5");
            ivAreaTitleTwo = includeAreaTitleTwo.findViewById(R.id.iv_area_title);
        }
        if (ivAreaTitleTwoMore == null) {
            ivAreaTitleTwoMore = includeAreaTitleTwo.findViewById(R.id.iv_area_more);
        }
//        第二块第一个内容

//        第二块第一个图片位置
        if (includeContentTwo1 == null) {
            includeContentTwo1 = headerView.findViewById(R.id.include_content_two_1);
            areaTwoIncludeView.add(includeContentTwo1);
        }
        if (tvContentTwo1MainTitle == null) {
            tvContentTwo1MainTitle = includeContentTwo1.findViewById(R.id.tv_main_title);
            areaTwoMainTitleList.add(tvContentTwo1MainTitle);
        }
        if (tvContentTwo1SubTitle == null) {
            tvContentTwo1SubTitle = includeContentTwo1.findViewById(R.id.tv_sub_title);
            areaTwoSubTitleList.add(tvContentTwo1SubTitle);
        }
        if (ivContentTwo1Image == null) {
            ivContentTwo1Image = includeContentTwo1.findViewById(R.id.iv_image);
            areaTwoImageList.add(ivContentTwo1Image);
        }
        if (ivContentTwo1Logo == null) {
            ivContentTwo1Logo = includeContentTwo1.findViewById(R.id.iv_logo);
            areaTwoLogoImageList.add(ivContentTwo1Logo);
        }
        if (tvContentTwo1ShopName == null) {
            tvContentTwo1ShopName = includeContentTwo1.findViewById(R.id.tv_shop_name);
            areaTwoShopNameList.add(tvContentTwo1ShopName);
        }
//        第二块第二个图片位置
        if (includeContentTwo2 == null) {
            includeContentTwo2 = headerView.findViewById(R.id.include_content_two_2);
            areaTwoIncludeView.add(includeContentTwo2);
        }
        if (tvContentTwo2MainTitle == null) {
            tvContentTwo2MainTitle = includeContentTwo2.findViewById(R.id.tv_main_title);
            areaTwoMainTitleList.add(tvContentTwo2MainTitle);
        }
        if (tvContentTwo2SubTitle == null) {
            tvContentTwo2SubTitle = includeContentTwo2.findViewById(R.id.tv_sub_title);
            areaTwoSubTitleList.add(tvContentTwo2SubTitle);
        }
        if (ivContentTwo2Image == null) {
            ivContentTwo2Image = includeContentTwo2.findViewById(R.id.iv_image);
            areaTwoImageList.add(ivContentTwo2Image);
        }
        if (ivContentTwo2Logo == null) {
            ivContentTwo2Logo = includeContentTwo2.findViewById(R.id.iv_logo);
            areaTwoLogoImageList.add(ivContentTwo2Logo);
        }
        if (tvContentTwo2ShopName == null) {
            tvContentTwo2ShopName = includeContentTwo2.findViewById(R.id.tv_shop_name);
            areaTwoShopNameList.add(tvContentTwo2ShopName);
        }
//        第二块第三个图片位置
        if (includeContentTwo3 == null) {
            includeContentTwo3 = headerView.findViewById(R.id.include_content_two_3);
            areaTwoIncludeView.add(includeContentTwo3);
        }
        if (tvContentTwo3MainTitle == null) {
            tvContentTwo3MainTitle = includeContentTwo3.findViewById(R.id.tv_main_title);
            areaTwoMainTitleList.add(tvContentTwo3MainTitle);
        }
        if (tvContentTwo3SubTitle == null) {
            tvContentTwo3SubTitle = includeContentTwo3.findViewById(R.id.tv_sub_title);
            areaTwoSubTitleList.add(tvContentTwo3SubTitle);
        }
        if (ivContentTwo3Image == null) {
            ivContentTwo3Image = includeContentTwo3.findViewById(R.id.iv_image);
            areaTwoImageList.add(ivContentTwo3Image);
        }
        if (ivContentTwo3Logo == null) {
            ivContentTwo3Logo = includeContentTwo3.findViewById(R.id.iv_logo);
            areaTwoLogoImageList.add(ivContentTwo3Logo);
        }
        if (tvContentTwo3ShopName == null) {
            tvContentTwo3ShopName = includeContentTwo3.findViewById(R.id.tv_shop_name);
            areaTwoShopNameList.add(tvContentTwo3ShopName);
        }
        //        第二块第四个图片位置
        if (includeContentTwo4 == null) {
            includeContentTwo4 = headerView.findViewById(R.id.include_content_two_4);
            areaTwoIncludeView.add(includeContentTwo4);
        }
        if (tvContentTwo4MainTitle == null) {
            tvContentTwo4MainTitle = includeContentTwo4.findViewById(R.id.tv_main_title);
            areaTwoMainTitleList.add(tvContentTwo4MainTitle);
        }
        if (tvContentTwo4SubTitle == null) {
            tvContentTwo4SubTitle = includeContentTwo4.findViewById(R.id.tv_sub_title);
            areaTwoSubTitleList.add(tvContentTwo4SubTitle);
        }
        if (ivContentTwo4Image == null) {
            ivContentTwo4Image = includeContentTwo4.findViewById(R.id.iv_image);
            areaTwoImageList.add(ivContentTwo4Image);
        }
        if (ivContentTwo4Logo == null) {
            ivContentTwo4Logo = includeContentTwo4.findViewById(R.id.iv_logo);
            areaTwoLogoImageList.add(ivContentTwo4Logo);
        }
        if (tvContentTwo4ShopName == null) {
            tvContentTwo4ShopName = includeContentTwo4.findViewById(R.id.tv_shop_name);
            areaTwoShopNameList.add(tvContentTwo4ShopName);
        }

    }

    private void setAreaTwoData(HomePageAreaEntity.DataBean.Index2Bean data) {
        final HomePageAreaEntity.DataBean.Index2Bean index2 = data;
        RxUtil.clicks(includeAreaTitleTwo, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                //                精选专区更多跳转
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(index2.indexType, index2.indexContent);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index2.titleIcon, ivAreaTitleTwo);
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index2.moreUrl, ivAreaTitleTwoMore);
        List<HomePageAreaEntity.DataBean.Index2Bean.TitleContentBeanX> content2 = index2.titleContent;
        for (int i = 0; i < content2.size(); i++) {
            HomePageAreaEntity.DataBean.Index2Bean.TitleContentBeanX contentBeanX = content2.get(i);
            areaTwoMainTitleList.get(i).setText(contentBeanX.title);
            areaTwoSubTitleList.get(i).setText(contentBeanX.subTitle);
            GlideUtil.showImageNoSuffix(mContext, contentBeanX.img1, areaTwoImageList.get(i));
            GlideUtil.showImageNoSuffixNoPlaceholder(mContext, contentBeanX.logo, areaTwoLogoImageList.get(i));
            areaTwoShopNameList.get(i).setText(contentBeanX.shopName);
            RxUtil.clicks(areaTwoIncludeView.get(i), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(contentBeanX.type, contentBeanX.content);
                }
            });
        }
    }

    private void initBanner2View() {
        if (bannerView2 == null) {
            bannerView2 = headerView.findViewById(R.id.include_banner_2);
        }
        if (banner2 == null) {
            banner2 = (Banner) bannerView2.findViewById(R.id.banner);
        }

    }

    private void setBanner2Data(List<HomePageAreaEntity.DataBean.Adv1Bean.TitleContentBeanXXX> content) {
        MyBannerUtil.setBannerData(banner2, content, mContext, 250, 60);
    }

    private void initAreaTaoBaoView() {
        //第三块Title/淘宝Title
        if (includeAreaTitleTaoBao == null) {
            Logger.i("initHeadView走了这里4");
            includeAreaTitleTaoBao = headerView.findViewById(R.id.include_area_title_taobao);
        }
        if (ivAreaTitleThree == null) {
            Logger.i("initHeadView走了这里5");
            ivAreaTitleThree = includeAreaTitleTaoBao.findViewById(R.id.iv_area_title);
        }
        if (ivAreaTitleThreeMore == null) {
            ivAreaTitleThreeMore = includeAreaTitleTaoBao.findViewById(R.id.iv_area_more);
        }

//        淘宝专区
        if (includeTaoBaoArea == null) {
            includeTaoBaoArea = headerView.findViewById(R.id.include_tao_bao_area);
        }
        if (ivTaoBao1 == null) {
            ivTaoBao1 = includeTaoBaoArea.findViewById(R.id.iv_tao_bao_1);
            areaTaoBaoImgList.add(ivTaoBao1);
        }
        if (tvTaoBaoTitle1 == null) {
            tvTaoBaoTitle1 = new TextView(mContext);
            areaTaoBaoTitleList.add(tvTaoBaoTitle1);
        }
        if (ivTaoBao2 == null) {
            ivTaoBao2 = includeTaoBaoArea.findViewById(R.id.iv_tao_bao_2);
            areaTaoBaoImgList.add(ivTaoBao2);
        }
        if (tvTaoBaoTitle2 == null) {
            tvTaoBaoTitle2 = new TextView(mContext);
            areaTaoBaoTitleList.add(tvTaoBaoTitle2);
        }
        if (ivTaoBao3 == null) {
            ivTaoBao3 = includeTaoBaoArea.findViewById(R.id.iv_tao_bao_3);
            areaTaoBaoImgList.add(ivTaoBao3);
        }
        if (tvTaoBaoTitle3 == null) {
            tvTaoBaoTitle3 = includeTaoBaoArea.findViewById(R.id.tv_tao_bao_title3);
            areaTaoBaoTitleList.add(tvTaoBaoTitle3);
        }
        if (ivTaoBao4 == null) {
            ivTaoBao4 = includeTaoBaoArea.findViewById(R.id.iv_tao_bao_4);
            areaTaoBaoImgList.add(ivTaoBao4);
        }
        if (tvTaoBaoTitle4 == null) {
            tvTaoBaoTitle4 = includeTaoBaoArea.findViewById(R.id.tv_tao_bao_title4);
            areaTaoBaoTitleList.add(tvTaoBaoTitle4);
        }
        if (ivTaoBao5 == null) {
            ivTaoBao5 = includeTaoBaoArea.findViewById(R.id.iv_tao_bao_5);
            areaTaoBaoImgList.add(ivTaoBao5);
        }
        if (tvTaoBaoTitle5 == null) {
            tvTaoBaoTitle5 = includeTaoBaoArea.findViewById(R.id.tv_tao_bao_title5);
            areaTaoBaoTitleList.add(tvTaoBaoTitle5);
        }

    }

    private void setAreaTaoBaoData(HomePageAreaEntity.DataBean.Index3Bean data) {
        final HomePageAreaEntity.DataBean.Index3Bean index3 = data;
        RxUtil.clicks(includeAreaTitleTaoBao, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                精选专区更多跳转
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(index3.indexType, index3.indexContent);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index3.titleIcon, ivAreaTitleThree);
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index3.moreUrl, ivAreaTitleThreeMore);
        List<HomePageAreaEntity.DataBean.Index3Bean.TitleContentBeanXX> content3 = index3.titleContent;
        for (int i = 0; i < content3.size(); i++) {
            HomePageAreaEntity.DataBean.Index3Bean.TitleContentBeanXX contentBeanXX = content3.get(i);
            areaTaoBaoTitleList.get(i).setText(contentBeanXX.title);
            ImageView imageView = areaTaoBaoImgList.get(i);
            GlideUtil.showImageNoSuffix(mContext, contentBeanXX.img1, imageView);
            RxUtil.clicks(imageView, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(contentBeanXX.type, contentBeanXX.content);
                }
            });
        }
    }

    private void initAreaJDView() {
        //第五块Title/京东Title
        if (includeAreaTitleJD == null) {
            Logger.i("initHeadView走了这里4");
            includeAreaTitleJD = headerView.findViewById(R.id.include_area_title_jd);
        }
        if (ivAreaTitleFive == null) {
            Logger.i("initHeadView走了这里5");
            ivAreaTitleFive = includeAreaTitleJD.findViewById(R.id.iv_area_title);
        }
        if (ivAreaTitleFiveMore == null) {
            ivAreaTitleFiveMore = includeAreaTitleJD.findViewById(R.id.iv_area_more);
        }

//        京东专区
        if (includeJDArea == null) {
            includeJDArea = headerView.findViewById(R.id.include_jd_area);
        }
        if (ivJD1 == null) {
            ivJD1 = includeJDArea.findViewById(R.id.iv_tao_bao_1);
            areaJDImgList.add(ivJD1);
        }
        if (tvJDTitle1 == null) {
            tvJDTitle1 = new TextView(mContext);
            areaJDTitleList.add(tvJDTitle1);
        }
        if (ivJD2 == null) {
            ivJD2 = includeJDArea.findViewById(R.id.iv_tao_bao_2);
            areaJDImgList.add(ivJD2);
        }
        if (tvJDTitle2 == null) {
            tvJDTitle2 = new TextView(mContext);
            areaJDTitleList.add(tvJDTitle2);
        }
        if (ivJD3 == null) {
            ivJD3 = includeJDArea.findViewById(R.id.iv_tao_bao_3);
            areaJDImgList.add(ivJD3);
        }
        if (tvJDTitle3 == null) {
            tvJDTitle3 = includeJDArea.findViewById(R.id.tv_tao_bao_title3);
            areaJDTitleList.add(tvJDTitle3);
        }
        if (ivJD4 == null) {
            ivJD4 = includeJDArea.findViewById(R.id.iv_tao_bao_4);
            areaJDImgList.add(ivJD4);
        }
        if (tvJDTitle4 == null) {
            tvJDTitle4 = includeJDArea.findViewById(R.id.tv_tao_bao_title4);
            areaJDTitleList.add(tvJDTitle4);
        }
        if (ivJD5 == null) {
            ivJD5 = includeJDArea.findViewById(R.id.iv_tao_bao_5);
            areaJDImgList.add(ivJD5);
        }
        if (tvJDTitle5 == null) {
            tvJDTitle5 = includeJDArea.findViewById(R.id.tv_tao_bao_title5);
            areaJDTitleList.add(tvJDTitle5);
        }

    }

    private void showAreaJDView(@Visibility int visibility) {
        //        淘宝专区
        if (includeAreaTitleJD != null) {
            includeAreaTitleJD.setVisibility(visibility);
        }
        if (includeJDArea != null) {
            includeJDArea.setVisibility(visibility);
        }
    }

    private void setAreaJDData(HomePageAreaEntity.DataBean.Index4Bean data) {
        final HomePageAreaEntity.DataBean.Index4Bean index4 = data;
        RxUtil.clicks(includeAreaTitleJD, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                精选专区更多跳转
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(index4.indexType, index4.indexContent);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index4.titleIcon, ivAreaTitleFive);
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index4.moreUrl, ivAreaTitleFiveMore);
        List<HomePageAreaEntity.DataBean.Index4Bean.TitleContentBeanXXX> content4 = index4.titleContent;
        for (int i = 0; i < content4.size(); i++) {
            HomePageAreaEntity.DataBean.Index4Bean.TitleContentBeanXXX contentBeanXXX = content4.get(i);
            areaJDTitleList.get(i).setText(contentBeanXXX.title);
            ImageView imageView = areaJDImgList.get(i);
            GlideUtil.showImageNoSuffix(mContext, contentBeanXXX.img1, imageView);
            RxUtil.clicks(imageView, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(contentBeanXXX.type, contentBeanXXX.content);
                }
            });
        }
    }

    private void initAreaSnView() {
        //第六块Title/苏宁Title
        if (includeAreaTitleSn == null) {
            Logger.i("initHeadView走了这里4");
            includeAreaTitleSn = headerView.findViewById(R.id.include_area_title_sn);
        }
        if (ivAreaTitleSix == null) {
            Logger.i("initHeadView走了这里5");
            ivAreaTitleSix = includeAreaTitleSn.findViewById(R.id.iv_area_title);
        }
        if (ivAreaTitleSixMore == null) {
            ivAreaTitleSixMore = includeAreaTitleSn.findViewById(R.id.iv_area_more);
        }

//        苏宁专区
        if (includeSnArea == null) {
            includeSnArea = headerView.findViewById(R.id.include_sn_area);
        }
        if (ivSn1 == null) {
            ivSn1 = includeSnArea.findViewById(R.id.iv_tao_bao_1);
            areaSnImgList.add(ivSn1);
        }
        if (tvSnTitle1 == null) {
            tvSnTitle1 = new TextView(mContext);
            areaSnTitleList.add(tvSnTitle1);
        }
        if (ivSn2 == null) {
            ivSn2 = includeSnArea.findViewById(R.id.iv_tao_bao_2);
            areaSnImgList.add(ivSn2);
        }
        if (tvSnTitle2 == null) {
            tvSnTitle2 = new TextView(mContext);
            areaSnTitleList.add(tvSnTitle2);
        }
        if (ivSn3 == null) {
            ivSn3 = includeSnArea.findViewById(R.id.iv_tao_bao_3);
            areaSnImgList.add(ivSn3);
        }
        if (tvSnTitle3 == null) {
            tvSnTitle3 = includeSnArea.findViewById(R.id.tv_tao_bao_title3);
            areaSnTitleList.add(tvSnTitle3);
        }
        if (ivSn4 == null) {
            ivSn4 = includeSnArea.findViewById(R.id.iv_tao_bao_4);
            areaSnImgList.add(ivSn4);
        }
        if (tvSnTitle4 == null) {
            tvSnTitle4 = includeSnArea.findViewById(R.id.tv_tao_bao_title4);
            areaSnTitleList.add(tvSnTitle4);
        }
        if (ivSn5 == null) {
            ivSn5 = includeSnArea.findViewById(R.id.iv_tao_bao_5);
            areaSnImgList.add(ivSn5);
        }
        if (tvSnTitle5 == null) {
            tvSnTitle5 = includeSnArea.findViewById(R.id.tv_tao_bao_title5);
            areaSnTitleList.add(tvSnTitle5);
        }
    }

    private void showAreaSnView(@Visibility int visibility) {
        //        淘宝专区
        if (includeAreaTitleSn != null) {
            includeAreaTitleSn.setVisibility(visibility);
        }
        if (includeSnArea != null) {
            includeSnArea.setVisibility(visibility);
        }
    }

    private void setAreaSnData(HomePageAreaEntity.DataBean.Index5Bean data) {
        final HomePageAreaEntity.DataBean.Index5Bean index5 = data;
        RxUtil.clicks(includeAreaTitleSn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                精选专区更多跳转
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(index5.indexType, index5.indexContent);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index5.titleIcon, ivAreaTitleSix);
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, index5.moreUrl, ivAreaTitleSixMore);
        List<HomePageAreaEntity.DataBean.Index5Bean.TitleContentBeanXXX> content5 = index5.titleContent;
        for (int i = 0; i < content5.size(); i++) {
            HomePageAreaEntity.DataBean.Index5Bean.TitleContentBeanXXX contentBeanXXX = content5.get(i);
            areaSnTitleList.get(i).setText(contentBeanXXX.title);
            ImageView imageView = areaSnImgList.get(i);
            GlideUtil.showImageNoSuffix(mContext, contentBeanXXX.img1, imageView);
            RxUtil.clicks(imageView, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(contentBeanXXX.type, contentBeanXXX.content);
                }
            });
        }
    }

    private void initView(View rootView) {
        emptyView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) emptyView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(this);
        ivReturnTop = (ImageView) rootView.findViewById(R.id.iv_return_top);
        tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
        tvAddress.setOnClickListener(this);
        tvSearch = (TextView) rootView.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);
        ivQrCodeScan = (ImageView) rootView.findViewById(R.id.iv_qrCode_scan);
        ivQrCodeScan.setOnClickListener(this);
        ivMessage = (ImageView) rootView.findViewById(R.id.iv_message);
        ivMessage.setOnClickListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                return !swipeRefreshLayout.isRefreshing();
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        brandSelectedAdapter = new HomePageJdBrandSelectedAdapter(new ArrayList<JdBrandSelectedMultiItem>());
        recyclerView.setAdapter(brandSelectedAdapter);
        brandSelectedAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                JdBrandSelectedMultiItem multiItem = (JdBrandSelectedMultiItem) brandSelectedAdapter.getItem(position);
                return multiItem.getSpanSize();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);

        homeTitleLayout = (LinearLayout) rootView.findViewById(R.id.home_title_layout);
        homeTitleLayout.post(new Runnable() {
            @Override
            public void run() {
                int oldHeight = homeTitleLayout.getHeight();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) homeTitleLayout.getLayoutParams();
                int statusBarHeight = StatusBarUtils.getStatusBarHeight(mContext);
                layoutParams.height = oldHeight + statusBarHeight;
                homeTitleLayout.setPadding(0, statusBarHeight, 0, 0);
            }
        });


        ivMessageBot = (ImageView) rootView.findViewById(R.id.iv_message_dot);
    }

    private void initData() {
        if (sp.getBoolean(Constants.HAS_MESSAGE, false)) {
            ivMessageBot.setVisibility(VISIBLE);
        } else {
            ivMessageBot.setVisibility(GONE);
        }
    }

    private void initListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
                brandSelectedAdapter.setEnableLoadMore(false);
            }
        });
        brandSelectedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JdBrandSelectedMultiItem multiItem = (JdBrandSelectedMultiItem) adapter.getItem(position);
                if (multiItem instanceof JdGoodsBrandSelectedEntity.Data) {
                    JdGoodsBrandSelectedEntity.Data data = (JdGoodsBrandSelectedEntity.Data) multiItem;
                    JumpJdActivityUtils.toJdBrandGoodsListActivity(mContext, data.brandName);
                } else if (multiItem instanceof JdGoodsBrandSelectedEntity.Data.Goods) {
                    JdGoodsBrandSelectedEntity.Data.Goods goods = (JdGoodsBrandSelectedEntity.Data.Goods) multiItem;
                    if (!TextUtils.isEmpty(goods.sku)) {
                        JumpJdActivityUtils.toGoodsDetail(mContext, goods.sku);
                    }
                }
            }
        });
        brandSelectedAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextBrandSelected();
                swipeRefreshLayout.setEnabled(false);
            }
        }, recyclerView);
        ivReturnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                Logger.i("ray--666-" + scrollDy);
                int screenHeight3 = ScreenUtils.getScreenHeight(mContext) * 3;
                if (scrollDy > screenHeight3) {
                    ivReturnTop.setVisibility(VISIBLE);
                } else {
                    ivReturnTop.setVisibility(GONE);
                }

                /**
                 * 设置title
                 */
                Drawable addressWhite = mContext.getResources().getDrawable(R.mipmap.iv_address_home_title_white);
                addressWhite.setBounds(0, 0, addressWhite.getMinimumWidth(), addressWhite.getMinimumHeight());
                Drawable addressBlack = mContext.getResources().getDrawable(R.mipmap.iv_address_home_title_black);
                addressBlack.setBounds(0, 0, addressBlack.getMinimumWidth(), addressBlack.getMinimumHeight());

                Drawable searchWhite = mContext.getResources().getDrawable(R.mipmap.iv_search_home_title_white);
                searchWhite.setBounds(0, 0, searchWhite.getMinimumWidth(), searchWhite.getMinimumHeight());
                Drawable searchBlack = mContext.getResources().getDrawable(R.mipmap.iv_search_home_title_black);
                searchBlack.setBounds(0, 0, searchBlack.getMinimumWidth(), searchBlack.getMinimumHeight());

                if (scrollDy == 0) {
                    homeTitleLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                    tvAddress.setTextColor(Color.argb((int) 255, 255, 255, 255));
                    tvAddress.setCompoundDrawables(null, null, addressWhite, null);

                    tvSearch.setBackgroundResource(R.drawable.searchview_line_white);
                    tvSearch.setCompoundDrawables(searchBlack, null, null, null);
                    tvSearch.setHintTextColor(getResources().getColor(R.color.color_999));

                    ivQrCodeScan.setImageResource(R.mipmap.iv_qr_home_title_white);
                    ivMessage.setImageResource(R.mipmap.iv_message_home_title_white);
                } else if (scrollDy > 0 && scrollDy <= 200) {
                    float scale = (float) scrollDy / 200;
                    float alpha = (255 * scale);
                    homeTitleLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));

                    tvAddress.setTextColor(Color.argb((int) 255, 255, 255, 255));
                    tvAddress.setCompoundDrawables(null, null, addressWhite, null);

                    tvSearch.setBackgroundResource(R.drawable.searchview_line_white);
                    tvSearch.setCompoundDrawables(searchBlack, null, null, null);
                    tvSearch.setHintTextColor(getResources().getColor(R.color.color_999));

                    ivQrCodeScan.setImageResource(R.mipmap.iv_qr_home_title_white);
                    ivMessage.setImageResource(R.mipmap.iv_message_home_title_white);
                } else {
                    homeTitleLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    tvAddress.setTextColor(getResources().getColor(R.color.black));
                    tvAddress.setCompoundDrawables(null, null, addressBlack, null);

                    tvSearch.setBackgroundResource(R.drawable.searchview_line_black);
                    tvSearch.setCompoundDrawables(searchWhite, null, null, null);
                    tvSearch.setHintTextColor(getResources().getColor(R.color.white));

                    ivQrCodeScan.setImageResource(R.mipmap.iv_qr_home_title_black);
                    ivMessage.setImageResource(R.mipmap.iv_message_home_title_black);
                }


                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void getNextBrandSelected() {
        page++;
        getBrandSelectedList();
    }

    /**
     * 主页定位成功后发送广播执行该方法
     *
     * @param aMapLocation
     */
    @Override
    public void subOnReceive(AMapLocation aMapLocation) {
        super.subOnReceive(aMapLocation);

        if (aMapLocation.getErrorCode() == 0) {
            Logger.i("执行了：subOnReceive()");
            if (request == null) {
                request = new AccountNetRequest(mContext);
            }
            request.location(String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getLongitude()), new RequestCallBack<Location>() {
                @Override
                public void onSuccess(ResponseInfo<Location> responseInfo) {
                    Location result = responseInfo.result;
                    if (result.code == 1) {
                        Location.location location = result.location;
                        lat = location.lat;
                        lng = location.lng;
                        cityId = location.cityId;
                        countyId = location.countyId;
                        tvAddress.setText(location.countyName);
                        Logger.i("getShopsList：自动定位后");
//                        getShopsList();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        } else {//定位失败
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                showToast("无定位权限");
            }
            if (!NetworkUtils.isConnected(mContext)) {
                Logger.i("noNetWork,emptyView");
                brandSelectedAdapter.setEmptyView(emptyView);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (isFirst) {
                ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            if (null != activity) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (swipeRefreshLayout != null) {
                                            swipeRefreshLayout.setRefreshing(true);
                                        }
                                        isFirst = false;
                                        getFirstPageData();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getFirstPageData() {
        page = 0;
        getFirstPageDataFlag = true;
        getHeaderData();
        Logger.i("getShopsList：getFirstPageData");
    }

    /**
     * 重新定位
     */
    void reLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            AMapLocationUtil.getInstance().initLocation(mContext)
                    .location(new AMapLocationSuccessListener() {
                        @Override
                        public void amapLocationSuccessListener(AMapLocation aMapLocation, Location.location location) {
                            cityId = location.cityId;
                            countyId = location.countyId;
                            tvAddress.setText(location.countyName);
                        }

                        @Override
                        public void amapLocationFailureListener(HttpException e, String s) {

                        }
                    }).startLocation();
        }

    }

    /**
     * @hide
     */
    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.tv_address://定位
                intent = new Intent(mContext, AreaSelectionActivity.class);
                break;
            case R.id.tv_search://商品搜索
//                intent = new Intent(mContext, SearchCommodityActivity.class);
                JumpCardActivityUtils.toCardHomePage(mContext);
                break;
            case R.id.iv_qrCode_scan://扫一扫

                if (isLogin()) {
                    intent = new Intent(mContext, MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                }
                JumpCardActivityUtils.toCardJdBrandList(mContext);
                break;
            case R.id.iv_message://会话或消息列表
//                if (isLogin()) {
//                    intent = new Intent(mContext, InformationActivity.class);
//                } else {
//                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
//                }
                // 测试 跳转购物卡详情
                JumpCardActivityUtils.toCardCommodityDetailActivity(mContext,"2131");

                break;
            case R.id.tv_refresh://刷新按钮
                swipeRefreshLayout.setRefreshing(true);
                getFirstPageData();
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


}
