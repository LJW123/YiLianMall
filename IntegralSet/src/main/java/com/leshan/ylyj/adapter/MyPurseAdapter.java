package com.leshan.ylyj.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.GlideUtil;

import rxfamily.entity.MyPurseEntity;

/**
 * @author Created by  on 2018/1/22.
 */

public class MyPurseAdapter extends BaseQuickAdapter<MyPurseEntity.DataBean.InfoBean, BaseViewHolder> {
    public MyPurseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyPurseEntity.DataBean.InfoBean item) {
        helper.setText(R.id.tv_icon, item.name);
        GlideUtil.showImageNoSuffix(mContext, item.image, (ImageView) helper.getView(R.id.iv_icon));
    }
}
