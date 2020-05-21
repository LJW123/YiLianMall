package com.yilianmall.merchant.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.widget.NumberAddSubView;
import com.yilian.networkingmodule.entity.BarCodeSearchEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2017/8/29 0029.
 */

public class BarcodeSearchAdapter extends BaseQuickAdapter<BarCodeSearchEntity.DataBean, BaseViewHolder> {


    private NumberAddSubView subView;
    private int goodsCount;

    public BarcodeSearchAdapter(@LayoutRes int layoutId, List<BarCodeSearchEntity.DataBean> searchData) {
        super(layoutId, searchData);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BarCodeSearchEntity.DataBean item) {
        helper.addOnClickListener(R.id.rl_content);

        subView = helper.getView(R.id.number_select);
        if (item.isCheck) {
            helper.getView(R.id.cb_select).setEnabled(true);
            subView.setVisibility(View.VISIBLE);
            helper.getView(R.id.rl_content).setEnabled(true);
        } else {
            helper.getView(R.id.cb_select).setEnabled(false);
            helper.getView(R.id.rl_content).setEnabled(false);
            subView.setVisibility(View.GONE);
        }

        goodsCount = 1;
        subView.setMinValue(goodsCount);
        subView.setValue(goodsCount);
        subView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                if (goodsCount >= Integer.parseInt(item.skuInventory)) {
                    Toast.makeText(mContext, "当前已选择最大库存数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                subView.setValue(goodsCount++);
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (goodsCount <= 1) {
                    return;
                }
                subView.setValue(goodsCount--);
            }
        });


        helper.setText(R.id.tv_name, item.goodsName);
        helper.setText(R.id.tv_sku, item.goodsNorms);
        helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.goodsCost)));
        helper.setText(R.id.tv_barcode, item.goodsCode);

    }
}
