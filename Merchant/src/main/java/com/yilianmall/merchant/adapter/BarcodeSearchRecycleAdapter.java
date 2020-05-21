package com.yilianmall.merchant.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.widget.NumberAddSubView;
import com.yilian.networkingmodule.entity.BarCodeSearchEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/8/29 0029.
 */

public class BarcodeSearchRecycleAdapter extends RecyclerView.Adapter<BarcodeSearchRecycleAdapter.ViewHolder> {


    private final ArrayList<BarCodeSearchEntity.DataBean> listDate;
    private final Context mContext;
    public int lastPosition;
    private OnRefreshDataListener listener;


    public BarcodeSearchRecycleAdapter(ArrayList<BarCodeSearchEntity.DataBean> searchList, Context mContext) {
        this.listDate = searchList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.merchant_item_barcode_search, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BarCodeSearchEntity.DataBean dataBean = listDate.get(position);
        holder.tvBarcode.setText(dataBean.goodsCode);
        holder.tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(dataBean.goodsCost)));
        holder.tvSkuInfo.setText(dataBean.goodsNorms);
        holder.subView.setMinValue(1);
        holder.subView.setValue(dataBean.goodsCount);
        holder.subView.setMaxValue(Integer.parseInt(dataBean.skuInventory) - 1);
        holder.tvName.setText(dataBean.goodsName);
        if (Integer.parseInt(dataBean.skuInventory) <= 0) {
            holder.rlContent.setEnabled(false);
            holder.ivSellOut.setVisibility(View.VISIBLE);
            holder.rlContent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.merchant_color_lines));
        } else {
            holder.rlContent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.merchant_color_white));
            holder.ivSellOut.setVisibility(View.GONE);
            holder.rlContent.setEnabled(true);
        }

        if (dataBean.isCheck && Integer.parseInt(dataBean.skuInventory) > 0) {
            holder.ivSelect.setBackgroundResource(R.mipmap.merchant_icon_select);
            holder.subView.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelect.setBackgroundResource(R.mipmap.merchant_icon_unselect);
            holder.subView.setVisibility(View.INVISIBLE);
        }

        holder.subView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                if (value >= Integer.parseInt(listDate.get(position).skuInventory)) {
                    Toast.makeText(mContext, "亲，库存只有这么多了哦~", Toast.LENGTH_SHORT).show();
                }
                upDataListener(dataBean.goodsCode, String.valueOf(holder.subView.getValue()));
                Logger.i("ADD  goodsCount   " + holder.subView.getValue());

            }

            @Override
            public void onButtonSubClick(View view, int value) {
                Logger.i("SUB  goodsCount   " + holder.subView.getValue());
                upDataListener(dataBean.goodsCode, String.valueOf(holder.subView.getValue()));
            }
        });

        holder.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("lastPosition   " + lastPosition + "  position  " + position);
                if (lastPosition == position) {
                    return;
                }
                refreshDataView(position);
                upDataListener(dataBean.goodsCode, String.valueOf(holder.subView.getValue()));
            }
        });
    }

    private void upDataListener(String goodsCode, String goodsCount) {
        if (listener != null) {
            listener.onClick(goodsCode, goodsCount);
        }
    }

    /**
     * 刷新界面
     *
     * @param
     * @param position
     */
    private void refreshDataView(int position) {
        for (int i = 0; i < listDate.size(); i++) {
            if (i == position) {
                listDate.get(i).isCheck = true;
            } else {
                listDate.get(i).isCheck = false;
            }
            listDate.get(i).goodsCount = 1;
        }
        lastPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listDate.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlContent;
        ImageView ivSelect;
        TextView tvName;
        TextView tvSkuInfo;
        NumberAddSubView subView;
        TextView tvPrice;
        TextView tvBarcode;
        ImageView ivSellOut;


        public ViewHolder(View itemView) {
            super(itemView);
            this.rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            this.ivSelect = (ImageView) itemView.findViewById(R.id.cb_select);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name);
            this.tvSkuInfo = (TextView) itemView.findViewById(R.id.tv_sku);
            this.subView = (NumberAddSubView) itemView.findViewById(R.id.number_select);
            this.tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            this.tvBarcode = (TextView) itemView.findViewById(R.id.tv_barcode);
            this.ivSellOut = (ImageView) itemView.findViewById(R.id.iv_sell_out);
        }
    }

    public interface OnRefreshDataListener {
        void onClick(String goodsCode, String goodsCount);
    }

    public void setOnRefreshButtonClick(OnRefreshDataListener listener) {
        this.listener = listener;
    }
}
