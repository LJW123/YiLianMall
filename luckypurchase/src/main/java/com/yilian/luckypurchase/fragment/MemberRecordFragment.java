package com.yilian.luckypurchase.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.luckypurchase.adapter.LuckyMemberJoinRecordAdapter;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.LuckyMemberJoinRecordEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  会员幸运购参与记录
 */
public class MemberRecordFragment extends BaseMyRecordFragment {


    private LuckyMemberJoinRecordAdapter luckyMemberJoinRecordAdapter;
    private String userId;
    /**
     * 1 参与记录 2中奖记录
     */
    private int type;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (userId == null) {
            Bundle arguments = getArguments();
            userId = arguments.getString("userId");
            type = arguments.getInt("type");
        }
        ArrayList<LuckyMemberJoinRecordEntity.ListBean> data = new ArrayList<>();
        luckyMemberJoinRecordAdapter = new LuckyMemberJoinRecordAdapter(data, userId);
        luckyMemberJoinRecordAdapter.setOnLoadMoreListener(this, recyclerView);
        luckyMemberJoinRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckyMemberJoinRecordEntity.ListBean item = (LuckyMemberJoinRecordEntity.ListBean) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                intent.putExtra("activity_id", item.activityId);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });
        return luckyMemberJoinRecordAdapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {

        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMemberLuckyJoinRecord("snatch/snatch_user_record", userId, page, Constants.PAGE_COUNT, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyMemberJoinRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(LuckyMemberJoinRecordEntity luckyMemberJoinRecordEntity) {
                        if (luckyMemberJoinRecordEntity != null) {
                            EventBus.getDefault().post(luckyMemberJoinRecordEntity);
                        }
                        List<LuckyMemberJoinRecordEntity.ListBean> list = luckyMemberJoinRecordEntity.list;

                        if (null == list || list.size() <= 0) {
                            if (page == 0) {
                                luckyMemberJoinRecordAdapter.setEmptyView(getEmptyView());
                                return;
                            }
                            luckyMemberJoinRecordAdapter.loadMoreComplete();
                        } else {
                            if (page == 0) {
                                luckyMemberJoinRecordAdapter.setNewData(list);
                            } else {
                                luckyMemberJoinRecordAdapter.addData(list);
                            }
                            if (list.size() < Constants.PAGE_COUNT) {
                                luckyMemberJoinRecordAdapter.loadMoreEnd();
                            } else {
                                luckyMemberJoinRecordAdapter.loadMoreComplete();
                            }
                        }


                    }
                });
        addSubscription(subscription);
    }
}
