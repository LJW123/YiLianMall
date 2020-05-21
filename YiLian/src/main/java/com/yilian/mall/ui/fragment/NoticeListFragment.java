package com.yilian.mall.ui.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.NoticeNewListAdapter;
import com.yilian.mall.entity.NoticeModel;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 * 益联快报
 */

public class NoticeListFragment extends InformationFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private NoticeNewListAdapter noticeAdapter;

    @Override
    public void getNewData() {
        if (null==noticeAdapter){
            noticeAdapter = new NoticeNewListAdapter(R.layout.item_infomation);
            recyclerView.setAdapter(noticeAdapter);
            onListener();
        }

        RetrofitUtils.getInstance(mContext).getNoticeList(String.valueOf(page), "30", new Callback<NoticeModel>() {
            @Override
            public void onResponse(Call<NoticeModel> call, Response<NoticeModel> response) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                    if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                        switch (response.body().code) {
                            case 1:
                                ArrayList<NoticeModel.NewsBean> news = response.body().news;
                                initList(news);
                                break;
                        }
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<NoticeModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                noticeAdapter.setEmptyView(getErrorView());
            }
        });
    }

    private void onListener() {
        noticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //更改通用跳转
                NoticeModel.NewsBean  item= (NoticeModel.NewsBean) adapter.getItem(position);
                if (null!=item)
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(item.type,item.content);
            }
        });
        noticeAdapter.setOnLoadMoreListener(this,recyclerView);
    }

    private void initList(ArrayList<NoticeModel.NewsBean> news) {
        if (null != news && news.size() > 0) {
            if (page > 0) {
                noticeAdapter.addData(news);
            } else {
                noticeAdapter.setNewData(news);
            }
            if (news.size()< Constants.PAGE_COUNT){
                noticeAdapter.loadMoreEnd();
            }else {
                noticeAdapter.loadMoreComplete();
            }
        } else {
            if (page==0){
                noticeAdapter.setEmptyView(getEmptyView());
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getNewData();
    }
}
