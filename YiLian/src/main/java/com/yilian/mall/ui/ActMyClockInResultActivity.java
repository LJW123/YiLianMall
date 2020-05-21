package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActClockInMyRecordAdapter;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.ActClockInMyRecordEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  我的打卡战绩
 */
public class ActMyClockInResultActivity extends BaseAppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private int page;
    private ActClockInMyRecordAdapter actClockInMyRecordAdapter;
    private View headerView;
    private TextView tvTitle;
    private ImageButton btnShare;
    private TextView tvTime;
    private TextView tvGet;
    private TextView tvSuccessDays;
    private LinearLayout llContent;
    private TextView tvFriendGet;
    private TextView tvRecordDetail;
    private int id=0;
    private boolean isFirstGetShare=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_my_clock_in_result);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        getColckInMyRecord();
        getShareUrl();
    }

    @SuppressWarnings("unchecked")
    void getColckInMyRecord() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getClockInMyRecord("my_clockin_record", page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActClockInMyRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ActClockInMyRecordEntity actClockInMyRecordEntity) {
                        Logger.i("actClockInMyRecordEntity.recordList.toString:" + actClockInMyRecordEntity.recordList.toString());
                        id=actClockInMyRecordEntity.friendClockinId;
//                        if (id==0){
//                            tvFriendGet.setCompoundDrawables(null,null,null,null);
//                        }
                        if (page == 0) {
                            if (actClockInMyRecordAdapter.getHeaderLayoutCount() <= 0) {
                                actClockInMyRecordAdapter.addHeaderView(headerView);
                            }
                            String guafenJifen="0";
                            if (!TextUtils.isEmpty(actClockInMyRecordEntity.friendGuafen)){
                                double integral=Double.parseDouble(actClockInMyRecordEntity.friendGuafen)/100;
                                java.text.DecimalFormat decimalFormat=new  java.text.DecimalFormat("#######0.#######");
                                guafenJifen=decimalFormat.format(integral);
                            }

                            tvFriendGet.setText("你的好友已瓜分奖券" +guafenJifen );
                            double giveIntegals;
                            String addIntegral;
                            if (!TextUtils.isEmpty(actClockInMyRecordEntity.record.giveIntegral)){
                                giveIntegals =Double.parseDouble(actClockInMyRecordEntity.record.giveIntegral)/100;
                                addIntegral=giveIntegals+"";
                            }else {
                                addIntegral=0+"";
                            }
                            Spanned timeSpanned, getSpanned, successDaysSpanned;
                            String s = "<big><big><big><big><big><big>";
                            String s1 = "</big></big></big></big></big></big>";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timeSpanned = Html.fromHtml(s + actClockInMyRecordEntity.record.joinNum + s1 + "<br>参与次数", Html.FROM_HTML_MODE_LEGACY);
                                getSpanned = Html.fromHtml(s + addIntegral + s1 + "<br>累计收益", Html.FROM_HTML_MODE_LEGACY);
                                successDaysSpanned = Html.fromHtml(s + actClockInMyRecordEntity.record.punchNum + s1 + "<br>成功天数", Html.FROM_HTML_MODE_LEGACY);
                            } else {
                                timeSpanned = Html.fromHtml(s + actClockInMyRecordEntity.record.joinNum + s1 + "<br>参与次数");
                                getSpanned = Html.fromHtml(s +addIntegral + s1 + "<br>累计收益");
                                successDaysSpanned = Html.fromHtml(s + actClockInMyRecordEntity.record.punchNum + s1 + "<br>成功天数");
                            }
                            tvTime.setText(timeSpanned);
                            tvGet.setText(getSpanned);
                            tvSuccessDays.setText(successDaysSpanned);

                            if (actClockInMyRecordEntity.recordList == null || actClockInMyRecordEntity.recordList.size() <= Constants.PAGE_COUNT) {
                                actClockInMyRecordAdapter.loadMoreEnd();
                                if (actClockInMyRecordAdapter.getFooterLayoutCount() <= 0 && actClockInMyRecordEntity.recordList.size() <= 0) {
                                    ImageView footer = new ImageView(mContext);
                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    footer.setLayoutParams(layoutParams);
                                    footer.setImageResource(R.mipmap.library_module_nothing);
                                    footer.setPadding(0, 50, 0, 5);
                                    actClockInMyRecordAdapter.setFooterView(footer);
                                }
                            }else{
                                actClockInMyRecordAdapter.loadMoreComplete();
                            }
                            actClockInMyRecordAdapter.setNewData(actClockInMyRecordEntity.recordList);
                        } else {
                            if (actClockInMyRecordEntity.recordList == null || actClockInMyRecordEntity.recordList.size() <= Constants.PAGE_COUNT) {
                                actClockInMyRecordAdapter.loadMoreEnd();
                            } else {
                                actClockInMyRecordAdapter.loadMoreComplete();
                            }
                            actClockInMyRecordAdapter.addData(actClockInMyRecordEntity.recordList);
                        }
                    }
                });
        addSubscription(subscription);
    }

    void getFirstPageData() {
        page = 0;
        getColckInMyRecord();
    }

    void getNextPageData() {
        page++;
        getColckInMyRecord();
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//             TODO   最后一个条目的分割线取消
//                if (newState== RecyclerView.SCROLL_STATE_IDLE) {
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    if (layoutManager instanceof LinearLayoutManager) {
//                        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                        actClockInMyRecordAdapter.getViewByPosition(lastVisibleItemPosition,R.id.view_line).setVisibility(View.INVISIBLE);
//                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDy += dy;
                if (scrollDy < 255) {
                    llTitle.getBackground().setAlpha(scrollDy);
                    v3Layout.getBackground().setAlpha(scrollDy);
                } else {
                    llTitle.getBackground().setAlpha(255);
                    v3Layout.getBackground().setAlpha(255);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        //        跳转打卡排行榜
        RxUtil.clicks(tvFriendGet, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (id==0){
                    return;
                }
                Intent intent=new Intent(mContext,ActClockRankActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        //        邀请好友
        RxUtil.clicks(btnShare, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (id==0){
                    return;
                }
                getAgainShareUrl();
//                startActivity(new Intent(mContext, ActInvitationFriendActivity.class));
            }
        });
    }

    //分享有关
    String share_type = "21"; // 9.邀请有奖分享
    String share_title,share_content,share_img,share_url,shareImg;
    private UmengDialog mShareDialog;
    private JPNetRequest jpNetRequest;

    private void getShareUrl() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        if (!isFirstGetShare){
            startMyDialog(false);
        }
        jpNetRequest.getShareUrl(share_type, "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        share_content = responseInfo.result.content;
                        String sub_title = responseInfo.result.subTitle;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg =  Constants.ImageUrl + share_img;
                            }
                        }
                        if (!isFirstGetShare){
                            share();
                        }
                        break;
                    default:
                        break;
                }
                if (!isFirstGetShare){
                    stopMyDialog();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                if (!isFirstGetShare){
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
    private void getAgainShareUrl(){
        isFirstGetShare=false;
        getShareUrl();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        actClockInMyRecordAdapter = new ActClockInMyRecordAdapter(R.layout.item_act_clock_in_my_record);
        actClockInMyRecordAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(actClockInMyRecordAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("早起打卡赢奖券");
        v3Title.setTextColor(Color.WHITE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.baijiantou);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        int titleColor = Color.parseColor("#FF8019");
        v3Layout.setBackgroundColor(titleColor);
        v3Layout.getBackground().setAlpha(0);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        llTitle.setBackgroundColor(titleColor);
        llTitle.getBackground().setAlpha(0);
        StatusBarUtil.setColor(this, titleColor);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);

        headerView = View.inflate(mContext, R.layout.header_act_clock_in_my_record, null);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        btnShare = (ImageButton) headerView.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);
        tvTime = (TextView) headerView.findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this);
        tvGet = (TextView) headerView.findViewById(R.id.tv_get);
        tvGet.setOnClickListener(this);
        tvSuccessDays = (TextView) headerView.findViewById(R.id.tv_success_days);
        tvSuccessDays.setOnClickListener(this);
        llContent = (LinearLayout) headerView.findViewById(R.id.ll_content);
        llContent.setOnClickListener(this);
        tvFriendGet = (TextView) headerView.findViewById(R.id.tv_friend_get);
        tvFriendGet.setOnClickListener(this);
        tvRecordDetail = (TextView) headerView.findViewById(R.id.tv_record_detail);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml("<font color='#666666'><big><b>记录明细</b></big></font>&nbsp;&nbsp;每日11:00前奖券到账", Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml("<font color='#666666'><big><b>记录明细</b></big></font>&nbsp;&nbsp;每日11:00前奖券到账");
        }
        tvRecordDetail.setText(spanned);
        tvRecordDetail.setOnClickListener(this);
        actClockInMyRecordAdapter.addHeaderView(headerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }


}
