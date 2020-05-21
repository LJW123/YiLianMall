package com.yilian.mall.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActClockRankingAdapter;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.ActClockRankingEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 好友打卡排名
 * Created by ZYH on 2017/12/14 0014.
 */

public class ActClockRankActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private MySwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private int pager=0;
    private int id;
    private ActClockRankingAdapter mAdapter;
    private List<ActClockRankingEntity.Data> testData=new ArrayList<>();
    private ImageView v3Share;
    private View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_clock_rank);

        initView();
        iniListner();
        initData();
    }

    private void iniListner() {
        findViewById(R.id.v3Back).setOnClickListener(this);
        v3Share.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPagerData();
            }
        },mRecycleView);
    }

    private void initView() {
        id=getIntent().getIntExtra("id",0);
        mRefreshLayout= (MySwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mRecycleView= (RecyclerView) findViewById(R.id.pull_listView);
        mAdapter=new ActClockRankingAdapter(R.layout.item_act_clock_rank);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.setAdapter(mAdapter);
        TextView textView= (TextView) findViewById(R.id.v3Title);
        textView.setText("好友打卡排名");
        v3Share= (ImageView)findViewById(R.id.v3Share);
        v3Share.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Share.setVisibility(View.VISIBLE);
        StatusBarUtil.setColor(this, Color.parseColor("#282828"));
    }


    private void initData() {
        if (mRefreshLayout!=null){
            mRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
    }

    private void getFirstPageData() {
        pager=0;
        getData();
    }
    private void getNextPagerData(){
        pager++;
        getData();
    }

    private void getData() {
        RetrofitUtils3.getRetrofitService(mContext).getClockRanking("friend_clockin_record",id,pager, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActClockRankingEntity>() {
                    @Override
                    public void onCompleted() {
                        mRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        mAdapter.loadMoreFail();
                        if (pager > 0) {
                            pager--;
                        } else {
                            if (errorView == null) {
                                errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
                            }
                            errorView.findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getFirstPageData();
                                }
                            });
                            mAdapter.setEmptyView(errorView);
                        }


                    }

                    @Override
                    public void onNext(ActClockRankingEntity data) {
                        setData(data);
                    }
                });

    }
    private void setData(ActClockRankingEntity data) {
        List<ActClockRankingEntity.Data> list = data.list;
        if (pager <= 0) {
            if (null == list || list.size() <= 0) {
                mAdapter.loadMoreEnd();
                //无数据界面
                mAdapter.setEmptyView(R.layout.library_module_no_data);
            } else {
                mAdapter.setNewData(list);//添加新的数据
                mAdapter.loadMoreComplete();//加载完成，上拉可以加更多
            }
        } else {
            if (list.size() >= Constants.PAGE_COUNT) {//每页30条数据
                mAdapter.loadMoreComplete();//加载完成，上拉可以加更多
            } else {
                mAdapter.loadMoreEnd();//没有更多的数据
            }
            mAdapter.addData(list);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Share:
                showMenu();
                break;
        }

    }
    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Share);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
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
