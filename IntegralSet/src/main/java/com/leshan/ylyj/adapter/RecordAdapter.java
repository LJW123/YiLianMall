package com.leshan.ylyj.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;

import rxfamily.entity.IntegralRecordEntity;

public class RecordAdapter extends BaseQuickAdapter<IntegralRecordEntity.Record, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;


    public RecordAdapter() {
        super(R.layout.item_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IntegralRecordEntity.Record item) {
        LinearLayout sticky_header_ll = helper.getView(R.id.sticky_header_ll);
        TextView sticky_date_tv = helper.getView(R.id.sticky_date_tv);//月时间
        TextView sticky_total1_tv = helper.getView(R.id.sticky_total1_tv);//总的 1
        TextView sticky_total2_tv = helper.getView(R.id.sticky_total2_tv);//总的 2
        sticky_total2_tv.setVisibility(View.GONE);
        LinearLayout unfold_ll = helper.getView(R.id.unfold_ll);//
        ImageView unfold_iv = helper.getView(R.id.unfold_iv);//

        ImageView img_iv = helper.getView(R.id.img_iv);
        TextView type_name_tv = helper.getView(R.id.type_name_tv);
        TextView time_tv = helper.getView(R.id.time_tv);
        TextView money_tv = helper.getView(R.id.money_tv);


        if (helper.getLayoutPosition() == 0) {
            sticky_header_ll.setVisibility(View.VISIBLE);
            sticky_date_tv.setText(item.getYear() + "年" + item.getMonth() + "月");
            sticky_total1_tv.setText("转入奖券 " + MoneyUtil.getTwoDecimalPlaces(item.getIncome()) + "   转出奖券 " + MoneyUtil.getTwoDecimalPlaces(item.getExpend()));
            sticky_total2_tv.setText("发奖励扣除奖券 " + MoneyUtil.getTwoDecimalPlaces(item.getRedExpend()));
            if (item.getUnfold() != null && item.getUnfold()) {
                sticky_total2_tv.setVisibility(View.VISIBLE);
                unfold_iv.setImageResource(R.mipmap.arrows_up_gray);
            } else {
                sticky_total2_tv.setVisibility(View.GONE);
                unfold_iv.setImageResource(R.mipmap.arrows_down_gray);
            }
            helper.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(item.getDate(), this.getItem(helper.getLayoutPosition() - 1).getDate())) {
                sticky_header_ll.setVisibility(View.VISIBLE);
                sticky_date_tv.setText(item.getYear() + "年" + item.getMonth() + "月");
                sticky_total1_tv.setText("转入奖券 " + MoneyUtil.getTwoDecimalPlaces(item.getIncome()) + "   转出奖券 " + MoneyUtil.getTwoDecimalPlaces(item.getExpend()));
                sticky_total2_tv.setText("发奖励扣除奖券 " + MoneyUtil.getTwoDecimalPlaces(item.getRedExpend()));
                if (item.getUnfold() != null && item.getUnfold()) {
                    sticky_total2_tv.setVisibility(View.VISIBLE);
                    unfold_iv.setImageResource(R.mipmap.arrows_up_gray);
                } else {
                    sticky_total2_tv.setVisibility(View.GONE);
                    unfold_iv.setImageResource(R.mipmap.arrows_down_gray);
                }
                helper.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                sticky_header_ll.setVisibility(View.GONE);
                helper.itemView.setTag(NONE_STICKY_VIEW);
            }
        }
        //小于 100 是"+"  大于100 是"-"
        if (Double.valueOf(item.getType()) > 100) {
            money_tv.setText("-" + MoneyUtil.getTwoDecimalPlaces(item.getAmount()));
        } else {
            money_tv.setText("+" + MoneyUtil.getTwoDecimalPlaces(item.getAmount()));
        }
        GlideUtil.showImageWithSuffix(mContext, item.getImg(), img_iv);
        type_name_tv.setText(item.getTypeMsg());
        time_tv.setText(DateUtils.timeStampToStr4(Long.parseLong(item.getTime())));

        helper.itemView.setContentDescription(new Gson().toJson(item));

        unfold_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getUnfold() == null) {
                    unfold(item.getDate(), true);
                } else {
                    unfold(item.getDate(), !item.getUnfold());
                }
            }
        });
    }

    public void unfold(String date, Boolean unfold) {
        for (IntegralRecordEntity.Record record : this.getData()) {
            if (record.getDate().equals(date)) {
                record.setUnfold(unfold);
            }
        }
        notifyDataSetChanged();
    }
}
