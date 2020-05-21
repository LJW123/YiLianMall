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
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import rxfamily.entity.IntegralExchangeRecordEntity;
import rxfamily.entity.TransactionRecordEntity;

public class RecordTransactionAdapter extends BaseQuickAdapter<TransactionRecordEntity.ListBean, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;


    public RecordTransactionAdapter() {
        super(R.layout.item_record_transaction);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TransactionRecordEntity.ListBean item) {
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
        String sign = "";
        if (item.getFlag().equals("0"))  // 1奖券变更  0余额变更  余额拼接¥符号
            sign = "¥";

        //小于 100 是"+"  大于100 是"-"
        if (Double.valueOf(item.getType()) > 100) {
            money_tv.setText("-" + sign + MoneyUtil.getTwoDecimalPlaces(item.getAmount()));
        } else {
            money_tv.setText("+" + sign + MoneyUtil.getTwoDecimalPlaces(item.getAmount()));
        }
        GlideUtil.showImageWithSuffix(mContext, item.getImg(), img_iv);
        type_name_tv.setText(item.getTypeMsg());
        if (TimeUtils.isToday(Long.valueOf(item.getTime()) * 1000)) {
            time_tv.setText("今日 " + TimeUtils.millis2String(Long.valueOf(item.getTime()) * 1000, new SimpleDateFormat("HH:mm", Locale.getDefault())));
        } else if (TimeUtils.isToday(Long.valueOf(item.getTime()) * 1000 + Constants.ONE_DAY)) {
            time_tv.setText("昨日 " + TimeUtils.millis2String(Long.valueOf(item.getTime()) * 1000, new SimpleDateFormat("HH:mm", Locale.getDefault())));
        } else {
            time_tv.setText(DateUtils.timeStampToMDHM(Long.parseLong(item.getTime())));
        }


        helper.itemView.setContentDescription(new Gson().toJson(item));
    }
}
