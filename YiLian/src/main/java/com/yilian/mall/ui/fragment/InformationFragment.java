package com.yilian.mall.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilian.mall.R;

/**
 * Created by liuyuqi on 2017/9/2 0002.
 */

public abstract class  InformationFragment extends BaseFragment {

    private View view;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;
    protected int page;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_information, container, false);
        }
        page = 0;
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getNewData();
            }
        });
    }


    boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isFirst) {
            isFirst = !isFirst;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (null!=swipeRefreshLayout){
                            initData();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getNewData();
            }
        });
    }

    public abstract void getNewData();

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void loadData() {

    }


    public View getEmptyView() {
        View emptyView = null;
        if (null == emptyView) {
            emptyView = View.inflate(mContext, com.yilian.mylibrary.R.layout.library_module_nothing, null);
        }
        return emptyView;
    }

    public View getErrorView() {
        View errorView = null;
        if (null == errorView) {
            errorView = View.inflate(mContext, com.yilian.mylibrary.R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNewData();
                }
            });
        }
        return errorView;
    }
}
