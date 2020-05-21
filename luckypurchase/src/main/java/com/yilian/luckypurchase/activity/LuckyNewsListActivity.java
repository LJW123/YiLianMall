package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyNewsListAdapter;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.LuckySnathNewsList;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 幸运购-中奖快报页
 *
 * @author Ray_L_Pain
 * @date 2017/11/22 0022
 */

public class LuckyNewsListActivity extends BaseAppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View emptyView, errorView, headView;
    private ImageView ivPrize, ivSend;
    private TextView tvRefresh;

    private int page = 0;
    private LuckyNewsListAdapter adapter;
    private ArrayList<LuckySnathNewsList.DataBean> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;

    private String from;
    private ImageView ivBackPrize;
    private TextView tvTitlePrize;
    private ImageView ivPrizeMore;
    private RelativeLayout layoutTitlePrize;
    private ImageView ivBackSend;
    private TextView tvTitleSend;
    private ImageView ivSendMore;
    private RelativeLayout layoutTitleSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_news_list);
        initView();
        initData();
        initListener();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) layoutTitlePrize.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            layoutTitlePrize.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            RelativeLayout.LayoutParams titleParams2 = (RelativeLayout.LayoutParams) layoutTitleSend.getLayoutParams();
            titleParams2.height += StatusBarUtils.getStatusBarHeight(mContext);
            layoutTitleSend.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            StatusBarUtil.setTranslucentForImageViewInFragment(LuckyNewsListActivity.this, 60, null);
        }
    }

    private void initView() {
        from = getIntent().getStringExtra("from");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        ivBackPrize = (ImageView) findViewById(R.id.iv_back_prize);
        tvTitlePrize = (TextView) findViewById(R.id.tv_title_prize);
        ivPrizeMore = (ImageView) findViewById(R.id.iv_prize_more);
        layoutTitlePrize = (RelativeLayout) findViewById(R.id.layout_title_prize);
        ivBackSend = (ImageView) findViewById(R.id.iv_back_send);
        tvTitleSend = (TextView) findViewById(R.id.tv_title_send);
        ivSendMore = (ImageView) findViewById(R.id.iv_send_more);
        layoutTitleSend = (RelativeLayout) findViewById(R.id.layout_title_send);

        switch (from) {
            case "prize":
                layoutTitleSend.setVisibility(View.GONE);
                layoutTitlePrize.setVisibility(View.VISIBLE);
                ivBackPrize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                ivPrizeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMenu();
                    }
                });
                if (headView == null) {
                    headView = View.inflate(mContext, R.layout.lucky_head_news_prize, null);
                    ivPrize = (ImageView) headView.findViewById(R.id.iv_prize);
                }
                break;
            case "send":
                layoutTitlePrize.setVisibility(View.GONE);
                layoutTitleSend.setVisibility(View.VISIBLE);
                ivBackSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                ivSendMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMenu();
                    }
                });
                if (headView == null) {
                    headView = View.inflate(mContext, R.layout.lucky_head_news_send, null);
                    ivSend = (ImageView) headView.findViewById(R.id.iv_send);
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
            adapter = new LuckyNewsListAdapter(R.layout.lucky_item_news_list, from);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.lucky_color_red));
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

    //recyclerView滑动高度
    int scrollDy = 0;

    private void initListener() {
        adapter.setOnLoadMoreListener(this, recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;

                if (scrollDy == 0) {
                    layoutTitlePrize.setBackgroundColor(Color.argb(0, 247, 101, 88));
                    layoutTitleSend.setBackgroundColor(Color.argb(0, 60, 94, 231));
                } else if (scrollDy > 0 && scrollDy <= 500) {
                    float scale = (float) scrollDy / 500;
                    float alpha = (255 * scale);
                    layoutTitlePrize.setBackgroundColor(Color.argb((int) alpha, 247, 101, 88));
                    layoutTitleSend.setBackgroundColor(Color.argb((int) alpha, 60, 94, 231));
                } else {
                    layoutTitlePrize.setBackgroundColor(Color.argb((int) 255, 247, 101, 88));
                    layoutTitleSend.setBackgroundColor(Color.argb((int) 255, 60, 94, 231));
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckySnathNewsList.DataBean bean = (LuckySnathNewsList.DataBean) adapter.getItem(position);
                if ("prize".equals(from)) {
                    Intent intent = new Intent(LuckyNewsListActivity.this, LuckyActivityDetailActivity.class);
                    intent.putExtra("activity_id", bean.snatchIndex);
                    intent.putExtra("type", "0");
                    intent.putExtra("showWindow", "");

                    startActivity(intent);
                } else if ("send".equals(from)) {
                    if (isLogin()) {
                        Intent intent = new Intent(mContext, LuckyRecordActivity.class);
                        intent.putExtra("userId", bean.userId);
                        intent.putExtra("position", 1);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        startActivity(intent);
                    }
                }
            }
        });
    }

    LuckySnathNewsList entity;

    private void getData() {
        switch (from) {
            case "prize":
                RetrofitUtils2.getInstance(mContext).luckySnatchPrizeList(String.valueOf(page), "30", new Callback<LuckySnathNewsList>() {
                    @Override
                    public void onResponse(Call<LuckySnathNewsList> call, Response<LuckySnathNewsList> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                entity = response.body();
                                switch (response.body().code) {
                                    case 1:
                                        ArrayList<LuckySnathNewsList.DataBean> newList = entity.data;
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
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<LuckySnathNewsList> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                            if (adapter.getEmptyViewCount() > 0) {
                                ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                            }
                            adapter.notifyDataSetChanged();
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.lucky_system_busy);
                    }
                });
                break;
            case "send":
                RetrofitUtils2.getInstance(mContext).luckySnatchSendList(String.valueOf(page), "30", new Callback<LuckySnathNewsList>() {
                    @Override
                    public void onResponse(Call<LuckySnathNewsList> call, Response<LuckySnathNewsList> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                entity = response.body();
                                switch (response.body().code) {
                                    case 1:
                                        ArrayList<LuckySnathNewsList.DataBean> newList = entity.data;
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
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<LuckySnathNewsList> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                            if (adapter.getEmptyViewCount() > 0) {
                                ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                            }
                            adapter.notifyDataSetChanged();
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.lucky_system_busy);
                    }
                });
                break;
            default:
                break;
        }

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
        Logger.i("ray-" + adapter.getHeaderLayoutCount());
        if (adapter.getHeaderLayoutCount() > 0) {
            adapter.removeAllHeaderView();
        }
        adapter.addHeaderView(headView);

        switch (from) {
            case "prize":
                GlideUtil.showImageNoSuffixNoPlaceholder(mContext, entity.headPhoto, ivPrize);
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ivPrize.getLayoutParams();
                params1.height = (int) ((660 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (750 * 0.1));
                break;
            case "send":
                GlideUtil.showImageNoSuffixNoPlaceholder(mContext, entity.headPhoto, ivSend);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ivSend.getLayoutParams();
                params2.height = (int) ((660 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (750 * 0.1));
                break;
            default:
                break;
        }
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        switch (from) {
            case "prize":
                popupMenu.showLocation(R.id.iv_prize_more);
                break;
            case "send":
                popupMenu.showLocation(R.id.iv_send_more);
                break;
            default:
                break;
        }
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(LuckyNewsListActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyNewsListActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyNewsListActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyNewsListActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
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
