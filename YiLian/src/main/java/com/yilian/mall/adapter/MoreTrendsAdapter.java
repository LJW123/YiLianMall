package com.yilian.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.StringFormat;
import com.yilian.networkingmodule.entity.StealMoreTrends;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZYH on 2017/12/5 0005.
 */

public class MoreTrendsAdapter extends BaseQuickAdapter<StealMoreTrends.Data, BaseViewHolder> {
    private boolean isHasHeader = false;

    public MoreTrendsAdapter(int layoutResId, boolean isHasHeader) {
        super(layoutResId);
        this.isHasHeader = isHasHeader;
    }

    @Override
    protected void convert(BaseViewHolder helper, StealMoreTrends.Data item) {
        if (null != item) {
            helper.setText(R.id.tv_content, item.merchantName);
            helper.setText(R.id.tv_time, item.applyAt);
            String today= DateUtils.getTodayDate();
            String time=DateUtils.timeStampToStr(Long.parseLong(item.applyAt));
            if (time.contains(today)){
                helper.setText(R.id.tv_time,timeStampToStr4(Long.parseLong(item.applyAt)));
            }else {
                helper.setText(R.id.tv_time, DateUtils.formatDate(Long.parseLong(item.applyAt)));
            }
        }
    }
    public static String timeStampToStr4(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }
}
