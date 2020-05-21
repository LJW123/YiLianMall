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

import rxfamily.entity.IntegralExchangeRecordEntity;
import rxfamily.entity.IntegralRecordEntity;

public class RecordExchangeAdapter extends BaseQuickAdapter<IntegralExchangeRecordEntity.Data, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;


    public RecordExchangeAdapter() {
        super(R.layout.item_record_exchange);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IntegralExchangeRecordEntity.Data item) {
        LinearLayout sticky_header_ll = helper.getView(R.id.sticky_header_ll);
        TextView sticky_date_tv = helper.getView(R.id.sticky_date_tv);//月时间

        ImageView img_iv = helper.getView(R.id.img_iv);
        TextView type_name_tv = helper.getView(R.id.type_name_tv);
        TextView time_tv = helper.getView(R.id.time_tv);
        TextView money_tv = helper.getView(R.id.money_tv);


        if (helper.getLayoutPosition() == 0) {
            sticky_header_ll.setVisibility(View.VISIBLE);
            sticky_date_tv.setText(item.getYear() + "年" + item.getMonth() + "月");
            helper.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(item.getDate(), this.getItem(helper.getLayoutPosition() - 1).getDate())) {
                sticky_header_ll.setVisibility(View.VISIBLE);
                sticky_date_tv.setText(item.getYear() + "年" + item.getMonth() + "月");
                helper.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                sticky_header_ll.setVisibility(View.GONE);
                helper.itemView.setTag(NONE_STICKY_VIEW);
            }
        }
        //小于 100 是"+"  大于100 是"-"
        if (Double.valueOf(item.getType()) > 100) {
            money_tv.setText("- ¥" + MoneyUtil.getTwoDecimalPlaces(item.getAmount()));
        } else {
            money_tv.setText("+ ¥" + MoneyUtil.getTwoDecimalPlaces(item.getAmount()));
        }
        GlideUtil.showImageWithSuffix(mContext, item.getImg(), img_iv);
        type_name_tv.setText(item.getTypeMsg());
        time_tv.setText(DateUtils.timeStampToStr4(Long.parseLong(item.getTime())));

        helper.itemView.setContentDescription(new Gson().toJson(item));
    }
}
