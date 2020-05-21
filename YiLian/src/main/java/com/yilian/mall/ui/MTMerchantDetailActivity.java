package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MerchantRecommendAdapter;
import com.yilian.mall.adapter.MerchantVpAdapter;
import com.yilian.mall.adapter.SpecialComboAdapter;
import com.yilian.mall.adapter.mEvaluateAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.MTMerchantCommendEntity;
import com.yilian.mall.entity.MTMerchantDetailEntity;
import com.yilian.mall.entity.MerchantPraiseEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.http.NearbyNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.MapUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ShopsSort;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuqi on 2016/10/18 0018.
 * 美团商家详情
 */
public class MTMerchantDetailActivity extends BaseActivity implements View.OnClickListener, PullToRefreshScrollView.ScrollListener {
    private ImageView mV3Back;
    private ImageView mV3Left;
    private TextView mV3Title;
    private ImageView mV3Shop;
    private ImageView mV3Share;
    private ViewPager mViewpager;
    private LinearLayout mLlPoints;
    private Button mBtnStoreConsumer;
    private LinearLayout mllmerchant_head;
    private TextView mTvComboPrice;
    private FrameLayout mFlBanner;
    private TextView mTvContentTitle;
    private RatingBar mRatingBar;
    private TextView mTvContentGrade;
    private TextView mTvContentAddress;
    private ImageView mIvCall;
    private TextView mTvContentTime;
    private TextView mTvPatronizeCount;
    private View mView2;
    private NoScrollListView mListViewCombo;
    private RelativeLayout mrlStoreConsume;
    private TextView mTvMerchantDesp;
    private TextView mTvShopDespContent;
    private TextView mTvNearMerchantRecommend;
    private NoScrollListView mLvNearMerchantRecommend;
    private Button mBtnClickPraise;
    private Button mBtnMerchantGo;
    private LinearLayout mLlayoutBottomMenu;
    private PullToRefreshScrollView mSvPullRefresh;
    private Context mContext;
    private MTNetRequest mtNetRequest;
    private MTMerchantDetailEntity.InfoBean merchantInfo;
    private MerchantVpAdapter VpAdapter;
    private ArrayList<ImageView> dotImageViews = new ArrayList<>();
    private TextView mSpecialCombo;
    private RatingBar mAllRating;
    private TextView mAllGraded;
    private NoScrollListView mEvaluateList;
    private List<MTMerchantDetailEntity.InfoBean.CommentsBean> commentsBeanList;
    private int page;
    private ArrayList<MTMerchantCommendEntity.MerchantListBean> copyMerchantList = new ArrayList<>();
    ;
    private String merchant_id;
    private NearbyNetRequest nearbyNetRequest;
    private String latitude;
    private String longitude;
    private NaviLatLng mNaviEnd;
    private Button storeConsumerDown;
    private JPNetRequest jpNetRequest;
    private boolean isFavorite = false;
    private List<MTMerchantDetailEntity.InfoBean.PackageBean> packages;

    //分享有关
    private UmengDialog mShareDialog;
    private String shareTitle, shareContent, share_Img, shareUrl, shareImg;
    private MerchantRecommendAdapter lvNearMerchantAdapter;
    private LinearLayout llContent;
    private LinearLayout llMerchantIntroduce;
    private FrameLayout flBottom;
    private TextView tvImageCount;
    private LinearLayout llTotalComment;
    private ImageView ivShowMore;
    private LinearLayout llGrayBottom;
    private TextView tvParticipation;
    private LinearLayout layoutCombo;
    private boolean isPraise;

    private LinearLayout layoutContentOnline;
    private TextView tvContentShopName;
    private String supplierId;

    private double finalLongitude;
    private double finalLatitude;
    private LinearLayout loadErrorView;
    private TextView tvRefresh;
    private Banner banner;
    private View viewTopBanner;
    private boolean isAMapExists, isTencentExists, isBaiduExists, isSoGouExists;
    private String address;
    private double myLat, myLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.mt_activity_native_home);
        mContext = this;
        // 获取上一个要获取信息的类型 id标示（type == 1 时为联盟商家 2时为兑换中心）
        merchant_id = getIntent().getStringExtra("merchant_id");
        initView();
        initData();
        initExists();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initListener() {
        mSvPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //上啦加载加载的是附近商家推荐的数据
                page++;
                getBottomData();
            }
        });
        //商家详情
        mLvNearMerchantRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", copyMerchantList.get(position).merchantId);
                startActivity(intent);
            }
        });

        //跳转套餐详情
        mListViewCombo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, MTComboDetailsActivity.class);
                Logger.i("package  " + packages.toString() + " posotion  " + position + " id " + packages.get(position).packageId);
                intent.putExtra("package_id", packages.get(position).packageId);
                intent.putExtra("merchant_id", merchant_id);
                startActivity(intent);
            }
        });
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public int Position;
            int lastPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Position = position;
                initChageDot(Position);
            }

            private void initChageDot(int position) {
                initAllDots();//改变点位之前先将所有的点置灰
                if (merchantInfo.images.size() != 0) {
                    position = position % merchantInfo.images.size();
                    if (position != 0) {
                        position = position - 1;
                    } else {
                        position = merchantInfo.images.size() - 1;
                    }
                    View childAt = mLlPoints.getChildAt(lastPosition);
                    if (childAt != null) {
                        childAt.setEnabled(false);
                    }
                    View childAt1 = mLlPoints.getChildAt(position);
                    if (childAt1 != null) {
                        childAt1.setEnabled(true);
                    }
                    lastPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // 页面切换   自动的切换到对应的界面    最后一个A----->第一个A
                    if (Position == VpAdapter.getCount() - 1) {
                        //最后一个元素  是否平滑切换
                        mViewpager.setCurrentItem(1, false);
                    } else if (Position == 0) {
                        //是第一个元素{D] ----> 倒数第二个元素[D]
                        mViewpager.setCurrentItem(VpAdapter.getCount() - 2, false);
                    }
                }
            }
        });
    }

    private void initAllDots() {
        for (int i = 0; i < dotImageViews.size(); i++) {
            dotImageViews.get(i).setEnabled(false);
        }
    }

    private void initViewData(MTMerchantDetailEntity.InfoBean merchantInfo, MTMerchantDetailEntity result) {
        Logger.i("banner走了这里0");
        List<MTMerchantDetailEntity.InfoBean.Banner> bannerList = merchantInfo.bannerList;
        if (bannerList != null && bannerList.size() > 0) {
            viewTopBanner.setVisibility(View.VISIBLE);
            banner.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
            layoutParams.height = (int) ((ScreenUtils.getScreenWidth(mContext) * 85.0f) / 375.0);
            Logger.i("layoutParams.height:" + layoutParams.height + "  ScreenUtils.getScreenWidth(mContext):" + ScreenUtils.getScreenWidth(mContext) + " bili:" + (375 / 85));
            Logger.i("banner走了这里1");
            ArrayList<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < bannerList.size(); i++) {
                Logger.i("bannerList.get(i)" + bannerList.get(i));
                String webImageUrlNOSuffix = WebImageUtil.getInstance().getWebImageUrlNOSuffix(bannerList.get(i).image);
                Logger.i("webImageUrlNOSuffix" + webImageUrlNOSuffix);
                imageUrls.add(webImageUrlNOSuffix);
            }
            Logger.i("banner走了这里2");
            banner.setImages(imageUrls)
                    .isAutoPlay(false)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
//                        TODO 通用跳转
                            MTMerchantDetailEntity.InfoBean.Banner bean = bannerList.get(position);
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(bean.type, bean.content);
                        }
                    }).start();
        }

        if (!TextUtils.isEmpty(merchantInfo.merDiscount)) {
            tvParticipation.setVisibility(View.VISIBLE);
            tvParticipation.setText(Html.fromHtml(merchantInfo.merDiscount + "<font><small><small>%</small></small></font>"));
        } else {
            tvParticipation.setVisibility(View.GONE);
        }
        isPraise = false;
        mV3Title.setText(merchantInfo.merchantName);
        tvImageCount.setText(merchantInfo.countMerchantAlbum + "张");
        Logger.i("当前的收藏状态" + result.yetCollect);
        if ("1".equals(result.yetCollect)) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }
        showFavoriteState();
        mV3Shop.setVisibility(View.VISIBLE);
        Drawable drawable;
        if (1 == merchantInfo.isPraise) {
            drawable = getResources().getDrawable(R.mipmap.ic_shops_praise_on);
        } else {
            drawable = getResources().getDrawable(R.mipmap.ic_shops_praise_off);
        }
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mBtnClickPraise.setCompoundDrawables(drawable, null, null, null);
        }
        mBtnClickPraise.setText(merchantInfo.praiseCount + "赞");

        mTvContentTitle.setText(merchantInfo.merchantName);
        Float graded = Float.parseFloat(merchantInfo.graded) / 10;
        // Logger.i("merchantInfo+graded" + merchantInfo.graded + "  处理过graded" + graded);
        if (graded == 0f) {
            mRatingBar.setRating(5.0f);
            mAllRating.setRating(5.0f);
        } else {
            mRatingBar.setRating(graded);
            mAllRating.setRating(graded);
        }
        mTvContentGrade.setText(graded + "分");
        mAllGraded.setText(graded + "分");
        mTvContentAddress.setText(merchantInfo.merchantAddress);
        mTvContentTime.setText("营业时间:" + merchantInfo.merchantWorktime);
        mTvPatronizeCount.setText(Html.fromHtml("已有<font color=\"#ff9000\"><big>" + this.merchantInfo.renqi + "</big></font>人光临本店"));
        //商家简介
        mTvShopDespContent.setText(merchantInfo.merchantDesp);

        latitude = this.merchantInfo.merchantLatitude;
        longitude = this.merchantInfo.merchantLongitude;

        finalLatitude = Double.parseDouble(merchantInfo.merchantLatitude);
        finalLongitude = Double.parseDouble(merchantInfo.merchantLongitude);
        //线上店铺显示与否
        Logger.i("info---" + merchantInfo.supplierIsExist);
        if (0 == merchantInfo.supplierIsExist) {
            layoutContentOnline.setVisibility(View.GONE);
        } else if (1 == merchantInfo.supplierIsExist) {
            layoutContentOnline.setVisibility(View.VISIBLE);
            tvContentShopName.setText("线上店铺：" + merchantInfo.supplierName);
            supplierId = merchantInfo.supplierId;
        }

        initListener();
    }

    private void showFavoriteState() {
        if (isFavorite) {
            mV3Shop.setImageDrawable(getResources().getDrawable(R.mipmap.star_solid));
        } else {
            mV3Shop.setImageDrawable(getResources().getDrawable(R.mipmap.merchant_star_empty));
        }
    }

    private void initView() {
        ivShowMore = (ImageView) findViewById(R.id.iv_show_more);
        llTotalComment = (LinearLayout) findViewById(R.id.ll_total_comment);
        llTotalComment.setOnClickListener(this);
        llMerchantIntroduce = (LinearLayout) findViewById(R.id.ll_merchant_introduce);
        mV3Back = (ImageView) findViewById(R.id.v3Back);
        mV3Left = (ImageView) findViewById(R.id.v3Left);
        mV3Left.setVisibility(View.INVISIBLE);
        mV3Title = (TextView) findViewById(R.id.v3Title);
        //设置显示不全会出现省略号
        mV3Title.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        mV3Shop = (ImageView) findViewById(R.id.v3Shop);
        mV3Share = (ImageView) findViewById(R.id.v3Share);
        mV3Share.setVisibility(View.VISIBLE);
        mV3Share.setImageDrawable(getResources().getDrawable(R.mipmap.iv_shear));
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mBtnStoreConsumer = (Button) findViewById(R.id.btn_store_consumer);
        mllmerchant_head = (LinearLayout) findViewById(R.id.ll_merchant_head);//商家详情的头布局
        mTvComboPrice = (TextView) findViewById(R.id.tv_combo_price);
        mFlBanner = (FrameLayout) findViewById(R.id.fl_banner);
        mTvContentTitle = (TextView) findViewById(R.id.tv_content_title);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mTvContentGrade = (TextView) findViewById(R.id.tv_content_grade);
        mAllRating = (RatingBar) findViewById(R.id.all_ratingBar);
        mAllGraded = (TextView) findViewById(R.id.all_graded_text);
        mTvContentAddress = (TextView) findViewById(R.id.tv_content_address);
        storeConsumerDown = (Button) findViewById(R.id.btn_store_consumer_down);
        mIvCall = (ImageView) findViewById(R.id.iv_call);
        mTvContentTime = (TextView) findViewById(R.id.tv_content_time);
        mTvPatronizeCount = (TextView) findViewById(R.id.tv_patronize_count);
        banner = (Banner) findViewById(R.id.banner);
        viewTopBanner = findViewById(R.id.view_top_banner);
        mView2 = (View) findViewById(R.id.view2);
        mSpecialCombo = (TextView) findViewById(R.id.tv_combo_title);
        mSpecialCombo.setText("特色套餐");
        layoutCombo = (LinearLayout) findViewById(R.id.layout_combo);
        mListViewCombo = (NoScrollListView) findViewById(R.id.listView_combo);
        mListViewCombo.setFocusable(false);
        LinearLayout mLlGoMore = (LinearLayout) findViewById(R.id.ll_go_more);
        mLlGoMore.setVisibility(View.GONE);
        mEvaluateList = (NoScrollListView) findViewById(R.id.nolv_evaluate);
        mEvaluateList.setFocusable(false);
        mTvMerchantDesp = (TextView) findViewById(R.id.tv_merchant_desp);
        mTvShopDespContent = (TextView) findViewById(R.id.tv_shop_desp_content);
        mTvNearMerchantRecommend = (TextView) findViewById(R.id.tv_near_merchant_recommend);
        mLvNearMerchantRecommend = (NoScrollListView) findViewById(R.id.lv_near_merchant_recommend);
        mLvNearMerchantRecommend.setFocusable(false);
        mBtnClickPraise = (Button) findViewById(R.id.btn_click_praise);
        mBtnMerchantGo = (Button) findViewById(R.id.btn_merchant_go);
        mLlayoutBottomMenu = (LinearLayout) findViewById(R.id.llayout_bottom_menu);
        mSvPullRefresh = (PullToRefreshScrollView) findViewById(R.id.sv_pull_refresh);
        mSvPullRefresh.setFocusable(true);
        //滑动到scrollview顶部，处理第一次加载这个页面时，页面不再顶部的问题
        mSvPullRefresh.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
        mSvPullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        mrlStoreConsume = (RelativeLayout) findViewById(R.id.rl_store_consume);
        tvParticipation = (TextView) findViewById(R.id.tv_participation);//发奖励的标签

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        loadErrorView = (LinearLayout) findViewById(R.id.ll_load_error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        flBottom = (FrameLayout) findViewById(R.id.fl_merchant_bottom);
        flBottom.setVisibility(View.VISIBLE);
        tvImageCount = (TextView) findViewById(R.id.tv_merchant_image_count);
        mLlPoints = (LinearLayout) findViewById(R.id.ll_points);
        mLlPoints.setVisibility(View.VISIBLE);
        //底部灰色背景
        llGrayBottom = (LinearLayout) findViewById(R.id.ll_flash_sale_content);
        llGrayBottom.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.GONE);
        TextView tvDes = (TextView) findViewById(R.id.tv_des);
        tvDes.setVisibility(View.GONE);
        TextView tvResidue = (TextView) findViewById(R.id.tv_residue);
        tvResidue.setVisibility(View.GONE);

        mBtnStoreConsumer.setOnClickListener(this);
        storeConsumerDown.setOnClickListener(this);
        mSvPullRefresh.setScrollListener(this);
        mIvCall.setOnClickListener(this);
        mV3Back.setOnClickListener(this);
        mV3Shop.setOnClickListener(this);
        mV3Share.setOnClickListener(this);
        mBtnClickPraise.setOnClickListener(this);
        mBtnMerchantGo.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);

        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }

        layoutContentOnline = (LinearLayout) findViewById(R.id.layout_content_online);
        layoutContentOnline.setOnClickListener(this);
        tvContentShopName = (TextView) findViewById(R.id.tv_content_shop_name);
    }

    private void initOtherData() {
        initViewPager();
        initViewPagerDot();
        initComboList();
        initEvaluateList();
    }

    @Override
    public void onScroll(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y >= (mFlBanner.getHeight() + llMerchantIntroduce.getHeight() - mrlStoreConsume.getHeight() - mTvPatronizeCount.getHeight())) {
            mllmerchant_head.setVisibility(View.GONE);
        } else {
            mllmerchant_head.setVisibility(View.GONE);
        }
    }

    //初始化数据
    private void initData() {
        Logger.i("传递过来的merchant_id：" + merchant_id);
        page = 0;
        //商家详情
        getMerchantDetailData();
        getBottomData();
    }

    private void getMerchantDetailData() {
        //通过id请求服务器获取商家信息
        startMyDialog(false);
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.getMerchantDetailList(merchant_id, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), new RequestCallBack<MTMerchantDetailEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MTMerchantDetailEntity> responseInfo) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                llContent.setVisibility(View.VISIBLE);
                                loadErrorView.setVisibility(View.GONE);
                                merchantInfo = responseInfo.result.info;
                                //刷新数据
                                initOtherData();
                                initViewData(merchantInfo, responseInfo.result);
                                //获取分享的URL
                                //initSroll();
                                mSvPullRefresh.onRefreshComplete();
                                break;
                            default:
                                break;
                        }
                    } else {
                        loadErrorView.setVisibility(View.VISIBLE);
                        llContent.setVisibility(View.GONE);
                    }
                }
                mSvPullRefresh.onRefreshComplete();
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Logger.i("请求商家详情数据错误" + e + "内容" + s);
                loadErrorView.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
                mSvPullRefresh.onRefreshComplete();
                stopMyDialog();
            }
        });
    }

    private void getBottomData() {
        //请求附近商家推荐
        startMyDialog(false);
        mtNetRequest.getMerchantRecommendList(merchant_id, page, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext)
                , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), new RequestCallBack<MTMerchantCommendEntity>() {
                    @Override
                    public void onSuccess(ResponseInfo<MTMerchantCommendEntity> responseInfo) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                            if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                                switch (responseInfo.result.code) {
                                    case 1:
                                        List<MTMerchantCommendEntity.MerchantListBean> merchantList = responseInfo.result.merchantList;
                                        ShopsSort.sort(mContext, merchantList);
                                        if (page == 0) {
                                            if (merchantList.size() > 0 && merchantList != null) {
                                                copyMerchantList.clear();
                                                copyMerchantList.addAll(merchantList);
                                            } else {
                                                mLvNearMerchantRecommend.setVisibility(View.GONE);
                                            }
                                        }
                                        if (page > 0) {
                                            if (merchantList.size() > 0) {
                                                copyMerchantList.addAll(merchantList);
                                            } else {
                                                showToast("没有更多数据");
                                            }
                                        }
                                        if (lvNearMerchantAdapter == null) {
                                            lvNearMerchantAdapter = new MerchantRecommendAdapter(copyMerchantList);
                                            mLvNearMerchantRecommend.setAdapter(lvNearMerchantAdapter);
                                        }
                                        lvNearMerchantAdapter.notifyDataSetChanged();
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                mLvNearMerchantRecommend.setVisibility(View.GONE);
                            }
                        }
                        mSvPullRefresh.onRefreshComplete();
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        mSvPullRefresh.onRefreshComplete();
                        stopMyDialog();
                        Logger.e("附近商家推荐请求" + e + "内容" + s);

                    }
                });
    }

    private void initViewPagerDot() {
        //添加小圆点之前先删除所有小圆点，避免小圆点数量异常
        mLlPoints.removeAllViews();
        dotImageViews.clear();
        for (int i = 0, count = merchantInfo.images.size(); i < count; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DPXUnitUtil.dp2px(mContext, 10);

            imageView.setImageDrawable(getResources().getDrawable(R.drawable.lldot_white_enable));
            dotImageViews.add(imageView);
            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
            }
            mLlPoints.addView(imageView, params);
        }
    }

    /**
     * 初始化Viewpager
     */
    private void initViewPager() {
        //创建数组用于图片的点击放大
        String[] images = new String[merchantInfo.images.size()];
        for (int i = 0; i < images.length; i++) {
            String photoPath = merchantInfo.images.get(i).photoPath;
            images[i] = photoPath;
        }

        if (images.length <= 0) {
            return;
        }
        if (VpAdapter == null) {
            VpAdapter = new MerchantVpAdapter(images, options, imageLoader, mContext, merchant_id);
        }
        mViewpager.setAdapter(VpAdapter);
        mViewpager.setCurrentItem(1);
    }

    //初始化特色套餐的数据
    private void initComboList() {
        packages = merchantInfo.packages;
        Logger.i("特色套餐数据" + packages.toString());
        if (null == packages || packages.size() <= 0) {
            layoutCombo.setVisibility(View.GONE);
            mSpecialCombo.setVisibility(View.GONE);
            mListViewCombo.setVisibility(View.GONE);
            return;
        }
        mListViewCombo.setAdapter(new SpecialComboAdapter(packages));
    }

    //初始化总体评价的数据
    private void initEvaluateList() {
        commentsBeanList = merchantInfo.comments;
        if (commentsBeanList.size() <= 0 || null == commentsBeanList) {
            ivShowMore.setVisibility(View.INVISIBLE);
            llTotalComment.setClickable(false);
            return;
        }
        mEvaluateList.setAdapter(new mEvaluateAdapter(commentsBeanList, merchantInfo.commentsCount, merchant_id));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //查看全部评论
            case R.id.ll_total_comment:
                Intent intent1 = new Intent(this, MTPackageCommentListActivity.class);
                intent1.putExtra("packageId", "0");
                intent1.putExtra("merchantId", merchant_id);
                startActivity(intent1);
                break;

            //店内消费
            case R.id.btn_store_consumer:
            case R.id.btn_store_consumer_down:
                if (!isLogin()) {
                    Intent intent = new Intent(this, LeFenPhoneLoginActivity.class);
                    startActivity(intent);
                } else {
//                    Intent intent=new Intent(this,MTStoreConsumeActivity.class);
//                    intent.putExtra("merchantId",merchant_id);
//                    startActivity(intent);
//                    店内支付2017/02/06调整为为Web页面
//                    http://test.lefenmall.com/wechat/m/pay/merLebiScanPay.php?merchant_id=4418&employee_id=0

                    /**
                     * 跳转web之前判断当前是否有支付密码，没有去设置，防止从web条转原生界面后无法获取最新数据
                     */
                    if (PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) {
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra(Constants.SPKEY_URL, Ip.getWechatURL(mContext) + "m/pay/merHappyBeanPay.php?merchant_id="
                                + merchant_id + "&employee_id=0&token=" + RequestOftenKey.getToken(this.mContext)
                                + "&device_index=" + RequestOftenKey.getDeviceIndex(this.mContext));
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setMessage("您还未设置支付密码，请设置支付密码后在支付！")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(mContext, InitialPayActivity.class));
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                }
                break;
            case R.id.btn_click_praise:
                chickPraise();
                break;
            case R.id.btn_merchant_go:
                goTo();
                break;
            case R.id.iv_call:
                //点击拨打电话
                callTel();
                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Share:
                share();
                break;
            case R.id.v3Shop:
                switchFavorite();
                break;
            case R.id.tv_refresh:
                refreshData();
                break;
            case R.id.layout_content_online:
                Intent intent = new Intent(MTMerchantDetailActivity.this, JPFlagshipActivity.class);
                intent.putExtra("index_id", supplierId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void refreshData() {
        initData();
    }

    private void switchFavorite() {
        if (!isLogin()) {
            Intent intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
            startActivity(intent);
            return;
        }
        if (isFavorite) {
            cancelFavroite();
        } else {
            addFavorite();
        }
    }

    private void addFavorite() {

        startMyDialog();
        jpNetRequest.collectV3(merchant_id, "1", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                mV3Shop.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        mV3Shop.setClickable(true);
                        showToast("添加收藏成功");
                        isFavorite = true;
                        showFavoriteState();
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        break;
                    default:
                        showToast("添加收藏异常码" + responseInfo.result.code);
                        mV3Shop.setClickable(true);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                mV3Shop.setClickable(true);
            }
        });
    }

    private void cancelFavroite() {
        startMyDialog();
        jpNetRequest.cancelCollectV3(merchant_id, "1", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                mV3Shop.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        mV3Shop.setClickable(true);
                        showToast("取消收藏成功");
                        isFavorite = false;
                        showFavoriteState();
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        break;
                    default:
                        showToast("异常码" + responseInfo.result.code);
                        mV3Shop.setClickable(true);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mV3Shop.setClickable(true);
                stopMyDialog();
            }
        });
    }

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

    //导航
    private void goTo() {
        if (isAMapExists || isBaiduExists || isTencentExists || isSoGouExists) {
            Intent intent = new Intent(MTMerchantDetailActivity.this, V5NavigationActivity.class);
            intent.putExtra("isAMapExists", isAMapExists);
            intent.putExtra("isBaiduExists", isBaiduExists);
            intent.putExtra("isTencentExists", isTencentExists);
            intent.putExtra("isSoGouExists", isSoGouExists);
            intent.putExtra("lat", latitude);
            intent.putExtra("lng", longitude);
            intent.putExtra("my_lat", merchantInfo.merchantLatitude);
            intent.putExtra("my_lng", merchantInfo.merchantLongitude);
            intent.putExtra("address", merchantInfo.merchantAddress);
            startActivity(intent);
        } else {
            Logger.i("2018年2月24日 16:03:05-" + PreferenceUtils.readStrConfig(Constants.SPKEY_LOCATION_CITY_NAME, mContext));
            Uri mapUri = Uri.parse(MapUtil.getWebBaiduMapUri(String.valueOf(latitude), String.valueOf(longitude), "我的位置",
                    String.valueOf(merchantInfo.merchantLatitude), String.valueOf(merchantInfo.merchantLongitude),
                    merchantInfo.merchantAddress, PreferenceUtils.readStrConfig(Constants.SPKEY_LOCATION_CITY_NAME, mContext), "益联益家"));
            Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
            startActivity(loction);
        }
    }

    //点赞
    private void chickPraise() {
        if (!isLogin()) {
            startActivity(new Intent(this, LeFenPhoneLoginActivity.class));
            return;
        }
        if (TextUtils.isEmpty(merchant_id)) {
            return;
        }
        startMyDialog();
        if (nearbyNetRequest == null) {
            nearbyNetRequest = new NearbyNetRequest(mContext);
        }

        nearbyNetRequest.clickPraise(merchant_id, new RequestCallBack<MerchantPraiseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MerchantPraiseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("操作成功");
                        //操作成功之后重新更新当前的界面
                        isPraise = true;
//                        getMerchantDetailData(merchant_id);
                        reFreshPraise(responseInfo.result.isPraise, responseInfo.result.praiseCount);
                        break;

                    case -24:
                    case -23:
                    case -4:
                        showToast("登录状态失效，请重新登录");
                        sp.edit().putBoolean("state", false).commit();
                        break;

                    default:
                        showToast("错误代码 ：" + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("网络连接失败");
                stopMyDialog();
            }
        });
    }

    /**
     * 点赞或取消点赞的刷新
     */
    public void reFreshPraise(int type, int count) {
        Drawable drawable;
        if (1 == type) {
            drawable = getResources().getDrawable(R.mipmap.ic_shops_praise_on);
        } else {
            drawable = getResources().getDrawable(R.mipmap.ic_shops_praise_off);
        }
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mBtnClickPraise.setCompoundDrawables(drawable, null, null, null);
        }
        mBtnClickPraise.setText(count + "赞");
    }

    public void getShareUrl() {
        startMyDialog();
        /**
         * 由于这些页面wx端不再更新 故分享出的页面变为登陆
         * 原参数getShareUrl(3, merchant_id)
         */
        jpNetRequest.getShareUrl("9", "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        shareTitle = responseInfo.result.title;
                        shareContent = responseInfo.result.content;
                        share_Img = responseInfo.result.imgUrl;
                        shareUrl = responseInfo.result.url;
                        if (TextUtils.isEmpty(shareImg)) {
                            shareImg = "";
                        } else {
                            if (share_Img.contains("http://") || share_Img.contains("https://")) {
                                shareImg = share_Img;
                            } else {
                                shareImg = Constants.ImageUrl + share_Img;
                            }
                        }
                        break;
                    case -3:
                        showToast("系统繁忙");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                Logger.e("分享错误" + e);
            }
        });

    }

    private void share() {
        getShareUrl();
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(mContext, R.anim.anim_wellcome_bottom),
                    R.style.DialogControl, new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {

                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            shareTitle,
                            shareContent,
                            shareImg,
                            shareUrl).share();
                }
            });
        }
        mShareDialog.show();
    }

    private void callTel() {
        if (merchantInfo == null) {
            return;
        }
        final String tel = merchantInfo.merchantTel;

        if (tel != null && tel.length() > 0) {
            showDialog(null, tel, null, 0, Gravity.CENTER, "拨打", "取消", true,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    }, this.mContext);

        } else {
            showToast("亲,这家太懒了,没留电话哦!");
        }

    }
}
