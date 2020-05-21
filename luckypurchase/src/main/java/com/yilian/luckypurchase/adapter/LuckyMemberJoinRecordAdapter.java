package com.yilian.luckypurchase.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.luckypurchase.activity.LuckyNumberRecordActivity;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.LuckyMemberJoinRecordEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author Created by  on 2017/11/15 0015.
 *         会员参与记录
 */

public class LuckyMemberJoinRecordAdapter extends BaseMultiItemQuickAdapter<LuckyMemberJoinRecordEntity.ListBean, BaseViewHolder> {
    public static final int ITEM1 = 1;
    public static final int ITEM2 = 2;
    private final String userId;
    private Spanned spanned;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data   A new list is created out of this one to avoid mutable list
     * @param userId
     */

    public LuckyMemberJoinRecordAdapter(List<LuckyMemberJoinRecordEntity.ListBean> data, String userId) {
        super(data);
        this.userId = userId;
        addItemType(ITEM1, R.layout.lucky_item_record_underway);
        addItemType(ITEM2, R.layout.lucky_item_record_announced);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LuckyMemberJoinRecordEntity.ListBean item) {
        GlideUtil.showImageWithSuffix(mContext, item.goodsUrl, (ImageView) helper.getView(R.id.iv_icon));
        helper.setText(R.id.tv_goods_name, item.snatchName);
        helper.setText(R.id.tv_date, item.snatchIssue);
        RxView.clicks(helper.getView(R.id.tv_look_detail))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(mContext, LuckyNumberRecordActivity.class);
                        intent.putExtra("from", "detail");
                        intent.putExtra("activity_id", item.activityId);
                        intent.putExtra("user_id", userId);
                        mContext.startActivity(intent);
                    }
                });
        switch (helper.getItemViewType()) {
            case ITEM1:
                ((ProgressBar) helper.getView(R.id.progress)).setMax(item.totalCount);
                ((ProgressBar) helper.getView(R.id.progress)).setProgress(item.joinCount);
                helper.setText(R.id.tv_total_count, "总需人次：" + item.totalCount);
                helper.setText(R.id.tv_residue_count, "剩余人次：" + (item.totalCount - item.joinCount));
                helper.setText(R.id.tv_add, "追加");
                RxView.clicks(helper.getView(R.id.tv_add))
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                                intent.putExtra("activity_id", item.activityId);
                                intent.putExtra("type", "0");
                                intent.putExtra("show_window", "1");
                                mContext.startActivity(intent);
                            }
                        });
                break;
            case ITEM2:
                if (item.awardSelf == 1) {
                    helper.getView(R.id.iv_win).setVisibility(View.VISIBLE);
                    helper.getView(R.id.rl_goods_content).setBackgroundColor(Color.parseColor("#FFF6F5"));
                } else {
                    helper.getView(R.id.iv_win).setVisibility(View.GONE);
                    helper.getView(R.id.rl_goods_content).setBackgroundColor(Color.WHITE);
                }

                helper.setText(R.id.tv_name, TextUtils.isEmpty(item.userName) ? "暂无昵称" : item.userName);
                GlideUtil.showCirImage(mContext, item.photo, (ImageView) helper.getView(R.id.iv_user_icon));
                int awardCount = item.awardcount;
//                参与记录和中奖纪录页面该字段的值不一样，参与记录使用的是awardcount 中奖纪录使用的是mycount
                helper.setText(R.id.tv_count, awardCount + "人次");
                helper.setText(R.id.tv_add, "我要参与");
                RxView.clicks(helper.getView(R.id.tv_add))
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                                intent.putExtra("activity_id", item.activityId);
                                intent.putExtra("type", "0");
                                mContext.startActivity(intent);
                            }
                        });
                break;
            default:
                break;
        }
        String ta;
        if (item.isSelf == 1) {
            ta = "我";
        } else {
            ta = "ta";
        }
        spanned = Html.fromHtml(ta + "已参与：<font color= '#fe5062'>" + item.mycount + "</font>人次");
        helper.setText(R.id.tv_involved_count, spanned);

    }
}
