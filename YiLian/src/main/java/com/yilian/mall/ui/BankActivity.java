package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.DefaultAdapter;
import com.yilian.mall.adapter.ViewHolder;
import com.yilian.mall.entity.BankMain;
import com.yilian.mall.http.BankRequest;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * 乐分宝主界面
 */

public class BankActivity extends BaseActivity implements PullToRefreshScrollView.ScrollListener {

    @ViewInject(R.id.sv_pull_refresh)
    private PullToRefreshScrollView mScrollView;

    @ViewInject(R.id.llayout_head)
    private LinearLayout lLayoutHead;

    @ViewInject(R.id.llayout2)
    private LinearLayout lLayout2;

    @ViewInject(R.id.llayout1)
    private LinearLayout lLayout1;

    @ViewInject(R.id.llayout_income)
    private LinearLayout lLayoutIncome;

    @ViewInject(R.id.tv_income)
    private TextView tv_income;

    @ViewInject(R.id.tv_income_number)
    private TextView mTvIncomeNumber; // 领奖励

    @ViewInject(R.id.tv_income_rate)
    private TextView mTvIncomeRote; // 万分领奖励

    @ViewInject(R.id.tv_income_total)
    private TextView mTvIncomeTotal; // 累计领奖励

    @ViewInject(R.id.list_bank_data)
    private NoScrollListView listBankLogData;

    @ViewInject(R.id.llayout_log_hint)
    private LinearLayout lLayoutLogHint;

    private BankRequest bankRequest;

    private ArrayList<BankMain.IncomeLogBean> listData;

    private DefaultAdapter logAdapter;

    private int page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ViewUtils.inject(this);

        Listener();
        initAdapter();

    }


    private void initAdapter() {
        listData = new ArrayList<BankMain.IncomeLogBean>();

        logAdapter = new DefaultAdapter<BankMain.IncomeLogBean>(this, listData, R.layout.item_bank_main_log) {
            @Override
            public void convert(ViewHolder helper, BankMain.IncomeLogBean item) {
                helper.setText(R.id.txt_msg, item.msg);
                helper.setText(R.id.txt_time, StringFormat.formatDate(item.incomeTime, "yyyy-MM-dd HH:mm"));
                String type = "";
                switch (item.type) {
                    case 0:
                        type = "+";
                        break;

                    case 1:
                        type = "-";
                        break;
                }
                helper.setText(R.id.txt_count, type + String.format("%.2f", NumberFormat.convertToDouble(item.consumerGainLebi, 0d) / 100));
                helper.setText(R.id.txt_total, String.format("%.2f", NumberFormat.convertToDouble(item.consumerAfterLebi, 0d) / 100));

            }
        };
        listBankLogData.setFocusable(false);
        listBankLogData.setAdapter(logAdapter);


    }

    private void Listener() {
        mScrollView.setScrollListener(this);
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        /**
         * 下拉刷新
         */
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        startMyDialog();

        loadData();
    }

    /**
     * 充值
     *
     * @param view
     */
    public void recharge(View view) {
        if (isLogin()) {
//            Intent intent = new Intent(this, NMemberChargeActivity.class);
//            intent.putExtra("type", "chooseCharge");
//            startActivity(intent);
            startActivity(new Intent(this, RechargeActivity.class));
        } else {
            startActivity(new Intent(this, LeFenPhoneLoginActivity.class));
        }
    }

    private void loadData() {
        if (bankRequest == null) {
            bankRequest = new BankRequest(mContext);
        }

        bankRequest.BankMain(page, new RequestCallBack<BankMain>() {
            @Override
            public void onSuccess(ResponseInfo<BankMain> responseInfo) {
                mScrollView.onRefreshComplete();
                Logger.json(responseInfo.result.toString());
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        loadView(responseInfo.result);

                        if (page == 0) {
                                                       listData.clear();

                            if (responseInfo.result.incomeLog == null || responseInfo.result.incomeLog.size() == 0) {
                                lLayoutLogHint.setVisibility(View.GONE);
                            } else {
                                lLayoutLogHint.setVisibility(View.VISIBLE);
                            }
                        }

                        listData.addAll(responseInfo.result.incomeLog);
                        logAdapter.notifyDataSetChanged();


                        break;

                    case -4:
                    case -23:
                        showToast("登录状态失效，请重新登录");
                        sp.edit().putBoolean("state", false).commit();

                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mScrollView.onRefreshComplete();
                stopMyDialog();

            }
        });
    }

    private void loadView(BankMain data) {

        mTvIncomeNumber.setText(String.format("%.2f", NumberFormat.convertToDouble(data.lastIncome, 0d) / 100));
        mTvIncomeRote.setText(String.format("%.2f", NumberFormat.convertToDouble(data.percentage, 0d) ));
        mTvIncomeTotal.setText(String.format("%.2f", NumberFormat.convertToDouble(data.incomeLebi, 0d) / 100));
        Logger.i("累计领奖励 ： " + data.incomeLebi + "--" + String.valueOf(NumberFormat.convertToDouble(data.incomeLebi, 0d) + "--" + String.valueOf(NumberFormat.convertToDouble(data.incomeLebi, 0d) / 100)));

        tv_income.setText(data.avible_lebi.equals("0") ? "0.00" : String.format("%.2f", NumberFormat.convertToDouble(data.avible_lebi, 0d) / 100));

    }

    ;

    @Override
    public void onScroll(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y >= lLayoutHead.getHeight()) {
            if (lLayoutIncome.getParent() != lLayout2) {
                lLayout1.removeView(lLayoutIncome);
                lLayout2.addView(lLayoutIncome);
            }
        } else {
            if (lLayoutIncome.getParent() != lLayout1) {
                lLayout2.removeView(lLayoutIncome);
                lLayout1.addView(lLayoutIncome);
            }
        }

    }

    public void back(View view) {
        finish();
    }

    public void more(View view) {

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title_name", "什么是乐分宝");
        intent.putExtra("url", Constants.LefenBaoWhy);

        startActivity(intent);

    }

    /**
     * 万分领奖励（领奖励利率）
     *
     * @param view
     */
    public void incomeRate(View view) {
        Intent intent = new Intent(this, BankProfitActivity.class);
        intent.putExtra("pageName", "profitRate");//领奖励利率
        startActivity(intent);
    }

    /**
     * 累计领奖励
     *
     * @param view
     */
    public void incomeTotal(View view) {
        Intent intent = new Intent(this, BankProfitActivity.class);
        intent.putExtra("pageName", "accumulatedProfit");//累计领奖励
        startActivity(intent);

    }

//    /**
//     * 领奖励
//     */
//    public void income(View view) {
//        // mTvIncomeNumber.setDuration(1000);
//        mTvIncomeNumber.start();
//    }

    /**
     * 立即转出
     *
     * @param view
     */
    public void out(View view) {
        if (isLogin()) {
            startActivity(new Intent(mContext, BankRollOutActivity.class));
        } else {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
        }
    }
}
