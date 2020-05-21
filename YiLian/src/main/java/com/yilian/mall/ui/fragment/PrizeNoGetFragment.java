package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MyPrizeAdapter;
import com.yilian.mall.ui.PrizeObtainActicity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.XlistView.XListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/4/28 0028.
 * 中将物品列表--未领取
 */
public class PrizeNoGetFragment extends BaseFragment implements XListView.IXListViewListener {

    private XListView listView;
    private LinearLayout llErrorView;
    private ImageView ivNothing;
    private String round, type, count;
    private int page;
    private ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> prizeList = new ArrayList<>();
    private MyPrizeAdapter adapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prize_content_fragment, container, false);
        listView = (XListView) view.findViewById(R.id.listView);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        llErrorView = (LinearLayout) view.findViewById(R.id.ll_error_view);
        llErrorView.setVisibility(View.GONE);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);

        page = 0;
        initListener();
        return view;
    }

    private void initListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WheelOfFortuneAwardListEntity.DataBean.ListBean selectBean = (WheelOfFortuneAwardListEntity.DataBean.ListBean) adapter.getItem(position - 1);
                switch (selectBean.round) {
                    case "0":
                        Intent intent = new Intent(mContext, PrizeObtainActicity.class);
                        intent.putExtra("prizeGoodsList", selectBean);
                        startActivity(intent);
                        break;
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void loadData() {
        //如果round字段根据viewpager的切换去重新刷新请求数据浪费资源
        round = "0";
        type = "0";
        count = "30";
        Logger.e("type  " + type + " \nround " + round + " \npage " + page + " \ncount " + count + " mContext" + mContext);
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(BaseFragment.mContext)).setToken(RequestOftenKey.getToken(BaseFragment.mContext)).
                getWheelOfFortuneAwardList(type, round, String.valueOf(page), count, new Callback<WheelOfFortuneAwardListEntity>() {
                    @Override
                    public void onResponse(Call<WheelOfFortuneAwardListEntity> call, Response<WheelOfFortuneAwardListEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> listBeen = response.body().data.list;
                                        if (null != listBeen && listBeen.size() >= 1) {
                                            listView.setVisibility(View.VISIBLE);
                                            if (page >= 1) {
                                                prizeList.addAll(listBeen);
                                            } else {
                                                prizeList.clear();
                                                prizeList.addAll(listBeen);
                                                if (adapter == null) {
                                                    adapter = new MyPrizeAdapter(prizeList);
                                                    listView.setAdapter(adapter);
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            if (page >= 1) {
                                                showToast("暂无更多物品");
                                            } else {
                                                listView.setVisibility(View.GONE);
                                                ivNothing.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        listView.stopLoadMore();
                                        listView.stopRefresh();
                                        stopMyDialog();
                                        break;
                                    default:
                                        showToast(response.body().code);
                                        listView.stopLoadMore();
                                        listView.stopRefresh();
                                        stopMyDialog();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WheelOfFortuneAwardListEntity> call, Throwable t) {
                        listView.setVisibility(View.GONE);
                        llErrorView.setVisibility(View.VISIBLE);
                        listView.stopLoadMore();
                        listView.stopRefresh();
                        stopMyDialog();
                    }
                });
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }
}
