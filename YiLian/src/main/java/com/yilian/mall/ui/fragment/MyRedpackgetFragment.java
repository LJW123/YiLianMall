package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.adapter.MyRedPackgetsAdapter;
import com.yilian.mall.R;
import com.yilian.mall.ui.StealRedPackgDetailsActivity;
import com.yilian.mall.ui.StealRedPackgeTrendsActivity;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.networkingmodule.entity.StealMyRedpackgets;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 *
 * @author ZYH
 * @date 2017/8/1 0001
 * 我的奖励fragment
 */
public class MyRedpackgetFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private TextView tvStealStrategy;
    private MySwipeRefreshLayout refreshLayout;
    private MyRedPackgetsAdapter myRedPackgetsAdapter;
    private View headerView;
    public List<StealMyRedpackgets.Data.RedDetails> list;
    private ImageView ivUserHeadPhoto;
    private TextView tvMoreTrends;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steal_redpackget_list, container, false);
        initView(view);
        initListener();
        return view;
    }


    private void initView(View view) {
        tvStealStrategy = (TextView) view.findViewById(R.id.tv_steal_redpacage_strategy);
        tvStealStrategy.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        headerView = View.inflate(mContext, R.layout.header_my_redpackget, null);
        ivUserHeadPhoto = (ImageView) headerView.findViewById(R.id.user_icon);
        tvMoreTrends = (TextView) headerView.findViewById(R.id.tv_more_trends);
        if (!TextUtils.isEmpty(sp.getString("photo",""))){
            GlideUtil.showImageRadius(mContext,sp.getString("photo",""),ivUserHeadPhoto,6);
        }
        refreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        refreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pull_listView);
        myRedPackgetsAdapter = new MyRedPackgetsAdapter(R.layout.item_my_redpackget, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(myRedPackgetsAdapter);
    }

    private void initListener() {
        tvMoreTrends.setOnClickListener(this);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        tvStealStrategy.setOnClickListener(this);
        myRedPackgetsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StealMyRedpackgets.Data.RedDetails redDetails = (StealMyRedpackgets.Data.RedDetails) adapter.getItem(position);
                if (redDetails instanceof StealMyRedpackgets.Data.MyPackges) {
                    startActivity(new Intent(mContext, StealRedPackgDetailsActivity.class)
                            .putExtra("data",redDetails)
//                            .putExtra("merchant_id", redDetails.merchantId)
//                            .putExtra("merchant_name", redDetails.merchantName)
//                            .putExtra("merchant_image", redDetails.merchantImage)
                    );
                }
            }
        });


    }

    boolean isFirst = true;

    //首次进入时进行刷新操作
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isFirst = false;
                                    initDatas();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initDatas() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
        getData();
    }

    private void getData() {
            Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getMyRedPackgetsResult("stealbonus/my_redbonus"
                    , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext)
                    , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StealMyRedpackgets>() {
                        @Override
                        public void onCompleted() {
                            refreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onNext(StealMyRedpackgets stealMyRedpackgets) {
                            setData(stealMyRedpackgets);
                        }
                    });
            addSubscription(subscription);
    }

    private void setData(StealMyRedpackgets stealMyRedpackgets) {
        list.clear();
        int myRedPackgetSize = stealMyRedpackgets.data.list.size();
        list.addAll(stealMyRedpackgets.data.list);
        list.addAll(stealMyRedpackgets.data.empList);
        //设置我的奖励的个数
        myRedPackgetsAdapter.setDateTypes(myRedPackgetSize);
        //这里进行数据的设置
        if (myRedPackgetsAdapter.getHeaderLayoutCount() <= 0) {
            myRedPackgetsAdapter.addHeaderView(headerView);
        }
        myRedPackgetsAdapter.setNewData(list);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_steal_redpacage_strategy:
                //偷奖励策略
                break;
            case R.id.tv_more_trends:
                //更多动态
                startActivity(new Intent(mContext, StealRedPackgeTrendsActivity.class));

                break;


        }

    }
}
