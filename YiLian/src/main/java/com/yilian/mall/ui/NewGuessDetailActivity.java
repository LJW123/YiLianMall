package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.NewGuessDetailAdapter;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.widgets.CircleView;
import com.yilian.mall.widgets.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.GuessInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.utils.DateUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ray_L_Pain
 * @date 2017/10/16 0016
 */

public class NewGuessDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    private View headView, emptyView, errorView, nothingView;
    private TextView tvRefresh, tvNoData, tvGuessNum, tvName, tvTime, tvState, tvGoingJoinNum, tvGoingAllNum, tvGoingResidueNum, tvFinishName, tvFinishCount, tvFinishWinNum;
    private ImageView ivPic, ivState, ivCrown;
    private LinearLayout goingLayout, finishLayout, guessLayout;
    private ProgressBar progressBar;
    private CircleView ivFinish;
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;
    @ViewInject(R.id.iv_right)
    ImageView ivMore;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private int page = 0;
    private NewGuessDetailAdapter adapter;
    private ArrayList<GuessInfoEntity.InfoBean.GuessNumberBean> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    //活动id
    private String id;
    //是否是我参与的 1我猜过的 2已开奖
    private String isParticipate;
    //
    private boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guess_detail);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        isParticipate = getIntent().getStringExtra("is_participate");
        Logger.i("ray-" + isParticipate);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (isParticipate) {
            case "1":
                tvTitle.setText("我猜过的数");
                ivMore.setVisibility(View.VISIBLE);
                ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuShow();
                    }
                });
                tvRight.setVisibility(View.GONE);
                break;
            case "2":
                tvTitle.setText("已开奖");
                ivMore.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText("我猜过的数");
                tvRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewGuessDetailActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("is_participate", "1");
                        startActivity(intent);
                    }
                });
                break;
        }

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
            tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
            tvNoData.setText("暂无更多数据");
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

        if (headView == null) {
            headView = View.inflate(mContext, R.layout.header_guess_detail, null);
            guessLayout = (LinearLayout) headView.findViewById(R.id.layout_guess);
            tvGuessNum = (TextView) headView.findViewById(R.id.tv_guess_num);
            ivPic = (ImageView) headView.findViewById(R.id.iv_pic);
            tvName = (TextView) headView.findViewById(R.id.tv_name);
            ivState = (ImageView) headView.findViewById(R.id.iv_state);
            ivState.setVisibility(View.VISIBLE);
            tvState = (TextView) headView.findViewById(R.id.tv_status);
            tvTime = (TextView) headView.findViewById(R.id.tv_time);

            goingLayout = (LinearLayout) headView.findViewById(R.id.layout_going);
            progressBar = (ProgressBar) headView.findViewById(R.id.progressBar);
            tvGoingJoinNum = (TextView) headView.findViewById(R.id.tv_going_join_num);
            tvGoingAllNum = (TextView) headView.findViewById(R.id.tv_going_all_num);
            tvGoingResidueNum = (TextView) headView.findViewById(R.id.tv_going_residue_num);

            finishLayout = (LinearLayout) headView.findViewById(R.id.layout_finish);
            ivFinish = (CircleView) headView.findViewById(R.id.iv_finish);
            ivCrown = (ImageView) headView.findViewById(R.id.iv_crown);
            tvFinishName = (TextView) headView.findViewById(R.id.tv_finish_name);
            tvFinishCount = (TextView) headView.findViewById(R.id.tv_finish_count);
            tvFinishCount.setText("中奖号码：");
            tvFinishWinNum = (TextView) headView.findViewById(R.id.tv_finish_win_num);
        }

        if (nothingView == null) {
            nothingView = View.inflate(mContext, R.layout.view_nothing, null);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST, 20, getResources().getColor(R.color.color_bg));
        recyclerView.addItemDecoration(decoration);
        if (adapter == null) {
            adapter = new NewGuessDetailAdapter(R.layout.item_new_guess_detail_list, isParticipate);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isRefresh) {
            Logger.i("ray-" + "onResume");
            swipeRefreshLayout.setRefreshing(true);
            getFirstPageDataFlag = true;
            getFirstPageData();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_DETAIL, isRefresh, mContext);
        }
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
                    iv_return_top.setVisibility(View.VISIBLE);
                } else {
                    iv_return_top.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        iv_return_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    private void initData() {

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_GUESS_DETAIL, mContext, false);

        Logger.i("ray-" + "走了initData");
        if (!isRefresh) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(true);
            }
            getFirstPageData();
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

    private void getData() {
        if ("1".equals(isParticipate)) {
            RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .guessHasPrize(id, String.valueOf(page), "20", new Callback<GuessInfoEntity>() {
                        @Override
                        public void onResponse(Call<GuessInfoEntity> call, Response<GuessInfoEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    GuessInfoEntity entity = response.body();
                                    switch (entity.code) {
                                        case 1:
                                            ArrayList<GuessInfoEntity.InfoBean.GuessNumberBean> newList = entity.info.guess_number;
                                            if (newList != null && newList.size() > 0) {
                                                if (getFirstPageDataFlag) {
                                                    adapter.setNewData(newList);
                                                    getFirstPageDataFlag = false;
                                                    list.clear();
                                                } else {
                                                    adapter.addData(newList);
                                                }
                                                list.addAll(newList);

                                                initHeaderView(entity.info);

                                                if (newList.size() < Constants.PAGE_COUNT_20) {
                                                    adapter.loadMoreEnd();
                                                } else {
                                                    adapter.loadMoreComplete();
                                                }
                                            } else {
                                                if (page == 0) {
                                                    //走这里 说明当前活动 我没有参加 显示一个头部和底部
                                                    adapter.setNewData(newList);
                                                    initHeaderView(entity.info);
                                                    initFootView();
                                                    if (adapter.getEmptyViewCount() > 0) {
                                                        ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                                                    }
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
                        public void onFailure(Call<GuessInfoEntity> call, Throwable t) {
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
                            showToast(R.string.merchant_module_service_exception);
                        }
                    });
        } else if ("2".equals(isParticipate)) {
            RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .guessInfoSelf(id, String.valueOf(page), "20", new Callback<GuessInfoEntity>() {
                        @Override
                        public void onResponse(Call<GuessInfoEntity> call, Response<GuessInfoEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    GuessInfoEntity entity = response.body();
                                    switch (entity.code) {
                                        case 1:
                                            ArrayList<GuessInfoEntity.InfoBean.GuessNumberBean> newList = entity.info.guess_number;
                                            if (newList != null && newList.size() > 0) {
                                                if (getFirstPageDataFlag) {
                                                    adapter.setNewData(newList);
                                                    getFirstPageDataFlag = false;
                                                    list.clear();
                                                } else {
                                                    adapter.addData(newList);
                                                }
                                                list.addAll(newList);

                                                initHeaderView(entity.info);

                                                if (newList.size() < Constants.PAGE_COUNT_20) {
                                                    adapter.loadMoreEnd();
                                                } else {
                                                    adapter.loadMoreComplete();
                                                }
                                            } else {
                                                if (page == 0) {
                                                    Logger.i("ray-10.18-" + "讲道理，不会走这里");
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
                        public void onFailure(Call<GuessInfoEntity> call, Throwable t) {
                            if (page == 0) {
                                adapter.setEmptyView(errorView);
                            } else if (page > 0) {
                                page--;
                            }
                            adapter.loadMoreFail();
                            netRequestEnd();
                            showToast(R.string.merchant_module_service_exception);
                        }
                    });
        }
    }

    private void initFootView() {
        if (adapter.getFooterLayoutCount() != 0) {
            adapter.removeAllFooterView();
        }
        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(nothingView);
        }
    }

    private void initHeaderView(GuessInfoEntity.InfoBean info) {
//        if (adapter.getHeaderLayoutCount() != 0) {
//            adapter.removeAllHeaderView();
//        }

        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
        guessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(info.status)) {
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_DETAIL, true, mContext);
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("url", Constants.PARADISE_GUESS + "?activity_id=" + id);
                    startActivity(intent);
                }
            }
        });

        GlideUtil.showImageNoSuffix(mContext, info.goods_icon, ivPic);
        tvName.setText(info.goods_name);

        int allNum = Integer.parseInt(info.total_should);
        switch (info.status) {
            case "1":
                ivState.setBackgroundResource(R.mipmap.icon_guess_state_going);
                tvState.setText("进行中");
                tvTime.setText(DateUtils.formatDate2(Long.parseLong(info.start_at)));
                goingLayout.setVisibility(View.VISIBLE);
                tvGoingJoinNum.setText(info.participants);
                tvGoingAllNum.setText(info.total_should);
                int shouldNum = Integer.parseInt(info.participants);
                tvGoingResidueNum.setText(String.valueOf(allNum - shouldNum));
                progressBar.setProgress(shouldNum);
                progressBar.setMax(allNum);
                finishLayout.setVisibility(View.GONE);
                tvGuessNum.setText("?");
                break;
            case "2":
                ivState.setBackgroundResource(R.mipmap.icon_guess_state_finish);
                tvState.setText("已开奖");
                tvTime.setText(DateUtils.formatDate2(Long.parseLong(info.lottery_at)));
                goingLayout.setVisibility(View.GONE);
                finishLayout.setVisibility(View.VISIBLE);
                GlideUtil.showCirImage(mContext, info.user_photo, ivFinish);
                tvFinishName.setText(info.user_name);
                tvFinishCount.setText(info.total_should);
                tvFinishWinNum.setText("中奖号码：" + info.prize_number);
                tvGuessNum.setText(info.prize_number);
                break;
            default:
                break;
        }
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }


    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

    /**
     * 右上角更多
     */
    private void menuShow() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.iv_right);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            startActivity(new Intent(NewGuessDetailActivity.this, InformationActivity.class));
                        } else {
                            startActivity(new Intent(NewGuessDetailActivity.this, LeFenPhoneLoginActivity.class));
                        }
                        break;
                    case ITEM2:
                        Intent intentHome = new Intent(NewGuessDetailActivity.this, JPMainActivity.class);
                        intentHome.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        startActivity(intentHome);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        Intent intentUser = new Intent(NewGuessDetailActivity.this, JPMainActivity.class);
                        intentUser.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        startActivity(intentUser);
                        break;
                }
            }
        });
    }
}
