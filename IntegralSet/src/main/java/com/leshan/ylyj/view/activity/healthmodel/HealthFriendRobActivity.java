package com.leshan.ylyj.view.activity.healthmodel;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.leshan.ylyj.adapter.RobAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.HealthPresenter;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.StatusBarUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.HealthFriendRobAllCountBean;
import rxfamily.entity.HealthFriendRobListBean;


/**
 * 好友抢果
 *
 * @author Ray_L_Pain
 */
public class HealthFriendRobActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private RobAdapter adapter;
    private LinearLayoutManager manager;
    private ImageView ivBack;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View footView, errorView, headView;
    private TextView tvRefresh, tvCountAll, tvCountToday, tvTitleRV;
    private int page = 0;
    private boolean getFirstPageDataFlag = true;

    private HealthPresenter presenter, tPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rob_health);
        presenter = new HealthPresenter(mContext, this, 0);
        tPresenter = new HealthPresenter(mContext, this, 1);

        initView();
        initListener();
        getFirstPageData();

        StatusBarUtils.setStatusBarColor(HealthFriendRobActivity.this, R.color.color_act_green, true);
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        if (headView == null) {
            headView = View.inflate(mContext, R.layout.head_friend_rob, null);
            tvCountAll = headView.findViewById(R.id.tv_count_all);
            tvCountToday = headView.findViewById(R.id.tv_count_today);
            tvTitleRV = headView.findViewById(R.id.tv_rv_title);
        }

        if (footView == null) {
            footView = View.inflate(mContext, R.layout.foot_friend_rob, null);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFirstPageData();
                }
            });
        }

        if (adapter == null) {
            adapter = new RobAdapter(R.layout.item_rob_health_layout);
        }
        if (manager == null) {
            manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
    }

    private String applesNum;
    private int touchPosition;
    private HealthFriendRobListBean.DataBean.ListBean notifityBean;

    @Override
    protected void initListener() {
        RxView.clicks(ivBack)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        adapter.setOnLoadMoreListener(this, recyclerView);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                notifityBean = (HealthFriendRobListBean.DataBean.ListBean) adapter.getItem(position);
                applesNum = notifityBean.apples_num;
                touchPosition = position;

                int i = view.getId();
                if (i == R.id.already_rob_tv) {

                    Logger.i("2018年1月17日 16:07:09-开始-" + touchPosition);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                    tPresenter.healRobFriend(notifityBean.user_id, applesNum);
                } else if (i == R.id.icon_iv) {

                    Logger.i("2018年1月17日 16:07:09-" + touchPosition);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
            }
        });
    }

    @Override
    protected void initData() {
    }

    private void getFirstPageData() {
        Logger.i("ray---666");
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        page = 0;
        getData();
    }

    private void getNextPageData() {
        Logger.i("ray---777");
        page++;
        presenter.getFriendRobList(String.valueOf(page), String.valueOf(Constants.PAGE_COUNT));
    }

    private void getData() {
        presenter.getFriendRobAllCount();
        presenter.getFriendRobList(String.valueOf(page), String.valueOf(Constants.PAGE_COUNT));
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onErrors(int flag, Throwable e) {
        if (1 == flag) {
            showToast(e.getMessage());
        } else {
            Logger.i("走了这里111");
            if (page == 0) {
                if (getFirstPageDataFlag) {
                    initHead();
                    adapter.setNewData(list);
                    adapter.loadMoreEnd(true);
                    initFoot();
                    getFirstPageDataFlag = false;
                    Logger.i("走了这里222");
                }
            } else if (page > 0) {
                page--;
                adapter.loadMoreFail();
                Logger.i("走了这里333");
            }
        }

        netRequestEnd();
    }

    private String totalCountStr, todayCountStr;
    private boolean flag1 = false;
    private boolean flag3 = false;
    private boolean flag4 = false;
    ArrayList<HealthFriendRobListBean.DataBean.ListBean> list = new ArrayList<>();

    @Override
    public void onNext(BaseEntity baseEntity) {
        if (baseEntity instanceof HealthFriendRobAllCountBean) {
            HealthFriendRobAllCountBean allBean = (HealthFriendRobAllCountBean) baseEntity;

            totalCountStr = allBean.data.lootApple;
            if (null == totalCountStr) {
                totalCountStr = "0";
            }
            tvCountAll.setText(totalCountStr);

            todayCountStr = allBean.data.lootappleDay;
            if (null == todayCountStr) {
                todayCountStr = "0";
            }
            tvCountToday.setText(todayCountStr);

            flag1 = true;
        } else if (baseEntity instanceof HealthFriendRobListBean) {
            HealthFriendRobListBean listBean = (HealthFriendRobListBean) baseEntity;

            if (getFirstPageDataFlag) {
                initHead();
                getFirstPageDataFlag = false;
            }

            ArrayList<HealthFriendRobListBean.DataBean.ListBean> newList = listBean.data.list;
            if (page <= 0) {
                if (null == newList || newList.size() <= 0) {
                    adapter.loadMoreEnd();
                    initFoot();
                } else {
                    adapter.removeAllFooterView();
                    adapter.setNewData(newList);
                    if (newList.size() >= Constants.PAGE_COUNT) {
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
            } else {
                if (newList.size() >= Constants.PAGE_COUNT) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }
                adapter.addData(newList);
            }

            flag3 = true;
        } else {
            Toast toast = Toast.makeText(mContext, "健康果 +" + applesNum , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Logger.i("2018年1月17日 16:07:09-完毕-" + touchPosition);
            int newCount = NumberFormat.convertToInt(applesNum, 0);
            int totalCount = NumberFormat.convertToInt(totalCountStr, 0);
            int todayCount = NumberFormat.convertToInt(todayCountStr, 0);

            totalCountStr = String.valueOf(newCount + totalCount);
            todayCountStr = String.valueOf(newCount + todayCount);

            tvCountAll.setText(totalCountStr);
            tvCountToday.setText(todayCountStr);


            Logger.i("2018年1月17日 16:07:09-之前-" + notifityBean.toString());
            notifityBean.state = "1";
            Logger.i("2018年1月17日 16:07:09-之后-" + notifityBean.toString());
            adapter.notifyItemChanged(touchPosition + 1, notifityBean);

            flag4 = true;
        }

        if ((flag1 && flag3) || flag4) {
            netRequestEnd();
        }
    }

    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    private void initHead() {
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
        tvTitleRV.setVisibility(View.VISIBLE);
    }

    private void initFoot() {
        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(footView);
        }
        tvTitleRV.setVisibility(View.GONE);
    }


    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }
}
