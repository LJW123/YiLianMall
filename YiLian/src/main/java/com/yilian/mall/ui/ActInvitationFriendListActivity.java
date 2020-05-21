package com.yilian.mall.ui;

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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActInvitationFriendListAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.ActInventSubMemberListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  运营活动-打卡-邀请好友-查看详情
 * @author Ray_L_Pain
 * @date 2017/12/16 0016
 */

public class ActInvitationFriendListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.iv_right)
    ImageView ivMore;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private View emptyView, errorView;
    private TextView tvRefresh;


    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    private int page = 0;
    private ActInvitationFriendListAdapter adapter;
    private ArrayList<ActInventSubMemberListEntity.DataBean> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_invitation_friend_list);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("好友列表");
        ivMore.setImageResource(R.mipmap.merchant_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                getFirstPageData();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ActInvitationFriendListAdapter(R.layout.item_act_invitation_friend_list);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
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

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).actGetSubMember(String.valueOf(page), "30", new Callback<ActInventSubMemberListEntity>() {
            @Override
            public void onResponse(Call<ActInventSubMemberListEntity> call, Response<ActInventSubMemberListEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        ActInventSubMemberListEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                ArrayList<ActInventSubMemberListEntity.DataBean> newList = entity.data;
                                if (newList.size() > 0 && newList != null) {
                                    if (getFirstPageDataFlag) {
                                        adapter.setNewData(newList);
                                        getFirstPageDataFlag = false;
                                        list.clear();
                                    } else {
                                        adapter.addData(newList);
                                    }
                                    list.addAll(newList);

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
            public void onFailure(Call<ActInventSubMemberListEntity> call, Throwable t) {
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

    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.iv_right);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = null;
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent = new Intent(mContext, InformationActivity.class);
                        } else {
                            intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                        }
                        break;
                    case ITEM2:
                        intent = new Intent(mContext, JPMainActivity.class);
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent = new Intent(mContext, JPMainActivity.class);
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
