package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ExpressInfoEntity;

/**
 * Created by Ray_L_Pain on 2017/12/7 0007.
 */

public class LookExpressAdapter extends BaseQuickAdapter<ExpressInfoEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {

    public LookExpressAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, ExpressInfoEntity.ListBean item) {

        ImageView iv = helper.getView(R.id.iv);

        TextView tvMessage = helper.getView(R.id.tv_message);
        tvMessage.setText(item.status);

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(item.time);

        Logger.i("2017年12月7日 11:39:33-" + helper.getAdapterPosition());
        if (helper.getAdapterPosition() == 1) {
            iv.setImageResource(R.mipmap.icon_look_check);
            tvMessage.setTextColor(mContext.getResources().getColor(R.color.color_new_red));
            tvTime.setTextColor(mContext.getResources().getColor(R.color.color_new_red));
        } else {
            iv.setImageResource(R.mipmap.icon_look_normal);
            tvMessage.setTextColor(mContext.getResources().getColor(R.color.color_999));
            tvTime.setTextColor(mContext.getResources().getColor(R.color.color_999));
        }

    }

}
