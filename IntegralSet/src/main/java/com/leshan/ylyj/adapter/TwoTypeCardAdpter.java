package com.leshan.ylyj.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

import rxfamily.entity.TwoTypeEntity;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class TwoTypeCardAdpter extends BaseQuickAdapter<TwoTypeEntity.DataBean,BaseViewHolder> {

    private Context mContext;

    public TwoTypeCardAdpter(Context mContext,List data) {
        super(R.layout.item_card_layout,data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, TwoTypeEntity.DataBean item) {

        helper.setText(R.id.bank_name_tv,item.getBankName());
        ImageView imageView = helper.getView(R.id.bank_icon_iv);
        GlideUtil.showImageWithSuffix(mContext,item.getBankLogo(),imageView);
    }
}
