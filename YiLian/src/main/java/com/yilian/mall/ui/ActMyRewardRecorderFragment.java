package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActRewardRecorderAdapter;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.ActRewardRecorderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${zhaiyaohua} on 2017/12/28 0028.
 * @author zhaiyaohua
 */

public class ActMyRewardRecorderFragment extends BaseFragment {
    /**
     *type： 0--我的打赏  1---我的求赏
     */
    private String type="0";
    private int pager=0;

    private MySwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecylcerview;
    private ActRewardRecorderAdapter adapter;
    private View errorView;
    private TextView tvRefresh;
    private int SEEK_REWARD_OK=1;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate( R.layout.fragment_reward_recorder,container,false);
        initView(view);
        initListner();
        return view;

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
                                    initData();
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


    private void initListner() {
        swipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPagerData();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPagerDate();
            }
        },mRecylcerview);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ActRewardRecorderEntity.Data data= (ActRewardRecorderEntity.Data) adapter.getItem(position);
                if (null==data||data.status!=0){
                    return;
                }
                // 求赏
                double awardIntegral=data.awardIntegral/100;
                startActivityForResult(new Intent(mContext, ActSeekRewardDialogActivity.class)
                        .putExtra("name", data.name)
                        .putExtra("reward", formatReward(awardIntegral+""))
                        .putExtra("reward_id", data.recordId)
                        .putExtra("from",1),0);
                getActivity().overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
            }
        });


    }

    private void initView(View view) {
        swipeRefreshLayout= (MySwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mRecylcerview= (RecyclerView) view.findViewById(R.id.pull_listView);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext);
        mRecylcerview.setLayoutManager(manager);
        adapter=new ActRewardRecorderAdapter(R.layout.item_act_reward_recorder,type);
        mRecylcerview.setAdapter(adapter);
    }

    @Override
    protected void loadData() {

    }
    private void getRewardRecorder(){
        Subscription subscription=RetrofitUtils3.getRetrofitService(mContext)
                .getRewardRecorder("clockin/seek_reward_list",pager, Constants.PAGE_COUNT,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActRewardRecorderEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        adapter.loadMoreFail();
                        showToast(e.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                        if (errorView == null) {
                            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
                            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
                            tvRefresh.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeRefreshLayout.setRefreshing(true);
                                    getFirstPagerData();
                                }
                            });
                        }
                    }

                    @Override
                    public void onNext(ActRewardRecorderEntity recorderEntity) {
                        swipeRefreshLayout.setRefreshing(false);
                        setData(recorderEntity);
                    }
                });
        addSubscription(subscription);

    }

    private void getFirstPagerData(){
        pager=0;
        getRewardRecorder();
    }
    private void getNextPagerDate(){
        pager++;
        getRewardRecorder();
    }
    private void initData(){
        swipeRefreshLayout.setRefreshing(true);
        pager=0;
        getRewardRecorder();
    }

    private void setData(ActRewardRecorderEntity recorderEntity) {
        List<ActRewardRecorderEntity.Data> list=new ArrayList<>();
        if (recorderEntity!=null||recorderEntity.data!=null){
            list.addAll(recorderEntity.data);
        }
        if (pager<=0){
            if (null==list||list.size()==0){
                //放置空页面
                adapter.loadMoreEnd();
                adapter.setEmptyView(R.layout.library_module_no_data);
            }else {
                adapter.setNewData(list);
                adapter.loadMoreComplete();
            }
        }else {
            if (Constants.PAGE_COUNT<=list.size()){
                adapter.loadMoreComplete();
            }else {
                adapter.loadMoreEnd();
            }
            adapter.addData(list);
        }
    }

    public void setType(String type){
        this.type=type;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==SEEK_REWARD_OK){
          getFirstPagerData();
        }
    }
    private String formatReward(String reward) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        if (TextUtils.isEmpty(reward)) {
            return "0.00";
        } else {
            if (reward.contains(".")) {
                int index = reward.indexOf(".");
                //点在首位
                if (index == 0) {
                    reward = 0 + reward + 0;
                } else {
                    if (index == reward.length() - 1) {//末尾
                        reward = reward + 0;
                    }
                }
            }
            BigDecimal bd = new BigDecimal(reward);
            reward = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));
            return reward;
        }
    }
}
