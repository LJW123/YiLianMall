package com.yilian.mall.shoppingcard.activity;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.shoppingcard.adapter.CardGalleryAdapter;
import com.yilian.mall.shoppingcard.adapter.CardGoodsListAdapter;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.shoppingcard.widgets.gallery.CardScaleHelper;
import com.yilian.mall.shoppingcard.widgets.gallery.SpeedRecyclerView;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsListEntity;
import com.yilian.networkingmodule.entity.shoppingcard.CardHomeHeaderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 购物卡 首页
 *
 * @author Created by Zg on 2018/11/14.
 */

public class CardHomePageActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private int page = Constants.PAGE_INDEX;//页数
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivJdList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CardGoodsListAdapter mAdapter;
    private View headerBannerView, headerRecommendView, headerHotSaleView, headerBigBrandView, headerGalleryView, headerListTitleView;
    //轮播  菜单icon 广告图
    private Banner banner;
    private LinearLayout cuPointer;
    private CardView cvIcon;
    private LinearLayout llIcon1, llIcon2, llIcon3, llIcon4;
    private ImageView ivIcon1, ivIcon2, ivIcon3, ivIcon4;
    private TextView tvIcon1, tvIcon2, tvIcon3, tvIcon4;
    private View viewLine1, viewLine2, viewLine3;
    private ImageView ivAdv;
    //好货推荐
    private LinearLayout llFloorOne;
    private ImageView ivFloor, ivFloorOne1, ivFloorOne2, ivFloorOne3, ivFloorOne4, ivFloorOne5;
    //热销榜
    private ImageView ivFloorTwo;
    private LinearLayout llFloorTwo;
    private LinearLayout llHot;
    private ImageView ivHot1, ivHot2, ivHot3;
    private TextView tvHot1, tvHot2, tvHot3;
    private RelativeLayout rlHot1, rlHot2, rlHot3;
    private LinearLayout llSundryList, llRow;
    private TextView tvTitle1, tvTitle2, tvTitle3, tvTitle4;
    private TextView tvIntro1, tvIntro2, tvIntro3, tvIntro4;
    private TextView tvJoin1, tvJoin2, tvJoin3, tvJoin4;
    private ImageView ivImg1, ivImg2, ivImg3, ivImg4;
    private LinearLayout llList1, llList2, llList3, llList4;
    //大牌专属
    private ImageView ivFloorThree;
    private LinearLayout llFloorThree;
    private ImageView ivBrand1, ivBrand2, ivBrand3, ivBrand4;
    private TextView tvBrand1, tvBrand2, tvBrand3, tvBrand4;
    private LinearLayout llBrand1, llBrand2, llBrand3, llBrand4;
    //首页画廊
    private SpeedRecyclerView vGallery;
    private CardScaleHelper mCardScaleHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_home_page);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新页面
                getHeaderData();
            }
        });
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivJdList = findViewById(R.id.iv_jd_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.red));
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mAdapter = new CardGoodsListAdapter();
        mAdapter.bindToRecyclerView(mRecyclerView);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(context, DPXUnitUtil.dp2px(context, 4), R.color.color_bg, true);
        mRecyclerView.addItemDecoration(decor);
        //轮播  菜单icon 广告图
        headerBannerView = View.inflate(context, R.layout.card_activity_home_page_header_banner, null);
        banner = headerBannerView.findViewById(R.id.banner);
        cuPointer = headerBannerView.findViewById(R.id.cu_pointer);
        ivIcon1 = headerBannerView.findViewById(R.id.iv_icon_1);
        tvIcon1 = headerBannerView.findViewById(R.id.tv_icon_1);
        llIcon1 = headerBannerView.findViewById(R.id.ll_icon_1);
        viewLine1 = headerBannerView.findViewById(R.id.view_line_1);
        ivIcon2 = headerBannerView.findViewById(R.id.iv_icon_2);
        tvIcon2 = headerBannerView.findViewById(R.id.tv_icon_2);
        llIcon2 = headerBannerView.findViewById(R.id.ll_icon_2);
        viewLine2 = headerBannerView.findViewById(R.id.view_line_2);
        ivIcon3 = headerBannerView.findViewById(R.id.iv_icon_3);
        tvIcon3 = headerBannerView.findViewById(R.id.tv_icon_3);
        llIcon3 = headerBannerView.findViewById(R.id.ll_icon_3);
        viewLine3 = headerBannerView.findViewById(R.id.view_line_3);
        ivIcon4 = headerBannerView.findViewById(R.id.iv_icon_4);
        tvIcon4 = headerBannerView.findViewById(R.id.tv_icon_4);
        llIcon4 = headerBannerView.findViewById(R.id.ll_icon_4);
        cvIcon = headerBannerView.findViewById(R.id.cv_icon);
        ivAdv = headerBannerView.findViewById(R.id.iv_adv);
        //好货推荐
        headerRecommendView = View.inflate(context, R.layout.card_activity_home_page_header_recommend, null);
        llFloorOne = headerRecommendView.findViewById(R.id.ll_floor_one);
        ivFloor = headerRecommendView.findViewById(R.id.iv_floor);
        ivFloorOne1 = headerRecommendView.findViewById(R.id.iv_floor_one_1);
        ivFloorOne2 = headerRecommendView.findViewById(R.id.iv_floor_one_2);
        ivFloorOne3 = headerRecommendView.findViewById(R.id.iv_floor_one_3);
        ivFloorOne4 = headerRecommendView.findViewById(R.id.iv_floor_one_4);
        ivFloorOne5 = headerRecommendView.findViewById(R.id.iv_floor_one_5);
        //热销榜
        headerHotSaleView = View.inflate(context, R.layout.card_activity_home_page_header_hot_sale, null);
        ivFloorTwo = headerHotSaleView.findViewById(R.id.iv_floor_two);
        llFloorTwo = headerHotSaleView.findViewById(R.id.ll_floor_two);
        llHot = headerHotSaleView.findViewById(R.id.ll_hot);
        ivHot1 = headerHotSaleView.findViewById(R.id.iv_hot_1);
        tvHot1 = headerHotSaleView.findViewById(R.id.tv_hot_1);
        rlHot1 = headerHotSaleView.findViewById(R.id.rl_hot_1);
        ivHot2 = headerHotSaleView.findViewById(R.id.iv_hot_2);
        tvHot2 = headerHotSaleView.findViewById(R.id.tv_hot_2);
        rlHot2 = headerHotSaleView.findViewById(R.id.rl_hot_2);
        ivHot3 = headerHotSaleView.findViewById(R.id.iv_hot_3);
        tvHot3 = headerHotSaleView.findViewById(R.id.tv_hot_3);
        rlHot3 = headerHotSaleView.findViewById(R.id.rl_hot_3);
        tvTitle1 = headerHotSaleView.findViewById(R.id.tv_title_1);
        tvIntro1 = headerHotSaleView.findViewById(R.id.tv_intro_1);
        tvJoin1 = headerHotSaleView.findViewById(R.id.tv_join_1);
        ivImg1 = headerHotSaleView.findViewById(R.id.iv_img_1);
        llList1 = headerHotSaleView.findViewById(R.id.ll_list_1);
        tvTitle2 = headerHotSaleView.findViewById(R.id.tv_title_2);
        tvIntro2 = headerHotSaleView.findViewById(R.id.tv_intro_2);
        tvJoin2 = headerHotSaleView.findViewById(R.id.tv_join_2);
        ivImg2 = headerHotSaleView.findViewById(R.id.iv_img_2);
        llList2 = headerHotSaleView.findViewById(R.id.ll_list_2);
        tvTitle3 = headerHotSaleView.findViewById(R.id.tv_title_3);
        tvIntro3 = headerHotSaleView.findViewById(R.id.tv_intro_3);
        tvJoin3 = headerHotSaleView.findViewById(R.id.tv_join_3);
        ivImg3 = headerHotSaleView.findViewById(R.id.iv_img_3);
        llList3 = headerHotSaleView.findViewById(R.id.ll_list_3);
        tvTitle4 = headerHotSaleView.findViewById(R.id.tv_title_4);
        tvIntro4 = headerHotSaleView.findViewById(R.id.tv_intro_4);
        tvJoin4 = headerHotSaleView.findViewById(R.id.tv_join_4);
        ivImg4 = headerHotSaleView.findViewById(R.id.iv_img_4);
        llList4 = headerHotSaleView.findViewById(R.id.ll_list_4);
        llSundryList = headerHotSaleView.findViewById(R.id.ll_sundry_list);
        llRow = headerHotSaleView.findViewById(R.id.ll_row);
        //大牌专属
        headerBigBrandView = View.inflate(context, R.layout.card_activity_home_page_header_big_brand, null);
        ivFloorThree = headerBigBrandView.findViewById(R.id.iv_floor_three);
        llFloorThree = headerBigBrandView.findViewById(R.id.ll_floor_three);
        ivBrand1 = headerBigBrandView.findViewById(R.id.iv_brand_1);
        tvBrand1 = headerBigBrandView.findViewById(R.id.tv_brand_1);
        llBrand1 = headerBigBrandView.findViewById(R.id.ll_brand_1);
        ivBrand2 = headerBigBrandView.findViewById(R.id.iv_brand_2);
        tvBrand2 = headerBigBrandView.findViewById(R.id.tv_brand_2);
        llBrand2 = headerBigBrandView.findViewById(R.id.ll_brand_2);
        ivBrand3 = headerBigBrandView.findViewById(R.id.iv_brand_3);
        tvBrand3 = headerBigBrandView.findViewById(R.id.tv_brand_3);
        llBrand3 = headerBigBrandView.findViewById(R.id.ll_brand_3);
        ivBrand4 = headerBigBrandView.findViewById(R.id.iv_brand_4);
        tvBrand4 = headerBigBrandView.findViewById(R.id.tv_brand_4);
        llBrand4 = headerBigBrandView.findViewById(R.id.ll_brand_4);
        //首页画廊
        headerGalleryView = View.inflate(context, R.layout.card_activity_home_page_header_gallery, null);
        vGallery = headerGalleryView.findViewById(R.id.v_gallery);
        vGallery.setFocusable(false);
        vGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //精品推荐列表 头部
        headerListTitleView = View.inflate(context, R.layout.card_activity_home_page_header_list_title, null);

    }

    private void initData() {
        tvTitle.setText("购物卡专区");
        getHeaderData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivJdList.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHeaderData();
                mAdapter.setEnableLoadMore(false);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCardHomePageListData();
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDGoodsAbstractInfoEntity entity = (JDGoodsAbstractInfoEntity) mAdapter.getItem(position);
                JumpCardActivityUtils.toGoodsDetail(context, entity.skuId);

            }
        });
    }


    /**
     * 获取列表头部数据
     */
    private void getHeaderData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCardHomePageHeader("shoppingCardIndex")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardHomeHeaderEntity>() {
                    @Override
                    public void onCompleted() {
                        varyViewUtils.showDataView();
                        swipeRefreshLayout.setRefreshing(false);
                        getFirstData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CardHomeHeaderEntity headerData) {
                        setHeaderData(headerData);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取首页为您推荐数据
     */
    private void getFirstData() {
        page = 0;
        getCardHomePageListData();
    }


    /**
     * 首页为您推荐列表
     */
    private void getCardHomePageListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getCardHomePageListData("shoppingcard/shoppingGoodsRecomand", page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JDGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        mAdapter.loadMoreFail();
                        if (page > Constants.PAGE_INDEX) {
                            page--;
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDGoodsListEntity data) {
                        if (page == Constants.PAGE_INDEX) {
                            if (data.data.size() > 0) {
                                mAdapter.addHeaderView(headerListTitleView);
                                mAdapter.setNewData(data.getData());
                            }
                        } else {
                            mAdapter.addData(data.getData());
                        }
                        if (data.data.size() < Constants.PAGE_COUNT) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);

    }

    private void setHeaderData(CardHomeHeaderEntity headerData) {
        if (mAdapter.getHeaderLayoutCount() > 0) {
            mAdapter.removeAllHeaderView();
        }
        initBanner(headerData.bannerList, headerData.iconList, headerData.indexadvlist);
        initFloorOne(headerData.cardOne);
        initFloorTwo(headerData.hotsalelist, headerData.cardTwo);
        initFloorThree(headerData.cardThree);
        initHomeGallery(headerData.middlebannerlist);
    }

    /**
     * 轮播
     *
     * @param bannerList
     */
    private void initBanner(List<CardHomeHeaderEntity.CommJumpBean> bannerList, List<CardHomeHeaderEntity.Icon> iconList, CardHomeHeaderEntity.CommJumpBean adv) {
        //指针控件容器
        List<View> mPointView = new ArrayList<>();
        //滑动方向
        boolean isPageOrientation = true;
        if (null == bannerList || bannerList.size() <= 0) {
            banner.setVisibility(View.GONE);
            cuPointer.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
            cuPointer.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams bannerParams = (RelativeLayout.LayoutParams) banner.getLayoutParams();
            bannerParams.width = DPXUnitUtil.getScreenWidth(context);
            bannerParams.height = DPXUnitUtil.getScreenWidth(context) * 525 / 750;
            banner.setLayoutParams(bannerParams);


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cuPointer.getLayoutParams();
            params.setMargins(0, bannerParams.height - DPXUnitUtil.dp2px(context, 52.5f), 0, 0);
            cuPointer.setLayoutParams(params);

            List<String> imageList = new ArrayList<>();
            for (CardHomeHeaderEntity.CommJumpBean entity : bannerList) {
                String url = entity.img;
                imageList.add(url);

                View view = new View(context);
                int viewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(viewSize, viewSize);
                mLayoutParams.leftMargin = mLayoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
                view.setLayoutParams(mLayoutParams);
                view.setBackgroundResource(R.drawable.card_home_banner_pointer);
                view.setSelected(false);
                cuPointer.addView(view);
                mPointView.add(view);

            }
            banner.setImages(imageList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            CardHomeHeaderEntity.CommJumpBean entity = bannerList.get(position);
                            jumpCommon(entity.type, entity.content);
                        }
                    }).start();

            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    //将所有指针置为默认状态
//                    for (int i = 0; i < bannerList.size(); i++) {
//                        mPointView.get(i).setSelected(false);
//                    }
//                    // 将当前界面对面的指针置为选中状态
//                    mPointView.get(position-1).setSelected(true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        //首页icon
        if (iconList == null || iconList.size() < 1) {
            cvIcon.setVisibility(View.GONE);
        } else {
            cvIcon.setVisibility(View.VISIBLE);
            llIcon1.setVisibility(View.GONE);
            llIcon2.setVisibility(View.GONE);
            llIcon3.setVisibility(View.GONE);
            llIcon4.setVisibility(View.GONE);
            viewLine1.setVisibility(View.GONE);
            viewLine2.setVisibility(View.GONE);
            viewLine3.setVisibility(View.GONE);

            if (iconList.size() >= 4) {
                llIcon4.setVisibility(View.VISIBLE);
                viewLine3.setVisibility(View.VISIBLE);
                GlideUtil.showImage(context, iconList.get(3).img, ivIcon4);
                tvIcon4.setText(iconList.get(3).title);
                RxUtil.clicks(llIcon4, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        jumpCommon(iconList.get(3).type, iconList.get(3).content);
                    }
                });
            }
            if (iconList.size() >= 3) {
                llIcon3.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.VISIBLE);
                GlideUtil.showImage(context, iconList.get(2).img, ivIcon3);
                tvIcon3.setText(iconList.get(2).title);
                RxUtil.clicks(llIcon3, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        jumpCommon(iconList.get(2).type, iconList.get(2).content);
                    }
                });
            }
            if (iconList.size() >= 2) {
                llIcon2.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.VISIBLE);
                GlideUtil.showImage(context, iconList.get(1).img, ivIcon2);
                tvIcon2.setText(iconList.get(1).title);
                RxUtil.clicks(llIcon2, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        jumpCommon(iconList.get(1).type, iconList.get(1).content);
                    }
                });
            }
            if (iconList.size() >= 1) {
                llIcon1.setVisibility(View.VISIBLE);
                GlideUtil.showImage(context, iconList.get(0).img, ivIcon1);
                tvIcon1.setText(iconList.get(0).title);
                RxUtil.clicks(llIcon1, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        jumpCommon(iconList.get(0).type, iconList.get(0).content);
                    }
                });
            }
        }

        if (adv == null) {
            ivAdv.setVisibility(View.GONE);
        } else {
            ivAdv.setVisibility(View.VISIBLE);
            GlideUtil.showImageNoSuffixNoPlaceholder(context, adv.img, ivAdv);
            RxUtil.clicks(ivAdv, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(adv.type, adv.content);
                }
            });
        }

        mAdapter.addHeaderView(headerBannerView);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 楼层1 - 好货推荐
     *
     * @param cardOne
     */
    private void initFloorOne(CardHomeHeaderEntity.CardOneBean cardOne) {
        if (cardOne != null) {
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardOne.cardOneTitle.img, ivFloor);
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardOne.cardOneLeftUp.img, ivFloorOne1);
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardOne.cardOneRightUp.img, ivFloorOne2);
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardOne.cardOneLeftDown.img, ivFloorOne3);
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardOne.cardOneMiddleDown.img, ivFloorOne4);
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardOne.cardOneTitle.img, ivFloorOne5);
            RxUtil.clicks(llFloorOne, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardOne.cardOneTitle.type, cardOne.cardOneTitle.content);
                }
            });
            RxUtil.clicks(ivFloorOne1, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardOne.cardOneLeftUp.type, cardOne.cardOneLeftUp.content);
                }
            });
            RxUtil.clicks(ivFloorOne2, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardOne.cardOneRightUp.type, cardOne.cardOneRightUp.content);
                }
            });
            RxUtil.clicks(ivFloorOne3, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardOne.cardOneLeftDown.type, cardOne.cardOneLeftDown.content);
                }
            });
            RxUtil.clicks(ivFloorOne4, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardOne.cardOneMiddleDown.type, cardOne.cardOneMiddleDown.content);
                }
            });
            RxUtil.clicks(ivFloorOne5, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardOne.cardOneRightDown.type, cardOne.cardOneRightDown.content);
                }
            });
            mAdapter.addHeaderView(headerRecommendView);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 楼层2 - 24H热销榜
     *
     * @param cardTwo
     */
    private void initFloorTwo(List<CardHomeHeaderEntity.HotsalelistBean> hotsalelist, CardHomeHeaderEntity.CardTwoBean cardTwo) {
        GlideUtil.showImageNoSuffixNoPlaceholder(context, cardTwo.cardTwoTitle.img, ivFloorTwo);
        RxUtil.clicks(llFloorTwo, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpCommon(cardTwo.cardTwoTitle.type, cardTwo.cardTwoTitle.content);
            }
        });

        //热销 排名展示
        if (hotsalelist == null || hotsalelist.size() < 1) {
            llHot.setVisibility(View.GONE);
        } else {
            llHot.setVisibility(View.VISIBLE);


            GlideUtil.showImageNoSuffixNoPlaceholder(context, JDImageUtil.getJDImageUrl_N1(hotsalelist.get(0).imagePath), ivHot1);
            tvHot1.setText(hotsalelist.get(0).name);
            RxUtil.clicks(rlHot1, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    //TODO 跳转详情
                }
            });
            rlHot2.setVisibility(View.INVISIBLE);
            rlHot3.setVisibility(View.INVISIBLE);

            if (hotsalelist.size() > 1) {
                rlHot2.setVisibility(View.VISIBLE);
                GlideUtil.showImageNoSuffixNoPlaceholder(context, JDImageUtil.getJDImageUrl_N1(hotsalelist.get(1).imagePath), ivHot2);
                tvHot2.setText(hotsalelist.get(1).name);
                RxUtil.clicks(rlHot2, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //TODO 跳转详情
                    }
                });
            } else {
                return;
            }

            if (hotsalelist.size() > 2) {
                rlHot3.setVisibility(View.VISIBLE);
                GlideUtil.showImageNoSuffixNoPlaceholder(context, JDImageUtil.getJDImageUrl_N1(hotsalelist.get(2).imagePath), ivHot3);
                tvHot3.setText(hotsalelist.get(2).name);
                RxUtil.clicks(rlHot3, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //TODO 跳转详情
                    }
                });
            }
        }
        //各种榜单展示
        if (cardTwo == null) {
            llSundryList.setVisibility(View.GONE);
        } else {
            llSundryList.setVisibility(View.VISIBLE);
            tvTitle1.setText(cardTwo.cardTwoLeftUp.title);
            tvIntro1.setText(cardTwo.cardTwoLeftUp.fuTitle);
            tvJoin1.setText(String.format("%s人参与", cardTwo.cardTwoLeftUp.joinNum));
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardTwo.cardTwoLeftUp.img, ivImg1);
            RxUtil.clicks(llList1, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardTwo.cardTwoLeftUp.type, cardTwo.cardTwoLeftUp.content);
                }
            });
            tvTitle2.setText(cardTwo.cardTwoRightUp.title);
            tvIntro2.setText(cardTwo.cardTwoRightUp.fuTitle);
            tvJoin2.setText(String.format("%s人参与", cardTwo.cardTwoRightUp.joinNum));
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardTwo.cardTwoRightUp.img, ivImg2);
            RxUtil.clicks(llList2, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardTwo.cardTwoRightUp.type, cardTwo.cardTwoRightUp.content);
                }
            });

            tvTitle3.setText(cardTwo.cardTwoLeftDown.title);
            tvIntro3.setText(cardTwo.cardTwoLeftDown.fuTitle);
            tvJoin3.setText(String.format("%s人参与", cardTwo.cardTwoLeftDown.joinNum));
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardTwo.cardTwoLeftDown.img, ivImg3);
            RxUtil.clicks(llList3, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardTwo.cardTwoLeftDown.type, cardTwo.cardTwoLeftDown.content);
                }
            });

            tvTitle4.setText(cardTwo.cardTwoRightDown.title);
            tvIntro4.setText(cardTwo.cardTwoRightDown.fuTitle);
            tvJoin4.setText(String.format("%s人参与", cardTwo.cardTwoRightDown.joinNum));
            GlideUtil.showImageNoSuffixNoPlaceholder(context, cardTwo.cardTwoRightDown.img, ivImg4);
            RxUtil.clicks(llList4, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    jumpCommon(cardTwo.cardTwoRightDown.type, cardTwo.cardTwoRightDown.content);
                }
            });
        }
        mAdapter.addHeaderView(headerHotSaleView);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 楼层3 - 大牌专属
     *
     * @param cardThree
     */
    private void initFloorThree(CardHomeHeaderEntity.CardThreeBean cardThree) {
        if (cardThree == null || cardThree.cardThreeMiddleFour == null || cardThree.cardThreeMiddleFour.size() < 1) {
            return;
        }
        GlideUtil.showImageNoSuffixNoPlaceholder(context, cardThree.cardThreeTitle.img, ivFloorThree);
        RxUtil.clicks(llFloorThree, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpCommon(cardThree.cardThreeTitle.type, cardThree.cardThreeTitle.content);
            }
        });

        GlideUtil.showImageNoSuffixNoPlaceholder(context, cardThree.cardThreeMiddleFour.get(0).img, ivBrand1);
        tvBrand1.setText(cardThree.cardThreeMiddleFour.get(0).title);
        RxUtil.clicks(llBrand1, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpCommon(cardThree.cardThreeMiddleFour.get(0).type, cardThree.cardThreeMiddleFour.get(0).content);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(context, cardThree.cardThreeMiddleFour.get(1).img, ivBrand2);
        tvBrand2.setText(cardThree.cardThreeMiddleFour.get(1).title);
        RxUtil.clicks(llBrand2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpCommon(cardThree.cardThreeMiddleFour.get(1).type, cardThree.cardThreeMiddleFour.get(1).content);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(context, cardThree.cardThreeMiddleFour.get(2).img, ivBrand3);
        tvBrand3.setText(cardThree.cardThreeMiddleFour.get(2).title);
        RxUtil.clicks(llBrand3, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpCommon(cardThree.cardThreeMiddleFour.get(2).type, cardThree.cardThreeMiddleFour.get(2).content);
            }
        });
        GlideUtil.showImageNoSuffixNoPlaceholder(context, cardThree.cardThreeMiddleFour.get(3).img, ivBrand4);
        tvBrand4.setText(cardThree.cardThreeMiddleFour.get(3).title);
        RxUtil.clicks(llBrand4, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpCommon(cardThree.cardThreeMiddleFour.get(3).type, cardThree.cardThreeMiddleFour.get(3).content);
            }
        });

        mAdapter.addHeaderView(headerBigBrandView);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 首页画廊展示
     *
     * @param galleryList
     */
    private void initHomeGallery(List<CardHomeHeaderEntity.CommJumpBean> galleryList) {
        if (galleryList == null || galleryList.size() < 1) {
            return;
        }
        vGallery.setAdapter(new CardGalleryAdapter(galleryList, context));
        // mRecyclerView绑定scale效果
        if (mCardScaleHelper == null) {
            mCardScaleHelper = new CardScaleHelper();
            mCardScaleHelper.attachToRecyclerView(vGallery);
        }
        mCardScaleHelper.setCurrentItemPos(0);

        mAdapter.addHeaderView(headerGalleryView);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通用跳转
     *
     * @param type
     * @param content
     */
    private void jumpCommon(int type, String content) {
        JumpToOtherPage.getInstance(context).jumpToOtherPage(type, content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_jd_list:
                JumpCardActivityUtils.toCardJdShoppingList(context, null);
                break;
            default:
                break;
        }
    }


}
