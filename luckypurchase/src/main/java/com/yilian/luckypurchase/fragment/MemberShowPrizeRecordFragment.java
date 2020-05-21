package com.yilian.luckypurchase.fragment;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyUnboxingActivity;
import com.yilian.luckypurchase.adapter.SnatchShowListAdapter;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.LuckyMemberShowPrizeRecordEntity;
import com.yilian.networkingmodule.entity.SnatchShowListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  晒单记录
 */
public class MemberShowPrizeRecordFragment extends BaseMyRecordFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private SnatchShowListAdapter snatchAwardAdapter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        snatchAwardAdapter = new SnatchShowListAdapter(R.layout.lucky_record_show_list);
        snatchAwardAdapter.setOnLoadMoreListener(this, recyclerView);
        snatchAwardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnatchShowListEntity.SnatchInfoBean item = (SnatchShowListEntity.SnatchInfoBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, LuckyUnboxingActivity.class);
                intent.putExtra("activity_id", item.commentIndex);
                startActivity(intent);
            }
        });
        return snatchAwardAdapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {

        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMemberLuckyShowPrizeRecord("snatch/snatch_user_record", userId, page, Constants.PAGE_COUNT, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyMemberShowPrizeRecordEntity>() {
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
                    public void onNext(LuckyMemberShowPrizeRecordEntity luckyMemberShowPrizeRecordEntity) {

                        List<SnatchShowListEntity.SnatchInfoBean> list = luckyMemberShowPrizeRecordEntity.list;
                        if (page <= 0) {
                            if (null == list || list.size() <= 0) {
                                snatchAwardAdapter.setEmptyView(R.layout.library_module_no_data);
                                return;
                            }
                            snatchAwardAdapter.setNewData(list);
                            if (list.size() < Constants.PAGE_COUNT) {
                                snatchAwardAdapter.loadMoreEnd();
                            }
                        } else {
                            if (null == list || list.size() <= Constants.PAGE_COUNT) {
                                snatchAwardAdapter.loadMoreEnd();
                            } else {
                                snatchAwardAdapter.loadMoreComplete();
                            }
                            snatchAwardAdapter.addData(list);
                        }
                    }
                });
        addSubscription(subscription);
    }

}
