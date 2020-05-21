package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.ChartsEntity;
import com.yilian.networkingmodule.entity.ScoreExponent;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.activity.MerchantScoreExponentActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/8/26 0026.
 * 排行榜界面
 */

public class ProfitExprofitFragment extends BaseFragment implements View.OnClickListener {
    private static final int CONTENT_TITLE_AREA = 0;
    private static final int CONTENT_TITLE_CONSUME = 1;
    private static final int CONTENT_TITLE_MERCHANT_PROFIT = 2;
    private static final int CONTENT_TITLE_INTEGRAL_BALANCE = 3;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private TextView tvIntegral;
    private LinearLayout llSevenExponent;
    private TextView tvMerchantCount;
    private TextView tvMemberCount;
    private ArrayList<ScoreExponent> scoreExponent;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout llIntegralBalanceContent;
    private LinearLayout llMerchantProfitContent;
    private LinearLayout llConsumeContent;
    private LinearLayout llAreaContent;
    private LinearLayout llErrorView;
    private TextView tvRefreshView;
    private View view;
    private ScrollView scrollView;
    private LinearLayout rootLayout;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.activity_profit_exponent, null);
        }
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        rootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
        v3Back = (ImageView) view.findViewById(R.id.v3Back);
        v3Back.setVisibility(View.INVISIBLE);
        v3Left = (ImageView) view.findViewById(R.id.v3Left);
        v3Title = (TextView) view.findViewById(R.id.v3Title);
        v3Title.setText("排行榜");
        v3Title.setTextColor(Color.WHITE);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        v3Shop = (ImageView) view.findViewById(R.id.v3Shop);
        v3Share = (ImageView) view.findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) view.findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(getResources().getColor(R.color.color_red));
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        scrollView = (ScrollView) view.findViewById(R.id.content_view);
        tvIntegral = (TextView) view.findViewById(R.id.tv_integral);
        llSevenExponent = (LinearLayout) view.findViewById(R.id.ll_seven_exponent);
        tvMerchantCount = (TextView) view.findViewById(R.id.tv_merchant_count);
        tvMemberCount = (TextView) view.findViewById(R.id.tv_member_count);
        llAreaContent = (LinearLayout) view.findViewById(R.id.ll_area_content);
        llConsumeContent = (LinearLayout) view.findViewById(R.id.ll_consume_content);
        llIntegralBalanceContent = (LinearLayout) view.findViewById(R.id.ll_integral_balance_content);
        llMerchantProfitContent = (LinearLayout) view.findViewById(R.id.ll_merchant_profit_content);
        llErrorView = (LinearLayout) view.findViewById(R.id.ll_error_view);
        llErrorView.setVisibility(View.GONE);
        tvRefreshView = (TextView) view.findViewById(R.id.tv_refresh);

        v3Back.setOnClickListener(this);
        llSevenExponent.setOnClickListener(this);
        tvRefreshView.setOnClickListener(this);
    }


    private void initListener() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });

    }


    private void initData() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                getData();
            }
        });
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getChartsEntity(new Callback<ChartsEntity>() {
                    @Override
                    public void onResponse(Call<ChartsEntity> call, Response<ChartsEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        llErrorView.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.VISIBLE);
                                        ChartsEntity.DataBean chartsData = response.body().data;
                                        ChartsEntity.DataBean.TotalAmountBean totalAmount = chartsData.totalAmount;//7天奖券指数
                                        scoreExponent = (ArrayList) totalAmount.integralNumberArr;
                                        if (null != totalAmount.integralNumber) {
                                            tvIntegral.setText(totalAmount.integralNumber);
                                        }
                                        tvMerchantCount.setText(Html.fromHtml("<font>" + totalAmount.merchantNum + "<br><small><small>平台商家</small></small></font>"));
                                        tvMemberCount.setText(Html.fromHtml("<font>" + totalAmount.userNum + "<br><small><small>平台会员</small></small></font>"));
                                        ArrayList<ChartsEntity.DataBean.ChartsBean> areaDiscountCharts = chartsData.discountCharts;//地区让利排行
                                        if (null != areaDiscountCharts && areaDiscountCharts.size() > 0) {
                                            llAreaContent.removeAllViews();
                                            setContentView(areaDiscountCharts, CONTENT_TITLE_AREA);
                                        }
                                        ArrayList<ChartsEntity.DataBean.ChartsBean> dealCharts = chartsData.dealCharts;//消费排行
                                        if (null != dealCharts && dealCharts.size() > 0) {
                                            llConsumeContent.removeAllViews();
                                            setContentView(dealCharts, CONTENT_TITLE_CONSUME);
                                        }
                                        ArrayList<ChartsEntity.DataBean.ChartsBean> merDiscountCharts = chartsData.merDiscountCharts;//商户让利排行
                                        if (null != merDiscountCharts && merDiscountCharts.size() > 0) {
                                            llMerchantProfitContent.removeAllViews();
                                            setContentView(merDiscountCharts, CONTENT_TITLE_MERCHANT_PROFIT);
                                        }
                                        ArrayList<ChartsEntity.DataBean.ChartsBean> userIntegralCharts = chartsData.userIntegralCharts;//用户奖券排行
                                        if (null != userIntegralCharts && userIntegralCharts.size() > 0) {
                                            llIntegralBalanceContent.removeAllViews();
                                            setContentView(userIntegralCharts, CONTENT_TITLE_INTEGRAL_BALANCE);
                                        }
                                        break;
                                }
                            }
                        }
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ChartsEntity> call, Throwable t) {
                        swipeLayout.setRefreshing(false);
                        scrollView.setVisibility(View.GONE);
                        llErrorView.setVisibility(View.VISIBLE);
                        Logger.e(getClass().getSimpleName() + "  t  " + t);
                    }
                });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_seven_exponent:
                Intent intent = new Intent(mContext, MerchantScoreExponentActivity.class);
                if (null != scoreExponent && scoreExponent.size() > 0) {
                    intent.putExtra("score_exponent_list", scoreExponent);
                    startActivity(intent);
                }
                break;
            case R.id.tv_refresh:
                getData();
                break;
        }
    }

    public void setContentView(ArrayList<ChartsEntity.DataBean.ChartsBean> contentList, int swichCode) {
        View contentView = null;
        ImageView ivMedal;
        TextView tvName;
        TextView tvValue;

        for (int i = 0; i < contentList.size(); i++) {
            contentView = View.inflate(mContext, R.layout.inner_item_discount2, null);
            tvName = (TextView) contentView.findViewById(R.id.tv_title);
            tvName.setText(contentList.get(i).name);
            tvValue = (TextView) contentView.findViewById(R.id.tv_content);
            tvValue.setText(MoneyUtil.getLeXiangBi(contentList.get(i).value));
            ivMedal = (ImageView) contentView.findViewById(R.id.iv_medal);
            if (i == 0) {
                ivMedal.setImageResource(R.mipmap.icon_2_1);
            } else if (i == 1) {
                ivMedal.setImageResource(R.mipmap.icon_2_2);
            } else if (i == 2) {
                ivMedal.setImageResource(R.mipmap.icon_2_3);
            }
            switch (swichCode) {
                case CONTENT_TITLE_AREA:
                    llAreaContent.addView(contentView);
                    break;
                case CONTENT_TITLE_CONSUME:
                    llConsumeContent.addView(contentView);
                    break;
                case CONTENT_TITLE_MERCHANT_PROFIT:
                    llMerchantProfitContent.addView(contentView);
                    break;
                case CONTENT_TITLE_INTEGRAL_BALANCE:
                    llIntegralBalanceContent.addView(contentView);
                    break;

            }
        }


    }
}
