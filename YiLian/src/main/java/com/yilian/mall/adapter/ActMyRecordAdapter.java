package com.yilian.mall.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ui.ActivityDetailActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ActMyRecordEntity;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActMyRecordAdapter extends BaseQuickAdapter<ActMyRecordEntity.ListBean, BaseViewHolder> {
    String actType;

    public ActMyRecordAdapter(@LayoutRes int layoutResId, String actType) {
        super(layoutResId);
        this.actType = actType;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActMyRecordEntity.ListBean item) {
        helper.addOnClickListener(R.id.tv_right);

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showImageWithSuffix(mContext, item.goodsIcon, iv);

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.goodsName);

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(DateUtils.timeStampToStr(Long.parseLong(item.orderTime)));

        TextView tvRight = helper.getView(R.id.tv_right);
        ImageView ivRight = helper.getView(R.id.iv_right);
        switch (item.orderStatus) {
            case "0":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("确认订单");
                break;
            case "1":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("去交押金");
                break;
            case "2":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("待出库");
                break;
            case "3":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("正在出库");
                break;
            case "4":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("部分发货");
                break;
            case "5":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("待收货");
                break;
            case "6":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("已完成");
                break;
            case "7":
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setImageResource(R.mipmap.act_my_record_right1);
                tvRight.setVisibility(View.GONE);
                break;
            case "8":
                ivRight.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);

                tvRight.setText("待评价");
                break;
            case "9":
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setImageResource(R.mipmap.act_my_record_right2);
                tvRight.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        switch (actType) {
            case "1":
                /**
                 * 碰运气
                 */
                tvRight.setBackgroundResource(R.drawable.act_bg_solid_touch_3);
                break;
            case "2":
                /**
                 * 猜价格
                 */
                tvRight.setBackgroundResource(R.drawable.act_bg_solid_guess_3);
                break;
            default:
                break;
        }

        LinearLayout itemLayout = helper.getView(R.id.item_layout);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(mContext, ActivityDetailActivity.class);
                intent.putExtra("goods_id", item.goodsId);
                intent.putExtra("activity_id", item.actIntegralGoodsId);
                intent.putExtra("is_show", "0");
                intent.putExtra("activity_orderId", "0");
                intent.putExtra("activity_type", actType);
                mContext.startActivity(intent);
            }
        });
    }
}
