package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yilian.mall.adapter.MainCategoryListAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.ui.JPFlagshipActivity;
import com.yilian.mall.ui.JPNewCommDetailActivity;
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
import com.yilian.networkingmodule.entity.IconClassBean;
import com.yilian.networkingmodule.entity.JPFragmentGoodEntity;
import com.yilian.networkingmodule.entity.JPGoodsEntity;
import com.yilian.networkingmodule.entity.JPMainHeaderEntity;
import com.yilian.networkingmodule.entity.JPShopEntity;
import com.yilian.networkingmodule.entity.MainCategoryData;
import com.yilian.networkingmodule.entity.MainCategoryGoodsTitleView;
import com.yilian.networkingmodule.retrofitutil.AppVersion;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主分类Fragment
 */
public class MainCategoryFragment extends JPBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainCategoryListAdapter adapter;
    private InnerViewPager viewPager;
    private ImageView ivReturnTop;
    private View headerView;
    private View emptyView;
    private TextView tvRefresh;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main_category, container, false);
        }
        initView(rootView);
        initListener();
        return rootView;
    }

    ArrayList<MainCategoryData> data = new ArrayList<>();

    private void initView(View rootView) {
        emptyView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) emptyView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(this);
        ivReturnTop = (ImageView) rootView.findViewById(R.id.iv_return_top);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter = new MainCategoryListAdapter(data);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isFirst) {
            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (mAppCompatActivity == null) {
                            return;
                        }
                        mAppCompatActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.setRefreshing(true);
                                }
                                isFirst = false;//放在这里，如果放在请求数据响应方法中，isFirst的赋值会延迟,导致出现快速滑过该界面再迅速滑回来，出现两次请求数据的情况
                                Logger.i("MainCategoryFragment: 执行了 getFirstPageData()");
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
     * 获取第一页数据标记(包括头部数据和列表第一页)
     */
    private boolean getFirstPageDataFlag = true;

    private void getFirstPageData() {
        firstPage = 0;
        secondPage = 0;
        getFirstPageDataFlag = true;
        getHeaderData();
        getShopDataList();
    }

    private void getHeaderData() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMainCategoryHeaderData(AppVersion.getAppVersion(mContext), new Callback<JPMainHeaderEntity>() {
                    @Override
                    public void onResponse(Call<JPMainHeaderEntity> call, Response<JPMainHeaderEntity> response) {
                        JPMainHeaderEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        JPMainHeaderEntity.DataBean jpData = body.JPData;
                                        setHeader(jpData);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JPMainHeaderEntity> call, Throwable t) {

                        adapter.setEmptyView(emptyView);
                    }
                });
    }

    private ArrayList<ImageView> dotImageViews = new ArrayList<>();

    private void setHeader(JPMainHeaderEntity.DataBean jpData) {
        if (jpData != null) {
            ArrayList<BannerEntity> jpAppBanner = jpData.JPAppBanner;
            ArrayList<IconClassBean> jpIconClass = jpData.JPIconClass;
            if (headerView == null) {
                headerView = View.inflate(mContext, R.layout.header_view_main_category, null);
            }
            if (viewPager == null) {
                viewPager = (InnerViewPager) headerView.findViewById(R.id.vp_jp_local_fragment);
            }
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(new ViewPager.LayoutParams());
            params2.height = (int) (screenWidth / 2.5);//新版商城顶部轮播宽高比例是2.5 通过该值动态设置viewpager高度
            viewPager.setLayoutParams(params2);
            LinearLayout llDot = (LinearLayout) headerView.findViewById(R.id.ll_dot);
            CategoryBannerViewPagerAdapter categoryBannerViewPagerAdapter = new CategoryBannerViewPagerAdapter(mContext, jpAppBanner) {
                @Override
                protected void bannerClickJump(BannerEntity entity) {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(entity.JPBannerType, entity.JPBannerData);
                }
            };
            viewPager.setAdapter(categoryBannerViewPagerAdapter);
            //添加小圆点之前先删除所有小圆点，避免小圆点数量异常
            llDot.removeAllViews();
            dotImageViews.clear();
            for (int i = 0, count = jpAppBanner.size(); i < count; i++) {
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
                obtain.obj = jpAppBanner.size();
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
                    if (jpAppBanner.size() != 0) {
                        position = position % jpAppBanner.size();
                        if (position != 0) {
                            position = position - 1;
                        } else {
                            position = jpAppBanner.size() - 1;
                        }
                        llDot.getChildAt(lastPosition).setEnabled(false);
                        llDot.getChildAt(position).setEnabled(true);
                        lastPosition = position;
                    }
                }
            });
            setIcon(jpIconClass, headerView);
            Logger.i("Adapter Header Count:" + adapter.getHeaderLayoutCount() + "  adapter.getHeaderViewsCount():" + adapter.getHeaderViewsCount()
            );
            if (adapter.getHeaderLayout() != null) {
                Logger.i("  adapter.getHeaderLayout().hashCode():" + adapter.getHeaderLayout().hashCode());
            }
            if (adapter.getHeaderLayoutCount() == 0) {
                adapter.addHeaderView(headerView);
            }
        }

    }

    //                View shopTitleView = getTitleView("商家推荐");
//                adapter.addHeaderView(shopTitleView);
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

    private void setIcon(ArrayList<IconClassBean> jpIconClass, View headerView) {
        if (jpIconClass != null && jpIconClass.size() > 0) {
            NoScrollGridView iconGridView = (NoScrollGridView) headerView.findViewById(R.id.gv_activity_5);
            iconGridView.setAdapter(new GVActivity5Adapter(jpIconClass));
        }
    }

    private View getTitleView(String title) {
        View titleView = View.inflate(mContext, R.layout.view_category_title, null);
        TextView tvTitle = (TextView) titleView.findViewById(R.id.tv_goods_list_title);
        tvTitle.setText(title);
        return titleView;
    }

    int firstPage;

    /**
     * 获取店铺列表
     */
    private void getShopDataList() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getRecommendShopsList("0", "0", firstPage, Constants.PAGE_COUNT, new Callback<JPShopEntity>() {
                    @Override
                    public void onResponse(Call<JPShopEntity> call, Response<JPShopEntity> response) {
                        JPShopEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        JPShopEntity.DataBean jpShopData = body.JPShopData;
                                        ArrayList<JPShopEntity.DataBean.SuppliersBean> jpShopSuppliers = jpShopData.JPShopSuppliers;
//                                        如果请求到数据size>0，则添加到adapter里，否则添加一个商品头部到adapter里并请求商品
                                        if (jpShopSuppliers.size() > 0) {
                                            if (getFirstPageDataFlag) {
                                                adapter.setNewData(jpShopSuppliers);
                                                getFirstPageDataFlag = false;
                                                data.clear();
                                            } else {
                                                adapter.addData(jpShopSuppliers);
                                            }
                                            data.addAll(jpShopSuppliers);
                                        }
                                        if (jpShopSuppliers.size() < Constants.PAGE_COUNT) {
//                                            adapter.loadMoreEnd(); //继续请求商品数据，所以这里不结束loadMore
                                            //TODO  添加商品头布局 并开始请求商品数据
                                            MainCategoryGoodsTitleView titleData = new MainCategoryGoodsTitleView("一 每日新品 一");
                                            adapter.addData(titleData);
                                            MainCategoryFragment.this.data.add(titleData);
                                            fistDataNoEnd = false;
                                            getGoodsDataList();
                                        } else {
                                            adapter.loadMoreComplete();
                                        }
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<JPShopEntity> call, Throwable t) {
                        if (firstPage > 0) {
                            firstPage--;
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    int secondPage = 0;

    private void getGoodsDataList() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMainCategoryGoodsList("0", 6, "0", secondPage, Constants.PAGE_COUNT, new Callback<JPFragmentGoodEntity>() {
                    @Override
                    public void onResponse(Call<JPFragmentGoodEntity> call, Response<JPFragmentGoodEntity> response) {
                        JPFragmentGoodEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        JPFragmentGoodEntity.DataBean jpShopData = body.JPShopData;
                                        ArrayList<JPGoodsEntity> jpShopGoods = jpShopData.JPShopGoods;
                                        if (jpShopGoods.size() > 0) {
                                            data.addAll(jpShopGoods);
                                            adapter.addData(jpShopGoods);
                                        }
                                        if (jpShopGoods.size() < Constants.PAGE_COUNT) {
                                            adapter.loadMoreEnd();
                                        } else {
                                            adapter.loadMoreComplete();
                                        }
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<JPFragmentGoodEntity> call, Throwable t) {
                        adapter.loadMoreFail();
                        if (secondPage > 0) {
                            secondPage--;
                        }
                        netRequestEnd();
                        showToast(R.string.net_work_not_available);
                    }
                });
    }


    @Override
    protected void loadData() {

    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                if (scrollDy > ScreenUtils.getScreenHeight(mContext) * 3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        ivReturnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MainCategoryData item = (MainCategoryData) adapter.getItem(position);
                Intent intent = null;
                switch (item.getItemType()) {
                    case MainCategoryData.SHOP:
                        JPShopEntity.DataBean.SuppliersBean suppliersBean = (JPShopEntity.DataBean.SuppliersBean) item;
                        intent = new Intent(mContext, JPFlagshipActivity.class);
                        intent.putExtra("index_id", suppliersBean.JPShopSupplierId);
                        Logger.i("请求商家ID：" + suppliersBean.JPShopSupplierId);
                        intent.putExtra("index_title", suppliersBean.JPShopSupplierName);
                        startActivity(intent);
                        break;
                    case MainCategoryData.GOOD:
                        JPGoodsEntity jpFragmentGoodEntity = (JPGoodsEntity) item;
                        intent = new Intent(mContext, JPNewCommDetailActivity.class);
                        intent.putExtra("tags_name", jpFragmentGoodEntity.JPTagsName);
                        intent.putExtra("filiale_id", jpFragmentGoodEntity.JPFilialeId);
                        intent.putExtra("goods_id", jpFragmentGoodEntity.JPGoodsId);
                        startActivity(intent);
                        break;
                    case MainCategoryData.TITLE:
                        break;
                    default:
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    //第一个列表（商家列表）数据是否请求完毕，没有完毕继续请求第一个列表三数据，完毕了则开始请求第二个列表（商品）数据
    boolean fistDataNoEnd = true;

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        if (fistDataNoEnd) {//请求第一个列表数据
            firstPage++;
            getShopDataList();
        } else {//开始请求第二个列表数据
            secondPage++;
            getGoodsDataList();

        }
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

        private final ArrayList<IconClassBean> list;

        public GVActivity5Adapter(ArrayList<IconClassBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
            final IconClassBean iconClassBean = list.get(position);
            viewHolder.ivCategoryName.setText(iconClassBean.JPName);
            String jpImageUrl = iconClassBean.JPImageUrl;
            if (null != jpImageUrl) {
                GlideUtil.showImage(mContext, jpImageUrl, viewHolder.ivCategoryIcon);
            } else {
                viewHolder.ivCategoryIcon.setImageResource(R.mipmap.default_jp);
            }
            viewHolder.llCategoryIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(iconClassBean.JPType, iconClassBean.JPContent);
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
}
