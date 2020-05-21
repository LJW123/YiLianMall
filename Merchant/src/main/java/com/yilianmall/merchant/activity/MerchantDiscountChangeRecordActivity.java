package com.yilianmall.merchant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.adapter.DefaultAdapter;
import com.yilian.mylibrary.adapter.ViewHolder;
import com.yilian.mylibrary.widget.MySwipeRefreshLayout;
import com.yilian.networkingmodule.entity.MerchantDiscountChangeRecordEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.NumberFormat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilianmall.merchant.R.id.tv_newest_discount;

public class MerchantDiscountChangeRecordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private TextView tvNewestDiscount;
    private TextView tvChangeTime;
    private TextView tvChangeIp;
    private ListView listView;
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private DefaultAdapter<MerchantDiscountChangeRecordEntity.ListBean> adapter;
    private ImageView ivNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_discount_change_record);
        initView();
        initData();
        initListener();
    }


    private void initData() {
        getMerchantDiscountChangeRecord();
    }

    int page;

    private void initListener() {
        mySwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getMerchantDiscountChangeRecord();
            }
        });
        mySwipeRefreshLayout.setOnPullUpRefreshListener(new MySwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                page++;
                getMerchantDiscountChangeRecord();
            }
        });
    }

    private void getMerchantDiscountChangeRecord() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantDiscountChangeRecord(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), page, new Callback<MerchantDiscountChangeRecordEntity>() {
                    @Override
                    public void onResponse(Call<MerchantDiscountChangeRecordEntity> call, Response<MerchantDiscountChangeRecordEntity> response) {
                        stopMyDialog();
                        mySwipeRefreshLayout.setRefreshing(false);
                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                        MerchantDiscountChangeRecordEntity body = response.body();
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
                    public void onFailure(Call<MerchantDiscountChangeRecordEntity> call, Throwable t) {
                        stopMyDialog();
                        mySwipeRefreshLayout.setRefreshing(false);
                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    ArrayList<MerchantDiscountChangeRecordEntity.ListBean> adapterList = new ArrayList<>();

    private void setData(MerchantDiscountChangeRecordEntity body) {
        List<MerchantDiscountChangeRecordEntity.ListBean> list = body.list;
        if (page == 0) {
            if (list.size() <= 0) {
                ivNothing.setVisibility(View.VISIBLE);
                return;
            } else {
                ivNothing.setVisibility(View.GONE);
            }
            adapterList.clear();
        } else {
            if (adapterList.size() > 0 && list.size() <= 0) {
                showToast(getString(R.string.merchant_no_more_data));
                page--;//没有更多时 还原page,防止没有更多page无限增加，虽然并没有什么影响，但是谁让我是一个完美boy呢；
                return;
            }
        }
        adapterList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("折扣变更记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        tvNewestDiscount = (TextView) findViewById(tv_newest_discount);
        tvNewestDiscount.setText("修改后折扣");
        tvChangeTime = (TextView) findViewById(R.id.tv_change_time);
        tvChangeTime.setText("修改时间");
        tvChangeIp = (TextView) findViewById(R.id.tv_change_ip);
        tvChangeIp.setText("操作IP地址");
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new DefaultAdapter<MerchantDiscountChangeRecordEntity.ListBean>(mContext, adapterList, R.layout.merchant_item_discount_change_record_three_text) {
            @Override
            public void convert(ViewHolder helper, MerchantDiscountChangeRecordEntity.ListBean item, int position) {
                ((TextView) helper.getView(R.id.tv_newest_discount)).setText(item.later + "折");
                ((TextView) helper.getView(R.id.tv_change_time)).setText(DateUtils.timeStampToStr2(NumberFormat.convertToLong(item.time, 0L)));
                ((TextView) helper.getView(R.id.tv_change_ip)).setText(item.ip);
                ((TextView) helper.getView(R.id.tv_change_ip)).setLines(1);

                if(position%2==0){
                    helper.getView(R.id.ll_content).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        };
        listView.setAdapter(adapter);
        mySwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        mySwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext,R.color.library_module_color_red));
        mySwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.BOTH);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
        }
    }
}
