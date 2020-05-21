package com.leshan.ylyj.adapter;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.WelfareLoveOperationEntity;

/**
 * 如何获取公益爱心经验值
 * Created by @author zhaiyaohua on 2018/1/18 0018.
 */

public class WelfareGetOperationAdapter extends BaseQuickAdapter<WelfareLoveOperationEntity.Data,BaseViewHolder> {

    public WelfareGetOperationAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WelfareLoveOperationEntity.Data item) {
        helper.setText(R.id.tv_integral_ranking,item.valueString);
        helper.setText(R.id.tv_integral,item.valueDouble);
    }
}
