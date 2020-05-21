package com.yilian.mall.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.RedDateGridAdapter;
import com.yilian.mall.adapter.RedPacketFragmentAdapter;
import com.yilian.mall.ui.OpenRedPackageActivity;
import com.yilian.mall.ui.RedExplainDialog;
import com.yilian.mall.ui.RedIndexDialog;
import com.yilian.mall.ui.RedPacketDialog2;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.NoDoubleClickListener;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.event.RefreshRedPacketStatus;
import com.yilian.networkingmodule.entity.RedPacketDateEntity;
import com.yilian.networkingmodule.entity.RedPacketFragmentHeadEntity;
import com.yilian.networkingmodule.entity.RedPacketOneKeyRemoveEntity;
import com.yilian.networkingmodule.entity.RemoveRedRecordEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 奖励的fragment
 *
 * @author Ray_L_Pain
 * @date 2017/11/22 0022
 */

public class RedPacketFragment extends JPBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, RefreshRedPacketStatus {
    public boolean isRefresh = false;
    /**
     * 获取用户奖券
     */
    public float userIntegral;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.main_layout)
    RelativeLayout mainLayout;
    @ViewInject(R.id.not_login_layout)
    LinearLayout notLoginLayout;
    @ViewInject(R.id.tv_no_data)
    TextView tvNotLogin;
    private View rootView, emptyView, errorView, headerView, footerView;
    private FrameLayout frameLayout;
    private TextView tvRefresh, tvFrameTitle, tvFrameMoney, tvFrameDefeat, tvFrameBtn, tvRedIndex, tvAllDay, tvMonth, tvNotGet, tvGetBtn;
    private ImageView ivLeft, ivRight;
    private Banner banner;
    private NoScrollGridView dateGridView;
    private LinearLayout getAllLayout;
    private int page = 0;
    private RedPacketFragmentAdapter adapter;
    private ArrayList<RemoveRedRecordEntity.DataBean> list = new ArrayList<>();
    //页面第一次加载标识
    private boolean isFirst = true;
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    private int screenWidth;
    private RedDateGridAdapter dateGridAdapter;
    private int year, month;
    private int getAllRedNum;
    private String getAllRedMoney;
    private int getAllRedNotNum;
    private String getAllRedNotMoney;
    /**
     * 奖励分享
     */
    private UmengDialog mShareDialog;
    private String shareTitle, shareContent, shareImg, shareUrl;
    /**
     * 一键拆取奖励
     */
    private String shareRedMoney, shareRedNum, shareRedTime, shareRedPhoto, shareRedName;
    //    实际的分享数据
    private String inFactShareRedMoney, inFactShareRedNum, inFactShareRedTime, inFactShareRedPhoto, inFactShareRedName;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isFirst) {
            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);

                        if (null != getActivity()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (swipeRefreshLayout != null) {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                    isFirst = false;
                                    getFirstPageData();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_red_packet, null);
        }
        ViewUtils.inject(this, rootView);
        initView();
        initListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_RED_FRAGMENT, mContext, false);

        if (isRefresh) {
            Logger.i("2017年11月28日 15:38:18-走了onResume");
            getFirstPageData();
            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, isRefresh, mContext);
        }
    }

    private void initView() {
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                FragmentActivity activity = getActivity();
                //刷新奖励标识
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);
                if (activity != null) {
                    activity.finish();
                } else {
                    AppManager.getInstance().killActivity(OpenRedPackageActivity.class);
                }
            }
        });

        screenWidth = ScreenUtils.getScreenWidth(mContext);

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RedExplainDialog.class));
            }
        });

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                }
            });
        }

        if (footerView == null) {
            footerView = View.inflate(mContext, R.layout.red_foot, null);
        }

        if (headerView == null) {
            headerView = View.inflate(mContext, R.layout.header_red_packet, null);

            frameLayout = (FrameLayout) headerView.findViewById(R.id.frame_layout);
            tvFrameTitle = (TextView) headerView.findViewById(R.id.tv_frame_title);
            tvFrameMoney = (TextView) headerView.findViewById(R.id.tv_frame_money);
            tvFrameDefeat = (TextView) headerView.findViewById(R.id.tv_defeat);
            tvFrameBtn = (TextView) headerView.findViewById(R.id.tv_frame_btn);
            banner = (Banner) headerView.findViewById(R.id.banner);
            tvRedIndex = (TextView) headerView.findViewById(R.id.tv_red_index);

            ivLeft = (ImageView) headerView.findViewById(R.id.iv_left);
            ivRight = (ImageView) headerView.findViewById(R.id.iv_right);
            tvMonth = (TextView) headerView.findViewById(R.id.tv_month);
            tvAllDay = (TextView) headerView.findViewById(R.id.tv_all_day);
            dateGridView = (NoScrollGridView) headerView.findViewById(R.id.grid_view_date);
            dateGridView.setFocusable(false);
            dateGridView.setFocusableInTouchMode(false);

            getAllLayout = (LinearLayout) headerView.findViewById(R.id.layout_get);
            tvNotGet = (TextView) headerView.findViewById(R.id.tv_not_get);
            tvGetBtn = (TextView) headerView.findViewById(R.id.tv_get_btn);

            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
            params1.width = screenWidth;
            params1.height = (int) ((794 * 0.1) * screenWidth / (1080 * 0.1));

            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) tvFrameTitle.getLayoutParams();
            FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) tvFrameMoney.getLayoutParams();

            FrameLayout.LayoutParams params4 = (FrameLayout.LayoutParams) tvFrameBtn.getLayoutParams();

            LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) banner.getLayoutParams();
//            params5.height = (int) ((306 * 0.1) * screenWidth / (1012 * 0.1));
            params5.height = (screenWidth * 100) / 345;
            Logger.i("banner width:" + params5.width + "  height:" + params5.height);
            FrameLayout.LayoutParams params6 = (FrameLayout.LayoutParams) tvFrameDefeat.getLayoutParams();

            switch (screenWidth) {
                case 720:
                    params2.setMargins(0, 167, 0, 0);
                    params3.setMargins(0, 233, 0, 0);
                    params6.setMargins(0, 320, 0, 0);
                    params4.setMargins(0, 367, 0, 0);
                    break;
                case 1080:
                    params2.setMargins(0, 250, 0, 0);
                    params3.setMargins(0, 350, 0, 0);
                    params6.setMargins(0, 480, 0, 0);
                    params4.setMargins(0, 550, 0, 0);
                    break;
                case 1440:
                    params2.setMargins(0, 333, 0, 0);
                    params3.setMargins(0, 467, 0, 0);
                    params6.setMargins(0, 640, 0, 0);
                    params4.setMargins(0, 733, 0, 0);
                    break;
                default:
                    break;
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new RedPacketFragmentAdapter(R.layout.item_red_packet_list, mAppCompatActivity);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));

        tvNotLogin.setText("请登录后查看");
        tvNotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
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
                refreshData();
            }
        });
    }

    @Override
    public void setOnRefreshRedPacketStatus() {
//         * 拆取奖励时，若奖励状态异常则刷新奖励数据
        getDateRecord(year, month);
    }

    /**
     * 获取某月的奖励数据
     *
     * @param year
     * @param month
     */
    private void getDateRecord(int year, int month) {
        Logger.i("ray-" + year);
        Logger.i("ray-" + month);
        RetrofitUtils2.getInstance(mContext).getRedDate(String.valueOf(year), String.valueOf(month), new Callback<RedPacketDateEntity>() {
            @Override
            public void onResponse(Call<RedPacketDateEntity> call, Response<RedPacketDateEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        RedPacketDateEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                getAllRedNum = entity.unClaimedNum;
                                getAllRedMoney = MoneyUtil.getLeXiangBiNoZero(entity.unClaimedAmount);
                                getAllRedNotNum = entity.unActivateNum;
                                getAllRedNotMoney = MoneyUtil.getLeXiangBiNoZero(entity.unActivateAmount);
                                userIntegral = Float.parseFloat(MoneyUtil.getLeXiangBiNoZero(entity.userIntegral));

                                tvMonth.setText(entity.monthStr);
                                tvAllDay.setText("当前已累计领取" + entity.claimedNum + "天");

                                int notGetNum = entity.unClaimedNum;
                                if (notGetNum > 4) {
                                    getAllLayout.setVisibility(View.VISIBLE);
                                    tvNotGet.setText("您有" + entity.unClaimedNum + "个未拆取的奖励");

                                    tvGetBtn.setOnClickListener(new NoDoubleClickListener() {
                                        @Override
                                        public void onNoDoubleClick(View v) {
                                            if (getAllRedNotNum > 0) {
                                                AlertDialog builder = new AlertDialog.Builder(mAppCompatActivity).create();
                                                builder.setMessage("您有" + String.valueOf(getAllRedNum) + "个未拆奖励，共" + getAllRedMoney + "。其中包含" + String.valueOf(getAllRedNotNum) + "个失效奖励，继续一键拆取需要消耗" +
                                                        getAllRedNotMoney + "奖券激活，是否继续一键拆取？");
                                                builder.setButton(DialogInterface.BUTTON_NEGATIVE, "再想想", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.setButton(DialogInterface.BUTTON_POSITIVE, "继续", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (userIntegral < Float.parseFloat(getAllRedNotMoney)) {
                                                            showIntegralLack();
                                                        } else {
                                                            getAllPacket("2");
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.show();
                                                builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mAppCompatActivity.getResources().getColor(R.color.color_red));
                                                builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mAppCompatActivity.getResources().getColor(R.color.color_333));
                                            } else {
                                                getAllPacket("1");
                                            }
                                        }
                                    });
                                } else {
                                    getAllLayout.setVisibility(View.INVISIBLE);
                                }

                                ArrayList<RedPacketDateEntity.ListArrBean> newList = entity.listArr;
                                int firstSize = newList.size();
                                Logger.i("2017年11月24日 10:57:26-" + firstSize);
                                RedPacketDateEntity.ListArrBean firstBean = newList.get(0);
                                String fakerTime = firstBean.packetTime;
                                String fakerExist = "2";
                                RedPacketDateEntity.ListArrBean fakeBean = new RedPacketDateEntity.ListArrBean(fakerTime, fakerExist);
                                Logger.i("2017年11月24日 10:57:26-" + fakeBean.toString());
                                String date = firstBean.packetTime;
                                int week = DateUtils.getWeekByStamp(Long.parseLong(date) * 1000) - 1;
                                Logger.i("2017年11月24日 10:57:26-week-" + date);
                                Logger.i("2017年11月24日 10:57:26-week-" + week);
                                switch (week) {
                                    case 1:
                                        Logger.i("2017年11月24日 10:57:26-" + "周一");
                                        break;
                                    case 2:
                                        Logger.i("2017年11月24日 10:57:26-" + "周二");
                                        newList.add(0, fakeBean);
                                        break;
                                    case 3:
                                        Logger.i("2017年11月24日 10:57:26-" + "周三");
                                        newList.add(0, fakeBean);
                                        newList.add(1, fakeBean);
                                        break;
                                    case 4:
                                        Logger.i("2017年11月24日 10:57:26-" + "周四");
                                        newList.add(0, fakeBean);
                                        newList.add(1, fakeBean);
                                        newList.add(2, fakeBean);
                                        break;
                                    case 5:
                                        Logger.i("2017年11月24日 10:57:26-" + "周五");
                                        newList.add(0, fakeBean);
                                        newList.add(1, fakeBean);
                                        newList.add(2, fakeBean);
                                        newList.add(3, fakeBean);
                                        break;
                                    case 6:
                                        Logger.i("2017年11月24日 10:57:26-" + "周六");
                                        newList.add(0, fakeBean);
                                        newList.add(1, fakeBean);
                                        newList.add(2, fakeBean);
                                        newList.add(3, fakeBean);
                                        newList.add(4, fakeBean);
                                        break;
                                    case 0:
                                        Logger.i("2017年11月24日 10:57:26-" + "周日");
                                        newList.add(0, fakeBean);
                                        newList.add(1, fakeBean);
                                        newList.add(2, fakeBean);
                                        newList.add(3, fakeBean);
                                        newList.add(4, fakeBean);
                                        newList.add(5, fakeBean);
                                        break;
                                    default:
                                        break;
                                }
                                int lastSize = newList.size();
                                int diffSize = lastSize - firstSize - 1;
                                Logger.i("2017年11月24日 10:57:26-" + newList.size());
                                Logger.i("2017年11月24日 10:57:26-" + newList.toString());
                                Logger.i("2017年11月24日 10:57:26-" + diffSize);

                                dateGridView.setVisibility(View.VISIBLE);
                                dateGridAdapter = new RedDateGridAdapter(mAppCompatActivity, newList, diffSize, userIntegral, RedPacketFragment.this);
                                dateGridView.setAdapter(dateGridAdapter);
                                break;
                            default:
                                break;
                        }
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RedPacketDateEntity> call, Throwable t) {
                dateGridView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 奖券不足时dialog
     */
    private void showIntegralLack() {
        AlertDialog builder = new AlertDialog.Builder(mAppCompatActivity).create();
        builder.setMessage("当前奖券不足，无法激活所有失效奖励，是否优先拆取未失效奖励？");
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getAllPacket("1");
                dialog.dismiss();
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mAppCompatActivity.getResources().getColor(R.color.color_red));
        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mAppCompatActivity.getResources().getColor(R.color.color_333));
    }

    private void getAllPacket(String type) {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).oneKeyRemoveRedPacket(type, new Callback<RedPacketOneKeyRemoveEntity>() {
            @Override
            public void onResponse(Call<RedPacketOneKeyRemoveEntity> call, Response<RedPacketOneKeyRemoveEntity> response) {
                stopMyDialog();
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        RedPacketOneKeyRemoveEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                shareRedMoney = entity.packetAmount;
                                shareRedNum = entity.packetNum;
                                shareRedTime = entity.serverTime;

                                Intent intent = new Intent(getActivity(), RedPacketDialog2.class);
                                intent.putExtra("red_id", "");
                                intent.putExtra("red_money", MoneyUtil.getLeXiangBiNoZero(shareRedMoney));
                                intent.putExtra("red_from", "all");
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RedPacketOneKeyRemoveEntity> call, Throwable t) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void refreshData() {
        getFirstPageDataFlag = true;
        getFirstPageData();
        adapter.setEnableLoadMore(false);
    }

    private void getFirstPageData() {
        startMyDialog();
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).removeRedPacketReocrds(String.valueOf(page), "30", new Callback<RemoveRedRecordEntity>() {
            @Override
            public void onResponse(Call<RemoveRedRecordEntity> call, Response<RemoveRedRecordEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    switch (body.code) {
                        case -4:
                        case -23:
                            Toast.makeText(mContext, "登录状态失效,请重新登录.", Toast.LENGTH_SHORT).show();
                            PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, false, mContext);
                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
                            Intent intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            startActivity(intent);

                            mainLayout.setVisibility(View.GONE);
                            notLoginLayout.setVisibility(View.VISIBLE);
                            break;
                        default:
                            mainLayout.setVisibility(View.VISIBLE);
                            notLoginLayout.setVisibility(View.GONE);
                            break;
                    }
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        RemoveRedRecordEntity entity = response.body();
                        switch (response.body().code) {
                            case 1:
                                initHeaderView();
                                ArrayList<RemoveRedRecordEntity.DataBean> newList = entity.data;
                                Logger.i("onResume:" + newList.size());
                                if (newList != null && newList.size() > 0) {
                                    if (page > 0) {
                                        adapter.addData(newList);
                                    } else {
                                        getFirstPageDataFlag = false;
                                        adapter.setNewData(newList);
                                    }
                                    if (newList.size() < Constants.PAGE_COUNT) {
                                        adapter.loadMoreEnd();
                                    } else {
                                        adapter.loadMoreComplete();
                                    }
                                } else {
                                    if (page == 0) {
                                        adapter.setNewData(newList);
                                        adapter.loadMoreEnd();
                                        initFootView();
                                    }
                                }
                                break;
                            default:
                                adapter.setEmptyView(emptyView);
                                break;
                        }
                    }
                }
                netRequestEnd();
            }

            @Override
            public void onFailure(Call<RemoveRedRecordEntity> call, Throwable t) {
                mainLayout.setVisibility(View.VISIBLE);
                notLoginLayout.setVisibility(View.GONE);
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
                showToast(R.string.system_busy);
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
        stopMyDialog();
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

    private void initFootView() {
        if (adapter.getFooterLayoutCount() > 0) {
            adapter.removeAllFooterView();
        }
        adapter.addFooterView(footerView);
        adapter.notifyDataSetChanged();
    }

    private void initHeaderView() {
        if (adapter.getFooterLayoutCount() > 0) {
            adapter.removeAllFooterView();
        }
        if (adapter.getHeaderLayoutCount() > 0) {
            adapter.removeAllHeaderView();
        }
        adapter.addHeaderView(headerView);
        adapter.notifyDataSetChanged();

        /**
         * 头部数据
         */
        getHeadData();

        /**
         * 日历数据
         */
        year = DateUtils.getYear();
        month = DateUtils.getMonth();
        getDateRecord(year, month);

        tvRedIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RedIndexDialog.class);
                startActivity(intent);
            }
        });

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month == 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                swipeRefreshLayout.setRefreshing(true);
                getDateRecord(year, month);
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month == 12) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                swipeRefreshLayout.setRefreshing(true);
                getDateRecord(year, month);
            }
        });
    }

    private void getHeadData() {
        RetrofitUtils2.getInstance(mContext).getRedHead(new Callback<RedPacketFragmentHeadEntity>() {
            @Override
            public void onResponse(Call<RedPacketFragmentHeadEntity> call, Response<RedPacketFragmentHeadEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        RedPacketFragmentHeadEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
//                                分享内容
                                RedPacketFragmentHeadEntity.ShareBean share = entity.share;
                                shareContent = share.content;
                                shareImg = share.imgUrl;
                                shareTitle = share.title;
                                shareUrl = share.url;
                                inFactShareRedTime = entity.packetInfo.applyAt;
                                inFactShareRedMoney = entity.packetInfo.amount;
                                inFactShareRedName = entity.name;
                                inFactShareRedPhoto = entity.photo;
                                if (TextUtils.isEmpty(inFactShareRedPhoto)) {
                                    inFactShareRedPhoto = "https://img.yilian.lefenmall.com/app/images/touxiang.png";
                                }


                                tvFrameTitle.setVisibility(View.VISIBLE);
                                tvFrameTitle.setText("今日奖励");
                                tvFrameMoney.setVisibility(View.VISIBLE);
                                tvFrameBtn.setVisibility(View.VISIBLE);
                                if ("0".equals(entity.packetExist)) {
                                    tvFrameMoney.setText("???");
                                    tvFrameDefeat.setVisibility(View.GONE);
                                    tvFrameBtn.setText("今日无奖励");
                                    tvFrameBtn.setBackgroundResource(R.mipmap.red_packet_fragment_btn);
                                    tvFrameBtn.setAlpha(0.6f);
                                } else {
                                    String status = entity.packetInfo.status;
                                    if (!TextUtils.isEmpty(status)) {
                                        switch (status) {
                                            case "0":
                                                tvFrameMoney.setText("???");
                                                tvFrameDefeat.setVisibility(View.GONE);
                                                tvFrameBtn.setBackgroundResource(R.mipmap.red_packet_fragment_btn);
                                                tvFrameBtn.setText("立刻拆奖励");
                                                tvFrameBtn.setAlpha(1);
                                                break;
                                            case "1":
                                                tvFrameMoney.setText(MoneyUtil.getLeXiangBiNoZero(entity.packetInfo.amount));
                                                tvFrameDefeat.setVisibility(View.VISIBLE);
                                                tvFrameDefeat.setText(Html.fromHtml("<font color=\"#CA6400\"><small>奖励打败了全国 </small></font><font color=\"#F72D42\"><big>" + entity.packetInfo.percent + "%" + "</big></font><font color=\"#CA6400\"><small>的益粉</small></font>"));
                                                tvFrameBtn.setBackgroundResource(R.mipmap.red_packet_fragment_btn2);
                                                tvFrameBtn.setText("炫耀一下");
                                                tvFrameBtn.setAlpha(1);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                                ArrayList<RedPacketFragmentHeadEntity.Banner> bannerList = entity.banner;
                                banner.setVisibility(View.VISIBLE);
                                ArrayList<String> imageList = new ArrayList<String>();
                                int size = bannerList.size();
                                for (int i = 0; i < size; i++) {
                                    String image = bannerList.get(i).image;
                                    imageList.add(WebImageUtil.getInstance().getWebImageUrlNOSuffix(image));
                                }
                                banner.setImages(imageList)
                                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                                        .setImageLoader(new BannerViewGlideUtil())
                                        .setIndicatorGravity(BannerConfig.CENTER)
                                        .setOnBannerListener(new OnBannerListener() {
                                            @Override
                                            public void OnBannerClick(int position) {
                                                RedPacketFragmentHeadEntity.Banner bean = bannerList.get(position);
                                                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(bean.type, bean.content);
                                            }
                                        }).start();

                                tvFrameBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (tvFrameBtn.getText().toString().trim()) {
                                            case "立刻拆奖励":
                                                Intent intent = new Intent(getActivity(), RedPacketDialog2.class);
                                                intent.putExtra("red_id", entity.packetInfo.id);
                                                intent.putExtra("red_money", MoneyUtil.getLeXiangBiNoZero(entity.packetInfo.amount));
                                                intent.putExtra("red_from", "one");
                                                startActivity(intent);
                                                break;
                                            case "炫耀一下":
                                                shareRedPacket();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RedPacketFragmentHeadEntity> call, Throwable t) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void shareRedPacket() {
//        shareTitle = "快来拆奖励吧~";
//        shareContent = "我刚刚在益联益家拆得一个" + MoneyUtil.getLeXiangBiNoZero(inFactShareRedMoney) + "奖励，每天都有，快来拆开你的吧~";
//        shareImg = "https://img.yilian.lefenmall.com/app/images/chb.png";
//        shareUrl = Ip.getWechatURL(mContext) + "m/activity/sharePacket/sharePacket.php?gain_money=" + MoneyUtil.getLeXiangBiNoZero(inFactShareRedMoney) +
//                "&gain_time=" + com.yilian.luckypurchase.utils.DateUtils.timeStampToStr6(Long.parseLong(inFactShareRedTime)) +
//                "&user_img=" + WebImageUtil.getInstance().getWebImageUrlWithSuffix(inFactShareRedPhoto) + "&user_name=" + inFactShareRedName + "&sign=" + PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE, mContext);
        Logger.i("2017年11月26日 12:30:55-" + shareRedMoney);
        Logger.i("2017年11月26日 12:30:55-" + shareRedTime);
        Logger.i("2017年11月26日 12:30:55-" + inFactShareRedName);
        Logger.i("2017年11月26日 12:30:55-" + WebImageUtil.getInstance().getWebImageUrlWithSuffix(inFactShareRedPhoto));
        Logger.i("2017年11月26日 12:30:55-" + shareImg);
        Logger.i("2017年11月26日 12:30:55-" + shareUrl);
        if (TextUtils.isEmpty(shareUrl)) {
            getHeadData();
            showToast(R.string.service_exception);
            return;
        }
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mAppCompatActivity, AnimationUtils.loadAnimation(getActivity(), R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mAppCompatActivity,
                            ((IconModel) arg4).getType(),
                            shareTitle,
                            shareContent,
                            shareImg,
                            shareUrl,
                            Constants.RED_SHARE)
                            .share();
                }
            });
        }
//        依附的activity对象被回收时会报错，目前依附的activity使用存储下来的变量，暂未发现崩溃，以防万一添加catch抓取
        try {
            mShareDialog.show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
