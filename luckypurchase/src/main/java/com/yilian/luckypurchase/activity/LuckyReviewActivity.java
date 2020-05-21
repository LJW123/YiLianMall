package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyReviewAdapter;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.LuckyRecordListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.luckypurchase.R.id.v3Back;

/**
 * @author Ray_L_Pain
 * 往期回顾页面
 */
public class LuckyReviewActivity extends BaseAppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private TextView tvTitle;
    private ImageView ivMore;
    private ImageView ivBack;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivReturnTop;

    private View emptyView, errorView;
    private TextView tvRefresh;
    private int page = 0;
    private LuckyReviewAdapter adapter;
    private ArrayList<LuckyRecordListEntity.SnatchInfo> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_review);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        id = getIntent().getStringExtra("activity_id");

        tvTitle = (TextView) findViewById(R.id.v3Title);
        tvTitle.setText("往期回顾");
        ivMore = (ImageView) findViewById(R.id.v3Shop);
        ivMore.setImageResource(R.mipmap.library_module_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        ivBack = (ImageView) findViewById(v3Back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        ivReturnTop = (ImageView) findViewById(R.id.iv_return_top);

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
            adapter = new LuckyReviewAdapter(R.layout.lucky_item_review);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.lucky_color_red));
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckyRecordListEntity.SnatchInfo entity = (LuckyRecordListEntity.SnatchInfo) adapter.getItem(position);
                Intent intent = new Intent(LuckyReviewActivity.this, LuckyActivityDetailActivity.class);
                intent.putExtra("activity_id", entity.activity_id);
                intent.putExtra("type", "0");
                intent.putExtra("show_window", "0");
                Logger.i("ray-" + entity.activity_id);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
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
        RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
            .luckyRecordList(id, String.valueOf(page), "20", new Callback<LuckyRecordListEntity>() {
                @Override
                public void onResponse(Call<LuckyRecordListEntity> call, Response<LuckyRecordListEntity> response) {
                    HttpResultBean body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                            LuckyRecordListEntity entity = response.body();
                            switch (response.body().code) {
                                case 1:
                                    ArrayList<LuckyRecordListEntity.SnatchInfo> newList = entity.snatch_info;
                                    if (newList != null && newList.size() > 0) {
                                        if (getFirstPageDataFlag) {
                                            adapter.setNewData(newList);
                                            getFirstPageDataFlag = false;
                                            list.clear();
                                        } else {
                                            adapter.addData(newList);
                                        }
                                        list.addAll(newList);

                                        if (newList.size() < Constants.PAGE_COUNT_20) {
                                            adapter.loadMoreEnd();
                                        } else {
                                            adapter.loadMoreComplete();
                                        }
                                    } else {
                                        if (page == 0) {
                                            adapter.setNewData(newList);
                                            adapter.setEmptyView(emptyView);
                                        } else {
                                            adapter.loadMoreEnd();
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
                public void onFailure(Call<LuckyRecordListEntity> call, Throwable t) {
                    if (page == 0) {
                        adapter.setEmptyView(errorView);
                    } else if (page > 0) {
                        page--;
                    }
                    adapter.loadMoreFail();
                    netRequestEnd();
                    showToast(R.string.lucky_system_busy);
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

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(LuckyReviewActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyReviewActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyReviewActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyReviewActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }


    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }
}
