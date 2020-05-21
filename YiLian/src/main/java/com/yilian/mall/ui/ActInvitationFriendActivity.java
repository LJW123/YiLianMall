package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActInvitationFriendAdapter;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.ActInventIntegralChangeListEntity;
import com.yilian.networkingmodule.entity.ActInventMyRcordEntity;
import com.yilian.networkingmodule.entity.ActKnowMoreEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 邀请好友赚领奖励
 *
 * @author LYQ
 */
public class ActInvitationFriendActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llRankingHead;
    private LinearLayout llDotTop;
    private LinearLayout llRankIngBottom;
    private LinearLayout llTitle;
    private int selectPosition = 0;
    private LinearLayout llDotBottom;

    /**
     * 下方列表的有关数据
     */
    private int page = 0;
    private ActInvitationFriendAdapter adapter;
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    private View emptyView, errorView, headView;
    private TextView tvRefresh;
    private RecyclerView.LayoutManager mManager;
    private boolean isFirstGetShare = true;
    private ActKnowMoreEntity knowMoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_invitation_friend);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                initHeadLocation();
                onPositionListener();
            }
        });

        adapter.setOnLoadMoreListener(this, recyclerView);
    }

    private void initHeadLocation() {
        int[] rankBottomLocation = new int[2];
        llRankIngBottom.getLocationInWindow(rankBottomLocation);
        int heightLocation = rankBottomLocation[1];
        int height = llTitle.getHeight() + com.yilian.mylibrary.StatusBarUtils.getStatusBarHeight(mContext);

        if (height >= heightLocation) {
            llRankingHead.setVisibility(View.VISIBLE);
        } else {
            llRankingHead.setVisibility(View.GONE);
        }
    }

    private void initView() {
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("邀请好友赚收益");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.icon_act_share);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Share.setVisibility(View.VISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        llRankingHead = (LinearLayout) findViewById(R.id.ll_ranking_head);
        llDotTop = (LinearLayout) findViewById(R.id.ll_dot_top);

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
        headView = View.inflate(mContext, R.layout.item_act_invitation_friend_head, null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ActInvitationFriendAdapter(R.layout.item_act_invent_record_list);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));

        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
        v3Share.setOnClickListener(this);
        initFirstHeadView();
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
                            getShareUrl();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 头部需要的三个数据
     */
    String memberCountStr = null;
    String integralStr = null;
    float myRankStr = 0;

    private void getHeadData() {
        Logger.i("2017年12月15日 18:26:47-走了请求");
        RetrofitUtils2.getInstance(mContext).actInventMyRecord(new Callback<ActInventMyRcordEntity>() {
            @Override
            public void onResponse(Call<ActInventMyRcordEntity> call, Response<ActInventMyRcordEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        ActInventMyRcordEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                Logger.i("2017年12月15日 18:26:47-走了成功");
                                memberCountStr = entity.data.sub_member_count;
                                myRankStr = entity.data.my_rank;
                                integralStr = entity.data.get_integral;

                                tvFriendCount.setText(memberCountStr);
                                tvExploitsPer.setText((int) (myRankStr * 100) + "%");
                                tvAward.setText(MoneyUtil.getLeXiangBiNoZero(integralStr));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActInventMyRcordEntity> call, Throwable t) {
                Logger.i("2017年12月15日 18:26:47-走了失败");
                showToast(R.string.merchant_module_service_exception);
                memberCountStr = "0";
                myRankStr = 0;
                integralStr = "0";

                tvFriendCount.setText(memberCountStr);
                tvExploitsPer.setText(myRankStr + "%");
                tvAward.setText(MoneyUtil.getLeXiangBiNoZero(integralStr));
            }
        });
    }

    private void getListData() {
        RetrofitUtils2.getInstance(mContext).actInventIntegralChangeList(String.valueOf(page), "" + Constants.PAGE_COUNT_20, new Callback<ActInventIntegralChangeListEntity>() {
            @Override
            public void onResponse(Call<ActInventIntegralChangeListEntity> call, Response<ActInventIntegralChangeListEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        ActInventIntegralChangeListEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                if (page==0&&llDotBottom.getChildCount()==0&&llDotTop.getChildCount()==0) {
                                    //向上取整
                                    double count=Math.min(Double.parseDouble(entity.count),100.00)/Constants.PAGE_COUNT_20;
                                    count=Math.ceil(count);
                                    initDot(llDotBottom, count);
                                    initDot(llDotTop, count);
                                }
                                //每次刷新同时也刷新头部数据
                                if (getFirstPageDataFlag) {
                                    initHeadView();
                                    getFirstPageDataFlag = false;
                                }
                                if (page <= 0) {
                                    if (null == entity.data || entity.data.size() <= 0) {
                                        adapter.loadMoreEnd();
                                        //无数据 TODO 放置无数据页面
                                        adapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                                    } else {
                                        adapter.setNewData(entity.data);
                                        adapter.loadMoreComplete();
                                    }
                                } else {
                                    if (entity.data.size() >= Constants.PAGE_COUNT_20) {
                                        adapter.loadMoreComplete();
                                    } else {
                                        adapter.loadMoreEnd();
                                    }
                                    adapter.addData(entity.data);
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
            public void onFailure(Call<ActInventIntegralChangeListEntity> call, Throwable t) {
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

    private void getFirstPageData() {
        page = 0;
        getListData();
    }

    private void getNextPageData() {
        page++;
        getListData();
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    private void initDot(LinearLayout llDot, double count) {
        for (int i=0;i<count;i++){
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(layoutParams);
            llDot.addView(imageView);
            imageView = (ImageView) llDot.getChildAt(i);
            if (i==0){
                imageView.setImageResource(R.mipmap.icon_invitation_circle_on);
            }else {
                imageView.setImageResource(R.mipmap.icon_invitation_circle_off);
            }
        }
    }

    private void initFootView() {
        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(emptyView);
        }
    }

    public TextView tvFriendCount, tvExploitsPer, tvAward;

    private void initFirstHeadView() {
        TextView btnShare = (TextView) headView.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        TextView tv_remark_me = (TextView) headView.findViewById(R.id.tv_remark_me);
        tv_remark_me.setText(Html.fromHtml("<font color=\"#ffffff\">每成功邀请一位好友下载并登录APP，即可获得</font>" + "<font color=\"#FFE748\">5%</font>" + "<font color=\"#ffffff\">消费奖励。</font>"));
        tvFriendCount = (TextView) headView.findViewById(R.id.tv_friend_count);
        tvExploitsPer = (TextView) headView.findViewById(R.id.tv_exploits_per);
        tvAward = (TextView) headView.findViewById(R.id.tv_award);
        TextView tvLookDetails = (TextView) headView.findViewById(R.id.tv_look_details);
        llRankIngBottom = (LinearLayout) headView.findViewById(R.id.ll_ranking_bottom);
        llDotBottom = (LinearLayout) headView.findViewById(R.id.ll_dot_bottom);

        tvLookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ActInvitationFriendListActivity.class));
            }
        });
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
    }

    private void initHeadView() {
        getHeadData();
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

    //分享有关
    String share_type = "9"; // 9.邀请有奖分享
    String share_title, share_content, share_img, share_url, shareImg;
    private UmengDialog mShareDialog;
    private JPNetRequest jpNetRequest;

    private void getShareUrl() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        if (!isFirstGetShare) {
            startMyDialog(false);
        }
        jpNetRequest.getShareUrl(share_type, "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        String sub_title = responseInfo.result.subTitle;
                        share_content = responseInfo.result.content;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg = Constants.ImageUrl + share_img;
                            }
                        }
                        if (!isFirstGetShare) {
                            share();
                        }
                        break;
                    default:
                        break;
                }
                if (!isFirstGetShare) {
                    stopMyDialog();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                if (!isFirstGetShare) {
                    stopMyDialog();
                }
            }
        });
    }

    private void share() {
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
                            share_content,
                            shareImg,
                            share_url).share();
                }
            });
        }
        mShareDialog.show();
    }

    private void getAgainShareUrl() {
        isFirstGetShare = false;
        getShareUrl();
    }

    private void showMenu() {
        MenuUtil.showMenu(ActInvitationFriendActivity.this, R.id.v3Share);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Share:
                showMenu();
                break;
            case R.id.v3Shop:
                if (!TextUtils.isEmpty(share_title)) {
                    share();
                } else {
                    getAgainShareUrl();
                }
                break;
            default:
                break;
        }
    }


    private void onPositionListener() {
        if (mManager == null) {
            mManager = recyclerView.getLayoutManager();
        }
        int lastPosition = 0;
        if (mManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) mManager).findLastVisibleItemPosition();
        }
        int index = (lastPosition - 1) / Constants.PAGE_COUNT_20;
        if (index >= llDotTop.getChildCount() - 1) {
            index = llDotTop.getChildCount() - 1;
        }

        setDotIndex(llDotTop, index);
        setDotIndex(llDotBottom, index);
    }

    /**
     * 根据位置设置点点的背景资源
     *
     * @param layout
     * @param position
     */
    private void setDotIndex(LinearLayout layout, int position) {
        for (int i = 0; i < llDotTop.getChildCount(); i++) {
            ImageView iv = (ImageView) layout.getChildAt(i);
            if (i == position) {
                iv.setImageResource(R.mipmap.icon_invitation_circle_on);
            } else {
                iv.setImageResource(R.mipmap.icon_invitation_circle_off);
            }
        }
    }

}
