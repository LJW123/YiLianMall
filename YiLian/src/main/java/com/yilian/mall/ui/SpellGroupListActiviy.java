package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.SpellGoodsListRecyleAdapter;
import com.yilian.mall.adapter.SpellGroupGridViewAdapter;
import com.yilian.mall.adapter.SpellGroupListBannerAdapter;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSignGVEntity;
import com.yilian.mall.entity.NoticeViewEntity;
import com.yilian.mall.entity.SpellGroupListEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mall.widgets.NoScrollRecyclerView;
import com.yilian.mall.widgets.NoticeView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.DPXUnitUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 拼团列表
 */
public class SpellGroupListActiviy extends BaseActivity implements View.OnClickListener {


    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private ViewPager viewpager;
    private LinearLayout llPoints;
    private NoticeView noticeViewJpMain;
    private LinearLayout llNoticeNews;
    private NoScrollGridView gridViwe;
    private JPNetRequest jpNetRequest;
    private LinearLayout llErrorView;
    private TextView tvRefresh;
    private SpellGroupGridViewAdapter adapter;
    private SpellGoodsListRecyleAdapter recycleViewAdapter;
    private LinearLayout llRecommend;
    private int page;
    private int count;
    private ArrayList<SpellGroupListEntity.DataBean> recycleListData = new ArrayList<>();
    private SpellGroupGridViewAdapter adapter1;
    private ImageView ivNothing;

    private RelativeLayout rlMySpellGroup;
    private ArrayList<ImageView> dotImageViews = new ArrayList<>();
    private List<SpellGroupListEntity.GroupBannerBean> groupBanner;
    private SpellGroupListBannerAdapter viewpagerAdapter;
    private PullToRefreshScrollView scrollView;
    private NoScrollRecyclerView recyclerView;
    private ImageView ivTopNothing;
    private ImageView ivDefauleBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_group_list_activiy);
        initView();
        count = 30;
        page = 0;
        initHeardData();
        initListener();
    }


    private void initHeardData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).getSpellGroupListContent(String.valueOf(page), String.valueOf(count), new Callback<SpellGroupListEntity>() {
            @Override
            public void onResponse(Call<SpellGroupListEntity> call, Response<SpellGroupListEntity> response) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                    if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                        switch (response.body().code) {
                            case 1:
                                List<SpellGroupListEntity.DataBean> listData = response.body().data;
                                if (null == listData && listData.size() == 0 && null == response.body().list &&
                                        response.body().list.size() == 0 && null == response.body().groupBanner && response.body().groupBanner.size() == 0) {
                                    ivTopNothing.setVisibility(View.VISIBLE);
                                } else {
                                    ivTopNothing.setVisibility(View.GONE);
                                    llErrorView.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    groupBanner = response.body().groupBanner;
                                    List<NoticeViewEntity> noticeList = response.body().list;
                                    initViewPager(groupBanner);
                                    initNoticeView((ArrayList<NoticeViewEntity>) noticeList);
                                    initListData(listData);
                                }
                                scrollView.onRefreshComplete();
                                break;
                        }
                    } else {
                        stopMyDialog();
                        ivTopNothing.setVisibility(View.VISIBLE);
                        initBottomData();
                    }
                }
            }

            @Override
            public void onFailure(Call<SpellGroupListEntity> call, Throwable t) {
                stopMyDialog();
                scrollView.onRefreshComplete();
                scrollView.setVisibility(View.GONE);
                llErrorView.setVisibility(View.VISIBLE);

            }
        });
    }

    private void initListener() {

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                initHeardData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                initHeardData();
            }
        });

        gridViwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JPGoodsEntity itemView = (JPGoodsEntity) adapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);

                intent.putExtra("goods_id", itemView.JPGoodsId);
                intent.putExtra("filiale_id", itemView.JPFilialeId);
                intent.putExtra("tags_name", itemView.JPTagsName);
                startActivity(intent);
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int finalPosition;
            int lastPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                finalPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                initChageDot(position);
            }

            private void initChageDot(int position) {
                initAllDots();//改变点位之前先将所有的点置灰
                if (groupBanner.size() != 0) {
                    position = position % groupBanner.size();
                    if (position != 0) {
                        position = position - 1;
                    } else {
                        position = groupBanner.size() - 1;
                    }
                    View childAt = llPoints.getChildAt(lastPosition);
                    if (childAt != null) {
                        childAt.setEnabled(false);
                    }
                    View childAt1 = llPoints.getChildAt(position);
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
                    if (finalPosition == viewpagerAdapter.getCount() - 1) {
                        //最后一个元素  是否平滑切换
                        viewpager.setCurrentItem(1, false);
                    } else if (finalPosition == 0) {
                        //是第一个元素{D] ----> 倒数第二个元素[D]
                        viewpager.setCurrentItem(viewpagerAdapter.getCount() - 2, false);
                    }
                }

            }
        });

    }

    private void initAllDots() {
        //默认置灰所有的点
        for (int i = 0; i < dotImageViews.size(); i++) {
            dotImageViews.get(i).setEnabled(false);
        }
    }


    //初始化listView的
    private void initListData(List<SpellGroupListEntity.DataBean> listData) {
        if (null != listData && listData.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //分页
            if (page >= 1) {
                recycleListData.addAll(listData);
            } else {
                recycleListData.clear();
                recycleListData.addAll(listData);
                if (recycleViewAdapter == null) {
                    recycleViewAdapter = new SpellGoodsListRecyleAdapter(recycleListData, mContext);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    linearLayoutManager.setSmoothScrollbarEnabled(true);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    linearLayoutManager.setAutoMeasureEnabled(true);//这四句话解决scrollView嵌套RecycleView滑动卡顿的情况
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    recyclerView.setNestedScrollingEnabled(false);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    recyclerView.setAdapter(recycleViewAdapter);
                }
                recycleViewAdapter.notifyDataSetChanged();
            }
        } else {
            if (page > 0) {
                showToast("暂无更多");
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }


        if (listData.size() < count) {
            initBottomData();
        }
    }

    private void initBottomData() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        //获取的每日精品推荐
        jpNetRequest.signGridView(new RequestCallBack<JPSignGVEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSignGVEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<JPGoodsEntity> goodsList = responseInfo.result.data.goodsList;
                        if (null != goodsList || goodsList.size() > 0) {
                            llRecommend.setVisibility(View.VISIBLE);
                            adapter = new SpellGroupGridViewAdapter(goodsList);
                            gridViwe.setAdapter(adapter);
                        } else {
                            gridViwe.setVisibility(View.GONE);
                            llRecommend.setVisibility(View.GONE);
                        }
                        stopMyDialog();
                        break;
                    default:
                        Logger.i(responseInfo.result.code + responseInfo.result.msg);
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    //实现中奖列表的轮播
    private void initNoticeView(ArrayList<NoticeViewEntity> list) {
        if (null == list || list.size() < 1) {
            llNoticeNews.setVisibility(View.GONE);
        } else {
            llNoticeNews.setVisibility(View.VISIBLE);
            noticeViewJpMain.getPublicNotices(3, list);
        }
    }

    private void initViewPager(List<SpellGroupListEntity.GroupBannerBean> data) {
        if (page == 0 && null != data && data.size() > 0) {
            ivDefauleBanner.setVisibility(View.GONE);
            viewpagerAdapter = new SpellGroupListBannerAdapter(data, mContext);
            viewpager.setAdapter(viewpagerAdapter);
            viewpager.setCurrentItem(1);
            initPionts(data);
        } else {
            ivDefauleBanner.setVisibility(View.VISIBLE);
        }
    }

    private void initPionts(List<SpellGroupListEntity.GroupBannerBean> data) {
        //添加小圆点之前先删除所有小圆点，避免小圆点数量异常
        llPoints.removeAllViews();
        dotImageViews.clear();
        if (data.size() > 1) {
            for (int i = 0, count = data.size(); i < count; i++) {
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
                llPoints.addView(imageView, params);
            }
        } else {
            llPoints.setVisibility(View.GONE);
        }

    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("拼团列表");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.GONE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.requestFocus();
        llPoints = (LinearLayout) findViewById(R.id.ll_points);
        noticeViewJpMain = (NoticeView) findViewById(R.id.noticeView_jp_main);
        llNoticeNews = (LinearLayout) findViewById(R.id.ll_notice_news);
        ImageView ivLeFenLetters = (ImageView) findViewById(R.id.iv_lefen_letters);
        ivLeFenLetters.setVisibility(View.GONE);
        llRecommend = (LinearLayout) findViewById(R.id.ll_recommend);
        gridViwe = (NoScrollGridView) findViewById(R.id.gridView);
        gridViwe.setFocusable(false);
        recyclerView = (NoScrollRecyclerView) findViewById(R.id.recycler_view);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        rlMySpellGroup = (RelativeLayout) findViewById(R.id.rl_my_spell_group);
        ivTopNothing = (ImageView) findViewById(R.id.iv_nothing_top);

        llErrorView = (LinearLayout) findViewById(R.id.ll_error_view);
        llErrorView.setVisibility(View.GONE);
        tvRefresh = (TextView) findViewById(R.id.tv_update_error);
        ivDefauleBanner = (ImageView) findViewById(R.id.iv_default_banner);

        v3Back.setOnClickListener(this);
        rlMySpellGroup.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.rl_my_spell_group:
                if (!isLogin()) {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, MySpellGroupActivity.class));
                }
                break;
            case R.id.tv_update_error:
                initHeardData();
                break;
        }
    }

}