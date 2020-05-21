package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripHomeListAdapter;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程 取消订单 成功页面
 *
 * @author Created by Zg on 2018/10/17.
 */

public class CtripOrderCancelSuccessfulActivity extends BaseFragmentActivity implements View.OnClickListener {


    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private TextView tvAgain;

    /**
     * 列表
     */
    private int page = Constants.PAGE_INDEX;
    private RecyclerView recyclerview;
    private CtripHomeListAdapter mAdapter;

    private String  hotelId;
    private String CityCode,gdLat,gdLng,dayInclusiveAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_order_cancel_successful);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new CtripHomeListAdapter();
        mAdapter.bindToRecyclerView(recyclerview);

        View headView = View.inflate(context,R.layout.ctrip_activity_order_cancel_successful_header,null);
        tvAgain = headView.findViewById(R.id.tv_again);
        mAdapter.addHeaderView(headView);
    }

    public void initData() {
        tvTitle.setText("已取消");
        tvRight.setText("完成");
        tvRight.setVisibility(View.VISIBLE);

        hotelId = getIntent().getStringExtra("hotelId");

        CityCode = getIntent().getStringExtra("CityCode");
        gdLat = getIntent().getStringExtra("gdLat");
        gdLng = getIntent().getStringExtra("gdLng");
        dayInclusiveAmount = getIntent().getStringExtra("dayInclusiveAmount");

        getFirstMaybeLike();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvAgain.setOnClickListener(this);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, recyclerview);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripHotelListEntity.DataBean mData = (CtripHotelListEntity.DataBean) adapter.getItem(position);
                JumpCtripActivityUtils.toCtripHotelDetail(context, mData.HotelID,null,null);
            }
        });

    }

    /**
     * 获取首页为您推荐数据
     */
    private void getFirstMaybeLike() {
        page = Constants.PAGE_INDEX;
        getMaybeLikeData();
    }
    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        page++;
        getMaybeLikeData();
    }
    /**
     * 猜你喜欢
     **/
    private void getMaybeLikeData() {
        String maxPric = MyBigDecimal.add(dayInclusiveAmount,"500");
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getHotelList("xc_goods/xc_hotel_list", CityCode, "comSort DESC", "100000000", "", "", "0", maxPric, "",
                        "", "", "", "", "", "", gdLat, gdLng, page, String.valueOf(Constants.PAGE_COUNT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelListEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        if (page != Constants.PAGE_INDEX) {
                            page--;
                        }
                    }

                    @Override
                    public void onNext(CtripHotelListEntity entity) {
                        if (page == Constants.PAGE_INDEX) {
                            if (entity.data != null && entity.data.size() > 0) {
                                mAdapter.setLocation(entity.location);
                                mAdapter.setNewData(entity.data);
                            }
                        } else {
                            mAdapter.addData(entity.data);
                        }
                        if (entity.data.size() < Constants.PAGE_COUNT) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_right:
                //返回
                finish();
                break;
            case R.id.tv_again:
                //重新预订该酒店
                JumpCtripActivityUtils.toCtripHotelDetail(context,hotelId,null,null);
                finish();
                break;
            default:
                break;
        }
    }
}
