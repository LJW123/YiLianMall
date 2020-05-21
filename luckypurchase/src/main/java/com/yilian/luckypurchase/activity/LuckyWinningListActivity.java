package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyAwardListAdapter;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.LuckyAwardListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LuckyWinningListActivity extends BaseAppCompatActivity implements View.OnClickListener{


    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View includeTitle;
    private LuckyAwardListAdapter luckyAdapter;
    private ImageView ivReturnTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_lucky_winning_list);
        initView();
        initData();
        initListener();
    }


    private void initView() {

        includeTitle = findViewById(R.id.include_title);
        ivReturnTop = (ImageView) findViewById(R.id.iv_return_top);

        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("最新开奖");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        luckyAdapter = new LuckyAwardListAdapter(R.layout.lucky_item_award_list);
        recyclerView.setAdapter(luckyAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
        ivReturnTop.setOnClickListener(this);
    }

    private void initData() {
        getFirstPageData();
    }

    /**
     * recyclerView滑动高度
     */
    int scrollDy = 0;

    private void initListener() {
        luckyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckyAwardListEntity.SnatchInfoBean adapterItem = (LuckyAwardListEntity.SnatchInfoBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                intent.putExtra("activity_id", adapterItem.activityId);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });
        luckyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
    }

    int page = 0;

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    /**
     * 获取幸运购活动列表数据
     */
    void getData() {
        getLuckyData();
    }
    @SuppressWarnings("unchecked")
    private void getLuckyData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getLuckyAwardList("snatch/snatch_new_award", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                         String.valueOf(page), Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyAwardListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Logger.i("swipe走了这里1");
                        showToast(e.getMessage());
                        luckyAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(LuckyAwardListEntity luckyGoodsListEntity) {
                        List<LuckyAwardListEntity.SnatchInfoBean> list = luckyGoodsListEntity.snatchInfo;
                        setData(list);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(List<LuckyAwardListEntity.SnatchInfoBean> list) {
        if (page <= 0) {
            if (null == list || list.size() <= 0) {
//                                luckyAdapter.loadMoreEnd();
                //无数据 TODO 放置无数据页面
                luckyAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
//                                处理Invalid view holder adapter positionViewHolder
                luckyAdapter.notifyDataSetChanged();
            } else {
                luckyAdapter.setNewData(list);
                if (list.size() < Constants.PAGE_COUNT) {
                    luckyAdapter.loadMoreEnd();
                } else {
                    luckyAdapter.loadMoreComplete();
                }
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
        } else if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_finish) {
        } else if (i == R.id.v3Shop) {
            showMenu();
        }else if(i==R.id.iv_return_top){
            recyclerView.smoothScrollToPosition(0);
        }
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
                        if (sp.getBoolean(Constants.SPKEY_STATE, false)) {
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
}
