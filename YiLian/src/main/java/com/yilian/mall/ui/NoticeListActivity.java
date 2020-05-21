package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.NoticeListAdapter;
import com.yilian.mall.entity.NoticeModel;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/7/13 0013.
 */

public class NoticeListActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.mrl)
    MySwipeRefreshLayout mrl;
    @ViewInject(R.id.recycleView)
    RecyclerView recyclerView;

    private int page = 0;
    private NoticeListAdapter adapter;
    private ArrayList<NoticeModel.NewsBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("益联快报");

        mrl.setMode(MySwipeRefreshLayout.Mode.BOTH);
        mrl.setColorSchemeColors(Color.RED);
        mrl.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                loadData();
            }
        });
        mrl.setOnPullUpRefreshListener(new MySwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                page++;
                loadData();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoticeListAdapter(mContext, list);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mrl != null) {
                                mrl.setRefreshing(true);
                            }
                            loadData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadData() {
        RetrofitUtils.getInstance(mContext).getNoticeList(String.valueOf(page), "20", new Callback<NoticeModel>() {
            @Override
            public void onResponse(Call<NoticeModel> call, Response<NoticeModel> response) {
                mrl.setPullUpRefreshing(false);
                mrl.setRefreshing(false);
                NoticeModel body = response.body();
                if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body()) && body.news != null) {
                    showToast(R.string.service_exception);
                    return;
                }
                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                    ArrayList<NoticeModel.NewsBean> newsBeanArrayList = body.news;
                    if (newsBeanArrayList == null) {
                        return;
                    }
                    if (page == 0) {
                        list.clear();
                    }
                    list.addAll(newsBeanArrayList);
                    if (newsBeanArrayList.size() <= 0 && list.size() <= 0) {
                        adapter.notifyDataSetChanged();
                    } else if (list.size() > 0 && newsBeanArrayList.size() <= 0) {
                        page--;
                        showToast(R.string.no_more_data);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<NoticeModel> call, Throwable t) {
                mrl.setPullUpRefreshing(false);
                mrl.setRefreshing(false);
                showToast(R.string.net_work_not_available);
            }
        });
    }
}
