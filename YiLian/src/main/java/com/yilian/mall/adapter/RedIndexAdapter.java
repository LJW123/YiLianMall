package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ScoreExponent;
import com.yilianmall.merchant.utils.NumberFormat;

/**
 * Created by Ray_L_Pain on 2017/11/23 0023.
 */

public class RedIndexAdapter extends BaseQuickAdapter<ScoreExponent, com.chad.library.adapter.base.BaseViewHolder> {

    public RedIndexAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreExponent item) {

        View topView = helper.getView(R.id.view_top);
        ImageView iv = helper.getView(R.id.iv);
        View botView = helper.getView(R.id.view_bot);

        switch (helper.getPosition()) {
            case 0:
                topView.setVisibility(View.INVISIBLE);
                iv.setImageResource(R.drawable.circle_red_index);
                botView.setVisibility(View.VISIBLE);
                break;
            case 6:
                topView.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.circle_red_index_line);
                botView.setVisibility(View.INVISIBLE);
                break;
            default:
                topView.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.circle_red_index_line);
                botView.setVisibility(View.VISIBLE);
                break;
        }

        TextView tvDate = helper.getView(R.id.tv_date);
        tvDate.setText(DateUtils.timeStampToStr3_1(NumberFormat.convertToLong(item.createdAt, 0L)));

        TextView tvIndex = helper.getView(R.id.tv_index);
        tvIndex.setText(item.integralNumber);

    }
}
