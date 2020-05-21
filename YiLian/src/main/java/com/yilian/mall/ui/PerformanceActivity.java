package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.networkingmodule.entity.PerformanceEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.utils.MoneyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/8/19 0019.
 */

public class PerformanceActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    ///
    @ViewInject(R.id.layout_net_error)
    LinearLayout layoutNetError;
    @ViewInject(R.id.tv_refresh)
    TextView tvRefresh;
    ///
    @ViewInject(R.id.tv_top_one)
    TextView tvTopOne;
    @ViewInject(R.id.tv_top_two)
    TextView tvTopTwo;
    @ViewInject(R.id.tv_top_three)
    TextView tvTopThree;
    @ViewInject(R.id.tv_top_four)
    TextView tvTopFour;
    ///
    @ViewInject(R.id.tv_bot_one)
    TextView tvBotOne;
    @ViewInject(R.id.tv_bot_two)
    TextView tvBotTwo;
    @ViewInject(R.id.tv_bot_three)
    TextView tvBotThree;
    @ViewInject(R.id.tv_bot_four)
    TextView tvBotFour;
    @ViewInject(R.id.tv_bot_five)
    TextView tvBotFive;
    @ViewInject(R.id.tv_bot_six)
    TextView tvBotSix;
    @ViewInject(R.id.tv_bot_seven)
    TextView tvBotSeven;

    private String title, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        title = getIntent().getStringExtra("title");
        userId = getIntent().getStringExtra("user_id");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(title);

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData();
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    private void initData() {
        getData();
    }

    private void getData() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).getAchievementInfo(userId, new Callback<PerformanceEntity>() {
            @Override
            public void onResponse(Call<PerformanceEntity> call, Response<PerformanceEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                PerformanceEntity entity = response.body();

                                tvTopOne.setText(MoneyUtil.setNoSmall¥Money(entity.direct.merDeal));
                                tvTopTwo.setText(MoneyUtil.setNoSmall¥Money(entity.direct.merBonus));
                                tvTopThree.setText(MoneyUtil.setNoSmall¥Money(entity.direct.userDeal));
                                tvTopFour.setText(MoneyUtil.setNoSmall¥Money(entity.direct.userBonus));

                                tvBotOne.setText(entity.all.userCount);
                                tvBotTwo.setText(entity.all.untityCount);
                                tvBotThree.setText(entity.all.entityCount);
                                tvBotFour.setText(MoneyUtil.setNoSmall¥Money(entity.all.merDeal));
                                tvBotFive.setText(MoneyUtil.setNoSmall¥Money(entity.all.merBonus));
                                tvBotSix.setText(MoneyUtil.setNoSmall¥Money(entity.all.userDeal));
                                tvBotSeven.setText(MoneyUtil.setNoSmall¥Money(entity.all.userBonus));

                                stopMyDialog();
                                scrollView.setVisibility(View.VISIBLE);
                                layoutNetError.setVisibility(View.GONE);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PerformanceEntity> call, Throwable t) {
                stopMyDialog();
                scrollView.setVisibility(View.GONE);
                layoutNetError.setVisibility(View.VISIBLE);
            }
        });
    }
}
