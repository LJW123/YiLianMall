package com.yilian.luckypurchase.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.entity.GoodsExpressItemEntity;

/**
 * @authorCreated by LYQ on 2017/10/31.
 */

public class GoodsExpressListAdapter extends BaseQuickAdapter<GoodsExpressItemEntity, BaseViewHolder> {

    public GoodsExpressListAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsExpressItemEntity item) {
        ImageView imageView = helper.getView(R.id.iv_status_icon);
        View topView = helper.getView(R.id.tv_line_top);
        View botView = helper.getView(R.id.tv_line_bottom);
        int layoutPosition = helper.getLayoutPosition();
        Logger.i("ray-" + layoutPosition);
        Logger.i("ray-" + getItemCount());

        if (layoutPosition == 1) {
            imageView.setImageResource(R.mipmap.lucky_icon_prize_green);
            topView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setImageResource(R.mipmap.lucky_icon_prize_gray);
            topView.setVisibility(View.VISIBLE);
        }

        helper.setText(R.id.tv_status, item.goodStatus);
        if (!TextUtils.isEmpty(item.date)) {
            helper.setText(R.id.tv_date, DateUtils.timeStampToStr(Long.parseLong(item.date)));
        }

        if (layoutPosition == getItemCount() - 2) {
            helper.getView(R.id.line).setVisibility(View.GONE);
            botView.setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.line).setVisibility(View.VISIBLE);
            botView.setVisibility(View.VISIBLE);
        }
    }
}
