package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseBarcodeEntity;
import com.yilian.networkingmodule.entity.ScOrderListEntity;
import com.yilian.networkingmodule.entity.ScOrderListEntityBottomBean;
import com.yilian.networkingmodule.entity.ScOrderListEntityHeadBean;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.BarcodeTransactionRecodeAdapter;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 条码交易记录
 */
public class MerchantBarcodeTransactionRecodeActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private FrameLayout v3Layout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<BaseBarcodeEntity> orderList = new ArrayList<>();
    private int page = 0;
    private BarcodeTransactionRecodeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_barcode_transation_recode);
        initView();
        initListener();
    }

    private void initListener() {
        adapter.setOnLoadMoreListener(this, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int i = view.getId();
                if (i == R.id.tv_status) {
                    //点击弹窗显示，选择支付方式的类型
                    ScOrderListEntityBottomBean bottomBean = (ScOrderListEntityBottomBean) adapter.getItem(position);

                    if (bottomBean.orderStatus.equals("0")) { //去支付
                        /**
                         * 现在的逻辑为 不弹窗，之前是什么支付方式，现在就是什么支付方式 2017-9-4
                         */
                        Intent intent = null;
                        if (TextUtils.isEmpty(bottomBean.phone)) {
                            //如果电话是空，则为二维码支付
                            intent = new Intent(mContext, MerchantCollectionRqcodeActivity.class);
                            intent.putExtra("order_index", bottomBean.orderIndex);
                            intent.putExtra("type", "MerchantShopActivity");
                        } else {
                            //如果有电话，则为现金支付
                            intent = new Intent(mContext, MerchantResalePayActivity.class);
                            intent.putExtra("order_index", bottomBean.orderIndex);
                            float profitPrice = Float.parseFloat(bottomBean.orderGoodsPrice) - Float.parseFloat(bottomBean.orderSupplierPrice);
                            intent.putExtra("profitCash", MoneyUtil.getLeXiangBiNoZero(profitPrice));
                            intent.putExtra("type", "8");
                            intent.putExtra("from_type", "MerchantShopActivity");
                            intent.putExtra("phone", bottomBean.phone);
                        }
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        orderList.clear();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getScOrderData(String.valueOf(page), new Callback<ScOrderListEntity>() {
                    @Override
                    public void onResponse(Call<ScOrderListEntity> call, Response<ScOrderListEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            HttpResultBean bean = response.body();
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        ScOrderListEntity body = response.body();
                                        List<ScOrderListEntity.DataBean> data = body.data;
                                        if (null != data && data.size() > 0) {
                                            for (int i = 0; i < data.size(); i++) {
                                                ScOrderListEntity.DataBean dataBean = data.get(i);
                                                ScOrderListEntityHeadBean headBean = new ScOrderListEntityHeadBean(dataBean.orderId, dataBean.phone);
                                                Logger.i("headBean:" + headBean.toString());
                                                adapter.addData(headBean);
                                                List<ScOrderListEntity.DataBean.GoodsInfoBean> goodsInfo = dataBean.goodsInfo;
                                                int size = goodsInfo.size();
                                                for (int j = 0; j < size; j++) {
                                                    adapter.addData(goodsInfo.get(j));
                                                }
                                                ScOrderListEntityBottomBean bottomBean = new ScOrderListEntityBottomBean(
                                                        dataBean.orderIndex, dataBean.orderGoodsPrice, dataBean.merchantIntegral, dataBean.userIntegral
                                                        , dataBean.orderStatus, dataBean.totalCount, dataBean.orderSupplierPrice, dataBean.merchantType, dataBean.orderConsumer,
                                                        dataBean.orderTotalCount, dataBean.phone
                                                );
                                                adapter.addData(bottomBean);
                                            }
                                            if (data.size() < Constants.PAGE_COUNT) {
                                                adapter.loadMoreEnd();//BaseAdapter封装好的有上拉加载
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
                                                adapter.setEmptyView(getEmptyView());
                                            } else {
                                                adapter.loadMoreEnd();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ScOrderListEntity> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(getErrorView());
                        } else {
                            page--;
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("交易记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BarcodeTransactionRecodeAdapter(orderList);
        adapter.bindToRecyclerView(recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));


        v3Back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_refresh) {
            initData();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }

    public View getErrorView() {
        View errorView = null;
        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(this);
        }
        return errorView;
    }

    public View getEmptyView() {
        View emptyView = null;
        if (null == emptyView) {
            emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
            TextView tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
            tvNoData.setText("暂无交易记录");
        }
        return emptyView;
    }
}
