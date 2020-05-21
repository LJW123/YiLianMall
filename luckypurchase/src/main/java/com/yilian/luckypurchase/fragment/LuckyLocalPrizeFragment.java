package com.yilian.luckypurchase.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.luckypurchase.activity.LuckyLocalListActivity;
import com.yilian.luckypurchase.adapter.LuckyGoodsListAdapter;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.LuckyGoodsListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LuckyLocalPrizeFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LuckyGoodsListAdapter luckyAdapter;
    private ImageView ivReturnTop;
    private String sort;
    private LuckyLocalListActivity fragmentActivity;
    private String merchantId;
    private boolean isVisibleToUser;
    private String merchantName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragmentActivity == null) {
            fragmentActivity = (LuckyLocalListActivity) getActivity();
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.lucky_fragment_lucky_local_prize, container, false);
        initView(inflate);
        initListener();
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst && isVisibleToUser) {
            getFirstPageData();
        }
        isFirst = false;
    }

    /**
     * 第一次进来时，onResume不获取数据，之后onResume再获取数据
     */
    private boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        sort = getArguments().getString("sort");
        merchantId = getArguments().getString("merchantId");
        if (isVisibleToUser) {
            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        fragmentActivity.runOnUiThread(new Runnable() {
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
    }

    private void initListener() {
        luckyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckyGoodsListEntity.ListBean adapterItem = (LuckyGoodsListEntity.ListBean) adapter.getItem(position);
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
            int scrollDy = 0;

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

    private void initView(View view) {
        ivReturnTop = (ImageView) view.findViewById(R.id.iv_return_top);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        luckyAdapter = new LuckyGoodsListAdapter(R.layout.lucky_item_prize_progress, false);
        recyclerView.setAdapter(luckyAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        ivReturnTop.setOnClickListener(this);
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
                .getLocalLuckyData("nearby_snatch_list", sort,
                        page, Constants.PAGE_COUNT, merchantId, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyGoodsListEntity>() {
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
                    public void onNext(LuckyGoodsListEntity luckyGoodsListEntity) {
                        List<LuckyGoodsListEntity.ListBean> list = luckyGoodsListEntity.list;
                        merchantName = luckyGoodsListEntity.merchantName;
                        ((TextView) fragmentActivity.findViewById(R.id.v3Title)).setText(merchantName);
                        setData(list);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(List<LuckyGoodsListEntity.ListBean> list) {
        if (page <= 0) {
            if (null == list || list.size() <= 0) {
                luckyAdapter.setEmptyView(R.layout.library_module_no_data);
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
    public void onClick(View view) {

    }
}
