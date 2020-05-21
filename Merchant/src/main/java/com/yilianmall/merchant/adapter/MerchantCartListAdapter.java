package com.yilianmall.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.MerchantCartListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantShopActivity;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.widget.NumberAddSubView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/8/29 0029.
 */

public class MerchantCartListAdapter extends RecyclerView.Adapter<MerchantCartListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<MerchantCartListEntity.DataBean> list;
    private MerchantShopActivity activity;
    private RefreshInterface refreshInterface;

    public MerchantCartListAdapter(Context mContext, ArrayList<MerchantCartListEntity.DataBean> list, MerchantShopActivity activity) {
        this.mContext = mContext;
        this.list = list;
        this.activity = activity;
    }

    public void setRefreshInterface(RefreshInterface refreshInterface) {
        this.refreshInterface = refreshInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.merchant_item_merchant_cart_list, parent, false);
        ViewHolder myHolder = new ViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MerchantCartListEntity.DataBean dataBean = list.get(position);
        holder.tvTitle.setText(dataBean.goodsName);
        holder.tvSku.setText(dataBean.goodsNorms);
        holder.tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(dataBean.goodsCostPrice)));
        holder.tvXiaoRed.setText(MoneyUtil.getLeXiangBiNoZero(dataBean.merchantIntegral) + "分");
        holder.tvXiaoYellow.setText(MoneyUtil.getLeXiangBiNoZero(dataBean.userIntegral) + "分");
        holder.numberAddSub.setValue(dataBean.goodsCount);
        holder.numberAddSub.setMinValue(1);
        holder.numberAddSub.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            int lastValue = holder.numberAddSub.getValue();
            @Override
            public void onButtonAddClick(View view, int value) {
                dataBean.goodsCount = value;
                changeCount(dataBean.cartIndex, value);
                refreshInterface.refreshCount();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (lastValue == 1) {
                    return;
                }
                dataBean.goodsCount = value;
                changeCount(dataBean.cartIndex, value);
                lastValue = value;//记录上次减少到的数量，如果是1 那么下次将不再继续减少，而是直接return。
                refreshInterface.refreshCount();
            }
        });
        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delGoods(dataBean.cartIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivClose;
        TextView tvSku;
        NumberAddSubView numberAddSub;
        TextView tvPrice;
        TextView tvXiaoRed;
        TextView tvXiaoYellow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
            numberAddSub = (NumberAddSubView) itemView.findViewById(R.id.number_add_sub);
            tvSku = (TextView) itemView.findViewById(R.id.tv_sku);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvXiaoRed = (TextView) itemView.findViewById(R.id.tv_xiao_red);
            tvXiaoYellow = (TextView) itemView.findViewById(R.id.tv_xiao_yellow);
        }
    }

    /**
     * 修该商品数量
     */
    private void changeCount(String cartId, int count) {
        activity.startMyDialog();
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).modifyMerchantCartList(cartId, String.valueOf(count), new Callback<HttpResultBean>() {
            @Override
            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                HttpResultBean resultBean = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, resultBean)) {
                    if (CommonUtils.serivceReturnCode(mContext, resultBean.code, resultBean.msg)) {
                        switch (resultBean.code) {
                            case 1:
                                activity.showToast("修改商品数量成功");
                                break;
                        }
                    }
                }
                activity.stopMyDialog();
            }

            @Override
            public void onFailure(Call<HttpResultBean> call, Throwable t) {
                activity.showToast("修改商品数量失败");
                activity.stopMyDialog();
            }
        });
    }

    /**
     * 删除购物车商品
     */
    private void delGoods(String cartId) {
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).delCartGoods(cartId, new Callback<HttpResultBean>() {
            @Override
            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                HttpResultBean resultBean = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, resultBean)) {
                    if (CommonUtils.serivceReturnCode(mContext, resultBean.code, resultBean.msg)) {
                        switch (resultBean.code) {
                            case 1:
                                activity.showToast("删除商品成功");
                                refreshInterface.deleteGoods();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<HttpResultBean> call, Throwable t) {
                activity.stopMyDialog();
            }
        });
    }


    public interface RefreshInterface {
        void refreshCount();

        void deleteGoods();
    }
}
