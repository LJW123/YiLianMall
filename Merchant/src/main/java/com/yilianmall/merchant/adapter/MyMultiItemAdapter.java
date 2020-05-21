package com.yilianmall.merchant.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.networkingmodule.entity.ComboDetailEntity;
import com.yilian.networkingmodule.entity.ComboOrderBaseEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.activity.MerchantComboOrderDetailActivity2;

import java.util.List;

/**
 * Created by  on 2017/8/18 0018.
 */

public class MyMultiItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyMultiItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ComboOrderBaseEntity.ITEM1, R.layout.merchant_layout_text_value_padding);
        addItemType(ComboOrderBaseEntity.ITEM1_1, R.layout.merchant_layout_text_value_padding);
        addItemType(ComboOrderBaseEntity.ITEM2, R.layout.merchant_layout_text_value_image_padding);
        addItemType(ComboOrderBaseEntity.ITEM3, R.layout.merchant_layout_text_value2_padding);
        addItemType(ComboOrderBaseEntity.ITEM4, R.layout.merchant_layout_text_value_padding_2);
        addItemType(ComboOrderBaseEntity.ITEM5, R.layout.merchant_layout_text_value_padding_2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case ComboOrderBaseEntity.ITEM1:
            case ComboOrderBaseEntity.ITEM4:
                helper.setText(R.id.tv_key, ((MerchantComboOrderDetailActivity2.ComboTitle) item).title);
                helper.setText(R.id.tv_value, ((MerchantComboOrderDetailActivity2.ComboTitle) item).value);
                helper.getView(R.id.tv_value).setVisibility(View.VISIBLE);
                break;
            case ComboOrderBaseEntity.ITEM1_1:
                helper.setText(R.id.tv_key, ((MerchantComboOrderDetailActivity2.ComboTitle) item).title);
                helper.getView(R.id.rl_arrow).setVisibility(View.VISIBLE);
                break;
            case ComboOrderBaseEntity.ITEM2:
                ComboDetailEntity.CodesBean codesBean = (ComboDetailEntity.CodesBean) item;
                helper.setText(R.id.tv_key, "券码");
                helper.setText(R.id.tv_value2, codesBean.codeX);
                ImageView ivRight = helper.getView(R.id.iv_right);
                switch (codesBean.status) {
                    case 1:
                        ivRight.setVisibility(View.GONE);
                        break;
                    case 2:
                        ivRight.setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.iv_right, R.mipmap.library_module_ic_combo_used);
                        break;
                    default:
                        ivRight.setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.iv_right, R.mipmap.library_module_ic_combo_cancel);
                        break;
                }
                break;
            case ComboOrderBaseEntity.ITEM3:
                ComboDetailEntity.PackageInfoBean.ContentBean packageInfoBean = (ComboDetailEntity.PackageInfoBean.ContentBean) item;
                helper.setText(R.id.tv_key, packageInfoBean.name);
                helper.setText(R.id.tv_value, packageInfoBean.number + "份");
                helper.setText(R.id.tv_value2, String.valueOf(MoneyUtil.setNoSmall¥Money(packageInfoBean.cost)));
                break;
            case ComboOrderBaseEntity.ITEM5:
                helper.setText(R.id.tv_key, ((MerchantComboOrderDetailActivity2.ComboTitle) item).title);
                helper.getView(R.id.rl_arrow).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_right).setVisibility(View.VISIBLE);
                int px = DPXUnitUtil.dp2px(mContext, 10);
                int px2 = DPXUnitUtil.dp2px(mContext, 15);
                helper.getView(R.id.linearLayout).setPadding(px2, px, px2, px);
                break;
        }
    }
}
