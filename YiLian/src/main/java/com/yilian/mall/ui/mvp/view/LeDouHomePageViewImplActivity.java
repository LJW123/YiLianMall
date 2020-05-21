package com.yilian.mall.ui.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.luckypurchase.widget.NoScrollGridView;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.adapter.LeDouListAdapter;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.SearchCommodityActivity;
import com.yilian.mall.ui.mvp.presenter.ILeDouHomePagePresenter;
import com.yilian.mall.ui.mvp.presenter.LeDouHomePagePresenterImpl;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.MenuUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.adapter.MTGridViewAdapter;
import com.yilian.mylibrary.adapter.MTViewPagerAdapter;
import com.yilian.mylibrary.entity.GridViewIconBean;
import com.yilian.mylibrary.widget.CustomViewPager;
import com.yilian.networkingmodule.entity.DataBean;
import com.yilian.networkingmodule.entity.LeDouHomePageDataEntity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Subscription;

import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS;
import static com.yilian.mall.ui.mvp.model.LeDouHomePageModelImpl.HOME_PAGE_LE_DOU;

public class LeDouHomePageViewImplActivity extends BaseAppCompatActivity implements ILeDouHomePageView, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ImageView ivBack;
    private ImageView ivPoint;
    private ImageView ivSearch;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;
    private ILeDouHomePagePresenter leDouHomePagePresenter;
    private LeDouListAdapter leDouListAdapter;
    private View headerView;
    private View includeBanner;
    private Banner banner;
    private ImageView ivLedouTitle;
    private CustomViewPager custonViewPager;
    private View includeIcons;
    private LinearLayout llSearch;
    private ImageView ivReturnTop;
    /**
     * recyclerView滑动高度
     */
    int scrollDy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_dou_home_page_view_impl);
        leDouHomePagePresenter = new LeDouHomePagePresenterImpl(this);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_white));
    }
    private void initListener() {
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
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
                super.onScrolled(recyclerView, dx, dy);
                scrollDy += dy;
                int screenHeight3 = ScreenUtils.getScreenHeight(mContext) * 3;
                if (scrollDy > screenHeight3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
            }
        });
        leDouListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DataBean item = (DataBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                intent.putExtra("filiale_id", item.filialeId);
                intent.putExtra("goods_id", item.goodsId);
                intent.putExtra("classify", LE_DOU_GOODS);
                startActivity(intent);
            }
        });
        leDouListAdapter.setOnLoadMoreListener(this, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载数据不使用该控件的动效
                swipeRefreshLayout.setRefreshing(false);
                getFirstData();
            }
        });
    }

    private void initData() {
        getFirstData();
    }

    void getFirstData() {
        page = 0;
        scrollDy = 0;
        Subscription subscription = leDouHomePagePresenter.getData(mContext, page, HOME_PAGE_LE_DOU);
        addSubscription(subscription);
    }

    void getNextPageData() {
        page++;
        Subscription subscription = leDouHomePagePresenter.getData(mContext, page, HOME_PAGE_LE_DOU);
        addSubscription(subscription);
    }

    /**
     * 当前图标列表显示的是第几页
     */
    int curIconIndex = 0;

    @Override
    public void setData(LeDouHomePageDataEntity leDouHomePageData) {
        List<DataBean> data = leDouHomePageData.data;
        if (page == 0) {
            List<LeDouHomePageDataEntity.BannerBean> bannerData = leDouHomePageData.banner;
            List<LeDouHomePageDataEntity.IconBean> iconList = leDouHomePageData.icon;
//            TODO 设置头部数据
            if (headerView == null) {
                headerView = View.inflate(mContext, R.layout.header_yi_beans, null);
                includeBanner = headerView.findViewById(R.id.include_banner);
                banner = includeBanner.findViewById(R.id.banner);
                includeIcons = headerView.findViewById(R.id.include_icons);
                llSearch = headerView.findViewById(R.id.ll_search);
                RxUtil.clicks(llSearch, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(mContext, SearchCommodityActivity.class);
                        intent.putExtra("type", GoodsType.TYPE_TEST_GOODS);
                        startActivity(intent);
                    }
                });
                custonViewPager = (CustomViewPager) includeIcons.findViewById(R.id.viewpager);
                ImageView ivListHeader = headerView.findViewById(R.id.iv_list_header);
            }
            if (leDouListAdapter.getHeaderLayoutCount() <= 0) {
                leDouListAdapter.setHeaderView(headerView);
            }
            setBannerData(bannerData, includeBanner, banner);
            setIconData(iconList, includeIcons);

            if (data.size() <= 0) {
                leDouListAdapter.loadMoreEnd();
//                TODO 设置空布局
            } else {
                leDouListAdapter.setNewData(data);
                isLoadMore(data);
            }
        } else {
            if (data.size() <= 0) {
                leDouListAdapter.loadMoreEnd();
            } else {
                leDouListAdapter.addData(data);
                isLoadMore(data);
            }
        }
    }

    private void setIconData(List<LeDouHomePageDataEntity.IconBean> iconList, View includeIcons) {
        if (iconList == null || iconList.size() <= 0) {
            includeIcons.setVisibility(View.GONE);
        } else {
            includeIcons.setVisibility(View.VISIBLE);
            final ArrayList<GridViewIconBean> gridViewIconBeanArrayList = new ArrayList<GridViewIconBean>();
            for (int i = 0; i < iconList.size(); i++) {
                GridViewIconBean gridViewIconBean = new GridViewIconBean();
                LeDouHomePageDataEntity.IconBean iconListBean = iconList.get(i);
                gridViewIconBean.iconUrl = iconListBean.img;
                gridViewIconBean.text = iconListBean.name;
                gridViewIconBeanArrayList.add(gridViewIconBean);
            }
            /**
             * 每一页显示ICON的个数
             */
            final int pageSize = 10;
            /**
             * 总的页数
             */
            int pageCount = (int) Math.ceil(iconList.size() * 1.0 / pageSize);
            final List<View> mPagerList = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                //每个页面都是inflate出一个新实例
                NoScrollGridView gridView = (NoScrollGridView) LayoutInflater.from(mContext).inflate(R.layout.library_module_gridview, custonViewPager, false);
                gridView.setNumColumns(5);
                gridView.setAdapter(new MTGridViewAdapter(mContext, gridViewIconBeanArrayList, i, pageSize));
                mPagerList.add(gridView);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /**
                         * 图标在数据源中的位置
                         */
                        int pos = position + curIconIndex * pageSize;
                        LeDouHomePageDataEntity.IconBean iconListBean = iconList.get(pos);
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(iconListBean.type, iconListBean.content);
                    }
                });
            }
            custonViewPager.setAdapter(new MTViewPagerAdapter(mPagerList));
        }
    }


    private void setBannerData(List<LeDouHomePageDataEntity.BannerBean> bannerData, View includeBanner, Banner banner) {
        if (bannerData == null || bannerData.size() <= 0) {
            includeBanner.setVisibility(View.GONE);
        } else {
            includeBanner.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) banner.getLayoutParams();
            layoutParams.height = (int) ((175 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));
            ArrayList<String> imageList = new ArrayList<String>();
            int size = bannerData.size();
            for (int i = 0; i < size; i++) {
                String image = bannerData.get(i).img;
                imageList.add(WebImageUtil.getInstance().getWebImageUrlNOSuffix(image));
            }
            banner.setImages(imageList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            LeDouHomePageDataEntity.BannerBean bannerBean = bannerData.get(position);
                            JumpToOtherPage.getInstance(mContext).jumpToOtherPage(bannerBean.type, bannerBean.content);
                        }
                    }).start();
        }
    }

    /**
     * 列表是否加载下一页
     *
     * @param data
     */
    private void isLoadMore(List<DataBean> data) {
        if (data.size() < Constants.PAGE_COUNT) {
            leDouListAdapter.loadMoreEnd();
        } else {
            leDouListAdapter.loadMoreComplete();
        }
    }

    private void initView() {
        View includeHeader = findViewById(R.id.include_header);
        ivLedouTitle = includeHeader.findViewById(R.id.iv_title);
        ivLedouTitle.setImageResource(R.mipmap.ledou_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPoint = (ImageView) findViewById(R.id.iv_point);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivSearch.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        leDouListAdapter = new LeDouListAdapter(R.layout.item_list_yi_beans);
        recyclerView.setAdapter(leDouListAdapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        ivBack.setOnClickListener(this);
        ivPoint.setOnClickListener(this);
        ivReturnTop = (ImageView) findViewById(R.id.iv_return_top);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_point:
                MenuUtil.showMenu(this, R.id.iv_point);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }
}
