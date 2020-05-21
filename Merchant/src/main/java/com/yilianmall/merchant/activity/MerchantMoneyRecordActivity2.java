package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.adapter.DefaultAdapter;
import com.yilian.mylibrary.adapter.ViewHolder;
import com.yilian.mylibrary.widget.MySwipeRefreshLayout;
import com.yilian.networkingmodule.entity.MerchantMoneyRecordEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.utils.NumberFormat;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantMoneyRecordActivity2 extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private ListView expandableListView;
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private ImageView ivNothing;
    private DefaultAdapter<MerchantMoneyRecordEntity.DataBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_money_record2);
        initView();
        initData();
        ininListener();
    }

    int page;

    private void initData() {
        getMerchantMoneyRecord();
    }

    private void getMerchantMoneyRecord() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantMoneyRecord(page, new Callback<MerchantMoneyRecordEntity>() {
                    @Override
                    public void onResponse(Call<MerchantMoneyRecordEntity> call, Response<MerchantMoneyRecordEntity> response) {
                        mySwipeRefreshLayout.setRefreshing(false);
                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                        MerchantMoneyRecordEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        setData(body);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantMoneyRecordEntity> call, Throwable t) {
                        showToast(R.string.network_module_net_work_error);
                        mySwipeRefreshLayout.setRefreshing(false);
                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                    }
                });
    }

    ArrayList<MerchantMoneyRecordEntity.DataBean> adapterList = new ArrayList<>();

    private void setData(MerchantMoneyRecordEntity body) {
        ArrayList<ArrayList<MerchantMoneyRecordEntity.DataBean>> newDatas = new ArrayList<>();
        ArrayList<MerchantMoneyRecordEntity.DataBean> data = body.data;
        if (page == 0) {
            if (data.size() <= 0) {
                ivNothing.setVisibility(View.VISIBLE);
                mySwipeRefreshLayout.setVisibility(View.GONE);
                return;
            } else {
                mySwipeRefreshLayout.setVisibility(View.VISIBLE);
                ivNothing.setVisibility(View.GONE);
            }
            adapterList.clear();
        } else {
            if (data.size() <= 0 && adapterList.size() > 0) {
                showToast(R.string.merchant_no_more_data);
                page--;//没有更多时 还原page,防止没有更多page无限增加，虽然并没有什么影响，但是谁让我是一个完美boy呢；
                return;
            }
        }
        adapterList.addAll(data);
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < 1; j++) {

            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商户收款记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        expandableListView = (ListView) findViewById(R.id.expandable_list_view);
        adapter = new DefaultAdapter<MerchantMoneyRecordEntity.DataBean>(mContext, adapterList, R.layout.merchant_item_money_record) {
            @Override
            public void convert(final ViewHolder helper, final MerchantMoneyRecordEntity.DataBean item, int position) {
                ((TextView) helper.getView(R.id.tv_time)).setText(DateUtils.timeStampToStr4(NumberFormat.convertToLong(item.time, 0L)));
                ((TextView) helper.getView(R.id.tv_money)).setText(MoneyUtil.getLeXiangBi(item.cash));
                ((TextView) (helper.getView(R.id.tv_data))).setText(DateUtils.timeStampToStr2(NumberFormat.convertToLong(item.time, 0L)));
                ((TextView) helper.getView(R.id.tv_way)).setText(item.typeName);
                ((TextView) (helper.getView(R.id.tv_nick))).setText(TextUtils.isEmpty(item.userName) ? "暂无昵称" : item.userName);
                ((TextView) helper.getView(R.id.tv_phone)).setText(item.userPhone);
                ((TextView) helper.getView(R.id.tv_coupon)).setText(MoneyUtil.getLeXiangBi(item.bonus));
                ((TextView) helper.getView(R.id.tv_remark)).setText(TextUtils.isEmpty(item.remark) ? "无备注信息" : item.remark);
                ((TextView) helper.getView(R.id.tv_income)).setText(MoneyUtil.getLeXiangBi(item.merchantCash));
                final ImageView ivArrow = helper.getView(R.id.iv_arrow);
                final View view = helper.getView(R.id.ll_content);
                setItem(helper, item);
                if (!item.isVisible) {
                    view.setVisibility(View.GONE);
                    ivArrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.merchant_arrow_down));
                } else {
                    view.setVisibility(View.VISIBLE);
                    ivArrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.merchant_arrow_up));
                }

                helper.getView(R.id.ll_title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!item.isVisible) {
                            view.setVisibility(View.VISIBLE);
                            ivArrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.merchant_arrow_up));
                        } else {
                            view.setVisibility(View.GONE);
                            ivArrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.merchant_arrow_down));
                        }
                        item.isVisible = !item.isVisible;

                    }
                });
            }

            private void setItem(ViewHolder helper, MerchantMoneyRecordEntity.DataBean item) {
                LinearLayout llPayerCaculated = helper.getView(R.id.ll_payer_calculated_stress);
                LinearLayout llPayeeCaculated = helper.getView(R.id.ll_payee_calculated_stress);

                LinearLayout llPayerIntergral = helper.getView(R.id.ll_payer_intergral);
                LinearLayout llPayerSaleIntergral = helper.getView(R.id.ll_payer_sale_intergral);
                if (item.blag==3) {
                    llPayerCaculated.setVisibility(View.VISIBLE);
                    llPayeeCaculated.setVisibility(View.VISIBLE);
                    llPayerIntergral.setVisibility(View.GONE);
                    llPayerSaleIntergral.setVisibility(View.GONE);

                    helper.setText(R.id.tv_payer_calculated_score, MoneyUtil.getLeXiangBi(item.happyBean));
                    helper.setText(R.id.tv_payeer_calculated_score, MoneyUtil.getLeXiangBi(item.merHappyBean));
                } else {
                    llPayerCaculated.setVisibility(View.GONE);
                    llPayeeCaculated.setVisibility(View.GONE);

                    llPayerIntergral.setVisibility(View.VISIBLE);
                    llPayerSaleIntergral.setVisibility(View.VISIBLE);

                    ((TextView) helper.getView(R.id.tv_payer_score)).setText(MoneyUtil.getLeXiangBi(item.userIntegral));
                    ((TextView) helper.getView(R.id.tv_sell_score)).setText(MoneyUtil.getLeXiangBi(item.merchantIntegral));

                }

            }
        };
        expandableListView.setAdapter(adapter);
        mySwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        mySwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        mySwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.BOTH);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }


    private void ininListener() {
        mySwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getMerchantMoneyRecord();
            }
        });
        mySwipeRefreshLayout.setOnPullUpRefreshListener(new MySwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                page++;
                getMerchantMoneyRecord();
            }
        });
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
        } else if (i == R.id.v3Back) {
            finish();
        }
    }
}
