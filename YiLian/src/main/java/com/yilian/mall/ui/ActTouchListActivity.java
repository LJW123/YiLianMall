package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActRecordAdapter;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.GALRecordEntity;
import com.yilian.networkingmodule.entity.GALZanEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ray_L_Pain
 *         碰运气活动列表页
 */
public class ActTouchListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @ViewInject(R.id.layout_title_guess)
    RelativeLayout guessTitleLayout;
    @ViewInject(R.id.iv_back_guess)
    ImageView ivBackGuess;
    @ViewInject(R.id.tv_title_guess)
    TextView tvTitleGuess;
    @ViewInject(R.id.tv_right_guess)
    TextView tvRightGuess;

    @ViewInject(R.id.layout_title_touch)
    RelativeLayout touchTitleLayout;
    @ViewInject(R.id.iv_back_touch)
    ImageView ivBackTouch;
    @ViewInject(R.id.tv_title_touch)
    TextView tvTitleTouch;
    @ViewInject(R.id.tv_right_touch)
    TextView tvRightTouch;

    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private View emptyView, errorView, headView;
    private FrameLayout guessLayout, touchLayout;
    private TextView tvRefresh, tvGuessNum, tvTouchNum;

    private int page = 0;
    private ActRecordAdapter adapter;
    private ArrayList<GALRecordEntity.ListBean> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;

    private String actType, goodsId;
    GALRecordEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_list);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) touchTitleLayout.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            touchTitleLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            RelativeLayout.LayoutParams titleBottomParams = (RelativeLayout.LayoutParams) guessTitleLayout.getLayoutParams();
            titleBottomParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            guessTitleLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            StatusBarUtil.setTranslucentForImageViewInFragment(ActTouchListActivity.this, 60, null);
        }
    }

    private void initView() {
        goodsId = getIntent().getStringExtra("goods_id");
        actType = getIntent().getStringExtra("act_type");
        Logger.i("2017年12月15日 10:01:34走了这里" + actType);
        switch (actType) {
            case "1":
                touchTitleLayout.setVisibility(View.VISIBLE);
                guessTitleLayout.setVisibility(View.GONE);
                ivBackTouch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                tvRightTouch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActTouchListActivity.this, ActMyRecordActivity.class);
                        intent.putExtra("act_type", actType);
                        startActivity(intent);
                    }
                });
                if (headView == null) {
                    headView = View.inflate(mContext, R.layout.header_act_touch, null);
                    tvTouchNum = (TextView) headView.findViewById(R.id.tv_touch_num);
                    touchLayout = (FrameLayout) headView.findViewById(R.id.layout_touch);
                }
                break;
            case "2":
                touchTitleLayout.setVisibility(View.GONE);
                guessTitleLayout.setVisibility(View.VISIBLE);
                ivBackGuess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                tvRightGuess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActTouchListActivity.this, ActMyRecordActivity.class);
                        intent.putExtra("act_type", actType);
                        startActivity(intent);
                    }
                });
                if (headView == null) {
                    headView = View.inflate(mContext, R.layout.header_act_guess, null);
                    tvGuessNum = (TextView) headView.findViewById(R.id.tv_guess_num);
                    guessLayout = (FrameLayout) headView.findViewById(R.id.layout_guess);
                }
                break;
            default:
                break;
        }

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                }
            });
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ActRecordAdapter(R.layout.item_new_evaluate, actType);
        }
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
    }

    private void initData() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefreshLayout != null) {
                                swipeRefreshLayout.setRefreshing(true);
                            }
                            getFirstPageData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    @Subscribe
    public void notifyItem(GALRecordEntity.ListBean item) {
        adapter.notifyItemChanged(item.position);

    }

    //recyclerView滑动高度
    int scrollDy = 0;

    private void initListener() {
        adapter.setOnLoadMoreListener(this, recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;

                if (scrollDy == 0) {
                    touchTitleLayout.setBackgroundColor(Color.argb(0, 164, 14, 234));
                    guessTitleLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                } else if (scrollDy > 0 && scrollDy <= 500) {
                    float scale = (float) scrollDy / 500;
                    float alpha = (255 * scale);
                    touchTitleLayout.setBackgroundColor(Color.argb((int) alpha, 164, 14, 234));
                    guessTitleLayout.setBackgroundColor(Color.argb((int) alpha, 247, 225, 161));
                } else {
                    touchTitleLayout.setBackgroundColor(Color.argb((int) 255, 164, 14, 234));
                    guessTitleLayout.setBackgroundColor(Color.argb((int) 255, 247, 225, 161));
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GALRecordEntity.ListBean item = (GALRecordEntity.ListBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_zan:
                        String isParise;
                        Logger.i("操作前数量："+item.countPraise);
                        if ("0".equals(item.isParise)) {
                            isParise = "1";
                        } else {
                            isParise = "2";
                        }
                        int zanCount = item.countPraise;
                        Logger.i("点赞走了zheli");
                        RetrofitUtils2.getInstance(mContext).setPraise(item.id, actType, isParise, new Callback<GALZanEntity>() {
                            @Override
                            public void onResponse(Call<GALZanEntity> call, Response<GALZanEntity> response) {
                                HttpResultBean body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        switch (body.code) {
                                            case 1:
                                                GALZanEntity entity = response.body();
                                                Logger.i("操作后数量："+entity.pariseNum);

                                                int newCount = entity.pariseNum;
                                                item.countPraise = newCount;
                                                if (zanCount > newCount) {
                                                    //取消赞
                                                    item.isParise = "0";
                                                } else {
                                                    //点赞
                                                    item.isParise = "1";
                                                }

                                                Logger.i("ray-" + "走了这里");
                                                adapter.notifyItemChanged(position + 1, item);
                                                break;
                                            default:
                                                showToast(body.msg);
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GALZanEntity> call, Throwable t) {
                                showToast(R.string.net_work_not_available_zan);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).galRecordList(goodsId, actType, String.valueOf(page), "30", new Callback<GALRecordEntity>() {
            @Override
            public void onResponse(Call<GALRecordEntity> call, Response<GALRecordEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        entity = response.body();
                        switch (response.body().code) {
                            case 1:
                                ArrayList<GALRecordEntity.ListBean> newList = entity.list;
                                Logger.i("onResume:" + newList.size());
                                if (newList != null && newList.size() > 0) {
                                    initHeadView();
                                    if (page > 0) {
                                        adapter.addData(newList);
                                    } else {
                                        getFirstPageDataFlag = false;
                                        adapter.setNewData(newList);
                                    }
                                    if (newList.size() < Constants.PAGE_COUNT) {
                                        adapter.loadMoreEnd();
                                    } else {
                                        adapter.loadMoreComplete();
                                    }
                                } else {
                                    if (page == 0) {
                                        adapter.setNewData(newList);
                                        adapter.setEmptyView(emptyView);
                                        if ("1".equals(actType)) {
                                            ivBackTouch.setImageResource(R.mipmap.v3back);
                                            tvTitleTouch.setTextColor(getResources().getColor(R.color.color_333));
                                            tvRightTouch.setTextColor(getResources().getColor(R.color.color_333));
                                        }
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        Logger.i("2017年12月15日 10:01:34走了这里1111");
                        if ("1".equals(actType)) {
                            Logger.i("2017年12月15日 10:01:34走了这里2222");
                            ivBackTouch.setImageResource(R.mipmap.v3back);
                            tvTitleTouch.setTextColor(getResources().getColor(R.color.color_333));
                            tvRightTouch.setTextColor(getResources().getColor(R.color.color_333));
                        }
                    }
                }
                netRequestEnd();
            }

            @Override
            public void onFailure(Call<GALRecordEntity> call, Throwable t) {
                if (page == 0) {
                    adapter.setEmptyView(errorView);
                    if ("1".equals(actType)) {
                        Logger.i("2017年12月15日 10:01:34走了这里3333");
                        ivBackTouch.setImageResource(R.mipmap.v3back);
                        tvTitleTouch.setTextColor(getResources().getColor(R.color.color_333));
                        tvRightTouch.setTextColor(getResources().getColor(R.color.color_333));
                    }
                } else if (page > 0) {
                    page--;
                    if (adapter.getEmptyViewCount() > 0) {
                        ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                    }
                    adapter.notifyDataSetChanged();
                }
                adapter.loadMoreFail();
                netRequestEnd();
                showToast(R.string.system_busy);
            }
        });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    private void initHeadView() {
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
        switch (actType) {
            case "1":
                tvTouchNum.setText(entity.prizeNum);

                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) touchLayout.getLayoutParams();
                params1.height = (int) ((668 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (1080 * 0.1));
                break;
            case "2":
                tvGuessNum.setText(entity.prizeNum);

                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) guessLayout.getLayoutParams();
                params2.height = (int) ((668 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (1080 * 0.1));
                break;
            default:
                break;
        }
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }
}
