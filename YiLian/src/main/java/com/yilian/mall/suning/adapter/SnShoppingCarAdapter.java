package com.yilian.mall.suning.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 苏宁购物车列表适配器
 *
 * @author Created by Zg on 2018/7/20.
 */

public class SnShoppingCarAdapter extends BaseQuickAdapter<SnShoppingCarEntity.ListBean, BaseViewHolder> {
    /**
     * 记录选中的goodsSku
     */
    private List<String> skuList = new ArrayList<>();

    public SnShoppingCarAdapter() {
        super(R.layout.item_sn_shopping_car);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnShoppingCarEntity.ListBean item) {
        if (null != item.goodsInfo) {
            if (helper.getLayoutPosition() == 0) {
                helper.getView(R.id.view_header).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.view_header).setVisibility(View.GONE);
            }

            TextView tv_name = helper.getView(R.id.tv_name);
            tv_name.setText(item.goodsInfo.getName());
            TextView tv_goods_price = helper.getView(R.id.tv_price);
            tv_goods_price.setText(MoneyUtil.setMoney(item.goodsInfo.getSnPrice()));
            GlideUtil.showImage(mContext, item.goodsInfo.getImage(), helper.getView(R.id.iv_goods));
            helper.setText(R.id.tv_shopping_num, item.num);

            helper.addOnClickListener(R.id.chk_shopping_cart_goods_list);
            helper.addOnClickListener(R.id.num_sub);
            helper.addOnClickListener(R.id.tv_shopping_num);
            helper.addOnClickListener(R.id.num_add);

            CheckBox checkBox = helper.getView(R.id.chk_shopping_cart_goods_list);
            //上下架状态 1在售 0下架
            if (item.goodsInfo.getState() == 1) {
                tv_name.setTextColor(mContext.getResources().getColor(R.color.main_black_text));
                tv_goods_price.setTextColor(mContext.getResources().getColor(R.color.color_main_suning));
                helper.getView(R.id.ll_sold_out).setVisibility(View.GONE);
                helper.setVisible(R.id.ll_shopping_num, true);
            } else {
                tv_name.setTextColor(mContext.getResources().getColor(R.color.notice_text_color));
                tv_goods_price.setTextColor(mContext.getResources().getColor(R.color.default_text_color));
                helper.getView(R.id.ll_sold_out).setVisibility(View.VISIBLE);
                helper.setVisible(R.id.ll_shopping_num, false);
            }
            checkBox.setChecked(item.isChecked);

        }
    }

    public boolean getAllCheckedStatus() {
        for (SnShoppingCarEntity.ListBean bean : mData) {
            if (!bean.isChecked) {
                return bean.isChecked;
            }
        }
        return true;
    }

    public void updateAllCheckBox(boolean isAllChecked, boolean isEditStatus) {
        for (SnShoppingCarEntity.ListBean bean : mData) {
            if (isEditStatus || bean.goodsInfo.getState() == 1) {
                //编辑状态下 或者 非编辑状态下切且为在售
                bean.setChecked(isAllChecked);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 获取被选中的商品集合
     *
     * @return
     */
    public List<SnShoppingCarEntity.ListBean> getCheckedList() {
        List<SnShoppingCarEntity.ListBean> listBeans = new ArrayList<>();
        for (SnShoppingCarEntity.ListBean bean : mData) {
            if (bean.isChecked) {
                listBeans.add(bean);
            }
        }
        return listBeans;
    }

    /**
     * 设置skulist
     *
     * @param beanList
     */
    public void updateSkuList(List<SnShoppingCarEntity.ListBean> beanList) {
        skuList.clear();
        for (SnShoppingCarEntity.ListBean mDatum : beanList) {
            if (mDatum.isChecked) {
                skuList.add(mDatum.skuId);
            }
        }
    }

    /**
     * 更新列表
     *
     * @param beanList
     */
    public void updateNewData(List<SnShoppingCarEntity.ListBean> beanList) {
        for (int i = 0; i < beanList.size(); i++) {
            if (skuList.contains(beanList.get(i).skuId)) {
                beanList.get(i).isChecked = true;
            } else {
                beanList.get(i).isChecked = false;
            }
        }
        setNewData(beanList);
    }
}
