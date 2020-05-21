package com.yilian.luckypurchase.adapter;

import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.luckypurchase.widget.NoScrollGridView;
import com.yilian.networkingmodule.entity.LuckyNumLogListEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/26 0026
 * 夺宝记录适配器
 */

public class LuckyRecordAdapter extends BaseQuickAdapter<LuckyNumLogListEntity.SnatchRecord, BaseViewHolder> {

    public LuckyRecordAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LuckyNumLogListEntity.SnatchRecord item) {

        TextView tvIssue = helper.getView(R.id.tv_time);
        tvIssue.setText(DateUtils.luckyTimeWithYear(item.snatch_time));

        TextView tvTime = helper.getView(R.id.tv_count);
        tvTime.setText(item.snatch_man_time + "人次");

        NoScrollGridView gridView = helper.getView(R.id.gridView);
        final String[] img = item.snatch_nums.split(",");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(img));
        LuckyGridViewAdapter adapter = new LuckyGridViewAdapter(mContext, list);
        gridView.setAdapter(adapter);
    }
}
