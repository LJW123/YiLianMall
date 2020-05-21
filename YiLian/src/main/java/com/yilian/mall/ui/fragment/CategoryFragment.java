package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.CategoryBannerViewPagerAdapter;
import com.yilian.mall.adapter.CategoryGoodsAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.OnLineMallGoodsListActivity;
import com.yilian.mall.widgets.InnerViewPager;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.BannerEntity;
import com.yilian.networkingmodule.entity.CategoryHeaderEntity;
import com.yilian.networkingmodule.entity.JPGoodsEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 2017年7月31日15:01:05 Create By
 * 子分类Fragment
 */

public class CategoryFragment extends JPBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private View rootView;
    private RecyclerView recyclerView;
    private ArrayList<JPGoodsEntity> jpCategoryRecommendList = new ArrayList<>();
    private CategoryGoodsAdapter categoryGoodsAdapter;
    private NoScrollGridView gvActivity5;
    private LinearLayout llDot;
    private InnerViewPager viewPager;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 获取第一页数据标记(包括头部数据和列表第一页)
     */
    private boolean getFirstPageDataFlag = true;
    private ImageView ivReturnTop;
    private View headerView;
    private View bannerView;
    private View emptyView;
    private TextView tvRefresh;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_category, container, false);
        }
        initView(rootView);
        initListener();
        return rootView;
    }

    private void initView(View rootView) {
        emptyView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) emptyView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(this);
        ivReturnTop = (ImageView) rootView.findViewById(R.id.iv_return_top);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.category_recycler_view);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        categoryGoodsAdapter = new CategoryGoodsAdapter(R.layout.item_jp_good, jpCategoryRecommendList, recyclerView);
        categoryGoodsAdapter.setOnLoadMoreListener(this, recyclerView);
        categoryGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JPGoodsEntity item = (JPGoodsEntity) adapter.getItem(position);

                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                intent.putExtra("tags_name", item.JPTagsName);
                intent.putExtra("filiale_id", item.JPFilialeId);
                intent.putExtra("goods_id", item.JPGoodsId);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(categoryGoodsAdapter);
    }


    public String jpLevel1CategoryId;
    private boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//放在此处， 而不是放在creativeView里面，防止获取对象为null
        jpLevel1CategoryId = getArguments().getString("categoryId");
        if (isVisibleToUser && isFirst) {
            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        mAppCompatActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //setUserVisibleHint方法会先于onCreateView方法执行，而initView是在onCreateView 方法中执行的，因此此处控件可能为空，所以延迟500ms并判空后再使用控件
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.setRefreshing(true);
                                }
                                //放在这里，如果放在请求数据响应方法中，isFirst的赋值会延迟,导致出现快速滑过该界面再迅速滑回来，出现两次请求数据的情况
                                isFirst = false;
                                getFirstPageData();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 获取第一页数据，包括头部数据，和列表第一页数据
     */
    private void getFirstPageData() {
        page = 0;
        getFirstPageDataFlag = true;
        getHeaderData();
        getCategoryRecommendData();
    }

    @Override
    protected void loadData() {
    }

    int page;

    /**
     * 获取头部数据
     * 只进行头部UI的交互，不做刷新UI的交互，简化界面逻辑
     */
    void getHeaderData() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getCategoryHeaderData(jpLevel1CategoryId, new Callback<CategoryHeaderEntity>() {
                    @Override
                    public void onResponse(Call<CategoryHeaderEntity> call, Response<CategoryHeaderEntity> response) {
                        CategoryHeaderEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        CategoryHeaderEntity.DataBean jpCategoryData = body.JPCategoryData;
                                        ArrayList<BannerEntity> jpCategoryAppBanner = jpCategoryData.JPCategoryAppBanner;//Banner
                                        ArrayList<CategoryHeaderEntity.DataBean.CategoryIconClassBean> jpCategoryIconClass = jpCategoryData.JPCategoryIconClass;//Icon
                                        if (jpCategoryData != null) {
                                            if (headerView == null) {
                                                headerView = View.inflate(mContext, R.layout.header_view_good_category, null);
                                            }
                                            setRecyclerHeader(headerView, jpCategoryData);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<CategoryHeaderEntity> call, Throwable t) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        showToast(R.string.net_work_not_available);
                        categoryGoodsAdapter.setEmptyView(emptyView);

                    }
                });
    }

    private ArrayList<ImageView> dotImageViews = new ArrayList<>();

    private void setRecyclerHeader(View headerView, CategoryHeaderEntity.DataBean jpCategoryData) {
        if (jpCategoryData == null) {
            headerView.setVisibility(View.GONE);
            return;
        }

        if (jpCategoryData.JPCategoryAppBanner != null && jpCategoryData.JPCategoryAppBanner.size() > 0) {
            setHeaderBanner(headerView, jpCategoryData);
        }

        if (jpCategoryData.JPCategoryIconClass != null && jpCategoryData.JPCategoryIconClass.size() > 0) {
            setIcons(headerView, jpCategoryData);
        }
        if (categoryGoodsAdapter.getHeaderLayoutCount() == 0) {
            categoryGoodsAdapter.addHeaderView(headerView);
        }
    }

    private void setIcons(View headerView, CategoryHeaderEntity.DataBean jpCategoryData) {
        if (gvActivity5 == null) {
            gvActivity5 = (NoScrollGridView) headerView.findViewById(R.id.gv_activity_5);
        }
        if (jpCategoryData == null || jpCategoryData.JPCategoryIconClass == null || jpCategoryData.JPCategoryIconClass.size() <= 0) {
            gvActivity5.setVisibility(View.GONE);
            return;
        }
        gvActivity5.setAdapter(new GVActivity5Adapter(jpCategoryData.JPCategoryIconClass));
    }

    private void setHeaderBanner(View headerView, final CategoryHeaderEntity.DataBean jpCategoryData) {
        if (jpCategoryData == null || jpCategoryData.JPCategoryAppBanner == null) {
            if (bannerView == null) {
                bannerView = headerView.findViewById(R.id.rl_category_banner);
            }
            bannerView.setVisibility(View.GONE);
            return;
        }
        if (viewPager == null) {
            viewPager = (InnerViewPager) headerView.findViewById(R.id.vp_jp_local_fragment);
        }
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(new ViewPager.LayoutParams());
        params2.height = (int) (screenWidth / 2.5);//新版商城顶部轮播宽高比例是2.5 通过该值动态设置viewpager高度
        viewPager.setLayoutParams(params2);
        if (llDot == null) {
            llDot = (LinearLayout) headerView.findViewById(R.id.ll_dot);
        }
        CategoryBannerViewPagerAdapter categoryBannerViewPagerAdapter = new CategoryBannerViewPagerAdapter(mContext, jpCategoryData.JPCategoryAppBanner) {
            @Override
            protected void bannerClickJump(BannerEntity entity) {
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(entity.JPBannerType, entity.JPBannerData);
            }
        };
        viewPager.setAdapter(categoryBannerViewPagerAdapter);
        //添加小圆点之前先删除所有小圆点，避免小圆点数量异常
        llDot.removeAllViews();
        dotImageViews.clear();
        for (int i = 0, count = jpCategoryData.JPCategoryAppBanner.size(); i < count; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;

            imageView.setImageResource(R.drawable.lldot_selector);
            dotImageViews.add(imageView);
            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
            }
            llDot.addView(imageView, params);
        }
        if (!handler.hasMessages(ROTATE)) {
            Message obtain = Message.obtain();
            obtain.obj = jpCategoryData.JPCategoryAppBanner.size();
            obtain.what = ROTATE;
            handler.sendMessageDelayed(obtain, 3000);
        }
        if (viewPager != null && categoryBannerViewPagerAdapter.getCount() > 0) {
            viewPager.setCurrentItem(1, false);
        }
        categoryBannerViewPagerAdapter.notifyDataSetChanged();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int lastPosition;
            private int Position;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Position = position;
                initChageDot(Position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // 页面切换   自动的切换到对应的界面    最后一个A----->第一个A
                    if (Position == categoryBannerViewPagerAdapter.getCount() - 1) {
                        //最后一个元素  是否平滑切换
                        viewPager.setCurrentItem(1, false);
                    } else if (Position == 0) {
                        //是第一个元素{D] ----> 倒数第二个元素[D]
                        viewPager.setCurrentItem(categoryBannerViewPagerAdapter.getCount() - 2, false);
                    }
                }
            }

            private void initChageDot(int position) {
                initAllDots();//改变点位之前先将所有的点置灰
                if (jpCategoryData.JPCategoryAppBanner.size() != 0) {
                    position = position % jpCategoryData.JPCategoryAppBanner.size();
                    if (position != 0) {
                        position = position - 1;
                    } else {
                        position = jpCategoryData.JPCategoryAppBanner.size() - 1;
                    }
                    llDot.getChildAt(lastPosition).setEnabled(false);
                    llDot.getChildAt(position).setEnabled(true);
                    lastPosition = position;
                }
            }
        });
    }

    private void initAllDots() {
        for (int i = 0; i < dotImageViews.size(); i++) {
            dotImageViews.get(i).setEnabled(false);
        }
    }

    private static final int ROTATE = 0;//viewpager轮播消息
    private int lastItem;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ROTATE://banner自动轮播消息
                    int currentItem = viewPager.getCurrentItem();
                    lastItem = currentItem;
                    Object obj = msg.obj;
                    Logger.i("接收到了ROTATE");
                    if ((int) obj > 0) {
                        viewPager.setCurrentItem((lastItem + 1));
                    }
                    Message obtain = Message.obtain();
                    obtain.obj = obj;
                    handler.sendMessageDelayed(obtain, 3000);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        if (handler != null && handler.hasMessages(ROTATE)) {
            handler.removeMessages(ROTATE);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (viewPager != null) {
            if (handler != null) {
                Message obtain = Message.obtain();
                obtain.obj = ROTATE;
                handler.sendMessageDelayed(obtain, 3000);
            }
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refresh:
                swipeRefreshLayout.setRefreshing(true);
                getFirstPageData();
                break;
        }
    }

    class GVActivity5Adapter extends BaseAdapter {


        private final ArrayList<CategoryHeaderEntity.DataBean.CategoryIconClassBean> jpIconClass;

        public GVActivity5Adapter(ArrayList<CategoryHeaderEntity.DataBean.CategoryIconClassBean> jpIconClass) {
//            Logger.i("分类图标：" + jpIconClass.toString());
            this.jpIconClass = jpIconClass;
        }

        @Override
        public int getCount() {
            return jpIconClass.size();
        }

        @Override
        public Object getItem(int position) {
            return jpIconClass.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.jp_category_icon, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            setViewHolderData(position, viewHolder);
            return convertView;
        }

        private void setViewHolderData(int position, ViewHolder viewHolder) {
            final CategoryHeaderEntity.DataBean.CategoryIconClassBean iconClassBean = jpIconClass.get(position);
            viewHolder.ivCategoryName.setText(iconClassBean.JPCategoryName);
            String jpImageUrl = iconClassBean.JPCategoryImg;
            viewHolder.ivCategoryIcon.setBackgroundColor(Color.TRANSPARENT);
            if (!TextUtils.isEmpty(jpImageUrl)) {
                GlideUtil.showImageWithSuffix(mContext, jpImageUrl, viewHolder.ivCategoryIcon);
            }
//            Logger.i("jpImageUrl:" + jpIm0ageUrl);
            viewHolder.llCategoryIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jpCategoryId = iconClassBean.JPCategoryId;
                    //TODO 根据ID判断跳转子分类页面
                    Intent intent = new Intent(mContext, OnLineMallGoodsListActivity.class);
                    intent.putExtra("class_id", jpCategoryId);
                    intent.putExtra("goods_classfiy", iconClassBean.JPCategoryName);
                    startActivity(intent);
                }
            });
        }

        public class ViewHolder {
            public View rootView;
            public ImageView ivCategoryIcon;
            public TextView ivCategoryName;
            public LinearLayout llCategoryIcon;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.ivCategoryIcon = (ImageView) rootView.findViewById(R.id.iv_category_icon);
                this.ivCategoryName = (TextView) rootView.findViewById(R.id.iv_category_name);
                this.llCategoryIcon = (LinearLayout) rootView.findViewById(R.id.ll_category_icon);
            }

        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getCategoryRecommendData();
        swipeRefreshLayout.setEnabled(false);
    }

    private void initListener() {
        ivReturnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy += dy;
                super.onScrolled(recyclerView, dx, dy);
                if (totalDy > ScreenUtils.getScreenHeight(mContext) * 3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
                categoryGoodsAdapter.setEnableLoadMore(false);
            }
        });
    }

    /**
     * 获取商品列表
     */
    private void getCategoryRecommendData() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getCategoryGoodsList("0000", jpLevel1CategoryId, page, Constants.PAGE_COUNT, null, new Callback<com.yilian.networkingmodule.entity.JPFragmentGoodEntity>() {
                    @Override
                    public void onResponse(Call<com.yilian.networkingmodule.entity.JPFragmentGoodEntity> call, Response<com.yilian.networkingmodule.entity.JPFragmentGoodEntity> response) {

                        com.yilian.networkingmodule.entity.JPFragmentGoodEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        com.yilian.networkingmodule.entity.JPFragmentGoodEntity.DataBean jpShopData = body.JPShopData;
                                        ArrayList<JPGoodsEntity> jpShopGoods = jpShopData.JPShopGoods;
                                        if (jpShopGoods != null && jpShopGoods.size() > 0) {
                                            if (getFirstPageDataFlag) {
                                                categoryGoodsAdapter.setNewData(jpShopGoods);
                                                getFirstPageDataFlag = false;
                                            } else {
                                                categoryGoodsAdapter.addData(jpShopGoods);
                                            }
                                        }
                                        if (jpShopGoods.size() < Constants.PAGE_COUNT) {
                                            categoryGoodsAdapter.loadMoreEnd();
                                        } else {
                                            categoryGoodsAdapter.loadMoreComplete();
                                        }
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(false);
                        categoryGoodsAdapter.setEnableLoadMore(true);
                    }

                    @Override
                    public void onFailure(Call<com.yilian.networkingmodule.entity.JPFragmentGoodEntity> call, Throwable t) {
                        categoryGoodsAdapter.loadMoreFail();
                        if (page > 0) {
                            page--;
                        }
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(false);
                        categoryGoodsAdapter.setEnableLoadMore(true);
                        showToast(R.string.net_work_not_available);
                    }
                });
    }


}
