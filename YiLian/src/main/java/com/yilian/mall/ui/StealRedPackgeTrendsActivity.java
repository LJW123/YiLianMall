package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MoreTrendsAdapter;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.Constants;

import com.yilian.networkingmodule.entity.StealMoreTrends;
import com.yilian.networkingmodule.entity.StealRedPackgesPushStatus;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZYH on 2017/12/5 0005.
 */

public class StealRedPackgeTrendsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private int page;
    private ImageView v3Back;
    private TextView v3Title;
    private ImageView tvRight;
    private FrameLayout mLinearLayout;
    private MoreTrendsAdapter mAdapter;
    private CheckBox mCheckBox;
    private UmengDialog mShareDialog;
    private String share_title;
    private String shareImg;
    private String share_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stealpackges_trends);
        initView();
        initListner();
        getPushStatus();
    }


    private void initListner() {
        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        mySwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();

            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextData();
            }
        }, mRecyclerView);

    }

    private void initView() {
        //设置状态栏的颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_status_redpackget));
        mySwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.pull_down_refreshlayout);
        mySwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mySwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));

        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.baijiantou);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("动态");
        v3Title.setTextColor(getResources().getColor(R.color.white));
        tvRight = (ImageView) findViewById(R.id.v3Share);
        tvRight.setImageResource(R.mipmap.icon_share_red_packge);
        tvRight.setVisibility(View.VISIBLE);
        mLinearLayout = (FrameLayout) findViewById(R.id.v3Layout);
        mLinearLayout.setBackgroundColor(getResources().getColor(R.color.color_status_redpackget));

        mRecyclerView = (RecyclerView) findViewById(R.id.pull_up_recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MoreTrendsAdapter(R.layout.item_more_trends, true);
        View headView = View.inflate(mContext, R.layout.header_more_trends, null);
        mAdapter.addHeaderView(headView);
        mRecyclerView.setAdapter(mAdapter);
        mCheckBox = (CheckBox) headView.findViewById(R.id.checkbox_notice);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //设置是否发送通知
                if (b) {
                    setStealRedPackgesPushStatus(1);

                } else {
                    setStealRedPackgesPushStatus(0);
                }

            }
        });
    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst && hasFocus) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (null != mContext) {
                            runOnUiThread(new Runnable() {
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
    }

    private void initData() {
        if (null != mySwipeRefreshLayout) {
            mySwipeRefreshLayout.setRefreshing(true);
        }
        getFirstData();
    }
    @SuppressWarnings("unchecked")
    private void setStealRedPackgesPushStatus(int type) {
         Subscription subscription= RetrofitUtils3.getRetrofitService(mContext).setStealRedPackgesPushStatus("stealbonus/set_push", type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResultBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(HttpResultBean stealRedPackgesPushStatus) {
                            showToast(stealRedPackgesPushStatus.msg);

                        }
                    });
            addSubscription(subscription);
    }

    @SuppressWarnings("unchecked")
    private void getPushStatus() {
         Subscription subscription= RetrofitUtils3.getRetrofitService(mContext).getStealRedPackgesPushStatus("stealbonus/get_push_status")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StealRedPackgesPushStatus>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(StealRedPackgesPushStatus stealRedPackgesPushStatus) {
                            String type = stealRedPackgesPushStatus.type;
                            if ("0".equals(type)) {
                                mCheckBox.setChecked(false);
                            } else {
                                mCheckBox.setChecked(true);
                            }

                        }
                    });
         addSubscription(subscription);
    }
    @SuppressWarnings("unchecked")
    private void getData() {
          Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getStealRedPackgesMoreTrends("stealbonus/my_dynamic", page
                    , Constants.PAGE_COUNT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StealMoreTrends>() {
                        @Override
                        public void onCompleted() {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            mySwipeRefreshLayout.setRefreshing(false);
                            mAdapter.loadMoreFail();

                        }

                        @Override
                        public void onNext(StealMoreTrends myRedpackgetsDetails) {
                            setData(myRedpackgetsDetails);
                        }
                    });

          addSubscription(subscription);
    }

    private void setData(StealMoreTrends myRedpackgetsDetails) {
        List<StealMoreTrends.Data> list = myRedpackgetsDetails.data;
        showToast(myRedpackgetsDetails.msg);
        if (page <= 0) {
            if (null == list || list.size() <= 0) {
                mAdapter.loadMoreEnd();
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

    private void getFirstData() {
        page = 0;
        getData();
    }

    private void getNextData() {
        page++;
        getData();
    }

    /**
     * 分享
     */
    public void umShare() {
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            shareImg,
                            share_url).share();
                }
            });

        }
        mShareDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Share:
                umShare();
                break;

        }
    }
}
