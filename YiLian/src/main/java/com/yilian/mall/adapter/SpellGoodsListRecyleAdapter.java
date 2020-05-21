package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.SpellGroupListEntity;
import com.yilian.mall.ui.SpellGroupDetailsActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

public class SpellGoodsListRecyleAdapter extends RecyclerView.Adapter<SpellGoodsListRecyleAdapter.SpellGroupGoodsListHolder> {

    private final ArrayList<SpellGroupListEntity.DataBean> listBean;
    private final Context mContext;

    public SpellGoodsListRecyleAdapter(ArrayList<SpellGroupListEntity.DataBean> recycleListData, Context mContext) {
        this.listBean = recycleListData;
        this.mContext = mContext;
    }

    @Override
    public SpellGoodsListRecyleAdapter.SpellGroupGoodsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpellGroupGoodsListHolder(View.inflate(mContext, R.layout.item_spell_group_list_content, null));
    }

    @Override
    public void onBindViewHolder(SpellGroupGoodsListHolder holder, int position) {
        SpellGroupListEntity.DataBean listBean = this.listBean.get(position);
        String imagUrl = listBean.goodsIcon;
        if (!TextUtils.isEmpty(imagUrl)) {
            if (imagUrl.contains("http://") || listBean.goodsIcon.contains("https://")) {
                imagUrl = imagUrl + Constants.ImageSuffix;
            } else {
                imagUrl = Constants.ImageUrl + imagUrl + Constants.ImageSuffix;
            }
        }

        holder.tvGoodsName.setText(listBean.goodsName);
        holder.tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(listBean.amount)));
        if (!TextUtils.isEmpty(listBean.goodsPrice)) {
            holder.tvCost.setText(MoneyUtil.setNoSmallMoney(MoneyUtil.getLeXiangBi(listBean.goodsPrice)));
            holder.tvCost.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG|Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.tvHasNumber.setText(listBean.groupCondition+"人团");
        String totalNumber = NumberFormat.convertToTenThousand(listBean.groupCondition, listBean.totalNumber);
        if (Integer.parseInt(totalNumber)>1000){
            holder.tvHasTotalNumber.setVisibility(View.VISIBLE);
            holder.tvHasTotalNumber.setText("已有"+totalNumber+"人参团");
        }else{
            holder.tvHasTotalNumber.setVisibility(View.GONE);
        }
        GlideUtil.showImage(mContext, imagUrl, holder.ivIcon);
        if (TextUtils.isEmpty(listBean.goodsLabel)){
            holder.tvTag.setVisibility(View.GONE);
        }else{
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(listBean.goodsLabel);
        }
        holder.llSpellGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SpellGroupDetailsActivity.class);
                intent.putExtra("index", listBean.index);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    class SpellGroupGoodsListHolder extends RecyclerView.ViewHolder {
        LinearLayout llSpellGroup;
        ImageView ivIcon;
        TextView tvHasTotalNumber;
        TextView tvTag;
        TextView tvGoodsName;
        TextView tvPrice;
        TextView tvCost;
        TextView tvHasNumber;
        TextView tvOpenGroup;

        public SpellGroupGoodsListHolder(View itemView) {
            super(itemView);
            this.llSpellGroup = (LinearLayout) itemView.findViewById(R.id.ll_spell_group_item);
            this.ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            this.tvHasTotalNumber = (TextView) itemView.findViewById(R.id.tv_has_total_number);
            this.tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            this.tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            this.tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            this.tvCost = (TextView) itemView.findViewById(R.id.tv_cost);
            this.tvHasNumber = (TextView) itemView.findViewById(R.id.tv_has_number);
            this.tvOpenGroup = (TextView) itemView.findViewById(R.id.tv_open_group);
        }
    }
}