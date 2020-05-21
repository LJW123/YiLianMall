package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyGoodsListAdapter;
import com.yilian.luckypurchase.widget.NoScrollGridView;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.OnTabSelectListener;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.adapter.MTGridViewAdapter;
import com.yilian.mylibrary.adapter.MTViewPagerAdapter;
import com.yilian.mylibrary.entity.GridViewIconBean;
import com.yilian.mylibrary.widget.CustomViewPager;
import com.yilian.mylibrary.widget.NoticeView2;
import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilian.networkingmodule.entity.LuckyGoodsListEntity;
import com.yilian.networkingmodule.entity.LuckyMainHeaderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author  幸运购首页
 */
public class LuckyMainActivity extends BaseAppCompatActivity implements View.OnClickListener, OnTabSelectListener {


    private ImageView ivBack;
    private ImageView ivPoint;
    private ImageView ivSearch;
    private RelativeLayout v3Layout;
    private RecyclerView recycleView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Banner banner;
    private GridView gridView;
    private NoticeView2 noticeView;
    private NoticeView2 noticeView2;
    private SlidingTabLayout tabLayout;
    private CustomViewPager custonViewPager;
    private LinearLayout llDots;
    private View includeIcons;
    private ViewPager viewPager;
    private LuckyGoodsListAdapter luckyAdapter;
    private View headerView;
    private String sort = "0,0,1,0";
    private ImageView ivReturnTop;
    private View includeHeader;
    private RelativeLayout viewFloatFather;
    private View viewFloat;
    private SlidingTabLayout tabLayoutFloat;
    private PagerAdapter viewPagerAdapter;

    private LinearLayout noticeLayout1, noticeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_main);
        initView();
        initListener();
    }

    /**
     * recyclerView滑动高度
     */
    int scrollDy = 0;

    private void initListener() {
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                int screenHeight3 = ScreenUtils.getScreenHeight(mContext) * 3;
                if (scrollDy > screenHeight3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
                int[] screenLocation = new int[2];
                tabLayout.getLocationOnScreen(screenLocation);
                int[] windowLocation = new int[2];
                tabLayout.getLocationInWindow(windowLocation);
                Logger.i("屏幕位置：----------------------------------------------------------------------------------------------------------------");
                Logger.i("屏幕位置：screenLocation[0]" + screenLocation[0] + "    screenLocation[1]" + screenLocation[1]);
                Logger.i("屏幕位置：windowLocation[0]" + windowLocation[0] + "    windowLocation[1]" + windowLocation[1]);
                int headerHeight = StatusBarUtils.getStatusBarHeight(mContext) + includeHeader.getLayoutParams().height;
                Logger.i("屏幕位置：StatusBar+includeHeader高度：" + headerHeight);
                Logger.i("屏幕位置：----------------------------------------------------------------------------------------------------------------");
                if (screenLocation[1] <= headerHeight) {
                    if (viewFloatFather.getChildCount() <= 0) {
                    }
                    viewFloatFather.removeAllViews();
                    viewFloatFather.addView(viewFloat);
                } else {
                    viewFloatFather.removeAllViews();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHeaderData();
                getFirstPageData();
            }
        });
        luckyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recycleView);
        luckyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckyGoodsListEntity.ListBean adapterItem = (LuckyGoodsListEntity.ListBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                intent.putExtra("activity_id", adapterItem.activityId);
                intent.putExtra("type", "0");
                intent.putExtra("prizeListPosition", position);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        viewFloatFather = (RelativeLayout) findViewById(R.id.view_float_father);
        includeHeader = findViewById(R.id.include_header);
        ivReturnTop = (ImageView) findViewById(R.id.iv_return_top);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPoint = (ImageView) findViewById(R.id.iv_point);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        v3Layout = (RelativeLayout) findViewById(R.id.v3Layout);
        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        recycleView.setLayoutManager(new GridLayoutManager(mContext, 2));
        luckyAdapter = new LuckyGoodsListAdapter(R.layout.lucky_item_prize_progress, true);
        recycleView.setAdapter(luckyAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));

        ivBack.setOnClickListener(this);
        ivPoint.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivReturnTop.setOnClickListener(this);
        initHeaderView();
        initData();
    }


    private final String[] mTitles = {
            "夺宝进度", "最佳人气", "最新发布", "总需人次"
    };

    private void initHeaderView() {
        if (headerView != null) {
            return;
        }
        headerView = View.inflate(mContext, R.layout.lucky_activity_main_header, null);
        banner = (Banner) headerView.findViewById(R.id.banner);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2.5);
        banner.setLayoutParams(layoutParams);
        banner.setOnClickListener(this);
        includeIcons = headerView.findViewById(R.id.include_icons);
        custonViewPager = (CustomViewPager) includeIcons.findViewById(R.id.viewpager);
        llDots = (LinearLayout) includeIcons.findViewById(R.id.ll_dot);
        noticeView = (NoticeView2) headerView.findViewById(R.id.notice_view);
        noticeView2 = (NoticeView2) headerView.findViewById(R.id.notice_view2);
        noticeView.setOnClickListener(this);
        noticeView2.setOnClickListener(this);
        viewPager = (ViewPager) headerView.findViewById(R.id.viewPager);
        viewPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View child = new View(mContext);
                container.addView(child);
                return child;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (SlidingTabLayout) headerView.findViewById(R.id.tab_layout);
        tabLayout.setOnClickListener(this);
        tabLayout.setOnTabSelectListener(this);
        tabLayout.setViewPager(viewPager);
        initFloatView();

        noticeLayout1 = (LinearLayout) headerView.findViewById(R.id.ll_notice_news);
        noticeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LuckyMainActivity.this, LuckyNewsListActivity.class);
                intent.putExtra("from", "prize");
                startActivity(intent);
            }
        });
        noticeLayout2 = (LinearLayout) headerView.findViewById(R.id.ll_notice_news2);
        noticeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LuckyMainActivity.this, LuckyNewsListActivity.class);
                intent.putExtra("from", "send");
                startActivity(intent);
            }
        });
    }

    private void initFloatView() {
        viewFloat = View.inflate(mContext, R.layout.lucky_merge_sliding_tab_layout, null);
        tabLayoutFloat = (SlidingTabLayout) viewFloat.findViewById(R.id.tab_layout_float);
        ViewPager viewPagerFloat = (ViewPager) viewFloat.findViewById(R.id.viewPager_float);
        viewPagerFloat.setAdapter(viewPagerAdapter);
        tabLayoutFloat.setOnTabSelectListener(this);
        tabLayoutFloat.setViewPager(viewPagerFloat);
    }

    /**
     * 获取幸运购首页头部数据
     */
    @SuppressWarnings("unchecked")
    private void getLuckyMainHeaderData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getLuckyMainHeaderData("snatch/snatch_index_v1", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyMainHeaderEntity>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("swipe走了这里3");
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("swipe走了这里4");
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(LuckyMainHeaderEntity luckyMainHeaderEntity) {
                        LuckyMainHeaderEntity.DataBean data = luckyMainHeaderEntity.data;
                        List<LuckyMainHeaderEntity.DataBean.BannerListBean> bannerList = data.bannerList;
                        initBanner(bannerList);
                        List<LuckyMainHeaderEntity.DataBean.IconListBean> iconList = data.iconList;
                        initIcons(iconList);
                        final ArrayList<LuckyMainHeaderEntity.DataBean.NewsListBean> newsList = data.newsList;
                        initNoticeView(newsList);
                        ArrayList<LuckyMainHeaderEntity.DataBean.AddressListBean> addressList = data.addressList;
                        initNoticeView2(addressList);
                        if (luckyAdapter.getHeaderLayoutCount() <= 0) {
                            luckyAdapter.addHeaderView(headerView);
                        }
                        recycleView.smoothScrollToPosition(0);
                    }
                });
        addSubscription(subscription);
    }

    private void initNoticeView(final ArrayList<LuckyMainHeaderEntity.DataBean.NewsListBean> newsList) {
        noticeView.getLuckyPrizeNotices(newsList, new NoticeView2.SetNew() {
            @Override
            public void setNews(View view, int position) {
                TextView tvNotice1 = (TextView) view.findViewById(R.id.tv_notice1);
                TextView tvNotice2 = (TextView) view.findViewById(R.id.tv_notice2);
                TextView tvNotice3 = (TextView) view.findViewById(R.id.tv_notice3);
                TextView tvMoreNotice1 = (TextView) view.findViewById(R.id.tv_more_notice1);
                TextView tvMoreNotice2 = (TextView) view.findViewById(R.id.tv_more_notice2);
                TextView tvMoreNotice3 = (TextView) view.findViewById(R.id.tv_more_notice3);
                for (int i = 0; i < 3; i++) {
                    int index = position * 3 + i;
                    if (index >= newsList.size()) {
                        return;
                    }
                    LuckyMainHeaderEntity.DataBean.NewsListBean newsListBean = newsList.get(index);
                    Spanned spanned = Html.fromHtml("恭喜" + newsListBean.ipAddress + "<font color='#fe5062'>" + newsListBean.userName + "</font>获得" + newsListBean.goodsName);
                    if (i == 0) {
                        tvNotice1.setText(spanned);
//                        此处设置详情是防止中奖条目不是3的倍数时，最后一条没有信息显示，但是会显示“详情”字样的问题
                        tvMoreNotice1.setText("  详情");
                    } else if (i == 1) {
                        tvNotice2.setText(spanned);
                        tvMoreNotice2.setText("  详情");
                    } else if (i == 2) {
                        tvNotice3.setText(spanned);
                        tvMoreNotice3.setText("  详情");
                    }
                }
//
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LuckyMainActivity.this, LuckyNewsListActivity.class);
                        intent.putExtra("from", "prize");
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void initNoticeView2(final ArrayList<LuckyMainHeaderEntity.DataBean.AddressListBean> newsList) {
        noticeView2.getLuckySendNotices(newsList, new NoticeView2.SetNew() {
            @Override
            public void setNews(View view, int position) {
                TextView tvNotice1 = (TextView) view.findViewById(R.id.tv_notice1);
                TextView tvNotice2 = (TextView) view.findViewById(R.id.tv_notice2);
                TextView tvNotice3 = (TextView) view.findViewById(R.id.tv_notice3);
                TextView tvMoreNotice1 = (TextView) view.findViewById(R.id.tv_more_notice1);
                TextView tvMoreNotice2 = (TextView) view.findViewById(R.id.tv_more_notice2);
                TextView tvMoreNotice3 = (TextView) view.findViewById(R.id.tv_more_notice3);
                for (int i = 0; i < 3; i++) {
                    int index = position * 3 + i;
                    Logger.i("index:" + index);
                    if (index >= newsList.size()) {
                        return;
                    }
                    final LuckyMainHeaderEntity.DataBean.AddressListBean addressListBean = newsList.get(index);
                    Spanned spanned = Html.fromHtml(addressListBean.awardCity + "&nbsp;<font color='#fe5062'>" + addressListBean.userName + "</font>的" + addressListBean.goodsName);
                    if (i == 0) {
                        tvNotice1.setText(spanned);
//                        此处设置已发货是防止发货条目不是3的倍数时，最后一条没有信息显示，但是会显示“已发货”字样的问题
                        tvMoreNotice1.setText("  已发货");
                    } else if (i == 1) {
                        tvNotice2.setText(spanned);
                        tvMoreNotice2.setText("  已发货");
                    } else if (i == 2) {
                        tvNotice3.setText(spanned);
                        tvMoreNotice3.setText("  已发货");
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LuckyMainActivity.this, LuckyNewsListActivity.class);
                        intent.putExtra("from", "send");
                        startActivity(intent);
                    }
                });
            }
        });
    }

    /**
     * 当前图标列表显示的是第几页
     */
    int curIconIndex = 0;

    /**
     * 初始化图标显示
     *
     * @param iconList
     */
    private void initIcons(final List<LuckyMainHeaderEntity.DataBean.IconListBean> iconList) {
        final ArrayList<GridViewIconBean> gridViewIconBeanArrayList = new ArrayList<GridViewIconBean>();
        if (null == iconList || iconList.size() <= 0) {
            includeIcons.setVisibility(View.GONE);
        } else {
            includeIcons.setVisibility(View.VISIBLE);
            for (int i = 0; i < iconList.size(); i++) {
                GridViewIconBean gridViewIconBean = new GridViewIconBean();
                LuckyMainHeaderEntity.DataBean.IconListBean iconListBean = iconList.get(i);
                gridViewIconBean.iconUrl = iconListBean.pic;
                gridViewIconBean.text = iconListBean.name;
                gridViewIconBeanArrayList.add(gridViewIconBean);
            }
        }
        /**
         * 每一页显示ICON的个数
         */
        final int pageSize = 8;

        /**
         * 总的页数
         */
        int pageCount = (int) Math.ceil(iconList.size() * 1.0 / pageSize);
        final List<View> mPagerList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            NoScrollGridView gridView = (NoScrollGridView) LayoutInflater.from(mContext).inflate(R.layout.library_module_gridview, custonViewPager, false);
            gridView.setAdapter(new MTGridViewAdapter(mContext, gridViewIconBeanArrayList, i, pageSize));
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /**
                     * 图标在数据源中的位置
                     */
                    int pos = position + curIconIndex * pageSize;
                    LuckyMainHeaderEntity.DataBean.IconListBean iconListBean = iconList.get(pos);
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(iconListBean.type, iconListBean.content);
                }
            });
        }
        if (pageCount <= 1) {
            llDots.setVisibility(View.GONE);
        } else {
            llDots.setVisibility(View.VISIBLE);
            for (int i = 0; i < pageCount; i++) {
                llDots.addView(LayoutInflater.from(mContext).inflate(R.layout.library_module_page_dot, null));
            }
            // 默认显示第一页
            llDots.getChildAt(0).findViewById(R.id.v_dot)
                    .setBackgroundResource(R.drawable.library_module_mt_home_page_dot_selected);
            custonViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // 取消圆点选中
                    llDots.getChildAt(curIconIndex)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.library_module_dot_normal);
                    // 圆点选中
                    llDots.getChildAt(position)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.library_module_mt_home_page_dot_selected);
                    curIconIndex = position;
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
        custonViewPager.setAdapter(new MTViewPagerAdapter(mPagerList));
    }

    private void initBanner(final List<LuckyMainHeaderEntity.DataBean.BannerListBean> bannerList) {
        ArrayList<String> images = new ArrayList<String>();
        for (int i = 0; i < bannerList.size(); i++) {
            String imageUrl = bannerList.get(i).imageUrl;
            images.add(WebImageUtil.getInstance().getWebImageUrlNOSuffix(imageUrl));
        }
        banner.setImages(images)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        LuckyMainHeaderEntity.DataBean.BannerListBean bannerListBean = bannerList.get(position);
                        Intent intent = new Intent();
                        switch (bannerListBean.bannerType) {
                            case 0:
                                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext,bannerListBean.bannerData,false);
                                break;
                            case 1:
                                String bannerData = bannerListBean.bannerData;
                                jumpToLuckyDetail(intent, bannerData);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .start();
    }

    /**
     * 跳转到幸运购详情页面
     *
     * @param intent
     * @param bannerData
     */
    private void jumpToLuckyDetail(Intent intent, String bannerData) {
        intent.setClass(mContext, LuckyActivityDetailActivity.class);
        intent.putExtra("type", "0");
        intent.putExtra("activity_id", bannerData);
        startActivity(intent);
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.iv_point);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    int page = 0;

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getHeaderData();
    }

    private void getHeaderData() {
        getLuckyMainHeaderData();
        scrollDy = 0;
    }

    private void getFirstPageData() {
        page = 0;
        getLuckyGoodsListData();
    }

    private void getNextPageData() {
        page++;
        getLuckyGoodsListData();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.iv_point) {
            showMenu();
        } else if (i == R.id.iv_search) {
            Intent intent = new Intent(this, LuckyAllPrizeListActivity.class);
            intent.putExtra("isSearch", true);
            startActivity(intent);
        } else if (i == R.id.iv_return_top) {
            recycleView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                tabLayoutFloat.setCurrentTab(0);
                tabLayout.setCurrentTab(0);
                sort = "0,0,1,0";
                break;
            case 1:
                tabLayoutFloat.setCurrentTab(1);
                tabLayout.setCurrentTab(1);
                sort = "1,0,0,0";
                break;
            case 2:
                tabLayoutFloat.setCurrentTab(2);
                tabLayout.setCurrentTab(2);
                sort = "0,1,0,0";
                break;
            case 3:
                tabLayoutFloat.setCurrentTab(3);
                tabLayout.setCurrentTab(3);
                sort = "0,0,0,1";
                break;
            default:
                break;
        }
        getFirstPageData();
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirstPageData();
    }

    @SuppressWarnings("unchecked")
    private void getLuckyGoodsListData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getLuckyGoodsListData("snatch/snatch_list", RequestOftenKey.getToken(mContext), RequestOftenKey.getDeviceIndex(mContext), sort, String.valueOf(page), String.valueOf(Constants.PAGE_COUNT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        Logger.i("swipe走了这里2");
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Logger.i("swipe走了这里1");
                        showToast(e.getMessage());
                        luckyAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(LuckyGoodsListEntity luckyGoodsListEntity) {
                        List<LuckyGoodsListEntity.ListBean> list = luckyGoodsListEntity.list;
                        if (page <= 0) {
                            if (null == list || list.size() <= 0) {
                                luckyAdapter.loadMoreEnd();
                                //无数据 TODO 放置无数据页面
                            } else {
                                if (list.size() >= Constants.PAGE_COUNT) {
                                    luckyAdapter.loadMoreComplete();
                                } else {
                                    luckyAdapter.loadMoreEnd();
                                }
                                luckyAdapter.setNewData(list);
                            }
                        } else {
                            if (list.size() >= Constants.PAGE_COUNT) {
                                luckyAdapter.loadMoreComplete();
                            } else {
                                luckyAdapter.loadMoreEnd();
                            }
                            luckyAdapter.addData(list);
                        }
                    }
                });
        addSubscription(subscription);
    }
}
