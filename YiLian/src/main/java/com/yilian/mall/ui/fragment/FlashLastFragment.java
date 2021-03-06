package com.yilian.mall.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.adapter.FlashSaleAdapter;
import com.yilian.mall.entity.FlashSaleEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.ui.FlashSaleDetailsActivity;
import com.yilian.mall.ui.MallFlashSaleDetailActivity;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuqi on 2017/3/23 0023.
 * 上一场fragment
 */
public class FlashLastFragment extends BaseFragment {

    private TextView tvHintState;
    private PullToRefreshListView listView;
    private Context mContext;
    private JPNetRequest request;
    private int page;
    private ImageView ivNothing;
    private List<FlashSaleEntity.DataBean.GoodsBean> flashList = new ArrayList<>();
    private FlashSaleAdapter adapter;
    private LinearLayout llUpdataError;
    private TextView tvRefreshError;
    private String city_id;
    private String county_id, belong;

    private LinearLayout llDownTimer;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_flash, container, false);
        llDownTimer = (LinearLayout) view.findViewById(R.id.flash_ll);
        llDownTimer.setVisibility(View.GONE);
        tvHintState = (TextView) view.findViewById(R.id.tv_hint_state);
        listView = (PullToRefreshListView) view.findViewById(R.id.listview);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);
        llUpdataError = (LinearLayout) view.findViewById(R.id.ll_updata_error);
        tvRefreshError = (TextView) view.findViewById(R.id.tv_update_error);
        mContext = getActivity();
        page = 0;

        Bundle bundle = getArguments();
        city_id = bundle.getString("city_id");
        county_id = bundle.getString("county_id");
        belong = bundle.getString("belong");
        Logger.i(getClass().getSimpleName() + " city_id " + city_id + "  county_id  " + county_id);

        initData();
        initListener();
        return view;
    }

    @Override
    protected void loadData() {

    }

    private void initListener() {
      listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
          @Override
          public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
              page=0;
              initData();
          }

          @Override
          public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
              initData();
          }
      });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlashSaleEntity.DataBean.GoodsBean goodsBean = (FlashSaleEntity.DataBean.GoodsBean) adapter.getItem(position-1);
                Intent intent = null;
                switch (goodsBean.belong) {
                    case "1":
                        intent = new Intent(mContext, MallFlashSaleDetailActivity.class);
                        break;
                    case "0":
                        intent = new Intent(mContext, FlashSaleDetailsActivity.class);
                        break;
                }
                intent.putExtra("goods_id", goodsBean.goodsId);
                mContext.startActivity(intent);
            }
        });

        tvRefreshError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }


    private void initData() {

        if (request == null) {
            request = new JPNetRequest(mContext);
        }
        startMyDialog();
        String flashState = "1";
        String type = "0";
        request.getFlashSaleList(city_id, county_id, flashState, page, type, belong, new RequestCallBack<FlashSaleEntity>() {
            @Override
            public void onSuccess(ResponseInfo<FlashSaleEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        FlashSaleEntity result = responseInfo.result;
                        String systemTime = result.data.time;
                        List<FlashSaleEntity.DataBean.GoodsBean> goodsBeen = result.data.goods;
                        if (null != goodsBeen && goodsBeen.size() >= 1) {
                            listView.setVisibility(View.VISIBLE);
                            ivNothing.setVisibility(View.GONE);
                            llUpdataError.setVisibility(View.GONE);
                            if (page >= 1) {
                                flashList.addAll(goodsBeen);
                            } else {
                                flashList.clear();
                                flashList.addAll(goodsBeen);
                                if (null == adapter) {
                                    adapter = new FlashSaleAdapter(flashList, systemTime, mContext, result.click);
                                    listView.setAdapter(adapter);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (page >= 1) {
                                showToast(R.string.no_more_data);
                            } else {
                                listView.setVisibility(View.GONE);
                                llUpdataError.setVisibility(View.GONE);
                                ivNothing.setVisibility(View.VISIBLE);
                            }
                        }
                        listView.onRefreshComplete();
                        stopMyDialog();
                        break;
                    default:
                        listView.setVisibility(View.GONE);
                        ivNothing.setVisibility(View.VISIBLE);
                        llUpdataError.setVisibility(View.GONE);
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listView.onRefreshComplete();
                listView.setVisibility(View.GONE);
                ivNothing.setVisibility(View.GONE);
                llUpdataError.setVisibility(View.VISIBLE);
                stopMyDialog();
            }
        });

    }

}
