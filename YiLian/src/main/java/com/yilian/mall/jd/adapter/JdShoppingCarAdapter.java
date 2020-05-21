package com.yilian.mall.jd.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东购物车列表适配器
 *
 * @author Created by zhaiyaohua on 2018/6/27.
 */

public class JdShoppingCarAdapter extends BaseQuickAdapter<JdShoppingCarEntity.ListBean, BaseViewHolder> {
    /**
     * 记录选中的goodsSku
     */
    private List<String> skuList = new ArrayList<>();

    public JdShoppingCarAdapter() {
        super(R.layout.item_jd_shopping_car);
    }

    @Override
    protected void convert(BaseViewHolder helper, JdShoppingCarEntity.ListBean item) {
        if (null != item.goodsInfo) {
            helper.setText(R.id.jd_goods_des, item.goodsInfo.name);
            helper.setText(R.id.tv_goods_price, MoneyUtil.add¥Front(item.goodsInfo.jdPrice));
            GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(item.goodsInfo.imagePath), helper.getView(R.id.iv_goods));
        }
        helper.setText(R.id.tv_shopping_num, item.goodsCount);
        CheckBox checkBox = helper.getView(R.id.chk_shopping_cart_goods_list);
        checkBox.setChecked(item.isChecked);
        helper.addOnClickListener(R.id.chk_shopping_cart_goods_list);
        helper.addOnClickListener(R.id.ll_shopping_num);
    }

    public boolean getAllCheckedStatus() {
        for (JdShoppingCarEntity.ListBean bean : mData) {
            if (!bean.isChecked) {
                return bean.isChecked;
            }
        }
        return true;
    }

    public void updateAllCheckBox(boolean isAllChecked) {
        for (JdShoppingCarEntity.ListBean bean : mData) {
            bean.setChecked(isAllChecked);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取被选中的商品集合
     *
     * @return
     */
    public List<JdShoppingCarEntity.ListBean> getCheckedList() {
        List<JdShoppingCarEntity.ListBean> listBeans = new ArrayList<>();
        for (JdShoppingCarEntity.ListBean bean : mData) {
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
    public void updateSkuList(List<JdShoppingCarEntity.ListBean> beanList) {
        skuList.clear();
        for (JdShoppingCarEntity.ListBean mDatum : beanList) {
            if (mDatum.isChecked) {
                skuList.add(mDatum.goodsSku);
            }
        }
    }

    /**
     * 更新列表
     *
     * @param beanList
     */
    public void updateNewData(List<JdShoppingCarEntity.ListBean> beanList) {
        for (int i = 0; i < beanList.size(); i++) {
            if (skuList.contains(beanList.get(i).goodsSku)) {
                beanList.get(i).isChecked = true;
            } else {
                beanList.get(i).isChecked = false;
            }
        }
        setNewData(beanList);
    }
}
