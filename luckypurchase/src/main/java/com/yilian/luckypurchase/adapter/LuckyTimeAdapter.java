package com.yilian.luckypurchase.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.networkingmodule.entity.CalculateEntity;

/**
 * @author LYQon 2017/10/31.
 */

public class LuckyTimeAdapter extends BaseQuickAdapter<CalculateEntity.SnatchLogBean,BaseViewHolder> {

    public LuckyTimeAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CalculateEntity.SnatchLogBean item) {
        helper.setText(R.id.tv_date, item.formatTime);

        helper.setText(R.id.tv_number, item.timeNumber);

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.userName)) {
            tvName.setText(R.string.lucky_user_no_name);
        } else {
            tvName.setText(item.userName);
        }
    }

}
