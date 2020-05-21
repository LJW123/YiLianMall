package com.yilian.mall.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.FavoriteEntity;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/8/20 0020.
 */

public class FavoriteStoreRecycleAdapter extends BaseQuickAdapter<FavoriteEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {
    public FavoriteStoreRecycleAdapter(int item_favorite_store2, ArrayList<FavoriteEntity.ListBean> storeList) {
        super(item_favorite_store2, storeList);
    }

    @Override
    protected void convert(BaseViewHolder helper, FavoriteEntity.ListBean item) {
        helper.addOnClickListener(R.id.un_select);
        helper.addOnClickListener(R.id.cb_select);
        helper.addOnClickListener(R.id.iv_icon);
        CheckBox cbCheck= helper.getView(R.id.cb_select);
        helper.setText(R.id.tv_name,item.collectName);

        if (item.isShowCheck) {
            helper.getView(R.id.un_select).setVisibility(View.GONE);
            cbCheck.setVisibility(View.VISIBLE);
            if (item.isCheck) {
                cbCheck.setChecked(true);
            }
            else {
                cbCheck.setChecked(false);
            }
        } else {
            helper.getView(R.id.un_select).setVisibility(View.VISIBLE);
            cbCheck.setVisibility(View.GONE);
        }
        ImageView ivIcon=helper.getView(R.id.iv_icon);
        GlideUtil.showImageNoSuffix(mContext,item.collectIcon,ivIcon);
    }
}
