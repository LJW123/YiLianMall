package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JPLocationItemAdapter;
import com.yilian.mall.entity.JPLocation;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.ui.DaZhuanPanDetailActivity;
import com.yilian.mall.ui.PrizeWinnerListActivity;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/10/26 0026.
 * 本地活动---大转盘
 */

public class JPLocationCircleFragment extends JPBaseFragment{
    private View rootView;
    private PullToRefreshListView listView;
    private JPLocationItemAdapter adapter;
    private ArrayList<JPLocation.ListBean> list = new ArrayList<>();
    private ActivityNetRequest request;
    private ImageView nothing;

    private int page = 0;
    private int type = 1;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location_jp, container,false);
        initView();
        initData();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    private void initView() {
        nothing = (ImageView) rootView.findViewById(R.id.nothing);
        listView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new JPLocationItemAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JPLocation.ListBean activity = (JPLocation.ListBean) parent.getItemAtPosition(position);
                Intent i;
                if(activity.activityType.equals(Constants.ACTIVITY_TYPE_TURN)){
                    if("3".equals(activity.activityStatus)){
                        i = new Intent(getActivity(), PrizeWinnerListActivity.class);
                        i.putExtra("activity_type", Constants.ACTIVITY_TYPE_TURN);
                    } else {
                        i = new Intent(getActivity(), DaZhuanPanDetailActivity.class);
                    }
                    i.putExtra("activity_index", activity.activityIndex);
                    i.putExtra("activity_name", activity.activityName);
                    startActivity(i);
                }
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page ++;
                initData();
            }
        });
    }


    private void initData(){
        if(request == null){
            request = new ActivityNetRequest(getContext());
        }

        String cityId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, getContext(), "0");
        String countyId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, getContext(), "0");
        request.getActivityListV3(type + "", cityId, countyId, page, new RequestCallBack<JPLocation>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<JPLocation> responseInfo) {
                listView.onRefreshComplete();
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        listView.setVisibility(View.VISIBLE);
                        ArrayList<JPLocation.ListBean> moreList = responseInfo.result.list;
                        if (page == 0) {
                            if (moreList.size() > 0) {
                                list.addAll(moreList);
                                adapter.notifyDataSetChanged();
                            } else {
                                nothing.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            }
                        } else {
                            if (moreList.size() > 0) {
                                list.addAll(moreList);
                                adapter.notifyDataSetChanged();
                            } else {
                                showToast(R.string.no_more_data);
                            }
                        }

                        break;
                    case -21:
                        showToast(R.string.no_more_data);
                        break;
                    default:
                        nothing.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                listView.onRefreshComplete();
            }
        });
    }
}
