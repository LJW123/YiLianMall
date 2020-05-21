package com.yilian.luckypurchase.fragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;


/**
 * @authorCreated by LYQ on 2017/10/23.
 * 记录的fragment
 */
public abstract class BaseMyRecordFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    public SwipeRefreshLayout refreshLayout;
    public RecyclerView recyclerView;
    public int page, position;
    public String userId;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (view == null) {
            view = inflater.inflate(R.layout.lucky_my_record_fragment, null, false);
        }
        initView(view);
        initListener();
        return view;
    }



    private void initListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });

    }

    private void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(getAdapter());
    }

    protected abstract RecyclerView.Adapter getAdapter();


    private boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirst) {
            isFirst = !isFirst;
            initData();
        }

    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(200);
                    Bundle arguments = getArguments();
                    userId = arguments.getString("userId");
                    Logger.i("userId::   " + userId);
                    //参与记录的标记是进行中 还是已揭晓
                    position = arguments.getInt("position");

                    refreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(true);
                            getFirstPageData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void getFirstPageData() {
        page = 0;
        getData();
    }

    /**
     * javadoc 获取数据
     */
    public abstract void getData();

    public View getErrorView() {
        View errorView = null;
        if (null == errorView) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFirstPageData();
                }
            });
        }
        return errorView;
    }

    public View getEmptyView() {
        View emptyView = null;
        if (null == emptyView) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }
        return emptyView;
    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }
}
