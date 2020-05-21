package com.yilian.mall.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V3MoneyWithDrawDetailProgressAdapter;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.V3MoneyDetailWithDrawEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author
 *         营收、 奖券、余额单笔详情的领奖励详情
 */
public class V3MoneyDetailActivity_With_Draw extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     *
     *2和4  银行卡信息而失败的状态，显示失败原因并需要重新提交银行信息
     */

    public static final int STATUS_FAILURE_OF_BANK_4 = 4;
    public static final int STATUS_FAILURE_OF_BANK_2 = 2;
    public static final int REQUEST_CODE = 0;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvAmount;
    private TextView tvDetail;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvRemitProgress;
    private RecyclerView progressRecyclerView;
    private TextView tvTitle1;
    private TextView tvContent1;
    private TextView tvTitle2;
    private TextView tvContent2;
    private TextView tvTitle3;
    private TextView tvContent3;
    private TextView tvTitle4;
    private TextView tvContent4;
    private TextView tvTitle5;
    private TextView tvContent5;
    private TextView tvTitle6;
    private TextView tvContent6;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 0 余额 1奖券 108提取营收
     */
    private int type;
    private String img;
    private String orderId;
    /**
     * 获取领奖励进度详情
     */
    private Observer<V3MoneyDetailWithDrawEntity> observer;
    private View include6;
    private View includeCommitBankInfo;
    private V3MoneyDetailWithDrawEntity mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, Color.WHITE, Constants.STATUS_BAR_ALPHA_60);
        type = getIntent().getIntExtra("type", 0);
        img = getIntent().getStringExtra("img");
        orderId = getIntent().getStringExtra("orderId");
        setContentView(R.layout.activity_v3_money_detail__with__draw);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getMoneyWithDrawDetailData();
    }

    private void initListener() {
        RxUtil.clicks(includeCommitBankInfo, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (mEntity != null) {
                    Intent intent = new Intent();
                    intent.putExtra("bank_info", mEntity);
                    intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.bankmodel.ReCommitBankInfoActivity"));
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(V3MoneyDetailActivity_With_Draw.this, R.id.v3Shop);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMoneyWithDrawDetailData();
            }
        });
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        //        header
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("明细详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.VISIBLE);
        v3Shop.setImageResource(R.mipmap.merchant_v3_more_bottom);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.merchant_v3back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(Color.WHITE);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
//        头部
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
//        数据
        View include1 = findViewById(R.id.include_1);
        tvTitle1 = (TextView) include1.findViewById(R.id.tv_title);
        tvContent1 = (TextView) include1.findViewById(R.id.tv_content);
        View include2 = findViewById(R.id.include_2);
        tvTitle2 = (TextView) include2.findViewById(R.id.tv_title);
        tvContent2 = (TextView) include2.findViewById(R.id.tv_content);
        View include3 = findViewById(R.id.include_3);
        tvTitle3 = (TextView) include3.findViewById(R.id.tv_title);
        tvContent3 = (TextView) include3.findViewById(R.id.tv_content);
        View include4 = findViewById(R.id.include_4);
        tvTitle4 = (TextView) include4.findViewById(R.id.tv_title);
        tvContent4 = (TextView) include4.findViewById(R.id.tv_content);
        View include5 = findViewById(R.id.include_5);
        tvTitle5 = (TextView) include5.findViewById(R.id.tv_title);
        tvContent5 = (TextView) include5.findViewById(R.id.tv_content);

//        失败原因
        include6 = findViewById(R.id.include_6);
        tvContent6 = include6.findViewById(R.id.tv_content);
        tvTitle6 = include6.findViewById(R.id.tv_title);
//        重新提交银行卡
        includeCommitBankInfo = findViewById(R.id.include_commit_bank_info);
        TextView tvName = includeCommitBankInfo.findViewById(R.id.tv_name);
        tvName.setText("重新提交银行卡信息");
        includeCommitBankInfo.findViewById(R.id.iv_icon).setVisibility(View.GONE);

//        打款进度
        tvRemitProgress = (TextView) findViewById(R.id.tv_remit_progress);
        progressRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getMoneyWithDrawDetailData();
            setResult(RESULT_OK);
        }
    }

    @SuppressWarnings("unchecked")
    private void getMoneyWithDrawDetailData() {
        swipeRefreshLayout.setRefreshing(true);
        if (observer == null) {
            observer = new Observer<V3MoneyDetailWithDrawEntity>() {
                @Override
                public void onCompleted() {
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    swipeRefreshLayout.setRefreshing(false);
                    showToast(e.getMessage());
                }

                @Override
                public void onNext(V3MoneyDetailWithDrawEntity v3MoneyDetailWithDrawEntity) {
                    mEntity = v3MoneyDetailWithDrawEntity;
                    setData(v3MoneyDetailWithDrawEntity);
                }
            };
        }
        Subscription subscription = null;
        if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
            subscription = RetrofitUtils3.getRetrofitService(mContext)
                    .getRevenueDetails("account/get_money_change_detail", orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            subscription = RetrofitUtils3.getRetrofitService(mContext)
                    .getMoneyWithDrawDetailData("jfb/get_balance_change_detail_v2", type, orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        addSubscription(subscription);
    }

    private void setData(V3MoneyDetailWithDrawEntity v3MoneyDetailWithDrawEntity) {
        //        头部
        GlideUtil.showCirIconNoSuffix(mContext, img, ivIcon);
        tvName.setText(v3MoneyDetailWithDrawEntity.topName);
        if (v3MoneyDetailWithDrawEntity.type < 100) {
            tvAmount.setText("+" + MoneyUtil.getLeXiangBiNoZero(v3MoneyDetailWithDrawEntity.amount));
        } else {
            tvAmount.setText("-" + MoneyUtil.getLeXiangBiNoZero(v3MoneyDetailWithDrawEntity.amount));
        }
        switch (v3MoneyDetailWithDrawEntity.status) {
//            打款成功为灰色，其他都为橙色
            case 1:
                tvDetail.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
                break;
            default:
                tvDetail.setTextColor(Color.parseColor("#F25024"));
                break;
        }
        tvDetail.setText(v3MoneyDetailWithDrawEntity.tradeStatus);
//数据
        tvTitle1.setText("交易类型");
        tvContent1.setText(v3MoneyDetailWithDrawEntity.tradeType);
        tvTitle2.setText("提现到");
        tvContent2.setText(v3MoneyDetailWithDrawEntity.extractTo);
        tvRemitProgress.setText("处理进度");


        V3MoneyWithDrawDetailProgressAdapter adapter = new V3MoneyWithDrawDetailProgressAdapter(R.layout.item_v3_money_withdraw_progress, v3MoneyDetailWithDrawEntity.extractStatus);
        if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
            for (V3MoneyDetailWithDrawEntity.ExtractStatusBean extractStatus : v3MoneyDetailWithDrawEntity.getExtractStatus()) {
                extractStatus.resultStatus = v3MoneyDetailWithDrawEntity.status;
            }
            adapter.setDataType(Constants.TYPE_EXTRACT_REVENUE_108);
        }
        progressRecyclerView.setAdapter(adapter);

        tvTitle3.setText("账单分类");
        tvContent3.setText(v3MoneyDetailWithDrawEntity.billType);
        tvTitle4.setText("创建时间");
        tvContent4.setText(v3MoneyDetailWithDrawEntity.time);
        tvTitle5.setText("关联订单号");
        tvContent5.setText(v3MoneyDetailWithDrawEntity.extractOrder);
        tvTitle6.setText("失败原因");

        if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
            tvContent6.setText(v3MoneyDetailWithDrawEntity.extractReason);
            if (v3MoneyDetailWithDrawEntity.status == STATUS_FAILURE_OF_BANK_2 || v3MoneyDetailWithDrawEntity.status == STATUS_FAILURE_OF_BANK_4) {
                include6.setVisibility(View.VISIBLE);
                includeCommitBankInfo.setVisibility(View.VISIBLE);
            } else {
                include6.setVisibility(View.GONE);
                includeCommitBankInfo.setVisibility(View.GONE);
            }
        } else {
            include6.setVisibility(View.GONE);
            includeCommitBankInfo.setVisibility(View.GONE);
        }

    }
}
