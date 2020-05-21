package com.yilian.mall.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yilian.mall.R;
import com.yilian.mall.adapter.MTPackageCommentListAdapter;
import com.yilian.mall.entity.MTPackageCommentListEntity;
import com.yilian.mall.http.MTNetRequest;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 套餐评论 晒图评论
 */
public class MTPackageCommentListHaveImgFragment extends BaseFragment {

    private PullToRefreshListView ptrlvMtPackageCommentList;
    private View rootView;
    private MTNetRequest mtNetRequest;
    private String packageId;
    private String merchantId;
    private int page = 0;
    private ArrayList<MTPackageCommentListEntity.DataBean> commentList = new ArrayList<>();
    private MTPackageCommentListAdapter adapter;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mtpackage_comment_list_all, container, false);
        }
        getIntentData();
        initView(rootView);
        initData();
        initListener();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    private void getIntentData() {

        Intent intent = getActivity().getIntent();
        packageId = intent.getStringExtra("packageId");
        merchantId = intent.getStringExtra("merchantId");

    }

    private void initListener() {
        ptrlvMtPackageCommentList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getData();
            }
        });

    }

    private void initData() {
        getData();
    }

    /**
     * 获取评论
     */
    private void getData() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.getMTPackageCommentAllList(packageId, merchantId, "1", page, new RequestCallBack<MTPackageCommentListEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTPackageCommentListEntity> responseInfo) {
                MTPackageCommentListEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        ArrayList<MTPackageCommentListEntity.DataBean> data = result.data;//评论列表内容
                        if (page == 0) {
                            commentList.clear();
                        }else{
                            showToast("加载完毕");
                        }
                        commentList.addAll(data);
                        adapter.notifyDataSetChanged();
                        break;
                    case 0:
                        break;
                    case -17:
                        break;
                    case -4:
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -23:
                        break;
                    default:
                        showToast("" + result.code);
                        break;

                }
                stopMyDialog();
                ptrlvMtPackageCommentList.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                ptrlvMtPackageCommentList.onRefreshComplete();
            }
        });
    }


    private void initView(View rootView) {
        ptrlvMtPackageCommentList = (PullToRefreshListView) rootView.findViewById(R.id.ptrlv_mt_package_comment_list);
        ptrlvMtPackageCommentList.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new MTPackageCommentListAdapter(mContext, commentList, imageLoader, options);
        ptrlvMtPackageCommentList.setAdapter(adapter);

    }



}
