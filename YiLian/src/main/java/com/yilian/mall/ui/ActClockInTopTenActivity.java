package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActRankingTopTenAdapter;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.ActClockInTopTenEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.text.SimpleDateFormat;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @authorLYQ 2017/11/10.
 * 平台打卡前十名 排行榜
 */

public class ActClockInTopTenActivity extends BaseAppCompatActivity implements View.OnClickListener {

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
    private String clockInId;
    private ActRankingTopTenAdapter actRankingTopTenAdapter;
    private View headerView;
    private TextView tvPeopleNum;
    private TextView tvCarveUp;
    private LinearLayout llHeader;
    private TextView tvDate;
    //求赏最大限制   用于客户端随机生成求赏金额
    private int seekRewardLimit;
    private boolean refresh=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_swip_recyclerview);
        com.yilian.mylibrary.StatusBarUtils.setStatusBarColor(this, R.color.transparent, false);
        initView();
        initListener();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus&&refresh){
            initData();
            refresh=false;
        }
    }

    private void initData() {
        getTopTenData();
    }

    @SuppressWarnings("unchecked")
    private void getTopTenData() {
        startMyDialog(false);
        swipeRefreshLayout.setRefreshing(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
//                .getActClockInTopTenData("clockin_record_chart", clockInId, 0, 10)
                .getActClockInTopTenData("clockin_record_chart", clockInId, 0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActClockInTopTenEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(ActClockInTopTenEntity actClockInTopTenEntity) {
                        seekRewardLimit = actClockInTopTenEntity.seekRewardLimit;
                        String noticeManNum=actClockInTopTenEntity.clockinSuccess+"人";
                        SpannableString spannableString=new SpannableString(noticeManNum);
                        spannableString.setSpan(new AbsoluteSizeSpan(35,true),0,noticeManNum.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new AbsoluteSizeSpan(14,true),noticeManNum.length()-1,noticeManNum.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvPeopleNum.setText(spannableString);
                        tvCarveUp.setText("瓜分" + MoneyUtil.getLeXiangBiNoZero(actClockInTopTenEntity.guafenIntegral) + "奖券");
                        List<ActClockInTopTenEntity.ListBean> list = actClockInTopTenEntity.list;
                        actRankingTopTenAdapter.setNewData(list);
                        if (actRankingTopTenAdapter.getHeaderLayoutCount() <= 0) {
                            actRankingTopTenAdapter.addHeaderView(headerView);
                        }
                        String time=TimeUtils.millis2String(actClockInTopTenEntity.dateNo*1000L,new SimpleDateFormat("yyyy年MM月dd日"));
                        tvDate.setText(time);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 对排行点赞或取消点赞
     *
     * @param recordId
     * @param type     1 点赞   2 取消点赞
     */
    @SuppressWarnings("unchecked")
    private void clockInPraise(String recordId, int type) {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .clockInRecordPraise("clockin_record_praise", recordId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean o) {

                    }
                });
        addSubscription(subscription);
    }

    private void initListener() {
        actRankingTopTenAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ActClockInTopTenEntity.ListBean item = (ActClockInTopTenEntity.ListBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_like_count:
//                        点赞
                        if (item.isPraise == 0) {
                            clockInPraise(item.recordId, item.isPraise + 1);
                            item.isPraise = 1;
                            item.praise++;
                        } else {
                            clockInPraise(item.recordId, item.isPraise + 1);
                            item.isPraise = 0;
                            item.praise--;
                        }
//                        由于headerView存在，此处需要对position矫正
                        actRankingTopTenAdapter.notifyItemChanged(position + 1, item);
                        break;
//                    查看评论
                    case R.id.tv_reply:
                        Intent intentToComment=new Intent(mContext,ActLookCommentsActivity.class);
                        intentToComment.putExtra("id",item.recordId);
                        intentToComment.putExtra("type",item.isPraise);
                        startActivityForResult(intentToComment,0);
                        break;
                    case R.id.tv_give_reward:
//                        求赏
                        Intent intent = new Intent(mContext, ActAskRewardDialogActivity.class);
                        intent.putExtra("recordId", item.recordId);
                        intent.putExtra("seekRewardLimit", seekRewardLimit);
                        intent.putExtra("photoUrl", item.photo);
                        startActivity(intent);
                        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
                        break;
                    case R.id.iv_icon:
//                       个人打卡记录
                        Intent intent1 = new Intent(mContext, ActClockInTopTenPersonRecordActivity.class);
                        intent1.putExtra("consumerId",item.consumerId);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTopTenData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDistance;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDistance += dy;
                if (scrollDistance < 255) {
                    v3Layout.getBackground().setAlpha(scrollDistance);
                } else {
                    v3Layout.getBackground().setAlpha(255);
                }
            }
        });
    }

    private void initView() {
        clockInId=getIntent().getStringExtra("photo_id");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        actRankingTopTenAdapter = new ActRankingTopTenAdapter(R.layout.item_ranking);
        recyclerView.setAdapter(actRankingTopTenAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("排行榜");
        v3Title.setTextColor(Color.WHITE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setText("打赏记录");
        tvRight2.setTextColor(getResources().getColor(R.color.white));
        tvRight2.setVisibility(View.VISIBLE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.baijiantou);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundResource(R.mipmap.bg_atc_ranking_head);
        v3Layout.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v3Layout.getLayoutParams();
        layoutParams.height += StatusBarUtils.getStatusBarHeight(mContext);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) llTitle.getLayoutParams();
        layoutParams1.height += StatusBarUtils.getStatusBarHeight(mContext);
        llTitle.setBackgroundColor(Color.TRANSPARENT);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);

        headerView = View.inflate(mContext, R.layout.item_act_ranking_head, null);
        tvPeopleNum = (TextView) headerView.findViewById(R.id.tv_people_num);
        tvCarveUp = (TextView) headerView.findViewById(R.id.tv_carve_up);
        llHeader = (LinearLayout) headerView.findViewById(R.id.ll_header);
        llHeader.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
        tvDate = (TextView) headerView.findViewById(R.id.tv_date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.tv_right2:
                Intent intent=new Intent(mContext,ActRewardRecorderActivity.class);
                startActivity(intent);
                break;
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==1){
            refresh=true;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
