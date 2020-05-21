package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.RedPackgeDetailsAdapter;

import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import com.yilian.networkingmodule.entity.StealMyRedpackgets;
import com.yilian.networkingmodule.entity.StealMyRedpackgetsDetails;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ZYH on 2017/12/4 0004.
 */

public class StealRedPackgDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Back;

    private TextView v3Title;

    private ImageView tvRight;
    private RecyclerView mRecyclerView;
    private FrameLayout mLinearLayout;
    private View headerView;
    private ImageView ivUserHeadPhoto;
    private TextView tvName;
    private String id;
    private MySwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 0;
    private RedPackgeDetailsAdapter mAdapter;
    private String merchantName;
    private String merchantImage;
    private UmengDialog mShareDialog;
    private String share_title;
    private String shareImg;
    private String share_url;
    StealMyRedpackgets.Data.RedDetails redDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steal_redpackge_detials);
        initView();
        initClickLisner();
    }

    private void initClickLisner() {
        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });

        //上拉加载更多监听
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, mRecyclerView);


    }

    private void initView() {
        //设置状态栏的颜色
        StatusBarUtils.setStatusBarColor(this, R.color.color_status_redpackget);
//        id = getIntent().getStringExtra("merchant_id");
//        merchantName = getIntent().getStringExtra("merchant_name");//姓名
//        merchantImage = getIntent().getStringExtra("merchant_image");//头像
        redDetails=getIntent().getParcelableExtra("data");
        id =redDetails.merchantId;
        merchantName = redDetails.merchantName;//姓名
        merchantImage = redDetails.merchantImage;//头像
        headerView = View.inflate(mContext, R.layout.header_redpackget_details, null);
        ivUserHeadPhoto = (ImageView) headerView.findViewById(R.id.redpackage_user_icon);
        tvName = (TextView) headerView.findViewById(R.id.name);
        setHeadData();
        mSwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));


        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.baijiantou);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("被偷奖励详情");
        v3Title.setTextColor(getResources().getColor(R.color.white));
        tvRight = (ImageView) findViewById(R.id.v3Share);
        tvRight.setImageResource(R.mipmap.icon_share_red_packge);
        tvRight.setVisibility(View.VISIBLE);
        mLinearLayout = (FrameLayout) findViewById(R.id.v3Layout);
        mLinearLayout.setBackgroundColor(getResources().getColor(R.color.color_status_redpackget));

        mRecyclerView = (RecyclerView) findViewById(R.id.pull_listView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RedPackgeDetailsAdapter(R.layout.item_steal_redpackge_details, true);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        List<StealMyRedpackgetsDetails.Data> list = new ArrayList<>();
        StealMyRedpackgetsDetails.Data data = new StealMyRedpackgetsDetails.Data();
        data.amount = "100.00";
        list.add(data);
        mAdapter.setNewData(list);
    }

    private void setHeadData() {
        tvName.setTextSize(18);
        if (!TextUtils.isEmpty(merchantName)){
            tvName.setText(merchantName);
        }
        if (!TextUtils.isEmpty(merchantImage)){
            GlideUtil.showImageRadius(mContext, merchantImage, ivUserHeadPhoto,6);
        }
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

    @SuppressWarnings("unchecked")
    public void getData() {
       Subscription subscription= RetrofitUtils3.getRetrofitService(mContext).getMyRedPackgetsDetails("stealbonus/my_stealbonus", page
                    , id, Constants.PAGE_COUNT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StealMyRedpackgetsDetails>() {
                        @Override
                        public void onCompleted() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            mSwipeRefreshLayout.setRefreshing(false);
                            mAdapter.loadMoreFail();

                        }

                        @Override
                        public void onNext(StealMyRedpackgetsDetails stealMyRedpackgetsDetails) {
                            setData(stealMyRedpackgetsDetails);
                        }
                    });
       addSubscription(subscription);
    }

    private void setData(StealMyRedpackgetsDetails stealMyRedpackgetsDetails) {
        List<StealMyRedpackgetsDetails.Data> list = stealMyRedpackgetsDetails.data;
        showToast(stealMyRedpackgetsDetails.msg);
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

    private void initData() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
        Logger.i("ray-走了这里拉拉2");
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
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


}
