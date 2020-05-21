package com.leshan.ylyj.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import rxfamily.entity.WelfareListEntity;

import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;

/**
 * 公益列表适配器
 * Created by @author zhaiyaohua on 2018/1/11 0011.
 */

public class WelfareListAdapter extends BaseQuickAdapter<WelfareListEntity.Data.Details, BaseViewHolder> {


    public WelfareListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WelfareListEntity.Data.Details item) {
        ImageView welfareImage = (ImageView) helper.getView(R.id.img_url);
        GlideUtil.showImageRadius(mContext, item.img1, welfareImage, 6);
        helper.setText(R.id.tv_welfare_theme, item.projectName);
    }
}
